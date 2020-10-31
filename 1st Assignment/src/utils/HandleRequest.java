package utils;

import jade.core.Agent;

public class HandleRequest {
	
	private Agent myAgent;
	private String request;
	private double timeToAttend;
	
	public HandleRequest(Agent agent,String content) {
		this.myAgent = agent;
		this.request = content;
	}
	
	public void processRequest() {
		
		/* steps */
		
		// 1) calculate time to attend request
		this.timeToAttend = timeToAttendRequest();
		// 2) obtain list of all lifts available
		//if() { //se ja tiver a lista nao fazer nada, caso nao tenha tem de se ir buscar
			
		//}
		// 3) implement a consensus algorithm to know who fulfills the request
		//		3.1) if it is me, add the request to the taskList
	}
	
	private double timeToAttendRequest() {
		return 1;
	}
	
	private void obtainLiftList(){
		
	}
}
