package org.woped.qualanalysis.coverabilitygraph.gui.views.formatters;

import org.woped.qualanalysis.coverabilitygraph.gui.views.CoverabilityGraphColorScheme;

/**
 * Defines the methods for the automatic formatting of a coverability graph node which is applied if no other format is set.
 * Individual setting set to the node attributes always overrides this settings.
 */
public interface NodeFormatter {

    /**
     * Gets the currently set color scheme.
     *
     * @return the current color scheme
     */
    CoverabilityGraphColorScheme getColorScheme();

    /**
     * Sets the color scheme to the provided value.
     *
     * @param colorScheme the color scheme to set
     */
    void setColorScheme(CoverabilityGraphColorScheme colorScheme);

    /**
     * Gets the currently set node text formatter.
     *
     * @return the current text formatter
     */
    NodeTextFormatter getNodeTextFormatter();

    /**
     * Sets the node text formatter to the provided value.
     *
     * @param nodeTextFormatter the node text formatter to set.
     */
    void setNodeTextFormatter(NodeTextFormatter nodeTextFormatter);
}
