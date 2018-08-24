package org.woped.file.apromore.worker;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.woped.file.apromore.ApromoreExportFrame;
import org.woped.file.apromore.processList.ApromoreProcessList;
import org.woped.gui.translations.Messages;

public class ExportWorker extends SwingWorker<Void, Void> {

	private ApromoreExportFrame parent;
	private String version;
	private boolean update;
	private ApromoreProcessList processList;

	public ExportWorker(ApromoreExportFrame parent, String version,
			boolean update) {
		this.version = version;
		this.update = update;
		this.parent = parent;
		processList = parent.getProcessList();
	}

	@Override
	protected Void doInBackground() throws Exception {

		if (parent.getAproAccess().checkIfServerAvailable()) {

			if (update) {

				try {

					processList.updateAction(Integer.valueOf(parent
							.getProcessIDText().getText()), parent
							.getProcessOwnerText().getText(), parent
							.getProcessTypeText().getText(), parent
							.getProcessNameText().getText(), version);
					JOptionPane
							.showMessageDialog(
									null,
									Messages.getString("Apromore.UI.UpdateDialog.Success"),
									Messages.getString("Apromore.UI.UpdateDialog.Title"),
									JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null,
							Messages.getString("Apromore.UI.Error.Export"),
							Messages.getString("Apromore.UI.Error.Title"),
							JOptionPane.ERROR_MESSAGE);
				}

			} else {

				if (!processList.getProcessNames().contains(
						parent.getProcessNameText().getText())) {

					processList.exportAction(parent.getProcessOwnerText()
							.getText(), parent.getProcessFolderBox()
							.getSelectedItem().toString(), parent
							.getProcessNameText().getText(), version);
					parent.showDialog(
							Messages.getString("Apromore.UI.ExportDialog.Success"),
							Messages.getString("Apromore.UI.ExportDialog.Title"),
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					parent.showDialog(
							Messages.getString("Apromore.UI.Error.NameExists"),
							Messages.getString("Apromore.UI.Error.Title"),
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		return null;
	}

	@Override
	public void done() {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				parent.getWopedPorgressBar().setIndeterminate(false);
				parent.setButtons(true);
			}
		});

	}
}