package com.neusoft.dmec.tcc.trafficsimulator.agent;

import com.neusoft.dmec.tcc.trafficsimulator.engine.CoreSimulatorEngine;

public class AgentContext {

	CoreSimulatorEngine simu_engine;
	long step_time_length;
	
	public CoreSimulatorEngine getSimulateEngine() {
		return simu_engine;
	}

	public void setSimulateEngine(CoreSimulatorEngine engine) {
		simu_engine = engine;
	}

	public long getStepTimeLength() {
		return step_time_length;
	}

	public void setStepTimeLength(long len) {
		step_time_length = len;
	}

}
