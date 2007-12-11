package org.woped.quantana.gui;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;

import org.woped.translations.Messages;
import org.woped.quantana.simulation.Simulator;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class ProtocolDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	private static final int WIDTH	= 600;
	
	private static final int HEIGHT	= 500;
	
	private Dialog owner;
	
	private ByteArrayInputStream in;
	
	private byte[] protocol;
	
	private Container contentPane;
	
	private JScrollPane scrollPane;
	
	private JPanel buttonPanel;
	
	private JTextArea txtProtocol;
	
	private JFileChooser fileChooser;
	
	private Simulator sim;
	
	private File dir;
	
	private final ExtensionFileFilter eff = new ExtensionFileFilter();
	
	public ProtocolDialog(Dialog owner, byte[] protocol){
		super(owner, Messages.getString("QuantAna.Simulation.Protocol"), true);
		this.owner = owner;
		this.protocol = protocol;
		sim = ((QuantitativeSimulationDialog)owner).getSimulator();
		dir = new File(sim.getProtocolPath());
		
		fileChooser = new JFileChooser();
		getFileFilter();
		
		initialize();
	}
	
	private void initialize(){
		contentPane = this.getContentPane();
		
		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.gridx = 0;
		constraints.gridy = 0;
		contentPane.add(getScrollPane(), constraints);
		
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 1;
		contentPane.add(getButtonPanel(), constraints);
		
		Rectangle bounds = owner.getBounds();
		int x = (bounds.width - WIDTH)/2 + bounds.x;
		int y = (bounds.height - HEIGHT)/2 + bounds.y;
		this.setBounds(x, y, WIDTH, HEIGHT);
		this.setVisible(true);
	}
	
	private JScrollPane getScrollPane(){
		if (scrollPane == null){
			txtProtocol = new JTextArea();
			getProtocol();
			scrollPane = new JScrollPane(txtProtocol);
		}
		
		return scrollPane;
	}
	
	private JPanel getButtonPanel(){
		if (buttonPanel == null){
			buttonPanel = new JPanel();
			
			JButton btnOK = new JButton();
			btnOK.setText(Messages.getTitle("QuantAna.Button.Close"));
			btnOK.setIcon(Messages.getImageIcon("QuantAna.Button.Close"));
			btnOK.setMinimumSize(new Dimension(120, 25));
			btnOK.setMaximumSize(new Dimension(120, 25));
			btnOK.setPreferredSize(new Dimension(120, 25));
			btnOK.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					exit();
				}
			});
			
			JButton btnSave = new JButton();
			btnSave.setText(Messages.getTitle("QuantAna.Button.Save"));
			btnSave.setIcon(Messages.getImageIcon("QuantAna.Button.Save"));
			btnSave.setMinimumSize(new Dimension(120, 25));
			btnSave.setMaximumSize(new Dimension(120, 25));
			btnSave.setPreferredSize(new Dimension(120, 25));
			btnSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					save();
				}
			});
			
			buttonPanel.setLayout(new GridBagLayout());
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.insets = new Insets(5, 25, 5, 5);
			constraints.weightx = 0;
			constraints.weighty = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			
			constraints.gridx = 0;
			constraints.gridy = 0;
			buttonPanel.add(btnSave, constraints);
			
			constraints.gridx = 1;
			constraints.gridy = 0;
			buttonPanel.add(btnOK, constraints);
		}
		
		return buttonPanel;
	}
	
	private void exit(){
		this.dispose();
	}
	
	private void save(){
		fileChooser.setCurrentDirectory(dir);
		fileChooser.setMultiSelectionEnabled(false);
		
		int result = fileChooser.showSaveDialog(this);
		if (result == JFileChooser.APPROVE_OPTION){
			String fname = fileChooser.getSelectedFile().getAbsolutePath();
			dir = fileChooser.getCurrentDirectory();
			String ext = "";
			int idx = fname.lastIndexOf(".");
			if (idx > -1){
				ext = fname.substring(idx + 1);
			} else {
				ext = "txt";
				fname += "." + ext;
			}
			
			try {
				if (ext.equals("txt")) {
					FileWriter fw = new FileWriter(fname);
					txtProtocol.write(fw);
					fw.close();
				} else if (ext.equals("xml")) {
					FileOutputStream fos = new FileOutputStream(fname);
					fos.write(sim.getProtocolContent());
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void getProtocol() {

		try {
			XMLReader xr = XMLReaderFactory.createXMLReader();

			DefaultHandler handler = new DefaultHandler() {

				private boolean msg = false;

				public void startDocument() {
					txtProtocol.append("--- Protocol Start ---\n\n");
				}

				public void startElement(String uri, String lname,
						String qname, Attributes attr) {
					try {
						if (lname.equalsIgnoreCase("message")) msg = true;

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				public void characters(char[] ch, int start, int length) {
					
					String s = String.copyValueOf(ch, start, length);
					
					if (msg) txtProtocol.append(s + "\n");
				}

				public void endElement(String uri, String lname, String qname) {
					if (lname.equalsIgnoreCase("message")) msg = false;
				}

				public void endDocument() {
					txtProtocol.append("\n\nsimulation took " + sim.getDuration() + " ms");
					txtProtocol.append("\n\n--- Protocol End ---");
				}
			};

			xr.setContentHandler(handler);
			xr.setErrorHandler(handler);

			in = new ByteArrayInputStream(protocol);
			xr.parse(new InputSource(in));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void getFileFilter(){
		eff.addExtension("txt");
		eff.addExtension("xml");
		eff.setDescription(Messages.getString("QuantAna.Simulation.Protocol.FileFilter"));
		fileChooser.setFileFilter(eff);
	}
	
	class ExtensionFileFilter extends FileFilter {
		
		private ArrayList<String> extensions = new ArrayList<String>();
		private String description = " ";
		
		public void addExtension(String ext){
			if (!ext.startsWith(".")) ext = "." + ext;
			extensions.add(ext.toLowerCase());
		}
		
		public boolean accept(File f){
			if (f.isDirectory()) return true;
			
			String name = f.getName().toLowerCase();
			for (String ext : extensions)
				if (name.endsWith(ext))
					return true;
			
			return false;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	}
}
