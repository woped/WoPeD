package org.woped.config.metrics;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.JOptionPane;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.woped.config.Constants;
import org.woped.config.WoPeDConfiguration;
import org.woped.config.metrics.AlgorithmGroup.AlgorithmID;
import org.woped.core.config.IMetricsConfiguration;
import org.woped.core.utilities.LoggerManager;
import org.woped.translations.Messages;

public class WoPeDMetricsConfiguration extends WoPeDConfiguration implements
		IMetricsConfiguration {

	private static ResourceBundle rb = PropertyResourceBundle
			.getBundle("org.woped.translations.Messages");

	private static org.woped.config.metrics.ConfigurationDocument confDoc = null;
	
	private static HashMap<String, Variable> variablesMap = new HashMap<String, Variable>();
	private static HashMap<String, Algorithm> algorithmsMap = new HashMap<String, Algorithm>();
	private static HashMap<String, AlgorithmGroup> algorithmGroupsMap = new HashMap<String, AlgorithmGroup>();
	private static HashMap<Integer, String> algorithmGroupIDOrder = new HashMap<Integer, String>();
	private static HashMap<Integer, String> algorithmGroupMemberIDOrder = new HashMap<Integer, String>();
	
	private static String CONFIG_FILE = "WoPeDmetrics.xml";
	private static String CONFIG_CUSTOM_DIR = "/usermetrics";
	private static String CONFIG_BUILTIN_FILE = "/org/woped/config/metrics/WoPeDmetrics.xml";

	public WoPeDMetricsConfiguration(boolean startedAsApplet) {
		super(startedAsApplet);
	}

	public String getConfigFilePath() {
		return getUserdir() + CONFIG_FILE;
	}

	protected org.woped.config.metrics.ConfigurationDocument getConfDocument() {
		return confDoc;
	}

	public boolean initConfig() {
		if (!isLoaded()) {
			// Set XML Options
			xmlOptions.setUseDefaultNamespace();
			xmlOptions.setSavePrettyPrint();
			xmlOptions.setSavePrettyPrintIndent(2);
	
			if (!readConfig())
				return false;
			
			isLoaded = true;
		}

		return true;
	}

	/**
	 * Read configuration (if not running in applet mode).
	 * 
	 * @return indicates whether loading was successful
	 */
	public boolean readConfig() {
		boolean confOk = true;
		String metricsFilePath = "";
		String[] customMetricsFilePaths = null;
		
		if (!startedAsApplet) {
			metricsFilePath = getConfigFilePath();
			if (new File(metricsFilePath).exists()) {
				LoggerManager.info(Constants.CONFIG_LOGGER,
						rb.getString("Init.Config.LoadingFrom") + ": " + metricsFilePath
								+ ".");
				confOk = readConfig(new File(metricsFilePath));
			} else {
				LoggerManager.warn(Constants.CONFIG_LOGGER,
						rb.getString("Init.Config.FileNotFound") + ": " + metricsFilePath
								+ ". " + rb.getString("Init.Config.Fallback")
								+ ".");
				confOk = readConfig(WoPeDConfiguration.class
						.getResourceAsStream(CONFIG_BUILTIN_FILE));
			}			
			
			File file = new File(getUserdir() + CONFIG_CUSTOM_DIR);
			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					if (name.endsWith(".xml") && name.startsWith("WoPeDcustomMetrics"))
						return true;
					return false;
				}
			};
			File[] customFiles = file.listFiles(filter);
			
			if (customFiles != null) {
				customMetricsFilePaths = new String[customFiles.length];
				for(int i = 0; i < customFiles.length; i++)
					customMetricsFilePaths[i] = customFiles[i].getPath();
				
				for(int i = 0; i < customMetricsFilePaths.length; i++) {
					LoggerManager.info(Constants.CONFIG_LOGGER,
								rb.getString("Init.Config.LoadingFrom") + ": " + customMetricsFilePaths[i]
										+ ".");
						confOk = readConfig(new File(customMetricsFilePaths[i]));
				}
			}
			
			createManualGroups();
		} else {
			// if started as an applet, always use built-in config file
			confOk = readConfig(WoPeDConfiguration.class
					.getResourceAsStream(CONFIG_BUILTIN_FILE));
		}

		if (!confOk) {
			JOptionPane.showMessageDialog(null,
					rb.getString("Init.Config.Error") + ". ",
					rb.getString("Init.Config.Error"),
					JOptionPane.ERROR_MESSAGE);
		}

		return confOk;
	}

	/**
	 * @param is
	 *            InputStream with config file content
	 * @return indicates whether loading was successful
	 */
	public boolean readConfig(InputStream is) {
		try {
			confDoc = org.woped.config.metrics.ConfigurationDocument.Factory
					.parse(is);
			return readConfig(confDoc);
		} catch (XmlException e) {
			LoggerManager.error(Constants.CONFIG_LOGGER,
					rb.getString("Init.Config.ParsingError"));
			return false;
		} catch (IOException e) {
			LoggerManager.error(Constants.CONFIG_LOGGER,
					rb.getString("Init.Config.ReadingError"));
			return false;
		}
	}

	/**
	 * Read configuration from a local file.
	 * 
	 * @param file the configuration is loaded from
	 * @return
	 */
	public boolean readConfig(File file) {
		if (file.exists()) {
			try {
				// Parse the instance into the type generated from the XML
				// schema
				confDoc = org.woped.config.metrics.ConfigurationDocument.Factory
						.parse(file);
				return readConfig(confDoc);
			} catch (XmlException e) {
				LoggerManager.error(Constants.CONFIG_LOGGER,
						rb.getString("Init.Config.ParsingError"));
				return false;
			} catch (IOException e) {
				LoggerManager.error(Constants.CONFIG_LOGGER,
						rb.getString("Init.Config.ReadingError"));
				return false;
			}
		} else {
			LoggerManager.error(
					Constants.CONFIG_LOGGER,
					rb.getString("Init.Config.FileNotFound") + ": "
							+ file.getName() + ".");
			return false;
		}
	}

	/**Read the actual metrics configuration and retrieve the variables, algorithms
	 * and algorithmGroups as well as "manually" creating the three internal
	 * algorithmGroups.
	 * 
	 * @param configDoc XmlObject with the actual XML content that will now be processed
	 * @return indicates whether loading was successful or was interrupted due to an error
	 */
	public boolean readConfig(XmlObject configDoc) {
		// Create an instance of a type generated from schema to hold the XML.
		org.woped.config.metrics.ConfigurationDocument confDoc = (org.woped.config.metrics.ConfigurationDocument) configDoc;
		org.woped.config.metrics.ConfigurationDocument.Configuration config;

		if (confDoc != null && (config = confDoc.getConfiguration()) != null) {
			Variable[] variables = null;
			Algorithm[] algorithms = null;
			AlgorithmGroup[] algorithmGroups;
				
			if (config.isSetVariables()) {
				variables = config.getVariables().getVariableArray();
				for (int i = 0; i < variables.length; i++)
					variablesMap.put(variables[i].getID(), variables[i]);
			}
			
			if (config.isSetAlgorithms()) {
				algorithms = config.getAlgorithms().getAlgorithmArray();
				for (int i = 0; i < algorithms.length; i++)
					algorithmsMap.put(algorithms[i].getID(), algorithms[i]);
			}
				
			if (config.isSetAlgorithmGroups()) {
				algorithmGroups = config.getAlgorithmGroups().getAlgorithmGroupArray();
				for (int i = 0; i < algorithmGroups.length; i++) {
					algorithmGroupsMap.put(algorithmGroups[i].getID(), algorithmGroups[i]);
					algorithmGroupIDOrder.put(algorithmGroups[i].getGroupOrder(), algorithmGroups[i].getID());
				}
			}

			LoggerManager.info(Constants.CONFIG_LOGGER,
					rb.getString("Init.Config.LoadingSuccess") + ".");
			return true;
		} else {
			LoggerManager.info(Constants.CONFIG_LOGGER,
					rb.getString("Init.Config.LoadingError") + ".");
			return false;
		}
	}
	
	private void createManualGroups() {
		// Manual "All metrics" algorithm group
		if (variablesMap != null && variablesMap.size() > 0 && 
				algorithmsMap != null && algorithmsMap.size() > 0) {
			AlgorithmGroup manualAlgorithmGroup = AlgorithmGroup.Factory.newInstance();
			manualAlgorithmGroup.setID("ALL_METRICS");
			manualAlgorithmGroup.setName("Metrics.AlgorthimGroup.Name.AllMetrics");
			manualAlgorithmGroup.setDescription("Metrics.AlgorthimGroup.Descr.AllMetrics");
			manualAlgorithmGroup.setGroupOrder(-3);
			for(String key : variablesMap.keySet())
				manualAlgorithmGroup.addNewAlgorithmID().setStringValue(variablesMap.get(key).getID());			
			for(String key : algorithmsMap.keySet())
				manualAlgorithmGroup.addNewAlgorithmID().setStringValue(algorithmsMap.get(key).getID());
			algorithmGroupsMap.put(manualAlgorithmGroup.getID(), manualAlgorithmGroup);
			algorithmGroupIDOrder.put(manualAlgorithmGroup.getGroupOrder(), manualAlgorithmGroup.getID());
		}
		
		// Manual "All variables" algorithm group
		if (variablesMap != null && variablesMap.size() > 0) {
			AlgorithmGroup manualAlgorithmGroup = AlgorithmGroup.Factory.newInstance();
			manualAlgorithmGroup.setID("ALL_VARIABLES");
			manualAlgorithmGroup.setName("Metrics.AlgorthimGroup.Name.AllVariables");
			manualAlgorithmGroup.setDescription("Metrics.AlgorthimGroup.Descr.AllVariables");
			manualAlgorithmGroup.setGroupOrder(-2);
			for(String key : variablesMap.keySet())
				manualAlgorithmGroup.addNewAlgorithmID().setStringValue(variablesMap.get(key).getID());
			algorithmGroupsMap.put(manualAlgorithmGroup.getID(), manualAlgorithmGroup);
			algorithmGroupIDOrder.put(manualAlgorithmGroup.getGroupOrder(), manualAlgorithmGroup.getID());
		}
		
		// Manual "All algorithms" algorithm group
		if (algorithmsMap != null && algorithmsMap.size() > 0) {
			AlgorithmGroup manualAlgorithmGroup = AlgorithmGroup.Factory.newInstance();
			manualAlgorithmGroup.setID("ALL_ALGORITHMS");
			manualAlgorithmGroup.setName("Metrics.AlgorthimGroup.Name.AllAlgorithms");
			manualAlgorithmGroup.setDescription("Metrics.AlgorthimGroup.Descr.AllAlgorithms");
			manualAlgorithmGroup.setGroupOrder(-1);
			for(String key : algorithmsMap.keySet())
				manualAlgorithmGroup.addNewAlgorithmID().setStringValue(algorithmsMap.get(key).getID());
			algorithmGroupsMap.put(manualAlgorithmGroup.getID(), manualAlgorithmGroup);
			algorithmGroupIDOrder.put(manualAlgorithmGroup.getGroupOrder(), manualAlgorithmGroup.getID());
		}
	}

	/** Saves the configuration to a local file
	 * 
	 * @param file where the configuration will be saved to
	 * @return indicates whether saving was successful or was interrupted due to an error
	 */
	public boolean saveConfig(File file) {
		try {
			getConfDocument().save(file, xmlOptions);
			LoggerManager.info(Constants.CONFIG_LOGGER, 
					rb.getString("Exit.Config.SavingSuccess") + ": " + file.getName());
			return true;
		} catch (IOException e) {
			LoggerManager.error(Constants.CONFIG_LOGGER,
					rb.getString("Exit.Config.SavingError") + ": " + file.getName());
			return false;
		}
	}

	// Variables
	public Set<String> getVariableIDs() {
		return variablesMap.keySet();
	}
	
	public String getVariableName(String variableID) {
		if (!variablesMap.containsKey(variableID))
			return null;
		
		if (variablesMap.get(variableID).isSetName()) {
			if (variablesMap.get(variableID).isSetNonTranslatable())
				return variablesMap.get(variableID).getName();
			else
				return Messages.getString(variablesMap.get(variableID).getName());
		}
		else 
			return variableID;
	}

	public boolean hasVariableFormula(String variableID) {
		if (!variablesMap.containsKey(variableID))
			return false;

		if (variablesMap.get(variableID).isSetFormula() && 
				!variablesMap.get(variableID).getFormula().isEmpty())
			return true;
		else
			return false;
	}

	public boolean hasVariableMethod(String variableID) {
		if (!variablesMap.containsKey(variableID))
			return false;

		if (variablesMap.get(variableID).isSetMethod() && 
				!variablesMap.get(variableID).getMethod().isEmpty())
			return true;
		else
			return false;
	}

	public String getVariableFormula(String variableID) {
		if (!variablesMap.containsKey(variableID))
			return null;
		
		return variablesMap.get(variableID).getFormula();
	}

	public String getVariableMethod(String variableID) {
		if (!variablesMap.containsKey(variableID))
			return null;
		
		return variablesMap.get(variableID).getMethod();
	}

	public String getVariableDescription(String variableID) {
		if (!variablesMap.containsKey(variableID))
			return null;
		
		if (variablesMap.get(variableID).isSetDescription())
			if (variablesMap.get(variableID).isSetNonTranslatable())
				return variablesMap.get(variableID).getDescription();
			else
				return Messages.getString(variablesMap.get(variableID).getDescription());
		else
			return "";
	}
	
	public String getVariableImplementation(String variableID) {
		if (!variablesMap.containsKey(variableID))
			return null;
		
		if (hasVariableMethod(variableID))
			return getVariableMethod(variableID);
		else if (hasVariableFormula(variableID))
			return getVariableFormula(variableID);
		else
			return null;
	}

	// Algorithms
	public Set<String> getAlgorithmIDs() {
		return algorithmsMap.keySet();
	}
	
	public String getAlgorithmName(String algorithmID) {
		if (!algorithmsMap.containsKey(algorithmID))
			return null;
		
		if (algorithmsMap.get(algorithmID).isSetName())
			if (algorithmsMap.get(algorithmID).isSetNonTranslatable())
				return algorithmsMap.get(algorithmID).getName();
			else
				return Messages.getString(algorithmsMap.get(algorithmID).getName());
		else 
			return algorithmID;
	}

	public boolean hasAlgorithmFormula(String algorithmID) {
		if (!algorithmsMap.containsKey(algorithmID))
			return false;

		if (algorithmsMap.get(algorithmID).isSetFormula() && 
				!algorithmsMap.get(algorithmID).getFormula().isEmpty())
			return true;
		else
			return false;
	}

	public boolean hasAlgorithmMethod(String algorithmID) {
		if (!algorithmsMap.containsKey(algorithmID))
			return false;

		if (algorithmsMap.get(algorithmID).isSetMethod() && 
				!algorithmsMap.get(algorithmID).getMethod().isEmpty())
			return true;
		else
			return false;
	}

	public String getAlgorithmFormula(String algorithmID) {
		if (!algorithmsMap.containsKey(algorithmID))
			return null;
		
		return algorithmsMap.get(algorithmID).getFormula();
	}

	public String getAlgorithmMethod(String algorithmID) {
		if (!algorithmsMap.containsKey(algorithmID))
			return null;
		
		return algorithmsMap.get(algorithmID).getMethod();
	}

	public String getAlgorithmDescription(String algorithmID) {
		if (!algorithmsMap.containsKey(algorithmID))
			return null;
		
		if (algorithmsMap.get(algorithmID).isSetDescription())
			if (algorithmsMap.get(algorithmID).isSetNonTranslatable())
				return algorithmsMap.get(algorithmID).getDescription();
			else
				return Messages.getString(algorithmsMap.get(algorithmID).getDescription());
		else
			return "";
	}

	public String getAlgorithmsImplementation(String algorithmID) {
		if (!algorithmsMap.containsKey(algorithmID))
			return null;
		
		if (hasAlgorithmMethod(algorithmID))
			return getAlgorithmMethod(algorithmID);
		else if (hasAlgorithmFormula(algorithmID))
			return getAlgorithmFormula(algorithmID);
		else
			return null;
	}
	
	public int getAlgorithmThresholdCount(String algorithmID) {
		if (!algorithmsMap.containsKey(algorithmID))
			return 0;
		
		if (algorithmsMap.get(algorithmID).isSetThresholds()) 
			return algorithmsMap.get(algorithmID).getThresholds().getThresholdArray().length;
		else
			return 0;
	}
	
	public double getAlgorithmThresholdLowValue(String algorithmID, int thresholdID) {
		if (getAlgorithmThresholdCount(algorithmID) > thresholdID)
			if (algorithmsMap.get(algorithmID).getThresholds().getThresholdArray()[thresholdID].getRange().isSetLow())
				return algorithmsMap.get(algorithmID).getThresholds().getThresholdArray()[thresholdID].getRange().getLow();
			else
				return Integer.MIN_VALUE;
		else
			return Double.NaN;			
	}
	
	public double getAlgorithmThresholdHighValue(String algorithmID, int thresholdID) {
		if (getAlgorithmThresholdCount(algorithmID) > thresholdID)
			if (algorithmsMap.get(algorithmID).getThresholds().getThresholdArray()[thresholdID].getRange().isSetHigh())
				return algorithmsMap.get(algorithmID).getThresholds().getThresholdArray()[thresholdID].getRange().getHigh();
			else
				return Double.MAX_VALUE;
		else
			return Double.NaN;			
	}
	
	public AlgorithmThresholdState getAlgorithmThresholdState(String algorithmID, int thresholdID) {
		if (getAlgorithmThresholdCount(algorithmID) > thresholdID) {
			if (algorithmsMap.get(algorithmID).getThresholds().getThresholdArray()[thresholdID].getState().intValue() == ThresholdState.Enum.INT_GREEN)
				return AlgorithmThresholdState.GREEN;
			else if (algorithmsMap.get(algorithmID).getThresholds().getThresholdArray()[thresholdID].getState().intValue() == ThresholdState.Enum.INT_YELLOW)
				return AlgorithmThresholdState.YELLOW;
			else if (algorithmsMap.get(algorithmID).getThresholds().getThresholdArray()[thresholdID].getState().intValue() == ThresholdState.Enum.INT_RED)
				return AlgorithmThresholdState.RED;
		}
		return null;
	}

	// AlgorithmGroups
	public List<String> getAlgorithmGroupIDs() {
		ArrayList<String> list = new ArrayList<String>();
		SortedSet<Integer> sortedset = new TreeSet<Integer>(algorithmGroupIDOrder.keySet());

	   for(int i : sortedset)
		   list.add(algorithmGroupsMap.get(algorithmGroupIDOrder.get(i)).getID());    
		
		return list;
	}
	
	public String getAlgorithmGroupName(String algorithmGroupID) {
		if (!algorithmGroupsMap.containsKey(algorithmGroupID))
			return null;
		
		if (algorithmGroupsMap.get(algorithmGroupID).isSetName())
			if (algorithmGroupsMap.get(algorithmGroupID).isSetNonTranslatable())
				return algorithmGroupsMap.get(algorithmGroupID).getName();
			else
				return Messages.getString(algorithmGroupsMap.get(algorithmGroupID).getName());
		else 
			return algorithmGroupID;
	}

	public List<String> getAlgorithmIDsFromGroup(String algorithmGroupID) {
		if (!algorithmGroupsMap.containsKey(algorithmGroupID))
			return null;
		
		AlgorithmID[] ids = algorithmGroupsMap.get(algorithmGroupID).getAlgorithmIDArray();

		for (int i = 0; i < ids.length; i++) {
			if (ids[i].isSetOrder())
				algorithmGroupMemberIDOrder.put(ids[i].getOrder(), ids[i].getStringValue());
		}
		
		ArrayList<String> list = new ArrayList<String>();
		SortedSet<Integer> sortedset = new TreeSet<Integer>(algorithmGroupMemberIDOrder.keySet());

	   for(int i : sortedset)
		   list.add(algorithmGroupMemberIDOrder.get(i));    
	   
	   for (int i = 0; i < ids.length; i++) {
			if (!ids[i].isSetOrder())
				list.add(ids[i].getStringValue());
		}
		
		return list;
	}

	public String getAlgorithmGroupDescription(String algorithmGroupID) {
		if (!algorithmGroupsMap.containsKey(algorithmGroupID))
			return null;

		if (algorithmGroupsMap.get(algorithmGroupID).isSetNonTranslatable())
			return algorithmGroupsMap.get(algorithmGroupID).getDescription();
		else
			return Messages.getString(algorithmGroupsMap.get(algorithmGroupID).getDescription());
	}
}