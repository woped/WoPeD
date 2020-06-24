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
/*
 * Created on 13.08.2004
 */
package org.woped.editor.gui;

import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.AbstractGraph;
import org.woped.core.model.ArcModel;
import org.woped.core.model.petrinet.GroupModel;
import org.woped.editor.action.WoPeDAction;
import org.woped.editor.controller.ActionFactory;
import org.woped.editor.controller.VisualController;

/**
 * Creates the Popupmenu and adds the MenuItems to the VisualController.
 * Elements will be created when the get-Method is called the first time. All
 * MenuItems are JMenuItems.
 * 
 * @author Thomas Pohl
 *  
 */

@SuppressWarnings("serial")
public class PopupMenuPetrinet extends JPopupMenu
{
    private static PopupMenuPetrinet 	c_instance                = null;

    private	static AbstractApplicationMediator c_mediator				  = null;

    private JMenuItem                	m_openSubprocessMenuItem  = null;
    private JMenuItem                	m_propertiesMenuItem      = null;

    private JMenuItem                	m_addPointMenuItem        = null;
    private JMenuItem                	m_removePointMenuItem     = null;
    private JMenuItem m_increaseArcWeight = null;
    private JMenuItem m_decreaseArcWeight = null;

    private JMenuItem                	m_externalTriggerMenuItem = null;
    private JMenuItem                	m_resourceTriggerMenuItem = null;
    private JMenuItem                	m_timeTriggerMenuItem     = null;
    private JMenuItem                	m_removeTriggerMenuItem   = null;

    private JMenuItem                	m_addTokenMenuItem        = null;
    private JMenuItem                	m_removeTokenMenuItem     = null;

    private JMenuItem                	m_removeMenuItem          = null;
    private JMenuItem                	m_renameMenuItem          = null;
    private JMenuItem                	m_copyMenuItem            = null;
    private JMenuItem                	m_cutMenuItem             = null;
    private JMenuItem                	m_pasteMenuItem           = null;
    private JMenuItem                	m_pasteAtMenuItem         = null;

    private JMenuItem                	m_addPlaceMenuItem        = null;
    private JMenuItem                	m_addTransitionMenuItem   = null;
    private JMenuItem                	m_addAndSplitMenuItem     = null;
    private JMenuItem                	m_addAndJoinMenuItem      = null;
    private JMenuItem		     	 	m_addAndSplitJoinMenuItem = null;
    private JMenuItem                	m_addXorSplitMenuItem     = null;
    private JMenuItem                	m_addXorJoinMenuItem      = null;
    private JMenuItem               	m_addXorSplitJoinMenuItem = null;
    private JMenuItem                	m_addAndJoinXorSplitMenuItem = null;
    private JMenuItem                	m_addXorJoinAndSplitMenuItem = null;
    private JMenuItem                	m_addSubprocessMenuItem     = null;
    private JMenuItem					m_rotateRightMenuItem		= null;
    private JMenuItem					m_rotateLeftMenuItem	    = null;
    private JMenuItem					m_groupMenuItem	    		= null;
    private JMenuItem					m_ungroupMenuItem	    	= null;

    private WoPeDAction action = null;
    private String						action_id;
 
    private PopupMenuPetrinet()
    {
        add(getOpenSubprocessMenuItem());
        add(getAddPlaceMenuItem());
        add(getAddTransitionMenuItem());
        add(getAddAndSplitMenuItem());
        add(getAddXorSplitMenuItem());
        add(getAddAndJoinMenuItem());
        add(getAddXorJoinMenuItem());
        add(getAddAndSplitJoinMenuItem());
        add(getAddXorSplitJoinMenuItem());
        add(getAddAndJoinXorSplitMenuItem());
        add(getAddXorJoinAndSplitMenuItem());
        add(getAddSubprocessMenuItem());
        add(getPropertiesMenuItem());
        add(getRenameMenuItem());
        add(getRemoveMenuItem());
        add(getCutMenuItem());
        add(getCopyMenuItem());
        add(getPasteMenuItem());  
        add(getPasteAtMenuItem());  
        add(getAddTokenMenuItem());
        add(getRemoveTokenMenuItem());
        add(getRotateLeftMenuItem());
        add(getRotateRightMenuItem());
        add(getExternalTriggerMenuItem());
        add(getResourceTriggerMenuItem());
        add(getTimeTriggerMenuItem());
        add(getRemoveTriggerMenuItem());
        add(getAddPointMenuItem());
        add(getIncreaseArcWeightMenuItem());
        add(getDecreaseArcWeightMenuItem());
        add(getRemovePointMenuItem());
        add(getGroupMenuItem());
        add(getUngroupMenuItem());
        pack();
    }

