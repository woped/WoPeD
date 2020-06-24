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
package org.woped.starter.controller.vep;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.security.AccessControlException;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractEventProcessor;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IEditor;
import org.woped.core.controller.ViewEvent;
import org.woped.core.gui.IEditorAware;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.GroupModel;
import org.woped.core.model.petrinet.SubProcessModel;
import org.woped.core.utilities.FileFilterImpl;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.controller.VisualController;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.editor.controller.vc.SubprocessEditorVC;
import org.woped.editor.help.HelpBrowser;
import org.woped.editor.help.action.LaunchDefaultBrowserAction;
import org.woped.file.t2p.T2PUI;
import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.service.IQualanalysisService;
import org.woped.qualanalysis.service.QualAnalysisServiceFactory;
import org.woped.starter.AboutUI;
import org.woped.starter.BugReportUI;
import org.woped.starter.Constants;
import org.woped.starter.RegistrationUI;
import org.woped.starter.controller.vc.DefaultApplicationMediator;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 * 
 */
public class GUIViewEventProcessor extends AbstractEventProcessor
{
	public GUIViewEventProcessor(DefaultApplicationMediator mediator)
	{
		super(mediator);
	}

	public void processViewEvent(AbstractViewEvent event)
	{
		IEditor editor;
		if (event.getSource() instanceof EditorVC)
		{
			editor = (EditorVC) event.getSource();
		} else
		{
			editor = getMediator().getUi().getEditorFocus();
		}
		switch (event.getOrder())
		{
		case AbstractViewEvent.T2P:				
			T2PUI t2p;
			if (getMediator().getUi() != null
					&& getMediator().getUi().getComponent() instanceof JFrame)
			{
				t2p = new T2PUI((JFrame) getMediator().getUi(), getMediator());
			} else
			{
				t2p = new T2PUI(getMediator());
			}
			t2p.setVisible(true);
			break;
		case AbstractViewEvent.NEW:
			getMediator().createEditor(true);
			break;
		case AbstractViewEvent.OPEN_SUBPROCESS:
			if (event.getSource() instanceof EditorVC)
			{
				editor = (EditorVC) event.getSource();
			} else
			{
				editor = getMediator().getUi().getEditorFocus();
			}
			Object cell = editor.getGraph().getSelectionCell();
			if (cell instanceof GroupModel)
			{

				cell = ((GroupModel) cell).getMainElement();
				if (cell instanceof SubProcessModel)
				{
					SubProcessModel model = (SubProcessModel) cell;
				
					IEditor subEditor = getMediator().createSubprocessEditor(
							true, editor, model);
					//rotate of sub process like main process
					if  (editor.isRotateSelected() != model.getDirection()){
						subEditor.rotateLayout();
						model.setDirection(editor.isRotateSelected());
					}
				}
			}
			break;

		case AbstractViewEvent.SELECT_EDITOR:
			selectEditor(editor);
			break;

		case AbstractViewEvent.INCONIFY_EDITOR:
			getMediator().getUi().hideEditor(editor);
			VisualController.getInstance().propertyChange(
					new PropertyChangeEvent(this, "InternalFrameCount", null,
							editor));
			break;
		case AbstractViewEvent.CLOSE:
			closeEditor(editor);
			break;
		case AbstractViewEvent.ABOUT:
			AboutUI about;
			if (getMediator().getUi() != null
					&& getMediator().getUi().getComponent() instanceof JFrame)
			{
				about = new AboutUI((JFrame) getMediator().getUi());
			} else
			{
				about = new AboutUI();
			}
			about.setVisible(true);
			break;

		case AbstractViewEvent.BUGREPORT:
			BugReportUI bugReport;
			if (getMediator().getUi() != null
					&& getMediator().getUi().getComponent() instanceof JFrame)
			{
				bugReport = new BugReportUI((JFrame) getMediator().getUi());
			} else
			{
				bugReport = new BugReportUI();
			}
			bugReport.setVisible(true);
			break;

		case AbstractViewEvent.HELP:
			try
			{
				showHelpPage((String)event.getData());
			} catch (Exception e)
			{
				LoggerManager.error(Constants.GUI_LOGGER,
						"Cannot find HTML manual files in " + event.getData() + "." + e.getMessage());
				JOptionPane.showMessageDialog(getMediator().getUi()
						.getComponent(), Messages
						.getString("Help.Message.HTMLManualFileNotFound") + " in " + event.getData(),
						Messages.getString("Help.Message.notFound"),
						JOptionPane.ERROR_MESSAGE);
			}
			break;
		case AbstractViewEvent.HELP_CONTENTS:
			try
			{
				showHelpContents();
			} catch (Exception e)
			{
				LoggerManager.error(Constants.GUI_LOGGER,
						"Cannot find HTML contents file. " + e.getMessage());
				JOptionPane.showMessageDialog(getMediator().getUi()
						.getComponent(), Messages
						.getString("Help.Message.HTMLManualFileNotFound"),
						Messages.getString("Help.Message.notFound"),
						JOptionPane.ERROR_MESSAGE);
			}
			break;

		case AbstractViewEvent.UPDATE:
			break;
		case AbstractViewEvent.EXIT:
			quit();
			break;
        case AbstractViewEvent.ZOOMED:
            break;
        case AbstractViewEvent.FACEBOOK:
        	new LaunchDefaultBrowserAction(Messages.getString("Community.Facebook.link"), null).displayURL();
            break;
        case AbstractViewEvent.TWITTER:
        	new LaunchDefaultBrowserAction(Messages.getString("Community.Twitter.link"), null).displayURL();
            break;
        case AbstractViewEvent.GOOGLEPLUS:
			//noinspection SpellCheckingInspection
			new LaunchDefaultBrowserAction(Messages.getString("Community.Googleplus.link"), null).displayURL();
            break;
        case AbstractViewEvent.COMMUNITY:
        	new LaunchDefaultBrowserAction(Messages.getString("Community.Community.link"), null).displayURL();
            break;
    	case AbstractViewEvent.REGISTER:
    		new RegistrationUI((JFrame) getMediator().getUi(), false);
    		break;
		}
	}

