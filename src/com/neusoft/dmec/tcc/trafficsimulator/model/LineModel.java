package com.neusoft.dmec.tcc.trafficsimulator.model;


public class LineModel {

	public enum Direction {UPWARD , DOWNWARD};

	public final  String line_id;
	public final  String line_name;
	public String[] stations;
	public String[] sections;
	
	public LineModel(String id, String name,	String[] stas, String[] sects) {
		
		line_id = id;
		line_name = name;
		stations = stas;
		sections = sects;
	}

	public String toString(){
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("(");
		strBuf.append("id:").append(line_id).append(";");
		strBuf.append("name:").append(line_name).append(";");
		strBuf.append("stations:<");
		for(String station : stations){
			strBuf.append(station).append(";");
		}
		strBuf.append(">;");

		strBuf.append("sections:<");
		for(String section : sections){
			strBuf.append(section).append(";");
		}
		strBuf.append(">;");

		strBuf.append(")");
		
		return strBuf.toString();
	}

	public void init() {
		// TODO Auto-generated method stub
		
	}
}
