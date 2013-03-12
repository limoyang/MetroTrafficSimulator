package com.neusoft.dmec.tcc.trafficsimulator.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class StationModel {
	
	public StationModel(String id, String line, String name, boolean b,  String tsid) 
	{
		station_id = id;
		line_id = line;
		station_name = name;
		is_sub_station = b;
		transfer_station_id = tsid;	
		is_transfer_station = false;
	}
	
	public StationModel(String id , String name , String[] subs)
	{
		station_id = id;
		station_name = name;
		is_sub_station = false;
		is_transfer_station = true;
		sub_station_ids = subs;
		line_id = null;
		transfer_station_id = null;
	}

	public final  String station_id;
	public final  String line_id;
	public final  String station_name;
	public final  boolean is_sub_station;
	public final  boolean is_transfer_station;
	public final  String transfer_station_id;
	public String[] sub_station_ids;
	public List<GateModel>  gate_list;
	public List<SiteModel>  site_list;
	
	
	public String toString(){
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("(");
		strBuf.append("id:").append(station_id).append(";");
		strBuf.append("line_id:").append(line_id).append(";");
		strBuf.append("name:").append(station_name).append(";");
		strBuf.append("gates:");
		for(GateModel gm : gate_list){
			strBuf.append(gm.gate_name).append("/");
		}
		strBuf.append(";");
		strBuf.append("sites:");
		for(SiteModel sm : site_list){
			strBuf.append(sm.site_name).append("/");
		}
		strBuf.append(")");
		
		return strBuf.toString();
	}

	public void init() {
		gate_list = new LinkedList<GateModel>();
		site_list = new LinkedList<SiteModel>();
	}

	public void addGate(GateModel gm){
		gate_list.add(gm);
	}
	
	public void addSite(SiteModel sm){
		site_list.add(sm);
	}
	
	public List<SiteModel> getSiteList() {
		return site_list;
	}
	
	public List<GateModel> getGateList() {
		return gate_list;
	}

}
