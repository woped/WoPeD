package org.woped.qualanalysis.coverabilitygraph.gui.views.formatters;

import org.woped.qualanalysis.soundness.marking.IMarking;

/**
 * Formats markings as token vector.
 * <p>
 * A token vector lists only the amount of tokens sorted by the identifier of the places.
 * e.g ( 1 0 2 ) means 1 token in p1, 0 tokens in p2, 2 tokens in p3.
 * <p>
 * This notation is best fit for markings with few places.
 */
public class TokenVectorMarkingFormatter implements MarkingFormatter {

    @Override
    public String getText(IMarking marking) {
        return marking.asTokenVectorString();
    }
}
