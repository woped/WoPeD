package org.woped.metrics.metricsCalculation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.woped.core.model.ArcModel;
import org.woped.metrics.exceptions.NaNException;

class LinearPathDescriptor {

    private HashMap<String, ArrayList<String>> pathMap = new HashMap<>();
    private Set<String> highlightedNodes = new HashSet<>();
    private List<ArcModel> arcs = new ArrayList<>();

    /**
     * Creates an object capable of analyzing paths through the net
     *
     * @param idMap  Map containing all nodes
     * @param arcMap Map containing all arcs
     */
    LinearPathDescriptor(Map<String, Map<String, Object>> idMap, Map<String, ArcModel> arcMap) {
        for (String key : idMap.keySet()) {
            ArrayList<String> children = new ArrayList<>();
            for (String subkey : idMap.get(key).keySet()) {
                ArcModel arc = arcMap.get(subkey);
                if (arc == null) continue;
                arcs.add(arc);
                children.add(arc.getTargetId());
            }
            pathMap.put(key, children);
        }
    }

    /**
     * Gets the ids of the currently highlighted nodes.
     *
     * @return the ids of the currently highlighted nodes.
     */
    Set<String> getHighlightedNodes() {
        return highlightedNodes;
    }

    /**
     * Gets the ids of the currently highlighted arcs.
     * <p>
     * Calculates the number of highlighted arcs based on prior requests
     *
     * @return Set of highlighted arc IDs
     */
    Set<String> getHighlightedArcIds() {
        Set<String> highlightedArcs = new HashSet<>();
        for (ArcModel arc : arcs)
            if (highlightedNodes.contains(arc.getSourceId()) && highlightedNodes.contains(arc.getTargetId()))
                highlightedArcs.add(arc.getId());
        return highlightedArcs;
    }

    /**
     * Gets the number of nodes which have a path to it self.
     * <p>
     * The cyclic nodes are going to be highlighted.
     *
     * @return the number of cyclic nodes
     */
    int getNumberOfCyclicNodes() {
        highlightedNodes = new HashSet<>();

        int cyclic = 0;
        for (String sourceID : pathMap.keySet())
            if (hasCycle(sourceID)) {
                cyclic++;

                // Highlighting
                highlightedNodes.add(sourceID);
            }

        return cyclic;
    }

    /**
     * Checks if the element with the sourceId has a path to itself.
     *
     * @param sourceId the element to check. Not null.
     * @return true if a cycle exists, otherwise false.
     */
    private boolean hasCycle(String sourceId) {
        return hasPath(sourceId, sourceId, new HashSet<String>());
    }

    /**
     * Checks if a path from the element with to sourceId to the element with the targetId exists.
     * <p>
     * The method uses itself recursively with the depth-first search algorithm.
     *
     * @param sourceID ID of the current node
     * @param findWhat ID of the searched node
     * @param visited  Set of all visited nodes
     * @return A boolean containint whether or not the child was found
     */
    private boolean hasPath(String sourceID, String findWhat, HashSet<String> visited) {
        if (visited.contains(sourceID)) return false;
        visited.add(sourceID);
        for (String s : pathMap.get(sourceID))
            if (s.equals(findWhat)) return true;
            else if (hasPath(s, findWhat, visited)) return true;
        return false;
    }

    /**
     * Finds and returns the longest path
     *
     * @return The longest path from beginning to end
     * @throws NaNException This exception occurs if the net is no workflow net and thus has no longest path by definition
     */
    public double longestPath() throws NaNException {
        highlightedNodes = new HashSet<String>();
        HashSet<String> nodes = new HashSet<String>();
        for (String key : pathMap.keySet())
            nodes.add(key);

        for (String key : pathMap.keySet())
            for (String childKey : pathMap.get(key))
                nodes.remove(childKey);

        if (nodes.size() != 1) throw new NaNException();

        String startNode = null;
        for (String key : nodes)
            startNode = key;

        return longestPath(1, startNode, new HashSet<String>(), highlightedNodes);
    }

