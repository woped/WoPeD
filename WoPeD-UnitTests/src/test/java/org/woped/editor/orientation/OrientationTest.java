package org.woped.editor.orientation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.woped.core.model.ArcModel;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.tests.TestNetGenerator;

import java.awt.geom.Point2D;

import static org.junit.Assert.assertEquals;

public class OrientationTest {

    private TestNetGenerator generator;
    private Orientation cut;

    @Before
    public void setUp() throws Exception {
        generator = new TestNetGenerator();
        cut = new Orientation();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void moveAllElements_simpleNetWithOneArcPoint_movesAllElements() throws Exception {

        PetriNetModelProcessor processor = generator.createNetWithoutArcs(1, 1);
        ModelElementContainer container = processor.getElementContainer();

        PlaceModel p1 = (PlaceModel) container.getElementById("p1");
        p1.setPosition(8, 10);
        p1.getNameModel().setPosition(0, 0);

        TransitionModel t1 = (TransitionModel) container.getElementById("t1");
        t1.setPosition(22, 14);
        t1.getNameModel().setPosition(0, 0);

        ArcModel arc = processor.createArc(p1.getId(), t1.getId());
        Point2D.Double arcPoint = new Point2D.Double(12, 16);
        arc.addPoint(arcPoint);

        int offsetX = 10;
        int offsetY = 15;

        Point2D expectedPlaceLocation = new Point2D.Double(p1.getX() + offsetX, p1.getY() + offsetY);
        Point2D expectedTransitionLocation = new Point2D.Double(t1.getX() + offsetX, t1.getY() + offsetY);
        Point2D expectedArcPointLocation = new Point2D.Double(arcPoint.getX() + offsetX, arcPoint.getY() + offsetY);
        cut.moveAllElements(offsetX, offsetY, container);

        assertEquals("Place should have been moved", expectedPlaceLocation, p1.getPosition());
        assertEquals("Transition should have been moved", expectedTransitionLocation, t1.getPosition());
        assertEquals("ArcPoint should have been moved", expectedArcPointLocation, arc.getPoints()[1]);
    }

    @Test
    public void rotateView_simpleNetWithArcPoint_switchesXYofElements() throws Exception {
        PetriNetModelProcessor processor = generator.createNetWithoutArcs(1, 1);
        ModelElementContainer container = processor.getElementContainer();

        PlaceModel p1 = (PlaceModel) container.getElementById("p1");
        p1.setPosition(8, 10);
        p1.getNameModel().setPosition(0, 0);

        TransitionModel t1 = (TransitionModel) container.getElementById("t1");
        t1.setPosition(22, 14);
        t1.getNameModel().setPosition(0, 0);

        ArcModel arc = processor.createArc(p1.getId(), t1.getId());
        Point2D.Double arcPoint = new Point2D.Double(12, 16);
        arc.addPoint(arcPoint);

        Point2D expectedPlacePosition = new Point2D.Double(p1.getY(), p1.getX());
        Point2D expectedTransitionPosition = new Point2D.Double(t1.getY(), t1.getX());
        Point2D expectedArcPointTransition = new Point2D.Double(arcPoint.getY(), arcPoint.getX());
        cut.rotateView(container);

        assertEquals("Place should rotate", expectedPlacePosition, p1.getPosition());
        assertEquals("Transition should rotate", expectedTransitionPosition, t1.getPosition());
        assertEquals("ArcPoint should rotate", expectedArcPointTransition, arc.getPoints()[1]);
    }
}