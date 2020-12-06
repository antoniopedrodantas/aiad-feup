package display;

import java.util.ArrayList;

import agents.LiftAgent;
import launcher.RepastLauncher;
import uchicago.src.sim.analysis.OpenSequenceGraph;
import uchicago.src.sim.analysis.Sequence;
import uchicago.src.sim.engine.Schedule;

public class AverageOccupationDisplay {

	private RepastLauncher repast;
	private OpenSequenceGraph plot;
	private ArrayList<LiftAgent> lifts;
	
	public AverageOccupationDisplay(ArrayList<LiftAgent> lifts,  RepastLauncher repast) {
		this.lifts = lifts;
		this.repast = repast;
		
		
		if (this.plot != null) plot.dispose();
			this.plot = new OpenSequenceGraph("Avg. Occupation (%)", repast);
		
		plot.setAxisTitles("time", "Average Lift Occupation (%)");
		this.buildDisplay();
		this.repast.getSchedule().scheduleActionAtInterval(2, plot, "step", Schedule.LAST);
	}
	
	
	private void buildDisplay() {
		for(LiftAgent liftAgent : this.lifts) {
			
			plot.addSequence("Lift"+ liftAgent.getId(), new Sequence() {
				public double getSValue() {
					return liftAgent.getCurrentWeight();
				}
			});
			
		}
		plot.display();
	}
}
