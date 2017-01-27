package org.woped.qualanalysis.coverabilitygraph.gui;

/**
 * Defines the events related to the highlighting of a graph
 */
interface HighlightListener {

    /**
     * Invoked when a highlight has been added to the graph
     *
     * @param source the view controller of the highlighted graph
     */
    void highlightAdded(CoverabilityGraphVC source);

    /**
     * Invoked when the highlighting has been removed from the graph
     *
     * @param source the view controller of the highlighted graph
     */
    void highlightRemoved(CoverabilityGraphVC source);
}
