package org.woped.editor.controller.vc;

import org.junit.Test;
import org.woped.core.model.ArcModel;
import org.woped.core.model.CreationMap;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;

import java.awt.geom.Point2D;

import static org.junit.Assert.assertEquals;


public class EditorVCTest {
    @Test
    public void createArc_validConnection_setsArcWeightFromMap() throws Exception {
        EditorVC sut = new EditorVC();

        sut.setModelProcessor(createDemoNet());

        int arcWeight = 2;
        CreationMap map = CreationMap.createMap();
        map.setArcSourceId("p1");
        map.setArcTargetId("t1");
        map.setArcWeight(arcWeight);

        ArcModel arc = sut.createArc(map, new Point2D[0]);

        int actual = arc.getInscriptionValue();
        assertEquals(arcWeight, actual);
    }

    private PetriNetModelProcessor createDemoNet() {
        PetriNetModelProcessor processor = new PetriNetModelProcessor();

        CreationMap placeMap = CreationMap.createMap();
        placeMap.setType(AbstractPetriNetElementModel.PLACE_TYPE);
        for (int i = 1; i <= 2; i++) {
            placeMap.setId("p" + i);
            processor.createElement(placeMap);
        }

        CreationMap transitionMap = CreationMap.createMap();
        transitionMap.setType(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
        transitionMap.setId("t1");
        processor.createElement(transitionMap);

        return processor;
    }

}
