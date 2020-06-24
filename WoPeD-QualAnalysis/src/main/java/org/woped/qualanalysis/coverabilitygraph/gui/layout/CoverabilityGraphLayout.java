package org.woped.qualanalysis.coverabilitygraph.gui.layout;

import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphModel;

/**
 * Defines the basic methods for graph layout algorithms.
 */
public interface CoverabilityGraphLayout {

    /**
     * Applies the layout to the provided model.
     *
     * @param model the model to apply the layout to.
     */
    void applyLayout(CoverabilityGraphModel model);
}
