package com.neusoft.dmec.tcc.trafficsimulator.engine.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.neusoft.dmec.tcc.trafficsimulator.agent.Location;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Station;
import com.neusoft.dmec.tcc.trafficsimulator.engine.StationSimulateEngine;

public class SimpleStationSimulateEngineImpl implements StationSimulateEngine {

	long current_time;
	Map<String , SimuPersonStatus>  pre_person_status_map;
	Map<String , SimuPersonStatus>  cur_person_status_map;
	Map<String , SimuPerson>  waiting_persons_map;
	String station_id;

	class SimuPerson{
		public SimuPerson(String id, long time) {
			pid = id;
			start_time = time;
		}
		String pid;
		long start_time;
	}
	
	class SimuPersonWalkingPath{
		String path_key;
		List<SimuPerson> persons;
		int path_type = -1;
		public SimuPersonWalkingPath(String key, Location from, Location to) {
			path_key = key;
			persons = new LinkedList<SimuPerson>();
			
			if(from.isGate()){
				if(to.isSite()){
					path_type = 1;
				}
			}else if(from.isSite()){
				if(to.isGate()){
					path_type = 2;
				}else if(to.isSite()){
					path_type = 3;
				}
			}
		}

		boolean isEnterPath(){
			return (path_type == 1);
		}
		
		boolean isExitPath(){
			return (path_type == 2);
		}
		
		public boolean isTransferPath() {
			return (path_type == 3);
		}
		
		long getDefaultDuration(){
			return 4*60*1000;
		}

	}
	
	List<SimuPersonWalkingPath> walking_paths;
	
	
	public SimpleStationSimulateEngineImpl() {
		
	}

	@Override
	public boolean initEngine(String sid) {
		pre_person_status_map = new HashMap<String , SimuPersonStatus>();
		cur_person_status_map = new HashMap<String , SimuPersonStatus>();
		walking_paths = new LinkedList<SimuPersonWalkingPath>();
		waiting_persons_map = new HashMap<String , SimuPerson>();
		station_id = sid;
		return true;
	}

	@Override
	public SimuPersonStatus getPersonStatus(String pid, long curTime) {
		SimuPersonStatus status = cur_person_status_map.get(pid);
		if(status == null){
			status = pre_person_status_map.get(pid);
			if(status != null){
				cur_person_status_map.put(pid, status);
			}
		}
		return status;
	}

	@Override
	public void nextStep(long curTime) {
		if(curTime <= current_time)
			return;
		
		current_time = curTime;
		
		pre_person_status_map.clear();
		Map<String, SimuPersonStatus> emptyMap = pre_person_status_map;
		pre_person_status_map = cur_person_status_map;
		cur_person_status_map = emptyMap;
		
		simulatePerson(curTime);
	}

	private void simulatePerson(long curTime) {
		for(SimuPersonWalkingPath path : walking_paths){
			simulateWalkingPath(path , curTime);
		}
		
		for(String pid : waiting_persons_map.keySet()){
			cur_person_status_map.put(pid, SimuPersonStatus.WAIT_TRAIN);
		}
	}

	private void simulateWalkingPath(SimuPersonWalkingPath path , long curTime) {
		long pathDurationTime = getWalkingPathDurationTime(path);
		SimuPerson[] persons = path.persons.toArray(new SimuPerson[0]);
		for(SimuPerson person : persons){
			if(curTime <= person.start_time + pathDurationTime){
				if(path.isEnterPath())
					cur_person_status_map.put(person.pid, SimuPersonStatus.ENTER_WALK);
				else if(path.isExitPath())
					cur_person_status_map.put(person.pid, SimuPersonStatus.EXIT_WALK);
				else if(path.isTransferPath()){
					cur_person_status_map.put(person.pid, SimuPersonStatus.TRANSFER_WALK);					
				}
			}else{
				if(path.isEnterPath()){
					cur_person_status_map.put(person.pid, SimuPersonStatus.WAIT_TRAIN);
					path.persons.remove(person);
					waiting_persons_map.put(person.pid, person);
				}else if(path.isExitPath()){
					cur_person_status_map.put(person.pid, SimuPersonStatus.TRIP_OVER);
					path.persons.remove(person);
				}else if (path.isTransferPath()){
					cur_person_status_map.put(person.pid, SimuPersonStatus.WAIT_TRAIN);
					path.persons.remove(person);
					waiting_persons_map.put(person.pid, person);
				}				
			}
		}
	}

	private long getWalkingPathDurationTime(SimuPersonWalkingPath path) {
		return path.getDefaultDuration();
	}

	@Override
	public void addPerson(String pid, Location from, Location to , long curTime) {
		String key = from.getId() + "->" + to.getId();
		
		SimuPersonWalkingPath path = getWalkingPath(key);
		if(path == null){
			path = new SimuPersonWalkingPath(key , from , to);
			walking_paths.add(path);
		}
		
		path.persons.add(new SimuPerson(pid , curTime));
	}

	@Override
	public void removePerson(String pid){
		waiting_persons_map.remove(pid);
	}
	
	private SimuPersonWalkingPath getWalkingPath(String key) {
		for(SimuPersonWalkingPath path: walking_paths){
			if(path.path_key.equals(key))
				return path;
		}
		return null;
	}

}
