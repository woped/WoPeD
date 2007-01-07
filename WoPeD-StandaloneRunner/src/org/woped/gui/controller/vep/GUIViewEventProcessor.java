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
package org.woped.gui.controller.vep;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.net.URL;
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
import org.woped.core.gui.IEditorAware;
import org.woped.core.model.AbstractModelProcessor;
import org.woped.core.model.petrinet.GroupModel;
import org.woped.core.model.petrinet.SubProcessModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.controller.VisualController;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.editor.controller.vc.StructuralAnalysis;
import org.woped.editor.utilities.FileFilterImpl;
import org.woped.editor.utilities.Messages;
import org.woped.gui.AboutUI;
import org.woped.gui.BugReportUI;
import org.woped.gui.Constants;
import org.woped.gui.controller.DefaultApplicationMediator;
import org.woped.gui.controller.ViewEvent;
import org.woped.gui.controller.vc.MenuBarVC;
import org.woped.gui.controller.vc.ToolBarVC;
import org.woped.gui.help.HelpBrowser;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 * 
 */
public class GUIViewEventProcessor extends AbstractEventProcessor
{
	public GUIViewEventProcessor(int vepID, DefaultApplicationMediator mediator)
	{
		super(vepID, mediator);
	}

	public void processViewEvent(AbstractViewEvent event)
	{
		IEditor editor;
		if (event.getSource() instanceof EditorVC)
		{
			editor = (EditorVC) event.getSource();
		} else
		{
			editor = (EditorVC) getMediator().getUi().getEditorFocus();
		}
		switch (event.getOrder())
		{
		case AbstractViewEvent.NEW:
			editor = getMediator().createEditor(
					AbstractModelProcessor.MODEL_PROCESSOR_PETRINET, true);
			break;
		case AbstractViewEvent.OPEN_SUBPROCESS:

			if (event.getSource() instanceof EditorVC)
			{
				editor = (EditorVC) event.getSource();
			} else
			{
				editor = (EditorVC) getMediator().getUi().getEditorFocus();
			}

			Object cell = editor.getGraph().getSelectionCell();
			if (cell instanceof GroupModel)
			{

				cell = ((GroupModel) cell).getMainElement();

				if (cell instanceof SubProcessModel)
				{
					SubProcessModel model = (SubProcessModel) cell;

					IEditor subProcessEditor = getMediator()
							.createSubprocessEditor(
									AbstractModelProcessor.MODEL_PROCESSOR_PETRINET,
									true, editor, model);
				}
			} else
			{
				// error
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
			AboutUI about = null;
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
			BugReportUI bugreport = null;
			if (getMediator().getUi() != null
					&& getMediator().getUi().getComponent() instanceof JFrame)
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
				LoggerManager.error(Constants.GUI_LOGGER,
						"Cannot find HTML manual files. " + e.getMessage());
				JOptionPane.showMessageDialog(getMediator().getUi()
						.getComponent(), Messages
						.getString("Help.Message.HTMLManualFileNotFound"),
						Messages.getString("Help.Message.notFound"),
						JOptionPane.ERROR_MESSAGE);
			}
			break;
		case AbstractViewEvent.HELP_CONTENTS:
			try
			{
				showHelp(null, true);
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
			ToolBarVC toolbar = (ToolBarVC) getMediator().getViewController(
					ToolBarVC.ID_PREFIX);
			if (toolbar != null)
			{
				toolbar.getWoflanButton().setEnabled(
						ConfigurationManager.getConfiguration().isUseWoflan());
			}
			MenuBarVC menu = (MenuBarVC) getMediator().getViewController(
					ToolBarVC.ID_PREFIX);
			if (menu != null)
			{
				menu.getWoflanMenuItem().setEnabled(
						ConfigurationManager.getConfiguration().isUseWoflan());
			}
			// TODO: fire update event for editor
			break;

		case AbstractViewEvent.EXIT:
			quit();
			break;
		}
	}

	private void quit()
	{
		Vector editorList = new Vector(getMediator().getUi().getAllEditors());
		boolean canceled = false;
		for (Iterator iter = editorList.iterator(); iter.hasNext();)
		{
			IEditor editor = (IEditor) iter.next();
			processViewEvent(new ViewEvent(editor,
					AbstractViewEvent.VIEWEVENTTYPE_GUI,
					AbstractViewEvent.CLOSE));
			if (getMediator().getUi().getAllEditors().contains(editor))
			{
				canceled = true;
				break;
			}

		}
		if (!canceled)
		{
			ConfigurationManager.getConfiguration().setWindowX(
					getMediator().getUi().getX());
			ConfigurationManager.getConfiguration().setWindowY(
					getMediator().getUi().getY());
			ConfigurationManager.getConfiguration().setWindowSize(
					getMediator().getUi().getSize());
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
		editor.getContainer().setVisible(true);
		if (editor.getContainer() instanceof JInternalFrame)
		{
			try
			{
				((JInternalFrame) editor.getContainer()).setIcon(false);
			} catch (PropertyVetoException e)
			{
				LoggerManager.warn(Constants.GUI_LOGGER,
						"Could not deiconify the editor");
			}
		}
		VisualController.getInstance().propertyChange(
				new PropertyChangeEvent(this, "FrameSelection", null, editor));
		// notify the editor aware vc
		Iterator editorIter = getMediator().getEditorAwareVCs().iterator();
		while (editorIter.hasNext())
		{
			((IEditorAware) editorIter.next()).selectEditor(editor);
		}
	}

	/**
	 * Close the Editor... will start the the save procedure if necessary.
	 * 
	 * @param editor
	 * @return returns if editor could be close or not.
	 */
	private boolean closeEditor(IEditor editor)
	{
		boolean closeEditor = false;
		if (editor instanceof EditorVC)
		{
			EditorVC editorVC = (EditorVC) editor;

			if (editor.isSubprocessEditor())
			{
				StructuralAnalysis analysis = new StructuralAnalysis(editor);

				if (analysis.getNumNotStronglyConnectedNodes() > 0
						|| analysis.getNumSinkPlaces() > 1
						|| analysis.getNumSourcePlaces() > 1)
				{
					String errorMessage = Messages
							.getString("Action.CloseSubProcessEditor.StructuralAnalysisResult.Message.Start");

					if (analysis.getNumNotStronglyConnectedNodes() > 0)
					{
						errorMessage += Messages
								.getString("Action.CloseSubProcessEditor.StructuralAnalysisResult.Message.StronglyConnected");
					}
					if (analysis.getNumSourcePlaces() > 1)
					{
						errorMessage += Messages
								.getString("Action.CloseSubProcessEditor.StructuralAnalysisResult.Message.Source");
					}
					if (analysis.getNumSinkPlaces() > 1)
					{
						errorMessage += Messages
								.getString("Action.CloseSubProcessEditor.StructuralAnalysisResult.Message.Sink");
					}

					errorMessage += Messages
							.getString("Action.CloseSubProcessEditor.StructuralAnalysisResult.Message.End");

					int value = JOptionPane
							.showConfirmDialog(
									editorVC,
									errorMessage,
									Messages
											.getString("Action.CloseSubProcessEditor.StructuralAnalysisResult.Title"),
									JOptionPane.YES_NO_OPTION);

					if (value == JOptionPane.NO_OPTION)
					{
						closeEditor = true;
					}

				} else
				{
					closeEditor = true;
				}
			} else
			{
				if (!editorVC.isSaved())
				{
					// Check sample net
					if (editorVC.getDefaultFileType() != FileFilterImpl.SAMPLEFilter)
					{
						String args[] = { editorVC.getName() };

						int value = JOptionPane
								.showConfirmDialog(
										editorVC,
										Messages
												.getStringReplaced(
														"Action.SaveEditor.Confirmation.Message",
														args),
										Messages
												.getString("Action.SaveEditor.Confirmation.Title"),
										JOptionPane.YES_NO_CANCEL_OPTION);

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
			Iterator editorIter = getMediator().getEditorAwareVCs().iterator();
			while (editorIter.hasNext())
			{
				((IEditorAware) editorIter.next()).removeEditor(editor);
			}
			VisualController.getInstance().propertyChange(
					new PropertyChangeEvent(this, "InternalFrameCount", null,
							editor));
		}
		return closeEditor;
	}

	private void showHelp(String contentFileName, boolean showContents)
			throws Exception
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
			filename = url.toExternalForm().concat("/html/").concat(
					Messages.getString("Help.Dir")).concat("/" + filename);
			contentFileName = url.toExternalForm().concat("/html/").concat(
					Messages.getString("Help.Dir")).concat(
					"/" + contentFileName);
		} else
		{
			// locate HTML files in local folder
			File f = new File(".");
			String filePath = "file:/" + f.getAbsolutePath();
			int dotAt = filePath.lastIndexOf(".");
			filename = filePath.substring(0, dotAt) + "doc/html/"
					+ Messages.getString("Help.Dir") + "/" + filename;
			contentFileName = filePath.substring(0, dotAt) + "doc/html/"
					+ Messages.getString("Help.Dir") + "/" + contentFileName;
		}
		/*
		 * { // locate HTML files in local folder filename =
		 * this.getClass().getResource("/").toExternalForm().concat("../doc/doc/html/").concat(Messages.getString("Help.Dir")).concat("/" +
		 * filename); contentFileName =
		 * this.getClass().getResource("/").toExternalForm().concat("../doc/doc/html/").concat(Messages.getString("Help.Dir")).concat("/" +
		 * contentFileName); System.out.println(filename); }
		 */

		HelpBrowser br = HelpBrowser.getInstance();
		if (showContents)
		{
			br.init(contentFileName, filename, contentFileName);
		} else
		{
			br.init(filename, filename, contentFileName);
		}
	}
}
