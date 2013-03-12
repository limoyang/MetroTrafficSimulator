package com.neusoft.dmec.tcc.trafficsimulator.odtable;

import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Station;
import com.neusoft.dmec.tcc.trafficsimulator.model.StationModel;

public class ODProportion {
	public ODProportion(StationModel o, StationModel d, float p) {
		o_station = o;
		d_station = d;
		proportion = p;
	}
	StationModel o_station;
	StationModel d_station;
	float proportion;
	public float getProportion() {
		return proportion;
	}
	public ODPair getODPair() {
		return new ODPair(o_station , d_station);
	}
	public String getOID() {
		return o_station.station_id;
	}
	public String getDID() {
		return d_station.station_id;
	}
}
