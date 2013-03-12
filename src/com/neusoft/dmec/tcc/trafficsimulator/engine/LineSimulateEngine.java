package com.neusoft.dmec.tcc.trafficsimulator.engine;

import java.util.List;

import com.neusoft.dmec.tcc.trafficsimulator.tripplan.train.TrainTripPlan;
import com.neusoft.dmec.tcc.trafficsimulator.tripplan.train.TrainTripPlanElement;

public interface LineSimulateEngine {
	public enum SimuTrainStatus { MOVE_NORMAL , MOVE_FAULT , STOP_ARRIVED , STOP_DOOR_OPEN , STOP_DOOR_CLOSED, TRIP_OVER };
	public boolean initEngine(String lid);
	public SimuTrainStatus getTrainStatus(String trainID, long curTime);
	public void notifyTrainHoldDoorOpen(String trainID, long curTime);
	public void notifyTrainCanCloseDoor(String trainID, long curTime);
	public void setTrainPlans(List<TrainTripPlan> plans);
	public void nextStep(long curTime);
}
