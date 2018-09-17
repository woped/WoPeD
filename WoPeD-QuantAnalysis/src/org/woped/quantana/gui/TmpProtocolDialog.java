package org.woped.quantana.gui;

import java.awt.Container;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JButton;

import org.woped.gui.translations.Messages;
import org.woped.quantana.sim.SimLog;

public class TmpProtocolDialog extends JDialog {
	private static final long serialVersionUID = 1L;
		
	private JTextArea txtArea;
	private Container contentPane;
	
	private SimLog xesLog;
	
	public TmpProtocolDialog(Dialog owner){
		super(owner, "", true);
		
		contentPane = this.getContentPane();
		contentPane.setLayout(new GridBagLayout());

		Dimension dimButton = new Dimension(120, 25);

		JButton btnXES = new JButton();
		btnXES.setText(Messages.getString("QuantAna.Simulation.Log.xesSave"));
		btnXES.setMinimumSize(dimButton);
		btnXES.setMaximumSize(dimButton);
		btnXES.setPreferredSize(dimButton);
		btnXES.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				writeXES();
			}
		});

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(5, 25, 5, 10);
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;		
		constraints.gridx = 0;
		constraints.gridy = 0;
		contentPane.add(btnXES, constraints);

		JButton btnCSV = new JButton();
		btnCSV.setText(Messages.getString("QuantAna.Simulation.Log.csvSave"));
		btnCSV.setMinimumSize(dimButton);
		btnCSV.setMaximumSize(dimButton);
		btnCSV.setPreferredSize(dimButton);
		btnCSV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				writeCSV();
			}
		});

		constraints = new GridBagConstraints();
		constraints.insets = new Insets(5, 10, 5, 25);
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;		
		constraints.gridx = 1;
		constraints.gridy = 0;
		contentPane.add(btnCSV, constraints);


		txtArea = new JTextArea();
		txtArea.setEditable(false);
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(5, 25, 5, 25);
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.gridwidth = 2;
		constraints.gridheight = 20;
		constraints.anchor=GridBagConstraints.PAGE_END;
		constraints.fill=GridBagConstraints.BOTH;
		constraints.gridx = 0;
		constraints.gridy = 1;
		contentPane.add(new JScrollPane(txtArea), constraints);

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
	
	public void setXESLog(SimLog xesLog) {
		this.xesLog = xesLog;
	}
	
	public void writeXES() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("XES Log", "xes");
		chooser.setFileFilter(filter);
		int result = chooser.showSaveDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File f = chooser.getSelectedFile();
			String filePath = f.getAbsolutePath();
			if(!filePath.toLowerCase().endsWith(".xes")) {
			    f = new File(filePath + ".xes");
			}
			if (f.exists()) {
				if (JOptionPane.showConfirmDialog(null, Messages.getString("File.Warning.Overwrite.Text"), Messages.getString("File.Warning.Overwrite.Title"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.OK_OPTION) {
					return;
				}
			}
			try {
				PrintWriter p = new PrintWriter(f);
				this.xesLog.writeXES(p);
				p.close();
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(this, Messages.getString("QuantAna.Simulation.Log.xesError"), "", JOptionPane.ERROR_MESSAGE);
			}					
		}		
	}

	public void writeCSV() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Log", "csv");
		chooser.setFileFilter(filter);
		int result = chooser.showSaveDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File f = chooser.getSelectedFile();
			String filePath = f.getAbsolutePath();
			if(!filePath.toLowerCase().endsWith(".csv")) {
			    f = new File(filePath + ".csv");
			}
			if (f.exists()) {
				if (JOptionPane.showConfirmDialog(null, Messages.getString("File.Warning.Overwrite.Text"), Messages.getString("File.Warning.Overwrite.Title"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.OK_OPTION) {
					return;
				}
			}
			try {
				PrintWriter p = new PrintWriter(f);
				this.xesLog.writeCSV(p);
				p.close();
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(this, Messages.getString("QuantAna.Simulation.Log.csvError"), "", JOptionPane.ERROR_MESSAGE);
			}
		}		
	}

}
