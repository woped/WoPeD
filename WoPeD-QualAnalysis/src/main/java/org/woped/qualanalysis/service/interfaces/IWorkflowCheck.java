package org.woped.qualanalysis.service.interfaces;

import java.util.Set;

import org.woped.core.model.ArcModel;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;

/**
 * interface for Workflow-parts of qualanalysis servies. all classes which implement the Workflow-methods of a service must implement this interface
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public interface IWorkflowCheck {

    /**
     * 
     * @return a set with all source places
     */
    public Set<AbstractPetriNetElementModel> getSourcePlaces();

    /**
     * 
     * @return a set with all sink places
     */
    public Set<AbstractPetriNetElementModel> getSinkPlaces();

    /**
     * 
     * @return a set with all transitions with empty preset
     */
    public Set<AbstractPetriNetElementModel> getSourceTransitions();

    /**
     * 
     * @return a set with all transitions with empty postset
     */
    public Set<AbstractPetriNetElementModel> getSinkTransitions();

    /**
     * 
     * @return a set with all nodes with empty preset and empty postset
     */
    public Set<AbstractPetriNetElementModel> getNotConnectedNodes();

    /**
     * 
     * @return a set with all nodes not being strongly connected
     */
    public Set<AbstractPetriNetElementModel> getNotStronglyConnectedNodes();

    /**
     * Gets the arcs of the petri net whose weight is larger than 1.
     *
     * @return the arcs that violate the weight condition
     */
    Set<ArcModel> getArcWeightViolations();

    /**
     * 
     * @return a set with strongly connected components.
     */
    public Set<Set<AbstractPetriNetElementModel>> getStronglyConnectedComponents();

    /**
     * 
     * @return a set with strongly connected components.
     */
    public Set<Set<AbstractPetriNetElementModel>> getConnectedComponents();

    /**
     * 
     * @return true if workflow-property is given
     */
    public boolean isWorkflowNet();
}
