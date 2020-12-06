package display;

import java.util.ArrayList;

import agents.LiftAgent;
import launcher.RepastLauncher;
import uchicago.src.sim.analysis.OpenSequenceGraph;
import uchicago.src.sim.analysis.Sequence;
import uchicago.src.sim.engine.Schedule;

public class LiftCurrentPosition {

	private RepastLauncher repast;
	private OpenSequenceGraph plot;
	private ArrayList<LiftAgent> lifts;
	
	public LiftCurrentPosition(ArrayList<LiftAgent> lifts, RepastLauncher repast) {
		this.lifts = lifts;
		this.repast = repast;
		
		
		if (this.plot != null) plot.dispose();
			this.plot = new OpenSequenceGraph("Lift's Current FLoor", repast);
		
		plot.setAxisTitles("time", "Lift's Current FLoor");
		this.buildDisplay();
		this.repast.getSchedule().scheduleActionAtInterval(2, plot, "step", Schedule.LAST);
	}
	
	
	private void buildDisplay() {
		for(LiftAgent liftAgent : this.lifts) {
			
			plot.addSequence("Lift"+ liftAgent.getId(), new Sequence() {
				public double getSValue() {
					return liftAgent.getFloor();
				}
			});
			
		}
		plot.display();
	}
}
