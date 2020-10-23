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
		
		Runtime rt = Runtime.instance();
		Profile p1 = new ProfileImpl();
		ContainerController mainContainer = rt.createMainContainer(p1);
		launchAgents(mainContainer);
		
	}
	
	protected void launchAgents(ContainerController ct) {
		AgentController ac1;
		
		try {
			ac1 = ct.acceptNewAgent("vitor", new LiftAgent());
			ac1.start();
			ac1.kill();
		} catch(StaleProxyException e) {
			e.printStackTrace();
		}

	}
}
