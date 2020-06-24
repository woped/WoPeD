/*
 * 
 * Copyright (C) 2004-2005, see @author in JavaDoc for the author 
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *
 * For contact information please visit http://woped.dhbw-karlsruhe.de
 *
 */
package org.woped.core.model;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.PortView;
import org.woped.core.Constants;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.config.DefaultStaticConfiguration;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.GroupModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.utilities.LoggerManager;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <p>
 *         <p>
 *         19.04.2003
 */

@SuppressWarnings("serial")
public class ArcModel extends DefaultEdge implements Serializable {

    private String id;
    private boolean activated = false;
    private boolean highlighted = false;
    private Vector<Object> unknownToolSpecs = new Vector<Object>();
    private ElementContext elementContext = null;
    private boolean DEFAULT_WEIGHT_VISIBLE = false;

    /**
     * Constructor for ArcModel.
     */
    public ArcModel() {
        this(null);
    }

    /**
     * Constructor for ArcModel.
     *
     * @param userObject
     */
    public ArcModel(Object userObject) {
        super(userObject);
        this.elementContext = new ElementContext();
        initAttributes();
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void initAttributes() {
        AttributeMap map = getAttributes();
        GraphConstants.setEditable(map, false);
        GraphConstants.setBendable(map, true);
        GraphConstants.setLineStyle(map, ConfigurationManager.getConfiguration().isRoundRouting() ? GraphConstants.STYLE_BEZIER : GraphConstants.STYLE_ORTHOGONAL);
        GraphConstants.setLineEnd(map, GraphConstants.ARROW_CLASSIC);
        GraphConstants.setLineWidth(map, ConfigurationManager.getConfiguration().getArrowWidth() == 0 ? ConfigurationManager.getStandardConfiguration().getArrowWidth() : ConfigurationManager.getConfiguration().getArrowWidth());
        GraphConstants.setEndSize(map, ConfigurationManager.getConfiguration().getArrowheadSize() == 0 ? ConfigurationManager.getStandardConfiguration().getArrowheadSize() : ConfigurationManager.getConfiguration().getArrowheadSize());
        GraphConstants.setEndFill(map, ConfigurationManager.getConfiguration().isFillArrowHead());
        GraphConstants.setDisconnectable(map, false);

        GraphConstants.setFont(map, DefaultStaticConfiguration.DEFAULT_SMALLLABEL_FONT);

        // Set arc weight label
        Point2D defaultPos = getDefaultLabelPosition();
        Point2D.Double weightLabelPosition = new Point2D.Double(defaultPos.getX(), defaultPos.getY() - 12);
        GraphConstants.setLabelPosition(map, weightLabelPosition);

        // Set probability label
        GraphConstants.setExtraLabels(map, new String[]{getProbabilityLabelText()});
        Point2D.Double probabilityLabelPosition = new Point2D.Double(defaultPos.getX(), defaultPos.getY() + 12);
        GraphConstants.setExtraLabelPositions(map, new Point2D[]{probabilityLabelPosition});

        List<Point2D> points = new ArrayList<>(2);
        points.add(getAttributes().createPoint(10, 10));
        points.add(getAttributes().createPoint(20, 20));
        GraphConstants.setPoints(map, points);
    }

    public Point2D getProbabilityLabelPosition() {
        Point2D result = null;
        Point2D positions[] = GraphConstants.getExtraLabelPositions(getAttributes());
        if (positions.length > 0) result = positions[0];

        Point2D offset = GraphConstants.getOffset(getAttributes());
        if (offset != null && result != null) {
            result = new Point2D.Double(result.getX(), result.getY() + offset.getY());
        }

        if (result == null) result = getDefaultLabelPosition();
        return result;
    }

    public void setProbabilityLabelPosition(Point2D newLabelPos) {
        AttributeMap map = getAttributes();
        Point2D[] positions = GraphConstants.getExtraLabelPositions(map);

        positions[0] = newLabelPos;
        GraphConstants.setExtraLabelPositions(map, positions);
        getAttributes().applyMap(map);
    }

    public boolean isXORsplit(PetriNetModelProcessor mp) {
        Object cell = ((DefaultPort) getSource()).getParent();

        if (cell instanceof GroupModel) {
            cell = ((GroupModel) cell).getMainElement();
        }

        if (cell instanceof TransitionModel) {
            TransitionModel trans = (TransitionModel) cell;
            int opType = trans.getToolSpecific().getOperatorType();
            if ((opType == OperatorTransitionModel.XOR_SPLIT_TYPE) || (opType == OperatorTransitionModel.XORJOIN_XORSPLIT_TYPE) || (opType == OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE)) {
                return true;
            }
        }

        if (cell instanceof PlaceModel) {
            PlaceModel place = (PlaceModel) cell;
            int num = mp.getElementContainer().
                    getOutgoingArcs(place.getId()).size();

            return num > 1;
        }

        return false;
    }

    /**
     * Returns the inscriptionValue of the arc.
     * <p>
     * The inscription value represents the amount of tokens that are transferred  from the source to the target
     * of the arc on a single execution. It is also known as the weight of the arc.
     *
     * @return the inscription value of the arc
     */
    public int getInscriptionValue() {
        int i;
        try {
            i = (int) getUserObject();
        } catch (Exception e) {
            i = 1;

        }
        return i == -1 ? 1 : i;
    }

    /**
     * Sets the inscriptionValue of the arc.
     * <p>
     * The inscription value represents the amount of tokens that are transferred  from the source to the target
     * of the arc on a single execution. It is also known as the weight of the arc.
     * <p>
     * The valid range is form {@code 1} to {@link Integer#MAX_VALUE}.
     *
     * @param inscriptionValue The inscriptionValue to set
     * @throws IllegalArgumentException if the value is less or equal {@code 0}
     */
    public void setInscriptionValue(int inscriptionValue) {

        if (inscriptionValue <= 0) throw new IllegalArgumentException("The inscription value has to be positive");

        setUserObject(inscriptionValue);
    }

    /**
     * Returns the id.
     *
     * @return Object
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id The id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the id of the arcs source
     *
     * @return the id of the source
     */
    public String getSourceId() {

        DefaultPort port = ((DefaultPort) getSource());
        AbstractPetriNetElementModel pnme = ((AbstractPetriNetElementModel) port.getParent());
        return pnme.getId();
    }

    /**
     * Gets the id of the arcs target
     *
     * @return the id of the target
     */
    public String getTargetId() {
        return ((AbstractPetriNetElementModel) ((DefaultPort) getTarget()).getParent()).getId();
    }

    public boolean isRoute() {
        return (!GraphConstants.ROUTING_DEFAULT.equals(GraphConstants.getRouting(getAttributes())));

    }

    /**
     * TODO: Documentation
     *
     * @param route
     */
    public void setRoute(boolean route) {

        if (route) {
            getAttributes().applyValue(GraphConstants.ROUTING, GraphConstants.ROUTING_SIMPLE);
            getAttributes().remove(GraphConstants.POINTS);
        } else {
            getAttributes().remove(GraphConstants.ROUTING);
            getAttributes().remove(GraphConstants.POINTS);
        }
    }

    /**
     * Adds point c to the arc at the position <code>index</code>.
     *
     * @param c
     * @param index
     */
    public void addPoint(Point2D c, int index) {
        List<Object> points = GraphConstants.getPoints(getAttributes());
        if (points == null) {
            points = new Vector<Object>();
        }
        points.add(index, c);
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        GraphConstants.setPoints(map, points);
        getAttributes().applyMap(map);
        LoggerManager.debug(Constants.CORE_LOGGER, "Point added " + c.toString());
    }

    /**
     * Adds point c to the arc. Calculates the right position
     *
     * @param c
     */
    public void addPoint(Point2D c) {
        AttributeMap map = getAttributes();
        List<Object> points = GraphConstants.getPoints(map);
        if (points == null) {
            points = new Vector<Object>();
            Point2D[] currentPoints = getPoints();
            for (int i = 0; i < currentPoints.length; i++) {
                points.add(currentPoints[i]);
            }
        }

        int index = 0;
        double min = Double.MAX_VALUE, dist = 0;
        for (int i = 0; i < points.size() - 1; i++) {
            Point2D p = null;
            Point2D p1 = null;
            if (points.get(i) instanceof Point2D) {
                p = (Point2D) points.get(i);
            } else if (points.get(i) instanceof PortView) {
                p = ((PortView) points.get(i)).getLocation();
            }
            if (points.get(i + 1) instanceof Point2D) {
                p1 = (Point2D) points.get(i + 1);
            } else if (points.get(i + 1) instanceof PortView) {
                p1 = ((PortView) points.get(i + 1)).getLocation();
            }
            dist = new Line2D.Double(p, p1).ptLineDistSq(c);
            if (dist < min) {
                min = dist;
                index = i + 1;
            }
        }
        addPoint(c, index);
    }

    public Point2D[] getPoints() {
        AttributeMap map = getAttributes();
        List<Object> points = GraphConstants.getPoints(map);
        Point2D[] result = new Point2D[]{};
        if (points != null) {
            result = new Point2D[points.size()];
            for (int i = 0; i < points.size(); i++) {
                if (points.get(i) instanceof PortView) {
                    result[i] = ((PortView) points.get(i)).getLocation();
                } else {
                    result[i] = (Point2D) points.get(i);
                }
            }
        }
        return result;
    }

    public void setPoints(Point2D[] points) {
        AttributeMap map = getAttributes();
        List<Object> pointList = GraphConstants.getPoints(map);
        if (pointList != null) {
            while (pointList.size() > 2) pointList.remove(1);
            for (int i = points.length - 1; i >= 0; i--) {
                pointList.add(1, points[i]);
            }
            GraphConstants.setPoints(map, pointList);
        }
    }

    /**
     * @param l
     */
    public void removePoint(Point2D l) {
        int pos = getPointPosition(l, 10);
        AttributeMap map = getAttributes();
        List<Object> points = GraphConstants.getPoints(map);
        points.remove(pos);
        GraphConstants.setPoints(map, points);
        getAttributes().applyMap(map);
        LoggerManager.debug(Constants.CORE_LOGGER, "Point removed");

    }

    public boolean hasPoint(Point2D p, int tolerance) {
        return (getPointPosition(p, tolerance)) != -1;
    }

    /**
     * Checks if the arc is activated
     *
     * @return Returns true if the arc is activated, otherwise false
     */
    public boolean isActivated() {
        return activated;
    }

    /**
     * Sets the arc activation state to the provide value
     *
     * @param newState the new activation state
     */
    public void setActivated(boolean newState) {
        this.activated = newState;
    }

    public Vector<Object> getUnknownToolSpecs() {
        return unknownToolSpecs;
    }

    public void setUnknownToolSpecs(Vector<Object> unknownToolSpecs) {
        this.unknownToolSpecs = unknownToolSpecs;
    }

    public void addUnknownToolSpecs(Object unknownToolSpecs) {
        getUnknownToolSpecs().add(unknownToolSpecs);
    }

    public CreationMap getCreationMap() {
        CreationMap map = CreationMap.createMap();
        map.setArcId(getId());
        map.setArcRoute(isRoute());
        map.setArcSourceId(getSourceId());
        map.setArcTargetId(getTargetId());
        List<Object> points = GraphConstants.getPoints(getAttributes());
        points = points != null ? points : new Vector<>(); // ensure points not null

        Vector<Object> newPoints = new Vector<Object>();
        for (int i = 1; i < points.size() - 1; i++) {
            newPoints.add(new IntPair((int) ((Point2D) points.get(i)).getX(), (int) ((Point2D) points.get(i)).getY()));
        }
        map.setArcPoints(newPoints);
        map.setArcProbability(getProbability());
        map.setArcDisplayProbability(displayProbability());
        map.setArcLabelPosition((int) this.getProbabilityLabelPosition().getX(), (int) this.getProbabilityLabelPosition().getY());

        map.setArcWeight(getInscriptionValue());
        map.setArcWeightLabelPosition(calculateWeightLabelPosition());

        return map;
    }


    private Point2D calculateWeightLabelPosition() {

        AttributeMap attributes = this.getAttributes();
        Point2D weightPosition = GraphConstants.getLabelPosition(attributes);
        Point2D offset = GraphConstants.getOffset(attributes);

        if (offset != null) {
            weightPosition = new Point2D.Double(weightPosition.getX(), offset.getY());
        }

        return weightPosition;
    }

    public Point2D getWeightLabelPosition() {
        return calculateWeightLabelPosition();
    }

    public void setWeightLablePosition(Point2D newPosition) {
        if (newPosition == null) return;

        GraphConstants.setLabelPosition(this.getAttributes(), newPosition);
    }

    public ElementContext getElementContext() {
        return elementContext;
    }

    public void setElementContext(ElementContext elementContext) {
        this.elementContext = elementContext;
    }

    public double getProbability() {
        Object probability = getAttributes().get("Probability");
        if (probability instanceof Double) {
            return (Double) probability;
        } else {
            return 1.0;
        }
    }

    public void setProbability(double probability) {
        getAttributes().put("Probability", new Double(probability));
        updateLabel();
    }

    /**
     * Determines, if the arc probability should be displayed, or not.
     *
     * @return true if the probability should be displayed, otherwise false
     */
    public boolean displayProbability() {
        Object probability = getAttributes().get("DisplayProbability");
        if (probability instanceof Boolean) {
            return ((Boolean) probability).booleanValue();
        } else {
            return false;
        }
    }

    /**
     * Sets the new state of the visibility of the arc probability label.
     *
     * @param display true if the label should be displayed, otherwise false.
     */
    public void displayProbability(boolean display) {
        getAttributes().put("DisplayProbability", new Boolean(display));
        updateLabel();
    }

    /**
     * Determine if the arc weight should be displayed or not.
     *
     * @return true if the arc weight should be displayed, otherwise false
     */
    public boolean displayWeight() {
        return this.getInscriptionValue() > 1 || DEFAULT_WEIGHT_VISIBLE;
    }

    private int getPointPosition(Point2D p, int tolerance) {
        List<Object> points = GraphConstants.getPoints(getAttributes());
        int pos = -1;
        double dist = Double.MAX_VALUE;
        for (int i = 1; i < points.size() - 1; i++) {
            Point2D a = (Point2D) points.get(i);

            double tp = Point2D.distance(a.getX(), a.getY(), p.getX(), p.getY());
            if (tp < dist) {
                dist = tp;
                pos = i;
            }
        }
        if (dist < tolerance) {
            return pos;
        }
        return -1;
    }

    private void updateLabel() {
        AttributeMap attributes = getAttributes();
        Object[] extraLabels = GraphConstants.getExtraLabels(attributes);
        extraLabels[0] = getProbabilityLabelText();
        GraphConstants.setExtraLabels(attributes, extraLabels);
        attributes.applyMap(attributes);
    }

    private String getProbabilityLabelText() {
        return Integer.toString(Double.valueOf(getProbability() * 100).intValue()) + "%";
    }

    private Point2D getDefaultLabelPosition() {
        return new Point2D.Double(GraphConstants.PERMILLE / 2, 0);
    }
}