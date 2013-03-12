package com.neusoft.dmec.tcc.trafficsimulator.agent.train.status;

import com.neusoft.dmec.tcc.trafficsimulator.agent.train.Train;
import com.neusoft.dmec.tcc.trafficsimulator.agent.train.status.NormalStopStatus.TrainStopStep;
import com.neusoft.dmec.tcc.trafficsimulator.tripplan.train.TrainTripPlanElement;

public class NormalStopStatus extends TrainStatus{
	
	public static enum TrainStopStep { TRAIN_ARRIVED , DOOR_OPEN_OUT , DOOR_OPEN_IN , DOOR_CLOSED , TRAIN_DEPARTURE};
	TrainStopStep stop_step;
	long step_start_time;


	public NormalStopStatus(TrainTripPlanElement startPlanElement) {
		stop_step = TrainStopStep.TRAIN_ARRIVED;
		step_start_time = startPlanElement.arrive_time;

	}

	void gotoNextStep(){
		switch(stop_step){
		case TRAIN_ARRIVED:
			stop_step = TrainStopStep.DOOR_OPEN_OUT;
			break;
		case DOOR_OPEN_OUT:
			stop_step = TrainStopStep.DOOR_OPEN_IN;
			break;
		case DOOR_OPEN_IN:
			stop_step = TrainStopStep.DOOR_CLOSED;
			break;
		case DOOR_CLOSED:
			stop_step = TrainStopStep.TRAIN_DEPARTURE;
			break;
		}
	}

	public TrainStopStep getStopStep(){
		return stop_step;
	}

	public void setStopStep(TrainStopStep step) {
		stop_step = step;
	}	

	public long getStepStartTime(){
		return step_start_time;
	}

	public void setStepStartTime(long time) {
		step_start_time = time;
	}	

	
	@Override
	public TrainStatusType getStatusType() {
		return TrainStatusType.NORMAL_STOP;
	}

	@Override
	public String toString() {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("Normal Stop (");
		switch(stop_step){
		case TRAIN_ARRIVED:
			strBuf.append("TRAIN_ARRIVED");
			break;
		case DOOR_OPEN_OUT:
			strBuf.append("DOOR_OPEN_OUT");
			break;
		case DOOR_OPEN_IN:
			strBuf.append("DOOR_OPEN_IN");
			break;
		case DOOR_CLOSED:
			strBuf.append("DOOR_CLOSED");
			break;
		case TRAIN_DEPARTURE:
			strBuf.append("TRAIN_DEPARTURE");
			break;
		}
		strBuf.append(")");
		
		return strBuf.toString();
	}
	
}
