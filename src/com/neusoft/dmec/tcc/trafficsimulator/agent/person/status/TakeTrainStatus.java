package com.neusoft.dmec.tcc.trafficsimulator.agent.person.status;

import com.neusoft.dmec.tcc.trafficsimulator.agent.line.Line;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.LineStation;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Station;
import com.neusoft.dmec.tcc.trafficsimulator.agent.train.Train;

public class TakeTrainStatus extends PersonStatus {
	
	Train train;
	Line line;
	Station from_station;
	Station to_station;
	long take_train_start_time;

	public TakeTrainStatus(Train t, Line line,	Station start_station, Station end_station, long curTime) {
		train = t;
		this.line = line;
		from_station = start_station;
		to_station = end_station;
		take_train_start_time = curTime;
	}

	@Override
	public PersonStatusType getStatusType() {
		return PersonStatusType.TAKE_TRAIN;
	}
}
