package agents;

import behaviours.FloorListeningBehaviour;
import utils.LiftTaskListEntry;

import java.util.ArrayList;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;


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
		//addBehaviour(new DFSubscriptionInit(this, template));
		
		
		try {
			DFAgentDescription[] result = DFService.search(this, template);
			for(int i = 0; i < result.length; ++i) {
				System.out.println("Found " + result[i].getName());
			}
		} catch(FIPAException fe) {
			fe.printStackTrace();
		}
	}
	
	
	public String getLiftsAvailable() {
		String lifts = new String();
		
		for(int i = 0; i < this.liftList.size(); i++) {
			lifts = lifts + this.liftList.get(i) + ";";
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
	
	/*setters*/
	public void addLiftToList(String liftName) {
		if(!this.liftList.contains(liftName)) {
			this.liftList.add(liftName);
		}
	}
/*	
	
class DFSubscriptionInit extends SubscriptionInitiator {
		
		DFSubscriptionInit(Agent agent, DFAgentDescription dfad) {
			super(agent, DFService.createSubscriptionMessage(agent, getDefaultDF(), dfad, null));
		}
		
		protected void handleInform(ACLMessage inform) {
			try {
				DFAgentDescription[] dfds = DFService.decodeNotification(inform.getContent());
				
				for(int i=0; i<dfds.length; i++) {
					AID agent = dfds[i].getName();
					System.out.println("New agent in town: " + agent.getLocalName());
				}
			} catch (FIPAException fe) {
				fe.printStackTrace();
			}
		}
		
	}*/

}
