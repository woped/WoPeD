package org.woped.qualanalysis.soundness.algorithms.generic;

import java.util.Set;

public interface INode<K> {
    public Set<K> getPostNodes();
}
