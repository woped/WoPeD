package org.woped.qualanalysis.soundness.builder.markingnet;

import org.woped.qualanalysis.soundness.datamodel.ILowLevelPetriNet;
import org.woped.qualanalysis.soundness.marking.IMarkingNet;
import org.woped.qualanalysis.soundness.marking.MarkingNet;

/**
 * abstract builder for marking net.
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public abstract class AbstractMarkingNetBuilder {
    protected ILowLevelPetriNet lolNet;
    protected IMarkingNet mNet;

    /**
     * 
     * @param lolNet source low level petri net.
     */
    public AbstractMarkingNetBuilder(ILowLevelPetriNet lolNet) {
        this.lolNet = lolNet;
        initMarkingNet();
        createMarkingNet();
    }

    /**
     * 
     * @return created marking net.
     */
    public IMarkingNet getMarkingNet() {
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
