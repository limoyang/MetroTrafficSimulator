package com.neusoft.dmec.tcc.trafficsimulator.agent.train;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.neusoft.dmec.tcc.trafficsimulator.agent.Agent;
import com.neusoft.dmec.tcc.trafficsimulator.agent.AgentContext;
import com.neusoft.dmec.tcc.trafficsimulator.agent.line.Line;
import com.neusoft.dmec.tcc.trafficsimulator.agent.line.LineManager;
import com.neusoft.dmec.tcc.trafficsimulator.agent.person.Person;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Site;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.LineStation;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Station;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.StationManager;
import com.neusoft.dmec.tcc.trafficsimulator.agent.train.status.NormalStopStatus;
import com.neusoft.dmec.tcc.trafficsimulator.agent.train.status.TrainStatus;
import com.neusoft.dmec.tcc.trafficsimulator.agent.train.status.NormalStopStatus.TrainStopStep;
import com.neusoft.dmec.tcc.trafficsimulator.agent.train.status.TrainStatus.TrainStatusType;
import com.neusoft.dmec.tcc.trafficsimulator.engine.CoreSimulatorEngine;
import com.neusoft.dmec.tcc.trafficsimulator.model.BasicModelManager;
import com.neusoft.dmec.tcc.trafficsimulator.model.TrainModel;
import com.neusoft.dmec.tcc.trafficsimulator.tripplan.train.TrainTripPlan;
import com.neusoft.dmec.tcc.trafficsimulator.tripplan.train.TrainTripPlanElement;

public class Train extends Agent {
	
	class PassengerGroup{
		Station dest_station;
		List<Person> passenger_list;
	}
	
	/*
	 * basic info
	 */
	String train_id;
	TrainModel train_model;
	Line line;

	/*
	 * Status
	 */
	TrainStatus current_status;
	boolean is_normal; /*������������*/
	boolean is_exit; /*������������*/
	
	int person_count;
	
	
	/*
	 * trip plan
	 */
	TrainTripPlan train_plan;
	
	/*
	 * statics
	 */
	List<TrainTripRecord> trip_records;

	/*
	 * passengers
	 */
	List<PassengerGroup> passenger_groups;
	
	CoreSimulatorEngine simulator_engine;
	/*
	 * Constructor
	 */
	public Train(String tid , String lid , TrainTripPlan plan){
		train_id = tid;
		line = LineManager.getInstance().getLine(lid);
		is_normal = true;
		is_exit = false;
		train_model = BasicModelManager.getInstance().getTrainModelByLineId(lid);
		train_plan = plan;
	}
	
	
	/*
	 * simulator step event 
	 * @see com.neusoft.dmec.tcc.trafficsimulator.agent.Agent#nextStep(long)
	 */
	
	@Override
	protected void doCurrentStepImpl(long curTime) {
		if(is_exit)
			return;
		
		TrainTripPlanElement curPlan = train_plan.getCurrentPlanElement();
		current_status = simulator_engine.getCurrentTrainStatus(this , current_status , curPlan , curTime);
		if(current_status.getStatusType() == TrainStatusType.NORMAL_EXIT){
			is_exit = true;
		}
		
		if(current_status.getStatusType() == TrainStatusType.NORMAL_STOP){
			NormalStopStatus stopStatus = (NormalStopStatus)current_status;
			processTrainStop(curPlan.stop_station_id , curPlan.stop_site_id , stopStatus , curTime);
		}
		
		System.out.println(toAgentDebugString());
	}

