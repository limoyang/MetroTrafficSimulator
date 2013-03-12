package com.neusoft.dmec.tcc.trafficsimulator.engine.impl;

import java.util.HashMap;
import java.util.List;

import com.neusoft.dmec.tcc.trafficsimulator.engine.LineSimulateEngine;
import com.neusoft.dmec.tcc.trafficsimulator.tripplan.train.TrainTripPlan;
import com.neusoft.dmec.tcc.trafficsimulator.tripplan.train.TrainTripPlanElement;

public class SimpleLineSimulateEngineImpl implements LineSimulateEngine {
	
	class SimpleTrainManager{
		
		HashMap<String , SimpleSimulateTrain> train_map;

		SimpleTrainManager(){
			train_map = new HashMap<String , SimpleSimulateTrain>();
		}
		
		public SimpleSimulateTrain getTrain(String trainID) {
			return train_map.get(trainID);
		}

		public void addTrain(String trainId, SimpleSimulateTrain train) {
			train_map.put(trainId, train);
		}
		
	}

	class SimpleSimulateTrain {
		
		boolean  door_close_flag;
		SimuTrainStatus train_status;
		List<TrainTripPlanElement> train_plan;
		int cur_plan_idx;
		String train_id;
		
		public SimpleSimulateTrain(String trainId, List<TrainTripPlanElement> plan){
			train_id = trainId;
			cur_plan_idx = 0;
			door_close_flag = true;
			train_status = SimuTrainStatus.STOP_ARRIVED;
			train_plan = plan;
		}
		
		public TrainTripPlanElement getCurrentPlan(long curTime) {
			
			if(cur_plan_idx < 0){
				for(TrainTripPlanElement plan : train_plan){
					if(plan.depature_time >= curTime){
						cur_plan_idx++;
						break;
					}
				}
			}else{
				TrainTripPlanElement curPlan = train_plan.get(cur_plan_idx);
				if(curPlan.depature_time > curTime){
				}else{
					cur_plan_idx++;
				}
			}
				
			return  (cur_plan_idx >= 0 && cur_plan_idx < train_plan.size())?train_plan.get(cur_plan_idx):null;
		}

		public void nextStep(TrainSimuStepResult stepResult , long curTime) {
			
			TrainTripPlanElement pe = getCurrentPlan(curTime);
			if(pe == null){
				train_status =  SimuTrainStatus.TRIP_OVER;
			}else if(pe.arrive_time > curTime ){
				train_status =   SimuTrainStatus.MOVE_NORMAL;
			}else if(pe.arrive_time <= curTime && pe.door_open_time > curTime){
				train_status =   SimuTrainStatus.STOP_ARRIVED;
			}else if(pe.door_open_time <= curTime && pe.door_close_time > curTime){
				train_status =   SimuTrainStatus.STOP_DOOR_OPEN;
			}else if(pe.door_close_time <= curTime && pe.depature_time > curTime){
				if(door_close_flag == true){
					train_status =   SimuTrainStatus.STOP_DOOR_CLOSED;
				}else{
					train_status =   SimuTrainStatus.STOP_DOOR_OPEN;
				}
			}else if(pe.depature_time <= curTime ){
				if(train_status == SimuTrainStatus.STOP_DOOR_OPEN){
					if(door_close_flag == true){
						train_status =   SimuTrainStatus.STOP_DOOR_CLOSED;
					}
				}else if(train_status == SimuTrainStatus.STOP_DOOR_CLOSED) {
					train_status = SimuTrainStatus.MOVE_NORMAL;
				}
			}
			
			stepResult.status_map.put(train_id,train_status);
		}

		public boolean isTripOver() {
			return (train_status == SimuTrainStatus.TRIP_OVER);
		}
		
		
	}
	
	class TrainSimuStepResult{
		long current_time;
		HashMap<String , SimuTrainStatus> status_map;
		
		TrainSimuStepResult(){
			current_time = -1;
			status_map = new HashMap<String , SimuTrainStatus>();
		}
	}
	
	SimpleTrainManager train_manager;
	TrainSimuStepResult step_result;
	
	public SimpleLineSimulateEngineImpl() {
		
	}

	@Override
	public SimuTrainStatus getTrainStatus(String trainID, long curTime) {
		
		if(step_result.current_time != curTime)
			return null;
		else
			return step_result.status_map.get(trainID);
		
	}

	@Override
	public void notifyTrainHoldDoorOpen(String trainID, long curTime) {
		SimpleSimulateTrain train = train_manager.getTrain(trainID);
		train.door_close_flag = false;
	}

	@Override
	public void notifyTrainCanCloseDoor(String trainID, long curTime) {
		SimpleSimulateTrain train = train_manager.getTrain(trainID);
		if(train.door_close_flag == false){
			train.door_close_flag = true;
		}
	}

	@Override
	public boolean initEngine(String lid) {
		train_manager = new SimpleTrainManager();
		step_result = new TrainSimuStepResult();
		return true;
	}

	public void setTrainPlans(List<TrainTripPlan> plans){
		for(TrainTripPlan plan : plans){
			String trainId = plan.getTrainID();	
			SimpleSimulateTrain train = new SimpleSimulateTrain(trainId , plan.getStopList());
			train_manager.addTrain(trainId, train);
		}

	}

	@Override
	public void nextStep(long curTime) {
		
		step_result.current_time = curTime;
		step_result.status_map.clear();
		
		for(String trainId : train_manager.train_map.keySet()){
			SimpleSimulateTrain train = train_manager.getTrain(trainId);
			if(train.isTripOver() == false)
				train.nextStep(step_result , curTime);
		}
	}
}
