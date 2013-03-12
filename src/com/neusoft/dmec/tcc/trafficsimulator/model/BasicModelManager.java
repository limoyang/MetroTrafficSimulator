package com.neusoft.dmec.tcc.trafficsimulator.model;

import java.util.HashMap;
import java.util.Map;

public class BasicModelManager {
	
	private static BasicModelManager basic_model_manager;
	
	private BasicModelManager(){
		gates_map = new HashMap<String , GateModel>();
		sites_map = new HashMap<String , SiteModel>();
		stations_map = new HashMap<String , StationModel>();
		sections_map = new HashMap<String , SectionModel>();
		lines_map = new HashMap<String , LineModel>();
		trains_map = new HashMap<String , TrainModel>();
	};
	
	Map<String , GateModel> gates_map;
	Map<String , SiteModel> sites_map;
	Map<String , StationModel> stations_map;
	Map<String , SectionModel> sections_map;
	Map<String , LineModel> lines_map;
	Map<String , TrainModel> trains_map;

	public static BasicModelManager getInstance(){
		if(basic_model_manager == null){
			basic_model_manager = new BasicModelManager();
		}
		return basic_model_manager;
	}

	
	public StationModel getStationModel(String stationID) {
		return stations_map.get(stationID);
	}
	
	public void registStationModel(String id , StationModel info){
		stations_map.put(id, info);
	}

	public GateModel getGateModel(String gateID) {
		return gates_map.get(gateID);
	}

	public void registGateModel(String id , GateModel info){
		gates_map.put(id, info);
	}

	public SiteModel getSiteModel(String sid) {
		return sites_map.get(sid);
	}

	public void registSiteModel(String id , SiteModel info){
		sites_map.put(id, info);
	}

	public LineModel getLineModel(String lineId) {
		return lines_map.get(lineId);
	}

	public void registLineModel(String id , LineModel info){
		lines_map.put(id, info);
	}

	public SectionModel getSectionModel(String sectId) {
		return sections_map.get(sectId);
	}

	public void registSectionModel(String id , SectionModel info){
		sections_map.put(id, info);
	}

	
	public TrainModel getTrainModelByLineId(String lid) {
		return trains_map.get(lid);
	}

	public void registTrainModel(String id , TrainModel info){
		trains_map.put(id, info);
	}

}
