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
package org.woped.gui.controller.vc;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JToolBar;

import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IViewController;
import org.woped.core.controller.IViewListener;
import org.woped.core.gui.IToolBar;
import org.woped.core.utilities.SwingUtils;
import org.woped.editor.controller.ActionFactory;
import org.woped.editor.controller.VisualController;
import org.woped.editor.controller.vep.ViewEvent;
import org.woped.editor.gui.ToolBarButton;
import org.woped.gui.RunWoPeD;
import org.woped.gui.controller.DefaultApplicationMediator;

/**
 * Part of a small example showing basic use of JToolBar. Creates a small
 * dockable toolbar that is supposed to look vaguely like one that might come
 * with a Web browser. Makes use of ToolBarButton, a small extension of JButton
 * that shrinks the margins around the icon and puts text label, if any, below
 * the icon. 1999 Marty Hall, http://www.apl.jhu.edu/~hall/java/ TODO:
 * DOCUMENTATION (silenco)
 */

@SuppressWarnings("serial")
public class ToolBarVC extends JToolBar implements IViewController, IToolBar
{
    // ViewControll
    private Vector<IViewListener>             viewListener         = new Vector<IViewListener>(1, 3);
    private String             id                   = null;
    public static final String ID_PREFIX            = "TOOLBAR_VC_";

    static final Insets        margins              = new Insets(0, 0, 0, 0);

    private JComboBox          zoomChooser          = null;

    private AbstractButton     m_newButton          = null;
    private AbstractButton     m_openButton         = null;
    private AbstractButton     m_saveButton         = null;
    private AbstractButton     m_saveAsButton       = null;

    private AbstractButton     m_cutButton          = null;
    private AbstractButton     m_copyButton         = null;
    private AbstractButton     m_pasteButton        = null;
    private AbstractButton     m_undoButton         = null;
    private AbstractButton     m_redoButton         = null;

    private AbstractButton     m_groupButton        = null;
    private AbstractButton     m_ungroupButton      = null;
    private AbstractButton     m_zoomInButton       = null;
    private AbstractButton     m_zoomOutButton      = null;
    
    private AbstractButton	   m_rotateViewButton 	= null;
    private AbstractButton	   m_rotateTransLeftButton = null;
    private AbstractButton	   m_rotateTransRightButton = null;

    private AbstractButton     m_placeButton        = null;
    private AbstractButton     m_transitionButton   = null;

    private AbstractButton     m_andSplitButton     = null;
    private AbstractButton     m_andJoinButton      = null;
    private AbstractButton	   m_andSplitJoinButton	= null;

    private AbstractButton     m_xorSplitButton     = null;
    private AbstractButton     m_xorJoinButton      = null;
    private AbstractButton     m_xorSplitJoinButton = null;
    
    private AbstractButton     m_andJoinXorSplitButton = null;
    private AbstractButton     m_xorJoinAndSplitButton = null;    

    private AbstractButton     m_subProcessButton   = null;

    private AbstractButton     m_tokenGameButton    = null;
    private AbstractButton     m_woflanButton       = null;
    private AbstractButton	   m_analysisButton		= null;
    private AbstractButton	   m_quantCapButton		= null;
    private AbstractButton	   m_quantSimButton		= null;
    private AbstractButton     m_reachabilityGraphButton = null;
    private AbstractButton	   m_coloringButton = null;
    private AbstractButton	   m_metricButton       = null;

