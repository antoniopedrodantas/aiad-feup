package agents;

import behaviours.FloorListeningBehaviour;
import utils.DFSubscriptionInit;


import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;


@SuppressWarnings("serial")
public class FloorPanelAgent extends Agent {
	
	private int floor;
	
	// for JADE testing purposes
	public FloorPanelAgent() {
		this.floor = 2;
	}
	
	// for the REAL DEAL
	public FloorPanelAgent(int floor) {
		this.floor = floor;
	} 
	 
	public void setup() {
		System.out.println(this.toString());
		addBehaviour(new FloorListeningBehaviour(this));
		
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("lift-service");
		template.addServices(sd);
		addBehaviour(new DFSubscriptionInit(this, template));

	}
	
	public void takeDown() {
		System.out.println(getLocalName() + ": done working.");
	}
	
	@Override
	public String toString() {
		return "Started FloorPanel at floor " + this.floor + "\n";
	}
	
	/*getters*/
	public int getFloor() {
		return this.floor;
	}

}
