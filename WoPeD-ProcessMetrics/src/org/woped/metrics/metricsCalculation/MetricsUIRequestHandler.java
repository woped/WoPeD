package org.woped.metrics.metricsCalculation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.config.IMetricsConfiguration;
import org.woped.core.controller.IEditor;
import org.woped.core.model.ModelElementContainer;
import org.woped.metrics.exceptions.CalculateFormulaException;
import org.woped.metrics.exceptions.NaNException;
import org.woped.metrics.helpers.LabeledFileFilter;
import org.woped.metrics.metricsCalculation.UITypes.UIMetricsGroup;
import org.woped.metrics.metricsCalculation.UITypes.UIThreshold;
import org.woped.gui.translations.Messages;

/**
 * Please note:
 * To ensure a strict MVC model, all requests from the UI and all information to the UI uses display strings rather than the real metrics or group IDs.
 * Therefore conversion is used within this class' method wherever needed.
 * However, real IDs may always be used - the conversion will automatically ignore those
 */
public class MetricsUIRequestHandler {	
	private HashMap<String, IMetricsConfiguration.MetricThresholdState> valueStates = null;
	private HashMap<String, String> errorMessages = new HashMap<String, String>();
	private IEditor editor;
	private MetricsCalculator mc;
	
	public MetricsUIRequestHandler(IEditor editor){
		this.editor = editor;
	}
		
	/**
	 * Takes the request to calculate all metrics within a group and returns the results
	 * 
	 * @param topGroupName	Name of the top group, containing further groups
	 * @return	Content of the top group and all its subgroups (Calculation results)
	 */
	public List<UIMetricsGroup> calculateMetrics(String topGroupName){
		if(topGroupName == null) return new ArrayList<UIMetricsGroup>();
		IMetricsConfiguration metricsConfig = ConfigurationManager.getMetricsConfiguration();
		mc = new MetricsCalculator(editor);
		valueStates = new HashMap<String, IMetricsConfiguration.MetricThresholdState>();
		
		List<String> groupnames = metricsConfig.getGroupIDsFromGroup(algorithmGroupNameToID(topGroupName));
		List<UIMetricsGroup> answerList = new ArrayList<UIMetricsGroup>();
		for(String algorithmGroupName:groupnames){
			List<StringPair> resultList = new ArrayList<StringPair>();
			
			for(String id : metricsConfig.getAlgorithmIDsFromGroup(algorithmGroupName))
				{
					StringPair result = calculateSingle(metricsConfig, id);
					if(result == null) continue;
					resultList.add(result);
				}
			if(resultList.size()>0)
				answerList.add(new UIMetricsGroup(metricsConfig.getAlgorithmGroupName(algorithmGroupName),resultList));
		}
		return answerList;
	}
	
	/**
	 * Calculates a single variable rather than a whole group
	 * 
	 * @param metricsConfig	Metrics config to be used
	 * @param iid			ID of the variable (Real or UI)
	 * @return				Name and result of the calculation
	 */
	public StringPair calculateSingle(IMetricsConfiguration metricsConfig, String iid){
		if(iid == null) return null;
		if(mc == null) mc = new MetricsCalculator(editor);
		if(valueStates == null) valueStates = new HashMap<String, IMetricsConfiguration.MetricThresholdState>();
		String id = algorithmNameToID(iid);
		try {
			double value = mc.calculate(id);
			int thresholds = metricsConfig.getMetricThresholdCount(id);
			valueStates.put(id, IMetricsConfiguration.MetricThresholdState.GRAY);
			for(int i=0;i<thresholds;i++)
				if(metricsConfig.getMetricThresholdLowValue(id, i) <= value
						&& metricsConfig.getMetricThresholdHighValue(id, i) >= value)
					    	valueStates.put(id, metricsConfig.getMetricThresholdState(id, i));
			return new StringPair(metricsConfig.getAlgorithmName(id) != null ? 
						metricsConfig.getAlgorithmName(id) : metricsConfig.getVariableName(id)
						, round(value, metricsConfig.getVariableIDs().contains(id)? 
						ConfigurationManager.getConfiguration().getVariableDecimalPlaces() : 
						ConfigurationManager.getConfiguration().getAlgorithmDecimalPlaces()));
		} catch (CalculateFormulaException e) {
			valueStates.put(id, IMetricsConfiguration.MetricThresholdState.GRAY);
			errorMessages.put(id, e.getLocalizedMessage());
			return new StringPair(metricsConfig.getAlgorithmName(id) != null ? 
					metricsConfig.getAlgorithmName(id) : metricsConfig.getVariableName(id), CalculateFormulaException.getShortError());

		} catch(Exception e){ e.printStackTrace(); return null; }
	}
	
