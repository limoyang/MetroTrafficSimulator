package com.neusoft.dmec.tcc.trafficsimulator.model;

public class TrainModel {

	public TrainModel(String lineId, int capacity , int open, int close) {
		line_id = lineId;
		max_capacity = capacity;
		time_before_door_open = open;
		time_before_door_close = close;
	}
	public String line_id;
	public int max_capacity;
	public long time_before_door_open;
	public long time_before_door_close;

	public void init() {
	}

}
