package com.neusoft.dmec.tcc.trafficsimulator.engine;

import java.util.HashMap;
import java.util.List;

import com.neusoft.dmec.tcc.trafficsimulator.agent.person.Person;
import com.neusoft.dmec.tcc.trafficsimulator.agent.person.status.EnterStationStatus;
import com.neusoft.dmec.tcc.trafficsimulator.agent.person.status.PersonStatus;
import com.neusoft.dmec.tcc.trafficsimulator.agent.person.status.TransferStatus;
import com.neusoft.dmec.tcc.trafficsimulator.agent.person.status.TripOverStatus;
import com.neusoft.dmec.tcc.trafficsimulator.agent.person.status.PersonStatus.PersonStatusType;
import com.neusoft.dmec.tcc.trafficsimulator.agent.person.status.WaitTrainStatus;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Station;
import com.neusoft.dmec.tcc.trafficsimulator.agent.train.Train;
import com.neusoft.dmec.tcc.trafficsimulator.agent.train.status.NormalExitStatus;
import com.neusoft.dmec.tcc.trafficsimulator.agent.train.status.NormalMoveStatus;
import com.neusoft.dmec.tcc.trafficsimulator.agent.train.status.NormalStopStatus;
import com.neusoft.dmec.tcc.trafficsimulator.agent.train.status.TrainStatus;
import com.neusoft.dmec.tcc.trafficsimulator.agent.train.status.NormalStopStatus.TrainStopStep;
import com.neusoft.dmec.tcc.trafficsimulator.agent.train.status.TrainStatus.TrainStatusType;
import com.neusoft.dmec.tcc.trafficsimulator.engine.LineSimulateEngine.SimuTrainStatus;
import com.neusoft.dmec.tcc.trafficsimulator.engine.StationSimulateEngine.SimuPersonStatus;
import com.neusoft.dmec.tcc.trafficsimulator.tripplan.train.TrainTripPlan;
import com.neusoft.dmec.tcc.trafficsimulator.tripplan.train.TrainTripPlanElement;

public class CoreSimulatorEngine {

	private HashMap<String, StationSimulateEngine> station_engine_map;;
	private HashMap<String , LineSimulateEngine> line_engine_map;

	public CoreSimulatorEngine(){
		station_engine_map = new  HashMap<String, StationSimulateEngine>();
		line_engine_map = new HashMap<String , LineSimulateEngine>();
	}
		
	public void init() {				
	}
	
	public void registTrainPlans(String lineID , List<TrainTripPlan> plans){
		LineSimulateEngine lineEngine  = line_engine_map.get(lineID);
		if(lineEngine != null )
			lineEngine.setTrainPlans(plans);
	}
	
	public TrainStatus getCurrentTrainStatus(
			Train train,
			TrainStatus preStatus, 
			TrainTripPlanElement curPlan, 
			long curTime) 
	{
		
		String trainID = train.getID();		
		LineSimulateEngine lineEngine  = line_engine_map.get(train.getLine().getId());
		TrainStatusType statusType = preStatus.getStatusType();
		
		if(statusType == TrainStatusType.NORMAL_STOP){

			SimuTrainStatus simuStatus = lineEngine.getTrainStatus(trainID , curTime);

			NormalStopStatus stopStatus = (NormalStopStatus)preStatus;
			TrainStopStep stopStep = stopStatus.getStopStep();
			
			if(stopStep == TrainStopStep.TRAIN_ARRIVED){
				/*
				 *   train arrived , check if door opend
				 */
				if(simuStatus == SimuTrainStatus.STOP_ARRIVED){
					return stopStatus;
				}else if(simuStatus == SimuTrainStatus.STOP_DOOR_OPEN){
					stopStatus.setStopStep(TrainStopStep.DOOR_OPEN_OUT);
					stopStatus.setStepStartTime(curTime);
					return stopStatus;
				}else if(simuStatus == SimuTrainStatus.STOP_DOOR_CLOSED){
					stopStatus.setStopStep(TrainStopStep.DOOR_CLOSED);
					stopStatus.setStepStartTime(curTime);
					return stopStatus;
				}else if(simuStatus == SimuTrainStatus.MOVE_NORMAL){
					NormalMoveStatus moveStatus = new NormalMoveStatus();
					moveStatus.setStartTime(curTime);
					train.gotoNextPlanElement();
					return moveStatus;
				}else{
					assert(false);
				}
				
			}else if(stopStep == TrainStopStep.DOOR_OPEN_OUT){
				
				if(simuStatus == SimuTrainStatus.STOP_DOOR_OPEN){
					lineEngine.notifyTrainHoldDoorOpen(trainID , curTime);
					if(train.isPassengerAllGetOff(curPlan.stop_station_id)){
						stopStatus.setStopStep(TrainStopStep.DOOR_OPEN_IN);
						stopStatus.setStepStartTime(curTime);
					}
					return stopStatus;
				}else{
					/*
					 * exception
					 */
					assert(false);
				}
				
			}else if(stopStep == TrainStopStep.DOOR_OPEN_IN){
				
				if(simuStatus == SimuTrainStatus.STOP_DOOR_OPEN){
					
					if(canTrainCloseDoor(train , curPlan.stop_station_id , curPlan.stop_site_id , curTime) == true){
						lineEngine.notifyTrainCanCloseDoor(trainID , curTime);
					}
					return stopStatus;
				}else if(simuStatus == SimuTrainStatus.STOP_DOOR_CLOSED){
					stopStatus.setStopStep(TrainStopStep.DOOR_CLOSED);
					stopStatus.setStepStartTime(curTime);
					return stopStatus;
				}else{
					/*
					 * exception
					 */
					assert(false);
				}
			}else if(stopStep == TrainStopStep.DOOR_CLOSED){
				
				if(simuStatus == SimuTrainStatus.MOVE_NORMAL){
					NormalMoveStatus moveStatus = new NormalMoveStatus();
					moveStatus.setStartTime(curTime);
					train.gotoNextPlanElement();
					return moveStatus;
				}else if(simuStatus == SimuTrainStatus.STOP_DOOR_CLOSED){
					return stopStatus;
				}else if(simuStatus == SimuTrainStatus.TRIP_OVER){
					NormalExitStatus exitStatus = new NormalExitStatus();
					return exitStatus;
				}
				
			}else if(stopStep == TrainStopStep.TRAIN_DEPARTURE){
				
				if(simuStatus == SimuTrainStatus.MOVE_NORMAL){
					NormalMoveStatus moveStatus = new NormalMoveStatus();
					moveStatus.setStartTime(curTime);
					train.gotoNextPlanElement();
					return moveStatus;
				}else{
					/*
					 * exception
					 */
					assert(false);
				}				
			}else{
				assert(false);
			}
			
		}else if(statusType == TrainStatusType.NORMAL_MOVE){

			NormalMoveStatus moveStatus = (NormalMoveStatus)preStatus;
			
			SimuTrainStatus simuStatus = lineEngine.getTrainStatus(trainID , curTime);
			if(simuStatus == SimuTrainStatus.MOVE_NORMAL){
				return moveStatus;
			}else if(simuStatus == SimuTrainStatus.STOP_ARRIVED){
				NormalStopStatus stopStatus = new NormalStopStatus(curPlan);
				stopStatus.setStopStep(TrainStopStep.TRAIN_ARRIVED);
				stopStatus.setStepStartTime(curTime);
				return stopStatus;
			}else if(simuStatus == SimuTrainStatus.STOP_DOOR_OPEN){
				NormalStopStatus stopStatus = new NormalStopStatus(curPlan);
				stopStatus.setStopStep(TrainStopStep.DOOR_OPEN_IN);
				stopStatus.setStepStartTime(curTime);
				return stopStatus;
			}else{
				/*
				 * exception
				 */
				assert(false);
			}
		}else{
			assert(false);
		}
		
		return null;
	}

	
	private boolean canTrainCloseDoor(Train train, String stopStationId , String stopSiteId, long curTime) {
		return true;
	}

