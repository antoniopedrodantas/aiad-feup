package agents;

import jade.core.Agent;

@SuppressWarnings("serial")
public class BuildingAgent extends Agent{
	
	private int nmrFloors;
	private int nmrLifts;
	private float maxWeight;
	private float maxSpeed;
	private float floorDistance;
	
	public BuildingAgent(String[] args){
        this.nmrFloors = Integer.parseInt(args[0]);
        this.nmrLifts = Integer.parseInt(args[1]);
        this.maxWeight = Float.parseFloat(args[2]);
        this.maxSpeed = Float.parseFloat(args[3]);
        this.floorDistance = Float.parseFloat(args[4]);
    }
	
	public void setup() {
		System.out.println(getLocalName() + ": started working.\n");
		System.out.println(this.toString());
	}
	
	public void takedown() {
		System.out.println(getLocalName() + ": done working.");
	}
	
	@Override
    public String toString() {
        return "INFORMATION\n" + "Agent: " + getLocalName()+"\n" + "Floors: " + this.nmrFloors + "\n" + "Lifts: " + this.nmrLifts + "\n" + "Max weight: " + this.maxWeight + "\n" + "Max speed: " + this.maxSpeed + "\n" + "Floor distance: " + this.floorDistance + "\n";
    }
	
	/*getters*/
	private int getFloors() {
		return this.nmrFloors;
	}
	
	private int getLifts() {
		return this.nmrLifts;
	}
	
	private float getWeight() {
		return this.maxWeight;
	}
	
	private float getSpeed() {
		return this.maxSpeed;
	}
	
	private float getDistance() {
		return this.floorDistance;
	}
}
