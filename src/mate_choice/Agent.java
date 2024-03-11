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