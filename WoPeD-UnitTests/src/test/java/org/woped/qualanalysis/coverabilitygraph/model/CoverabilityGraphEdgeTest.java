package org.woped.qualanalysis.coverabilitygraph.model;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;

public class CoverabilityGraphEdgeTest {
  @Before
  public void setUp() throws Exception {}

  @After
  public void tearDown() throws Exception {}

  @Test
  public void toString_oneTrigger_containsOnlyTriggerId() throws Exception {
    TransitionNode t1 =
        new TransitionNode(
            "t1", "create Something", "originId", AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);

    CoverabilityGraphEdge cut = new CoverabilityGraphEdge(t1);

    String expected = "create Something";
    String actual = cut.toString();

    assertEquals(expected, actual);
  }

  @Test
  public void toString_twoTriggers_containsBothTriggerIds() throws Exception {
    TransitionNode t1 =
        new TransitionNode(
            "t10", "create Something", "originId", AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
    TransitionNode t2 =
        new TransitionNode(
            "t2",
            "create Something else",
            "otherOriginId",
            AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);

    CoverabilityGraphEdge cut = new CoverabilityGraphEdge(t1);
    cut.addTrigger(t2);

    // id order, not lex order of names
    String expected =
        "create Something else " + CoverabilityGraphEdge.TRANSITION_DELIMITER + " create Something";
    String actual = cut.toString();

    assertEquals(expected, actual);
  }
}
