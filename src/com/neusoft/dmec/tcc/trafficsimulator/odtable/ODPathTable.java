package com.neusoft.dmec.tcc.trafficsimulator.odtable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface ODPathTable {

	String getTableString();

	List<ODPath> getODPathList(ODPair pair);

	float getOPPathProportion(long curTime, ODPath path);


}
