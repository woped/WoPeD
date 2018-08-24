package org.woped.file.apromore;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import org.jdesktop.swingx.JXCollapsiblePane;

import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.file.apromore.worker.ImportWorker;
import org.woped.gui.lookAndFeel.WopedButton;
import org.woped.gui.translations.Messages;

public class ApromoreImportFrame extends AbstractApromoreFrame {

	private ImportWorker importWorker = null;
	private JXCollapsiblePane cp;
	private JCheckBox edgesToPlacesCheckBox;
	private JCheckBox tasksToTransitionsCheckBox;
	private JCheckBox optimizeCheckBox;
	private JToggleButton toggleButton;

	/**
	 *
	 */
	private static final long serialVersionUID = -2682701694108230125L;

	public ApromoreImportFrame(AbstractApplicationMediator mediator) {
		super(mediator);
		this.setTitle(Messages.getString("Apromore.UI.ImportDialog.Title"));
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

		c.gridy = 3;
		getContentPane().add(getCollapsiblePane(), c);

		c.gridy = 2;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(5,25,5,0);
		getContentPane().add(getToggleButton(), c);

		c.insets = new Insets(5,0,5,0);
		c.gridy = 4;
		c.fill = GridBagConstraints.HORIZONTAL;

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

	private JXCollapsiblePane getCollapsiblePane() {

		if (cp == null){

			cp = new JXCollapsiblePane(new FlowLayout(FlowLayout.CENTER));
			cp.setCollapsed(true);

			cp.getContentPane().add(getEdgesToPlacesCheckbox());
			cp.getContentPane().add(getTaskToTransitionCheckbox());
			cp.getContentPane().add(getOptimizeCheckbox());

		}

		return cp;
	}

	private JToggleButton getToggleButton() {

		if (toggleButton == null){

			Action toggleAction = cp.getActionMap().get(JXCollapsiblePane.TOGGLE_ACTION);
			toggleAction.putValue(JXCollapsiblePane.COLLAPSE_ICON, UIManager.getIcon("Tree.expandedIcon"));
			toggleAction.putValue(JXCollapsiblePane.EXPAND_ICON, UIManager.getIcon("Tree.collapsedIcon"));

			toggleButton = new JToggleButton(toggleAction);
			toggleButton.setText(Messages.getString("Apromore.UI.Import.Options"));
		}

		return toggleButton;
	}

	private JCheckBox getEdgesToPlacesCheckbox(){

		if (edgesToPlacesCheckBox == null){
			edgesToPlacesCheckBox = new JCheckBox();
			edgesToPlacesCheckBox.setText(Messages.getString("Apromore.UI.Import.TreatEdgesAsPlaces"));
		}
		return edgesToPlacesCheckBox;
	}

	private JCheckBox getTaskToTransitionCheckbox(){

		if (tasksToTransitionsCheckBox == null){
			tasksToTransitionsCheckBox = new JCheckBox();
			tasksToTransitionsCheckBox.setText(Messages.getString("Apromore.UI.Import.TreatTasksAsTransitions"));
			tasksToTransitionsCheckBox.setSelected(true);
		}
		return tasksToTransitionsCheckBox;
	}

	private JCheckBox getOptimizeCheckbox(){

		if (optimizeCheckBox == null){
			optimizeCheckBox = new JCheckBox();
			optimizeCheckBox.setText(Messages.getString("Apromore.UI.Import.OptimizeLayout"));
		}
		return optimizeCheckBox;
	}

	private boolean isTasksToTransitions(){
		return getTaskToTransitionCheckbox().isSelected();
	}

	private boolean isEdgesToPlaces(){
		return getEdgesToPlacesCheckbox().isSelected();
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

				importWorker = new ImportWorker(ApromoreImportFrame.this, isEdgesToPlaces(), isTasksToTransitions());
				importWorker.execute();
			}
		};
		queryThread.start();
	}

}