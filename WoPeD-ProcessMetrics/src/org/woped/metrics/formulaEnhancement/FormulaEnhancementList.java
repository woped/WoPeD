package org.woped.metrics.formulaEnhancement;

import java.util.HashMap;
import java.util.Set;

import javax.swing.event.EventListenerList;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.config.IMetricsConfiguration;

/**
 * @author Tobias Lorentz
 * This Class should store all the Formula enhancement which had been found by the parser
 * This Class implements the Singleton-Pattern
 */
public class FormulaEnhancementList {
	/**
	 *  The Enhancements are Stored as a HashMap
	 *  Key is the formulaID
	 */
	private HashMap<String, String> enhancementList;
	/**
	 * This Reference on a object of this class is used to implement the Singleton-Pattern
	 */
	private static FormulaEnhancementList instance;
		
	/**
	 *  This list stores all UIs which are binded to this data-container
	 *  It stores instances of event-listeners
	 */
	private EventListenerList listeners ;
	
	private FormulaEnhancementList(){
		enhancementList = new HashMap<String, String>();
	}
	
	/**
	 * @return an Instance of the class
	 * Singleton-Pattern
	 */
	public static FormulaEnhancementList getInstance(){
		if (instance == null)
			instance = new FormulaEnhancementList();
		
		return instance;		
	}

	/**
	 * @param formulaID This is the FormulaId like it is stored at the FormulaXML
	 * @param enhancedFormula This is the new Formula which the parser had produced
	 * If there is already a Entry for this FormulaID, it will be overwritten
	 */
	public void AddFormula(String formulaID, String enhancedFormula) {
		enhancementList.put(formulaID, enhancedFormula);
		this.notifyFormulaEnhancementListChangedEvent(new FormulaEnhancementListChangedEvent(this));
	}
	
	
	/**
	 * @return all FormulaIDs
	 */
	public Set<String> getAllFormulaIDs() {
		return enhancementList.keySet();
	}
	
	public String getEnhancedFormula(String formulaID){
		return enhancementList.get(formulaID);
	}
	
	/**
	 * @return a Array for displaying the EnhancementList as a Table
	 * First-Column: FormulaID
	 * Second-Column: Old Formula
	 * Third-Column: Enhanced Formula
	 */
	public String[][] getEnhancementListAsArray(){
		//Array were all the data should be posted into
		String[][] array = new String[enhancementList.size()][3];
		
		//List of all FormulaIDs
		String[] keylist = new String[enhancementList.size()];
		this.enhancementList.keySet().toArray(keylist);

		//Sorting the FormulaIDs alphabetically
		java.util.Arrays.sort( keylist );
		
		//Getting access to the Formula Configuration
		//needed to get the old Formula
		IMetricsConfiguration metricsConfig = ConfigurationManager.getMetricsConfiguration();
		
		for(int i = 0; i < keylist.length; i++){
			//FormulaID
			array[i][0] = keylist[i];
			//Old-Formula
			if(metricsConfig.hasAlgorithmFormula(keylist[i])){
				//This Formula is a Algorithm
				array[i][1] = metricsConfig.getAlgorithmFormula(keylist[i]);
			}else if(metricsConfig.hasVariableFormula(keylist[i])){
				//This Formula is a Variable
				array[i][1] = metricsConfig.getVariableFormula(keylist[i]);
			}else{
				array[i][1] = " ";
			}
			 
			//EnhancedFormula
			array[i][2] = this.enhancementList.get(keylist[i]);
		}
		
		return array;
		
	}
	
	public boolean enhancementsAvailable(){
		return !this.enhancementList.isEmpty();
	}
	
	/**
	 * @param formulaID
	 */
	public void acceptEnhancement(String formulaID){
		//Put the new formula into the xml-writing-queue
		ConfigurationManager.getMetricsConfiguration().setAlgorithmFormula(formulaID, this.enhancementList.get(formulaID));		
		//Flush the Queue
		ConfigurationManager.getMetricsConfiguration().saveConfig();
		//Delete the Formula from the list
		this.enhancementList.remove(formulaID);
		this.notifyFormulaEnhancementListChangedEvent(new FormulaEnhancementListChangedEvent(this));
	}
	/**
	 * @param selectedRow The Row-Number of the FormulaEnhancementUI
	 */
	public void acceptEnhancement(int selectedRow){
		this.acceptEnhancement(this.getEnhancementListAsArray()[selectedRow][0]);
	}
	
	public void clearList(){
		this.enhancementList.clear();
		this.notifyFormulaEnhancementListChangedEvent(new FormulaEnhancementListChangedEvent(this));
	}
	
	//Event-Handling Section
	/**
	 * This Methods adds a new Listener-Implemention, which will be called if the enhancementList changes
	 * @param listener A Class that implements the FormulaEnhancementListChangedEventListener-Interface
	 * Should be a FormulaEnhancementUI
	 */
	public void addFormulaEnhancementListChangedEventListener(
			FormulaEnhancementListChangedEventListener listener) {
		if (listeners == null){
			listeners = new EventListenerList();
		}
		listeners.add(FormulaEnhancementListChangedEventListener.class, listener);
	}
	
	/**
	 * This Methods removes a new Listener-Implemention, which will be called if the enhancementList changes
	 * @param listener A Class that implements the FormulaEnhancementListChangedEventListener-Interface
	 * Should be a FormulaEnhancementUI
	 */
	public void removeFormulaEnhancementListChangedEventListener(
			FormulaEnhancementListChangedEventListener listener) {
		listeners.remove(FormulaEnhancementListChangedEventListener.class, listener);
	}
	
	/**
	 * This Methods executes the listeners
	 * @param event
	 */
	protected synchronized void notifyFormulaEnhancementListChangedEvent(
			FormulaEnhancementListChangedEvent event) {
		if(listeners != null){
			for (FormulaEnhancementListChangedEventListener l : listeners
					.getListeners(FormulaEnhancementListChangedEventListener.class)) {
				l.formulaEnhancementListChanged(event);
			}
		}
	}

	public void acceptAllEnhancements() {
		// TODO Auto-generated method stub
		
	}
	
}
