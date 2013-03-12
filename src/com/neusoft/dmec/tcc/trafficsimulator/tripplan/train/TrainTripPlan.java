package com.neusoft.dmec.tcc.trafficsimulator.tripplan.train;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.neusoft.dmec.tcc.trafficsimulator.agent.station.LineStation;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.StationManager;
import com.neusoft.dmec.tcc.trafficsimulator.model.BasicModelManager;
import com.neusoft.dmec.tcc.trafficsimulator.model.LineModel.Direction;
import com.neusoft.dmec.tcc.trafficsimulator.model.TrainModel;

public class TrainTripPlan {
	
	enum TrainCommand { NORMAL , CLEAR , PASS };
	
	List<TrainTripPlanElement>  stop_list;
	String train_id;
	String line_id;
	boolean is_started;
	boolean is_over;
	int cur_index;
	Direction direction;
	
	public TrainTripPlan(String tid , String lid , Direction dir){
		train_id = tid;
		line_id = lid;
		direction = dir;
		is_started = false;
		is_over = false;
		cur_index = 0;
	}

	/*
	 * initialization train plan by records
	 */

	public boolean init(List<Map<String, Object>> tripRecords) {
		
		stop_list = new LinkedList<TrainTripPlanElement>();
		
		for(Map<String, Object> record:(List<Map<String,Object>>)tripRecords){
			String stationID = (String)record.get("Station_ID");
			Date arriveTime = (Date)record.get("Arrive_Tm");
			Date depatureTime = (Date)record.get("Deptr_Tm");
			
			TrainTripPlanElement pe = new  TrainTripPlanElement();
			pe.stop_station_id = stationID;
			pe.stop_site_id = getStopSiteID();
			pe.arrive_time = arriveTime.getTime();
			pe.depature_time = depatureTime.getTime();
			pe.door_open_time = getDoorOpenTime(pe.arrive_time);
			pe.door_close_time = getDoorCloseTime(pe.depature_time);
			pe.command = TrainCommand.NORMAL;
			
			stop_list.add(pe);
		}
		
		return true;
	}
	
	private String getStopSiteID() {
		if(direction == Direction.UPWARD)
			return "UPWARD";
		else
			return "DOWNWARD";
	}

	private long getDoorOpenTime(long arriveTime){
		
		TrainModel tinf = BasicModelManager.getInstance().getTrainModelByLineId(line_id);
		return arriveTime + tinf.time_before_door_open;
		
	}
	
	private long getDoorCloseTime(long depatureTime){
		TrainModel tinf = BasicModelManager.getInstance().getTrainModelByLineId(line_id);
		return depatureTime - tinf.time_before_door_close;
		
	}

	public TrainTripPlanElement getCurrentPlanElement(){
		if(cur_index < stop_list.size()){
			return stop_list.get(cur_index);
		}else
			return stop_list.get(stop_list.size()-1);
	}
	
	public void gotoNextPlanElement(){
		cur_index++;
	}
	
	public TrainTripPlanElement getStartPlanElement(){
		if(stop_list != null && stop_list.isEmpty() == false){
			return stop_list.get(0);
		}else
			return null;
	}
	
	public List<TrainTripPlanElement> getStopList(){
		return stop_list;
	}

	public String getTrainID() {
		return train_id;
	}
	
	public long getStartTime(){
		if(stop_list == null || stop_list.size() <= 0){
			return -1;
		}else{
			return stop_list.get(0).arrive_time;
		}
	}
	
	public void startPlan(){
		is_started = true;
	}
	
	public void stopPlan(){
		is_over = true;
	}
	
	public boolean isStarted(){
		return is_started;
	}
	
	public boolean isOver(){
		return is_over;
	}

	public String getLineID() {
		return line_id;
	}

	
}
