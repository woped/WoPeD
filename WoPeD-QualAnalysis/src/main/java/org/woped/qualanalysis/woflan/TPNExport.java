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
package org.woped.qualanalysis.woflan;

import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.utilities.LoggerManager;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 * <br>
 * 
 *         Export to Woflan File.
 * 
 *         14.05.2003
 */
public class TPNExport {

    private static String TPN_Logger = "TPNFILE_LOGGER";

    public static boolean save(String fileName, PetriNetModelProcessor net2save) {

        try {
            LoggerManager.debug(TPN_Logger, "********** START TPN EXPORT **********");

            FileWriter fos = new FileWriter(new File(fileName));
            Iterator<AbstractPetriNetElementModel> rootIter = net2save.getElementContainer().getRootElements().iterator();
            AbstractPetriNetElementModel currentModel;

            while (rootIter.hasNext()) {

                currentModel = (AbstractPetriNetElementModel) rootIter.next();

                if (currentModel.getType() == AbstractPetriNetElementModel.PLACE_TYPE) {
                    fos.write("place #");
                    if (ConfigurationManager.getConfiguration().isTpnSaveElementAsName()) {
                        String nameValue = currentModel.getNameValue();
                        if (nameValue != null) {
                            fos.write(nameValue.replaceAll("[[\\W]&&[\\S|\\s]]", "") + "_");
                        }
                    }
                    fos.write(currentModel.getId());

                    int numtoken = ((PlaceModel) currentModel).getTokenCount();
                    if (numtoken > 0) {
                        fos.write(" init " + numtoken);
                    }

                    fos.write(";\n");

                } else
                    if ((currentModel.getType() == AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE)
                            || (currentModel.getType() == AbstractPetriNetElementModel.SUBP_TYPE)) {
                        fos.write(getLine4Transition(currentModel.getNameValue(), currentModel, net2save
                                .getElementContainer()));

                    } else
                        if (currentModel.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE) {

                            OperatorTransitionModel aalstModel = (OperatorTransitionModel) currentModel;
                            Iterator<AbstractPetriNetElementModel> simpleTransIter = aalstModel.getSimpleTransContainer().getRootElements()
                                    .iterator();
                            while (simpleTransIter.hasNext()) {

                                AbstractPetriNetElementModel simpleTransModel = (AbstractPetriNetElementModel) simpleTransIter.next();
                                if (aalstModel.getSimpleTransContainer().getElementById(simpleTransModel.getId())
                                        .getType() == AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE) {
                                    fos.write(getLine4Transition(currentModel.getNameValue(), simpleTransModel,
                                            aalstModel.getSimpleTransContainer()));
                                }

                            }
                        }
            }
            fos.flush();
            fos.close();
            LoggerManager.info(TPN_Logger, "File saved to: " + fileName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LoggerManager.error(TPN_Logger, "Could not export as TPN");
            return false;
        } finally {
            LoggerManager.debug(TPN_Logger, "*********** END TPN EXPORT ***********");
        }
    }

    private static String getLine4Transition(String name, AbstractPetriNetElementModel currentModel,
            ModelElementContainer container) {
        String line = null;
        Iterator<String> tempIter;

        line = "trans #";
        if (!ConfigurationManager.getConfiguration().isTpnSaveElementAsName()) {
            line += name.replaceAll("[[\\W]&&[\\S|\\s]]", "") + "_";
        }
        line += currentModel.getId();

        // Input
        AbstractPetriNetElementModel tempPlace;
        line += " in";
        tempIter = container.getSourceElements(currentModel.getId()).keySet().iterator();
        while (tempIter.hasNext()) {
            tempPlace = (AbstractPetriNetElementModel) container.getElementById(tempIter.next());
            line += " #";
            // Should given name be appended to the transition ID ?
            // If so, check whether such a name exists and add
            String nameValue = tempPlace.getNameValue();
            if ((ConfigurationManager.getConfiguration().isTpnSaveElementAsName()) && (nameValue != null)) {
                line += nameValue.replaceAll("[[\\W]&&[\\S|\\s]]", "") + "_";
            }

            line += tempPlace.getId();
        }
        // Output

        line += " out";
        tempIter = container.getTargetElements(currentModel.getId()).keySet().iterator();
        while (tempIter.hasNext()) {
            tempPlace = (AbstractPetriNetElementModel) container.getElementById(tempIter.next());
            line += " #";
            // Should given name be appended to the transition ID ?
            // If so, check whether such a name exists and add
            String nameValue = tempPlace.getNameValue();
            if ((ConfigurationManager.getConfiguration().isTpnSaveElementAsName()) && (nameValue != null)) {
                line += nameValue.replaceAll("[[\\W]&&[\\S|\\s]]", "") + "_";
            }

            line += tempPlace.getId();
        }
        line += ";\n";

        return line;
    }

}