package org.woped.editor.view.uml;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.VertexRenderer;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.model.uml.ActivityModel;
import org.woped.core.view.AbstractElementView;

public class ActivityView extends AbstractElementView
{
    private ActivityRenderer renderer = new ActivityRenderer();

    public ActivityView(Object cell)
    {
        super(cell);
    }

    public CellViewRenderer getRenderer()
    {
        return renderer;
    }

    /**
     * 
     * this inner class contains the render information of a place
     * 
     */
    private class ActivityRenderer extends VertexRenderer
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
            if (((ActivityModel) ActivityView.this.getCell()).getIcon() != null)
            {
                ImageIcon img = ((ActivityModel) ActivityView.this.getCell()).getIcon();
                g2.drawImage(img.getImage(), 2, 2, img.getImageObserver());
                //((ActivityModel) ActivityView.this.getCell()).setSize(img.getIconWidth()+4, img.getIconHeight()+4);
                if (selected)
                {
                    g2.drawRect(0, 0, d.width - 1, d.height - 1);
                }
            } else
            {
                if (super.isOpaque())
                {
                    g2.fillRoundRect(b, b, d.width - b - 1, d.height - b - 1, 15, 15);
                }
                if (bordercolor != null)
                {
                    g2.drawRoundRect(b, b, d.width - b - 2, d.height - b - 2, 15, 15);
                }
                if (selected)
                {
                    g2.drawRect(0, 0, d.width - 1, d.height - 1); // , 15,
                                                                    // 15);
                }
            }
            // try
            // {
            // setBorder(null);
            // setOpaque(false);
            // selected = false;
            // super.paint(g);
            // } finally
            // {
            // selected = tmp;
            // }
        }
    }

}
