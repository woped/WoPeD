package org.woped.file.controller.vep;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessControlException;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractEventProcessor;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IEditor;
import org.woped.core.controller.IStatusBar;
import org.woped.core.controller.IViewController;
import org.woped.core.gui.IEditorAware;
import org.woped.core.model.AbstractModelProcessor;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.utilities.LoggerManager;
import org.woped.core.utilities.Utils;
import org.woped.editor.controller.ApplicationMediator;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.editor.utilities.FileFilterImpl;
import org.woped.editor.utilities.Messages;
import org.woped.file.Constants;
import org.woped.file.JPGExport;
import org.woped.file.OLDPNMLImport2;
import org.woped.file.PNMLExport;
import org.woped.file.PNMLImport;
import org.woped.file.TPNExport;
import org.woped.woflan.NetAnalysisDialog;

public class FileEventProcessor extends AbstractEventProcessor
{
    public FileEventProcessor(int vepID, ApplicationMediator mediator)
    {
        super(vepID, mediator);
    }
  
    public void processViewEvent(AbstractViewEvent event)
    {
        IEditor currentEditor;
        boolean bRunWofLanDLL = true;
        switch (event.getOrder())
        {
        case AbstractViewEvent.OPEN:
            if (event.getData() != null && event.getData() instanceof File)
            {
                currentEditor = openFile((File) event.getData(), FileFilterImpl.PNMLFilter);
            } else
            {
                currentEditor = openEditor();
            }
            break;
        case AbstractViewEvent.OPEN_SAMPLE:
            currentEditor = openFile((File) event.getData(), FileFilterImpl.SAMPLEFilter);
            break;
        case AbstractViewEvent.SAVE:
            save((EditorVC) getMediator().getUi().getEditorFocus());
            break;
        case AbstractViewEvent.SAVEAS:
            saveAs((EditorVC) getMediator().getUi().getEditorFocus());
            break;
        case AbstractViewEvent.EXPORT:
            export((EditorVC) getMediator().getUi().getEditorFocus());
            break;
        case AbstractViewEvent.ANALYSIS_WOPED:
        	// This veriable determines whether we will run internal
        	// or external analysis below
        	bRunWofLanDLL = true;
        case AbstractViewEvent.ANALYSIS_WOFLAN:

            if (getMediator().getUi().getEditorFocus() != null && getMediator().getUi().getEditorFocus().getModelProcessor().getProcessorType() == AbstractModelProcessor.MODEL_PROCESSOR_PETRINET)
            {

                boolean succeed = TPNExport.save(ConfigurationManager.getConfiguration().getHomedir() + "tempWoflanAnalyse.tpn", (PetriNetModelProcessor) getMediator().getUi().getEditorFocus()
                        .getModelProcessor());

                if (succeed)
                {
                    File f = new File(ConfigurationManager.getConfiguration().getHomedir() + "tempWoflanAnalyse.tpn");
                    if (f.exists())
                    {
                    	if (!bRunWofLanDLL)
                    	{
                    		Process process = null;
                    		try
                    		{
                    			process = Runtime.getRuntime().exec(ConfigurationManager.getConfiguration().getWoflanPath() + " " + "\"" + f.getAbsolutePath() + "\"");
                    			LoggerManager.info(Constants.FILE_LOGGER, "WOFLAN Started.");
                    		} catch (IOException e)
                    		{
                    			LoggerManager.warn(Constants.FILE_LOGGER, "COULD NOT START WOFLAN PROCESS.");
                    		}
                    		// Success in this context means that
                    		// we could run the woflan process
                    		succeed = process != null;
                    	}
                    	else
                    	{                    	
//                    		try
                    		{                        	
                    			// Instantiate net analysis dialog and display it
                    			// Arguments are the Woflan TPN file and the model processor
                    			// for the petri-net model that is in focus
                    			NetAnalysisDialog myDialog = new NetAnalysisDialog(f,
                    					getMediator().getUi().getEditorFocus(), this.getMediator());
                    			myDialog.setVisible(true);                        	
                    			
                    			LoggerManager.info(Constants.FILE_LOGGER, "Local WoPeD analysis started.");
                    		}/* catch (Exception e)
                    		{
                    			LoggerManager.warn(Constants.FILE_LOGGER, "Local WoPeD analysis failed.");
                    		}*/
                    	}

                    }    
                    else 
                    	// The temporary file doesn't exist
                    	// This most definitely means failure!
                    	succeed = false;
                    if (!succeed)
                    {
                    	String arg[] = {ConfigurationManager.getConfiguration().getHomedir()};
                        JOptionPane.showMessageDialog(null, 
                        		Messages.getString("File.Error.Woflan.Text", arg),
                        	    Messages.getString("File.Error.Woflan.Title"),
                        	    JOptionPane.WARNING_MESSAGE);
                    }
                }
            } else
            {
                 JOptionPane.showMessageDialog(null, 
                		Messages.getString("File.Error.NoNet.Text"),
                	    Messages.getString("File.Error.NoNet.Title"),
                	    JOptionPane.WARNING_MESSAGE);
            }

            break;
        }
    }

