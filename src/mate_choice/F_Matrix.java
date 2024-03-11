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
        // Increment interaction count between agent1 and agent2
        this.matrix[agent1][agent2]++;
        // For undirected interactions, increment count in both directions
        this.matrix[agent2][agent1]++;
    }

    public int getInteractionCount(int agent1, int agent2) {
        return this.matrix[agent1][agent2];
    }

    // Other methods for querying and analyzing interactions
}
