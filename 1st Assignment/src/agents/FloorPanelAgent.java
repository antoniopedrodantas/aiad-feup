package agents;

import behaviours.FloorListeningBehaviour;


import java.util.ArrayList;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;



@SuppressWarnings("serial")
public class FloorPanelAgent extends Agent {
	
	private int floor;
	private ArrayList<String> liftList;
	
	// for JADE testing purposes
	public FloorPanelAgent() {
		this.floor = 2;
		this.liftList  = new ArrayList<>();
	}
	
	// for the REAL DEAL
	public FloorPanelAgent(int floor) {
		this.floor = floor;
		this.liftList  = new ArrayList<>();
	} 
	 
	public void setup() {
		
		System.out.println(this.toString());
		addBehaviour(new FloorListeningBehaviour(this));
		
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("lift-service");
		template.addServices(sd);
		
		/* Finds all the lifts that have already subscribed to the service and adds them to the liftList */
		try {
			
			DFAgentDescription[] result = DFService.search(this, template);
			
			for(int i = 0; i < result.length; ++i) {
				this.addLiftToList(result[i].getName().getLocalName());
			}
			
		} catch(FIPAException fe) {
			fe.printStackTrace();
		}
		
		System.out.println("FloorPanel" + this.floor + "\n" + getLiftsAvailable() + "\n");
	}
	
	
	public String getLiftsAvailable() {
		String lifts = "Available lifts: ";
		
		for(int i = 0; i < this.liftList.size(); i++) {
			lifts = lifts + this.liftList.get(i) + "; ";
		}
		
		return lifts;
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
	
	public ArrayList<String> getLiftList(){
		return this.liftList;
	}
	
	/*setters*/
	public void addLiftToList(String liftName) {
		if(!this.liftList.contains(liftName)) {
			this.liftList.add(liftName);
		}
	}


}
