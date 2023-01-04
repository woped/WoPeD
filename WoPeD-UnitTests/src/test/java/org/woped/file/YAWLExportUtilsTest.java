package org.woped.file;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import org.jgraph.graph.DefaultPort;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.model.ArcModel;
import org.woped.core.model.CreationMap;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.*;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.file.WoPeDToYAWL.YAWLExportUtils;

public class YAWLExportUtilsTest {

  @Before
  public void setUp() {
    ConfigurationManager.setConfiguration(ConfigurationManager.getStandardConfiguration());
  }

  @After
  public void tearDown() {
    ConfigurationManager.setConfiguration(ConfigurationManager.getStandardConfiguration());
  }

  @Test
  public void getNetName_test_Returnstest() {
    EditorVC editor = mock(EditorVC.class);
    when(editor.getName()).thenReturn("test.yawl");

    String netName = YAWLExportUtils.getNetName(editor);

    assertEquals("test", netName);
  }

  @Test
  public void getNormalizedString_MaxMustermann_ReturnsMax_Mustermann() {
    String input = "Max Mustermann";

    assertEquals("Max_Mustermann", YAWLExportUtils.getNormalizedString(input));
  }

  @Test
  public void hasResources_notEmpty_Returnstrue() {
    EditorVC editor = mock(EditorVC.class);
    PetriNetModelProcessor modelProcessor = mock(PetriNetModelProcessor.class);

    Vector<ResourceModel> resourceModel = new Vector<>();
    ResourceModel participantA = new ResourceModel("Max");
    resourceModel.add(participantA);
    Vector<ResourceClassModel> rolesModel = new Vector<>();
    Vector<ResourceClassModel> groupsModel = new Vector<>();

    when(editor.getModelProcessor()).thenReturn(modelProcessor);

    when(editor.getModelProcessor().getResources()).thenReturn(resourceModel);
    when(editor.getModelProcessor().getRoles()).thenReturn(rolesModel);
    when(editor.getModelProcessor().getOrganizationUnits()).thenReturn(groupsModel);

    boolean hasResources = YAWLExportUtils.hasResources(editor);

    assertTrue(hasResources);
  }

  @Test
  public void hasResources_empty_Returnsfalse() {
    EditorVC editor = mock(EditorVC.class);
    PetriNetModelProcessor modelProcessor = mock(PetriNetModelProcessor.class);

    Vector<ResourceModel> resourcesModel = new Vector<>();
    Vector<ResourceClassModel> rolesModel = new Vector<>();
    Vector<ResourceClassModel> groupsModel = new Vector<>();

    when(editor.getModelProcessor()).thenReturn(modelProcessor);

    when(editor.getModelProcessor().getResources()).thenReturn(resourcesModel);
    when(editor.getModelProcessor().getRoles()).thenReturn(rolesModel);
    when(editor.getModelProcessor().getOrganizationUnits()).thenReturn(groupsModel);

    boolean hasResources = YAWLExportUtils.hasResources(editor);

    assertFalse(hasResources);
  }

  @Test
  public void getRolesForResource_MaxIsManager_ReturnsManager() {
    PetriNetModelProcessor processor = Mockito.mock(PetriNetModelProcessor.class);
    EditorVC editor = Mockito.mock(EditorVC.class);
    ResourceModel testResource = new ResourceModel("Max");
    Vector<String> resourceIsAssignedTo = new Vector<>();
    resourceIsAssignedTo.add("Manager");

    when(editor.getModelProcessor()).thenReturn(processor);
    when(processor.getResourceClassesResourceIsAssignedTo(testResource.getName()))
        .thenReturn(resourceIsAssignedTo);
    when(processor.getOrganizationUnits()).thenReturn(new Vector<>());

    Vector<String> rolesForResource = YAWLExportUtils.getRolesForResource(editor, testResource);

    assertEquals("Manager", rolesForResource.get(0));
  }

  @Test
  public void generateDefaultPassword_ReturnsEncodedYAWLString() {
    String encodedPassword = YAWLExportUtils.generateDefaultPassword();

    assertEquals("Se4tMaQCi9gr0Q2usp7P56Sk5vM=", encodedPassword);
  }

