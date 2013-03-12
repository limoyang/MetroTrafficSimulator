package com.neusoft.dmec.tcc.trafficsimulator.agent.person;

import java.util.List;

import com.neusoft.dmec.tcc.trafficsimulator.agent.Agent;
import com.neusoft.dmec.tcc.trafficsimulator.agent.AgentContext;
import com.neusoft.dmec.tcc.trafficsimulator.agent.Location;
import com.neusoft.dmec.tcc.trafficsimulator.agent.line.Line;
import com.neusoft.dmec.tcc.trafficsimulator.agent.line.LineManager;
import com.neusoft.dmec.tcc.trafficsimulator.agent.person.status.EnterStationStatus;
import com.neusoft.dmec.tcc.trafficsimulator.agent.person.status.ExitStationStatus;
import com.neusoft.dmec.tcc.trafficsimulator.agent.person.status.PersonStatus;
import com.neusoft.dmec.tcc.trafficsimulator.agent.person.status.PersonStatus.PersonStatusType;
import com.neusoft.dmec.tcc.trafficsimulator.agent.person.status.TakeTrainStatus;
import com.neusoft.dmec.tcc.trafficsimulator.agent.person.status.TransferStatus;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Gate;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Station;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.TransferStation;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Site;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.LineStation;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.StationManager;
import com.neusoft.dmec.tcc.trafficsimulator.agent.train.Train;
import com.neusoft.dmec.tcc.trafficsimulator.engine.CoreSimulatorEngine;
import com.neusoft.dmec.tcc.trafficsimulator.model.LineModel.Direction;
import com.neusoft.dmec.tcc.trafficsimulator.odtable.ODPathElement;
import com.neusoft.dmec.tcc.trafficsimulator.tripplan.person.PersonTripPlan;

public class Person extends Agent {
	
	static long id_count = 1; 
	
	String person_id;
	long trip_start_time;
	
	PersonTripPlan trip_plan;
	int cur_plan_index;

	PersonStatus pre_status;
	PersonStatus current_status;
	Station current_station;
	Location current_from_location;
	Location current_to_location;
	
	List<PersonTripRecord>  trip_records;
	
	boolean is_started;
	boolean is_exit;
	
	CoreSimulatorEngine simulator_engine;

	
	public Person(PersonTripPlan plan){
		person_id = generatePersonID();
		is_started = false;
		is_exit = false;
		trip_plan = plan;
		cur_plan_index = 0;

	}

	@Override
	protected void doCurrentStepImpl(long curTime) {
		
		if(is_exit)
			return;
		
		changeCurrentStatus(simulator_engine.getCurrentPersonStatus(this , current_status , curTime));

		PersonStatusType preStatusType = pre_status.getStatusType();
		PersonStatusType curStatusType = current_status.getStatusType();
		
		if(curStatusType == PersonStatusType.TRIP_OVER){
			is_exit = true;
			if(preStatusType == PersonStatusType.EXIT_STATION){
				exitStation(trip_plan.d_station , curTime);
			}						
		}else if(curStatusType == PersonStatusType.WAIT_TRAIN){
			ODPathElement curPlan = trip_plan.getPathElement(cur_plan_index);	
			if(preStatusType == PersonStatusType.ENTER_STATION){
				Line line = LineManager.getInstance().getLine(curPlan.line.line_id);
				Station station = StationManager.getInstance().getStation(curPlan.start_station.station_id);
				waitTrainFromEnter(line , station.getNormalStation() , curPlan.direction);
			}else if(preStatusType == PersonStatusType.TRANSFER_STATION){
				ODPathElement prePlan = trip_plan.getPreviousPathElement(cur_plan_index);
				if(prePlan != null){
					waitTrainFromTransfer();
				}else
					assert(true);
			}
		
		}else if(curStatusType == PersonStatusType.TRANSFER_STATION){
			assert(true);
			if(preStatusType == PersonStatusType.TAKE_TRAIN){
				//getOffTrain();
			}			
		}else if(curStatusType == PersonStatusType.EXIT_STATION){
			
			
			
		}else{
		}	
	}	
	

	@Override
	public String toAgentDebugString() {
		StringBuffer buf = new StringBuffer();
		buf.append("id=").append(person_id).append(";");
		buf.append("o_sta=").append(trip_plan.o_station.getId()).append(";");
		buf.append("o_gate=").append(trip_plan.o_station_gate.getId()).append(";");		
		buf.append("d_sta=").append(trip_plan.d_station.getId()).append(";");
		buf.append("d_gate=").append(trip_plan.d_station_gate.getId()).append(";");

		return buf.toString();
	}

	@Override
	public void init(AgentContext ctx) {
		simulator_engine = ctx.getSimulateEngine();
	}
	
