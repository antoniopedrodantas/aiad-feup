package agents;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import jade.core.AID;
import sajas.core.Agent;
import sajas.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import sajas.proto.AchieveREInitiator;
import sajas.proto.AchieveREResponder;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;


@SuppressWarnings("serial")
public class FloorPanelAgent extends Agent implements Drawable {
	
	private int floor;
	private int expectedSize;
	private ArrayList<String> liftList;
	private String type;
	private int nmrResponders;
	
	// for JADE testing purposes
	public FloorPanelAgent() {
		this.floor = 2;
		this.liftList  = new ArrayList<>();
	}
	
	// for the REAL DEAL
	public FloorPanelAgent(int floor, int nmrLifts) {
		this.floor = floor;
		this.expectedSize = nmrLifts;
		this.liftList  = new ArrayList<>();
		this.type = "Down"; //TODO: change this
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
		
		System.out.println("FloorPanel" + this.floor + "\n" + getLiftsAvailable());
		
		addFloorListener(); //adds AchieveREResponder
	}
	
	/* AchieveREResponder */
	protected void addFloorListener() {
		
		System.out.println("Agent "+ getLocalName()+ " waiting for requests...\n");
		
	  	MessageTemplate template = MessageTemplate.and(
  		MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
  		MessageTemplate.MatchPerformative(ACLMessage.REQUEST) );
  		
	  	
		addBehaviour(new AchieveREResponder(this, template) {
			protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
				
				// System.out.println("Agent " + getLocalName() + ": REQUEST received from " + request.getSender().getLocalName() + ". Action is "+ request.getContent());
				// System.out.println(getLocalName() + ": REQUEST received! Action is "+ request.getContent());
				type = request.getContent();
				
				if (checkSender(request.getSender().getName())) {
					
					// System.out.println("Agent " + getLocalName() + ": Agree");
					ACLMessage agree = request.createReply();
					agree.setPerformative(ACLMessage.AGREE);
					return agree;
					
				}
				else {
					// System.out.println("Agent " + getLocalName()+ ": Refuse");
					throw new RefuseException("check-failed");
				}
			}
			
			protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException{
				
				if(sendRequestToLifts()) {
					// System.out.println("Agent " + getLocalName() + ": Action successfully performed");
					ACLMessage inform = request.createReply();
					inform.setPerformative(ACLMessage.INFORM);
					return inform;
				}
				else {
					// System.out.println("Agent " + getLocalName() + ": Action failed");
					throw new FailureException("unexpected-error");
				}
			}
		} );
	 }

	protected boolean sendRequestToLifts() {
		
		System.out.println("Size of liftList: " + this.liftList.size());
		System.out.println("Expected " + this.expectedSize);
		if(this.liftList.size() != this.expectedSize) {
			updateLiftList();
			System.out.println("UPDATE: FloorPanel" + this.floor + "\n" + getLiftsAvailable());
		}
		
		if(this.liftList.size() != 0 && this.liftList.size() == this.expectedSize) {
			
				this.nmrResponders = this.liftList.size();
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		         
				for(String listener : this.liftList) {
					msg.addReceiver(new sajas.core.AID((String) listener,AID.ISLOCALNAME));
				}
				
				
				msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		        msg.setReplyByDate(new Date(System.currentTimeMillis() + 5000));
		        
		        switch(this.type) {
		     	case "Down":
		     		msg.setContent(Integer.toString(-1 * this.floor));
		     		break;
		     	case "Up":
		     		msg.setContent(Integer.toString(this.floor));
		     		break;
		     	default:
		     		break;
		     }
		           
		     addBehaviour(new AchieveREInitiator(this, msg) {
		    	 
				protected void handleInform(ACLMessage inform) {
					// System.out.println("Agent " + inform.getSender().getLocalName() + " successfully performed the requested action");
				}
				protected void handleRefuse(ACLMessage refuse) {
					// System.out.println("Agent " + refuse.getSender().getLocalName() + " refused to perform the requested action");
					nmrResponders--;
				}
				protected void handleFailure(ACLMessage failure) {
					if (failure.getSender().equals(myAgent.getAMS())) {
						// System.out.println("Responder does not exist");
					}
					else {
						// System.out.println("Agent " + failure.getSender().getLocalName() + " failed to perform the requested action");
					}
				}
				protected void handleAllResultNotifications(Vector notifications) {
					if (notifications.size() < nmrResponders) {
						//System.out.println("Timeout expired: missing " + (nmrResponders - notifications.size()) + " responses");
					}
				}
			} );
		     
		     return true;
		}
		else {
			return false;
		}
	}
	
	protected void updateLiftList() {
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

	@Override
	public void draw(SimGraphics g) {
		// TODO Auto-generated method stub
		g.drawRectBorder(new BasicStroke(), Color.GREEN);
	}

	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return this.floor;
	}


}
