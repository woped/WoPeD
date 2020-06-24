package org.woped.metrics.builder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.config.IMetricsConfiguration;
import org.woped.gui.translations.Messages;
import org.woped.metrics.builder.MetricsBuilderPanel.NameIDListEntry;
import org.woped.metrics.exceptions.CalculateFormulaException;
import org.woped.metrics.exceptions.NestedCalculateFormulaException;
import org.woped.metrics.helpers.LabeledFileFilter;
import org.woped.metrics.metricsCalculation.MetricsCalculator;

/**
 * Class that handles all the events from the metrics builder
 * 
 * @author Philip Allgaier
 * 
 */
public class MetricsBuilderListener implements ActionListener, MouseListener,
		ItemListener, DocumentListener, ChangeListener {

	/**
	 * Indicates whether there are unsaved changes
	 */
	private static boolean dirty = false;

	/**
	 * Indicates that we are right now processing a double click on a list
	 * element, Needed to prevent those action from changing the "dirty"
	 * variable.
	 */
	private static boolean whileMetricFilling = false;

	/**
	 * The ID of the currently edited metric
	 */
	private static String currentMetricID = "";

	/**
	 * Index of the tab that we were on. This variable will get changed in the
	 * ChangeListener, but only after we stored the changed data on the "old"
	 * tab first.
	 */
	private static int oldTabIndex = 0;

	protected void closeFrame() {
	}

	protected void update() {
	}

	protected void addNew(String name) {

	}

	public void actionPerformed(ActionEvent evt) {
		String btnName = ((JButton) evt.getSource()).getName();
		String textAddition = "";
		IMetricsConfiguration metricsConfig = ConfigurationManager
				.getMetricsConfiguration();

		if (btnName != null) {
			if (btnName.equals(MetricsBuilderPanel.NEW_BTN)) {

				String newMetricID = (String) JOptionPane.showInputDialog(null,
						Messages.getString("Metrics.Builder.New.EnterNewID"),
						Messages.getString("Metrics.Builder.New.EnterNewID"),
						JOptionPane.PLAIN_MESSAGE);
				// User canceled
				if(newMetricID == null)
					return;
				else {
					clearFields();
					currentMetricID = "";
				}
				
				if (metricsConfig.isMetricIDInUse(newMetricID))
					JOptionPane
							.showMessageDialog(
									null,
									Messages.getString("Metrics.Builder.New.IDAlreadyInUse")
											+ "!",
									Messages.getString("Metrics.Builder.New.IDAlreadyInUse"),
									JOptionPane.ERROR_MESSAGE);
				else {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					FileFilter filter = new LabeledFileFilter() {
						public boolean accept(File file) {
							if (file.getAbsolutePath().endsWith(".xml"))
								return true;
							return false;
						}

						public String getDescription() {
							return "Metrics XML file (*.xml)";
						}

						public String getExtension() {
							return ".xml";
						}
					};

					fileChooser.addChoosableFileFilter(filter);
					fileChooser.setCurrentDirectory(new File(metricsConfig
							.getCustomMetricsDir() + File.separatorChar));
					File file = null;
					int returnVal = fileChooser.showSaveDialog(null);

					if (returnVal == JFileChooser.APPROVE_OPTION) {
						String path = fileChooser.getSelectedFile()
								.getAbsolutePath();

						if (!path.substring(path.length() - 4, path.length())
								.equals(".xml"))
							path += ".xml";

						file = new File(path);

						// User selected a new file for the new algorithm, so
						// add it
						if (metricsConfig.findFileIDToFileName(file
								.getAbsolutePath()) == -1)
							metricsConfig.addNewMetricFile(file
									.getAbsolutePath());

						metricsConfig.addNewAlgorithm(newMetricID,
								metricsConfig.findFileIDToFileName(file
										.getAbsolutePath()));
						update();
						currentMetricID = newMetricID;
						MetricsBuilderPanel.displayMetric(newMetricID);
					}
					updateMetricWithGUIData();
				}
			} else if (btnName.equals(MetricsBuilderPanel.SAVE_BTN)) {
				updateMetricWithGUIData();
				metricsConfig.endEditSession(true);
				dirty = false;
				metricsConfig.startEditSession();
			} else if (btnName.equals(MetricsBuilderPanel.IMPORT_BTN))
				importMetricsFile();
			else if (btnName.equals(MetricsBuilderPanel.EXPORT_BTN))
				exportMetricsFile();
			else if (btnName.equals(MetricsBuilderPanel.EXIT_BTN)) {
				if (dirty) {
					int option = JOptionPane
							.showConfirmDialog(
									null,
									Messages.getString("Metrics.Builder.Exit.SaveChanges"),
									Messages.getString("Metrics.Builder.Exit.SaveChanges.Title"),
									JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.WARNING_MESSAGE);

					if (option == JOptionPane.YES_OPTION) {
						metricsConfig.endEditSession(true);
						dirty = false;
						currentMetricID = "";
						closeFrame();
					} else if (option == JOptionPane.NO_OPTION) {
						metricsConfig.endEditSession(false);
						dirty = false;
						currentMetricID = "";
						closeFrame();
					} else
						return;

				} else {
					currentMetricID = "";
					closeFrame();
				}
			} else if (btnName.equals(MetricsBuilderPanel.CHECK_BTN)) {
				try {
					MetricsCalculator
							.checkFormula(MetricsBuilderPanel.jMetricsFormulaTextField
									.getText());
					JOptionPane
							.showMessageDialog(
									null,
									Messages.getString("Metrics.Builder.ConsistencyCheck.NoIssuesFound"), //$NON-NLS-1$
									Messages.getString("Metrics.Builder.ConsistencyCheck.ConsistencyChecker"), //$NON-NLS-1$
									JOptionPane.INFORMATION_MESSAGE);
				} catch (CalculateFormulaException cfe) {
					boolean showErrorFrame = true;

					// This logic should check if there is an enhancement
					// available
					if (cfe instanceof NestedCalculateFormulaException) {
						NestedCalculateFormulaException ncfe = (NestedCalculateFormulaException) cfe;

						if (ncfe.hasEnhancementExceptions()) {
							String newLine = System
									.getProperty("line.separator"); //$NON-NLS-1$

							int answer = JOptionPane
									.showConfirmDialog(
											null,
											Messages.getString("Metrics.Builder.ConsistencyCheck.ErrorsCanBeReducedByUsingTheFormula") + newLine + //$NON-NLS-1$
													"\""
													+ ncfe.getEnhancementExceptions()
															.get(0)
															.getEnhancedFormula()
													+ "\""
													+ newLine
													+ Messages
															.getString("Metrics.Builder.ConsistencyCheck.DoYouWantToUseThisFormula"), //$NON-NLS-1$

											Messages.getString("Metrics.Builder.ConsistencyCheck.EnhancementAvailable"), //$NON-NLS-1$
											JOptionPane.YES_NO_OPTION,
											JOptionPane.QUESTION_MESSAGE);

							// Check if Yes is clicked
							if (answer == JOptionPane.YES_OPTION) {
								// Writing the new Formula into the Text-Field
								MetricsBuilderPanel.jMetricsFormulaTextField
										.setText(ncfe
												.getEnhancementExceptions()
												.get(0).getEnhancedFormula());

								// Disable that the original error-screen
								showErrorFrame = false;

								// Run the check process a second time for
								// getting the errors for the new formula
								try {
									// Run the checker
									MetricsCalculator.checkFormula(ncfe
											.getEnhancementExceptions().get(0)
											.getEnhancedFormula());

									// Show the successful Dialog if no errors
									// appear
									JOptionPane
											.showMessageDialog(
													null,
													Messages.getString("Metrics.Builder.ConsistencyCheck.NoIssuesFound"), //$NON-NLS-1$
													Messages.getString("Metrics.Builder.ConsistencyCheck.ConsistencyChecker"), //$NON-NLS-1$
													JOptionPane.INFORMATION_MESSAGE);
								} catch (CalculateFormulaException e) {
									// Show the Error-Frame
									new MetricsBuilderErrorFrame(e);
								}
							}
						}
					}

					// If had not been any enhancement available or the
					// enhancements had not been accepted
					// --> the formula has not been changed
					if (showErrorFrame) {
						new MetricsBuilderErrorFrame(cfe);
					}
				}
			} else if (btnName.equals(MetricsBuilderPanel.DELETE_TAB2_BTN)) {
					
				  int row =	MetricsBuilderPanel.jTableValues.getSelectedRow();
				  
				  MetricsBuilderPanel.jTableValues.editingCanceled(new ChangeEvent(MetricsBuilderPanel.jTableValues));
				  MetricsBuilderPanel.model.removeRow(row);
				  MetricsBuilderPanel.model.fireTableDataChanged();
					
			} else if (btnName.equals(MetricsBuilderPanel.ADD_TAB2_BTN)) {	
				
					MetricsBuilderPanel.model.addRow(MetricsBuilderPanel.DEFAULT_CELL_VALUES);
					MetricsBuilderPanel.model.fireTableDataChanged();

					
			} else if (btnName.equals(MetricsBuilderPanel.CLEAR_GROUP_BTN)) {
			} else if (btnName.equals(MetricsBuilderPanel.EXIT_GROUP_BTN)) {
				closeFrame();
			} else if (btnName.equals(MetricsBuilderPanel.SAVE_TAB2_BTN)) {
				updateMetricWithGUIData();
				metricsConfig.endEditSession(true);
				dirty = false;
				metricsConfig.startEditSession();
			} else if (btnName.equals(MetricsBuilderPanel.EXIT_TAB2_BTN)) {
				closeFrame();
			} else if (btnName.equals(MetricsBuilderPanel.FUNC_BTN))
				textAddition = ((JButton) evt.getSource()).getText()
						.toLowerCase() + "(";
		} else
			textAddition = ((JButton) evt.getSource()).getText();
		if (textAddition != "") {
			int index = MetricsBuilderPanel.jMetricsFormulaTextField
					.getCaretPosition();

			String text = MetricsBuilderPanel.jMetricsFormulaTextField
					.getText();
			String firstPart = text.substring(0, index);
			String secondPart = "";

			if (index < text.length())
				secondPart = text.substring(index, text.length());
			
			if(secondPart.substring(0, 1).equals(" "))
				secondPart = secondPart.substring(1, secondPart.length());

			MetricsBuilderPanel.jMetricsFormulaTextField.setText(firstPart
					+ " " + textAddition + " " + secondPart);
			MetricsBuilderPanel.jMetricsFormulaTextField.grabFocus();
			MetricsBuilderPanel.jMetricsFormulaTextField.setCaretPosition(index
					+ textAddition.length() + 1);

			// If first character is a blank, remove it
			if (MetricsBuilderPanel.jMetricsFormulaTextField.getText().length() > 0
					&& MetricsBuilderPanel.jMetricsFormulaTextField.getText()
							.substring(0, 1).equals(" "))

				MetricsBuilderPanel.jMetricsFormulaTextField
						.setText(MetricsBuilderPanel.jMetricsFormulaTextField
								.getText().substring(1));
		}
	}

	/**
	 * Empties the editable Metrics Builder UI elements
	 */
	private void clearFields() {
		MetricsBuilderPanel.setElementsEditable(true);
		MetricsBuilderPanel.jMetricsIDTextField.setText("");
		MetricsBuilderPanel.jMetricsFormulaTextField.setText("");
		MetricsBuilderPanel.jMetricsNameTextField.setText("");
		MetricsBuilderPanel.jMetricsDescriptionTextField.setText("");
	}

	/**
	 * Import a metrics XML file (= add it to the user metrics folder and load
	 * it)
	 */
	private void importMetricsFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		FileFilter filter = new LabeledFileFilter() {
			public boolean accept(File file) {
				if (file.getAbsolutePath().endsWith(".xml"))
					return true;
				return false;
			}

			public String getDescription() {
				return "Metrics XML file (*.xml)";
			}

			public String getExtension() {
				return ".xml";
			}
		};

		fileChooser.addChoosableFileFilter(filter);
		File importFromFile = null;
		int returnVal = fileChooser.showSaveDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			importFromFile = new File(fileChooser.getSelectedFile()
					.getAbsolutePath());
			IMetricsConfiguration metricsConfig = ConfigurationManager
					.getMetricsConfiguration();
			File importToFile = new File(metricsConfig.getCustomMetricsDir()
					+ File.separatorChar
					+ fileChooser.getSelectedFile().getName());
			if (importToFile.exists())
				JOptionPane
						.showMessageDialog(
								null,
								"A custom metrics file with the same name already exists. Please rename the file.",
								"Error", JOptionPane.ERROR_MESSAGE);
			else {
				// Copy file to usermetrics folder
				try {
					FileReader in = new FileReader(importFromFile);
					FileWriter out = new FileWriter(importToFile);
					int c;
					while ((c = in.read()) != -1)
						out.write(c);

					in.close();
					out.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				// Load content of the file
				metricsConfig.readConfig(importToFile);
				update();
				JOptionPane.showMessageDialog(null,
						"Import finished succesfully.");
			}
		}
	}

	/**
	 * Exports the selected entries from the variables and algorithm lists to a
	 * metrics XML file
	 */
	private void exportMetricsFile() {
		// No selection -> stop export
		if (MetricsBuilderPanel.jAlgorithmListTab1.getSelectedIndices().length == 0
				&& MetricsBuilderPanel.jVariableListTab3.getSelectedIndices().length == 0) {
			JOptionPane
					.showMessageDialog(
							null,
							"Please first select the element from the list on the left you want to export.",
							"No elements selected", JOptionPane.ERROR_MESSAGE);
			return;
		}

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		FileFilter filter = new LabeledFileFilter() {
			public boolean accept(File file) {
				if (file.getAbsolutePath().endsWith(".xml"))
					return true;
				return false;
			}

			public String getDescription() {
				return "Metrics XML file (*.xml)";
			}

			public String getExtension() {
				return ".xml";
			}
		};

		fileChooser.addChoosableFileFilter(filter);
		File exportFile = null;
		boolean valid_selection = false;
		boolean cancel = false;

		while (!valid_selection && !cancel) {
			int returnVal = fileChooser.showSaveDialog(null);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				exportFile = new File(fileChooser.getSelectedFile()
						.getAbsolutePath());

				if (!exportFile.getName().endsWith(
						((LabeledFileFilter) fileChooser.getFileFilter())
								.getExtension()))
					exportFile = new File(
							exportFile.getAbsolutePath()
									+ ((LabeledFileFilter) fileChooser
											.getFileFilter()).getExtension());

				// File already exists -> ask for overwrite

				if (exportFile.exists()) {
					int option = JOptionPane.showConfirmDialog(null,
							Messages.getString("File.Warning.Overwrite.Text"),
							Messages.getString("File.Warning.Overwrite.Title"),
							JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE);

					if (option == JOptionPane.YES_OPTION)
						valid_selection = true;
				} else
					valid_selection = true;
			} else if (returnVal == JFileChooser.CANCEL_OPTION)
				cancel = true;
		}

		ArrayList<String> list = new ArrayList<String>();

		for (int i : MetricsBuilderPanel.jAlgorithmListTab1
				.getSelectedIndices())
			list.add((String) MetricsBuilderPanel.jAlgorithmListTab1.getModel()
					.getElementAt(i));

		for (int i : MetricsBuilderPanel.jVariableListTab3.getSelectedIndices())
			list.add((String) MetricsBuilderPanel.jVariableListTab3.getModel()
					.getElementAt(i));

		IMetricsConfiguration metricsConfig = ConfigurationManager
				.getMetricsConfiguration();
		if (metricsConfig.save(list, exportFile))
			JOptionPane.showMessageDialog(null, "Export finished succesfully.");
		else
			JOptionPane.showMessageDialog(null, "Error while exporting.");
	}

	public void itemStateChanged(ItemEvent e) {

		if (e.getItem().equals("red"))
			MetricsBuilderPanel.jComboBoxColor.setSelectedIndex(0);
		else if (e.getItem().equals("yellow"))
			MetricsBuilderPanel.jComboBoxColor.setSelectedIndex(1);
		else
			MetricsBuilderPanel.jComboBoxColor.setSelectedIndex(2);
	}

	public void mouseClicked(MouseEvent evt) {
		JList list = (JList) evt.getSource();

		if (evt.getClickCount() == 2) {
			int index = list.locationToIndex(evt.getPoint());

			if (index >= 0) {
				list.setSelectedIndex(index);
				String listName = list.getName();

				String metricsID = "";

				if (oldTabIndex == 0) {
					if (listName != null) {
						if (listName.equals(MetricsBuilderPanel.ALGORITHM_LIST)) {
							NameIDListEntry listEntry = (NameIDListEntry) MetricsBuilderPanel.jAlgorithmListTab1
									.getModel().getElementAt(index);
							metricsID = listEntry.getMetricID();
							MetricsBuilderPanel.clear();
							MetricsBuilderPanel.fillTable(metricsID);

						} else if (listName
								.equals(MetricsBuilderPanel.VARIABLE_LIST)) {
							NameIDListEntry listEntry = (NameIDListEntry) MetricsBuilderPanel.jVariableListTab1
									.getModel().getElementAt(index);
							metricsID = listEntry.getMetricID();
							MetricsBuilderPanel.clear();
							MetricsBuilderPanel.fillTable(metricsID);
						} else if (listName
								.equals(MetricsBuilderPanel.ALGORITHM_LIST_TAB2)) {
							NameIDListEntry listEntry = (NameIDListEntry) MetricsBuilderPanel.jAlgorithmListTab2
									.getModel().getElementAt(index);
							metricsID = listEntry.getMetricID();
							MetricsBuilderPanel.clear();
							MetricsBuilderPanel.fillTable(metricsID);
						} else if (listName
								.equals(MetricsBuilderPanel.VARIABLE_LIST_TAB2)) {
							NameIDListEntry listEntry = (NameIDListEntry) MetricsBuilderPanel.jVariableListTab2
									.getModel().getElementAt(index);
							MetricsBuilderPanel.clear();
							metricsID = listEntry.getMetricID();
							MetricsBuilderPanel.fillTable(metricsID);
						}

					}

					whileMetricFilling = true;
					updateMetricWithGUIData();
					MetricsBuilderPanel.displayMetric(metricsID);
					IMetricsConfiguration metricsConfig = ConfigurationManager
							.getMetricsConfiguration();
					MetricsBuilderPanel.setElementsEditable(metricsConfig
							.isCustomMetric(metricsID));
					whileMetricFilling = false;

					currentMetricID = metricsID;
				} else if (oldTabIndex == 1) {
					if (listName != null) {
						if (listName.equals(MetricsBuilderPanel.ALGORITHM_LIST)) {
							NameIDListEntry listEntry = (NameIDListEntry) MetricsBuilderPanel.jAlgorithmListTab1
									.getModel().getElementAt(index);
							metricsID = listEntry.getMetricID();
							MetricsBuilderPanel.clear();
							MetricsBuilderPanel.fillTable(metricsID);

						} else if (listName
								.equals(MetricsBuilderPanel.VARIABLE_LIST)) {
							NameIDListEntry listEntry = (NameIDListEntry) MetricsBuilderPanel.jVariableListTab1
									.getModel().getElementAt(index);
							metricsID = listEntry.getMetricID();
							MetricsBuilderPanel.clear();
							MetricsBuilderPanel.fillTable(metricsID);
						} else if (listName
								.equals(MetricsBuilderPanel.ALGORITHM_LIST_TAB2)) {
							NameIDListEntry listEntry = (NameIDListEntry) MetricsBuilderPanel.jAlgorithmListTab2
									.getModel().getElementAt(index);
							metricsID = listEntry.getMetricID();
							MetricsBuilderPanel.clear();
							MetricsBuilderPanel.fillTable(metricsID);
						} else if (listName
								.equals(MetricsBuilderPanel.VARIABLE_LIST_TAB2)) {
							NameIDListEntry listEntry = (NameIDListEntry) MetricsBuilderPanel.jVariableListTab2
									.getModel().getElementAt(index);
							MetricsBuilderPanel.clear();
							metricsID = listEntry.getMetricID();
							MetricsBuilderPanel.fillTable(metricsID);
						}

					}

					whileMetricFilling = true;
					updateMetricWithGUIData();
					MetricsBuilderPanel.displayMetric(metricsID);
					IMetricsConfiguration metricsConfig = ConfigurationManager
							.getMetricsConfiguration();
					MetricsBuilderPanel.setElementsEditable(metricsConfig
							.isCustomMetric(metricsID));
					whileMetricFilling = false;
					currentMetricID = metricsID;
				}
			}
		}
	}

	public void mouseEntered(MouseEvent evt) {
	}

	public void mouseExited(MouseEvent evt) {
	}

	public void mousePressed(MouseEvent evt) {
	}

	public void mouseReleased(MouseEvent evt) {
	}

	public void changedUpdate(DocumentEvent evt) {
		if (!whileMetricFilling)
			dirty = true;
	}

	public void insertUpdate(DocumentEvent evt) {
		if (!whileMetricFilling)
			dirty = true;
	}

	public void removeUpdate(DocumentEvent evt) {
		if (!whileMetricFilling)
			dirty = true;
	}

	/**
	 * Called on tab change. Will trigger the actual changing of internal metric
	 * data as represented in the GUI (change != save).
	 */
	public void stateChanged(ChangeEvent evt) {
		updateMetricWithGUIData();
		JTabbedPane tabbedPane = (JTabbedPane) evt.getSource();
		oldTabIndex = tabbedPane.getSelectedIndex();
	}

	/**
	 * Will modify the internal metric representation with data from the GUI.
	 * Note: This method will not save the data yet!
	 */
	private boolean updateMetricWithGUIData() {
		if (currentMetricID.isEmpty())
			return false;

		IMetricsConfiguration metricsConfig = ConfigurationManager
				.getMetricsConfiguration();

		// Check on which tab we are to process the correct data
		switch (oldTabIndex) {
		case 0:
			// If this field is not enabled we currently display an internal
			// metric,
			// so there cannot have been any changes -> return
			if (!MetricsBuilderPanel.jMetricsNameTextField.isEnabled())
				return true;

			if (MetricsBuilderPanel.jMetricsFormulaTextField.getText()
					.isEmpty()) {
				JOptionPane
						.showMessageDialog(
								null,
								Messages.getString("Metrics.Builder.General.FormulaNeeded"),
								Messages.getString("Metrics.Builder.General.FormulaNeeded"),
								JOptionPane.WARNING_MESSAGE);
				return false;
			}

			// Since the user can only create new algorithms, we do not need to
			// check for the metric type
			metricsConfig.setAlgorithmName(currentMetricID,
					MetricsBuilderPanel.jMetricsNameTextField.getText());
			metricsConfig.setAlgorithmFormula(currentMetricID,
					MetricsBuilderPanel.jMetricsFormulaTextField.getText());
			metricsConfig.setAlgorithmDescription(currentMetricID,
					MetricsBuilderPanel.jMetricsDescriptionTextField.getText());
			break;

		case 1:
			
			 MetricsBuilderPanel.clear();
			 MetricsBuilderPanel.fillTable(currentMetricID);
			 MetricsBuilderPanel.model.fireTableDataChanged();
			
			break;

		}
		return true;
	}

}
