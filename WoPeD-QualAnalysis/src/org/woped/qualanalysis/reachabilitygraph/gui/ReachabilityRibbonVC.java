/*
 *
 * Copyright (C) 2004-2005, see @author in JavaDoc for the author
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *
 * For contact information please visit http://woped.dhbw-karlsruhe.de
 *
 */
package org.woped.qualanalysis.reachabilitygraph.gui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;

import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandMenuButton;
import org.pushingpixels.flamingo.api.common.RichTooltip;
import org.pushingpixels.flamingo.api.common.icon.ImageWrapperResizableIcon;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.pushingpixels.flamingo.api.common.popup.JCommandPopupMenu;
import org.pushingpixels.flamingo.api.common.popup.JPopupPanel;
import org.pushingpixels.flamingo.api.common.popup.PopupPanelCallback;
import org.pushingpixels.flamingo.api.ribbon.JRibbon;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;
import org.pushingpixels.flamingo.api.ribbon.RibbonTask;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizeSequencingPolicies;
import org.pushingpixels.flamingo.api.ribbon.resize.IconRibbonBandResizePolicy;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.utilities.FileFilterImpl;
import org.woped.core.utilities.LoggerManager;
import org.woped.core.utilities.SwingUtils;
import org.woped.core.utilities.Utils;
import org.woped.qualanalysis.Constants;
import org.woped.qualanalysis.reachabilitygraph.controller.SimulationRunningException;
import org.woped.gui.images.svg.file_new;
import org.woped.gui.images.svg.help_smaplenets;
import org.woped.gui.translations.Messages;

/**
 * Part of a small example showing basic use of JToolBar. Creates a small
 * dockable toolbar that is supposed to look vaguely like one that might come
 * with a Web browser. Makes use of ToolBarButton, a small extension of JButton
 * that shrinks the margins around the icon and puts text label, if any, below
 * the icon. 1999 Marty Hall, http://www.apl.jhu.edu/~hall/java/ TODO:
 * DOCUMENTATION (silenco)
 */

/**
 * New Toolbar for the Reachabilty Graph
 *
 * @author <a href="mailto:b.joerger@gmx.de">Benjamin Joerger</a>
 * @since 02.01.2009
 *
 */

@SuppressWarnings("serial")
public class ReachabilityRibbonVC extends JRibbon {
    // ViewControll
    public static final String 		ID_PREFIX            = "ReachabilityToolbar_VC_";

    //Other Variables
	private static final int        stXsize              = 80;
	private static final int        stYsize              = 80;

	// Declaration of all Buttons
	private JCommandButton					pbnClose			 = null;
	private JCommandButton					pbnRefresh			 = null;
	private JCommandButton					pbnUnselect			 = null;
	private JCommandButton					pbnSettings			 = null;
	private JCommandButton					pbnExport			 = null;

	private JCommandButton					layoutButton       	 = null;
	private JCommandButton					zoomInButton       	 = null;
	private JCommandButton					zoomOutButton		 = null;
	private JCommandButton					zoomChooserButton		 = null;
	private JCommandMenuButton zoomButton100, zoomButton75, zoomButton50, zoomButton25 = null;

	private JCommandMenuButton jmiHierachic, jmiCircle 			 = null;

	private int LayoutIndex = 0;

	// Declaration of all ComboBoxes
	private JComboBox				jcbZoomChooser			 = null;

	// Listener for the Buttons
	private TB_Listener				listener			 = null;
	private BoxItemListener			boxListener			 = null;

	private RibbonTask reachabilityTask = null;
	private JRibbonBand reachabilityBand, viewBand = null;

	public static ResizableIcon getResizableIcon(String resource) {
		  return ImageWrapperResizableIcon.getIcon(Messages.getImageIcon(resource).getImage(), new Dimension(stXsize, stYsize));
	}

