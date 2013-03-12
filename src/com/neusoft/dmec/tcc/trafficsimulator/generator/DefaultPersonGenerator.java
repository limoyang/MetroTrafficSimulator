package com.neusoft.dmec.tcc.trafficsimulator.generator;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.neusoft.dmec.tcc.trafficsimulator.agent.person.Person;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Gate;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Station;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.StationManager;
import com.neusoft.dmec.tcc.trafficsimulator.model.BasicModelManager;
import com.neusoft.dmec.tcc.trafficsimulator.model.GateModel;
import com.neusoft.dmec.tcc.trafficsimulator.model.SiteModel;
import com.neusoft.dmec.tcc.trafficsimulator.model.StationModel;
import com.neusoft.dmec.tcc.trafficsimulator.odtable.ODPair;
import com.neusoft.dmec.tcc.trafficsimulator.odtable.ODPath;
import com.neusoft.dmec.tcc.trafficsimulator.odtable.ODPathTable;
import com.neusoft.dmec.tcc.trafficsimulator.odtable.ODProportion;
import com.neusoft.dmec.tcc.trafficsimulator.odtable.ODProportionTable;
import com.neusoft.dmec.tcc.trafficsimulator.tripplan.person.PersonTripPlan;

public class DefaultPersonGenerator implements PersonGenerator {

	String station_id;
	long start_time;
	long count_unit_len;
	long generator_step_len;
	int[] original_enter_count;  
	int[] minor_step_count;
	
	ODProportionTable   od_proportion_table;
	ODPathTable         od_path_table;
	
	ODPair[]  random_odpair_table;
	ODPath[]  random_odpath_table;
	
	Random rand_generator;


	public DefaultPersonGenerator(String sid, long genStepLen){
		station_id = sid;
		generator_step_len = genStepLen;		
		rand_generator = new Random();
	}
	
	@Override
	public List<Person> getNewPersons(long curTime) {
		
		int curMainIndex = (int)((curTime - start_time)/count_unit_len);

		int minorIdxCount = (int) (count_unit_len/generator_step_len);
		
		long curMainOffsite = count_unit_len*curMainIndex + start_time;
		
		int curMinorIndex = (int) ((curTime - curMainOffsite)/generator_step_len);
		
		int personCount = 0;
		
		if(curMinorIndex <  minorIdxCount){
			if(curMinorIndex == (minorIdxCount - 1))
				personCount = original_enter_count[curMainIndex];
			else{
				personCount = getCurrentPersonCount(original_enter_count[curMainIndex] , curMinorIndex);
			}
		}
		
		if(personCount > 0){
			List<Person>  retList = new LinkedList<Person>();
			for(int i = 0 ; i < personCount ; i++){
				PersonTripPlan plan = generatePersonTripPlan(curTime);
				Person person = new Person(plan);
				retList.add(person);
			}
			return retList;
		}else
			return null;
	}
	
	@Override
	public void setPersonEnterCounts(long startTime, long unitLen, int[] enterCounts) {
		start_time = startTime;
		count_unit_len = unitLen;
		original_enter_count = enterCounts;
		minor_step_count = new int[(int) (unitLen/generator_step_len)];
	}
	
	@Override
	public void setODProportionTable(ODProportionTable odTable) {
		od_proportion_table = odTable;
	}

	@Override
	public void setODPathTable(ODPathTable pathTable) {
		od_path_table = pathTable;
		
	}	
	
	private PersonTripPlan generatePersonTripPlan(long curTime) {
		
		ODPair pair = getRandomODPair(curTime);
		if(pair != null){
			ODPath path = getRandomODPath(pair , curTime);
			if(path != null){
				Station oSta = StationManager.getInstance().getStation(pair.getOID());
				Station dSta = StationManager.getInstance().getStation(pair.getDID());

				PersonTripPlan plan = 
						new PersonTripPlan(
								oSta,
								getRandomExitGate(oSta) , 
								dSta, 
								getRandomExitGate(dSta), 
								path.getPathElements());
				return plan;
			}
		}
		
		return null;
	}


	private int getCurrentPersonCount(int count  , int minorIdx){
		
		if(minorIdx == 0){
			genRandomPersonCount(count);
		}
		
		return minor_step_count[minorIdx];
	}
	
	private ODPath getRandomODPath(ODPair pair, long curTime) {
		int rand = rand_generator.nextInt(9999);
		List<ODPath> pathList = od_path_table.getODPathList(pair);
		int propLimit = 0;
		for(ODPath path : pathList){
			float prop = od_path_table.getOPPathProportion(curTime , path);
			propLimit = (int)(prop * 10000.0 + 0.5) + propLimit;
			if(rand < propLimit)
				return path;
		}
		
		return pathList.get(0);
	}

	private ODPair getRandomODPair(long curTime) {
		int rand = rand_generator.nextInt(9999);
		List<ODProportion> propList = od_proportion_table.getODProportionListByOID(curTime, station_id);
		int nextLimit = 0;
		for(ODProportion prop : propList){
			nextLimit = (int)(prop.getProportion()*10000.0 + 0.5) + nextLimit;
			if(rand < nextLimit)
				return prop.getODPair();
		}
		return propList.get(0).getODPair();
	}
	
	private void genRandomPersonCount(int count){
		int bucks = minor_step_count.length;
		for(int i = 0 ; i < bucks ; i++){
			minor_step_count[i] = 0;
		}
		
		if(bucks <= 1){
			minor_step_count[0] = count;
		}else{
			for(int i = 0 ; i < count ; i++){
				int buck = Math.abs(rand_generator.nextInt())%bucks;
				minor_step_count[buck]++;
			}
		}
	}
	
	private Gate getRandomExitGate(Station  station) {
		Gate[] gates = station.getGates();
		int count = gates.length;
		return gates[Math.abs(rand_generator.nextInt()%count)];
	}

	private String getRandomEntryGate(String sid) {
		StationModel sta = BasicModelManager.getInstance().getStationModel(sid);
		if(sta != null){
			List<GateModel> gates = sta.getGateList();
			int count = gates.size();
			GateModel gm = gates.get(Math.abs(rand_generator.nextInt()%count));
			return gm.gate_name;
		}
		return null;
	}
}
