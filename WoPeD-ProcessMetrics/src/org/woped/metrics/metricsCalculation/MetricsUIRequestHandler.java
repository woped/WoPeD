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
import org.woped.metrics.metricsCalculation.UITypes.UIThreshold;
import org.woped.translations.Messages;

public class MetricsUIRequestHandler {

	private HashMap<String, IMetricsConfiguration.AlgorithmThresholdState> valueStates = null;
	private HashMap<String, String> errorMessages = new HashMap<String, String>();
	private IEditor editor;
	
	public MetricsUIRequestHandler(IEditor editor){
		this.editor = editor;
	}
		
	public List<StringPair> calculateMetrics(String algorithmGroupName){
		if(algorithmGroupName==null) return new ArrayList<StringPair>();
		IMetricsConfiguration metricsConfig = ConfigurationManager.getMetricsConfiguration();
		List<StringPair> resultList = new ArrayList<StringPair>();
		MetricsCalculator mc = new MetricsCalculator(editor);
		valueStates = new HashMap<String, IMetricsConfiguration.AlgorithmThresholdState>();
		
		for(String id : metricsConfig.getAlgorithmIDsFromGroup(algorithmGroupNameToID(algorithmGroupName)))
			try {
				double value = mc.calculate(id);
				int thresholds = metricsConfig.getAlgorithmThresholdCount(id);
				if(!metricsConfig.getVariableIDs().contains(id))valueStates.put(id, IMetricsConfiguration.AlgorithmThresholdState.GRAY);
				for(int i=0;i<thresholds;i++)
					if(metricsConfig.getAlgorithmThresholdLowValue(id, i) <= value
							&& metricsConfig.getAlgorithmThresholdHighValue(id, i) >= value)
						    if(!metricsConfig.getVariableIDs().contains(id))
						    	valueStates.put(id, metricsConfig.getAlgorithmThresholdState(id, i));
				resultList.add(new StringPair(metricsConfig.getAlgorithmName(id) != null ? 
							metricsConfig.getAlgorithmName(id) : metricsConfig.getVariableName(id)
							, round(value, metricsConfig.getVariableIDs().contains(id)? 
							ConfigurationManager.getConfiguration().getVariableDecimalPlaces() : 
							ConfigurationManager.getConfiguration().getAlgorithmDecimalPlaces())));
			} catch (CalculateFormulaException e) {
				if(!metricsConfig.getVariableIDs().contains(id))valueStates.put(id, IMetricsConfiguration.AlgorithmThresholdState.GRAY);
				resultList.add(new StringPair(metricsConfig.getAlgorithmName(id) != null ? 
						metricsConfig.getAlgorithmName(id) : metricsConfig.getVariableName(id), CalculateFormulaException.getShortError()));
				errorMessages.put(id, e.getLocalizedMessage());
			} catch(Exception e){}
		return resultList;
	}
	
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

	private static String algorithmGroupNameToID(String name){
		List<String> groups = ConfigurationManager.getMetricsConfiguration().getAlgorithmGroupIDs();
		for(String s:groups)
			if(ConfigurationManager.getMetricsConfiguration().getAlgorithmGroupName(s).equals(name))
				return s;
		return name;
	}
	
	private static String algorithmNameToID(String name){
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
		System.out.println(">> Gotcha!");
		removeHighlights();
		ModelElementContainer mec = editor.getModelProcessor().getElementContainer();
		String id = algorithmNameToID(metricsID);
		MetricHighlighting high = MetricsHighlighter.getHighlighting(id);
		Set<String> nodeIDs = high.getNodeIDs();
		
		for(String nodeID:nodeIDs)
			mec.getElementById(nodeID).setHighlighted(true);
	}
	
	public void removeHighlights(){
		ModelElementContainer mec = editor.getModelProcessor().getElementContainer();
		mec.removeAllHighlighting();
	}
	
	public boolean hasHighlight(String metricsID){
		return MetricsHighlighter.getHighlighting(algorithmNameToID(metricsID))!= null;
	}
	
	public IMetricsConfiguration.AlgorithmThresholdState getTreshholdState(String metricsID){
		return valueStates.containsKey(algorithmNameToID(metricsID)) ? valueStates.get(algorithmNameToID(metricsID)) : IMetricsConfiguration.AlgorithmThresholdState.NONE;
	}
	
	public List<UIThreshold> getUIThresholds(String metricsID){
		IMetricsConfiguration metricsConfig = ConfigurationManager.getMetricsConfiguration();
		String id = algorithmNameToID(metricsID);
		int thresholds = metricsConfig.getAlgorithmThresholdCount(id);
		List<UIThreshold> thresholdList = new ArrayList<UIThreshold>();
		for(int i=0;i<thresholds;i++)
			thresholdList.add(new UIThreshold(metricsConfig.getAlgorithmThresholdLowValue(id, i),
					metricsConfig.getAlgorithmThresholdHighValue(id, i), metricsConfig.getAlgorithmThresholdState(id, i)));
		return thresholdList;
	}
	
	public boolean exportMetricsResults(List<StringPair> metricsResults) {
		// If no user export directory is yet existing, create it
		File exportDir = new File(ConfigurationManager.getConfiguration()
				.getUserdir() + "exports");
		if (!exportDir.exists())
			exportDir.mkdirs();

		JFileChooser fileChooser = new JFileChooser(exportDir);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		FileFilter filter = new FileFilter() {
			public boolean accept(File file) {
				if (file.getAbsolutePath().endsWith(".cvs"))
					return true;
				return false;
			}

			public String getDescription() {
				return "Comma Separated Values (*.csv)";
			}
		};
		fileChooser.addChoosableFileFilter(filter);
		
		filter = new FileFilter() {
			public boolean accept(File file) {
				if (file.getAbsolutePath().endsWith(".txt"))
					return true;
				return false;
			}

			public String getDescription() {
				return "Text (*.txt)";
			}
		};
		fileChooser.addChoosableFileFilter(filter);
			
		File exportFile = null;
		boolean valid_selection = false;
		while (!valid_selection) {
			int returnVal = fileChooser.showSaveDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {		
				exportFile = new File(exportDir.getAbsolutePath() + File.separator + 
						fileChooser.getSelectedFile().getName());

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
				return true;
		}

		try {
			BufferedWriter bw = new BufferedWriter(new PrintWriter(exportFile));

			for (StringPair sp : metricsResults) {
				bw.write(sp.getKey() + ", ");
				bw.write(sp.getValue());
				if (fileChooser.getSelectedFile().getName().endsWith("csv"))
					bw.write(";");
				bw.write("\r\n");
			}

			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}