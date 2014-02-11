package org.woped.file.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.apromore.manager.model_portal.EditSessionType;
import org.woped.apromore.ApromoreAccess;
import org.woped.apromore.ArrayMaker;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.config.DefaultStaticConfiguration;
import org.woped.gui.translations.Messages;

public class ExportFrame extends JDialog {

	private static final long serialVersionUID = 1L;
	EditSessionType z;
	boolean isNew;
	JTextField serverURLText = null;
	JLabel serverURLLabel = null;
	JLabel lblPnmlExportFunction;
	JLabel lblExportAPnml;
	JLabel lblResults;
	JLabel lblSelectProcess;
	ArrayMaker dc;
	JLabel lblUser;
	JLabel lblDomain;
	JLabel lblProcessName;
	JLabel lblVersion;
	JLabel lblPublic;
	JLabel lblAngaben;
	String[][] rowData;
	final String[] columnNames = {
			Messages.getString("Apromore.Import.UI.ProcessName"),
			Messages.getString("Apromore.Import.UI.ID"),
			Messages.getString("Apromore.Import.UI.Owner"),
			Messages.getString("Apromore.Import.UI.Type"),
			Messages.getString("Apromore.Import.UI.Versions") };
	DefaultTableModel tabModel;
	JTable table;
	JScrollPane scrollPane;
	JComboBox comboBox;
	private int selectedRow = -1;
	ApromoreAccess initAAO;
	
	public static JTextField idField;
	public static JTextField userField;
	public static JTextField versionField;
	public static JTextField domainField;
	public static JTextField processField;
	public static JCheckBox pub;
	JButton btnExport;

	public ExportFrame() {
		initComponents();
		z = new EditSessionType();
		isNew = true;
	}

	public ExportFrame(EditSessionType z) {
		initComponents();
		this.z = z;
		isNew = false;
		idField.setText("" + z.getProcessId());
		versionField.setText(z.getVersionName());
		userField.setText(z.getUsername());
		domainField.setText(z.getDomain());
		processField.setText(z.getProcessName());
		domainField.setEditable(false);
		userField.setEditable(false);
		idField.setEditable(false);
		processField.setEditable(false);
	}

	public EditSessionType showDialog() {
		setModal(true);
		start();
		return z;
	}
	
