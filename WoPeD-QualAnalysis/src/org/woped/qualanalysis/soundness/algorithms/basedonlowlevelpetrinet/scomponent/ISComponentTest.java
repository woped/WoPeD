package org.woped.qualanalysis.soundness.algorithms.basedonlowlevelpetrinet.scomponent;

import java.util.Set;

import org.woped.qualanalysis.soundness.datamodel.AbstractNode;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;

/**
 *interface for s-components tests.
 *
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 *
 */
public interface ISComponentTest {
    /**
     * 
     * @return the number of sComponents.
     */
    public int getNumSComponents();

    /**
     * 
     * @return a set of all sComponents.
     */
    public Set<Set<AbstractNode>> getSComponents();

    /**
     * 
     * @return the number of not-SCovered places.
     */
    public int getNumNotSCovered();

    /**
     * 
     * @return an iterator of all not-SCovered places.
     */
    public Set<PlaceNode> getNotSCovered();
}