  @Test
  public void isTransition_SimpleTrans_ReturnsTrue() {
    TransitionModel transition = new TransitionModel(CreationMap.createMap());

    assertTrue(YAWLExportUtils.isTransition(transition));
  }

  @Test
  public void isTransition_Place_ReturnsFalse() {
    PlaceModel place = new PlaceModel(CreationMap.createMap());

    assertFalse(YAWLExportUtils.isTransition(place));
  }

  @Test
  public void isImplicitXORSplit_ImplicitXORSplit_ReturnsTrue() {
    // Create the elements and arcs
    ModelElementContainer container = new ModelElementContainer();
    TransitionModel t1 = new TransitionModel(CreationMap.createMap());
    t1.setId("t1");
    TransitionModel t2 = new TransitionModel(CreationMap.createMap());
    t2.setId("t2");
    TransitionModel t3 = new TransitionModel(CreationMap.createMap());
    t3.setId("t3");
    PlaceModel p1 = new PlaceModel(CreationMap.createMap());
    p1.setId("p1");
    DemoArcModelData a1 = new DemoArcModelData("a1", "t1", "p1", 1);
    DemoArcModelData a2 = new DemoArcModelData("a2", "p1", "t2", 1);
    DemoArcModelData a3 = new DemoArcModelData("a3", "p1", "t3", 1);

    // Build the net
    container.addElement(t1);
    container.addElement(t2);
    container.addElement(t3);
    container.addElement(p1);
    container.addReference(a1.arc);
    container.addReference(a2.arc);
    container.addReference(a3.arc);

    assertTrue(YAWLExportUtils.isImplicitXORSplit(t1));
  }

  @Test
  public void isImplicitXORSplit_NoImplicitXORSplit_ReturnsFalse() {
    // Create the elements and arcs
    ModelElementContainer container = new ModelElementContainer();
    TransitionModel t1 = new TransitionModel(CreationMap.createMap());
    t1.setId("t1");
    TransitionModel t2 = new TransitionModel(CreationMap.createMap());
    t2.setId("t2");
    PlaceModel p1 = new PlaceModel(CreationMap.createMap());
    p1.setId("p1");
    DemoArcModelData a1 = new DemoArcModelData("a1", "t1", "p1", 1);
    DemoArcModelData a2 = new DemoArcModelData("a2", "p1", "t2", 1);

    // Build the net
    container.addElement(t1);
    container.addElement(t2);
    container.addElement(p1);
    container.addReference(a1.arc);
    container.addReference(a2.arc);

    assertFalse(YAWLExportUtils.isImplicitXORSplit(t1));
  }

  @Test
  public void isImplicitXORJoin_ImplicitXORJoin_ReturnsTrue() {
    // Create the elements and arcs
    ModelElementContainer container = new ModelElementContainer();
    TransitionModel t1 = new TransitionModel(CreationMap.createMap());
    t1.setId("t1");
    TransitionModel t2 = new TransitionModel(CreationMap.createMap());
    t2.setId("t2");
    TransitionModel t3 = new TransitionModel(CreationMap.createMap());
    t3.setId("t3");
    PlaceModel p1 = new PlaceModel(CreationMap.createMap());
    p1.setId("p1");
    DemoArcModelData a1 = new DemoArcModelData("a1", "t1", "p1", 1);
    DemoArcModelData a2 = new DemoArcModelData("a2", "t2", "p1", 1);
    DemoArcModelData a3 = new DemoArcModelData("a3", "p1", "t3", 1);

    // Build the net
    container.addElement(t1);
    container.addElement(t2);
    container.addElement(t3);
    container.addElement(p1);
    container.addReference(a1.arc);
    container.addReference(a2.arc);
    container.addReference(a3.arc);

    assertTrue(YAWLExportUtils.isImplicitXORJoin(t3));
  }

