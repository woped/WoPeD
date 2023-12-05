package org.woped.file;

import org.woped.core.controller.IStatusBar;

public class HeadlessPNMLExportDisplayer implements IPNMLExportDisplayer {

  @Override
  public IStatusBar[] getStatusBars() {
    return new IStatusBar[] {};
  }

  @Override
  public int chooseNetChangedBehaviour(Object[] options, String message) {
    return 1;
  }

  @Override
  public boolean isSimulationExport() {
    return false;
  }
}
