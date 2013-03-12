package com.neusoft.dmec.tcc.trafficsimulator.agent.station;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.neusoft.dmec.tcc.trafficsimulator.agent.Agent;
import com.neusoft.dmec.tcc.trafficsimulator.agent.AgentContext;
import com.neusoft.dmec.tcc.trafficsimulator.agent.line.Line;
import com.neusoft.dmec.tcc.trafficsimulator.agent.line.LineManager;
import com.neusoft.dmec.tcc.trafficsimulator.agent.person.Person;
import com.neusoft.dmec.tcc.trafficsimulator.agent.person.status.PersonStatus.PersonStatusType;
import com.neusoft.dmec.tcc.trafficsimulator.agent.train.status.TrainStatus.TrainStatusType;
import com.neusoft.dmec.tcc.trafficsimulator.generator.DefaultPersonGenerator;
import com.neusoft.dmec.tcc.trafficsimulator.model.BasicModelManager;
import com.neusoft.dmec.tcc.trafficsimulator.model.GateModel;
import com.neusoft.dmec.tcc.trafficsimulator.model.LineModel.Direction;
import com.neusoft.dmec.tcc.trafficsimulator.model.SiteModel;
import com.neusoft.dmec.tcc.trafficsimulator.model.StationModel;
import com.neusoft.dmec.tcc.trafficsimulator.odtable.ODPathTable;
import com.neusoft.dmec.tcc.trafficsimulator.odtable.ODProportionTable;

public class LineStation extends Station {

	Line line;
	TransferStation transfer_station;
	HashMap<String, Gate> gates_map;
	HashMap<String, Site> sites_map;

	public LineStation(String stationID) {
		super();
		station_model = BasicModelManager.getInstance().getStationModel(stationID);
	}

	public String getId(){
		return station_model.station_id;
	}
	/*
	 * initialization function
	 */
	public void init(AgentContext ctx) {
		
		super.init(ctx);
				
		initGates();
		initSites();
		
		StationManager.getInstance().registStation(station_model.station_id,this);		

		if(station_model.is_sub_station){
			Station tranSta = StationManager.getInstance().getStation(station_model.transfer_station_id);
			if(tranSta == null){
				StationModel sm = BasicModelManager.getInstance().getStationModel(station_model.transfer_station_id);
				if(sm != null){
					transfer_station = new TransferStation(sm);
					transfer_station.init(ctx);
					StationManager.getInstance().registStation(station_model.transfer_station_id, transfer_station);
				}
			}else if(tranSta.isTransferStation()){
				transfer_station = (TransferStation) tranSta;
			}
			transfer_station.addSubStations(this);
		}

	}

	private void initSites() {
		sites_map = new HashMap<String, Site>();
		
		List<SiteModel> sites = station_model.getSiteList();

		if(sites != null && sites.size() > 0){
			for(SiteModel sm : sites){
				sites_map.put(sm.site_name, new Site(sm , this));
			}
		}
	}

	private void initGates() {
		gates_map = new HashMap<String, Gate>();
		
		List<GateModel> gates = station_model.getGateList();

		if(gates != null && gates.size() > 0){
			for(GateModel gm : gates){
				gates_map.put(gm.gate_name, new Gate(gm ,this));
			}
		}
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
		sbuf.append(">");
		return sbuf.toString();
	}

	public Site getSite(String siteId) {
		return sites_map.get(siteId);
	}

	public Site getSiteByDirection(Direction dir) {
		if(sites_map.size() > 0){
			for(Site site : sites_map.values()){
				if(site.getDirection() == dir){
					return site;
				}
			}
		}
		return null;
	}

	public Gate getGate(String gateId) {
		return gates_map.get(gateId);
	}


	public void addTransferPerson(Person person) {
		if(transfer_station != null){
			transfer_station.addTransferPerson(person);
		}
	}

	public TransferStation getTransferStation() {
		return transfer_station;
	}

	@Override
	public boolean isTransferStation() {
		return false;
	}
	
	@Override
	protected void doCurrentStepImpl(long curTime) {
		super.doCurrentStepImpl(curTime);
		System.out.println(toAgentDebugString());
	}
	
	public boolean isSubStation() {
		return station_model.is_sub_station;
	}


	public Line getLine(){
		if(line == null){
			line = LineManager.getInstance().getLine(station_model.line_id);
		}
		return line;
	}

	@Override
	public Gate[] getGates() {
		return gates_map.values().toArray(new Gate[0]);
	}

	@Override
	public Site[] getSites() {
		return sites_map.values().toArray(new Site[0]);
	}

	@Override
	public Station getNormalStation() {
		if(isSubStation())
			return transfer_station;
		else
			return this;
	}
}
