package com.neusoft.dmec.tcc.trafficsimulator.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.neusoft.dmec.tcc.trafficsimulator.NetworkTrafficSimulator;
import com.neusoft.dmec.tcc.trafficsimulator.NetworkTrafficSimulatorConfig;
import com.neusoft.dmec.tcc.trafficsimulator.agent.line.Line;
import com.neusoft.dmec.tcc.trafficsimulator.agent.line.LineManager;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.LineStation;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.Station;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.StationManager;
import com.neusoft.dmec.tcc.trafficsimulator.agent.station.TransferStation;
import com.neusoft.dmec.tcc.trafficsimulator.model.BasicModelManager;
import com.neusoft.dmec.tcc.trafficsimulator.model.GateModel;
import com.neusoft.dmec.tcc.trafficsimulator.model.LineModel;
import com.neusoft.dmec.tcc.trafficsimulator.model.LineModel.Direction;
import com.neusoft.dmec.tcc.trafficsimulator.model.SectionModel;
import com.neusoft.dmec.tcc.trafficsimulator.model.SiteModel;
import com.neusoft.dmec.tcc.trafficsimulator.model.StationModel;
import com.neusoft.dmec.tcc.trafficsimulator.model.TrainModel;
import com.neusoft.dmec.tcc.trafficsimulator.odtable.ODPair;
import com.neusoft.dmec.tcc.trafficsimulator.odtable.ODPath;
import com.neusoft.dmec.tcc.trafficsimulator.odtable.ODPathElement;
import com.neusoft.dmec.tcc.trafficsimulator.odtable.ODPathTable;
import com.neusoft.dmec.tcc.trafficsimulator.odtable.ODProportionTable;
import com.neusoft.dmec.tcc.trafficsimulator.tripplan.train.TrainTripPlan;

public class SimulatorSimpleTest {

	static final StationModel[] Stations_Description = {
		/********************line 1******************************/
		new StationModel("0101" , "01" , "PingGuYuan", false ,null),
		new StationModel("0102" , "01" , "GuChengLu", false ,null),
		new StationModel("0103" , "01" , "BaJiaoYouLeYuan", false ,null),
		new StationModel("0104" , "01" , "BaBaoShan", false ,null),
		new StationModel("0105" , "01" , "YuQuanLu", false ,null),
		new StationModel("0106" , "01" , "WuKeSong", false ,null),
		new StationModel("0107" , "01" , "WanShouLu", false ,null),
		new StationModel("0108" , "01" , "GongZhuFen", false ,null),
		new StationModel("0109" , "01" , "JunShiBoWuGuan", false ,null),
		new StationModel("0110" , "01" , "MuXiDi", false ,null),
		new StationModel("0111" , "01" , "NanLiShiLu", false ,null),
		new StationModel("0112" , "01" , "Fuxingmen(1)", true ,"H01"),
		new StationModel("0113" , "01" , "Tian'anmenxi", false ,null),
		new StationModel("0114" , "01" , "Tian'anmendong", false ,null),
		new StationModel("0115" , "01" , "Wangfujing", false ,null),
		
		/************************line 2******************************/
		new StationModel("0201" , "02" , "DongZhiMen", false ,null),
		new StationModel("0202" , "02" , "YongHeGong", false ,null),
		new StationModel("0203" , "02" , "AnDingMen", false ,null),
		new StationModel("0204" , "02" , "GuLouDaJie", false ,null),
		new StationModel("0205" , "02" , "JiShuiTan", false ,null),
		new StationModel("0206" , "02" , "XiZhiMen", false ,null),
		new StationModel("0207" , "02" , "CheGongZhuang", false ,null),
		new StationModel("0208" , "02" , "FuChengMen", false ,null),
		new StationModel("0209" , "02" , "FuXingMen(2)", true ,"H01"),
		new StationModel("0210" , "02" , "ChangChunJie", false ,null),
		new StationModel("0211" , "02" , "XuanWuMen", false ,null),
		/*************************transfer*****************************/
		new StationModel("H01" , "FuXingMen" , new String[]{"0112" , "0209"})
	};
	
	static final SectionModel[] Sections_Description = {
		/**********************************line 1 ***************************************************/
		new SectionModel("0101-0102" , "01" , LineModel.Direction.UPWARD, "0101" , "0102"),
		new SectionModel("0102-0103" , "01" , LineModel.Direction.UPWARD, "0102" , "0103"),
		new SectionModel("0103-0104" , "01" , LineModel.Direction.UPWARD, "0103" , "0104"),
		new SectionModel("0104-0105" , "01" , LineModel.Direction.UPWARD, "0104" , "0105"),
		new SectionModel("0105-0106" , "01" , LineModel.Direction.UPWARD, "0105" , "0106"),
		new SectionModel("0106-0107" , "01" , LineModel.Direction.UPWARD, "0106" , "0107"),
		new SectionModel("0107-0108" , "01" , LineModel.Direction.UPWARD, "0107" , "0108"),
		new SectionModel("0108-0109" , "01" , LineModel.Direction.UPWARD, "0108" , "0109"),
		new SectionModel("0109-0110" , "01" , LineModel.Direction.UPWARD, "0109" , "0110"),
		new SectionModel("0110-0111" , "01" , LineModel.Direction.UPWARD, "0110" , "0111"),
		new SectionModel("0111-0112" , "01" , LineModel.Direction.UPWARD, "0111" , "0112"),
		new SectionModel("0112-0113" , "01" , LineModel.Direction.UPWARD, "0112" , "0113"),
		new SectionModel("0113-0114" , "01" , LineModel.Direction.UPWARD, "0113" , "0114"),
		new SectionModel("0114-0115" , "01" , LineModel.Direction.UPWARD, "0114" , "0115"),
		
		/**********************************line 2 ***************************************************/
		new SectionModel("0201-0202" , "02" , LineModel.Direction.UPWARD, "0201" , "0202"),
		new SectionModel("0202-0203" , "02" , LineModel.Direction.UPWARD, "0202" , "0203"),
		new SectionModel("0203-0204" , "02" , LineModel.Direction.UPWARD, "0203" , "0204"),
		new SectionModel("0204-0205" , "02" , LineModel.Direction.UPWARD, "0204" , "0205"),
		new SectionModel("0205-0206" , "02" , LineModel.Direction.UPWARD, "0205" , "0206"),
		new SectionModel("0206-0207" , "02" , LineModel.Direction.UPWARD, "0206" , "0207"),
		new SectionModel("0207-0208" , "02" , LineModel.Direction.UPWARD, "0207" , "0208"),
		new SectionModel("0208-0209" , "02" , LineModel.Direction.UPWARD, "0208" , "0209"),
		new SectionModel("0209-0210" , "02" , LineModel.Direction.UPWARD, "0209" , "0210"),
		new SectionModel("0210-0211" , "02" , LineModel.Direction.UPWARD, "0210" , "0211"),		
	};

