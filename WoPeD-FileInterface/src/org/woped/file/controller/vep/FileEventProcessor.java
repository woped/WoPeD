package org.woped.file.controller.vep;

import java.awt.Cursor;
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

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.apromore.access.ApromoreAccessObject;
import org.apromore.manager.model_portal.EditSessionType;
import org.apromore.manager.model_portal.ImportProcessOutputMsgType;
import org.woped.bpel.BPEL;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractEventProcessor;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IEditor;
import org.woped.core.controller.IStatusBar;
import org.woped.core.controller.IViewController;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.ArcModel;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.utilities.FileFilterImpl;
import org.woped.core.utilities.LoggerManager;
import org.woped.core.utilities.Utils;
import org.woped.editor.controller.ApplicationMediator;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.file.Constants;
import org.woped.file.ImageExport;
import org.woped.file.OLDPNMLImport2;
import org.woped.file.PNMLExport;
import org.woped.file.PNMLImport;
import org.woped.file.gui.ExportFrame;
import org.woped.file.gui.ImportFrame;
import org.woped.metrics.builder.MetricsBuilder;
import org.woped.metrics.helpers.LabeledFileFilter;
import org.woped.qualanalysis.service.IQualanalysisService;
import org.woped.qualanalysis.service.QualAnalysisServiceFactory;
import org.woped.qualanalysis.woflan.TPNExport;
import org.woped.quantana.gui.CapacityAnalysisDialog;
import org.woped.quantana.gui.QuantitativeSimulationDialog;
import org.woped.translations.Messages;

public class FileEventProcessor extends AbstractEventProcessor {
    public FileEventProcessor(int vepID, ApplicationMediator mediator) {
        super(vepID, mediator);
    }

