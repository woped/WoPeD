package org.woped.starter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.woped.core.utilities.LoggerManager;
import org.woped.gui.lookAndFeel.WopedButton;
import org.woped.gui.translations.Messages;

public class AskToStartWoPeDUI extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private WopedButton noButton, yesButton;

	public AskToStartWoPeDUI(JFrame owner) {
		super(owner, Messages.getString("Dialog.StartWoPeD.Title"), true);
		this.setAlwaysOnTop(true);
		setLayout(new BorderLayout());
		setSize(400,300); 
		Dimension d = this.getToolkit().getScreenSize(); 
	    setLocation((int) ((d.getWidth() - this.getWidth()) / 2), (int) ((d.getHeight() - this.getHeight()) / 2));
	    JPanel panel1 = new JPanel();
	    panel1.setLayout(new BorderLayout());
        add(new JLabel(Messages.getImageIcon("Dialog.StartWoPeD")), BorderLayout.CENTER);
		JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout(FlowLayout.CENTER));
		yesButton = new WopedButton(Messages.getString("Dialog.StartWoPeD.Yes.Text"));
        yesButton.setIcon(Messages.getImageIcon("Dialog.StartWoPeD.Yes"));
		yesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				setVisible(false);
				dispose();
			}
		});
		panel2.add(yesButton);
		noButton = new WopedButton(Messages.getString("Dialog.StartWoPeD.No.Text"));
        noButton.setIcon(Messages.getImageIcon("Dialog.StartWoPeD.No"));
		noButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				LoggerManager.info(Constants.GUI_LOGGER, "EXIT APPLICATION");
				System.exit(0);
			}
		});
		panel2.add(noButton);
		add(panel1, BorderLayout.NORTH);
		add(panel2, BorderLayout.SOUTH);
		pack();
	}
}
