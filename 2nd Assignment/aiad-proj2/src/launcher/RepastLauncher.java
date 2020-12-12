package launcher;

import java.util.ArrayList;
import agents.BuildingAgent;
import agents.FloorPanelAgent;
import agents.LiftAgent;
import agents.RequestAgent;
import display.AverageOccupationPlot;
import display.AverageTimeHistogram;
import display.ConsensusNetworkDisplay;
import display.CurrentWeightDisplay;
import display.CurrentWeightHistogram;
import display.LiftCurrentPosition;
import display.LiftsGridDisplay;
import display.PeopleEnteringFloor;
import display.PeopleLeavingFloor;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import sajas.core.Runtime;
import sajas.sim.repast3.Repast3Launcher;
import sajas.wrapper.AgentController;
import sajas.wrapper.ContainerController;
import uchicago.src.sim.engine.Schedule;
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
	private CurrentWeightHistogram currWeightHisto;
	private ConsensusNetworkDisplay consensusNetwork;
	private AverageTimeHistogram averageTimeHistogram;
	private PeopleEnteringFloor peopleEntering;
	private PeopleLeavingFloor peopleLeaving;
	
	/* This values can be changed in Model Parameters*/
	private int nmrFLoors = 30;
	private int nmrLifts = 5;
	private float maxSpeed = (float)2.5;
	private float maxWeight = 600;
	private float distanceBetweenFloors = 5;
	private float timeAtFloor = 1;
	private float simulationTime = 50000;
	private double ticksBetweenRequests = 100;
	private double ticksBetweenMove = 5;

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
		return new String[] { "nmrFloors", "nmrLifts", "maxWeight", "maxSpeed", "distanceBetweenFloors", "timeAtFloor", "simulationTime", "ticksBetweenRequests", "ticksBetweenMove"};
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
			getSchedule().scheduleActionAt(this.getSimulationTime(), this, "killSimulation", Schedule.LAST);
		} catch(StaleProxyException e) {
			e.printStackTrace();
		}
	}


	public void killSimulation() throws StaleProxyException {
		this.buildingAgent.takeDown();
		this.stop();
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
			this.liftCurrentPos = new LiftCurrentPosition(lifts, this);
			this.avgPlot = new AverageOccupationPlot(lifts, this.analysis, this);
			this.currWeightHisto = new CurrentWeightHistogram(lifts, this);
			this.consensusNetwork = new ConsensusNetworkDisplay(lifts, nmrLifts, this.nmrFLoors, this);
			this.averageTimeHistogram = new AverageTimeHistogram(lifts, this);
			
			for(LiftAgent lift : lifts) {
				lift.setConsensusNetwork(this.consensusNetwork);;
			}
			
			this.peopleEntering = new PeopleEnteringFloor(floors,this.analysis, this);
			this.peopleLeaving = new PeopleLeavingFloor(floors, this.analysis, this);
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


	public boolean isRunInBatchMode() {
		return runInBatchMode;
	}


	public void setRunInBatchMode(boolean runInBatchMode) {
		this.runInBatchMode = runInBatchMode;
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


	public ConsensusNetworkDisplay getConsensusNetwork() {
		return consensusNetwork;
	}


	public float getSimulationTime() {
		return simulationTime;
	}


	public void setSimulationTime(float simulationTime) {
		this.simulationTime = simulationTime;
	}
	
	public BuildingAgent getBuildingAgent() {
		return buildingAgent;
	}


	public void setBuildingAgent(BuildingAgent buildingAgent) {
		this.buildingAgent = buildingAgent;
	}


	public CurrentWeightHistogram getCurrWeightHisto() {
		return currWeightHisto;
	}


	public void setCurrWeightHisto(CurrentWeightHistogram currWeightHisto) {
		this.currWeightHisto = currWeightHisto;
	}


	public PeopleEnteringFloor getPeopleEntering() {
		return peopleEntering;
	}


	public void setPeopleEntering(PeopleEnteringFloor peopleEntering) {
		this.peopleEntering = peopleEntering;
	}


	public PeopleLeavingFloor getPeopleLeaving() {
		return peopleLeaving;
	}


	public void setPeopleLeaving(PeopleLeavingFloor peopleLeaving) {
		this.peopleLeaving = peopleLeaving;
	}

	
	public void setTicksBetweenRequests(double ticksBetweenRequests) {
		this.ticksBetweenRequests = ticksBetweenRequests;
	}

	public double getTicksBetweenRequests() {
		return this.ticksBetweenRequests;
	}


	public double getTicksBetweenMove() {
		return this.ticksBetweenMove;
	}


	public void setTicksBetweenMove(double ticksBetweenMove) {
		this.ticksBetweenMove = ticksBetweenMove;
	}

}
