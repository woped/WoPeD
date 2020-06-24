package org.woped.file;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.IStatusBar;
import org.woped.core.controller.IViewController;
import org.woped.core.model.*;
import org.woped.core.model.petrinet.*;
import org.woped.pnml.*;

import java.awt.geom.Point2D;
import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class PNMLExportTest {

    // the system under test
    private PNMLExport sut;

    @Before
    public void setUp() throws Exception {

        AbstractApplicationMediator mediator = mock(AbstractApplicationMediator.class);
        when(mediator.findViewController(IStatusBar.TYPE)).thenReturn(new IViewController[0]);

        sut = new PNMLExport(mediator);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void initArc_noInnerArc_setOuterArcWeight() throws Exception {

        DemoArcTypeData exportArcData = new DemoArcTypeData();
        ArcType exportArc = exportArcData.arc;

        DemoArcModelData outerArcData = new DemoArcModelData();
        ArcModel outerArc = outerArcData.arc;

        sut.initArc(exportArc, outerArc, null);

        verify(exportArcData.arcName).setText(outerArcData.weight);
    }

    @Test
    public void initArc_innerArcNotNull_setOuterArcWeight() throws Exception {

        DemoArcTypeData exportArcData = new DemoArcTypeData();
        ArcType exportArc = exportArcData.arc;

        DemoArcModelData outerArcData = new DemoArcModelData();
        ArcModel outerArc = outerArcData.arc;

        DemoArcModelData innerArcData = new DemoArcModelData("a2", "p13", "t2", 1);
        ArcModel innerArc = innerArcData.arc;

        sut.initArc(exportArc, outerArc, innerArc);

        verify(exportArcData.arcName).setText(outerArcData.weight);
    }

    @Test
    public void initArc_noInnerArc_setOuterArcId() throws Exception {

        DemoArcTypeData exportArcData = new DemoArcTypeData();
        ArcType exportArc = exportArcData.arc;

        DemoArcModelData outerArcData = new DemoArcModelData();
        ArcModel outerArc = outerArcData.arc;

        sut.initArc(exportArc, outerArc, null);

        verify(exportArc).setId(outerArcData.id);
    }

    @Test
    public void initArc_innerArcNotNull_setOuterArcId() throws Exception {

        DemoArcTypeData exportArcData = new DemoArcTypeData();
        ArcType exportArc = exportArcData.arc;

        DemoArcModelData outerArcData = new DemoArcModelData();
        ArcModel outerArc = outerArcData.arc;

        DemoArcModelData innerArcData = new DemoArcModelData("a2", "p13", "t2", 1);
        ArcModel innerArc = innerArcData.arc;

        sut.initArc(exportArc, outerArc, innerArc);

        verify(exportArc).setId(outerArcData.id);
    }

    @Test
    public void initNodeName_validParameters_setInscriptionValueAsName() throws Exception {

        int arcWeight = 2;
        DemoArcTypeData arcTypeData = new DemoArcTypeData();
        ArcNameType exportArc = arcTypeData.arcName;

        DemoArcModelData arcModelData = new DemoArcModelData();
        ArcModel arc = arcModelData.arc;

        sut.initNodeName(exportArc, arc);
        verify(exportArc).setText(arcWeight);
    }

    @Test
    public void initArc_noInnerArc_setOuterSourceId() throws Exception {

        DemoArcTypeData exportArcData = new DemoArcTypeData();
        ArcType exportArc = exportArcData.arc;

        DemoArcModelData outerArcData = new DemoArcModelData();
        ArcModel outerArc = outerArcData.arc;

        sut.initArc(exportArc, outerArc, null);

        verify(exportArcData.arc).setSource(outerArcData.sourceId);
    }

    @Test
    public void initArc_innerArcNotNull_setInnerSourceId() throws Exception {

        DemoArcTypeData exportArcData = new DemoArcTypeData();
        ArcType exportArc = exportArcData.arc;

        DemoArcModelData outerArcData = new DemoArcModelData();
        ArcModel outerArc = outerArcData.arc;

        DemoArcModelData innerArcData = new DemoArcModelData("a2", "p13", "t2", 1);
        ArcModel innerArc = innerArcData.arc;

        sut.initArc(exportArc, outerArc, innerArc);

        verify(exportArcData.arc).setSource(innerArcData.sourceId);
    }

    @Test
    public void initArc_noInnerArc_setOuterTargetId() throws Exception {

        DemoArcTypeData exportArcData = new DemoArcTypeData();
        ArcType exportArc = exportArcData.arc;

        DemoArcModelData outerArcData = new DemoArcModelData();
        ArcModel outerArc = outerArcData.arc;

        sut.initArc(exportArc, outerArc, null);

        verify(exportArcData.arc).setTarget(outerArcData.targetId);
    }

    @Test
    public void initArc_innerArcNotNull_setInnerTargetId() throws Exception {

        DemoArcTypeData exportArcData = new DemoArcTypeData();
        ArcType exportArc = exportArcData.arc;

        DemoArcModelData outerArcData = new DemoArcModelData();
        ArcModel outerArc = outerArcData.arc;

        DemoArcModelData innerArcData = new DemoArcModelData("a2", "p13", "t2", 1);
        ArcModel innerArc = innerArcData.arc;

        sut.initArc(exportArc, outerArc, innerArc);

        verify(exportArcData.arc).setTarget(innerArcData.targetId);
    }

    @Test
    public void exportArc_containsSimpleTransition_usesTransitionId() throws Exception {

        NetType netBean = NetType.Factory.newInstance();
        ModelElementContainer container = new ModelElementContainer();

        PlaceModel p1 = new PlaceModel(CreationMap.createMap());
        p1.setId("p1");
        container.addElement(p1);

        TransitionModel t1 = new TransitionModel(CreationMap.createMap());
        t1.setId("t1");
        container.addElement(t1);

        DemoArcModelData a1Data = new DemoArcModelData("a1", p1.getId(), t1.getId(), 2);
        container.addReference(a1Data.arc);

        sut.exportArcs(netBean, container);

        ArcType[] exportedArcs = netBean.getArcArray();
        assertEquals(exportedArcs.length, 1);
        assertEquals(exportedArcs[0].getId(), a1Data.id);
        assertEquals(exportedArcs[0].getSource(), p1.getId());
        assertEquals(exportedArcs[0].getTarget(), t1.getId());
    }

    @Test
    public void exportArc_containsOperatorTransitionAsTarget_usesInnerTransitionId() throws Exception {

        NetType netBean = NetType.Factory.newInstance();

        DemoNet net = new DemoNet();
        ArcModel arc = net.processor.createArc(net.p1.getId(), net.t1.getId());
        arc.setInscriptionValue(2);

        sut.exportArcs(netBean, net.container);
        ArcType[] exportedArcs = netBean.getArcArray();

        String expectedSourceId = net.p1.getId();
        String expectedTargetId = "t1_op_1";
        assertTrue(containsArc(exportedArcs, expectedSourceId, expectedTargetId));
    }

    @Test
    public void exportArc_containsOperatorTransitionAsSource_usesInnerTransitionId() throws Exception {

        NetType netBean = NetType.Factory.newInstance();

        DemoNet net = new DemoNet();
        ArcModel arc = net.processor.createArc(net.t1.getId(), net.p1.getId());
        arc.setInscriptionValue(2);

        sut.exportArcs(netBean, net.container);
        ArcType[] exportedArcs = netBean.getArcArray();

        String expectedSourceId = "t1_op_1";
        String expectedTargetId = net.p1.getId();
        assertTrue(containsArc(exportedArcs, expectedSourceId, expectedTargetId));
    }

    private boolean containsArc(ArcType[] exportedArcs, String sourceId, String targetId) {

        for (ArcType arc : exportedArcs) {
            if (arc.getSource().equals(sourceId) && arc.getTarget().equals(targetId)) return true;
        }

        return false;
    }

    private class DemoArcTypeData {

        ArcType arc;
        ArcNameType arcName;
        AnnotationGraphisType arcNameGraphics;

        GraphicsArcType graphics;
        ArcToolspecificType arcToolspecificType;
        PositionType labelPosition;

        DemoArcTypeData() {
            arc = mock(ArcType.class);
            arcName = mock(ArcNameType.class);
            arcNameGraphics = mock(AnnotationGraphisType.class);
            graphics = mock(GraphicsArcType.class);

            arcToolspecificType = mock(ArcToolspecificType.class);
            labelPosition = mock(PositionType.class);

            // Setup behaviour
            when(arcToolspecificType.addNewDisplayProbabilityPosition()).thenReturn(labelPosition);
            when(arc.addNewInscription()).thenReturn(arcName);
            when(arc.addNewGraphics()).thenReturn(graphics);
            when(arc.addNewToolspecific()).thenReturn(arcToolspecificType);

            when(arcName.addNewGraphics()).thenReturn(arcNameGraphics);
            when(arcNameGraphics.addNewOffset()).thenReturn(mock(PositionType.class));
        }
    }

    private class DemoArcModelData {

        String id;
        String sourceId;
        String targetId;
        ArcModel arc;
        int weight;
        Point2D[] points;
        Point2D labelPosition;

        DemoArcModelData() {
            this("a1", "p1", "t1", 2);
        }

        DemoArcModelData(String id, String sourceId, String targetId, int weight) {

            this.id = id;
            this.sourceId = sourceId;
            this.weight = weight;
            this.targetId = targetId;
            points = new Point2D[0];
            labelPosition = new Point2D.Double(0, 0);

            arc = mock(ArcModel.class);
            when(arc.getInscriptionValue()).thenReturn(this.weight);
            when(arc.getWeightLabelPosition()).thenReturn(new Point2D.Double(0, 0));
            when(arc.getId()).thenReturn(this.id);
            when(arc.getSourceId()).thenReturn(this.sourceId);
            when(arc.getTargetId()).thenReturn(this.targetId);
            when(arc.getPoints()).thenReturn(points);
            when(arc.getProbabilityLabelPosition()).thenReturn(labelPosition);
            when(arc.getUnknownToolSpecs()).thenReturn(new Vector<>(0));
        }
    }

    private class DemoNet {
        PetriNetModelProcessor processor;
        ModelElementContainer container;
        PlaceModel p1;
        XORJoinSplitOperatorTransitionModel t1;

        DemoNet() {

            processor = new PetriNetModelProcessor();
            container = processor.getElementContainer();

            CreationMap placeMap = CreationMap.createMap();
            placeMap.setId("p1");
            placeMap.setType(AbstractPetriNetElementModel.PLACE_TYPE);
            p1 = (PlaceModel) ModelElementFactory.createModelElement(placeMap);
            container.addElement(p1);

            CreationMap map = CreationMap.createMap();
            map.setId("t1");
            map.setType(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE);
            map.setOperatorType(OperatorTransitionModel.XORJOIN_XORSPLIT_TYPE);
            t1 = (XORJoinSplitOperatorTransitionModel) ModelElementFactory.createModelElement(map);
            container.addElement(t1);
        }
    }
}