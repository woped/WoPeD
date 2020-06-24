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
//import org.woped.config.metrics.Algorithm;
//import org.woped.config.metrics.AlgorithmGroup;
//import org.woped.config.metrics.ConfigurationDocument;
//import org.woped.config.metrics.ThresholdState;
//import org.woped.config.metrics.Variable;
//import org.woped.config.metrics.AlgorithmGroup.AlgorithmID;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.config.IMetricsConfiguration;
import org.woped.core.utilities.LoggerManager;
import org.woped.gui.translations.Messages;

/**
 * Class that provides access to the metrics configuration settings. 
 * Access to it at runtime is to be gained through ConfigurationManager.
 * @see ConfigurationManager
 * @author Philip Allgaier
 *
 */
public class WoPeDMetricsConfiguration extends WoPeDConfiguration implements
		IMetricsConfiguration {

	private ResourceBundle rb = PropertyResourceBundle
			.getBundle("org.woped.gui.translations.Messages");

	private org.woped.config.metrics.ConfigurationDocument confDoc = null;
	
	// Metrics XML files from which content was loaded
	private HashMap<Integer, String> fileMap = new HashMap<Integer, String>();
	private HashMap<Integer, ConfigurationDocument> confMap = new HashMap<Integer, ConfigurationDocument>();
	private HashMap<Integer, ConfigurationDocument> confMapBackup = new HashMap<Integer, ConfigurationDocument>();
	private int currentFileID;
		
	// HashMaps for variables
	private HashMap<String, Variable> variablesMap = new HashMap<String, Variable>();
	//private HashMap<Integer, String> variableIDOrder = new HashMap<Integer, String>();
	private HashMap<String, Integer> variableOrigin = new HashMap<String, Integer>();
	
	// HashMaps for algorithms
	private HashMap<String, Algorithm> algorithmsMap = new HashMap<String, Algorithm>();
	//private HashMap<Integer, String> algorithmIDOrder = new HashMap<Integer, String>();
	private HashMap<String, Integer> algorithmOrigin = new HashMap<String, Integer>();
	
	// HashMaps for algorithm groups
	private HashMap<String, AlgorithmGroup> algorithmGroupsMap = new HashMap<String, AlgorithmGroup>();
	private HashMap<Integer, String> algorithmGroupIDOrder = new HashMap<Integer, String>();
	private HashMap<String, Integer> algorithmGroupOrigin = new HashMap<String, Integer>();
	private int currentGroupOrder = 0;
	
	private final String CONFIG_FILE 			= "WoPeDmetrics.xml";
	private final String CONFIG_CUSTOM_DIR 		= "/usermetrics";
	private final String CONFIG_BUILTIN_FILE	= "/org/woped/config/metrics/WoPeDmetrics.xml";
	private final String ALL_METRICS_GROUP_NAME = rb.getString("Metrics.AlgorthimGroup.Name.AllMetrics");
	private final String ALL_CUSTOM_GROUP_NAME	= rb.getString("Metrics.AlgorthimGroup.Name.AllCustom");
	
	private boolean doubleIDUsage = false;

	public String getConfigFilePath() {
		return getUserdir() + CONFIG_FILE;
	}
	
	public String getCustomMetricsDir() {
		return getUserdir() + CONFIG_CUSTOM_DIR;
	}

	/**
	 * DO NOT CALL! ALWAYS RETRIEVE THE CONFIGURATION DOCUMENT VIA THE "confMap"!
	 */
	protected org.woped.config.metrics.ConfigurationDocument getConfDocument() {
		return null;
	}

	/**
	 * Initialize the configuration.
	 * @return true if initialization successful
	 */
	public boolean initConfig() {
		if (!isLoaded()) {
			// Set XML Options
			xmlOptions.setUseDefaultNamespace();
			xmlOptions.setSavePrettyPrint();
			xmlOptions.setSavePrettyPrintIndent(2);
			
			if (!readConfig())
				return false;
			
			if(doubleIDUsage)
				JOptionPane.showMessageDialog(null, rb.getString("Init.Config.DoubleIDUsage") + "!");
			
			isLoaded = true;
		}

		return true;
	}

	/**
	 * Read configuration (if not running in applet mode). 
	 * @return indicates whether loading was successful
	 */
	public boolean readConfig() {
		boolean confOk = true;
		String metricsFilePath = "";
		String[] customMetricsFilePaths = null;
		
		metricsFilePath = getConfigFilePath();
		currentFileID = 0;
		if (new File(metricsFilePath).exists()) {
			fileMap.put(currentFileID, getConfigFilePath());
			LoggerManager.info(Constants.CONFIG_LOGGER,
					rb.getString("Init.Config.LoadingFrom") + ": "
							+ metricsFilePath + ".");
			confOk = readConfig(new File(metricsFilePath));
			if (confOk)
				confMap.put(currentFileID, confDoc);
		} else {
			fileMap.put(currentFileID, CONFIG_BUILTIN_FILE);
			LoggerManager.warn(
					Constants.CONFIG_LOGGER,
					rb.getString("Init.Config.FileNotFound") + ": "
							+ metricsFilePath + ". "
							+ rb.getString("Init.Config.Fallback") + ".");
			confOk = readConfig(WoPeDConfiguration.class
					.getResourceAsStream(CONFIG_BUILTIN_FILE));
			if (confOk)
				confMap.put(currentFileID, confDoc);
		}

		File file = new File(getUserdir() + CONFIG_CUSTOM_DIR);
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if (name.endsWith(".xml"))
					return true;
				return false;
			}
		};
		File[] customFiles = file.listFiles(filter);

		if (customFiles != null) {
			customMetricsFilePaths = new String[customFiles.length];
			for (int i = 0; i < customFiles.length; i++)
				customMetricsFilePaths[i] = customFiles[i].getPath();

			for (int i = 0; i < customMetricsFilePaths.length; i++) {
				currentFileID = i + 1;
				fileMap.put(currentFileID, customMetricsFilePaths[i]);
				LoggerManager.info(Constants.CONFIG_LOGGER,
						rb.getString("Init.Config.LoadingFrom") + ": "
								+ customMetricsFilePaths[i] + ".");
				confOk = readConfig(new File(customMetricsFilePaths[i]));
				confMap.put(currentFileID, confDoc);
			}
		}

		createManualGroups();

		if (!confOk) {
			JOptionPane.showMessageDialog(null,
					rb.getString("Init.Config.Error") + ". ",
					rb.getString("Init.Config.Error"),
					JOptionPane.ERROR_MESSAGE);
		}

		return confOk;
	}

	/**
	 * Read a configuration input stream
	 * @param is InputStream with config file content
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

	/**
	 * Read the actual metrics configuration and retrieve the variables, algorithms
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
		
		HashMap<Integer, String> localAlgorithmGroupIDOrder = new HashMap<Integer, String>();

		if (confDoc != null && (config = confDoc.getConfiguration()) != null) {
			Variable[] variables = null;
			Algorithm[] algorithms = null;
			AlgorithmGroup[] algorithmGroups;
				
			if (config.isSetVariables()) {
				variables = config.getVariables().getVariableArray();
				for (int i = 0; i < variables.length; i++) {
					if(variablesMap.containsKey(variables[i].getID())){
						doubleIDUsage = true;
						LoggerManager.info(Constants.CONFIG_LOGGER,
								rb.getString("Init.Config.DoubleID") + ": " + variables[i].getID());
					}
					variablesMap.put(variables[i].getID(), variables[i]);
					variableOrigin.put(variables[i].getID(), currentFileID);
				}
			}
			
			if (config.isSetAlgorithms()) {
				algorithms = config.getAlgorithms().getAlgorithmArray();
				for (int i = 0; i < algorithms.length; i++) {
					if(algorithmsMap.containsKey(algorithms[i].getID())){
						doubleIDUsage = true;
						LoggerManager.info(Constants.CONFIG_LOGGER,
								rb.getString("Init.Config.DoubleID") + ": " + algorithms[i].getID());
					}
					algorithmsMap.put(algorithms[i].getID(), algorithms[i]);
					algorithmOrigin.put(algorithms[i].getID(), currentFileID);
				}
			}
				
			if (config.isSetAlgorithmGroups()) {
				algorithmGroups = config.getAlgorithmGroups().getAlgorithmGroupArray();
				for (int i = 0; i < algorithmGroups.length; i++) {
					if(algorithmGroupsMap.containsKey(algorithmGroups[i].getID())){
						doubleIDUsage = true;
						LoggerManager.info(Constants.CONFIG_LOGGER,
								rb.getString("Init.Config.DoubleID") + ": " + algorithmGroups[i].getID());
					}
					algorithmGroupsMap.put(algorithmGroups[i].getID(), algorithmGroups[i]);
					localAlgorithmGroupIDOrder.put(algorithmGroups[i].getGroupOrder(), algorithmGroups[i].getID());
					algorithmGroupOrigin.put(algorithmGroups[i].getID(), currentFileID);
				}
				
				for(int i = 1; i <= localAlgorithmGroupIDOrder.size(); i++) {
					algorithmGroupIDOrder.put(currentGroupOrder, localAlgorithmGroupIDOrder.get(i));
					currentGroupOrder++;
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
	
	/**
	 * Create the two groups "ALL_METRICS" and "ALL_CUSTOM". 
	 * Those will always be shown as the two first in the metrics sidebar dropdown box.
	 */
	private void createManualGroups() {
		// Manual "All metrics" algorithm group
		AlgorithmGroup manualAlgorithmGroup = AlgorithmGroup.Factory.newInstance();
		manualAlgorithmGroup.setID(ALL_METRICS_GROUP_NAME);
		manualAlgorithmGroup.setName("Metrics.AlgorthimGroup.Name.AllMetrics");
		manualAlgorithmGroup.setDescription("Metrics.AlgorthimGroup.Descr.AllMetrics");
		manualAlgorithmGroup.setGroupOrder(-3);
		// Add all groups 
		for(String key : algorithmGroupsMap.keySet())
			manualAlgorithmGroup.addNewAlgorithmID().setStringValue(key);		
		
		// Create dummy group for all yet ungrouped metrics of a file
		List<String> ungroupedMetrics = getUngroupedMetricsFromFile(0);
		AlgorithmGroup dummyAlgorithmGroup = AlgorithmGroup.Factory.newInstance();
		dummyAlgorithmGroup.setID("UNGROUPED_" + fileMap.get(0));
		dummyAlgorithmGroup.setName(new File(fileMap.get(0)).getName() + "_ungrouped");
		dummyAlgorithmGroup.setNonTranslatable(true);
		dummyAlgorithmGroup.setGroupOrder(currentGroupOrder++);
		for(String key : ungroupedMetrics)
			dummyAlgorithmGroup.addNewAlgorithmID().setStringValue(key);
				
		manualAlgorithmGroup.addNewAlgorithmID().setStringValue(dummyAlgorithmGroup.getID());
		algorithmGroupsMap.put(dummyAlgorithmGroup.getID(), dummyAlgorithmGroup);
		algorithmGroupsMap.put(manualAlgorithmGroup.getID(), manualAlgorithmGroup);
		algorithmGroupIDOrder.put(dummyAlgorithmGroup.getGroupOrder(), dummyAlgorithmGroup.getID());
		algorithmGroupIDOrder.put(manualAlgorithmGroup.getGroupOrder(), manualAlgorithmGroup.getID());
		algorithmGroupOrigin.put(dummyAlgorithmGroup.getID(), 0);
		
		// Manual "All custom" algorithm group
		manualAlgorithmGroup = AlgorithmGroup.Factory.newInstance();
		manualAlgorithmGroup.setID(ALL_CUSTOM_GROUP_NAME);
		manualAlgorithmGroup.setName("Metrics.AlgorthimGroup.Name.AllCustom");
		manualAlgorithmGroup.setDescription("Metrics.AlgorthimGroup.Descr.AllCustom");
		manualAlgorithmGroup.setGroupOrder(-1);
		
		// Add all custom groups 
		for(String key : algorithmGroupsMap.keySet()){
			if(isCustomMetric(key))
				manualAlgorithmGroup.addNewAlgorithmID().setStringValue(key);	
		}
		
		for(int i : fileMap.keySet()){
			// FileID = internal metrics -> skip that iteration
			if(i == 0)
				continue;
			
			// Create dummy group for all yet ungrouped metrics of a file
			List<String> ungroupedCustomMetrics = getUngroupedMetricsFromFile(i);
			dummyAlgorithmGroup = AlgorithmGroup.Factory.newInstance();
			dummyAlgorithmGroup.setID("UNGROUPED_" + fileMap.get(i));
			dummyAlgorithmGroup.setName(new File(fileMap.get(i)).getName() + "_ungrouped");
			dummyAlgorithmGroup.setNonTranslatable(true);
			dummyAlgorithmGroup.setGroupOrder(currentGroupOrder++);
			for(String key : ungroupedCustomMetrics){
				dummyAlgorithmGroup.addNewAlgorithmID().setStringValue(key);
				dummyAlgorithmGroup.getAlgorithmIDArray(dummyAlgorithmGroup.getAlgorithmIDArray().length - 1).setOrder(dummyAlgorithmGroup.getAlgorithmIDArray().length);
			}
		}
				
		manualAlgorithmGroup.addNewAlgorithmID().setStringValue(dummyAlgorithmGroup.getID());
		algorithmGroupsMap.put(dummyAlgorithmGroup.getID(), dummyAlgorithmGroup);
		algorithmGroupsMap.put(manualAlgorithmGroup.getID(), manualAlgorithmGroup);
		algorithmGroupIDOrder.put(dummyAlgorithmGroup.getGroupOrder(), dummyAlgorithmGroup.getID());
		algorithmGroupIDOrder.put(manualAlgorithmGroup.getGroupOrder(), manualAlgorithmGroup.getID());
		algorithmGroupOrigin.put(dummyAlgorithmGroup.getID(), -1);
	}

	/**
	 * DO NOT CALL! ONLY IMPLEMENTED HERE TO PREVENT THE EXECUTION OF THE
	 * BEHAVIOR OF PARENT CLASS!
	 * @return always false
	 */
	public boolean saveConfig(File file) {
		return false;
	}
	
	/**
	 * Save configuration to a specified fileID that is connected with a file path.
	 * @param fileID connected by the file path 
	 * @return indicates whether saving was successful or was interrupted due to an error
	 */
	public boolean saveConfig(int fileID){
		// fileID == 0 means that someone is trying to save the internal metrics,
		// which are static and cannot be manipulated in the first place -> return
		if(fileID == 0)
			return true;
		
		try {
			// Since the values of the algorithms could have been changed via the metrics builder,
			// we have to insert them again manually in the configuration.
			// So first remove the current ones and then put the new ones in.
			ConfigurationDocument toBeSavedConfig = (ConfigurationDocument) confMap.get(fileID).copy();
						
			if(toBeSavedConfig.getConfiguration().isSetAlgorithms())
				toBeSavedConfig.getConfiguration().unsetAlgorithms();
			toBeSavedConfig.getConfiguration().addNewAlgorithms();
			
			for(String metricID : algorithmOrigin.keySet())
				if(algorithmOrigin.get(metricID) == fileID) {	
					Algorithm algo = algorithmsMap.get(metricID);
					toBeSavedConfig.getConfiguration().getAlgorithms().addNewAlgorithm().set(algo);
				}
			
			toBeSavedConfig.save(new File(fileMap.get(fileID)), xmlOptions);
			confMap.put(fileID, toBeSavedConfig);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	/**
	 * DO NOT CALL! ONLY IMPLEMENTED HERE TO PREVENT THE EXECUTION OF THE
	 * BEHAVIOR OF PARENT CLASS!
	 * @return always false
	 */
	public boolean save() {
		// DO NOT CALL
		return false;
	}
	
	/**
	 * Saves/exports the specified metrics to the specified file
	 */
	public boolean save(List<String> metricIDs, File exportFile) {
		ConfigurationDocument confDoc = ConfigurationDocument.Factory.newInstance(xmlOptions);
		confDoc.addNewConfiguration();
		confDoc.getConfiguration().addNewVariables();
		confDoc.getConfiguration().addNewAlgorithms();
		confDoc.getConfiguration().addNewAlgorithmGroups();
		
		for(String metricID : metricIDs){
			if(isVariable(metricID)){	
				confDoc.getConfiguration().getVariables().addNewVariable();
				confDoc.getConfiguration().getVariables().setVariableArray(
						confDoc.getConfiguration().getVariables().sizeOfVariableArray() - 1, 
						variablesMap.get(metricID));
			}
			else if(isAlgorithm(metricID)){
				confDoc.getConfiguration().getAlgorithms().addNewAlgorithm();
				confDoc.getConfiguration().getAlgorithms().setAlgorithmArray(
						confDoc.getConfiguration().getAlgorithms().sizeOfAlgorithmArray() - 1, 
						algorithmsMap.get(metricID));
			}
		}
		
		try {
			confDoc.save(exportFile);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	// Variables
	public Set<String> getVariableIDs() {
		return variablesMap.keySet();
	}
	
	public String getVariableName(String variableID) {
		if (!variablesMap.containsKey(variableID))
			return null;
		
		if (variablesMap.get(variableID).isSetName()) {
			if (variablesMap.get(variableID).isSetNonTranslatable() ||
					Messages.exists(variablesMap.get(variableID).getName()) == false)
				return variablesMap.get(variableID).getName();
			else
				return Messages.getString(variablesMap.get(variableID).getName());
		}
		else 
			return variableID;
	}
	
	public void setVariableName(String variableID, String variableName) {
		if (variablesMap.containsKey(variableID)){
			Variable[] vars = confMap.get(variableOrigin.get(variableID)).getConfiguration().getVariables().getVariableArray();
			for(Variable var : vars)
				if(var.getID().equals(variableID)) {		
					var.setName(variableName);
					break;
				}
		}
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
	
	public void setVariableFormula(String variableID, String variableFormula) {
		if (variablesMap.containsKey(variableID)){
			Variable[] vars = confMap.get(variableOrigin.get(variableID)).getConfiguration().getVariables().getVariableArray();
			for(Variable var : vars)
				if(var.getID().equals(variableID)) {		
					var.setFormula(variableFormula);
					break;
				}
		}
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
	
	public void setVariableDescription(String variableID, String variableDescription) {
		if (variablesMap.containsKey(variableID)){
			Variable[] vars = confMap.get(variableOrigin.get(variableID)).getConfiguration().getVariables().getVariableArray();
			for(Variable var : vars)
				if(var.getID().equals(variableID)) {		
					var.setDescription(variableDescription);
					break;
				}
		}
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
			if (algorithmsMap.get(algorithmID).isSetNonTranslatable() || 
					Messages.exists(algorithmsMap.get(algorithmID).getName()) == false)
				return algorithmsMap.get(algorithmID).getName();
			else
				return Messages.getString(algorithmsMap.get(algorithmID).getName());
		else 
			return algorithmID;
	}
	
	public void setAlgorithmName(String algorithmID, String algorithmName) {
		if (algorithmsMap.containsKey(algorithmID))
			algorithmsMap.get(algorithmID).setName(algorithmName);
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
	
	public void setAlgorithmFormula(String algorithmID, String algorithmFormula) {
		if (algorithmsMap.containsKey(algorithmID))
			algorithmsMap.get(algorithmID).setFormula(algorithmFormula);
	}

	public String getAlgorithmMethod(String algorithmID) {
		if (!algorithmsMap.containsKey(algorithmID))
			return null;
		
		return algorithmsMap.get(algorithmID).getMethod();
	}
	
	public void setAlgorithmMethod(String algorithmID, String algorithmMethod) {
		if (algorithmsMap.containsKey(algorithmID))
			algorithmsMap.get(algorithmID).setMethod(algorithmMethod);
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
	
	public void setAlgorithmDescription(String algorithmID, String algorithmDescription) {
		if (algorithmsMap.containsKey(algorithmID))
			algorithmsMap.get(algorithmID).setDescription(algorithmDescription);
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
	
	public int getMetricThresholdCount(String metricID) {
		if (algorithmsMap.containsKey(metricID))
			if (algorithmsMap.get(metricID).isSetThresholds()) 
				return algorithmsMap.get(metricID).getThresholds().getThresholdArray().length;
			else
				return 0;
		else if(variablesMap.containsKey(metricID)) 
			if (variablesMap.get(metricID).isSetThresholds()) 
				return variablesMap.get(metricID).getThresholds().getThresholdArray().length;
			else
				return 0;
		else
			return 0;
	}
	
	public double getMetricThresholdLowValue(String metricID, int thresholdID) {
		if (getMetricThresholdCount(metricID) > thresholdID)
			if (algorithmsMap.containsKey(metricID))			
				if (algorithmsMap.get(metricID).getThresholds().getThresholdArray()[thresholdID].getRange().isSetLow())
					return algorithmsMap.get(metricID).getThresholds().getThresholdArray()[thresholdID].getRange().getLow();
				else
					return Integer.MIN_VALUE;
			else if(variablesMap.containsKey(metricID))
				if (variablesMap.get(metricID).getThresholds().getThresholdArray()[thresholdID].getRange().isSetLow())
					return variablesMap.get(metricID).getThresholds().getThresholdArray()[thresholdID].getRange().getLow();
				else
					return Integer.MIN_VALUE;
			else
				return Double.NaN;	
		else
			return Double.NaN;			
	}
	
	public double getMetricThresholdHighValue(String metricID, int thresholdID) {
		if (getMetricThresholdCount(metricID) > thresholdID)
			if (algorithmsMap.containsKey(metricID))	
				if (algorithmsMap.get(metricID).getThresholds().getThresholdArray()[thresholdID].getRange().isSetHigh())
					return algorithmsMap.get(metricID).getThresholds().getThresholdArray()[thresholdID].getRange().getHigh();
				else
					return Double.MAX_VALUE;
			else if(variablesMap.containsKey(metricID))
				if (variablesMap.get(metricID).getThresholds().getThresholdArray()[thresholdID].getRange().isSetHigh())
					return variablesMap.get(metricID).getThresholds().getThresholdArray()[thresholdID].getRange().getHigh();
				else
					return Double.MAX_VALUE;
			else
				return Double.NaN;
		else
			return Double.NaN;			
	}
	
	public MetricThresholdState getMetricThresholdState(String metricID, int thresholdID) {
		if (getMetricThresholdCount(metricID) > thresholdID) {
			if (algorithmsMap.containsKey(metricID)) {
				if (algorithmsMap.get(metricID).getThresholds().getThresholdArray()[thresholdID].getState().intValue() == ThresholdState.Enum.INT_GREEN)
					return MetricThresholdState.GREEN;
				else if (algorithmsMap.get(metricID).getThresholds().getThresholdArray()[thresholdID].getState().intValue() == ThresholdState.Enum.INT_YELLOW)
					return MetricThresholdState.YELLOW;
				else if (algorithmsMap.get(metricID).getThresholds().getThresholdArray()[thresholdID].getState().intValue() == ThresholdState.Enum.INT_RED)
					return MetricThresholdState.RED;
			}
			else if(variablesMap.containsKey(metricID)) {
				if (variablesMap.get(metricID).getThresholds().getThresholdArray()[thresholdID].getState().intValue() == ThresholdState.Enum.INT_GREEN)
					return MetricThresholdState.GREEN;
				else if (variablesMap.get(metricID).getThresholds().getThresholdArray()[thresholdID].getState().intValue() == ThresholdState.Enum.INT_YELLOW)
					return MetricThresholdState.YELLOW;
				else if (variablesMap.get(metricID).getThresholds().getThresholdArray()[thresholdID].getState().intValue() == ThresholdState.Enum.INT_RED)
					return MetricThresholdState.RED;
			}
			else
				return MetricThresholdState.NONE;
		}
		return null;
	}
	
	/**
	 * Add a new algorithm
	 */
	public boolean addNewAlgorithm(String newAlgorithmID, int fileID) {
		Algorithm algo = Algorithm.Factory.newInstance();
		algo.setID(newAlgorithmID);
		algo.setNonTranslatable(true);
		algo.addNewThresholds();
		
		algorithmsMap.put(newAlgorithmID, algo);	
		algorithmOrigin.put(newAlgorithmID, fileID);
		
		createManualGroups();
		return true;
	}
	
	/**
	 * Completely removes an algorithm from all configuration files.
	 * @return indicates if deletion was successful
	 */
	public boolean deleteAlgorithm(String algorithmID) {
		if(!isCustomMetric(algorithmID)) {
			JOptionPane.showMessageDialog(null, Messages.getString("Metrics.General.CannotDeleteInternal"), Messages.getString("Metrics.General.CannotDeleteInternal"), JOptionPane.ERROR_MESSAGE);
			return false;
		} else {
			int res = JOptionPane.showConfirmDialog(null, Messages.getString("Metrics.General.ReallyDeleteAlgo"), Messages.getString("Metrics.General.ReallyDeleteAlgo"), JOptionPane.YES_NO_OPTION);
			if(res == JOptionPane.OK_OPTION) {
				algorithmsMap.remove(algorithmID);
				int fileID = algorithmOrigin.get(algorithmID);
				algorithmOrigin.remove(algorithmID);
				saveConfig(fileID);
				return true;
			} else
				return false;
		}
	}

	// AlgorithmGroups
	public List<String> getAlgorithmGroupIDs() {
		ArrayList<String> list = new ArrayList<String>();
		SortedSet<Integer> sortedset = new TreeSet<Integer>(algorithmGroupIDOrder.keySet());
		
		for(int i : sortedset)
			list.add(algorithmGroupIDOrder.get(i));    
		
		return list;
	}
	
	public String getAlgorithmGroupName(String algorithmGroupID) {
		// Never translatable so just give back the ID
		if(algorithmGroupID.equals(ALL_METRICS_GROUP_NAME) || algorithmGroupID.equals(ALL_CUSTOM_GROUP_NAME))
			return algorithmGroupID;
			
		if (!algorithmGroupsMap.containsKey(algorithmGroupID))
			return null;
		
		if (algorithmGroupsMap.get(algorithmGroupID).isSetName())
			if (algorithmGroupsMap.get(algorithmGroupID).isSetNonTranslatable() ||
					Messages.exists(algorithmGroupsMap.get(algorithmGroupID).getName()) == false)
				return algorithmGroupsMap.get(algorithmGroupID).getName();
			else
				return Messages.getString(algorithmGroupsMap.get(algorithmGroupID).getName());
		else 
			return algorithmGroupID;
	}
	
	public void setAlgorithmGroupName(String algorithmGroupID, String algorithmGroupName) {
		if (algorithmGroupsMap.containsKey(algorithmGroupID)){
			AlgorithmGroup[] groups = confMap.get(algorithmGroupOrigin.get(algorithmGroupID)).getConfiguration().getAlgorithmGroups().getAlgorithmGroupArray();
			for(AlgorithmGroup group : groups)
				if(group.getID().equals(algorithmGroupID)) {		
					group.setName(algorithmGroupName);
					break;
				}
		}
	}
	
	public List<String> getGroupIDsFromGroup(String algorithmGroupID){
		// If sub groups for ALL_METRICS are requested give back all groups from the file = 0
		// and create a dummy group for those metrics elements that are not in any group
		ArrayList<String> list = new ArrayList<String>();
		
		if(algorithmGroupID.equals(ALL_METRICS_GROUP_NAME)) {
			SortedSet<Integer> sortedset = new TreeSet<Integer>(algorithmGroupIDOrder.keySet());
			
			for(int i : sortedset)
				list.add(algorithmGroupIDOrder.get(i));    
		}	
		else if(algorithmGroupID.equals(ALL_CUSTOM_GROUP_NAME))
			for(String key : algorithmGroupOrigin.keySet()){
				if(algorithmGroupOrigin.get(key) != 0)
					list.add(key);
			}
		else
			list.add(algorithmGroupID);
		
		list.remove(ALL_METRICS_GROUP_NAME);
		list.remove(ALL_CUSTOM_GROUP_NAME);
		return list;
	}
	
	// Find all metrics in a file that are not part of any group
	private List<String> getUngroupedMetricsFromFile(int fileID){
		ArrayList<String> list = new ArrayList<String>();
		
		// First add all metrics of the file. Then loop over all groups of that file
		// and remove those entries from the list that are found in the groups
		for(String key : variableOrigin.keySet()){
			if(variableOrigin.get(key) == fileID)
				list.add(key);
		}
		
		for(String key : algorithmOrigin.keySet()){
			if(algorithmOrigin.get(key) == fileID)
				list.add(key);
		}
		
		for(String key : algorithmGroupsMap.keySet()){
			// Ignore those two
			if(key.equals(ALL_METRICS_GROUP_NAME) || key.equals(ALL_CUSTOM_GROUP_NAME))
				continue;
			if(algorithmGroupOrigin.get(key) == fileID){
				List<String> children = getAlgorithmIDsFromGroup(key);
				for(String childKey : children)
					list.remove(childKey);
			}
		}
		
		return list;
	}

	public List<String> getAlgorithmIDsFromGroup(String algorithmGroupID) {
		HashMap<Integer, String> algorithmGroupMemberIDOrder = new HashMap<Integer, String>();
		
		if (!algorithmGroupsMap.containsKey(algorithmGroupID))
			return null;
		
		// Get the member of the group (that might still be unsorted/unordered)
		AlgorithmID[] ids = algorithmGroupsMap.get(algorithmGroupID).getAlgorithmIDArray();

		for (int i = 0; i < ids.length; i++) {
			// We expect to always have an order for elements in groups
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
	
	public void setAlgorithmGroupDescription(String algorithmGroupID, String algorithmGroupDescription) {
		if (algorithmGroupsMap.containsKey(algorithmGroupID)){
			AlgorithmGroup[] groups = confMap.get(algorithmGroupOrigin.get(algorithmGroupID)).getConfiguration().getAlgorithmGroups().getAlgorithmGroupArray();
			for(AlgorithmGroup group : groups)
				if(group.getID().equals(algorithmGroupID)) {		
					group.setDescription(algorithmGroupDescription);
					break;
				}
		}
	}
	
	// Checks if a metrics belonging to the given metricsID is part of the metrics
	// shipped with WoPeD or whether it was loaded from a custom metrics file.
	public boolean isCustomMetric(String metricsID) {
		if(variablesMap.containsKey(metricsID) == true)
			// fielID 0 = internal WoPeD metrics -> not custom -> return false
			if(variableOrigin.containsKey(metricsID) && variableOrigin.get(metricsID) != 0)
				return true;
			else
				return false;
		else if(algorithmsMap.containsKey(metricsID) == true)
			if(algorithmOrigin.containsKey(metricsID) && algorithmOrigin.get(metricsID) != 0)
				return true;
			else
				return false;
		else if(algorithmGroupsMap.containsKey(metricsID) == true)
			if(algorithmGroupOrigin.containsKey(metricsID) && algorithmGroupOrigin.get(metricsID) != 0)
				return true;
			else return false;

		return false;
	}

	public void setMetricThresholdLowValue(String metricID, int thresholdID, double lowValue) {
		if(isAlgorithm(metricID) && algorithmsMap.containsKey(metricID))
			algorithmsMap.get(metricID).getThresholds().getThresholdArray(thresholdID).getRange().setLow(lowValue);	
	}

	public void setMetricThresholdHighValue(String metricID, int thresholdID, double highValue) {
		if(isAlgorithm(metricID) && algorithmsMap.containsKey(metricID))
			algorithmsMap.get(metricID).getThresholds().getThresholdArray(thresholdID).getRange().setHigh(highValue);	;
	}

	public void setMetricThreholdState(String metricID, int thresholdID, java.lang.Enum<?> metricState) {
		if(isAlgorithm(metricID) && algorithmsMap.containsKey(metricID))
			algorithmsMap.get(metricID).getThresholds().getThresholdArray(thresholdID).xsetState((ThresholdState) metricState);
	}
	
	/**
	 * Check if the passed metric ID is already in use
	 * @return true when metric ID is already used
	 */
	public boolean isMetricIDInUse(String metricID) {
		if(variablesMap.containsKey(metricID) || algorithmsMap.containsKey(metricID))
			return true;
		else
			return false;
	}

	/**
	 * Check is a metric is a variable
	 * @return true when metric a variable
	 */
	public boolean isVariable(String metricID) {
		if(variablesMap.get(metricID) != null)
			return true;
		else
			return false;
	}

	/**
	 * Check is a metric is an algorithm
	 * @return true when metric an algorithm
	 */
	public boolean isAlgorithm(String metricID) {
		if(algorithmsMap.get(metricID) != null)
			return true;
		else
			return false;
	}

	/**
	 * Retrieve the highlighting formula of a metric if there is any
	 * @param metricID ID of the metric
	 */
	public String getHighlightingFormula(String metricID) {
		if(isVariable(metricID)){
			if(variablesMap.get(metricID).isSetHighlightingFormula())
				return variablesMap.get(metricID).getHighlightingFormula();
			else
				return "";
		} else if(isAlgorithm(metricID)){
			if(algorithmsMap.get(metricID).isSetHighlightingFormula())
				return algorithmsMap.get(metricID).getHighlightingFormula();
			else
				return "";
		} else
			return "";	
	}

	/**
	 * Get the file path from which this metric was loaded from
	 * @param metricID ID of the metric
	 */
	public String getMetricOrigin(String metricID) {
		if(isVariable(metricID))
			return fileMap.get(variableOrigin.get(metricID));
		else if(isAlgorithm(metricID))
			return fileMap.get(algorithmOrigin.get(metricID));
		else
			return "";	
	}
	
	/**
	 * Starts a new edit session = create an internal copy of the current XML state
	 * so that we can go back if editing is canceled. Once the editing changes are acknowledged,
	 * the internal backup state gets overwritten and the new state is saved.
	 */
	public void startEditSession() {
		confMapBackup.clear();
				
		for(int i : confMap.keySet()) {
			confMapBackup.put(i, (ConfigurationDocument) confMap.get(i).copy());
		}
	}
	
	/**
	 * Ends the current edit session = either drop the changes or keep them, 
	 * depending on the passed parameter
	 * @param keepChanges indicator if the changes of the last edit session are to be kept or dropped
	 */
	public void endEditSession(boolean keepChanges) {
		if(keepChanges){
			for(int i : confMap.keySet()) {
				saveConfig(i);
			}
		} else
			for(int i : confMapBackup.keySet()) {
				confMap.put(i, (ConfigurationDocument) confMapBackup.get(i).copy());
			}
		
		confMapBackup.clear();
	}

	/**
	 * Find the matching fileID to a given file name.
	 * @return file ID matching the given file name or -1 if no match was found
	 */
	public int findFileIDToFileName(String fileName) {
		for(int i : fileMap.keySet()) {
			if(fileMap.get(i).equals(fileName))
				return i;
		}
		return -1;
	}

	/**
	 * Add a new metric file to our internal lists.
	 */
	public void addNewMetricFile(String filePath) {
		if(!fileMap.containsValue(filePath))
			fileMap.put(fileMap.size(), filePath);
		
		ConfigurationDocument c = ConfigurationDocument.Factory.newInstance();
		c.addNewConfiguration();
		confMap.put(fileMap.size() - 1, c);
	}
}