package com.neusoft.dmec.tcc.trafficsimulator.agent.line;

import java.util.LinkedList;
import java.util.List;

import com.neusoft.dmec.tcc.trafficsimulator.agent.Agent;
import com.neusoft.dmec.tcc.trafficsimulator.agent.AgentContext;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.LineStation;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Station;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.TransferStation;
import com.neusoft.dmec.tcc.trafficsimulator.agent.train.Train;
import com.neusoft.dmec.tcc.trafficsimulator.generator.DefaultTrainGenerator;
import com.neusoft.dmec.tcc.trafficsimulator.generator.TrainGenerator;
import com.neusoft.dmec.tcc.trafficsimulator.model.BasicModelManager;
import com.neusoft.dmec.tcc.trafficsimulator.model.LineModel;
import com.neusoft.dmec.tcc.trafficsimulator.tripplan.train.TrainTripPlan;

public class Line  extends Agent{
	LineModel line_info;
	LineStation[] stations;
	Section[] sections;

	List<Train> online_trains;
	
	TrainGenerator train_generator;
	
	private AgentContext agent_ctx;
	
	public Line(String lineId) {
		line_info = BasicModelManager.getInstance().getLineModel(lineId);
		online_trains = new LinkedList<Train>();
	}

	public void removeTrain(Train train) {
		online_trains.remove(train);
	}
	
	public void addTrain(Train train) {
		online_trains.add(train);
	}


	@Override
	protected void doCurrentStepImpl(long time) {
		if(online_trains.isEmpty() == false){
			Train[] trains = online_trains.toArray(new Train[0]);
			for(Train train : trains){
				if(train.isExit())
					online_trains.remove(train);
				else
					train.doCurrentStep(time);
			}
		}
	}

	public String getId() {
		return line_info.line_id;
	}

	@Override
	public void init(AgentContext ctx) {
		
		agent_ctx = ctx;
		
		int i = 0 , j=0;
		stations = new LineStation[line_info.stations.length];
		for(String stationId : line_info.stations){
			stations[i] = new LineStation(stationId);
			stations[i].init(agent_ctx);
			i++;
		}
		
		sections = new Section[line_info.sections.length];
		for(String sectId : line_info.sections){
			sections[j] = new Section(sectId);
			sections[j].init(agent_ctx);
			j++;
		}
		
		LineManager.getInstance().registLine(line_info.line_id, this);
	}

	public void setTripPlans(List<TrainTripPlan> plans){
		train_generator = new DefaultTrainGenerator(plans);
	}
	
	public Train getNewStartTrain(long curTime) {
		Train train = train_generator.getNewTrain(line_info.line_id, curTime);
		if(train != null)
			train.init(agent_ctx);
		
		return train;
	}

	public LineStation[] getLineStations(){
		return stations;
	}
	
	public Station[] getStations(){
		return null;
	}
	
	public TransferStation[] getTransferStations(){
		List<TransferStation> tranStations = new LinkedList<TransferStation>();
		for(LineStation s : stations){
			if(s.isSubStation())
				tranStations.add(s.getTransferStation());
		}
		return tranStations.toArray(new TransferStation[0]);
	}
	
	
	@Override
	public String toAgentDebugString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("line id : ").append(line_info.line_id).append('\n');
		sbuf.append("line name : ").append(line_info.line_name).append('\n');
		sbuf.append("stations : ").append('\n');
		
		for(LineStation station : stations){
			sbuf.append(station.toAgentDebugString());
		}

		sbuf.append("sections : ").append('\n');
		for(Section section : sections){
			sbuf.append(section.toAgentDebugString());
		}
		
		return sbuf.toString();
	}

}
