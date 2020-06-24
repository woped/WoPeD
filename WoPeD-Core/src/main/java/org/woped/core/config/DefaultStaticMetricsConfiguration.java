package org.woped.core.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Class that provides fallback configuration settings for the metrics configuration part
 * @author Philip Allgaier
 *
 */
public class DefaultStaticMetricsConfiguration implements IMetricsConfiguration {
	// General configuration implementations
	public DefaultStaticMetricsConfiguration() {
        initConfig();
    }

    public boolean initConfig() {
        return true;
    }

    public boolean readConfig(File file) {
        return false;
    }

    public boolean saveConfig() {
        return true;
    }

    public boolean saveConfig(File file) {
        return false;
    }

    // Metric specific implementations
	public String getCustomMetricsDir() {
		return null;
	}
	
	public boolean save(List<String> metricIDs, File exportFile) {
		return false;
	}
    
    // Variables
	public Set<String> getVariableIDs() {
		return new HashSet<String>();
	}
	
	public String getVariableName(String variableID) {
		return null;
	}
	
	public void setVariableName(String variableID, String newVariableName){
	}

	public boolean hasVariableForumla(String variableID) {
		return false;
	}
	
	public boolean hasVariableMethod(String variableID) {
		return false;
	}

	public String getVariableFormula(String variableID) {
		return null;
	}
	
	public void setVariableFormula(String variableID, String variableFormula){
	}

	public String getVariableMethod(String variableID) {
		return null;
	}

	public boolean hasVariableFormula(String variableID) {
		return false;
	}

	public String getVariableDescription(String variableID) {
		return null;
	}
	
	public void setVariableDescription(String variableID, String variableDescription) {
	}
	
	public String getVariableImplementation(String variableID) {
		return null;
	} 

	// Algorithms
	public Set<String> getAlgorithmIDs() {
		return new HashSet<String>();
	}
	
	public String getAlgorithmName(String algorithmID) {
		return null;
	}
	
	public void setAlgorithmName(String algorithmID, String algorithmName) {
	}

	public boolean hasAlgorithmFormula(String algorithmID) {
		return false;
	}

	public boolean hasAlgorithmMethod(String algorithmID) {
		return false;
	}

	public String getAlgorithmFormula(String algorithmID) {
		return null;
	}
	
	public void setAlgorithmFormula(String algorithmID, String algorithmFormula) {
	}

	public String getAlgorithmMethod(String algorithmID) {
		return null;
	}

	public void setAlgorithmMethod(String algorithmID, String algorithmMethod) {	
	}
	
	public String getAlgorithmDescription(String algorithmID) {
		return null;
	}
	
	public void setAlgorithmDescription(String algorithmID,	String algorithmDescription) {
	}

	public String getAlgorithmsImplementation(String algorithmID) {
		return null;
	}
	
	public boolean addNewAlgorithm(String newAlgorithmID, int fileID) {
		return false;
	}
	
	public boolean deleteAlgorithm(String algorithmID) {
		return false;
	}

	// AlgorithmGroups
	public List<String> getAlgorithmGroupIDs() {
		// If we actually come by this point here, we are not loading any metrics XML file
		// and have to use this static default implementation. To tell this the user we add 
		// a "metrics not enabled" dummy entry to the dropdown where normally all groups 
		// would be.
		ArrayList<String> list = new ArrayList<String>();
		list.add("METRICS_NOT_ENABLED");
		return list;
	}
	
	public String getAlgorithmGroupName(String algorithmGroupID) {
		ResourceBundle rb = PropertyResourceBundle.getBundle("org.woped.translations.Messages", ConfigurationManager.getConfiguration().getLocale());
		if (algorithmGroupID.equals("METRICS_NOT_ENABLED"))
			return rb.getString("Configuration.Metrics.NotEnabled");
		
		return null;
	}
	
	public void setAlgorithmGroupName(String algorithmGroupID, String algorithmGroupName) {
	}

	public List<String> getAlgorithmIDsFromGroup(String algorithmGroupID) {
		return new ArrayList<String>();
	}

	public String getAlgorithmGroupDescription(String algorithmGroupID) {
		return null;
	}
	
	public void setAlgorithmGroupDescription(String algorithmGroupID, String algorithmGroupDescription) {		
	}
	
	public List<String> getGroupIDsFromGroup(String algorithmGroupID) {
		return new ArrayList<String>();
	}
	
	// General
	public boolean isMetricIDInUse(String metricID) {
		return false;
	}
	
	public boolean isCustomMetric(String metricsID) {
		return false;
	}
	
	public int getMetricThresholdCount(String metricID) {
		return 0;
	}  
	
	public double getMetricThresholdLowValue(String metricID, int thresholdID) {
		return Double.NaN;
	}
	
	public void setMetricThresholdLowValue(String metricID, int thresholdID, double lowValue) {
	}

	public double getMetricThresholdHighValue(String metricID, int thresholdID) {
		return Double.NaN;
	}
	
	public void setMetricThresholdHighValue(String metricID, int thresholdID, double highValue) {
	}
	
	public MetricThresholdState getMetricThresholdState(String metricID, int thresholdID) {
		return null;
	}
	
	public void setMetricThreholdState(String metricID, int thresholdID, java.lang.Enum<?> metricState) {
	}

	public boolean isVariable(String metricID) {
		return false;
	}

	public boolean isAlgorithm(String metricID) {
		return false;
	}

	public String getHighlightingFormula(String metricID) {
		return "";
	}

	public String getMetricOrigin(String metricID) {
		return "";
	}

	public void startEditSession() {
	}

	public void endEditSession(boolean keepChanges) {
	}

	public int findFileIDToFileName(String fileName) {
		return -1;
	}

	public void addNewMetricFile(String filePath) {
	}
}
