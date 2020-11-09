package agents;

import behaviours.LiftTickerBehaviour;
import utils.HandleRequest;
import utils.LiftTaskListEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import jade.core.AID;
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
import jade.proto.AchieveREInitiator;
import jade.proto.AchieveREResponder;


@SuppressWarnings("serial")
public class LiftAgent extends Agent{
	
	private int id;
	private float maxWeight;
	private float speed;
	private int totalLifts;
	private float floorDistance;
	private float timeAtFloors;
	
	private int currentFloor;
	private float currentWeight;
	private ArrayList<LiftTaskListEntry> taskList = new ArrayList<>();
	private ArrayList<String> liftContacts;
	
	public LiftAgent() {
      	
		this.id = 1;
		this.maxWeight = 600;
		this.speed = 2;
		this.totalLifts = 6;
		this.floorDistance = 5;
        this.setTimeAtFloors(1);
		
        this.currentFloor = 0;
        this.currentWeight = 0;
        
	}
	
	public LiftAgent(String[] args) {
		
		this.id = Integer.parseInt(args[0]);
        this.maxWeight = Float.parseFloat(args[1]);
        this.speed = Float.parseFloat(args[2]);
        this.totalLifts = Integer.parseInt(args[3]);
        this.floorDistance = Float.parseFloat(args[4]);
        this.setTimeAtFloors(Float.parseFloat(args[5]));
        
        this.currentFloor = 0;
        this.currentWeight = 0;
        
        this.liftContacts = new ArrayList<>();
          
	}
	
	public void setup() { 
		
		System.out.println(this.toString());
		
		
		/* DF service register */
		
		DFAgentDescription df = new DFAgentDescription();
		df.setName(getAID());
		
		ServiceDescription sd = new ServiceDescription();
		sd.setType("lift-service");
		sd.setName(getLocalName());
		
		df.addServices(sd);
		
		try {
			DFService.register(this, df);
			System.out.println("Register done successfully");
		} catch(FIPAException fe) {
			fe.printStackTrace();
		}
		
		addLiftListener();
		this.addBehaviour(new LiftTickerBehaviour(this, 1000)); //add TickerBehaviour to update Lift's position
	}
	
