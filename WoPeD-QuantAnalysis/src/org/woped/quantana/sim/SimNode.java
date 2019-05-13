package org.woped.quantana.sim;

import java.util.ArrayList;

/**
 * @version 1.0
 * @created 29-Mrz-2009 12:55:55
 */
public class SimNode {

	private ArrayList<SimArc> arcIn = new ArrayList<SimArc>();	
	private ArrayList<SimArc> arcOut = new ArrayList<SimArc>();
	private String group = "";
	private String role = "";
	private String id;
	private String name;
	private byte type = NT_UNKNOWN; 
	private boolean is_and_join = false;
	private boolean is_and_split = false;
	private boolean has_resource = false;
	public static byte NT_AND_JOIN = 5;
	public static byte NT_AND_SPLIT = 4;
	public static byte NT_PLACE = 1;
	public static byte NT_SUBPROCESS = 3;
	public static byte NT_TRANSITION = 2;
	public static byte NT_UNKNOWN = 0;	
	private double time = 0.0;	
	private int timeunit = 0;
	private double unitfactor = 1.0;
	private boolean joinReached = false;
	private double numOfRuns = 0.0;
	private double tempRuns = 0.0;
	private int iteration = 0;	

	public SimNode(){

	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 */
	public SimNode(String id, String name){
		this.id = id;
		this.name = name;		
	}

	public void finalize() throws Throwable {

	}

	public ArrayList<SimArc> getarcOut(){
		return arcOut;
	}

	public ArrayList<SimArc> getarcIn(){
		return arcIn;
	}

	public String getgroup(){
		return group;
	}

	public String getid(){
		return id;
	}

	public String getname(){
		return name;
	}

	public String getrole(){
		return role;
	}

	public double gettime(){
		return time;
	}

	public int gettimeunit(){
		return timeunit;
	}
	
	public boolean isAndJoin(){
		return is_and_join;
	}
	
	public boolean isTransition(){
		return (!((type==NT_PLACE)||(type==NT_UNKNOWN)));		
	}
	
	public boolean isAndSplit(){
		return is_and_split;
	}	
	
	public boolean hasResource(){
		return has_resource;
	}
	
	/**
	 * 
	 * @param newVal
	 */
	public void setgroup(String newVal){
		group = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setid(String newVal){
		id = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setname(String newVal){
		name = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setrole(String newVal){
		role = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void settime(double newVal){
		time = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void settimeunit(int newVal){
		timeunit = newVal;
	}
	
	/**
	 * 
	 * @param newVal
	 */
	public void settype(byte newVal){
		type = newVal;
	}
	
	public byte gettype(){
		return type;
	}
	
	/**
	 * 
	 * @param newVal
	 */
	public void setandsplit(boolean newVal){
		is_and_split = newVal;
	}
	
	/**
	 * 
	 * @param newVal
	 */
	public void setandjoin(boolean newVal){
		is_and_join = newVal;
	}
	
	/**
	 * 
	 * @param newVal
	 */
	public void setHasResource(boolean newVal){
		has_resource = newVal;
	}
	
	public String toString(){
		return id;
	}

	public void setUnitFactor(double stdUnitMultiple) {
		unitfactor  = stdUnitMultiple;		
	}

	public double getUnitFactor() {
		return unitfactor;
	}
	
	public boolean isJoinReached(){
		return joinReached;
	}
	
	public void setJoinReached(boolean newVal){
		joinReached = newVal;
	}
	
	public double getNumOfRuns() {
		return numOfRuns;
	}

	public void setNumOfRuns(double newVal) {
		numOfRuns = newVal;
	}

	public double getTempRuns() {
		return tempRuns;
	}

	public void setTempRuns(double newVal) {
		tempRuns = newVal;
	}
	
	public int getIteration() {
		return iteration ;
	}

	public void setIteration(int iteration) {
		this.iteration = iteration;
	}
	
	public void incIteration(){
		this.iteration++;
	}

	public String getId() {
		return id;
	}
}