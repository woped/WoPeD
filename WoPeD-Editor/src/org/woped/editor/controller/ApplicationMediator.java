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
package org.woped.editor.controller;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.config.IGeneralConfiguration;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IEditor;
import org.woped.core.controller.IViewController;
import org.woped.core.gui.IEditorAware;
import org.woped.core.gui.IUserInterface;
import org.woped.core.model.petrinet.SubProcessModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.Constants;
import org.woped.editor.controller.vc.ConfigVC;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.editor.controller.vc.SubprocessEditorVC;
import org.woped.editor.controller.vc.TaskBarVC;
import org.woped.editor.controller.vep.ApplicationEventProcessor;
import org.woped.editor.controller.vep.EditorEventProcessor;
import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.structure.NetAlgorithms;
import org.woped.qualanalysis.structure.components.ArcConfiguration;
import org.woped.qualanalysis.understandability.NetColorScheme;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.util.Iterator;
import java.util.List;

/**
 * This Class should be the Mediator for the Editor VC... It must be implemented (inherit) by each GUI. The Mediator must register each VC... Actionperformig is
 * only run by messages. Each VC must implement the VC Interface in order to perform actions. The Method is calles by the Mediator... The actions must be an
 * AbstractWopedAction, which get the VC in the Constructor!
 * 
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 * 
 */
public class ApplicationMediator extends AbstractApplicationMediator {

    public static final int VIEWCONTROLLER_EDITOR = 0;
    public static final int VIEWCONTROLLER_TASKBAR = 1;
    public static final int VIEWCONTROLLER_CONFIG = 2;
    private int editorCounter = 0;
    private EditorClipboard clipboard = new EditorClipboard();
    private VisualController visualController = null;
    private int newEditorCounter = 0;
    private static JFrame displayUI = null;

    public ApplicationMediator() {
        this(null, null);
    }
    
    public static void setDisplayUI(JFrame js){
    	displayUI = js;
    }
    
    public static JFrame getDisplayUI(){
    	return displayUI;
    }

    public ApplicationMediator(IUserInterface ui, IGeneralConfiguration conf) {
        super(ui, conf);
        visualController = new VisualController(this);
        ActionFactory.createStaticActions(this);

        getVepController().register(AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, new ApplicationEventProcessor(this));
        getVepController().register(AbstractViewEvent.VIEWEVENTTYPE_EDIT,new EditorEventProcessor(this));

        clipboard.addClipboardListener(visualController);
    }

    @Override
    public void addViewController(IViewController viewController) {
        if (viewController != null) {
            // viewController.init();
            // viewController.setVisible(true);
            // viewController.setEnabled(true);
            switch (viewController.getViewControllerType()) {
            case VIEWCONTROLLER_EDITOR:
                editorCounter++;
                // if (getUi() != null) getUi().addEditor((EditorVC)
                // viewController);
                break;
            case VIEWCONTROLLER_TASKBAR:
                // only one
                break;
            default:
                break;
            }
            super.addViewController(viewController);
        }
    }
    
    public IEditor createEditor(boolean undoSupport){
    	return createEditor(undoSupport, true);
    }

    // ! Create a new editor window and register it with all visual controllers
    // ! @param undoSupport if set to true undo support is to be enabled
    // ! @return reference to new editor object
    @Override
    public IEditor createEditor(boolean undoSupport, boolean loadUI) {
        EditorVC editor = new EditorVC(EditorVC.ID_PREFIX + editorCounter, clipboard, undoSupport,
                this, loadUI);
        addViewController(editor);
        if(loadUI){
//        	editor.getGraph().addMouseListener(visualController);

	        editor.setName(Messages.getString("Document.Title.Untitled") + " - " + newEditorCounter++);
	        // notify the editor aware vc
	        Iterator<?> editorIter = getEditorAwareVCs().iterator();
	        while (editorIter.hasNext()) {
	            ((IEditorAware) editorIter.next()).addEditor(editor);
	        }
	        VisualController.getInstance()
	                .propertyChange(new PropertyChangeEvent(this, "InternalFrameCount", null, editor));
        }
        editor.setSaved(true);
        return editor;
    }
    
    
    public IEditor createTextEditor(boolean undoSupport){
    	return createTextEditor(undoSupport, true);
    }

