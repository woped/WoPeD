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
 * For contact information please visit http://woped.ba-karlsruhe.de
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
import java.util.Iterator;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.utilities.FileFilterImpl;
import org.woped.core.utilities.LoggerManager;
import org.woped.core.utilities.SwingUtils;
import org.woped.core.utilities.Utils;
import org.woped.qualanalysis.Constants;
import org.woped.qualanalysis.ImageExport;
import org.woped.qualanalysis.reachabilitygraph.controller.SimulationRunningException;
import org.woped.translations.Messages;

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
public class ReachabilityToolbarVC extends JToolBar {
    // ViewControll
    public static final String 		ID_PREFIX            = "ReachabilityToolbar_VC_";

    //Other Variables
	private static final int        stXsize              = 20;
	private static final int        stYsize              = 20;
	
	// Declaration of all Buttons
	private JButton					pbnRefresh			 = null;
	private JButton					pbnUnselect			 = null;
	private JButton					pbnSettings			 = null;
	private JButton					pbnExport			 = null;
	
	private JButton					zoomInButton       	 = null;
	private JButton					zoomOutButton		 = null;
	
	private JMenuItem jmiHierachic, jmiCircle 			 = null;
	private int LayoutIndex = 0;
	
	// Declaration of all ComboBoxes
	private JComboBox				jcbZoomChooser			 = null;
	
	// Listener for the Buttons
	private TB_Listener				listener			 = null;
	private BoxItemListener			boxListener			 = null;
	
	
    public ReachabilityToolbarVC(ReachabilityGraphPanel rgp)
    {
        
    	setBorder(BorderFactory.createEtchedBorder());
        setRollover(true);
        
        listener = new TB_Listener(rgp);
        addButtons();
        
        DropDownButton dp = new DropDownButton(Messages.getImageIcon("QuanlAna.ReachabilityGraph.Layout"));
        dp.setFocusable(false);
        dp.setToolTipText(Messages.getString("QuanlAna.ReachabilityGraph.Layout"));
        
        
        
        jmiHierachic = dp.getMenu().add(Messages.getString("QuanlAna.ReachabilityGraph.Hierarchic"));
        jmiHierachic.addActionListener(listener);
        jmiHierachic.setIcon(Messages.getImageIcon("QuanlAna.ReachabilityGraph.Hierarchic"));
        jmiCircle = dp.getMenu().add(Messages.getString("QuanlAna.ReachabilityGraph.Circle"));
        jmiCircle.addActionListener(listener);
        jmiCircle.setIcon(Messages.getImageIcon("QuanlAna.ReachabilityGraph.Circle"));        
        dp.getMenu().addItemListener(boxListener);
        
        add(dp);      
               
        addSeparator();
        addSeparator(new Dimension(12, 0));
        addSeparator();
        
        boxListener = new BoxItemListener(rgp);
        
        
        addZoomChooser();  
        
        setUnselectButtonEnabled(false);
    }        
     /**
	 * Adding all needed Buttons 
	 */
	private void addButtons()
	{
		pbnRefresh	= new JButton(Messages.getImageIcon("QuanlAna.ReachabilityGraph.RefreshButton"));
		pbnUnselect	= new JButton(Messages.getImageIcon("QuanlAna.ReachabilityGraph.UnselectButton"));
		pbnSettings = new JButton(Messages.getImageIcon("QuanlAna.ReachabilityGraph.SettingsButton"));
		pbnExport 	= new JButton(Messages.getImageIcon("QuanlAna.ReachabilityGraph.ExportAsButton"));
		
		// Buttons without focus
		pbnRefresh.setFocusable(false);
		pbnUnselect.setFocusable(false);
		pbnSettings.setFocusable(false);
		pbnExport.setFocusable(false);
		
		//Define Button-Size
		pbnRefresh.setPreferredSize(new Dimension(stXsize, stYsize));
		pbnUnselect.setPreferredSize(new Dimension(stXsize, stYsize));
		pbnSettings.setPreferredSize(new Dimension(stXsize, stYsize));
		pbnExport.setPreferredSize(new Dimension(stXsize, stYsize));
		
		//Define ToolTips
		pbnRefresh.setToolTipText(Messages.getTitle("QuanlAna.ReachabilityGraph.RefreshButton")); 
		pbnUnselect.setToolTipText(Messages.getTitle("QuanlAna.ReachabilityGraph.UnselectButton")); 
		pbnSettings.setToolTipText(Messages.getTitle("QuanlAna.ReachabilityGraph.SettingsButton"));
		pbnExport.setToolTipText(Messages.getTitle("QuanlAna.ReachabilityGraph.ExportAsButton"));
		
		//Define Actions
		pbnRefresh.addActionListener(listener);
		pbnUnselect.addActionListener(listener);
		pbnSettings.addActionListener(listener);
		pbnExport.addActionListener(listener);
		
		add(pbnRefresh);
		add(pbnUnselect);
		add(pbnSettings);
		add(pbnExport);
	}
	
	protected int getSelectedType(){
		return LayoutIndex;
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
		zoomInButton = new JButton(Messages.getImageIcon("Action.ZoomIn"));//new JButton(Messages.getTitle("ToolBar.ZoomIn"));
        zoomOutButton = new JButton(Messages.getImageIcon("Action.ZoomOut"));
        zoomInButton.setFocusable(false);
        zoomOutButton.setFocusable(false);
        zoomInButton.setPreferredSize(new Dimension(stXsize,stYsize));
        zoomOutButton.setPreferredSize(new Dimension(stXsize,stYsize));
        zoomInButton.setToolTipText(Messages.getTitle("Action.ZoomIn"));
        zoomOutButton.setToolTipText(Messages.getTitle("Action.ZoomOut"));
        zoomInButton.addActionListener(listener);
        zoomOutButton.addActionListener(listener);
        add(zoomInButton);
        add(zoomOutButton);
                
        String[] zoomFactors = { "25", "50", "75", "100"};        
        jcbZoomChooser = new JComboBox(zoomFactors);
        jcbZoomChooser.setSelectedIndex(3);
        jcbZoomChooser.setBorder(BorderFactory.createEtchedBorder());
        jcbZoomChooser.setEditable(true);
        SwingUtils.setFixedWidth(jcbZoomChooser, 70);        
        this.add(jcbZoomChooser);
        jcbZoomChooser.addItemListener(boxListener);
	}
	
    class TB_Listener implements ActionListener {
		ReachabilityGraphPanel rgp = null;

		public TB_Listener(ReachabilityGraphPanel rgp){
			this.rgp = rgp;			
		}

		public void actionPerformed(ActionEvent e) {
			if		(e.getSource() == pbnRefresh){
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
		}
		
		private void switchLayout(int selectedIndex){			
			try {
				rgp.layoutGraph(selectedIndex, false);
			} catch (SimulationRunningException e1) {
				ReachabilityWarning.showSimulationRunningWarning(this.rgp);
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
				ReachabilityWarning.showSimulationRunningWarning(this.rgp);
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
				ReachabilityWarning.showUnselectWarning(this.rgp);
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