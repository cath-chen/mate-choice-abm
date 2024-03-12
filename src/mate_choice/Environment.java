package mate_choice;

import ec.util.MersenneTwisterFast;
import mate_choice.Agent;
import sim.util.Bag;
import sim.util.Int2D;
import sim.util.distribution.Normal;
import spaces.Spaces;
import sweep.SimStateSweep;
import mate_choice.F_Matrix;


public class Environment extends SimStateSweep {
	public static int id = 0;
	int gridWidth = 100;
	int gridHeight = 100;
	int neighborhoodWidth = 10;
	int neighborhoodHeight = 10;
	int total = 50;
	int searchRadius = 5; // how many squares should agents search for neighbors
	boolean attract_similar = true;   // false similar, true is attractive
	boolean familiar = true;
	int movementSize = 1;
	double p = 1; // probability that the agent is active
	double preference_threshold = 0;
	double sd_a = 0.1; 	// width of distribution -- standard deviation for attractiveness
	double sd_s = 0.3;	// width of distribution for similarity
	int mate_count = 0;  // counts number of removed pairs
	boolean charts = true;
	F_Matrix matrix; // Instance of F_Matrix for recording interactions
	Bag allAgents; // Store all agents for interaction recording

	
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
		this.matrix = new F_Matrix(total);
	}
	
	public void agentTraits(int num_of_agent, Sexuality sexuality) {
		Normal normal_a = new Normal(0.6, sd_a, random);
		Normal normal_s = new Normal(0.5, sd_s, random);
		
		double attractive_rate = 0.0;
		double similar_rate = 0.0;
		
		for(int i = 0; i < num_of_agent; i++) {
			int x = random.nextInt(gridWidth);
			int y = random.nextInt(gridHeight);
			int id = this.id++;
			
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
			
			Agent agent =  new Agent(id, attract_similar, rate, sexuality, preference_threshold, x, y, xdir ,ydir, neighborhood);
			 
			agent.colorBySexuality(agent.sexuality, this, agent);
			agent.event = schedule.scheduleRepeating(agent);
			sparseSpace.setObjectLocation(agent, x, y);
//			setNeighborhoods(x, y);
		}
	}
	
	public void makeAgents() {
		int size = gridWidth * gridHeight;
	
		if (total > size) {
			System.out.println("too many agents :(");
			return;
		}
		F_Matrix matrix = new F_Matrix(total);
		
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
		return this.familiar;
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
	
	public void recordInteraction(int agent1, int agent2) {
        matrix.recordInteraction(agent1, agent2);
    }

    public int getInteractionCount(int agent1, int agent2) {
        return matrix.getInteractionCount(agent1, agent2);
    }
    
// // Method to check for interactions among agents in the same neighborhood
//    public void checkInteractions() {
//        // Iterate over all agents
//    	if (allAgents == null || allAgents.isEmpty()) {
//    		return;
//    	}
//        for (Object obj : allAgents) {
//            Agent agent = (Agent) obj;
//            Int2D location = sparseSpace.getObjectLocation(agent);
//            int[] neighborhood = createNeighborhood(location.getX(), location.getY());
//            
//            // Iterate over all agents again to find those in the same neighborhood
//            for (Object obj2 : allAgents) {
//                Agent otherAgent = (Agent) obj2;
//                Int2D otherLocation = sparseSpace.getObjectLocation(otherAgent);
//                int[] otherNeighborhood = createNeighborhood(otherLocation.getX(), otherLocation.getY());
//                
//                // Check if agents are in the same neighborhood
//                if (neighborhood[0] == otherNeighborhood[0] && neighborhood[1] == otherNeighborhood[1]) {
//                    // Record interaction in the matrix
//                    recordInteraction(agent.id, otherAgent.id);
//                }
//            }
//        }
    //}

}
