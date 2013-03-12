package com.neusoft.dmec.tcc.trafficsimulator.test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Station;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.StationManager;
import com.neusoft.dmec.tcc.trafficsimulator.model.BasicModelManager;
import com.neusoft.dmec.tcc.trafficsimulator.model.StationModel;
import com.neusoft.dmec.tcc.trafficsimulator.odtable.ODPair;
import com.neusoft.dmec.tcc.trafficsimulator.odtable.ODPath;
import com.neusoft.dmec.tcc.trafficsimulator.odtable.ODPathTable;

public class TestODPathTable implements ODPathTable {
	Map<String , List<ODPath>> path_table;
	List<Map<ODPath , Float>> path_proportion_list;
	
	long start_time;
	long statistic_unit;
	long statistic_size;

	
	public TestODPathTable(long start , long unit , long size){
		start_time = start;
		statistic_unit = unit;
		statistic_size = size;
		
		path_table = new HashMap<String , List<ODPath>>();
		path_proportion_list = new LinkedList<Map<ODPath , Float>>();//new HashMap<ODPath , Float>();
		for(long i = 0 ; i < size ; i++){
			path_proportion_list.add(new HashMap<ODPath , Float>());
		}
	}
	/*
	public List<ODPath> getODPathList(String o , String d){
		ODPair pair = new ODPair(o,d);
		return path_table.get(pair.toString());
	}
	*/
	
	public List<ODPath> getODPathList(ODPair pair){
		return path_table.get(pair.toString());
	}

	public void addODPath(String o, String d, ODPath path) {
		StationModel osm = BasicModelManager.getInstance().getStationModel(o);
		StationModel dsm = BasicModelManager.getInstance().getStationModel(d);
		
		ODPair pair = new ODPair(osm,dsm);
		addODPath(pair, path);
	}
	
	public void addODPath(ODPair pair , ODPath path){
		List<ODPath> pathList = path_table.get(pair);
		if(pathList == null){
			pathList = new LinkedList<ODPath>();
			path_table.put(pair.toString() ,pathList);
		}
		pathList.add(path);
	}
	
	
	
	public void addODPathProportion(long time , ODPath path, float p){
		int index = getTimeIndexByTime(time);
		Map<ODPath , Float> map = path_proportion_list.get(index);
		map.put(path, p);
	}
	
	public float getOPPathProportion(long time , ODPath path){
		int index = getTimeIndexByTime(time);
		Map<ODPath , Float> map = path_proportion_list.get(index);
		if(map != null){
			Float prop = map.get(path);
			if(prop != null)
				return prop.floatValue();
		}
		return (float) 0.0;
	}

	public int getTimeIndexByTime(long curTime) {
		return (int)((curTime - start_time)/statistic_unit);
	}
	
	public String getTableString(){
		StringBuffer buf = new StringBuffer();
		int idx = 0;
		buf.append("****************************OD path table*******************************************").append("\n");
		for(Map<ODPath , Float> pathMap : path_proportion_list){
			buf.append("================================= time index :").append(idx++).append("=========================").append("\n");
			for(ODPath path : pathMap.keySet()){
				Float prop = pathMap.get(path);
				buf.append(path).append("  proportion = ").append(prop).append("\n");
			}
		}
		buf.append("*************************************************************************************").append("\n");
		return buf.toString();
	}

}
