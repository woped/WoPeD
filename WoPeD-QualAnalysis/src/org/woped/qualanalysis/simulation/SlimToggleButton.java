package org.woped.qualanalysis.simulation;
import javax.swing.*;
import java.awt.*;
import org.woped.translations.Messages;
import java.awt.geom.*;

/**
 * The SlimButton-class has been implemented to create nice buttons for the TokenGameBar's
 * SlimView
 * 
 * @author Tilmann Glaser
 *
 */
public class SlimToggleButton extends JToggleButton{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Button Images for the different Situations
	private Image  standard     = null;
	private Image  rollover     = null;
	private Image  pressed      = null;
		
	//Shape
	private Shape  shape        = null;
	
	public SlimToggleButton(Icon icon)
	{	
      super(icon);
      Dimension d = new Dimension(40, 40);
      this.setPreferredSize(d);
    
      setContentAreaFilled(false);
    }

    // Paint the round background and label.
    protected void paintComponent(Graphics g) 
    {
      /*
       * Load the Button's GUI
       */
	  standard = Messages.getImageSource("Tokengame.SlimView.Button.Standard.Standard");
	  rollover = Messages.getImageSource("Tokengame.SlimView.Button.Standard.Rollover");
	  pressed  = Messages.getImageSource("Tokengame.SlimView.Button.Standard.Pressed");
	  
	 /*
	  * Define the GUI for the different Actions of the Buttons 
	  */
	 if (getModel().isSelected()) 
	 {	 
		 g.drawImage (pressed, 0, 0, this);
     }
	 if(getModel().isRollover())
	 {
		 g.drawImage (rollover, 0, 0, this); 
	 }

	 if(!getModel().isRollover() && !getModel().isSelected())
	 {
         g.drawImage (standard, 0, 0, this);
	 }
     
	 super.paintComponent(g);
  }

   protected void paintBorder(Graphics g) 
   {
	  //No Border will be painted
      return; 
   }

   //Hit detection.
   public boolean contains(int x, int y) 
   {
     if (shape == null || !shape.getBounds().equals(getBounds())) 
     {
       shape = new Ellipse2D.Float(0, 0, 40, 40);
     }
     return shape.contains(x, y);
   }
   

}


