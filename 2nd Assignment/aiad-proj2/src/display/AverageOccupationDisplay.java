package display;

import java.util.ArrayList;

import agents.BuildingAgent;
import agents.FloorPanelAgent;
import agents.LiftAgent;
import launcher.RepastLauncher;
import uchicago.src.sim.analysis.OpenSequenceGraph;
import uchicago.src.sim.analysis.Sequence;
import uchicago.src.sim.engine.Schedule;


public class AverageOccupationDisplay {

	private RepastLauncher repast;
	private OpenSequenceGraph plot;
	private ArrayList<LiftAgent> lifts;
	
	public AverageOccupationDisplay(ArrayList<LiftAgent> lifts, ArrayList<FloorPanelAgent> floors, RepastLauncher repast) {
		this.lifts = lifts;
		this.repast = repast;
		
		
		if (this.plot != null) plot.dispose();
			this.plot = new OpenSequenceGraph("Avg. Occupation (%)", repast);
		
		plot.setAxisTitles("time", "Average Lift Occupation (%)");
		this.buildDisplay();
		repast.getSchedule().scheduleActionAtInterval(2, plot, "step", Schedule.LAST);
	}
	
	
	private void buildDisplay() {

		
		for(LiftAgent la : this.lifts) {
			
			plot.addSequence("Lift"+ la.getId(), new Sequence() {
				public double getSValue() {
					return la.getCurrentWeight();
				}
			});
			
		}
		plot.display();
		
	}
	
	
}
