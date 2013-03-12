package com.neusoft.dmec.tcc.trafficsimulator.agent.train.status;

public class NormalExitStatus extends TrainStatus{
	public static final NormalExitStatus train_exit_status = new NormalExitStatus();

	@Override
	public TrainStatusType getStatusType() {
		// TODO Auto-generated method stub
		return TrainStatusType.NORMAL_EXIT;
	}

	@Override
	public String toString() {
		return "Normal Exit";
	}

	
}


