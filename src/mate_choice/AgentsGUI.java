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
		// makes visualization of the simulation (doesn't set the grid height/width used by agents in the simulation)
		AgentsGUI.initialize(Environment.class, null, AgentsGUI.class, 400, 400, Color.WHITE, Color.RED, false, Spaces.SPARSE);
	}

}
