package org.woped.qualanalysis.coverabilitygraph.gui.layout.tree;

import java.util.Collection;
import java.util.List;

import org.abego.treelayout.util.AbstractTreeForTreeLayout;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphNode;

/**
 * This class wraps {@link CoverabilityGraphNode}s to provided the necessary interface for the tree layout.
 */
class TreeNode extends AbstractTreeForTreeLayout<CoverabilityGraphNode> {

    /**
     * Constructs a new layoutable tree.
     *
     * @param root the root node of the tree
     */
    TreeNode(CoverabilityGraphNode root) {
        super(root);
    }

    @Override
    public CoverabilityGraphNode getParent(CoverabilityGraphNode node) {

        Collection<CoverabilityGraphNode> ancestors = node.getDirectAncestors();

        if(ancestors.isEmpty()) return null;

        if(ancestors.size()> 1) throw new IllegalStateException("There is more than one parent.");
        return ancestors.iterator().next();
    }

    @Override
    public List<CoverabilityGraphNode> getChildrenList(CoverabilityGraphNode node) {
        return (List<CoverabilityGraphNode>) node.getDirectDescendants();
    }
}
