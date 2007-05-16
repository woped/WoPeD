package org.woped.quantana.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.woped.editor.utilities.Messages;

public class WaitDialog extends JDialog {
	
	private static final long serialVersionUID = 22L;
	
	private static final int WIDTH	= 250;
	private static final int HEIGHT	= 100;
	
	public WaitDialog(JDialog owner){
		super(owner, "", true);
		JLabel lblWait = new JLabel(Messages.getString("QuantAna.Simulation.Wait"), SwingConstants.CENTER);
		add(lblWait, BorderLayout.CENTER);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screenSize.width - WIDTH) / 2;
		int y = (screenSize.height - HEIGHT) / 2;
		setBounds(x, y, WIDTH, HEIGHT);
	}
}
