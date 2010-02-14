package org.woped.qualanalysis.soundness.algorithms.basedonlowlevelpetrinet;

import org.woped.qualanalysis.soundness.datamodel.LowLevelPetriNet;

/**
 * abstract class for tests basing on LowLevelPetriNets
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 */
public abstract class AbstractLowLevelPetriNetTest {
    protected LowLevelPetriNet lolNet;

    /**
     * 
     * @param lolNet LowLevelPetriNet the algorithm is based on
     */
    public AbstractLowLevelPetriNetTest(LowLevelPetriNet lolNet) {
        this.lolNet = lolNet;
    }
}
