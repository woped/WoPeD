package org.woped.qualanalysis.soundness.algorithms.generic.scc;

import org.woped.qualanalysis.soundness.algorithms.generic.INode;
import org.woped.qualanalysis.soundness.algorithms.generic.INodeNet;

import java.util.*;

/**
 * indicates strongly connected components in a generic net. uses the tarjan algorithm.
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * @see IStronglyConnectedComponentTestGen
 */
public class StronglyConnectedComponentTestGenTarjan<K extends INode<K>> implements
        IStronglyConnectedComponentTestGen<K> {

    private INodeNet<K> nodeNet;
    private Integer maxdfs = Integer.valueOf(0);
    private Set<Set<K>> stronglyConnectedComponents;
    private HashSet<K> nodes;
    private Stack<K> visitedNodes;

    private Map<K, Integer> dfs;
    private Map<K, Integer> lowLink;

    /**
     *
     * @param nodeNet MarkingNet the algorithm is based on
     */
    public StronglyConnectedComponentTestGenTarjan(INodeNet<K> nodeNet) {
        this.nodeNet = nodeNet;
    }

    /**
     * init method.
     */
    @SuppressWarnings("unchecked")
	private void init() {

        maxdfs = 0;
        nodes = new HashSet<K>();
        dfs = new HashMap<K, Integer>();
        lowLink = new HashMap<K, Integer>();
        visitedNodes = new Stack<K>();
        stronglyConnectedComponents = new HashSet<Set<K>>();

        for (K node : nodeNet.getAllContainedNodes()) {
            nodes.add(node);
            dfs.put(node, Integer.valueOf(-1));
            lowLink.put(node, Integer.valueOf(0));
        }

        while (nodes.size() > 0) {
            tarjan((K) nodes.toArray()[0]);
        }
    }

    /**
     * @see IStronglyConnectedComponentTestGen#getStronglyConnectedComponents()
     * @return a set of strongly connected components.
     */
    @Override
    public Set<Set<K>> getStronglyConnectedComponents() {
        init();
        return stronglyConnectedComponents;
    }

    @Override
    public boolean isStronglyConnected() {
        init();
        return stronglyConnectedComponents.size() <= 1;
    }

    /**
     * tarjan algorithm.
     *
     * @param node the node to check
     * @return a set of strongly connected components. each strongly connected component set consists of markings, which are part of the scc.
     */
    private Set<Set<K>> tarjan(K node) {
        dfs.put(node, maxdfs);
        lowLink.put(node, maxdfs);
        maxdfs++;
        visitedNodes.add(0, node);
        nodes.remove(node);

        for ( K nextNode : node.getSuccessorNodes() ) {
            if (dfs.get(nextNode) == -1) {
                tarjan(nextNode);
                lowLink.put(node, Math.min(lowLink.get(node), lowLink.get(nextNode)));
                // marking.lowlink = Math.min(marking.lowlink, nextMarking.lowlink);
            } else
                if (visitedNodes.contains(nextNode)) {
                    lowLink.put(node, Math.min(lowLink.get(node), dfs.get(nextNode)));
                    // marking.lowlink = Math.min(marking.lowlink, nextMarking.dfs);
                }
        }
        if (lowLink.get(node) == dfs.get(node)) {
            K reachable;
            HashSet<K> stronglyConnectedComponent = new HashSet<K>();
            do {
                reachable = visitedNodes.remove(0);
                stronglyConnectedComponent.add(reachable);
            } while (reachable != node);
            stronglyConnectedComponents.add(stronglyConnectedComponent);
        }
        return stronglyConnectedComponents;
    }

}
