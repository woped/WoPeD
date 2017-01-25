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
package org.woped.editor.action;

import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.ViewEvent;
import org.woped.gui.translations.Messages;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * @author Thomas Pohl
 *         <p>
 *         <p>
 *         13.10.2003
 */

@SuppressWarnings("serial")
public class WoPeDAction extends AbstractAction {

    private boolean m_selected = false;
    private AbstractApplicationMediator am = null;
    private int type = -1;
    private int order = -1;
    private Object data = null;
    private ArrayList<JComponent> target = new ArrayList<JComponent>();

    public WoPeDAction(String propertiesPrefix) {
        this(null, -1, -1, null, propertiesPrefix);
    }

    public WoPeDAction(String propertiesPrefix, Object[] args) {
        this(null, -1, -1, null, propertiesPrefix, args);
    }

    public WoPeDAction(AbstractApplicationMediator am, String name, Icon icon) {
        super(name, icon);
        this.am = am;
    }

    public WoPeDAction(AbstractApplicationMediator am, int type, int order) {
        this(am, type, order, null, null);
    }

    public WoPeDAction(AbstractApplicationMediator am, int type, int order, Object data) {
        this(am, type, order, data, null);
    }

    public WoPeDAction(AbstractApplicationMediator am, int type, int order, Object data, String propertiesPrefix) {
        this(am, type, order, data, propertiesPrefix, null);
    }

    public WoPeDAction(AbstractApplicationMediator am, int type, int order, Object data, String propertiesPrefix, Object[] args) {
        super();
        this.type = type;
        this.order = order;
        this.am = am;
        this.data = data;

        if (propertiesPrefix != null) {
            putValue(NAME, Messages.getTitle(propertiesPrefix, args));
            putValue(SMALL_ICON, Messages.getImageIcon(propertiesPrefix));
            putValue(MNEMONIC_KEY, new Integer(Messages.getMnemonic(propertiesPrefix)));
            putValue(ACCELERATOR_KEY, Messages.getShortcut(propertiesPrefix));
            putValue(SHORT_DESCRIPTION, Messages.getTitle(propertiesPrefix, args));
        }
    }

    public boolean isSelected() {
        return m_selected;
    }

    public void actionPerformed(final ActionEvent e) {

        if (am == null) return;

        if (e instanceof AbstractViewEvent) {
            AbstractViewEvent event = (AbstractViewEvent) e;
            this.data = event.getData();
        }

        am.viewEventPerformed(new EditorViewEvent(e.getSource(), type, order, data));
    }

    public void triggerAction(Object source){
        triggerAction(source, null);
    }

    public void triggerAction(Object source, Object data){
        this.actionPerformed(new ViewEvent(source, type, order, data));
    }

    public Object getData() {
        return data;
    }

    public void setData(Object newValue) {
        data = newValue;
    }

    public void addTarget(JComponent target) {
        this.target.add(target);

    }

    public ArrayList<JComponent> getTarget() {
        return target;
    }


}