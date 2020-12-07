package utils;

import agents.LiftAgent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class LiftBullyStart {

	LiftAgent lift;

	public LiftBullyStart(LiftAgent lift) {
		this.lift = lift;
	}

	public void sendBullyProposal(LiftProposal liftProposal) {
		
		System.out.println("SENT PROPOSAL: " + liftProposal.getTime());
		
		if(lift.getContacts().size() != 0) {
			
			ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
	         
			for(String listener : lift.getContacts()) {
				msg.addReceiver(new sajas.core.AID((String) listener,AID.ISLOCALNAME));
				if(lift.getConsensusNetwork() != null)
					lift.getConsensusNetwork().addEdge("liftAgent" + Integer.toString(lift.getId()), listener);
				System.out.println("PRINT LISTENER  " + listener);
			}
				        
	        msg.setContent(Float.toString(liftProposal.getTime()) + ":" + lift.getId());
	        lift.send(msg);
	    }
	}
}