  @Test
  public void isImplicitXORJoin_NoImplicitXORJoin_ReturnsFalse() {
    // Create the elements and arcs
    ModelElementContainer container = new ModelElementContainer();
    TransitionModel t1 = new TransitionModel(CreationMap.createMap());
    t1.setId("t1");
    TransitionModel t2 = new TransitionModel(CreationMap.createMap());
    PlaceModel p1 = new PlaceModel(CreationMap.createMap());
    p1.setId("p1");
    DemoArcModelData a1 = new DemoArcModelData("a1", "t1", "p1", 1);
    DemoArcModelData a2 = new DemoArcModelData("a2", "p1", "t2", 1);

    // Build the net
    container.addElement(t1);
    container.addElement(t2);
    container.addElement(p1);
    container.addReference(a1.arc);
    container.addReference(a2.arc);

    assertFalse(YAWLExportUtils.isImplicitXORJoin(t2));
  }

  @Test
  public void isImplicitANDSplit_ImplicitANDSplit_ReturnsTrue() {
    // Create the elements and arcs
    ModelElementContainer container = new ModelElementContainer();
    TransitionModel t1 = new TransitionModel(CreationMap.createMap());
    t1.setId("t1");
    PlaceModel p1 = new PlaceModel(CreationMap.createMap());
    p1.setId("p1");
    PlaceModel p2 = new PlaceModel(CreationMap.createMap());
    p2.setId("p2");
    DemoArcModelData a1 = new DemoArcModelData("a1", "t1", "p1", 1);
    DemoArcModelData a2 = new DemoArcModelData("a2", "t1", "p2", 1);

    // Build the net
    container.addElement(t1);
    container.addElement(p1);
    container.addElement(p2);
    container.addReference(a1.arc);
    container.addReference(a2.arc);

    assertTrue(YAWLExportUtils.isImplicitANDSplit(t1));
  }

  @Test
  public void isImplicitANDSplit_NoImplicitANDSplit_ReturnsFalse() {
    // Create the elements and arcs
    ModelElementContainer container = new ModelElementContainer();
    TransitionModel t1 = new TransitionModel(CreationMap.createMap());
    t1.setId("t1");
    PlaceModel p1 = new PlaceModel(CreationMap.createMap());
    p1.setId("p1");
    DemoArcModelData a1 = new DemoArcModelData("a1", "t1", "p1", 1);

    // Build the net
    container.addElement(t1);
    container.addElement(p1);
    container.addReference(a1.arc);

    assertFalse(YAWLExportUtils.isImplicitANDSplit(t1));
  }

  @Test
  public void isImplicitANDJoin_ImplicitANDJoin_ReturnsTrue() {
    // Create the elements and arcs
    ModelElementContainer container = new ModelElementContainer();
    TransitionModel t1 = new TransitionModel(CreationMap.createMap());
    t1.setId("t1");
    PlaceModel p1 = new PlaceModel(CreationMap.createMap());
    p1.setId("p1");
    PlaceModel p2 = new PlaceModel(CreationMap.createMap());
    p2.setId("p2");
    DemoArcModelData a1 = new DemoArcModelData("a1", "p1", "t1", 1);
    DemoArcModelData a2 = new DemoArcModelData("a2", "p2", "t1", 1);

    // Build the net
    container.addElement(t1);
    container.addElement(p1);
    container.addElement(p2);
    container.addReference(a1.arc);
    container.addReference(a2.arc);

    assertTrue(YAWLExportUtils.isImplicitANDJoin(t1));
  }

  @Test
  public void isImplicitANDJoin_NoImplicitANDJoin_ReturnsFalse() {
    // Create the elements and arcs
    ModelElementContainer container = new ModelElementContainer();
    TransitionModel t1 = new TransitionModel(CreationMap.createMap());
    t1.setId("t1");
    PlaceModel p1 = new PlaceModel(CreationMap.createMap());
    p1.setId("p1");
    DemoArcModelData a1 = new DemoArcModelData("a1", "p1", "t1", 1);

    // Build the net
    container.addElement(t1);
    container.addElement(p1);
    container.addReference(a1.arc);

    assertFalse(YAWLExportUtils.isImplicitANDJoin(t1));
  }

