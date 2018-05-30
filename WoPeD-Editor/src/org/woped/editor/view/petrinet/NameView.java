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
package org.woped.editor.view.petrinet;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.VertexRenderer;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.view.AbstractElementView;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * 
 * 21.09.2003
 */

@SuppressWarnings("serial")
public class NameView extends AbstractElementView {

	static NameViewRenderer renderer = new NameViewRenderer();

	/**
	 * Constructor for TransitionView.
	 * 
	 * @param cell
	 * @param graph
	 * @param mapper
	 */
	public NameView(Object cell) {
		super(cell);
	}

	public CellViewRenderer getRenderer() {

		return renderer;

	}

	/**
	 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
	 * 
	 * this inner class contains the Renderer information of an transition
	 * 
	 * 28.03.2003
	 */

	static class NameViewRenderer extends VertexRenderer {

		public void paint(Graphics g) {

			/* Trigger hinzufügen */
			int b = borderWidth;
			Graphics2D g2 = (Graphics2D) g;
			Dimension d = getSize();
			boolean tmp = selected;
			if (super.isOpaque()) {
				g.setColor(super.getBackground());
				g.fillRect(b - 1, b - 1, d.width - b, d.height - b);
			}
			try {
				setBorder(null);
				setOpaque(false);
				selected = false;
				super.paint(g);
			} finally {
				selected = tmp;
			}
			if (bordercolor != null) {
				g.setColor(bordercolor);
				g2.setStroke(new BasicStroke(b));
				// g.drawRect(b , b , d.width - b -1, d.height - b -1);
			}
			if (selected) {
				Color primary = ConfigurationManager.getConfiguration().getSelectionColor(); 
				Color borderColor = new Color(209, 209, 255);
				Color backgroundColor = new Color(primary.getRed(), primary.getGreen(), primary.getBlue(), 22);
				g.setColor(backgroundColor);
				g.fillRect(0, 0, d.width - b - 1, d.height - 1);
				g.setColor(borderColor);
				g.drawRect(0, 0, d.width - b - 1, d.height - 1);
			}

		}
	}

}