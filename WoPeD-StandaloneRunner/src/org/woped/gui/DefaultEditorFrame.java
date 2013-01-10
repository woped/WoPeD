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
 * Created on Oct 5, 2004
 */
package org.woped.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyVetoException;
import java.util.Iterator;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;

import org.woped.bpel.gui.EditorData;
import org.woped.bpel.gui.EditorOperations;
import org.woped.core.controller.IEditor;
import org.woped.core.gui.IEditorFrame;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.editor.controller.PetriNetResourceEditor;
import org.woped.editor.controller.vc.EditorPanel;
import org.woped.editor.controller.vc.EditorStatusBarVC;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.translations.Messages;

import org.woped.qualanalysis.paraphrasing.view.ParaphrasingPanel;


/**
 * The EditorFrame is the container for each <code>Editor</code>.
 * 
 * @author Thomas Pohl
 */
@SuppressWarnings("serial")
public class DefaultEditorFrame extends JInternalFrame implements IEditorFrame
{
    private EditorVC               m_editor                 = null;
    private JScrollPane 		   m_resourcePanel			= null;
    private EditorPanel			   m_editorPanel		    = null;
    private PetriNetResourceEditor m_petriNetResourceEditor = null;
    private EditorStatusBarVC      m_statusBar              = null;
    private EditorOperations       m_operationsPanel		= null;
    private ParaphrasingPanel	   m_paraPhrasingPanel		= null;
    private JTabbedPane            tabbedPane               = null;

    public DefaultEditorFrame(EditorVC editor, EditorOperations opEditor, EditorData dEditor, PetriNetResourceEditor resEditor)
    {          
        super(editor.getName(), true, true, true, true);
        this.setVisible(false);
        m_editor = editor;
        this.getContentPane().add(getStatusBar(), BorderLayout.SOUTH);
        m_petriNetResourceEditor = resEditor;
    	addInternalFrameListener(m_editor);
    	m_operationsPanel = opEditor;
        m_editorPanel = m_editor.getEditorPanel();
        m_paraPhrasingPanel = new ParaphrasingPanel(m_editor);
        this.setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);

        if (editor.isSubprocessEditor()) {
            this.setFrameIcon(Messages.getImageIcon("Popup.Add.Subprocess"));
            this.getContentPane().add(m_editor.getEditorPanel(), BorderLayout.CENTER);
        } else {
            this.setFrameIcon(Messages.getImageIcon("Document"));
            
            // TabbedPane
            m_resourcePanel = new JScrollPane(getPetriNetResourceEditor());
            tabbedPane = new JTabbedPane();
            tabbedPane.addTab(Messages.getString("PetriNet.Process.Title"), m_editorPanel);
            tabbedPane.addTab(Messages.getString("PetriNet.Resources.Title"), m_resourcePanel);
            tabbedPane.addTab(Messages.getString("PetriNet.Operations.Title"), m_operationsPanel);
            tabbedPane.addTab(Messages.getString("Paraphrasing.Description"), m_paraPhrasingPanel);   
            tabbedPane.getModel().addChangeListener(new ChangeListener()
            {
            	public void stateChanged(ChangeEvent e)
            	{
            		getPetriNetResourceEditor().reset();
            		
            	}
            	
            		});
            
            this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
            if (resEditor != null)
            {
        	m_resourcePanel.addFocusListener(new FocusListener()
        	{
        	    public void focusGained(FocusEvent e)
        	    {
        		getPetriNetResourceEditor().reset();
        	    }
        	    
        	    public void focusLost(FocusEvent e)
        	    {
        		;
        	    }
        	});
        	m_resourcePanel.addFocusListener(new FocusListener()
        	{
        	    public void focusGained(FocusEvent e)
        	    {

        		getPetriNetResourceEditor().reset();
        	    }
        	    
        	    public void focusLost(FocusEvent e)
        	    {
        		;
        	    }
        	});
        	m_resourcePanel.addFocusListener(new FocusListener()
        	{
        	    public void focusGained(FocusEvent e)
        	    {
        		getPetriNetResourceEditor().reset();
        	    }
        	    
        	    public void focusLost(FocusEvent e)
        	    {
        		;
        	    }
        	});
            } else
            {
        	this.getContentPane().add(m_editor.getEditorPanel(), BorderLayout.CENTER);
            }
        }
        
        setParaphrasingListeners();
                
        setTitle(m_editor.getName());
                       
