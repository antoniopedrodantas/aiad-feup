package behaviours;

import agents.LiftAgent;

import sajas.core.Agent;
import sajas.core.behaviours.TickerBehaviour;

@SuppressWarnings("serial")
public class LiftTickerBehaviour extends TickerBehaviour {
	
	private LiftAgent myAgent;
	private float realAgentPosition;
	private int tick;
	
	public LiftTickerBehaviour(Agent agent, long period) {
		super(agent, period);
		this.myAgent = (LiftAgent) agent;
		this.realAgentPosition = (float) this.myAgent.getFloor();
		this.tick = 0;
	}

	@Override
	protected void onTick() {
		
		if(!this.myAgent.getTaskList().isEmpty()) {
			if(this.myAgent.getTaskList().get(0).getFloor() == this.myAgent.getFloor()) {
				processRequest();
			}
			else if(this.myAgent.getTaskList().get(0).getFloor() > this.myAgent.getFloor()) {
				this.realAgentPosition += (float) (this.getPeriod() / 1000) / this.myAgent.getSpeed();
			}
			else {
				this.realAgentPosition -= (float) (this.getPeriod() / 1000) / this.myAgent.getSpeed();
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
	
	

}
