package org.woped.file;

import javax.swing.JOptionPane;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.IStatusBar;
import org.woped.core.controller.IViewController;
import org.woped.gui.translations.Messages;

public class PNMLExportDisplayer implements IPNMLExportDisplayer {

  private IStatusBar[] statusBars = null;
  private boolean simulationExport = true;

  public PNMLExportDisplayer(AbstractApplicationMediator mediator) {
    IViewController[] iVC = mediator.findViewController(IStatusBar.TYPE);
    statusBars = new IStatusBar[iVC.length];
    for (int i = 0; i < iVC.length; i++) {

      statusBars[i] = (IStatusBar) iVC[i];
    }
  }

  @Override
  public IStatusBar[] getStatusBars() {
    return statusBars;
  }

  @Override
  public int chooseNetChangedBehaviour(Object[] options, String message) {
    return JOptionPane.showOptionDialog(
        null,
        message,
        Messages.getString("Tokengame.ChangedNetDialog.Title"),
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE,
        null,
        options,
        options[0]);
  }

  @Override
  public boolean isSimulationExport() {
    return simulationExport;
  }
}
