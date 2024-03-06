package mate_choice;

import mate_choice.Agent;
import sim.util.Bag;
import sim.util.Int2D;
import spaces.Spaces;
import sweep.SimStateSweep;

public class Environment extends SimStateSweep {
	int gridWidth = 50;
	int gridHeight = 50;
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
	
	public void makeAgents() {
		int total = _maleS + _maleG + _maleB + _femaleS + _femaleL + _femaleB;
		int size = gridWidth * gridHeight;
		if (total > size) {
			System.out.println("too many agents :(");
			return;
		}
		
		for(int i = 0; i < _maleS; i++) {
			int x = random.nextInt(gridWidth);
			int y = random.nextInt(gridHeight);
		
			Bag b = sparseSpace.getObjectsAtLocation(x, y);
			while (b != null) {
				y = random.nextInt(gridHeight);
				x = random.nextInt(gridWidth);
			}
		
			int xdir = random.nextInt(3) - 1;
			int ydir = random.nextInt(3) - 1;
			
			Agent agent =  new Agent(attract_similar, Sexuality.STRAIGHT_M, preference_threshold, x, y, xdir ,ydir);
			 
			agent.colorBySexuality(agent.sexuality, this, agent);
			schedule.scheduleRepeating(agent);
			sparseSpace.setObjectLocation(agent, x, y);
		}
		
		for(int i = 0; i < _maleG; i++) {
			int x = random.nextInt(gridWidth);
			int y = random.nextInt(gridHeight);
		
			Bag b = sparseSpace.getObjectsAtLocation(x, y);
			while (b != null) {
				y = random.nextInt(gridHeight);
				x = random.nextInt(gridWidth);
			}
		
			int xdir = random.nextInt(3) - 1;
			int ydir = random.nextInt(3) - 1;
			
			Agent agent =  new Agent(attract_similar, Sexuality.GAY, preference_threshold, x, y, xdir ,ydir);
			agent.colorBySexuality(agent.sexuality, this, agent);
			schedule.scheduleRepeating(agent);
			sparseSpace.setObjectLocation(agent, x, y);
	
		
		}
		
		for(int i = 0; i < _maleB; i++) {
			int x = random.nextInt(gridWidth);
			int y = random.nextInt(gridHeight);
		
			Bag b = sparseSpace.getObjectsAtLocation(x, y);
			while (b != null) {
				y = random.nextInt(gridHeight);
				x = random.nextInt(gridWidth);
			}
		
			int xdir = random.nextInt(3) - 1;
			int ydir = random.nextInt(3) - 1;
			
			Agent agent =  new Agent(attract_similar, Sexuality.BI_M, preference_threshold, x, y, xdir ,ydir);
			 
			agent.colorBySexuality(agent.sexuality, this, agent);
			schedule.scheduleRepeating(agent);
			sparseSpace.setObjectLocation(agent, x, y);
		}
		
		for(int i = 0;i < _femaleS; i++) {
			int x = random.nextInt(gridWidth);
			int y = random.nextInt(gridHeight);
		
			Bag b = sparseSpace.getObjectsAtLocation(x, y);
			while (b != null) {
				y = random.nextInt(gridHeight);
				x = random.nextInt(gridWidth);
			}
		
			int xdir = random.nextInt(3) - 1;
			int ydir = random.nextInt(3) - 1;
			
			Agent agent =  new Agent(attract_similar, Sexuality.STRAIGHT_F, preference_threshold, x, y, xdir ,ydir);
			 
			agent.colorBySexuality(agent.sexuality, this, agent);
			schedule.scheduleRepeating(agent);
			sparseSpace.setObjectLocation(agent, x, y);
		}
		
		for(int i = 0; i < _femaleL; i++) {
			int x = random.nextInt(gridWidth);
			int y = random.nextInt(gridHeight);
		
			Bag b = sparseSpace.getObjectsAtLocation(x, y);
			while (b != null) {
				y = random.nextInt(gridHeight);
				x = random.nextInt(gridWidth);
			}
		
			int xdir = random.nextInt(3) - 1;
			int ydir = random.nextInt(3) - 1;
			
			Agent agent =  new Agent(attract_similar, Sexuality.LESBIAN, preference_threshold, x, y, xdir ,ydir);
			 
			agent.colorBySexuality(agent.sexuality, this, agent);
			schedule.scheduleRepeating(agent);
			sparseSpace.setObjectLocation(agent, x, y);
		}
		
		for(int i = 0; i < _femaleB; i++) {
			int x = random.nextInt(gridWidth);
			int y = random.nextInt(gridHeight);
		
			Bag b = sparseSpace.getObjectsAtLocation(x, y);
			while (b != null) {
				y = random.nextInt(gridHeight);
				x = random.nextInt(gridWidth);
			}
		
			int xdir = random.nextInt(3) - 1;
			int ydir = random.nextInt(3) - 1;
			
			Agent agent =  new Agent(attract_similar, Sexuality.BI_F, preference_threshold, x, y, xdir ,ydir);
			 
			agent.colorBySexuality(agent.sexuality, this, agent);
			schedule.scheduleRepeating(agent);
			sparseSpace.setObjectLocation(agent, x, y);
		}
	}
	
	
	public void start() {
		super.start();
		spaces = Spaces.SPARSE;	// set the space
		make2DSpace(spaces, gridWidth, gridHeight); // make the space
		makeAgents();
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

}
