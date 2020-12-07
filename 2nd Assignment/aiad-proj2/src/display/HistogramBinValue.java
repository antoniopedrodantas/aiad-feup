package display;

import agents.RequestAgent;
import uchicago.src.sim.analysis.BinDataSource;

public class HistogramBinValue implements BinDataSource{

	private boolean inOut;
	
	public HistogramBinValue(boolean inOut) {
		super();
		this.inOut = inOut;
	}

	@Override
	public double getBinValue(Object arg0) {
		RequestAgent request = (RequestAgent)arg0;
		if (inOut) {
			int value = request.getInFlow();
			if(value == -1) return (double)20;
			return (double)value;
		}
		else {
			int value = request.getOutFlow();
			if(value == -1) return (double)20;
			return (double)value;
		}
	}

}
