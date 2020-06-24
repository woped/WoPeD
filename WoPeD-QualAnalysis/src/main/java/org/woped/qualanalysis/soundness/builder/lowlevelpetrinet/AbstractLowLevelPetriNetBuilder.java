package org.woped.qualanalysis.soundness.builder.lowlevelpetrinet;

import org.woped.core.controller.IEditor;
import org.woped.qualanalysis.soundness.datamodel.LowLevelPetriNet;

/**
 * abstract class. creates an LowLevelPetriNet. the type of the source object is IEditor
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public abstract class AbstractLowLevelPetriNetBuilder {
    protected IEditor editor;
    protected LowLevelPetriNet lowLevelPetriNet;

    /**
     * 
     * @param editor source object.
     */
    public AbstractLowLevelPetriNetBuilder(IEditor editor) {
        this.editor = editor;
    }

    /**
     * 
     * @return created lowLevelPetriNet, can be null!
     */
    public LowLevelPetriNet getLowLevelPetriNet() {
        return lowLevelPetriNet;
    }
}
