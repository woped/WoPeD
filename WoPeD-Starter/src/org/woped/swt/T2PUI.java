package org.woped.swt;

import java.awt.BorderLayout;
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
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.IEditor;
import org.woped.editor.controller.ApplicationMediator;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.file.PNMLImport;
import org.woped.gui.lookAndFeel.WopedButton;
import org.woped.gui.translations.Messages;

import processing.FrameNetWrapper;
import worldModel.WorldModel;

public class T2PUI extends JDialog {
	private JLabel logoLabel = null;
	private JLabel aboutTextLabel = null;
	private JLabel javaTextLabel = null;
	private JLabel homepageLabel = null;
	private JLabel mailtoLabel = null;
	private JLabel sfLabel = null;
	private JLabel icLabel = null;
	private WopedButton closeButton = null;
	private WopedButton aboutButton = null;
	private WopedButton changelogButton = null;
	private JTextArea textArea = null;

	private JScrollPane aboutPanel = null;
	private JScrollPane changeLogPanel = null;
	private JPanel buttonPanel = null;
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
		this.getContentPane().add(getAboutPanel(), BorderLayout.NORTH);
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

	private JScrollPane getAboutPanel() {
		String[] aboutArgs = { Messages.getWoPeDVersionWithTimestamp() };
		String aboutText = "<html><p>" + Messages.getStringReplaced("T2PUI.Scrollpane", aboutArgs) + "</p></html>";
		/*
		 * String javaText = "<html><p><b>" + Messages.getString("About.Java") +
		 * ": </b>" + System.getProperty("java.version") + "</p></html>";
		 */

		if (aboutPanel == null) {
			JPanel panel = new JPanel();
			panel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();

			c.gridx = 0;
			c.gridy = 0;
			c.anchor = GridBagConstraints.NORTH;
			c.insets = new Insets(10, 10, 10, 10);
			logoLabel = new JLabel(new ImageIcon(getClass().getResource(Messages.getString("Window.T2PUI.Image"))));
			panel.add(logoLabel, c);

			/*
			 * c.gridy = 1; c.insets = new Insets(0, 10, 0, 10); c.anchor =
			 * GridBagConstraints.WEST; javaTextLabel = new JLabel(javaText);
			 * panel.add(javaTextLabel, c);
			 */

			c.gridy = 1;
			c.insets = new Insets(0, 10, 0, 10);
			c.anchor = GridBagConstraints.NORTH;
			aboutTextLabel = new JLabel(aboutText);
			panel.add(aboutTextLabel, c);

			/*
			 * c.gridy = 3; c.insets = new Insets(0, 10, 0, 10); homepageLabel =
			 * new JLabel("<html><p>" + Messages.getString("About.Homepage") +
			 * "</p></html>"); homepageLabel.addMouseListener( new
			 * LaunchDefaultBrowserAction(Messages.getString(
			 * "About.Homepage.Link"), homepageLabel)); panel.add(homepageLabel,
			 * c);
			 * 
			 * c.gridy = 4; c.insets = new Insets(0, 10, 0, 10); mailtoLabel =
			 * new JLabel("<html><p>" + Messages.getString("About.Email") +
			 * "</p></html>"); mailtoLabel.addMouseListener( new
			 * LaunchDefaultBrowserAction(Messages.getString("About.Email.Link")
			 * , mailtoLabel)); panel.add(mailtoLabel, c);
			 * 
			 * c.gridy = 5; c.insets = new Insets(0, 10, 0, 10); sfLabel = new
			 * JLabel("<html><p>" + Messages.getString("About.Development") +
			 * "</p></html>"); sfLabel.addMouseListener( new
			 * LaunchDefaultBrowserAction(Messages.getString(
			 * "About.Development.Link"), sfLabel)); panel.add(sfLabel, c);
			 * 
			 * c.gridy = 6; c.insets = new Insets(0, 10, 0, 10); icLabel = new
			 * JLabel("<html><p>" + Messages.getString("About.Iconset") +
			 * "</p></html>"); icLabel.addMouseListener(new
			 * LaunchDefaultBrowserAction(Messages.getString(
			 * "About.Iconset.Link"), icLabel)); panel.add(icLabel, c);
			 */

			/*
			 * c.gridy = 3; c.insets = new Insets(10, 10, 10, 10); homepageLabel
			 * = new JLabel("<html><p>" + Messages.getString("About.Homepage") +
			 * "</p></html>"); homepageLabel.addMouseListener( new
			 * LaunchDefaultBrowserAction(Messages.getString(
			 * "About.Homepage.Link"), homepageLabel)); panel.add(homepageLabel,
			 * c);
			 */

			aboutPanel = new JScrollPane(panel);
		}
		return aboutPanel;
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

					} else if (!FrameNetWrapper.getGenrateButton()) {
						String textMessages[] = { Messages.getString("Dialog.Ok"),

						};

						int value = JOptionPane.showOptionDialog(null,
								Messages.getStringReplaced("Action.Confirm.T2P.NotLoad.TextArea.Text", null),
								Messages.getString("Action.Confirm.T2P.NotLoad.TextArea.Title"),
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

							WorldModel world = WorldModelExecution.getInitiator().convert(textArea.getText());

							InterpetWorldModel interpreter = new InterpetWorldModel();

							PNMLGenerator generator = new PNMLGenerator();
							generator.init();
							generator.createDummyPlace();
							generator.setTransition(interpreter.getTextTrans(world.getActions()));
							generator.setArc();

							PNMLImport pnmlImport = new PNMLImport(mediator);
							pnmlImport.run(generator.after(), Messages.getString("Document.T2P.Output"), true);

							IEditor[] editor = pnmlImport.getEditor();
							((EditorVC) editor[0]).startBeautify(0, 0, 0);

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
