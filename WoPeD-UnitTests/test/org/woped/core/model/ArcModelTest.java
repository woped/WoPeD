package org.woped.core.model;

import org.jgraph.graph.DefaultPort;
import org.junit.Test;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;

import java.awt.geom.Point2D;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ArcModelTest {
    @Test
    public void getInscriptionValue_newInstance_returnsOne() throws Exception {

        ArcModel cut = new ArcModel();

        int expected = 1;
        int actual = cut.getInscriptionValue();

        assertEquals(expected, actual);
    }

    @Test
    public void getInscriptionValue_changedValue_returnsThatValue() throws Exception {

        ArcModel cut = new ArcModel();

        int expected = 2;
        cut.setInscriptionValue(expected);
        int actual = cut.getInscriptionValue();

        assertEquals(expected, actual);
    }

    @Test
    public void getCreationMap_validArc_containsArcWeight() throws Exception {
        int arcWeight = 2;
        ArcModel cut = createDemoArc(arcWeight);

        CreationMap creationMap = cut.getCreationMap();
        int actual = creationMap.getArcWeight();

        assertEquals(arcWeight, actual);
    }

    private ArcModel createDemoArc(int arcWeight) {
        ArcModel cut = new ArcModel();
        cut.setInscriptionValue(arcWeight);

        DefaultPort mockSourcePort = mock(DefaultPort.class);
        AbstractPetriNetElementModel mockSource = mock(AbstractPetriNetElementModel.class);
        when(mockSourcePort.getParent()).thenReturn(mockSource);
        when(mockSource.getId()).thenReturn("p1");
        cut.setSource(mockSourcePort);

        DefaultPort mockTargetPort = mock(DefaultPort.class);
        AbstractPetriNetElementModel mockTarget = mock(AbstractPetriNetElementModel.class);
        when(mockTargetPort.getParent()).thenReturn(mockTarget);
        when(mockTarget.getId()).thenReturn("t1");
        cut.setTarget(mockTargetPort);

        cut.addPoint(new Point2D.Double(0, 0));
        cut.addPoint(new Point2D.Double(10, 10));

        return cut;
    }

}