	public PersonStatus getCurrentPersonStatus(
			Person person,
			PersonStatus preStatus, 
			long curTime)
	{

		Station curStation = person.getCurrentStation();
		assert(curStation == null);
		
		
		StationSimulateEngine stationEngine = station_engine_map.get(curStation.getId());
		
		PersonStatusType statusType = PersonStatusType.ENTER_STATION;
		if(preStatus != null)
			statusType = preStatus.getStatusType();
		
		if(statusType == PersonStatusType.ENTER_STATION){
			EnterStationStatus enterStatus = (EnterStationStatus)preStatus;
			SimuPersonStatus simuStatus = stationEngine.getPersonStatus(person.getID(), curTime);
			if(simuStatus == SimuPersonStatus.ENTER_WALK){
				return enterStatus;
			}else if(simuStatus == SimuPersonStatus.WAIT_TRAIN){
				WaitTrainStatus waitStatus = new WaitTrainStatus(curStation , curTime);
				return waitStatus;
			}
		}else if(statusType == PersonStatusType.WAIT_TRAIN){
			assert(true);
			
		}else if(statusType == PersonStatusType.TAKE_TRAIN){
			assert(true);
			
		}else if(statusType == PersonStatusType.TRANSFER_STATION){
			TransferStatus transStatus = (TransferStatus)preStatus;
			SimuPersonStatus simuStatus = stationEngine.getPersonStatus(person.getID(), curTime);
			if(simuStatus == SimuPersonStatus.TRANSFER_WALK){
				return transStatus;
			}else if(simuStatus == SimuPersonStatus.WAIT_TRAIN){
				WaitTrainStatus waitStatus = new WaitTrainStatus(curStation , curTime);
				return waitStatus;
			}
			
		}else if(statusType == PersonStatusType.EXIT_STATION){
			SimuPersonStatus simuStatus = stationEngine.getPersonStatus(person.getID(), curTime);
			if(simuStatus == SimuPersonStatus.TRIP_OVER){
				return new TripOverStatus();
			}
			assert(true);
			
		}else if(statusType == PersonStatusType.TRIP_OVER){
			assert(true);
		}
		return preStatus;
	}

	public void nextStep(long curTime) {
		for(LineSimulateEngine engine : line_engine_map.values())
			engine.nextStep(curTime);
		
		for(StationSimulateEngine engine : station_engine_map.values())
			engine.nextStep(curTime);
	}

	public void registLineSimulateEngine(String line , LineSimulateEngine engine){
		line_engine_map.put(line, engine);
	}
	
	public void registStationSimulateEngine(String line , StationSimulateEngine engine){
		station_engine_map.put(line, engine);
	}

	public void addPerson(String staId, Person person ,long curTime){
		StationSimulateEngine engine = station_engine_map.get(staId);
		engine.addPerson(person.getID(), person.getCurrentFromLocation(), person.getCurrentToLocation(), curTime);
		
	}
	
	public void removePerson(String staId , Person person , long curTime){
		StationSimulateEngine engine = station_engine_map.get(staId);
		engine.removePerson(person.getID());
	}

}
