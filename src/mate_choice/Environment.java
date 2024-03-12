package mate_choice;

import ec.util.MersenneTwisterFast;
import mate_choice.Agent;
import sim.util.Bag;
import sim.util.Int2D;
import sim.util.distribution.Normal;
import spaces.Spaces;
import sweep.SimStateSweep;


public class Environment extends SimStateSweep {
	int gridWidth = 100;
	int gridHeight = 100;
	int neighborhoodWidth = 10;
	int neighborhoodHeight = 10;
	int _maleS = 10;		// straight males
	int _maleG = 10;		// gay males
	int _maleB = 10;		// bi males
	int _femaleS = 10;	// straight females
	int _femaleL = 10;	// lesbian females
	int _femaleB = 10;	// bi females
	int searchRadius = 1; // how many squares should agents search for neighbors
	boolean attract_similar = false;   // false similar, true is attractive
	boolean familiar = true;
	int movementSize = 1;
	double p = 1; // probability that the agent is active
	double preference_threshold = 0;
	double sd_a = 0.1; 	// width of distribution -- standard deviation for attractiveness
	double sd_s = 0.3;	// width of distribution for similarity
	int mate_count = 0;  // counts number of removed pairs
	boolean charts = true;

	
	public Environment(long seed) {
		super(seed);
		// TODO Auto-generated constructor stub
	}

	public Environment(long seed, Class observer) {
		super(seed, observer);
		// TODO Auto-generated constructor stub
	}

	public Environment(long seed, Class observer, String runTimeFileName) {
		super(seed, observer, runTimeFileName);
		// TODO Auto-generated constructor stub
	}
	
	public void agentTraits(int num_of_agent, Sexuality sexuality) {
		Normal normal_a = new Normal(0.6, sd_a, random);
		Normal normal_s = new Normal(0.5, sd_s, random);
		
		double attractive_rate = 0.0;
		double similar_rate = 0.0;
		
		for(int i = 0; i < num_of_agent; i++) {
			int x = random.nextInt(gridWidth);
			int y = random.nextInt(gridHeight);
			
			if (attract_similar) { 		// if attrative
				attractive_rate = normal_a.nextDouble();
				if (attractive_rate > 1.0) {
					attractive_rate = 1.0;
				} else if (attractive_rate < 0) {
					attractive_rate = 0;
				}
			} else {
				similar_rate = normal_s.nextDouble();
				if (similar_rate > 1.0) {
					similar_rate = 1.0;
				} else if (similar_rate < 0) {
					similar_rate = 0;
				}
			}
			
			double rate;
			if (attract_similar) {
				rate = attractive_rate;
			} else {
				rate = similar_rate;
			}
		
			Bag b = sparseSpace.getObjectsAtLocation(x, y);
			while (b != null) {
				y = random.nextInt(gridHeight);
				x = random.nextInt(gridWidth);
			}
		
			int xdir = random.nextInt(3) - 1;
			int ydir = random.nextInt(3) - 1;
			
			int[] neighborhood = createNeighborhood(x, y);
			
			Agent agent =  new Agent(attract_similar, rate, sexuality, preference_threshold, x, y, xdir ,ydir, neighborhood);
			 
			agent.colorBySexuality(agent.sexuality, this, agent);
			schedule.scheduleRepeating(agent);
			sparseSpace.setObjectLocation(agent, x, y);
//			setNeighborhoods(x, y);
		}
	}
	
	public void makeAgents() {
		int total = _maleS + _maleG + _maleB + _femaleS + _femaleL + _femaleB;
		int size = gridWidth * gridHeight;
	
		
		if (total > size) {
			System.out.println("too many agents :(");
			return;
		}
		
		agentTraits(_maleS, Sexuality.STRAIGHT_M);
		agentTraits(_maleG, Sexuality.GAY);
		agentTraits(_maleB, Sexuality.BI_M);
		agentTraits(_femaleS, Sexuality.STRAIGHT_F);
		agentTraits(_femaleL, Sexuality.LESBIAN);
		agentTraits(_femaleB, Sexuality.BI_F);
	}
	
	public int[] createNeighborhood(int x, int y) {

		int neighborhoodWidthSize =  gridWidth / neighborhoodWidth;
		int neighborhoodHeightSize =  gridHeight / neighborhoodHeight;
		
		int x_neighborhood = x / neighborhoodWidthSize;
	    int y_neighborhood = y / neighborhoodHeightSize;

	    int[] neighborhood = {x_neighborhood, y_neighborhood};
	    
		return neighborhood;
	}
	
	
	public void start() {
		super.start();
		spaces = Spaces.SPARSE;	// set the space
		make2DSpace(spaces, gridWidth, gridHeight); // make the space
		makeAgents();
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

	public int getSearchRadius() {
		return searchRadius;
	}

	public void setSearchRadius(int searchRadius) {
		this.searchRadius = searchRadius;
	}

	public boolean getAttract_similar() {
		return this.attract_similar;
	}
	
	public void setAttract_similar(boolean attract_similar) {
		this.attract_similar = attract_similar;
	}
	
	public boolean getFamiliar() {
		return this.attract_similar;
	}
	
	public void setFamiliar(boolean familiar) {
		this.familiar = familiar;
	}
	
	public int getMovementSize() {
		return this.movementSize;
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
	public int get_maleS() {
		return _maleS;
	}

	public void set_maleS(int _maleS) {
		this._maleS = _maleS;
	}

	public int get_maleG() {
		return _maleG;
	}

	public void set_maleG(int _maleG) {
		this._maleG = _maleG;
	}

	public int get_maleB() {
		return _maleB;
	}

	public void set_maleB(int _maleB) {
		this._maleB = _maleB;
	}

	public int get_femaleS() {
		return _femaleS;
	}

	public void set_femaleS(int _femaleS) {
		this._femaleS = _femaleS;
	}

	public int get_femaleL() {
		return _femaleL;
	}

	public void set_femaleL(int _femaleL) {
		this._femaleL = _femaleL;
	}

	public int get_femaleB() {
		return _femaleB;
	}

	public void set_femaleB(int _femaleB) {
		this._femaleB = _femaleB;
	}
	
	public int get_mateCount(int mate_count) {
		return mate_count;
	}
	
	public void setMateCount(int mate_count) {
		this.mate_count = mate_count;
	}
	
	public boolean isCharts() {
		return charts;
	}

	public void setCharts(boolean charts) {
		this.charts = charts;
	}

}
