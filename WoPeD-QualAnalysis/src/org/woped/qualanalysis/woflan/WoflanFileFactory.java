package org.woped.qualanalysis.woflan;

import java.io.File;

import javax.swing.JOptionPane;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.IEditor;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.gui.translations.Messages;

public class WoflanFileFactory {
    /**
     * 
     * @param editor export the provided editor.
     * @return the created export File, on error null will returned.
     */
    public File createFile(IEditor editor) {
        boolean bExportSuccessful;
        File createdFile;
        String fileDir = ConfigurationManager.getConfiguration().getHomedir() + "tempWoflanAnalyse.tpn";

        bExportSuccessful = TPNExport.save(fileDir, (PetriNetModelProcessor) editor.getModelProcessor());

        if (bExportSuccessful) {
            createdFile = new File(fileDir);
            if (createdFile.exists()) {
                return createdFile;

            } else {
                String arg[] = { fileDir };
                JOptionPane.showMessageDialog(null, Messages.getString("File.Error.Woflan.Text", arg), Messages
                        .getString("File.Error.Woflan.Title"), JOptionPane.WARNING_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(null, Messages.getString("File.Error.NoNet.Text"), Messages
                    .getString("File.Error.NoNet.Title"), JOptionPane.WARNING_MESSAGE);
        }
        return null;

    }
}
