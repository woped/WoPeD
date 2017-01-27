package org.woped.qualanalysis.coverabilitygraph.assistant.event;

import org.woped.qualanalysis.coverabilitygraph.events.EdgeEvent;
import org.woped.qualanalysis.coverabilitygraph.events.NodeEvent;

/**
 * An abstract class for receiving coverability graph events. The mehtods in this class are empty. This class exists for convenience
 * for creating listeners objects.
 * <p>
 * Extend this class if you are only interested in a subset of events provided by listener interface.
 */
public abstract class CoverabilityGraphAdapter implements CoverabilityGraphListener {

    @Override
    public void edgeAdded(EdgeEvent event) {

    }

    @Override
    public void edgeRemoved(EdgeEvent event) {

    }

    @Override
    public void edgeReconnected(EdgeReconnectedEvent event) {

    }

    @Override
    public void edgeModified(EdgeModifiedEvent event) {

    }

    @Override
    public void nodeAdded(NodeEvent event) {

    }

    @Override
    public void nodeRemoved(NodeEvent event) {

    }

    @Override
    public void nodeModified(NodeModifiedEvent event) {

    }

    @Override
    public void deselectGraphRequested() {

    }

    @Override
    public void refreshRequested() {

    }

    @Override
    public void prepareRestartRequested() {

    }

    @Override
    public void restartRequested() {

    }
}
