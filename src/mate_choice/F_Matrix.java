package mate_choice;

public class F_Matrix {
    private int[][] matrix;

    public F_Matrix(int numAgents) {
        this.matrix = new int[numAgents][numAgents];
        // Initialize matrix with zeros
        for (int i = 0; i < numAgents; i++) {
            for (int j = 0; j < numAgents; j++) {
                this.matrix[i][j] = 0;
            }
        }
    }

    public void recordInteraction(int agent1, int agent2) {
    	
        if (agent1 <= 0 || agent2 <= 0 || agent1 > this.matrix.length || agent2 > this.matrix.length) {
            // Handle invalid agents, maybe throw an exception or return early
            return;
        }
        
        // Increment interaction count between agent1 and agent2
    	// familiarity score caps at 6, or a change in value of 0.3
    	if (this.matrix[agent1 - 1][agent2 - 1] == 6) {
    		return;
    	}
        this.matrix[agent1 - 1][agent2 - 1]++;
        // For undirected interactions, increment count in both directions
        this.matrix[agent2 - 1][agent1 - 1]++;
//    	System.out.println("recorded interaction\n");
    }

    public int getInteractionCount(int agent1, int agent2) {
    	
        if (agent1 <= 0 || agent2 <= 0 || agent1 > this.matrix.length || agent2 > this.matrix.length) {
            // Handle invalid agents, maybe throw an exception or return 0
            return 0;
        }
        
        return this.matrix[agent1 - 1][agent2 - 1];
    }
}
