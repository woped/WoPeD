package org.woped.core.config;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * Interface defining the public capabilities of the metrics configuration
 * @author Philip Allgaier
 *
 */
public interface IMetricsConfiguration extends IConfiguration {
	
	/** Possible states for the algorithm thresholds
	 */
	enum MetricThresholdState {
		GREEN,
		YELLOW,
		RED, 
		GRAY,
		NONE
	}
	
	public String getCustomMetricsDir();
	
	public boolean save(List<String> metricIDs, File exportFile);

	// Variables
	/** Returns the IDs of all loaded variables.
	 * 
	 * @return Set<String> containing the variable IDs
	 */
	public Set<String> getVariableIDs();
	
	public String getVariableName(String variableID);
	
	public void setVariableName(String variableID, String variableName);
	
	public boolean hasVariableFormula(String variableID);
	
	public boolean hasVariableMethod(String variableID);
	
	public String getVariableFormula(String variableID);
	
	public void setVariableFormula(String variableID, String variableFormula);
	
	public String getVariableMethod(String variableID);
	
	public String getVariableDescription(String variableID);
	
	public void setVariableDescription(String variableID, String variableDescription);
	
	public String getVariableImplementation(String variableID);
	
	// Algorithms
	public Set<String> getAlgorithmIDs();
	
	public String getAlgorithmName(String algorithmID);
	
	public void setAlgorithmName(String algorithmID, String algorithmName);
	
	public boolean hasAlgorithmFormula(String algorithmID);
	
	public boolean hasAlgorithmMethod(String algorithmID);
	
	public String getAlgorithmFormula(String algorithmID);
	
	public void setAlgorithmFormula(String algorithmID, String algorithmFormula);
	
	public String getAlgorithmMethod(String algorithmID);
	
	public void setAlgorithmMethod(String algorithmID, String algorithmMethod);
	
	public String getAlgorithmDescription(String algorithmID);
	
	public void setAlgorithmDescription(String algorithmID, String algorithmDescription);
	
	public String getAlgorithmsImplementation(String algorithmID);
	
	public boolean addNewAlgorithm(String newAlgorithmID, int fileID);
	
	public boolean deleteAlgorithm(String algorithmID);
		
	// AlgorithmGroups
	public List<String> getAlgorithmGroupIDs();
	
	public String getAlgorithmGroupName(String algorithmGroupID);
	
	public void setAlgorithmGroupName(String algorithmGroupID, String algorithmGroupName);
	
	public List<String> getAlgorithmIDsFromGroup(String algorithmGroupID);
	
	public String getAlgorithmGroupDescription(String algorithmGroupID);
	
	public void setAlgorithmGroupDescription(String algorithmGroupID, String algorithmGroupDescription);
	
	public List<String> getGroupIDsFromGroup(String algorithmGroupID);
	
	// General
	public boolean isMetricIDInUse(String metricID);
	
	public boolean isCustomMetric(String metricsID);
	
	public int getMetricThresholdCount(String metricID);
	
	public double getMetricThresholdLowValue(String metricID, int thresholdID);
	
	public void setMetricThresholdLowValue(String metricID, int thresholdID, double lowValue);
	
	public double getMetricThresholdHighValue(String metricID, int thresholdID);
	
	public void setMetricThresholdHighValue(String metricID, int thresholdID, double highValue);
	
	public MetricThresholdState getMetricThresholdState(String metricID, int thresholdID);
	
	public void setMetricThreholdState(String metricID, int thresholdID, java.lang.Enum<?> metricState);

	public boolean isVariable(String metricID);
	
	public boolean isAlgorithm(String metricID);

	public String getHighlightingFormula(String metricID);
	
	public String getMetricOrigin(String metricID);
	
	public void startEditSession();
	
	public void endEditSession(boolean keepChanges);
	
	public int findFileIDToFileName(String fileName);
	
	public void addNewMetricFile(String filePath);
}
