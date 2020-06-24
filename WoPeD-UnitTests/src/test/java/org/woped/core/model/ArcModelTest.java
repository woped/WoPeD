package org.woped.core.model;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultPort;
import org.junit.Test;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.tests.TestNetGenerator;

import java.awt.geom.Point2D;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SuppressWarnings("ConstantConditions")
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

    @Test(expected = IllegalArgumentException.class)
    public void setInscriptionValue_negativeWeight_throwsException() throws Exception {
        ArcModel cut = new ArcModel();

        int arcWeight = -1;
        cut.setInscriptionValue(arcWeight);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setInscriptionValue_weightIsZero_throwsException() throws Exception {
        ArcModel cut = new ArcModel();

        int arcWeight = 0;
        cut.setInscriptionValue(arcWeight);
    }
    @Test
    public void getCreationMap_validArc_containsArcWeightLabelPosition() throws Exception {
        ArcModel cut = createDemoArc();

        CreationMap creationMap = cut.getCreationMap();
        Point2D arcWeightLabelPosition = creationMap.getArcWeightLabelPosition();

        assertNotNull(arcWeightLabelPosition);
    }

    @Test
    public void getWeightLabelPosition_validArc_positionNotNull() throws Exception {
        ArcModel cut = createDemoArc();

        Point2D weightLabelPosition = cut.getWeightLabelPosition();
        assertNotNull(weightLabelPosition);
    }

    @Test
    public void setWeightLabelPosition_validPosition_attributesContainsPosition() throws Exception {
        ArcModel cut = createDemoArc();
        AttributeMap attributes = cut.getAttributes();
        Point2D newPosition = new Point2D.Double(10, 10);
        Point2D currentPosition = (Point2D) attributes.get("labelposition");
 //       assertNotEquals("Position should not match before update", newPosition, currentPosition);

        cut.setWeightLablePosition(newPosition);
        currentPosition = (Point2D) attributes.get("labelposition");
        assertEquals("Positions should match after update", newPosition, currentPosition);

        currentPosition = cut.getWeightLabelPosition();
        assertEquals("New position should be accessable.", newPosition, currentPosition);
    }

    @Test
    public void setWeightLabelPosition_newPosIsNull_ignoresUpdate() throws Exception {
        ArcModel cut = createDemoArc();

        Point2D postionBeforeUpdate = cut.getWeightLabelPosition();

        cut.setWeightLablePosition(null);

        Point2D positoinAfterUpdate = cut.getWeightLabelPosition();

        assertEquals(postionBeforeUpdate, positoinAfterUpdate);
    }
    @Test
    public void getCreationMap_validArc_containsArcWeight() throws Exception {
        int arcWeight = 2;
        ArcModel cut = createDemoArc(arcWeight);

        CreationMap creationMap = cut.getCreationMap();
        int actual = creationMap.getArcWeight();

        assertEquals(arcWeight, actual);
    }

    @Test
    public void getCreationMap_arcHasId_mapContainsId() throws Exception {
        ArcModel cut = createDemoArc();

        String expected = cut.getId();
        CreationMap map = cut.getCreationMap();

        String actual = map.getId();
        assertEquals(expected, actual);
    }

    @Test
    public void getCreationMap_arcIsRoute_mapContainsRouteState() throws Exception {
        ArcModel cut = createDemoArc();
        boolean expected = true;
        cut.setRoute(expected);
        CreationMap map = cut.getCreationMap();

        boolean actual = map.isArcRoute();
        assertEquals(expected, actual);
    }

    @Test
    public void getCreationMap_arcIsNotRoute_mapContainsRouteState() throws Exception {
        ArcModel cut = createDemoArc();
        boolean expected = false;
        cut.setRoute(expected);
        CreationMap map = cut.getCreationMap();

        boolean actual = map.isArcRoute();
        assertEquals(expected, actual);
    }

    @Test
    public void getCreationMap_arcHasSource_mapContainsSourceId() throws Exception {
        ArcModel cut = createDemoArc();

        String expected = cut.getSourceId();
        CreationMap map = cut.getCreationMap();

        String actual = map.getArcSourceId();
        assertEquals(expected, actual);
    }

    @Test
    public void getCreationMap_arcHasTarget_mapContainsTargetId() throws Exception {
        ArcModel cut = createDemoArc();

        String expected = cut.getTargetId();
        CreationMap map = cut.getCreationMap();

        String actual = map.getArcTargetId();
        assertEquals(expected, actual);
    }

    @Test
    public void getCreationMap_arcHasNoPoints_mapContainsEmptyPointsList() throws Exception {
        ArcModel cut = createDemoArc();

        cut.setPoints(new Point2D[0]);
        CreationMap map = cut.getCreationMap();
        assertTrue(map.getArcPoints().isEmpty());
    }

    @Test
    public void getCreationMap_arcHasPoints_mapContainsPoints() throws Exception {
        ArcModel cut = createArc();

        Point2D.Double start = new Point2D.Double(10, 10);
        Point2D.Double end = new Point2D.Double(20, 20);
        Point2D.Double via = new Point2D.Double(30, 30);

        cut.addPoint(via);

        Point2D[] points = cut.getPoints();
        List<Point2D> arcPoints = cut.getCreationMap().getArcPoints();

        int expectedAmount = points.length - 2; //start and end does not count
        assertEquals("Number of points must be the same", expectedAmount, arcPoints.size());

        assertFalse("The start point should not be contained", arcPoints.contains(start));
        assertFalse("The end point should not be contained", arcPoints.contains(end));
        assertTrue("Point should be contained: " + via.toString(), arcPoints.contains(via));
    }

    private ArcModel createDemoArc() {
        return createDemoArc(1);
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

    private ArcModel createArc() {
        TestNetGenerator generator = new TestNetGenerator();
        PetriNetModelProcessor processor = generator.createNetWithoutArcs(1, 1);

        return processor.createArc("p1", "t1");
    }

}