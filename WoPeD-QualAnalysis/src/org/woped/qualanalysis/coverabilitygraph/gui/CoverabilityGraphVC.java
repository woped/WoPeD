package org.woped.qualanalysis.coverabilitygraph.gui;

import org.woped.core.controller.IEditor;
import org.woped.core.utilities.LoggerManager;
import org.woped.qualanalysis.Constants;
import org.woped.qualanalysis.coverabilitygraph.events.CoverabilityGraphMouseAdapter;
import org.woped.qualanalysis.coverabilitygraph.events.EdgeClickedEvent;
import org.woped.qualanalysis.coverabilitygraph.events.EmptySpaceClickedEvent;
import org.woped.qualanalysis.coverabilitygraph.events.NodeClickedEvent;
import org.woped.qualanalysis.coverabilitygraph.gui.views.CoverabilityGraphResultView;
import org.woped.qualanalysis.coverabilitygraph.gui.views.CoverabilityGraphView;
import org.woped.qualanalysis.soundness.marking.IMarking;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * This class manages different views of the coverability graph for one petri net.
 * <p>
 * Different views are e.g. the result view or the assistant view.
 */
public class CoverabilityGraphVC extends JPanel {

    private static final long serialVersionUID = 1L;
    private IEditor editor = null;
    private Map<String, CoverabilityGraphView> views;
    private CoverabilityGraphView activeView;

    private EditorValidator editorValidator;
    private GraphHighlighter graphHighlighter;

    /**
     * Constructs a new view controller for the coverability graph of a petri net.
     *
     * @param editor the editor of the petri net
     */
    CoverabilityGraphVC(IEditor editor) {
        super();
        this.editor = editor;
        this.editorValidator = new EditorValidator(editor);
        this.editor.updateNet();

        initialize();
    }

    /**
     * Returns if the view for the provided key has been registered in this view controller.
     *
     * @param key the key of the view
     * @return true if the view has been registered, otherwise false
     */
    boolean containsView(String key) {
        return views.containsKey(key);
    }

    /**
     * Registers a specific coverability graph view.
     * <p>
     * All views have to be registered before they can be activated.
     *
     * @param key  the unique identifier for the view
     * @param view the view to register
     */
    void registerView(String key, CoverabilityGraphView view) {
        views.put(key, view);
        view.addCoverabilityGraphMouseListener(graphHighlighter);
    }

    /**
     * Activates the view that has been registered with the provided key.
     *
     * @param key the key of the registered view
     * @throws IllegalStateException if no view with the provided key has been registered
     */
    void activateView(String key) {

        if (!views.containsKey(key)) throw new IllegalStateException("No view found for key " + key);

        if (activeView != null) {
            activeView.removeHighlighting();
            this.remove(activeView);
        }
        activeView = views.get(key);
        this.add(activeView);

        refresh();
    }

    /**
     * Gets the currently active view.
     *
     * @return the active view
     */
    public CoverabilityGraphView getActiveView() {
        return activeView;
    }

    /**
     * Recomputes the active view and resets it to its initial state.
     */
    public void reset() {
        editorValidator.acceptChanges();
        activeView.reset();

        refresh();
    }

    /**
     * Highlight all markings which covers the provide marking.
     *
     * @param marking the marking to highlight
     */
    void highlightMarking(IMarking marking) {
        activeView.highlightMarking(marking);
    }

    /**
     * Removes all highlighting form the active view.
     */
    void removeHighlighting() {
        graphHighlighter.fireGraphHighlightingRemovedEvent();
        activeView.removeHighlighting();
    }

    /**
     * Gets the graph of the current active view.
     *
     * @return the graph of the active view
     */
    public CoverabilityGraph getGraph() {
        return this.getActiveView().getGraphModel().getGraph();
    }

    /**
     * Gets a value representing if the coverability graph is out of sync with the underlying petri net.
     *
     * @return true if the graph is out of sync, otherwise false
     */
    boolean isGraphOutOfSync() {

        boolean outOfSync = editorValidator.hasChanged();
        if (outOfSync) {
            activeView.setOutOfSync();
        }

        return outOfSync;
    }

    /**
     * Adds the specified highlight listener to receive highlight events to this class.
     *
     * @param listener the highlight listener
     */
    void addGraphHighlightListener(HighlightListener listener) {
        this.graphHighlighter.addListener(listener);
    }

    private void initialize() {
        LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-> init() " + this.getClass().getName());

        views = new HashMap<>();

        this.setLayout(new BorderLayout());
        activeView = new CoverabilityGraphResultView(editor);

        graphHighlighter = new GraphHighlighter();

        this.add(activeView, BorderLayout.CENTER);

        LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- init() " + this.getClass().getName());
    }

    private void refresh() {

        activeView.refresh();
        validate();
        repaint();
    }

    /**
     * Handles the enabled state of the deselect action
     */
    private class GraphHighlighter extends CoverabilityGraphMouseAdapter {

        private Collection<HighlightListener> listeners;

        GraphHighlighter() {
            this.listeners = new LinkedList<>();
        }

        void addListener(HighlightListener listener) {
            this.listeners.add(listener);
        }

        void fireGraphHighlightedEvent() {
            for (HighlightListener listener : listeners) {
                listener.highlightAdded(CoverabilityGraphVC.this);
            }
        }

        void fireGraphHighlightingRemovedEvent() {
            for (HighlightListener listener : listeners) {
                listener.highlightRemoved(CoverabilityGraphVC.this);
            }
        }

        @Override
        public void nodeClicked(NodeClickedEvent event) {
            fireGraphHighlightedEvent();
        }

        @Override
        public void edgeClicked(EdgeClickedEvent event) {
            fireGraphHighlightedEvent();
        }

        @Override
        public void emptySpaceClicked(EmptySpaceClickedEvent event) {
            fireGraphHighlightingRemovedEvent();
        }

    }
}