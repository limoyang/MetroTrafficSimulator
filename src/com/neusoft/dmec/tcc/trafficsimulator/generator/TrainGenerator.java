package com.neusoft.dmec.tcc.trafficsimulator.generator;

import com.neusoft.dmec.tcc.trafficsimulator.agent.train.Train;

public interface TrainGenerator {
	public Train getNewTrain(String lid ,long curTime);
}
