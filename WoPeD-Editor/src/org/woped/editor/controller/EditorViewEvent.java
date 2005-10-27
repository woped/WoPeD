
package org.woped.editor.controller;

import org.woped.core.controller.AbstractViewEvent;

/*
 * Created on 21.10.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author lai
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class EditorViewEvent extends AbstractViewEvent
{

    /**
     * @param source
     * @param type
     * @param order
     */
    public EditorViewEvent(Object source, int type, int order)
    {
        super(source, type, order);
    }

    /**
     * @param source
     * @param type
     * @param order
     * @param data
     */
    public EditorViewEvent(Object source, int type, int order, Object data)
    {
        super(source, type, order, data);
    }

}
