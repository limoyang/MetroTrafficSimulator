package com.neusoft.dmec.tcc.trafficsimulator.agent.train.status;

public abstract class TrainStatus {
	public static enum TrainStatusType { NORMAL_STOP , NORMAL_MOVE , NORMAL_EXIT}
	public abstract TrainStatusType getStatusType();
	public abstract String toString();
}
