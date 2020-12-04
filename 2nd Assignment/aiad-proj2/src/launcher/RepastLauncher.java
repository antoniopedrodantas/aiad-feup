package launcher;
import uchicago.src.reflector.ListPropertyDescriptor;

import java.util.ArrayList;
import java.util.List;

import agents.BuildingAgent;
import agents.FloorPanelAgent;
import agents.LiftAgent;
import display.LiftsGridDisplay;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;

import sajas.core.Agent;
import sajas.core.Runtime;
import sajas.sim.repast3.Repast3Launcher;
import sajas.wrapper.AgentController;
import sajas.wrapper.ContainerController;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Displayable;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.Network2DDisplay;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.gui.Zoomable;
import uchicago.src.sim.network.DefaultDrawableNode;
import uchicago.src.sim.space.Object2DGrid;
import utils.Analysis;


public class RepastLauncher extends Repast3Launcher {
	
	private ContainerController mainContainer;
	private ContainerController agentContainer;
	private static final boolean BATCH_MODE = true;
	private boolean runInBatchMode;
	
	/* display */
	private DisplaySurface dsurf;
	private int WIDTH = 100, HEIGHT = 100;
	
	/* This values can be changed in Model Parameters*/
	private int nmrFLoors = 18;
	private int nmrLifts = 3;
	private float maxSpeed = (float)2.5;
	private float maxWeight = 600;
	private float distanceBetweenFloors = 5;
	private float timeAtFloor = 1;
	
	private ArrayList<LiftAgent> liftAgents;
	private BuildingAgent buildingAgent;
	private ArrayList<FloorPanelAgent> floorPanelAgents;
	
	public RepastLauncher(boolean runInBatchMode) {
		super();
		this.runInBatchMode = runInBatchMode;
	}
	
	
	@Override
	public String[] getInitParam() {
		return new String[] { "nmrFloors", "nmrLifts", "maxWeight", "maxSpeed", "distanceBetweenFloors", "timeAtFloor"};
	}

	@Override
	public String getName() {
		return "Lift Management System";
	}

	@Override
	protected void launchJADE() {
	
		Runtime runTime = Runtime.instance();
		Profile profile = new ProfileImpl();
		ContainerController mainContainer = runTime.createMainContainer(profile);
		try {
			setAgentContainer(mainContainer);
			launchAgents(mainContainer);
		} catch (StaleProxyException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	protected void launchAgents(ContainerController mainContainer) throws InterruptedException, StaleProxyException {
		
		AgentController agentController;
		
		this.liftAgents = new ArrayList<LiftAgent>();
		this.floorPanelAgents = new ArrayList<FloorPanelAgent>();
		
		String[] args = {String.valueOf(this.getNmrFLoors()), String.valueOf(this.getNmrLifts()), String.valueOf(this.getMaxWeight()), String.valueOf(this.getMaxSpeed()), String.valueOf(this.getDistanceBetweenFloors()), String.valueOf(this.getTimeAtFloor())}; 
						//nmrFloors, nmrLifts, maxWeight per lift, lift maxSpeed, distance between floors, timeAtFloor(time the lift stops on floors for people to enter and exit)
		
		try {
			this.buildingAgent = new BuildingAgent(args, mainContainer, new Analysis(args), this);
			agentController = mainContainer.acceptNewAgent("buildingAgent", this.buildingAgent);
			agentController.start();
			this.liftAgents = this.buildingAgent.getLiftsAgent();
			this.floorPanelAgents = this.buildingAgent.getFloorPanels();
		} catch(StaleProxyException e) {
			e.printStackTrace();
		}
		
		this.buildAndScheduleDisplay();
	}

	@Override
	public void setup() {
		super.setup();
		// property descriptors
		// ...
	}

	@Override
	public void begin() {
		super.begin();
		if(!runInBatchMode) {
			//buildAndScheduleDisplay();
		}
	}
	
	private void buildAndScheduleDisplay() {
		//this.displayLiftsGrid();
		var liftGridDisplay = new LiftsGridDisplay(this.liftAgents, this.floorPanelAgents, this);
	}
	
	private void displayLiftsGrid() {

		this.liftAgents = this.buildingAgent.getLiftsAgent();
		this.floorPanelAgents = this.buildingAgent.getFloorPanels();
		if (dsurf != null) dsurf.dispose();
		dsurf = new DisplaySurface(this, "Lift Position Display");
		registerDisplaySurface("Lift Position Display", dsurf);
		
		Object2DGrid space = new Object2DGrid(WIDTH,HEIGHT);
		addLiftsToDisplay(space);
		Object2DDisplay disp = new Object2DDisplay(space);
		dsurf.addDisplayableProbeable(disp, "lifts");
        
		dsurf.display();
		
		if(this.liftAgents.size() !=0)
			System.out.println(this.liftAgents.get(0).getFloor());
		
		getSchedule().scheduleActionAtInterval(1, dsurf, "updateDisplay", Schedule.LAST);
	}
	
	private void addLiftsToDisplay(Object2DGrid space) {
		
		space.putObjectAt(10, this.liftAgents.get(0).getY(), this.liftAgents.get(0));
	}

	
	
	/**
	 * Launching Repast3
	 * @param args
	 */
	public static void main(String[] args) {
		
		boolean runMode = !BATCH_MODE; 
		SimInit init = new SimInit();
		init.setNumRuns(1);   // works only in batch mode
		init.loadModel(new RepastLauncher(runMode), null, runMode);
	

	}


	public int getNmrFLoors() {
		return nmrFLoors;
	}


	public void setNmrFLoors(int nmrFLoors) {
		this.nmrFLoors = nmrFLoors;
	}


	public int getNmrLifts() {
		return nmrLifts;
	}


	public void setNmrLifts(int nmrLifts) {
		this.nmrLifts = nmrLifts;
	}


	public float getMaxWeight() {
		return maxWeight;
	}


	public void setMaxWeight(float maxWeight) {
		this.maxWeight = maxWeight;
	}


	public float getMaxSpeed() {
		return maxSpeed;
	}


	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}


	public float getDistanceBetweenFloors() {
		return distanceBetweenFloors;
	}


	public void setDistanceBetweenFloors(float distanceBetweenFloors) {
		this.distanceBetweenFloors = distanceBetweenFloors;
	}


	public float getTimeAtFloor() {
		return timeAtFloor;
	}


	public void setTimeAtFloor(float timeAtFloor) {
		this.timeAtFloor = timeAtFloor;
	}


	public ContainerController getMainContainer() {
		return mainContainer;
	}


	public void setMainContainer(ContainerController mainContainer) {
		this.mainContainer = mainContainer;
	}


	public ContainerController getAgentContainer() {
		return agentContainer;
	}


	public void setAgentContainer(ContainerController agentContainer) {
		this.agentContainer = agentContainer;
	}

}
