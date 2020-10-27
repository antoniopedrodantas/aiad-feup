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
			
			//Getting all lifts
			ArrayList<String> liftListeners = getLiftListeners();
			
			//Sending message to all lifts
			if(liftListeners.size() != 0) {
				SenderFloorLift senderFloorLift = new SenderFloorLift(liftListeners, myAgent, "Down", myAgent.getFloor());
				senderFloorLift.sendToLift();
			}
		}
		else {
			block();
		}
		
	}
	
	
	protected ArrayList<String> getLiftListeners() {
		
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
			
		ArrayList<String> liftListeners = new ArrayList<>();
		
		sd.setType("lift-service");
		template.addServices(sd);
		
		try {
			DFAgentDescription[] result = DFService.search(myAgent, template);
			for(int i = 0; i < result.length; ++i) {
				liftListeners.add(result[i].getName().getLocalName());
			}
			
		} catch(FIPAException fe) {
			fe.printStackTrace();
		}
		
		return liftListeners;
	}
	
}