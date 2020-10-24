package agents;

import jade.core.Agent;

@SuppressWarnings("serial")
public class LiftAgent extends Agent{
	
	public void setup() {
		System.out.println("Hey, lift here\n");
	}
	
    public void takeDown() {
        System.out.println(getLocalName() + ": done working.");
    }
}
