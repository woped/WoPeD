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
 * For contact information please visit http://woped.ba-karlsruhe.de
 *
 */
package org.woped.core.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphCellEditor;
import org.jgraph.graph.VertexRenderer;
import org.jgraph.graph.VertexView;
import org.woped.core.model.AbstractElementModel;

/**
 * @author Simon Landes
 * 
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of type
 * comments go to Window>Preferences>Java>Code Generation.
 */
public abstract class AbstractElementView extends VertexView {

    /**
     * Constructor for PetriNetElementView.
     * 
     * @param cell
     * @param graph
     * @param mapper
     */
    public AbstractElementView(Object cell) {
	super(cell);
	if (cell instanceof DefaultGraphCell) {
	    DefaultGraphCell cellEl = (DefaultGraphCell) cell;
	    setAttributes(cellEl.getAttributes());

	}
    }

    /**
     * @see com.jgraph.graph.CellView#getEditor()
     */
    public GraphCellEditor getEditor() {
	return cellEditor;
    }

    public boolean isActivated() {
	return ((AbstractElementModel) getCell()).isActivated();
    }

    public boolean isFireing() {
	return ((AbstractElementModel) getCell()).isFireing();
    }

    protected abstract class AbstractElementRenderer extends VertexRenderer {
	private boolean readOnly;

	private Color readOnlyColor = new Color(225, 225, 225);

	private Color highLightedColor = new Color(255, 0, 0, 128);

	public AbstractElementRenderer(Object cell) {
	    AbstractElementModel model = (AbstractElementModel) cell;
	    readOnly = model.isReadOnly();
	}

	protected Color getFillColor() {
	    Color result = null;
	    if (((AbstractElementModel) (AbstractElementView.this.getCell()))
		    .isHighlighted())
		result = highLightedColor;
	    else if (readOnly) {
		result = readOnlyColor;
	    } else if (isActive() || isFireing()) {
		return (new Color(50, 200, 100, 25));
	    } else {
		result = super.getBackground();
	    }
	    return result;
	}

	//! Get the color to be used for inner drawings
	//! Note that this color is context-sensitive
	//! and will change if an element is active
	//! @return Color to be used for inner drawings
	protected Color getInnerDrawingsColor() {
	    // When active during the token game,
	    // draw the inner symbols in gray
	    if (isActive() || isFireing())
		return (new Color(50, 200, 100));
	    else {
		if (bordercolor == null)
		    return Color.BLACK;
		else
		    return bordercolor;
	    }
	}

	/**
	 * @return
	 */
	public boolean isActive() {
	    return AbstractElementView.this.isActivated();
	}

	/**
	 * @return
	 */
	public boolean isFireing() {
	    return AbstractElementView.this.isFireing();
	}

	//! Draw one of the famous operator arrows
	//! @param g specifies the graphical context to be used for drawing
	//! @param positionRightSide if true, draw the arrow on the right third of the operator element
	//! @param directionRightSide if true, draw the arrow such that it points to the right side of the element
	protected void drawOperatorArrow(Graphics g, boolean positionRightSide,
		boolean directionRightSide) {
	    Dimension d = getSize();
	    int b = borderWidth;

	    int tipy = 0;
	    int tipx = 0;
	    int basex = 0;
	    int linex = 0;

	    // To be positioned on the right side ?
	    if (positionRightSide) {
		// Determine direction of the arrow
		tipy = d.height / 2;
		tipx = directionRightSide ? (d.width - b) : (2 * d.width / 3);
		basex = directionRightSide ? (2 * d.width / 3) : (d.width - b);
		linex = 2 * d.width / 3;
	    } else {
		// Determine direction of the arrow
		tipy = d.height / 2;
		tipx = directionRightSide ? (d.width / 3) : b;
		basex = directionRightSide ? b : (d.width / 3);
		linex = d.width / 3;
	    }

	    // draw light gray polygons
	    Color drawingColor = getInnerDrawingsColor();
	    g.setColor(new Color(drawingColor.getRed(),
		    drawingColor.getGreen(), drawingColor.getBlue(), 25));

	    Polygon p = new Polygon();
	    p.addPoint(tipx, tipy);
	    p.addPoint(tipx, b);
	    p.addPoint(basex, b);
	    g.fillPolygon(p);

	    p = new Polygon();
	    p.addPoint(tipx, tipy);
	    p.addPoint(tipx, d.height - b);
	    p.addPoint(basex, d.height - b);
	    g.fillPolygon(p);

	    // draw arrow
	    g.setColor(getInnerDrawingsColor());
	    g.drawLine(linex, b, linex, d.height - b);
	    g.drawLine(basex, b, tipx, tipy);
	    g.drawLine(basex, d.height - b, tipx, tipy);

	}

    }
}