package org.woped.core.utilities;

import java.awt.Component;
import java.awt.Dimension;

public class SwingUtils {
	
	/* Sets the preferred height of a Swing component */
	public static void setPreferredHeight(Component component, int height)
	{
		Dimension dim = component.getPreferredSize();
		dim.height = height;
		component.setPreferredSize(dim);
	}
	
	/* Sets the preferred width of a Swing component */
	public static void setPreferredWidth(Component component, int width)
	{
		Dimension dim = component.getPreferredSize();
		dim.width = width;
		component.setPreferredSize(dim);
	}

	/* Sets the minimum height of a Swing component */
	public static void setMinimumHeight(Component component, int height)
	{
		Dimension dim = component.getMinimumSize();
		dim.height = height;
		component.setMinimumSize(dim);
	}
	
	/* Sets the minimum width of a Swing component */
	public static void setMinimumWidth(Component component, int width)
	{
		Dimension dim = component.getMinimumSize();
		dim.width = width;
		component.setMinimumSize(dim);
	}

	/* Sets the maximum height of a Swing component */
	public static void setMaximumHeight(Component component, int height)
	{
		Dimension dim = component.getMaximumSize();
		dim.height = height;
		component.setMaximumSize(dim);
	}
	
	/* Sets the maximum width of a Swing component */
	public static void setMaximumWidth(Component component, int width)
	{
		Dimension dim = component.getMaximumSize();
		dim.width = width;
		component.setMaximumSize(dim);
	}
	
	/* Sets the minimum, preferred and maximum height of a Swing component */
	public static void setFixedHeight(Component component, int height)
	{
		setMinimumHeight(component, height);
		setPreferredHeight(component, height);
		setMaximumHeight(component, height);
	}
	
	/* Sets the minimum, preferred and maximum width of a Swing component */
	public static void setFixedWidth(Component component, int width)
	{
		setMinimumWidth(component, width);
		setPreferredWidth(component, width);
		setMaximumWidth(component, width);
	}
	
	/* Set the minimum, preferred and maximum size of a Swing component */
	public static void setFixedSize(Component component, int widht, int height)
	{
		Dimension dim = new Dimension(widht, height);
		component.setMinimumSize(dim);
		component.setPreferredSize(dim);
		component.setMaximumSize(dim);
	}

}
