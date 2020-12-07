package launcher;

import java.util.ArrayList;
import agents.BuildingAgent;
import agents.FloorPanelAgent;
import agents.LiftAgent;
import agents.RequestAgent;
import display.AverageOccupationPlot;
import display.CurrentWeightDisplay;
import display.FloorInOutHistogram;
import display.LiftCurrentPosition;
import display.LiftsGridDisplay;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import sajas.core.Runtime;
import sajas.sim.repast3.Repast3Launcher;
import sajas.wrapper.AgentController;
import sajas.wrapper.ContainerController;
import uchicago.src.sim.engine.SimInit;
import utils.Analysis;


public class RepastLauncher extends Repast3Launcher {
	
	private ContainerController mainContainer;
	private ContainerController agentContainer;
	private static final boolean BATCH_MODE = true;
	private boolean runInBatchMode;
	
	/* display */
	private LiftsGridDisplay liftGridDisplay;
	private CurrentWeightDisplay currWeight;
	private LiftCurrentPosition liftCurrentPos;
	private AverageOccupationPlot avgPlot;
	private FloorInOutHistogram peopleFlow;
	
	/* This values can be changed in Model Parameters*/
	private int nmrFLoors = 18;
	private int nmrLifts = 3;
	private float maxSpeed = (float)2.5;
	private float maxWeight = 600;
	private float distanceBetweenFloors = 5;
	private float timeAtFloor = 1;
	
	private ArrayList<LiftAgent> liftAgents;
	private BuildingAgent buildingAgent;
	private Analysis analysis;
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
		this.analysis = new Analysis(args);
		
		try {
			this.buildingAgent = new BuildingAgent(args, mainContainer, this.analysis, this);
			agentController = mainContainer.acceptNewAgent("buildingAgent", this.buildingAgent);
			agentController.start();	
		} catch(StaleProxyException e) {
			e.printStackTrace();
		}
	}



	@Override
	public void setup() {
		super.setup();
	}

	@Override
	public void begin() {
		super.begin();
	}
	
	public void buildAndScheduleDisplay(ArrayList<LiftAgent> lifts, ArrayList<FloorPanelAgent> floors, RequestAgent request) {
		if(!this.runInBatchMode) {
			this.liftGridDisplay = new LiftsGridDisplay(lifts, this.nmrLifts, this.nmrFLoors, this);
			this.currWeight = new CurrentWeightDisplay(lifts, this);
			this.liftCurrentPos = new LiftCurrentPosition(lifts, this);
			this.avgPlot = new AverageOccupationPlot(lifts, this.analysis, this);
			this.peopleFlow = new FloorInOutHistogram(request, this.nmrFLoors, this);
		}
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


	public LiftsGridDisplay getLiftGridDisplay() {
		return liftGridDisplay;
	}


	public CurrentWeightDisplay getcurrWeight() {
		return currWeight;
	}


	public void setcurrWeight(CurrentWeightDisplay currWeight) {
		this.currWeight = currWeight;
	}


	public void setLiftGridDisplay(LiftsGridDisplay liftGridDisplay) {
		this.liftGridDisplay = liftGridDisplay;
	}
	
	public ArrayList<LiftAgent> getLiftAgents() {
		return liftAgents;
	}


	public void setLiftAgents(ArrayList<LiftAgent> liftAgents) {
		this.liftAgents = liftAgents;
	}


	public ArrayList<FloorPanelAgent> getFloorPanelAgents() {
		return floorPanelAgents;
	}


	public void setFloorPanelAgents(ArrayList<FloorPanelAgent> floorPanelAgents) {
		this.floorPanelAgents = floorPanelAgents;
	}


	public LiftCurrentPosition getLiftCurrentPos() {
		return liftCurrentPos;
	}


	public void setLiftCurrentPos(LiftCurrentPosition liftCurrentPos) {
		this.liftCurrentPos = liftCurrentPos;
	}


	public AverageOccupationPlot getAvgPlot() {
		return avgPlot;
	}


	public void setAvgPlot(AverageOccupationPlot avgPlot) {
		this.avgPlot = avgPlot;
	}

}
