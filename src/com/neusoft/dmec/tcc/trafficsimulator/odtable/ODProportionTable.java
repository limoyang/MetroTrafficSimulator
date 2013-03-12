package com.neusoft.dmec.tcc.trafficsimulator.odtable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface ODProportionTable {

	List<ODProportion> getODProportionListByOID(long curTime, String station_id);
}
