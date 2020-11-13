package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import agents.LiftAgent;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.util.leap.HashMap;

public class LiftBullyConsensus {

	private LiftAgent lift;
	// TODO: change ArrayList to HashMap<String, ArrayList<Floar>>s
	private ArrayList<Float> proposalsList;
	
	public LiftBullyConsensus(LiftAgent lift) {
		this.lift = lift;
		this.proposalsList = new ArrayList<Float>();
		
		bullyListener();
	}
	
	public void bullySender() {
		// TODO : send Proposal to other lift's and add Proposal ID
	}
	
	public void bullyListener() {
		
	  	MessageTemplate template = MessageTemplate.and(
  		MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
  		MessageTemplate.MatchPerformative(ACLMessage.PROPOSE) );
  		
	  	
//		lift.addBehaviour(new AchieveREResponder(lift, template) {
//			
//			if(this.proposalsList.size() == this.lift.getContacts().size()) {
//				// TODO: starts bully algorithm
//			}
//			else {
//				// adds waits for more proposals
//			}
//			
//			/*
//			protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
//				if (checkSender(request.getSender().getName())) {
//					
//					ACLMessage agree = request.createReply();
//					agree.setPerformative(ACLMessage.AGREE);
//					return agree;
//					
//				}
//				else {
//					throw new RefuseException("check-failed");
//				}
//			}
//			
//			protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException{
//					// if has all proposals	
//					HandleRequest handleRequest = new HandleRequest(myAgent, request.getContent());
//					handleRequest.processRequest();
//					
//					ACLMessage inform = request.createReply();
//					inform.setPerformative(ACLMessage.INFORM);
//					return inform;
//			}
//			*/
//			
//		});
	}
	
}