  @Test
  public void getNextElements_TransitionWithExplicitPlaces_ReturnsPlaces() {
    // Create the elements and arcs
    ModelElementContainer container = new ModelElementContainer();
    TransitionModel t1 = new TransitionModel(CreationMap.createMap());
    t1.setId("t1");
    PlaceModel p1 = new PlaceModel(CreationMap.createMap());
    p1.setId("p1");
    PlaceModel p2 = new PlaceModel(CreationMap.createMap());
    p2.setId("p2");

    DefaultPort port1 = mock(DefaultPort.class);
    when(port1.getParent()).thenReturn(p1);

    ArcModel arc1 = mock(ArcModel.class);
    when(arc1.getId()).thenReturn("arc1");
    when(arc1.getSourceId()).thenReturn(t1.getId());
    when(arc1.getTargetId()).thenReturn(p1.getId());
    when(arc1.getTarget()).thenReturn(port1);

    DefaultPort port2 = mock(DefaultPort.class);
    when(port2.getParent()).thenReturn(p2);

    ArcModel arc2 = mock(ArcModel.class);
    when(arc2.getId()).thenReturn("arc2");
    when(arc2.getSourceId()).thenReturn(t1.getId());
    when(arc2.getTargetId()).thenReturn(p2.getId());
    when(arc2.getTarget()).thenReturn(port2);

    // Build the net
    container.addElement(t1);
    container.addElement(p1);
    container.addElement(p2);
    container.addReference(arc1);
    container.addReference(arc2);

    ConfigurationManager.getConfiguration().setYAWLExportExplicitPlaces(true);

    Collection<AbstractPetriNetElementModel> nextElements = YAWLExportUtils.getNextElements(t1);
    List<AbstractPetriNetElementModel> nextElementsList = new ArrayList<>(nextElements);

    assertEquals("p1", nextElementsList.get(0).getId());
    assertEquals("p2", nextElementsList.get(1).getId());
  }

  @Test
  public void getNextElements_TransitionWithImplicitPlaces_ReturnsTransitions() {
    // Create the elements and arcs
    ModelElementContainer container = new ModelElementContainer();
    TransitionModel t1 = new TransitionModel(CreationMap.createMap());
    t1.setId("t1");
    PlaceModel p1 = new PlaceModel(CreationMap.createMap());
    p1.setId("p1");
    TransitionModel t2 = new TransitionModel(CreationMap.createMap());
    t2.setId("t2");
    TransitionModel t3 = new TransitionModel(CreationMap.createMap());
    t3.setId("t3");

    DefaultPort port1 = mock(DefaultPort.class);
    when(port1.getParent()).thenReturn(p1);

    ArcModel arc1 = mock(ArcModel.class);
    when(arc1.getId()).thenReturn("arc1");
    when(arc1.getSourceId()).thenReturn(t1.getId());
    when(arc1.getTargetId()).thenReturn(p1.getId());
    when(arc1.getTarget()).thenReturn(port1);

    DefaultPort port2 = mock(DefaultPort.class);
    when(port2.getParent()).thenReturn(t2);

    ArcModel arc2 = mock(ArcModel.class);
    when(arc2.getId()).thenReturn("arc2");
    when(arc2.getSourceId()).thenReturn(p1.getId());
    when(arc2.getTargetId()).thenReturn(t2.getId());
    when(arc2.getTarget()).thenReturn(port2);

    DefaultPort port3 = mock(DefaultPort.class);
    when(port3.getParent()).thenReturn(t2);

    ArcModel arc3 = mock(ArcModel.class);
    when(arc3.getId()).thenReturn("arc3");
    when(arc3.getSourceId()).thenReturn(p1.getId());
    when(arc3.getTargetId()).thenReturn(t3.getId());
    when(arc3.getTarget()).thenReturn(port3);

    // Build the net
    container.addElement(t1);
    container.addElement(t2);
    container.addElement(t3);
    container.addElement(p1);
    container.addReference(arc1);
    container.addReference(arc2);
    container.addReference(arc3);

    ConfigurationManager.getConfiguration().setYAWLExportExplicitPlaces(false);

    Collection<AbstractPetriNetElementModel> nextElements = YAWLExportUtils.getNextElements(t1);
    List<AbstractPetriNetElementModel> nextElementsList = new ArrayList<>(nextElements);

    assertEquals("t3", nextElementsList.get(0).getId());
    assertEquals("t2", nextElementsList.get(1).getId());
  }

  // DemoArcModelData from PNMLExportTest
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
}
