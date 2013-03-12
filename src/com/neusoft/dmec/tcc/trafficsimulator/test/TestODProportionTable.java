package com.neusoft.dmec.tcc.trafficsimulator.test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Station;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.StationManager;
import com.neusoft.dmec.tcc.trafficsimulator.model.BasicModelManager;
import com.neusoft.dmec.tcc.trafficsimulator.model.StationModel;
import com.neusoft.dmec.tcc.trafficsimulator.odtable.ODProportion;
import com.neusoft.dmec.tcc.trafficsimulator.odtable.ODProportionTable;

public class TestODProportionTable implements ODProportionTable {
	List<Map<String , List<ODProportion>>> proportion_map_list;
	
	long start_time;
	long statistic_unit;
	long statistic_size;
	
	/*
	 * params:
	 * start -- start time
	 * unit -- length per unit
	 * size -- total unit count
	 */
	
	TestODProportionTable(long start , long unit , long size){
		proportion_map_list = new LinkedList<Map<String , List<ODProportion>>>();
		for(long i = 0 ; i < size ; i++){
			proportion_map_list.add(new HashMap<String , List<ODProportion>>());
		}
		start_time = start;
		statistic_unit = unit;
		statistic_size = size;
	}
	
	
	public void addODProportion(int index , String oid , String did , float p){
		
		StationModel osm = BasicModelManager.getInstance().getStationModel(oid);
		StationModel dsm = BasicModelManager.getInstance().getStationModel(did);
		
		ODProportion proportion = new ODProportion(osm,dsm,p);
		Map<String , List<ODProportion>> map = proportion_map_list.get(index);
		List<ODProportion> propList = map.get(osm.station_id);
		if(propList == null){
			propList = new LinkedList<ODProportion>();
			propList.add(proportion);
			map.put(osm.station_id, propList);
		}else{
			propList.add(proportion);
		}
	}
	
	public float getODProportion(int index , String o , String d ){
		
		Map<String , List<ODProportion>> map = proportion_map_list.get(index);
		if(map != null){
			List<ODProportion> propList = map.get(o);
			if(propList != null){
				for(ODProportion prop : propList){
					if(prop.getDID().equals(d) == true)
						return prop.getProportion();
				}
			}
		}
		return (float) -1.0;
	}
	
	public List<ODProportion> getODProportionListByOID(long curTime , String o){
		
		int index = getTimeIndexByTime(curTime);
		Map<String , List<ODProportion>> map = proportion_map_list.get(index);
		if(map != null){
			return  map.get(o);
		}else
			return null;
	}

	private int getTimeIndexByTime(long curTime) {
		//long offset = curTime - (curTime/(86400*1000))*86400*1000;
		return (int)((curTime - start_time)/statistic_unit);
	}
}
