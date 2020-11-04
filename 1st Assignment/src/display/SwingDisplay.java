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

public class SwingDisplay implements Runnable {
	
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

	@Override
	public void run() {
		
		// -------------------------- Displays Swing --------------------------
		// arrange Floor's display
		while(true) {
			
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
	    	label.setBounds(750, 50, size.width, size.height);
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
		    	
		    	// Draws lift info
		    	label = new JLabel("--> Lift" + l.getId() + ": CurrentFloor [" + l.getFloor() + "]; TaskList = {" + l.getTaskList() + "}; CurrentWeight: " + l.getCurrentWeight());
		    	label.setPreferredSize(new Dimension(500, 20));
		    	size = label.getPreferredSize();
		    	label.setBounds(750, 75 + (25 * i), size.width, size.height);
		    	this.panel.add(label);
		    	
		    	i++;
		    }
		    
		    this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		    this.frame.add(this.panel);
		    
		    this.frame.setVisible(true);
		}
		
	    //  ---------------------------------------------------------------------
		
	}

}
