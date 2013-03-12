package com.neusoft.dmec.tcc.trafficsimulator.agent;

public abstract class Agent {
	long last_step_time;
	protected AgentContext agent_ctx;

	public void doCurrentStep(long curTime){
		if(curTime > last_step_time){
			doCurrentStepImpl(curTime);
			last_step_time = curTime;
		}
	}
	public abstract void init(AgentContext ctx);
	public abstract String toAgentDebugString();
	
	protected abstract void doCurrentStepImpl(long curTime);
}
