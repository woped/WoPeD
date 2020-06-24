package org.woped.qualanalysis.soundness.marking;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.woped.core.utilities.ShortLexStringComparator;
import org.woped.qualanalysis.soundness.algorithms.generic.INode;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.datamodel.PlaceNodeComparator;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;

/**
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * @see IMarking
 */
public class Marking implements IMarking {
    public static final String UNBOUND_SIGN = "\u03c9"; // small greek omega
    private static int markingCounter = 0;

    // declaration
    private final Map<PlaceNode, Integer> placeToIndexMap = new HashMap<>();
    private final PlaceNode[] places;
    private final int[] tokens;
    private final boolean[] placeUnlimited;
    private final Set<Arc> successors = new HashSet<>();
    private IMarking predecessor;
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
            placeToIndexMap.put(places[i], i);
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

        for (int i = 0; i < tokens.length; i++) {
            if (placeUnlimited[i]) {
                result = prime * result + prime;
            } else {
                result = prime * result + tokens[i];
            }

        }
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
    @Override
    public boolean addSuccessor(Arc successor) {
        return successors.add(successor);
    }

    /**
     * @return a TreeMap with the placeId as String and the number of tokens on the key ID as value
     */
    public TreeMap<String, Integer> getMarking() {
        TreeMap<String, Integer> marking = new TreeMap<>(new ShortLexStringComparator());
        for (int i = 0; i < places.length; i++) {
            marking.put(places[i].getId(), tokens[i]);
        }
        return marking;
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

    @Override
    public boolean isPlaceUnbound(String placeId) {
        int pos = getIndexOfPlaceNode(placeId);
        return pos != -1 && placeUnlimited[pos];
    }

    private int getIndexOfPlaceNode(String placeId) {

        for (PlaceNode place : places) {
            if (place.getId().equals(placeId)) {
                return getIndexByPlace(place);
            }
        }

        return -1;
    }

    /**
     * @return the predecessor (marking)
     */
    @Override
    public IMarking getPredecessor() {
        return predecessor;
    }

    /**
     * @param predecessor the predecessor to set
     */
    @Override
    public void setPredecessor(IMarking predecessor) {
        this.predecessor = predecessor;
    }

    /**
     * @return the arcs that point to the successors (Set<Arc>)
     */
    @Override
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
        HashSet<String> transitions = new HashSet<>();
        for (Arc arc : getSuccessors()) {
            transitions.add(arc.getTrigger().getOriginId());
        }
        return transitions;
    }

    /**
     * Gets the set of places of the marking sorted by their id in short lex order.
     *
     * @return the set of places
     */
    @Override
    public SortedSet<PlaceNode> getPlaces() {
        TreeSet<PlaceNode> sortedPlaces = new TreeSet<>(new PlaceNodeComparator());
        sortedPlaces.addAll(Arrays.asList(this.places));

        return sortedPlaces;
    }

    /**
     * Gets the amount of tokens stored in the provided place
     *
     * @param place the place to get the tokens for
     * @return the amount of tokens stored in the provided place
     */
    @Override
    public int getTokens(PlaceNode place) {

        int index = getIndexByPlace(place);
        return tokens[index];
    }

    /**
     * Sets the amount of tokens of the given place to the provided value.
     *
     * @param place  the place to set the tokens
     * @param amount the new amount of tokens
     */
    @Override
    public void setTokens(PlaceNode place, int amount) {
        int idx = getIndexByPlace(place);
        tokens[idx] = amount;
    }

    /**
     * Returns if the provided place is unbound.
     *
     * @param place the place to check
     * @return true if the place is unbound, otherwise false
     */
    @Override
    public boolean isPlaceUnbound(PlaceNode place) {
        int idx = getIndexByPlace(place);

        return placeUnlimited[idx];
    }

    /**
     * Sets the unbound state of the provided place to the provided value.
     *
     * @param place   the place to set the unbound state
     * @param unbound the new state
     */
    @Override
    public void setPlaceUnbound(PlaceNode place, boolean unbound) {
        int idx = getIndexByPlace(place);
        placeUnlimited[idx] = unbound;
    }

