package org.woped.file;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.IStatusBar;
import org.woped.core.controller.IViewController;
import org.woped.core.model.ArcModel;
import org.woped.pnml.*;

import java.awt.geom.Point2D;
import java.util.Vector;

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

        ArcNameType exportArc = mock(ArcNameType.class);

        ArcModel arc = mock(ArcModel.class);
        when(arc.getInscriptionValue()).thenReturn(arcWeight);

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

    private class DemoArcTypeData {

        ArcType arc;
        ArcNameType arcName;
        GraphicsArcType graphics;
        ArcToolspecificType arcToolspecificType;
        PositionType labelPosition;

        DemoArcTypeData() {

            arc = mock(ArcType.class);
            arcName = mock(ArcNameType.class);
            graphics = mock(GraphicsArcType.class);
            arcToolspecificType = mock(ArcToolspecificType.class);
            labelPosition = mock(PositionType.class);

            // Setup behaviour
            when(arcToolspecificType.addNewDisplayProbabilityPosition()).thenReturn(labelPosition);
            when(arc.addNewInscription()).thenReturn(arcName);
            when(arc.addNewGraphics()).thenReturn(graphics);
            when(arc.addNewToolspecific()).thenReturn(arcToolspecificType);
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
            when(arc.getId()).thenReturn(this.id);
            when(arc.getSourceId()).thenReturn(this.sourceId);
            when(arc.getTargetId()).thenReturn(this.targetId);
            when(arc.getPoints()).thenReturn(points);
            when(arc.getLabelPosition()).thenReturn(labelPosition);
            when(arc.getUnknownToolSpecs()).thenReturn(new Vector<>(0));
        }
    }
}