    /**
     * Finds and returns the longest path (recursive internal method)
     *
     * @param length       Current length of the checked path
     * @param myKey        Current node being analyzed
     * @param previousKeys List of previously visited nodes
     * @param maxset       Largest Set found so far
     * @return The longest path from beginning to end
     */
    private double longestPath(double length, String myKey, HashSet<String> previousKeys, Set<String> maxset) {
        if (pathMap.get(myKey).isEmpty()) {
            if (length > maxset.size()) {
                maxset.clear();
                for (String key : previousKeys)
                    maxset.add(key);
                maxset.add(myKey);
            }
            return length;
        }
        if (previousKeys.contains(myKey)) return -1;
        HashSet<String> newPrevKeys = new HashSet<String>();
        for (String key : previousKeys)
            newPrevKeys.add(key);
        newPrevKeys.add(myKey);
        double maxlength = -2;
        for (String child : pathMap.get(myKey))
            maxlength = Math.max(maxlength, longestPath(length + 1, child, newPrevKeys, maxset));
        return maxlength;
    }

    /**
     * Calculates the number of cut vertices
     *
     * @return Number of cut vertices
     * @throws NaNException This exception occurs if the net is no workflow net and thus has no cut vertices by definition
     */
    public double cutVertices() throws NaNException {
        if (!isFinite()) throw new NaNException();

        highlightedNodes = new HashSet<String>();

        double vertices = 0;
        HashSet<String> nodes = new HashSet<String>();
        for (String key : pathMap.keySet())
            nodes.add(key);

        for (String key : pathMap.keySet())
            for (String childKey : pathMap.get(key))
                nodes.remove(childKey);

        if (nodes.size() < 1) throw new NaNException();

        Set<String> keySet = pathMap.keySet();

        String startNode = null;
        for (String key : nodes)
            startNode = key;
        for (String key : keySet) {
            HashMap<String, ArrayList<String>> newMap = (HashMap<String, ArrayList<String>>) pathMap.clone();
            newMap.put(key, new ArrayList<String>());
            if (!isConnected(startNode, newMap, new HashSet<String>(), key)) {
                vertices++;

                //Highlight
                highlightedNodes.add(key);
            }

        }
        return vertices;
    }

    /**
     * Checks if a node is finite and thus a linear path analysis even makes sense
     *
     * @return Boolean containing whether or not the net is finite
     */
    private boolean isFinite() {
        boolean hasStart = false;
        boolean hasEnd = false;
        HashSet<String> ids = new HashSet<>();
        for (String key : pathMap.keySet())
            ids.add(key);
        for (String key : pathMap.keySet()) {
            if (pathMap.get(key).isEmpty()) hasEnd = true;
            for (String subKey : pathMap.get(key))
                ids.remove(subKey);
        }
        if (!ids.isEmpty()) hasStart = true;
        return hasEnd && hasStart;
    }

    /**
     * Checks whether or not the net is still connected if a node is killed
     *
     * @param myKey        Current key checked in this recursive method
     * @param newMap       Map containing the state after the node deletion
     * @param previousKeys Previously checked nodes in the net
     * @param killedKey    ID of the removed key
     * @return true if the net is still connected, otherwise false
     */
    private boolean isConnected(String myKey, HashMap<String, ArrayList<String>> newMap, HashSet<String> previousKeys, String killedKey) {
        if (myKey.equals(killedKey) && previousKeys.isEmpty()) return true;
        if (newMap.get(myKey).isEmpty() && (!myKey.equals(killedKey) || pathMap.get(myKey).isEmpty())) return true;
        if (previousKeys.contains(myKey)) return false;
        HashSet<String> newPrevKeys = new HashSet<>();
        for (String key : previousKeys)
            newPrevKeys.add(key);
        newPrevKeys.add(myKey);
        boolean connected = false;
        for (String child : newMap.get(myKey))
            connected = connected || isConnected(child, newMap, newPrevKeys, killedKey);
        return connected;
    }
}
