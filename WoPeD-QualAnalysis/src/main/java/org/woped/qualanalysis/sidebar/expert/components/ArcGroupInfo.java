package org.woped.qualanalysis.sidebar.expert.components;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.woped.core.model.ArcModel;

/**
 * This class is provides information about a group of arcs in the expert view.
 */
public class ArcGroupInfo extends NetInfo {

    /**
     * Creates a new arc group info.
     *
     * @param title the title of the node
     * @param arcs the arcs contained in the group
     */
    protected ArcGroupInfo(String title, Collection<ArcModel> arcs) {
        super(String.format("%s: %d", title, arcs.size()));

        for(ArcModel arc : arcs){
            add(new ArcInfo(arc));
        }
    }

    @Override
    public Object[] getReferencedElements() {
       Set<Object> elements = new HashSet<>();

        for(Object n : children){
            if(!(n instanceof ArcInfo)) continue;

            ArcInfo ai = (ArcInfo) n;
            elements.addAll(Arrays.asList(ai.getReferencedElements()));
        }

        return elements.toArray();
    }
}
