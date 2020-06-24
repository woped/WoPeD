package org.woped.qualanalysis.soundness.algorithms.basedonlowlevelpetrinet.scomponent;

import java.util.HashSet;
import java.util.Set;

import org.woped.qualanalysis.soundness.algorithms.basedonlowlevelpetrinet.AbstractLowLevelPetriNetTest;
import org.woped.qualanalysis.soundness.datamodel.AbstractNode;
import org.woped.qualanalysis.soundness.datamodel.LowLevelPetriNet;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;




/**
 * @see ISComponentTest
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 *
 */
 public class SComponentTest extends AbstractLowLevelPetriNetTest implements ISComponentTest {
	
    private Set<Set<AbstractNode>> sComponents;
    private HashSet<PlaceNode> uncoveredPlaces;

    /**
     * 
     * @param lolNetWithTStar LowLevelPetriNet (with t*) the algorithm is based on
     */  
    public SComponentTest(LowLevelPetriNet lolNetWithTStar) {
        super(lolNetWithTStar);
        reCalculate();
    }

    @Override
    public Set<PlaceNode> getNotSCovered() {
        return uncoveredPlaces;
    }

    @Override
    public Set<Set<AbstractNode>> getSComponents() {
    	return sComponents;
    }

    public void reCalculate() {
        sComponents = new HashSet<Set<AbstractNode>>();
        uncoveredPlaces = new HashSet<PlaceNode>();
        searchCoverable();
    }

    private void searchCoverable() {
        HashSet<AbstractNode> component;
        HashSet<PlaceNode> unvisitedPlaces;

        // at first, all places are unvisited and uncovered.

        uncoveredPlaces.addAll(lolNet.getPlaces());
        unvisitedPlaces = (HashSet<PlaceNode>) uncoveredPlaces.clone();

        while (!unvisitedPlaces.isEmpty()) {

            // 3. Schritt
            PlaceNode place = unvisitedPlaces.iterator().next();

            // 4.Schritt
            unvisitedPlaces.remove(place);
            component = new HashSet<AbstractNode>();
            component.add(place);

            // 5.Schritt
            searchComponent(component, new HashSet<AbstractNode>());

            // 6.Schritt
            // sComponents.addAll(SK);

            // 7.Schritt

            for (Set<AbstractNode> nodeSet : sComponents) {
                unvisitedPlaces.removeAll(nodeSet);
            }
        }

        // 8.Schritt
        for (Set<AbstractNode> nodeSet : sComponents) {
            uncoveredPlaces.removeAll(nodeSet);
        }

    }

    private void searchComponent(HashSet<AbstractNode> component, HashSet<AbstractNode> checkedCompEntries) {
        HashSet<AbstractNode> uncheckedCompEntries = (HashSet<AbstractNode>) component.clone();
        uncheckedCompEntries.removeAll(checkedCompEntries);
        
        while (!uncheckedCompEntries.isEmpty()) {
            // 1.Schritt
            AbstractNode node = uncheckedCompEntries.iterator().next();
            checkedCompEntries.add(node); 
            // 2.Schritt
            if (PlaceNode.class == node.getClass()) {
                for ( AbstractNode abstractNode : node.getSuccessorNodes() ) {
                    component.add(abstractNode);
                }
                for ( AbstractNode abstractNode : node.getPredecessorNodes() ) {
                    component.add(abstractNode);
                }
            }
            // 3.Schritt
            else
                if (TransitionNode.class == node.getClass()) {
                    Set<AbstractNode> postNodes = node.getSuccessorNodes();
                    if (postNodes.size() == 1) {
                        component.add(postNodes.iterator().next());
                    }
                    // 4.Schritt
                    if (postNodes.size() > 1) {
                        for (AbstractNode abstractNode : postNodes) {
                            // 4.1 SChritt
                            HashSet<AbstractNode> componentCopy = (HashSet<AbstractNode>) component.clone();
                            componentCopy.add(abstractNode);
                            // 4.2 Schritt
                            searchComponent(componentCopy, (HashSet<AbstractNode>) checkedCompEntries.clone());
                            // 4.3Schritt
                        }
                    }
                }
            // 5. Schritt
            uncheckedCompEntries = (HashSet<AbstractNode>) component.clone();
			uncheckedCompEntries.removeAll(checkedCompEntries);
        }
        // Schritt 6
        boolean componentValid = true;
        // Schritt7
        for (AbstractNode abstractNode : component) {
            if (abstractNode.getClass() == TransitionNode.class) {
                HashSet<AbstractNode> postNodesInComponent = new HashSet<AbstractNode>();
                for ( AbstractNode postNode : abstractNode.getSuccessorNodes() ) {
                    if (component.contains(postNode)) {
                        postNodesInComponent.add(postNode);
                    }
                }
                if (postNodesInComponent.size() != 1) {
                    componentValid = false;
                } else {
		            HashSet<AbstractNode> preNodesInComponent = new HashSet<AbstractNode>();
                    for ( AbstractNode preNode : abstractNode.getPredecessorNodes() ) {
                        if ( component.contains(preNode) ) {
                            preNodesInComponent.add(preNode);
		                }
		            }
		            if (preNodesInComponent.size() != 1) {
		                componentValid = false;
		            }
                }
            }
        }
        // Schritt 8 ohne Trajan
        if (componentValid) {
            sComponents.add(component);
        }
    }
}