package com.neusoft.dmec.tcc.trafficsimulator.odtable;

import com.neusoft.dmec.tcc.trafficsimulator.agent.line.Line;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Station;
import com.neusoft.dmec.tcc.trafficsimulator.model.LineModel;
import com.neusoft.dmec.tcc.trafficsimulator.model.LineModel.Direction;
import com.neusoft.dmec.tcc.trafficsimulator.model.StationModel;

public class ODPathElement {
	/*
	public String  line_id;
	public String start_station_id;
	public String end_station_id;
	public Direction direction;
	*/

	public LineModel  line;
	public StationModel start_station;
	public StationModel end_station;
	public Direction direction;

	public String toString(){
		String ret =  line.line_id + "/" 
				+ start_station.station_id + "/" 
				+ end_station.station_id + "/" ;
		
		if(direction == Direction.UPWARD)
			ret += "upward";
		else
			ret +="downward";
		
		return ret;
	}
}
