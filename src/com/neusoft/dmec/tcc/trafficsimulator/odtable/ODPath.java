package com.neusoft.dmec.tcc.trafficsimulator.odtable;

import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Station;
import com.neusoft.dmec.tcc.trafficsimulator.model.StationModel;

public class ODPath {
	StationModel o_station;
	StationModel d_station;
	ODPathElement[] sub_path;
	
	public ODPath(StationModel osta , StationModel dsta, ODPathElement[] subPath){
		o_station = osta;
		d_station = dsta;
		sub_path = subPath;
	}
	
	public ODPathElement[] getPathElements(){
		return sub_path;
	}
	
	public String toString(){
		StringBuffer buf = new StringBuffer();
		buf.append("OID = ").append(o_station.station_id).append(";");
		buf.append("DID = ").append(d_station.station_id).append(";");
		buf.append("PathElement : ");
		for(ODPathElement element : sub_path){
			buf.append(element).append(" ");
		}
		return buf.toString();
		
	}
}
