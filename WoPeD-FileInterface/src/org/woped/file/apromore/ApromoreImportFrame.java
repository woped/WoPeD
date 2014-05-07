package org.woped.file.apromore;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.woped.apromore.ApromoreAccess;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.editor.controller.ApplicationMediator;
import org.woped.gui.lookAndFeel.WopedButton;
import org.woped.gui.translations.Messages;

public class ApromoreImportFrame extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private WopedButton 				importButton = null;
	private WopedButton 				cancelButton = null;
	private ApromoreAccess 				aproAccess = null;
	private JPanel 						buttonPanel = null;
	private ApromoreProcessList			processList = null;
	private AbstractApplicationMediator mediator = null;

	public ApromoreImportFrame(AbstractApplicationMediator mediator) {

		this.mediator = mediator;
		aproAccess = new ApromoreAccess();
		processList = new ApromoreProcessList(aproAccess, this, mediator);
		setModal(true);
		setResizable(false);
		initialize();
	}

	public AbstractApplicationMediator getMediator() {
		return mediator;
	}
	
	private String getURI() {
		return ConfigurationManager.getConfiguration()
		.getApromoreServerURL()
		+ ":"
		+ ConfigurationManager.getConfiguration()
				.getApromoreServerPort()
		+ "/"
		+ ConfigurationManager.getConfiguration()
				.getApromoreManagerPath();
	}
	
	private void initialize() {

		try {
			aproAccess.connect(getURI());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					Messages.getString("Apromore.UI.Error.Connect"),
					Messages.getString("Apromore.UI.Error.Title"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		setTitle(Messages.getString("Apromore.UI.Import.Title"));
		setSize(new Dimension(670, 400));
		int left = (int)ApplicationMediator.getDisplayUI().getLocation().getX();
		int top = (int)ApplicationMediator.getDisplayUI().getLocation().getY();
		setLocation(left+400, top+180);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(processList.getFilterPanel(), BorderLayout.NORTH);
		getContentPane().add(processList.getProcessListPanel(), BorderLayout.CENTER);
		processList.getTable().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					processList.importAction();
				}
			}
		});

		getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);
		getContentPane().addMouseListener(new MouseAdapter() {
			   public void mouseClicked(MouseEvent e) {
				   processList.clearSelection();
			   	}
				});
		setVisible(true);
	}
		
	private JPanel getButtonPanel() {

		if (buttonPanel == null) {
			buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			buttonPanel.add(getImportButton());
			buttonPanel.add(getCancelButton());
		}
		
		return buttonPanel;
	}
	
	private WopedButton getImportButton() {

		if (importButton == null) {
			importButton = new WopedButton();
			importButton.setText(Messages.getTitle("Button.Import"));
			importButton.setIcon(Messages.getImageIcon("Button.Import"));
			importButton.setMnemonic(Messages.getMnemonic("Button.Import"));
			importButton.setPreferredSize(new Dimension(130, 25));
			importButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					processList.importAction();
				}
			});
		}

		return importButton;
	}

	private WopedButton getCancelButton() {

		if (cancelButton == null) {
			cancelButton = new WopedButton();
			cancelButton.setText(Messages.getTitle("Button.Cancel"));
			cancelButton.setIcon(Messages.getImageIcon("Button.Cancel"));
			cancelButton.setMnemonic(Messages.getMnemonic("Button.Cancel"));
			cancelButton.setPreferredSize(new Dimension(130, 25));
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					processList.cancelAction();
				}
			});
		}
		
		return cancelButton;
	}
}