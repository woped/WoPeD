package org.woped.qualanalysis.coverabilitygraph.gui.views.formatters;

import org.woped.qualanalysis.coverabilitygraph.gui.views.CoverabilityGraphColorScheme;

/**
 * This class is the default implementation for a {@link NodeFormatter}.
 */
public class DefaultNodeFormatter implements NodeFormatter {

    private CoverabilityGraphColorScheme colorScheme;
    private NodeTextFormatter nodeTextFormatter;

    /**
     * Constructs a new node formatter with gray scaled color scheme and default node text formatter.
     */
    public DefaultNodeFormatter(){
        this.colorScheme = CoverabilityGraphColorScheme.GRAY_SCALED_SCHEME();
        this.nodeTextFormatter = new DefaultNodeTextFormatter();
    }

    @Override
    public CoverabilityGraphColorScheme getColorScheme() {
        return colorScheme;
    }

    @Override
    public void setColorScheme(CoverabilityGraphColorScheme colorScheme) {
        this.colorScheme = colorScheme;
    }

    @Override
    public NodeTextFormatter getNodeTextFormatter() {
        return nodeTextFormatter;
    }

    @Override
    public void setNodeTextFormatter(NodeTextFormatter nodeTextFormatter) {
        this.nodeTextFormatter = nodeTextFormatter;
    }
}
