package org.woped.qualanalysis.coverabilitygraph.gui.views.formatters;

import org.woped.qualanalysis.soundness.marking.IMarking;

/**
 * This class defines the methods to get the textual representation of an marking.
 * <p>
 * The advantage of the extraction of this toString logic is that you can have different representations (e.g. MultiSet, TokenVector)
 * without changing the marking class itself.
 */
public interface MarkingFormatter {

    /**
     * Gets the textual representation of the provided marking.
     *
     * @param marking the marking to get the text for
     * @return the textual representation of the marking
     */
    String getText(IMarking marking);
}
