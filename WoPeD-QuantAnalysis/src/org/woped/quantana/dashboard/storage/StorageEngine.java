package org.woped.quantana.dashboard.storage;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.imageio.ImageIO;

import org.jgraph.JGraph;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.utilities.LoggerManager;
import org.woped.quantana.dashboard.storage.TableInfo;
import org.woped.quantana.dashboard.storage.UIDCreater;
import org.woped.quantana.gui.ActivityPanel;
import org.woped.quantana.gui.QuantitativeSimulationDialog;
import org.woped.quantana.model.ResourceStats;
import org.woped.quantana.model.ServerStatisticsModel;
import org.woped.quantana.resourcealloc.Resource;
import org.woped.quantana.resourcealloc.ResourceAllocation;
import org.woped.quantana.sim.SimParameters;
import org.woped.quantana.sim.SimReportServerStats;
import org.woped.quantana.sim.SimReportStats;
import org.woped.quantana.sim.SimRunStats;
import org.woped.quantana.sim.SimServer;
import org.woped.quantana.sim.SimServerStats;

import com.google.gson.Gson;

//import java.util.zip.GZIPInputStream;
//import java.util.zip.GZIPOutputStream;

public class StorageEngine {
	
  public static enum Table {
	  SIM_ATTRIBUTES	{public String toString() { return "SIM_ATTRIBUTES";}},
	  SIM_VALUES 		{public String toString() { return "SIM_VALUES";}},
	  SIM_RESALLOC 		{public String toString() { return "SIM_RESALLOC";}}
	  
  }
  private Connection connect = null;
  private Statement statement = null;
  private ResultSet resultSet = null;

  
  private ResourceAllocation resAlloc = null;
  private HashMap<String, SimServer> serverList = null;
  
  private SimParameters simParams = null;
  static int currentRun = 0;
  private SimReportStats repStats = new SimReportStats();
  
  
  private String strCurrentSimulationTable = "";

  private final String ERR_NOCONNECTION = "No connection established";
  
  private static StorageEngine uniqInstance;
  
  final Lock dbLock = new ReentrantLock();

  private int clockTick = 0;
  
  private int maxEntries = ConfigurationManager.getConfiguration().getBusinessDashboardMaxValues();
  
  WoPeDDashboardConfiguration wdc = null;
  
  public static synchronized StorageEngine getInstance(){

	  if (uniqInstance == null) {
	      uniqInstance = new StorageEngine();
	    }
	    return uniqInstance;
  }
  
