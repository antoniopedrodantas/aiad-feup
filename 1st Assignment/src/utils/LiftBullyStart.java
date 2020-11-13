package utils;

import agents.LiftAgent;
import jade.core.AID;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

public class LiftBullyStart {

	LiftAgent lift;

	public LiftBullyStart(LiftAgent lift) {
		this.lift = lift;
	}

	/*
	 * Sends proposed time to all lifts in contacts list.
	 */
	public void sendBullyProposal(LiftProposal liftProposal) {
		
		System.out.println("SENT PROPOSAL: " + liftProposal.getTime());
		
		if(lift.getContacts().size() != 0) {
			
			ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
	         
			for(String listener : lift.getContacts()) {
				msg.addReceiver(new AID((String) listener,AID.ISLOCALNAME));
			}
				        
	        msg.setContent(Float.toString(liftProposal.getTime()));
	        lift.send(msg);
	    }
	}
}
