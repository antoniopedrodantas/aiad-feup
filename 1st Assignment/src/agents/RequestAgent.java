package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;
import jade.proto.AchieveREResponder;

import java.sql.Date;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("serial")
public class RequestAgent extends Agent{
	
	private int floors;
	
	public RequestAgent(int floors) {
		this.floors = floors;
	}
	
	public void setup() {
		sendRequests();
		addListener();
	}
	
	public void takeDown() {
		System.out.println("RequestAgent done working...");
	}
	
	private void sendRequests() {
		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            	public void run() {
            		sendRequest();
            	}
          	}, 2000, 7500, TimeUnit.MILLISECONDS);
	}
	
	private void sendRequest() {
		
		Random rand = new Random();
		int randomInteger = rand.nextInt(floors);
		Boolean randomBoolean = rand.nextBoolean();
		
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("floorPanelAgent" + randomInteger ,AID.ISLOCALNAME));
        msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        msg.setReplyByDate(new Date(System.currentTimeMillis() + 5000)); //we want to receive a reply in 5 seconds at most
        
        String content;
        
        if (randomBoolean) {
			content = "Up";
		} else {
			content = "Down";
		}
        
        if(randomInteger == 0) content = "Up";
        
        msg.setContent(content);
        System.out.println("\nFloor: "+ randomInteger + "  Message: " + content);
      
		addBehaviour(new AchieveREInitiator(this, msg) {
			protected void handleInform(ACLMessage inform) {
				System.out.println("Agent " + inform.getSender().getLocalName() + " successfully performed the requested action");
			}
			protected void handleRefuse(ACLMessage refuse) {
				System.out.println("Agent " + refuse.getSender().getLocalName() + " refused to perform the requested action");
				
			}
			protected void handleFailure(ACLMessage failure) {
				if (failure.getSender().equals(myAgent.getAMS())) {
					System.out.println("Responder does not exist");
				}
				else {
					System.out.println("Agent " + failure.getSender().getLocalName() + " failed to perform the requested action");
				}
			}
		} );
	}
	
	private void addListener() {
		System.out.println("Agent "+ getLocalName()+ " waiting for requests...");

	  	MessageTemplate template = MessageTemplate.and(
  		MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
  		MessageTemplate.MatchPerformative(ACLMessage.REQUEST) );
	  	
	  	addBehaviour(new AchieveREResponder(this, template) {
	  		
			protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
				
				System.out.println("Agent " + getLocalName() + ": REQUEST received from " + request.getSender().getLocalName() + ". Action is "+ request.getContent());
				
				if (checkSender(request.getSender().getName())) {
					
					ACLMessage agree = request.createReply();
					agree.setPerformative(ACLMessage.AGREE);
					return agree;
					
				}
				else {
					System.out.println("Agent " + getLocalName()+ ": Refuse");
					throw new RefuseException("check-failed");
				}
			}
			
			protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException{
				
				if(request.getContent() != null) {
					ACLMessage inform = request.createReply();
					inform.setContent(createResponse(request.getContent()));
					inform.setPerformative(ACLMessage.INFORM);
					return inform;
				}
				else {
					System.out.println("Agent " + getLocalName() + ": Action failed");
					throw new FailureException("unexpected-error");
				}
			}
		} );
	}
	
	protected boolean checkSender(String name) {
		return name.contains("liftAgent") ? true : false;
	}
	
	protected String createResponse(String request) {
		return "esta shit é assim";
	}
}

