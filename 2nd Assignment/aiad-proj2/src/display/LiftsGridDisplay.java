package display;

import java.util.ArrayList;

import agents.FloorPanelAgent;
import agents.LiftAgent;
import launcher.RepastLauncher;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.space.Object2DGrid;

public class LiftsGridDisplay {
	
	private ArrayList<LiftAgent> liftAgents;
	private ArrayList<FloorPanelAgent> floorPanelAgents;
	private DisplaySurface dsurf;
	private Object2DGrid space;
	private RepastLauncher repast;
	final int WIDTH = 30, HEIGHT = 20; 
	
	public LiftsGridDisplay(ArrayList<LiftAgent> liftAgents, ArrayList<FloorPanelAgent> floorPanelAgents, RepastLauncher repast) {
		this.liftAgents = liftAgents;
		this.floorPanelAgents = floorPanelAgents;
		this.repast = repast;
		if (dsurf != null) 
			dsurf.dispose();
		dsurf = new DisplaySurface(repast, "Lift Position Display");
		repast.registerDisplaySurface("Lift Position Display", dsurf);
		
		this.buildModel();
		this.buildDisplay();
		repast.getSchedule().scheduleActionAtInterval(1, dsurf, "updateDisplay", Schedule.LAST);
	}
	
	private void buildModel() {
		this.space = new Object2DGrid(WIDTH,HEIGHT);
		for(LiftAgent lift : liftAgents) {
			System.out.println("HERE ADDDD");
			this.space.putObjectAt(lift.getX(), lift.getY(), lift);}
	}
	private void buildDisplay() {
		Object2DDisplay agentDisplay = new Object2DDisplay(this.space);
		agentDisplay.setObjectList(liftAgents);
		dsurf.addDisplayableProbeable(
		agentDisplay, "Agents");
		this.repast.addSimEventListener(dsurf);
		dsurf.display();
	}	
	
}
