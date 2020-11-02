package agents;

import utils.HandleRequest;
import utils.LiftTaskListEntry;

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
    
    /* setters */
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

	public float getTimeAtFloors() {
		return timeAtFloors;
	}

	public void setTimeAtFloors(float timeAtFloors) {
		this.timeAtFloors = timeAtFloors;
	}
    
}
