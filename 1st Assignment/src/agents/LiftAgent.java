package agents;

import behaviours.LiftListeningBehaviour;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.util.leap.ArrayList;


@SuppressWarnings("serial")
public class LiftAgent extends Agent{
	
	private int id;
	private float maxWeight;
	private float speed;
	
	private int currentFloor;
	private float currentWeight;
	private int[] taskList;
	
	// for JADE testing purposes
	public LiftAgent() {
      	
		this.id = 1;
		this.maxWeight = 600;
		this.speed = 25; 
		
        
        this.currentFloor = 0;
        this.currentWeight = 0;
        this.taskList = new int[5];
        
	}
	
	// for the REAL deal
	public LiftAgent(String[] args) {
		
		this.id = Integer.parseInt(args[0]);
        this.maxWeight = Float.parseFloat(args[1]);
        this.speed = Float.parseFloat(args[2]);
        
        this.currentFloor = 0;
        this.currentWeight = 0;
        this.taskList = new int[5];
          
	}
	
	public void setup() { 
		
		System.out.println(this.toString());
		addBehaviour(new LiftListeningBehaviour(this));
		
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
    
    // Calculates Lift's direction
    // 0 -> static || -1 -> down || 1 -> up 
    public int calculateMyDirection() {
		if(this.taskList.length == 0) {
			return 0;
		}
		else if(this.currentFloor > this.taskList[0]) {
    		return -1;
    	}
		else if (this.currentFloor < this.taskList[0]){
			return 1;
		}
		else {
			
			// [ATTENTION] May be lacking if cases
			
			if(this.taskList.length == 1) {
				return 0;
			}
			else if(this.taskList[0] < this.taskList[1]) {
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
    
    /*getters*/
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
    
    public int[] getTaskList() {
    	return this.taskList;
    }
    
    
}
