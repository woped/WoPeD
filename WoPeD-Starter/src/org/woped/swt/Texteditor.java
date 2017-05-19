package org.woped.swt;


import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

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
setBounds(100, 100, 450, 300);
		
		JTextArea textArea = new JTextArea();
		getContentPane().add(textArea, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new GridLayout(1, 0, 0, 0));
		
		JButton btnGenerate = new JButton("Generate");
		panel.add(btnGenerate);
		
		JButton btnErase = new JButton("Delete");
		btnErase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
		if(textArea.getText() != null){
			textArea.setText(null);
		}
				
			}
		});
		panel.add(btnErase);
		
	
		
		JButton btnUpload = new JButton("Upload");
		btnUpload.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ewt){
		
			OpenFile of = new OpenFile();
			
			try {
				of.PickMe();
			} catch (Exception e){
				e.getStackTrace();
		}
			textArea.setText(of.sb.toString());
			}
		});
		panel.add(btnUpload);

		
		
		
		
		
	}
}
