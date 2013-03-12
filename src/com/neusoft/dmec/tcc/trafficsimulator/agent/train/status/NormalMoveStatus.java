package com.neusoft.dmec.tcc.trafficsimulator.agent.train.status;

public class NormalMoveStatus extends TrainStatus {

	long start_time;
	
	@Override
	public TrainStatusType getStatusType() {
		return TrainStatusType.NORMAL_MOVE;
	}

	public void setStartTime(long curTime) {
		start_time = curTime;
	}

	@Override
	public String toString() {
		return "Normal Move";
	}

}