    public ToolBarVC(String id)
    {
        this.id = id;
        setBorder(BorderFactory.createEtchedBorder());
        setRollover(true);

        add(getNewButton());
        add(getOpenButton());
        add(getSaveButton());
        if (!RunWoPeD.isApplet()) {
        	add(getSaveAsButton());
        }        
        addSeparator();
        addSeparator(new Dimension(12, 0));
        addSeparator();
        add(getCutButton());
        add(getCopyButton());
        add(getPasteButton());
        add(getUndoButton());
        add(getRedoButton());
        addSeparator();
        addSeparator(new Dimension(12, 0));
        addSeparator();
        add(getGroupButton());
        add(getUngroupButton());
        addSeparator(new Dimension(12, 0));
        //Zoom
        add(getZoomInButton());
        add(getZoomOutButton());
        addSeparator(new Dimension(12, 0));
        add(getZoomChooser());
        addSeparator(new Dimension(12, 0));
        //Rotation
        add(getRotateTransLeftButton());
        add(getRotateTransRightButton());
        add(getRotateViewButton());
        // Transitions and Places
        addSeparator();
        add(getPlaceButton());
        add(getTransitionButton());
        addSeparator(new Dimension(5, 0));
        add(getAndSplitButton());
        add(getXorSplitButton());
        addSeparator(new Dimension(5, 0));
        add(getAndJoinButton());
        add(getXorJoinButton());
        addSeparator(new Dimension(5, 5));
        add(getXorSplitJoinButton());
        add(getAndSplitJoinButton());
        add(getAndJoinXorSplitButton());
        add(getXorJoinAndSplitButton());
        // add(getOrSplitButton());
        addSeparator(new Dimension(5, 0));
        add(getSubProcessButton());
        // TokenGameController
        addSeparator();
        addSeparator(new Dimension(12, 0));
        addSeparator();
        add(getTokenGameButton());
        add(getWoflanButton());
        add(getAnalysisButton());
        add(getMetricButton());
        add(getQuantCapButton());
        add(getQuantSimButton());
        add(getReachabilityGraphButton());
        add(getColoringButton());
    }

    public void removeAnalysisButtons()
    {
    	this.remove(getWoflanButton());
    	this.remove(getAnalysisButton());
    	this.remove(getMetricButton());
    	this.remove(getQuantCapButton());
    	this.remove(getQuantSimButton());
    	this.remove(getReachabilityGraphButton());
    	this.remove(getColoringButton());
    }
    
    public void addAnalysisButtons()
    {
    	this.add(getWoflanButton());
    	this.add(getAnalysisButton());
    	this.add(getMetricButton());
    	this.add(getQuantCapButton());
    	this.add(getQuantSimButton());
    	this.add(getReachabilityGraphButton());
        this.add(getColoringButton());
        
    }
    
