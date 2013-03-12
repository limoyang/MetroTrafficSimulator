package com.neusoft.dmec.tcc.trafficsimulator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.neusoft.dmec.tcc.trafficsimulator.engine.impl.SimpleLineSimulateEngineImpl;
import com.neusoft.dmec.tcc.trafficsimulator.engine.impl.SimpleStationSimulateEngineImpl;
import com.neusoft.dmec.tcc.trafficsimulator.generator.PersonGenerator;
import com.neusoft.dmec.tcc.trafficsimulator.odtable.ODPathTable;
import com.neusoft.dmec.tcc.trafficsimulator.odtable.ODProportionTable;
import com.neusoft.dmec.tcc.trafficsimulator.tripplan.train.TrainTripPlan;

public class NetworkTrafficSimulatorConfig {
	
	HashSet<String>  lines_set;

	HashMap<String , LinkedList<TrainTripPlan>>  train_plan_map;
	HashMap<String , int[]>  station_enter_map;
	//HashMap<String , ODProportionTable> od_proportion_map;
	ODProportionTable od_proportion_table;
	ODPathTable od_path_table;
	

	HashMap<String , Class<?>>  line_engine_class_map;
	HashMap<String , Class<?>>  station_engine_class_map;
	
	public long simu_start_time;
	public long simu_end_time;
	public long simu_step_len;
	public long station_enter_statistic_len;

	
	public NetworkTrafficSimulatorConfig(){
		
		lines_set = new HashSet<String>();
		train_plan_map = new HashMap<String , LinkedList<TrainTripPlan>>();
		
		station_enter_map = new HashMap<String , int[]>();  
		//od_proportion_map = new HashMap<String , ODProportionTable>();
		
		line_engine_class_map = new HashMap<String , Class<?>>();
		station_engine_class_map = new HashMap<String , Class<?>>();
	}
			
	public String[] getAllLines(){
		if(lines_set.size() > 0){
			String[] retArray = new String[lines_set.size()];
			lines_set.toArray(retArray);
			return retArray;
		}else
			return null;
	}

	public void addEnterStationStatistics(String station , int[] enterCounts) {
		station_enter_map.put(station, enterCounts);
	}
	
	public void addODProportionTable(ODProportionTable table) {
		//od_proportion_map.put(station, table);
		od_proportion_table = table;
	}

	public void setODPathTable(ODPathTable table) {
		od_path_table = table;
	}

	
	public void addTrainTripPlan(TrainTripPlan trainPlan) {
	
		String lid = trainPlan.getLineID();
		
		lines_set.add(lid);
		
		LinkedList<TrainTripPlan> lineTripList = train_plan_map.get(lid);
		if(lineTripList == null){
			lineTripList = new LinkedList<TrainTripPlan>();
			lineTripList.add(trainPlan);
			train_plan_map.put(lid, lineTripList);
		}else{
			long startTime = trainPlan.getStartTime();
			
			int idx = 0;
			for(TrainTripPlan plan : lineTripList){
				if(startTime < plan.getStartTime()){
					lineTripList.add(idx, trainPlan);
					return;
				}
				idx++;
			}
			lineTripList.add(trainPlan);
		}
	}

	public List<TrainTripPlan> getLineTrainPlans(String lid) {
		return train_plan_map.get(lid);
	}
	
	public List<TrainTripPlan> getAllTrainPlans() {
		
		LinkedList<TrainTripPlan> retList = new LinkedList<TrainTripPlan>();
		for(String lid : lines_set){
			retList.addAll(train_plan_map.get(lid));
		}
		return retList;
	}

	public List<TrainTripPlan> getTrainPlans(String lineId) {
		return train_plan_map.get(lineId);
	}

	public void registLineEngineClass(String lid , Class<?> clz) {
		line_engine_class_map.put(lid, clz);
	}
	
	public void registStationEngineClass(String sid , Class<?> clz) {
		station_engine_class_map.put(sid, clz);
	}

	public Class<?> getLineEngineClass(String lid) {
		Class<?> clz = line_engine_class_map.get(lid);
		if(clz != null)
			return clz;
		else
			return SimpleLineSimulateEngineImpl.class;
	}
	
	public Class<?> getStationEngineClass(String sid) {
		Class<?> clz = station_engine_class_map.get(sid);
		if(clz != null)
			return clz;
		else
			return SimpleStationSimulateEngineImpl.class;
	}

	public int[] getEnterStationCounts(String id) {
		return station_enter_map.get(id);
	}

	public ODProportionTable getODProportionTable() {
		return od_proportion_table;
	}

	public ODPathTable getODPathTable() {
		return od_path_table;
	}
}
