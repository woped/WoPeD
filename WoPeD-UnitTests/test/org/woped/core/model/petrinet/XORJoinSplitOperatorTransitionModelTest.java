package org.woped.core.model.petrinet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.woped.core.model.CreationMap;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.PetriNetModelProcessor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class XORJoinSplitOperatorTransitionModelTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void isActivated_noActiveInputPlaces_returnsFalse() throws Exception {

        PetriNetModelProcessor processor = createTestNet();
        XORJoinSplitOperatorTransitionModel cut = (XORJoinSplitOperatorTransitionModel) processor.getElementContainer().getElementById("t1");

        assertFalse(cut.isActivated());
    }

    @Test
    public void isActivated_centerPlaceContainsToken_returnsTrue() throws Exception {
        PetriNetModelProcessor processor = createTestNet();
        XORJoinSplitOperatorTransitionModel cut = (XORJoinSplitOperatorTransitionModel) processor.getElementContainer().getElementById("t1");

        PlaceModel centerPlace = cut.getCenterPlace();
        centerPlace.setVirtualTokens(1);

        assertTrue(cut.isActivated());
    }

    @Test
    public void isActivated_hasIncomingActivePlaces_returnsTrue() throws Exception {
        PetriNetModelProcessor processor = createTestNet();
        XORJoinSplitOperatorTransitionModel cut = (XORJoinSplitOperatorTransitionModel) processor.getElementContainer().getElementById("t1");

        PlaceModel p1 = (PlaceModel) processor.getElementContainer().getElementById("p1");
        p1.setVirtualTokens(1);

        assertTrue(cut.isActivated());
    }

    private PetriNetModelProcessor createTestNet() {
        PetriNetModelProcessor processor = new PetriNetModelProcessor();
        ModelElementContainer container = processor.getElementContainer();

        CreationMap placeMap = CreationMap.createMap();
        placeMap.setType(AbstractPetriNetElementModel.PLACE_TYPE);
        for (int i = 1; i <= 4; i++) {
            placeMap.setId("p" + i);
            processor.createElement(placeMap);
        }

        CreationMap transitionMap = CreationMap.createMap();
        transitionMap.setType(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE);
        transitionMap.setOperatorType(OperatorTransitionModel.XORJOIN_XORSPLIT_TYPE);
        transitionMap.setId("t1");
        processor.createElement(transitionMap);

        processor.createArc("p1", "t1");
        processor.createArc("p2", "t1");
        processor.createArc("t1", "p3");
        processor.createArc("t1", "p4");

        return processor;
    }

}