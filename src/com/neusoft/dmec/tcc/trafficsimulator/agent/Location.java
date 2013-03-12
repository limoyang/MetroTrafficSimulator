package com.neusoft.dmec.tcc.trafficsimulator.agent;

public abstract class Location {

	public abstract String getId() ;

	public boolean isGate(){
		return false;
	}
	public boolean isSite(){
		return false;
	}

}
