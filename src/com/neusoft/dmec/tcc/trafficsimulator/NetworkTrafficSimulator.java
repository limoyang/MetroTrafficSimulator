package com.neusoft.dmec.tcc.trafficsimulator;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.neusoft.dmec.tcc.trafficsimulator.agent.AgentContext;
import com.neusoft.dmec.tcc.trafficsimulator.agent.line.Line;
import com.neusoft.dmec.tcc.trafficsimulator.agent.line.LineManager;
import com.neusoft.dmec.tcc.trafficsimulator.agent.person.Person;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.LineStation;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Station;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.StationManager;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.TransferStation;
import com.neusoft.dmec.tcc.trafficsimulator.agent.train.Train;
import com.neusoft.dmec.tcc.trafficsimulator.engine.CoreSimulatorEngine;
import com.neusoft.dmec.tcc.trafficsimulator.engine.LineSimulateEngine;
import com.neusoft.dmec.tcc.trafficsimulator.engine.StationSimulateEngine;

public class NetworkTrafficSimulator {

	NetworkTrafficSimulatorConfig simu_config;
	AgentContext agent_ctx;
	
	private CoreSimulatorEngine simulator_engine;
	
	public NetworkTrafficSimulator(NetworkTrafficSimulatorConfig simuConfig){
		simu_config = simuConfig;
		simulator_engine = new CoreSimulatorEngine();
		agent_ctx = new AgentContext();
	}
	
	public void init() {
		initAgentContext();		
		initLines();
		initStations();
		initSimuEngines();
	}
	
	private void initStations() {
		Collection<Station> stations = StationManager.getInstance().getAllStations();
		for(Station sta : stations){
			int[] counts =  simu_config.getEnterStationCounts(sta.getId());
			sta.setPersonEnterCounts(simu_config.simu_start_time, simu_config.station_enter_statistic_len, counts);
			
			sta.setODProportionTable(simu_config.getODProportionTable());
			sta.setODPathTable(simu_config.getODPathTable());
		}
	}

	private void initAgentContext() {
		agent_ctx.setSimulateEngine(simulator_engine);
		agent_ctx.setStepTimeLength(simu_config.simu_step_len);
	}

	private void initLines(){
		String[] lines = simu_config.getAllLines();
		for(String lineId : lines){
			Line line = new Line(lineId);
			line.init(agent_ctx);
			line.setTripPlans(simu_config.getTrainPlans(lineId));
		}		
	}
	
	private void initSimuEngines(){	
		
		simulator_engine.init();
		try {
			Collection<Line> lines = LineManager.getInstance().getAllLines();
			for(Line line : lines){
				String lid = line.getId();
				Class<?> clz = simu_config.getLineEngineClass(lid);
				LineSimulateEngine engine = (LineSimulateEngine) clz.newInstance();
				engine.initEngine(lid);
				simulator_engine.registLineSimulateEngine(lid, engine);
				simulator_engine.registTrainPlans(lid, simu_config.getLineTrainPlans(lid));
			}
	
			Collection<Station> stations = StationManager.getInstance().getAllStations();
			for(Station sta : stations){
				Class<?> clz = simu_config.getStationEngineClass(sta.getId());
				StationSimulateEngine engine = (StationSimulateEngine)clz.newInstance();
				engine.initEngine(sta.getId());
				simulator_engine.registStationSimulateEngine(sta.getId(), engine);
				if(sta.isTransferStation()){
					for(Station subSta : ((TransferStation)sta).getSubStations()){
						simulator_engine.registStationSimulateEngine(subSta.getId(), engine);					
					}
				}
			}			
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
	}

	public void run() {
		
		for(long curTime = simu_config.simu_start_time ; curTime <= simu_config.simu_end_time ; curTime += simu_config.simu_step_len){
			Date date = new Date();
			date.setTime(curTime);
			System.out.println("-------------------------------------- " + date.toString() + " --------------------------------------------------");

			Collection<Line> lines = LineManager.getInstance().getAllLines();
			Collection<Station> stations = StationManager.getInstance().getAllStations();

			for(Line line : lines){
				Train train = line.getNewStartTrain(curTime);
				if(train != null){
					train.init(agent_ctx);
					train.startTrip();
				}
			}
					
			for(Station station : stations){
				List<Person> persons = station.getNewStartPersons(curTime);
				if(persons != null){
					for(Person p : persons){
						p.init(agent_ctx);
						p.startTrip(curTime);
						//System.out.println("station : " + station.getId() + ";" + "person :" + p.toAgentString() );
					}
				}
			}
			
			
			
			simulator_engine.nextStep(curTime);
			
			for(Line line : lines){
				line.doCurrentStep(curTime);
			}
			
			for(Station station : stations){
				station.doCurrentStep(curTime);
			}		
		}
	}

}
