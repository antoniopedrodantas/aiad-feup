package display;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import agents.LiftAgent;
import agents.RequestAgent;
import launcher.RepastLauncher;
import uchicago.src.sim.analysis.BinDataSource;
import uchicago.src.sim.analysis.Histogram;
import uchicago.src.sim.analysis.OpenHistogram;
import uchicago.src.sim.analysis.Sequence;
import uchicago.src.sim.engine.Schedule;

public class AverageTimeHistogram {

	private RepastLauncher repast;
	private Histogram histogram;
	
	private ArrayList<LiftAgent> lifts;
	
	public AverageTimeHistogram(ArrayList<LiftAgent> lifts, RepastLauncher repast) {
		this.lifts = lifts;
		this.repast = repast;
		
		if (this.histogram != null) histogram.dispose();
			
		this.histogram = new Histogram("Lift's Average Time", 10, 0, 50, repast);
			
		
		histogram.setAxisTitles("Average Time", "Number of Lifts");
		this.buildDisplay();
		this.repast.getSchedule().scheduleActionAtInterval(2, histogram, "step", Schedule.LAST);
	}
	
	
	private void buildDisplay() {

		histogram.createHistogramItem("Lift's Average waiting time", this.lifts, new BinDataSource() {
			public double getBinValue(Object o) {
			    LiftAgent agent = (LiftAgent)o;
			    return agent.getAverageRequestTime();
			  }
		});
		
		histogram.display();
	}
}