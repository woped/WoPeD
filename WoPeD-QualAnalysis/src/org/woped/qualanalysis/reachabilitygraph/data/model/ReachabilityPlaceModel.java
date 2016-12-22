/**
 * ReachabilityGraph implementation was done by Manuel Fladt and Benjamin Geiger.
 * The code was written for a project at BA Karlsruhe in 2007/2008 under authority
 * of Prof. Dr. Thomas Freytag and Andreas Eckleder.
 * <p>
 * This class was written by
 *
 * @author Benjamin Geiger
 */

package org.woped.qualanalysis.reachabilitygraph.data.model;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.woped.core.utilities.LoggerManager;
import org.woped.qualanalysis.Constants;
import org.woped.qualanalysis.soundness.marking.IMarking;
import org.woped.qualanalysis.soundness.marking.Marking;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.TreeMap;

public class ReachabilityPlaceModel extends DefaultGraphCell {

    private static final long serialVersionUID = 8798017712097428249L;
    private boolean isBoundSet = false;
    private boolean highlight = false;
    private boolean grayscaled = true;
    private Marking marking;


    public ReachabilityPlaceModel(Marking marking) {
        super(marking.asMultiSetString());
        this.marking = marking;
        AttributeMap attributes = getAttributes();
        GraphConstants.setMoveable(attributes, true);
        GraphConstants.setEditable(attributes, false);
        GraphConstants.setSizeable(attributes, false);
        // (x,y,w,h)
        GraphConstants.setBounds(attributes, new Rectangle2D.Double(0, 0, 80, 20));
        GraphConstants.setBackground(attributes, Color.lightGray);
        GraphConstants.setOpaque(attributes, true);
        setAttributes(attributes);
    }

    public IMarking getMarking() {
        return this.marking;
    }

    /**
     * gets the marking of a place if it has been used in a recursive algorithm
     *
     * @return
     */
    public boolean isSetRecursiveBounds() {
        return this.isBoundSet;
    }

    /**
     * marks a cell as processed for recursive algorithms
     *
     * @param set
     */
    public void setIsSetRecursiveBounds(boolean set) {
        this.isBoundSet = set;
    }

    /**
     * returns the tooltip-message for this marking
     *
     * @return
     */
    public String getToolTipText() {

        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<table>");
        sb.append("<tr><th>ID</th><th>#</th></tr>");

        TreeMap<String, Integer> tokenMap = this.marking.getMarking();
        for(String id: tokenMap.keySet()){
            String token = marking.isPlaceUnbound(id) ? Marking.UNBOUND_SIGN : String.valueOf(tokenMap.get(id));
            sb.append(String.format("<tr><td>%s</td><td>%s</td></tr>", id, token));
        }

        sb.append("</table>");
        sb.append("</html>");
        return sb.toString();
    }

    public boolean getHighlight() {
        return this.highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public boolean getGrayscaled() {
        return this.grayscaled;

    }

    public void setGrayscaled(boolean grayscaled) {
        this.grayscaled = grayscaled;
    }

    public void setMarkingNotation(String notation) {
        if (notation.equals("MultiSet")) this.setUserObject(marking.asMultiSetString());
        else if (notation.equals("TokenVector")) this.setUserObject(marking.asTokenVectorString());
        else
            LoggerManager.warn(Constants.QUALANALYSIS_LOGGER, "Try do set an unknown marking notation: " + notation);
    }
}
