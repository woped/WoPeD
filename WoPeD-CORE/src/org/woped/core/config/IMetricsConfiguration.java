package org.woped.core.config;

import java.util.List;
import java.util.Set;

public interface IMetricsConfiguration extends IConfiguration {
	
	/** Possible states for the algorithm thresholds
	 */
	enum AlgorithmThresholdState {
		GREEN,
		YELLOW,
		RED, 
		GRAY,
		NONE
	}

	// Variables
	/** Returns the IDs of all loaded variables.
	 * 
	 * @return Set<String> containing the variable IDs
	 */
	public Set<String> getVariableIDs();
	
	public String getVariableName(String variableID);
	
	public boolean hasVariableFormula(String variableID);
	
	public boolean hasVariableMethod(String variableID);
	
	public String getVariableFormula(String variableID);
	
	public String getVariableMethod(String variableID);
	
	public String getVariableDescription(String variableID);
	
	public String getVariableImplementation(String variableID);
	
	// Algorithms
	public Set<String> getAlgorithmIDs();
	
	public String getAlgorithmName(String algorithmID);
	
	public boolean hasAlgorithmFormula(String algorithmID);
	
	public boolean hasAlgorithmMethod(String algorithmID);
	
	public String getAlgorithmFormula(String algorithmID);
	
	public String getAlgorithmMethod(String algorithmID);
	
	public String getAlgorithmDescription(String algorithmID);
	
	public String getAlgorithmsImplementation(String algorithmID);
	
	public int getAlgorithmThresholdCount(String algorithmID);
	
	public double getAlgorithmThresholdLowValue(String algorithmID, int thresholdID);
	
	public double getAlgorithmThresholdHighValue(String algorithmID, int thresholdID);
	
	public AlgorithmThresholdState getAlgorithmThresholdState(String algorithmID, int thresholdID);
	
	// AlgorithmGroups
	public List<String> getAlgorithmGroupIDs();
	
	public String getAlgorithmGroupName(String algorithmGroupID);
	
	public List<String> getAlgorithmIDsFromGroup(String algorithmGroupID);
	
	public String getAlgorithmGroupDescription(String algorithmGroupID);
}
