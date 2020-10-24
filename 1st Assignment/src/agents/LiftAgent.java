package agents;

import jade.core.Agent;

@SuppressWarnings("serial")
public class LiftAgent extends Agent{
	
	private int id;
	private float maxWeight;
	private float speed;
	
	private int currentFloor;
	private float currentWeight;
	private int[] taskList;
	
	public LiftAgent(String[] args) {
		this.id = Integer.parseInt(args[0]);
        this.maxWeight = Float.parseFloat(args[1]);
        this.speed = Float.parseFloat(args[2]);
        
        this.currentFloor = 0;
        this.currentWeight = 0;
        this.taskList = new int[5];
	}
	
	public void setup() {
		System.out.println("Hey, " + this.getLocalName() + " here\n");
		System.out.println(this.toString());
	}
	
    public void takeDown() {
        System.out.println(getLocalName() + ": done working.");
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