	static final TrainModel[] Line_Train_Description = {
		new TrainModel("01" , 1000000 , 2000 , 5000),
		new TrainModel("02" , 1000000 , 3000 , 8000),
	};

	private static final String[] line_no1_stations = {"0101","0102","0103","0104","0105","0106","0107","0108","0109","0110","0111","0112","0113","0114","0115"};
	private static final String[] line_no1_sections = {"0101-0102","0102-0103","0103-0104","0104-0105","0105-0106","0106-0107","0107-0108","0108-0109","0109-0110","0110-0111","0111-0112","0112-0113","0113-0114","0114-0115"};

	private static final String[] line_no2_stations = {"0201","0202","0203","0204","0205","0206","0207","0208","0209","0210","0211"};
	private static final String[] line_no2_sections = {"0201-0202","0202-0203","0203-0204","0204-0205","0205-0206","0206-0207","0207-0208","0208-0209","0209-0210","0210-0211"};

	static final LineModel line_no1_info = new LineModel("01" ,  "1号线" , line_no1_stations , line_no1_sections);
	static final LineModel line_no2_info = new LineModel("02" ,  "2号线" , line_no2_stations , line_no2_sections);

	
	static final String[][][] Test_Line1_Train_Plans = {
		{
			{"01#1101" , "0101" , "UPWARD" , "2013-01-23 06:00:02" , "2013-01-23 06:00:52" , "NORMAL"},
			{"01#1101" , "0102" , "UPWARD" , "2013-01-23 06:02:21" , "2013-01-23 06:03:12" , "NORMAL"},
			{"01#1101" , "0103" , "UPWARD" , "2013-01-23 06:05:09" , "2013-01-23 06:06:01" , "NORMAL"},
			{"01#1101" , "0104" , "UPWARD" , "2013-01-23 06:08:02" , "2013-01-23 06:08:52" , "NORMAL"},
			{"01#1101" , "0105" , "UPWARD" , "2013-01-23 06:10:22" , "2013-01-23 06:11:22" , "NORMAL"},
			{"01#1101" , "0106" , "UPWARD" , "2013-01-23 06:13:32" , "2013-01-23 06:14:20" , "NORMAL"},
			{"01#1101" , "0107" , "UPWARD" , "2013-01-23 06:17:11" , "2013-01-23 06:18:03" , "NORMAL"},
			{"01#1101" , "0108" , "UPWARD" , "2013-01-23 06:20:06" , "2013-01-23 06:20:56" , "NORMAL"},
			{"01#1101" , "0109" , "UPWARD" , "2013-01-23 06:24:07" , "2013-01-23 06:25:01" , "NORMAL"},
			{"01#1101" , "0110" , "UPWARD" , "2013-01-23 06:27:43" , "2013-01-23 06:28:36" , "NORMAL"},
			{"01#1101" , "0111" , "UPWARD" , "2013-01-23 06:30:02" , "2013-01-23 06:30:57" , "NORMAL"},
			{"01#1101" , "0112" , "UPWARD" , "2013-01-23 06:32:08" , "2013-01-23 06:33:01" , "NORMAL"},
			{"01#1101" , "0113" , "UPWARD" , "2013-01-23 06:36:12" , "2013-01-23 06:37:04" , "NORMAL"},
			{"01#1101" , "0114" , "UPWARD" , "2013-01-23 06:40:43" , "2013-01-23 06:41:36" , "NORMAL"},
			{"01#1101" , "0115" , "UPWARD" , "2013-01-23 06:44:02" , "2013-01-23 06:44:57" , "NORMAL"}

		},
		{
			{"01#1102" , "0101" , "UPWARD" , "2013-01-23 06:25:02" , "2013-01-23 06:25:52" , "NORMAL"},
			{"01#1102" , "0102" , "UPWARD" , "2013-01-23 06:27:21" , "2013-01-23 06:28:12" , "NORMAL"},
			{"01#1102" , "0103" , "UPWARD" , "2013-01-23 06:30:09" , "2013-01-23 06:31:01" , "NORMAL"},
			{"01#1102" , "0104" , "UPWARD" , "2013-01-23 06:33:02" , "2013-01-23 06:33:52" , "NORMAL"},
			{"01#1102" , "0105" , "UPWARD" , "2013-01-23 06:35:22" , "2013-01-23 06:36:22" , "NORMAL"},
			{"01#1102" , "0106" , "UPWARD" , "2013-01-23 06:38:32" , "2013-01-23 06:39:20" , "NORMAL"},
			{"01#1102" , "0107" , "UPWARD" , "2013-01-23 06:42:11" , "2013-01-23 06:43:03" , "NORMAL"},
			{"01#1102" , "0108" , "UPWARD" , "2013-01-23 06:45:06" , "2013-01-23 06:45:56" , "NORMAL"},
			{"01#1102" , "0109" , "UPWARD" , "2013-01-23 06:49:07" , "2013-01-23 06:50:01" , "NORMAL"},
			{"01#1102" , "0110" , "UPWARD" , "2013-01-23 06:52:43" , "2013-01-23 06:53:36" , "NORMAL"},
			{"01#1102" , "0111" , "UPWARD" , "2013-01-23 06:55:02" , "2013-01-23 06:55:57" , "NORMAL"},
			{"01#1102" , "0112" , "UPWARD" , "2013-01-23 06:57:08" , "2013-01-23 06:58:01" , "NORMAL"},
			{"01#1102" , "0113" , "UPWARD" , "2013-01-23 07:02:02" , "2013-01-23 07:03:04" , "NORMAL"},
			{"01#1102" , "0114" , "UPWARD" , "2013-01-23 07:06:53" , "2013-01-23 07:07:36" , "NORMAL"},
			{"01#1102" , "0115" , "UPWARD" , "2013-01-23 07:10:02" , "2013-01-23 07:10:57" , "NORMAL"}
		},
		{
			{"01#1103" , "0101" , "UPWARD" , "2013-01-23 06:45:02" , "2013-01-23 06:45:52" , "NORMAL"},
			{"01#1103" , "0102" , "UPWARD" , "2013-01-23 06:47:21" , "2013-01-23 06:48:12" , "NORMAL"},
			{"01#1103" , "0103" , "UPWARD" , "2013-01-23 06:50:09" , "2013-01-23 06:51:01" , "NORMAL"},
			{"01#1103" , "0104" , "UPWARD" , "2013-01-23 06:53:02" , "2013-01-23 06:53:52" , "NORMAL"},
			{"01#1103" , "0105" , "UPWARD" , "2013-01-23 06:55:22" , "2013-01-23 06:56:22" , "NORMAL"},
			{"01#1103" , "0106" , "UPWARD" , "2013-01-23 06:58:32" , "2013-01-23 06:59:20" , "NORMAL"},
			{"01#1103" , "0107" , "UPWARD" , "2013-01-23 07:02:11" , "2013-01-23 07:03:03" , "NORMAL"},
			{"01#1103" , "0108" , "UPWARD" , "2013-01-23 07:05:06" , "2013-01-23 07:05:56" , "NORMAL"},
			{"01#1103" , "0109" , "UPWARD" , "2013-01-23 07:09:07" , "2013-01-23 07:10:01" , "NORMAL"},
			{"01#1103" , "0110" , "UPWARD" , "2013-01-23 07:12:43" , "2013-01-23 07:13:36" , "NORMAL"},
			{"01#1103" , "0111" , "UPWARD" , "2013-01-23 07:15:02" , "2013-01-23 07:15:57" , "NORMAL"},
			{"01#1103" , "0112" , "UPWARD" , "2013-01-23 07:17:08" , "2013-01-23 07:18:01" , "NORMAL"},
			{"01#1103" , "0113" , "UPWARD" , "2013-01-23 07:22:02" , "2013-01-23 07:23:04" , "NORMAL"},
			{"01#1103" , "0114" , "UPWARD" , "2013-01-23 07:26:53" , "2013-01-23 07:27:36" , "NORMAL"},
			{"01#1103" , "0115" , "UPWARD" , "2013-01-23 07:30:02" , "2013-01-23 07:30:57" , "NORMAL"}
		},
		/*downward*/
		{
			{"01#2101" , "0115" , "DOWNWARD" , "2013-01-23 06:00:02" , "2013-01-23 06:00:52" , "NORMAL"},
			{"01#2101" , "0114" , "DOWNWARD" , "2013-01-23 06:02:21" , "2013-01-23 06:03:12" , "NORMAL"},
			{"01#2101" , "0113" , "DOWNWARD" , "2013-01-23 06:05:09" , "2013-01-23 06:06:01" , "NORMAL"},
			{"01#2101" , "0112" , "DOWNWARD" , "2013-01-23 06:08:02" , "2013-01-23 06:08:52" , "NORMAL"},
			{"01#2101" , "0111" , "DOWNWARD" , "2013-01-23 06:10:22" , "2013-01-23 06:11:22" , "NORMAL"},
			{"01#2101" , "0110" , "DOWNWARD" , "2013-01-23 06:13:32" , "2013-01-23 06:14:20" , "NORMAL"},
			{"01#2101" , "0109" , "DOWNWARD" , "2013-01-23 06:17:11" , "2013-01-23 06:18:03" , "NORMAL"},
			{"01#2101" , "0108" , "DOWNWARD" , "2013-01-23 06:20:06" , "2013-01-23 06:20:56" , "NORMAL"},
			{"01#2101" , "0107" , "DOWNWARD" , "2013-01-23 06:24:07" , "2013-01-23 06:25:01" , "NORMAL"},
			{"01#2101" , "0106" , "DOWNWARD" , "2013-01-23 06:27:43" , "2013-01-23 06:28:36" , "NORMAL"},
			{"01#2101" , "0105" , "DOWNWARD" , "2013-01-23 06:30:02" , "2013-01-23 06:30:57" , "NORMAL"},
			{"01#2101" , "0104" , "DOWNWARD" , "2013-01-23 06:32:08" , "2013-01-23 06:33:01" , "NORMAL"},
			{"01#2101" , "0103" , "DOWNWARD" , "2013-01-23 06:36:12" , "2013-01-23 06:37:04" , "NORMAL"},
			{"01#2101" , "0102" , "DOWNWARD" , "2013-01-23 06:40:43" , "2013-01-23 06:41:36" , "NORMAL"},
			{"01#2101" , "0101" , "DOWNWARD" , "2013-01-23 06:44:02" , "2013-01-23 06:44:57" , "NORMAL"}

		},
		{
			{"01#2102" , "0115" , "DOWNWARD" , "2013-01-23 06:25:02" , "2013-01-23 06:25:52" , "NORMAL"},
			{"01#2102" , "0114" , "DOWNWARD" , "2013-01-23 06:27:21" , "2013-01-23 06:28:12" , "NORMAL"},
			{"01#2102" , "0113" , "DOWNWARD" , "2013-01-23 06:30:09" , "2013-01-23 06:31:01" , "NORMAL"},
			{"01#2102" , "0112" , "DOWNWARD" , "2013-01-23 06:33:02" , "2013-01-23 06:33:52" , "NORMAL"},
			{"01#2102" , "0111" , "DOWNWARD" , "2013-01-23 06:35:22" , "2013-01-23 06:36:22" , "NORMAL"},
			{"01#2102" , "0110" , "DOWNWARD" , "2013-01-23 06:38:32" , "2013-01-23 06:39:20" , "NORMAL"},
			{"01#2102" , "0109" , "DOWNWARD" , "2013-01-23 06:42:11" , "2013-01-23 06:43:03" , "NORMAL"},
			{"01#2102" , "0108" , "DOWNWARD" , "2013-01-23 06:45:06" , "2013-01-23 06:45:56" , "NORMAL"},
			{"01#2102" , "0107" , "DOWNWARD" , "2013-01-23 06:49:07" , "2013-01-23 06:50:01" , "NORMAL"},
			{"01#2102" , "0106" , "DOWNWARD" , "2013-01-23 06:52:43" , "2013-01-23 06:53:36" , "NORMAL"},
			{"01#2102" , "0105" , "DOWNWARD" , "2013-01-23 06:55:02" , "2013-01-23 06:55:57" , "NORMAL"},
			{"01#2102" , "0104" , "DOWNWARD" , "2013-01-23 06:57:08" , "2013-01-23 06:58:01" , "NORMAL"},
			{"01#2102" , "0103" , "DOWNWARD" , "2013-01-23 07:02:02" , "2013-01-23 07:03:04" , "NORMAL"},
			{"01#2102" , "0102" , "DOWNWARD" , "2013-01-23 07:06:53" , "2013-01-23 07:07:36" , "NORMAL"},
			{"01#2102" , "0101" , "DOWNWARD" , "2013-01-23 07:10:02" , "2013-01-23 07:10:57" , "NORMAL"}
		},
		{
			{"01#2103" , "0115" , "DOWNWARD" , "2013-01-23 06:45:02" , "2013-01-23 06:45:52" , "NORMAL"},
			{"01#2103" , "0114" , "DOWNWARD" , "2013-01-23 06:47:21" , "2013-01-23 06:48:12" , "NORMAL"},
			{"01#2103" , "0113" , "DOWNWARD" , "2013-01-23 06:50:09" , "2013-01-23 06:51:01" , "NORMAL"},
			{"01#2103" , "0112" , "DOWNWARD" , "2013-01-23 06:53:02" , "2013-01-23 06:53:52" , "NORMAL"},
			{"01#2103" , "0111" , "DOWNWARD" , "2013-01-23 06:55:22" , "2013-01-23 06:56:22" , "NORMAL"},
			{"01#2103" , "0110" , "DOWNWARD" , "2013-01-23 06:58:32" , "2013-01-23 06:59:20" , "NORMAL"},
			{"01#2103" , "0109" , "DOWNWARD" , "2013-01-23 07:02:11" , "2013-01-23 07:03:03" , "NORMAL"},
			{"01#2103" , "0108" , "DOWNWARD" , "2013-01-23 07:05:06" , "2013-01-23 07:05:56" , "NORMAL"},
			{"01#2103" , "0107" , "DOWNWARD" , "2013-01-23 07:09:07" , "2013-01-23 07:10:01" , "NORMAL"},
			{"01#2103" , "0106" , "DOWNWARD" , "2013-01-23 07:12:43" , "2013-01-23 07:13:36" , "NORMAL"},
			{"01#2103" , "0105" , "DOWNWARD" , "2013-01-23 07:15:02" , "2013-01-23 07:15:57" , "NORMAL"},
			{"01#2103" , "0104" , "DOWNWARD" , "2013-01-23 07:17:08" , "2013-01-23 07:18:01" , "NORMAL"},
			{"01#2103" , "0103" , "DOWNWARD" , "2013-01-23 07:22:02" , "2013-01-23 07:23:04" , "NORMAL"},
			{"01#2103" , "0102" , "DOWNWARD" , "2013-01-23 07:26:53" , "2013-01-23 07:27:36" , "NORMAL"},
			{"01#2103" , "0101" , "DOWNWARD" , "2013-01-23 07:30:02" , "2013-01-23 07:30:57" , "NORMAL"}
		}
	};
	
