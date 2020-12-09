package display;

import java.util.ArrayList;

import agents.FloorPanelAgent;
import launcher.RepastLauncher;
import utils.Analysis;

public class PeopleLeavingFloor {
	
	private ArrayList<FloorPanelAgent> floors;
	private Analysis analysis;
	private RepastLauncher repast;
	
	public PeopleLeavingFloor(ArrayList<FloorPanelAgent> floors, Analysis analysis, RepastLauncher repast) {
		this.floors = floors;
		this.analysis = analysis;
		this.repast = repast;
	}
}
