/**
 * ReachabilityGraph implementation was done by Manuel Fladt and Benjamin Geiger.
 * The code was written for a project at BA Karlsruhe in 2007/2008 under authority
 * of Prof. Dr. Thomas Freytag and Andreas Eckleder.
 *
 * This class was written by
 * @author Benjamin Geiger
 */

package org.woped.qualanalysis.reachabilitygraph.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.IEditor;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.utilities.FileFilterImpl;
import org.woped.core.utilities.LoggerManager;
import org.woped.core.utilities.Utils;
import org.woped.qualanalysis.Constants;
import org.woped.qualanalysis.ImageExport;
import org.woped.qualanalysis.reachabilitygraph.controller.SimulationRunningException;
import org.woped.qualanalysis.reachabilitygraph.data.Marking;
import org.woped.qualanalysis.reachabilitygraph.data.ReachabilityEdgeModel;
import org.woped.qualanalysis.reachabilitygraph.data.ReachabilityGraphModel;
import org.woped.qualanalysis.reachabilitygraph.data.ReachabilityPlaceModel;
import org.woped.translations.Messages;

public class ReachabilityGraphPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private IEditor editor = null;

	// Panels
	private JScrollPane rgp_topPanel = null; // top SplitPane

	// jGraph related
	private ReachabilityJGraph rgp_jgraph = null; // the jGraph
	private String logicalFingerprint = null; // PetrinetIdentifier
	private String legendById = null;
	private String legendByName = null;

	// Labels
	private JLabel bottomInfo = null;
	private JLabel outOfSyncInfo = null;
	private JLabel legendInfo = null;
	private JButton legendToggleButton = null;
	private JButton refreshButton = null;
	private JButton settingsButton = null;
	private JComboBox layout = null;

	// Helper
	private boolean legendToggle = false;

	public ReachabilityGraphPanel(IEditor editor) {
		super();
		this.editor = editor;
		this.logicalFingerprint = ((PetriNetModelProcessor)editor.getModelProcessor()).getLogicalFingerprint();
		init();
	}

	private void init() {
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-> init() " + this.getClass().getName());
		this.setLayout(new BorderLayout());
		// NORTH Components
		refreshButton = new JButton(Messages.getString("QuanlAna.ReachabilityGraph.RefreshButton"));
        refreshButton.addActionListener(new RefreshGraphButtonListener(this));
        refreshButton.setEnabled(false);
        settingsButton = new JButton("Settings");
        settingsButton.addActionListener(new SettingsButtonListener(this));
        JLabel layout_text = new JLabel(Messages.getString("QuanlAna.ReachabilityGraph.Layout"));
        layout = new JComboBox();
        layout.addItemListener(new LayoutBoxItemListener(this));
        layout.addItem(Messages.getString("QuanlAna.ReachabilityGraph.Hierarchic"));
        layout.addItem(Messages.getString("QuanlAna.ReachabilityGraph.Circle"));
        JButton export = new JButton(Messages.getString("QuanlAna.ReachabilityGraph.ExportAsButton"));
        export.addActionListener(new ExportGraphButtonListener(this));
        JPanel northSubPanel = new JPanel();
        northSubPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        northSubPanel.add(layout_text);
        northSubPanel.add(layout);
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));
        northPanel.add(refreshButton);
        northPanel.add(northSubPanel);
        northPanel.add(settingsButton);
        northPanel.add(export);
        this.add(BorderLayout.NORTH, northPanel);
        // SOUTH Components
        legendInfo = new JLabel(Messages.getString("QuanlAna.ReachabilityGraph.Legend") + ": ()");
        legendToggleButton = new JButton(Messages.getImageIcon("Action.Browser.Refresh"));
        legendToggleButton.setToolTipText(Messages.getString("QuanlAna.ReachabilityGraph.Legend.Toggle"));
        legendToggleButton.addActionListener(new LegendListener(this));
        legendToggleButton.setBorder(BorderFactory.createEmptyBorder());
        legendToggleButton.setFocusPainted(false);
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        this.add(BorderLayout.SOUTH, southPanel);
        southPanel.add(legendInfo);
        southPanel.add(legendToggleButton);
        southPanel.add(bottomInfo = new JLabel(""));
        southPanel.add(outOfSyncInfo = new JLabel(Messages.getImageIcon("Analysis.Tree.Warning")));
        outOfSyncInfo.setToolTipText(Messages.getString("QuanlAna.ReachabilityGraph.GraphOutOfSync"));
		rgp_jgraph = new ReachabilityJGraph();
		rgp_topPanel = new JScrollPane();
		this.add(BorderLayout.CENTER, rgp_topPanel);
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- init() " + this.getClass().getName());
	}

	/**
	 * returns the editor for this instance.
	 * @return
	 */
	public IEditor getEditor(){
		return editor;
	}

	/**
	 * returns the selected layout type.
	 * @return
	 */
	protected int getSelectedType(){
		return this.layout.getSelectedIndex();
	}

	/**
	 * layout a graph with given layout type. Layouting can be done without to recmpute the graph.
	 * In some cases it's needed to recompute e.g. the petri net has changed.
	 * @param type
	 * @param computeNew
	 */
	public void layoutGraph(int type, boolean computeNew) throws SimulationRunningException {
		if(rgp_topPanel != null){
			if(computeNew){
				if(editor.isTokenGameEnabled()){
					this.refreshButton.setEnabled(true);
					throw new SimulationRunningException();
				} else {
					this.remove(rgp_topPanel);
					this.add(rgp_topPanel = new JScrollPane(this.rgp_jgraph = this.getDefaultGraph(type)));
					this.updateVisibility();
				}
			} else {
				ReachabilityGraphModel.layoutGraph(this.rgp_jgraph, type, this.getSize());
			}
			this.validate();
		}
	}
	/**
	 * is used to get a new computed {@link ReachabilityJGraph} instance. Layout in a given type.
	 * @param type
	 * @return
	 */
	private ReachabilityJGraph getDefaultGraph(int type){
		HashMap<String, String> old_attributes = null;
		if(rgp_jgraph != null){
			old_attributes = rgp_jgraph.getAttributeMap();
		}
		ReachabilityGraphModel builder = new ReachabilityGraphModel(editor);
		ReachabilityJGraph new_graph = builder.getGraph();
		if(old_attributes != null){
			new_graph.setAttributeMap(old_attributes);
		}
		return ReachabilityGraphModel.layoutGraph(new_graph, type, this.getSize());
	}

	/**
	 * returns {@link ReachabilityJGraph} instance of this panel.
	 * @return
	 */
	public ReachabilityJGraph getGraph(){
		return this.rgp_jgraph;
	}

	/**
	 * updated all graph relevant objects after the graph was changed.
	 */
	public void updateVisibility(){
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-> updateVisibility() " + this.getClass().getName());
		// is RG in sync ? And Token game not running...
		if(((PetriNetModelProcessor)editor.getModelProcessor()).getLogicalFingerprint().equals(this.logicalFingerprint) && !editor.isTokenGameEnabled()){
			if(this.rgp_jgraph.getRoots().length == 0){
				refreshButton.setEnabled(true);
				setGraphOutOfSync(true);
			} else {
				refreshButton.setEnabled(false);
				setGraphOutOfSync(false);
			}
		} else {
			refreshButton.setEnabled(true);
			setGraphOutOfSync(true);
		}
		bottomInfo.setText(this.getGraphInfo());
		legendInfo.setText(this.getLegend());
	}

	/**
	 * sets legendByName. It's used for showing the legend with names or id's.
	 * @param legend
	 */
	protected void setLegendByName(boolean legend){
		this.legendToggle = legend;
	}

	/**
	 * returns the legendByName toggle attribute.
	 * @return
	 */
	protected boolean getLegendByName(){
		return this.legendToggle;
	}

	/**
	 * sets the graph out of sync label.
	 * @param outOfSync
	 */
	protected void setGraphOutOfSync(boolean outOfSync){
		this.outOfSyncInfo.setVisible(outOfSync);
	}
	/**
	 * returns the Legend as String.
	 * @return
	 */
	private String getLegend(){
		// check if legend is still up to date with the petri-net.
		if(((PetriNetModelProcessor)editor.getModelProcessor()).getLogicalFingerprint().equals(this.logicalFingerprint)){
			Object[] roots = this.rgp_jgraph.getRoots();
			// iterate over all cells in graph
			for(int i = 0; i < roots.length; i++){
				// found a PlaceModel ?
				if(roots[i] instanceof ReachabilityPlaceModel){
					ReachabilityPlaceModel place = (ReachabilityPlaceModel) roots[i];
					// get the Marking - every Marking has all places with it.
					Marking marking = (Marking)place.getUserObject();
					LinkedList<PlaceModel> placeModels = marking.getKeySet();
					String legendByName_temp = "";
					String legendById_temp = "";
					// build legend string.
					for(int placeCounter = 0; placeCounter < placeModels.size(); placeCounter++){
						legendByName_temp += placeModels.get(placeCounter).getNameValue() + ",";
						legendById_temp += placeModels.get(placeCounter).getId() + ",";
					}
					// remove last comma if there is one
					if(legendById_temp.length() > 1 && legendByName_temp.length() > 1){
						legendByName_temp = legendByName_temp.substring(0, legendByName_temp.length()-1);
						legendById_temp = legendById_temp.substring(0,legendById_temp.length()-1);
					}
					// return the legend
					this.legendByName = Messages.getString("QuanlAna.ReachabilityGraph.Legend") + ": (" + legendByName_temp + ")";
					this.legendById = Messages.getString("QuanlAna.ReachabilityGraph.Legend") + ": (" + legendById_temp + ")";
					if(legendToggle){
						return this.legendByName;
					} else {
						return this.legendById;
					}
				}
			}
			// no PlaceModel was found, so return empty legend.
			this.legendByName = Messages.getString("QuanlAna.ReachabilityGraph.Legend") + ": ()";
			this.legendById = Messages.getString("QuanlAna.ReachabilityGraph.Legend") + ": ()";
			return this.legendById;
		} else { // still up to date, so return the legend.
			if(legendToggle){
				return this.legendByName;
			} else {
				return this.legendById;
			}
		}
	}

	/**
	 * returns a string with information on the graph
	 * @return
	 */
	private String getGraphInfo(){
		Object[] roots = this.rgp_jgraph.getRoots();
		int vertices = 0;
		int edges = 0;
		// count vertices and edges of the graph
		for(int i = 0; i < roots.length; i++){
			if(roots[i] instanceof ReachabilityEdgeModel){
				edges++;
			}
			if(roots[i] instanceof ReachabilityPlaceModel){
				vertices++;
			}
		}
		return Messages.getString("QuanlAna.ReachabilityGraph.Vertices") + " " + vertices + " " +
			Messages.getString("QuanlAna.ReachabilityGraph.Edges") + " " + edges;
	}
	/**
	 * set the logicalFingerprint of actual petrinet. LogicalFingerprint is used to track changes of the petrinet.
	 * @param logicalFingerprint
	 */
	protected void setLogicalFingerPrint(String logicalFingerprint) {
		this.logicalFingerprint = logicalFingerprint;
	}
	/**
	 * sets the refresh button to be clickable - or not.
	 * @param b
	 */
	protected void setRefreshButtonEnabled(boolean b){
		this.refreshButton.setEnabled(b);
	}

	/**
	 * enables parallel rounting for the RG or not.
	 * ParallelRouting is very slow on big RG's.
	 * @param enabled
	 */
	protected void setParallelRouting(boolean enabled){
		ReachabilityGraphModel.setParallelRouting(rgp_jgraph, enabled);
	}

	/**
	 * enables a gray-scale view of the graph.
	 * @param enabled
	 */
	protected void setGrayScale(boolean enabled){
		ReachabilityGraphModel.setGrayScale(rgp_jgraph, enabled);
	}
}

	class ExportGraphButtonListener implements ActionListener {
		ReachabilityGraphPanel rgp = null;

		public ExportGraphButtonListener(ReachabilityGraphPanel rgp){
			this.rgp = rgp;
		}

		public void actionPerformed(ActionEvent arg0) {
			rgp.getGraph();

			int filetype = 0;
			String filepath = null;

			JFileChooser jfc;
			if (ConfigurationManager.getConfiguration().getHomedir() != null) {
				jfc = new JFileChooser(new File(ConfigurationManager
						.getConfiguration().getHomedir()));
			} else {
				jfc = new JFileChooser();
			}

			// FileFilters
			Vector<String> pngExtensions = new Vector<String>();
			pngExtensions.add("png");
			FileFilterImpl PNGFilter = new FileFilterImpl(
					FileFilterImpl.PNGFilter, "PNG (*.png)", pngExtensions);
			jfc.setFileFilter(PNGFilter);

			Vector<String> bmpExtensions = new Vector<String>();
			bmpExtensions.add("bmp");
			FileFilterImpl BMPFilter = new FileFilterImpl(
					FileFilterImpl.BMPFilter, "BMP (*.bmp)", bmpExtensions);
			jfc.setFileFilter(BMPFilter);

			Vector<String> jpgExtensions = new Vector<String>();
			jpgExtensions.add("jpg");
			jpgExtensions.add("jpeg");
			FileFilterImpl JPGFilter = new FileFilterImpl(
					FileFilterImpl.JPGFilter, "JPG (*.jpg)", jpgExtensions);
			jfc.setFileFilter(JPGFilter);

			jfc.setFileFilter(PNGFilter);

			jfc.setDialogTitle(Messages.getString("Action.Export.Title"));
			jfc.showSaveDialog(null);

			if (jfc.getSelectedFile() != null && rgp != null) {

				String savePath = jfc.getSelectedFile().getAbsolutePath()
						.substring(
								0,
								jfc.getSelectedFile().getAbsolutePath()
										.length()
										- jfc.getSelectedFile().getName()
												.length());
				if (((FileFilterImpl) jfc.getFileFilter())
						.getFilterType() == FileFilterImpl.JPGFilter) {
					savePath = savePath
							+ Utils.getQualifiedFileName(jfc.getSelectedFile()
									.getName(), jpgExtensions);
				} else if (((FileFilterImpl) jfc.getFileFilter())
						.getFilterType() == FileFilterImpl.PNGFilter) {
					savePath = savePath
							+ Utils.getQualifiedFileName(jfc.getSelectedFile()
									.getName(), pngExtensions);
				}else if (((FileFilterImpl) jfc.getFileFilter())
						.getFilterType() == FileFilterImpl.BMPFilter) {
					savePath = savePath
							+ Utils.getQualifiedFileName(jfc.getSelectedFile()
									.getName(), bmpExtensions);
				}else {
					LoggerManager.error(Constants.QUALANALYSIS_LOGGER,
							"\"Export\" NOT SUPPORTED FILE TYPE.");
				}
				filetype = ((FileFilterImpl) jfc.getFileFilter()).getFilterType();
				filepath = savePath;
			}

			if (filetype == FileFilterImpl.JPGFilter) {
				ImageExport.saveJPG(ImageExport
						.getRenderedImage(rgp), new File(filepath));
			} else if (filetype == FileFilterImpl.PNGFilter) {
				ImageExport.savePNG(ImageExport
						.getRenderedImage(rgp), new File(filepath));
			} else if (filetype == FileFilterImpl.BMPFilter) {
				ImageExport.saveBMP(ImageExport
						.getRenderedImage(rgp), new File(filepath));
			}else {
				LoggerManager.warn(Constants.QUALANALYSIS_LOGGER,
						"Unable to save File. Filetype not known: "
								+ filetype);
			}
		}
	}

	class LegendListener implements ActionListener{

		ReachabilityGraphPanel rgp = null;

		public LegendListener(ReachabilityGraphPanel rgp){
			this.rgp = rgp;
		}

		public void actionPerformed(ActionEvent e) {
			if(rgp.getLegendByName()){
				rgp.setLegendByName(false);
			} else {
				rgp.setLegendByName(true);
			}
			rgp.updateVisibility();
		}
	}

	class LayoutBoxItemListener implements ItemListener {

		ReachabilityGraphPanel rgp = null;

		public LayoutBoxItemListener(ReachabilityGraphPanel rgp){
			this.rgp = rgp;
		}

		public void itemStateChanged(ItemEvent e) {
			JComboBox selectedChoice = (JComboBox) e.getSource();
	        try {
				rgp.layoutGraph(selectedChoice.getSelectedIndex(), false);
			} catch (SimulationRunningException e1) {
				ReachabilityWarning.showSimulationRunningWarning(this.rgp);
			}
		}
	}

	class RefreshGraphButtonListener implements ActionListener {

		ReachabilityGraphPanel rgp = null;

		public RefreshGraphButtonListener(ReachabilityGraphPanel rgp){
			this.rgp = rgp;
		}

		public void actionPerformed(ActionEvent arg0) {
			try {
				rgp.layoutGraph(rgp.getSelectedType(), true);
				rgp.setLogicalFingerPrint(((PetriNetModelProcessor)rgp.getEditor().getModelProcessor()).getLogicalFingerprint());
				rgp.setRefreshButtonEnabled(false);
				rgp.setGraphOutOfSync(false);
				rgp.updateVisibility();
			} catch (SimulationRunningException e) {
				rgp.setRefreshButtonEnabled(true);
				ReachabilityWarning.showSimulationRunningWarning(this.rgp);
			}
		}
	}

	class SettingsButtonListener implements ActionListener {
		ReachabilityGraphPanel rgp = null;

		public SettingsButtonListener(ReachabilityGraphPanel rgp){
			this.rgp = rgp;
		}

		public void actionPerformed(ActionEvent e) {
			ReachabilitySettingsDialog dialog = new ReachabilitySettingsDialog(rgp);
			dialog.setVisible(true);
		}
	}