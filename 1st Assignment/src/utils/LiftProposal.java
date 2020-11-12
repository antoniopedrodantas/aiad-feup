package utils;

import java.util.ArrayList;

public final class LiftProposal {
	private ArrayList<LiftTaskListEntry> taskList;
	private float time;
	
	public LiftProposal(ArrayList<LiftTaskListEntry> taskList, LiftTaskListEntry newEntry, int newEntryPos, float time) {
		this.taskList = new ArrayList<>(taskList);;
		this.taskList.add(newEntryPos, newEntry);
		this.time = time;
	}
	public ArrayList<LiftTaskListEntry> getTaskList() {
		return taskList;
	}
	public void setTaskList(ArrayList<LiftTaskListEntry> taskList) {
		this.taskList = taskList;
	}
	public float getTime() {
		return time;
	}
	public void setTime(float time) {
		this.time = time;
	}
	
}
