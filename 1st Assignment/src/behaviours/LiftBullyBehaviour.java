package behaviours;

import java.util.ArrayList;

import agents.LiftAgent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import utils.HandleRequest;
import utils.LiftProposal;

@SuppressWarnings("serial")
public class LiftBullyBehaviour extends CyclicBehaviour {

	MessageTemplate templatePropose = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
	
	MessageTemplate templateHalt = MessageTemplate.and(
	MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
	MessageTemplate.MatchPerformative(ACLMessage.CANCEL) );
	
	ArrayList<Float> proposalsList = new ArrayList<Float>(); //TODO: change ArrayList to HashMap
	
	private LiftAgent lift;
	  	
	public LiftBullyBehaviour(LiftAgent liftAgent) {
		super();
		this.lift = liftAgent;
	}


	public void action() {
		processProposalMessage();
		//processHaltMessage();
	}
	
	private void processHaltMessage() {
		
		this.lift.addBehaviour(new AchieveREResponder(this.lift, this.templateHalt) {	
			protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
				if (request.getSender().getName().contains("liftAgent")) {
					ACLMessage agree = request.createReply();
					agree.setPerformative(ACLMessage.AGREE);
					return agree;
				}
				else {
					throw new RefuseException("check-failed");
				}
			}
			
			protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException{
					proposalsList.clear(); 
					
					ACLMessage inform = request.createReply();
					inform.setPerformative(ACLMessage.INFORM);
					return inform;
			}
		} );
	}
	
	private void processProposalMessage() {
		//Receiving Proposal Messages
				ACLMessage msg = myAgent.receive(templatePropose);
				if(msg != null) {

					System.out.println("Recieved PROPOSALLLLL : " + msg.getContent());
					proposalsList.add(Float.parseFloat(msg.getContent()));
				} 
				//Processing Proposal Messages

				var myProposal = lift.getCurrentLiftProposal();
				//Only if I have proposal and received all other proposals
				if(proposalsList.size() >= lift.getContacts().size() && myProposal != null) {
					boolean betterProposal = true;
					for (Float proposal : proposalsList) {
						if(myProposal.getTime() > proposal) betterProposal = false;
					}
					if(betterProposal) acceptProposal();
				}
	}
	
	private void handleSameProposal() {
		System.out.println("ok :D");
	}


	private void acceptProposal() {
		//Send halt to all lifts
		 
		if(lift.getContacts().size() != 0) {
			
			ACLMessage msg = new ACLMessage(ACLMessage.CANCEL);
	         
			for(String listener : lift.getContacts()) {
				msg.addReceiver(new AID((String) listener,AID.ISLOCALNAME));
			}
				        
	        lift.send(msg);
	    }
		// TODO Wait for Halt responses
		lift.setTaskList(lift.getCurrentLiftProposal().getTaskList());
		
	}

}
