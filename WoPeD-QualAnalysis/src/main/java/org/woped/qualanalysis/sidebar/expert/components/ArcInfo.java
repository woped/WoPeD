package org.woped.qualanalysis.sidebar.expert.components;

import org.woped.core.model.ArcModel;

/**
 * This class provides information about an arc in the expert page.
 */
public class ArcInfo extends NetInfo {

    private ArcModel arc;

    /**
     * Constructs a new arc info for the provided arc.
     *
     * @param arc the arc to display the info for
     */
    ArcInfo(ArcModel arc) {
        super(arc.getId());
        this.arc = arc;
    }

    @Override
    public Object[] getReferencedElements() {
        return new Object[]{arc};
    }
}
