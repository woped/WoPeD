package org.woped.qualanalysis.coverabilitygraph.gui.views.formatters;

import org.woped.qualanalysis.soundness.marking.IMarking;

/**
 * Formats markings as multi set.
 * <p>
 * A multiset lists the amount of tokens direct in front of the place.
 * e.g. 2p1 means 2 tokens in place p1
 * <p>
 * Places without tokens are not contained in this notation. This increases the readability for markings with many places without tokens.
 * e.g. ( 0 0 0 0 1 0 0 0 ) compared to ( 1p5 )
 */
public class MultiSetMarkingFormatter implements MarkingFormatter {

    @Override
    public String getText(IMarking marking) {
        return marking.asMultiSetString();
    }
}