    private AbstractButton getNewButton()
    {
        if (m_newButton == null)
        {
            m_newButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_NEW));
        }
        return m_newButton;
    }

    private AbstractButton getOpenButton()
    {
        if (m_openButton == null)
        {
            if (!RunWoPeD.isApplet()) {
            	m_openButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_OPEN));
            } else {
            	m_openButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_OPENWEBSERVICE));
            }
        	
        }
        return m_openButton;
    }

    private AbstractButton getSaveButton()
    {
        if (m_saveButton == null)
        {
            if (!RunWoPeD.isApplet()) {
            	m_saveButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_SAVE));
            } else {
            	m_saveButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_SAVEWEBSERVICE));
            }
        }
        return m_saveButton;
    }

    private AbstractButton getSaveAsButton()
    {
        if (m_saveAsButton == null)
        {
            m_saveAsButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_SAVEAS));                    	
        }
        return m_saveAsButton;
    }

    private AbstractButton getCopyButton()
    {
        if (m_copyButton == null)
        {
            m_copyButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_COPY));
        }
        return m_copyButton;
    }

    private AbstractButton getCutButton()
    {
        if (m_cutButton == null)
        {
            m_cutButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_CUT));
        }
        return m_cutButton;
    }

    private AbstractButton getPasteButton()
    {
        if (m_pasteButton == null)
        {
            m_pasteButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_PASTE));
        }
        return m_pasteButton;
    }

    private AbstractButton getRedoButton()
    {
        if (m_redoButton == null)
        {
            m_redoButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_REDO));
        }
        return m_redoButton;
    }

    private AbstractButton getUndoButton()
    {
        if (m_undoButton == null)
        {
            m_undoButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_UNDO));
        }
        return m_undoButton;
    }

    private AbstractButton getGroupButton()
    {
        if (m_groupButton == null)
        {
            m_groupButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_GROUP));
        }
        return m_groupButton;
    }

    private AbstractButton getUngroupButton()
    {
        if (m_ungroupButton == null)
        {
            m_ungroupButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_UNGROUP));
        }
        return m_ungroupButton;
    }

    private AbstractButton getZoomInButton()
    {
        if (m_zoomInButton == null)
        {
            m_zoomInButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ZOOMIN));
        }
        return m_zoomInButton;
    }

    private AbstractButton getZoomOutButton()
    {
        if (m_zoomOutButton == null)
        {
            m_zoomOutButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ZOOMOUT));
        }
        return m_zoomOutButton;
    }

    private AbstractButton getPlaceButton()
    {
        if (m_placeButton == null)
        {
            m_placeButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_DRAWMODE_PLACE), true);
        }
        return m_placeButton;
    }

    private AbstractButton getTransitionButton()
    {
        if (m_transitionButton == null)
        {
            m_transitionButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_DRAWMODE_TRANSITION), true);
        }
        return m_transitionButton;
    }

    private AbstractButton getAndJoinButton()
    {
        if (m_andJoinButton == null)
        {
            m_andJoinButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_DRAWMODE_ANDJOIN), true);
        }
        return m_andJoinButton;
    }

    private AbstractButton getAndSplitButton()
    {
        if (m_andSplitButton == null)
        {
            m_andSplitButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_DRAWMODE_ANDSPLIT), true);
        }
        return m_andSplitButton;
    }

    private AbstractButton getXorJoinButton()
    {
        if (m_xorJoinButton == null)
        {
            m_xorJoinButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_DRAWMODE_XORJOIN), true);
        }
        return m_xorJoinButton;
    }

    private AbstractButton getXorSplitButton()
    {
        if (m_xorSplitButton == null)
        {
            m_xorSplitButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_DRAWMODE_XORSPLIT), true);
        }
        return m_xorSplitButton;
    }

    private AbstractButton getXorSplitJoinButton()
    {
        if (m_xorSplitJoinButton == null)
        {
            m_xorSplitJoinButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_DRAWMODE_XORSPLITJOIN), true);
        }
        return m_xorSplitJoinButton;
    }

    private AbstractButton getAndSplitJoinButton()
    {
        if (m_andSplitJoinButton == null)
        {
            m_andSplitJoinButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_DRAWMODE_ANDSPLITJOIN), true);
        }
        return m_andSplitJoinButton;
    }
    
    private AbstractButton getAndJoinXorSplitButton()
    {
        if (m_andJoinXorSplitButton == null)
        {
            m_andJoinXorSplitButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_DRAWMODE_ANDJOINXORSPLIT), true);
        }
        return m_andJoinXorSplitButton;
    }

    private AbstractButton getXorJoinAndSplitButton()
    {
        if (m_xorJoinAndSplitButton == null)
        {
        	m_xorJoinAndSplitButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_DRAWMODE_XORJOINANDSPLIT), true);
        }
        return m_xorJoinAndSplitButton;
    }
    
    // private AbstractButton getOrSplitButton()
    // {
    // if (m_orSplitButton == null)
    // {
    // m_transitionButton =
    // ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_DRAWMODE_TRANSITION),
    // true);
    // }
    // return m_orSplitButton;
    // }

    private AbstractButton getSubProcessButton()
    {
        if (m_subProcessButton == null)
        {
            m_subProcessButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_DRAWMODE_SUB), true);
        }
        return m_subProcessButton;
    }

    public AbstractButton getTokenGameButton()
    {
        if (m_tokenGameButton == null)
        {
            m_tokenGameButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_TOGGLE_TOKENGAME));
        }
        return m_tokenGameButton;
    }

    public AbstractButton getWoflanButton()
    {
        if (m_woflanButton == null)
        {
            m_woflanButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_WOFLAN), false);
        }
        return m_woflanButton;
    }
    
    public AbstractButton getAnalysisButton()
    {
    	if (m_analysisButton == null)
    	{
    		m_analysisButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_WOPED), false);
    	}
    	return m_analysisButton;
    }
    
    public AbstractButton getQuantCapButton()
    {
    	if (m_quantCapButton == null)
    	{
    		m_quantCapButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_QUANTCAP), false);
    	}
    	return m_quantCapButton;
    }

    public AbstractButton getQuantSimButton()
    {
    	if (m_quantSimButton == null)
    	{
    		m_quantSimButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_QUANTSIM), false);
    	}
    	return m_quantSimButton;
    }
    
    public AbstractButton getReachabilityGraphButton()
    {
    	if (m_reachabilityGraphButton == null)
    	{
    		m_reachabilityGraphButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_REACHGRAPH_START));
    	}
    	return m_reachabilityGraphButton;
    }

    public AbstractButton getColoringButton()
    {
    	if (m_coloringButton == null)
    	{
    		m_coloringButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_COLORING),true);
    	}
    	return m_coloringButton;
    }
    
    public AbstractButton getRotateViewButton()
    {
    	if (m_rotateViewButton == null)
    	{
    		m_rotateViewButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ROTATEVIEW),true);
    	}
    	return m_rotateViewButton;
    }
    
    public AbstractButton getRotateTransLeftButton()
    {
    	if (m_rotateTransLeftButton == null)
    	{
    		m_rotateTransLeftButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ROTATE_TRANS_LEFT),false);
    	}
    	return m_rotateTransLeftButton;
    }
    
    public AbstractButton getRotateTransRightButton()
    {
    	if (m_rotateTransRightButton == null)
    	{
    		m_rotateTransRightButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ROTATE_TRANS_RIGHT),false);
    	}
    	return m_rotateTransRightButton;
    }
    
	@Override
	public AbstractButton getMetricButton() {
		
		if(m_metricButton == null)
		{
			m_metricButton = ToolBarButton.createButton(ActionFactory.getStaticAction(ActionFactory.ACTIONID_METRIC),false);
		}
		return m_metricButton;
	}
 
    public void addViewListener(IViewListener listener)
    {
        viewListener.addElement(listener);
    }

    public String getId()
    {
        return id;
    }

    public void removeViewListener(IViewListener listenner)
    {
        viewListener.removeElement(listenner);
    }

    public int getViewControllerType()
    {
        return DefaultApplicationMediator.VIEWCONTROLLER_TOOLBAR;
    }

    /**
     * Fires a ViewEvent to each listener as long as the event is not consumed.
     * The event is also set with a reference to the current listener.
     */
    @SuppressWarnings("unchecked")
	public final void fireViewEvent(AbstractViewEvent viewevent)
    {
        if (viewevent == null) return;
        Vector<IViewListener> vector;
        synchronized (viewListener)
        {
            vector = (Vector<IViewListener>) viewListener.clone();
        }
        if (vector == null) return;
        int i = vector.size();
        for (int j = 0; !viewevent.isConsumed() && j < i; j++)
        {
            IViewListener viewlistener = (IViewListener) vector.elementAt(j);
            viewevent.setViewListener(viewlistener);
            viewlistener.viewEventPerformed(viewevent);
        }
    }

    /**
     * TODO: DOCUMENTATION (silenco)
     * 
     * @return
     */
    public JComboBox getZoomChooser()
    {
        if (zoomChooser == null)
        {
            String[] zoomFactors = { "25", "50", "75", "100", "150", "200" };
            zoomChooser = new JComboBox(zoomFactors);
            zoomChooser.setSelectedIndex(3);
            zoomChooser.setBorder(BorderFactory.createEtchedBorder());
            zoomChooser.setEditable(true);
            SwingUtils.setFixedWidth(zoomChooser, 50);
            zoomChooser.addItemListener(new ItemListener()
            {

                public void itemStateChanged(ItemEvent e)
                {
                    fireViewEvent(new ViewEvent(ToolBarVC.this, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.ZOOM_ABSOLUTE, zoomChooser.getSelectedItem()));
                }
            });
            VisualController.getInstance().addElement(zoomChooser, VisualController.WITH_EDITOR, VisualController.ALWAYS, VisualController.IGNORE);
        }

        return zoomChooser;
    }
    
    public void changeZoomChooserValueWithoutListeners(String value) {
        ItemListener[] listeners = zoomChooser.getItemListeners();
        for (int i=0;i<listeners.length;i++) {
            zoomChooser.removeItemListener(listeners[i]);
        }
        zoomChooser.setSelectedItem(value);
        for (int i=0;i<listeners.length;i++) {
            zoomChooser.addItemListener(listeners[i]);
        }
    }


}