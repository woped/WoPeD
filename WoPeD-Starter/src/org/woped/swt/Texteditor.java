package org.woped.swt;


import java.awt.EventQueue;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JInternalFrame;

public class Texteditor extends JInternalFrame {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Texteditor frame = new Texteditor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Texteditor() {
		setBounds(100, 100, 561, 416);
		
		JButton btnGenerate = new JButton("Generate");
		btnGenerate.setBounds(10, 324, 99, 29);
		

		getContentPane().setLayout(null);
		getContentPane().add(btnGenerate);
		
		
		TextArea textArea_1 = new TextArea();
		textArea_1.setBounds(10, 10, 517, 296);
		getContentPane().add(textArea_1);

		JButton btnErase = new JButton("Delete");
		btnErase.setBounds(121, 324, 84, 29);
		btnErase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
		if(textArea_1.getText() != null){
			textArea_1.setText(null);
		}
				
			}
		});
		getContentPane().add(btnErase);
		
		JButton btnUpload = new JButton("Upload");
		btnUpload.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ewt){
		
			OpenFile of = new OpenFile();
			
			try {
				of.PickMe();
			} catch (Exception e){
				e.getStackTrace();
		}
			textArea_1.setText(of.sb.toString());
			}
		});
		btnUpload.setBounds(410, 324, 117, 29);
		getContentPane().add(btnUpload);

	}
}
