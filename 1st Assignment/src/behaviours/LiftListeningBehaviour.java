package behaviours;

import jade.core.behaviours.CyclicBehaviour;

import jade.lang.acl.ACLMessage;
import utils.LiftTaskListEntry;

import java.util.ArrayList;


import agents.LiftAgent;

@SuppressWarnings("serial")
public class LiftListeningBehaviour extends CyclicBehaviour {
	
	private LiftAgent myAgent;
	private ArrayList<LiftTaskListEntry> taskList = new ArrayList<>();
	
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
	protected float calcTime(int request) {
		
		float liftSpeed = this.myAgent.getSpeed();
		
		if(this.taskList.isEmpty())
			return Math.abs(this.myAgent.getFloor() - Math.abs(request)) * liftSpeed;
		else {
			float time = Math.abs(this.myAgent.getFloor() - taskList.get(0).getFloor()) * liftSpeed;
			for (int i = 0; i < taskList.size() - 1; i++) {
				time += taskList.get(i).timeTo(taskList.get(i+1), liftSpeed);
			}
			return time;	
		}
	}
	
	//Gets the list position for a new request
	protected int getListPos(LiftTaskListEntry entry) {
		
		return 0;
	}
}