    /**
     * Gets the current popup menu instance.
     *
     * @return the instance of the popup menu
     */
    public static PopupMenuPetrinet getInstance()
    {
       	if (c_instance == null)
        {
            c_instance = new PopupMenuPetrinet();
            VisualController.getInstance().propertyChange(new PropertyChangeEvent(c_mediator, "InternalFrameCount", null, null));
        }
        c_instance.pack();
        return c_instance;
    }
    
    public static void setMediator(AbstractApplicationMediator mediator) {
    	c_mediator = mediator;
    }
   
    public void show(final Object obj, final AbstractGraph graph, final int x, final int y)
    {
     	// Wait with displaying the popup until the
    	// selection event has been processed.
    	// This way, the size of the popup menu will be correct
    	// according to the selected item
    	Runnable runner = new Runnable() {
            public void run() {
    			Object selectedCell = graph.getSelectionCell();            	            	
                if (selectedCell == null || selectedCell instanceof GroupModel 
                    	|| selectedCell instanceof ArcModel) 
                    {
                    	pack();
                        PopupMenuPetrinet.super.show(graph, x, y);
                    }

            }
        };
        EventQueue.invokeLater(runner);
    }
    
    public void show(final AbstractGraph graph, final int x, final int y)
    {
        PopupMenuPetrinet.super.show(graph, x, y);
    }
    
