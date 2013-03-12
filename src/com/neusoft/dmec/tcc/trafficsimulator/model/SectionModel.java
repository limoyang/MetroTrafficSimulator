package com.neusoft.dmec.tcc.trafficsimulator.model;

import com.neusoft.dmec.tcc.trafficsimulator.model.LineModel.Direction;

public class SectionModel {
	
	public SectionModel(String sid, String lid, Direction dir, String fromsta, String tosta) {
		section_id = sid;
		line_id = lid;
		direction = dir;
		from_station_id = fromsta;
		to_station_id = tosta;
	}
	public final String section_id;
	public final String line_id;
	public final Direction direction;
	public final String from_station_id;
	public final String to_station_id;
	
	public String toString(){
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("(");
		strBuf.append("id:").append(section_id).append(";");
		strBuf.append("line_id:").append(line_id).append(";");
		strBuf.append("direction:").append((direction==Direction.UPWARD)?"上行":"下行").append(";");
		strBuf.append("from_sta:").append(from_station_id).append(";");
		strBuf.append("to_sta:").append(to_station_id).append(";");		
		strBuf.append(")");
		
		return strBuf.toString();
	}
	
	public void init(){
		
	}

}