        this.pack();        
        if (!editor.isSubprocessEditor()) {
        	this.setSize(800,600);
        }
        this.repaint();
        this.setVisible(true);
    }
    
	// add listener to paraphrasing tool
	void setParaphrasingListeners() {
		if (m_paraPhrasingPanel.getParaphrasingOutput().getTable() != null) {
			m_paraPhrasingPanel.getParaphrasingOutput().addListeners();

			// if the table has no entries, load values from element container
			if (m_paraPhrasingPanel.getParaphrasingOutput()
					.getDefaultTableModel().getRowCount() == 0) {
				for (int x = 0; x < m_editor.getModelProcessor()
						.getElementContainer().getParaphrasingModel()
						.getTableSize(); x++) {
					int elements = 0;
					// check ids if they are in the diagram
					String[] content = m_editor.getModelProcessor()
							.getElementContainer().getParaphrasingModel()
							.getElementByRow(x)[0].split(",");
					String ids = "";
					Iterator<AbstractPetriNetElementModel> i = m_editor
							.getModelProcessor().getElementContainer()
							.getRootElements().iterator();
					while (i.hasNext()) {
						AbstractPetriNetElementModel cur = (AbstractPetriNetElementModel) i
								.next();

						if (cur.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE) {
							OperatorTransitionModel operatorModel = (OperatorTransitionModel) cur;
							Iterator<AbstractPetriNetElementModel> simpleTransIter = operatorModel
									.getSimpleTransContainer()
									.getElementsByType(
											AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE)
									.values().iterator();
							while (simpleTransIter.hasNext()) {
								AbstractPetriNetElementModel simpleTransModel = (AbstractPetriNetElementModel) simpleTransIter
										.next();
								if (simpleTransModel != null
										&& operatorModel
												.getSimpleTransContainer()
												.getElementById(
														simpleTransModel
																.getId())
												.getType() == AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE) {
									for (String v : content) {
										if (v.equals(simpleTransModel.getId())) {
											ids = ids + v + ",";
											elements++;
										}
									}
								}

							}
						} else {
							for (String v : content) {
								if (v.equals(cur.getId())) {
									ids = ids + v + ",";
									elements++;
								}
							}
						}

					}

					if (elements > 0) {
						ids = ids.substring(0, ids.length() - 1);
						String[] test = {
								ids,
								m_editor.getModelProcessor()
										.getElementContainer()
										.getParaphrasingModel()
										.getElementByRow(x)[1] };
						m_paraPhrasingPanel.getParaphrasingOutput()
								.addRow(test);
					}

				}
			}
		}
	}

    private EditorStatusBarVC getStatusBar()
    {
        if (m_statusBar == null)
        {
           m_statusBar = new EditorStatusBarVC(m_editor);
        }
        
        return m_statusBar;
    }
    

    /**
     * 
     * @return returns the containing Editor.
     */
    public IEditor getEditor()
    {
        return m_editor;
    }

    /**
     * @return Returns the m_petriNetResourceEditor.
     */

    public PetriNetResourceEditor getPetriNetResourceEditor()
    {
        return m_petriNetResourceEditor;
    }
	/**
	 * When a Frame isIconified it should be invisible.
	 */
	public void setIcon(boolean arg0) throws PropertyVetoException
    {
        super.setIcon(arg0);
        setVisible(!arg0);
    }
	
	/**
     * 
     * get the process tab
     */
    public JTabbedPane getProcessTab()
    {
        return tabbedPane;
    }
    
	//! Enable or disable the processing of all mouse events
	//! (also for all child components)
	//! @param if true, mouse events are accepted (default)
	//!        if false, mouse events are disabled
	public void acceptMouseEvents(boolean accept)
	{
        this.setClosable(accept);
        this.setResizable(accept);
        
		if (accept==false)
		{
	        glass.setOpaque(false);
	     
	        glass.addMouseListener(mouseGrabber);
	        glass.addMouseMotionListener(mouseGrabber);
	        glass.setVisible(true);
	        // Change glass pane to our panel
	        if (old==null)
	        	old = getRootPane().getGlassPane();
	        getRootPane().setGlassPane(glass);	        
		}
		else
		{
			glass.removeMouseListener(mouseGrabber);
			glass.removeMouseMotionListener(mouseGrabber);
			glass.setVisible(false);
			
			if (old!=null)
			{
				getRootPane().setGlassPane(old);
				old = null;
			}
		}
	}

	@Override
	public void setBounds(int p_arg0, int p_arg1, int p_arg2, int p_arg3)
	{
		super.setBounds(p_arg0, p_arg1, p_arg2, p_arg3);
		if(m_editor.getEditorPanel().isAutomaticResize()){
			m_editor.getEditorPanel().setAutomaticResize(false);
			return;
		}
		// editor resized -> set dirty
		m_editor.setSaved(false);
	}



	private JPanel glass = new JPanel();
	private Component old = null;
	private MouseInputAdapter mouseGrabber = new MouseInputAdapter() {};

}