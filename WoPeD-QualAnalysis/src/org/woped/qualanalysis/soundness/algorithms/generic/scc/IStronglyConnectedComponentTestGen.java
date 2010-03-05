package org.woped.qualanalysis.soundness.algorithms.generic.scc;

import java.util.Set;

import org.woped.qualanalysis.soundness.algorithms.generic.INode;

/**
 * indicates all strongly connected components in generic net.
 * 
 * * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public interface IStronglyConnectedComponentTestGen<K extends INode<K>> {

    /**
     * 
     * @return true, if only one strongly connected component exist.
     */
    boolean isStronglyConnected();

    /**
     * 
     * @return all strongly connected components. the first set contains the different strongly connected components. the second set includes all nodes, which
     *         are part of the strongly connected component.
     */
    Set<Set<K>> getStronglyConnectedComponents();
}
