/*
 * Created on 21.10.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.woped.gui.controller.vep;

import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractEventProcessor;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.gui.AboutUI;
import org.woped.gui.BugReportUI;
import org.woped.gui.Constants;
import org.woped.gui.controller.DefaultApplicationMediator;
import org.woped.gui.controller.vc.MenuBarVC;
import org.woped.gui.controller.vc.ToolBarVC;
import org.woped.gui.help.HelpBrowser;

/**
 * @author lai
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class GUIViewEventProcessor extends AbstractEventProcessor
{
    private DefaultApplicationMediator mediator = null;

    public GUIViewEventProcessor(int vepID, DefaultApplicationMediator mediator)
    {
        super(vepID, mediator);
        this.mediator = mediator;
    }

    public void processViewEvent(AbstractViewEvent event)
    {
        EditorVC editor;
        if (event.getSource() instanceof EditorVC)
        {
            editor = (EditorVC) event.getSource();
        } else
        {
            editor = (EditorVC) getMediator().getUi().getEditorFocus();
        }
        switch (event.getOrder())
        {
        case AbstractViewEvent.ABOUT:
            AboutUI about = null;
            if (getMediator().getUi() != null && getMediator().getUi().getComponent() instanceof JFrame)
            {
                about = new AboutUI((JFrame) getMediator().getUi());
            } else
            {
                about = new AboutUI();
            }
            about.setVisible(true);
            break;
        case AbstractViewEvent.BUGREPORT:
            BugReportUI bugreport = null;
            if (getMediator().getUi() != null && getMediator().getUi().getComponent() instanceof JFrame)
            {
                bugreport = new BugReportUI((JFrame) getMediator().getUi());
            } else
            {
                bugreport = new BugReportUI();
            }
            bugreport.setVisible(true);
            break;
        case AbstractViewEvent.HELP:
            try
            {
                showHelp((String) event.getData(), false);
            } catch (Exception e)
            {
                LoggerManager.error(Constants.GUI_LOGGER, "Cannot find HTML manual files. " + e.getMessage());
                JOptionPane.showMessageDialog(getMediator().getUi().getComponent(), "Cannot find HTML manual files", "Not found", JOptionPane.ERROR_MESSAGE);
            }
            break;
        case AbstractViewEvent.HELP_CONTENTS:
            try
            {
                showHelp(null, true);
            } catch (Exception e)
            {
                LoggerManager.error(Constants.GUI_LOGGER, "Cannot find HTML contents file. " + e.getMessage());
                JOptionPane.showMessageDialog(getMediator().getUi().getComponent(), "Cannot find HTML contents file", "Not found", JOptionPane.ERROR_MESSAGE);
            }
            break;

        case AbstractViewEvent.UPDATE:
            ToolBarVC toolbar = (ToolBarVC) getMediator().getViewController(ToolBarVC.ID_PREFIX);
            if (toolbar != null)
            {
                toolbar.getWoflanButton().setEnabled(ConfigurationManager.getConfiguration().isUseWoflan());
            }
            MenuBarVC menu = (MenuBarVC) getMediator().getViewController(ToolBarVC.ID_PREFIX);
            if (menu != null)
            {
                menu.getWoflanMenuItem().setEnabled(ConfigurationManager.getConfiguration().isUseWoflan());
            }
            // TODO: fire update event for editor
            break;
        case AbstractViewEvent.CLOSE:

            if (!checkSave(editor))
            {

            }
            // TODO: update MenuVC
            break;
        }
    }

    /**
     * Trys to save the petrinet in a specific Editor.
     * 
     * @param editor
     * @return returns wether the net could be saved.
     */
    private boolean checkSave(EditorVC editor)
    {

        boolean canceled = false;
        if (!editor.isSaved())
        {
            int value = JOptionPane.showConfirmDialog(editor, "Save changes to " + editor.getFileName() + "?", "Confirm Save Current File", JOptionPane.YES_NO_CANCEL_OPTION);
            if (value == (JOptionPane.YES_OPTION))
            {
                // TODO: !! if (editor.getDefaultFileType() !=
                // FileFilterImpl.SAMPLEFilter) canceled = !save(editor);
                // else canceled = !saveAs(editor);
            } else if (value == JOptionPane.NO_OPTION)
            {
                // do nothing
            } else
            {
                canceled = true;
            }
        }
        return canceled;
    }

    private void showHelp(String contentFileName, boolean showContents) throws Exception
    {
        String filename = "index.htm";
        if (contentFileName == null)
        {
            contentFileName = "contents.htm";
        }
        URL url = this.getClass().getResource("/doc");

        if (url != null)
        {
            // locate HTML files in jarfile
            filename = url.toExternalForm().concat("/html/").concat(filename);
            contentFileName = url.toExternalForm().concat("/html/").concat(contentFileName);
        } else
        {
            // locate HTML files in local folder
            filename = this.getClass().getResource("/").toExternalForm() + "../doc/html/".concat(filename);
            contentFileName = this.getClass().getResource("/").toExternalForm() + "../doc/html/".concat(contentFileName);
        }

        HelpBrowser br = HelpBrowser.getInstance();
        if (showContents)
        {
            br.init(filename, filename, contentFileName);
        } else
        {
            br.init(filename, contentFileName, filename);
        }
    }
}
