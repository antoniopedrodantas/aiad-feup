package agents;

import jade.core.Agent;

@SuppressWarnings("serial")
public class LiftAgent extends Agent{
	
	private int id;
	
	public LiftAgent(Integer id) {
		this.id = id;
	}
	
	public void setup() {
		System.out.println("Hey, " + this.getLocalName() + " here\n");
		System.out.println(this.toString());
	}
	
    public void takeDown() {
        System.out.println(getLocalName() + ": done working.");
    }
    
    @Override
    public String toString() {
        return "Lift ID: " + this.id + "\n";
    }
    
    /*getters*/
    public int getId() {
    	return this.id;
    }
}
