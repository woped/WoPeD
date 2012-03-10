package org.woped.qualanalysis.soundness.marking;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

import org.woped.qualanalysis.soundness.algorithms.generic.INode;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;

/**
 * @see IMarking
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 */
public class Marking implements IMarking, INode<Marking> {
    // declaration
    private final PlaceNode[] places;
    private final Integer[] tokens;
    private final Boolean[] placeUnlimited;
    private final Set<Arc> successors = new HashSet<Arc>();
    private Marking predecessor;
    private boolean isInitial = false;
    private int markingID;
    
    private static int markingCounter = 0;
    

    /**
     * 
     * @param tokens an array with the number of tokens for each place in the same order as places
     * @param places all Places in the right order
     * @param placeUnlimited an array with true where the places are unlimited in the same order as places
     */
    public Marking(Integer[] tokens, PlaceNode[] places, Boolean[] placeUnlimited) {
        this.places = places;
        this.tokens = new Integer[tokens.length];
        this.placeUnlimited = new Boolean[placeUnlimited.length];
        for (int i = 0; i < tokens.length; i++) {
            this.tokens[i] = tokens[i];
            this.placeUnlimited[i] = placeUnlimited[i];
        }
        markingID = markingCounter;
        markingCounter++;
    }
    
    public String getID(){
    	return ""+markingID;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(placeUnlimited);
        result = prime * result + Arrays.hashCode(tokens);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Marking other = (Marking) obj;

        if (!Arrays.equals(placeUnlimited, other.placeUnlimited)) {
            return false;
        }
        
        for (int i = 0; i < tokens.length; i++) {

            if (placeUnlimited[i] && other.placeUnlimited[i]) {
                // it's okay
            } else
                if (tokens[i].intValue() != other.tokens[i].intValue()) {
                    return false;
                }

        }
        return true;
    }

    /**
     * @param successor the succeeding arc
     * @return the success of adding the arc
     */
    public boolean addSuccessor(Arc successor) {
    	return successors.add(successor);
    }

    /**
     * @return a TreeMap with the placeId as String and the number of tokens on the key Id as value
     */
    public TreeMap<String, Integer> getMarking() {
        TreeMap<String, Integer> marking = new TreeMap<String, Integer>();
        for (int i = 0; i < places.length; i++) {
            marking.put(places[i].getId(), tokens[i]);
        }
        return marking;
    }

    /**
     * 
     * @return the array where UnlimitedPlaces are marked as true
     */
    public Boolean[] getPlaceUnlimited() {
        return placeUnlimited;
    }

    /**
     * @return the predecessor (marking)
     */
    public Marking getPredecessor() {
        return predecessor;
    }

    /**
     * @return the arcs that point to the successors (Set<Arc>)
     */
    public Set<Arc> getSuccessors() {
        return this.successors;
    }

    /**
     * @return the tokens
     */
    public Integer[] getTokens() {
        return this.tokens;
    }

    /**
     * @see IMarking#getActivatedTransitions()
     */
    @Override
    public HashSet<String> getActivatedTransitions() {
        HashSet<String> transitions = new HashSet<String>();
        for (Arc arc : getSuccessors()) {
            transitions.add(arc.getTrigger().getOriginId());
        }
        return transitions;
    }

    /**
     * 
     * @param compareMarking marking to compare
     * @return true, if markings are comparable and the marking is smaller or equal than the provided marking.
     */
    public boolean smallerEquals(Marking compareMarking) {
        boolean smallerEquals = true;
        for (int i = 0; i < this.tokens.length && smallerEquals; i++) {
            if (!(this.tokens[i] <= compareMarking.tokens[i])) {
                smallerEquals = false;
            }
            if (compareMarking.placeUnlimited[i]) {
                smallerEquals = true;
            }
        }
        return smallerEquals;
    }

    /**
     * @return the isInitial
     */
    public boolean isInitial() {
        return isInitial;
    }

    /**
     * 
     * checks if the given transition is reachable from the current marking
     * 
     * @param tn the transition to reach
     * @param markings a set of markings already checked
     * @return true if the Transition is reachable
     */
    public boolean isTransitionReachable(TransitionNode tn, Set<Marking> markings) {
        for (Arc arc : successors) {
            if (arc.getTrigger().equals(tn)) {
                return true;
            }
            if (!markings.contains(arc.getTarget())) {
                markings.add(arc.getTarget());
                if (arc.getTarget().isTransitionReachable(tn, markings)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @return the predecessor as string
     */
    public String predecessorToString() {
        String line = "Predecessor:";
        if (this.predecessor != null) {
            line += "\r\n" + this.predecessor.toString();
        }
        return line;

    }

    /**
     * @param isInitial the isInitial to set
     */
    public void setInitial(boolean isInitial) {
        this.isInitial = isInitial;
    }

    /**
     * @param position position of the place being unlimited
     */
    public void setPlaceUnlimited(Integer position) {
        this.placeUnlimited[position] = true;
    }

    /**
     * @param predecessor the predecessor to set
     */
    public void setPredecessor(Marking predecessor) {
        this.predecessor = predecessor;
    }

    /**
     * @return the token-array as string
     */
    @Override
    public String toString() {
        String line = "(";
        for (int i = 0; i < this.tokens.length; i++) {

            if (this.placeUnlimited[i]) {
                line += "\u221E";
            } else {
                line += this.tokens[i];
            }
            line += " ";
        }
        return line.substring(0, line.length() - 1) + ")";
    }

    /**
     * @see INode#getPostNodes()
     */
    @Override
    public Set<Marking> getPostNodes() {
        Set<Marking> set = new HashSet<Marking>();
        for (Arc arc : getSuccessors()) {
            set.add(arc.getTarget());
        }
        return set;
    }

    /**
     * @see INode#getPreNodes()
     */
    @Override
    public Set<Marking> getPreNodes() {
        Set<Marking> set = new HashSet<Marking>();
        set.add(predecessor);
        return set;
    }
}
