package com.neusoft.dmec.tcc.trafficsimulator.generator;

import java.util.List;

import com.neusoft.dmec.tcc.trafficsimulator.agent.person.Person;
import com.neusoft.dmec.tcc.trafficsimulator.odtable.ODPathTable;
import com.neusoft.dmec.tcc.trafficsimulator.odtable.ODProportionTable;

public interface PersonGenerator {
	public List<Person> getNewPersons(long curTime);
	public void setPersonEnterCounts(long startTime , long unitLength , int[] counts);
	public void setODProportionTable(ODProportionTable odTable);
	public void setODPathTable(ODPathTable odTable);
	
}
