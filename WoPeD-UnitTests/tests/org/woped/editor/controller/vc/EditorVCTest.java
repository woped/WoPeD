package org.woped.editor.controller.vc;

import org.junit.Before;
import org.junit.Test;
import org.woped.core.model.ArcModel;
import org.woped.core.model.CreationMap;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.tests.TestNetGenerator;
import org.woped.qualanalysis.understandability.NetColorScheme;

import java.awt.geom.Point2D;
import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class EditorVCTest {

    private EditorVC cut;

    @Before
    public void setup() {
        cut = new EditorVC();
        EditorPanel panel = mock(EditorPanel.class);
        cut.setEditorPanel(panel);

        NetColorScheme scheme = mock(NetColorScheme.class);
        when(panel.getUnderstandColoring()).thenReturn(scheme);
    }

    @Test
    public void createArc_validConnection_setsArcWeightFromMap() throws Exception {

        cut.setModelProcessor(createDemoNet(2, 1));

        int arcWeight = 2;
        CreationMap map = CreationMap.createMap();
        map.setArcSourceId("p1");
        map.setArcTargetId("t1");
        map.setArcWeight(arcWeight);

        ArcModel arc = cut.createArc(map, new Point2D[0]);

        int actual = arc.getInscriptionValue();
        assertEquals(arcWeight, actual);
    }

    @Test
    public void pasteArc_containsArcWithNoSource_doesNotAddArc() throws Exception {

        PetriNetModelProcessor processor = createDemoNet(1, 1);
        cut.setModelProcessor(processor);

        ArcModel copiedArc = processor.createArc("p1", "t1");

        CreationMap sourceMap = copiedArc.getCreationMap();
        sourceMap.setArcSourceId("p2");

        ArcModel pastedArc = cut.pasteArc(sourceMap, 0, 0);
        assertNull(pastedArc);
    }

    @Test
    public void pasteArc_containsArcWithNoTarget_doesNotAddArc() throws Exception {

        PetriNetModelProcessor processor = createDemoNet(1, 1);
        cut.setModelProcessor(processor);

        ArcModel copiedArc = processor.createArc("p1", "t1");

        CreationMap sourceMap = copiedArc.getCreationMap();
        sourceMap.setArcTargetId("t2");

        ArcModel pastedArc = cut.pasteArc(sourceMap, 0, 0);
        assertNull(pastedArc);
    }

    @Test
    public void pasteArc_validArcWithOnePoint_addsOffsetToPoint() throws Exception {

        PetriNetModelProcessor processor = createDemoNet(2, 2);
        cut.setModelProcessor(processor);

        ArcModel sourceArc = processor.createArc("p1", "t1");
        CreationMap sourceMap = sourceArc.getCreationMap();

        Point2D.Double originalPoint = new Point2D.Double(15, 18);
        Vector<Object> originalPoints = new Vector<>();
        originalPoints.add(originalPoint);

        sourceMap.setArcSourceId("p2");
        sourceMap.setArcTargetId("t2");
        sourceMap.setArcPoints(originalPoints);

        int deltaX = 10;
        int deltaY = 20;

        Point2D expected = new Point2D.Double(originalPoint.getX() + deltaX, originalPoint.getY() + deltaY);
        ArcModel pastedArc = cut.pasteArc(sourceMap, deltaX, deltaY);

        Point2D[] points = pastedArc.getPoints();
        Point2D actual = points[0];
        assertEquals(expected, actual);
    }

    @Test
    public void pasteArc_ArcSourceIdIsNull_returnsNull() throws Exception {

        PetriNetModelProcessor processor = createDemoNet(2, 2);
        cut.setModelProcessor(processor);

        ArcModel sourceArc = processor.createArc("p1", "t1");
        CreationMap sourceMap = sourceArc.getCreationMap();
        sourceMap.setArcSourceId(null);

        ArcModel pastedArc = cut.pasteArc(sourceMap, 0, 0);
        assertNull(pastedArc);
    }

    @Test
    public void pasteArc_ArcTargetIdIsNull_returnsNull() throws Exception {

        PetriNetModelProcessor processor = createDemoNet(2, 2);
        cut.setModelProcessor(processor);

        ArcModel sourceArc = processor.createArc("p1", "t1");
        CreationMap sourceMap = sourceArc.getCreationMap();
        sourceMap.setArcTargetId(null);

        ArcModel pastedArc = cut.pasteArc(sourceMap, 0, 0);
        assertNull(pastedArc);
    }

    private PetriNetModelProcessor createDemoNet(int places, int transitions) {

        TestNetGenerator netGenerator = new TestNetGenerator();
        return netGenerator.createNetWithoutArcs(places, transitions);
    }

}
