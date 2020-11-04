package behaviours;

import agents.LiftAgent;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

@SuppressWarnings("serial")
public class LiftTickerBehaviour extends TickerBehaviour {
	
	private LiftAgent myAgent;
	private float realAgentPosition;
	
	public LiftTickerBehaviour(Agent agent, long period) {
		super(agent, period);
		this.myAgent = (LiftAgent) agent;
		this.realAgentPosition = (float) this.myAgent.getFloor();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onTick() {
		if(!this.myAgent.getTaskList().isEmpty()) {
			if(this.myAgent.getTaskList().get(0).getFloor() == this.myAgent.getFloor()) {
				processRequest();
			}
			else if(this.myAgent.getTaskList().get(0).getFloor() == this.myAgent.getFloor()) {
				this.realAgentPosition += (float) this.getPeriod() / this.myAgent.getSpeed();
			}
			else {
				this.realAgentPosition -= (float) this.getPeriod() / this.myAgent.getSpeed();
			}
			updatePosition();
		}
	}
	
	protected void updatePosition(){
		if(this.myAgent.getFloor() != (int) this.realAgentPosition) {
			this.myAgent.setFloor((int) this.realAgentPosition);
		}
	}
	
	protected void updateWeight() {
		
	}
	
	protected void removeEntry() {
		
	}
	
	protected void processRequest() {
		// mandar mensagem ao requestAgent com o pedido que estamos a atender(1º da lista)
		//processar mensagem
			/*
			 entrar e sair( notar que entrada pode ser carregar num botao ou mais, ou em nenhum) EXEMPLO: "E:2[5-6],S:1"
			 sair EXEMPLO "S:1"
			 entrar (pode entrar e nao carregar no botao OU carregar e tocar em um ou varios) EXEMPLO : "E:3" ou "E:1[1]"
			 */
		//eliminar 1º elemento da queue
	}

}
