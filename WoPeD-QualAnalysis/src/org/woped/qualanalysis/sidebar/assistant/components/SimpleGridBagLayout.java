package org.woped.qualanalysis.sidebar.assistant.components;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

/**
 * simplification of gridbaglayout
 * 
 * @author Lennart Oess, Arthur Vetter, Jens Tessen, Heiko Herzog
 * 
 */
@SuppressWarnings("serial")
public class SimpleGridBagLayout extends GridBagLayout {

	/**
	 * lays a component in the given container
	 * 
	 * @param cont
	 *            - container
	 * @param c
	 *            - component
	 * @param x
	 *            - x-position
	 * @param y
	 *            - y-position
	 * @param width
	 *            - component width
	 * @param height
	 *            - component height
	 * @param weightx
	 *            - component weight in x direction
	 * @param weighty
	 *            - component weight in y direction
	 */
	public void addComponent(Container cont, Component c, int x, int y,
			int width, int height, double weightx, double weighty) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		this.setConstraints(c, gbc);
		cont.add(c);
	}

}
