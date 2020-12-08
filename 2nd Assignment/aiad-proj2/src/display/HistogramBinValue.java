package display;

import agents.LiftAgent;
import agents.RequestAgent;
import uchicago.src.sim.analysis.BinDataSource;

public class HistogramBinValue implements BinDataSource{
	
	public HistogramBinValue() {
		super();
	}

	@Override
	public double getBinValue(Object arg0) {
		LiftAgent lift = (LiftAgent)arg0;
		
		return lift.getCurrentWeight();
	}

}
