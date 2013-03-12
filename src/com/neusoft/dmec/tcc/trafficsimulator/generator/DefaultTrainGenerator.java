package com.neusoft.dmec.tcc.trafficsimulator.generator;

import java.util.List;

import com.neusoft.dmec.tcc.trafficsimulator.agent.train.Train;
import com.neusoft.dmec.tcc.trafficsimulator.tripplan.train.TrainTripPlan;

public class DefaultTrainGenerator implements TrainGenerator{

	List<TrainTripPlan> train_plans;
	
	public DefaultTrainGenerator(List<TrainTripPlan> plans){
		train_plans = plans;
	}

	public Train getNewTrain(String lid ,long curTime){
		
		if(train_plans != null && train_plans.isEmpty() == false){
			for(TrainTripPlan plan : train_plans){
				if(plan.isStarted() == false && plan.isOver() == false){
					if(curTime >= plan.getStartTime()){
						plan.startPlan();
						Train train = new Train(plan.getTrainID() , lid , plan);
						return train;
					}
				}
			}
		}
		return null;
	}
}
