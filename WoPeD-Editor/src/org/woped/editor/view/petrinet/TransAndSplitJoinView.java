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
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.VertexRenderer;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.config.DefaultStaticConfiguration;
import org.woped.editor.utilities.Messages;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * 
 * 29.04.2003
 */

@SuppressWarnings("serial")
public class TransAndSplitJoinView extends TransSimpleView
{
    private TransAndSplitJoinRenderer renderer = new TransAndSplitJoinRenderer();

    /**
     * Constructor for TransAndJoinView.
     * 
     * @param cell
     * @param graph
     * @param mapper
     */
    public TransAndSplitJoinView(Object cell)
    {
        super(cell);
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
    private class TransAndSplitJoinRenderer extends VertexRenderer
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
        	g.setColor(Color.WHITE);
        	g.fillRect(b - 1, b - 1, d.width - b, d.height - b);        	
                g.setColor(bordercolor);
                g2.setStroke(new BasicStroke(b));
                g.drawRect(b, b, d.width - b - 1, d.height - b - 1);
            }
            if (selected)
            {
                //				g2.setStroke(GraphConstants.SELECTION_STROKE);
                g.setColor(ConfigurationManager.getConfiguration().getSelectionColor());
                g.drawRect(b, b, d.width - b - 1, d.height - b - 1);
            }
            
            // AND SPLIT JOIN Lines
            // Consists of the symbol for split and the symbol for join

            
            g.drawLine(d.width / 3, b, d.width / 3, d.height - b);
            g.drawLine(b, b, d.width / 3, d.height / 2);
            g.drawLine(d.width / 3, d.height / 2, b, d.height - b);

            g.drawLine(d.width * 2 / 3, b, d.width * 2 / 3, d.height - b);
            g.drawLine(d.width * 2 / 3, d.height / 2, d.width - b, d.height - b);
            g.drawLine(d.width - b, b, d.width * 2 / 3, d.height / 2);

            if (isActive() || isFireing())
            {
                g.setColor(Color.LIGHT_GRAY);
                g.drawLine(d.width / 3, b, d.width / 3, d.height - b);
                g.drawLine(b, b, d.width / 3, d.height / 2);
                g.drawLine(d.width / 3, d.height / 2, b, d.height - b);
                
                g.drawLine(d.width * 2 / 3, b, d.width * 2 / 3, d.height - b);
                g.drawLine(d.width * 2 / 3, d.height / 2, d.width - b, d.height - b);
                g.drawLine(d.width - b, b, d.width * 2 / 3, d.height / 2);
                
                g2.setColor(Color.RED);
                g2.setFont(DefaultStaticConfiguration.DEFAULT_TOKENGAME_FONT);
            }
                  
            if (isActive() && !isFireing())
            {
                //g2.drawString("enabled", 3, 18);
        	ImageIcon img = Messages.getImageIcon("TokenGame.Active");
                g2.drawImage(img.getImage(), 5, 20, 16, 16, img.getImageObserver());

            }
            if (isFireing())
            {
                // g.setColor(Color.BLACK);
                ImageIcon img = new ImageIcon(getClass().getResource("/org/woped/editor/gui/images/tokenGame_fire.gif"));
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
            return TransAndSplitJoinView.this.isActivated();
        }

        /**
         * @return
         */
        public boolean isFireing()
        {
            return TransAndSplitJoinView.this.isFireing();
        }
    }

}