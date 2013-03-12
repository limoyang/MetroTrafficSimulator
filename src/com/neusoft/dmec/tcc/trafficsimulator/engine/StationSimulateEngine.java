package com.neusoft.dmec.tcc.trafficsimulator.engine;

import com.neusoft.dmec.tcc.trafficsimulator.agent.Location;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Station;

public interface StationSimulateEngine {
	public enum SimuPersonStatus { ENTER_WALK , WAIT_TRAIN , EXIT_WALK , TAKE_TRAIN , TRANSFER_WALK, TRIP_OVER };
	public boolean initEngine(String sid);
	public SimuPersonStatus getPersonStatus(String pid, long curTime);
	public void addPerson(String pid, Location from, Location to, long curTime);
	public void removePerson(String pid);
	public void nextStep(long curTime);
}
