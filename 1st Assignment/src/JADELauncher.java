import agents.BuildingAgent;
import agents.LiftAgent;

import java.io.IOException;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import jade.core.Runtime;

public class JADELauncher {
	
	public static void main(String[] args) throws IOException {
		new JADELauncher().launchJade();
    }
	
	protected void launchJade(){
		
		Runtime runTime = Runtime.instance();
		Profile profile = new ProfileImpl();
		ContainerController mainContainer = runTime.createMainContainer(profile);
		launchAgents(mainContainer);
		
	}
	
	protected void launchAgents(ContainerController mainContainer) {
		
		AgentController agentController1;
		String[] args = {"50", "8", "600.0", "2.5", "5.0"}; //maybe we could do this via console or txt file 
		
		try {
			agentController1 = mainContainer.acceptNewAgent("buildingAgent", new BuildingAgent(args));
			agentController1.start();
		} catch(StaleProxyException e) {
			e.printStackTrace();
		}
		
		
		//just here for testing reason - will be called from BuildingAgent
		AgentController agentController2;
		
		try {
			agentController2 = mainContainer.acceptNewAgent("liftAgent1", new LiftAgent());
			agentController2.start();
		} catch(StaleProxyException e) {
			e.printStackTrace();
		}

	}
}
