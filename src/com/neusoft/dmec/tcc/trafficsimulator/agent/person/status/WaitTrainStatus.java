package com.neusoft.dmec.tcc.trafficsimulator.agent.person.status;

import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Site;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.LineStation;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Station;

public class WaitTrainStatus extends PersonStatus {
	public WaitTrainStatus(Station station, long time) {
		this.station = station;
		this.waiting_start_time = time;
	}

	Station station;
	long waiting_start_time;
	
	@Override
	public PersonStatusType getStatusType() {
		return PersonStatusType.WAIT_TRAIN;
	}
}
