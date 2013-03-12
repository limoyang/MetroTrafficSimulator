package com.neusoft.dmec.tcc.trafficsimulator.agent.person.status;

import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Station;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.TransferStation;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Site;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.LineStation;

public class TransferStatus extends PersonStatus {
	TransferStation transfer_station;
	Site from_site;
	Site to_site;
	long transfer_start_time;
	
	public TransferStatus(TransferStation tranStation, Site fromSite, Site toSite, long curTime) {
		transfer_station = tranStation;
		from_site = fromSite;
		to_site = toSite;
		transfer_start_time = curTime;
	}

	@Override
	public PersonStatusType getStatusType() {
		return PersonStatusType.TRANSFER_STATION;
	}
}
