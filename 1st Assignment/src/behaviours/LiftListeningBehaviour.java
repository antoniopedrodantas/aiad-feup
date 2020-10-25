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
			System.out.println(msg);
			ACLMessage reply = msg.createReply();
			reply.setPerformative(ACLMessage.INFORM);
			reply.setContent(myAgent.getAID().getLocalName() + ": Got your message!");
			myAgent.send(reply);
		}
		else {
			block();
		}
	}
	
}

