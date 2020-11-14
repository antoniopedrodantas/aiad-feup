package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        
        /* open files that will keep track of lift current floors through time */
        openLiftFiles();
	}

	protected void openLiftFiles() {
		
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
		Date date = new Date() ;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
		
		String path = "./analysis/analysis-" + dateFormat.format(date) + ".csv";
		File new_file = new File(path);

        try{
            if(!new_file.exists()){
                new_file.getParentFile().mkdirs();
                new_file.createNewFile();
            }
        }catch(IOException exception){
            exception.printStackTrace();
        }
        
        FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(new_file);
			PrintWriter printWriter = new PrintWriter(fileWriter);
		    StringBuilder sb = new StringBuilder();
		    
		    sb.append("Number of Floors");
		    sb.append(",");
		    sb.append("Number of Lifts");
		    sb.append(",");
		    sb.append("Max Weight");
		    sb.append(",");
		    sb.append("Max Speed");
		    sb.append(",");
		    sb.append("Floor Distance");
		    sb.append(",");
		    sb.append("Time at Floors");
		    sb.append("\r\n");
		    
		    sb.append(this.nmrFloors);
		    sb.append(",");
		    sb.append(this.nmrLifts);
		    sb.append(",");
		    sb.append(this.maxWeight);
		    sb.append(",");
		    sb.append(this.maxSpeed);
		    sb.append(",");
		    sb.append(this.floorDistance);
		    sb.append(",");
		    sb.append(this.timeAtFloors);
		    
		    sb.append("\r\n");
		    sb.append("\r\n");
		    
		    sb.append("Floor");
		    sb.append(",");
		    sb.append("People entering");
		    sb.append(",");
		    sb.append("People exiting");
		    sb.append(",");
		    sb.append("\r\n");
		    
		    for(int i = 0; i <= this.nmrFloors; i++) {
		    	sb.append(i);
		    	sb.append(",");
		    	sb.append(this.enteringAtFloor.get(i));
		    	sb.append(",");
		    	sb.append(this.exitingAtFloor.get(i));
		    	sb.append("\r\n");
		    }
		    
		    sb.append("\r\n");
		    sb.append("Lift");
		    sb.append(",");
		    sb.append("Up");
		    sb.append(",");
		    sb.append("Down");
		    sb.append(",");
		    sb.append("End");
		    sb.append(",");
		    sb.append("Average Occupation");
		    sb.append("\r\n");
		    
		    
		    for(int k = 0; k < this.nmrLifts; k++) {
		    	sb.append(k + 1);
			    sb.append(",");
			    sb.append(this.liftTasks.get(k).get(0));
			    sb.append(",");
			    sb.append(this.liftTasks.get(k).get(1));
			    sb.append(",");
			    sb.append(this.liftTasks.get(k).get(2));
			    sb.append(",");
			    sb.append(this.averageOccupation.get(k).get(1));
			    sb.append("\r\n");
		    }
		    
		    sb.append("\r\n");
		    printWriter.write(sb.toString());
		    printWriter.close();
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
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
