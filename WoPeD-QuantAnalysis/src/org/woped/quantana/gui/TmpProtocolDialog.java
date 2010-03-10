package org.woped.quantana.gui;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TmpProtocolDialog extends JDialog {
	private static final long serialVersionUID = 1L;
		
	private JTextArea txtArea;
	private Container contentPane;
	
	
	public TmpProtocolDialog(Dialog owner){
		super(owner, "", true);
		
		contentPane = this.getContentPane();
		txtArea = new JTextArea();
		txtArea.setEditable(false);
		contentPane.add(new JScrollPane(txtArea));
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width > 800 ? 800 : screenSize.width;
		int x = screenSize.width > width ? (screenSize.width - width) / 2 : 0;
		int height = screenSize.height > 740 ? 740 : screenSize.height;
		int y = screenSize.height > height ? (screenSize.height - height) / 2 : 0;
		this.setBounds(x, y, width, height);
	}

	public JTextArea getTxtArea() {
		return txtArea;
	}

	public void clear() {
		txtArea.setText("");		
	}

	public void addLine(String line) {
		txtArea.append(line);		
	}

	public void configTxt() {
		txtArea.select(0,0);
		txtArea.scrollRectToVisible(new Rectangle(0,0,0,0));
	}
}
