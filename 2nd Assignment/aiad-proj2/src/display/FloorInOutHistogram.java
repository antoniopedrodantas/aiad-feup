package display;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import agents.RequestAgent;
import launcher.RepastLauncher;
import uchicago.src.sim.analysis.BinDataSource;
import uchicago.src.sim.analysis.OpenHistogram;
import uchicago.src.sim.analysis.Sequence;
import uchicago.src.sim.engine.Schedule;

public class FloorInOutHistogram {

	private RepastLauncher repast;
	private OpenHistogram histogram;
	private RequestAgent request;
	private int floors;
	
	public FloorInOutHistogram(RequestAgent request, int nmrFloors, RepastLauncher repast) {
		this.request = request;
		this.repast = repast;
		this.floors = nmrFloors;
		
		if (this.histogram != null) histogram.dispose();
			this.histogram = new OpenHistogram("Floor's People flow", nmrFloors, -1, repast);
		

		histogram.setYRange(0, 100.0);
		
		histogram.setAxisTitles("Floors", "Number of People");
		this.buildDisplay();
		this.repast.getSchedule().scheduleActionAtInterval(2, histogram, "step", Schedule.LAST);
	}
	
	
	private void buildDisplay() {

		histogram.createHistogramItem("Floor " + 1 + "IN", Collections.singletonList(this.request), new HistogramBinValue(true), 1, 0);
		histogram.createHistogramItem("Floor " + 1 + "OUT", Collections.singletonList(this.request), new HistogramBinValue(false), 1, 0);
		for (int i = 0; i < this.floors; i++) {
			
			//OpenHistogram.createSequence(request.getInFlow().get(i), "Floor "+ i + "IN");
		}	
		histogram.display();
	}
}
