package org.woped.swt;

import java.awt.BorderLayout;
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
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.woped.editor.action.DisposeWindowAction;
import org.woped.gui.lookAndFeel.WopedButton;
import org.woped.gui.translations.Messages;

public class T2PUI extends JDialog {

	
	private static final long serialVersionUID = 1L;

	private JTextArea textArea = null;

	private JScrollPane aboutPanel = null;
	private JPanel buttonPanel = null;

	public T2PUI() {
		this(null);
	}

	/**
	 * Constructor for AboutUI.
	 * 
	 * @param owner
	 * @throws HeadlessException
	 */
	public T2PUI(Frame owner) throws HeadlessException {
		super(owner, true);
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
		this.getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);
		this.setTitle(Messages.getTitle("Action.ShowAbout"));
		this.pack();

		if (getOwner() != null) {
			this.setLocation(getOwner().getX() + ((getOwner().getWidth() - this.getWidth()) / 2),
					getOwner().getY() + ((getOwner().getHeight() - this.getHeight()) / 2));
		} else {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			this.setLocation((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2);
		}

		this.setSize(this.getWidth(), this.getHeight());
	}

	private JScrollPane getAboutPanel() {
		
		if (aboutPanel == null) {
			JPanel panel = new JPanel();
			panel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridy = 1;
			//c.insets = new Insets(0, 10, 0, 10);

			textArea = new JTextArea();
			textArea.setFont(new Font("Lucia Grande", Font.PLAIN, 13));
			panel.add(textArea, c);
			
			aboutPanel = new JScrollPane(panel);
		}
		return aboutPanel;
	}

	private JPanel getButtonPanel() {
		if (buttonPanel == null) {

			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			GridBagConstraints c1 = new GridBagConstraints();

			WopedButton btnGenerate = new WopedButton("Generate");
			btnGenerate.setIcon(new ImageIcon(getClass().getResource(Messages.getString("Action.ShowAbout.Icon"))));
			buttonPanel.add(btnGenerate,c1);

			WopedButton btnErase = new WopedButton("Delete");
			btnErase.setIcon(new ImageIcon(getClass().getResource(Messages.getString("T2P.Icon.Delete"))));

			btnErase.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					if (textArea.getText() != null) {
						textArea.setText(null);
					}

				}
			});
			buttonPanel.add(btnErase,c1);

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
			buttonPanel.add(btnUpload,c1);
			
			
			/*
		
			aboutButton = new WopedButton(new AbstractAction() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						getContentPane().remove(getChangeLogPanel());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					getContentPane().add(getAboutPanel(), BorderLayout.CENTER, 0);
					aboutButton.setEnabled(false);
					changelogButton.setEnabled(true);
					pack();
					repaint();
				}
			});

			aboutButton.setMnemonic(KeyEvent.VK_A);
			aboutButton.setIcon(new ImageIcon(getClass().getResource(Messages.getString("Action.ShowAbout.Icon"))));
			aboutButton.setText(Messages.getString("Action.ShowAbout.Title"));
			aboutButton.setEnabled(false);
			c1.gridy = 0;
			c1.gridx = 0;
			c1.insets = new Insets(10, 10, 10, 10);
			c1.anchor = GridBagConstraints.WEST;
			buttonPanel.add(aboutButton, c1);

			
			changelogButton = new WopedButton(new AbstractAction() {
				public void actionPerformed(ActionEvent arg0) {
					getContentPane().remove(getAboutPanel());
					try {
						getContentPane().add(getChangeLogPanel(), BorderLayout.CENTER, 0);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					aboutButton.setEnabled(true);
					changelogButton.setEnabled(false);
					pack();
					repaint();
				}
			});

			changelogButton.setMnemonic(KeyEvent.VK_L);
			changelogButton.setText(Messages.getString("Window.About.Versions"));
			changelogButton.setIcon(Messages.getImageIcon("Window.About.Versions"));
			c1.gridy = 0;
			c1.gridx = 1;
			c1.insets = new Insets(0, 0, 0, 0);
			c1.anchor = GridBagConstraints.CENTER;
			buttonPanel.add(changelogButton, c1);

		
			closeButton = new WopedButton(new DisposeWindowAction());
			closeButton.setMnemonic(KeyEvent.VK_C);
			closeButton.requestFocus();

			c1.gridy = 0;
			c1.gridx = 2;
			c1.insets = new Insets(10, 10, 10, 10);
			c1.anchor = GridBagConstraints.EAST;
			buttonPanel.add(closeButton, c1);
			 */
		}
		return buttonPanel;
	}

}
