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
	private OpenHistogram OpenHistogram;
	private RequestAgent request;
	private int floors;
	
	public FloorInOutHistogram(RequestAgent request, int nmrFloors, RepastLauncher repast) {
		this.request = request;
		this.repast = repast;
		this.floors = nmrFloors;
		
		if (this.OpenHistogram != null) OpenHistogram.dispose();
			this.OpenHistogram = new OpenHistogram("Floor's People flow", 10, 0, repast);
		
		OpenHistogram.setAxisTitles("Floors", "Number of People");
		this.buildDisplay();
		this.repast.getSchedule().scheduleActionAtInterval(2, OpenHistogram, "step", Schedule.LAST);
	}
	
	
	private void buildDisplay() {
		for (int i = 0; i < this.floors; i++) {
			OpenHistogram.createHistogramItem("Floor " + i + "IN", Collections.singletonList(this.request), new BinDataSource() {
			      public double getBinValue(Object o) {
			          RequestAgent agent = (RequestAgent)o;
			          return agent.getInFlow().get(0);
			        }
			      }, 10, 0);
			OpenHistogram.createHistogramItem("Floor " + i + "OUT", Collections.singletonList(this.request), new BinDataSource() {
			      public double getBinValue(Object o) {
			          RequestAgent agent = (RequestAgent)o;
			          return agent.getOutFlow().get(0);
			        }
			      }, 10, 0);
			//OpenHistogram.createSequence(request.getInFlow().get(i), "Floor "+ i + "IN");
		}	
		
		OpenHistogram.display();
	}
}
