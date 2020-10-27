package behaviours;

import jade.core.behaviours.CyclicBehaviour;

import jade.lang.acl.ACLMessage;

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
			
			// prints received message
			//System.out.println(msg);
			
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
	
}

