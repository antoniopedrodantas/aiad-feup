package display;

import java.util.ArrayList;

import agents.LiftAgent;
import launcher.RepastLauncher;
import uchicago.src.sim.analysis.OpenSequenceGraph;
import uchicago.src.sim.analysis.Sequence;
import uchicago.src.sim.engine.Schedule;
import utils.Analysis;

public class AverageOccupationPlot {

	private RepastLauncher repast;
	private OpenSequenceGraph plot;
	private ArrayList<LiftAgent> lifts;
	Analysis analysis;
	
	public AverageOccupationPlot(ArrayList<LiftAgent> lifts, Analysis analysis, RepastLauncher repast) {
		this.lifts = lifts;
		this.repast = repast;
		this.analysis = analysis;
		
		if (this.plot != null) plot.dispose();
			this.plot = new OpenSequenceGraph("Lift's Average Occupation", repast);
		
		plot.setAxisTitles("time", "Lift's Average Occupation");
		this.buildDisplay();
		this.repast.getSchedule().scheduleActionAtInterval(5, plot, "step", Schedule.LAST);
	}
	
	
	private void buildDisplay() {
		for(LiftAgent liftAgent : this.lifts) {
			
			plot.addSequence("Lift"+ liftAgent.getId(), new Sequence() {
				public double getSValue() {
					return analysis.getAverageOccupation().get(liftAgent.getId() - 1).get(1);
				}
			});
			
		}
		plot.display();
	}
}