  private  StorageEngine(){

	  //logger.register(this, loggerName);
	  
	  try {
 
		LoggerManager.info(Constants.DASHBOARDSTORE_LOGGER, "StorageEngine: get connection jdbc:derby:data/dashboarddb;create=true");
		
		Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
		
		wdc = new WoPeDDashboardConfiguration();
		
		String strDerbyPath = wdc.getUserdir();
		
		connect = DriverManager.getConnection("jdbc:derby:" + strDerbyPath + "data/dashboarddb;create=true", "", "");

		LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER, "StorageEngine: DB: " + connect.getMetaData().toString());
		
		
		
	} catch (InstantiationException | IllegalAccessException
			| ClassNotFoundException | SQLException e) {
		
		LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER, "StorageEngine: Exception while connect to database: " + e.toString());
		//e.printStackTrace();
	}
      
  

  }
  
  
  
  private String BuildInsertSimulationDataString(int values){
	  String str="";
	  
	  //open with a bracket when more than one entry
	  if(values > 1){
		  str+="(";
	  }
	  
	  for(int i= 0; i<values; i++){
		  str += "(?,?)";
	  
		  //when not last set of values, add a commata
		  if(i<(values-1)){
			  str += ",";
		  }
	  } 
	  //close with a bracket when more than one entry
	  if(values > 1){
		  str+=")";
	  }
	  
	  return str;
  }
  
  /*synchronized public void InsertGeneric(SimulationObject o){
	  ArrayList<SimulationData>objList = new ArrayList<>();
	  objList.add((SimulationData)o);
	  InsertSimulationDataBulk(objList);
  }*/
  
  synchronized private SimulationStorageData toStorageObject(SimRunStats rs){

	  
	  StorageEngine.currentRun++;
	  
	  
	  SimulationStorageData stoDat = new SimulationStorageData();
	  
	  if(stoDat.getProcess() == null)
		  stoDat.setProcess(new SimulationStorageDataProcess());
	  
	  

		
	  
	  /*
	   * Ressources
	   */
	  
	  int countOfRessources = rs.getResStats().size();
	  
	  
	  
  
	  stoDat.setRessources(new SimulationStorageDataRessource[countOfRessources]);

	  //avg-calculation  
	  SimulationStorageDataRessource[] avgCalc = this.generateReportResourceStats(rs);
	  SimulationStorageDataServer[] avgCalcServ = this.generateReportServerStats(rs);
	  
	  int i= 0;

	  
	  for (ResourceStats rr : rs.getResStats().values()){
		  //durchlauf lauf hochzahlen (begann bei 0)
		 
		  
		  stoDat.getRessources()[i] = new SimulationStorageDataRessource();	
		  stoDat.getRessources()[i].setRessourceName(rr.getName());

		  stoDat.getRessources()[i].setIdleTime(UIDCreater.round(rr.getIdleTime()));
		  stoDat.getRessources()[i].setAvgIdleTime(UIDCreater.round(avgCalc[i].getAvgIdleTime() / currentRun));
		  
		  stoDat.getRessources()[i].setUtilizationRatio(UIDCreater.round(rr.getUtilizationRatio()));	
		  stoDat.getRessources()[i].setAvgUtilizationRatio(UIDCreater.round(avgCalc[i].getAvgUtilizationRatio() / currentRun));
		  i++;
	  }
	  
	  
	  /*
	   * Server
	   */
	  int countOfServers = rs.getServStats().size();
	  
	  stoDat.setServers(new SimulationStorageDataServer[countOfServers]);
	  
	  i =  0;
	  
	  for (SimServerStats rr : rs.getServStats().values()){
		  //durchlauf lauf hochzahlen (begann bei 0)
		 
		  
		  stoDat.getServers()[i] = new SimulationStorageDataServer();	
		  stoDat.getServers()[i].setServerName(rr.getName());

		  stoDat.getServers()[i].setAccesses(rr.getAccesses());
		  stoDat.getServers()[i].setAvgAccesses(avgCalcServ[i].getAvgAccesses());
		  
		  
		  stoDat.getServers()[i].setAvgQLength(UIDCreater.round(rr.getAvgQLength()));
		  stoDat.getServers()[i].setAvgResNumber(UIDCreater.round(rr.getAvgResNumber()));
		  
		  stoDat.getServers()[i].setServTime(UIDCreater.round(rr.getAvgServTime()));
		  stoDat.getServers()[i].setAvgServTime( UIDCreater.round(avgCalcServ[i].getAvgServTime() / currentRun));
		  
		  
		  stoDat.getServers()[i].setWaitTime(UIDCreater.round(rr.getAvgWaitTime()));
		  stoDat.getServers()[i].setAvgWaitTime( UIDCreater.round(avgCalcServ[i].getAvgWaitTime() / currentRun));
		  

		  stoDat.getServers()[i].setAvgAccesses( (avgCalcServ[i].getAvgAccesses() / currentRun));
		  
		  stoDat.getServers()[i].setCalls((int)UIDCreater.round(rr.getCalls()) );
		  stoDat.getServers()[i].setAvgCalls(avgCalcServ[i].getAvgCalls() / currentRun);
		  
		  i++;
	  }
	  
	  /*
	   * Process
	   */
	  SimulationStorageDataProcess avgCalcProc = this.generateReportProcessStats(rs);
	  
		  stoDat.getProcess().setFinishedCases(rs.getFinishedCases()); 
		  stoDat.getProcess().setDuration(UIDCreater.round(rs.getDuration()));
		  stoDat.getProcess().setProcCompTime(UIDCreater.round(rs.getProcCompTime()));
		  stoDat.getProcess().setProcServTime(UIDCreater.round(rs.getProcServTime()));
		  stoDat.getProcess().setProcWaitTime(UIDCreater.round(rs.getProcWaitTime()));
		  stoDat.getProcess().setThroughPut(UIDCreater.round(rs.getThroughPut()));

			
		  stoDat.getProcess().setAvgFinishedCases((int)(avgCalcProc.getFinishedCases() / currentRun)); 
		  stoDat.getProcess().setAvgDuration(UIDCreater.round(avgCalcProc.getDuration()/ currentRun));
		  stoDat.getProcess().setAvgProcCompTime(UIDCreater.round(avgCalcProc.getProcCompTime()/ currentRun));
		  stoDat.getProcess().setAvgProcServTime(UIDCreater.round(avgCalcProc.getProcServTime()/ currentRun));
		  stoDat.getProcess().setAvgProcWaitTime(UIDCreater.round(avgCalcProc.getProcWaitTime()/ currentRun));
		  stoDat.getProcess().setAvgThroughPut(UIDCreater.round(avgCalcProc.getThroughPut()/ currentRun));

	
	
	  return stoDat;
  }
  
  synchronized public void add(SimRunStats o){
	  
	  //convert data
	 ArrayList<SimulationStorageEntry> arrSse = new ArrayList<>();
	 
	 
	  
	 ArchiveFilter archiveFilter = new ArchiveFilter();
	  
	 int[] arrRelevantIndizes = archiveFilter.Filter(simParams.getRuns(), maxEntries);
	 
	 ArrayList<Integer> arrlist = new ArrayList<Integer>(maxEntries);
	 
	 for(int i : arrRelevantIndizes){
		 arrlist.add(i);
	 }
	 
	 
	
	 SimulationStorageData  sd = toStorageObject(o);
	  
	  
	 SimulationStorageEntry sse = new SimulationStorageEntry();
	  
	 sse.setTick(++this.clockTick);
	 sse.setData(sd);
		  
	 if(  arrlist.contains(this.clockTick-1) ){  
	
		 //aktuellen wert speichern
		  arrSse.add(sse);
	  
		  InsertSimRunStatsBulk(arrSse);
	 
	 }
	  
	  
  }
  public void InsertSimRunStatsBulk(ArrayList<SimulationStorageEntry> objList){
	  	
	  	String strSerializedObject = "";
	  		  	
	  	Gson gson = new Gson();
	  		  	
	  	PreparedStatement pstmt;
	  	  	
	  	if(connect != null ){
		  	try {

			  	if(this.strCurrentSimulationTable.length() == 0)
			  	{
			  		LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER,"InsertSimRunStatsBulk: no current table / no data will be written");
			  		return;
			  	}

		  		
		  		// simulationswert speichern
		  		  	
				String insertNewSQLObject = "insert into " + strCurrentSimulationTable +  " (Tick,Data) values " + this.BuildInsertSimulationDataString(objList.size());

				dbLock.lock();

				
				pstmt = connect.prepareStatement(insertNewSQLObject);

				int i = 0;
				int iColumn = 0;
				for(i=0 ; i< objList.size();i++){
					
					SimulationStorageEntry sd = objList.get(i);
					strSerializedObject = gson.toJson(sd.getData());
					pstmt.setLong(++iColumn, sd.getTick());
					pstmt.setString(++iColumn, strSerializedObject);
				  	
				}
				
				
				pstmt.executeUpdate();
				
				LoggerManager.debug(Constants.DASHBOARDSTORE_LOGGER,"InsertSimRunStatsBulk: insert finished");
				
				dbLock.unlock();
	  
		    } catch (SQLException e) {
		    	LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER,"InsertSimRunStatsBulk: SQLException: " + e.toString());
			}
	    }
		else{
			
			  LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER,"InsertSimRunStatsBulk: "+ ERR_NOCONNECTION);
		  
		  }
}
  public void setResAlloc(ResourceAllocation ra){
	  
	  if(ra != null ){
		  this.resAlloc = ra;
	  }
	  
  }
  public void setServerList(HashMap<String, SimServer> serverlist){
	  
	  if(serverlist != null ){
		  this.serverList = serverlist;
	  }
	  
  }
  
  
  public void setSimParameters(SimParameters simParams){
	  
	  this.simParams = simParams;
  }
  
  
  
  private  SimulationStorageDataProcess generateReportProcessStats(SimRunStats rs) {
		
		//ArrayList<SimulationStorageDataRessource> sumDataProcess = new ArrayList<SimulationStorageDataRessource>();
		
		
		this.repStats.setFinishedCases(this.repStats.getFinishedCases() + rs.getFinishedCases()); 
		this.repStats.setDuration(this.repStats.getDuration() + rs.getDuration());
		this.repStats.setProcCompTime(this.repStats.getProcCompTime() + rs.getProcCompTime());
		this.repStats.setProcServTime(this.repStats.getProcServTime() + rs.getProcServTime());
		this.repStats.setProcWaitTime(this.repStats.getProcWaitTime() + rs.getProcWaitTime());
		this.repStats.setThroughPut(this.repStats.getThroughPut() + rs.getThroughPut());

		SimulationStorageDataProcess sumDataProcess = new SimulationStorageDataProcess(this.repStats.getFinishedCases(),
																					this.repStats.getDuration() ,
																					this.repStats.getProcServTime(),
																					this.repStats.getProcWaitTime(),
																					this.repStats.getProcCompTime(),
																					this.repStats.getThroughPut());
		
		return sumDataProcess;
	}
  
  
  
  
  
  
  
  
  
  
  
  
  
  

  
	private  SimulationStorageDataRessource[] generateReportResourceStats(SimRunStats rs) {
		
		ArrayList<SimulationStorageDataRessource> sumDataRessource = new ArrayList<SimulationStorageDataRessource>();
		
		
		for (ResourceStats rr : rs.getResStats().values()){
			ResourceStats rrs = null;
			Resource r = resAlloc.getResources().get(rr.getName());
			if (repStats.getResStats().containsKey(r)){
				rrs = repStats.getResStats().get(r);
				rrs.incIdleTime(rr.getIdleTime());
				rrs.incUtilizationRatio(rr.getUtilizationRatio());
			} else {
				rrs = new ResourceStats(rr.getName());
				rrs.setIdleTime(rr.getIdleTime());
				rrs.setUtilizationRatio(rr.getUtilizationRatio());
				
				repStats.getResStats().put(r, rrs);
				
	
			}
			
			sumDataRessource.add(new SimulationStorageDataRessource(rrs.getName(),
					rrs.getIdleTime(),
					rrs.getUtilizationRatio()));
		}
		
		SimulationStorageDataRessource[] ret = new SimulationStorageDataRessource[sumDataRessource.size()];
		for(int i= 0; i< sumDataRessource.size(); i++){
			ret[i]= sumDataRessource.get(i);
		}
		
		return ret;
	}

	private  SimulationStorageDataServer[] generateReportServerStats(SimRunStats rs) {
		
		ArrayList<SimulationStorageDataServer> sumDataServer = new ArrayList<SimulationStorageDataServer>();
		
		
		
		for (SimServerStats ss2 : rs.getServStats().values()){
					
			SimServerStats ss = ss2;
			
			SimReportServerStats rss = null;
			SimServer s = serverList.get(ss.getId());
			if (repStats.getServStats().containsKey(s)){
				 rss = (SimReportServerStats)repStats.getServStats().get(s);
				rss.incAvgZeroDelays(ss.getZeroDelays());
				rss.incAvgCalls(ss.getCalls());
				rss.incAvgAccesses(ss.getAccesses());
				rss.incAvgDepartures(ss.getDepartures());
				rss.incAvgMaxQLength(ss.getMaxQLength());
				rss.incAvgMaxResNumber(ss.getMaxResNumber());
				rss.incAvgNumServedWhenStopped(ss.getNumServedWhenStopped());
				rss.incAvgQLengthWhenStopped(ss.getQLengthWhenStopped());
				rss.incAvgAvgQLength(ss.getAvgQLength());
				rss.incAvgAvgResNumber(ss.getAvgResNumber());
				rss.incAvgAvgWaitTime(ss.getAvgWaitTime());
				rss.incAvgMaxWaitTime(ss.getMaxWaitTime());
				rss.incAvgServTime(ss.getAvgServTime());
			} else {
				 rss = new SimReportServerStats(ss.getName(), ss.getId());
				rss.setAvgZeroDelays(ss.getZeroDelays());
				rss.setAvgCalls(ss.getCalls());
				rss.setAvgAccesses(ss.getAccesses());
				rss.setAvgDepartures(ss.getDepartures());
				rss.setAvgMaxQLength(ss.getMaxQLength());
				rss.setAvgMaxResNumber(ss.getMaxResNumber());
				rss.setAvgNumServedWhenStopped(ss.getNumServedWhenStopped());
				rss.setAvgQLengthWhenStopped(ss.getQLengthWhenStopped());
				rss.setAvgQLength(ss.getAvgQLength());
				rss.setAvgResNumber(ss.getAvgResNumber());
				rss.setAvgWaitTime(ss.getAvgWaitTime());
				rss.setMaxWaitTime(ss.getMaxWaitTime());
				rss.setAvgServTime(ss.getAvgServTime());
				rss.setDistributionLogger(ss.getDistributionLogger());
				repStats.getServStats().put(s, rss);
			}
			
		
			sumDataServer.add(
								new SimulationStorageDataServer(
										rss.getName(),
										rss.getAvgAccesses(),
										rss.getAvgQLength(),
										rss.getAvgResNumber(),
										rss.getAvgServTime(),
										rss.getAvgWaitTime(),
										rss.getAvgCalls(),
										rss.getDepartures(),	
										rss.getMaxQLength(),
										rss.getMaxResNumber(),	
										rss.getMaxWaitTime(),
										rss.getNumServedWhenStopped(),
										rss.getQLengthWhenStopped(),
										rss.getZeroDelays()
										)
								);

			
		}
		
		SimulationStorageDataServer[] ret = new SimulationStorageDataServer[sumDataServer.size()];
		for(int i= 0; i< sumDataServer.size(); i++){
			ret[i]= sumDataServer.get(i);
		}
		
		return ret;
	}
  
  
  public void DeleteAllEntries(String tablename)
  {
	  PreparedStatement statement;
	  if(connect != null)
	  {
	  	try {	      

	  		statement = connect.prepareStatement("DELETE from " + tablename);
	  		statement.executeUpdate();
		} catch (SQLException e) {
			LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER,"DeleteAllEntries: SQLException in DeleteAllEntries: " + e.toString());
		}
	  }
	  else{
		  LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER,"DeleteAllEntries: " + ERR_NOCONNECTION);
	  }
  }
  
  
  
  
  public void CreateTable(Table t)
  {
	  
	  //initialize values for this new simulation run
	  StorageEngine.currentRun = 0;
	  this.clockTick = 0;
	  this.repStats = null;
	  this.repStats = new SimReportStats();
	  
	  
	  
	  
	  String strCreateStatement = "";
	  PreparedStatement statement;
	  
	  String strTableName = t.toString() + "_"+ UIDCreater.CreateUIDFromDateTime();
	  
	  dbLock.lock();
	  
	  if(t == Table.SIM_VALUES){
		  
		  
		  strCreateStatement ="CREATE TABLE " + strTableName +
							  " (" +
							  " ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),  " +
							  " Tick INTEGER,"+
							  " Data CLOB"+
							  ")"; 
	  }
	  
	  
	  if(connect != null)
	  {
	  	try {	      
	  		
	  		statement = connect.prepareStatement(strCreateStatement);
	  		
	  		statement.executeUpdate();
	  			 
	  		this.strCurrentSimulationTable = strTableName;
	  		
	  		LoggerManager.debug(Constants.DASHBOARDSTORE_LOGGER, "CreateTable: Table " + strTableName + " created" );
	  		
		} catch (SQLException e) {
			//e.printStackTrace();
			LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER,"CreateTable: SQLException : " + e.toString());
		}
	  }
	  else{
		  LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER,"CreateTable: " + ERR_NOCONNECTION);
	  }

	  
	  
	  
	  	// informationen über simulation in separater DB speichern
	  	
		  
	  	strCreateStatement ="CREATE TABLE " + Table.SIM_ATTRIBUTES.toString() +
								  "(" +
								  " ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),  " +
								  " Tablename varchar(255)," +
								  " Description varchar(255)," +
								  " Data CLOB,"+
								  " ResAlloc CLOB,"+
								  " Image BLOB," +
								  " UIConfig varchar(255)" +
								  ")"; 
		  
		  
		  if(connect != null)
		  {
		  	try {	      
		  		statement = connect.prepareStatement(strCreateStatement);
		  		statement.executeUpdate();
		  		this.strCurrentSimulationTable = strTableName;
			
		  		LoggerManager.debug(Constants.DASHBOARDSTORE_LOGGER, "CreateTable: finished with 'create Table " + Table.SIM_ATTRIBUTES.toString() + "created with columns ID, Tablename, Description, Data, Resalloc'" );
		  		
		  	} catch (SQLException e) {
				LoggerManager.info(Constants.DASHBOARDSTORE_LOGGER,"CreateTable: table already exists: " +Table.SIM_ATTRIBUTES.toString());
				//e.printStackTrace();
			}finally{
				dbLock.unlock();
			}
		  }
		  else{
			  LoggerManager.info(Constants.DASHBOARDSTORE_LOGGER,"CreateTable " + ERR_NOCONNECTION);
		  }
			  
		  
		  
		  
		  
		  InsertSimRunParams();
  }
  

  public SimulationStorageEntry GetSimulationDataSingle(long tick){
	  SimulationStorageEntry[] ret =  GetSimulationData(this.strCurrentSimulationTable, tick, true, false);
	  return ret[0];
	  
  }
  
  
  public SimulationStorageEntry[] GetSimulationData(long tick, boolean single){
	  return GetSimulationData(this.strCurrentSimulationTable, tick, single, false);
  }
  
 /* public SimulationStorageEntry GetLastSimulationData(){
	  //SimulationStorageEntry[] ret =  GetSimulationData(this.strCurrentSimulationTable, 0);
	  return ret[0];
  }
  */
  
  public void InsertSimRunParams(){
	  	
	  	String strSerializedObject = "";
	  		  	
	  	Gson gson = new Gson();
	  		  	
	  	PreparedStatement pstmt;
	  	  	
	  	if(connect != null ){
		  	try {

			  	if(this.strCurrentSimulationTable.length() == 0)
			  	{
			  		LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER, "InsertSimRunParams : no current table" );
			  		
			  		return;
			  	}

		  		
		  		// simulationswert speichern
		  		  	
				String insertNewSQLObject = "insert into " + Table.SIM_ATTRIBUTES.toString() +  
											" (Tablename,Description, Data) values (?,?,?)";

				dbLock.lock();

				LoggerManager.debug (Constants.DASHBOARDSTORE_LOGGER, "InsertSimRunParams : prepare statement: insert into " + Table.SIM_ATTRIBUTES.toString() + " (Tablename,Description, Data) values (<data,<datat>,<data>)" );
		  		
				pstmt = connect.prepareStatement(insertNewSQLObject);
				
			
				
				strSerializedObject = gson.toJson(this.simParams);
				
				pstmt.setString(1, this.strCurrentSimulationTable);
				pstmt.setString(2, UIDCreater.CreateComment());
				pstmt.setString(3, strSerializedObject);
				
				//LoggerManager.debug (Constants.DASHBOARDSTORE_LOGGER, "InsertSimRunParams: Strings set");
		
				pstmt.executeUpdate();

				LoggerManager.debug (Constants.DASHBOARDSTORE_LOGGER, "InsertSimRunParams: update finished");
				
				dbLock.unlock();
	  
		    } catch (SQLException e) {
				e.printStackTrace();
			}
	    }
		  else{
			  LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER,"InsertSimResAlloc "+ ERR_NOCONNECTION);
		  }
}
  
  
  public void InsertSimResAlloc(ArrayList<SimulationRessourceAllocData>  resallocdata){
	  	
	  	String strSerializedObject = "";
	  		  	
	  	Gson gson = new Gson();
	  		  	
	  	PreparedStatement pstmt;
	  	  	
	  	if(connect != null ){
		  	try {

			  	if(this.strCurrentSimulationTable.length() == 0)
			  	{
			  		LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER,"InsertSimResAlloc: no current table to write into");
			  		return;
			  	}

		  		
		  		// simulationswert speichern
		  		  	
				String insertNewSQLObject = "update " + Table.SIM_ATTRIBUTES.toString() +  
											" SET ResAlloc=? where Tablename = ?";
				
				dbLock.lock();

				LoggerManager.debug(Constants.DASHBOARDSTORE_LOGGER,"InsertSimResAlloc: prepare statement: update " + Table.SIM_ATTRIBUTES.toString() + " SET ResAlloc= <json-content> where Tablename = "+ strCurrentSimulationTable +";");
				
				pstmt = connect.prepareStatement(insertNewSQLObject);
				
				
				strSerializedObject = gson.toJson(resallocdata);
				
				
				pstmt.setString(1, strSerializedObject);
				pstmt.setString(2, this.strCurrentSimulationTable);
				
				LoggerManager.debug(Constants.DASHBOARDSTORE_LOGGER,"InsertSimResAlloc: strings are set");
				
				pstmt.executeUpdate();
				
				LoggerManager.debug (Constants.DASHBOARDSTORE_LOGGER, "InsertSimResAlloc: update finished");
				
				dbLock.unlock();
	  
		    } catch (SQLException e) {
				//e.printStackTrace();
		    	LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER,"InsertSimResAlloc: SQLException in InsertSimResAlloc: " + e.toString());
			}
	    }
		  else{
			  LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER,"InsertSimResAlloc "+ ERR_NOCONNECTION);
		  }
			  
}

  public void InsertImage(){
	  		 

		//String strName = this.editor.getClass().toString();
		JGraph graph = owner.getEditorGraph();
	    byte[] imgByte = null;
	    PreparedStatement pstmt = null;
		ObjectOutputStream oos = null;
	    
		graph.clearSelection();
		Object[] cells = graph.getRoots();
		BufferedImage image = null;
		
		if (cells.length > 0) {
          Rectangle2D rectangle = graph.getCellBounds(cells);

          graph.setGridVisible(false);
          graph.toScreen(rectangle);

          // Create a Buffered Image
          Dimension dimension = rectangle.getBounds().getSize();
          image = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_RGB);
          Graphics2D graphics = image.createGraphics();
          graphics.translate(-rectangle.getX(), -rectangle.getY());
          graph.paint(graphics);
		}


		try {
			//Graphics2D g2 = image.createGraphics();
		
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			if (ImageIO.write(image, "PNG", baos)) {
				imgByte = baos.toByteArray();
			}
				
		} catch (IOException ex) {
			
		}	  	
	  	  	
	  	if(imgByte != null && connect != null ){
		  	try {

			  	if(this.strCurrentSimulationTable.length() == 0)
			  	{
			  		LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER,"InsertImage: no current table to write into");
			  		return;
			  	}

		  		
		  		// simulationswert speichern
		  		  	
				String insertNewSQLObject = "update " + Table.SIM_ATTRIBUTES.toString() +  
											" SET Image=? where Tablename = ?";
				
				dbLock.lock();

				LoggerManager.debug(Constants.DASHBOARDSTORE_LOGGER,"InsertImage: prepare statement: update " + Table.SIM_ATTRIBUTES.toString() + " SET ResAlloc= <json-content> where Tablename = "+ strCurrentSimulationTable +";");
				
				pstmt = connect.prepareStatement(insertNewSQLObject);
				
				Blob blob = connect.createBlob();


		
				try {
		         	
					oos = new ObjectOutputStream(blob.setBinaryStream(1));
					oos.writeObject(imgByte);
				    oos.close();
				    
				} catch (IOException e) {
					LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER,"InsertImage IOException : " + e.getMessage());	
				}
		        finally {
		           
		        }
			
			    
				
			    pstmt.setBlob(1, blob);
			    pstmt.setString(2, this.strCurrentSimulationTable);
			    
				LoggerManager.debug(Constants.DASHBOARDSTORE_LOGGER,"InsertImage: image saved");
				
				pstmt.executeUpdate();
				
				LoggerManager.debug (Constants.DASHBOARDSTORE_LOGGER, "InsertImage: update finished");
				
				dbLock.unlock();
	  
		    } catch (SQLException e) {
				//e.printStackTrace();
		    	LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER,"InsertImage: SQLException in InsertSimResAlloc: " + e.toString());
			}
	    }
		  else{
			  LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER,"InsertImage: No Image or "+ ERR_NOCONNECTION);
		  }
			  
}

  
 public SimulationRessourceAllocData[]  GetSimResAlloc(String tablename, String ressource, String server){
	
	 
	 PreparedStatement statement;
	  
	  
	  
	  Gson gson = new Gson();
	  SimulationRessourceAllocData[] simresalloc = null;
	  
	  
	  tablename = GetValidTablename(tablename);
	  
	  
	  if(ressource.equals("") || ressource == null || ressource.equals("null") || ressource.equals("undefined")){
		  ressource = null;	  
	  }
	  if(server.equals("") || server == null || server.equals("null") || server.equals("undefined")){
		  server = null;	  
	  }
	  
	  if(connect != null && (tablename != ""))
	  {
	  	try {	 
	  		
	  		LoggerManager.debug(Constants.DASHBOARDSTORE_LOGGER,"GetSimResAlloc: SELECT ResAlloc from " + Table.SIM_ATTRIBUTES +" where Tablename = "+ tablename);
			
	  		statement = connect.prepareStatement("SELECT ResAlloc from " + Table.SIM_ATTRIBUTES + " where Tablename = ?");
	  		statement.setString(1, tablename); // set input parameter
	  		
	  		LoggerManager.debug(Constants.DASHBOARDSTORE_LOGGER,"GetSimResAlloc: strings are set");
	
	  		dbLock.lock();
	  		
	  		resultSet = statement.executeQuery();  
	      
	  		LoggerManager.debug(Constants.DASHBOARDSTORE_LOGGER,"GetSimResAlloc: select finished");
	      
	  		dbLock.unlock();
	      
	      
	      while (resultSet.next()) {
	    
	    	  String strData = resultSet.getString("ResAlloc");
	    			  
	    	  simresalloc = gson.fromJson(strData, SimulationRessourceAllocData[].class);
	    	     
	      }
	      
	      ArrayList<SimulationRessourceAllocData> resallecRet = new ArrayList<SimulationRessourceAllocData>();
	      for (SimulationRessourceAllocData s : simresalloc){
	    	  
	    	  //nur bestimmte ressource
	    	  
	    	  if(ressource != null && server == null){
		    	  if (s.getRessource().equals(ressource)){
		    		  resallecRet.add(s);
		    	  } 
	    	  }else if(ressource == null && server != null){
	    		  if (s.getServer().equals(server)){
		    		  resallecRet.add(s);
		    	  }
	    	  }else if(ressource != null && server != null){
	    		  if (s.getServer().equals(server) && s.getRessource().equals(ressource)){
		    		  resallecRet.add(s);
		    	  }
	    	  }else 
	    		  resallecRet.add(s);
	    	  
	      }
	      simresalloc = new SimulationRessourceAllocData[resallecRet.size()];
	      for(int i= 0; i< resallecRet.size();i++){
	    	  simresalloc[i] = resallecRet.get(i);
	      }
		} catch (SQLException e) {
			LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER , "GetSimResAlloc: SQLException: " + e.toString());
		}
	  	
	  	return simresalloc;
	  }
	  else{
		  return null;
	  }
	 
	 
 }
 
 public byte[]  GetImage(String tablename){
	
	 PreparedStatement statement;
	    
	  
	  
	 tablename = GetValidTablename(tablename);
	 byte[] retVal = null;
	  
	  if(connect != null && (tablename != ""))
	  {
	  	try {	 
	  		
	  		LoggerManager.debug(Constants.DASHBOARDSTORE_LOGGER,"GetImage: SELECT ResAlloc from " + Table.SIM_ATTRIBUTES +" where Tablename = "+ tablename);
			
	  		statement = connect.prepareStatement("SELECT Image from " + Table.SIM_ATTRIBUTES + " where Tablename = ?");
	  		statement.setString(1, tablename); // set input parameter
	  		
	  		LoggerManager.debug(Constants.DASHBOARDSTORE_LOGGER,"GetImage: strings are set");
	
	  		dbLock.lock();
	  		
	  		resultSet = statement.executeQuery();  
	      
	  		LoggerManager.debug(Constants.DASHBOARDSTORE_LOGGER,"GetImage: select finished");
	      
	  		dbLock.unlock();
	      
	  		while (resultSet.next()) {
	    
	    	  Blob photo = resultSet.getBlob(1);
	          ObjectInputStream ois = null;
	          try {
	        	  ois = new ObjectInputStream(photo.getBinaryStream());
	        	  retVal = (byte[]) ois.readObject();
	          } catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	          }
	  		}
	
		} catch (SQLException e) {
			LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER , "GetImage: SQLException: " + e.toString());
		}
	  	
	  	return retVal;
	  }
	  else{
		  return null;
	  }
	 
	 
 }
 
 

 public SaveConfig GetUIConfig(String tablename){
	
	 PreparedStatement statement;
	 String strUIConfig = "";
	 SaveConfig uiConfig = null;
	 Gson gson = new Gson();
	 
	 
	 BufferedReader reader = null;
	 String strDerbyPath = wdc.getUserdir();
	 String strConfigfile =  strDerbyPath+  "uiconfig.conf";
	 try	{
		 reader = new BufferedReader( new FileReader(strConfigfile));
		 strUIConfig = reader.readLine();
		 
		 uiConfig = gson.fromJson(strUIConfig, SaveConfig.class);
	
	 }catch ( IOException e)
	 {
	 }
	 finally
	 {
		    try
		    {
		        if ( reader != null)
		        reader.close( );
		    }
		    catch ( IOException e)
		    {
		    }
	 }
	
	 return uiConfig;
	/*	
	 tablename = GetValidTablename(tablename);

	 
	 
	  if(connect != null && (tablename != ""))
	  {
	  	try {	 
	  		
	  		LoggerManager.debug(Constants.DASHBOARDSTORE_LOGGER,"GetUIConfig: SELECT UIConfig from " + Table.SIM_ATTRIBUTES +" where Tablename = "+ tablename);
			
	  		statement = connect.prepareStatement("SELECT UIConfig from " + Table.SIM_ATTRIBUTES + " where Tablename = ?");
	  		statement.setString(1, tablename); // set input parameter
	  		
	  		LoggerManager.debug(Constants.DASHBOARDSTORE_LOGGER,"GetUIConfig: strings are set");
	
	  		dbLock.lock();
	  		
	  		resultSet = statement.executeQuery();  
	      
	  		LoggerManager.debug(Constants.DASHBOARDSTORE_LOGGER,"GetUIConfig: select finished");
	      
	  		dbLock.unlock();
	      
	  			
	  		while (resultSet.next()) {
	  			try{
	  				strUIConfig = resultSet.getString(1);
	  				
	  				uiConfig = gson.fromJson(strUIConfig, SaveConfig.class);
	  	          
	  			}catch(SQLException e){
	  				LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER,"GetUIConfig: config not available");
	  				
	  			}
	  		}
	
		} catch (SQLException e) {
			LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER , "UIConfig: SQLException: " + e.toString());
		}
	  	
	  	return uiConfig;
	  }
	  else{
		  return null;
	  }
	  */
 }
 private HashMap<Integer,Integer> FillHashMapID2Tick(){
	 
	
	 
	 HashMap<Integer,Integer> hmTick2ID = new HashMap<Integer, Integer>();
	 
	 ArchiveFilter archiveFilter = new ArchiveFilter();
	  
	 int[] arrRelevantIndizes = archiveFilter.Filter(simParams.getRuns(), maxEntries);
	 
	 ArrayList<Integer> arrlist = new ArrayList<Integer>(maxEntries);
	 
	 for(int i : arrRelevantIndizes){
		 arrlist.add(i);
	 }
	 
	 for(int i=0; i< maxEntries; i++){
		 
		 hmTick2ID.put(i, arrlist.get(i)+1);
	 }
 
	 
	 return hmTick2ID;
 }
  
 private HashMap<Integer,Integer> FillHashMapTick2ID(){
	 
	 
	 
	 HashMap<Integer,Integer> hmTick2ID = new HashMap<Integer, Integer>();
	 
	 ArchiveFilter archiveFilter = new ArchiveFilter();
	  
	 int[] arrRelevantIndizes = archiveFilter.Filter(simParams.getRuns(), maxEntries);
	 
	 ArrayList<Integer> arrlist = new ArrayList<Integer>(maxEntries);
	 
	 
	 for(int i : arrRelevantIndizes){
		 arrlist.add(i);
	 }
	 
	 for(int i=0 ; i< maxEntries; i++){
		 //arrlist.add(i);
		 hmTick2ID.put(arrlist.get(i)+1,i);
	 }
 
	 
	 return hmTick2ID;
 }
 
 public int GetNextTick(int tick){
	 
	 int nextTick = tick;
	 
	 if( maxEntries > simParams.getRuns() ){
		 maxEntries = simParams.getRuns() ;
	 }
	 
	 HashMap<Integer,Integer> hmTick2ID  = FillHashMapTick2ID();
	 HashMap<Integer,Integer> hmID2Tick  = FillHashMapID2Tick();
	 
	 if( !hmTick2ID.containsKey((int)tick)){
		
	 
		 while(tick< simParams.getRuns()){
			 tick++;
			 
			 if(hmTick2ID.containsKey((int)tick)){
				 int index = hmTick2ID.get((int)tick);
				 if(index < (maxEntries)){
					 nextTick = hmID2Tick.get((int)index);
				 }
				 break;
			 }
		 }
	 
	 }
	
	 
	 return nextTick;
	 
 }
 
  public SimulationStorageEntry[] GetSimulationData(String tablename , long tick,  Boolean singlevalue, Boolean bounding){
	  
	  PreparedStatement statement;
	  
	  SimulationStorageEntry[] simData = null;
	  Gson gson = new Gson();
	  
	  tablename = GetValidTablename(tablename);
	  
	  String strQuery = "";
	  
	  if(connect != null && (tablename != ""))
	  {
	  	try {	      
	  		if (singlevalue == true){
	  			//statement = connect.prepareStatement("SELECT ID,Tick,Data from " + tablename);
	  			if(tick != -1){
	  				strQuery = "SELECT ID,Tick,Data from " + tablename + " where ID = ?";
	  				statement = connect.prepareStatement(strQuery);
	  				statement.setLong(1, tick); // set input parameter	
	  			}else{
	  				strQuery = "SELECT ID,Tick,Data from " + tablename + " order by ID desc FETCH FIRST 1 ROWS ONLY";
	  				statement = connect.prepareStatement(strQuery);
	  			}
	  		}else{
	  			if( bounding == true ){
	  				tick = GetNextTick((int)tick);
	  			}
	  			
	  			strQuery = "SELECT ID,Tick,Data from " + tablename + " where Tick <= ?";
	  			statement = connect.prepareStatement(strQuery );
	  			statement.setLong(1, tick); // set input parameter	
	  			
	  			
	  			//statement.setLong(2, id); // set input parameter	
	  		}
	  		LoggerManager.debug(Constants.DASHBOARDSTORE_LOGGER,"GetSimulationData: statement prepared (" +strQuery+")");
	  		
	      resultSet = statement.executeQuery();

	      LoggerManager.debug(Constants.DASHBOARDSTORE_LOGGER,"GetSimulationData: select finished");
	      
	      //groesse ermitteln
	      int size = 0;
	     	
	      while (resultSet.next())
	      {
	    	  size++;
	      }
	      
	      LoggerManager.debug(Constants.DASHBOARDSTORE_LOGGER,"GetSimulationData: " + size + " values will be processed");
		     
	      simData = new SimulationStorageEntry[size];
	      
	     
	      resultSet = statement.executeQuery();
	      
	      int iSim=0;	  
	      //resultSet.first();
	      
	      
	      
	      while (resultSet.next()) {
	    	  if(iSim < size){
		    	  simData[iSim] = new SimulationStorageEntry();
		    	  simData[iSim].setID (resultSet.getInt("ID"));
		    	  simData[iSim].setTick ( resultSet.getLong("Tick"));
		    	  String strData = resultSet.getString("Data");
		    			  
		    	  SimulationStorageData dataRes = gson.fromJson(strData, SimulationStorageData.class);
		    	  simData[iSim].setData(dataRes);
		    	  
		    	  iSim++;	  
	    	  }
	      }
		} catch (SQLException e) {
			LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER , "GetSimulationData: SQLException in GetSimulationData: " + e.toString());
		}
	  	
	  	return simData;
	  }
	  else{
		  return null;
	  }
  }
  
  
