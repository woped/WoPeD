package org.woped.qualanalysis.soundness;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.woped.core.controller.IEditor;
import org.woped.core.model.AbstractElementModel;
import org.woped.qualanalysis.service.interfaces.IWorkflowCheck;
import org.woped.qualanalysis.soundness.algorithms.AlgorithmFactory;
import org.woped.qualanalysis.soundness.algorithms.basedonlowlevelpetrinet.workflow.IWorkflowTest;
import org.woped.qualanalysis.soundness.builder.BuilderFactory;
import org.woped.qualanalysis.soundness.datamodel.AbstractNode;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;

/**
 * 
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public class WorkflowCheckImplement implements IWorkflowCheck {
	
	private IEditor editor;
	private IWorkflowTest workflowTest;
	
	public WorkflowCheckImplement(IEditor editor) {
        this.editor = editor;
        workflowTest = AlgorithmFactory.createWorkflowTest(BuilderFactory.createLowLevelPetriNetWithoutTStarBuilder(
				editor).getLowLevelPetriNet());
    }

	@Override
	public Iterator<AbstractElementModel> getSourcePlacesIterator() {
		Set<AbstractElementModel> sourcePlaces = new HashSet<AbstractElementModel>();
        for (PlaceNode place : workflowTest.getSourcePlaces()) {
        	sourcePlaces.add(getAEM(place));
        }
        return sourcePlaces.iterator();
	}

	@Override
	public Iterator<AbstractElementModel> getSinkPlacesIterator() {
		Set<AbstractElementModel> sinkPlaces = new HashSet<AbstractElementModel>();
        for (PlaceNode place : workflowTest.getSinkPlaces()) {
        	sinkPlaces.add(getAEM(place));
        }
        return sinkPlaces.iterator();
	}

	@Override
	public Iterator<AbstractElementModel> getSourceTransitionsIterator() {
		Set<AbstractElementModel> sourceTransitions = new HashSet<AbstractElementModel>();
        for (TransitionNode transition : workflowTest.getSourceTransitions()) {
        	sourceTransitions.add(getAEM(transition));
        }
        return sourceTransitions.iterator();
	}
	
	@Override
	public Iterator<AbstractElementModel> getSinkTransitionsIterator() {
		Set<AbstractElementModel> sinkTransitions = new HashSet<AbstractElementModel>();
        for (TransitionNode transition : workflowTest.getSinkTransitions()) {
        	sinkTransitions.add(getAEM(transition));
        }
        return sinkTransitions.iterator();
	}

	@Override
	public Iterator<AbstractElementModel> getNotConnectedNodes() {
		Set<AbstractElementModel> notConnectedNodes = new HashSet<AbstractElementModel>();
        for (AbstractNode node : workflowTest.getNotConnectedNodes()) {
        	notConnectedNodes.add(getAEM(node));
        }
        return notConnectedNodes.iterator();
	}

	@Override
	public Iterator<AbstractElementModel> getNotStronglyConnectedNodes() {
		Set<AbstractElementModel> notStronglyConnectedNodes = new HashSet<AbstractElementModel>();
        for (AbstractNode node : workflowTest.getNotStronglyConnected()) {
        	notStronglyConnectedNodes.add(getAEM(node));
        }
        return notStronglyConnectedNodes.iterator();
	}
	
    /**
     * 
     * @param node the AbstractNode to get the referring AbstractElementModel from
     * @return the referred AbstractElementModel
     */
    private AbstractElementModel getAEM(AbstractNode node) {
        return editor.getModelProcessor().getElementContainer().getElementById(node.getOriginId());
    }
}
