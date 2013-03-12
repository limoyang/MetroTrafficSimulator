package com.neusoft.dmec.tcc.trafficsimulator.agent.line;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LineManager {
	private static LineManager line_manager;
	
	private LineManager(){
		lines_map = new HashMap<String , Line>();
	};
	
	Map<String , Line> lines_map;

	public static LineManager getInstance(){
		if(line_manager == null){
			line_manager = new LineManager();
		}
		return line_manager;
	}
	
	public Line getLine(String id){
		return lines_map.get(id);
	}
	
	public void registLine(String id , Line line){
		lines_map.put(id, line);
	}

	public Collection<Line> getAllLines() {
		return lines_map.values();
	}

}
