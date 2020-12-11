package behaviours;

import agents.LiftAgent;
import launcher.RepastLauncher;
import sajas.core.Agent;
import sajas.core.behaviours.CyclicBehaviour;
import sajas.core.behaviours.TickerBehaviour;

@SuppressWarnings("serial")
public class LiftTickerBehaviour extends CyclicBehaviour {
	
	private LiftAgent myAgent;
	private float realAgentPosition;
	private int tick;
	private RepastLauncher repast;
	
	public LiftTickerBehaviour(Agent agent, RepastLauncher repast) {
		super(agent);
		this.myAgent = (LiftAgent) agent;
		this.realAgentPosition = (float) this.myAgent.getFloor();
		this.tick = 0;
		this.repast = repast;
	}

	protected void moveLifts() {
		
		if(!this.myAgent.getTaskList().isEmpty()) {
			if(this.myAgent.getTaskList().get(0).getFloor() == this.myAgent.getFloor()) {
				processRequest();
			}
			else if(this.myAgent.getTaskList().get(0).getFloor() > this.myAgent.getFloor()) {
				this.realAgentPosition += this.myAgent.getSpeed() / this.myAgent.getFloorDistance();
			}
			else {
				this.realAgentPosition -= this.myAgent.getSpeed()/ this.myAgent.getFloorDistance();
			}
			
			updatePosition();
		}
		
		this.tick = this.tick + 1;
		if( this.tick % 2 == 0) {
			this.myAgent.writeToFile(tick, this.myAgent.getFloor());
		}
	}
	
	protected void updatePosition(){
		if(this.myAgent.getFloor() != (int) this.realAgentPosition) {
			this.myAgent.setFloor((int) this.realAgentPosition);
		}
	}
	

	protected void removeEntry() {
		if(!this.myAgent.getTaskList().isEmpty()) {
			this.myAgent.removeEntry();
		}
	}
	
	protected void processRequest() {
		this.myAgent.askRequestAgent();
		removeEntry();
	}

	@Override
	public void action() {
		if((int)this.repast.getTickCount()%(int)this.repast.getTicksBetweenMove() == 0) {
			this.moveLifts();
		}		
	}
	
	

}
