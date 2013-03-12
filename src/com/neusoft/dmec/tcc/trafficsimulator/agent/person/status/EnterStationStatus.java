package com.neusoft.dmec.tcc.trafficsimulator.agent.person.status;

import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Gate;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.LineStation;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Site;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Station;

public class EnterStationStatus extends PersonStatus{
	
	Station station;
	Gate from_gate;
	Site to_site;
	long enter_time;
	
	public EnterStationStatus(Station sta, Gate gate, Site site , long time) {
		station = sta;
		from_gate = gate;
		to_site = site;
		enter_time = time;
	}

	@Override
	public PersonStatusType getStatusType() {
		return PersonStatusType.ENTER_STATION;
	}
}
