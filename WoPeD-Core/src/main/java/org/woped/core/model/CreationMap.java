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

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;

import org.woped.core.Constants;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.utilities.LoggerManager;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 *         <p>
 *         Created on: 20.01.2005 Last Change on: 20.01.2005
 */

@SuppressWarnings("serial")
public class CreationMap extends HashMap<String, Object> {
    private static final String ELEMENT_ID = "ELEMENT_ID";
    private static final String ELEMENT_TYPE = "ELEMENT_TYPE";
    private static final String ELEMENT_SIZE = "ELEMENT_SIZE";
    private static final String ELEMENT_POSITION = "ELEMENT_POSITION";
    private static final String ELEMENT_NAME = "ELEMENT_NAME";
    private static final String ELEMENT_NAME_SIZE = "ELEMENT_NAME_SIZE";
    private static final String ELEMENT_NAME_POSITION = "ELEMENT_NAME_POSITION";
    private static final String VERTICAL_VIEW = "VERTICAL_VIEW";
    private static final String EDIT_ON_CREATION = "EDIT_ON_CREATION";
    private static final String OPERATOR_TYPE = "OPERATOR_TYPE";
    private static final String OPERATOR_POSITION = "OPERATOR_POSITION";
    private static final String TRIGGER_TYPE = "TRIGGER_TYPE";
    private static final String TRIGGER_POSITION = "TRIGGER_POSITION";
    private static final String TOKENS = "TOKENS";
    private static final String UNKNOWN_TOOLSPEC = "UNKNOWN_TOOLSPEC";
    private static final String ARC_ID = "ARC_ID";
    private static final String ARC_TARGET_ID = "ARC_TARGET_ID";
    private static final String ARC_SOURCE_ID = "ARC_SOURCE_ID";
    private static final String ARC_POINTS = "ARC_POINTS";
    private static final String ARC_ROUTE = "ARC_ROUTE";
    private static final String ARC_PROBABILITY = "ARC_PROBABILITY";
    private static final String ARC_DISPLAYPROB = "ARC_DISPLAYPROB";
    private static final String ARC_LABELPOSITION = "ARC_LABELPOSITION";
    private static final String ARC_WEIGHT = "ARC_WEIGHT";
    private static final String ARC_WEIGHT_LABEL_POSITION = "ARC_WEIGHT_LABEL_POSITION";
    private static final String RESOURCE_POSITION = "RESOURCE_POSITION";
    private static final String RESOURCE_ROLE = "RESOURCE_ROLE";
    private static final String RESOURCE_ORGUNIT = "RESOURCE_ORGUNIT";
    private static final String IMAGEICON = "IMAGEICON";
    private static final String STATE_TYPE = "STATETYPE";
    private static final String READ_ONLY = "READ_ONLY";
    private static final String TRANSITION_TIME = "TRANSITION_TIME";
    private static final String TRANSITION_TIMEUNIT = "TRANSITION_TIMEUNIT";
    private static final String UPPER_ELEMENT = "UPPER_ELEMENT";
    private static final String SUBELEMENT_CONTAINER = "SUBELEMENT_CONTAINER";
    private static final String BPELDATA = "BPELDATA";
    private static final String HIGHLIGHT = "HIGHLIGHT";


    public static CreationMap createMap() {
        return new CreationMap();
    }

    public boolean isValid() {
        return getType() != -1;
    }

    public boolean isHighlight() {
        if (containsKey(HIGHLIGHT)) {
            return ((Boolean) get(HIGHLIGHT)).booleanValue();
        } else
            return false;
    }

    public void setHighlight(boolean highlight) {
        put(HIGHLIGHT, new Boolean(highlight));
    }

    public String getId() {
        if (containsKey(ELEMENT_ID)) {
            return (String) get(ELEMENT_ID);
        } else {
            return null;
        }
    }

    public void setId(String id) {
        put(ELEMENT_ID, id);
    }

    public int getType() {
        if (containsKey(ELEMENT_TYPE)) {
            return ((Integer) get(ELEMENT_TYPE)).intValue();
        } else {
            return -1;
        }
    }

    public void setType(int type) {
        put(ELEMENT_TYPE, new Integer(type));
    }