    @Override
    public void processViewEvent(AbstractViewEvent event) {
        EditorVC editor = (EditorVC) getMediator().getUi().getEditorFocus();

        switch (event.getOrder()) {
        case AbstractViewEvent.OPEN:
            if (event.getData() != null && event.getData() instanceof File) {
                openFile((File) event.getData(), FileFilterImpl.PNMLFilter);
            } else {
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
		case AbstractViewEvent.IMPORTAPRO:
			importApromore();
			break;
		case AbstractViewEvent.EXPORTAPRO:
			exportApromore((EditorVC) getMediator().getUi().getEditorFocus());
			break;
        case AbstractViewEvent.EXPORT:
            export((EditorVC) getMediator().getUi().getEditorFocus());
            break;
        case AbstractViewEvent.ANALYSIS_WOPED:
            if (editor != null) {
                editor.getEditorPanel().showAnalysisBar();
                LoggerManager.info(Constants.FILE_LOGGER, "Local WoPeD analysis started.");
            }
            break;
         
        case AbstractViewEvent.ANALYSIS_METRIC:
        	// calls the new metrics sidebar
        	if (!editor.getEditorPanel().isMetricsBarVisible())
        	{
        		editor.getEditorPanel().showMetricsBar();
        		
        	}
        	else
        	{
        		editor.getEditorPanel().hideMetricsBar();
        	}
            
            LoggerManager.info(Constants.FILE_LOGGER, Messages.getString("Metrics.General.MetricsStarted") + ".");

            //new MetricsUIRequestHandler().showInitialData(editor.getModelProcessor().getElementContainer());
            break;
            
        case AbstractViewEvent.ANALYSIS_MASSMETRICANALYSE:        	
        	//new MetricsBuilder(editor); 
        	JFileChooser cFolder = new JFileChooser();
    		cFolder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        	cFolder.setDialogTitle(Messages.getString("Metrics.Mass.OpenDialog")); 
    		int cont = cFolder.showOpenDialog(null);
    		if(cont != JFileChooser.APPROVE_OPTION) return;
    		File folder = cFolder.getSelectedFile();
    		cFolder.setFileSelectionMode(JFileChooser.FILES_ONLY);
    		FileFilter filter = new LabeledFileFilter() {
    			public boolean accept(File file) {
    				if (file.getAbsolutePath().endsWith(".csv"))
    					return true;
    				return false;
    			}

    			public String getDescription() {
    				return "Comma Separated Values (*.csv)";
    			}

    			public String getExtension() {
    				return ".csv";
    			}
    		};
    		cFolder.addChoosableFileFilter(filter);
    		cFolder.setDialogTitle(Messages.getString("Metrics.Mass.SaveDialog")); 
    		cont = cFolder.showSaveDialog(null);
    		if(cont != JFileChooser.APPROVE_OPTION) return;
    		File saveTo  = new File(cFolder.getSelectedFile().getAbsolutePath());
			if(!saveTo.getName().endsWith(((LabeledFileFilter)cFolder.getFileFilter()).getExtension()))
					saveTo = new File(saveTo.getAbsolutePath()+((LabeledFileFilter)cFolder.getFileFilter()).getExtension());
			
    		saveTo.delete();
    		MassMetricsCalculator mass = new MassMetricsCalculator();
    		mass.prepareMetrics(folder.listFiles());
    		new MassMetricsStatus(mass).start();
    		mass.calculateMetrics(saveTo, (ApplicationMediator)getMediator());
        	break;
        case AbstractViewEvent.ANALYSIS_METRICSBUILDER:       	
        	new MetricsBuilder(editor, "");
        	break;

        case AbstractViewEvent.QUANTCAP:
            if (isSound(editor) & isBranchingOk(editor)) {
                new CapacityAnalysisDialog((JFrame) getMediator().getUi(), editor);
            }
            break;

        case AbstractViewEvent.QUANTSIM:
            if (isSound(editor) & isBranchingOk(editor)) {
                if ((editor).getSimDlg() == null) {
                    (editor).setSimDlg(new QuantitativeSimulationDialog((JFrame) getMediator().getUi(), editor));
                } else {
                    (editor).getSimDlg().showdlg();
                }
            }
            break;
        }
    }

    /**
     * TODO: DOCUMENTATION (silenco)
     * 
     * @param editor
     */

    private boolean isSound(IEditor editor) {

        IQualanalysisService qualanService = QualAnalysisServiceFactory.createNewQualAnalysisService(editor);

        if (qualanService.isSound()) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, Messages.getString("QuantAna.Message.SoundnessViolation"));
            return false;
        }
    }

    private boolean isBranchingOk(EditorVC editor) {
        ModelElementContainer mec = editor.getModelProcessor().getElementContainer();
        boolean branchingOk = true;
        String[] param = { "", "", "" };
        Iterator<AbstractPetriNetElementModel> transes = (mec.getElementsByType(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE)).values()
                .iterator();
        IQualanalysisService qualanService = QualAnalysisServiceFactory.createNewQualAnalysisService(editor);
        Iterator<AbstractPetriNetElementModel> places = qualanService.getPlaces().iterator();
        AbstractPetriNetElementModel end = (AbstractPetriNetElementModel) qualanService.getSinkPlaces().iterator().next();

        while (transes.hasNext()) {
            AbstractPetriNetElementModel trans = (AbstractPetriNetElementModel) transes.next();
            Map<String, Object> outArcs = mec.getOutgoingArcs(trans.getId());
            int sum = 0;
            for (Object v : outArcs.values()) {
                double p = ((ArcModel) v).getProbability();
                sum += (Double.valueOf(p * 100)).intValue();
            }

            int type = ((OperatorTransitionModel) trans).getOperatorType();
            if ((type == OperatorTransitionModel.XOR_SPLIT_TYPE || type == OperatorTransitionModel.XOR_SPLITJOIN_TYPE || type == OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE)
                    & sum != 100) {
                branchingOk = false;
                param[0] = Messages.getString("QuantAna.Transition");
                param[1] = trans.getNameValue();
                param[2] = sum + "";
            }
        }

        while (places.hasNext()) {
            AbstractPetriNetElementModel place = (AbstractPetriNetElementModel) places.next();
            if (!place.equals(end)) {
                Map<String, Object> outArcs = mec.getOutgoingArcs(place.getId());
                int sum = 0;
                for (Object v : outArcs.values()) {
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
        } else {
            JOptionPane.showMessageDialog(null, Messages.getString("QuantAna.Message.BranchingProbabilityViolation",
                    param));
            return false;
        }
    }

    public void export(EditorVC editor) {
        getMediator().getUi().getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if (editor != null) {
            // Open save as Dialog
            JFileChooser jfc;
            String hd = ConfigurationManager.getConfiguration().getCurrentWorkingdir();
            if (hd != null) {
                jfc = new JFileChooser(new File(hd));
            } else {
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

            // insert of the BPEL Export filefilter
 //           jfc.setFileFilter(BPEL.getBPELMainClass().getFilefilter());

            jfc.setDialogTitle(Messages.getString("Action.Export.Title"));
            jfc.showSaveDialog(null);
            if (jfc.getSelectedFile() != null && editor != null) {

                String savePath = jfc.getSelectedFile().getAbsolutePath().substring(0,
                        jfc.getSelectedFile().getAbsolutePath().length() - jfc.getSelectedFile().getName().length());
                if (((FileFilterImpl) jfc.getFileFilter()).getFilterType() == FileFilterImpl.TPNFilter) {
                    savePath = savePath + Utils.getQualifiedFileName(jfc.getSelectedFile().getName(), tpnExtensions);
                } else
                    if (((FileFilterImpl) jfc.getFileFilter()).getFilterType() == FileFilterImpl.JPGFilter) {
                        savePath = savePath
                                + Utils.getQualifiedFileName(jfc.getSelectedFile().getName(), jpgExtensions);
                    } else
                        if (((FileFilterImpl) jfc.getFileFilter()).getFilterType() == FileFilterImpl.PNGFilter) {
                            savePath = savePath
                                    + Utils.getQualifiedFileName(jfc.getSelectedFile().getName(), pngExtensions);
                        } else
                            if (((FileFilterImpl) jfc.getFileFilter()).getFilterType() == FileFilterImpl.BMPFilter) {
                                savePath = savePath
                                        + Utils.getQualifiedFileName(jfc.getSelectedFile().getName(), bmpExtensions);
                            } else
                                if (BPEL.getBPELMainClass().checkFileExtension(jfc)) {
                                    savePath = BPEL.getBPELMainClass().getSavePath(savePath, jfc);
                                } else {
                                    LoggerManager.error(Constants.FILE_LOGGER, "\"Export\" NOT SUPPORTED FILE TYPE.");
                                }
                // ... and saving

                editor.setDefaultFileType(((FileFilterImpl) jfc.getFileFilter()).getFilterType());
                editor.setFilePath(savePath);
                ConfigurationManager.getConfiguration().setCurrentWorkingdir(savePath.substring(0,savePath.lastIndexOf(File.separator)));
                save(editor);
            } else {
                LoggerManager.info(Constants.FILE_LOGGER, "\"Export\" canceled or nothing to export.");
            }
        }
        getMediator().getUi().getComponent().setCursor(Cursor.getDefaultCursor());
    }

    /**
     * TODO: DOCUMENTATION (silenco)
     * 
     * @param editor
     * @return
     */
    public boolean save(final EditorVC editor) {
        boolean succeed = false;

        getMediator().getUi().getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            // Open save dialog
            if (editor != null) {
                if (editor != null) {
                    if (editor.getFilePath() == null) {
                        LoggerManager.debug(Constants.FILE_LOGGER,
                                "File was not saved before. Call \"Save as\" instead.");
                        saveAs(editor);
                    } else {

                        if (editor.getDefaultFileType() == FileFilterImpl.JPGFilter) {
                            succeed = ImageExport.saveJPG(ImageExport.getRenderedImage(editor), new File(editor
                                    .getFilePath()));
                            // TODO: !!! Working dir
                        } else
                            if (editor.getDefaultFileType() == FileFilterImpl.PNGFilter) {
                                succeed = ImageExport.savePNG(ImageExport.getRenderedImage(editor), new File(editor
                                        .getFilePath()));
                            } else
                                if (editor.getDefaultFileType() == FileFilterImpl.BMPFilter) {
                                    succeed = ImageExport.saveBMP(ImageExport.getRenderedImage(editor), new File(editor
                                            .getFilePath()));
                                }
                                /* Tool for PNML Export */
                                else
                                    if (editor.getDefaultFileType() == FileFilterImpl.PNMLFilter) {
                                        IViewController[] iVC = getMediator().findViewController(IStatusBar.TYPE);
                                        IStatusBar iSB[] = new IStatusBar[iVC.length];
                                        for (int i = 0; i < iSB.length; i++) {

                                            iSB[i] = (IStatusBar) iVC[i];
                                        }
                                        PNMLExport pe = new PNMLExport(iSB);
                                        pe.saveToFile(editor, editor.getFilePath());
                                        LoggerManager.info(Constants.FILE_LOGGER, "Petrinet saved in file: "
                                                + editor.getFilePath());

                                        ConfigurationManager.getConfiguration().addRecentFile(
                                                new File(editor.getFilePath()).getName(), editor.getFilePath());
                                        getMediator().getUi().updateRecentMenu();
                                        editor.setSaved(true);
                                        ConfigurationManager.getConfiguration().setCurrentWorkingdir(editor.getPathName());
                                        succeed = true;
                                    }
                                    // BPEL-Export
                                    else
                                        if (editor.getDefaultFileType() == FileFilterImpl.BPELFilter
                                                && this.isSound(editor)) {
                                        	IQualanalysisService qualanService = QualAnalysisServiceFactory.createNewQualAnalysisService(editor);
                                            int wellStruct = qualanService.getPTHandles().size() + qualanService.getTPHandles().size();
                                            int freeChoice = qualanService.getFreeChoiceViolations().size();
                                            int sound = wellStruct + freeChoice;
                                            if (sound == 0) {
                                                succeed = BPEL.getBPELMainClass().saveFile(editor.getFilePath(), editor);
                                                ConfigurationManager.getConfiguration().setCurrentWorkingdir(editor.getPathName());
                                            } else {
                                                JOptionPane.showMessageDialog(null, Messages
                                                        .getString("QuantAna.Message.SoundnessViolation"));
                                            }

                                        }
                                        /* Tool for TPN Export */
                                        else
                                            if (editor.getDefaultFileType() == FileFilterImpl.TPNFilter) {
                                                succeed = TPNExport.save(editor.getFilePath(),
                                                        (PetriNetModelProcessor) editor.getModelProcessor());
                                                ConfigurationManager.getConfiguration().setCurrentWorkingdir(editor.getPathName());

                                            } else
                                                if (editor.getDefaultFileType() == FileFilterImpl.SAMPLEFilter) {
                                                    String arg[] = { editor.getName() };
                                                    JOptionPane.showMessageDialog(null, Messages.getString(
                                                            "File.Error.SampleSave.Text", arg), Messages
                                                            .getString("File.Error.SampleSave.Title"),
                                                            JOptionPane.ERROR_MESSAGE);
                                                    succeed = false;
                                                } else {
                                                    LoggerManager.warn(Constants.FILE_LOGGER,
                                                            "Unable to save File. Filetype not known: "
                                                                    + editor.getDefaultFileType());
                                                    succeed = false;
                                                }
                    }
                }
            }
        } catch (AccessControlException ace) {
            ace.printStackTrace();
            LoggerManager.warn(Constants.FILE_LOGGER, "Could not save Editor. No rights to write the file to "
                    + editor.getFilePath() + ". " + ace.getMessage());
            JOptionPane.showMessageDialog(getMediator().getUi().getComponent(), Messages
                    .getString("File.Error.Applet.Text"), Messages.getString("File.Error.Appleat.Title"),
                    JOptionPane.ERROR_MESSAGE);
        }

        getMediator().getUi().getComponent().setCursor(Cursor.getDefaultCursor());
        return succeed;
    }

    public static boolean isFileOverride(Frame owner, String fileName) {
        String textMessages[] = { Messages.getString("Dialog.Yes"), Messages.getString("Dialog.No") };
        String[] args = { fileName };

        return JOptionPane.showOptionDialog(owner, Messages.getStringReplaced("Action.Confirm.File.Overwrite.Text",
                args), Messages.getString("Action.Confirm.File.Overwrite.Title"), JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, textMessages, textMessages[1]) == JOptionPane.YES_OPTION;
    }

    /**
     * TODO: Documentation
     * 
     * @param editor
     * @return
     */
    public boolean saveAs(EditorVC editor) {
        boolean succeed = false;

        getMediator().getUi().getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if (editor != null) {
            // Open save as Dialog
            JFileChooser jfc;
            if (ConfigurationManager.getConfiguration().isCurrentWorkingdirSet()) {
                jfc = new JFileChooser(new File(ConfigurationManager.getConfiguration().getCurrentWorkingdir()));
            } else
                if (ConfigurationManager.getConfiguration().isHomedirSet()) {
                    jfc = new JFileChooser(new File(ConfigurationManager.getConfiguration().getHomedir()));
                } else {
                    jfc = new JFileChooser();
                }

            // FileFilters
            Vector<String> extensions = new Vector<String>();
            extensions.add("pnml");
            extensions.add("xml");
            FileFilterImpl PNMLFilter = new FileFilterImpl(FileFilterImpl.PNMLFilter,
            		"Petri Net Markup Language (1.3.2) (*.pnml)", extensions);
            jfc.setFileFilter(PNMLFilter);
            
            jfc.setDialogTitle(Messages.getString("Action.EditorSaveAs.Title"));

            int returnVal = jfc.showSaveDialog(null);

            if (jfc.getSelectedFile() != null && returnVal == JFileChooser.APPROVE_OPTION) {
                String savePath = jfc.getSelectedFile().getAbsolutePath().substring(0,
                        jfc.getSelectedFile().getAbsolutePath().length() - jfc.getSelectedFile().getName().length());
                String fileName = Utils.getQualifiedFileName(jfc.getSelectedFile().getName(), extensions);
                if (!new File(savePath.concat(fileName)).exists()
                        || (isFileOverride(null, jfc.getSelectedFile().getPath()))) {
                    if (editor != null && new File(savePath).exists()) {
                        // setting the Default File Type
                        editor.setDefaultFileType(((FileFilterImpl) jfc.getFileFilter()).getFilterType());
                        // setting the new filename to editor, and Title to
                        // Frame
                        editor.setName(fileName);
                        editor.setPathName(savePath);
                        editor.setFilePath(savePath.concat(fileName));
                        ConfigurationManager.getConfiguration().setCurrentWorkingdir(savePath);
                        LoggerManager.debug(Constants.FILE_LOGGER, "Current working dir is: "
                                + ConfigurationManager.getConfiguration().getCurrentWorkingdir());
                        editor.setDefaultFileType(((FileFilterImpl) jfc.getFileFilter()).getFilterType());
                        succeed = save(editor);
                    } else {
                        LoggerManager.debug(Constants.FILE_LOGGER, "\"Save as\" canceled or nothing to save at all.");
                        succeed = false;
                    }
                }
            }
        }

        getMediator().getUi().getComponent().setCursor(Cursor.getDefaultCursor());
        return succeed;
    }

    /**
     * TODO: DOCUMENTATION (silenco)
     */
    public IEditor openEditor() {
        JFileChooser jfc;
        String fn;

        if (ConfigurationManager.getConfiguration().isCurrentWorkingdirSet()) {
            fn = ConfigurationManager.getConfiguration().getCurrentWorkingdir();
            jfc = new JFileChooser(new File(fn));
        } else
            if (ConfigurationManager.getConfiguration().isHomedirSet()) {
                fn = ConfigurationManager.getConfiguration().getHomedir();
                jfc = new JFileChooser(new File(fn));
            } else {
                jfc = new JFileChooser();
            }
        // FileFilters
        Vector<String> extensions = new Vector<String>();

        extensions.add("pnml");
        extensions.add("xml");
        // extensions.add("xmi");
        jfc.setFileFilter(new FileFilterImpl(FileFilterImpl.OLDPNMLFilter,
                "Petri Net Markup Language older version (*.pnml,*.xml)", extensions));
        jfc.setFileFilter(new FileFilterImpl(FileFilterImpl.PNMLFilter,
                "Petri Net Markup Language (1.3.2) (*.pnml,*.xml)", extensions));
        jfc.showOpenDialog(null);

        if (jfc.getSelectedFile() != null) {
        	String abspath = jfc.getSelectedFile().getAbsolutePath();
        	ConfigurationManager.getConfiguration().setCurrentWorkingdir(abspath.substring(0,abspath.lastIndexOf(File.separator)));
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
    private IEditor openFile(File file, int filter) {
        IEditor editor = null;
        final PNMLImport pr;

        getMediator().getUi().getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if (filter == FileFilterImpl.PNMLFilter || filter == FileFilterImpl.SAMPLEFilter) {

            IViewController[] iVC = getMediator().findViewController(IStatusBar.TYPE);
            IStatusBar[] iSB = new IStatusBar[iVC.length];
            for (int i = 0; i < iSB.length; i++) {
                iSB[i] = (IStatusBar) iVC[i];
            }

            pr = new PNMLImport((ApplicationMediator) getMediator());

        } else
            if (filter == FileFilterImpl.OLDPNMLFilter) {
                pr = new OLDPNMLImport2((ApplicationMediator) getMediator());
            } else {
                pr = null;
            }
        if (pr != null) {
            boolean loadSuccess = false;
            InputStream is;

            // TODO Generate Thread
            try {
                is = new FileInputStream(file.getAbsolutePath());
                loadSuccess = pr.run(is);
            } catch (FileNotFoundException e) {
                String jarPath = file.getPath().replace('\\', '/');

                is = this.getClass().getResourceAsStream(jarPath);
                loadSuccess = pr.run(is);

                /*
                 * if (!loadSuccess) LoggerManager.error(Constants.FILE_LOGGER, "Could not open InputStream. " + file.getAbsolutePath());
                 */
                // }
            }
            if (loadSuccess) {
                editor = pr.getEditor()[pr.getEditor().length - 1];
                for (int i = 0; i < pr.getEditor().length; i++) {
                    if (editor instanceof EditorVC) {
                        ((EditorVC) pr.getEditor()[i]).setDefaultFileType(filter);
                        ((EditorVC) pr.getEditor()[i]).setName(file.getName());
                        ((EditorVC) pr.getEditor()[i]).setFilePath(file.getAbsolutePath());
                    }
                    // add recent
                	String abspath = file.getAbsolutePath();
                    if (filter == FileFilterImpl.PNMLFilter) {
                        ConfigurationManager.getConfiguration().addRecentFile(file.getName(), file.getAbsolutePath());
                        ConfigurationManager.getConfiguration().setCurrentWorkingdir(abspath.substring(0,abspath.lastIndexOf(File.separator)));
                    }
                    if (filter != FileFilterImpl.SAMPLEFilter) {
//                        ConfigurationManager.getConfiguration().setCurrentWorkingdir(abspath.substring(0,abspath.lastIndexOf(File.separator)));
                    }
                    // add Editor
                    LoggerManager.info(Constants.FILE_LOGGER, "Petrinet loaded from file: " + file.getAbsolutePath());
                }
            } else {
                ConfigurationManager.getConfiguration().removeRecentFile(file.getName(), file.getAbsolutePath());
                String arg[] = { file.getAbsolutePath() };
                JOptionPane.showMessageDialog(null, Messages.getString("File.Error.FileOpen.Text", arg), Messages
                        .getString("File.Error.FileOpen.Title"), JOptionPane.ERROR_MESSAGE);
            }

            getMediator().getUi().updateRecentMenu();
        }

        getMediator().getUi().getComponent().setCursor(Cursor.getDefaultCursor());
        return editor;
    }
    
	private IEditor importApromore() {
		IEditor editor = null;
		final PNMLImport pr;

		pr = new PNMLImport((ApplicationMediator) getMediator());
		boolean loadSuccess = false;

		InputStream is;

		ImportFrame aImp = new ImportFrame();
		if (aImp.getElement() == null) 
		{	
	        getMediator().getUi().getComponent().setCursor(Cursor.getDefaultCursor());
			return null;
		}
			ApromoreAccessObject aao = new ApromoreAccessObject();//"proxy.dhbw-karlsruhe.de", 8080);
		
		try {
			is = aao.getPNML(aImp.getElement()).getInputStream();
            loadSuccess = pr.run(is);
            		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		if (loadSuccess) {
			editor = pr.getEditor()[pr.getEditor().length - 1];
			for (int i = 0; i < pr.getEditor().length; i++) {

				// add Editor
				LoggerManager.info(Constants.FILE_LOGGER,
						"Petrinet loaded from Apromore");
				
				
				((EditorVC) pr.getEditor()[i]).setApromoreSettings(aImp.getEditSession());
			}
		} else {
			String arg[] = { "Apromore" };
			JOptionPane.showMessageDialog(null,
					Messages.getString("File.Error.FileOpen.Text", arg),
					Messages.getString("File.Error.FileOpen.Title"),
					JOptionPane.ERROR_MESSAGE);
		}
		
		getMediator().getUi().updateRecentMenu();

		if (ConfigurationManager.getConfiguration().getColorOn() == true) {
			// new NetColorScheme().update();
		}

		getMediator().getUi().getComponent()
		.setCursor(Cursor.getDefaultCursor());

		return editor;
	}

	public boolean exportApromore(EditorVC editor) {

		boolean succeed = false;
		getMediator().getUi().getComponent()
		.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		ExportFrame a;
		if (editor.getApromoreSettings() != null)
		{
			a = new ExportFrame(editor.getApromoreSettings());
		}
		else
		{
			a = new ExportFrame();
		}
		EditSessionType editSess = a.showDialog();
		
					if (editor != null) {
                        IViewController[] iVC = getMediator().findViewController(IStatusBar.TYPE);
                        IStatusBar iSB[] = new IStatusBar[iVC.length];
                        for (int i = 0; i < iSB.length; i++) {

                            iSB[i] = (IStatusBar) iVC[i];
                        }
                        PNMLExport pe = new PNMLExport(iSB);
                        pe.saveToFile(editor, "tmp.pnml");
                        LoggerManager.info(Constants.FILE_LOGGER, "Petrinet saved in file: "
                                + "tmp.pnml");

                        ApromoreAccessObject aao = new ApromoreAccessObject();
                        if (!aao.IsOnline())
                        {
                            getMediator().getUi().getComponent().setCursor(Cursor.getDefaultCursor());
                        	return false;
                        }
                        DataHandler dh = new DataHandler(new FileDataSource("tmp.pnml"));
                        ImportProcessOutputMsgType check = aao.export(dh, editSess); 
                        if (check.getResult().getCode() == 0)
                         { 
                        	 succeed = true;
                        editor.setSaved(true);
                         } 

					} 
		getMediator().getUi().getComponent()
				.setCursor(Cursor.getDefaultCursor());
		if (succeed == true)
            JOptionPane.showMessageDialog(null, Messages.getString("Apromore.Export.UI.Status.AproUpload"), Messages
                    .getString("Apromore.Export.UI.Status.AproUploadTitle"), JOptionPane.INFORMATION_MESSAGE);

		return succeed;

	}
}