	/**
	 * Returns the algorithms contained in a group
	 * 
	 * @param algorithmGroupID	ID of the group
	 * @return					List of all algorithms contained in it
	 */
	public static List<String> getAlgoNames(String algorithmGroupID){
		List<String> algoNames = new ArrayList<String>();
		IMetricsConfiguration metricsConfig = ConfigurationManager.getMetricsConfiguration();
		for(String id:metricsConfig.getAlgorithmIDsFromGroup(algorithmGroupID))
			algoNames.add(metricsConfig.getAlgorithmName(id) != null ? metricsConfig.getAlgorithmName(id) : metricsConfig.getVariableName(id));
		return algoNames;
	}
	
	/**
	 * Returns all algorithms names, even if the group contains subgroups (and only then!)
	 * 
	 * @param algorithmGroupID	Name of the top layer gorup
	 * @return					List of IDs of all algorithms on the lowest level
	 */
	public static List<String> getLayeredAlgoNames(String algorithmGroupID){
		List<String> algoNames = new ArrayList<String>();
		IMetricsConfiguration metricsConfig = ConfigurationManager.getMetricsConfiguration();
		for(String groupId:metricsConfig.getGroupIDsFromGroup(algorithmGroupID))
			for(String id:metricsConfig.getAlgorithmIDsFromGroup(groupId))
				algoNames.add(metricsConfig.getAlgorithmName(id) != null ? metricsConfig.getAlgorithmName(id) : metricsConfig.getVariableName(id));
		return algoNames;
	}
	
	/**
	 * Rounds a number to x places
	 * 
	 * @param number	Number to be rounded
	 * @param places	Places after the separator
	 * @return			Rounded number as a string
	 * @throws NaNException	Gets thrown if the input string is no number at all
	 */
	private static String round(double number, int places) throws NaNException{
		if(Double.isNaN(number)) throw new NaNException();
		else if(Double.isInfinite(number)) throw new NaNException(); //return ""+number;
		if(places == 0)
			return (int)Math.round(number)+"";
		String s = ((Math.round(number*Math.pow(10, places))))/Math.pow(10, places)+"";
		if(s.contains(".") && s.indexOf(".")+places+1 < s.length())
			s = s.substring(0, s.indexOf(".")+1+places);
		return s;
	}

	/**
	 * Gets the algorithm group ID based on an algorithm group name
	 * 
	 * @param name	Name of the algorithm group
	 * @return		ID of the algorithm group
	 */
	private static String algorithmGroupNameToID(String name){
		List<String> groups = ConfigurationManager.getMetricsConfiguration().getAlgorithmGroupIDs();
		for(String s:groups)
			if(ConfigurationManager.getMetricsConfiguration().getAlgorithmGroupName(s).equals(name))
				return s;
		return name;
	}
	
	/**
	 * Gets the algorithm ID based on an algorithm name
	 * 
	 * @param name	Name of the algorithm
	 * @return		ID of the algorithm
	 */
	public static String algorithmNameToID(String name){
		Set<String> groups = ConfigurationManager.getMetricsConfiguration().getAlgorithmIDs();
		for(String s:groups)
			if(ConfigurationManager.getMetricsConfiguration().getAlgorithmName(s).equals(name))
				return s;
		groups = ConfigurationManager.getMetricsConfiguration().getVariableIDs();
		for(String s:groups)
			if(ConfigurationManager.getMetricsConfiguration().getVariableName(s).equals(name))
				return s;
		return name;
	}
	
	public List<String> getMetricGroups(){
		List<String> groups = ConfigurationManager.getMetricsConfiguration().getAlgorithmGroupIDs();
		List<String> groupNames = new ArrayList<String>();
		for(String s:groups)groupNames.add(ConfigurationManager.getMetricsConfiguration().getAlgorithmGroupName(s));
		return groupNames;
	}
	
	public String getDescription(String ID){
		String id = algorithmNameToID(ID);
		String descrip = ConfigurationManager.getMetricsConfiguration().getAlgorithmDescription(id);
		if(descrip == null)
			return ConfigurationManager.getMetricsConfiguration().getVariableDescription(id);
		return descrip;
	}
	
	public String getError(String ID){
		String id = algorithmNameToID(ID);
		if(errorMessages.containsKey(id))
			return errorMessages.get(id);
		return null;
	}
	
	public void setHighlight(String metricsID){
		removeHighlights();
		if(metricsID == null) 
			return;
		ModelElementContainer mec = editor.getModelProcessor().getElementContainer();
		String id = algorithmNameToID(metricsID);
		MetricHighlighting high = mc.getHighlighter().getHighlighting(id);
		Set<String> nodeIDs = high.getNodeIDs();
		
		for(String nodeID:nodeIDs)
			mec.getElementById(nodeID).setHighlighted(true);
		
		Set<String> arcIDs = high.getArcIDs();
		for(String arcID:arcIDs)
			mec.getArcById(arcID).setHighlighted(true);
	}
	