    public IntPair getSize() {
        if (containsKey(ELEMENT_SIZE)) {
            return (IntPair) get(ELEMENT_SIZE);
        } else {
            return null;
        }
    }

    public void setSize(IntPair intPair) {
        put(ELEMENT_SIZE, intPair);
    }

    public void setPosition(int x, int y) {
        put(ELEMENT_POSITION, new Point(x, y));
    }

    public Point getPosition() {
        if (containsKey(ELEMENT_POSITION)) {
            return (Point) get(ELEMENT_POSITION);
        } else {
            return null;
        }
    }

    public void setPosition(Point point) {
        put(ELEMENT_POSITION, point);
    }

    public String getName() {
        if (containsKey(ELEMENT_NAME)) {
            return (String) get(ELEMENT_NAME);
        } else {
            return null;
        }
    }

    public void setName(String name) {
        put(ELEMENT_NAME, name);
    }

    public IntPair getNameSize() {
        if (containsKey(ELEMENT_NAME_SIZE)) {
            return (IntPair) get(ELEMENT_NAME_SIZE);
        } else {
            return null;
        }
    }

    public void setNameSize(IntPair intPair) {
        put(ELEMENT_NAME_SIZE, intPair);
    }

    public void setNamePosition(int x, int y) {
        put(ELEMENT_NAME_POSITION, new Point(x, y));
    }

    public Point getNamePosition() {
        if (containsKey(ELEMENT_NAME_POSITION)) {
            return (Point) get(ELEMENT_NAME_POSITION);
        } else {
            return null;
        }
    }

    public void setNamePosition(Point point) {
        put(ELEMENT_NAME_POSITION, point);
    }

    public int getOperatorType() {
        if (containsKey(OPERATOR_TYPE)) {
            return ((Integer) get(OPERATOR_TYPE)).intValue();
        } else {
            return -1;
        }
    }

    public void setOperatorType(int operatorType) {
        put(OPERATOR_TYPE, new Integer(operatorType));
    }

    public boolean getVerticalView() {
        if (containsKey(VERTICAL_VIEW)) {
            return ((Boolean) get(VERTICAL_VIEW)).booleanValue();
        } else {
            return false;
        }
    }

    public void setVerticalView(boolean verticalView) {
        put(VERTICAL_VIEW, new Boolean(verticalView));
    }

    public int getOperatorPosition() {
        if (containsKey(OPERATOR_POSITION)) {
            return ((Integer) get(OPERATOR_POSITION)).intValue();
        } else {
            return -1;
        }
    }

    public void setOperatorPosition(int operatorPosition) {
        put(OPERATOR_POSITION, new Integer(operatorPosition));
    }

    public boolean isEditOnCreation() {
        if (containsKey(EDIT_ON_CREATION)) {
            return ((Boolean) get(EDIT_ON_CREATION)).booleanValue();
        } else {
            return ConfigurationManager.getConfiguration().isEditingOnCreation();
        }
    }

    public void setEditOnCreation(boolean edit) {
        put(EDIT_ON_CREATION, new Boolean(edit));
    }

    public int getTriggerType() {
        if (containsKey(TRIGGER_TYPE)) {
            return ((Integer) get(TRIGGER_TYPE)).intValue();
        } else {
            return -1;
        }
    }

    public void setTriggerType(int type) {
        put(TRIGGER_TYPE, new Integer(type));
    }

    public void setTriggerPosition(int x, int y) {
        put(TRIGGER_POSITION, new Point(x, y));
    }

    public Point getTriggerPosition() {
        if (containsKey(TRIGGER_POSITION)) {
            return (Point) get(TRIGGER_POSITION);
        } else {
            return null;
        }
    }

    public void setTriggerPosition(Point point) {
        put(TRIGGER_POSITION, point);
    }

    public int getTokens() {
        if (containsKey(TOKENS)) {
            return ((Integer) get(TOKENS)).intValue();
        } else {
            return -1;
        }
    }

    public void setTokens(int tokens) {
        put(TOKENS, new Integer(tokens));
    }

    public Vector<?> getUnknownToolSpec() {
        if (get(UNKNOWN_TOOLSPEC) == null) {
            setUnknownToolSpec(new Vector<Object>());
        }
        return (Vector<?>) get(UNKNOWN_TOOLSPEC);
    }