    /**
     * Creates a copy of the current marking.
     * <p>
     * Only the reference to the contained places is copied. (shallow copy)
     *
     * @return a copy of the current marking
     */
    @Override
    public IMarking copy() {
        int size = tokens.length;
        int[] ctokens = new int[size];
        boolean[] cplaceUnlimited = new boolean[size];
        PlaceNode[] cplaces = new PlaceNode[size];

        for (int i = 0; i < size; i++) {
            cplaces[i] = this.places[i];
            ctokens[i] = this.tokens[i];
            cplaceUnlimited[i] = placeUnlimited[i];
        }

        return new Marking(ctokens, cplaces, cplaceUnlimited);
    }

    /**
     * Returns if the current element is less or equal than the provided other element.
     *
     * @param other the element to compare against
     * @return true if the current marking is less or equal than the other marking, otherwise false
     */
    @Override
    public boolean lessOrEqual(IMarking other) {

        SortedSet<PlaceNode> places = this.getPlaces();
        SortedSet<PlaceNode> otherPlaces = other.getPlaces();
        if (places.size() != otherPlaces.size()) return false;

        for (PlaceNode place : places) {

            if (!otherPlaces.contains(place)) return false;
            if (this.isPlaceUnbound(place) && !other.isPlaceUnbound(place)) return false;
            if (other.isPlaceUnbound(place)) continue;
            if (this.getTokens(place) > other.getTokens(place)) return false;
        }

        return true;
    }

    /**
     * Returns if the element is less than the other element.
     * <p>
     * Caution: Markings are not totally ordered. If a element is not less than an other element does not imply
     * that the element is greater than the other element.
     *
     * @param other the element to compare
     * @return true if the element is less than the other element, otherwise false
     */
    @Override
    public boolean less(IMarking other) {
        return this.lessOrEqual(other) && !this.equals(other);
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
            return index;
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
    @Override
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
    @Override
    public boolean isTransitionReachable(TransitionNode tn, Set<IMarking> markings) {
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
     * @return the token-array as string
     */
    @Override
    public String toString() {
        return asTokenVectorString();
    }

    /**
     * @see INode#getSuccessorNodes()
     */
    public Set<IMarking> getSuccessorNodes() {
        Set<IMarking> set = new HashSet<>();
        for (Arc arc : getSuccessors()) {
            set.add(arc.getTarget());
        }
        return set;
    }

    /**
     * @see INode#getPredecessorNodes()
     */
    public Set<IMarking> getPredecessorNodes() {
        Set<IMarking> set = new HashSet<>();
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
     *
     * @return a textual representation of the marking in multi set notation
     */
    @Override
    public String asMultiSetString() {
        StringBuilder result = new StringBuilder();
        result.append("(");

        SortedMap<String, Integer> placeIds = getSortedIdIndexMap();
        for (String id : placeIds.keySet()) {

            int pos = placeIds.get(id);
            if (tokens[pos] == 0 && !placeUnlimited[pos]) continue;

            if (result.length() == 1) result.append(" ");

            if (placeUnlimited[pos]) {
                result.append(UNBOUND_SIGN);
            } else if (tokens[pos] > 1) {
                result.append(tokens[pos]);
            }

            result.append(places[pos].getId()).append(" ");
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
     *
     * @return a textual representation of the marking in token vector notation
     */
    @Override
    public String asTokenVectorString() {
        StringBuilder result = new StringBuilder();
        result.append("(");
        SortedMap<String, Integer> idIndexMap = getSortedIdIndexMap();
        for (String id : idIndexMap.keySet()) {
            int pos = idIndexMap.get(id);

            if (result.length() == 1) result.append(" ");

            if (placeUnlimited[pos]) {
                result.append(Marking.UNBOUND_SIGN);
            } else {
                result.append(tokens[pos]);
            }

            result.append(" ");
        }

        result.append(")");
        return result.toString();
    }

    private SortedMap<String, Integer> getSortedIdIndexMap() {
        SortedMap<String, Integer> placeIds = new TreeMap<>(new ShortLexStringComparator());
        for (int i = 0; i < places.length; i++) {
            placeIds.put(places[i].getId(), i);
        }
        return placeIds;
    }
}
