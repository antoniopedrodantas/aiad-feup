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
		
		this.lifts = lifts;
		this.floorPanels = floorPanels;
		
		this.frame = new JFrame("LiftManagementSystem");
        this.panel = new JPanel();
        this.frame.setSize(500, 750);
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
		    
		    for(FloorPanelAgent fp: this.floorPanels) {
		    	
		    	// Draws FloorPanel
		    	
		    	label = new JLabel("FP" + fp.getFloor());
		    	label.setPreferredSize(new Dimension(50, 20));
		    	Dimension size = label.getPreferredSize();
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
		    
		    for(LiftAgent l : this.lifts) {
		    	label = new JLabel("Lift" + l.getId());
		    	label.setPreferredSize(new Dimension(200, 20));
		    	Dimension size = label.getPreferredSize();
		    	label.setBounds(50 + (offsetX * l.getId()), 650 - (offsetY * l.getFloor()), size.width, size.height);
		    	this.panel.add(label);
		    }
		    
		    this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		    this.frame.add(this.panel);
		    
		    this.frame.setVisible(true);
		}
		
	    //  ---------------------------------------------------------------------
		
	}

}
