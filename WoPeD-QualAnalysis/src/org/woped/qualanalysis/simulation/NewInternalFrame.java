package org.woped.qualanalysis.simulation;
import javax.imageio.ImageIO;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicInternalFrameUI;


import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;



/**
 * This class specifies the remote control UI of the Tokengame-simulator
 * Currently no Actions can be performed by the Buttons except the close-"x"
 * Standard-Constructor is available
 * 
 * @author Tilmann Glaser
 * 
 */
public class NewInternalFrame extends JInternalFrame {
	private JPanel mover = null;
	private Shape  shape = null;
	
	//Constructor(s)
	/**
	 * never change opacity, please.
	 * The "mover" is realized with an non-opaque JPanel
	 */
	public NewInternalFrame(int ViewMode)
	{
		super();
		this.setSize(580, 130);
		this.setVisible(true);
		this.setOpaque(false);
		mover = new JPanel();
		mover.setOpaque(false);
	    ((BasicInternalFrameUI) this.getUI()).setNorthPane(mover);
	      	
	}
	

	// Paint the border of the button using a simple stroke.
	protected void paintBorder(Graphics g)
	{
	  return; 
	}

	//Hit detection.
	public boolean contains(int x, int y) 
	{
	  if (shape == null || !shape.getBounds().equals(getBounds())) 
	  {
	    shape = new Rectangle.Float(0, 0, 580,130);
	  }
	  return shape.contains(x, y);
	}

}