    public void setUnknownToolSpec(Vector<Object> toolSpec) {
        put(UNKNOWN_TOOLSPEC, toolSpec);
    }

    public void addUnknownToolSpec(Object toolSpec) {
        if (get(UNKNOWN_TOOLSPEC) == null) setUnknownToolSpec(new Vector<Object>());

        ((Vector<Object>) get(UNKNOWN_TOOLSPEC)).add(toolSpec);
    }

    public String getArcId() {
        if (containsKey(ARC_ID)) {
            return (String) get(ARC_ID);
        } else {
            return null;
        }
    }

    public void setArcId(String id) {
        put(ARC_ID, id);
    }

    public String getArcTargetId() {
        if (containsKey(ARC_TARGET_ID)) {
            return (String) get(ARC_TARGET_ID);
        } else {
            return null;
        }
    }

    public void setArcTargetId(String id) {
        put(ARC_TARGET_ID, id);
    }

    public String getArcSourceId() {
        if (containsKey(ARC_SOURCE_ID)) {
            return (String) get(ARC_SOURCE_ID);
        } else {
            return null;
        }
    }

    public void setArcSourceId(String id) {
        put(ARC_SOURCE_ID, id);
    }

    public boolean isArcRoute() {
        if (containsKey(ARC_ROUTE)) {
            return ((Boolean) get(ARC_ROUTE)).booleanValue();
        } else {
            return true;
        }
    }

    public void setArcRoute(boolean route) {
        put(ARC_ROUTE, new Boolean(route));
    }

    /**
     * Gets the weight of the arc.
     * <p>
     * If no weight was set, it returns the default weight of the arc.
     *
     * @return the weight of the arc, or 1 if not weight was set.
     */
    public int getArcWeight() {

        int defaultWeight = 1;
        if (containsKey(ARC_WEIGHT)) {
            return (int) get(ARC_WEIGHT);
        }

        return defaultWeight;
    }

    /**
     * Sets the weight of the arc.
     *
     * @param weight the weight to set
     */
    public void setArcWeight(int weight) {
        put(ARC_WEIGHT, weight);
    }

    public List<Point2D> getArcPoints() {
        if (containsKey(ARC_POINTS)) {
            return ((List<Point2D>) get(ARC_POINTS));
        } else {
            return new Vector<>();
        }
    }

    public void setArcPoints(List<?> points) {

        // First convert all points to Point2D
        Vector<Point2D> newPoints = new Vector<>();
        for (Object o : points) {
            if (o instanceof Point2D) {
                newPoints.add((Point2D) o);
            } else if (o instanceof IntPair) {
                IntPair p = (IntPair) o;
                newPoints.add(new Point2D.Double(p.getX1(), p.getX2()));
            } else {
                LoggerManager.error(Constants.CORE_LOGGER, "Could not convert " + o.toString() + "to Point2D. Skipped");
            }
        }

        put(ARC_POINTS, newPoints);
    }

    /**
     * Gets the position of the arc weight label.
     *
     * @return the position of the arc weight label
     */
    public Point2D getArcWeightLabelPosition() {
        if ( containsKey(ARC_WEIGHT_LABEL_POSITION) ) {
            return (Point2D) get(ARC_WEIGHT_LABEL_POSITION);
        }

        return null;
    }

    /**
     * Sets the positoin of the arc weight label.
     *
     * @param newPosition the new position of the label
     */
    public void setArcWeightLabelPosition(Point2D newPosition) {
        put(ARC_WEIGHT_LABEL_POSITION, newPosition);
    }

    public double getArcProbability() {
        if (containsKey(ARC_PROBABILITY)) {
            return ((Double) get(ARC_PROBABILITY)).doubleValue();
        } else {
            return 1;
        }
    }

    public void setArcProbability(double value) {
        put(ARC_PROBABILITY, new Double(value));
    }

    public void setArcDisplayProbability(boolean show) {
        put(ARC_DISPLAYPROB, new Boolean(show));
    }

