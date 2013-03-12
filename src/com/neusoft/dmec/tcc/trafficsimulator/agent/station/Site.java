package com.neusoft.dmec.tcc.trafficsimulator.agent.station;

import java.util.LinkedList;
import java.util.List;

import com.neusoft.dmec.tcc.trafficsimulator.agent.Agent;
import com.neusoft.dmec.tcc.trafficsimulator.agent.AgentContext;
import com.neusoft.dmec.tcc.trafficsimulator.agent.Location;
import com.neusoft.dmec.tcc.trafficsimulator.agent.person.Person;
import com.neusoft.dmec.tcc.trafficsimulator.model.BasicModelManager;
import com.neusoft.dmec.tcc.trafficsimulator.model.LineModel.Direction;
import com.neusoft.dmec.tcc.trafficsimulator.model.SiteModel;

public class Site  extends Location {
	SiteModel site_model;
	LineStation station;
	public List<Person> waiting_persons;
	public List<Person> leaving_persons;
	

	public Site(SiteModel sm , LineStation sta) {
		site_model = sm;
		station = sta;
		waiting_persons = new LinkedList<Person>();
		leaving_persons = new LinkedList<Person>();
		
	}

	public Direction getDirection() {
		return site_model.direction;
	}

	public String getId() {
		return station.getId() + ":" + site_model.site_name;
	}
	
	public boolean isSite(){
		return true;
	}
}
