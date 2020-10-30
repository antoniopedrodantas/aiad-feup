package agents;

import java.util.ArrayList;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import utils.SenderFloorLift;



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
		
		addListener(); //adds AchieveREResponder
	}
	
	/* AchieveREResponder */
	protected void addListener() {
		
		System.out.println("Agent "+ getLocalName()+ " waiting for requests...");
		
	  	MessageTemplate template = MessageTemplate.and(
  		MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
  		MessageTemplate.MatchPerformative(ACLMessage.REQUEST) );
  		
		addBehaviour(new AchieveREResponder(this, template) {
			protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
				
				System.out.println("Agent "+ getLocalName() + ": REQUEST received from "+ request.getSender().getName() + ". Action is "+ request.getContent());
				
				if (checkSender(request.getSender().getName())) {
					
					System.out.println("Agent " + getLocalName() + ": Agree");
					ACLMessage agree = request.createReply();
					agree.setPerformative(ACLMessage.AGREE);
					return agree;
					
				}
				else {
					System.out.println("Agent "+ getLocalName()+ ": Refuse");
					throw new RefuseException("check-failed");
				}
			}
			
			protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException{
				
				if(sendRequestToLifts()) {
					System.out.println("Agent "+ getLocalName() + ": Action successfully performed");
					ACLMessage inform = request.createReply();
					inform.setPerformative(ACLMessage.INFORM);
					return inform;
				}
				else {
					System.out.println("Agent "+ getLocalName() + ": Action failed");
					throw new FailureException("unexpected-error");
				}
			}
		} );
	 }

	protected boolean sendRequestToLifts() {
		
		if(this.liftList.size() != 0) {
			SenderFloorLift senderFloorLift = new SenderFloorLift(this.liftList, this, "Down", this.floor);
			if(senderFloorLift.sendToLift()) {
				return true;
			}
			else {
				return false;
			}
		}
		
		return false;
	}
	
	
	protected boolean checkSender(String name) {
		return name.contains("requestAgent") ? true : false;
	}
	
	/* utils */
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
