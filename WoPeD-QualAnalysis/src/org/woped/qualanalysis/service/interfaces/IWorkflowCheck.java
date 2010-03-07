package org.woped.qualanalysis.service.interfaces;

import java.util.Set;

import org.woped.core.model.AbstractElementModel;

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
    public Set<AbstractElementModel> getSourcePlaces();

    /**
     * 
     * @return a set with all sink places
     */
    public Set<AbstractElementModel> getSinkPlaces();

    /**
     * 
     * @return a set with all transitions with empty preset
     */
    public Set<AbstractElementModel> getSourceTransitions();

    /**
     * 
     * @return a set with all transitions with empty postset
     */
    public Set<AbstractElementModel> getSinkTransitions();

    /**
     * 
     * @return a set with all nodes with empty preset and empty postset
     */
    public Set<AbstractElementModel> getNotConnectedNodes();

    /**
     * 
     * @return a set with all nodes not being strongly connected
     */
    public Set<AbstractElementModel> getNotStronglyConnectedNodes();

    /**
     * 
     * @return a set with strongly connected components.
     */
    public Set<Set<AbstractElementModel>> getStronglyConnectedComponents();

    /**
     * 
     * @return a set with strongly connected components.
     */
    public Set<Set<AbstractElementModel>> getConnectedComponents();

    /**
     * 
     * @return true if workflow-property is given
     */
    public boolean isWorkflowNet();
}
