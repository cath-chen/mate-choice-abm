package mate_choice;

import ec.util.MersenneTwisterFast;
import mate_choice.Agent;
import sim.util.Bag;
import sim.util.Int2D;
import sim.util.distribution.Beta;
import sim.util.distribution.Normal;
import spaces.Spaces;
import sweep.SimStateSweep;
import mate_choice.F_Matrix;


public class Environment extends SimStateSweep {
	public static int id = 0;
	public int gridWidth = 100;
	public int gridHeight = 100;
	public int neighborhoodSize = 50; ///Essential = # of neighborhoods in the space 
	public int total = 50;
	public int searchRadius = 5; // how many squares should agents search for neighbors
	public boolean attract_similar = true;   // false similar, true is attractive
	public boolean familiar = true;
	public int movementSize = 1;
	public double p = .8; // probability that the agent is active
	public double threshold = 0.1;
	public int mate_count = 0;  // counts number of removed pairs
	public boolean charts = false;  // uses charts when true. false -- parameter sweeps are enabled
	public F_Matrix matrix; // Instance of F_Matrix for recording interactions
	public Bag allAgents; // Store all agents for interaction recording
	public double alpha_a = 2;
	public double alpha_s = 2;
	public double beta_a = 2; 
	public double beta_s = 2;	
	public Bag similarity = new Bag();
	public Bag familiarity = new Bag();
	public Bag attractiveness = new Bag();
	int actual_n;
	int updateFam = 10;


	
	public Environment(long seed) {
		super(seed);
		// TODO Auto-generated constructor stub
		this.matrix = new F_Matrix(total);
	}

	public Environment(long seed, Class observer) {
		super(seed, observer);
		// TODO Auto-generated constructor stub
		this.matrix = new F_Matrix(total);
	}

	public Environment(long seed, Class observer, String runTimeFileName) {
		super(seed, observer, runTimeFileName);
		// TODO Auto-generated constructor stub
	}
	
	public void agentTraits(int num_of_agent, Sexuality sexuality) {
		
		Beta dist_a = new Beta(alpha_a, beta_a, random);
		Beta dist_s = new Beta(alpha_s, beta_s, random);
		
		double attractive_rate = 0.0;
		double similar_rate = 0.0;
		double preference_threshold = 0;
		int num_agents = 0;
		for(int i = 0; i < num_of_agent; i++) {
			int x = random.nextInt(gridWidth);
			int y = random.nextInt(gridHeight);
			int id = this.id++;
			
			// need clarification for preference threshold
			
			if (attract_similar) { 		// if attrative
				attractive_rate = dist_a.nextDouble();
				preference_threshold = 1 - threshold;
			} else {
				similar_rate = dist_s.nextDouble();
				preference_threshold = threshold;
			}
			
			double rate;
			if (attract_similar) {
				rate = attractive_rate;
			} else {
				rate = similar_rate;
			}
		
			
		
			int xdir = random.nextInt(3) - 1;
			int ydir = random.nextInt(3) - 1;
			
			int[] neighborhood = createNeighborhood(x, y);
			
			Agent agent =  new Agent(id, rate, sexuality, preference_threshold, x, y, xdir ,ydir, neighborhood);
			
			
			agent.colorBySexuality(agent.sexuality, this, agent);
			agent.event = schedule.scheduleRepeating(agent);
			sparseSpace.setObjectLocation(agent, x, y);
			
			num_agents ++;
		}
		//System.out.print(num_agents + " " + sexuality + "\n");
	}
	
