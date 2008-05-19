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
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashSet;

import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.jgraph.JGraph;
import org.woped.core.controller.IEditor;
import org.woped.core.gui.IUserInterface;
import org.woped.core.qualanalysis.IReachabilityGraph;
import org.woped.core.utilities.LoggerManager;
import org.woped.qualanalysis.Constants;
import org.woped.qualanalysis.reachabilitygraph.controller.SimulationRunningException;
import org.woped.qualanalysis.reachabilitygraph.data.ReachabilityGraphModel;
import org.woped.translations.Messages;

public class ReachabilityGraphVC extends JInternalFrame implements IReachabilityGraph, InternalFrameListener {

	private HashSet<ReachabilityGraphPanel> panels = new HashSet<ReachabilityGraphPanel>();
	private IUserInterface dui = null;
	
	private static final long serialVersionUID = 1L;
	private static ReachabilityGraphVC myInstance = null;

	/**
	 * returns an instance of ReachabilityGraphVC. All RG's of all editor windows are held
	 * in a instance of this class.
	 * @param editor
	 * @return
	 */
	public static ReachabilityGraphVC getInstance(IUserInterface dui){
		if(ReachabilityGraphVC.myInstance == null){
			ReachabilityGraphVC.myInstance = new ReachabilityGraphVC(dui);
		}
		return myInstance;
	}

	private ReachabilityGraphVC(IUserInterface dui) {
		super();
		init();
		this.addInternalFrameListener(this);
		this.dui = dui;
	}

	/**
	 * initialize JInternalFrame.
	 */
	private void init() {
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-> init() " + this.getClass().getName());
		this.setFrameIcon(Messages.getImageIcon("ToolBar.ReachabilityGraph"));
		this.setSize(new Dimension(640, 480));
		this.setMinimumSize(new Dimension(320,240));
		this.setTitle(Messages.getString("ToolBar.ReachabilityGraph.Title"));
        this.setClosable(true);
        this.setResizable(true);
        this.setMaximizable(true);
        this.setIconifiable(false);
        this.setLayout(new GridLayout(1,1));
        this.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- init() " + this.getClass().getName());
	}
	/**
	 * add a new editor to the class. Editor references are held in {@link ReachabilityGraphPanel}'s.
	 * @param editor
	 */
	public void addEditor(IEditor editor){
		boolean alreadyContainsPanel = false;

		for(ReachabilityGraphPanel rgp : panels){
			if(rgp.getEditor().equals(editor)){
				alreadyContainsPanel = true;
			}
		}

		if(!alreadyContainsPanel){
			ReachabilityGraphPanel rgp =  new ReachabilityGraphPanel(editor);
			this.panels.add(rgp);
			this.add(rgp);
			this.add(BorderLayout.CENTER, rgp);
			editor.setReachabilityEnabled(true);
		}

		this.updatePanelsVisibility(editor);
	}

	/**
	 * checks if a editor already has been added.
	 * @param editor
	 * @return
	 */
	public boolean hasEditor(IEditor editor){
		for (ReachabilityGraphPanel rgp : panels) {
			if(rgp.getEditor() == editor){
				return true;
			}
		}
		return false;
	}

	/**
	 * returns the (Reachability)JGraph instance for a given editor.
	 */
	public JGraph getJGraph(IEditor editor){
		for (ReachabilityGraphPanel rgp : panels) {
			if(rgp.getEditor() == editor){
				return (JGraph) rgp.getGraph();
			}
		}
		return null;
	}

	/**
	 * removes the {@link ReachabilityGraphPanel} for a given editor.
	 */
	public void removePanel(IEditor editor){

		ReachabilityGraphPanel rememberRgp = null;

		// Lookup a panel with given editor
		for(ReachabilityGraphPanel rgp : panels){
			if(rgp.getEditor().equals(editor)){
				rememberRgp = rgp;
			}
		}

		// if found a panel, then remove it from internal frame and from panels HashSet
		if(rememberRgp != null){
			this.remove(rememberRgp);
			this.repaint();
			this.panels.remove(rememberRgp);
			editor.setReachabilityEnabled(false);
		}

		// if it was the last active editor, set internal frame invisible
		if(this.panels.size() == 0){
			this.setVisible(false);
		}
	}

	/**
	 * recomputes the RG.
	 * @param type - is one layout type of {@link ReachabilityGraphModel}
	 */
	public void refreshGraph(int type){
		for (ReachabilityGraphPanel rgp : panels) {
			if(rgp.isShowing()){
				try {
					rgp.layoutGraph(rgp.getSelectedType(), true);
				} catch (SimulationRunningException e) {
					ReachabilityWarning.showSimulationRunningWarning(this);
					rgp.setRefreshButtonEnabled(true);
					rgp.setGraphOutOfSync(true);
				}
			}
		}
		this.repaint();
	}

	/**
	 * removes all editors from being showed on the JInternalFrame except the given one.
	 * @param editor
	 */
	public void updatePanelsVisibility(IEditor editor){
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-> updatePanelsVisibility " + this.getClass().getName());
		for (ReachabilityGraphPanel rgp : panels) {
			if(rgp.getEditor().equals(editor)){
				this.add(rgp);
				rgp.updateVisibility();
				this.setTitle(Messages.getString("ToolBar.ReachabilityGraph.Title")+ " - " + editor.getName());
			} else {
				this.remove(rgp);
			}
		}

		boolean isAnyPanelShowing = false;
		for (ReachabilityGraphPanel rgp : panels) {
			if(rgp.isShowing()){
				isAnyPanelShowing = true;
			}
		}

		if(!isAnyPanelShowing){
			this.setTitle(Messages.getString("ToolBar.ReachabilityGraph.Title"));
		}

		this.repaint();
	}

	private void updateShowingPanelVisibility(){
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-> updateShowingPanelVisibilty " + this.getClass().getName());
		for (ReachabilityGraphPanel rgp : panels) {
			if(rgp.isShowing()){
				rgp.updateVisibility();
			}
		}
	}

	public void internalFrameActivated(InternalFrameEvent e) {
		this.updateShowingPanelVisibility();
	}
	public void internalFrameClosed(InternalFrameEvent e) { }
	public void internalFrameClosing(InternalFrameEvent e) { dui.refreshFocusOnFrames(); }
	public void internalFrameDeactivated(InternalFrameEvent e) { }
	public void internalFrameDeiconified(InternalFrameEvent e) { }
	public void internalFrameIconified(InternalFrameEvent e) { }
	public void internalFrameOpened(InternalFrameEvent e) { }
}