    /**
     * TODO: DOCUMENTATION (silenco)
     * 
     * @param editor
     */
    public void export(EditorVC editor)
    {
        // TODO: Cursor Handling
        // setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if (editor != null)
        {
            // Open save as Dialog
            JFileChooser jfc;
            if (ConfigurationManager.getConfiguration().getHomedir() != null)
            {
                jfc = new JFileChooser(new File(ConfigurationManager.getConfiguration().getHomedir()));
            } else
            {
                jfc = new JFileChooser();
            }

            // FileFilters
            Vector<String> tpnExtensions = new Vector<String>();
            tpnExtensions.add("tpn");
            FileFilterImpl TPNFilter = new FileFilterImpl(FileFilterImpl.TPNFilter, "TPN (*.tpn)", tpnExtensions);
            jfc.setFileFilter(TPNFilter);

            // FileFilters
            Vector<String> jpgExtensions = new Vector<String>();
            jpgExtensions.add("jpg");
            jpgExtensions.add("jpeg");
            FileFilterImpl JPGFilter = new FileFilterImpl(FileFilterImpl.JPGFilter, "JPG (*.jpg)", jpgExtensions);
            jfc.setFileFilter(JPGFilter);

            jfc.setDialogTitle(Messages.getString("Action.Export.Title"));
            jfc.showSaveDialog(null);
            if (jfc.getSelectedFile() != null && editor != null)
            {

                String savePath = jfc.getSelectedFile().getAbsolutePath().substring(0, jfc.getSelectedFile().getAbsolutePath().length() - jfc.getSelectedFile().getName().length());
                if (((FileFilterImpl) jfc.getFileFilter()).getFilterType() == FileFilterImpl.TPNFilter)
                {
                    savePath = savePath + Utils.getQualifiedFileName(jfc.getSelectedFile().getName(), tpnExtensions);
                } else if (((FileFilterImpl) jfc.getFileFilter()).getFilterType() == FileFilterImpl.JPGFilter)
                {
                    savePath = savePath + Utils.getQualifiedFileName(jfc.getSelectedFile().getName(), jpgExtensions);
                } else
                {
                    LoggerManager.error(Constants.FILE_LOGGER, "\"Export\" NOT SUPPORTED FILE TYPE.");
                }
                // ... and saving

                editor.setDefaultFileType(((FileFilterImpl) jfc.getFileFilter()).getFilterType());
                editor.setFilePath(savePath);
                save(editor);
            } else
            {
                LoggerManager.info(Constants.FILE_LOGGER, "\"Export\" canceled or nothing to export.");
            }
        }
        // TODO: Cursor Handling setCursor(Cursor.getDefaultCursor());
    }

