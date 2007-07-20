package org.woped.quantana.gui;

import java.awt.Container;
import java.awt.Dialog;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TmpProtocolDialog extends JDialog {
	private static final long serialVersionUID = 1L;
		
	private static final int WIDTH	= 500;
		
	private static final int HEIGHT	= 400;
		
	private JTextArea txtArea;
	private Container contentPane;
	
	
	public TmpProtocolDialog(Dialog owner){
		super(owner, "", true);
		
		contentPane = this.getContentPane();
		txtArea = new JTextArea();
		
		contentPane.add(new JScrollPane(txtArea));
		
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		int x = (screenSize.width - WIDTH) / 2;
//		int y = (screenSize.height - HEIGHT) / 2;
		this.setBounds(0, 0, WIDTH, HEIGHT);
		this.setVisible(true);
	}

	public JTextArea getTxtArea() {
		return txtArea;
	}
}
