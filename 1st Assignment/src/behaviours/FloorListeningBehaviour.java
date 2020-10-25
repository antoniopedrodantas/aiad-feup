package behaviours;

import jade.core.behaviours.CyclicBehaviour;

import jade.lang.acl.ACLMessage;
import agents.FloorPanelAgent;

@SuppressWarnings("serial")
public class FloorListeningBehaviour extends CyclicBehaviour {
	
	private FloorPanelAgent myAgent;
	
	public FloorListeningBehaviour(FloorPanelAgent floorPanelAgent) {
		this.myAgent = floorPanelAgent;
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