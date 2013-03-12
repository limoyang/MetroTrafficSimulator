package com.neusoft.dmec.tcc.trafficsimulator.agent.station;

import com.neusoft.dmec.tcc.trafficsimulator.agent.Location;
import com.neusoft.dmec.tcc.trafficsimulator.model.GateModel;

public class Gate extends Location{
	GateModel gate_model;
	LineStation station;
	
	public Gate(GateModel model , LineStation sta) {
		gate_model = model;
		station = sta;
	}
	
	public String getId(){
		return station.getId() + ":" + gate_model.gate_name;
	}

	public boolean isGate() {
		return true;
	}
}
