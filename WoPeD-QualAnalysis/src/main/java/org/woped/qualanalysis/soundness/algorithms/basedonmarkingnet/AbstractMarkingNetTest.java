package org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet;

import org.woped.qualanalysis.soundness.marking.IMarkingNet;

/**
 * abstract class for tests basing on MarkingNets
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 */
public abstract class AbstractMarkingNetTest {
    protected IMarkingNet mNet;

	/**
	 * 
	 * @param iMarkingNet MarkingNet the algorithm is based on
	 */
    public AbstractMarkingNetTest(IMarkingNet markingNet) {
        this.mNet = markingNet;
    }
}
