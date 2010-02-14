package org.woped.qualanalysis.soundness.builder.markingnet;

import org.woped.qualanalysis.soundness.datamodel.LowLevelPetriNet;
import org.woped.qualanalysis.soundness.marking.MarkingNet;

/**
 * abstract builder for marking net.
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public abstract class AbstractMarkingNetBuilder {
    protected LowLevelPetriNet lolNet;
    protected MarkingNet mNet;

    /**
     * 
     * @param lolNet source low level petri net.
     */
    public AbstractMarkingNetBuilder(LowLevelPetriNet lolNet) {
        this.lolNet = lolNet;
        initMarkingNet();
        createMarkingNet();
    }

    /**
     * 
     * @return created marking net.
     */
    public MarkingNet getMarkingNet() {
        return mNet;
    }

    /**
     * init marking net.
     */
    protected void initMarkingNet() {
        mNet = new MarkingNet(lolNet);
    }

    /**
     * create marking net.
     */
    public abstract void createMarkingNet();
}
