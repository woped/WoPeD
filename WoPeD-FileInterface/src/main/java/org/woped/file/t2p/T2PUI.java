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
package org.woped.file.t2p;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;

//import T2PWebservice.T2PController;  /* Uncomment to call T2P locally, don't forget to update dependency in project structure
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.IEditor;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.file.PNMLImport;
import org.woped.gui.lookAndFeel.WopedButton;
import org.woped.gui.translations.Messages;
import org.woped.core.config.ConfigurationManager;

/**
 * @author <a href="mailto:freytag@dhbw-karlsruhe.de">Thomas Freytag </a> <br>
 *         <br>
 *         TODO: DOCUMENTATION (tfreytag)
 * 
 *         17.01.2005
 */

@SuppressWarnings("serial")
public class T2PUI extends JDialog {
	private JTextAreaWithHint textArea;
	
	private JDialog loadDialog;
	
	private AbstractApplicationMediator mediator;
	
	private boolean requested = false;
	private SwingWorker<HttpResponse, Void> bgTask;
	
	private String inputText;
	
	public T2PUI(AbstractApplicationMediator mediator) {
		this(null, mediator);
	}

	/**
	 * Constructor for AboutUI.
	 * 
	 * @param owner
	 * @throws HeadlessException
	 */
	public T2PUI(Frame owner, AbstractApplicationMediator mediator) throws HeadlessException {
		super(owner, true);
		this.mediator = mediator;
		initialize();
	}
	/**
	 * This method initializes and layouts the about information
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setVisible(false);
		this.getContentPane().setLayout(new BorderLayout());
		this.setUndecorated(false);
		this.setResizable(true);
		
		textArea = new JTextAreaWithHint();
		
		this.setTitle(Messages.getString("T2P.tooltip"));
		this.getContentPane().add(wrapTextArea(initializeTextArea(textArea)), BorderLayout.CENTER);
		this.getContentPane().add(initializeButtonsPanel(), BorderLayout.SOUTH);
		
		this.pack();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((screenSize.width - this.getWidth()) / 3, (screenSize.height - this.getHeight()) / 3);
		Dimension size = new Dimension(600, 440);
		this.setSize(size);

		// set prev text if available
		
		int index = 0;
		boolean doesContain = false;
		
		if (mediator.getViewControllers().containsKey("EDITOR_VC_" + index)) {
			doesContain = true;
			while(mediator.getViewControllers().containsKey("EDITOR_VC_" + index)) {
				index++;
			}
			index--;
		}
		
		if (doesContain) {
			String lastTextInput = ((EditorVC) mediator.getViewControllers().get("EDITOR_VC_" + index)).getEditorPanel().getT2PText();
			textArea.setText(lastTextInput);
		}
	}
	
	
	private JTextAreaWithHint initializeTextArea(JTextAreaWithHint ta) {
		
		Font f = new Font("Lucia Grande", Font.PLAIN, 13);
		String hint = Messages.getString("T2PUI.HowTo");
		
		ta.setFont(f);
		ta.changeHintText(hint);
		ta.setLineWrap(true);
		ta.setWrapStyleWord(true);
		
		ta.requestFocus();
		ta.requestFocusInWindow();
		ta.setMargin(new Insets(10, 10, 10, 10));
		
		return ta;
	}
	
	private JScrollPane wrapTextArea(JTextAreaWithHint ta) {
		JScrollPane scrollPane = new JScrollPane(ta);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		return scrollPane;
	}
		
	private JPanel initializeButtonsPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
		
		String[] lang = { Messages.getString("T2PUI.Lang"), Messages.getString("T2PUI.Lang.English")};
		JComboBox<String> langBox = new JComboBox<String>(lang);
		langBox.setSelectedIndex(1);
		
		// TODO: Listener for ComboBox Changes
		
		WopedButton btnGenerate = new WopedButton(new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				request();
			}
		});
		
		btnGenerate.setMnemonic(KeyEvent.VK_A);
		btnGenerate.setText(Messages.getString("T2PUI.Button.Generate.Text"));
		btnGenerate.setIcon(new ImageIcon(getClass().getResource(Messages.getString("T2PUI.Button.Generate.Icon"))));
		
		
		WopedButton btnErase = new WopedButton(new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				clearTextArea();
			}
		});

		btnErase.setMnemonic(KeyEvent.VK_L);
		btnErase.setText(Messages.getString("T2PUI.Button.Clear.Text"));
		btnErase.setIcon(new ImageIcon(getClass().getResource(Messages.getString("T2PUI.Button.Clear.Icon"))));
		
		
		WopedButton btnUpload = new WopedButton(new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				readFile();
			}
		});

		btnUpload.setMnemonic(KeyEvent.VK_C);
		btnUpload.setText(Messages.getString("T2PUI.Button.Read.Text"));
		btnUpload.setIcon(new ImageIcon(getClass().getResource(Messages.getString("T2PUI.Button.Read.Icon"))));
		
		buttonPanel.add(btnUpload);
		buttonPanel.add(btnErase);
		buttonPanel.add(langBox);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(btnGenerate);
		
		return buttonPanel;
	}
	
	private void request() {
		if (requested) return;
		requested = true;
		
		inputText = textArea.getText();

		if (!inputText.isEmpty()) {
			// Call of T2P via webservice
			httpBackgroundWorker(inputText);
			showLoadingBox();
			// End of option to call T2P webservice

// Alternative code to call T2P locally
			/*try {
				T2PController tp = new T2PController(inputText);
				String pnml = tp.generatePetrinetFromText();
			}
			catch (Exception e) {
				e.getStackTrace();
			}*/
// End of alternative code
		} else {
			showErrorPopUp("T2PUI.NoText.Title", "T2PUI.NoText.Text");
		}

		requested = false;
	}
	
	private void showLoadingBox() {
		JOptionPane jop = new JOptionPane();
	    jop.setMessageType(JOptionPane.INFORMATION_MESSAGE);
	    jop.setMessage(Messages.getString("T2PUI.Loading.Text"));
	    
	    loadDialog = jop.createDialog(this, Messages.getString("T2PUI.Loading.Title"));
	    jop.setOptions(new String[]{Messages.getString("T2PUI.Loading.Cancel")});
	    loadDialog.setVisible(true);
	    
	    // Thread gets blocked and awaits an UI action.
	    
	    if (bgTask != null && !bgTask.isDone() && !bgTask.isCancelled()) {
	    	bgTask.cancel(true);
	    }
	}
	
	private void displayPMNL(String pnml) {
		PNMLImport pnmlImport = new PNMLImport(mediator);
		InputStream stream = new ByteArrayInputStream(pnml.getBytes(StandardCharsets.UTF_8));

		pnmlImport.run(stream, Messages.getString("Document.T2P.Output"), true);

		IEditor[] editor = pnmlImport.getEditor();
		
		EditorVC evc = ((EditorVC) editor[0]);
		
		try {
			if (inputText != null) {
				evc.getEditorPanel().showT2PBar(inputText);
			}
			
			evc.startBeautify(0, 0, 0);

		} catch (ArithmeticException exc) {
			close();
			
			// error popup
		}

		close();
	}
	
	private void showErrorPopUp(String titleId, String msgId) {
		String text[] = { Messages.getString("Dialog.Ok") };
		
		String msg = Messages.getStringReplaced(msgId, null);
		String title = Messages.getString(titleId);
		int optionType = JOptionPane.YES_NO_CANCEL_OPTION;
		int messageType = JOptionPane.ERROR_MESSAGE;
		
		int value = JOptionPane.showOptionDialog(
				null, msg, title, optionType, messageType, null, text, text[0]
		);
	}
	
	
	private void httpBackgroundWorker(String text) {
		if (bgTask != null && !bgTask.isDone()) return;


		String url = "http://" + ConfigurationManager.getConfiguration().getText2ProcessServerHost() + ":" + ConfigurationManager.getConfiguration().getText2ProcessServerPort() + ConfigurationManager.getConfiguration().getText2ProcessServerURI() + "/generate";

		bgTask = new SwingWorker<HttpResponse, Void>() {
			HttpRequest req;
			
			@Override
			protected HttpResponse doInBackground() throws Exception {
				req = new HttpRequest(url, text);
				return req.getResponse();
			}

			@Override
			protected void done() {
				try {
					HttpResponse res = get();
					
					String PNML = res.body;
					int httpCode = res.responseCode;
					
					if (!PNML.isEmpty() && httpCode == 200) {
						displayPMNL(PNML);
					} else {
						if (req != null) req.cancel();
						if (loadDialog != null) loadDialog.dispose();
						
						if (httpCode == 400) {
							showErrorPopUp("T2PUI.400Error.Title", "T2PUI.400Error.Text");
						} else if (httpCode == 500) {
							showErrorPopUp("T2PUI.500Error.Title", "T2PUI.GeneralError.Text");
						} else if (httpCode == 503) {
							showErrorPopUp("T2PUI.503Error.Title", "T2PUI.503Error.Text");
						} else {
							showErrorPopUp("T2PUI.GeneralError.Title", "T2PUI.GeneralError.Text");
						}
					}
				} catch (Exception e) {
					if (req != null) req.cancel();
					if (loadDialog != null) loadDialog.dispose();
					
					//showErrorPopUp("T2PUI.GeneralError.Title", "T2PUI.GeneralError.Text");
				}
			}			
		};
		
		bgTask.execute();
	}
	
	private void close() {
		this.dispose();
	}

	public void clearTextArea() {
		if (textArea.getText() != null) {
			textArea.setText(null);
		}
	}

	public void readFile() {
		PlainTextFileReader r = new PlainTextFileReader();
		String txt = r.read();
		if (txt != null)
			textArea.setText(txt);
	}
}