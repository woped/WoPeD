package org.woped.file.controller.vep;

import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessControlException;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.woped.core.analysis.StructuralAnalysis;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractEventProcessor;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IEditor;
import org.woped.core.controller.IStatusBar;
import org.woped.core.controller.IViewController;
import org.woped.core.model.AbstractModelProcessor;
import org.woped.core.model.ArcModel;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.AbstractPetriNetModelElement;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.core.utilities.Utils;
import org.woped.editor.controller.ApplicationMediator;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.editor.utilities.FileFilterImpl;
import org.woped.file.Constants;
import org.woped.file.ImageExport;
import org.woped.file.OLDPNMLImport2;
import org.woped.file.PNMLExport;
import org.woped.file.PNMLImport;
import org.woped.file.TPNExport;
import org.woped.translations.Messages;
import org.woped.qualanalysis.NetAnalysisDialog;
import org.woped.qualanalysis.WoflanAnalysis;
import org.woped.quantana.gui.CapacityAnalysisDialog;
import org.woped.quantana.gui.QuantitativeSimulationDialog;
import org.woped.bpel.BPEL_MAIN;

public class FileEventProcessor extends AbstractEventProcessor
{
	public FileEventProcessor(int vepID, ApplicationMediator mediator)
    {
        super(vepID, mediator);
    }
  
    public void processViewEvent(AbstractViewEvent event)
    {
    	EditorVC editor = (EditorVC) getMediator().getUi().getEditorFocus();
    	boolean bRunWofLanDLL = false;
        
        switch (event.getOrder())
        {
        case AbstractViewEvent.OPEN:
            if (event.getData() != null && event.getData() instanceof File)
            {
                openFile((File) event.getData(), FileFilterImpl.PNMLFilter);
            } else
            {
                openEditor();
            }
            break;
        case AbstractViewEvent.OPEN_SAMPLE:
            openFile((File) event.getData(), FileFilterImpl.SAMPLEFilter);
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
                    		// Instantiate net analysis dialog and display it
                    		// Arguments are the Woflan TPN file and the model processor
                    		// for the petri-net model that is in focus
                    		NetAnalysisDialog myDialog = new NetAnalysisDialog(
                    				(JFrame) getMediator().getUi(),
                    				f,
                    				getMediator().getUi().getEditorFocus(), this.getMediator());
                    		myDialog.setVisible(true);                        	

                    		LoggerManager.info(Constants.FILE_LOGGER, "Local WoPeD analysis started.");
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

        case AbstractViewEvent.QUANTCAP:
    		if (isSound(editor) & isBranchingOk(editor)) 
        			new CapacityAnalysisDialog((JFrame) getMediator().getUi(), editor);
             break;
            
        case AbstractViewEvent.QUANTSIM:
       		if (isSound(editor) & isBranchingOk(editor)) 
       		 		new QuantitativeSimulationDialog((JFrame) getMediator().getUi(), editor);
     		break;
    	}
    }

    /**
     * TODO: DOCUMENTATION (silenco)
     * 
     * @param editor
     */

	private boolean isSound(EditorVC editor) {
		TPNExport.save(ConfigurationManager.getConfiguration().getHomedir() + "tempWoflanAnalyse.tpn", (PetriNetModelProcessor) getMediator().getUi().getEditorFocus()
				.getModelProcessor());
		File f = new File(ConfigurationManager.getConfiguration().getHomedir() + "tempWoflanAnalyse.tpn");

		WoflanAnalysis wa  = new WoflanAnalysis(editor, f);
		StructuralAnalysis sa = new StructuralAnalysis(editor);

		int unbound = wa.getNumUnboundedPlaces();
		int nonlive = wa.getNumNonLiveTransitions();

		int sound = unbound + nonlive;
		int soPl = sa.getNumSourcePlaces();
		int soTr = sa.getNumSourceTransitions();
		int siPl = sa.getNumSinkPlaces();
		int siTr = sa.getNumSinkTransitions();
		boolean wfpn = (soPl >= 1 && soPl + soTr == 1) && (siPl >= 1 && siPl + siTr == 1);
		if ((sound == 0) & wfpn) {
    		return true;
    		}
		else {
			JOptionPane.showMessageDialog(null, 
					Messages.getString("QuantAna.Message.SoundnessViolation"));
			return false;
		}
	}

