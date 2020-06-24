package org.woped.qualanalysis.soundness.algorithms.basedonlowlevelpetrinet;

import org.woped.qualanalysis.soundness.datamodel.ILowLevelPetriNet;

/**
 * abstract class for tests basing on LowLevelPetriNets
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 */
public abstract class AbstractLowLevelPetriNetTest {
    protected ILowLevelPetriNet lolNet;

    /**
     * 
     * @param lolNet LowLevelPetriNet the algorithm is based on
     */
    public AbstractLowLevelPetriNetTest(ILowLevelPetriNet lolNet) {
        this.lolNet = lolNet;
    }
}
