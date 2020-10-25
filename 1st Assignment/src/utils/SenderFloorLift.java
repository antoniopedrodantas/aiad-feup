package utils;

import agents.FloorPanelAgent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class SenderFloorLift {
	
	private String[] listeners;
	private FloorPanelAgent floorPanelAgent;
	private String type;
	private int floor;
	
	public SenderFloorLift(String[] listeners, FloorPanelAgent floorPanelAgent, String type, Integer floor) { 
		
		this.listeners = listeners;
		this.floorPanelAgent = floorPanelAgent;
		this.type = type;
		sendToLift();
	}
	
	protected void sendToLift() {
		
		for(String listener: this.listeners) {
			 ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
	         msg.addReceiver(new AID(listener,AID.ISLOCALNAME));
	         msg.setContent(type + floor);
	         this.floorPanelAgent.send(msg);
		}
	}
}