    // ! Create a new editor window and register it with all visual controllers
    // ! @param undoSupport if set to true undo support is to be enabled
    // ! @return reference to new editor object
    @Override
    public IEditor createTextEditor(boolean undoSupport, boolean loadUI) {
        EditorVC editor = new EditorVC(EditorVC.ID_PREFIX + editorCounter, clipboard, undoSupport,
                this, loadUI);
        addViewController(editor);
        if(loadUI){
//        	editor.getGraph().addMouseListener(visualController);

	        editor.setName(Messages.getString("Document.Title.Untitled") + " - " + newEditorCounter++);
	        // notify the editor aware vc
	        Iterator<?> editorIter = getEditorAwareVCs().iterator();
	        while (editorIter.hasNext()) {
	            ((IEditorAware) editorIter.next()).addTextEditor(editor);
	        }
	        VisualController.getInstance()
	                .propertyChange(new PropertyChangeEvent(this, "InternalFrameCount", null, editor));
        }
        editor.setSaved(true);
        return editor;
    }

    // ! Create a sub-process editor window and register it with all visual controllers.
    // ! If a sub-process editor window already exists for the given sub-process return it.
    // ! @param modelProcessorType specifies the model processor type for the new editor
    // ! @param undoSupport if set to true undo support is to be enabled
    // ! @param parentEditor specifies the parent editor for this sub-process
    // ! @param subProcess specifies the sub process element to be edited
    // ! @return reference to new editor object
    @Override
    public IEditor createSubprocessEditor(boolean undoSupport, IEditor parentEditor,
            SubProcessModel subProcess) {
        IEditor editor = null;

        List<IEditor> editors = getUi().getAllEditors();
        Iterator<IEditor> editorIter = editors.iterator();

        while (editorIter.hasNext()) {
            IEditor currentEditor = editorIter.next();

            if (currentEditor.getName().equals("Subprocess " + subProcess.getNameValue())) {
                editor = currentEditor;
            }
        }

        if (editor == null) {
            // Editor doesn't already exist,
            // we need to create it

            // Subprozess darf momentan nur jeweils einen Ein- und
            // Ausgang haben
            ArcConfiguration arcConfig = new ArcConfiguration();
            NetAlgorithms.getArcConfiguration(subProcess, arcConfig);

            if ((arcConfig.m_numIncoming != 1) || (arcConfig.m_numOutgoing != 1)) {

                JOptionPane.showMessageDialog(null, Messages.getString("Editor.Message.Subprocess.Text"), Messages
                        .getString("Editor.Message.Subprocess.Title"), JOptionPane.ERROR_MESSAGE);

                return null;
            }
            
            editor = new SubprocessEditorVC(EditorVC.ID_PREFIX + editorCounter, clipboard, undoSupport,
                    parentEditor, subProcess, this);
            addViewController(editor);

            newEditorCounter++;
            // notify the editor aware vc
            editorIter = getEditorAwareVCs().iterator();
            while (editorIter.hasNext()) {
                ((IEditorAware) editorIter.next()).addEditor(editor);
            }
            VisualController.getInstance().propertyChange(
                    new PropertyChangeEvent(this, "InternalFrameCount", null, editor));

            // Update handle coloring if active
            if (ConfigurationManager.getConfiguration().getColorOn() == true) {
                new NetColorScheme().update();
            }
        }

        // Found a matching editor
        // Before we return it, give it the focus
        JComponent frame = ((EditorVC)editor).getEditorPanel().getContainer();
        if (frame instanceof JInternalFrame) {
            JInternalFrame internalFrame = (JInternalFrame) frame;
            try {
                internalFrame.setSelected(true);
            } catch (Exception e) {
            }
        }

        return editor;
    }

    public IViewController createViewController(int type) {
        IViewController vc = null;
        switch (type) {
        case VIEWCONTROLLER_TASKBAR:
            vc = new TaskBarVC(TaskBarVC.ID_PREFIX);
            break;
        case VIEWCONTROLLER_CONFIG:
            if (getUi() != null && getUi().getComponent() instanceof JFrame) {
                vc = new ConfigVC((JFrame) getUi(), true, ConfigVC.ID_PREFIX);
            } else {
                vc = new ConfigVC(true, ConfigVC.ID_PREFIX);
            }
            break;
        default:
            LoggerManager.warn(Constants.EDITOR_LOGGER, "No VC created.");
            break;
        }
        return vc;
    }

    public VisualController getVisualController() {
        return visualController;
    }

    /*
     * (non-Javadoc)
     * @see org.woped.core.controller.AbstractApplicationMediator#setUi(org.woped.core.gui.IUserInterface)
     */
    @Override
    public void setUi(IUserInterface ui) {
        if (ui != null) {
            PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(ui.getPropertyChangeSupportBean());
            propertyChangeSupport.addPropertyChangeListener(VisualController.getInstance());
        }
        super.setUi(ui);
    }
}
