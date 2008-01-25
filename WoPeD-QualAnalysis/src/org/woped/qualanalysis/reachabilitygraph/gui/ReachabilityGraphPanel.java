package org.woped.qualanalysis.reachabilitygraph.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import org.jgraph.JGraph;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.IEditor;
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

public class ReachabilityGraphPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private IEditor editor = null;

	// Panels
	private JPanel rgp_bottomPanel = null; // bottom SplitPane
	private JScrollPane rgp_topPanel = null; // top SplitPane
	
	// jGraph related
	private JGraph rgp_jgraph = null; // the jGraph
	
	// Labels
	private JRadioButton circleButton = null;
	private JRadioButton hierarchicButton= null;
	private JLabel bottomInfo = null;
	private JLabel legendInfo = null;
	
	
	public ReachabilityGraphPanel(IEditor editor) {
		super();
		this.editor = editor;
		init();
	}
	
	private void init() {
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-> init() " + this.getClass().getName());
		this.setLayout(new BorderLayout());
		// NORTH Components
        legendInfo = new JLabel();
        legendInfo.setText(Messages.getString("QuanlAna.ReachabilityGraph.Legend") + ": ()");
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        northPanel.add(legendInfo);
        JButton export = new JButton(Messages.getString("QuanlAna.ReachabilityGraph.ExportAsButton")); // TODO: Add to Messages
        export.addActionListener(new ExportGraphButtonListener(this));
        northPanel.add(export);
        this.add(BorderLayout.NORTH, northPanel);
        // SOUTH Components
        JButton refreshButton = new JButton(Messages.getString("QuanlAna.ReachabilityGraph.RefreshButton"));
        refreshButton.addActionListener(new RefreshGraphButtonListener(this));
        circleButton = new JRadioButton(Messages.getString("QuanlAna.ReachabilityGraph.Circle") + " " +  
        									Messages.getString("QuanlAna.ReachabilityGraph.Layout"));
        hierarchicButton = new JRadioButton(Messages.getString("QuanlAna.ReachabilityGraph.Hierarchic") + " " + 
											 Messages.getString("QuanlAna.ReachabilityGraph.Layout"));
        hierarchicButton.setActionCommand(ReachabilityGraphModel.HIERARCHIC + "");
        hierarchicButton.setSelected(true);
        circleButton.setActionCommand(ReachabilityGraphModel.CIRCLE + "");
        ButtonGroup layoutGroup = new ButtonGroup();
        layoutGroup.add(circleButton);
        layoutGroup.add(hierarchicButton);
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));
        southPanel.add(refreshButton);
        southPanel.add(circleButton);
        southPanel.add(hierarchicButton);
        southPanel.add(bottomInfo = new JLabel(""));
        this.add(BorderLayout.SOUTH, southPanel);
		rgp_jgraph = this.getDefaultGraph(ReachabilityGraphModel.HIERARCHIC);
		rgp_topPanel = new JScrollPane();
		this.add(rgp_topPanel);
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- init() " + this.getClass().getName());
	}
	
	public IEditor getEditor(){
		return editor;
	}
	
	public void refreshGraph(int type){
		this.remove(rgp_topPanel);
		this.add(rgp_topPanel = new JScrollPane(this.rgp_jgraph = this.getDefaultGraph(type)));
		this.validate();
	}
	
	private JGraph getDefaultGraph(int type){
		Iterator<JRadioButton> iterButtons = getRadioButtons().iterator();
		while(iterButtons.hasNext()){
			JRadioButton button = iterButtons.next();
			if(Integer.parseInt(button.getActionCommand()) == type){
				button.setSelected(true);
			}
		}
		ReachabilityGraphModel builder = new ReachabilityGraphModel(editor);
		return ReachabilityGraphModel.layoutGraph(builder.getGraph(), type, this.getSize());
	}
	
	public JGraph getGraph(){
		return this.rgp_jgraph;
	}
	
	public void updateVisibility(){
		bottomInfo.setText(this.getGraphInfo());
		legendInfo.setText(this.getLegend());
	}
	
	protected LinkedList<JRadioButton> getRadioButtons(){
		LinkedList<JRadioButton> buttons = new LinkedList<JRadioButton>();
		buttons.add(circleButton);
		buttons.add(hierarchicButton);
		return buttons;
	}
	
	private String getLegend(){
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
				return Messages.getString("QuanlAna.ReachabilityGraph.Legend") + ": (" + legend + ")";
			}
		}
		return Messages.getString("QuanlAna.ReachabilityGraph.Legend") + ": ()";
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

			jfc.setFileFilter(JPGFilter);
			
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
				} else if (((FileFilterImpl) jfc.getFileFilter())
						.getFilterType() == FileFilterImpl.BMPFilter) {
					savePath = savePath
							+ Utils.getQualifiedFileName(jfc.getSelectedFile()
									.getName(), bmpExtensions);
				} else {
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

	class RefreshGraphButtonListener implements ActionListener {

		ReachabilityGraphPanel rgp = null;
		
		public RefreshGraphButtonListener(ReachabilityGraphPanel rgp){
			this.rgp = rgp;
		}
		
		public void actionPerformed(ActionEvent arg0) {
			Iterator<JRadioButton> iterButtons = rgp.getRadioButtons().iterator();
			while(iterButtons.hasNext()){
				JRadioButton button = iterButtons.next();
				if(button.isSelected()){
					rgp.refreshGraph(Integer.parseInt(button.getActionCommand()));	
				}		
			}
		}
	}
