import agents.BuildingAgent;

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
		
		Runtime rt = Runtime.instance();
		Profile p1 = new ProfileImpl();
		ContainerController mainContainer = rt.createMainContainer(p1);
		launchAgents(mainContainer);
		
	}
	
	protected void launchAgents(ContainerController ct) {
		
		AgentController ac1;
		String[] args = {"50", "8", "600.0", "2.5", "5.0"}; //maybe we could do this via console or txt file 
		
		try {
			ac1 = ct.acceptNewAgent("building", new BuildingAgent(args));
			ac1.start();
		} catch(StaleProxyException e) {
			e.printStackTrace();
		}

	}
}
