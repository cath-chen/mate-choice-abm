package mate_choice;

import java.util.Random;

import ec.util.MersenneTwisterFast;
import sim.engine.Schedule;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Bag;
import mate_choice.Environment;

public class Agent implements Steppable {
	int x;
	int y;
	int xdir;
	int ydir;
	double a_rate; // attractive rate
	double s_rate; // similarity rate
	double preference_threshold;
	Sexuality sexuality;
	
	
	public Agent(boolean attractive_rate, double rate, Sexuality sexuality, double preference_threshold, int x, int y, int xdir, int ydir) {
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
	}
	
	public void move(Environment state) {
		if(state.random.nextBoolean(state.getP())) {
			xdir = (state.random.nextInt(3)-1) * state.getMovementSize();
			ydir = (state.random.nextInt(3)-1) * state.getMovementSize();
		}
		placeAgent(state);
	}
	
	
	
	public int decideX(Environment state, Bag neighbors) {
		int posX = 0, negX = 0;
		for(int i = 0; i < neighbors.numObjs; i++) {
			Agent a = (Agent)neighbors.objs[i];
			if(a.x > this.x) {
				posX++;
			}
			else if(a.x<this.x) {
				negX++;
			}
		} //end for
		if(posX > negX) {
			return 1;
		} else if(negX > posX) {
			return -1;
		}
		return state.random.nextInt(3)-1;
	}
	
    public int decideY(Environment state, Bag neighbors) {
    	int posY = 0, negY = 0;
    	for(int i=0;i<neighbors.numObjs;i++) {
    		Agent a = (Agent)neighbors.objs[i];
    		if(a.y > this.y) {
    			posY++;
    		}
    		else if(a.y < this.y) {
    			negY++;
    		}
    	}//end for
    	if(posY > negY) {
    		return 1;
    	} else if(posY < negY) {
    		return -1;
    	}
    	return state.random.nextInt(3)-1;
	}
    
    public int decideDiry(Environment state, Bag neighbors) {
    	int A = 0;
    	int B = 0;
    	int C = 0;
    	int dy;
    	for(int i=0;i<neighbors.numObjs;i++) {
    		Agent a = (Agent)neighbors.objs[i];
    		dy = a.ydir;
    		if(dy == -1) {
    			A++;
    		} else if(dy == 0) {
    			B++;
    		} else {
    			C++;
    		}
    	}
		if(A>B && A>C) {
			return -1;
		} else if(B>A && B>C) {
			return 0;
		} else if(C>A && C>B) {
			return 1;
		} else if(A==B && B==C) {
			return state.random.nextInt(3)-1;
		} else if(A==B) {
			if(A>C) {
				return state.random.nextInt(2)-1;
			} else {
				return 1;
			}
		} else if(A==C) {
			if(A>B) {
				if(state.random.nextBoolean(0.5) == true) {
					return 1;
				} else {
					return -1;
				}
			} else {
				return 0;
			}
		} else if(B==C) {
			if(B>A) {
				return state.random.nextInt(2);
			} else {
				return -1;
			}
		} else {
			return state.random.nextInt(3)-1;
		}
    }
    	
    public int decideDirx(Environment state, Bag neighbors) {
    	int A = 0;
    	int B = 0;
    	int C = 0;
    	int dx;
    	for(int i=0;i<neighbors.numObjs;i++) {
    		Agent a = (Agent)neighbors.objs[i];
    		dx = a.xdir;
    		if(dx == -1) {
    			A++;
    		} else if(dx == 0) {
    			B++;
    		} else {
    			C++;
    		}
    	}
		if(A>B && A>C) {
			return -1;
		} else if(B>A && B>C) {
			return 0;
		} else if(C>A && C>B) {
			return 1;
		} else if(A==B && B==C) {
			return state.random.nextInt(3)-1;
		} else if(A==B) {
			if(A>C) {
				return state.random.nextInt(2)-1;
			} else {
				return 1;
			}
		} else if(A==C) {
			if(A>B) {
				if(state.random.nextBoolean(0.5) == true) {
					return 1;
				} else {
					return -1;
				}
			} else {
				return 0;
			}
		} else if(B==C) {
			if(B>A) {
				return state.random.nextInt(2);
			} else {
				return -1;
			}
		} else {
			return state.random.nextInt(3)-1;
    	}
    }
	
	
	public void placeAgent(Environment state) {
		x = state.sparseSpace.stx(x + xdir);
		y = state.sparseSpace.stx(y + ydir);
		state.sparseSpace.setObjectLocation(this, x, y);
	}
	
	public void mateDecision(Environment state, Bag neighbors) {
		Agent a = (Agent)neighbors.objs[0];
		
		// if the curr agent is male
		if (this.sexuality == Sexuality.GAY || this.sexuality == Sexuality.BI_M || this.sexuality == Sexuality.STRAIGHT_M) {
			// agent a into males
			if (!(a.sexuality == Sexuality.BI_F || a.sexuality == Sexuality.BI_M || a.sexuality == Sexuality.STRAIGHT_F || a.sexuality == Sexuality.GAY)) {
				move(state);
				return;
			}
		} 
		// if the curr agent is female
		else {
			// if agent a is into females
			if (!(a.sexuality == Sexuality.BI_F || a.sexuality == Sexuality.BI_M || a.sexuality == Sexuality.STRAIGHT_M || a.sexuality == Sexuality.LESBIAN)) {
				move(state);
				return;
			}
		}
		
		double preference;
		// if toggled on --> attractive
		if (state.getAttract_similar()) {
			preference = this.a_rate - 0.2;
			if (a.a_rate > preference) {
				//TODO: partner and leave mating pool
			} else {
				// lower threshold
				preference -= 0.1;
				move(state);
				return;
			}
		} else {
			preference = (state.random.nextInt(3) + 2)/10;
			double s_range = Math.abs(this.s_rate - a.s_rate);
			if (s_range < preference) {
				//TODO: partner and leave mating pool
			} else {
				// increase threshold
				preference += 0.1;
				move(state);
				return;
			}
		}
	}
	
	public void remove(Environment state) {
		state.sparseSpace.remove(this);
		state.setMateCount(state.mate_count + 1);	// increment number of pairs removed from env 
//		event.stop();
	}

	@Override
	public void step(SimState state) {
		Environment estate = (Environment)state;
		move(estate);
	}
	
	public void colorBySexuality(Sexuality sexuality, Environment state, Agent a) {
		switch (sexuality) {
		case LESBIAN:
			state.gui.setOvalPortrayal2DColor(a, (float)0, (float)0, (float)1, (float)1);
			break;
		case GAY:
			state.gui.setOvalPortrayal2DColor(a, (float)1, (float)0, (float)0, (float)1);
			break;
		case STRAIGHT_M:
			state.gui.setOvalPortrayal2DColor(a, (float)0.5, (float)0.5, (float)1, (float)1);
			break;
		case STRAIGHT_F:
			state.gui.setOvalPortrayal2DColor(a, (float)1, (float)0, (float)1, (float)1);
			break;
		case BI_M:
			state.gui.setOvalPortrayal2DColor(a, (float)1, (float)1, (float)0, (float)1);
			break;
		case BI_F:
			state.gui.setOvalPortrayal2DColor(a, (float)1, (float)1, (float)1, (float)1);
			break;
		default:
			state.gui.setOvalPortrayal2DColor(a, (float)1, (float)0, (float)0, (float)1);
		}
	}

}