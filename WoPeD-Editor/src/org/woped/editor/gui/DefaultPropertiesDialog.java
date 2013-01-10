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
 * Created on Oct 14, 2004
 *
 */
package org.woped.editor.gui;

import java.util.Hashtable;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;

import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.GroupModel;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.TransitionModel;

/**
 * TODO: DOCUMENTATION (xraven)
 * 
 * @author Thomas Pohl
 */

@SuppressWarnings("serial")
public class DefaultPropertiesDialog extends JDialog implements IEditorProperties
{

    private Object[]            	m_selection;
    private Hashtable<?, ?>         m_parameters;
    private static final String 	TOKENS = "Tokens";

    /**
     * TODO: DOCUMENTATION (xraven)
     * 
     * @param selection
     * @throws IllegalArgumentException
     */
    public DefaultPropertiesDialog(JFrame owner)
    {
        super(owner, true);
        setUndecorated(true);
    }

    public DefaultPropertiesDialog()
    {
        setUndecorated(true);
    }

    public void show(AbstractPetriNetElementModel element)
    {
    // TODO: !
    }

    /**
     * TODO: DOCUMENTATION (xraven)
     * 
     * @throws NumberFormatException
     */
    public void save() throws NumberFormatException
    {
        if (m_selection != null)
        {
            if (m_selection.length == 1)
            {
                if (m_selection[0] instanceof GroupModel)
                {
                    m_selection[0] = ((GroupModel) m_selection[0]).getMainElement();
                }
                if (m_selection[0] instanceof AbstractPetriNetElementModel)
                {
                    AbstractPetriNetElementModel element = (AbstractPetriNetElementModel) m_selection[0];

                    if (element instanceof PlaceModel)
                    {
                        //logger.info("Changing PlaceModel");
                        // OLDUserInterface.getInstance().getActiveEditor().getGraph().getGraphLayoutCache().valueForCellChanged(((PlaceModel)element).getNameModel(),
                        // ((JTextField) m_parameters.get(NAME)).getText());
                        // ((PlaceModel) element).setNameValue(((JTextField)
                        // m_parameters.get(NAME)).getText());
                        ((PlaceModel) element).setTokens(Integer.parseInt(((JTextField) m_parameters.get(TOKENS)).getText()));
                    } else if (element instanceof TransitionModel)
                    {
                        // OLDUserInterface.getInstance().getActiveEditor().getGraph().getGraphLayoutCache().valueForCellChanged(((TransitionModel)element).getNameModel(),
                        // ((JTextField) m_parameters.get(NAME)).getText());
                        // ((TransitionModel)
                        // element).setNameValue(((JTextField)
                        // m_parameters.get(NAME)).getText());
                    }
                }
            }
        }
    }

}