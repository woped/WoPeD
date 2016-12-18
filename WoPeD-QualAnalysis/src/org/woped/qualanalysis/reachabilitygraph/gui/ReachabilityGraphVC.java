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

import org.jgraph.JGraph;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.IEditor;
import org.woped.core.gui.IUserInterface;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.qualanalysis.IReachabilityGraph;
import org.woped.core.utilities.FileFilterImpl;
import org.woped.core.utilities.LoggerManager;
import org.woped.core.utilities.Utils;
import org.woped.editor.action.WoPeDAction;
import org.woped.editor.controller.ActionFactory;
import org.woped.editor.controller.vep.ViewEvent;
import org.woped.qualanalysis.reachabilitygraph.controller.CoverabilityGraphActions;
import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.Constants;
import org.woped.qualanalysis.reachabilitygraph.controller.SimulationRunningException;
import org.woped.qualanalysis.reachabilitygraph.data.ReachabilityEdgeModel;
import org.woped.qualanalysis.reachabilitygraph.data.ReachabilityPlaceModel;

import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.HashSet;
import java.util.Vector;


public class ReachabilityGraphVC extends JInternalFrame implements IReachabilityGraph, InternalFrameListener, MouseWheelListener {

    private static final long serialVersionUID = 1L;
    private static final double ZOOM_STEP = 0.125;
    private static ReachabilityGraphVC myInstance = null;

    private HashSet<ReachabilityGraphPanel> panels = new HashSet<ReachabilityGraphPanel>();
    private IUserInterface dui = null;

    private ReachabilityGraphVC(IUserInterface dui) {
        super();
        init();
        this.addInternalFrameListener(this);
        this.dui = dui;
    }

    /**
     * returns an instance of ReachabilityGraphVC. All RG's of all editor windows are held
     * in a instance of this class.
     *
     * @param dui
     * @return
     */
    public static ReachabilityGraphVC getInstance(IUserInterface dui) {
        if (ReachabilityGraphVC.myInstance == null) {
            ReachabilityGraphVC.myInstance = new ReachabilityGraphVC(dui);
        }
        return myInstance;
    }

    /**
     * initialize JInternalFrame.
     */
    private void init() {
        LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-> init() " + this.getClass().getName());
        this.setFrameIcon(Messages.getImageIcon("ToolBar.ReachabilityGraph"));
        this.setSize(new Dimension(680, 480));
        this.setMinimumSize(new Dimension(320, 240));
        this.setTitle(Messages.getString("ToolBar.ReachabilityGraph.Title"));
        this.setClosable(true);
        this.setResizable(true);
        this.setMaximizable(true);
        this.setIconifiable(false);
        this.setLayout(new GridLayout(1, 1));
        this.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- init() " + this.getClass().getName());
    }

    /**
     * add a new editor to the class. Editor references are held in {@link ReachabilityGraphPanel}'s.
     *
     * @param editor
     */
    public void addEditor(IEditor editor) {
        boolean alreadyContainsPanel = false;

        for (ReachabilityGraphPanel rgp : panels) {
            if (rgp.getEditor().equals(editor)) {
                alreadyContainsPanel = true;
            }
        }

        if (!alreadyContainsPanel) {
            ReachabilityGraphPanel rgp = new ReachabilityGraphPanel(this, editor);
            this.panels.add(rgp);
            this.add(rgp);
            this.add(BorderLayout.CENTER, rgp);
            editor.setReachabilityEnabled(true);
        }

        this.updatePanelsVisibility(editor);
    }

    /**
     * checks if a editor already has been added.
     *
     * @param editor
     * @return
     */
    public boolean hasEditor(IEditor editor) {
        for (ReachabilityGraphPanel rgp : panels) {
            if (rgp.getEditor() == editor) {
                return true;
            }
        }
        return false;
    }

    /**
     * returns the (Reachability)JGraph instance for a given editor.
     */
    public JGraph getJGraph(IEditor editor) {
        for (ReachabilityGraphPanel rgp : panels) {
            if (rgp.getEditor() == editor) {
                return rgp.getGraph();
            }
        }
        return null;
    }

    public void setUnselectButtonEnabled(IEditor editor, boolean value) {
        for (ReachabilityGraphPanel rgp : panels) {
            if (rgp.getEditor() == editor) {
                rgp.setUnselectButtonEnabled(value);
            }
        }
    }

