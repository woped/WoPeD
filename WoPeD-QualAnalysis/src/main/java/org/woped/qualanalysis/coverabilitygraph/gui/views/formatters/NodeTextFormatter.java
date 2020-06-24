package org.woped.qualanalysis.coverabilitygraph.gui.views.formatters;

import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphNode;

/**
 * Defines the methods to format the textual representation of a node.
 * <p>
 * The advantage of this class is that the logic of the textual representation is extracted from the node itself.
 * This makes it easy to use different textual representations without changing the node.
 */
public interface NodeTextFormatter {

    /**
     * Gets the textual representation of the node.
     *
     * @param node the node to get the text for
     * @return the textual representation of the node
     */
    String getText(CoverabilityGraphNode node);

    /**
     * Sets the formatter for the textual representation of the marking contained in a node.
     *
     * @param markingFormatter the marking formatter to set
     */
    void setMarkingFormatter(MarkingFormatter markingFormatter);

    /**
     * Gets the currently set marking formatter.
     *
     * @return the current marking formatter
     */
    MarkingFormatter getMarkingFormatter();
}
