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

import javax.swing.ImageIcon;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.VertexRenderer;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.config.DefaultStaticConfiguration;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.view.AbstractElementView;

/**
 * @author rey
 * 
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of type
 * comments go to Window>Preferences>Java>Code Generation.
 */
public class SubProcessView extends AbstractElementView
{

    private SubProcessRenderer renderer = new SubProcessRenderer();

    /**
     * Constructor for TransitionView.
     * 
     * @param cell
     * @param graph
     * @param mapper
     */
    public SubProcessView(Object cell)
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

    public boolean isActivated()
    {
        return ((TransitionModel) getCell()).isActivated();
    }

    public boolean isFireing()
    {
        return ((TransitionModel) getCell()).isFireing();
    }

    /**
     * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
     * 
     * this inner class contains the Renderer information of an transition
     * 
     * 28.03.2003
     */
    private class SubProcessRenderer extends VertexRenderer
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
                // g.setColor(super.getBackground());
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
                g.drawRect(b - 1, b - 1, d.width - b, d.height - b);
            }
            if (selected)
            {
                g2.setStroke(GraphConstants.SELECTION_STROKE);
                g.setColor(ConfigurationManager.getConfiguration().getSelectionColor());
                g.drawRect(b - 1, b - 1, d.width - b, d.height - b);
            }
            // SubProcess Lines
            g.drawLine(5, 5, 35, 5);
            g.drawLine(5, 5, 5, 34);
            g.drawLine(5, 34, 34, 34);
            g.drawLine(34, 34, 34, 5);
            if (isFireing())
            {
                // g.setColor(Color.BLACK);
                g2.setFont(DefaultStaticConfiguration.DEFAULT_TOKENGAME_FONT);
                ImageIcon img = new ImageIcon(getClass().getResource("/org/woped/gui/images/tokenGame_fire.gif"));
                g2.drawImage(img.getImage(), 5, 22, 18, 11, img.getImageObserver());
                g.drawRect(b, b, d.width - b - 1, d.height - b - 1);
                g2.drawString("fire", 5, 18);
            }

        }

        /**
         * @return
         */
        public boolean isActive()
        {
            return SubProcessView.this.isActivated();
        }

        /**
         * @return
         */
        public boolean isFireing()
        {
            return SubProcessView.this.isFireing();
        }
    }

    /**
     * @see org.woped.editor.core.view.AbstractElementView#paint()
     */
    public void paint()
    {}

    /**
     * @see org.woped.editor.core.view.AbstractElementView#refresh()
     */
    public void refresh()
    {}

}