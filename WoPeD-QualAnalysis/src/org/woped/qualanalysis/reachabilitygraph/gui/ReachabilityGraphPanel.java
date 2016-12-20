/**
 * ReachabilityGraph implementation was done by Manuel Fladt and Benjamin Geiger.
 * The code was written for a project at BA Karlsruhe in 2007/2008 under authority
 * of Prof. Dr. Thomas Freytag and Andreas Eckleder.
 * <p>
 * This class was written by
 *
 * @author Benjamin Geiger
 */

package org.woped.qualanalysis.reachabilitygraph.gui;

import org.woped.core.controller.IEditor;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.action.WoPeDAction;
import org.woped.editor.controller.ActionFactory;
import org.woped.editor.controller.VisualController;
import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.Constants;
import org.woped.qualanalysis.reachabilitygraph.controller.CoverabilityGraphActions;
import org.woped.qualanalysis.reachabilitygraph.controller.SimulationRunningException;
import org.woped.qualanalysis.reachabilitygraph.data.AbstractReachabilityGraphModel;
import org.woped.qualanalysis.reachabilitygraph.data.IReachabilityGraphModel;
import org.woped.qualanalysis.reachabilitygraph.data.ReachabilityGraphModelUsingMarkingNet;
import org.woped.qualanalysis.reachabilitygraph.data.model.ReachabilityEdgeModel;
import org.woped.qualanalysis.reachabilitygraph.data.model.ReachabilityPlaceModel;
import org.woped.qualanalysis.reachabilitygraph.gui.dialogs.ReachabilityWarning;
import org.woped.qualanalysis.reachabilitygraph.gui.layout.CoverabilityGraphLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class ReachabilityGraphPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private IEditor editor = null;
    private ReachabilityGraphVC rgvc = null;

    // Panels
    private JScrollPane rgp_topPanel = null; // top SplitPane
    private JPanel graphPanel = null;

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
        this.logicalFingerprint = editor.getModelProcessor().getLogicalFingerprint();
        this.editor.updateNet();
        init();
    }

    private void init() {
        LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-> init() " + this.getClass().getName());
        this.builder = new ReachabilityGraphModelUsingMarkingNet(editor);
        this.setLayout(new BorderLayout());

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

        JPanel infoPanel = new JPanel(new GridBagLayout());
        JLabel title = new JLabel("Information");
        infoPanel.add(title);
        this.add(BorderLayout.EAST, infoPanel);

        rgp_jgraph = new ReachabilityJGraph();
        rgp_topPanel = new JScrollPane(rgp_jgraph);

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
    public CoverabilityGraphLayout getSelectedType() {
        WoPeDAction staticAction = ActionFactory.getStaticAction(CoverabilityGraphActions.LAYOUT);
        CoverabilityGraphLayout selectedType = (CoverabilityGraphLayout) staticAction.getData();
        return selectedType;
    }

    /**
     * layout a graph with given layout layout. Layouting can be done without to recmpute the graph. In some cases it's needed to recompute e.g. the petri net has
     * changed.
     *
     * @param layout
     * @param computeNew
     */
    public void layoutGraph(CoverabilityGraphLayout layout, boolean computeNew) throws SimulationRunningException {
        // Define the cursors
        Cursor crWait = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
        Cursor crDefault = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
        // Set the cursor when the RG is calculated the first time
        setCursor(crWait);
        // set the cursor when the RG is refreshed
//        ribbon.setCursor(crWait);
        if (rgp_topPanel != null) {
            if (computeNew) {
                if (editor.isTokenGameEnabled()) {
                    this.setRefreshButtonEnabled(true);
                    // set the default cursors
                    setCursor(crDefault);
//                    ribbon.setCursor(crDefault);
                    throw new SimulationRunningException();
                } else {

                    this.remove(rgp_topPanel);
                    this.rgp_topPanel = new JScrollPane(this.rgp_jgraph = this.getDefaultGraph(layout));
                    this.add(BorderLayout.CENTER, rgp_topPanel);
                    this.updateVisibility();
                }
            } else {
                AbstractReachabilityGraphModel.layoutGraph(this.rgp_jgraph, layout, this.getSize());
            }
            this.validate();
        }
        // set the default cursors
        setCursor(crDefault);
//        ribbon.setCursor(crDefault);
    }

    /**
     * is used to get a new computed {@link ReachabilityJGraph} instance. Layout in a given type.
     *
     * @param layout
     * @return
     */
    private ReachabilityJGraph getDefaultGraph(CoverabilityGraphLayout layout) {
        HashMap<String, String> old_attributes = null;
        if (rgp_jgraph != null) {
            old_attributes = rgp_jgraph.getAttributeMap();
        }

        ReachabilityJGraph new_graph = builder.getGraph();

        if (old_attributes != null) {
            new_graph.setAttributeMap(old_attributes);
        }
        return AbstractReachabilityGraphModel.layoutGraph(new_graph, layout, this.getSize());
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
        if (editor.getModelProcessor().getLogicalFingerprint().equals(this.logicalFingerprint) && !editor.isTokenGameEnabled()) {
            if (this.rgp_jgraph.getRoots().length == 0) {
                setRefreshButtonEnabled(true);
                setGraphOutOfSync(true);
            } else {
                this.setRefreshButtonEnabled(false);
                setGraphOutOfSync(false);
            }
        } else {
            this.setRefreshButtonEnabled(true);
            setGraphOutOfSync(true);
        }
        bottomInfo.setText(this.getGraphInfo());
        legendInfo.setText(this.getLegend());
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
     * sets legendByName. It's used for showing the legend with names or id's.
     *
     * @param legend
     */
    protected void setLegendByName(boolean legend) {
        this.legendToggle = legend;
    }

    /**
     * sets the graph out of sync label.
     *
     * @param outOfSync
     */
    protected void setGraphOutOfSync(boolean outOfSync) {
        this.outOfSyncInfo.setVisible(outOfSync);
    }

    protected boolean isGraphOutOfSync(){
        return this.outOfSyncInfo.isVisible();
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

    public boolean getRefreshButtonEnabled() {
        WoPeDAction action = ActionFactory.getStaticAction(CoverabilityGraphActions.REFRESH);
        return VisualController.getInstance().isEnabled(action);
    }

    /**
     * sets the refresh button to be clickable - or not.
     *
     * @param newState
     */
    public void setRefreshButtonEnabled(boolean newState) {
//        this.ribbon.setRefreshButtonEnabled(newState);
        setActionState(CoverabilityGraphActions.REFRESH, newState);
    }

    public void setUnselectButtonEnabled(boolean newState) {
//        this.ribbon.setUnselectButtonEnabled(newState);
        setActionState(CoverabilityGraphActions.UNSELECT, newState);
    }

    /**
     * enables parallel rounting for the RG or not. ParallelRouting is very slow on big RG's.
     *
     * @param enabled
     */
    public void setParallelRouting(boolean enabled) {
        AbstractReachabilityGraphModel.setParallelRouting(rgp_jgraph, enabled);
    }

    /**
     * enables a gray-scale view of the graph.
     *
     * @param enabled
     */
    public void setGrayScale(boolean enabled) {
        AbstractReachabilityGraphModel.setGrayScale(rgp_jgraph, enabled);
    }

    private void setActionState(String actionID, boolean isEnabled) {
        int enabled = isEnabled ? VisualController.ALWAYS : VisualController.NEVER;
        WoPeDAction action = ActionFactory.getStaticAction(actionID);
        VisualController.getInstance().addElement(action, enabled, VisualController.ALWAYS, VisualController.IGNORE);
    }


    private class LegendListener implements ActionListener {

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
}