	public void initComponents() {

		setTitle(Messages.getString("Apromore.Export.UI.Title"));

		Dimension frameSize = new Dimension(800, 600);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int top = (screenSize.height - frameSize.height) / 2;
		int left = (screenSize.width - frameSize.width) / 2;
		setSize(frameSize);
		setLocation(left, top);
		setResizable(false);

		getContentPane().setLayout(null);

		lblPnmlExportFunction = new JLabel(
				Messages.getString("Apromore.Export.UI.Headline"));
		lblPnmlExportFunction
				.setFont(DefaultStaticConfiguration.DEFAULT_HUGELABEL_BOLDFONT);
		lblPnmlExportFunction.setBounds(10, 11, 200, 20);
		getContentPane().add(lblPnmlExportFunction);

		lblExportAPnml = new JLabel(
				Messages.getString("Apromore.Export.UI.ExportDescription"));
		lblExportAPnml.setBounds(10, 36, 333, 14);
		getContentPane().add(lblExportAPnml);
		
		lblAngaben = new JLabel(
				Messages.getString("Apromore.Export.UI.Properties"));
		lblAngaben.setFont(DefaultStaticConfiguration.DEFAULT_LABEL_BOLDFONT);
		lblAngaben.setBounds(10, 61, 228, 14);
		getContentPane().add(lblAngaben);

		serverURLLabel = new JLabel("<html>"
				+ Messages.getString("Configuration.Apromore.Label.ServerURL")
				+ "</html>");
		serverURLLabel.setHorizontalAlignment(JLabel.RIGHT);
		serverURLLabel.setBounds(360, 33, 210, 14);
		getContentPane().add(serverURLLabel);

		serverURLText = new JTextField(
				Messages.getString("Configuration.Apromore.Label.ServerURL"
						+ ":"));
		serverURLText.setColumns(25);
		serverURLText.setBounds(580, 30, 200, 20);
		serverURLText.setText(ConfigurationManager.getConfiguration()
				.getApromoreServerURL());
		serverURLText.setEditable(false);
		getContentPane().add(serverURLText);

		lblUser = new JLabel(Messages.getString("Apromore.Export.UI.User"));
		lblUser.setBounds(10, 90, 86, 14);
		getContentPane().add(lblUser);

		userField = new JTextField();
		userField.setBounds(135, 87, 86, 20);
		getContentPane().add(userField);
		userField.setColumns(10);

		lblDomain = new JLabel(Messages.getString("Apromore.Export.UI.Domain"));
		lblDomain.setBounds(10, 125, 86, 14);
		getContentPane().add(lblDomain);

		domainField = new JTextField();
		domainField.setBounds(135, 122, 86, 20);
		getContentPane().add(domainField);
		domainField.setColumns(10);

		lblProcessName = new JLabel(Messages.getString("Apromore.Export.UI.ProcessName"));
		lblProcessName.setBounds(315, 87, 80, 14);
		getContentPane().add(lblProcessName);

		processField = new JTextField();
		processField.setBounds(440, 84, 86, 20);
		getContentPane().add(processField);
		processField.setColumns(10);

		lblVersion = new JLabel(Messages.getString("Apromore.Export.UI.Version"));
		lblVersion.setBounds(315, 122, 46, 14);
		getContentPane().add(lblVersion);

		versionField = new JTextField();
		versionField.setColumns(10);
		versionField.setBounds(440, 119, 86, 20);
		versionField.setText("1.0");
		getContentPane().add(versionField);
		
		lblPublic = new JLabel (Messages.getString ("Apromore.Export.UI.Public"));
		lblPublic.setBounds(10, 164, 89, 23);
		getContentPane().add(lblPublic);
		
		pub = new JCheckBox();
		pub.setBounds(135, 164, 180, 20);
		getContentPane().add(pub);

		lblResults = new JLabel(
				Messages.getString("Apromore.Export.UI.ExistingProcesses"));
		lblResults
				.setFont(DefaultStaticConfiguration.DEFAULT_HUGELABEL_BOLDFONT);
		lblResults.setBounds(10, 202, 200, 20);
		getContentPane().add(lblResults);

		lblSelectProcess = new JLabel(
				Messages.getString("Apromore.Import.UI.SelectProcess"));
		lblSelectProcess.setBounds(10, 227, 444, 14);
		getContentPane().add(lblSelectProcess);
		
		JButton btnUpdate = new JButton(Messages.getString("Apromore.Export.UI.Update"));
		btnUpdate.setBounds(135, 457, 89, 23);
		getContentPane().add(btnUpdate);
		btnUpdate.setVisible(true);
		
		btnExport = new JButton(
				Messages.getString("Apromore.Export.UI.Export"));
		btnExport.setBounds(10, 457, 89, 23);
		getContentPane().add(btnExport);
		btnExport.setVisible(true);

		tabModel = new DefaultTableModel(null, columnNames);

		initAAO = new ApromoreAccess();
		if (!initAAO.IsOnline()) {
			dispose();
			return;
		}

		dc = new ArrayMaker();
		try {
			rowData = ArrayMaker.run(initAAO.getList());
		} catch (Exception e) {
			Object[] options = { Messages.getString("Apromore.Connect.Error.Button") };
			JOptionPane
					.showOptionDialog(
							null,
							Messages.getString("Apromore.Connect.Error"),
							Messages.getString("Apromore.Connect.Error.Title"),

							JOptionPane.DEFAULT_OPTION,
							JOptionPane.WARNING_MESSAGE,

							null, options, options[0]);
			dispose();
		}

		for (String[] s : rowData) {
			tabModel.addRow(s);
		}

		table = new JTable(tabModel);
		table.setShowVerticalLines(false);

		table.setBounds(10, 261, 588, 169);

		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(10, 252, 764, 194);
		getContentPane().add(scrollPane);

		comboBox = new JComboBox();
		comboBox.setBounds(196, 485, 54, 20);
		comboBox.setVisible(false);
		getContentPane().add(comboBox);
		
		setVisible(true);
	}

	public void start() {

		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SimpleDateFormat sdf = new SimpleDateFormat();
				sdf.applyPattern("yyyy'-'MM'-'dd'T'hh:mm:ss");

				z.setAnnotation("");

				if (isNew) {
					z.setCreationDate(sdf.format(new Date()));
					z.setNativeType("PNML");
				}

				z.setDomain(domainField.getText());
				z.setLastUpdate(sdf.format(new Date()));

				

				z.setProcessName(processField.getText());
				z.setUsername(userField.getText());
				z.setVersionName(versionField.getText());
				z.setWithAnnotation(false);
				

				
					setVisible(false);
					dispose();
				

			}
		});
		setVisible(true);

	}
	
	public static String getUserName(){
		return userField.getText();
	}
	
	public static String getVersion(){
		return versionField.getText();
	}
	
	public static String getDomain(){
		return domainField.getText();
	}
	
	public static String getProcess(){
		return processField.getText();
	}
	
	public static boolean getPub(){
		return pub.isSelected();
	}
}

