package display;

import java.util.ArrayList;

import agents.FloorPanelAgent;
import launcher.RepastLauncher;
import uchicago.src.sim.analysis.OpenSequenceGraph;
import uchicago.src.sim.analysis.Sequence;
import uchicago.src.sim.engine.Schedule;
import utils.Analysis;

public class PeopleLeavingFloor {
	
	private ArrayList<FloorPanelAgent> floors;
	private Analysis analysis;
	private RepastLauncher repast;
	private OpenSequenceGraph plot;
	
	public PeopleLeavingFloor(ArrayList<FloorPanelAgent> floors, Analysis analysis, RepastLauncher repast) {
		this.floors = floors;
		this.analysis = analysis;
		this.repast = repast;
		
		if (this.plot != null) plot.dispose();
			this.plot = new OpenSequenceGraph("People exiting at floor", repast);
			
		this.plot.setAxisTitles("time", "People exiting");
		this.buildDisplay();
		this.repast.getSchedule().scheduleActionAtInterval(2, plot, "step", Schedule.LAST);
	}
	
	private void buildDisplay() {
		for(FloorPanelAgent fa : this.floors) {
			
			plot.addSequence("FloorPanelAgent"+ fa.getFloor(), new Sequence() {
				public double getSValue() {
					return analysis.getExitingFloor().get(fa.getFloor());
				}
			});
					
		}
		plot.display();
	}
}
