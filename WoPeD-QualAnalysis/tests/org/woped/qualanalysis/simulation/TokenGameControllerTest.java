package org.woped.qualanalysis.simulation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.woped.core.controller.IEditor;
import org.woped.core.model.ArcModel;
import org.woped.core.model.CreationMap;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.XORJoinSplitOperatorTransitionModel;

import java.beans.PropertyChangeSupport;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.woped.core.model.petrinet.AbstractPetriNetElementModel.PLACE_TYPE;
import static org.woped.core.model.petrinet.AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE;

public class TokenGameControllerTest {

    private TokenGameController sut;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void arcClicked_arcNotActive_doesNothing() throws Exception {

        sut = createTestInstance(createXorJoinNet());
        ArcModel inactiveArc = mock(ArcModel.class);
        when(inactiveArc.isActivated()).thenReturn(false);

        sut.arcClicked(inactiveArc);
        verify(inactiveArc, times(0)).getSourceId();
        verify(inactiveArc, times(0)).getTargetId();
    }

    @Test
    public void executeOperators_targetIsXorJoinOperator_sourceSendsTokens() throws Exception {

        sut = createTestInstance(createXorJoinNet());
        ModelElementContainer container = sut.getThisEditor().getModelProcessor().getElementContainer();

        int initialTokens = 2;
        PlaceModel source = (PlaceModel) container.getElementById("p1");
        source.setTokens(initialTokens);

        int arcWeight = 1;
        ArcModel clickedArc = container.findArc("p1", "t1");
        clickedArc.setInscriptionValue(arcWeight);
        clickedArc.setActivated(true);

        sut.executeOperators(clickedArc);

        int expected = initialTokens - arcWeight;
        int actual = source.getVirtualTokenCount();

        assertEquals(expected, actual);
    }

    @Test
    public void executeOperators_targetIsXorJoinSplitOperatorIncomingArcClicked_centerPlaceReceivesTokens() throws Exception {
        sut = createTestInstance(createXorJoinSplitNet());
        ModelElementContainer container = sut.getThisEditor().getModelProcessor().getElementContainer();
        PlaceModel source = (PlaceModel) container.getElementById("p1");
        source.setTokens(1);

        OperatorTransitionModel target = (OperatorTransitionModel) container.getElementById("t1");
        PlaceModel centerPlace = target.getCenterPlace();

        int arcWeight = 3;
        ArcModel clickedArc = container.findArc(source.getId(), target.getId());
        clickedArc.setInscriptionValue(arcWeight); // outer arc weight does not influence inner arc weight

        sut.executeOperators(clickedArc);

        int expected = centerPlace.getTokenCount() + 1; // inner arc weight should always be one
        int actual = centerPlace.getVirtualTokenCount();

        assertEquals(expected, actual);
    }

    @Test
    public void executeOperators_sourceIsXorSplit_targetReceivesTokens() throws Exception {
        sut = createTestInstance(createXorSplitNet());
        ModelElementContainer container = sut.getThisEditor().getModelProcessor().getElementContainer();

        PlaceModel sourcePlace = (PlaceModel) container.getElementById("p1");
        PlaceModel targetPlace = (PlaceModel) container.getElementById("p2");
        ArcModel clickedArc = container.findArc("t1", "p2");
        int arcWeight = 1;

        sourcePlace.setTokens(1);
        clickedArc.setInscriptionValue(arcWeight);

        int expected = targetPlace.getVirtualTokenCount() + arcWeight;

        sut.executeOperators(clickedArc);
        int actual = targetPlace.getVirtualTokenCount();

        assertEquals(expected, actual);
    }

    @Test
    public void executeOperators_sourceIsXorJoinSplitOutgoingArcClicked_targetReceivesTokens() throws Exception {
        sut = createTestInstance(createXorJoinSplitNet());
        ModelElementContainer container = sut.getThisEditor().getModelProcessor().getElementContainer();

        XORJoinSplitOperatorTransitionModel transition = (XORJoinSplitOperatorTransitionModel) container.getElementById("t1");
        PlaceModel centerPlace = transition.getCenterPlace();
        centerPlace.setTokens(1);

        PlaceModel targetPlace = (PlaceModel) container.getElementById("p3");
        ArcModel clickedArc = container.findArc("t1", "p3");
        int arcWeight = 1;
        clickedArc.setInscriptionValue(arcWeight);

        int expected = targetPlace.getVirtualTokenCount() + arcWeight;

        sut.executeOperators(clickedArc);
        int actual = targetPlace.getVirtualTokenCount();

        assertEquals(expected, actual);
    }

    private TokenGameController createTestInstance(PetriNetModelProcessor net) {

        IEditor editor = mock(IEditor.class);
        when(editor.getModelProcessor()).thenReturn(net);
        when(editor.getGraph()).thenReturn(null);

        PropertyChangeSupport propertyChangedSupport = new PropertyChangeSupport(editor);
        return new TokenGameController(editor, propertyChangedSupport);
    }

    private PetriNetModelProcessor createXorJoinNet() {
        PetriNetModelProcessor processor = new PetriNetModelProcessor();

        CreationMap placeMap = CreationMap.createMap();
        placeMap.setType(PLACE_TYPE);
        for (int i = 1; i <= 3; i++) {
            placeMap.setId("p" + i);
            processor.createElement(placeMap);
        }

        CreationMap transitionMap = CreationMap.createMap();
        transitionMap.setType(TRANS_OPERATOR_TYPE);
        transitionMap.setOperatorType(OperatorTransitionModel.XOR_JOIN_TYPE);
        transitionMap.setId("t1");
        processor.createElement(transitionMap);

        processor.createArc("p1", "t1");
        processor.createArc("p2", "t1");
        processor.createArc("t1", "p3");

        return processor;
    }

    private PetriNetModelProcessor createXorJoinSplitNet() {
        PetriNetModelProcessor processor = new PetriNetModelProcessor();

        CreationMap placeMap = CreationMap.createMap();
        placeMap.setType(PLACE_TYPE);
        for (int i = 1; i <= 4; i++) {
            placeMap.setId("p" + i);
            processor.createElement(placeMap);
        }

        CreationMap transitionMap = CreationMap.createMap();
        transitionMap.setType(TRANS_OPERATOR_TYPE);
        transitionMap.setOperatorType(OperatorTransitionModel.XORJOIN_XORSPLIT_TYPE);
        transitionMap.setId("t1");
        processor.createElement(transitionMap);

        processor.createArc("p1", "t1");
        processor.createArc("p2", "t1");
        processor.createArc("t1", "p3");
        processor.createArc("t1", "p4");

        return processor;
    }

    private PetriNetModelProcessor createXorSplitNet() {
        PetriNetModelProcessor processor = new PetriNetModelProcessor();

        CreationMap placeMap = CreationMap.createMap();
        placeMap.setType(PLACE_TYPE);
        for (int i = 1; i <= 3; i++) {
            placeMap.setId("p" + i);
            processor.createElement(placeMap);
        }

        CreationMap transitionMap = CreationMap.createMap();
        transitionMap.setType(TRANS_OPERATOR_TYPE);
        transitionMap.setOperatorType(OperatorTransitionModel.XOR_SPLIT_TYPE);
        transitionMap.setId("t1");
        processor.createElement(transitionMap);

        processor.createArc("p1", "t1");
        processor.createArc("t1", "p2");
        processor.createArc("t1", "p3");

        return processor;
    }
}