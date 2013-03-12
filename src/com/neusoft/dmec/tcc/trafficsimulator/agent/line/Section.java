package com.neusoft.dmec.tcc.trafficsimulator.agent.line;

import com.neusoft.dmec.tcc.trafficsimulator.agent.Agent;
import com.neusoft.dmec.tcc.trafficsimulator.agent.AgentContext;
import com.neusoft.dmec.tcc.trafficsimulator.model.BasicModelManager;
import com.neusoft.dmec.tcc.trafficsimulator.model.SectionModel;

public class Section extends Agent {
	
	SectionModel section_info;

	public Section(String sectId) {
		section_info = BasicModelManager.getInstance().getSectionModel(sectId);
	}

	@Override
	protected void doCurrentStepImpl(long time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toAgentDebugString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(AgentContext ctx) {
		// TODO Auto-generated method stub
		
	}
	
}
