package behaviours;

import agents.FloorPanelAgent;
import utils.SenderFloorLift;

import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class FloorListeningBehaviour extends CyclicBehaviour {
	
	private FloorPanelAgent myAgent;
	
	public FloorListeningBehaviour(FloorPanelAgent floorPanelAgent) {
		this.myAgent = floorPanelAgent;
	}

	public void action() {

		ACLMessage msg = myAgent.receive();
		
		if(msg != null && msg.getPerformative() != ACLMessage.INFORM) {
			
			System.out.println(msg);
			ACLMessage reply = msg.createReply();
			reply.setPerformative(ACLMessage.INFORM);
			reply.setContent(myAgent.getAID().getLocalName() + ": Got your message!");
			myAgent.send(reply);
			//TODO: change body of message in line 40 to content in send floor lift
			/* Obtaining all lifts in the building */
			ArrayList<String> liftListeners = this.myAgent.getLiftList();
			
			/* Sending request to all lifts */
			if(liftListeners.size() != 0) {
				SenderFloorLift senderFloorLift = new SenderFloorLift(liftListeners, myAgent, "Down", myAgent.getFloor());
				senderFloorLift.sendToLift();
			}
		}
		else {
			block();
		}
		
	}

}