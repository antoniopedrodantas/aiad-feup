package utils;

import agents.LiftAgent;

import java.util.ArrayList;
import sajas.core.Agent;
import utils.LiftTaskListEntry.Type;

public class HandleRequest {
	
	private LiftAgent myAgent;
	private String request;
	private LiftProposal liftProposal;
	
	public HandleRequest(Agent agent,String content) {
		this.myAgent = (LiftAgent) agent;
		this.request = content;
	}
	
	public void processRequest() {

		this.liftProposal = timeToAttendRequest(Integer.parseInt(this.request));
	
		if(this.myAgent.getContacts().isEmpty()) {
			this.myAgent.setContacts(buildContactList());
		}

		myAgent.setCurrentLiftProposal(this.liftProposal);

		var bullyStart = new LiftBullyStart(myAgent); 
		bullyStart.sendBullyProposal(liftProposal);
		
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
							|| between(taskListEntry.getFloor(), lastPos, entry.getFloor())
							|| shouldPlace(pos, entry))
						return pos; 
					lastPos = taskListEntry.getFloor();
					pos++;
				}
				return pos;
			case Down:			
				for(LiftTaskListEntry taskListEntry : myAgent.getTaskList()) {
					if(between(taskListEntry.getFloor(), lastPos, entry.getFloor())
							|| shouldPlace(pos, entry))
						return pos;
					lastPos = taskListEntry.getFloor();
					pos++;
				}
				return pos;
			case Up:
				for(LiftTaskListEntry taskListEntry : myAgent.getTaskList()) {
					if(between(lastPos, taskListEntry.getFloor(), entry.getFloor()) 
							|| shouldPlace(pos, entry))
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
	
	//Returns true if should place Proposed Task for catching turning points
	private boolean shouldPlace(int i, LiftTaskListEntry entry) {
		var tasks = myAgent.getTaskList();
		if (i > 2) {
			if (turningPoint(i)) {
				if (entry.getFloor() > tasks.get(i).getFloor() 
						&& entry.getFloor() > tasks.get(i-1).getFloor() 
						&& entry.getType() != Type.Up)
					return true;

				if (entry.getFloor() < tasks.get(i).getFloor() 
						&& entry.getFloor() < tasks.get(i-1).getFloor() 
						&& entry.getType() != Type.Down)
					return true;
			}
		}
		return false;
	}
	
	
	//Returns true if is turning point
	private boolean turningPoint(int i) {
		
		var tasks = myAgent.getTaskList();
		
		if ( i < 2 ) return false;
		
		boolean goingUp = tasks.get(i-2).getFloor() < tasks.get(i-1).getFloor();
		boolean goingDown = tasks.get(i-2).getFloor() > tasks.get(i-1).getFloor();
		
		if(goingUp && tasks.get(i).getType() == Type.Down) return true;
		if(goingDown && tasks.get(i).getType() == Type.Up) return true;
		
		if(tasks.size() > i+1) {	
			if(goingUp && tasks.get(i).getType() == Type.End)
				if(tasks.get(i).getFloor() > tasks.get(i+1).getFloor()) return true;
			if(goingDown && tasks.get(i).getType() == Type.End)
				if(tasks.get(i).getFloor() < tasks.get(i+1).getFloor()) return true;
		}
		
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