	public void removeHighlights(){
		ModelElementContainer mec = editor.getModelProcessor().getElementContainer();
		mec.removeAllHighlighting();
	}
	
	public boolean hasHighlight(String metricsID){
		return mc.getHighlighter().getHighlighting(algorithmNameToID(metricsID))!= null;
	}
	
	public IMetricsConfiguration.MetricThresholdState getTreshholdState(String metricsID){
		return valueStates.containsKey(algorithmNameToID(metricsID)) ? valueStates.get(algorithmNameToID(metricsID)) : IMetricsConfiguration.MetricThresholdState.NONE;
	}
	
	public static List<UIThreshold> getUIThresholds(String metricsID){
		IMetricsConfiguration metricsConfig = ConfigurationManager.getMetricsConfiguration();
		String id = algorithmNameToID(metricsID);
		int thresholds = metricsConfig.getMetricThresholdCount(id);
		List<UIThreshold> thresholdList = new ArrayList<UIThreshold>();
		for(int i=0;i<thresholds;i++)
			thresholdList.add(new UIThreshold(metricsConfig.getMetricThresholdLowValue(id, i),
					metricsConfig.getMetricThresholdHighValue(id, i), metricsConfig.getMetricThresholdState(id, i)));
		return thresholdList;
	}
	
	/**
	 * Exports the results of a metrics calculation to a .csv or .txt file
	 * 
	 * @param groups	Groups to be exported
	 * @return			Whether the export was successful
	 */
	public boolean exportMetricsResults(List<UIMetricsGroup> groups) {
		// If no user export directory is existing yet, create it
		File defaultExportDir = new File(ConfigurationManager.getConfiguration()
				.getUserdir() + "exports");
		if (!defaultExportDir.exists())
			defaultExportDir.mkdirs();

		JFileChooser fileChooser = new JFileChooser(defaultExportDir);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		FileFilter filter = new LabeledFileFilter() {
			public boolean accept(File file) {
				if (file.isDirectory() || file.getAbsolutePath().endsWith(".txt"))
					return true;
				return false;
			}

			public String getDescription() {
				return "Text (*.txt)";
			}
			
			public String getExtension() {
				return ".txt";
			}
			
		};
		fileChooser.addChoosableFileFilter(filter);
		
		filter = new LabeledFileFilter() {
			public boolean accept(File file) {
				if (file.isDirectory() || file.getAbsolutePath().endsWith(".csv"))
					return true;
				return false;
			}

			public String getDescription() {
				return "Comma Separated Values (*.csv)";
			}

			public String getExtension() {
				return ".csv";
			}
		};
		fileChooser.addChoosableFileFilter(filter);	
			
		File exportFile = null;
		boolean valid_selection = false;
		boolean cancel = false;
		while (!valid_selection && !cancel) {
			int returnVal = fileChooser.showSaveDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {		
				exportFile = new File(fileChooser.getSelectedFile().getAbsolutePath());
				if(!exportFile.getName().endsWith(((LabeledFileFilter)fileChooser.getFileFilter()).getExtension()))
						exportFile = new File(exportFile.getAbsolutePath()+((LabeledFileFilter)fileChooser.getFileFilter()).getExtension());
				// File already exists -> ask for overwrite
				if (exportFile.exists()) {
					int option = JOptionPane.showConfirmDialog(null,
							Messages.getString("File.Warning.Overwrite.Text"), Messages.getString("File.Warning.Overwrite.Title"),
							JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE);
					
					if (option == JOptionPane.YES_OPTION)
						valid_selection = true;
				}
				else
					valid_selection = true;
			}
			else if (returnVal == JFileChooser.CANCEL_OPTION)
				cancel = true;
		}

		try {
			BufferedWriter bw = new BufferedWriter(new PrintWriter(exportFile));
			for(UIMetricsGroup group : groups){
				bw.write(group.getName() + "\r\n");
				for (StringPair sp : group.getValues()) {
					bw.write(sp.getKey() + ", ");
					bw.write(sp.getValue());
					if (fileChooser.getSelectedFile().getName().endsWith("csv"))
						bw.write(";");
					bw.write("\r\n");
				}
			}
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * Returns whether or not a variable or algorithm can be edited (if its a core one, it can't)
	 * 
	 * @param metricsID		Name or ID of the variable or algorithm
	 * @return				Whether or not the variable / algorithm can be edited
	 */
	public static boolean canBeEdited(String metricsID){
		String id = algorithmNameToID(metricsID);
		IMetricsConfiguration metricsConfig = ConfigurationManager.getMetricsConfiguration();
		return metricsConfig.isCustomMetric(id);
	}
}