private QuantitativeSimulationDialog owner;

public void setOwner(QuantitativeSimulationDialog d){
	owner = d;
}
  

public void storeAllocationMatrix(){
	
	ServerStatisticsModel ssm = new ServerStatisticsModel(this.owner.getTaskAndResource(),
			this.owner.getSimulator().getActPanelList());
	ssm.getSortedMatrix();
	
	ArrayList<ArrayList<ArrayList<ActivityPanel>>> matrix = ssm.getSortedMatrix();
	int numTasks = ssm.getNumTasks();
	
	ArrayList<SimulationRessourceAllocData> simresallocdata = new ArrayList<SimulationRessourceAllocData>();
	
	
	for (int i = 0; i < numTasks; i++) {
		int n = matrix.get(i).size();

		if (n > 0) {
			for (int j = 0; j < n; j++) {
				ArrayList<ActivityPanel> list = matrix.get(i).get(j);
				
				//y = count++ * ROW_HEIGHT; //+ MARGIN_TOP;
				for (ActivityPanel ap : list) {
					String server = ap.getServer();
					
					String ressource =  ap.getResource();
					
					double TimeStart = UIDCreater.round(ap.getTimeStart());
					double TimeStop = UIDCreater.round(ap.getTimeStop());
					
					
					
					simresallocdata.add(new SimulationRessourceAllocData(ressource, server,TimeStart,TimeStop));
					
				}
			}
		} else {
			//y = count++ * ROW_HEIGHT + MARGIN_TOP;
		}
	}

	InsertSimResAlloc(simresallocdata);

	
}

