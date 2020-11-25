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
	
	@Override
	public String[] getInitParam() {
		return new String[0];
	}

	@Override
	public String getName() {
		return "SAJaS Project";
	}

	@Override
	protected void launchJADE() {
		
		// Runtime rt = Runtime.instance();
		// Profile p1 = new ProfileImpl();
		// mainContainer = rt.createMainContainer(p1);
		
		// launchAgents();
		
		
		Runtime runTime = Runtime.instance();
		Profile profile = new ProfileImpl();
		ContainerController mainContainer = runTime.createMainContainer(profile);
		try {
			System.out.println("About to launch Building!");
			launchBuilding(mainContainer);
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void launchAgents() {
		
		try {
			
			// TODO
			mainContainer.acceptNewAgent("MyAgent", new Agent()).start();
			// ...
			
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
		
	}
	
	protected void launchBuilding(ContainerController mainContainer) throws InterruptedException, StaleProxyException {
		
		System.out.println("Entered Building");
		
		AgentController agentController;
		
		System.out.println("Created agentController");
		
		String[] args = {"18", "3", "600.0", "2.5", "5.0", "1"}; 
						//nmrFloors, nmrLifts, maxWeight per lift, lift maxSpeed, distance between floors, timeAtFloor(time the lift stops on floors for people to enter and exit)
		
		try {
			agentController = mainContainer.acceptNewAgent("buildingAgent", new BuildingAgent(args, mainContainer, new Analysis(args)));
			agentController.start();
			
		} catch(StaleProxyException e) {
			e.printStackTrace();
		}
	}
/*
	@Override
	public void setup() {
		super.setup();

		// property descriptors
		// ...
	}

	@Override
	public void begin() {
		super.begin();
		
		// display surfaces, spaces, displays, plots, ...
		// ...
	}
*/
	/**
	 * Launching Repast3
	 * @param args
	 */
	public static void main(String[] args) {
		// boolean BATCH_MODE = true;
		// SimInit init = new SimInit();
		// init.setNumRuns(1);   // works only in batch mode
		// init.loadModel(new RepastLauncher(), null, BATCH_MODE);
		
		boolean BATCH_MODE = true;
		SimInit init = new SimInit();
		init.setNumRuns(1);   // works only in batch mode
		init.loadModel(new RepastLauncher(), null, BATCH_MODE);

	}

}
