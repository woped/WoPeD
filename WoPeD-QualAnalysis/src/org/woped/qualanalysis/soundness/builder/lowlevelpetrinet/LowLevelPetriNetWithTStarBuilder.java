package org.woped.qualanalysis.soundness.builder.lowlevelpetrinet;

import org.woped.core.controller.IEditor;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;

/**
 * creates an lowLevel petri net with tStar.
 * 
 * @see AbstractLowLevelPetriNetBuilderUsingSA
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public class LowLevelPetriNetWithTStarBuilder extends AbstractLowLevelPetriNetBuilderUsingSA {

    /**
     * adds tStar element to the provided source object, creates low level petri net, removes the created tStar element.
     * 
     * @param editor source object.
     */
    public LowLevelPetriNetWithTStarBuilder(IEditor editor) {
        super(editor);
        AbstractPetriNetElementModel tStar;
        tStar = sa.addTStar();
        createLowLevelPetriNet();
        sa.removeTStar(tStar);

    }

}
