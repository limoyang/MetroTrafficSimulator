package com.neusoft.dmec.tcc.trafficsimulator.agent.station;


import java.util.LinkedList;
import java.util.List;

import com.neusoft.dmec.tcc.trafficsimulator.agent.Agent;
import com.neusoft.dmec.tcc.trafficsimulator.agent.AgentContext;
import com.neusoft.dmec.tcc.trafficsimulator.agent.person.Person;
import com.neusoft.dmec.tcc.trafficsimulator.agent.person.status.PersonStatus.PersonStatusType;
import com.neusoft.dmec.tcc.trafficsimulator.generator.DefaultPersonGenerator;
import com.neusoft.dmec.tcc.trafficsimulator.generator.PersonGenerator;
import com.neusoft.dmec.tcc.trafficsimulator.model.StationModel;
import com.neusoft.dmec.tcc.trafficsimulator.odtable.ODPathTable;
import com.neusoft.dmec.tcc.trafficsimulator.odtable.ODProportionTable;


public abstract class Station extends Agent {
	
	List<Person> enter_walking_persons;
	List<Person> exit_walking_persons;
	int exit_persons = 0;
	int enter_persons = 0;
	PersonGenerator person_generator;
	StationModel station_model;

	Station(){
		enter_walking_persons = new LinkedList<Person>();
		exit_walking_persons = new LinkedList<Person>();
	}
	
	public void init(AgentContext ctx){
		agent_ctx = ctx;
		person_generator = new DefaultPersonGenerator(station_model.station_id , agent_ctx.getStepTimeLength());		
	}

	public abstract String getId();
	public abstract boolean isTransferStation();
	public abstract Site[] getSites();
	public abstract Gate[] getGates();
	public abstract Station getNormalStation();
	public abstract void addTransferPerson(Person person); 

	
	public void addEnterPerson(Person person) {
		enter_walking_persons.add(person);
		enter_persons++;
	}
	
	public void addExitPerson(Person person) {
		exit_walking_persons.add(person);
	}
	
	public void removeExitPerson(Person person) {
		exit_walking_persons.remove(person);
		exit_persons++;
	}

	public void removeEnterPerson(Person person) {
		enter_walking_persons.remove(person);		
	}

	public List<Person> getNewStartPersons(long curTime) {
		return person_generator.getNewPersons(curTime);
	}
	
	public void setPersonEnterCounts(long startTime, long statLen, int[] counts) {
		if(person_generator != null){
			person_generator.setPersonEnterCounts(startTime, statLen, counts);
		}
	}
	
	public void setODProportionTable(ODProportionTable odTable) {
		if(person_generator != null){
			person_generator.setODProportionTable(odTable);
		}
	}

	public void setODPathTable(ODPathTable pathTable) {
		if(person_generator != null){
			person_generator.setODPathTable(pathTable);
		}
		
	}

	public List<Person> getEnterWalkingPersons() {
		return enter_walking_persons;
	}	
	
	public List<Person> getExitWalkingPersons() {
		return exit_walking_persons;
	}

	@Override
	protected void doCurrentStepImpl(long curTime) {
		
		Person[] enterPersons = enter_walking_persons.toArray(new Person[0]);
		if(enterPersons != null && enterPersons.length > 0){
			for(Person p : enterPersons){
				p.doCurrentStep(curTime);
			}
		}

		Person[] exitPersons = exit_walking_persons.toArray(new Person[0]);
		if(exitPersons != null && exitPersons.length > 0){
			for(Person p : exitPersons){
				p.doCurrentStep(curTime);
			}
		}
		
		for(Site site : getSites()){
			Person[] waitPersons = (Person[]) site.waiting_persons.toArray(new Person[0]);
			if(waitPersons != null && waitPersons.length > 0){
				for(Person p : waitPersons){
					p.doCurrentStep(curTime);
				}
			}
			
			while(site.leaving_persons.isEmpty() == false){
				Person p = site.leaving_persons.remove(0);
				PersonStatusType statusType = p.getCurrentStatus().getStatusType();
				if(statusType == PersonStatusType.EXIT_STATION)
					addExitPerson(p);
				else if(statusType == PersonStatusType.TRANSFER_STATION)
					addTransferPerson(p);
			}
			
		}
	}
	
	
}
