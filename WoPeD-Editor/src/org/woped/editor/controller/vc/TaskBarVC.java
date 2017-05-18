package org.woped.editor.controller.vc;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IEditor;
import org.woped.core.controller.IViewController;
import org.woped.core.controller.IViewListener;
import org.woped.core.gui.IEditorAware;
import org.woped.editor.action.WoPeDAction;
import org.woped.editor.controller.ActionFactory;
import org.woped.editor.controller.ApplicationMediator;

/**
 * @author ABBASA
 *
 */
@SuppressWarnings("serial")
public class TaskBarVC extends JPanel implements IViewController, IEditorAware {

	private String id = null;
	private Vector<IViewListener> viewListener = new Vector<IViewListener>(1, 3);
	public static final String ID_PREFIX = "TASKBAR_VC";

	private IEditor selectedEditor = null;
	private HashMap<IEditor, JToggleButton> actions = new HashMap<IEditor, JToggleButton>();

	public TaskBarVC(String id) {
		this.id = id;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		// setBorder(new BevelBorder(BevelBorder.LOWERED));
	}

	/* (non-Javadoc)
	 * @see org.woped.core.gui.IEditorAware#renameEditor(org.woped.core.controller.IEditor)
	 * this method change label of the toggle button when editor saved under anthoer name
	 */
	public void renameEditor(IEditor editor) {
		JToggleButton taskButton = (JToggleButton) actions.get(editor);
		if (taskButton != null) {
			taskButton.setText(editor.getName());
		}
	}

	
	/* (non-Javadoc)
	 * @see org.woped.core.gui.IEditorAware#selectEditor(org.woped.core.controller.IEditor)
	 * identify editor to be selected
	 * setSelected of all other editor to false
	 * setSelected of the current editor to true 
	 */
	public void selectEditor(IEditor editor) {
		
		IEditor oldEditor = getSelectedEditor();
		if (oldEditor != null)
			oldEditor.getGraph().clearSelection();
		if (actions.get(editor) != null) {
			selectedEditor = editor;
			for (Iterator<JToggleButton> iter = actions.values().iterator(); iter
					.hasNext();) {
				iter.next().setSelected(false);
			}
			((JToggleButton) actions.get(editor)).setSelected(true);
		}
	}
	
	class ClosePopupMenu extends JPopupMenu {
		public ClosePopupMenu() {
          	String action_id = ActionFactory.ACTIONID_CLOSE;
        	WoPeDAction action = ActionFactory.getStaticAction(action_id);
            add(new JMenuItem(action));
            pack();
		}
	}
	
	class ClosePopupListener extends MouseAdapter {
		private Component parent = null;
		
		ClosePopupListener(Component parent) {
			this.parent = parent;
		}
		public void mousePressed(MouseEvent e) {  
            if (SwingUtilities.isRightMouseButton(e)) {  
                ClosePopupMenu cpm = new ClosePopupMenu();
                cpm.show(parent, parent.getX(), parent.getY()-25);
            }
		}
    }

	/* (non-Javadoc)
	 * @see org.woped.core.gui.IEditorAware#addEditor(org.woped.core.controller.IEditor)
	 * add new Togglebutton and connect it to the new editor   
	 * 
	 * add new element editor and button to actions 
	 * select the editor 
	 */
	public void addEditor(IEditor editor) {
		JToggleButton button = new JToggleButton(ActionFactory.getSelectEditorAction(editor));
		actions.put(editor, button);
		this.add(button);
		button.addMouseListener(new ClosePopupListener(button));
		
		selectEditor(editor);
	}
	
	/* (non-Javadoc)
	 * @see org.woped.core.gui.IEditorAware#removeEditor(org.woped.core.controller.IEditor)
	 * remove togglebutton 
	 * remove editor
	 * validate
	 * set selectedEditor to null
	 * repaint
	 */
	public void removeEditor(IEditor editor) {
		if (actions.get(editor) != null)
			remove((JToggleButton) actions.get(editor));
		actions.remove(editor);
		validate();
		selectedEditor = null;
		repaint();
	}

	/* (non-Javadoc)
	 * @see org.woped.core.controller.IViewController#addViewListener(org.woped.core.controller.IViewListener)
	 * add element to viewerListeners
	 */
	public void addViewListener(IViewListener listener) {
		viewListener.addElement(listener);
	}

	/* (non-Javadoc)
	 * @see org.woped.core.controller.IViewController#getId()
	 * getter
	 */
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.woped.core.controller.IViewController#removeViewListener(org.woped.core.controller.IViewListener)
	 * remove element from viewerListener 
	 */
	public void removeViewListener(IViewListener listenner) {
		viewListener.removeElement(listenner);
	}

	/* (non-Javadoc)
	 * @see org.woped.core.controller.IViewController#getViewControllerType()
	 * Return ApApplicationMediator.VIEWCONTROLLER_TASKBAR ?!?!?! 
	 */

	public int getViewControllerType() {
		return ApplicationMediator.VIEWCONTROLLER_TASKBAR;
	}

	/**
	 * Fires a ViewEvent to each listener as long as the event is not consumed.
	 * The event is also set with a reference to the current listener.
	 * If the viewEvent not null
	 * Clone viewListener to a vector
	 * If the vector not null
	 * To Test is to check the viewEvent 
	 */
     public final void fireViewEvent(AbstractViewEvent viewevent) {
		if (viewevent == null)
			return;
		Vector<IViewListener> vector;
		synchronized (viewListener) {
			vector = (Vector<IViewListener>) viewListener.clone();
		}
		if (vector == null)
			return;
		int i = vector.size();
		for (int j = 0; !viewevent.isConsumed() && j < i; j++) {
			IViewListener viewlistener = (IViewListener) vector.elementAt(j);
			viewevent.setViewListener(viewlistener);
			viewlistener.viewEventPerformed(viewevent);
		}
	}

	/**
	 * @return
	 * Return selected editor 
	 */
	public IEditor getSelectedEditor() {
		return selectedEditor;
	}

	@Override
	public void addTextEditor(IEditor editor) {
		// TODO Auto-generated method stub
		
	}
}

