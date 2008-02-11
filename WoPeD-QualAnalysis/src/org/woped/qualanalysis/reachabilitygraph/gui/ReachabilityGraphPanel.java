package org.woped.qualanalysis.reachabilitygraph.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.jgraph.JGraph;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.IEditor;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.utilities.FileFilterImpl;
import org.woped.core.utilities.LoggerManager;
import org.woped.core.utilities.Utils;
import org.woped.qualanalysis.Constants;
import org.woped.qualanalysis.ImageExport;
import org.woped.qualanalysis.reachabilitygraph.data.Marking;
import org.woped.qualanalysis.reachabilitygraph.data.ReachabilityEdgeModel;
import org.woped.qualanalysis.reachabilitygraph.data.ReachabilityGraphModel;
import org.woped.qualanalysis.reachabilitygraph.data.ReachabilityPlaceModel;
import org.woped.translations.Messages;

import quicktime.streaming.SettingsDialog;

public class ReachabilityGraphPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private IEditor editor = null;

	// Panels
	private JScrollPane rgp_topPanel = null; // top SplitPane
	
	// jGraph related
	private ReachabilityJGraph rgp_jgraph = null; // the jGraph
	private String logicalFingerprint = null; // PetrinetIdentifier
	private String legend = null; 
	
	// Labels
	private JLabel bottomInfo = null;
	private JLabel legendInfo = null;
	private JButton refreshButton = null;
	private JButton settingsButton = null;
	private JComboBox layout = null;
	
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
        legendInfo = new JLabel();
        legendInfo.setText(Messages.getString("QuanlAna.ReachabilityGraph.Legend") + ": ()");
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        this.add(BorderLayout.SOUTH, southPanel);
        southPanel.add(legendInfo);
        southPanel.add(bottomInfo = new JLabel(""));
		rgp_jgraph = new ReachabilityJGraph();
		rgp_topPanel = new JScrollPane();
		this.add(BorderLayout.CENTER, rgp_topPanel);
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- init() " + this.getClass().getName());
	}
		
	public IEditor getEditor(){
		return editor;
	}
	
	protected int getSelectedType(){
		return this.layout.getSelectedIndex();
	}
	
	public void layoutGraph(int type, boolean computeNew){
		if(rgp_topPanel != null){
			if(computeNew){
				this.remove(rgp_topPanel);
				this.add(rgp_topPanel = new JScrollPane(this.rgp_jgraph = this.getDefaultGraph(type)));
				this.updateVisibility();
			} else {
				ReachabilityGraphModel.layoutGraph(this.rgp_jgraph, type, this.getSize());
			}
			this.validate();
		}
	}
	
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
	
	public ReachabilityJGraph getGraph(){
		return this.rgp_jgraph;
	}
	
	public void updateVisibility(){
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-> updateVisibility() " + this.getClass().getName());
		if(((PetriNetModelProcessor)editor.getModelProcessor()).getLogicalFingerprint().equals(this.logicalFingerprint)){
			refreshButton.setEnabled(false);
		} else {
			refreshButton.setEnabled(true);
		}
		bottomInfo.setText(this.getGraphInfo());
		legendInfo.setText(this.getLegend());
	}
	
	private String getLegend(){
		if(((PetriNetModelProcessor)editor.getModelProcessor()).getLogicalFingerprint().equals(this.logicalFingerprint)){
			Object[] roots = this.rgp_jgraph.getRoots();
			for(int i = 0; i < roots.length; i++){
				if(roots[i] instanceof ReachabilityPlaceModel){				
					ReachabilityPlaceModel place = (ReachabilityPlaceModel) roots[i];
					Marking marking = (Marking)place.getUserObject();
					LinkedList<PlaceModel> placeModels = marking.getKeySet();
					String legend = "";
					for(int placeCounter = 0; placeCounter < placeModels.size(); placeCounter++){
						legend += placeModels.get(placeCounter).getId() + ",";
					}
					legend = legend.substring(0, legend.length()-1);
					return this.legend = Messages.getString("QuanlAna.ReachabilityGraph.Legend") + ": (" + legend + ")";
				}
			}
			return this.legend = Messages.getString("QuanlAna.ReachabilityGraph.Legend") + ": ()"; 
		} else {
			return this.legend;
		}
	}
	
	private String getGraphInfo(){
		Object[] roots = this.rgp_jgraph.getRoots();
		int vertices = 0;
		int edges = 0;
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

	protected void setLogicalFingerPrint(String logicalFingerprint) {
		this.logicalFingerprint = logicalFingerprint;
	}
	
	protected void setRefreshButtonEnabled(boolean b){
		this.refreshButton.setEnabled(b);
	}
	
	protected void setParallelRouting(boolean enabled){
		ReachabilityGraphModel.setParallelRouting(rgp_jgraph, enabled);
	}
	
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
			
			Vector<String> pngbwExtensions = new Vector<String>();
			pngbwExtensions.add("png");
			FileFilterImpl PNGBWFilter = new FileFilterImpl(
					FileFilterImpl.PNGBWFilter, "PNG B/W (*.png)", pngbwExtensions);
			jfc.setFileFilter(PNGBWFilter);

			Vector<String> bmpExtensions = new Vector<String>();
			bmpExtensions.add("bmp");
			FileFilterImpl BMPFilter = new FileFilterImpl(
					FileFilterImpl.BMPFilter, "BMP (*.bmp)", bmpExtensions);
			jfc.setFileFilter(BMPFilter);
			
			Vector<String> bmpbwExtensions = new Vector<String>();
			bmpbwExtensions.add("bmp");
			FileFilterImpl BMPBWFilter = new FileFilterImpl(
					FileFilterImpl.BMPBWFilter, "BMP B/W (*.bmp)", bmpbwExtensions);
			jfc.setFileFilter(BMPBWFilter);

			Vector<String> jpgExtensions = new Vector<String>();
			jpgExtensions.add("jpg");
			jpgExtensions.add("jpeg");
			FileFilterImpl JPGFilter = new FileFilterImpl(
					FileFilterImpl.JPGFilter, "JPG (*.jpg)", jpgExtensions);
			jfc.setFileFilter(JPGFilter);
			
			Vector<String> jpgbwExtensions = new Vector<String>();
			jpgbwExtensions.add("jpg");
			jpgbwExtensions.add("jpeg");
			FileFilterImpl JPGBWFilter = new FileFilterImpl(
					FileFilterImpl.JPGBWFilter, "JPG B/W (*.jpg)", jpgbwExtensions);
			jfc.setFileFilter(JPGBWFilter);

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
						.getFilterType() == FileFilterImpl.JPGBWFilter) {
					savePath = savePath
							+ Utils.getQualifiedFileName(jfc.getSelectedFile()
									.getName(), jpgbwExtensions);
				}else if (((FileFilterImpl) jfc.getFileFilter())
						.getFilterType() == FileFilterImpl.PNGFilter) {
					savePath = savePath
							+ Utils.getQualifiedFileName(jfc.getSelectedFile()
									.getName(), pngExtensions);
				}else if (((FileFilterImpl) jfc.getFileFilter())
						.getFilterType() == FileFilterImpl.PNGBWFilter) {
					savePath = savePath
							+ Utils.getQualifiedFileName(jfc.getSelectedFile()
									.getName(), pngbwExtensions);
				} else if (((FileFilterImpl) jfc.getFileFilter())
						.getFilterType() == FileFilterImpl.BMPFilter) {
					savePath = savePath
							+ Utils.getQualifiedFileName(jfc.getSelectedFile()
									.getName(), bmpExtensions);
				}else if (((FileFilterImpl) jfc.getFileFilter())
						.getFilterType() == FileFilterImpl.BMPBWFilter) {
					savePath = savePath
							+ Utils.getQualifiedFileName(jfc.getSelectedFile()
									.getName(), bmpbwExtensions);
				} else {
					LoggerManager.error(Constants.QUALANALYSIS_LOGGER,
							"\"Export\" NOT SUPPORTED FILE TYPE.");
				}
				filetype = ((FileFilterImpl) jfc.getFileFilter()).getFilterType();
				filepath = savePath;
			}
			
			if (filetype == FileFilterImpl.JPGFilter) {
				ImageExport.saveJPG(ImageExport
						.getRenderedImage(rgp,false), new File(filepath));
			} else if (filetype == FileFilterImpl.JPGBWFilter) {
				ImageExport.saveJPG(ImageExport
						.getRenderedImage(rgp,true), new File(filepath));
			} else if (filetype == FileFilterImpl.PNGFilter) {
				ImageExport.savePNG(ImageExport
						.getRenderedImage(rgp,false), new File(filepath));
			} else if (filetype == FileFilterImpl.PNGBWFilter) {
				ImageExport.savePNG(ImageExport
						.getRenderedImage(rgp,true), new File(filepath));
			}else if (filetype == FileFilterImpl.BMPFilter) {
				ImageExport.saveBMP(ImageExport
						.getRenderedImage(rgp,false), new File(filepath));
			}else if (filetype == FileFilterImpl.BMPBWFilter) {
				ImageExport.saveBMP(ImageExport
						.getRenderedImage(rgp,true), new File(filepath));
			}else {
				LoggerManager.warn(Constants.QUALANALYSIS_LOGGER,
						"Unable to save File. Filetype not known: "
								+ filetype);
			}
		}
	}

	class LayoutBoxItemListener implements ItemListener {

		ReachabilityGraphPanel rgp = null;
		
		public LayoutBoxItemListener(ReachabilityGraphPanel rgp){
			this.rgp = rgp;
		}
		
		public void itemStateChanged(ItemEvent e) {
			JComboBox selectedChoice = (JComboBox) e.getSource();
	        rgp.layoutGraph(selectedChoice.getSelectedIndex(), false);
		}
	}
	
	class RefreshGraphButtonListener implements ActionListener {

		ReachabilityGraphPanel rgp = null;
		
		public RefreshGraphButtonListener(ReachabilityGraphPanel rgp){
			this.rgp = rgp;
		}
		
		public void actionPerformed(ActionEvent arg0) {
			rgp.layoutGraph(rgp.getSelectedType(), true);
			rgp.setLogicalFingerPrint(((PetriNetModelProcessor)rgp.getEditor().getModelProcessor()).getLogicalFingerprint());
			rgp.setRefreshButtonEnabled(false);
			rgp.updateVisibility();
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