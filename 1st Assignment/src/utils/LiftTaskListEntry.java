package utils;

public class LiftTaskListEntry {
	enum Type {
		  Up,
		  Down,
		  End
		}
	private final int floor;
	private final Type type;
	
	public LiftTaskListEntry(int floor, int type) {
		this.floor = floor;
		switch (type) {
		case 1:
			this.type = Type.Up;
			break;
		case -1:
			this.type = Type.Down;
			break;
		case 0:
			this.type = Type.End;
			break;
			
		default:
			System.err.println("Unrecognized type of LiftTaskListEntery: " + type);
			this.type = Type.End;
			break;
		}
	}
	
	public float timeTo(LiftTaskListEntry next, float liftSpeed) {
		return Math.abs(this.floor - next.getFloor()) * liftSpeed;
	}
	
	public int getFloor() {
		return floor;
	}
}
