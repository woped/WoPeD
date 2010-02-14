package org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.scc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.AbstractMarkingNetTest;
import org.woped.qualanalysis.soundness.marking.Arc;
import org.woped.qualanalysis.soundness.marking.Marking;
import org.woped.qualanalysis.soundness.marking.MarkingNet;

/**
 * indicates strongly connected components in a marking net. uses the tarjan algorithm.
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * @see IStronglyConnectedComponentTest
 */
public class StronglyConnectedComponentTestTarjan extends AbstractMarkingNetTest implements
        IStronglyConnectedComponentTest {
    private Integer maxdfs = Integer.valueOf(0);
    private Set<Set<Marking>> stronglyConnectedComponents;
    private HashSet<Marking> markings;
    private Stack<Marking> visitedMarkings;

    private Map<Marking, Integer> dfs;
    private Map<Marking, Integer> lowLink;

	/**
	 * 
	 * @param mNet MarkingNet the algorithm is based on
	 */
    public StronglyConnectedComponentTestTarjan(MarkingNet mNet) {
        super(mNet);
    }

    /**
     * init method.
     */
    private void init() {
        maxdfs = 0;
        markings = new HashSet<Marking>();
        dfs = new HashMap<Marking, Integer>();
        lowLink = new HashMap<Marking, Integer>();
        visitedMarkings = new Stack<Marking>();
        stronglyConnectedComponents = new HashSet<Set<Marking>>();

        for (Marking marking : mNet.getMarkings()) {
            markings.add(marking);
            dfs.put(marking, Integer.valueOf(-1));
            lowLink.put(marking, Integer.valueOf(0));
        }
    }

    /**
     * @see IStronglyConnectedComponentTest#getStronglyConnectedComponents()
     * @return a set of strongly connected components.
     */
    @Override
    public Set<Set<Marking>> getStronglyConnectedComponents() {
        return stronglyConnectedComponents;
    }


    @Override
    public boolean isStronglyConnected() {
        init();

        while (markings.size() > 0) {
            tarjan((Marking) markings.toArray()[0]);
        }
        if (stronglyConnectedComponents.size() > 1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * tarjan algorithm.
     * 
     * @param marking
     * @return a set of strongly connected components. each strongly connected component set consists of markings, which are part of the scc.
     */
    private Set<Set<Marking>> tarjan(Marking marking) {
        dfs.put(marking, maxdfs);
        lowLink.put(marking, maxdfs);
        maxdfs++;
        visitedMarkings.add(0, marking);
        markings.remove(marking);

        for (Arc arc : marking.getSuccessors()) {
            Marking nextMarking = arc.getTarget();
            if (dfs.get(nextMarking) == -1) {
                tarjan(nextMarking);
                lowLink.put(marking, Math.min(lowLink.get(marking), lowLink.get(nextMarking)));
                // marking.lowlink = Math.min(marking.lowlink, nextMarking.lowlink);
            } else
                if (visitedMarkings.contains(nextMarking)) {
                    lowLink.put(marking, Math.min(lowLink.get(marking), dfs.get(nextMarking)));
                    // marking.lowlink = Math.min(marking.lowlink, nextMarking.dfs);
                }
        }
        if (lowLink.get(marking) == dfs.get(marking)) {
            Marking reachable;
            HashSet<Marking> stronglyConnectedComponent = new HashSet<Marking>();
            do {
                reachable = visitedMarkings.remove(0);
                stronglyConnectedComponent.add(reachable);
            } while (reachable != marking);
            stronglyConnectedComponents.add(stronglyConnectedComponent);
        }
        return stronglyConnectedComponents;
    }

}
