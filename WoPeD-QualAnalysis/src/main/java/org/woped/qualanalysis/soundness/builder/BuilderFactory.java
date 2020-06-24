package org.woped.qualanalysis.soundness.builder;

import org.woped.core.controller.IEditor;
import org.woped.qualanalysis.soundness.builder.currentmarking.CurrentMarking;
import org.woped.qualanalysis.soundness.builder.lowlevelpetrinet.AbstractLowLevelPetriNetBuilder;
import org.woped.qualanalysis.soundness.builder.lowlevelpetrinet.LowLevelPetriNetWithTStarBuilder;
import org.woped.qualanalysis.soundness.builder.lowlevelpetrinet.LowLevelPetriNetWithoutTStarBuilder;
import org.woped.qualanalysis.soundness.builder.markingnet.MarkingNetBuilderBook;
import org.woped.qualanalysis.soundness.datamodel.ILowLevelPetriNet;
import org.woped.qualanalysis.soundness.marking.IMarking;
import org.woped.qualanalysis.soundness.marking.IMarkingNet;

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
    public static IMarkingNet createMarkingNet(ILowLevelPetriNet lolNet) {
        return new MarkingNetBuilderBook(lolNet).getMarkingNet();
    }
    
    /**
     * 
     * @param lolNet LowLevelPetriNet to calculate current marking from
     * @param useVirtualTokens true if the virtual tokenCount must be used for calculation (necessary for tokengame-support)
     * @return
     */
    public static IMarking createCurrentMarking(ILowLevelPetriNet lolNet, Boolean useVirtualTokens) {
        return new CurrentMarking(lolNet, useVirtualTokens).getMarking();
    }

}