    /**
     * removes the {@link ReachabilityGraphPanel} for a given editor.
     */
    public void removePanel(IEditor editor) {

        ReachabilityGraphPanel rememberRgp = null;

        // Lookup a panel with given editor
        for (ReachabilityGraphPanel rgp : panels) {
            if (rgp.getEditor().equals(editor)) {
                rememberRgp = rgp;
            }
        }

        // if found a panel, then remove it from internal frame and from panels HashSet
        if (rememberRgp != null) {
            this.remove(rememberRgp);
            this.repaint();
            this.panels.remove(rememberRgp);
            editor.setReachabilityEnabled(false);
        }

        // if it was the last active editor, set internal frame invisible
        if (this.panels.size() == 0) {
            this.setVisible(false);
        }
    }

    /**
     * Recomputes the graph and refreshes the coverability graph view
     */
    public void refresh() {
        ReachabilityGraphPanel activePanel = getActivePanel();

        if (activePanel == null) return;

        JGraph graph = this.getJGraph(activePanel.getEditor());
        double scale = graph.getScale();

        try {
            activePanel.layoutGraph(activePanel.getSelectedType(), true);
            activePanel.setLogicalFingerPrint(activePanel.getEditor().getModelProcessor().getLogicalFingerprint());
            activePanel.setRefreshButtonEnabled(false);
            activePanel.setGraphOutOfSync(false);
            activePanel.updateVisibility();
        } catch (SimulationRunningException e) {
            activePanel.setRefreshButtonEnabled(true);
            ReachabilityWarning.showReachabilityWarning(activePanel, "QuanlAna.ReachabilityGraph.SimulationWarning");
        }

        graph = this.getJGraph(activePanel.getEditor());
        graph.setScale(scale);
    }

    /**
     * Changes the layout of the current active graph.
     * <p>
     * The layout id's are defined in {@link org.woped.qualanalysis.reachabilitygraph.data.IReachabilityGraphModel}.
     *
     * @param layoutId the algorithm id of the noe layout
     */
    public void switchLayout(int layoutId) {
        ReachabilityGraphPanel activePanel = getActivePanel();
        if (activePanel == null) return;

        try {
            activePanel.layoutGraph(layoutId, false);
        } catch (SimulationRunningException e) {
            ReachabilityWarning.showReachabilityWarning(activePanel, "QuanlAna.ReachabilityGraph.SimulationWarning");
        }
    }

    private ReachabilityGraphPanel getActivePanel() {
        ReachabilityGraphPanel activePanel = null;
        for (ReachabilityGraphPanel panel : panels) {
            if (panel.isShowing()) activePanel = panel;
        }
        return activePanel;
    }

    /**
     * Removes all highlighting from the graph and the related editor
     */
    public void unselect() {
        ReachabilityGraphPanel activePanel = getActivePanel();
        if (activePanel == null) return;

        activePanel.getDefaultGraph().deHighlight();
        activePanel.getEditor().getModelProcessor().resetRGHighlightAndVTokens();
        activePanel.getEditor().setReadOnly(true);
        activePanel.setUnselectButtonEnabled(false);
    }

    /**
     * Closes the view for the graph
     */
    public void close() {
        this.setVisible(false);
        fireAction(CoverabilityGraphActions.FRAME_DEACTIVATED);
    }

    /**
     * Shows the settings dialog for the coverability graph
     */
    public void showSettingsDialog() {
        ReachabilityGraphPanel activePanel = getActivePanel();
        ReachabilitySettingsDialog dialog = new ReachabilitySettingsDialog(activePanel);
        dialog.setVisible(true);
    }

    public void zoomIn() {
        double scale = getActivePanel().getGraph().getScale();
        setZoom(scale + ZOOM_STEP);
    }

    public void zoomOut() {
        double scale = getActivePanel().getGraph().getScale();
        setZoom(scale - ZOOM_STEP);
    }

    public void setZoom(double factor) {

        double ZOOM_MIN = 0.25;
        double ZOOM_MAX = 2.0;

        factor = Math.max(factor, ZOOM_MIN);
        factor = Math.min(factor, ZOOM_MAX);

        getActivePanel().getGraph().setScale(factor);
    }


