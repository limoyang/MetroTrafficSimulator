package com.neusoft.dmec.tcc.trafficsimulator.agent.person.status;

public class TripOverStatus extends PersonStatus {

	@Override
	public PersonStatusType getStatusType() {
		return PersonStatusType.TRIP_OVER;
	}

}
