import agents.BuildingAgent;
import utils.Analysis;

import java.io.IOException;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import jade.core.Runtime;

public class JADELauncher {
	
	public static void main(String[] args) throws IOException, InterruptedException, StaleProxyException {
		new JADELauncher().launchJade();
    }
	
	protected void launchJade() throws InterruptedException, StaleProxyException{
		Runtime runTime = Runtime.instance();
		Profile profile = new ProfileImpl();
		ContainerController mainContainer = runTime.createMainContainer(profile);
		launchBuilding(mainContainer);
	}
	
	protected void launchBuilding(ContainerController mainContainer) throws InterruptedException, StaleProxyException {
		
		AgentController agentController;
		String[] args = {"20", "4", "600.0", "2.5", "5.0", "1"}; //maybe we could do this via console or txt file 
		
		try {
			agentController = mainContainer.acceptNewAgent("buildingAgent", new BuildingAgent(args, mainContainer, new Analysis(args)));
			agentController.start();
			
		} catch(StaleProxyException e) {
			e.printStackTrace();
		}
	}
}
