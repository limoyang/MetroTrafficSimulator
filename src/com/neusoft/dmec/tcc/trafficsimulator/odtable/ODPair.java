package com.neusoft.dmec.tcc.trafficsimulator.odtable;

import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Station;
import com.neusoft.dmec.tcc.trafficsimulator.model.StationModel;

public class ODPair {
	StationModel o_station;
	StationModel d_station;
	String string;
	
	public ODPair(StationModel o , StationModel d){
		o_station = o;
		d_station = d;
		string = o.station_id + d.station_id;
	}
	
	public String toString(){
		return string;
	}
	
	public boolean equals(ODPair pair){
		return string.equals(pair);
	}
	
	public boolean hasOID(String oid){
		return o_station.station_id.equals(oid);
	}
	
	public boolean hasDID(String did){
		return d_station.station_id.equals(did);
	}

	public StationModel getO() {
		return o_station;
	}

	public StationModel getD() {
		return d_station;
	}

	public String getOID() {
		return o_station.station_id;
	}

	public String getDID() {
		return d_station.station_id;
	}

}
