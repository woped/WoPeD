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
package org.woped.editor.view.petrinet;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.VertexRenderer;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.view.AbstractElementView;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * 
 * 10.05.2003
 */
public class TriggerExtView extends AbstractElementView
{

    static TriggerExternalRenderer renderer = new TriggerExternalRenderer();

    /**
     * Constructor for TriggerResource.
     * 
     * @param cell
     * @param graph
     * @param mapper
     */
    public TriggerExtView(Object cell)
    {
        super(cell);
    }

    public Point2D getPerimeterPoint(Point2D source, Point2D p)
    {

        return super.getPerimeterPoint(source, p);

    }

    public CellViewRenderer getRenderer()
    {

        return renderer;

    }

    /**
     * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
     * 
     * this inner class contains the Renderer information of an transition
     * 
     * 28.03.2003
     */
    static class TriggerExternalRenderer extends VertexRenderer
    {

        public void paint(Graphics g)
        {

            int b = borderWidth;
            Graphics2D g2 = (Graphics2D) g;
            Dimension d = getSize();
            boolean tmp = selected;
            if (super.isOpaque())
            {
                g.setColor(super.getBackground());
            }
            if (bordercolor != null)
            {
                g.setColor(bordercolor);
                g2.setStroke(new BasicStroke(b));
            }
            if (selected)
            {
                g.setColor(ConfigurationManager.getConfiguration().getSelectionColor());
            }
            g.drawRect(0, 0, 20, 15);
            g.drawLine(0, 0, 10, 10);
            g.drawLine(10, 10, 20, 0);
            g.drawLine(0, 15, 8, 7);
            g.drawLine(13, 8, 20, 15);
        }
    }

}