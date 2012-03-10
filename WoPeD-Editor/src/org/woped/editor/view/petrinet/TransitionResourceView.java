/*
 * Created on 01.07.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.woped.editor.view.petrinet;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.VertexRenderer;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.config.DefaultStaticConfiguration;
import org.woped.core.model.petrinet.TransitionResourceModel;
import org.woped.core.view.AbstractElementView;

/**
 * @author waschtl
 */

@SuppressWarnings("serial")
public class TransitionResourceView extends AbstractElementView
{
    private TransitionResourceRenderer renderer = new TransitionResourceRenderer();

    /**
     * @param cell
     */
    public TransitionResourceView(Object cell)
    {
        super(cell);
    }

    public CellViewRenderer getRenderer()
    {
        return renderer;
    }

	class TransitionResourceRenderer extends VertexRenderer
    {
        public void paint(Graphics g)
        {
            int b = borderWidth;
            Dimension d = getSize();
            Graphics2D g2 = (Graphics2D) g;
            if (super.isOpaque())
            {
                g.setColor(super.getBackground());
                //g.fillRect(b - 1, b - 1, d.width - b, d.height - b);

            }
            if (bordercolor != null)
            {
                g.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(b));
            }
            
            if (selected)
            {
				Color primary = ConfigurationManager.getConfiguration().getSelectionColor(); 
				Color borderColor = new Color(209, 209, 255);
				Color backgroundColor = new Color(primary.getRed(), primary.getGreen(), primary.getBlue(), 22);
				g.setColor(backgroundColor);
				g.fillRect(b, b, d.width - b - 1, d.height - b - 1);
				g.setColor(borderColor);
				g.drawRect(b, b, d.width - b - 1, d.height - b - 1);
//                g2.setStroke(GraphConstants.SELECTION_STROKE);
//                g.setColor(ConfigurationManager.getConfiguration().getSelectionColor());
//                g.drawRect(b, b, d.width - b - 1, d.height - b - 1);
            }

            g.setColor(new Color(192,192,192));
            g.fillRect(0, TransitionResourceModel.DEFAULT_HEIGHT / 2, TransitionResourceModel.DEFAULT_WIDTH - 1, TransitionResourceModel.DEFAULT_HEIGHT / 2 - 1);
            g.setColor(Color.BLACK);
            g.drawRect(0, TransitionResourceModel.DEFAULT_HEIGHT / 2, TransitionResourceModel.DEFAULT_WIDTH - 1, TransitionResourceModel.DEFAULT_HEIGHT / 2 - 1);
            g2.setFont(DefaultStaticConfiguration.DEFAULT_RESOURCE_ORG_FONT);
            g2.drawString(((TransitionResourceModel) TransitionResourceView.this.getCell()).getTransOrgUnitName(), 2, (TransitionResourceModel.DEFAULT_HEIGHT / 2) - 1);
            g2.setFont(DefaultStaticConfiguration.DEFAULT_RESOURCE_ROLE_FONT);
            g2.drawString(((TransitionResourceModel) TransitionResourceView.this.getCell()).getTransRoleName(), 2, TransitionResourceModel.DEFAULT_HEIGHT - 2);
        }

    }
}