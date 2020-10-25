package agents;

import jade.core.Agent;

@SuppressWarnings("serial")
public class FloorPanelAgent extends Agent {
	
	private int floor;
	
	public FloorPanelAgent(int floor) {
		this.floor = floor;
	}
	 
	public void setup() {
		System.out.println(this.toString());
	}
	
	public void takedown() {
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

}
