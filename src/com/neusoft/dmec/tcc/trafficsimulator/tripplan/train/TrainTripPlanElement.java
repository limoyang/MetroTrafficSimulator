package com.neusoft.dmec.tcc.trafficsimulator.tripplan.train;

import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Site;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.LineStation;
import com.neusoft.dmec.tcc.trafficsimulator.tripplan.train.TrainTripPlan.TrainCommand;

public class TrainTripPlanElement{
	public String  stop_station_id;
	public String stop_site_id;
	public long arrive_time;
	public long door_open_time;
	public long door_close_time;
	public long depature_time;
	public TrainCommand command;
	
	public String toString(){
		return stop_station_id;
	}
}