	private void changeCurrentStatus(PersonStatus curStatus){
		pre_status = current_status;
		current_status = curStatus;
	}
	
	public void startTrip(long time){
		trip_start_time = time;
		is_started = true;
		enterStation(trip_plan.o_station , time);
	}
	

	public void waitTrainFromTransfer() {
		
		if(current_station.isTransferStation() == false || current_to_location.isSite() == false)
			return;
		
		Site site = (Site)current_to_location;
		site.waiting_persons.add(this);
		
		((TransferStation) current_station).removeTransferPerson(this);

	}

	public void waitTrainFromEnter(Line line , Station station, Direction dir) {
		Site site;
		if(station.isTransferStation()){
			site = ((TransferStation)station).getSiteByLineDirection(line , dir);
		}else{
			site = ((LineStation)station).getSiteByDirection(dir);
		}
		site.waiting_persons.add(this);
		station.removeEnterPerson(this);
	}

	public void enterStationFromTrain(Train train, LineStation stopStation, Site stopSite, long curTime) {
		if(current_status.getStatusType() == PersonStatusType.TAKE_TRAIN){
			Station curStation = null;
			if(stopStation.isSubStation()){
				curStation = stopStation.getTransferStation();
			}else
				curStation = stopStation;
			
			if(trip_plan.d_station == curStation){
				/*
				 * exit station
				 */
				changeCurrentStatus(new ExitStationStatus(curStation , stopSite , trip_plan.o_station_gate , curTime));
				
				stopSite.leaving_persons.add(this);
				current_station = curStation;
				current_from_location = stopSite;
				current_to_location = trip_plan.d_station_gate;
				
				simulator_engine.addPerson(stopStation.getId(), this, curTime);			
			
			}else{
				/*
				 * transfer
				 */
				ODPathElement curPlan = trip_plan.getPathElement(cur_plan_index);
				if(curPlan.start_station.is_sub_station){
					TransferStation tranStation = (TransferStation)curStation;
					Line line = LineManager.getInstance().getLine(curPlan.line.line_id);
					Site toSite = tranStation.getSiteByLineDirection(line, curPlan.direction);
					
					changeCurrentStatus(new TransferStatus(tranStation , stopSite , toSite, curTime));
					
					stopSite.leaving_persons.add(this);
					current_station = tranStation;
					current_from_location = stopSite;
					current_to_location = toSite;
					simulator_engine.addPerson(stopStation.getId(), this, curTime);
				}
			}
			
		}else{
			assert(true);
		}
	}

	public void takeTrainFromStation(Train train, LineStation stopStation , long curTime) {
		if(current_status.getStatusType() == PersonStatusType.WAIT_TRAIN){
			ODPathElement curPlan = trip_plan.getPathElement(cur_plan_index);
			Station startStation = StationManager.getInstance().getStation(curPlan.start_station.station_id);
			Station endStation = StationManager.getInstance().getStation(curPlan.end_station.station_id);
			
			changeCurrentStatus(new TakeTrainStatus(train , train.getLine() , startStation , endStation , curTime));
			
			current_station = null;
			current_from_location = null;
			current_to_location = null;
			cur_plan_index++;
			
			train.addPerson(this, endStation);
			simulator_engine.removePerson(stopStation.getId(), this, curTime);
			
		}else{
			assert(true);
		}	
	}

	public void enterStation(Station station , long curTime){
			current_station = station;
			current_from_location = trip_plan.o_station_gate;
			
			ODPathElement curPlan = trip_plan.getPathElement(cur_plan_index);
			if(station.isTransferStation()){
				Line line = LineManager.getInstance().getLine(curPlan.line.line_id);
				current_to_location = ((TransferStation) station).getSiteByLineDirection(line,curPlan.direction);
			}else{
				current_to_location = ((LineStation) station).getSiteByDirection(curPlan.direction);
				
			}
			
			changeCurrentStatus(new EnterStationStatus(current_station , (Gate)current_from_location , (Site)current_to_location , curTime));
			
			station.addEnterPerson(this);			
			simulator_engine.addPerson(station.getId() , this , curTime);
	}
	
	public void exitStation(Station station , long curTime){
		is_exit = true;

		station.removeExitPerson(this);
		simulator_engine.removePerson(station.getId(), this, curTime);
		
		current_station = null;
	}
	

	public String getID() {
		return person_id;
	}

	private String generatePersonID() {
		return Long.toString(id_count++);
	}

	public Station getCurrentStation() {
		return current_station;
	}
	
	public Location getCurrentFromLocation(){
		return current_from_location;
	}
	
	public Location getCurrentToLocation(){
		return current_to_location;
	}
	
	public PersonStatus getCurrentStatus(){
		return current_status;
	}
}
