package display;

import java.util.ArrayList;

import agents.FloorPanelAgent;
import launcher.RepastLauncher;
import utils.Analysis;

public class PeopleEnteringFloor {
	
	private ArrayList<FloorPanelAgent> floors;
	private Analysis analysis;
	private RepastLauncher repast;
	
	public PeopleEnteringFloor(ArrayList<FloorPanelAgent> floors, Analysis analysis, RepastLauncher repast) {
		this.floors = floors;
		this.analysis = analysis;
		this.repast = repast;
	}
}
