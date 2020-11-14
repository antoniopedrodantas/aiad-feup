package utils;

import java.util.ArrayList;

public final class LiftProposal {
	private TaskList taskList;
	private float time;
	private LiftTaskListEntry newEntry;
	
	public LiftProposal(TaskList taskList, LiftTaskListEntry newEntry, int newEntryPos, float time) {
		this.taskList = new TaskList(taskList);;
		this.taskList.add(newEntryPos, newEntry);
		this.time = time;
		this.newEntry = newEntry;
	}
	public TaskList getTaskList() {
		return taskList;
	}
	public void setTaskList(TaskList taskList) {
		this.taskList = taskList;
	}
	public float getTime() {
		return time;
	}
	public void setTime(float time) {
		this.time = time;
	}
	
	public LiftTaskListEntry getEntry() {
		return this.newEntry;
	}
	
}
