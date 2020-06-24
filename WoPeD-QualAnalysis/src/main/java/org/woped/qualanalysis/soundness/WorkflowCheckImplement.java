package org.woped.qualanalysis.soundness;

import java.util.HashSet;
import java.util.Set;

import org.woped.core.controller.IEditor;
import org.woped.core.model.ArcModel;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.qualanalysis.service.interfaces.IWorkflowCheck;
import org.woped.qualanalysis.soundness.algorithms.AlgorithmFactory;
import org.woped.qualanalysis.soundness.algorithms.basedonlowlevelpetrinet.sourcesink.ISourceSinkTest;
import org.woped.qualanalysis.soundness.builder.BuilderFactory;
import org.woped.qualanalysis.soundness.datamodel.AbstractNode;
import org.woped.qualanalysis.soundness.datamodel.LowLevelPetriNet;
import org.woped.qualanalysis.structure.StructuralAnalysis;

/**
 * 
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public class WorkflowCheckImplement implements IWorkflowCheck {

    private IEditor editor = null;
    private ISourceSinkTest sourceSinkTest = null;
    private LowLevelPetriNet lolNetWithTStar = null;
    private LowLevelPetriNet lolNetWithoutTStar = null;

    private Set<AbstractPetriNetElementModel> sourcePlaces = null;
    private Set<AbstractPetriNetElementModel> sinkPlaces = null;
    private Set<AbstractPetriNetElementModel> sourceTransitions = null;
    private Set<AbstractPetriNetElementModel> sinkTransitions = null;
    private Set<Set<AbstractPetriNetElementModel>> stronglyConnectedComponents = null;
    private Set<Set<AbstractPetriNetElementModel>> connectedComponents = null;

    public WorkflowCheckImplement(IEditor editor) {
        this.editor = editor;
    }

    /**
     * 
     * @return the LowLevelPetriNet without t* (if not existing it will be instantiated)
     */
    public LowLevelPetriNet getLolNetWithoutTStar() {
        if (lolNetWithoutTStar == null) {
            lolNetWithoutTStar = BuilderFactory.createLowLevelPetriNetWithoutTStarBuilder(editor).getLowLevelPetriNet();
        }
        return lolNetWithoutTStar;
    }

    /**
     * 
     * @return the LowLevelPetriNet without t* (if not existing it will be instantiated)
     */
    public LowLevelPetriNet getLolNetWithTStar() {
        if (lolNetWithTStar == null) {
            lolNetWithTStar = BuilderFactory.createLowLevelPetriNetWithTStarBuilder(editor).getLowLevelPetriNet();
        }
        return lolNetWithTStar;
    }

    /**
     * 
     * @return the SourceSinkTest (if not existing it will be instantiated)
     */
    public ISourceSinkTest getSourceSinkTest() {
        if (sourceSinkTest == null) {
            sourceSinkTest = AlgorithmFactory.createSourceSinkTest(getLolNetWithoutTStar());
        }
        return sourceSinkTest;
    }

    @Override
    public Set<AbstractPetriNetElementModel> getSourcePlaces() {
        if (sourcePlaces == null) {

            sourcePlaces = translateAbstractNodeSet2AEMSet(getSourceSinkTest().getSourcePlaces());
        }
        return sourcePlaces;
    }

    @Override
    public Set<AbstractPetriNetElementModel> getSinkPlaces() {
        if (sinkPlaces == null) {

            sinkPlaces = translateAbstractNodeSet2AEMSet(getSourceSinkTest().getSinkPlaces());

        }
        return sinkPlaces;
    }

    @Override
    public Set<AbstractPetriNetElementModel> getSourceTransitions() {
        if (sourceTransitions == null) {

            sourceTransitions = translateAbstractNodeSet2AEMSet(getSourceSinkTest().getSourceTransitions());
        }
        return sourceTransitions;
    }

    @Override
    public Set<AbstractPetriNetElementModel> getSinkTransitions() {
        if (sinkTransitions == null) {

            sinkTransitions = translateAbstractNodeSet2AEMSet(getSourceSinkTest().getSinkTransitions());
        }
        return sinkTransitions;
    }

    @Override
    public Set<AbstractPetriNetElementModel> getNotConnectedNodes() {

        if (getConnectedComponents().size() > 1) {

            return new HashSet<AbstractPetriNetElementModel>(editor.getModelProcessor().getElementContainer().getRootElements());
        }

        return new HashSet<AbstractPetriNetElementModel>();
    }

    @Override
    public Set<AbstractPetriNetElementModel> getNotStronglyConnectedNodes() {
        // delegate
        return new StructuralAnalysis(editor).getNotStronglyConnectedNodes();
    }

    /**
     * Gets the arcs of the petri net whose weight is larger than 1.
     *
     * @return the arcs that violate the weight condition
     */
    @Override
    public Set<ArcModel> getArcWeightViolations() {
        return new StructuralAnalysis(editor).getArcWeightViolations();
    }

    @Override
    public Set<Set<AbstractPetriNetElementModel>> getStronglyConnectedComponents() {
        if (stronglyConnectedComponents == null) {

            stronglyConnectedComponents = translateAbstractNodeSetSet2AEMSetSet(AlgorithmFactory.createSccTest(
                    getLolNetWithTStar()).getStronglyConnectedComponents());
        }

        return stronglyConnectedComponents;
    }

    @Override
    public Set<Set<AbstractPetriNetElementModel>> getConnectedComponents() {
        if (connectedComponents == null) {

            connectedComponents = translateAbstractNodeSetSet2AEMSetSet(AlgorithmFactory.createCcTest(
                    getLolNetWithoutTStar()).getConnectedComponents());
        }

        return connectedComponents;
    }

    @Override
    public boolean isWorkflowNet() {
        return new StructuralAnalysis(editor).isWorkflowNet();
    }

    /**
     * 
     * @param node the AbstractNode to get the referring AbstractElementModel from
     * @return the referred AbstractElementModel
     */
    private AbstractPetriNetElementModel getAEM(AbstractNode node) {
        return editor.getModelProcessor().getElementContainer().getElementById(node.getOriginId());
    }

    /**
     * set transformation.
     * 
     * @author Sebastian Fu�
     * @param abstractNodeSet set of AbstractNode objects
     * @return set of AbstractElementModel objects
     */
    private Set<AbstractPetriNetElementModel> translateAbstractNodeSet2AEMSet(Set<? extends AbstractNode> abstractNodeSet) {
        Set<AbstractPetriNetElementModel> aemSet = new HashSet<AbstractPetriNetElementModel>();

        for (AbstractNode node : abstractNodeSet) {
            aemSet.add(getAEM(node));
        }

        // remove null value
        aemSet.remove(null);

        return aemSet;
    }

    /**
     * double set transformation.
     * 
     * @author Sebastian Fu�
     * @param abstractNodeSetSet set of an set of AbstractNode objects
     * @return set of an set of AbstractElementModel-Objects
     */
    private Set<Set<AbstractPetriNetElementModel>> translateAbstractNodeSetSet2AEMSetSet(
            Set<Set<AbstractNode>> abstractNodeSetSet) {

        Set<Set<AbstractPetriNetElementModel>> aemSetSet = new HashSet<Set<AbstractPetriNetElementModel>>();
        Set<AbstractPetriNetElementModel> aemSet;

        for (Set<AbstractNode> set : abstractNodeSetSet) {
            if (set.size() > 0) {
                aemSet = translateAbstractNodeSet2AEMSet(set);

                if (aemSet != null && aemSet.size() > 0) {
                    aemSetSet.add(aemSet);
                }
            }
        }

        return aemSetSet;
    }

}
