package org.woped.qualanalysis.soundness.algorithms.generic;

import java.util.Set;

public interface INodeNet<K extends INode<K>> {

    public Set<K> getAllContainedNodes();

}
