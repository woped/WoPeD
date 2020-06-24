package org.woped.qualanalysis.soundness.algorithms.generic;

import java.util.Set;

/**
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss generic node net.
 * @param <K> generic node type.
 */
public interface INodeNet<K extends INode<K>> {

    /**
     * 
     * @return all contained nodes.
     */
    public Set<K> getAllContainedNodes();

}
