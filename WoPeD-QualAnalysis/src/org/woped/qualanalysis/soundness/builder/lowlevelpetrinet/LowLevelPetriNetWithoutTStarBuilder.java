package org.woped.qualanalysis.soundness.builder.lowlevelpetrinet;

import org.woped.core.controller.IEditor;

/**
 * creates an lowlevel petri net from the provided source.
 * 
 * @see AbstractLowLevelPetriNetBuilderUsingSA
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public class LowLevelPetriNetWithoutTStarBuilder extends AbstractLowLevelPetriNetBuilderUsingSA {

    /**
     * @param editor source object.
     */
    public LowLevelPetriNetWithoutTStarBuilder(IEditor editor) {
        super(editor);
        createLowLevelPetriNet();
    }

}
