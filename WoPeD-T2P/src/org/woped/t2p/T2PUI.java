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
package org.woped.t2p;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ToolWrapper.FrameNetFunctionality;
import ToolWrapper.FrameNetInitializer;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.IEditor;
import org.woped.editor.controller.ApplicationMediator;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.file.PNMLImport;
import org.woped.gui.lookAndFeel.WopedButton;
import org.woped.gui.translations.Messages;

import worldModel.WorldModel;

/**
 * @author <a href="mailto:freytag@dhbw-karlsruhe.de">Thomas Freytag </a> <br>
 *         <br>
 *         TODO: DOCUMENTATION (tfreytag)
 * 
 *         17.01.2005
 */

@SuppressWarnings("serial")
public class T2PUI extends JDialog {
	private JLabel logoLabel = null;
	private JLabel textLabel = null;

	private WopedButton btnUpload = null;
	private WopedButton btnGenerate = null;
	private WopedButton btnErase = null;

	private JTextArea textArea = null;

	private JScrollPane t2pPanel = null;
	private JPanel buttonPanel = null;
	private JPanel logoPanel = null;
	private JPanel textPanel = null;

	private ApplicationMediator mediator;

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
		this.mediator = (ApplicationMediator) mediator;
		initialize();
	}

	/*
	 * public static void main(String[] args) { new AboutUI(null); }
	 */
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
		this.getContentPane().add(getTextPanel(), BorderLayout.NORTH);

		// this.getContentPane().add(getAboutPanel(), BorderLayout.NORTH);
		this.getContentPane().add(getT2PPanel(), BorderLayout.CENTER);

		this.getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);
		this.setTitle(Messages.getString("T2P.tooltip"));
		this.pack();

		if (getOwner() != null) {
			this.setLocation(0, getOwner().getHeight() / 4);
		} else {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			this.setLocation((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2);
		}

		this.setSize(830, 500);
	}

	private JPanel getTextPanel() {

		if (textPanel == null) {
			textPanel = new JPanel();
		}

		String[] aboutArgs = { Messages.getWoPeDVersionWithTimestamp() };
		String aboutText = "<html><p>" + Messages.getStringReplaced("T2PUI.Scrollpane", aboutArgs) + "</p></html>";

		textPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.NORTH;
		 c.insets = new Insets(10, 10, 10, 10);
		logoLabel = new JLabel(new ImageIcon(getClass().getResource(Messages.getString("Window.T2PUI.Image"))));
		textPanel.add(logoLabel, c);

		c.gridy = 1;
		 c.insets = new Insets(0, 10, 0, 10);
		c.anchor = GridBagConstraints.NORTH;
		textLabel = new JLabel(aboutText);
		textPanel.add(textLabel, c);

		return textPanel;

	}

	private JScrollPane getT2PPanel() {

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout());

		panel.setBackground(Color.white);
		textArea = new JTextArea();
		textArea.setFont(new Font("Lucia Grande", Font.PLAIN, 13));

		panel.add(textArea);

		t2pPanel = new JScrollPane(panel);

		return t2pPanel;
	}

	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			GridBagConstraints c1 = new GridBagConstraints();

			/* Generate Button */
			btnGenerate = new WopedButton(new AbstractAction() {
				public void actionPerformed(ActionEvent arg0) {
					generate();
				}
			});

			btnGenerate.setMnemonic(KeyEvent.VK_A);
			btnGenerate.setIcon(new ImageIcon(getClass().getResource(Messages.getString("Action.ShowAbout.Icon"))));
			btnGenerate.setText("Generate"); // TODO: config auslagern
			c1.gridy = 0;
			c1.gridx = 0;
			c1.insets = new Insets(10, 10, 10, 10);
			c1.anchor = GridBagConstraints.WEST;
			buttonPanel.add(btnGenerate, c1);

			/* Delete Button */
			btnErase = new WopedButton(new AbstractAction() {
				public void actionPerformed(ActionEvent arg0) {
					delete();

				}
			});

			btnErase.setMnemonic(KeyEvent.VK_L);
			btnErase.setText("Delete"); // Properties
			btnErase.setIcon(new ImageIcon(getClass().getResource(Messages.getString("T2P.Icon.Delete"))));
			c1.gridy = 0;
			c1.gridx = 1;
			c1.insets = new Insets(0, 0, 0, 0);
			c1.anchor = GridBagConstraints.CENTER;
			buttonPanel.add(btnErase, c1);

			/* Upload Button */
			btnUpload = new WopedButton(new AbstractAction() {
				public void actionPerformed(ActionEvent arg0) {
					uploadFile();
				}
			});

			btnUpload.setMnemonic(KeyEvent.VK_C);
			btnUpload.setIcon(new ImageIcon(getClass().getResource(Messages.getString("Button.Import.Icon"))));
			btnUpload.setText("Upload"); // Properties
			c1.gridy = 0;
			c1.gridx = 2;
			c1.insets = new Insets(10, 10, 10, 10);
			c1.anchor = GridBagConstraints.EAST;
			buttonPanel.add(btnUpload, c1);
		}
		return buttonPanel;
	}

	public void generate() {
		if (textArea.getText().equals("")) {

			String textMessages[] = { Messages.getString("Dialog.Ok"),

			};

			int value = JOptionPane.showOptionDialog(null,
					Messages.getStringReplaced("Action.Confirm.T2P.Empty.TextArea.Text", null),
					Messages.getString("Action.Confirm.T2P.Empty.TextArea.Title"), JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.ERROR_MESSAGE, null, textMessages, textMessages[0]);

		} else if (!FrameNetInitializer.getGenrateButton()) {
			String textMessages[] = { Messages.getString("Dialog.Ok"),

			};

			int value = JOptionPane.showOptionDialog(null,
					Messages.getStringReplaced("Action.Confirm.T2P.NotLoad.TextArea.Text", null),
					Messages.getString("Action.Confirm.T2P.NotLoad.TextArea.Title"), JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.ERROR_MESSAGE, null, textMessages, textMessages[0]);

		} else {

			String textMessages[] = { Messages.getString("Dialog.Ok"), Messages.getString("Dialog.Cancel") };

			int value = JOptionPane.showOptionDialog(null,
					Messages.getStringReplaced("Action.Confirm.T2P.NewEditor.TextArea.Text", null),
					Messages.getString("T2P.textBandTitle"), JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, textMessages, textMessages[0]);

			if (value == (JOptionPane.YES_OPTION)) {

				WorldModelExecution WMex = new WorldModelExecution();
				WorldModel world = WMex.getWorldModelBuilder().getWorldModel(textArea.getText());

				InterpetWorldModel interpreter = new InterpetWorldModel();

				PNMLGenerator generator = new PNMLGenerator();
				generator.init();
				generator.createDummyPlace();
				generator.setTransition(interpreter.getTextTrans(world.getActions()));
				generator.setArc();

				PNMLImport pnmlImport = new PNMLImport(mediator);
				pnmlImport.run(generator.after(), Messages.getString("Document.T2P.Output"), true);

				IEditor[] editor = pnmlImport.getEditor();

				try {
					((EditorVC) editor[0]).startBeautify(0, 0, 0);
				} catch (ArithmeticException exc) {
					close();

					String textMessagesNV[] = { Messages.getString("Dialog.Ok"), };

					JOptionPane.showOptionDialog(null,
							Messages.getStringReplaced("Action.Confirm.T2P.NotValid.TextArea.Text", null),
							Messages.getString("Action.Confirm.T2P.NotValid.TextArea.Title"),
							JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, textMessagesNV,
							textMessagesNV[0]);

				}

				close();

			}
		}

	}

	private void close() {
		this.dispose();
	}

	public void delete() {

		if (textArea.getText() != null) {
			textArea.setText(null);
		}

	}

	public void uploadFile() {
		OpenFile of = new OpenFile();

		try {
			of.PickMe();
		} catch (Exception e) {
			e.getStackTrace();
		}
		textArea.setText(of.sb.toString());

	}

}
