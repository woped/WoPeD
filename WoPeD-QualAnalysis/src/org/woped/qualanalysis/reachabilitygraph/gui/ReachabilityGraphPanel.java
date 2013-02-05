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
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.woped.core.controller.IEditor;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.utilities.LoggerManager;
import org.woped.qualanalysis.Constants;
import org.woped.qualanalysis.reachabilitygraph.controller.SimulationRunningException;
import org.woped.qualanalysis.reachabilitygraph.data.AbstractReachabilityGraphModel;
import org.woped.qualanalysis.reachabilitygraph.data.IReachabilityGraphModel;
import org.woped.qualanalysis.reachabilitygraph.data.ReachabilityEdgeModel;
import org.woped.qualanalysis.reachabilitygraph.data.ReachabilityGraphModelUsingMarkingNet;
import org.woped.qualanalysis.reachabilitygraph.data.ReachabilityPlaceModel;
import org.woped.gui.translations.Messages;

public class ReachabilityGraphPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private IEditor editor = null;
    private ReachabilityGraphVC rgvc = null;
    
    // Panels
    private JScrollPane rgp_topPanel = null; // top SplitPane

    private ReachabilityToolbarVC toolbar = null;

    // jGraph related
    private ReachabilityJGraph rgp_jgraph = null; // the jGraph
    private IReachabilityGraphModel builder;
    private String logicalFingerprint = null; // PetrinetIdentifier
    private String legendById = null;
    private String legendByName = null;

    // Labels
    private JLabel bottomInfo = null;
    private JLabel outOfSyncInfo = null;
    private JLabel legendInfo = null;
    private JButton legendToggleButton = null;

    // Helper
    private boolean legendToggle = false;

    public ReachabilityGraphPanel(ReachabilityGraphVC rg, IEditor editor) {
        super();
        this.editor = editor;
        this.rgvc = rg;
        this.logicalFingerprint = ((PetriNetModelProcessor) editor.getModelProcessor()).getLogicalFingerprint();
        this.editor.updateNet();
        init();
    }

    private void init() {
        this.builder = new ReachabilityGraphModelUsingMarkingNet(editor);
        LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-> init() " + this.getClass().getName());
        this.setLayout(new BorderLayout());
        toolbar = new ReachabilityToolbarVC(this);
        // NORTH Components
        this.add(BorderLayout.NORTH, toolbar);
        // SOUTH Components
        legendInfo = new JLabel(Messages.getString("QuanlAna.ReachabilityGraph.Legend") + ": ()");
        legendToggleButton = new JButton(Messages.getImageIcon("Action.Browser.Refresh"));
        legendToggleButton.setToolTipText(Messages.getString("QuanlAna.ReachabilityGraph.Legend.Toggle"));
        legendToggleButton.addActionListener(new LegendListener(this));
        legendToggleButton.setBorder(BorderFactory.createEmptyBorder());
        legendToggleButton.setFocusPainted(false);
        setLegendByName(true);
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        this.add(BorderLayout.SOUTH, southPanel);
        southPanel.add(legendInfo);
        southPanel.add(legendToggleButton);
        southPanel.add(bottomInfo = new JLabel(""));
        southPanel.add(outOfSyncInfo = new JLabel(Messages.getImageIcon("QuanlAna.ReachabilityGraph.GraphOutOfSync")));
        outOfSyncInfo.setToolTipText(Messages.getString("QuanlAna.ReachabilityGraph.GraphOutOfSync"));
        rgp_jgraph = new ReachabilityJGraph();
        rgp_topPanel = new JScrollPane();
        this.add(BorderLayout.CENTER, rgp_topPanel);
        LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- init() " + this.getClass().getName());
    }

    /**
     * returns the editor for this instance.
     * 
     * @return
     */
    public ReachabilityGraphVC getRGVC() {
        return rgvc;
    }

   /**
     * returns the editor for this instance.
     * 
     * @return
     */
    public IEditor getEditor() {
        return editor;
    }

    /**
     * returns the selected layout type.
     * 
     * @return
     */
    protected int getSelectedType() {
        return this.toolbar.getSelectedType();
    }

    /**
     * layout a graph with given layout type. Layouting can be done without to recmpute the graph. In some cases it's needed to recompute e.g. the petri net has
     * changed.
     * 
     * @param type
     * @param computeNew
     */
    public void layoutGraph(int type, boolean computeNew) throws SimulationRunningException {
        // Define the cursors
        Cursor crWait = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
        Cursor crDefault = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
        // Set the cursor when the RG is calculated the first time
        setCursor(crWait);
        // set the cursor when the RG is refreshed
        toolbar.setCursor(crWait);
        if (rgp_topPanel != null) {
            if (computeNew) {
                if (editor.isTokenGameEnabled()) {
                    this.toolbar.setRrefreshButtonEnabled(true);
                    // set the default cursors
                    setCursor(crDefault);
                    toolbar.setCursor(crDefault);
                    throw new SimulationRunningException();
                } else {
                    this.remove(rgp_topPanel);
                    this.add(rgp_topPanel = new JScrollPane(this.rgp_jgraph = this.getDefaultGraph(type)));
                    this.updateVisibility();
                }
            } else {
                AbstractReachabilityGraphModel.layoutGraph(this.rgp_jgraph, type, this.getSize());
            }
            this.validate();
        }
        // set the default cursors
        setCursor(crDefault);
        toolbar.setCursor(crDefault);
    }

    /**
     * is used to get a new computed {@link ReachabilityJGraph} instance. Layout in a given type.
     * 
     * @param type
     * @return
     */
    private ReachabilityJGraph getDefaultGraph(int type) {
        HashMap<String, String> old_attributes = null;
        if (rgp_jgraph != null) {
            old_attributes = rgp_jgraph.getAttributeMap();
        }

        ReachabilityJGraph new_graph = builder.getGraph();
        if (old_attributes != null) {
            new_graph.setAttributeMap(old_attributes);
        }
        return AbstractReachabilityGraphModel.layoutGraph(new_graph, type, this.getSize());
    }

    /**
     * Returns the graph with no tokens changed
     * 
     * @return
     */
    public ReachabilityJGraph getDefaultGraph() {
        return rgp_jgraph;
    }

    /**
     * returns {@link ReachabilityJGraph} instance of this panel.
     * 
     * @return
     */
    public ReachabilityJGraph getGraph() {
        return this.rgp_jgraph;
    }

    /**
     * updated all graph relevant objects after the graph was changed.
     */
    public void updateVisibility() {
        LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-> updateVisibility() " + this.getClass().getName());
        // is RG in sync ? And Token game not running...
        if (((PetriNetModelProcessor) editor.getModelProcessor()).getLogicalFingerprint().equals(
                this.logicalFingerprint)
                && !editor.isTokenGameEnabled()) {
            if (this.rgp_jgraph.getRoots().length == 0) {
                this.toolbar.setRrefreshButtonEnabled(true);
                setGraphOutOfSync(true);
            } else {
                this.toolbar.setRrefreshButtonEnabled(false);
                setGraphOutOfSync(false);
            }
        } else {
            this.toolbar.setRrefreshButtonEnabled(true);
            setGraphOutOfSync(true);
        }
        bottomInfo.setText(this.getGraphInfo());
        legendInfo.setText(this.getLegend());
    }

    /**
     * sets legendByName. It's used for showing the legend with names or id's.
     * 
     * @param legend
     */
    protected void setLegendByName(boolean legend) {
        this.legendToggle = legend;
    }

    /**
     * returns the legendByName toggle attribute.
     * 
     * @return
     */
    protected boolean getLegendByName() {
        return this.legendToggle;
    }

    /**
     * sets the graph out of sync label.
     * 
     * @param outOfSync
     */
    protected void setGraphOutOfSync(boolean outOfSync) {
        this.outOfSyncInfo.setVisible(outOfSync);
    }

    /**
     * returns the Legend as String.
     * 
     * @return
     */
    private String getLegend() {
        // check if legend is still up to date with the petri-net.
        if (((PetriNetModelProcessor) editor.getModelProcessor()).getLogicalFingerprint().equals(
                this.logicalFingerprint)) {
            this.legendByName = Messages.getString("QuanlAna.ReachabilityGraph.Legend") + ": ("
                    + builder.getLegendByName() + ")";
            this.legendById = Messages.getString("QuanlAna.ReachabilityGraph.Legend") + ": (" + builder.getLegendByID()
                    + ")";
        }
        if (legendToggle) {
            return this.legendByName;
        } else {
            return this.legendById;
        }
    }

    /**
     * returns a string with information on the graph
     * 
     * @return
     */
    private String getGraphInfo() {
        Object[] roots = this.rgp_jgraph.getRoots();
        int vertices = 0;
        int edges = 0;
        // count vertices and edges of the graph
        for (int i = 0; i < roots.length; i++) {
            if (roots[i] instanceof ReachabilityEdgeModel) {
                edges++;
            }
            if (roots[i] instanceof ReachabilityPlaceModel) {
                vertices++;
            }
        }
        return Messages.getString("QuanlAna.ReachabilityGraph.Vertices") + " " + vertices + " "
                + Messages.getString("QuanlAna.ReachabilityGraph.Edges") + " " + edges;
    }

    /**
     * set the logicalFingerprint of actual petrinet. LogicalFingerprint is used to track changes of the petrinet.
     * 
     * @param logicalFingerprint
     */
    protected void setLogicalFingerPrint(String logicalFingerprint) {
        this.logicalFingerprint = logicalFingerprint;
    }

    /**
     * sets the refresh button to be clickable - or not.
     * 
     * @param b
     */
    public void setRefreshButtonEnabled(boolean b) {
        this.toolbar.setRrefreshButtonEnabled(b);
    }

    public boolean getRefreshButtonEnabled() {
        return this.toolbar.getRrefreshButtonEnabled();
    }

    public void setUnselectButtonEnabled(boolean b) {
        this.toolbar.setUnselectButtonEnabled(b);
    }

    /**
     * enables parallel rounting for the RG or not. ParallelRouting is very slow on big RG's.
     * 
     * @param enabled
     */
    protected void setParallelRouting(boolean enabled) {
        AbstractReachabilityGraphModel.setParallelRouting(rgp_jgraph, enabled);
    }

    /**
     * enables a gray-scale view of the graph.
     * 
     * @param enabled
     */
    protected void setGrayScale(boolean enabled) {
        AbstractReachabilityGraphModel.setGrayScale(rgp_jgraph, enabled);
    }
}

class LegendListener implements ActionListener {

    ReachabilityGraphPanel rgp = null;

    public LegendListener(ReachabilityGraphPanel rgp) {
        this.rgp = rgp;
    }

    public void actionPerformed(ActionEvent e) {
        if (rgp.getLegendByName()) {
            rgp.setLegendByName(false);
        } else {
            rgp.setLegendByName(true);
        }
        rgp.updateVisibility();
    }
}