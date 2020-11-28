import uchicago.src.reflector.ListPropertyDescriptor;

import agents.BuildingAgent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;

import sajas.core.Agent;
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
	
	/* This values can be changed in Model Parameters*/
	private int nmrFLoors = 18;
	private int nmrLifts = 3;
	private float maxWeight = 600;
	private float maxSpped = (float) 2.5;
	private float distanceBetweenFloors = 5;
	private float timeAtFloor = 1;
	
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
		
		String[] args = {"18", "3", "600.0", "2.5", "5.0", "1"}; 
						//nmrFloors, nmrLifts, maxWeight per lift, lift maxSpeed, distance between floors, timeAtFloor(time the lift stops on floors for people to enter and exit)
		
		try {
			agentController = mainContainer.acceptNewAgent("buildingAgent", new BuildingAgent(args, mainContainer, new Analysis(args)));
			agentController.start();
			
		} catch(StaleProxyException e) {
			e.printStackTrace();
		}
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
			buildAndScheduleDisplay();
		}
	}
	
	private void buildAndScheduleDisplay() {
	
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


	public float getMaxSpped() {
		return maxSpped;
	}


	public void setMaxSpped(float maxSpped) {
		this.maxSpped = maxSpped;
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
