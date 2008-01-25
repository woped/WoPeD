package org.woped.editor.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextArea;

import org.woped.translations.Messages;

@SuppressWarnings("serial")
public class PopUpDialog extends JDialog {

	private String _message;

	public PopUpDialog(Window owner, boolean modal, String Title, String Message) {
		super(owner,Title);
		this.setModal(modal);
		this._message = Message;
		this.init();
	}

	private void init() {
		this.setVisible(false);
		this.setSize(400, 200);
		this.setLocation(150, 150);
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 1;
		c.weighty = 1;

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		JTextArea text = new JTextArea();
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		text.setEditable(false);
		text.setText(this._message);
		this.add(text, c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		JButton button = new JButton(Messages
				.getString("Transition.Properties.BPEL.Buttons.OK"));
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
			
		});
		this.add(button, c);

	}
}
