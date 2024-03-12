package mate_choice;

import java.awt.Color;
import spaces.Spaces;
import sweep.GUIStateSweep;
import sweep.SimStateSweep;

public class AgentsGUI extends GUIStateSweep {

	public AgentsGUI(SimStateSweep state, int gridWidth, int gridHeight, Color backdrop, Color agentDefaultColor,
			boolean agentPortrayal) {
		super(state, gridWidth, gridHeight, backdrop, agentDefaultColor, agentPortrayal);
	}

	public AgentsGUI(SimStateSweep state) {
		super(state);
	}

	public static void main(String[] args) {
		String[] title2 = {"Frequency Distribution of Strategies"};//A string array, where every entry is the title of a chart
		String[] x2 = {"Time Steps"};//A string array, where every entry is the x-axis title
		String[] y2 = {"Frequency"};//A string array, where every entry is the y-axis title
		AgentsGUI.initializeArrayHistogramChart(1, title2, x2, y2, new int[10]);
		// makes visualization of the simulation (doesn't set the grid height/width used by agents in the simulation)
		AgentsGUI.initialize(Environment.class, null, AgentsGUI.class, 400, 400, Color.WHITE, Color.RED, false, Spaces.SPARSE);
	}

}
