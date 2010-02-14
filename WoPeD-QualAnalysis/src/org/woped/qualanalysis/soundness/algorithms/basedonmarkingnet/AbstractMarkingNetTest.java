package org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet;

import org.woped.qualanalysis.soundness.marking.MarkingNet;

/**
 * abstract class for tests basing on MarkingNets
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 */
public abstract class AbstractMarkingNetTest {
    protected MarkingNet mNet;

	/**
	 * 
	 * @param mNet MarkingNet the algorithm is based on
	 */
    public AbstractMarkingNetTest(MarkingNet mNet) {
        this.mNet = mNet;
    }
}
