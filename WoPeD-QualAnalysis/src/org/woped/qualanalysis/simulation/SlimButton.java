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
public class SlimButton extends JButton{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Constants
	public final static int DEFAULT     = 0;
	public final static int EYE_PLAY    = 1;
	public final static int EYE_BACK    = 2;
	public final static int EYE_FORWARD = 3;
	public final static int EYE_STOP    = 4;
	public final static int EYE_EXPERT  = 5;
	
	
	//Button Images for the different Situations
	private Image  standard     = null;
	private Image  rollover     = null;
	private Image  pressed      = null;
		
	//Shape
	private Shape  shape        = null;
	
	//Dimension
	private Dimension d = null;
	
	//Integers
	private int ViewMode        = DEFAULT;   
	
	
	public SlimButton(Icon icon)
	{	
      super(icon);
      /*
       * Load the Button's GUI
       */
      

	  standard = Messages.getImageSource("Tokengame.SlimView.Button.Standard.Standard");
	  rollover = Messages.getImageSource("Tokengame.SlimView.Button.Standard.Rollover");
	  pressed  = Messages.getImageSource("Tokengame.SlimView.Button.Standard.Pressed");
	  
      Dimension d = new Dimension(40, 40);
      this.setPreferredSize(d);
    
      setContentAreaFilled(false);
    }
	
	public SlimButton(int ViewMode)
	{
      super();
      this.ViewMode = ViewMode;	
      
  
      this.setPreferredSize(d);
      setContentAreaFilled(false);

      
    }
	

    // Paint the round background and label.
    protected void paintComponent(Graphics g) 
    {
        switch(ViewMode)
        {
         case DEFAULT:
        	  standard = Messages.getImageSource("Tokengame.SlimView.Button.Standard.Standard");
        	  rollover = Messages.getImageSource("Tokengame.SlimView.Button.Standard.Rollover");
        	  pressed  = Messages.getImageSource("Tokengame.SlimView.Button.Standard.Pressed");
        	  
              new Dimension(40, 40);
              break;
        
         case EYE_PLAY:
      	 /*
      	  * Load the Button's GUI
      	  */
      	    	
      	  standard = Messages.getImageSource("Tokengame.EyeView.Button.Play.Standard");
      	  rollover = Messages.getImageSource("Tokengame.EyeView.Button.Play.Rollover");
      	  pressed  = Messages.getImageSource("Tokengame.EyeView.Button.Play.Pressed");
      		  
      	//  d = new Dimension(62, 62);
      	  break;
      	  
         case EYE_BACK:
        	 /*
        	  * Load the Button's GUI
        	  */
        	    	
        	  standard = Messages.getImageSource("Tokengame.EyeView.Button.Back.Standard");
        	  rollover = Messages.getImageSource("Tokengame.EyeView.Button.Back.Rollover");
        	  pressed  = Messages.getImageSource("Tokengame.EyeView.Button.Back.Pressed");
        		  
        	  new Dimension(32, 68);
        	  break;
      	  
         case EYE_FORWARD:
        	 /*
        	  * Load the Button's GUI
        	  */
        	    	
        	  standard = Messages.getImageSource("Tokengame.EyeView.Button.Forward.Standard");
        	  rollover = Messages.getImageSource("Tokengame.EyeView.Button.Forward.Rollover");
        	  pressed  = Messages.getImageSource("Tokengame.EyeView.Button.Forward.Pressed");
        		  
        	  new Dimension(27, 68);
        	  break;
        	  
         case EYE_STOP:
           /*
            * Load the Button's GUI
            */
          	    	
            standard = Messages.getImageSource("Tokengame.EyeView.Button.Stop.Standard");
         	  rollover = Messages.getImageSource("Tokengame.EyeView.Button.Stop.Rollover");
         	  pressed  = Messages.getImageSource("Tokengame.EyeView.Button.Stop.Pressed");
          		  
         	  new Dimension(68, 29);
         	  break;
         	  
         case EYE_EXPERT:
           /*
            * Load the Button's GUI
            */
            	    	
            standard = Messages.getImageSource("Tokengame.EyeView.Button.Expert.Standard");
            rollover = Messages.getImageSource("Tokengame.EyeView.Button.Expert.Rollover");
            pressed  = Messages.getImageSource("Tokengame.EyeView.Button.Expert.Pressed");
            		  
            new Dimension(68, 28);
            break;

        }	
        this.setPreferredSize(d);
	 /*
	  * Define the GUI for the different Actions of the Buttons 
	  */
	 if (getModel().isPressed()) 
	 {	 
		 g.drawImage (pressed, 0, 0, this);
     }
	 if(getModel().isRollover())
	 {
		 g.drawImage (rollover, 0, 0, this); 
	 }

	 if(!getModel().isRollover() && !getModel().isPressed())
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
         switch(ViewMode)
         {
          case DEFAULT:
              d = new Dimension(40, 40);
              break;
         
          case EYE_PLAY:
       	      d = new Dimension(42, 62);
       	      break;
       	  
          case EYE_BACK:  
         	  d = new Dimension(32, 68);
         	  break;
       	  
          case EYE_FORWARD:
         	  d = new Dimension(27, 68);
         	  break;
         	  
          case EYE_STOP:
           	  d = new Dimension(68, 29);
          	  break;
          	  
          case EYE_EXPERT:
              d = new Dimension(68, 28);
              break;

         }
       shape = new Ellipse2D.Float(0, 0, d.width, d.height);
     }
     return shape.contains(x, y);
   }
   

}


