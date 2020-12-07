
package display;

import java.awt.Color;
import java.util.ArrayList;

import agents.LiftAgent;
import launcher.RepastLauncher;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Network2DDisplay;
import uchicago.src.sim.gui.OvalNetworkItem;
import uchicago.src.sim.network.DefaultDrawableNode;
import uchicago.src.sim.space.Object2DGrid;
import utils.Edge;

public class ConsensusNetworkDisplay {
	
	private ArrayList<LiftAgent> liftAgents;
	private DisplaySurface dsurf;
	private Object2DGrid space;
	private RepastLauncher repast;
	private ArrayList<DefaultDrawableNode> nodes = new ArrayList<>();
	private ArrayList<Edge> edges = new ArrayList<>();
	private int WIDTH;
	private int HEIGHT;
	
	final int offset = 1;
	
	public ConsensusNetworkDisplay(ArrayList<LiftAgent> liftAgents, int nmrLifts, int nmrFloors, RepastLauncher repast) {
		this.liftAgents = liftAgents;
		this.repast = repast;
		
		this.WIDTH = 100;
		this.HEIGHT = 100;
		
		if (dsurf != null) 
			dsurf.dispose();
		dsurf = new DisplaySurface(repast, "Lift Consensus");
		repast.registerDisplaySurface("Lift Consensus", dsurf);
		int i = 0;
		for(LiftAgent lift : liftAgents) {
			int xZero = WIDTH/2;
			int yZero = HEIGHT/2;
			int xradius = WIDTH/6;
			int yradius = HEIGHT/6;

			double angle = Math.toRadians(360/nmrLifts);
			int x = (int) (xZero + (xradius * Math.cos(angle*i)));
			int y = (int) (yZero + (yradius * Math.sin(angle*i)));
			this.nodes.add(generateNode("liftAgent" + Integer.toString(lift.getId()), Color.blue, x, y));
			i++;
		}
		
		this.buildDisplay(dsurf, this.buildModel());
		repast.getSchedule().scheduleActionAtInterval(1, dsurf, "updateDisplay", Schedule.LAST);
	}
	
	private Network2DDisplay buildModel() {
		
		Network2DDisplay display = new Network2DDisplay(this.nodes,WIDTH,HEIGHT);
		return display;
	}
	private void buildDisplay(DisplaySurface dsurf, Network2DDisplay display) {
		dsurf.addDisplayableProbeable(display, "Network Display");
        dsurf.addZoomable(display);
        this.repast.addSimEventListener(dsurf);
		dsurf.display();

	}

	private DefaultDrawableNode generateNode(String label, Color color, int x, int y) {
        OvalNetworkItem oval = new OvalNetworkItem(x,y);
        oval.allowResizing(false);
        oval.setHeight(5);
        oval.setWidth(5);
        
		DefaultDrawableNode node = new DefaultDrawableNode(label, oval);
		node.setColor(color);
        
		return node;
	}
	
	private DefaultDrawableNode getNode(String label) {
		for(DefaultDrawableNode node : this.nodes) {
			if(node.getNodeLabel().equals(label)) {
				return node;
			}
		}
		return null;
	}

	public void addEdge(String label1, String label2, Color c) {
		DefaultDrawableNode node1 = getNode(label1);
		DefaultDrawableNode node2 = getNode(label2);
		
		if(node1 != null & node2 != null) {
			Edge edge = new Edge(node1, node2);
			edge.setColor(c);
			edges.add(edge);
			node1.addOutEdge(edge);
		}
		
	}
	
	public void removeEdges(String label) {
		DefaultDrawableNode node1 = getNode(label);
		
		if(node1 != null) {
			for(Edge e : this.edges) {
				if(e.getFromNode().getNodeLabel().equals(label)) {
					//this.edges.remove(e);
					node1.removeOutEdge(e);
				}
			}
		}
	}
	
	public void sendsHalt(String label) {
		DefaultDrawableNode node1 = getNode(label);
		if(node1 != null) {
			for(Edge e : this.edges) {
				if(e.getFromNode().getNodeLabel().equals(label)) {
					e.setColor(Color.YELLOW);
				}
			}
		}
	}
	
	
}