package org.woped.qualanalysis.soundness.marking;

import org.woped.core.utilities.ShortLexStringComparator;
import org.woped.qualanalysis.soundness.algorithms.generic.INode;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;

import java.util.*;

/**
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * @see IMarking
 */
public class Marking implements IMarking, INode<Marking> {
    static final String UNBOUND_SIGN = "\u03c9"; // small greek omega
    private static int markingCounter = 0;
    // declaration
    private final Map<PlaceNode, Integer> placeToIndexMap = new HashMap<PlaceNode, Integer>();
    private final PlaceNode[] places;
    private final int[] tokens;
    private final boolean[] placeUnlimited;
    private final Set<Arc> successors = new HashSet<Arc>();
    private Marking predecessor;
    private boolean isInitial = false;
    private int markingID;
    // Cache the hash code unless something changes in our marking. -1 means the hash code
    // needs to be updated.
    private int cachedHashCode = -1;

    /**
     * @param tokens         an array with the number of tokens for each place in the same order as places
     * @param places         all Places in the right order
     * @param placeUnlimited an array with true where the places are unlimited in the same order as places
     */
    public Marking(int[] tokens, PlaceNode[] places, boolean[] placeUnlimited) {
        this.places = places;
        this.tokens = tokens.clone();
        this.placeUnlimited = placeUnlimited.clone();
        for (int i = 0; i < tokens.length; i++) {
            placeToIndexMap.put(places[i], new Integer(i));
        }
        markingID = markingCounter;
        markingCounter++;
    }

    public String getID() {
        return "" + markingID;
    }

    @Override
    public int hashCode() {
        if (cachedHashCode != -1) {
            return cachedHashCode;
        }
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(placeUnlimited);
        result = prime * result + Arrays.hashCode(tokens);
        cachedHashCode = result;
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
            // We only need to check one of the two unlimited arrays, the other one must be the same
            // due to the Arrays.equals() check above.
            if (!placeUnlimited[i]
                    && tokens[i] != other.tokens[i]) {
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
        TreeMap<String, Integer> marking = new TreeMap<>(new ShortLexStringComparator());
        for (int i = 0; i < places.length; i++) {
            marking.put(places[i].getId(), tokens[i]);
        }
        return marking;
    }

    /**
     * @return the array where UnlimitedPlaces are marked as true.
     * Do not manipulate the returned array!
     */
    public boolean[] getPlaceUnlimited() {
        return placeUnlimited;
    }

    /**
     * @param position position of the place being unlimited
     */
    public void setPlaceUnlimited(Integer position) {
        this.placeUnlimited[position] = true;
        // Reset token count for the new unlimited place to ensure we generate
        // the same hash for equivalent representations.
        this.tokens[position] = 0;
        // We need to recalculate the hash if we do this.
        cachedHashCode = -1;
    }

    /**
     * @return the predecessor (marking)
     */
    public Marking getPredecessor() {
        return predecessor;
    }

    /**
     * @param predecessor the predecessor to set
     */
    public void setPredecessor(Marking predecessor) {
        this.predecessor = predecessor;
    }

    /**
     * @return the arcs that point to the successors (Set<Arc>)
     */
    public Set<Arc> getSuccessors() {
        return this.successors;
    }

    /**
     * @return the tokens
     * Do not manipulate the returned array!
     */
    public int[] getTokens() {
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
     * @param compareMarking marking to compare
     * @return true, if markings are comparable and the marking is smaller or equal than the provided marking.
     */
    public boolean smallerEquals(Marking compareMarking) {
        boolean smallerEquals = true;
        for (int i = 0; i < this.tokens.length && smallerEquals; i++) {
            if (!compareMarking.placeUnlimited[i]
                    && (this.placeUnlimited[i] || this.tokens[i] > compareMarking.tokens[i])) {
                smallerEquals = false;
            }
        }
        return smallerEquals;
    }

    /**
     * Returns the index of a given place or -1 if not found
     *
     * @param place
     * @return
     */
    public int getIndexByPlace(PlaceNode place) {
        Integer index = placeToIndexMap.get(place);
        if (index == null) {
            return -1;
        } else {
            return index.intValue();
        }
    }

    /**
     * @return the isInitial
     */
    public boolean isInitial() {
        return isInitial;
    }

    /**
     * @param isInitial the isInitial to set
     */
    public void setInitial(boolean isInitial) {
        this.isInitial = isInitial;
    }

    /**
     * checks if the given transition is reachable from the current marking
     *
     * @param tn       the transition to reach
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
     * @return the token-array as string
     */
    @Override
    public String toString() {
        return asTokenVectorString();
    }

    /**
     * @see INode#getSuccessorNodes()
     */
    public Set<Marking> getSuccessorNodes() {
        Set<Marking> set = new HashSet<Marking>();
        for (Arc arc : getSuccessors()) {
            set.add(arc.getTarget());
        }
        return set;
    }

    /**
     * @see INode#getPredecessorNodes()
     */
    public Set<Marking> getPredecessorNodes() {
        Set<Marking> set = new HashSet<Marking>();
        set.add(predecessor);
        return set;
    }

    /**
     * Gets a textual representation of the marking in the multi set notation.
     * <p>
     * The multi set notation is specified as follows:
     * <ul>
     * <li>Places with no tokens are not contained in the output
     * <li>Places with one token contain only their place id
     * <li>Places with more than one token are listed as combination of token count and place id
     * </ul>
     * for example: {@code ( p2 2p3 )}
     * @return a textual representation of the marking in multi set notation
     */
    public String asMultiSetString() {
        StringBuilder result = new StringBuilder();
        result.append("(");

        SortedMap<String, Integer> placeIds = getSortedIdIndexMap();
        for(String id : placeIds.keySet()){

            int pos = placeIds.get(id);
            if (tokens[pos] == 0 && !placeUnlimited[pos]) continue;

            if (result.length() == 1) result.append(" ");

            if (placeUnlimited[pos]) {
                result.append(UNBOUND_SIGN);
            } else if (tokens[pos] > 1) {
                result.append(tokens[pos]);
            }

            result.append(places[pos].getId() + " ");
        }

        result.append(")");
        return result.toString();
    }

    /**
     * Gets a textual representation of the marking in the token vector notation.
     * <p>
     * The token vector notation is specified as follows:
     * <ul>
     * <li>Places are ordered in the short lex order of their id's
     * <li>For each place only the amount of tokens is contained
     * </ul>
     * for example: {@code ( 0 1 2 )}
     * @return a textual representation of the marking in token vector notation
     */
    public String asTokenVectorString(){
        StringBuilder result = new StringBuilder();
        result.append("(");
        SortedMap<String, Integer> idIndexMap = getSortedIdIndexMap();
        for(String id: idIndexMap.keySet()){
            int pos = idIndexMap.get(id);

            if(result.length() == 1) result.append(" ");

            if(placeUnlimited[pos]){
                result.append(Marking.UNBOUND_SIGN);
            }else {
                result.append(tokens[pos]);
            }

            result.append(" ");
        }

        result.append(")");
        return result.toString();
    }

    private SortedMap<String, Integer> getSortedIdIndexMap() {
        SortedMap<String, Integer> placeIds = new TreeMap<>(new ShortLexStringComparator());
        for (int i = 0; i < places.length; i++){
            placeIds.put(places[i].getId(), i);
        }
        return placeIds;
    }
}
