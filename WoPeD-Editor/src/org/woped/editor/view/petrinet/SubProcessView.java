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
import java.net.URL;

import javax.swing.ImageIcon;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.GraphConstants;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.config.DefaultStaticConfiguration;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.view.AbstractElementView;
import org.woped.editor.utilities.Messages;

/**
 * @author rey
 * 
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of type
 * comments go to Window>Preferences>Java>Code Generation.
 */

@SuppressWarnings("serial")
public class SubProcessView extends AbstractElementView
{

    private SubProcessRenderer renderer = null;

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
        renderer = new SubProcessRenderer(cell);
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
    private class SubProcessRenderer extends AbstractElementRenderer
    {
    	public SubProcessRenderer(Object cell)
    	{
    		super(cell);
    	}

        public void paint(Graphics g)
        {

            /* Trigger hinzufügen */
            int b = borderWidth;
            Graphics2D g2 = (Graphics2D) g;
            Dimension d = getSize();
            boolean tmp = selected;
            if (super.isOpaque())
            {
                g.setColor(getFillColor());
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

            	g.setColor(getFillColor());
        	g.fillRect(b - 1, b - 1, d.width - b, d.height - b);
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
            if (isActive() || isFireing())
            {
                g2.setColor(Color.RED);
                g2.setFont(DefaultStaticConfiguration.DEFAULT_TOKENGAME_FONT);
            }
            if (isActive() && !isFireing())
            {
                //g2.drawString("enabled", 3, 18);
        	ImageIcon img1 = Messages.getImageIcon("TokenGame.Subprocess.StepInto");
                ImageIcon img2 = Messages.getImageIcon("TokenGame.Subprocess.StepOver");
                g2.drawImage(img1.getImage(), b + 5, d.height - b - 16 - 5, 16, 16, img1.getImageObserver());
                g2.drawImage(img2.getImage(), d.width - b -16 - 5, b + 5, 16, 16, img2.getImageObserver());
                g2.drawLine(b + 6,b + 6,d.width-b-7,d.height-b-7);

            }
            if (isFireing())
            {
                // g.setColor(Color.BLACK);
            	Class myClass = getClass();
            	URL resource = ((myClass!=null)?myClass.getResource("/org/woped/editor/gui/images/tokenGame_fire.gif"):null);
            	if (resource!=null)
            	{
            		ImageIcon img = new ImageIcon(resource);
            		g2.drawImage(img.getImage(), 5, 22, 18, 11, img.getImageObserver());
            	}
                g.drawRect(b, b, d.width - b - 1, d.height - b - 1);
                g2.drawString("fire", 5, 18);
            }
            
            // SubProcess Lines
            g.drawRect(5, 5, 29, 29);
            
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