	static final String[][][] Test_Line2_Train_Plans = {
		{
			{"02#1101" , "0201" , "UPWARD" , "2013-01-23 06:01:02" , "2013-01-23 06:01:42" , "NORMAL"},
			{"02#1101" , "0202" , "UPWARD" , "2013-01-23 06:03:21" , "2013-01-23 06:04:02" , "NORMAL"},
			{"02#1101" , "0203" , "UPWARD" , "2013-01-23 06:06:09" , "2013-01-23 06:07:06" , "NORMAL"},
			{"02#1101" , "0204" , "UPWARD" , "2013-01-23 06:09:02" , "2013-01-23 06:09:42" , "NORMAL"},
			{"02#1101" , "0205" , "UPWARD" , "2013-01-23 06:11:22" , "2013-01-23 06:12:12" , "NORMAL"},
			{"02#1101" , "0206" , "UPWARD" , "2013-01-23 06:14:32" , "2013-01-23 06:15:10" , "NORMAL"},
			{"02#1101" , "0207" , "UPWARD" , "2013-01-23 06:18:11" , "2013-01-23 06:19:07" , "NORMAL"},
			{"02#1101" , "0208" , "UPWARD" , "2013-01-23 06:21:06" , "2013-01-23 06:21:46" , "NORMAL"},
			{"02#1101" , "0209" , "UPWARD" , "2013-01-23 06:25:07" , "2013-01-23 06:26:09" , "NORMAL"},
			{"02#1101" , "0210" , "UPWARD" , "2013-01-23 06:28:43" , "2013-01-23 06:29:26" , "NORMAL"},
			{"02#1101" , "0211" , "UPWARD" , "2013-01-23 06:31:02" , "2013-01-23 06:31:47" , "NORMAL"}
		},
		{
			{"02#1102" , "0201" , "UPWARD" , "2013-01-23 06:25:12" , "2013-01-23 06:26:12" , "NORMAL"},
			{"02#1102" , "0202" , "UPWARD" , "2013-01-23 06:27:31" , "2013-01-23 06:28:22" , "NORMAL"},
			{"02#1102" , "0203" , "UPWARD" , "2013-01-23 06:30:19" , "2013-01-23 06:31:11" , "NORMAL"},
			{"02#1102" , "0204" , "UPWARD" , "2013-01-23 06:33:12" , "2013-01-23 06:34:02" , "NORMAL"},
			{"02#1102" , "0205" , "UPWARD" , "2013-01-23 06:35:32" , "2013-01-23 06:36:32" , "NORMAL"},
			{"02#1102" , "0206" , "UPWARD" , "2013-01-23 06:38:42" , "2013-01-23 06:39:30" , "NORMAL"},
			{"02#1102" , "0207" , "UPWARD" , "2013-01-23 06:42:21" , "2013-01-23 06:43:13" , "NORMAL"},
			{"02#1102" , "0208" , "UPWARD" , "2013-01-23 06:45:16" , "2013-01-23 06:46:06" , "NORMAL"},
			{"02#1102" , "0209" , "UPWARD" , "2013-01-23 06:49:17" , "2013-01-23 06:50:11" , "NORMAL"},
			{"02#1102" , "0210" , "UPWARD" , "2013-01-23 06:52:53" , "2013-01-23 06:53:46" , "NORMAL"},
			{"02#1102" , "0211" , "UPWARD" , "2013-01-23 06:55:12" , "2013-01-23 06:56:07" , "NORMAL"}
		},
		{
			{"02#1103" , "0201" , "UPWARD" , "2013-01-23 06:45:12" , "2013-01-23 06:46:12" , "NORMAL"},
			{"02#1103" , "0202" , "UPWARD" , "2013-01-23 06:47:31" , "2013-01-23 06:48:22" , "NORMAL"},
			{"02#1103" , "0203" , "UPWARD" , "2013-01-23 06:50:19" , "2013-01-23 06:51:11" , "NORMAL"},
			{"02#1103" , "0204" , "UPWARD" , "2013-01-23 06:53:12" , "2013-01-23 06:54:02" , "NORMAL"},
			{"02#1103" , "0205" , "UPWARD" , "2013-01-23 06:55:32" , "2013-01-23 06:56:32" , "NORMAL"},
			{"02#1103" , "0206" , "UPWARD" , "2013-01-23 06:58:42" , "2013-01-23 06:59:30" , "NORMAL"},
			{"02#1103" , "0207" , "UPWARD" , "2013-01-23 07:02:21" , "2013-01-23 07:03:13" , "NORMAL"},
			{"02#1103" , "0208" , "UPWARD" , "2013-01-23 07:05:16" , "2013-01-23 07:06:06" , "NORMAL"},
			{"02#1103" , "0209" , "UPWARD" , "2013-01-23 07:09:17" , "2013-01-23 07:10:11" , "NORMAL"},
			{"02#1103" , "0210" , "UPWARD" , "2013-01-23 07:12:53" , "2013-01-23 07:13:46" , "NORMAL"},
			{"02#1103" , "0211" , "UPWARD" , "2013-01-23 07:15:12" , "2013-01-23 07:16:07" , "NORMAL"}
		},
		
		/*downward*/
		{
			{"02#2101" , "0211" , "DOWNWARD" , "2013-01-23 06:02:02" , "2013-01-23 06:02:42" , "NORMAL"},
			{"02#2101" , "0210" , "DOWNWARD" , "2013-01-23 06:04:21" , "2013-01-23 06:05:02" , "NORMAL"},
			{"02#2101" , "0209" , "DOWNWARD" , "2013-01-23 06:07:09" , "2013-01-23 06:08:06" , "NORMAL"},
			{"02#2101" , "0208" , "DOWNWARD" , "2013-01-23 06:10:02" , "2013-01-23 06:10:42" , "NORMAL"},
			{"02#2101" , "0207" , "DOWNWARD" , "2013-01-23 06:12:22" , "2013-01-23 06:13:12" , "NORMAL"},
			{"02#2101" , "0206" , "DOWNWARD" , "2013-01-23 06:15:32" , "2013-01-23 06:16:10" , "NORMAL"},
			{"02#2101" , "0205" , "DOWNWARD" , "2013-01-23 06:19:11" , "2013-01-23 06:20:07" , "NORMAL"},
			{"02#2101" , "0204" , "DOWNWARD" , "2013-01-23 06:22:06" , "2013-01-23 06:22:46" , "NORMAL"},
			{"02#2101" , "0203" , "DOWNWARD" , "2013-01-23 06:26:07" , "2013-01-23 06:27:09" , "NORMAL"},
			{"02#2101" , "0202" , "DOWNWARD" , "2013-01-23 06:29:43" , "2013-01-23 06:30:26" , "NORMAL"},
			{"02#2101" , "0201" , "DOWNWARD" , "2013-01-23 06:32:02" , "2013-01-23 06:32:47" , "NORMAL"}
		},
		{
			{"02#2102" , "0211" , "DOWNWARD" , "2013-01-23 06:26:12" , "2013-01-23 06:27:12" , "NORMAL"},
			{"02#2102" , "0210" , "DOWNWARD" , "2013-01-23 06:28:31" , "2013-01-23 06:29:22" , "NORMAL"},
			{"02#2102" , "0209" , "DOWNWARD" , "2013-01-23 06:31:19" , "2013-01-23 06:32:11" , "NORMAL"},
			{"02#2102" , "0208" , "DOWNWARD" , "2013-01-23 06:34:12" , "2013-01-23 06:35:02" , "NORMAL"},
			{"02#2102" , "0207" , "DOWNWARD" , "2013-01-23 06:36:32" , "2013-01-23 06:37:32" , "NORMAL"},
			{"02#2102" , "0206" , "DOWNWARD" , "2013-01-23 06:39:42" , "2013-01-23 06:40:30" , "NORMAL"},
			{"02#2102" , "0205" , "DOWNWARD" , "2013-01-23 06:43:21" , "2013-01-23 06:44:13" , "NORMAL"},
			{"02#2102" , "0204" , "DOWNWARD" , "2013-01-23 06:46:16" , "2013-01-23 06:47:06" , "NORMAL"},
			{"02#2102" , "0203" , "DOWNWARD" , "2013-01-23 06:50:17" , "2013-01-23 06:51:11" , "NORMAL"},
			{"02#2102" , "0202" , "DOWNWARD" , "2013-01-23 06:53:53" , "2013-01-23 06:54:46" , "NORMAL"},
			{"02#2102" , "0201" , "DOWNWARD" , "2013-01-23 06:56:12" , "2013-01-23 06:57:07" , "NORMAL"}
		},
		{
			{"02#2103" , "0211" , "DOWNWARD" , "2013-01-23 07:06:12" , "2013-01-23 07:07:12" , "NORMAL"},
			{"02#2103" , "0210" , "DOWNWARD" , "2013-01-23 07:08:31" , "2013-01-23 07:09:22" , "NORMAL"},
			{"02#2103" , "0209" , "DOWNWARD" , "2013-01-23 07:11:19" , "2013-01-23 07:12:11" , "NORMAL"},
			{"02#2103" , "0208" , "DOWNWARD" , "2013-01-23 07:14:12" , "2013-01-23 07:15:02" , "NORMAL"},
			{"02#2103" , "0207" , "DOWNWARD" , "2013-01-23 07:16:32" , "2013-01-23 07:17:32" , "NORMAL"},
			{"02#2103" , "0206" , "DOWNWARD" , "2013-01-23 07:19:42" , "2013-01-23 07:20:30" , "NORMAL"},
			{"02#2103" , "0205" , "DOWNWARD" , "2013-01-23 07:23:21" , "2013-01-23 07:24:13" , "NORMAL"},
			{"02#2103" , "0204" , "DOWNWARD" , "2013-01-23 07:26:16" , "2013-01-23 07:27:06" , "NORMAL"},
			{"02#2103" , "0203" , "DOWNWARD" , "2013-01-23 07:30:17" , "2013-01-23 07:31:11" , "NORMAL"},
			{"02#2103" , "0202" , "DOWNWARD" , "2013-01-23 07:33:53" , "2013-01-23 07:34:46" , "NORMAL"},
			{"02#2103" , "0201" , "DOWNWARD" , "2013-01-23 07:36:12" , "2013-01-23 07:37:07" , "NORMAL"}
		}
	};

	
	static final int[] station_enter_fivemin_statistics = {100 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 ,0 ,0 , 0 , 0 ,0 ,0 , 0, 0 ,0 ,0}; 

	
	private static List<Map<String , Object>> makeTripPlanList(String[][] plan){

		List<Map<String , Object>> retList = new LinkedList<Map<String , Object>>();
		for(String[] planStrs :  plan){
			String stationID = planStrs[1];
			Date arriveTime = null , depatureTime = null;
			DateFormat dateFormate = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
			try {
				arriveTime = dateFormate.parse(planStrs[3]);
				depatureTime = dateFormate.parse(planStrs[4]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			Map<String , Object> record = new HashMap<String , Object>();
			record.put("Station_ID" , stationID);
			record.put("Arrive_Tm" , arriveTime);
			record.put("Deptr_Tm" , depatureTime);
			
			retList.add(record);
		}
	
		return retList;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		initBasicInfo();
		
		NetworkTrafficSimulatorConfig simuConfig = new NetworkTrafficSimulatorConfig();
				
		for(String[][] planArray : Test_Line1_Train_Plans){
			Direction direction;
			if(planArray[0][2].equals("DOWNWARD")){
				direction = Direction.DOWNWARD;
			}else{
				direction = Direction.UPWARD;
			}
	
			TrainTripPlan trainPlan = new TrainTripPlan(planArray[0][0] , "01" ,direction);
			trainPlan.init(makeTripPlanList(planArray));
			simuConfig.addTrainTripPlan(trainPlan);
		}
		
		for(String[][] planArray : Test_Line2_Train_Plans){
			Direction direction;
			if(planArray[0][2].equals("DOWNWARD")){
				direction = Direction.DOWNWARD;
			}else{
				direction = Direction.UPWARD;
			}
			TrainTripPlan trainPlan = new TrainTripPlan(planArray[0][0] , "02" , direction);
			trainPlan.init(makeTripPlanList(planArray));
			simuConfig.addTrainTripPlan(trainPlan);
		}

		simuConfig.station_enter_statistic_len = 300*1000; /*5 minutes */
		
		Date startTime = null , endTime = null;
		DateFormat dateFormate = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		try {
			startTime = dateFormate.parse("2013-01-23 06:00:00");
			endTime = dateFormate.parse("2013-01-23 07:45:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		simuConfig.simu_start_time = startTime.getTime();
		simuConfig.simu_end_time = endTime.getTime();
		simuConfig.simu_step_len = 1000;

		for(StationModel staModel : Stations_Description){
			if(staModel.is_sub_station == false){
				simuConfig.addEnterStationStatistics(staModel.station_id, station_enter_fivemin_statistics);
			}
		}
				
		ODProportionTable odTable = createRandomODTable(simuConfig.simu_start_time , 300*1000 , 20);
		simuConfig.addODProportionTable(odTable);

		ODPathTable odPathTable = createRandomODPathTable(simuConfig.simu_start_time , 86400*1000 , 1);
		simuConfig.setODPathTable(odPathTable);

		NetworkTrafficSimulator simulator = new NetworkTrafficSimulator(simuConfig);
		simulator.init();	
		simulator.run();
		
	}


	private static void initBasicInfo() {
		
		BasicModelManager infoManager = BasicModelManager.getInstance();
		
		for(StationModel stationInfo : Stations_Description){
			stationInfo.init();
			if(stationInfo.is_transfer_station == false){
				stationInfo.addGate(new GateModel("GateA"));
				stationInfo.addGate(new GateModel("GateB"));
				stationInfo.addGate(new GateModel("GateC"));
				stationInfo.addGate(new GateModel("GateD"));
				stationInfo.addSite(new SiteModel("UPWARD" , Direction.UPWARD));
				stationInfo.addSite(new SiteModel("DOWNWARD" , Direction.DOWNWARD));
			}
			infoManager.registStationModel(stationInfo.station_id, stationInfo);
		}
				
		for(SectionModel sectionInfo : Sections_Description){
			sectionInfo.init();
			infoManager.registSectionModel(sectionInfo.section_id, sectionInfo);
		}
		
		for(TrainModel trainInfo : Line_Train_Description){
			trainInfo.init();
			infoManager.registTrainModel(trainInfo.line_id, trainInfo);
		}

		line_no1_info.init();
		infoManager.registLineModel(line_no1_info.line_id, line_no1_info);

		line_no2_info.init();
		infoManager.registLineModel(line_no2_info.line_id, line_no2_info);

	}
	
	private static ODProportionTable createRandomODTable(long startTime , long unitLen , int unitCount ){
		TestODProportionTable retTable = new TestODProportionTable(startTime , unitLen , unitCount);
		
		for(int idx = 0 ; idx < unitCount ; idx++){
			for(StationModel osm :Stations_Description){
				if(osm.is_sub_station == false){
					int staCount = getStationCount();
					float[] proportion = genRandomProportion(staCount-1);
					int j= 0;
					for(StationModel dsm : Stations_Description){
						if(dsm.is_sub_station == false){
							if(osm != dsm)
								retTable.addODProportion(idx, osm.station_id, dsm.station_id, proportion[j++]);
						}
					}
				}
			}
		}
		return retTable;
	}

	private static int getStationCount() {
		int i = 0;
		for(StationModel sm : Stations_Description){
			if(sm.is_sub_station == false){
				i++;
			}
		}
		return i;
	}

	private static ODPathTable createRandomODPathTable(long startTime , long unitLen , int unitCount){
		TestODPathTable retTable = new TestODPathTable(startTime , unitLen , unitCount);
		for(int idx = 0 ; idx < unitCount ; idx++){
			for(StationModel osm : Stations_Description){
				if(osm.is_sub_station == false){
					for(StationModel dsm : Stations_Description){
						if(	osm != dsm && dsm.is_sub_station == false){						
							List<ODPath> pathList = getODPathListByODPair(osm , dsm);
							if(pathList != null ){
								float[] proportion = genRandomProportion(pathList.size());
								for(int i = 0 ; i < pathList.size() ; i++){
									retTable.addODPath(osm.station_id , dsm.station_id , pathList.get(i));
									retTable.addODPathProportion(startTime+(unitLen*idx), pathList.get(i), proportion[i]);
								}
							}
						}
					}
				}
			}
		}
		return retTable;
	}

	private static List<ODPath> getODPathListByODPair(StationModel osm, StationModel dsm) {
		
		List<ODPath> retList = new LinkedList<ODPath>();
		
		if(osm.is_transfer_station == false && osm.is_sub_station == true){
			osm = BasicModelManager.getInstance().getStationModel(osm.transfer_station_id);
		}
		
		if(dsm.is_transfer_station == false && dsm.is_sub_station == true){
			dsm = BasicModelManager.getInstance().getStationModel(dsm.transfer_station_id);
		}
		
		LineModel line = getSameLine(osm , dsm);
		
		if(line != null){
			ODPathElement[] pathElements = new ODPathElement[1];
			pathElements[0] = new ODPathElement();
			pathElements[0].start_station = getLineStationModel(line , osm);
			pathElements[0].end_station = getLineStationModel(line , dsm);
			pathElements[0].line = line;
			pathElements[0].direction = getPathDirection(line , osm , dsm);
			ODPath path = new ODPath(osm , dsm , pathElements);
			retList.add(path);
			
		}else{
			
			LineModel olm = BasicModelManager.getInstance().getLineModel(osm.line_id);
			LineModel dlm = BasicModelManager.getInstance().getLineModel(dsm.line_id);
			
			StationModel tsm = getTransferStation(olm , osm);
			
			ODPathElement[] pathElements = new ODPathElement[2];
			pathElements[0] = new ODPathElement();
			pathElements[0].start_station = getLineStationModel(olm , osm);
			pathElements[0].end_station = getLineStationModel(olm , tsm);;
			pathElements[0].line = olm;
			pathElements[0].direction = getPathDirection(olm , pathElements[0].start_station , pathElements[0].end_station);

			pathElements[1] = new ODPathElement();
			pathElements[1].start_station = getLineStationModel(dlm , tsm);
			pathElements[1].end_station = getLineStationModel(dlm , dsm);;
			pathElements[1].line = dlm;
			pathElements[1].direction = getPathDirection(dlm , pathElements[1].start_station , pathElements[1].end_station);
			
			ODPath path = new ODPath(osm , dsm , pathElements);
			retList.add(path);			
			
		}
		
		return retList;
	}
	
	private static StationModel getLineStationModel(LineModel lm, StationModel sm) {
		if(sm.is_transfer_station){
			for(String sid : sm.sub_station_ids){
				StationModel subSM = BasicModelManager.getInstance().getStationModel(sid);
				if(subSM.line_id.equals(lm.line_id))
					return subSM;
			}
			return null;
		}else
			return sm;
	}
	

	private static LineModel getSameLine(StationModel osm, StationModel dsm) {
		
		if(osm.is_transfer_station){
			if(dsm.is_transfer_station == false)
				return BasicModelManager.getInstance().getLineModel(dsm.line_id);
		}else{
			if(dsm.is_transfer_station){
				return BasicModelManager.getInstance().getLineModel(osm.line_id);
			}
			else{
				LineModel olm = BasicModelManager.getInstance().getLineModel(osm.line_id);
				LineModel dlm = BasicModelManager.getInstance().getLineModel(dsm.line_id);
				
				if(olm == dlm){
					return olm;
				}
			}
		}
		return null;
	}

	private static StationModel getTransferStation(LineModel lm, StationModel osm) {
		
		if(osm.line_id.equals(lm.line_id) ){	
			for(String sid : lm.stations){
				StationModel sm = BasicModelManager.getInstance().getStationModel(sid);
				if(sm.is_sub_station){
					return BasicModelManager.getInstance().getStationModel(sm.transfer_station_id);
				}
			}
		}
		return null;
	}

	private static Direction getPathDirection(LineModel line,StationModel osm, StationModel dsm) {
		StationModel losm=null , ldsm=null;
		if(osm.is_transfer_station){
			for(String sid : osm.sub_station_ids){
				StationModel sm = BasicModelManager.getInstance().getStationModel(sid);
				if(sm.line_id.equals(line.line_id)){
					losm = sm;
					break;
				}
			}
		}else{
			losm = osm;
		}


		if(dsm.is_transfer_station){
			for(String sid : dsm.sub_station_ids){
				StationModel sm = BasicModelManager.getInstance().getStationModel(sid);
				if(sm.line_id.equals(line.line_id)){
					ldsm = sm;
					break;
				}
			}
		}else{
			ldsm = dsm;
		}

		
		if(losm.station_id.compareTo(ldsm.station_id) < 0 )
			return Direction.UPWARD;
		else
			return Direction.DOWNWARD;
	}

	private static float[] genRandomProportion(int count) {
		
		Random rand = new Random();
		float[] retArray = new float[count];
		float total = 0;
		
		for(int i = 0 ; i < count ; i++){
			retArray[i] = Math.abs(rand.nextFloat())/(float)count;
			total += retArray[i];
		}
		
		for(int i = 0 ; i < count ; i++){
			retArray[i] = retArray[i]/total;
		}
		return retArray;
	}
}
