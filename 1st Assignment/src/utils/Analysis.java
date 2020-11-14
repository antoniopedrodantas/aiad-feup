package utils;

import java.util.ArrayList;

public class Analysis {
	
	private int nmrFloors;
	private int nmrLifts;
	private float maxWeight;
	private float maxSpeed;
	private float floorDistance;
	private float timeAtFloors;
	
	private ArrayList<Integer> enteringAtFloor;
	private ArrayList<Integer> exitingAtFloor;
	private ArrayList<ArrayList<Integer>> liftTasks;
	private ArrayList<ArrayList<Float>> averageOccupation;

	public Analysis(String[] args) {
		this.nmrFloors = Integer.parseInt(args[0]);
        this.nmrLifts = Integer.parseInt(args[1]);
        this.maxWeight = Float.parseFloat(args[2]); 
        this.maxSpeed = Float.parseFloat(args[3]);
        this.floorDistance = Float.parseFloat(args[4]);
        this.timeAtFloors = Float.parseFloat(args[5]);
        
        enteringAtFloor = new ArrayList<Integer>(this.nmrFloors + 1);
        exitingAtFloor = new ArrayList<Integer>(this.nmrFloors + 1);
        
        for (int i = 0; i <= this.nmrFloors; i++) {
        	 this.enteringAtFloor.add(0);
        	 this.exitingAtFloor.add(0);
        }
        
        /* initialize arraylist to keep track of tasks performed by lifts */
        liftTasks = new ArrayList<ArrayList<Integer>>();
        
        for(int j = 0; j < this.nmrLifts; j++) {
        	
        	ArrayList<Integer> list = new ArrayList<Integer>();
        	
        	for(int k = 0; k < 3; k++) {
        		list.add(0);
        	}
        	
        	liftTasks.add(list);
       }
        
        /* initialize arraylist to keep track of lifts occupation */
        averageOccupation = new ArrayList<ArrayList<Float>>();
        
        for(int j = 0; j < this.nmrLifts; j++) {
        	
        	ArrayList<Float> info = new ArrayList<Float>();
        	
        	info.add(Float.valueOf(0));
        	info.add(Float.valueOf(0));
        	
        	averageOccupation.add(info);
       }
	}

	/* functions that allow changing floor entry and exit values */
	public void enterAtFloor(int floor, int quantity) {
		
		int value = quantity + this.enteringAtFloor.get(floor);
		this.enteringAtFloor.set(floor, value);
	}
	
	public void exitAtFloor(int floor, int quantity) {
		
		int value = quantity + this.exitingAtFloor.get(floor); 
		this.exitingAtFloor.set(floor, value);
	}
	
	/* add to liftTasks */
	public void addToLiftTasks(int lift, int op) {
		int value = 1 + this.liftTasks.get(lift - 1).get(op);
		
		this.liftTasks.get(lift - 1).set(op, value);
	}
	
	/* calculate new occupation */
	public void recalculateOccupation(int lift, float weight) { //this weight needs to be the total, after entering/exiting people
		
		float avg = weight / this.maxWeight;
		float times = this.averageOccupation.get(lift - 1).get(0) + 1;
		float newOccupation = ((this.averageOccupation.get(lift - 1).get(0) * this.averageOccupation.get(lift - 1).get(1)) + avg) / (this.averageOccupation.get(lift - 1).get(0) + 1);
		
		this.averageOccupation.get(lift-1).set(0, times);
		this.averageOccupation.get(lift-1).set(1, newOccupation);
	}
	
	/* functions for writing to a file at shutdown */
	public void shutDown() {
		writeToFile();
	}
	
	protected void writeToFile() {
		
	}
	
	/* getters and setters */
	public int getNmrFloors() {
		return nmrFloors;
	}

	public void setNmrFloors(int nmrFloors) {
		this.nmrFloors = nmrFloors;
	}

	public int getNmrLifts() {
		return nmrLifts;
	}

	public void setNmrLifts(int nmrLifts) {
		this.nmrLifts = nmrLifts;
	}

	public float getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(float maxWeight) {
		this.maxWeight = maxWeight;
	}

	public float getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public float getFloorDistance() {
		return floorDistance;
	}

	public void setFloorDistance(float floorDistance) {
		this.floorDistance = floorDistance;
	}

	public float getTimeAtFloors() {
		return timeAtFloors;
	}

	public void setTimeAtFloors(float timeAtFloors) {
		this.timeAtFloors = timeAtFloors;
	}
}