    public ReachabilityRibbonVC(ReachabilityGraphPanel rgp)
    {

        listener = new TB_Listener(rgp);

        boxListener = new BoxItemListener(rgp);
        addZoomChooser();

    	this.addTask(getReachabilityTask());

        setUnselectButtonEnabled(false);
    }
	/*********/
	/* BANDS */
	/*********/
    private JRibbonBand getViewBand() {
    	if (viewBand == null) {
    		viewBand = new JRibbonBand(Messages.getString("View.textBandTitle"), null);

            viewBand.addCommandButton(getLayoutButton(), RibbonElementPriority.MEDIUM);
            viewBand.addCommandButton(getZoomInButton(), RibbonElementPriority.MEDIUM);
            viewBand.addCommandButton(getZoomOutButton(), RibbonElementPriority.MEDIUM);
            viewBand.addCommandButton(getZoomChooser(), RibbonElementPriority.MEDIUM);

        	viewBand.setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(viewBand.getControlPanel()), new IconRibbonBandResizePolicy(viewBand.getControlPanel())));

		}
		return viewBand;
	}

	private JRibbonBand getReachabilityBand() {
		if (reachabilityBand == null) {
			reachabilityBand = new JRibbonBand(Messages.getString("Tools.reachabilityGraph.text"), null);

	    	reachabilityBand.addCommandButton(getCloseButton(), RibbonElementPriority.MEDIUM);
	    	reachabilityBand.addCommandButton(getRefreshButton(), RibbonElementPriority.MEDIUM);
	    	reachabilityBand.addCommandButton(getUnselectButton(), RibbonElementPriority.MEDIUM);
	    	reachabilityBand.addCommandButton(getSettingsButton(), RibbonElementPriority.MEDIUM);
	    	reachabilityBand.addCommandButton(getExportButton(), RibbonElementPriority.MEDIUM);

	    	reachabilityBand.setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(reachabilityBand.getControlPanel()), new IconRibbonBandResizePolicy(reachabilityBand.getControlPanel())));
		}
		return reachabilityBand;
	}

	/*********/
	/* TASK */
	/*********/
	private RibbonTask getReachabilityTask() {

		if (reachabilityTask == null) {
			reachabilityTask = new RibbonTask(Messages.getString("Tools.reachabilityGraph.text"), getReachabilityBand(), getViewBand());
		}
		return reachabilityTask;
	}
	/*********/
	/* BUTTONS */
	/*********/
	private JCommandButton getCloseButton() {
		if (pbnClose == null) {
			pbnClose	= new JCommandButton(Messages.getTitle("QuanlAna.ReachabilityGraph.CloseButton"), getResizableIcon("QuanlAna.ReachabilityGraph.CloseButton"));
			pbnClose.addActionListener(listener);
		}
		return pbnClose;
	}
	private JCommandButton getRefreshButton() {
		if (pbnRefresh == null) {
			pbnRefresh	= new JCommandButton(Messages.getTitle("QuanlAna.ReachabilityGraph.RefreshButton"), getResizableIcon("QuanlAna.ReachabilityGraph.RefreshButton"));
			pbnRefresh.addActionListener(listener);
		}
		return pbnRefresh;
	}
	private JCommandButton getUnselectButton() {
		if (pbnUnselect == null) {
			pbnUnselect	= new JCommandButton(Messages.getTitle("QuanlAna.ReachabilityGraph.UnselectButton"), getResizableIcon("QuanlAna.ReachabilityGraph.UnselectButton"));
			pbnUnselect.addActionListener(listener);
		}
		return pbnUnselect;
	}
	private JCommandButton getSettingsButton() {
		if (pbnSettings == null) {
			pbnSettings = new JCommandButton(Messages.getTitle("QuanlAna.ReachabilityGraph.SettingsButton"), getResizableIcon("QuanlAna.ReachabilityGraph.SettingsButton"));
			pbnSettings.addActionListener(listener);
		}
		return pbnSettings;
	}
	private JCommandButton getExportButton() {
		if (pbnExport == null) {
			pbnExport 	= new JCommandButton(Messages.getTitle("QuanlAna.ReachabilityGraph.ExportAsButton"), getResizableIcon("QuanlAna.ReachabilityGraph.ExportAsButton"));
			pbnExport.addActionListener(listener);
		}
		return pbnExport;
	}
	private JCommandButton getLayoutButton() {
		if (layoutButton == null) {
			layoutButton = new JCommandButton(Messages.getString("QuanlAna.ReachabilityGraph.Layout"), getResizableIcon("QuanlAna.ReachabilityGraph.Layout"));
	    	layoutButton.setCommandButtonKind(JCommandButton.CommandButtonKind.POPUP_ONLY);
	    	layoutButton.setPopupCallback(new PopupPanelCallback() {
				@Override
				public JPopupPanel getPopupPanel(JCommandButton commandButton) {
					JCommandPopupMenu layoutOption = new JCommandPopupMenu();
					jmiHierachic = new JCommandMenuButton(Messages.getString("QuanlAna.ReachabilityGraph.Hierarchic"), getResizableIcon("QuanlAna.ReachabilityGraph.Hierarchic"));
			    	jmiHierachic.addActionListener(listener);
			    	jmiCircle = new JCommandMenuButton(Messages.getString("QuanlAna.ReachabilityGraph.Circle"), getResizableIcon("QuanlAna.ReachabilityGraph.Circle"));
					jmiCircle.addActionListener(listener);
					layoutOption.addMenuButton(jmiHierachic);
					layoutOption.addMenuButton(jmiCircle);
					return layoutOption;
				}
			});
		}
		return layoutButton;
	}
	private JCommandButton getZoomInButton() {
		if (zoomInButton == null) {
	    	zoomInButton = new JCommandButton(Messages.getTitle("Action.ZoomIn"), getResizableIcon("Action.ZoomIn"));
	    	zoomInButton.addActionListener(listener);
		}
		return zoomInButton;
	}
	private JCommandButton getZoomOutButton() {
		if (zoomOutButton == null) {
	        zoomOutButton = new JCommandButton(Messages.getTitle("Action.ZoomOut"), getResizableIcon("Action.ZoomOut"));
	        zoomOutButton.addActionListener(listener);
		}
		return zoomOutButton;
	}
	private JCommandButton getZoomChooser() {
		if (zoomChooserButton == null) {
	        zoomChooserButton = new JCommandButton("Zoom", null);
	        zoomChooserButton.setCommandButtonKind(JCommandButton.CommandButtonKind.POPUP_ONLY);
	        zoomChooserButton.setPopupCallback(new PopupPanelCallback() {
				@Override
				public JPopupPanel getPopupPanel(JCommandButton commandButton) {
					JCommandPopupMenu zoomOptions = new JCommandPopupMenu();
					zoomButton100 = new JCommandMenuButton("100 %", null);
					zoomButton100.addActionListener(listener);
					zoomButton75 = new JCommandMenuButton("75 %", null);
					zoomButton75.addActionListener(listener);
					zoomButton50 = new JCommandMenuButton("50 %", null);
					zoomButton50.addActionListener(listener);
					zoomButton25 = new JCommandMenuButton("25 %", null);
					zoomButton25.addActionListener(listener);
					zoomOptions.addMenuButton(zoomButton100);
					zoomOptions.addMenuButton(zoomButton75);
					zoomOptions.addMenuButton(zoomButton50);
					zoomOptions.addMenuButton(zoomButton25);
					return zoomOptions;
				}
			});
		}
		return zoomChooserButton;
	}
	/*********/
	protected int getSelectedType(){
		return LayoutIndex;
	}

	public void setCloseButtonEnabled(boolean value){
		pbnClose.setEnabled(value);
	}

	public boolean pbnCloseButtonEnabled(){
		return pbnClose.isEnabled();
	}

	public void setRrefreshButtonEnabled(boolean value){
		pbnRefresh.setEnabled(value);
	}

	public boolean getRrefreshButtonEnabled(){
		return pbnRefresh.isEnabled();
	}

	public void setUnselectButtonEnabled(boolean value){
		pbnUnselect.setEnabled(value);
	}

	private void addZoomChooser(){
        String[] zoomFactors = { "25", "50", "75", "100"};
        jcbZoomChooser = new JComboBox(zoomFactors);
        jcbZoomChooser.setSelectedIndex(3);
        jcbZoomChooser.setEditable(true);
        jcbZoomChooser.addItemListener(boxListener);
	}

    class TB_Listener implements ActionListener {
		ReachabilityGraphPanel rgp = null;

		public TB_Listener(ReachabilityGraphPanel rgp){
			this.rgp = rgp;
		}

		public void actionPerformed(ActionEvent e) {
			if	(e.getSource() == pbnClose){
				doClose();
			}
			else if	(e.getSource() == pbnRefresh){
				doRefresh();
			}
			else if	(e.getSource() == pbnUnselect){
				doUnselect();
			}
			else if	(e.getSource() == pbnSettings){
				doShowSettings();
			}
			else if	(e.getSource() == pbnExport){
				doExport();
			}
			else if	(e.getSource() == jmiHierachic){
				LayoutIndex = 0;
				switchLayout(0);
			}
			else if	(e.getSource() == jmiCircle){
				LayoutIndex = 1;
				switchLayout(1);
			}
			else if	(e.getSource() == zoomInButton){
				String str = (String)jcbZoomChooser.getSelectedItem();
				jcbZoomChooser.setSelectedItem(""+(Integer.valueOf(str)+10));
			}
			else if	(e.getSource() == zoomOutButton){
				String str = (String)jcbZoomChooser.getSelectedItem();
				jcbZoomChooser.setSelectedItem(""+(Integer.valueOf(str)-10));
			}
			else if	(e.getSource() == zoomButton100){
				jcbZoomChooser.setSelectedItem(""+(Integer.valueOf("100")));
			}
			else if	(e.getSource() == zoomButton75){
				String str = (String)jcbZoomChooser.getSelectedItem();
				jcbZoomChooser.setSelectedItem(""+(Integer.valueOf("75")));
			}
			else if	(e.getSource() == zoomButton50){
				String str = (String)jcbZoomChooser.getSelectedItem();
				jcbZoomChooser.setSelectedItem(""+(Integer.valueOf("50")));
			}
			else if	(e.getSource() == zoomButton25){
				String str = (String)jcbZoomChooser.getSelectedItem();
				jcbZoomChooser.setSelectedItem(""+(Integer.valueOf("25")));
			}
		}

		private void switchLayout(int selectedIndex){
			try {
				rgp.layoutGraph(selectedIndex, false);
			} catch (SimulationRunningException e1) {
				ReachabilityWarning.showReachabilityWarning(this.rgp, "QuanlAna.ReachabilityGraph.SimulationWarning");
			}
		}

		private void doExport(){
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

		private void doShowSettings(){
			ReachabilitySettingsDialog dialog = new ReachabilitySettingsDialog(rgp);
			dialog.setVisible(true);
		}

		private void doClose(){
			rgp.getRGVC().removePanel(rgp.getEditor());
		}

		private void doRefresh(){
			try {
				rgp.layoutGraph(rgp.getSelectedType(), true);
				rgp.setLogicalFingerPrint(((PetriNetModelProcessor)rgp.getEditor().getModelProcessor()).getLogicalFingerprint());
				rgp.setRefreshButtonEnabled(false);
				rgp.setGraphOutOfSync(false);
				rgp.updateVisibility();


				String str = (String)jcbZoomChooser.getSelectedItem();
				jcbZoomChooser.setSelectedItem("100");
				jcbZoomChooser.setSelectedItem(str);
			} catch (SimulationRunningException e) {
				rgp.setRefreshButtonEnabled(true);
				ReachabilityWarning.showReachabilityWarning(this.rgp, "QuanlAna.ReachabilityGraph.SimulationWarning");
			}
		}

		private void doUnselect(){
			try {
				if (rgp.getEditor().isTokenGameEnabled())
					throw new SimulationRunningException();
				rgp.getDefaultGraph().deHighlight();
				// Dehighlight Petrinet and reset VirtualTokens
				((PetriNetModelProcessor)rgp.getEditor().getModelProcessor()).resetRGHighlightAndVTokens();
				rgp.getEditor().setReadOnly(true);
				setUnselectButtonEnabled(false);
			} catch (SimulationRunningException ex) {
				ReachabilityWarning.showReachabilityWarning(this.rgp, "QuanlAna.ReachabilityGraph.UnselectWarning");
			}
		}
	}

    class BoxItemListener implements ItemListener {

		ReachabilityGraphPanel rgp = null;

		public BoxItemListener(ReachabilityGraphPanel rgp){
			this.rgp = rgp;
		}

		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == jcbZoomChooser) {
				setRGZoom((JComboBox)e.getSource());
			}
		}

		private void setRGZoom(JComboBox combo){
			int factor = Integer.parseInt((String) combo.getSelectedItem());
			Rectangle2D oldVisRect = rgp.getGraph().fromScreen(
					rgp.getGraph().getVisibleRect()
					);
			double scale;
			double MIN_SCALE = 0.2;
			double MAX_SCALE = 1.0;
			// Check if absolute
			scale = factor / 100.0;

			// ste to max resp. min if out of range
			if (scale < MIN_SCALE)
			{
				scale = MIN_SCALE;
			}
			if (scale > MAX_SCALE)
			{
				scale = MAX_SCALE;
			}
			// set scale and move to center of old visible rect
			String str = ""+((int)(scale*100));
			combo.removeItemListener(boxListener);
			combo.setSelectedItem(str);
			combo.addItemListener(boxListener);

			rgp.getGraph().setScale(scale);
			oldVisRect = rgp.getGraph().toScreen(oldVisRect);
			Rectangle2D visibleRect = rgp.getGraph().getVisibleRect();
			Rectangle newVisRect = new Rectangle2D.Double(visibleRect.getX()
					+ oldVisRect.getCenterX() - visibleRect.getCenterX(),
					visibleRect.getY() + oldVisRect.getCenterY()
							- visibleRect.getCenterY(), visibleRect.getWidth(),
					visibleRect.getHeight()).getBounds();
			rgp.getGraph().scrollRectToVisible(newVisRect);
			rgp.getGraph().getGraphLayoutCache().reload();
		}
	}

}