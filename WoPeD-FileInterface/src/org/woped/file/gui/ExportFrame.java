package org.woped.file.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apromore.manager.model_portal.EditSessionType;
import org.woped.gui.translations.Messages;

public class ExportFrame extends JDialog {

	private static final long serialVersionUID = 1L;
	EditSessionType z;
	boolean isNew;

	JTextField idField;
	JTextField userField;
	JTextField versionField;
	JTextField domainField;
	JTextField processField;
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

		Dimension frameSize = new Dimension(300, 300);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int top = (screenSize.height - frameSize.height) / 2;
		int left = (screenSize.width - frameSize.width) / 2;
		setSize(frameSize);
		setLocation(left, top);
		setResizable(false);

		getContentPane().setLayout(null);

		JLabel lblId = new JLabel(Messages.getString("Apromore.Export.UI.ID"));
		lblId.setBounds(10, 34, 76, 14);
		getContentPane().add(lblId);

		idField = new JTextField();
		idField.setBounds(104, 34, 180, 20);
		getContentPane().add(idField);
		idField.setColumns(10);

		JLabel lblDomain = new JLabel(
				Messages.getString("Apromore.Export.UI.Domain"));
		lblDomain.setBounds(10, 74, 76, 14);
		getContentPane().add(lblDomain);

		domainField = new JTextField();
		domainField.setColumns(10);
		domainField.setBounds(104, 74, 180, 20);
		getContentPane().add(domainField);

		JLabel lblProcess = new JLabel(
				Messages.getString("Apromore.Export.UI.ProcessName"));
		lblProcess.setBounds(10, 114, 76, 14);
		getContentPane().add(lblProcess);

		processField = new JTextField();
		processField.setColumns(10);
		processField.setBounds(104, 114, 180, 20);
		getContentPane().add(processField);

		JLabel lblUser = new JLabel(
				Messages.getString("Apromore.Export.UI.User"));
		lblUser.setBounds(10, 156, 76, 14);
		getContentPane().add(lblUser);

		userField = new JTextField();
		userField.setColumns(10);
		userField.setBounds(104, 154, 180, 20);
		getContentPane().add(userField);

		JLabel lblVersion = new JLabel(
				Messages.getString("Apromore.Export.UI.Version"));
		lblVersion.setBounds(10, 197, 76, 14);
		getContentPane().add(lblVersion);

		versionField = new JTextField();
		versionField.setColumns(10);
		versionField.setBounds(104, 194, 180, 20);
		getContentPane().add(versionField);

		btnExport = new JButton(Messages.getString("Apromore.Export.UI.Export"));
		btnExport.setBounds(97, 238, 89, 23);
		getContentPane().add(btnExport);

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

				boolean flag = true;

				try {
					z.setProcessId(Integer.parseInt(idField.getText()));
				} catch (Exception e) {
					flag = false;
					Object[] options = { "OK" };
					JOptionPane.showOptionDialog(
							null,
							Messages.getString("Apromore.Export.UI.Error.IDFieldNotInteger"),
							Messages.getString("Apromore.Export.UI.Error.IDFieldNotIntegerTitle"),

							JOptionPane.DEFAULT_OPTION,
							JOptionPane.WARNING_MESSAGE,

							null, options, options[0]);
				}

				z.setProcessName(processField.getText());
				z.setUsername(userField.getText());
				z.setVersionName(versionField.getText());
				z.setWithAnnotation(false);

				if (flag) {
					setVisible(false);
					dispose();
				}

			}
		});
		setVisible(true);

	}
}
