package org.woped.qualanalysis.p2t;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.util.LinkedHashMap;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;

import org.woped.core.controller.IEditor;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.utilities.LoggerManager;
import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.paraphrasing.Constants;
import org.woped.qualanalysis.paraphrasing.controller.WebServiceThread;
import org.woped.qualanalysis.service.IQualanalysisService;
import org.woped.qualanalysis.service.QualAnalysisServiceFactory;

@SuppressWarnings("serial")
/**
 * The sidebar to be used for displaying of the natural text-presentation
 * (Process2Text).
 *
 */
public class P2TSideBar extends JPanel implements ActionListener {

	private IEditor editor = null;
	private JEditorPane textpane = null;
	private Process2Text naturalTextParser = null;
	private JButton buttonLoad = null;
	private JButton buttonExport = null;
	private JLabel labelLoading = null;
	private WebServiceThread webService = null;
	private boolean threadInProgress = false;
	private boolean firstTimeDisplayed = false;

	/**
	 * 
	 * @param currentEditor
	 *            Editor in which the instance of the sidebar is used
	 */
	public P2TSideBar(IEditor currentEditor) {
		super();
		editor = currentEditor;

		addComponents();
		showLoadingAnimation(false);
	}

	/**
	 * Getter for the used parser
	 * 
	 * @return org.woped.qualanalysis.p2t.Process2Text used parser
	 */
	public org.woped.qualanalysis.p2t.Process2Text getNaturalTextParser() {
		return naturalTextParser;
	}

	/**
	 * Setter for the used parser
	 * 
	 * @param naturalTextParser
	 *            Parser instance to be used in this sidebar
	 */
	public void setNaturalTextParser(org.woped.qualanalysis.p2t.Process2Text naturalTextParser) {
		this.naturalTextParser = naturalTextParser;
	}

	/**
	 * Getter for the IEditor
	 * 
	 * @return IEditor the editor in which the instance of the sidebar is used
	 */
	public IEditor getEditor() {
		return editor;
	}

	public void defineButtonSize(JButton button) {
		Dimension dim = new Dimension(20, 20);
		button.setSize(dim);
		button.setMinimumSize(dim);
		button.setMaximumSize(dim);
		button.setPreferredSize(dim);
		button.setBorderPainted(false);
	}

	public JButton getbuttonLoad() {
		if (buttonLoad == null) {
			buttonLoad = new JButton();
			buttonLoad.setIcon(Messages.getImageIcon("Paraphrasing.Load"));
			buttonLoad.setToolTipText(Messages.getString("Paraphrasing.Load.Title"));
			buttonLoad.setEnabled(true);
			buttonLoad.addActionListener(this);
			defineButtonSize(buttonLoad);
			buttonLoad.setBorderPainted(false);
		}

		return buttonLoad;
	}

	public JButton getbuttonExport() {
		if (buttonExport == null) {
			buttonExport = new JButton();
			buttonExport.setIcon(Messages.getImageIcon("Paraphrasing.Export"));
			buttonExport.setToolTipText(Messages.getString("Paraphrasing.Export.Title"));
			buttonExport.setEnabled(true);
			buttonExport.addActionListener(this);
			defineButtonSize(buttonExport);
			buttonExport.setBorderPainted(false);
		}

		return buttonExport;
	}

