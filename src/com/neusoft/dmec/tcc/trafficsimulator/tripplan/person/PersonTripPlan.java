package com.neusoft.dmec.tcc.trafficsimulator.tripplan.person;

import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Gate;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Station;
import com.neusoft.dmec.tcc.trafficsimulator.odtable.ODPathElement;


public class PersonTripPlan {
	
	String plan_key;
	
	public  Station o_station;
	public  Gate o_station_gate;
	public  Station d_station;
	public  Gate d_station_gate;
	
	ODPathElement[]  transfer_plan;

	public PersonTripPlan(Station o , Gate og , Station d , Gate dg , ODPathElement[] plan){
		o_station = o;
		o_station_gate = og;
		d_station = d;
		d_station_gate = dg;
		transfer_plan = plan;
	}
	
	public Station getStartStation() {
		return o_station;
	}
	
	public ODPathElement getPathElement(int idx){
		if(idx >= 0 && idx < transfer_plan.length)
			return transfer_plan[idx];
		else
			return null;	
	}
	
	public ODPathElement getPreviousPathElement(int idx){
		if(idx > 0 && idx < transfer_plan.length)
			return transfer_plan[idx -1];
		else
			return null;
	}
	
}
