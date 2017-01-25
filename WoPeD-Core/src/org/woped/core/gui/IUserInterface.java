package org.woped.core.gui;

import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.IEditor;

import java.awt.*;
import java.util.List;


public interface IUserInterface extends IEditorAware
{
    public void hideEditor(IEditor editor);

    public IEditor getEditorFocus();

    public List<IEditor> getAllEditors();

    public void quit();

    public Rectangle getBounds();

    public Component getComponent();

    public Component getPropertyChangeSupportBean();

    public void cascadeFrames();

    public void arrangeFrames();
    
    public void refreshFocusOnFrames();

    /* * some component methods * */

    public int getX();

    public int getY();

    public Dimension getSize();
    
    public boolean isMaximized();

    public void setVisible(boolean visible);
    
    public void updateRecentMenu();

    public Container getContentPane();

	public void initialize(AbstractApplicationMediator mediator);
	
}
