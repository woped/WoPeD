package org.woped.qualanalysis.simulation;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import org.woped.translations.Messages;
import org.woped.core.model.ArcModel;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.qualanalysis.simulation.controller.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.util.Vector;

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
	public NewInternalFrame()
	{
		super();
		this.setSize(580, 130);
		this.setVisible(true);
		this.setOpaque(false);
		mover = new JPanel();
		mover.setOpaque(false);
	    ((javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI()).setNorthPane(mover);
	      	
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