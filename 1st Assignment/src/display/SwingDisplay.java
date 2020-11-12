package display;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import agents.FloorPanelAgent;
import agents.LiftAgent;

public class SwingDisplay {
	
	private JFrame frame;
	private JPanel panel;
	
	private ArrayList<LiftAgent> lifts;
	private ArrayList<FloorPanelAgent> floorPanels;
	
	
	public SwingDisplay(ArrayList<LiftAgent> lifts, ArrayList<FloorPanelAgent> floorPanels) {
		
		// lifts and floor panels
		this.lifts = lifts;
		this.floorPanels = floorPanels;
		
		// initializes frame
		this.frame = new JFrame("LiftManagementSystem");
        this.panel = new JPanel();
        this.frame.setSize(1250, 750);
	}

	
	public void draw() {
		
		// -------------------------- Displays Swing --------------------------
		// arrange Floor's display
		
		this.panel.removeAll();
		
		// this will get the display along the y axis for the FloorPanels
		int offsetY = 650 / this.floorPanels.size();
		int offsetX = 350 / this.lifts.size();
		
		this.frame.getContentPane();
	    JLabel label;
	    this.panel.setLayout(null);
	    this.panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    
	    // Draws lift info Header
    	label = new JLabel("Lift's Info:");
    	label.setPreferredSize(new Dimension(500, 20));
    	Dimension size = label.getPreferredSize();
    	label.setBounds(500, 50, size.width, size.height);
    	this.panel.add(label);
	    
	    // iterates through floor panels
	    for(FloorPanelAgent fp: this.floorPanels) {
	    	
	    	// Draws FloorPanel
	    	label = new JLabel("FP" + fp.getFloor());
	    	label.setPreferredSize(new Dimension(50, 20));
	    	size = label.getPreferredSize();
	    	label.setBounds(50, 650 - (offsetY * fp.getFloor()), size.width, size.height);
	    	this.panel.add(label);
	    	
	    	// Draws intersection
	    	if(fp.getFloor() != 0) {
	    		label = new JLabel("------------------------------------------------------------------------------------------------------");
		    	label.setPreferredSize(new Dimension(500, 20));
		    	size = label.getPreferredSize();
		    	label.setBounds(50, 660 - (offsetY * fp.getFloor()), size.width, size.height);
		    	this.panel.add(label);
	    	}
	    	
	    }
	    
	    // this variable acts as the offset for the Lift's Info
	    int i = 0;
	    
	    // iterates through lifts
	    for(LiftAgent l : this.lifts) {
	    	
	    	// Draws Lift
	    	label = new JLabel("Lift" + l.getId());
	    	label.setPreferredSize(new Dimension(200, 20));
	    	size = label.getPreferredSize();
	    	label.setBounds(50 + (offsetX * l.getId()), 650 - (offsetY * l.getFloor()), size.width, size.height);
	    	this.panel.add(label);
	    	
	    	String taskList = "";
	    	// Draws lift info
	    	for(int j = 0; j < l.getTaskList().size(); j++) {
	    		taskList = taskList + l.getTaskList().get(j).getFloor() + ":" + l.getTaskList().get(j).getType() + ";";
	    	}
	    	label = new JLabel("--> Lift" + l.getId() + " CurrentWeight: " + l.getCurrentWeight() + ": CurrentFloor [" + l.getFloor() + "]; TaskList = {" + taskList + "}");
	    	label.setPreferredSize(new Dimension(1000, 20));
	    	size = label.getPreferredSize();
	    	label.setBounds(500, 75 + (25 * i), size.width, size.height);
	    	this.panel.add(label);
	    	
	    	i++;
	    }
	    
	    this.panel.revalidate();
	    this.panel.repaint();
	    
	    this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    this.frame.add(this.panel);
	    
	    this.frame.setVisible(true);
		
		
	    //  ---------------------------------------------------------------------
		
	}
	
	public void update(ArrayList<LiftAgent> lifts, ArrayList<FloorPanelAgent> floorPanels) {
		
		// lifts and floor panels
		this.lifts = lifts;
		this.floorPanels = floorPanels;
		
	}
	
	
	/*
	public void updateTaskLists(int liftID, ArrayList<LiftTaskListEntry> taskList) {
		// iterates through lifts
	    for(LiftAgent l : this.lifts) {
	    	if(l.getId() == liftID) {
	    		l.setTaskList(taskList);
	    	}
	    }
	}
	*/
	

}
