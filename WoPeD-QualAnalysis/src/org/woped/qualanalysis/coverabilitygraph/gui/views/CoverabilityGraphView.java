package org.woped.qualanalysis.coverabilitygraph.gui.views;

import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphModel;
import org.woped.qualanalysis.coverabilitygraph.events.CoverabilityGraphMouseListener;
import org.woped.qualanalysis.coverabilitygraph.gui.CoverabilityGraphSettings;
import org.woped.qualanalysis.coverabilitygraph.gui.CoverabilityGraphVC;
import org.woped.qualanalysis.coverabilitygraph.gui.ZoomController;

import javax.swing.*;

/**
 * This class defines the public functionality of an coverability graph view that can be registered in a {@link CoverabilityGraphVC}.
 */
public abstract class CoverabilityGraphView extends JPanel {
    /**
     * Refreshes the view.
     */
    public abstract void refresh();

    /**
     * Resets the view to its initial state.
     */
    public abstract void reset();

    /**
     * Notifies the view that it is out of sync with the petri net.
     */
    public abstract void setOutOfSync();

    /**
     * Gets the current settings of the view.
     *
     * @return the current settings of the view
     */
    public abstract CoverabilityGraphSettings getSettings();

    /**
     * Applies the provided settings to the view.
     *
     * @param settings the new settings
     */
    public abstract void applySettings(CoverabilityGraphSettings settings);

    /**
     * Gets the zoom controller of the view.
     *
     * @return the zoom controller of the view
     */
    public abstract ZoomController getZoomController();

    /**
     * Gets the graph model of the view.
     *
     * @return the graph model of the view
     */
    public abstract CoverabilityGraphModel getGraphModel();

    /**
     * Removes all highlighting from the view.
     */
    public abstract void removeHighlighting();

    /**
     * Adds a listener that is interested in mouse events from the graph.
     *
     * @param listener the listener to add
     */
    public abstract void addCoverabilityGraphMouseListener(CoverabilityGraphMouseListener listener);
}
