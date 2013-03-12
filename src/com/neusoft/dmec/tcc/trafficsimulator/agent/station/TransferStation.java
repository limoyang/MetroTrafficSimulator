package com.neusoft.dmec.tcc.trafficsimulator.agent.station;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.neusoft.dmec.tcc.trafficsimulator.agent.AgentContext;
import com.neusoft.dmec.tcc.trafficsimulator.agent.Location;
import com.neusoft.dmec.tcc.trafficsimulator.agent.line.Line;
import com.neusoft.dmec.tcc.trafficsimulator.agent.person.Person;
import com.neusoft.dmec.tcc.trafficsimulator.agent.person.status.PersonStatus.PersonStatusType;
import com.neusoft.dmec.tcc.trafficsimulator.model.LineModel.Direction;
import com.neusoft.dmec.tcc.trafficsimulator.model.StationModel;
import com.neusoft.dmec.tcc.trafficsimulator.odtable.ODPathTable;
import com.neusoft.dmec.tcc.trafficsimulator.odtable.ODProportionTable;

public class TransferStation  extends Station {
	
	class TransferPath{
		public TransferPath(String key){
			path_key = key;
			transfer_walking_persons = new LinkedList<Person>(); 
		}
		String path_key;
		List<Person> transfer_walking_persons;
	}
	
	List<LineStation> sub_stations;	
	Map<String , TransferPath> transfer_path_map;
	private int transfer_persons;
	
	public TransferStation(StationModel sm){
		super();
		station_model = sm;
		transfer_path_map = new HashMap<String , TransferPath>();
		sub_stations = new LinkedList<LineStation>();
		transfer_persons = 0;
	}
	
	
	@Override
	protected void doCurrentStepImpl(long curTime) {

		super.doCurrentStepImpl(curTime);

		for(TransferPath path : transfer_path_map.values()){
			Person[] walkPersons = (Person[]) path.transfer_walking_persons.toArray(new Person[0]);
			if(walkPersons != null && walkPersons.length > 0){
				for(Person p : walkPersons){
					p.doCurrentStep(curTime);
				}
			}
		}
				
		System.out.println(toAgentDebugString());
	}
	
	@Override
	public String toAgentDebugString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("Station <");
		sbuf.append("id = ").append(station_model.station_id).append(";");
		sbuf.append("enter_wakling_persons = ").append(enter_walking_persons.size()).append(";");
		sbuf.append("exit_wakling_persons = ").append(exit_walking_persons.size()).append(";");
		int waitcount = 0 , leavecount = 0;
		for(Site site : getSites()){
			leavecount += site.leaving_persons.size();
			waitcount += site.waiting_persons.size();			
		}
		sbuf.append("site_waiting_persons = ").append(waitcount).append(";");
		sbuf.append("site_leaving_persons = ").append(leavecount).append(";");
		sbuf.append("enter_persons = ").append(enter_persons).append(";");
		sbuf.append("exit_persons = ").append(exit_persons).append(";");
		sbuf.append("transfer_persons = ").append(transfer_persons).append(";");
		for(TransferPath path : transfer_path_map.values()){
			sbuf.append(path.path_key).append(" = ").append(path.transfer_walking_persons.size()).append(";");
		}
		sbuf.append(">");
		return sbuf.toString();
	}
	
	@Override
	public void init(AgentContext ctx) {
		super.init(ctx);
	}

	public void removeTransferPerson(Person person) {

		String key = getTransferPathKey(person);
		TransferPath path = transfer_path_map.get(key);
		if(path != null){
			path.transfer_walking_persons.remove(person);
		}
	}

	public void addTransferPerson(Person person) {
		
		String key = getTransferPathKey(person);
		TransferPath path = transfer_path_map.get(key);
		if(path == null){
			path = new TransferPath(key);
			transfer_path_map.put(key, path);
		}
		path.transfer_walking_persons.add(person);
		
		transfer_persons++;
	}

	private String getTransferPathKey(Person person) {
		String fromId = person.getCurrentFromLocation().getId();
		String toId  = person.getCurrentToLocation().getId();
		return fromId + "->" + toId;
	}



	@Override
	public String getId() {
		return station_model.station_id;
	}

	@Override
	public boolean isTransferStation() {
		return true;
	}
	
	public LineStation[] getSubStations(){
		return sub_stations.toArray(new LineStation[0]);
	}

	public void addSubStations(LineStation subSta) {
		sub_stations.add(subSta);
	}

	public Site getSiteByLineDirection(Line line, Direction dir) {
		for(LineStation subStation : sub_stations){
			if(subStation.getLine() == line)
				return subStation.getSiteByDirection(dir);
		}
		return null;
	}

	public LineStation getSubStationByLine(Line line) {
		for(LineStation subStation : sub_stations){
			if(subStation.getLine() == line)
				return subStation;
		}
		return null;
	}

	@Override
	public Gate[] getGates() {
		List<Gate> gates = new LinkedList<Gate>();
		for(LineStation subStation : sub_stations){
			for(Gate gate : subStation.getGates()){
				gates.add(gate);
			}
		}
		return gates.toArray(new Gate[0]);
	}


	@Override
	public Site[] getSites() {
		List<Site> sites = new LinkedList<Site>();
		for(LineStation subStation : sub_stations){
			for(Site site : subStation.getSites()){
				sites.add(site);
			}
		}
		return sites.toArray(new Site[0]);
	}


	@Override
	public Station getNormalStation() {
		return this;
	}
}
