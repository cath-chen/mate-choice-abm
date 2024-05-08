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
	boolean date = false;
	
	
	public Agent(int id, double rate, Sexuality sexuality, double preference_threshold, int x, int y, int xdir, int ydir, int[] neighborhood) {
		super();
		
		// just using attractiveness
		this.a_rate = rate;
		this.sexuality = sexuality;
		this.preference_threshold = preference_threshold;
		this.x = x;
		this.y = y;
		this.xdir = xdir;
		this.ydir = ydir;
		this.hood = neighborhood;
		this.move_count = 0;
		this.id = id;
	}
	
	
	public Agent(int id, double rate, Sexuality sexuality, double preference_threshold, int x, int y, int xdir, int ydir) {
		super();
		this.a_rate = rate;
		this.sexuality = sexuality;
		this.preference_threshold = preference_threshold;
		this.x = x;
		this.y = y;
		this.xdir = xdir;
		this.ydir = ydir;
		this.move_count = 0;
		this.id = id;
	}
	
	public void move(Environment state) {
		if(state.random.nextBoolean(state.getP())) {
			xdir = (state.random.nextInt(3)-1) * state.getMovementSize();
			ydir = (state.random.nextInt(3)-1) * state.getMovementSize();
		}
		placeAgent(state);
		move_count++;
		if (move_count == state.updateFam ) {
			move_count = 0;
			checkInteractions(state);
		}
	}
	
	
	public void placeAgent(Environment state) {
		x = state.sparseSpace.stx(x + xdir);
		y = state.sparseSpace.sty(y + ydir);
		state.sparseSpace.setObjectLocation(this, x, y);
        hood = state.createNeighborhood(x, y);
	}
	
	public void areNeighbors(Environment state) {
		Bag neighbors;
		neighbors = state.sparseSpace.getMooreNeighbors(x, y, state.getSearchRadius(), state.sparseSpace.BOUNDED, false);
		if (neighbors != null && !neighbors.isEmpty()) {
			Agent a = (Agent) neighbors.objs[0];
			this.date = mateDecision(state, a);
			a.date = a.mateDecision(state, this);
			
			if(this.date && a.date) {
				this.remove_agents(state, a);
			} else if(!a.date && this.date) {
				if(state.attract_similar) {
					this.preference_threshold -= .001;
				} else {
					this.preference_threshold += .0005;
				}
			}else if (a.date && !this.date) {
				if(state.attract_similar) {
					a.preference_threshold -= .001;
				} else {
					a.preference_threshold += .0005;
				}
			} else {
				return;
			}
		} else {
			return;
		}
		return;
	}

	
	public boolean mateDecision(Environment state, Agent a) {
			double preference;
		
		if (this.sexuality == Sexuality.BI_F) {
			if (a.sexuality == Sexuality.GAY || a.sexuality == Sexuality.STRAIGHT_F) {
				return false;
			}
		} else if (this.sexuality == Sexuality.BI_M) {
			if (a.sexuality == Sexuality.LESBIAN || a.sexuality == Sexuality.STRAIGHT_M) {
				return false;
			}
		} else if (this.sexuality == Sexuality.GAY) {
			if (a.sexuality == Sexuality.STRAIGHT_M || a.sexuality == Sexuality.STRAIGHT_F || a.sexuality == Sexuality.BI_F || a.sexuality == Sexuality.LESBIAN) {
				return false;
			}
		} else if (this.sexuality == Sexuality.LESBIAN) {
			if (a.sexuality == Sexuality.STRAIGHT_M || a.sexuality == Sexuality.STRAIGHT_F || a.sexuality == Sexuality.BI_M || a.sexuality == Sexuality.GAY) {
				return false;
			}
		} else if (this.sexuality == Sexuality.STRAIGHT_F) {
			if (a.sexuality == Sexuality.BI_F || a.sexuality == Sexuality.GAY || a.sexuality == Sexuality.LESBIAN) {
				return false;
			}
		} else {
			if (a.sexuality == Sexuality.BI_M || a.sexuality == Sexuality.LESBIAN || a.sexuality == Sexuality.GAY) {
				return false;
			}
		}
		
		
		// if toggled on --> attractive
		if (state.isAttract_similar()) {
			preference = preference_threshold;
			if (state.isFamiliar()) {
				// familiarity subtracts 0.5 from preference for each increase in score
				double f_score = state.getInteractionCount(this.id, a.id)/20;
				preference = preference - f_score;
				
			}
			if (a.a_rate > preference) {
				
				// remove_agents(state, a);
				//update familiarity 
				return true;
			} else {
				// lower threshold
				preference_threshold -= 0.001;
				return false;
			}
		// if the sim is testing for similarity
		} else {
			preference = preference_threshold;
			double s_range = Math.abs(this.s_rate - a.s_rate);
			if (state.isFamiliar()) {
				// familiarity adds 0.5 from preference range for each increase in score
				double f_score = state.getInteractionCount(this.id, a.id)/20;
				preference = preference + f_score;
			}
			if (s_range < preference) {
				//TODO: partner and leave mating pool
				//remove_agents(state, a);
				
				return true;
			} else {
				// increase threshold
				preference_threshold += 0.0005;
				return false;
			}
		}
	}
	
	public void remove_agents(Environment state, Agent a) {
		double thisFam = state.getInteractionCount(this.id, a.id); // divide by 20?? -- up to 6
		double thisSim = this.a_rate - a.a_rate;
		state.similarity.add(thisSim);
		state.familiarity.add(thisFam);
		state.attractiveness.add(this.a_rate);
		state.attractiveness.add(a.a_rate);
		
		state.sparseSpace.remove(this);
		state.sparseSpace.remove(a);
		event.stop();
		a.event.stop();
		state.setMateCount(state.mate_count + 1);	// increment number of pairs removed from env 
	}
	
	public void checkInteractions(Environment state) {
        
        // Iterate over all agents again to find those in the same neighborhood
		/*Bag neighbors = state.sparseSpace.getMooreNeighbors(x, y, state.neighborhoodSize, state.sparseSpace.TOROIDAL, false);
        for (int i = 0; i < neighbors.numObjs; i++) {
        	Agent otherAgent = (Agent) neighbors.objs[i];
                     state.recordInteraction(this.id, otherAgent.id);
        }*/
		
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
		if (date) {
			return;
		}
		areNeighbors(estate);
	}
	
	
	
	public void colorBySexuality(Sexuality sexuality, Environment state, Agent a) {
		switch (sexuality) {
		case LESBIAN: // orange
			state.gui.setOvalPortrayal2DColor(a, (float)1, (float)0.5, (float)0, (float)1);
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
		case BI_M: // purple
			state.gui.setOvalPortrayal2DColor(a, (float)0, (float)1, (float)0, (float)1);
			break;
		case BI_F: // pink
			state.gui.setOvalPortrayal2DColor(a, (float)1, (float)0.2, (float)1, (float)1);
			break;
		default:
			state.gui.setOvalPortrayal2DColor(a, (float)1, (float)1, (float)1, (float)1);
		}
	}

}
