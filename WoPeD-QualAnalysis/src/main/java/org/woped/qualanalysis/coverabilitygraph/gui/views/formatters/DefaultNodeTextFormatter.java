package org.woped.qualanalysis.coverabilitygraph.gui.views.formatters;

import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphNode;

/**
 * This class contains the default settings for a node text formatter.
 * <p>
 * It uses the text provided by the marking formatter without any decoration.
 */
public class DefaultNodeTextFormatter implements NodeTextFormatter{

    protected MarkingFormatter markingFormatter;

    /**
     * Constructs a new node text formatter with an {@link MultiSetMarkingFormatter} as marking formatter.
     */
    public DefaultNodeTextFormatter(){
        this(new MultiSetMarkingFormatter());
    }

    /**
     * Constructs a new node text formatter with the provided marking formatter.
     *
     * @param markingFormatter the marking formatter to create the textual representation of the marking
     */
    @SuppressWarnings("WeakerAccess")
    public DefaultNodeTextFormatter(MarkingFormatter markingFormatter){
        this.markingFormatter = markingFormatter;
    }

    @Override
    public String getText(CoverabilityGraphNode node) {
        return markingFormatter.getText(node.getMarking());
    }

    @Override
    public void setMarkingFormatter(MarkingFormatter markingFormatter) {
        this.markingFormatter = markingFormatter;
    }

    @Override
    public MarkingFormatter getMarkingFormatter() {
        return markingFormatter;
    }
}
