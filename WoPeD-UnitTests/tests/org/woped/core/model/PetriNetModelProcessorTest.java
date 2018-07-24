package org.woped.core.model;

import org.jgraph.graph.DefaultPort;
import org.junit.Test;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.tests.TestNetGenerator;

import java.awt.geom.Point2D;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PetriNetModelProcessorTest {

    @Test
    public void createArc_SourceNotExists_returnsNull() {
        PetriNetModelProcessor sut = new PetriNetModelProcessor();

        String sourceId = "notExistingId";
        String targetId = "transition1Id";
        AbstractPetriNetElementModel target = mock(AbstractPetriNetElementModel.class);

        ModelElementContainer container = mock(ModelElementContainer.class);
        when(container.getElementById(sourceId)).thenReturn(null);
        when(container.getElementById(targetId)).thenReturn(target);

        sut.setElementContainer(container);

        assertNull(sut.createArc(null, sourceId, targetId, new Point2D[0], true));
    }

    @Test
    public void createArc_TargetNotExists_returnsNull() {
        PetriNetModelProcessor sut = new PetriNetModelProcessor();

        String sourceId = "place1Id";
        String targetId = "notExistingId";
        AbstractPetriNetElementModel source = mock(AbstractPetriNetElementModel.class);

        ModelElementContainer container = mock(ModelElementContainer.class);
        when(container.getElementById(sourceId)).thenReturn(source);
        when(container.getElementById(targetId)).thenReturn(null);

        sut.setElementContainer(container);

        assertNull(sut.createArc(null, sourceId, targetId, new Point2D[0], true));
    }

    @Test
    public void createArc_IdIsNull_arcIdNotNull() {
        PetriNetModelProcessor sut = new PetriNetModelProcessor();
        SimpleDemoData simpleDemoData = new SimpleDemoData().invoke();
        sut.setElementContainer(simpleDemoData.container);

        ArcModel arc = sut.createArc(null, simpleDemoData.place1Id, simpleDemoData.transition1Id, new Point2D[0], true);

        assertNotNull(arc.getId());
    }

    @Test
    public void createArc_IdExists_arcIdNotNull() {
        PetriNetModelProcessor sut = new PetriNetModelProcessor();
        SimpleDemoData simpleDemoData = new SimpleDemoData().invoke();
        sut.setElementContainer(simpleDemoData.container);

        String arcId = "a1";
        ArcModel arc = sut.createArc(arcId, simpleDemoData.place1Id, simpleDemoData.transition1Id, new Point2D[0], true);

   //     assertNotEquals(arcId, arc.getId());
    }

    @Test
    public void createArc_IdNotExists_suggestedIdIsUsed() {
        PetriNetModelProcessor sut = new PetriNetModelProcessor();
        SimpleDemoData simpleDemoData = new SimpleDemoData().invoke();
        sut.setElementContainer(simpleDemoData.container);

        String expected = "suggestArcId";
        ArcModel arc = sut.createArc(expected, simpleDemoData.place1Id, simpleDemoData.transition1Id, new Point2D[0], true);
        String actual = arc.getId();

        assertEquals(expected, actual);
    }

    @Test
    public void getLogicallyFingerprint_validNet_returnsExpectedResult() {
        PetriNetModelProcessor sut = new PetriNetModelProcessor();
        SimpleDemoData demoData = new SimpleDemoData().invoke();
        sut.setElementContainer(demoData.container);

        String expected = "2p10p20#2*p1t1*t1p2";
        String actual = sut.getLogicalFingerprint();

        assertEquals(expected, actual);
    }

    @Test
    public void getLogicallyFingerpring_netWithWeight_resultContainsWeight() throws Exception {
        PetriNetModelProcessor sut = new PetriNetModelProcessor();
        SimpleDemoData demoData = new SimpleDemoData().invoke();
        sut.setElementContainer(demoData.container);

        int arcWeight = 2;
        demoData.arc2.setInscriptionValue(arcWeight);

        String expected = "2p10p20#2*p1t1*2t1p2";
        String actual = sut.getLogicalFingerprint();

        assertEquals(expected, actual);
    }

    @Test
    public void isLogicallyFingerprint_OwnFingerprint_returnsTrue() {
        PetriNetModelProcessor sut = new PetriNetModelProcessor();
        SimpleDemoData demoData = new SimpleDemoData().invoke();
        sut.setElementContainer(demoData.container);

        String fingerprint = sut.getLogicalFingerprint();

        assertTrue(sut.isLogicalFingerprintEqual(fingerprint));
    }

    @Test
    public void isLogicallyFingerprint_differentLength_returnsFalse() {
        PetriNetModelProcessor sut = new PetriNetModelProcessor();
        SimpleDemoData demoData = new SimpleDemoData().invoke();
        sut.setElementContainer(demoData.container);

        String fingerprint = "2p10p20p30#2*p1t1*t1p2";

        assertFalse(sut.isLogicalFingerprintEqual(fingerprint));
    }

    @Test
    public void isLogicallyFingerprint_differentPlaces_returnsFalse() {
        PetriNetModelProcessor sut = new PetriNetModelProcessor();
        SimpleDemoData demoData = new SimpleDemoData().invoke();
        sut.setElementContainer(demoData.container);

        String fingerprint = "2p10p30#2*p1t1*t1p2";

        assertFalse(sut.isLogicalFingerprintEqual(fingerprint));
    }

    @Test
    public void isLogicallyFingerprint_differentArcOrder_returnsTrue() {
        PetriNetModelProcessor sut = new PetriNetModelProcessor();
        SimpleDemoData demoData = new SimpleDemoData().invoke();
        sut.setElementContainer(demoData.container);

        String fingerprint = "2p10p20#2*t1p2*p1t1";

        assertTrue(sut.isLogicalFingerprintEqual(fingerprint));
    }

    @Test
    public void isLogicallyFingerprint_differentArcs_returnsFalse() {
        PetriNetModelProcessor sut = new PetriNetModelProcessor();
        SimpleDemoData demoData = new SimpleDemoData().invoke();
        sut.setElementContainer(demoData.container);

        String fingerprint = "2p10p20#2*t1p2*p2t1";

        assertFalse(sut.isLogicalFingerprintEqual(fingerprint));
    }

    @Test
    public void isLogicallyFingerprint_netsDifferInWeights_returnsFalse() throws Exception {
        SimpleDemoData demoDataNet1 = new SimpleDemoData().invoke();
        PetriNetModelProcessor net1 = new PetriNetModelProcessor("net1");
        net1.setElementContainer(demoDataNet1.container);

        SimpleDemoData demoDataNet2 = new SimpleDemoData().invoke();
        demoDataNet2.arc1.setInscriptionValue(2);
        PetriNetModelProcessor net2 = new PetriNetModelProcessor("net2");
        net2.setElementContainer(demoDataNet2.container);

        assertFalse(net1.isLogicalFingerprintEqual(net2.getLogicalFingerprint()));
    }

    // Creates demo data with two places connected with one transition.
    private class SimpleDemoData {
        private String place1Id;
        private String place2Id;
        private String transition1Id;
        private ModelElementContainer container;
        private ArcModel arc1;
        private ArcModel arc2;

        SimpleDemoData invoke() {
            place1Id = "p1";
            PlaceModel place1 = mock(PlaceModel.class);
            DefaultPort place1port = mock(DefaultPort.class);
            when(place1port.getParent()).thenReturn(place1);
            when(place1.getId()).thenReturn(place1Id);
            when(place1.getChildAt(0)).thenReturn(place1port);
            when(place1.getType()).thenReturn(AbstractPetriNetElementModel.PLACE_TYPE);

            place2Id = "p2";
            PlaceModel place2 = mock(PlaceModel.class);
            DefaultPort place2port = mock(DefaultPort.class);
            when(place2port.getParent()).thenReturn(place2);
            when(place2.getId()).thenReturn(place2Id);
            when(place2.getChildAt(0)).thenReturn(place2port);
            when(place2.getType()).thenReturn(AbstractPetriNetElementModel.PLACE_TYPE);

            transition1Id = "t1";
            AbstractPetriNetElementModel transition1 = mock(AbstractPetriNetElementModel.class);
            DefaultPort transition1Port = mock(DefaultPort.class);
            when(transition1Port.getParent()).thenReturn(transition1);
            when(transition1.getId()).thenReturn(transition1Id);
            when(transition1.getChildAt(0)).thenReturn(transition1Port);

            arc1 = ModelElementFactory.createArcModel("a1", place1port, transition1Port);
            arc2 = ModelElementFactory.createArcModel("a2", transition1Port, place2port);

            container = new ModelElementContainer();
            container.addElement(place1);
            container.addElement(place2);
            container.addElement(transition1);
            container.addReference(arc1);
            container.addReference(arc2);

            return this;
        }
    }

    @Test
    public void usesArcWeights_noWeights_returnsFalse() throws Exception {
        PetriNetModelProcessor cut = new TestNetGenerator().createSimpleNet();

        assertFalse(cut.usesArcWeights());
    }

    @Test
    public void usesArcWeights_hasArcWeights_retunrsTrue() throws Exception {
        PetriNetModelProcessor cut = new TestNetGenerator().createSimpleNet();
        ArcModel arc = cut.getElementContainer().findArc("p1", "t1");
        arc.setInscriptionValue(2);

        assertTrue(cut.usesArcWeights());
    }
}