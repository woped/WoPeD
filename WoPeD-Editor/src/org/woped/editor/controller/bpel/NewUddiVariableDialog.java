package org.woped.editor.controller.bpel;

import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.woped.gui.translations.Messages;

/*
 * @author: Alexander Ro�wog
 */
@SuppressWarnings("serial")
public class NewUddiVariableDialog extends JDialog
{
	static final int _OKBUTTON = 0;
	static final int _CANCELBUTTON = 1;

	private JTextField VariableName;
	private JTextField VariableURL;
	private JPanel dialogButtons = null;
	private int activbutton = -1;
	public JDialog errorPopup = null;

	/**
	 * @param arg0
	 *            TransitionPropertyEditor
	 * @param arg1
	 *            boolean
	 * @throws HeadlessException
	 */
	public NewUddiVariableDialog(Dialog Dialog)
			throws HeadlessException {
		super(Dialog, true);
		this.init();
	}

	public void init() {
		this.setVisible(false);
		this.setTitle(Messages
				.getString("Transition.Properties.BPEL.UDDI.CreateUDDITitle"));
		this.setSize(400, 150);
		this.setLocation(this.getOwner().getLocation().x + 50, this.getOwner().getLocation().y + 50);
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 1;
		c.weighty = 1;

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		this.add(new JLabel(Messages
				.getString("Transition.Properties.BPEL.UDDI.VariableName")
				+ ":"), c);

		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 5);
		if (this.VariableName == null)
			this.VariableName = new JTextField("");
		this.add(this.VariableName, c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		this.add(new JLabel(Messages
				.getString("Transition.Properties.BPEL.UDDI.VariableURL")
				+ ":"), c);

		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 5);
		if(VariableURL == null)
		{
			VariableURL = new JTextField();
		}
		this.add(VariableURL, c);

		c.fill = GridBagConstraints.NONE;
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		this.add(addVariableDialogButtons(), c);

		this.setVisible(true);
	}

	public JPanel addVariableDialogButtons() {
		if (dialogButtons == null) {
			dialogButtons = new JPanel();

			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.NONE;
			c.anchor = GridBagConstraints.WEST;
			c.weightx = 1;
			c.weighty = 1;

			c.gridx = 0;
			c.gridy = 0;
			c.gridwidth = 1;
			c.insets = new Insets(0, 5, 0, 0);
			JButton b = new JButton(Messages
					.getString("Transition.Properties.BPEL.Buttons.OK"));
			b.addActionListener(new ButtonEvent(this, NewUddiVariableDialog._OKBUTTON));
			dialogButtons.add(b, c);

			c.gridx = 1;
			c.gridy = 0;
			c.gridwidth = 1;
			c.insets = new Insets(0, 5, 0, 0);
			b = new JButton(Messages
					.getString("Transition.Properties.BPEL.Buttons.Cancel"));
			b.addActionListener(new ButtonEvent(this, NewUddiVariableDialog._CANCELBUTTON));
			dialogButtons.add(b, c);
		}
		return dialogButtons;
	}
	
	/**
	 * 
	 * @param Name
	 */
	public void setVariableName(String Name)
	{
		this.VariableName.setText(Name);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getVariableName()
	{
		return this.VariableName.getText();
	}
	
	/**
	 * s
	 * @param URL
	 */
	public void setVariableURL(String URL)
	{
		this.VariableURL.setText(URL);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getVariableURL()
	{
		return this.VariableURL.getText();
	}
	
	public void setActivButton(int Buttontype) {
		this.activbutton = Buttontype;
	}

	public int getActivButton() {
		return this.activbutton;
	}

	public boolean isInputOk() {
		if(VariableName.getText().equalsIgnoreCase("") || VariableURL.getText().equalsIgnoreCase(""))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	class ButtonEvent implements ActionListener {
		private NewUddiVariableDialog _adaptee;
		private int _buttontype = -1;

		public ButtonEvent(NewUddiVariableDialog Adaptee, int Buttontype) {
			this._adaptee = Adaptee;
			this._buttontype = Buttontype;
		}

		public void actionPerformed(ActionEvent arg0) {
			if (this._buttontype == NewUddiVariableDialog._OKBUTTON) {
				if (!this._adaptee.isInputOk())
				{
					errorPopup = new JDialog(this._adaptee, true);
					errorPopup.setVisible(false);
					errorPopup.setTitle(Messages.getString("Editor.Message.Subprocess.Title"));
					errorPopup.setSize(300, 140);
					errorPopup.setLocation(this._adaptee.getLocation().x + 90,
							this._adaptee.getLocation().y + 50);
					errorPopup.setLayout(new GridBagLayout());
					GridBagConstraints c = new GridBagConstraints();

					c.fill = GridBagConstraints.CENTER;
					c.weightx = 1;
					c.weighty = 1;

					c.gridx = 0;
					c.gridy = 0;
					c.gridwidth = 1;
					c.insets = new Insets(10, 10, 0, 10);
					errorPopup.add(new JLabel(Messages.getString("Transition.Properties.BPEL.ErrorDuringFieldCheck")), c);

					c.gridx = 0;
					c.gridy = 1;
					c.gridwidth = 1;
					c.insets = new Insets(0, 5, 0, 0);
					c.fill = GridBagConstraints.CENTER;

					JButton okButton = new JButton(Messages
							.getString("Transition.Properties.BPEL.Buttons.OK"));
					okButton.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent e) {
							errorPopup.dispose();
						}

					});

					errorPopup.add(okButton, c);
					errorPopup.setVisible(true);
					
					return;
				}
			}
			this._adaptee.setActivButton(this._buttontype);
			this._adaptee.dispose();
		}
	}
}
