package display;

import java.util.ArrayList;

import agents.FloorPanelAgent;
import launcher.RepastLauncher;
import uchicago.src.sim.analysis.OpenSequenceGraph;
import uchicago.src.sim.engine.Schedule;
import utils.Analysis;

public class PeopleEnteringFloor {
	
	private ArrayList<FloorPanelAgent> floors;
	private Analysis analysis;
	private RepastLauncher repast;
	private OpenSequenceGraph plot;
	
	public PeopleEnteringFloor(ArrayList<FloorPanelAgent> floors, Analysis analysis, RepastLauncher repast) {
		this.floors = floors;
		this.analysis = analysis;
		this.repast = repast;
		
		if (this.plot != null) plot.dispose();
			this.plot = new OpenSequenceGraph("People entering at floor", repast);
		
		this.plot.setAxisTitles("time", "People entering");
		this.buildDisplay();
		this.repast.getSchedule().scheduleActionAtInterval(2, plot, "step", Schedule.LAST);
	}
	
	private void buildDisplay() {
		
	}
}