    public boolean getDisplayArcProbability() {
        if (containsKey(ARC_DISPLAYPROB)) {
            return ((Boolean) get(ARC_DISPLAYPROB)).booleanValue();
        } else {
            return false;
        }
    }


    public void setArcLabelPosition(int x, int y) {
        put(ARC_LABELPOSITION, new Point(x, y));
    }

    public Point getArcLabelPosition() {
        if (containsKey(ARC_LABELPOSITION)) {
            return (Point) get(ARC_LABELPOSITION);
        } else {
            return null;
        }
    }

    public void setArcLabelPosition(Point point) {
        put(ARC_LABELPOSITION, point);
    }

    public void setResourcePosition(int x, int y) {
        put(RESOURCE_POSITION, new Point(x, y));
    }

    public Point getResourcePosition() {
        if (containsKey(RESOURCE_POSITION)) {
            return (Point) get(RESOURCE_POSITION);
        } else {
            return null;
        }
    }

    public void setResourcePosition(Point point) {
        put(RESOURCE_POSITION, point);
    }

    public String getResourceRole() {
        if (containsKey(RESOURCE_ROLE)) {
            return (String) get(RESOURCE_ROLE);
        } else {
            return null;
        }
    }

    public void setResourceRole(String roleName) {
        put(RESOURCE_ROLE, roleName);
    }

    public String getResourceOrgUnit() {
        if (containsKey(RESOURCE_ORGUNIT)) {
            return (String) get(RESOURCE_ORGUNIT);
        } else {
            return null;
        }
    }

    public void setResourceOrgUnit(String orgUnitName) {
        put(RESOURCE_ORGUNIT, orgUnitName);
    }

    public ImageIcon getImageIcon() {
        if (containsKey(IMAGEICON)) {
            return (ImageIcon) get(IMAGEICON);
        } else {
            return null;
        }
    }

    public void setImageIcon(ImageIcon img) {
        put(IMAGEICON, img);
    }

    public int getStateType() {
        if (containsKey(STATE_TYPE)) {
            return ((Integer) get(STATE_TYPE)).intValue();
        } else {
            return -1;
        }
    }

    public void setStateType(int type) {
        put(STATE_TYPE, new Integer(type));
    }

    public Boolean getReadOnly() {
        if (containsKey(READ_ONLY)) {
            return (Boolean) get(READ_ONLY);
        } else {
            return false;
        }
    }

    public void setReadOnly(boolean readOnly) {
        put(READ_ONLY, readOnly);
    }

    public int getTransitionTime() {
        if (containsKey(TRANSITION_TIME)) {
            return ((Integer) get(TRANSITION_TIME)).intValue();
        } else {
            return -1;
        }
    }

    public void setTransitionTime(int time) {
        put(TRANSITION_TIME, time);
    }

    public int getTransitionTimeUnit() {
        if (containsKey(TRANSITION_TIMEUNIT)) {
            return ((Integer) get(TRANSITION_TIMEUNIT)).intValue();
        } else {
            return -1;
        }
    }

    public void setTransitionTimeUnit(int time) {
        put(TRANSITION_TIMEUNIT, time);
    }

    public void setUpperElement(AbstractPetriNetElementModel upperElement) {
        put(UPPER_ELEMENT, upperElement);

    }

    public AbstractPetriNetElementModel getRealElement() {
        if (containsKey(UPPER_ELEMENT)) {
            return (AbstractPetriNetElementModel) get(UPPER_ELEMENT);
        } else {
            return null;
        }
    }

    public ModelElementContainer getSubElementContainer() {
        if (containsKey(SUBELEMENT_CONTAINER)) {
            return (ModelElementContainer) get(SUBELEMENT_CONTAINER);
        }
        return null;
    }

    public void setSubElementContainer(ModelElementContainer container) {
        put(SUBELEMENT_CONTAINER, container);
    }

    public Object getBpeldata() {
        return this.get(CreationMap.BPELDATA);
    }

    public void setBpeldata(Object Bpeldata) {
        this.put(CreationMap.BPELDATA, Bpeldata);
    }

    public boolean isHighLight() {
        if (containsKey(HIGHLIGHT)) {
            return ((Boolean) get(HIGHLIGHT)).booleanValue();
        } else {
            return false;
        }
    }

}