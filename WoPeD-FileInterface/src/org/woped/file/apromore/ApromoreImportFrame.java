package org.woped.file.apromore;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.file.apromore.worker.ImportWorker;
import org.woped.gui.lookAndFeel.WopedButton;
import org.woped.gui.translations.Messages;

public class ApromoreImportFrame extends AbstractApromoreFrame {

	private ImportWorker importWorker = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = -2682701694108230125L;

	public ApromoreImportFrame(AbstractApplicationMediator mediator) {
		super(mediator);
		this.setTitle(Messages.getString("Apromore.UI.Import.Title"));
		this.initialize();
		this.pack();
		this.setModal(true);
		loadProcessList();
		this.setVisible(true);

	}

	private void initialize() {

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(5, 0, 5, 0); // Abstand nach oben/unten von 10px
		getContentPane().add(getButtonPanel(), c);
		getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				processList.clearSelection();
			}
		});

		// Import Event
		processList.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() == 2 && !importing) {
					importing = true;

					loadImport();

				}
			}
		});

		importButton.setEnabled(false);

	}

	private JPanel getButtonPanel() {

		if (buttonPanel == null) {

			buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
//			buttonPanel.setBorder(BorderFactory.createCompoundBorder(
//					BorderFactory.createTitledBorder(Messages
//							.getString("Apromore.UI.ButoonPanelImport")),
//					BorderFactory.createEmptyBorder(0, 2, 0, 2)));

			buttonPanel.add(getImportButton());
			buttonPanel.add(getUpdateButton());
			buttonPanel.add(getCancelButton());
			buttonPanel.add(getWopedProgressBar());

		}

		return buttonPanel;
	}

	@Override
	public WopedButton getImportButton() {

		if (importButton == null) {
			importButton = new WopedButton();
			importButton.setText(Messages.getTitle("Button.Import"));
			importButton.setIcon(Messages.getImageIcon("Button.Import"));
			importButton.setMnemonic(Messages.getMnemonic("Button.Import"));
			importButton.setPreferredSize(new Dimension(130, 25));
			importButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					loadImport();

				}
			});
		}

		return importButton;
	}

	protected void loadImport() {
		getWopedPorgressBar().setIndeterminate(false);
		setButtons(true);
		Thread queryThread = new Thread() {
			@Override
			public void run() {

				if (importWorker != null) {
					importWorker.cancel(true);

				}

				importWorker = new ImportWorker(ApromoreImportFrame.this);
				importWorker.execute();
			}
		};
		queryThread.start();
	}

}