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
package org.woped.core.model.petrinet;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.GraphConstants;
import org.woped.core.model.CreationMap;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <p>
 *         <p>
 *         29.03.2003
 */

@SuppressWarnings("serial")
public class PlaceModel extends AbstractPetriNetElementModel {

    private static final int WIDTH = 40;
    private static final int HEIGHT = 40;
    private int m_token = 0;
    private int m_virtualToken = 0;

    /**
     * @param map the map containing the information to create the place
     */
    public PlaceModel(CreationMap map) {
        super(map);
        AttributeMap attributes = getAttributes();
        GraphConstants.setMoveable(attributes, true);
        GraphConstants.setEditable(attributes, false);
        GraphConstants.setSizeable(attributes, false);
        setAttributes(attributes);
    }

    /**
     * Returns the token.
     *
     * @return boolean
     */
    public boolean hasTokens() {
        return m_token > 0;
    }

    /**
     * Sets the token.
     *
     * @param token The token to set
     */
    public void setTokens(int token) {
        m_token = token;
        m_virtualToken = m_token;
    }

    /**
     * Sets the virtual token.
     *
     * @param token The virtual token to set
     */
    public void setVirtualTokens(int token) {
        m_virtualToken = token;
    }

    public int getTokenCount() {
        return m_token;
    }

    public void addToken() {
        m_token += 1;
        m_virtualToken = m_token;
    }

    public void removeToken() {
        m_token = m_token > 0 ? m_token - 1 : 0;
        m_virtualToken = m_token;
    }

    /**
     * Adds the given amount to the virtual tokens of this place.
     *
     * @param amount the positive amount of tokens to add
     * @throws IllegalArgumentException if amount is negative
     *                                  or the addition would cause an overflow
     */
    public void addVirtualTokens(int amount) throws IllegalArgumentException {

        if ( amount < 0 ) throw new IllegalArgumentException("Amount has to be positive.");

        if ( (Integer.MAX_VALUE - amount) < getVirtualTokenCount() ) throw new IllegalArgumentException("Amount would cause an overflow");

        this.setVirtualTokens(this.getVirtualTokenCount() + amount);
    }

    /**
     * Removes the given amount of virtual tokens from this place.
     *
     * @param amount the positive amount of tokens to remove.
     * @throws IllegalArgumentException if the amount is a negative number
     *                                  or the place does not have enough tokens.
     */
    public void removeVirtualTokens(int amount) throws IllegalArgumentException {

        if ( amount < 0 ) throw new IllegalArgumentException("Negative amount is not allowed.");

        if ( amount > getVirtualTokenCount() ) throw new IllegalArgumentException("Place does not have enough virtual tokens.");

        this.setVirtualTokens(this.getVirtualTokenCount() - amount);
    }

    public int getVirtualTokenCount() {
        return m_virtualToken;
    }

    public void sendToken() {
        m_virtualToken = m_virtualToken > 0 ? m_virtualToken - 1 : 0;
    }

    public void receiveToken() {
        m_virtualToken += 1;
    }

    public void resetVirtualTokens() {
        m_virtualToken = m_token;
    }

    /*
     * @see org.woped.model.AbstractPetriNetModelElement#getDefaultWidth()
     */
    public int getDefaultWidth() {
        return WIDTH;
    }

    /*
     * @see org.woped.model.AbstractPetriNetModelElement#getDefaultHeight()
     */
    public int getDefaultHeight() {
        return HEIGHT;
    }

    public String getToolTipText() {
        return "Place\nID: " + getId() + "\nName: " + getNameValue() + "\nTokens: " + getVirtualTokenCount();
    }

    public CreationMap getCreationMap() {
        CreationMap map = super.getCreationMap();
        // Tokens
        if ( getTokenCount() > 0 ) map.setTokens(getTokenCount());

        return map;
    }

    public int getType() {
        return AbstractPetriNetElementModel.PLACE_TYPE;
    }

    public boolean isActivated() {
        // The model activates a place when it is a sink sub process element and
        // its token count is > 0. The only purpose of this is to be able to return from a sub process
        // by clicking its sink place
        return (isSubprocessElement() && isSink() && (getVirtualTokenCount() > 0));
    }
}