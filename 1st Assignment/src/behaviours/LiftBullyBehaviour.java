package behaviours;

import agents.LiftAgent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import utils.HandleRequest;

public class LiftBullyBehaviour extends CyclicBehaviour {

	private LiftAgent lift;
	
	public LiftBullyBehaviour(LiftAgent lift) {
		this.lift = lift;
	}
	
	
	@Override
	public void action() {
		
		reachConsensus();
		
	}
	
	private void reachConsensus() {
	  	
		
		
	}

}