	public void makeAgents() {
		id = 0;
		int size = gridWidth * gridHeight;
	
		if (total > size) {
			System.out.println("too many agents :(");
			return;
		}
		
		// straight males (blue)
		agentTraits((int)(0.45 * total), Sexuality.STRAIGHT_M);
		// gay males (cyan)
		agentTraits((int)(0.02 * total), Sexuality.GAY);
		// bi males (green)
		agentTraits((int)(0.03 * total), Sexuality.BI_M);
		// straight females (red)
		agentTraits((int)(0.45 * total), Sexuality.STRAIGHT_F);
		// lesbian females (magenta)
		agentTraits((int)(0.02 * total), Sexuality.LESBIAN);
		// bi females (yellow)
		agentTraits((int)(0.03 * total), Sexuality.BI_F);

		
		allAgents = sparseSpace.getAllObjects();
		actual_n = allAgents.numObjs;
	}
	
	public int[] createNeighborhood(int x, int y) {
		
		// if the number isn't divisible
		if (gridWidth % neighborhoodSize != 0) {
			System.out.println("grid size isn't divisible by neibhorhood size\n");
			int[] error = {0};
			return error;
		}

		int neighborhoodWidthSize =  gridWidth / neighborhoodSize;
		int neighborhoodHeightSize =  gridHeight / neighborhoodSize;
		
		
		int x_neighborhood = x / neighborhoodWidthSize;
	    int y_neighborhood = y / neighborhoodHeightSize;

	    int[] neighborhood = {x_neighborhood, y_neighborhood};
	    
		return neighborhood;
	}
	
	
	public void start() {
		super.start();
		mate_count = 0;
		spaces = Spaces.SPARSE;	// set the space
		makeSpace(gridWidth, gridHeight); // make the space
		makeAgents();
		this.matrix = new F_Matrix(actual_n);
		if(observer != null)
			observer.initialize(space, spaces);
	}
	
	
	
	
	// Getters and setters


	public int getGridWidth() {
		return gridWidth;
	}

	public void setGridWidth(int gridWidth) {
		this.gridWidth = gridWidth;
	}

	public int getGridHeight() {
		return gridHeight;
	}

	public void setGridHeight(int gridHeight) {
		this.gridHeight = gridHeight;
	}

	public int getNeighborhoodSize() {
		return neighborhoodSize;
	}

	public void setNeighborhoodSize(int neighborhoodSize) {
		this.neighborhoodSize = neighborhoodSize;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getSearchRadius() {
		return searchRadius;
	}

	public void setSearchRadius(int searchRadius) {
		this.searchRadius = searchRadius;
	}

	public boolean isAttract_similar() {
		return attract_similar;
	}

	public void setAttract_similar(boolean attract_similar) {
		this.attract_similar = attract_similar;
	}

	public boolean isFamiliar() {
		return familiar;
	}

	public void setFamiliar(boolean familiar) {
		this.familiar = familiar;
	}

	public int getMovementSize() {
		return movementSize;
	}

	public void setMovementSize(int movementSize) {
		this.movementSize = movementSize;
	}

	public double getP() {
		return p;
	}

	public void setP(double p) {
		this.p = p;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public boolean isCharts() {
		return charts;
	}

	public void setCharts(boolean charts) {
		this.charts = charts;
	}

	public double getAlpha_a() {
		return alpha_a;
	}

	public void setAlpha_a(double alpha_a) {
		this.alpha_a = alpha_a;
	}

	public double getAlpha_s() {
		return alpha_s;
	}

	public void setAlpha_s(double alpha_s) {
		this.alpha_s = alpha_s;
	}

	public double getBeta_a() {
		return beta_a;
	}

	public void setBeta_a(double beta_a) {
		this.beta_a = beta_a;
	}

	public double getBeta_s() {
		return beta_s;
	}

	public void setBeta_s(double beta_s) {
		this.beta_s = beta_s;
	}

	public int get_mateCount(int mate_count) {
		return mate_count;
	}
	
	public void setMateCount(int mate_count) {
		this.mate_count = mate_count;
	}

	public void recordInteraction(int agent1, int agent2) {
        matrix.recordInteraction(agent1, agent2);
    }
	
	public int getInteractionCount(int agent1, int agent2) {
        return matrix.getInteractionCount(agent1, agent2);
    }
}
