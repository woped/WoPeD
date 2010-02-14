package org.woped.qualanalysis.soundness.builder;

import org.woped.core.controller.IEditor;
import org.woped.qualanalysis.soundness.builder.lowlevelpetrinet.AbstractLowLevelPetriNetBuilder;
import org.woped.qualanalysis.soundness.builder.lowlevelpetrinet.LowLevelPetriNetWithTStarBuilder;
import org.woped.qualanalysis.soundness.builder.lowlevelpetrinet.LowLevelPetriNetWithoutTStarBuilder;
import org.woped.qualanalysis.soundness.builder.markingnet.MarkingNetBuilderBook;
import org.woped.qualanalysis.soundness.datamodel.LowLevelPetriNet;
import org.woped.qualanalysis.soundness.marking.MarkingNet;

/**
 * factory class. creates builders.
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public class BuilderFactory {

	/**
	 * 
	 * @param editor source object
	 * @return
	 */
    public static AbstractLowLevelPetriNetBuilder createLowLevelPetriNetWithoutTStarBuilder(IEditor editor) {
        return new LowLevelPetriNetWithoutTStarBuilder(editor);
    }

    /**
     * 
     * @param editor source object
     * @return
     */
    public static AbstractLowLevelPetriNetBuilder createLowLevelPetriNetWithTStarBuilder(IEditor editor) {
        return new LowLevelPetriNetWithTStarBuilder(editor);
    }

    /**
     * 
     * @param lolNet LowLevelPetriNet to calculate MarkingNet from
     * @return
     */
    public static MarkingNet createMarkingNet(LowLevelPetriNet lolNet) {
        return new MarkingNetBuilderBook(lolNet).getMarkingNet();
    }

}
