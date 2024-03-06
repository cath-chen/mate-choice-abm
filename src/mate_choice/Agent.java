package mate_choice;

import java.util.Random;

//import ec.util.MersenneTwisterFast;
//import sim.engine.Schedule;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Bag;

public class Agent implements Steppable {
	int x;
	int y;
	int xdir;
	int ydir;
	int attractive_rate;
	double preference_threshold;
	Sexuality sexuality;
	
	
	public Agent(boolean attractive_rate, Sexuality sexuality, double preference_threshold, int x, int y, int xdir, int ydir) {
		super();
		if (attractive_rate) {
			this.attractive_rate = new Random().nextInt(11);	//random int between 1 and 10
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
			xdir = state.random.nextInt(3)-1;
			ydir = state.random.nextInt(3)-1;
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