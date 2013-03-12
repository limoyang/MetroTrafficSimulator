package com.neusoft.dmec.tcc.trafficsimulator.model;

import com.neusoft.dmec.tcc.trafficsimulator.model.LineModel.Direction;

public class SiteModel {
	public SiteModel(String name, Direction dir) {
		direction = dir;
		site_name = name;
	}
	public Direction direction;
	public String site_name;
}