    /**
     * TODO: DOCUMENTATION (silenco)
     * 
     * @param editor
     * @return
     */
    public boolean save(final EditorVC editor)
    {
        boolean succeed = false;
        try
        {
            // TODO: Cursor Handling
            // setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            // Open save dialog
            if (editor != null)
            {
                if (editor != null)
                {
                    if (editor.getFilePath() == null)
                    {
                        LoggerManager.debug(Constants.FILE_LOGGER, "File was not saved before. Call \"Save as\" instead.");
                        saveAs(editor);
                    } else
                    {

                        /* Tool for JPG Export */
                        if (editor.getDefaultFileType() == FileFilterImpl.JPGFilter)
                        {
                            succeed = JPGExport.save(editor.getFilePath(), editor);
                            // TODO: !!! Working dir
                        }
                        /* Tool for PNML Export */
                        else if (editor.getDefaultFileType() == FileFilterImpl.PNMLFilter)
                        {
                            IViewController[] iVC = getMediator().findViewController(IStatusBar.TYPE);
                            IStatusBar iSB[] = new IStatusBar[iVC.length];
                            for (int i = 0; i < iSB.length; i++)
                            {
                                
                                iSB[i] = (IStatusBar) iVC[i];
                            }
                            PNMLExport pe = new PNMLExport(iSB);
                            pe.saveToFile(editor, editor.getFilePath());
                            LoggerManager.info(Constants.FILE_LOGGER, "Petrinet saved in file: " + editor.getFilePath());

                            ConfigurationManager.getConfiguration().addRecentFile(new File(editor.getFilePath()).getName(), editor.getFilePath());
                            getMediator().getUi().updateRecentMenu();
                            editor.setSaved(true);
                            ConfigurationManager.getConfiguration().setCurrentWorkingDir(editor.getFilePath());
                            succeed = true;
                        }
                        /* Tool for TPN Export */
                        else if (editor.getDefaultFileType() == FileFilterImpl.TPNFilter)
                        {
                            succeed = TPNExport.save(editor.getFilePath(), (PetriNetModelProcessor) editor.getModelProcessor());
                            ConfigurationManager.getConfiguration().setCurrentWorkingDir(editor.getFilePath());
                        } else if (editor.getDefaultFileType() == FileFilterImpl.SAMPLEFilter)
                        {
                        	String arg[] = {editor.getName()};
                            JOptionPane.showMessageDialog(null, 
                            		Messages.getString("File.Error.SampleSave.Text", arg),
                            	    Messages.getString("File.Error.SampleSave.Title"),
                            	    JOptionPane.ERROR_MESSAGE);
                            succeed = false;
                        } else
                        {
                            LoggerManager.warn(Constants.FILE_LOGGER, "Unable to save File. Filetype not known: " + editor.getDefaultFileType());
                            succeed = false;
                        }

                    }
                }
            }
        } catch (AccessControlException ace)
        {
            ace.printStackTrace();
            LoggerManager.warn(Constants.FILE_LOGGER, "Could not save Editor. No rights to write the file to " + editor.getFilePath() + ". " + ace.getMessage());
            JOptionPane.showMessageDialog(getMediator().getUi().getComponent(), 
            		Messages.getString("File.Error.Applet.Text"),
            	    Messages.getString("File.Error.Appleat.Title"),
                    JOptionPane.ERROR_MESSAGE);
        } finally
        {
            // TODO: Cursor Handling setCursor(Cursor.getDefaultCursor());
        }
        return true;
    }

    /**
     * TODO: Documentation
     * 
     * @param editor
     * @return
     */
    public boolean saveAs(EditorVC editor)
    {
        boolean succeed = false;
        if (editor != null)
        {
            // Open save as Dialog
            JFileChooser jfc;
            if (ConfigurationManager.getConfiguration().getCurrentWorkingDir() != null 
        	    && !ConfigurationManager.getConfiguration().getCurrentWorkingDir().equals(""))
            {
                jfc = new JFileChooser(new File(ConfigurationManager.getConfiguration().getCurrentWorkingDir()));
            } else if (ConfigurationManager.getConfiguration().getHomedir() != null)
            {
                jfc = new JFileChooser(new File(ConfigurationManager.getConfiguration().getHomedir()));
            } else
            {
                jfc = new JFileChooser();
            }

            // FileFilters
            Vector<String> extensions = new Vector<String>();
            if (editor.getModelProcessor().getProcessorType() == AbstractModelProcessor.MODEL_PROCESSOR_PETRINET)
            {
                extensions.add("pnml");
                extensions.add("xml");
                FileFilterImpl PNMLFilter = new FileFilterImpl(FileFilterImpl.PNMLFilter, "Petri Net Markup Language (1.3.2) (*.pnml)", extensions);
                jfc.setFileFilter(PNMLFilter);
            }
            jfc.setDialogTitle(Messages.getString("Action.EditorSaveAs.Title"));
            
            
            int returnVal = jfc.showSaveDialog(null);
            
            if (jfc.getSelectedFile() != null && returnVal == JFileChooser.APPROVE_OPTION)
            {
                String savePath = jfc.getSelectedFile().getAbsolutePath().substring(0, jfc.getSelectedFile().getAbsolutePath().length() - jfc.getSelectedFile().getName().length());
                String fileName = Utils.getQualifiedFileName(jfc.getSelectedFile().getName(), extensions);
                if (!new File(savePath.concat(fileName)).exists() || (Utils.isFileOverride(null, jfc.getSelectedFile().getPath())))
                {
                    if (editor != null && new File(savePath).exists())
                    {
                        // setting the Default File Type
                        editor.setDefaultFileType(((FileFilterImpl) jfc.getFileFilter()).getFilterType());
                        // setting the new filename to editor, and Title to
                        // Frame
                        editor.setName(fileName);
                        editor.setFilePath(savePath.concat(fileName));
                        // getTaskBar().getsetToolTipText(getActiveEditor().getFileName());
                        // ... and saving
                        ConfigurationManager.getConfiguration().setCurrentWorkingDir(savePath);
                        LoggerManager.debug(Constants.FILE_LOGGER, "Current working dir is: " + ConfigurationManager.getConfiguration().getCurrentWorkingDir());
                        editor.setDefaultFileType(((FileFilterImpl) jfc.getFileFilter()).getFilterType());
                        editor.setFilePath(savePath.concat(fileName));
                        succeed = save(editor);
                    } else
                    {
                        LoggerManager.debug(Constants.FILE_LOGGER, "\"Save as\" canceled or nothing to save at all.");
                        succeed = false;
                    }
                }
            }
        }
        return succeed;

    }

