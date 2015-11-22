package org.woped.file.apromore.worker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.apromore.model.FolderType;
import org.woped.config.ApromoreServer;
import org.woped.core.config.ConfigurationManager;
import org.woped.file.apromore.AbstractApromoreFrame;
import org.woped.file.apromore.worker.elements.ReturnElement;

public class ProcessListWorker extends SwingWorker<ReturnElement, Void> {

	private AbstractApromoreFrame parent;

	public ProcessListWorker(AbstractApromoreFrame abstractApromoreFrame) {
		this.parent = abstractApromoreFrame;
	}

	@Override
	protected ReturnElement doInBackground() throws Exception {

		ApromoreServer[] servers = ConfigurationManager.getConfiguration()
				.getApromoreServers();
		parent.getAproAccess().setCurrentServer(
				parent.getServerDropdown().getSelectedIndex());

		ConfigurationManager.getConfiguration().setApromoreUsername(
				servers[parent.getServerDropdown().getSelectedIndex()]
						.getApromoreUserName());

		final boolean serverAviable = parent.getAproAccess()
				.checkIfServerAvailable();

		final List<FolderType> folders = parent.getAproAccess()
				.getFoldersForCurrentUser();
		final String[][] data = parent.getProcessList().getRowData();

		return new ReturnElement(data, folders, serverAviable);
	}

	@Override
	public void done() {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				ReturnElement ele = null;
				try {
					ele = get();
				} catch (InterruptedException | ExecutionException
						| java.util.concurrent.CancellationException e) {

					// e.printStackTrace();
				}

				ArrayList<String> subFolders = new ArrayList<String>();

				if (ele != null) {

					for (FolderType folder : ele.getFolders()) {

						// ParentID is always null ---> no Subfolderstrukture
						if (parent.getProcessFolderBox() != null) {
							parent.getProcessFolderBox().addItem(
									folder.getFolderName());
						}

						subFolders.add(folder.getFolderName().toString());
						// Create JTree Subfolders!

					}

					parent.setApromorTree(subFolders);
					if (ele.getData() == null) {
						parent.getWopedPorgressBar().connectingToSeverFailed();
					} else {

						parent.getWopedPorgressBar().connectingToSeverSuccess();
					}
					parent.getProcessList().setList(ele.getData());
					parent.getWopedPorgressBar().setIndeterminate(false);

					parent.setButtons(ele.getServerAviable());

				}
			}
		});
	}

}