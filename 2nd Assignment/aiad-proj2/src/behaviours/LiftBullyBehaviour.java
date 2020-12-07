package behaviours;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import agents.LiftAgent;
import jade.core.AID;
import sajas.core.behaviours.CyclicBehaviour;
import sajas.core.behaviours.ReceiverBehaviour;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import sajas.proto.AchieveREResponder;
import utils.HandleRequest;
import utils.LiftProposal;
import utils.LiftTaskListEntry.Type;

@SuppressWarnings("serial")
public class LiftBullyBehaviour extends CyclicBehaviour {

	MessageTemplate templatePropose = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
	MessageTemplate templateHalt = MessageTemplate.MatchPerformative(ACLMessage.CANCEL);
	MessageTemplate templateAgree = MessageTemplate.MatchPerformative(ACLMessage.AGREE);
	
    private ReceiverBehaviour proposeMessageReceiver;
    private ReceiverBehaviour haltMessageReceiver;
    private ReceiverBehaviour agreeMessageReceiver;
	
	private HashMap<Integer,Float> proposalsList = new HashMap<>(); //TODO: change ArrayList to HashMap
	
	private ArrayList<String> haltReceivers = new ArrayList<String>();
	
	private LiftAgent lift;
	
	private boolean accepted_request = false;
	
	public LiftBullyBehaviour(LiftAgent liftAgent) {
		super();
		this.lift = liftAgent;
		
		this.proposeMessageReceiver = new ReceiverBehaviour(lift, -1, templatePropose); 
	    this.lift.addBehaviour(this.proposeMessageReceiver);
	    
	    this.haltMessageReceiver = new ReceiverBehaviour(lift, -1, templateHalt); 
	    this.lift.addBehaviour(this.haltMessageReceiver);
	    
	    this.agreeMessageReceiver = new ReceiverBehaviour(lift, -1, templateAgree); 
	    this.lift.addBehaviour(this.agreeMessageReceiver);
	}


	public void action() {
		ACLMessage msg;
	    
		if (accepted_request) {
			try {    
	    		msg = agreeMessageReceiver.getMessage();
	    		agreeMessageReceiver.reset();
	    		this.lift.addBehaviour(agreeMessageReceiver);
	        } 
	        catch (ReceiverBehaviour.TimedOut | ReceiverBehaviour.NotYetReady exception) {
	        	msg = null;
	        } 
	    	processAgreeMessage(msg);
		}else {
			try {    
	    		msg = proposeMessageReceiver.getMessage();
	    		this.proposeMessageReceiver.reset();
	    		this.lift.addBehaviour(proposeMessageReceiver);
	        } 
	    	catch (ReceiverBehaviour.TimedOut | ReceiverBehaviour.NotYetReady exception) {
	        	msg = null;
	        } 
	    	processProposalMessage(msg);
	    	
	    	try {    
	    		msg = haltMessageReceiver.getMessage();
	    		haltMessageReceiver.reset();
	    		this.lift.addBehaviour(haltMessageReceiver);
	        } 
	    	catch (ReceiverBehaviour.TimedOut | ReceiverBehaviour.NotYetReady exception) {
	        	msg = null;
	        } 
	    	processHaltMessage(msg);	
		}
	}
	
	private void processHaltMessage(ACLMessage msg) {
		
		// Receives HALT Message
		//ACLMessage msg = myAgent.receive(templateHalt);
		if(msg != null) {
			ACLMessage response = new ACLMessage(ACLMessage.AGREE);
			
			String bully = "liftAgent" + msg.getContent();
			
			// looks for the bully
			for(String listener : lift.getContacts()) {
				if(listener.equals(bully)) {
					response.addReceiver(new sajas.core.AID((String) listener,AID.ISLOCALNAME));
				}
			}
			
			// sends agreement
			lift.getConsensusNetwork().removeEdges("liftAgent"+ lift.getId());
			response.setContent(Integer.toString(lift.getId()));
	        lift.send(response);
			proposalsList.clear();
			
		}
		
	}
	
	private void processProposalMessage(ACLMessage msg) {
	
		int liftId = 0;
		float proposedTime = 0;
		if(msg != null) {
			String[] content = msg.getContent().split(":", 2);
			proposedTime = Float.parseFloat(content[0]);
			liftId = Integer.parseInt(content[1]);

			proposalsList.put(liftId, proposedTime);
		} 
		//Processing Proposal Messages

		var myProposal = lift.getCurrentLiftProposal();
		//Only if I have proposal and received all other proposals
		if(proposalsList.size() >= lift.getContacts().size() && myProposal != null) {
			boolean betterProposal = true;

			Iterator it = proposalsList.entrySet().iterator();
		    while (it.hasNext()) {
		        HashMap.Entry pair = (HashMap.Entry)it.next();
		        float time = (float)pair.getValue();
		        int id = (int)pair.getKey();
		        if(myProposal.getTime() > time) betterProposal = false;
				else if(myProposal.getTime() == time && id < lift.getId()) betterProposal = false;     
		        it.remove();
		    }
			if(betterProposal) acceptProposal();
		}
	}
	
	private void acceptProposal() {
		
		//Sends halt to all lifts
		System.out.println("LIFT " + lift.getId() + " is taking care of REQUEST: " + lift.getCurrentLiftProposal().getEntry().getFloor() + ":" + lift.getCurrentLiftProposal().getEntry().getType());
		if(lift.getContacts().size() != 0) {
			ACLMessage msg = new ACLMessage(ACLMessage.CANCEL);
			for(String listener : lift.getContacts()) {
				msg.addReceiver(new sajas.core.AID((String) listener,AID.ISLOCALNAME));
			}
			msg.setContent(Integer.toString(this.lift.getId()));
			lift.getConsensusNetwork().sendsHalt("liftAgent" + this.lift.getId());
	        lift.send(msg);
	    }
		this.accepted_request = true;
	}
	
	private void processAgreeMessage(ACLMessage msg) {
		
		// Receives HALT Message
		//ACLMessage msg = myAgent.receive(templateAgree);
		if(msg != null) {
			//TODO: Single Threaded, can't let it go into endless loop here Use waker behavior
			haltReceivers.add(msg.getContent());
			if(haltReceivers.size() >= lift.getContacts().size()) {
				// clears halt and proposals list and assigns new task
				haltReceivers.clear();
				proposalsList.clear();
				lift.setTaskList(lift.getCurrentLiftProposal().getTaskList());
				
				if(lift.getCurrentLiftProposal().getEntry().getType().equals(Type.Up)){
					lift.getAnalysis().addToLiftTasks(lift.getId(), 0);
				}
				else if(lift.getCurrentLiftProposal().getEntry().getType().equals(Type.Down)) {
					lift.getAnalysis().addToLiftTasks(lift.getId(), 1);
				}
				else {
					lift.getAnalysis().addToLiftTasks(lift.getId(), 2);
				}
				this.accepted_request = false;
			}
		}
		
	}

}
