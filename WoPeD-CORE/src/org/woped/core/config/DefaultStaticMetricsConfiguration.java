package org.woped.core.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

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
    
    // Variables
	public Set<String> getVariableIDs() {
		return new HashSet<String>();
	}
	
	public String getVariableName(String variableID) {
		return null;
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

	public String getVariableMethod(String variableID) {
		return null;
	}

	public boolean hasVariableFormula(String variableID) {
		return false;
	}

	public String getVariableDescription(String variableID) {
		return null;
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

	public boolean hasAlgorithmFormula(String algorithmID) {
		return false;
	}

	public boolean hasAlgorithmMethod(String algorithmID) {
		return false;
	}

	public String getAlgorithmFormula(String algorithmID) {
		return null;
	}

	public String getAlgorithmMethod(String algorithmID) {
		return null;
	}

	public String getAlgorithmDescription(String algorithmID) {
		return null;
	}

	public String getAlgorithmsImplementation(String algorithmID) {
		return null;
	}
	
	public int getAlgorithmThresholdCount(String algorithmID) {
		return 0;
	}  
	
	public double getAlgorithmThresholdLowValue(String algorithmID, int thresholdID) {
		return Double.NaN;
	}

	public double getAlgorithmThresholdHighValue(String algorithmID, int thresholdID) {
		return Double.NaN;
	}
	
	public AlgorithmThresholdState getAlgorithmThresholdState(String algorithmID, int thresholdID) {
		return null;
	}

	// AlgorithmGroups
	public List<String> getAlgorithmGroupIDs() {
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

	public List<String> getAlgorithmIDsFromGroup(String algorithmGroupID) {
		return new ArrayList<String>();
	}

	public String getAlgorithmGroupDescription(String algorithmGroupID) {
		return null;
	}
}
