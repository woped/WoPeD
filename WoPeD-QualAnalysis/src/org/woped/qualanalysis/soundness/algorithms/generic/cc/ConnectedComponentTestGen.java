package org.woped.qualanalysis.soundness.algorithms.generic.cc;

import org.woped.qualanalysis.soundness.algorithms.generic.INode;
import org.woped.qualanalysis.soundness.algorithms.generic.INodeNet;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 *         checks a generic node net for connected components.
 * 
 * @param <K> node type.
 */
public class ConnectedComponentTestGen<K extends INode<K>> implements IConnectedComponentTestGen<K> {

    private INodeNet<K> nodeNet;
    private Set<K> visitedNodes;

    public ConnectedComponentTestGen(INodeNet<K> nodeNet) {
        this.nodeNet = nodeNet;
    }

    /**
     * @see IConnectedComponentTestGen#getConnectedComponents()
     */
    @Override
    public Set<Set<K>> getConnectedComponents() {
        Set<Set<K>> ccs; // all connected components.
        Set<K> cc; // connected component

        visitedNodes = new HashSet<K>();
        ccs = new HashSet<Set<K>>();

        for (K node : nodeNet.getAllContainedNodes()) {

            if (!visitedNodes.contains(node)) {
                cc = new HashSet<K>();
                ccs.add(cc);
                checkNodeConnection(cc, node);
            }
        }

        return ccs;
    }

    /**
     * 
     * @param cc actual component of nodes, all these nodes are connected.
     * @param node current visited node
     */
    private void checkNodeConnection(Set<K> cc, K node) {

        // add current node to cc
        cc.add(node);

        // mark node as visited.
        visitedNodes.add(node);

        // visit successors
        for ( K postNode : node.getSuccessorNodes() ) {
            if (!visitedNodes.contains(postNode)) {
                checkNodeConnection(cc, postNode);
            }
        }

        // visit predecessors
        for ( K preNode : node.getPredecessorNodes() ) {
            if (!visitedNodes.contains(preNode)) {
                checkNodeConnection(cc, preNode);
            }
        }
    }

}