    /**
     * TODO: DOCUMENTATION (silenco)
     */
    public IEditor openEditor()
    {
        JFileChooser jfc;

        if (ConfigurationManager.getConfiguration().getCurrentWorkingDir() != null)
        {
            jfc = new JFileChooser(new File(ConfigurationManager.getConfiguration().getCurrentWorkingDir()));
        } else if (ConfigurationManager.getConfiguration().getHomedir() != null)
        {
            jfc = new JFileChooser(new File(ConfigurationManager.getConfiguration().getHomedir()));
        } else
        {
            jfc = new JFileChooser();
        }
        // FileFilters
        Vector<String> extensions = new Vector<String>();

        extensions.add("pnml");
        extensions.add("xml");
        // extensions.add("xmi");
        jfc.setFileFilter(new FileFilterImpl(FileFilterImpl.OLDPNMLFilter, "Petri Net Markup Language older version (*.pnml,*.xml)", extensions));
        jfc.setFileFilter(new FileFilterImpl(FileFilterImpl.PNMLFilter, "Petri Net Markup Language (1.3.2) (*.pnml,*.xml)", extensions));
        // jfc.setFileFilter(new FileFilterImpl(FileFilterImpl.XMIFilter, "UML
        // (*.xmi)", extensions));

        jfc.showOpenDialog(null);

        if (jfc.getSelectedFile() != null)
        {
            return openFile(jfc.getSelectedFile(), ((FileFilterImpl) jfc.getFileFilter()).getFilterType());
        }
        return null;
    }

    /**
     * For open files outside the jar file
     * 
     * @param file
     * @param filter
     */
    private IEditor openFile(File file, int filter)
    {
        IEditor editor = null;
        final PNMLImport pr;
        if (filter == FileFilterImpl.PNMLFilter || filter == FileFilterImpl.SAMPLEFilter)
        {

            IViewController[] iVC = getMediator().findViewController(IStatusBar.TYPE);
            IStatusBar[] iSB = new IStatusBar[iVC.length];
            for (int i = 0; i < iSB.length; i++)
            {
                iSB[i] = (IStatusBar) iVC[i];
            }

            pr = new PNMLImport((ApplicationMediator) getMediator(), iSB);
        } else if (filter == FileFilterImpl.OLDPNMLFilter)
        {
            pr = new OLDPNMLImport2((ApplicationMediator) getMediator());
        } else pr = null;
        if (pr != null)
        {
            boolean loadSuccess = false;
            InputStream is;

            //TODO Generate Thread
            try
            {
                is = new FileInputStream(file.getAbsolutePath());
                loadSuccess = pr.run(is);
            } catch (FileNotFoundException e)
            {                
                String jarPath = file.getPath().replace('\\', '/');

                is = this.getClass().getResourceAsStream(jarPath);
                loadSuccess = pr.run(is);

                /*
                 * if (!loadSuccess) LoggerManager.error(Constants.FILE_LOGGER,
                 * "Could not open InputStream. " + file.getAbsolutePath());
                 */
                // }
            }
            if (loadSuccess)
            {
                editor = pr.getEditor()[pr.getEditor().length - 1];
                for (int i = 0; i < pr.getEditor().length; i++)
                {
                    if (editor instanceof EditorVC)
                    {
                        ((EditorVC) pr.getEditor()[i]).setDefaultFileType(filter);
                        ((EditorVC) pr.getEditor()[i]).setName(file.getName());
                        ((EditorVC) pr.getEditor()[i]).setFilePath(file.getAbsolutePath());
                    }
                    // add recent
                    if (filter == FileFilterImpl.PNMLFilter) ConfigurationManager.getConfiguration().addRecentFile(file.getName(), file.getAbsolutePath());
                    if (filter != FileFilterImpl.SAMPLEFilter) ConfigurationManager.getConfiguration().setCurrentWorkingDir(file.getAbsolutePath());
                    // add Editor
                    LoggerManager.info(Constants.FILE_LOGGER, "Petrinet loaded from file: " + file.getAbsolutePath());
                }
            } else
            {
                ConfigurationManager.getConfiguration().removeRecentFile(file.getName(), file.getAbsolutePath());
                String arg[] = {file.getAbsolutePath()};
                JOptionPane.showMessageDialog(null, 
                		Messages.getString("File.Error.FileOpen.Text", arg),
                	    Messages.getString("File.Error.FileOpen.Title"),
                	    JOptionPane.ERROR_MESSAGE);
            }

            getMediator().getUi().updateRecentMenu();
        } else
        {}
        return editor;
    }

}
