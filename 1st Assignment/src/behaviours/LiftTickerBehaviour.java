package behaviours;

import agents.LiftAgent;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

@SuppressWarnings("serial")
public class LiftTickerBehaviour extends TickerBehaviour {
	
	private LiftAgent myAgent;
	private float realAgentPosition;
	private int i;
	
	public LiftTickerBehaviour(Agent agent, long period) {
		super(agent, period);
		this.myAgent = (LiftAgent) agent;
		this.realAgentPosition = (float) this.myAgent.getFloor();
		i = 0;
	}

	@Override
	protected void onTick() {
		
		System.out.println("ticking");
		
		if (i > 4)
			this.processRequest();
		else
			i++;
		/*if(!this.myAgent.getTaskList().isEmpty()) {
			if(this.myAgent.getTaskList().get(0).getFloor() == this.myAgent.getFloor()) {
				processRequest();
			}
			else if(this.myAgent.getTaskList().get(0).getFloor() > this.myAgent.getFloor()) {
				this.realAgentPosition += (float) this.getPeriod() / this.myAgent.getSpeed();
			}
			else {
				this.realAgentPosition -= (float) this.getPeriod() / this.myAgent.getSpeed();
			}
			
			updatePosition();
		}
		*/
	}
	
	protected void updatePosition(){
		if(this.myAgent.getFloor() != (int) this.realAgentPosition) {
			this.myAgent.setFloor((int) this.realAgentPosition);
		}
	}
	
	protected void updateWeight(int op, int quantity) {
		float totalWeight = quantity * 75; //75 is the average weight that we will assume for each person entering and exiting the elevator
		
		switch(op) {
			case 1:
				this.myAgent.addWeight(totalWeight);
				break;
			case -1:
				this.myAgent.subWeight(totalWeight);
				break;
			default:
				break;
		}
	}
	
	protected void removeEntry() {
		if(!this.myAgent.getTaskList().isEmpty()) {
			this.myAgent.removeEntry();
		}
	}
	
	protected void processRequest() {
		// mandar mensagem ao requestAgent com o pedido que estamos a atender(1º da lista)
		this.myAgent.askRequestAgent();
		//processar mensagem
			/*
			 entrar e sair( notar que entrada pode ser carregar num botao ou mais, ou em nenhum) EXEMPLO: "E:2[5-6],S:1"
			 sair EXEMPLO "S:1"
			 entrar (pode entrar e nao carregar no botao OU carregar e tocar em um ou varios) EXEMPLO : "E:3" ou "E:1[1]"
			 */
		//eliminar 1º elemento da queue
		//removeEntry();
	}
	
	

}
