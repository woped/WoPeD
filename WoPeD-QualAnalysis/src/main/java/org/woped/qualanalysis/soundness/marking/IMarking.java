package org.woped.qualanalysis.soundness.marking;

import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import org.woped.qualanalysis.soundness.algorithms.generic.INode;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;

/**
 * This is a marking in a marking net compatible to the Woped reachability graph
 *
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 */
public interface IMarking extends INode<IMarking> {

    /**
     * @return true, if the marking is the initial marking.
     */
    boolean isInitial();

    /**
     * @param successor the succeeding arc
     * @return the success of adding the arc
     */
    boolean addSuccessor(Arc successor);

    /**
     * @return Map the key of the map is the id of an place. the value is the actual token count of the the place with the id of the key.
     */
    SortedMap<String, Integer> getMarking();

    /**
     * Checks if the place with the given id is unbound.
     *
     * @param placeId the id of the place to check
     * @return true if the place is unbound, otherwise false
     */
    boolean isPlaceUnbound(String placeId);

    /**
     * @return the predecessor (marking)
     */
    IMarking getPredecessor();

    /**
     * @param predecessor the predecessor to set
     */
    void setPredecessor(IMarking predecessor);

    /**
     * @return the arcs that point to the successors (Set<Arc>)
     */
    Set<Arc> getSuccessors();

    /**
     * @return all activated transitions.
     */
    Set<String> getActivatedTransitions();

    /**
     * Gets the set of places of the marking sorted by their id in short lex order.
     *
     * @return the set of places
     */
    SortedSet<PlaceNode> getPlaces();

    /**
     * Gets the amount of tokens stored in the provided place
     *
     * @param place the place to get the tokens for
     * @return the amount of tokens stored in the provided place
     */
    int getTokens(PlaceNode place);

    /**
     * Sets the amount of tokens of the given place to the provided value.
     *
     * @param place  the place to set the tokens
     * @param amount the new amount of tokens
     */
    void setTokens(PlaceNode place, int amount);

    /**
     * Returns if the provided place is unbound.
     *
     * @param place the place to check
     * @return true if the place is unbound, otherwise false
     */
    boolean isPlaceUnbound(PlaceNode place);

    /**
     * Sets the unbound state of the provided place to the provided value.
     *
     * @param place   the place to set the unbound state
     * @param unbound the new state
     */
    void setPlaceUnbound(PlaceNode place, boolean unbound);

    /**
     * Creates a copy of the current marking.
     * <p>
     * Only the reference to the contained places is copied. (shallow copy)
     *
     * @return a copy of the current marking
     */
    IMarking copy();

    /**
     * Returns if the current element is less or equal to the provided other element.
     *
     * @param other the element to compare against
     * @return true if the current marking is less or equal than the other marking, otherwise false
     */
    boolean lessOrEqual(IMarking other);

    /**
     * Returns if the element is less than the other element.
     * <p>
     * Caution: Markings are not totally ordered. If a element is not less than an other element does not imply
     * that the element is greater than the other element.
     *
     * @param other the element to compare
     * @return true if the element is less than the other element, otherwise false
     */
    boolean less(IMarking other);


    /**
     * @param isInitial the isInitial to set
     */
    void setInitial(boolean isInitial);

    /**
     * checks if the given transition is reachable from the current marking
     *
     * @param tn       the transition to reach
     * @param markings a set of markings already checked
     * @return true if the Transition is reachable
     */
    boolean isTransitionReachable(TransitionNode tn, Set<IMarking> markings);

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
    String asMultiSetString();

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
    String asTokenVectorString();
}
