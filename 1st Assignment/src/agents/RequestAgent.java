package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

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
          	}, 1000, 1500, TimeUnit.MILLISECONDS);
	}
	
	private void sendRequest() {
		Random rand = new Random();
		int randomInteger = rand.nextInt(floors);
		Boolean randomBoolean = rand.nextBoolean();
		
		System.out.println(randomBoolean);
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("floorPanelAgent" + randomInteger ,AID.ISLOCALNAME));
        String content;
        if (randomBoolean) {
			content = "Up";
		} else {
			content = "Down";
		}
        msg.setContent(content);
        System.out.println("Floor: "+ randomInteger + "  Message: " + content);
        this.send(msg);
	}
}
