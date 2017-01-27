package org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.model;

import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphNode;
import org.woped.qualanalysis.coverabilitygraph.gui.views.formatters.DefaultNodeTextFormatter;

/**
 * Decorates the textual representation of a {@link MpNode} with additional information about its analysis.
 */
public class MpNodeTextFormatter extends DefaultNodeTextFormatter{

    @Override
    public String getText(CoverabilityGraphNode node) {

        if(!(node instanceof MpNode)) return super.getText(node);
        MpNode n = (MpNode) node;

        if(n.getState() == MpNodeState.UNPROCESSED) return "?";

        String text = String.format("%d : %s", n.getProcessedInStep(), markingFormatter.getText(n.getMarking()));

        if(n.getState()== MpNodeState.INACTIVE){
            text = String.format("%s : %d", text, n.getDeactivatedInStep());
        }

        return text;
    }
}