    public void takeDown() {
    	
    	try {
			DFService.deregister(this);
			System.out.println("Deregister done successfully");
			System.out.println(getLocalName() + ": done working.");
		} catch(FIPAException fe) {
			fe.printStackTrace();
		}
    }
    
    
    /* AchieveREResponder */
	protected void addLiftListener() {
		
		System.out.println("Agent "+ getLocalName()+ " waiting for requests...");
		
	  	MessageTemplate template = MessageTemplate.and(
  		MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
  		MessageTemplate.MatchPerformative(ACLMessage.REQUEST) );
  		
	  	
		addBehaviour(new AchieveREResponder(this, template) {
			
			protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
				System.out.println("Agent "+ getLocalName() + ": REQUEST received from "+ request.getSender().getLocalName() + ". Action is "+ request.getContent());
				
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
				
					System.out.println("Agent " + getLocalName() + ": Action successfully performed");
					
					HandleRequest handleRequest = new HandleRequest(myAgent, request.getContent());
					handleRequest.processRequest();
					
					ACLMessage inform = request.createReply();
					inform.setPerformative(ACLMessage.INFORM);
					return inform;
			}
		} );
	 }
	
	public void askRequestAgent() {
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(new AID("requestAgent",AID.ISLOCALNAME));
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        msg.setReplyByDate(new Date(System.currentTimeMillis() + 5000));
        msg.setContent(this.getTaskList().get(0).getFloor() + ":" + this.getTaskList().get(0).getType()); //msg = 4:Up
        
        addBehaviour(new AchieveREInitiator(this, msg) {
	    	 
			protected void handleInform(ACLMessage inform) {
				System.out.println("Agent " + inform.getSender().getLocalName() + " successfully performed the requested action");
				System.out.println("RECEIVED MESSAGE FROM REQUESTAGENT: " + inform.getContent());
				//TODO: parse message received
				parseReceivedMessage(inform.getContent());
			}
			protected void handleRefuse(ACLMessage refuse) {
				System.out.println("Agent " + refuse.getSender().getLocalName() + " refused to perform the requested action");
				
			}
			protected void handleFailure(ACLMessage failure) {
				if (failure.getSender().equals(myAgent.getAMS())) {
					System.out.println("Responder does not exist");
				}
				else {
					System.out.println("Agent " + failure.getSender().getLocalName() + " failed to perform the requested action");
				}
			}
		} );
	}
	
	/* Parsing functions */
	//TODO: quando saiem todas as pessoas retirar todos as entry do tipo END
	//TODO: acrescentar entrys do tipo END quando entram pessoas
	protected void parseReceivedMessage(String msg) {
		
		String[] actionsToBePerformed = msg.split(",", 2);
		
		if(actionsToBePerformed.length == 2) { //entering and exiting
			parseExiting(actionsToBePerformed[1]);
			parseEntering(actionsToBePerformed[0]);
		}
		else if(actionsToBePerformed.length == 1) { //entering or exiting
			
			if(actionsToBePerformed[0].substring(0,1).equals("E")) {
				parseEntering(actionsToBePerformed[0]);
			}
			else if(actionsToBePerformed[0].substring(0,1).equals("S")) {
				parseExiting(actionsToBePerformed[0]);
			}
			else {
				System.out.println("No valide format for message: " + msg);
			}
		}
		else {
			System.out.println("No valide format for message: " + msg);
		}
	}
	
	protected void parseEntering(String msg) {
		
		String[] entering = msg.split(":", 2);
		
		if(entering.length == 2) {
			String enter = entering[1];
			
			if(!enter.contains("[")) {
				int people = Integer.parseInt(enter);
				System.out.println("entering: " + people);
				//only update current weight
			}
			else {
				if(enter.contains("[") && enter.contains("]")) {
					int initIndex = enter.indexOf('[');
					int finalIndex = enter.indexOf(']');
					int i = 0;
					String nmr = "";
					String floorsToAttend ="";
					
					if(i == initIndex || initIndex== finalIndex - 1) {
						System.out.println("No valide format for message: " + msg);
					}
					else {
						while(i < initIndex) {
							nmr += enter.charAt(i);
							i = i+1;
						}
						while(initIndex < finalIndex - 1) {
							initIndex = initIndex + 1;
							floorsToAttend += enter.charAt(initIndex);
						}
					}
					
					int people = Integer.parseInt(nmr);
					String[] floors = floorsToAttend.split("-");
					//update current weight and add End Entrys
					System.out.println("entering: " + people);
                    for(int j = 0; j < floors.length; j++){
                        System.out.println(floors[j]);
                    }
				}
				else {
					System.out.println("No valide format for message: " + msg);
				}
			}
			
		}
		else {
			System.out.println("No valide format for message: " + msg);
		}
	}
	
	protected void parseExiting(String msg) {
		String[] exiting = msg.split(":", 2);
		
		if(exiting.length == 2) {
			int people = Integer.parseInt(exiting[1]);
			System.out.println("exiting: " + people);
		}
		else {
			System.out.println("No valide format for message: " + msg);
		}
	}
	
	protected boolean checkSender(String name) {
		return name.contains("floorPanelAgent") ? true : false;
	}
    
    // Lift's direction
    // 0 -> static
	// -1 -> down 
	// 1 -> up 
    public int calculateMyDirection() {
		if(this.taskList.size() == 0) {
			return 0;
		}
		else if(this.currentFloor > this.taskList.get(0).getFloor()) {
    		return -1;
    	}
		else if (this.currentFloor < this.taskList.get(0).getFloor()){
			return 1;
		}
		else {
			
			// [ATTENTION] May be lacking if cases
			
			if(this.taskList.size() == 1) {
				return 0;
			}
			else if(this.taskList.get(0).getFloor() < this.taskList.get(1).getFloor()) {
				return 1;
			}
			else {
				return -1;
			}

		}
    }
    
    @Override
    public String toString() {
        return "Lift ID: " + this.id + "\n" +  "Max weight: " + this.maxWeight + "\n" + "Max speed: " + this.speed + "\n";
    }
    
    /* getters */
    public int getId() {
    	return this.id;
    }
    
    public float getWeight() {
    	return this.maxWeight;
    }
    
    public float getSpeed() {
    	return this.speed;
    }
    
    public int getFloor() {
    	return this.currentFloor;
    }
    
    public float getCurrentWeight() {
    	return this.currentWeight;
    }
    
    
    public ArrayList<LiftTaskListEntry> getTaskList() {
		return taskList;
	}

	public int getTotalLifts() {
    	return this.totalLifts;
    }
    
    public ArrayList<String> getContacts(){
    	return this.liftContacts;
    }
    
    public float getTimeAtFloors() {
    	return timeAtFloors;
    }
    
    /* setters */
    public void setFloor(int floor) {
    	this.currentFloor = floor;
    }
    public void setContacts(ArrayList<String> contacts) {
    	this.liftContacts = contacts;
    }
    
    
	public void setTaskList(ArrayList<LiftTaskListEntry> taskList) {
		this.taskList = taskList;
	}

	public float getFloorDistance() {
		return floorDistance;
	}

	public void setFloorDistance(float floorDistance) {
		this.floorDistance = floorDistance;
	}


	public void setTimeAtFloors(float timeAtFloors) {
		this.timeAtFloors = timeAtFloors;
	}
	
	public void addWeight(float weight) {
		this.currentWeight = this.currentWeight + weight;
	}
	
	public void subWeight(float weight) {
		this.currentWeight = this.currentWeight - weight;
	}
	
	public void removeEntry() {
		this.taskList.remove(0);
	}
}
