package com.neusoft.dmec.tcc.trafficsimulator.agent.station;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StationManager {
	
	private static StationManager station_manager;
	
	private StationManager(){
		stations_map = new HashMap<String , Station>();
		line_stations_map = new HashMap<String , LineStation>();
		transfer_stations_map = new HashMap<String , TransferStation>();

	};
	
	Map<String , LineStation> line_stations_map;
	Map<String , TransferStation> transfer_stations_map;
	Map<String , Station> stations_map;

	public static StationManager getInstance(){
		if(station_manager == null){
			station_manager = new StationManager();
		}
		return station_manager;
	}
	
	public Station getStation(String id){
		Station sta = line_stations_map.get(id);
		if(sta == null )
			sta = transfer_stations_map.get(id);
		return sta;
	}
	
	public void registStation(String id , Station sta){
		if(sta.isTransferStation()){
			transfer_stations_map.put(id , (TransferStation) sta);
			stations_map.put(id, sta);
		}else{
			LineStation lineSta = (LineStation)sta;
			if(lineSta.isSubStation()){
				//TransferStation transferSta = lineSta.getTransferStation();
				//transfer_stations_map.put(transferSta.getId(), transferSta);
			}else{
				stations_map.put(id, sta);
			}
			line_stations_map.put(id, lineSta);
		}
	}

	public Collection<Station> getAllStations() {
		return stations_map.values();
	}
	
	public Collection<String> getAllStationsID() {
		return stations_map.keySet();
	}

	
	public Collection<String> getAllLineStationID() {
		return line_stations_map.keySet();
	}

	public Collection<LineStation> getAllLineStations() {
		return line_stations_map.values();
	}
	
	
	public Collection<String> getAllTransferStationID() {
		return transfer_stations_map.keySet();
	}

	public Collection<TransferStation> getAllTransferStations() {
		return transfer_stations_map.values();
	}

}
