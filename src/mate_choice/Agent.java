package mate_choice;

import java.util.Random;

import ec.util.MersenneTwisterFast;
import sim.engine.Schedule;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;
import sim.util.Int2D;
import mate_choice.Environment;

public class Agent implements Steppable {
	int x;
	int y;
	int xdir;
	int ydir;
	int id;//this is the agent's id
	double a_rate; // attractive rate
	double s_rate; // similarity rate
	int[] hood;
	double preference_threshold;
	Sexuality sexuality;
	public Stoppable event;
	int move_count;
	
	
	public Agent(int id, boolean attractive_rate, double rate, Sexuality sexuality, double preference_threshold, int x, int y, int xdir, int ydir, int[] neighborhood) {
		super();
		if (attractive_rate) {
			this.a_rate = rate;
		} else {
			this.s_rate = rate;
		}
		this.sexuality = sexuality;
		this.preference_threshold = preference_threshold;
		this.x = x;
		this.y = y;
		this.xdir = xdir;
		this.ydir = ydir;
		this.hood = neighborhood;
		this.move_count = 0;
	}
	
	public void move(Environment state) {
		if(state.random.nextBoolean(state.getP())) {
			xdir = (state.random.nextInt(3)-1) * state.getMovementSize();
			ydir = (state.random.nextInt(3)-1) * state.getMovementSize();
		}
		placeAgent(state);
		move_count++;
		if (move_count == 9) {
			move_count = 0;
			checkInteractions(state);
		}
	}
	
	
	public void placeAgent(Environment state) {
		x = state.sparseSpace.stx(x + xdir);
		y = state.sparseSpace.stx(y + ydir);
		state.sparseSpace.setObjectLocation(this, x, y);
        hood = state.createNeighborhood(x, y);
	}
	
	public void areNeighbors(Environment state) {
		Bag neighbors;
		neighbors = state.sparseSpace.getMooreNeighbors(x, y, state.getSearchRadius(), state.sparseSpace.BOUNDED, false);
		if (neighbors != null && !neighbors.isEmpty()) {
			mateDecision(state, neighbors);
		} else {
			return;
		}
		return;
	}
	
	
	public void mateDecision(Environment state, Bag neighbors) {
		Agent a = (Agent)neighbors.objs[0];
		double preference;
		
		// if the curr agent is male
		if (this.sexuality == Sexuality.GAY || this.sexuality == Sexuality.BI_M || this.sexuality == Sexuality.STRAIGHT_M) {
			// agent a into males
			if (!(a.sexuality == Sexuality.BI_F || a.sexuality == Sexuality.BI_M || a.sexuality == Sexuality.STRAIGHT_F || a.sexuality == Sexuality.GAY)) {
				return;
			}
		} 
		// if the curr agent is female
		else {
			// if agent a is into females
			if (!(a.sexuality == Sexuality.BI_F || a.sexuality == Sexuality.BI_M || a.sexuality == Sexuality.STRAIGHT_M || a.sexuality == Sexuality.LESBIAN)) {
				return;
			}
		}
		
		
		// if toggled on --> attractive
		if (state.attract_similar) {
			preference = this.a_rate - 0.2;
			if (state.familiar) {
				// familiarity subtracts 0.5 from preference for each increase in score
				double f_score = state.getInteractionCount(this.id, a.id)/20;
				preference = preference - f_score;
				
			}
			if (a.a_rate > preference) {
				//TODO: partner and leave mating pool
				remove_agents(state, a);
				return;
			} else {
				// lower threshold
				preference -= 0.1;
				return;
			}
		} else {
			preference = (state.random.nextInt(3) + 2)/10; 			// TODO update preferences threshold?
			double s_range = Math.abs(this.s_rate - a.s_rate);
			if (state.familiar) {
				// familiarity adds 0.5 from preference range for each increase in score
				double f_score = state.getInteractionCount(this.id, a.id)/20;
				preference = preference + f_score;
			}
			if (s_range < preference) {
				//TODO: partner and leave mating pool
				remove_agents(state, a);
				return;
			} else {
				// increase threshold
				preference_threshold += 0.1;
				return;
			}
		}
	}
	
	public void remove_agents(Environment state, Agent a) {
		state.sparseSpace.remove(this);
		state.sparseSpace.remove(a);
//		System.out.println("removed\n");
		event.stop();
		a.event.stop();
		state.setMateCount(state.mate_count + 1);	// increment number of pairs removed from env 
	}
	
	public void checkInteractions(Environment state) {
        
        // Iterate over all agents again to find those in the same neighborhood
        for (Object obj : state.allAgents) {
            Agent otherAgent = (Agent) obj;
            if (this != otherAgent) {
            	 Int2D otherLocation = state.sparseSpace.getObjectLocation(otherAgent);
                 int[] otherNeighborhood = state.createNeighborhood(otherLocation.getX(), otherLocation.getY());
                 
                 // Check if agents are in the same neighborhood
                 if (hood[0] == otherNeighborhood[0] && hood[1] == otherNeighborhood[1]) {
                     // Record interaction in the matrix
//                 	System.out.println("same neighborhood\n" + state.allAgents.size());
                     state.recordInteraction(this.id, otherAgent.id);
                 }
            }
        }
	}

	@Override
	public void step(SimState state) {
		Environment estate = (Environment)state;
		move(estate);
		areNeighbors(estate);
	}
	
	public void colorBySexuality(Sexuality sexuality, Environment state, Agent a) {
		switch (sexuality) {
		case LESBIAN: // magenta
			state.gui.setOvalPortrayal2DColor(a, (float)1, (float)0, (float)1, (float)1);
			break;
		case GAY: // cyan
			state.gui.setOvalPortrayal2DColor(a, (float)0, (float)1, (float)1, (float)1);
			break;
		case STRAIGHT_M: // blue
			state.gui.setOvalPortrayal2DColor(a, (float)0, (float)0, (float)1, (float)1);
			break;
		case STRAIGHT_F: // red
			state.gui.setOvalPortrayal2DColor(a, (float)1, (float)0, (float)0, (float)1);
			break;
		case BI_M: // green
			state.gui.setOvalPortrayal2DColor(a, (float)0, (float)1, (float)0, (float)1);
			break;
		case BI_F: // yellow
			state.gui.setOvalPortrayal2DColor(a, (float)1, (float)1, (float)0, (float)1);
			break;
		default:
			state.gui.setOvalPortrayal2DColor(a, (float)1, (float)1, (float)1, (float)1);
		}
	}

}
