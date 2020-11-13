package utils;

import agents.LiftAgent;

import java.util.ArrayList;
import jade.core.Agent;

public class HandleRequest {
	
	private LiftAgent myAgent;
	private String request;
	private LiftProposal liftProposal;
	
	public HandleRequest(Agent agent,String content) {
		this.myAgent = (LiftAgent) agent;
		this.request = content;
	}
	
	public void processRequest() {

		/* steps */
		
		// 1) calculate time to attend request
		
		this.liftProposal = timeToAttendRequest(Integer.parseInt(this.request));
		// 2) obtain list of all lifts available
		//  	2.1) We can obtain the name of all the lifts with which we have to communicate through the variable totalLifts that each LiftAgent has
		//			e.g totalLifts = 4 and my Lift has the id = 1, so i know that i have to change messages with liftAgent2/3/4
		
		if(this.myAgent.getContacts().isEmpty()) {
			this.myAgent.setContacts(buildContactList());
		}

		System.out.println("LIFT PROPOSED TIME: " + this.liftProposal.getTime());
		System.out.println("LIFT PROPOSED TaskList: ");
		for (LiftTaskListEntry task : this.liftProposal.getTaskList()) {
			System.out.println("\t" + task.getFloor() + ", " + task.getType().toString());
		}
		this.myAgent.setTaskList(this.liftProposal.getTaskList());
		// 3) implement a consensus algorithm to know who fulfills the request
		//		3.1) if it is me, add the request to the taskList
	}
	
	protected LiftProposal timeToAttendRequest(int request) {
		float timeBetweenFloors = this.myAgent.getFloorDistance() / this.myAgent.getSpeed();
		float timeAtFloors = this.myAgent.getTimeAtFloors();
		
		LiftTaskListEntry entry = new LiftTaskListEntry(Math.abs(request), request > 0 ? 1 : -1);
		float time;
		
		int optimalPosition = getListPos(entry);
		
		
		if(optimalPosition == 0 || this.myAgent.getTaskList().isEmpty())
			time = Math.abs(this.myAgent.getFloor() - Math.abs(request)) * timeBetweenFloors;
		else {
			time = Math.abs(this.myAgent.getFloor() - myAgent.getTaskList().get(0).getFloor()) * timeBetweenFloors;
			time += timeAtFloors;
			for (int i = 0; i < optimalPosition - 1; i++) {
				time += myAgent.getTaskList().get(i).timeTo(myAgent.getTaskList().get(i+1), timeBetweenFloors);
				time += timeAtFloors;
			}
			time += myAgent.getTaskList().get(optimalPosition-1).timeTo(entry, timeBetweenFloors);
		}
		return new LiftProposal(this.myAgent.getTaskList(), entry, optimalPosition, time);	
	}
	
	//Gets the list position for a new request
	public int getListPos(LiftTaskListEntry entry) {	

		if (myAgent.getTaskList().size() == 0) return 0;
		
		int lastPos = myAgent.getFloor();
		int pos = 0;
		
		switch (entry.getType()) {
			case End:
				for(LiftTaskListEntry taskListEntry : myAgent.getTaskList()) {	
					if(between(lastPos, taskListEntry.getFloor(), entry.getFloor()) 
							|| between(taskListEntry.getFloor(), lastPos, entry.getFloor()))
						return pos; 
					lastPos = taskListEntry.getFloor();
					pos++;
				}
				return pos;
			case Down:			
				for(LiftTaskListEntry taskListEntry : myAgent.getTaskList()) {
					if(between(taskListEntry.getFloor(), lastPos, entry.getFloor()))
						return pos;
					lastPos = taskListEntry.getFloor();
					pos++;
				}
				return pos;
			case Up:
				for(LiftTaskListEntry taskListEntry : myAgent.getTaskList()) {
					if(between(lastPos, taskListEntry.getFloor(), entry.getFloor()))
						return pos;
					lastPos = taskListEntry.getFloor();
					pos++;
				}
				return pos;
			default:
				return myAgent.getTaskList().size();
		}
	}
	
	//is X between a and b
	private boolean between(int a, int b, int x) {
		return ((a <= x && x <= b));
	}
	
	private boolean turningPoint(int i) {
		var tasks = myAgent.getTaskList();
		if (tasks.size() < i+1 || i < 1 ) return false;
		boolean goingUp = tasks.get(i-1).getFloor() < tasks.get(i).getFloor();
		boolean goingDown = tasks.get(i-1).getFloor() > tasks.get(i).getFloor();
		//COMPLETAR
		return false;
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
