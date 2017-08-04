package org.woped.file.apromore.worker;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.woped.file.apromore.AbstractApromoreFrame;

public class ImportWorker extends SwingWorker<Boolean, Void> {

	private AbstractApromoreFrame parent;
	private boolean importSuccess = false;
	private boolean edgesToPlaces;
	private boolean tasksToTransitions;

	public ImportWorker(AbstractApromoreFrame parent, boolean edgesToPlaces, boolean tasksToTransitions) {
		this.parent = parent;
		this.edgesToPlaces = edgesToPlaces;
		this.tasksToTransitions = tasksToTransitions;
	}

	@Override
	protected Boolean doInBackground() throws Exception {

		parent.getWopedPorgressBar().setIndeterminate(true);
		parent.setButtons(false);

		// TODO IMPORT Funktion GUI UND Date Recieve trennen
		// Hier Datenempfang von Server
		return importSuccess;
	}

	@Override
	public void done() {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				parent.setImporting(false);
				boolean succees = parent.getProcessList().importAction(false, edgesToPlaces, tasksToTransitions);

				try {
					get();
					if (succees) {
						parent.dispose(); // ensure Frame is Closing after
					}
				} catch (InterruptedException | ExecutionException e) {
					parent.dispose(); // ensure Frame is Closing after
				}

			}
		});

	}
}