    /**
     * removes all editors from being showed on the JInternalFrame except the given one.
     *
     * @param editor
     */
    public void updatePanelsVisibility(IEditor editor) {
        LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-> updatePanelsVisibility " + this.getClass().getName());
        for (ReachabilityGraphPanel rgp : panels) {
            if (rgp.getEditor().equals(editor)) {
                this.add(rgp);
                rgp.updateVisibility();
                this.setTitle(Messages.getString("ToolBar.ReachabilityGraph.Title") + " - " + editor.getName());
            } else {
                this.remove(rgp);
            }
        }

        boolean isAnyPanelShowing = false;
        for (ReachabilityGraphPanel rgp : panels) {
            if (rgp.isShowing()) {
                isAnyPanelShowing = true;
            }
        }

        if (!isAnyPanelShowing) {
            this.setTitle(Messages.getString("ToolBar.ReachabilityGraph.Title"));
        }

        this.repaint();
    }

    /**
     * Exports the current coverability graph as image
     */
    public void exportImage() {
        ReachabilityGraphPanel activePanel = getActivePanel();
        activePanel.getGraph();

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

        if (jfc.getSelectedFile() != null && activePanel != null) {

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
                    .getRenderedImage(activePanel), new File(filepath));
        } else if (filetype == FileFilterImpl.PNGFilter) {
            ImageExport.savePNG(ImageExport
                    .getRenderedImage(activePanel), new File(filepath));
        } else if (filetype == FileFilterImpl.BMPFilter) {
            ImageExport.saveBMP(ImageExport
                    .getRenderedImage(activePanel), new File(filepath));
        } else {
            LoggerManager.warn(Constants.QUALANALYSIS_LOGGER,
                    "Unable to save File. Filetype not known: "
                            + filetype);
        }

    }

    private void updateShowingPanelVisibility() {
        LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-> updateShowingPanelVisibilty " + this.getClass().getName());
        for (ReachabilityGraphPanel rgp : panels) {
            if (rgp.isShowing()) {
                rgp.updateVisibility();
            }
        }
    }

    public void internalFrameActivated(InternalFrameEvent e) {
        this.updateShowingPanelVisibility();
        fireAction(CoverabilityGraphActions.FRAME_ACTIVATED);
    }

    public void internalFrameDeactivated(InternalFrameEvent e) {
        fireAction(CoverabilityGraphActions.FRAME_DEACTIVATED);
    }

    private void fireAction(String action) {
        WoPeDAction staticAction = ActionFactory.getStaticAction(action);
        staticAction.actionPerformed(new ViewEvent(this, -1, -1));
    }


    public void internalFrameClosed(InternalFrameEvent e) {
    }

    public void internalFrameClosing(InternalFrameEvent e) {
        dui.refreshFocusOnFrames();
        for (ReachabilityGraphPanel rgp : panels) {
            // Dehighlight RG
            for (int i = 0; i < rgp.getDefaultGraph().getModel().getRootCount(); i++) {
                if (rgp.getDefaultGraph().getModel().getRootAt(i) instanceof ReachabilityPlaceModel) {
                    ((ReachabilityPlaceModel) rgp.getDefaultGraph().getModel().getRootAt(i)).setHighlight(false);
                } else if (rgp.getDefaultGraph().getModel().getRootAt(i) instanceof ReachabilityEdgeModel) {
                    ((ReachabilityEdgeModel) rgp.getDefaultGraph().getModel().getRootAt(i)).setIngoing(false);
                    ((ReachabilityEdgeModel) rgp.getDefaultGraph().getModel().getRootAt(i)).setOutgoing(false);
                }
            }
            rgp.setUnselectButtonEnabled(false);
            rgp.getDefaultGraph().getGraphLayoutCache().reload();
            rgp.getDefaultGraph().clearSelection();

            // Dehighlight Petrinet and reset VirtualTokens
            ((PetriNetModelProcessor) rgp.getEditor().getModelProcessor()).resetRGHighlightAndVTokens();
            rgp.getEditor().setReadOnly(true);

        }
    }

    public void internalFrameDeiconified(InternalFrameEvent e) {
    }

    public void internalFrameIconified(InternalFrameEvent e) {
    }

    public void internalFrameOpened(InternalFrameEvent e) {
    }

    /**
     * Invoked when the mouse wheel is rotated.
     *
     * @param e
     * @see MouseWheelEvent
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        if (e.getWheelRotation() > 0) zoomIn();
        else zoomOut();
    }
}