	/**
	 * Method to initialize and add the the components to the sidebar
	 */
	private void addComponents() {
		BorderLayout layout = new BorderLayout();
		this.setLayout(layout);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(getbuttonLoad());
		buttonPanel.add(getbuttonExport());

		this.add(buttonPanel, BorderLayout.NORTH);

		this.labelLoading = new JLabel("", Messages.getImageIcon("Paraphrasing.Output.Load.Animation"), JLabel.CENTER);

		textpane = new JEditorPane("text/html", "");
		textpane.addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent hle) {
				if (hle.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					highlightElement(hle.getDescription());
				}
			}
		});
		textpane.setAutoscrolls(true);
		textpane.setEditable(false);
		textpane.setMinimumSize(new Dimension(150, 100));

		this.add(labelLoading, BorderLayout.SOUTH);
		this.add(textpane, BorderLayout.CENTER);
	}

	/**
	 * Method to handle the highlighting of the elements and in the text
	 * 
	 * @param ids;
	 *            ID of the element and the text-passage to be highlighted
	 */
	private void highlightElement(String ids) {
		String[] singleIDs = ids.split(",");

		for (String id : singleIDs) {
			if(id.contains("t")) {
				highlightIDinText(id);
				id = id.split("_op_")[0]; // ignore the path option
				highlightIDinProcess(id);
			}
		}
	}

	/**
	 * Handles the highlighting of the elements in the text
	 * 
	 * @param id;
	 *            ID of the element to be set highlighted
	 */
	private void highlightIDinProcess(String id) {
		ModelElementContainer mec = editor.getModelProcessor().getElementContainer();
		mec.getElementById(id).setHighlighted(true);
	}

	/**
	 * 
	 * Highlights passages linked to the given ID within the displayed text
	 * 
	 * @param id,
	 *            ID of the element of which the corresponding text is to be
	 *            highlighted
	 */
	public void highlightIDinText(String id) {
		clean();

		// Check if there is a linked text
		if (naturalTextParser != null) {
			// get the text(s) of the ID
			String[] textsToHighlight = naturalTextParser.getLinkedTexts(id);
			for (String find : textsToHighlight) {

				for (int index = 0; index + find.length() < textpane.getText().length(); index++) {
					String match = null;

					try {
						match = textpane.getText(index, find.length());
					} catch (BadLocationException e1) {
						break; // the end of the displayed text is reached
					}
					// if the text is found
					if (find.equals(match)) {
						javax.swing.text.DefaultHighlighter.DefaultHighlightPainter highlightPainter = null;
						highlightPainter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(
								new Color(255, 0, 0, 128));

						try {
							textpane.getHighlighter().addHighlight(index, index + find.length(), highlightPainter);
						} catch (BadLocationException e) {
							// ignore
						}
					}
				}
			}
		}
	}

	/**
	 * If the reload-button is pressed and the webservice thread is not in
	 * progress it will be started.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// Process "load" button
		if (e.getSource() == this.buttonLoad) {
			showLoadingAnimation(true);

			new WebServiceThread(this);

			// If we already have a text/process description, ask for overwrite
			// confirmation.
			if (naturalTextParser != null && naturalTextParser.getXmlText().length() > 0) {
				if (JOptionPane.showConfirmDialog(null, Messages.getString("Paraphrasing.Load.Question.Content"),
						Messages.getString("Paraphrasing.Load.Question.Title"),
						JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
					return;
			}

			if (this.getThreadInProgress() == false) {
				getText();
			} else {
				JOptionPane.showMessageDialog(null,
						Messages.getString("Paraphrasing.Webservice.ThreadInProgress.Message"),
						Messages.getString("Paraphrasing.Webservice.Error.Title"), JOptionPane.INFORMATION_MESSAGE);
			}
			showLoadingAnimation(false);
		}
		// Process "export "button
		else if (e.getSource() == this.buttonExport) {

			boolean fileTypeOk = false;
			if (this.textpane.getText().length() > 0) {
				JFileChooser jFileChooser = new JFileChooser();
				jFileChooser.setFileFilter(new FileFilter() {
					public boolean accept(File f) {
						return f.getName().toLowerCase().endsWith(".txt");
					}

					public String getDescription() {
						return "*.txt File";
					}
				});
				while (!fileTypeOk) {
					if (jFileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
						String path = jFileChooser.getSelectedFile().getAbsolutePath();
						if (!path.endsWith(".txt")) {
							path = path + ".txt";
						}

						try {
							fileTypeOk = true;
							File file = new File(path);

							FileWriter out = new FileWriter(file);

							out.write("Created with Workflow PetriNet Designer Version "
									+ Messages.getString("Application.Version") + " (woped.org) \n\n");

							LinkedHashMap<String, String> textelements = naturalTextParser.getTextelements();

							for (String key : textelements.keySet()) {
								out.write(textelements.get(key) + "\r\n");
							}

							LoggerManager.info(Constants.PARAPHRASING_LOGGER, " Description exported to: " + path);

							out.close();
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(null,
									Messages.getString("Paraphrasing.Export.Error.Message") + "\n" + ex.getMessage(),
									Messages.getString("Paraphrasing.Export.Error.Title"),
									JOptionPane.INFORMATION_MESSAGE);
						}

					} else {
						fileTypeOk = true;
					}
				}

			} else {
				JOptionPane.showMessageDialog(null, Messages.getString("Paraphrasing.Export.Numberelements.Message"),
						Messages.getString("Paraphrasing.Export.Numberelements.Title"),
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	/**
	 * Starts the webservice to get the description of the Petri-Net.
	 */
	private void getText() {
		this.textpane.setText(Messages.getString("P2T.loading"));
		clean();

		// Ensure their are no arc weights
		if(editor.getModelProcessor().usesArcWeights()){
			this.textpane.setText(Messages.getString("P2T.Error.ArcWeights.title"));
			showErrorMessage("P2T.Error.ArcWeights");
			return;
		}

		this.setThreadInProgress(true);
		webService = new WebServiceThread(this);
		webService.start();
		while (!webService.getIsFinished()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				// ignore
			}
		}
		this.textpane.setText("");

		if (naturalTextParser != null)
			this.textpane.setText(naturalTextParser.getHtmlText());

		setThreadInProgress(false);
		webService = null;
	}

	private void showErrorMessage(String resourceKey) {
		Component parent = editor.getMediator().getUi().getComponent();
		String message = Messages.getString(resourceKey + ".message");
		String title = Messages.getString(resourceKey + ".title");
		JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Setter of the thread-in-progress flag
	 * 
	 * @param b sets the in-progress-state-flag of the webservice
	 */
	public void setThreadInProgress(boolean b) {
		threadInProgress = b;
	}

	/**
	 * Getter of the Thread in
	 * 
	 * @return boolean; returns true if the webservice is still supposed to be
	 *         in progress
	 */
	private boolean getThreadInProgress() {
		return threadInProgress;
	}

	/**
	 * Removes the highlights from the elements and the text. Afterwards those
	 * are repainted.
	 */
	public void clean() {
		textpane.getHighlighter().removeAllHighlights();
		ModelElementContainer mec = editor.getModelProcessor().getElementContainer();
		mec.removeAllHighlighting();
		editor.getGraph().refreshNet();
		editor.getGraph().repaint();
	}

	/**
	 * Shows or hides the loading animation label.
	 * 
	 * @param show
	 */
	public void showLoadingAnimation(boolean show) {
		this.labelLoading.setVisible(show);
	}

	/**
	 * Shows or hides the buttons.
	 * 
	 * @param enable
	 */
	public void enableButtons(boolean enable) {
		this.buttonLoad.setEnabled(enable);
		this.buttonExport.setEnabled(enable);
	}

	/**
	 * Callback method which automatically gets called every time the side bar
	 * gets displayed or hidden. Can be used to prepare / update the displayed
	 * content, if required.
	 * 
	 * @param visible
	 */

	public void onSideBarShown(boolean visible) {
		//checkt, ob der Prozess sound ist, bevor er an den Webservice übergeben wird. Alle anderen Prozesse werden nicht übersetzt.
		IQualanalysisService analyseService = QualAnalysisServiceFactory.createNewQualAnalysisService(editor);
		if(analyseService.isWorkflowNet()) {
			if (visible == true && this.firstTimeDisplayed == false) {
				getText();
				this.firstTimeDisplayed = true;
			}
		}
		else
			JOptionPane.showMessageDialog(null,
					Messages.getString("PetriNet.NotSound"),
					Messages.getString("AnalysisSideBar.SoundnessAnalysis"), JOptionPane.ERROR_MESSAGE);
	}
}