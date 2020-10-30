package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

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
	}
	
	public void takeDown() {
		
	}
	
	private void sendRequests() {
		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            	public void run() {
            		sendRequest();
            	}
          	}, 2000, 15000, TimeUnit.MILLISECONDS);
	}
	
	private void sendRequest() {
		
		Random rand = new Random();
		int randomInteger = rand.nextInt(floors);
		Boolean randomBoolean = rand.nextBoolean();
		System.out.println(randomBoolean);
		
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("floorPanelAgent" + randomInteger ,AID.ISLOCALNAME));
        msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        msg.setReplyByDate(new Date(System.currentTimeMillis() + 10000)); //we want to receive a reply in 10 seconds at most
        
        String content;
        if (randomBoolean) {
			content = "Up";
		} else {
			content = "Down";
		}
        
        msg.setContent(content);
        System.out.println("Floor: "+ randomInteger + "  Message: " + content);
        this.send(msg);
        
    	
		
		addBehaviour(new AchieveREInitiator(this, msg) {
			protected void handleInform(ACLMessage inform) {
				System.out.println("Agent " + inform.getSender().getName()+ " successfully performed the requested action");
			}
			protected void handleRefuse(ACLMessage refuse) {
				System.out.println("Agent " + refuse.getSender().getName()+ " refused to perform the requested action");
				
			}
			protected void handleFailure(ACLMessage failure) {
				if (failure.getSender().equals(myAgent.getAMS())) {
					System.out.println("Responder does not exist");
				}
				else {
					System.out.println("Agent "+failure.getSender().getName()+" failed to perform the requested action");
				}
			}
		} );
	}
}

