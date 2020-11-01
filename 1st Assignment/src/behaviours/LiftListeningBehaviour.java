package behaviours;

import jade.core.behaviours.CyclicBehaviour;

import jade.lang.acl.ACLMessage;
import utils.LiftTaskListEntry;
import utils.liftProposal;

import java.util.ArrayList;


import agents.LiftAgent;

@SuppressWarnings("serial")
public class LiftListeningBehaviour extends CyclicBehaviour {
	
	private LiftAgent myAgent;
	
	public LiftListeningBehaviour(LiftAgent liftAgent) {
		this.myAgent = liftAgent;
	}

	public void action() {

		ACLMessage msg = myAgent.receive();
		if(msg != null) {
			
			
			ACLMessage reply = msg.createReply();
			reply.setPerformative(ACLMessage.INFORM);
			reply.setContent(myAgent.getAID().getLocalName() + ": Got your message!");
			myAgent.send(reply);
			
			// checks FloorPanelRequest
			handleFLoorPanelRequest(msg);
			
		}
		else {
			block();
		}
	}
	
	protected void handleFLoorPanelRequest(ACLMessage msg) {
		
		// checks if it is Up or Down
		int message = Integer.parseInt(msg.getContent());
		
		if(message <= 0) {
			// needs to go down
			if(myAgent.calculateMyDirection() <= 0 && myAgent.getFloor() >= Math.abs(message)) {
				// can attend, calculate time to get there
				System.out.println(myAgent.getAID().getLocalName() + " I can attend and will go Down.");
			}
		}
		else {
			// needs to go up
			if(myAgent.calculateMyDirection() >= 0 && myAgent.getFloor() <= Math.abs(message)) {
				// can attend, calculate time to get there
				System.out.println(myAgent.getAID().getLocalName() + " I can attend and will go Up.");
			}
		}
		
	}
	
	//Calculates time for a new request
	// lift @ 5 taks[4,3,2,1] 
	// calc time calculates 5-4, 4-3, 3-2,2-1 => returns sum
	protected liftProposal calcProposal(int request) {
		
		float liftSpeed = this.myAgent.getSpeed(Math.abs(request), );

		LiftTaskListEntry entry = new LiftTaskListEntry(Math.abs(request), request > 0 ? 1 : -1);
		float time;
		
		int optimalPosition = getListPos(entry);
		
		
		if(this.myAgent.getTaskList().isEmpty())
			time = Math.abs(this.myAgent.getFloor() - Math.abs(request)) * liftSpeed;
		else {
			getListPos(entry);
			time = Math.abs(this.myAgent.getFloor() - myAgent.getTaskList().get(0).getFloor()) * liftSpeed;
			for (int i = 0; i < myAgent.getTaskList().size() - 1; i++) {
				time += myAgent.getTaskList().get(i).timeTo(myAgent.getTaskList().get(i+1), liftSpeed);
			}
		}
		return new liftProposal(this.myAgent.getTaskList(), entry, optimalPosition, time);	
	}
	
	//Gets the list position for a new request
	protected int getListPos(LiftTaskListEntry entry) {	
		
		//For all list pairs
		for (int i = 0; i < myAgent.getTaskList().size()-1; i++) {
			
			//If request is between
			if(between(myAgent.getTaskList().get(i).getFloor(), 
						myAgent.getTaskList().get(i+1).getFloor(), 
						entry.getFloor())) {
				if(entry.getType() == LiftTaskListEntry.Type.Up) return i;
			}
			
			if(between(myAgent.getTaskList().get(i+1).getFloor(), 
					myAgent.getTaskList().get(i).getFloor(), 
					entry.getFloor())) {
				if(entry.getType() == LiftTaskListEntry.Type.Down) return i;
			}
		}
		return myAgent.getTaskList().size();
	}
	
	//is X between a and b
	private boolean between(int a, int b, int x) {
		return ((a < x && x < b));
	}
}

