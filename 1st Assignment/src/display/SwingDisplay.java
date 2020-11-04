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
	}

	@Override
	public void run() {
		
		// -------------------------- Displays Swing --------------------------
		// arrange Floor's display
		while(true) {
			
			// this will get the display along the y axis for the FloorPanels
			int margin = 400 / this.floorPanels.size();
			
			this.frame.getContentPane();
		    JLabel label;
		    this.panel.setLayout(null);
		    this.panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		    
		    for(FloorPanelAgent fp: this.floorPanels) {
		    	label = new JLabel(fp.getFloor() + "FP");
		    	Dimension size = label.getPreferredSize();
		    	label.setBounds(50, 400 - (margin * fp.getFloor()), size.width, size.height);
		    	this.panel.add(label);
		    }
		    
		    for(LiftAgent l : this.lifts) {
		    	label = new JLabel("L" + l.getId());
		    	Dimension size = label.getPreferredSize();
		    	label.setBounds(100 * l.getId(), 400, size.width, size.height);
		    	this.panel.add(label);
		    }
		    
		    this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		    this.frame.add(this.panel);
		    this.frame.setSize(500, 500);
		    this.frame.setVisible(true);
		}
		
	    //  ---------------------------------------------------------------------
		
	}

}
