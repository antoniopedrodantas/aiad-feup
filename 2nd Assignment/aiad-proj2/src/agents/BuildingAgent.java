package agents;

import agents.LiftAgent;
import utils.Analysis;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import agents.FloorPanelAgent;
import agents.RequestAgent;
import display.SwingDisplay;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import sajas.core.Agent;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.ShutdownPlatform;
import jade.lang.acl.ACLMessage;
import sajas.wrapper.AgentController;
import sajas.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import launcher.RepastLauncher;

 
@SuppressWarnings("serial")
public class BuildingAgent extends Agent{
	
	private int nmrFloors;
	private int nmrLifts;
	private float maxWeight;
	private float maxSpeed;
	private float floorDistance;
	private float timeAtFloors;
	private ContainerController mainContainer;
	private Analysis analysis;
	private RepastLauncher repast;
	private RequestAgent request;
	
	private ArrayList<LiftAgent> lifts = new ArrayList<>();
	private ArrayList<FloorPanelAgent> floorPanels = new ArrayList<>();
	
	public BuildingAgent(String[] args, ContainerController mainContainer, Analysis analysis, RepastLauncher repast){
        this.nmrFloors = Integer.parseInt(args[0]);
        this.nmrLifts = Integer.parseInt(args[1]);
        this.maxWeight = Float.parseFloat(args[2]); 
        this.maxSpeed = Float.parseFloat(args[3]);
        this.floorDistance = Float.parseFloat(args[4]);
        this.timeAtFloors = Float.parseFloat(args[5]);
        this.mainContainer = mainContainer;
        this.analysis = analysis;
        this.repast = repast;
    }
	
	public void setup() {
		
		System.out.println(getLocalName() + ": started working.\n");
		System.out.println(this.toString());
		
		SwingDisplay swing = new SwingDisplay(lifts, floorPanels);
		
		launchLiftAgents(this.nmrLifts, swing, analysis);
		launchRequestAgent(this.nmrFloors);
		launchFloorPanelAgents(this.nmrFloors, this.nmrLifts);
	
		this.repast.buildAndScheduleDisplay(this.lifts, this.floorPanels, this.request);
		
		swing.update(lifts, floorPanels);
//		swing.draw();
	}

	public void takeDown() {
		for(LiftAgent l: this.lifts) {
			l.takeDown();
		}
		
		for(FloorPanelAgent f: this.floorPanels) {
			f.takeDown();
		}
		
		this.request.takeDown();
		System.out.println(getLocalName() + ":done working."); 
		this.analysis.shutDown(); 
		try {
			this.mainContainer.kill();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}
	
	
	/*LiftAgents launching functions */
	
	protected void launchLiftAgents(Integer nmrLifts, SwingDisplay swing, Analysis analysis) {
		
		int lift;
		for(lift = 1; lift <= nmrLifts; lift++) {
			createLiftAgent(lift, swing, analysis);
		}
	}
	
	protected void createLiftAgent(Integer lift, SwingDisplay swing, Analysis analysis) {
		
		AgentController liftAgent;
		LiftAgent newLiftAgent = new LiftAgent(buildArgs(lift), this.nmrFloors, swing, analysis);
		
		try {
			liftAgent = this.mainContainer.acceptNewAgent("liftAgent" + lift, newLiftAgent);
			liftAgent.start();
			lifts.add(newLiftAgent);
		} catch(StaleProxyException e) {
			System.err.println("Error launching liftAgent");
			e.printStackTrace();
		}
	}
	
	protected String[] buildArgs(Integer lift) {
		String[] args = {String.valueOf(lift), String.valueOf(this.maxWeight), 
				String.valueOf(this.maxSpeed), String.valueOf(this.nmrLifts),
				String.valueOf(this.floorDistance), String.valueOf(this.timeAtFloors)};
		return args;
	}
	
	
	/* FloorPanelAgents launching functions */
	
	protected void launchFloorPanelAgents(Integer nmrFloors, Integer nmrLifts) {
		
		int floor;
		for(floor = 0; floor <= nmrFloors; floor++) {
			createFloorPanelAgent(floor, nmrLifts);
		}
	}
	
	protected void createFloorPanelAgent(Integer floor, Integer nmrLifts) {
		
		AgentController floorPanelAgent;
		
		FloorPanelAgent newFloorPanelAgent = new FloorPanelAgent(floor, nmrLifts);
		
		try {
			floorPanelAgent = this.mainContainer.acceptNewAgent("floorPanelAgent" + floor, newFloorPanelAgent);
			floorPanelAgent.start();
			floorPanels.add(newFloorPanelAgent);
		} catch(StaleProxyException e) {
			System.err.println("Error launching floorPanelAgent");
			e.printStackTrace();
		}
	}
	
	/* RequestAgent launching functions */
	
	protected void launchRequestAgent(int nmrFloors) {
		
		AgentController requestAgent;
		try {
			this.request = new RequestAgent(nmrFloors, nmrLifts);
			requestAgent = this.mainContainer.acceptNewAgent("requestAgent", this.request);
			requestAgent.start();
		} catch(StaleProxyException e) {
			System.err.println("Error launching requestAgent");
			e.printStackTrace();
		}
	}
	
	@Override
    public String toString() {
        return "INFORMATION\n" + "Agent: " + getLocalName()+"\n" + "Floors: " + this.nmrFloors + "\n" + "Lifts: " + this.nmrLifts + "\n" + "Max weight: " + this.maxWeight + "\n" + "Max speed: " + this.maxSpeed + "\n" + "Floor distance: " + this.floorDistance + "\n";
    }
	
	
	
	/*getters*/
	public int getFloors() {
		return this.nmrFloors;
	}
	
	public int getLifts() {
		return this.nmrLifts;
	}
	
	public float getWeight() {
		return this.maxWeight;
	}
	
	public float getSpeed() {
		return this.maxSpeed;
	}
	
	public float getDistance() {
		return this.floorDistance;
	}
	
	public ContainerController getController() {
		return this.mainContainer; 
	}
	
	public ArrayList<LiftAgent> getLiftsAgent() {
		return this.lifts;
	}
	
	public ArrayList<FloorPanelAgent> getFloorPanels(){
		return this.floorPanels;
	}
	
}
