package org.woped.qualanalysis.soundness.algorithms.generic;

import java.util.Set;

/**
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 *         generic node. is part of the generic node net.
 * @param <K> generic node type
 */
public interface INode<K> {
    /**
     * 
     * @return all successors.
     */
    public Set<K> getPostNodes();

    /**
     * 
     * @return all predecessors.
     */
    public Set<K> getPreNodes();
}
