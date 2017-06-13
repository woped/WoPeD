package org.woped.swt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.editor.controller.ApplicationMediator;
import org.woped.gui.lookAndFeel.WopedButton;
import org.woped.gui.translations.Messages;

import gui.Initiator;
import worldModel.WorldModel;

public class T2PUI extends JDialog {

	private static final long serialVersionUID = 1L;

	private JTextArea textArea = null;

	private JScrollPane aboutPanel = null;
	private JPanel buttonPanel = null;
	private ApplicationMediator mediator = null;

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
		this.getContentPane().add(getT2PPanel(), BorderLayout.CENTER);
		this.getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);
		this.setTitle(Messages.getString("T2P.textBandTitle"));
		this.pack();

		if (getOwner() != null) {
			this.setLocation(0, getOwner().getHeight() / 4);
		} else {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			this.setLocation((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2);
		}

		this.setSize(800, 500);
	}

	private void close() {
		this.dispose();
	}

	private JScrollPane getT2PPanel() {

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout());
		// GridBagConstraints c = new GridBagConstraints();

		panel.setBackground(Color.white);
		textArea = new JTextArea();
		textArea.setFont(new Font("Lucia Grande", Font.PLAIN, 13));

		panel.add(textArea);

		aboutPanel = new JScrollPane(panel);

		return aboutPanel;
	}

	private JPanel getButtonPanel() {
		if (buttonPanel == null) {

			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridLayout(1, 3));
			// GridBagConstraints c1 = new GridBagConstraints();

			WopedButton btnGenerate = new WopedButton("Generate");
			btnGenerate.setIcon(new ImageIcon(getClass().getResource(Messages.getString("Action.ShowAbout.Icon"))));
			buttonPanel.add(btnGenerate);
			btnGenerate.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					if (textArea.getText().equals("")) {

						String textMessages[] = { Messages.getString("Dialog.Ok"),

						};

						int value = JOptionPane.showOptionDialog(null,
								Messages.getStringReplaced("Action.Confirm.T2P.Empty.TextArea.Text", null),
								Messages.getString("Action.Confirm.T2P.Empty.TextArea.Title"),
								JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, textMessages,
								textMessages[0]);

					} else {

						String textMessages[] = { Messages.getString("Dialog.Ok"),
								Messages.getString("Dialog.Cancel") };

						int value = JOptionPane.showOptionDialog(null,
								Messages.getStringReplaced("Action.Confirm.T2P.NewEditor.TextArea.Text", null),
								Messages.getString("T2P.textBandTitle"), JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, textMessages, textMessages[0]);

						if (value == (JOptionPane.YES_OPTION)) {

							Initiator init = new Initiator();
							WorldModel world = init.convert(textArea.getText());
							
							
							Ausgabe ausgabe = new Ausgabe();
							ausgabe.init();
							System.out.println(world.getActors());
							System.out.println(world.getLastFlowAdded());
							System.out.println(world.getResources());
							ausgabe.setPlace(world.getActors());
							// ausgabe.setPlace(world.getResources());
							System.out.println(world.getActions());
							ausgabe.setTransition(world.getActions());
							System.out.println(world.getFlows());
							ausgabe.setArc(world.getFlows());
							ausgabe.after();

							// orld.

							mediator.createEditor(true);
							close();

						}
					}
				}
			});

			WopedButton btnErase = new WopedButton("Delete");
			btnErase.setIcon(new ImageIcon(getClass().getResource(Messages.getString("T2P.Icon.Delete"))));

			btnErase.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					if (textArea.getText() != null) {
						textArea.setText(null);
					}

				}
			});
			buttonPanel.add(btnErase);

			WopedButton btnUpload = new WopedButton("Upload");
			btnUpload.setIcon(new ImageIcon(getClass().getResource(Messages.getString("Button.Import.Icon"))));

			btnUpload.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent ewt) {

					OpenFile of = new OpenFile();

					try {
						of.PickMe();
					} catch (Exception e) {
						e.getStackTrace();
					}
					textArea.setText(of.sb.toString());
				}
			});
			buttonPanel.add(btnUpload);

		}
		return buttonPanel;
	}

}
