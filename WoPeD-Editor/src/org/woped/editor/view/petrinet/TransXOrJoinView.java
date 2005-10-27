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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import javax.swing.ImageIcon;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.VertexRenderer;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * 
 * 29.04.2003
 */
public class TransXOrJoinView extends TransSimpleView
{

    private TransXOrJoinRenderer renderer = new TransXOrJoinRenderer();

    /**
     * Constructor for TransAndJoinView.
     * 
     * @param cell
     * @param graph
     * @param mapper
     */
    public TransXOrJoinView(Object cell)
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
    private class TransXOrJoinRenderer extends VertexRenderer
    {

        public void paint(Graphics g)
        {

            /* Trigger hinzufügen */
            int b = borderWidth;
            Graphics2D g2 = (Graphics2D) g;
            Dimension d = getSize();
            boolean tmp = selected;
            if (super.isOpaque())
            {
                //g.setColor(super.getBackground());
                g.fillRect(b - 1, b - 1, d.width - b, d.height - b);
            }
            try
            {
                setBorder(null);
                setOpaque(false);
                selected = false;
                super.paint(g);
            } finally
            {
                selected = tmp;
            }
            if (bordercolor != null)
            {
                g.setColor(bordercolor);
                g2.setStroke(new BasicStroke(b));
                g.drawRect(b, b, d.width - b - 1, d.height - b - 1);
            }
            if (selected)
            {
                //				g2.setStroke(GraphConstants.SELECTION_STROKE);
                g.setColor(graph.getHighlightColor());
                g.drawRect(b, b, d.width - b - 1, d.height - b - 1);
            }
            // XOR JOIN Lines
            g.drawLine(d.width / 3, b, d.width / 3, d.height - b);
            g.drawLine(d.width / 3, b, b, d.height / 2);
            g.drawLine(b, d.height / 2, d.width / 3, d.height - b);
            if (isActive() || isFireing())
            {
                g.setColor(Color.LIGHT_GRAY);
                g.drawLine(d.width / 3, b, d.width / 3, d.height - b);
                g.drawLine(d.width / 3, b, b, d.height / 2);
                g.drawLine(b, d.height / 2, d.width / 3, d.height - b);
                g2.setColor(Color.RED);
                g2.setFont(new Font("Verdana", Font.ITALIC, 10));
            }
            if (isActive() && !isFireing())
            {
                g2.drawString("enabled", 3, 18);
                ImageIcon img = new ImageIcon(getClass().getResource("/org/woped/gui/images/tokenGame_active.gif"));
                g2.drawImage(img.getImage(), 5, 22, 6, 11, img.getImageObserver());
            }
            if (isFireing())
            {
                // g.setColor(Color.BLACK);
                g2.drawRect(b, b, d.width - b - 1, d.height - b - 1);
                g2.drawString("choose", 3, 20);
                g2.drawString("arc", 3, 27);
                ;
            }

        }

        /**
         * @return
         */
        public boolean isActive()
        {
            return TransXOrJoinView.this.isActivated();
        }

        /**
         * @return
         */
        public boolean isFireing()
        {
            return TransXOrJoinView.this.isFireing();
        }
    }

}