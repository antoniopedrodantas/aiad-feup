package utils;

import java.util.ArrayList;

public class TaskList extends ArrayList<LiftTaskListEntry>{

	public TaskList(TaskList taskList) {
		super((ArrayList<LiftTaskListEntry>) taskList);
	}

	public TaskList() {
		super();
	}

	@Override
	public boolean add(LiftTaskListEntry e) {
		if(this.size() != 0)
			if(e.getFloor() == this.get(this.size()).getFloor()) return true;
		return super.add(e);
	}

	@Override
	public void add(int index, LiftTaskListEntry element) {
		if (index >= this.size()) {
			super.add(element);
		}
		else {
			boolean canAdd = true;
			if(index < this.size())
				if(element.getFloor() == this.get(index).getFloor()) {
					canAdd = false;
					prioritizeUpDown(index, element);
				}
			if(index > 0)
				if(element.getFloor() == this.get(index - 1).getFloor()) {
					canAdd = false;
					prioritizeUpDown(index, element);
				}
			if(canAdd) super.add(index, element);
			}
	}
	
	private void prioritizeUpDown(int index, LiftTaskListEntry element) {
		
		if(this.get(index).getType() == LiftTaskListEntry.Type.End) {
			if(element.getType() == LiftTaskListEntry.Type.Up ||
					element.getType() == LiftTaskListEntry.Type.Down) {
				this.set(index, element);
			}
			
		}
			
	}

}
