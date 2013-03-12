package com.neusoft.dmec.tcc.trafficsimulator.agent.person.status;

import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Gate;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.LineStation;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Site;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Station;

public class ExitStationStatus extends PersonStatus {
	
	public Station station;
	public Site from_site;
	public Gate to_gate;
	long takeoff_train_time;
	
	public ExitStationStatus(Station curStation, Site site , Gate gate, long curTime) {

		station = curStation;
		from_site = site;
		to_gate = gate;
		takeoff_train_time = curTime;
		
	}

	@Override
	public PersonStatusType getStatusType() {
		return PersonStatusType.EXIT_STATION;
	}
}
