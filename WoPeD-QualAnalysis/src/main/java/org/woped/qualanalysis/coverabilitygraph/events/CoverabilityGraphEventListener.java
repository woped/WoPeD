package org.woped.qualanalysis.coverabilitygraph.events;

/**
 * Defines basic graph events.
 */
public interface CoverabilityGraphEventListener {

    /**
     * Invoked if the graph and the underlying petri net are out of sync.
     */
    void editorSyncLost();

    /**
     * Invoked, when the synchronisation between the graph and the underlying petri net has been renewed.
     */
    void editorSyncEstablished();

    /**
     * Invoked when a highlighting has been added to the graph.
     */
    void graphHighlightingAdded();

    /**
     * Invoked when all highlighting has been removed from the graph.
     */
    void graphHighlightingRemoved();
}
