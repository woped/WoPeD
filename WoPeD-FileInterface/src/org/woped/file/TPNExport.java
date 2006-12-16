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
 * For contact information please visit http://woped.ba-karlsruhe.de
 *
 */
package org.woped.file;

import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.PetriNetModelElement;
import org.woped.core.utilities.LoggerManager;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * Export to Woflan File.
 * 
 * 14.05.2003
 */
public class TPNExport
{

    public static boolean save(String fileName, PetriNetModelProcessor net2save)
    {

        try
        {
            LoggerManager.debug(Constants.FILE_LOGGER, "********** START TPN EXPORT **********");

            FileWriter fos = new FileWriter(new File(fileName));
            Iterator rootIter = net2save.getElementContainer().getRootElements().iterator();
            PetriNetModelElement currentModel;

            while (rootIter.hasNext())
            {

                currentModel = (PetriNetModelElement) rootIter.next();

                if (currentModel.getType() == PetriNetModelElement.PLACE_TYPE)
                {
                    fos.write("place #");
                    if (ConfigurationManager.getConfiguration().isTpnSaveElementAsName())
                    {
                        fos.write(currentModel.getNameValue().replaceAll("[[\\W]&&[\\S|\\s]]", "") + "_");
                    }
                    fos.write(currentModel.getId());

                    if (net2save.getElementContainer().getSourceElements(currentModel.getId()).size() == 0) fos.write(" init 1");
                    fos.write(";\n");

                } else if ((currentModel.getType() == PetriNetModelElement.TRANS_SIMPLE_TYPE)||
                		(currentModel.getType() == PetriNetModelElement.SUBP_TYPE))
                {
                    fos.write(getLine4Transition(currentModel.getNameValue(), currentModel, net2save.getElementContainer()));

                } else if (currentModel.getType() == PetriNetModelElement.TRANS_OPERATOR_TYPE)
                {

                    OperatorTransitionModel aalstModel = (OperatorTransitionModel) currentModel;
                    Iterator simpleTransIter = aalstModel.getSimpleTransContainer().getRootElements().iterator();
                    while (simpleTransIter.hasNext())
                    {

                        PetriNetModelElement simpleTransModel = (PetriNetModelElement) simpleTransIter.next();
                        if (aalstModel.getSimpleTransContainer().getElementById(simpleTransModel.getId()).getType() == PetriNetModelElement.TRANS_SIMPLE_TYPE)
                        {
                            fos.write(getLine4Transition(currentModel.getNameValue(), simpleTransModel, aalstModel.getSimpleTransContainer()));
                        }

                    }
                }
            }
            fos.flush();
            fos.close();
            LoggerManager.info(Constants.FILE_LOGGER, "File saved to: " + fileName);
            return true;
        } catch (Exception e)
        {
            e.printStackTrace();
            LoggerManager.error(Constants.FILE_LOGGER, "Could not export as TPN");
            return false;
        } finally
        {
            LoggerManager.debug(Constants.FILE_LOGGER, "*********** END TPN EXPORT ***********");
        }
    }

    private static String getLine4Transition(String name, PetriNetModelElement currentModel, ModelElementContainer container)
    {
        String line = null;
        Iterator tempIter;

        line = "trans #";
        if (ConfigurationManager.getConfiguration().isTpnSaveElementAsName())
        {
            line += name.replaceAll("[[\\W]&&[\\S|\\s]]", "") + "_";
        }
        line += currentModel.getId();


        // Input
        PetriNetModelElement tempPlace;
        line += " in";
        tempIter = container.getSourceElements(currentModel.getId()).keySet().iterator();
        while (tempIter.hasNext())
        {
            tempPlace = (PetriNetModelElement) container.getElementById(tempIter.next());
            line += " #";
            // Should given name be appended to the transition ID ?
            // If so, check whether such a name exists and add
            String nameValue = tempPlace.getNameValue();
            if ((ConfigurationManager.getConfiguration().isTpnSaveElementAsName())&&
            		(nameValue!=null))
            	line+=nameValue.replaceAll("[[\\W]&&[\\S|\\s]]", "") + "_";

            line += tempPlace.getId();
        }
        // Output

        line += " out";
        tempIter = container.getTargetElements(currentModel.getId()).keySet().iterator();
        while (tempIter.hasNext())
        {
            tempPlace = (PetriNetModelElement) container.getElementById(tempIter.next());
            line += " #";
            // Should given name be appended to the transition ID ?
            // If so, check whether such a name exists and add
            String nameValue = tempPlace.getNameValue();
            if ((ConfigurationManager.getConfiguration().isTpnSaveElementAsName())&&
            		(nameValue!=null))
            {
                line += nameValue.replaceAll("[[\\W]&&[\\S|\\s]]", "") + "_";
            }

            line += tempPlace.getId();
        }
        line += ";\n";

        return line;
    }

}