package org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.scc;

import java.util.Set;

import org.woped.qualanalysis.soundness.marking.Marking;

/**
 * indicates all strongly connected components in marking net.
 * 
 * * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public interface IStronglyConnectedComponentTest {

    /**
     * 
     * @return true, if only one strongly connected component exist.
     */
    boolean isStronglyConnected();

    /**
     * 
     * @return all strongly connected components. the first set contains the different strongly connected components. the second set includes all marking, which
     *         are part of the strongly connected component.
     */
    Set<Set<Marking>> getStronglyConnectedComponents();
}
