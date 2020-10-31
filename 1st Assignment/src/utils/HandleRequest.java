package utils;

import agents.LiftAgent;

import java.util.ArrayList;
import jade.core.Agent;

public class HandleRequest {
	
	private LiftAgent myAgent;
	private String request;
	private double timeToAttend;
	
	public HandleRequest(Agent agent,String content) {
		this.myAgent = (LiftAgent) agent;
		this.request = content;
	}
	
	public void processRequest() {

		/* steps */
		
		// 1) calculate time to attend request
		this.timeToAttend = timeToAttendRequest();
		
		// 2) obtain list of all lifts available
		//  	2.1) We can obtain the name of all the lifts with which we have to communicate through the variable totalLifts that each LiftAgent has
		//			e.g totalLifts = 4 and my Lift has the id = 1, so i know that i have to change messages with liftAgent2/3/4
		
		if(this.myAgent.getContacts().isEmpty()) {
			this.myAgent.setContacts(buildContactList());
		}

		// 3) implement a consensus algorithm to know who fulfills the request
		//		3.1) if it is me, add the request to the taskList
	}
	
	protected double timeToAttendRequest() {
		//TODO: Implementar aqui a parte do Macedo
		return 1;
	}
	
	protected ArrayList<String> buildContactList(){
		
		ArrayList<String> lifts = new ArrayList<>();
		
		for(Integer i = 1; i <= this.myAgent.getTotalLifts(); i++) {
			if(!i.equals(this.myAgent.getId())) {
				lifts.add("liftAgent" + i);
			}
		}
		
		return lifts;
	}
	
	
}
