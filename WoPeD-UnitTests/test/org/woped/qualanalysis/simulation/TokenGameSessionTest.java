package org.woped.qualanalysis.simulation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.woped.core.model.ArcModel;
import org.woped.core.model.CreationMap;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TokenGameSessionTest {

    private TokenGameSession sut;

    @Before
    public void setUp() throws Exception {

        sut = new TokenGameSession();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void isTransitionInThisContainer_simpleTransition_returnsTrue() throws Exception {

        PetriNetModelProcessor processor = createXorJoinNet();
        ModelElementContainer container = processor.getElementContainer();

        TransitionModel t1 = (TransitionModel) container.getElementById("t1");
        sut.setTransitionToOccur(t1);

        boolean inThisContainer = sut.isTransitionInThisContainer(container);

        assertTrue(inThisContainer);
    }

    @Test
    public void isTransitionInThisContainer_virtualTransition_returnsTrue() throws Exception {

        PetriNetModelProcessor processor = createXorJoinNet();
        ModelElementContainer container = processor.getElementContainer();

        TransitionModel transition = (TransitionModel) container.getElementById("t1");
        ArcModel arc = container.findArc("p1", "t1");

        // Create virtual transition
        CreationMap map = CreationMap.createMap();
        map.setId(arc.getId());
        TransitionModel virtualTransition = new TransitionModel(map);
        PlaceModel helpPlace = (PlaceModel) container.getElementById(arc.getSourceId());
        String XorName = "(" + helpPlace.getNameValue() + ")-> " + transition.getNameValue();
        virtualTransition.setNameValue(XorName);

        sut.setTransitionToOccur(virtualTransition);

        boolean inThisContainer = sut.isTransitionInThisContainer(container);

        assertTrue(inThisContainer);
    }

    @Test
    public void isTransitionInThisContainer_subProcessTransition_returnsFalse() throws Exception {

        PetriNetModelProcessor processor = createSubProcessNet();
        ModelElementContainer outerContainer = processor.getElementContainer();

        SubProcessModel sub1 = (SubProcessModel) outerContainer.getElementById("sub1");
        ModelElementContainer innerContainer = sub1.getSimpleTransContainer();

        TransitionModel sub1_t1 = (TransitionModel) innerContainer.getElementById("sub1_t1");
        sut.setTransitionToOccur(sub1_t1);

        boolean inThisContainer = sut.isTransitionInThisContainer(outerContainer);

        assertFalse(inThisContainer);
    }

    private PetriNetModelProcessor createXorJoinNet() {

        PetriNetModelProcessor processor = new PetriNetModelProcessor();

        CreationMap placeMap = CreationMap.createMap();
        placeMap.setType(AbstractPetriNetElementModel.PLACE_TYPE);
        for (int i = 1; i <= 3; i++) {
            placeMap.setId("p" + i);
            processor.createElement(placeMap);
        }

        CreationMap transitionMap = CreationMap.createMap();
        transitionMap.setType(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE);
        transitionMap.setOperatorType(OperatorTransitionModel.XOR_JOIN_TYPE);
        transitionMap.setId("t1");
        processor.createElement(transitionMap);

        processor.createArc("p1", "t1");
        processor.createArc("p2", "t1");
        processor.createArc("t1", "p3");

        return processor;
    }

    private PetriNetModelProcessor createSubProcessNet() {

        PetriNetModelProcessor processor = new PetriNetModelProcessor();
        PetriNetModelProcessor subProcessor = new PetriNetModelProcessor("sub1");

        CreationMap transitionMap = CreationMap.createMap();
        transitionMap.setId("sub1");
        transitionMap.setType(AbstractPetriNetElementModel.SUBP_TYPE);
        SubProcessModel transition = (SubProcessModel) processor.createElement(transitionMap);
        subProcessor.setElementContainer(transition.getSimpleTransContainer());

        CreationMap placeMap = CreationMap.createMap();
        placeMap.setType(AbstractPetriNetElementModel.PLACE_TYPE);
        for (int i = 1; i <= 2; i++) {
            placeMap.setId("p" + i);
            PlaceModel place = (PlaceModel) processor.createElement(placeMap);
            subProcessor.getElementContainer().addElement(place);
        }

        CreationMap subTransitionMap = CreationMap.createMap();
        subTransitionMap.setId("sub1_t1");
        subTransitionMap.setType(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
        subProcessor.createElement(subTransitionMap);

        processor.createArc("p1", "sub1");
        processor.createArc("sub1", "p2");

        subProcessor.createArc("p1", "sub1_t1");
        subProcessor.createArc("sub1_t1", "p2");

        return processor;
    }
}