/*
private String compressJSON(String storage){
	
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	GZIPOutputStream zos;
	try {
		zos = new GZIPOutputStream(baos);
		zos.write(storage.getBytes());
		zos.finish();
		zos.flush();

	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	byte[] udpBuffer = baos.toByteArray();
	return udpBuffer.toString();

}
  
private String decompressJSON(String str){
	
	
	byte[] udpBuffer = baos.toByteArray();
	
	
	String readed;
	ByteArrayInputStream in = new ByteArrayInputStream(udpBuffer);
    GZIPInputStream gzis;
	try {
		gzis = new GZIPInputStream(in);
		 InputStreamReader reader = new InputStreamReader(gzis);
		 BufferedReader pr = new BufferedReader(reader);
		 
		 while ((readed = pr.readLine()) != null) {
			 return readed;
		 }
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	return null;
}
*/


private String GetValidTablename(String tablename){
	

	if(tablename == null || tablename.equals("") ||  tablename.equals("null") || tablename.equals("undefined")){
		  tablename = this.strCurrentSimulationTable;	 

		  if(this.strCurrentSimulationTable.equals("")){
			  TableInfo[] ti = GetTables();
			  if(ti.length>0) tablename = ti[0].getTABLENAME();
		  }
	 }
	
	return tablename;
}
public SimParameters GetSimulationStatisticData(String tablename){
	  
	  PreparedStatement statement;
	  
	 
	  
	  Gson gson = new Gson();
	  SimParameters simparams = null;
	  
	  tablename = GetValidTablename(tablename);
	 	  
	  if(connect != null && (tablename != ""))
	  {
	  	try {	      
	  		statement = connect.prepareStatement("SELECT Tablename,Description, Data from " + Table.SIM_ATTRIBUTES + " where Tablename = ?");
	  		statement.setString(1, tablename); // set input parameter	
	  		
	  		LoggerManager.debug(Constants.DASHBOARDSTORE_LOGGER,"GetSimulationStatisticData: Strings are set");
	  		
	  		dbLock.lock();
		    resultSet = statement.executeQuery();  
		    
		    LoggerManager.debug(Constants.DASHBOARDSTORE_LOGGER,"GetSimulationStatisticData: Select finished");
		    dbLock.unlock();
	      
	      
		    while (resultSet.next()) {
	    
		    	String strData = resultSet.getString("Data");
	    			  
		    	simparams = gson.fromJson(strData, SimParameters.class);
	      }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER, "GetSimulationStatisticData: SQLException  while  GetSimulationStatisticData: " + e.toString());
			  
		}  	
	  	return simparams;
	  }
	  else{
		  return null;
	  }
  }


  public void DeleteTable(String tablename)
  {
	  
	  LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER, "DeleteTable: delete table " +tablename);
	  
	  if(this.strCurrentSimulationTable.equals(tablename)){
		  strCurrentSimulationTable = "";
	  }
	  
	  // delete table with simulation values
	  PreparedStatement statement;
	  if(connect != null)
	  {
	  	try {	      
	  		
	  		statement = connect.prepareStatement("DROP TABLE " + tablename);
	  		
	  		statement.executeUpdate();
	  		
	  		LoggerManager.debug(Constants.DASHBOARDSTORE_LOGGER,"DeleteTable: Delete of table " + tablename + " complete");
	  		
		} catch (SQLException e) {
			
			LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER, "DeleteTable: SQLException in DeleteTable: " + e.toString());
		}
	  }
	  else{
		  LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER, "DeleteTable: " + ERR_NOCONNECTION);
	  }
		  
	  
	  
	  //delete statisics in ATTRIBUTES
	  
	  
	  if(connect != null)
	  {
	  	try {	 
	  		String strStatement = "DELETE FROM " + Table.SIM_ATTRIBUTES.toString() + 
					" WHERE Tablename = ?";
	  		statement = connect.prepareStatement(strStatement);
	  		
	  		statement.setString(1, tablename);
	  		
	  		statement.executeUpdate();
	  		
	  		LoggerManager.debug(Constants.DASHBOARDSTORE_LOGGER,"DeleteTable: Delete of entry complete (DELETE FROM " + Table.SIM_ATTRIBUTES.toString() + " WHERE Tablename =" +tablename);
	  		
		} catch (SQLException e) {
			LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER, "DeleteTable: tabelleneintrag nicht vorhanden (" +tablename + "): " +e.toString());
			//e.printStackTrace();
		}
	  }
	  else
		  LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER,"DeleteTable: " +ERR_NOCONNECTION);
	  
  }
  
 
 
  public TableInfo[] GetTables(){
	   
	  PreparedStatement statement;
	  
	  ArrayList<TableInfo> retTableInfo = new ArrayList<>();
	  
	  TableInfo[] retTables = null;
	  
	  if(connect != null)
	  {
	  	try {	      
	      //statement = connect.prepareStatement("SELECT TABLENAME,TABLETYPE,TABLEID  from SYS.SYSTABLES WHERE TABLETYPE like 'T'");
	      
	      statement = connect.prepareStatement("SELECT Tablename, Description from " +Table.SIM_ATTRIBUTES.toString() +" ORDER BY Tablename DESC");
	      
	      dbLock.lock();
	      
	      resultSet = statement.executeQuery();
	      
	      LoggerManager.debug(Constants.DASHBOARDSTORE_LOGGER,"GetUserTables finished (SELECT Tablename from " +Table.SIM_ATTRIBUTES.toString()  + " ORDER BY Tablename DESC)");
	  		
	      
	      dbLock.unlock();
	      while (resultSet.next()) {
	    	  
	    	
	        //String tablename = resultSet.getString("TABLENAME");
		    String tablename = resultSet.getString("Tablename");
	        
	        //String tabletype = resultSet.getString("TABLETYPE");
		    String tabletype  = "T";
		    
	        //String tableID = resultSet.getString("TABLEID");
		    String tableID = "1234";
		    
		    //String tablename = resultSet.getString("TABLENAME");
		    String description = resultSet.getString("Description");
	        
		    
	        TableInfo ti = new TableInfo(tablename,tabletype,tableID,description);
	        retTableInfo.add(ti);
	        
	      }
		} catch (SQLException e) {
			LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER, "GetUserTables: SQLException beim Lesen der Usertables: " +e.toString());
		}
	  	
	  	
	  	retTables = new TableInfo[retTableInfo.size()];
	  	
	  	for(int i = 0; i< retTableInfo.size(); i++){
	  		retTables[i] = retTableInfo.get(i);
	  	}
	  	
	  	return retTables;
	  }
	  else
		  return null;
	  
  }
  
  public void InsertDescription(String tablename, String description){
	  	
	  	//Gson gson = new Gson();
	  		  	
	  	PreparedStatement pstmt;
	  	
	  	if(description.length()>255){
	  		description = description.substring(0, 254);
	  	}
	  	
	  	
	  	if(connect != null ){
		  	try {

			  
		  		
		  		// simulationswert speichern
		  		  	
				String insertNewSQLObject = "update " + Table.SIM_ATTRIBUTES.toString() +  
											" SET Description=? where Tablename = ?";
				
				dbLock.lock();

				LoggerManager.debug(Constants.DASHBOARDSTORE_LOGGER,"Insert description: prepare statement: update " + Table.SIM_ATTRIBUTES.toString() + " SET description= <content> where Tablename = "+ strCurrentSimulationTable +";");
				
				pstmt = connect.prepareStatement(insertNewSQLObject);
				
			
			    pstmt.setString(1, description);
			    pstmt.setString(2, tablename);
			    
				LoggerManager.debug(Constants.DASHBOARDSTORE_LOGGER,"Insert description: description saved");
				
				pstmt.executeUpdate();
				
				LoggerManager.debug (Constants.DASHBOARDSTORE_LOGGER, "Insert description: update finished");
				
				dbLock.unlock();
	  
			
		    } catch (SQLException e) {
				//e.printStackTrace();
		    	LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER,"InsertImage: SQLException in InsertSimResAlloc: " + e.toString());
			}
	    }
		  else{
			  LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER,"InsertImage "+ ERR_NOCONNECTION);
	}
			  
}
  
  //Change String UIconfig to SaveConfig Object
  public void InsertUIconfig(String tablename, SaveConfig UIconfig){
	  	
	  
	  
	  	Gson gson = new Gson();
	  	String strSerializedObject = "";
	  	
	  	strSerializedObject = gson.toJson(UIconfig);
	  		
		String strDerbyPath = wdc.getUserdir();
		String strConfigfile =  strDerbyPath+  "data/uiconfig.conf";
		BufferedWriter writer = null;
		try	{
		    writer = new BufferedWriter( new FileWriter(strConfigfile));
		    writer.write( strSerializedObject);

		}catch ( IOException e)
		{
		}
		finally
		{
		    try
		    {
		        if ( writer != null)
		        writer.close( );
		    }
		    catch ( IOException e)
		    {
		    }
		}
			  
}
  
 
  public void CloseConnection() {
    try {
      if (resultSet != null) {
        resultSet.close();
      }
      if (statement != null) {
        statement.close();
      }
      if (connect != null) {
        connect.close();
      }
    } catch (Exception e) {

    }
  }
} 