	private void quit()
	{
		Vector<IEditor> editorList = new Vector<>(getMediator().getUi()
				.getAllEditors());
		boolean canceled = false;
		for (IEditor editor : editorList) {
			processViewEvent(new ViewEvent(editor,
					AbstractViewEvent.VIEWEVENTTYPE_GUI,
					AbstractViewEvent.CLOSE));
			if (getMediator().getUi().getAllEditors().contains(editor)) {
				canceled = true;
				break;
			}

		}
		if (!canceled)
		{
			// If window is maximized don't store the current size, since that
			// would lead to strange results when later leaving the maximised state
			// such as too large windows and negative positions
			ConfigurationManager.getConfiguration().setMaximizeWindow(
					getMediator().getUi().isMaximized());
			if (!getMediator().getUi().isMaximized()) {
			ConfigurationManager.getConfiguration().setWindowX(
					getMediator().getUi().getX());
			ConfigurationManager.getConfiguration().setWindowY(
					getMediator().getUi().getY());
			ConfigurationManager.getConfiguration().setWindowSize(
					getMediator().getUi().getSize());
			}
			ConfigurationManager.getConfiguration().saveConfig();
			try
			{
				LoggerManager.info(Constants.GUI_LOGGER, "EXIT WoPeD");
				System.exit(0);
			} catch (AccessControlException ace)
			{
				getMediator().getUi().setVisible(false);
			}
		} else
		{
			LoggerManager.debug(Constants.GUI_LOGGER,
					"User has canceled quit-operation.");
		}
	}

	private void selectEditor(IEditor editor)
	{
		((EditorVC)editor).getEditorPanel().getContainer().setVisible(true);
		if (((EditorVC)editor).getEditorPanel().getContainer() instanceof JInternalFrame)
		{
			try
			{
				((JInternalFrame) ((EditorVC)editor).getEditorPanel().getContainer()).setIcon(false);
			} catch (PropertyVetoException e)
			{
				//noinspection SpellCheckingInspection
				LoggerManager.warn(Constants.GUI_LOGGER,
						"Could not deiconify the editor");
			}
		}
		VisualController.getInstance().propertyChange(
				new PropertyChangeEvent(this, "FrameSelection", null, editor));
		// notify the editor aware vc
		for (Object o : getMediator().getEditorAwareVCs()) {
			((IEditorAware) o).selectEditor(editor);
		}
	}

