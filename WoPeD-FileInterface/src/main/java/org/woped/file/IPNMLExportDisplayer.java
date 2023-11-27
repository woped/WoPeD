package org.woped.file;

import org.woped.core.controller.IStatusBar;

public interface IPNMLExportDisplayer {

  IStatusBar[] getStatusBars();

  int chooseNetChangedBehaviour(Object[] options, String message);

  boolean isSimulationExport();
}