    private JMenuItem getRenameMenuItem()
    {
        if (m_renameMenuItem == null)
        {
          	action_id = ActionFactory.ACTIONID_RENAME;
        	action = ActionFactory.getStaticAction(action_id);
            m_renameMenuItem = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_renameMenuItem);            
        }
        return m_renameMenuItem;
    }

    private JMenuItem getRemoveMenuItem()
    {
        if (m_removeMenuItem == null)
        {
           	action_id = ActionFactory.ACTIONID_REMOVE;
        	action = ActionFactory.getStaticAction(action_id);
            m_removeMenuItem = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_removeMenuItem);            
        }
        return m_removeMenuItem;
    }

     private JMenuItem getCopyMenuItem()
    {
        if (m_copyMenuItem == null)
        {
           	action_id = ActionFactory.ACTIONID_COPY;
        	action = ActionFactory.getStaticAction(action_id);
            m_copyMenuItem = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_copyMenuItem);            
        }
        return m_copyMenuItem;
    }

    private JMenuItem getCutMenuItem()
    {
        if (m_cutMenuItem == null)
        {
           	action_id = ActionFactory.ACTIONID_CUT;
        	action = ActionFactory.getStaticAction(action_id);
            m_cutMenuItem = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_cutMenuItem);            
        }
        return m_cutMenuItem;
    }

    private JMenuItem getExternalTriggerMenuItem()
    {
        if (m_externalTriggerMenuItem == null)
        {
           	action_id = ActionFactory.ACTIONID_ADD_MESSAGE_TRIGGER;
        	action = ActionFactory.getStaticAction(action_id);
            m_externalTriggerMenuItem = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_externalTriggerMenuItem);            
        }
        return m_externalTriggerMenuItem;
    }

    private JMenuItem getResourceTriggerMenuItem()
    {
        if (m_resourceTriggerMenuItem == null)
        {
           	action_id = ActionFactory.ACTIONID_ADD_RESOURCE_TRIGGER;
        	action = ActionFactory.getStaticAction(action_id);
            m_resourceTriggerMenuItem = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_resourceTriggerMenuItem);            
        }
        return m_resourceTriggerMenuItem;
    }

    private JMenuItem getTimeTriggerMenuItem()
    {
        if (m_timeTriggerMenuItem == null)
        {
           	action_id = ActionFactory.ACTIONID_ADD_TIME_TRIGGER;
        	action = ActionFactory.getStaticAction(action_id);
            m_timeTriggerMenuItem = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_timeTriggerMenuItem);            
        }
        return m_timeTriggerMenuItem;
    }

    private JMenuItem getRemoveTriggerMenuItem()
    {
        if (m_removeTriggerMenuItem == null)
        {
           	action_id = ActionFactory.ACTIONID_REMOVE_TRIGGER;
        	action = ActionFactory.getStaticAction(action_id);
            m_removeTriggerMenuItem = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_removeTriggerMenuItem);            
        }
        return m_removeTriggerMenuItem;
    }

    private JMenuItem getAddPlaceMenuItem()
    {
        if (m_addPlaceMenuItem == null)
        {
        	action_id = ActionFactory.ACTIONID_ADD_PLACE;
        	action = ActionFactory.getStaticAction(action_id);
            m_addPlaceMenuItem = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_addPlaceMenuItem);            
        }
        return m_addPlaceMenuItem;
    }

    private JMenuItem getAddTokenMenuItem()
    {
        if (m_addTokenMenuItem == null)
        {
           	action_id = ActionFactory.ACTIONID_ADD_TOKEN;
        	action = ActionFactory.getStaticAction(action_id);
            m_addTokenMenuItem = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_addTokenMenuItem);            
        }
        return m_addTokenMenuItem;
    }

    private JMenuItem getRemoveTokenMenuItem()
    {
        if (m_removeTokenMenuItem == null)
        {
           	action_id = ActionFactory.ACTIONID_REMOVE_TOKEN;
        	action = ActionFactory.getStaticAction(action_id);
            m_removeTokenMenuItem = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_removeTokenMenuItem);            
//            VisualController.getInstance().addElement(action, VisualController.TOKEN_PLACE_SELECTION, VisualController.PLACE_SELECTION, VisualController.IGNORE);
        }
        return m_removeTokenMenuItem;
    }

    private JMenuItem getOpenSubprocessMenuItem()
    {
        if (m_openSubprocessMenuItem == null)
        {
           	action_id = ActionFactory.ACTIONID_OPEN_SUBPROCESS;
        	action = ActionFactory.getStaticAction(action_id);
            m_openSubprocessMenuItem = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_openSubprocessMenuItem);            
        }
        return m_openSubprocessMenuItem;
    }

    private JMenuItem getAddAndJoinMenuItem()
    {
        if (m_addAndJoinMenuItem == null)
        {
           	action_id = ActionFactory.ACTIONID_ADD_ANDJOIN;
        	action = ActionFactory.getStaticAction(action_id);
            m_addAndJoinMenuItem = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_addAndJoinMenuItem);            
        }
        return m_addAndJoinMenuItem;
    }

    private JMenuItem getAddAndSplitMenuItem()
    {
        if (m_addAndSplitMenuItem == null)
        {
           	action_id = ActionFactory.ACTIONID_ADD_ANDSPLIT;
        	action = ActionFactory.getStaticAction(action_id);
            m_addAndSplitMenuItem = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_addAndSplitMenuItem);            
//            VisualController.getInstance().addElement(action, VisualController.NO_SELECTION, VisualController.NO_SELECTION, VisualController.IGNORE);
        }
        return m_addAndSplitMenuItem;
    }
    
    private JMenuItem getAddAndSplitJoinMenuItem()
    {
    	if (m_addAndSplitJoinMenuItem == null)
    	{
           	action_id = ActionFactory.ACTIONID_ADD_ANDSPLITJOIN;
        	action = ActionFactory.getStaticAction(action_id);
            m_addAndSplitJoinMenuItem = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_addAndSplitJoinMenuItem);            
    	}
    	return m_addAndSplitJoinMenuItem;    	
    }

    private JMenuItem getAddTransitionMenuItem()
    {
        if (m_addTransitionMenuItem == null)
        {
           	action_id = ActionFactory.ACTIONID_ADD_TRANSITION;
        	action = ActionFactory.getStaticAction(action_id);
            m_addTransitionMenuItem = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_addTransitionMenuItem);            
        }
        return m_addTransitionMenuItem;
    }

    private JMenuItem getAddXorJoinMenuItem()
    {
        if (m_addXorJoinMenuItem == null)
        {
           	action_id = ActionFactory.ACTIONID_ADD_XORJOIN;
        	action = ActionFactory.getStaticAction(action_id);
            m_addXorJoinMenuItem = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_addXorJoinMenuItem);            
        }
        return m_addXorJoinMenuItem;
    }

    private JMenuItem getAddXorSplitMenuItem()
    {
        if (m_addXorSplitMenuItem == null)
        {
           	action_id = ActionFactory.ACTIONID_ADD_XORSPLIT;
        	action = ActionFactory.getStaticAction(action_id);
            m_addXorSplitMenuItem = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_addXorSplitMenuItem);            
        }
        return m_addXorSplitMenuItem;
    }

    private JMenuItem getAddXorSplitJoinMenuItem()
    {
        if (m_addXorSplitJoinMenuItem == null)
        {
           	action_id = ActionFactory.ACTIONID_ADD_XORSPLITJOIN;
        	action = ActionFactory.getStaticAction(action_id);
            m_addXorSplitJoinMenuItem = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_addXorSplitJoinMenuItem);            
        }
        return m_addXorSplitJoinMenuItem;
    }

    private JMenuItem getAddAndJoinXorSplitMenuItem()
    {
        if (m_addAndJoinXorSplitMenuItem == null)
        {
           	action_id = ActionFactory.ACTIONID_ADD_ANDJOINXORSPLIT;
        	action = ActionFactory.getStaticAction(action_id);
            m_addAndJoinXorSplitMenuItem = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_addAndJoinXorSplitMenuItem);            
         }
        return m_addAndJoinXorSplitMenuItem;
    }

    private JMenuItem getAddXorJoinAndSplitMenuItem()
    {
        if (m_addXorJoinAndSplitMenuItem == null)
        {
           	action_id = ActionFactory.ACTIONID_ADD_XORJOINANDSPLIT;
        	action = ActionFactory.getStaticAction(action_id);
            m_addXorJoinAndSplitMenuItem = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_addXorJoinAndSplitMenuItem);            
        }
        return m_addXorJoinAndSplitMenuItem;
    }
    
    private JMenuItem getAddSubprocessMenuItem()
    {
        if (m_addSubprocessMenuItem == null)
        {
           	action_id = ActionFactory.ACTIONID_ADD_SUBPROCESS;
        	action = ActionFactory.getStaticAction(action_id);
            m_addSubprocessMenuItem = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_addSubprocessMenuItem);            
        }
        return m_addSubprocessMenuItem;
    }

    private JMenuItem getPasteMenuItem()
    {
        if (m_pasteMenuItem == null)
        {
           	action_id = ActionFactory.ACTIONID_PASTE;
        	action = ActionFactory.getStaticAction(action_id);
            m_pasteMenuItem = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_pasteMenuItem);            
        }
        return m_pasteMenuItem;
    }

    private JMenuItem getPasteAtMenuItem()
    {
        if (m_pasteAtMenuItem == null)
        {
           	action_id = ActionFactory.ACTIONID_PASTE_AT;
        	action = ActionFactory.getStaticAction(action_id);
            m_pasteAtMenuItem = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_pasteAtMenuItem);            
        }
        return m_pasteAtMenuItem;
    }

    private JMenuItem getAddPointMenuItem()
    {
        if (m_addPointMenuItem == null)
        {
           	action_id = ActionFactory.ACTIONID_ADD_POINT;
        	action = ActionFactory.getStaticAction(action_id);
            m_addPointMenuItem = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_addPointMenuItem);            
        }
        return m_addPointMenuItem;
    }

    private JMenuItem getRemovePointMenuItem()
    {
        if (m_removePointMenuItem == null)
        {
           	action_id = ActionFactory.ACTIONID_REMOVE_POINT;
        	action = ActionFactory.getStaticAction(action_id);
            m_removePointMenuItem = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_removePointMenuItem);            
        }
        return m_removePointMenuItem;
    }

    private JMenuItem getPropertiesMenuItem()
    {
        if (m_propertiesMenuItem == null)
        {
           	action_id = ActionFactory.ACTIONID_OPEN_PROPERTIES;
        	action = ActionFactory.getStaticAction(action_id);
            m_propertiesMenuItem = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_propertiesMenuItem);            
        }
        return m_propertiesMenuItem;
    }
    
    private JMenuItem getRotateRightMenuItem()
    {
        if (m_rotateRightMenuItem == null)
        {
           	action_id = ActionFactory.ACTIONID_ROTATE_TRANS_RIGHT;
        	action = ActionFactory.getStaticAction(action_id);
            m_rotateRightMenuItem = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_rotateRightMenuItem);            
        }
        return m_rotateRightMenuItem;
    }

    private JMenuItem getRotateLeftMenuItem()
    {
        if (m_rotateRightMenuItem == null)
        {
           	action_id = ActionFactory.ACTIONID_ROTATE_TRANS_LEFT;
        	action = ActionFactory.getStaticAction(action_id);
            m_rotateLeftMenuItem = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_rotateLeftMenuItem);            
        }
        return m_rotateLeftMenuItem;
    }
    
    private JMenuItem getGroupMenuItem()
    {
        if (m_groupMenuItem == null)
        {
           	action_id = ActionFactory.ACTIONID_GROUP;
        	action = ActionFactory.getStaticAction(action_id);
        	m_groupMenuItem = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_groupMenuItem);            
        }
        return m_groupMenuItem;
    }

    private JMenuItem getUngroupMenuItem()
    {
        if (m_ungroupMenuItem == null)
        {
           	action_id = ActionFactory.ACTIONID_UNGROUP;
        	action = ActionFactory.getStaticAction(action_id);
        	m_ungroupMenuItem = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_ungroupMenuItem);            
        }
        return m_ungroupMenuItem;
    }

    private JMenuItem getIncreaseArcWeightMenuItem() {

        if ( m_increaseArcWeight == null ) {
            action_id = ActionFactory.ACTIONID_ARC_WEIGHT_INCREASE;
            action = ActionFactory.getStaticAction(action_id);
            m_increaseArcWeight = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_increaseArcWeight);
        }
        return m_increaseArcWeight;
    }

    private JMenuItem getDecreaseArcWeightMenuItem() {

        if ( m_decreaseArcWeight == null ) {
            action_id = ActionFactory.ACTIONID_ARC_WEIGHT_DECREASE;
            action = ActionFactory.getStaticAction(action_id);
            m_decreaseArcWeight = new JMenuItem(action);
            ActionFactory.addTarget(c_mediator, action_id, m_decreaseArcWeight);
        }

        return m_decreaseArcWeight;
    }
}