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
	
	
	//TODO: nao pode gerar pedido Up no ultimo piso do Building
	//TODO: so pode gerar 0 Up, nunca 0 Down
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
					//inform.setContent(createResponse(request.getContent()));
					System.out.println("recebi isto: " + request.getContent());
					inform.setContent("E:3[4-5-6],S:2");
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
	
	//create response to send to liftAgent
	protected String createResponse(String request) {
		
		String response;
		
		if(request != null) {
			String[] content = request.split(":", 2);
			response = generateResponse(Integer.parseInt(content[0]),content[1]);
		}
		else {
			response = "[WARNING] Request is empty";
		}
		
		return response;
	}
	
	protected String generateResponse(int floor, String action) { 
		String response = "";
		
		switch(action){
			case "Up": //lift is attending someone who called the lift and wants to go up
				break;
			case "Down": //lift is attending someone who called the lift and wants to go Down
				break;
			case "End": //lift is leaving someone on a certain floor(exiting is mandatory)
				break;
			default:
				break;
		}
		
		return response;
	}
}

//TODO

/*
 * O que é que vai acontecer quando o elevador tem espaço apenas para mais uma pessoa e o request Agent gera mais que uma pessoa a entrar?
 * (será recusado quando recebe o pedido ou o requestAgent vai ter informação de quantas pessoas pode gerar no maximo?
 * 
 * O que fazer nos casos em que requestAgent diz que saiem 4 pessoas mas so estam 3 la dentro?
 *
 */

/*
 * 
 * Function to generate numbers between 4 and 10
 * 	Random r = new Random();
	int floors = 10;
	int fl = 4;
	int result = r.nextInt((floors + 1) -fl) + fl;
 * 
 * */

