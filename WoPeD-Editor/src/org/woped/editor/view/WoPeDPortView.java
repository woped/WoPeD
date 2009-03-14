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
package org.woped.editor.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.PortRenderer;
import org.jgraph.graph.PortView;
import org.woped.core.config.ConfigurationManager;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * 
 * 01.05.2003
 */

@SuppressWarnings("serial")
public class WoPeDPortView extends PortView
{

    protected boolean shouldInvokePortMagic(EdgeView arg0)
    {
        return false;
    }

    /** Renderer for the class. */
    public PetriPortRenderer renderer = new PetriPortRenderer();

    /**
     * Constructor for PWTPortView.
     * 
     * @param cell
     * @param graph
     * @param mapper
     */
    public WoPeDPortView(Object cell)
    {
        super(cell);

    }

    private class PetriPortRenderer extends PortRenderer
    {

        /**
         * Paint the renderer. Overrides superclass paint to add specific
         * painting. Note: The preview flag is interpreted as "highlight" in
         * this context. (This is used to highlight the port if the mouse is
         * over it.)
         */
        public void paint(Graphics g)
        {
            Dimension d = getSize();
            g.setColor(getBackground());
            g.setXORMode(getBackground());

            if (hasFocus)
            {
                g.setColor(ConfigurationManager.getConfiguration().getSelectionColor());
                g.drawRect(2, 2, d.width - 3, d.height - 3);
                g.drawRect(3, 3, d.width - 5, d.height - 5);
                g.drawRect(d.width / 2, d.height / 2, 4, 4);
            }
            boolean offset = (GraphConstants.getOffset(view.getAllAttributes()) != null);
            g.setColor(getForeground());

            if (preview)
            {
                g.setColor(ConfigurationManager.getConfiguration().getSelectionColor());
                g.drawRect(2, 2, d.width - 4, d.height - 4);
                g.drawRect(3, 3, d.width - 6, d.height - 6);
                g.fillOval((d.width - 1) / 2, (d.height - 1) / 2, 6, 6);
            }

            if (offset)
            {
                g.setColor(Color.GREEN);
                g.drawLine(2, 2, d.width - 2, d.height - 2);
                g.drawLine(2, d.width - 2, d.height - 2, 2);
            }

            else if (!preview)
            {
                g.setColor(Color.WHITE);
                g.drawRect(3, 3, d.width - 4, d.height - 4);
            }

        }
    }

    /**
     * @see com.jgraph.graph.AbstractCellView#getRenderer()
     */
    public CellViewRenderer getRenderer()
    {
        return renderer;
    }

    public Rectangle2D getBounds()
    {
		Rectangle2D bounds = getAttributes().createRect(getLocation(null));
		if (bounds != null)
			bounds.setFrame(bounds.getX() - SIZE / 2, bounds.getY() - SIZE / 2,
					bounds.getWidth() + SIZE, bounds.getHeight() + SIZE);
		return bounds;
//        Rectangle2D rect = super.getBounds();
//        if (rect == null)
//        {
//            rect = getAttributes().createRect();
//        }
//        return rect;
    }
}