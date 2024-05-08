package mate_choice;

import java.awt.Color;

import mate_choice.Experimenter;
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
		String[] title = {"Frequency Distribution of Sexualities"};//A string array, where every entry is the title of a chart
		String[] x = {"Time Steps"};//A string array, where every entry is the x-axis title
		String[] y = {"Frequency"};//A string array, where every entry is the y-axis title
		AgentsGUI.initializeArrayHistogramChart(1, title, x, y, new int[10]);
		// makes visualization of the simulation (doesn't set the grid height/width used by agents in the simulation)
		
		// TODO: changed null to experimenter.class in initialize -> null pointer exception Cannot invoke "spaces.SparseGrid2Dex.getAllObjects()" because "this.sparseSpaceGrid" is null
		AgentsGUI.initialize(Environment.class, Experimenter.class, AgentsGUI.class, 400, 400, Color.WHITE, Color.BLUE, false, Spaces.SPARSE);
	}

}
