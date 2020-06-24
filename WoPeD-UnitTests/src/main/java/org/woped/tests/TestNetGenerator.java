package org.woped.tests;

import org.woped.core.model.CreationMap;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.editor.controller.vc.EditorVC;

public class TestNetGenerator {

    public PetriNetModelProcessor createNetWithoutArcs(int places, int transitions) {
        PetriNetModelProcessor processor = new PetriNetModelProcessor();

        CreationMap placeMap = CreationMap.createMap();
        placeMap.setType(AbstractPetriNetElementModel.PLACE_TYPE);
        for (int i = 1; i <= places; i++) {
            placeMap.setId("p" + i);
            placeMap.setName("test+i");
            processor.createElement(placeMap);
        }

        CreationMap transitionMap = CreationMap.createMap();
        transitionMap.setType(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
        for (int i = 1; i <= transitions; i++) {
            transitionMap.setId("t" + i);
            processor.createElement(transitionMap);
        }

        return processor;
    }

    /**
     * Creates a petrinet containing 2 places (p1, p2) connected with one simple transition (t1).
     *
     * @return a simple petrinet
     */
    public PetriNetModelProcessor createSimpleNet() {
        PetriNetModelProcessor processor = createNetWithoutArcs(2, 1);
        processor.createArc("p1", "t1");
        processor.createArc("t1", "p2");

        return processor;
    }

    /**
     * Creates a editor with a contained simple net.
     * @return the editor
     */
    public EditorVC getDemoEditor(){
        EditorVC editor = new EditorVC();
        editor.setModelProcessor(createSimpleNet());

        return editor;
    }
}
