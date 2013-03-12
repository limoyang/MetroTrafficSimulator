package com.neusoft.dmec.tcc.trafficsimulator.agent.person;

public class PersonTripRecord {
	public enum TripRecordType{ ENTER_STATION , WAITINF_TRAIN , TAKING_TRAIN , TRANSFER , EXIT_STATION};
	
	TripRecordType type;
	long start_time;
	long end_time;
}