	/**
	 * Close the Editor... will start the the save procedure if necessary.
	 * 
	 * @param editor the editor to close
	 * @return returns if editor could be close or not.
	 */
	private boolean closeEditor(IEditor editor)
	{
		boolean closeEditor = false;
		if (editor instanceof EditorVC)
		{
			EditorVC editorVC = (EditorVC) editor;
			if ((editor instanceof SubprocessEditorVC) && !editorVC.isTokenGameEnabled())
			{

				@SuppressWarnings("SpellCheckingInspection") IQualanalysisService qualanService = QualAnalysisServiceFactory.createNewQualAnalysisService(editor);
                
				String inputID = ((SubprocessEditorVC)editor).getSubprocessInput().getId();
				String outputID = editor.getId();

				// Try to get the first source place of the model as well as the 
				// first sink place
				// Note that there is a chance neither a source nor a sink actually exist
				// so we need to check whether the iterator is valid!!
				String ainputID = null;
				String aoutputID = null;
				Iterator<AbstractPetriNetElementModel> i = qualanService.getSinkPlaces().iterator();
				if (i.hasNext()) {
					ainputID = i.next().getId();
				}
				i = qualanService.getSourcePlaces().iterator();
				if (i.hasNext())
					aoutputID = i.next().getId();
				
				if (qualanService.getNotStronglyConnectedNodes().size() > 0
						|| qualanService.getSinkPlaces().size() > 1
						|| qualanService.getSourcePlaces().size() > 1
						|| inputID.equals(ainputID)
						|| outputID.equals(aoutputID))
				{
					String errorMessage = Messages
							.getString("Action.CloseSubProcessEditor.StructuralAnalysisResult.Message.Start");

					if (qualanService.getNotStronglyConnectedNodes().size() > 0)
					{
						errorMessage += Messages
								.getString("Action.CloseSubProcessEditor.StructuralAnalysisResult.Message.StronglyConnected");
					}
					if (qualanService.getSourcePlaces().size() > 1)
					{
						errorMessage += Messages
								.getString("Action.CloseSubProcessEditor.StructuralAnalysisResult.Message.Source");
					} else
					{
						if (inputID.equals(ainputID))
						{
							errorMessage += Messages
							.getString("Action.CloseSubProcessEditor.StructuralAnalysisResult.Message.Input");
						}
					}

					if (qualanService.getSinkPlaces().size() > 1)
					{
						errorMessage += Messages
								.getString("Action.CloseSubProcessEditor.StructuralAnalysisResult.Message.Sink");
					} else
					{
						if (outputID.equals(aoutputID))
						{
							errorMessage += Messages
							.getString("Action.CloseSubProcessEditor.StructuralAnalysisResult.Message.Output");
						}
					}

					errorMessage += Messages
							.getString("Action.CloseSubProcessEditor.StructuralAnalysisResult.Message.End");

					String textMessages[] = { 	Messages.getString("Dialog.Yes"),
												Messages.getString("Dialog.No")
						};

					int value = JOptionPane.showOptionDialog(editorVC.getEditorPanel(),
									errorMessage,
									Messages.getString("Action.CloseSubProcessEditor.StructuralAnalysisResult.Title"),
									JOptionPane.YES_NO_OPTION,
									JOptionPane.WARNING_MESSAGE,
									null, 
									textMessages,
									textMessages[0]);

					if (value == JOptionPane.YES_OPTION)
					{
						closeEditor = true;
					}

				} else
				{
					closeEditor = true;
				}
			} 
			else if(editorVC.isSubprocessEditor() && editorVC.isTokenGameEnabled())
			{
				closeEditor = true;
			}
			else
			{
				if (!editorVC.isSaved())
				{
					// Check sample net
					if (editorVC.getDefaultFileType() != FileFilterImpl.SAMPLEFilter)
					{
						String args[] = { editorVC.getName() };

						String textMessages[] = { 	Messages.getString("Dialog.Yes"),
													Messages.getString("Dialog.No"),
													Messages.getString("Dialog.Cancel")
						};

						int value = JOptionPane.showOptionDialog(editorVC.getEditorPanel(),
								Messages
								.getStringReplaced(
										"Action.Confirm.File.Save.Text",
										args),
								Messages.getString("Action.Confirm.File.Save.Title"),
								JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.ERROR_MESSAGE,
								null, 
								textMessages,
								textMessages[0]);

						if (value == (JOptionPane.YES_OPTION))
						{
							// try to save
							getMediator()
									.processViewEvent(
											new ViewEvent(
													editor,
													AbstractViewEvent.VIEWEVENTTYPE_FILE,
													AbstractViewEvent.SAVE));
							closeEditor = editorVC.isSaved();
						} else if (value == JOptionPane.NO_OPTION)
						{
							closeEditor = true;
						} else if (value == JOptionPane.CANCEL_OPTION)
						{
							closeEditor = false;
						}

					} else
					{
						closeEditor = true;
					}
				} else
				{
					closeEditor = true;
				}
			}
		}

		if (closeEditor)
		{
			getMediator().removeViewController(editor);
			// notify the editor aware vc
			for (Object o : getMediator().getEditorAwareVCs()) {
				((IEditorAware) o).removeEditor(editor);
			}
			VisualController.getInstance().propertyChange(
					new PropertyChangeEvent(this, "InternalFrameCount", null,
							editor));
		}
		return closeEditor;
	}

	private void showHelpPage(String fileName)
			throws Exception
	{
		HelpBrowser br = HelpBrowser.getInstance();
		br.showURL(fileName);
	}
	private void showHelpContents() throws Exception
	{
		HelpBrowser br = HelpBrowser.getInstance();
		br.showURL(Messages.getString("Help.File.Contents"));
	}
}
