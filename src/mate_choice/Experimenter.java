package mate_choice;

import observer.Observer;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Bag;
import sweep.ParameterSweeper;
import sweep.SimStateSweep;

public class Experimenter extends Observer implements Steppable {

	// global variable data we want to collect
	public int gay = 0;
	public int lesbian = 0;
	public int straight_m = 0;
	public int straight_f = 0;
	public int bi_f = 0;
	public int bi_m = 0;
	public int mate_count = 0;
	public double avgFam = 0;
	public double avgSim = 0;
	public double avgAtt = 0;
	
	
	public Experimenter(String fileName, String folderName, SimStateSweep state, ParameterSweeper sweeper,
			String precision, String[] headers) {
		super(fileName, folderName, state, sweeper, precision, headers);
		// TODO Auto-generated constructor stub
	}

		
	public void stop(Environment state) {
		Bag agents = state.sparseSpace.getAllObjects();
		if(agents == null || agents.numObjs == 0) {
			event.stop();
		}
	}
		

	public void countSexualities(Environment state) {
		mate_count = state.mate_count;
		Bag agents = state.sparseSpace.getAllObjects();
		for(int i=0;i<agents.numObjs;i++) {
			Agent a =(Agent)agents.objs[i];
			switch(a.sexuality) {
			case GAY:
				gay++;
				break;
			case LESBIAN:
				lesbian++;
				break;
			case STRAIGHT_M:
				straight_m++;
				break;
			case STRAIGHT_F:
				straight_f++;
				break;
			case BI_M:
				bi_m++;
				break;
			case BI_F:
				bi_f++;
				break;
			}
		}
	}

	public boolean reset(SimState state) {
		gay = 0;
		lesbian = 0;
		straight_m = 0;
		straight_f = 0;
		bi_f = 0;
		bi_m = 0;
		mate_count = 0;
		avgAtt = 0;
		return true;
	}
	
	/**
	 * method to switch all agent's date to False 
	 */
	
	
	
	/**
	 * This method collects data for automated simulation sweeps.  Behind the scenes, data are stored in arrays
	 * that allow the calculations of means and standard deviations between simulation runs.
	 * @return
	 */
	public boolean nextInterval() {
		double total = state.sparseSpace.getAllObjects().numObjs;
		data.add(total);
		data.add(mate_count);
		data.add(gay/total);
		data.add(lesbian/total);
		data.add(straight_m/total);
		data.add(straight_f/total);
		data.add(bi_f/total);
		data.add(bi_m/total);
		data.add(avgSim);
		data.add(avgFam);
		data.add(avgAtt);
		return false;
	}

		
	public void strategyDistribution(Environment state) {
		Bag agents = state.sparseSpace.allObjects;//get remaining agents
		if(agents.numObjs > 0) {
			double [] data = new double[agents.numObjs];
			for(int i = 0;i<data.length;i++) {
				Agent a = (Agent)agents.objs[i];
				data[i]=a.sexuality.id();
			}
			if(agents.numObjs > 0)
				this.upDateHistogramChart(0, (int)state.schedule.getSteps(), data, 100);	//give it the data with a 1000 milisecond delay
		}
	}
	
	public void matchChars(Environment state) {
		
		// calculate the avg of the 2 arrays
		double sumSim = 0;
		double sumFam = 0;
		double sumAtt = 0; 
		
		if (state.similarity.numObjs != state.familiarity.numObjs) {
			System.out.println("something is wrong with collecting the familiarity and similarity");
			return;
		}
		
		if(state.similarity.numObjs != 0) {
			
			for (int i = 0; i < state.similarity.numObjs; i++) {
				double sim = (double) state.similarity.objs[i];
				double fam = (double) state.familiarity.objs[i];
				sumSim += sim;
				sumFam += fam;
			}
			
			avgSim = sumSim / state.similarity.numObjs;
			avgFam = sumFam / state.familiarity.numObjs;
		}
		
		if(state.attractiveness.numObjs != 0) {
			for (int j = 0; j < state.attractiveness.numObjs; j++) {
				double att = (double) state.attractiveness.objs[j];
				sumAtt += att;
			}
			avgAtt = sumAtt/ state.attractiveness.numObjs;
		}
	}

	public void stateChange(Environment state) {
		state.attractiveness = new Bag();
		
		Bag allAgents = state.sparseSpace.getAllObjects();
		for(int i = 0; i < allAgents.numObjs; i++) {
			Agent a = (Agent) allAgents.objs[i];
			a.date = false;
		}
	}
	
	public void step(SimState state) {
		super.step(state);
		Environment estate = (Environment)state;

		if(estate.paramSweeps && getdata) {   //for parameter sweeps
			reset(state);
			matchChars(estate);
			countSexualities(estate);   // count sexualities
			nextInterval();   // saves data to a file
			stateChange(estate);
		}
		
		
	}
	
	
}