	private boolean isBranchingOk(EditorVC editor) {
		ModelElementContainer mec = editor.getModelProcessor().getElementContainer();
		boolean branchingOk = true;
		String[] param = {"", "", ""};
		Iterator transes = (mec.getElementsByType(AbstractPetriNetModelElement.TRANS_OPERATOR_TYPE)).values().iterator();
		StructuralAnalysis sa = new StructuralAnalysis(editor);
		Iterator places = sa.getPlacesIterator();
		AbstractPetriNetModelElement end = (AbstractPetriNetModelElement)sa.getSinkPlacesIterator().next();

		while (transes.hasNext()){
			AbstractPetriNetModelElement trans = (AbstractPetriNetModelElement)transes.next();
			Map outArcs = mec.getOutgoingArcs(trans.getId());
			int sum = 0;
			for (Object v : outArcs.values()){
				double p = ((ArcModel) v).getProbability();
				sum += (Double.valueOf(p * 100)).intValue();
			}

			int type = ((OperatorTransitionModel)trans).getOperatorType();
			if ((type == OperatorTransitionModel.XOR_SPLIT_TYPE || 
					type == OperatorTransitionModel.XOR_SPLITJOIN_TYPE || 
						type == OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE) 
							& sum != 100) {
				branchingOk = false;
				param[0] = Messages.getString("QuantAna.Transition");
				param[1] = trans.getNameValue();
				param[2] = sum + "";
			}
		}
	
		while (places.hasNext()){
			AbstractPetriNetModelElement place = (AbstractPetriNetModelElement)places.next();
			if (!place.equals(end)) {
				Map outArcs = mec.getOutgoingArcs(place.getId());
				int sum = 0;
				for (Object v : outArcs.values()){
					double p = ((ArcModel) v).getProbability();
					sum += (Double.valueOf(p * 100)).intValue();
				}

				if (outArcs.size() > 1 && sum != 100) {
					branchingOk = false;
					param[0] = Messages.getString("QuantAna.Place");
					param[1] = place.getNameValue();
					param[2] = sum + "";
				}
			}
		}
		if (branchingOk) {
			return true;
		}
		else {
			JOptionPane.showMessageDialog(null, 
					Messages.getString("QuantAna.Message.BranchingProbabilityViolation", param));
			return false;
		}
	}
	
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
            Vector<String> pngExtensions = new Vector<String>();
            pngExtensions.add("png");
            FileFilterImpl PNGFilter = new FileFilterImpl(FileFilterImpl.PNGFilter, "PNG (*.png)", pngExtensions);
            jfc.setFileFilter(PNGFilter);
            
            Vector<String> bmpExtensions = new Vector<String>();
            bmpExtensions.add("bmp");
            FileFilterImpl BMPFilter = new FileFilterImpl(FileFilterImpl.BMPFilter, "BMP (*.bmp)", bmpExtensions);
            jfc.setFileFilter(BMPFilter);

            Vector<String> jpgExtensions = new Vector<String>();
            jpgExtensions.add("jpg");
            jpgExtensions.add("jpeg");
            FileFilterImpl JPGFilter = new FileFilterImpl(FileFilterImpl.JPGFilter, "JPG (*.jpg)", jpgExtensions);
            jfc.setFileFilter(JPGFilter);

            Vector<String> tpnExtensions = new Vector<String>();
            tpnExtensions.add("tpn");
            FileFilterImpl TPNFilter = new FileFilterImpl(FileFilterImpl.TPNFilter, "TPN (*.tpn)", tpnExtensions);
            jfc.setFileFilter(TPNFilter);            
            
            jfc.setFileFilter(PNGFilter);
            
            //insert of the BPEL Export filefilter
            jfc.setFileFilter(BPEL_MAIN.get_BPEL_MAIN_CLASS().get_filefilter());
            
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
                } else if (((FileFilterImpl) jfc.getFileFilter()).getFilterType() == FileFilterImpl.PNGFilter)
                {
                    savePath = savePath + Utils.getQualifiedFileName(jfc.getSelectedFile().getName(), pngExtensions);
                } else if (((FileFilterImpl) jfc.getFileFilter()).getFilterType() == FileFilterImpl.BMPFilter)
                {
                    savePath = savePath + Utils.getQualifiedFileName(jfc.getSelectedFile().getName(), bmpExtensions);
                } else if(BPEL_MAIN.get_BPEL_MAIN_CLASS().check_file_extension(jfc))
                {
                	savePath = BPEL_MAIN.get_BPEL_MAIN_CLASS().get_SavePath(savePath, jfc);
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

                        if (editor.getDefaultFileType() == FileFilterImpl.JPGFilter)
                        {
                            succeed = ImageExport.saveJPG(ImageExport.getRenderedImage(editor), 
                        	    new File(editor.getFilePath()));
                            // TODO: !!! Working dir
                        } else if (editor.getDefaultFileType() == FileFilterImpl.PNGFilter)
                        {
                            succeed = ImageExport.savePNG(ImageExport.getRenderedImage(editor), 
                        	    new File(editor.getFilePath()));
                        } else if (editor.getDefaultFileType() == FileFilterImpl.BMPFilter)
                        {
                            succeed = ImageExport.saveBMP(ImageExport.getRenderedImage(editor), 
                        	    new File(editor.getFilePath()));
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
                        //BPEL-Export
                        else if (editor.getDefaultFileType() == FileFilterImpl.BPELFilter)
                        {
                            succeed = TPNExport.save(editor.getFilePath(), (PetriNetModelProcessor) editor.getModelProcessor());
                            ConfigurationManager.getConfiguration().setCurrentWorkingDir(editor.getFilePath());
                        }
                        /* Tool for TPN Export */
                        else if (editor.getDefaultFileType() == FileFilterImpl.TPNFilter)
                        {
                            succeed = BPEL_MAIN.get_BPEL_MAIN_CLASS().save_file(editor.getFilePath(), (PetriNetModelProcessor) editor.getModelProcessor());
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
        return succeed;
    }

    public static boolean isFileOverride(Frame owner, String fileName)
    {
		String textMessages[] = { 	Messages.getString("Dialog.Yes"),
									Messages.getString("Dialog.No")
								};
		String[] args = {fileName};
		
		return JOptionPane.showOptionDialog(owner, 
					Messages.getStringReplaced(
							"Action.Confirm.File.Overwrite.Text",
							args),
					Messages.getString("Action.Confirm.File.Overwrite.Title"),
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.QUESTION_MESSAGE,
					null,
					textMessages,
					textMessages[1]) == JOptionPane.YES_OPTION;
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
                if (!new File(savePath.concat(fileName)).exists() || (isFileOverride(null, jfc.getSelectedFile().getPath())))
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
