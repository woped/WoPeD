package org.woped.qualanalysis.soundness.algorithms.generic.cc;

import java.util.Set;

import org.woped.qualanalysis.soundness.algorithms.generic.INode;

/**
 * indicates all strongly connected components in generic net.
 * 
 * * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public interface IConnectedComponentTestGen<K extends INode<K>> {

    /**
     * 
     * @return set of connected components. the connected component is a set of the generic type.
     */
    Set<Set<K>> getConnectedComponents();

}
