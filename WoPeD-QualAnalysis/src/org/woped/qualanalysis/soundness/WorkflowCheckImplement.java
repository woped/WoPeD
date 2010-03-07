package org.woped.qualanalysis.soundness;

import java.util.HashSet;
import java.util.Set;

import org.woped.core.controller.IEditor;
import org.woped.core.model.AbstractElementModel;
import org.woped.qualanalysis.service.interfaces.IWorkflowCheck;
import org.woped.qualanalysis.soundness.algorithms.AlgorithmFactory;
import org.woped.qualanalysis.soundness.algorithms.basedonlowlevelpetrinet.sourcesink.ISourceSinkTest;
import org.woped.qualanalysis.soundness.builder.BuilderFactory;
import org.woped.qualanalysis.soundness.datamodel.AbstractNode;
import org.woped.qualanalysis.soundness.datamodel.LowLevelPetriNet;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;
import org.woped.qualanalysis.structure.StructuralAnalysis;

/**
 * 
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public class WorkflowCheckImplement implements IWorkflowCheck {

	private IEditor editor;
	private ISourceSinkTest sourceSinkTest;
	private LowLevelPetriNet lolNetWithTStar;
	private LowLevelPetriNet lolNetWithoutTStar;

	private Set<AbstractElementModel> sourcePlaces = null;
	private Set<AbstractElementModel> sinkPlaces = null;
	private Set<AbstractElementModel> sourceTransitions = null;
	private Set<AbstractElementModel> sinkTransitions = null;
	private Set<Set<AbstractNode>> uccs = null;
	private Set<Set<AbstractElementModel>> sccs = null;
	private Set<Set<AbstractElementModel>> ccs = null;

	public WorkflowCheckImplement(IEditor editor) {
		this.editor = editor;
		lolNetWithoutTStar = BuilderFactory.createLowLevelPetriNetWithoutTStarBuilder(editor).getLowLevelPetriNet();
		lolNetWithTStar = BuilderFactory.createLowLevelPetriNetWithTStarBuilder(editor).getLowLevelPetriNet();
		sourceSinkTest = AlgorithmFactory.createSourceSinkTest(lolNetWithoutTStar);

	}

	@Override
	public Set<AbstractElementModel> getSourcePlaces() {
		if (sourcePlaces == null) {
			sourcePlaces = new HashSet<AbstractElementModel>();
			for (PlaceNode place : sourceSinkTest.getSourcePlaces()) {
				sourcePlaces.add(getAEM(place));
			}
		}
		return sourcePlaces;
	}

	@Override
	public Set<AbstractElementModel> getSinkPlaces() {
		if (sinkPlaces == null) {
			sinkPlaces = new HashSet<AbstractElementModel>();
			for (PlaceNode place : sourceSinkTest.getSinkPlaces()) {
				sinkPlaces.add(getAEM(place));
			}
		}
		return sinkPlaces;
	}

	@Override
	public Set<AbstractElementModel> getSourceTransitions() {
		if (sourceTransitions == null) {
			sourceTransitions = new HashSet<AbstractElementModel>();
			for (TransitionNode transition : sourceSinkTest.getSourceTransitions()) {
				sourceTransitions.add(getAEM(transition));
			}
		}
		return sourceTransitions;
	}

	@Override
	public Set<AbstractElementModel> getSinkTransitions() {
		if (sinkTransitions == null) {
			sinkTransitions = new HashSet<AbstractElementModel>();

			for (TransitionNode transition : sourceSinkTest.getSinkTransitions()) {
				sinkTransitions.add(getAEM(transition));
			}
		}
		return sinkTransitions;
	}

	@Override
	public Set<AbstractElementModel> getNotConnectedNodes() {
		if (uccs == null) {
			uccs = AlgorithmFactory.createCcTest(lolNetWithoutTStar).getConnectedComponents();
		}

		if (uccs.size() > 1) {
			return new HashSet<AbstractElementModel>(editor.getModelProcessor().getElementContainer().getRootElements());
		}

		return new HashSet<AbstractElementModel>();
	}

	@Override
	public Set<AbstractElementModel> getNotStronglyConnectedNodes() {
		// delegate
		return new StructuralAnalysis(editor).getNotStronglyConnectedNodes();
	}

	@Override
	public Set<Set<AbstractElementModel>> getStronglyConnectedComponents() {
		if (sccs == null) {
			sccs = new HashSet<Set<AbstractElementModel>>();

			Set<AbstractElementModel> scc;
			for (Set<AbstractNode> set : AlgorithmFactory.createSccTest(lolNetWithTStar)
					.getStronglyConnectedComponents()) {
				if (set.size() > 0) {
					scc = new HashSet<AbstractElementModel>();

					for (AbstractNode node : set) {
						scc.add(getAEM(node));
					}
					// remove t*
					scc.remove(null);
					if (scc.size() > 0) {
						sccs.add(scc);
					}
				}
			}
		}

		return sccs;
	}

	@Override
	public Set<Set<AbstractElementModel>> getConnectedComponents() {
		if (ccs == null) {
			ccs = new HashSet<Set<AbstractElementModel>>();
			Set<AbstractElementModel> cc;
			for (Set<AbstractNode> set : AlgorithmFactory.createCcTest(lolNetWithoutTStar).getConnectedComponents()) {
				if (set.size() > 0) {
					cc = new HashSet<AbstractElementModel>();

					for (AbstractNode node : set) {
						cc.add(getAEM(node));
					}
					// remove t*
					cc.remove(null);
					if (cc.size() > 0) {
						ccs.add(cc);
					}
				}
			}
		}

		return ccs;
	}

	@Override
	public boolean isWorkflowNet() {
		if (getSourcePlaces().size() != 1) {
            return false;
        }
        if (getSinkPlaces().size() != 1) {
            return false;
        }
        if (getSourceTransitions().size() != 0) {
            return false;
        }
        if (getSinkTransitions().size() != 0) {
            return false;
        }
        if (getNotConnectedNodes().size() != 0) {
            return false;
        }
        if (getNotStronglyConnectedNodes().size() != 0) {
            return false;
        }
        return true;
	}

	/**
	 * 
	 * @param node
	 *            the AbstractNode to get the referring AbstractElementModel from
	 * @return the referred AbstractElementModel
	 */
	private AbstractElementModel getAEM(AbstractNode node) {
		return editor.getModelProcessor().getElementContainer().getElementById(node.getOriginId());
	}

}
