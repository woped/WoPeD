package org.woped.quantana.gui;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import org.woped.core.config.ConfigurationManager;
import org.woped.editor.utilities.Messages;
import org.woped.quantana.utilities.ExportStatistics;

public class ExportDialog extends JDialog {
	
	private static final long serialVersionUID = 101L;
	
	private static final int WIDTH	= 280;
	
	private static final int HEIGHT	= 200;
	
	private Dialog owner;
	
	private ExportStatistics export;
	
	private JFileChooser fileChooser;
	
	private File dir;
	
	private final ExtensionFileFilter eff = new ExtensionFileFilter();
	
	private JPanel buttonPanel;
	private JButton btnTable;
	private JButton btnServer;
	private JButton btnResource;
	private JButton btnClose;
	
	public ExportDialog(Dialog owner){
		super(owner, Messages.getString("QuantAna.Simulation.Export.Title"), true);
		this.owner = owner;
		this.export = new ExportStatistics((QuantitativeSimulationDialog)owner);
		
		dir = new File(ConfigurationManager.getConfiguration().getLogdir());
		
		fileChooser = new JFileChooser();
		getFileFilter();
		
		initialize();
	}
	
	private void initialize(){
		getContentPane().add(getButtonPanel());
		
		Rectangle bounds = owner.getBounds();
		int x = (bounds.width - WIDTH)/2 + bounds.x;
		int y = (bounds.height - HEIGHT)/2 + bounds.y;
		this.setBounds(x, y, WIDTH, HEIGHT);
		this.setVisible(true);
	}
	
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.insets = new Insets(5, 25, 5, 10);
			constraints.weightx = 0;
			constraints.weighty = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			
			btnTable = new JButton();
			btnTable.setText(Messages.getTitle("QuantAna.Button.Export.Table"));
			btnTable.setIcon(Messages.getImageIcon("QuantAna.Button.Export.Table"));
			btnTable.setMinimumSize(new Dimension(200, 25));
			btnTable.setMaximumSize(new Dimension(200, 25));
			btnTable.setPreferredSize(new Dimension(200, 25));
			btnTable.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e){
					exportTable();
				}
			});
			constraints.gridx = 0;
			constraints.gridy = 0;
			buttonPanel.add(btnTable, constraints);
			
			btnServer = new JButton();
			btnServer.setText(Messages.getTitle("QuantAna.Button.Export.Server"));
			btnServer.setIcon(Messages.getImageIcon("QuantAna.Button.Export.Server"));
			btnServer.setMinimumSize(new Dimension(200, 25));
			btnServer.setMaximumSize(new Dimension(200, 25));
			btnServer.setPreferredSize(new Dimension(200, 25));
			btnServer.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e){
					exportServer();
				}
			});
			constraints.gridx = 0;
			constraints.gridy = 1;
			buttonPanel.add(btnServer, constraints);
			
			btnResource = new JButton();
			btnResource.setText(Messages.getTitle("QuantAna.Button.Export.Resources"));
			btnResource.setIcon(Messages.getImageIcon("QuantAna.Button.Export.Resources"));
			btnResource.setMinimumSize(new Dimension(200, 25));
			btnResource.setMaximumSize(new Dimension(200, 25));
			btnResource.setPreferredSize(new Dimension(200, 25));
			btnResource.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e){
					exportResource();
				}
			});
			constraints.gridx = 0;
			constraints.gridy = 2;
			buttonPanel.add(btnResource, constraints);
			
			btnClose = new JButton();
			btnClose.setText(Messages.getTitle("QuantAna.Button.Close"));
			btnClose.setIcon(Messages.getImageIcon("QuantAna.Button.Close"));
			btnClose.setMinimumSize(new Dimension(200, 25));
			btnClose.setMaximumSize(new Dimension(200, 25));
			btnClose.setPreferredSize(new Dimension(200, 25));
			btnClose.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e){
					dispose();
				}
			});
			constraints.gridx = 0;
			constraints.gridy = 3;
			buttonPanel.add(btnClose, constraints);
		}
		
		return buttonPanel;
	}
	
	private void exportTable(){
		save(export.getStatsTable());
	}

	private void exportServer(){
		save(export.getServerStats());
	}

	private void exportResource(){
		save(export.getResourceStats());
	}
	
	private void save(String text){
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
				ext = "csv";
				fname += "." + ext;
			}

			try {
				if (ext.equals("csv")) {
					FileWriter fw = new FileWriter(fname);
					fw.write(text);
					fw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void getFileFilter(){
		eff.addExtension("csv");
		eff.setDescription(Messages.getString("QuantAna.Simulation.Export.FileFilter"));
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