	private void processTrainStop(String stopStationId, String stopSiteId, NormalStopStatus stopStatus, long curTime) {
		
		LineStation stopStation = (LineStation) StationManager.getInstance().getStation(stopStationId);
		Site stopSite = stopStation.getSite(stopSiteId);
		
		if( stopStatus.getStopStep() == TrainStopStep.DOOR_OPEN_OUT){
			/*
			 * 1. get all passengers who get off current station 
			 */
			for(PassengerGroup pg : passenger_groups){
				if(pg.dest_station == stopStation && pg.passenger_list.size() > 0){
					Person[] getoffPersons = pg.passenger_list.toArray(new Person[0]);
					for(Person person : getoffPersons){
						person.enterStationFromTrain(this , stopStation , stopSite , curTime);
						decrPersonCount();
					}
					pg.passenger_list.clear();
					break;
				}
				
			}
			
		}else if (stopStatus.getStopStep() == TrainStopStep.DOOR_OPEN_IN){
			/*
			 * passenger get in train
			 */
			if(stopSite == null || stopSite.waiting_persons.isEmpty())
				return;
			
			while(stopSite.waiting_persons.isEmpty() == false){
				if(isTrainFull() == false){
					Person person = stopSite.waiting_persons.remove(0);
					if(person != null)
						person.takeTrainFromStation(this , stopStation , curTime);					
				}else{
					/*
					 * train is full , the left persons could not get on train
					 */
					return;
				}
			}
			
		}
		
	}
	
	public void addPerson(Person person , Station destStation){
	
		for(PassengerGroup pg : passenger_groups){
			if(pg.dest_station == destStation){
				pg.passenger_list.add(person);
				incrPersonCount();
				return;
			}			
		}		
	}
	
	public void incrPersonCount(){
		person_count++;
	}
	
	public void decrPersonCount(){
		person_count--;
	}

	public boolean isTrainFull(){
		
		return person_count >= train_model.max_capacity;
	}

	public boolean isExit(){
		return is_exit;
	}

	public String getID() {
		return train_id;
	}

	public boolean isPassengerAllGetOff(String stopStationId) {
		
		for(PassengerGroup pg : passenger_groups){
			if(pg.dest_station.getId().equals(stopStationId) && pg.passenger_list.isEmpty() == false){
				return false;
			}
			
		}
		return true;
	}

	@Override
	public String toAgentDebugString() {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("Train <");
		strBuf.append("id = ").append(train_id).append(";");
		strBuf.append("line = ").append(train_model.line_id).append(";");
		strBuf.append("persons = ").append(person_count).append(";");
		if(current_status.getStatusType() == TrainStatusType.NORMAL_STOP){
			strBuf.append(" status[ stopping at station :").append(train_plan.getCurrentPlanElement().stop_station_id).append(";");
		}else if(current_status.getStatusType() == TrainStatusType.NORMAL_MOVE){
			strBuf.append(" status[ moving to next station :").append(train_plan.getCurrentPlanElement().stop_station_id).append(";");
		}else if(current_status.getStatusType() == TrainStatusType.NORMAL_EXIT){
			strBuf.append(" status[ train exit;");
		}
		strBuf.append(current_status).append(";").append("]>");
		return strBuf.toString();
	}

	@Override
	public void init(AgentContext ctx) {
		
		
		simulator_engine = ctx.getSimulateEngine();
		
		trip_records = new LinkedList<TrainTripRecord>();
		
		/*
		 * init status
		 */
		TrainTripPlanElement firstPlan = train_plan.getStartPlanElement();
		if(firstPlan == null)
			return;
		
		current_status = new NormalStopStatus(firstPlan);
		passenger_groups = new LinkedList<PassengerGroup>();
		
		for(TrainTripPlanElement pe : train_plan.getStopList()){
			PassengerGroup pasGroup = new PassengerGroup();
			pasGroup.dest_station = (LineStation) StationManager.getInstance().getStation(pe.stop_station_id);
			pasGroup.passenger_list = new LinkedList<Person>();
			passenger_groups.add(pasGroup);
		}
	}


	public void gotoNextPlanElement() {
		train_plan.gotoNextPlanElement();
	}


	public Line getLine() {
		return line;
	}


	public void startTrip() {
		line.addTrain(this);
	}
	
	public void exitTrip() {
		line.removeTrain(this);
	}
	
}
