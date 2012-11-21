package org.woped.qualanalysis.simulation;

import org.woped.core.controller.*;
import org.woped.core.gui.*;

import javax.swing.*;

/**
 * PLEASE NOTE: THIS CLASS IS NOT FINAL, CURRENTLY AND MAY BE REMOVED, AGAIN.
 * DO ONLY USE FOR QUAL-ANALYSIS PURPOSES!
 * The ReferenceProvider-Class is a help-class to be able to reference several QualAnalysis-Components to
 * Either Main-Window, Desktop-Pane or the Default-Application-Mediator.
 * This is needed for example, to attach QualAnalysis-Elements as JInternalFrames, JDialogs etc.
 * The constructor is just an empty method.
 * The approach of this method is, to link those three (in the application only once occurring) modules as 
 * references. Each in a instance-variable.
 * This is done through the get and set Methods of this Class
 * 
 *  @author Tilmann Glaser
 *
 */
public class ReferenceProvider {
 	private static AbstractApplicationMediator MediatorReference = null;
 	private static IUserInterface              UIReference       = null;
	private static JDesktopPane                DesktopReference  = null;
	private static TokenGameSession      RemoteControl     = null;
	
	//private static boolean					   changePanel		 = false;
    	
	
	/**
	 * Creates new ReferencePRovider Object to get or set the Reference-Instance-Variables
	 * Handle with care!
	 */
	public ReferenceProvider()
	{
		
	}
    
	/**
	 * sets the reference of the AbstractApplicationMediator
	 * @param Mediator
	 */
	public void setMediatorReference(AbstractApplicationMediator Mediator)
	{
		MediatorReference = Mediator;
	}
	
	/**
	 * 
	 * @return Reference to the AbstractApplicationMediator (default: null)
	 */
	public AbstractApplicationMediator getMediatorReference()
	{
		return MediatorReference;
		
	}
	
	/**
	 * sets the DefaultUserInterface reference (through the IUserInterface)
	 * @param UI
	 */
	public void setUIReference(IUserInterface UI)
	{
		UIReference = UI;
	}
	
	/**
	 * 
	 * @return reference to the IUserInterface
	 */
	public IUserInterface getUIReference()
	{
		return UIReference;
		
	}
	
	/**
	 * sets the reference to the DesktopPane
	 * @param Desktop
	 */
	public void setDesktopReference(JDesktopPane Desktop)
	{
		DesktopReference = Desktop;
	}
	
	/**
	 * 
	 * @return the reference to the JDesktopPane
	 */
	public JDesktopPane getDesktopReference()
	{
		return DesktopReference;
		
	}
	
	public void setRemoteControlReference(TokenGameSession Control)
	{
		RemoteControl = Control;
	}
	
	public TokenGameSession getRemoteControlReference()
	{
		return RemoteControl;
	}
}
