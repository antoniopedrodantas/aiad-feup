package agents;
import agents.LiftAgent;

import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

@SuppressWarnings("serial")
public class BuildingAgent extends Agent{
	
	private int nmrFloors;
	private int nmrLifts;
	private float maxWeight;
	private float maxSpeed;
	private float floorDistance;
	private ContainerController mainContainer;
	
	public BuildingAgent(String[] args, ContainerController mainContainer){
        this.nmrFloors = Integer.parseInt(args[0]);
        this.nmrLifts = Integer.parseInt(args[1]);
        this.maxWeight = Float.parseFloat(args[2]);
        this.maxSpeed = Float.parseFloat(args[3]);
        this.floorDistance = Float.parseFloat(args[4]);
        this.mainContainer = mainContainer;
    }
	
	public void setup() {
		System.out.println(getLocalName() + ": started working.\n");
		System.out.println(this.toString());
		launchLiftAgents(this.nmrLifts);
	}
	
	public void takedown() {
		System.out.println(getLocalName() + ": done working.");
	}
	
	protected void launchLiftAgents(Integer nmrLifts) {
		
		int lift;
		for(lift = 1; lift <= nmrLifts; lift++) {
			createLiftAgent(lift);
		}
	}
	
	protected void createLiftAgent(Integer lift) {
		
		AgentController liftAgent;
		
		
		try {
			liftAgent = this.mainContainer.acceptNewAgent("liftAgent" + lift, new LiftAgent(lift));
			liftAgent.start();
		} catch(StaleProxyException e) {
			System.err.println("Error launching liftAgent");
			e.printStackTrace();
		}
	}
	
	@Override
    public String toString() {
        return "INFORMATION\n" + "Agent: " + getLocalName()+"\n" + "Floors: " + this.nmrFloors + "\n" + "Lifts: " + this.nmrLifts + "\n" + "Max weight: " + this.maxWeight + "\n" + "Max speed: " + this.maxSpeed + "\n" + "Floor distance: " + this.floorDistance + "\n";
    }
	
	/*getters*/
	public int getFloors() {
		return this.nmrFloors;
	}
	
	public int getLifts() {
		return this.nmrLifts;
	}
	
	public float getWeight() {
		return this.maxWeight;
	}
	
	public float getSpeed() {
		return this.maxSpeed;
	}
	
	public float getDistance() {
		return this.floorDistance;
	}
	
	public ContainerController getController() {
		return this.mainContainer;
	}
}
