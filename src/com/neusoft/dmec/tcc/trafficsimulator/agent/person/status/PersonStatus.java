package com.neusoft.dmec.tcc.trafficsimulator.agent.person.status;

public abstract class PersonStatus {
	public enum PersonStatusType { ENTER_STATION , WAIT_TRAIN , TAKE_TRAIN , TRANSFER_STATION , EXIT_STATION , TRIP_OVER }

	public abstract PersonStatusType getStatusType();
}
