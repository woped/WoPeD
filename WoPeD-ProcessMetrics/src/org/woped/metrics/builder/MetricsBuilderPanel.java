package org.woped.metrics.builder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.TransferHandler;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.config.IMetricsConfiguration;
import org.woped.core.config.IMetricsConfiguration.MetricThresholdState;
import org.woped.gui.lookAndFeel.WopedButton;
import org.woped.gui.translations.Messages;
import org.woped.metrics.helpers.WatermarkedJTextField;
import org.woped.metrics.metricsCalculation.MetricsUIRequestHandler;
import org.woped.metrics.metricsCalculation.UITypes.UIThreshold;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class MetricsBuilderPanel extends javax.swing.JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3514711132769580063L;
	
	// Button name constants
	protected static final String IMPORT_BTN 		= "importBtn";
	protected static final String EXPORT_BTN 		= "exportBtn";
	protected static final String CHECK_BTN 		= "checkBtn";
	protected static final String SAVE_BTN 		= "saveBtn";
	protected static final String EXIT_BTN 		= "exitBtn";
	protected static final String NEW_BTN 			= "newBtn";
	protected static final String FUNC_BTN 		= "funcBtn";
	protected static final String SAVE_TAB2_BTN 	= "saveTab2Btn";
	protected static final String EXIT_TAB2_BTN 	= "exitTab2Btn";
	protected static final String DELETE_TAB2_BTN 	= "deleteBtn";
	protected static final String SAVE_GROUP_BTN   = "saveGrBtn";
	protected static final String NEW_GROUP_BTN    = "newGrBtn";
	protected static final String CLEAR_GROUP_BTN  = "clearGrBtn";
	protected static final String EXIT_GROUP_BTN   = "exitGrBtn";
	protected static final String ADD_TAB2_BTN		= "addRowBtn";
	protected static final Object[] DEFAULT_CELL_VALUES = { null, "-infinity", "infinity" };
	
	private static final Color COLOR_DEACTIVATED = new Color(220,220,220);

	// List name constants
	protected static final String ALGORITHM_LIST 			= "algorimthList";
	protected static final String VARIABLE_LIST 			= "variableList";
	protected static final String ALGORITHM_LIST_TAB2		= "algorithmListTab2";
	protected static final String VARIABLE_LIST_TAB2		= "variableListTab2";

	protected static JTextField jMetricsFormulaTextField;
	protected static JList jAlgorithmListTab1;
	protected static JList jAlgorithmListTab2;
	protected static JList jAlgorithmListTab3;
	protected static JList jVariableListTab1;
	protected static JList jVariableListTab2;
	protected static JList jVariableListTab3;
	protected static JTextField jMetricsNameTextField;
	protected static JTextField jMetricsDescriptionTextField;
	protected static JTextField jMetricsIDTextField;
	protected static JTextField jMetricsFilePathTextField;
	protected static WopedButton deleteRowButton;
	public static CustomTableModel model;
	protected static JTextField jMetricsLowThresholdTextField;
	protected static JTextField jMetricsHighThresholdTextField;
	protected static JComboBox jComboBoxColor;

	// Private Members
	private JTabbedPane jTabbedPane;
	private JPanel jTabbedPaneMetrics;
	private JPanel jPanelId;
	private JPanel jPanelOptions;
	private JPanel jPanelWestCenter;
	private JPanel jPanelAGCenterEast;
	private WopedButton jButtonCheck;
	private WopedButton jButtonExport;
	private WopedButton jButtonSave;
	private WopedButton jButtonExit;
	private JList jListGroup;
	private JTextField jTextFieldAGDescription;
	private JPanel jPanelAGNorthCenter;
	private JButton jButtonGroupClose;
	private JButton jButtonGroupClear;
	private JButton jButtonGroupSave;
	private JScrollPane jScrollPaneVariablesTab1;
	private JScrollPane jScrollPaneVariablesTab2;
	private JScrollPane jScrollPaneVariablesTab3;
	private JScrollPane jScrollPaneAlgorithmsTab1;
	private JScrollPane jScrollPaneAlgorithmsTab2;
	private JScrollPane jScrollPaneAlgorithmsTab3;
	private JLabel jLabel8;
	private JLabel jLabel7;
	private JPanel jPanel13;
	private JPanel jPanel12;
	private JPanel jListPanelTab3;
	private JLabel jLabel5;
	private JLabel jLabel4;
	private JPanel jPanel11;
	private JPanel jPanelSpacerTab2;
	private JPanel jListPanelTab2;
	private JPanel jPanel8;
	private JButton jOpenFileChooserButton;
	private WopedButton jButtonNew;
	private JButton jButtonPlus;
	private JLabel jAlgorithmListLabel;
	public static JTable jTableValues;
	private static JScrollPane jScrollPanelValue;
	private JPanel jPanelValueSouth;
	private JTextArea jLabelInput;
	private static JPanel jPanelValueCenter;
	private JPanel jPanel1;
	private JPanel jThresholdPanel;
	private JPanel jTabbedPaneThresholds;
	private JButton jButtonMax;
	private JButton jButtonCloseBracket;
	private JButton jButtonOpenBracket;
	private JButton jButtonFloor;
	private JPanel jPanel7;
	private JLabel jLabel3;
	private JList jListAlgoGroup;
	private JButton jButtonLeft;
	private JButton jButtonRight;
	private JPanel jPanelEastW;
	private JPanel jPanel3;
	private JPanel jPanelAlgo;
	private JLabel jVariableListLabel;
	private JPanel jPanelMBCebter;
	private JPanel jPanelMBNorthCenter;
	private JPanel jPanelAGCenterSouth;
	private JTextField jTextField3;
	private JTextField jTextField1;
	private JPanel jPanel4;
	private JPanel jPanelAGCenterNorth;
	private JPanel jPanelAGCenter;
	private JPanel jTabbedPaneGroups;
	private JButton jButtonDiv;
	private JButton jButtonZero;
	private JButton jButtonMulti;
	private JButton jButtonNine;
	private JButton jButtonEight;
	private JButton jButtonSeven;
	private JButton jButtonMinus;
	private JButton jButtonSix;
	private JButton jButtonFive;
	private JButton jButtonFour;
	private JButton jButtonThree;
	private JButton jButtonTwo;
	private JButton jButtonOne;
	private JButton jButtonMin;
	private JButton jButtonE;
	private JButton jButtonAbs;
	private JButton jButtonTan;
	private JButton jButtonPow;
	private JButton jButtonCos;
	private JButton jButtonLog;
	private JButton jButtonCeil;
	private JButton jButtonSin;
	private JButton jButtonSqrt;
	private JPanel jPanelMBNorth;
	private JButton jButtonImport;
	private JPanel jPanelBuilder;
	private JPanel jListPanelTab1;
	private JPanel jPanelSpacerTab1;
	private Container jPanel6;
	private JButton jButtonGroupNew;
	private WopedButton jButtonAddRow;

	private TableColumnModel colmModel;
	private TableColumn tc1;
	private TableColumn tc2;
	private TableColumn tc3;
	
	private static Vector<String> columns;
	private static Vector<String> combo;

	public MetricsBuilderPanel() {
		super();
		initGUI();
		setValues();
	}

	/**
	 * Build up the Metrics Builder UI
	 */
	private void initGUI() {
		try {
			  {
				this.setPreferredSize(new java.awt.Dimension(600, 437));
			  }
			{
				BorderLayout layout = new BorderLayout();
				this.setLayout(layout);
				jTabbedPane = new JTabbedPane();
				jTabbedPane.addChangeListener(new MetricsBuilderListener());
				this.add(jTabbedPane, BorderLayout.CENTER);
				jTabbedPane.setPreferredSize(new java.awt.Dimension(600, 430));
				{
					jTabbedPaneMetrics = new JPanel();
					jTabbedPane.addTab("Metrics", null, jTabbedPaneMetrics, null);
					BorderLayout tabMetricsCreationBLayout = new BorderLayout();
					jTabbedPaneMetrics.setLayout(tabMetricsCreationBLayout);
					jTabbedPaneMetrics.setPreferredSize(new java.awt.Dimension(
							600, 400));
					{
						jListPanelTab1 = new JPanel();
						BorderLayout jPanelWestLayout = new BorderLayout();
						jTabbedPaneMetrics.add(jListPanelTab1, BorderLayout.WEST);
						jListPanelTab1.setLayout(jPanelWestLayout);
						jListPanelTab1.setPreferredSize(new java.awt.Dimension(200,
								400));
						jListPanelTab1.setSize(200, 400);
						{
							jPanelSpacerTab1 = new JPanel();
							jListPanelTab1.add(jPanelSpacerTab1, BorderLayout.EAST);
							jPanelSpacerTab1.setPreferredSize(new java.awt.Dimension(
									49, 372));
							jPanelSpacerTab1.setInheritsPopupMenu(true);
						}
						{
							jPanelWestCenter = new JPanel();
							jListPanelTab1.add(jPanelWestCenter,
									BorderLayout.CENTER);
							GridBagLayout jPanelWestGridLayout = new GridBagLayout();
							jPanelWestGridLayout.rowWeights = new double[] {
									0.0, 0.0, 0.0, 0.1 };
							jPanelWestGridLayout.rowHeights = new int[] { 20,
									195, 20, 7 };
							jPanelWestGridLayout.columnWeights = new double[] { 0.1 };
							jPanelWestGridLayout.columnWidths = new int[] { 7 };
							jPanelWestCenter.setLayout(jPanelWestGridLayout);
							jPanelWestCenter
									.setPreferredSize(new java.awt.Dimension(
											135, 357));
							{
								jAlgorithmListLabel = new JLabel();
								jPanelWestCenter.add(jAlgorithmListLabel,
										new GridBagConstraints(0, 0, 1, 1, 0.0,
												0.0, GridBagConstraints.CENTER,
												GridBagConstraints.BOTH,
												new Insets(0, 0, 0, 0), 0, 0));
								jAlgorithmListLabel.setText(Messages.getString("Metrics.Builder.Algorithms"));
							}
							{
								jVariableListLabel = new JLabel();
								jPanelWestCenter.add(jVariableListLabel,
										new GridBagConstraints(0, 2, 1, 1, 0.0,
												0.0, GridBagConstraints.CENTER,
												GridBagConstraints.BOTH,
												new Insets(0, 0, 0, 0), 0, 0));
								jVariableListLabel.setText(Messages.getString("Metrics.Builder.Variables"));
								jVariableListLabel
										.setPreferredSize(new java.awt.Dimension(
												135, 12));
							}
							{
								jScrollPaneVariablesTab1 = new JScrollPane();
								jPanelWestCenter.add(jScrollPaneVariablesTab1, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
								jScrollPaneVariablesTab1.setAutoscrolls(true);
								{
									jVariableListTab1 = new JList();
									jScrollPaneVariablesTab1.setViewportView(jVariableListTab1);
									jVariableListTab1.setName(VARIABLE_LIST);
									jVariableListTab1.setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
									jVariableListTab1.addMouseListener(new MetricsBuilderListener());
									jVariableListTab1.setDragEnabled(true);
								}
							}
							{
								jScrollPaneAlgorithmsTab1 = new JScrollPane();
								jPanelWestCenter.add(jScrollPaneAlgorithmsTab1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
								jScrollPaneAlgorithmsTab1.setPreferredSize(new java.awt.Dimension(41,137));
								jScrollPaneAlgorithmsTab1.setAutoscrolls(true);
								{
									jAlgorithmListTab1 = new JList();
									jScrollPaneAlgorithmsTab1.setViewportView(jAlgorithmListTab1);
									jAlgorithmListTab1.setDragEnabled(true);
									jAlgorithmListTab1.setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
									jAlgorithmListTab1.addMouseListener(new MetricsBuilderListener());
									jAlgorithmListTab1.setName(ALGORITHM_LIST);
								}
							}
						}
					}
					{
						jPanelBuilder = new JPanel();
						BorderLayout jPanelBuilderLayout = new BorderLayout();
						jPanelBuilder.setLayout(jPanelBuilderLayout);
						jTabbedPaneMetrics.add(jPanelBuilder,
								BorderLayout.CENTER);
						jPanelBuilder.setPreferredSize(new java.awt.Dimension(
								350, 400));
						jPanelBuilder.setSize(395, 400);
						{
							jPanelId = new JPanel();
							BorderLayout jPanelIdLayout = new BorderLayout();
							jPanelIdLayout.setVgap(3);
							jPanelIdLayout.setHgap(3);
							jPanelId.setLayout(jPanelIdLayout);
							jPanelBuilder.add(jPanelId, BorderLayout.NORTH);
							jPanelId.setPreferredSize(new java.awt.Dimension(
									350, 150));
							{
								jPanelMBNorth = new JPanel();
								GridLayout jPanelMBNorthLayout = new GridLayout(
										1, 1);
								jPanelMBNorthLayout.setHgap(5);
								jPanelMBNorthLayout.setVgap(5);
								jPanelMBNorthLayout.setColumns(1);
								jPanelMBNorth.setLayout(jPanelMBNorthLayout);
								jPanelId.add(jPanelMBNorth, BorderLayout.NORTH);
								{
									jMetricsIDTextField = new JTextField("");
									jPanelMBNorth.add(jMetricsIDTextField);
									jMetricsIDTextField
											.setPreferredSize(new java.awt.Dimension(
													110, 25));
									jMetricsIDTextField.setEditable(false);
									jMetricsIDTextField.setBackground(COLOR_DEACTIVATED);
									jMetricsIDTextField.setForeground(Color.DARK_GRAY);
									jMetricsIDTextField.setTransferHandler(new TransferHandler() {
										private static final long serialVersionUID = 8700297967169121889L;

										public boolean canImport(
												TransferHandler.TransferSupport support) {
												return false;
										}
									});
									jMetricsIDTextField.getDocument().addDocumentListener(new MetricsBuilderListener());
								}
								{
									jMetricsNameTextField = new WatermarkedJTextField(Messages.getString("MetricsBuilder.Watermarks.Name"));
									jPanelMBNorth.add(jMetricsNameTextField);
									jMetricsNameTextField
											.setPreferredSize(new java.awt.Dimension(
													236, 25));
									jMetricsNameTextField.setTransferHandler(new TransferHandler(){
										private static final long serialVersionUID = 973043666511565759L;

										public boolean canImport(
												TransferHandler.TransferSupport support) {
												return false;
										}
									});
									jMetricsNameTextField.getDocument().addDocumentListener(new MetricsBuilderListener());
								}
							  }
					    	}
							{
								jPanelMBNorthCenter = new JPanel();
								BorderLayout jPanelMBNorthCenterLayout = new BorderLayout();
								jPanelMBNorthCenterLayout.setVgap(3);
								jPanelMBNorthCenterLayout.setHgap(5);
								jPanelMBNorthCenter.setLayout(jPanelMBNorthCenterLayout);
								jPanelId.add(jPanelMBNorthCenter,
										BorderLayout.SOUTH);
								jPanelMBNorthCenter.setPreferredSize(new java.awt.Dimension(395, 123));
								jPanelMBNorthCenter.setSize(395, 120);
								{
									jMetricsDescriptionTextField = new WatermarkedJTextField(Messages.getString("MetricsBuilder.Watermarks.Description"));
									jPanelMBNorthCenter.add(jMetricsDescriptionTextField, BorderLayout.NORTH);
									jMetricsDescriptionTextField.setPreferredSize(new java.awt.Dimension(395, 68));
									jMetricsDescriptionTextField.setSize(395, 68);
									jMetricsDescriptionTextField.setTransferHandler(new TransferHandler() {
										private static final long serialVersionUID = 8700297967169121889L;

										public boolean canImport(
												TransferHandler.TransferSupport support) {
												return false;
										}
									});
									jMetricsDescriptionTextField.getDocument().addDocumentListener(new MetricsBuilderListener());
								}
								{
									jMetricsFormulaTextField = new WatermarkedJTextField(Messages.getString("MetricsBuilder.Watermarks.Formula"));
									jPanelMBNorthCenter.add(jMetricsFormulaTextField, BorderLayout.CENTER);
									jMetricsFormulaTextField.setPreferredSize(new java.awt.Dimension(395, 25));
									jMetricsFormulaTextField
											.setDropMode(DropMode.USE_SELECTION);
									jMetricsFormulaTextField
											.setTransferHandler(new TransferHandler() {
												private static final long serialVersionUID = -5435327025028911097L;

												public boolean canImport(
														TransferHandler.TransferSupport support) {
													if (!support
															.isDataFlavorSupported(DataFlavor.stringFlavor) || 
															!jMetricsFormulaTextField.isEditable())
														return false;
													else if(!support.getDropLocation().equals(jMetricsDescriptionTextField))
														return true;
													else
														return false;
												}

												public boolean importData(
														TransferHandler.TransferSupport support) {
													if (!canImport(support))
														return false;

													Transferable transferable = support
															.getTransferable();
													String data;
													try {
														data = (String) transferable
																.getTransferData(DataFlavor.stringFlavor);
													} catch (Exception e) {
														return false;
													}
													
													if(ConfigurationManager.getConfiguration().isShowNamesInBuilder())
														data = MetricsUIRequestHandler.algorithmNameToID(data);

													JTextField.DropLocation dl = (JTextField.DropLocation) support
															.getDropLocation();
													int index = dl.getIndex();

													String oldText = jMetricsFormulaTextField.getText();
													
													if(!oldText.equals(Messages.getString("MetricsBuilder.Watermarks.Formula"))) {
														String firstPart = oldText.substring(0, index);
														// If the last character is a blank remove it
														if(firstPart.length() > 0 && firstPart.substring(firstPart.length() - 1, firstPart.length()).equals(" "))
															firstPart = firstPart.substring(0, firstPart.length() - 1);
														
														String secondPart = oldText.substring(index, oldText.length());
														// If the first character is a blank remove it
														if(secondPart.length() > 0 && secondPart.substring(0, 1).equals(" "))
															secondPart = secondPart.substring(1);
												
														jMetricsFormulaTextField
																.setText(firstPart
																		+ " "
																		+ data
																		+ " "
																		+ secondPart);
														
														// If formula now starts with a blank remove that blank
														if(jMetricsFormulaTextField.getText().length() > 0 &&
																jMetricsFormulaTextField.getText().substring(0, 1).equals(" "))
															jMetricsFormulaTextField.setText(jMetricsFormulaTextField.getText().substring(1));
														return true;
													} else {
														jMetricsFormulaTextField.setText(data);
														return true;
													}
												}
											});
									jMetricsFormulaTextField.getDocument().addDocumentListener(new MetricsBuilderListener());
								}
								{
									jPanel8 = new JPanel();
									GridBagLayout jPanel8Layout = new GridBagLayout();
									jPanelMBNorthCenter.add(jPanel8, BorderLayout.SOUTH);
									jPanel8.setPreferredSize(new java.awt.Dimension(395, 25));
									jPanel8Layout.rowWeights = new double[] {0.1};
									jPanel8Layout.rowHeights = new int[] {1};
									jPanel8Layout.columnWeights = new double[] {1.0, 0.01};
									jPanel8Layout.columnWidths = new int[] {7, 1};
									jPanel8.setLayout(jPanel8Layout);
									{
										jOpenFileChooserButton = new JButton();
										jPanel8.add(jOpenFileChooserButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
										jOpenFileChooserButton.setText("...");
									}
									{
										jMetricsFilePathTextField = new JTextField();
										jPanel8.add(jMetricsFilePathTextField, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
//										jMetricsFilePathTextField.setText("C:\\");
										jMetricsFilePathTextField.setPreferredSize(new java.awt.Dimension(391, 25));
										jMetricsFilePathTextField.setSize(338, 25);
										jMetricsFilePathTextField.setEnabled(false);
										jMetricsFilePathTextField.setTransferHandler(new TransferHandler() {
											private static final long serialVersionUID = 6507240735372523805L;

											public boolean canImport(
													TransferHandler.TransferSupport support) {
													return false;
											}
										});
										jMetricsFilePathTextField.getDocument().addDocumentListener(new MetricsBuilderListener());
									}
								}
							}
						}
						{
							jPanelOptions = new JPanel();
							GridLayout jPanelOptionsLayout = new GridLayout(3,2);
							jPanelOptionsLayout.setHgap(5);
							jPanelOptionsLayout.setVgap(5);
							jPanelOptionsLayout.setColumns(2);
							jPanelOptionsLayout.setRows(3);
							jPanelOptions.setLayout(jPanelOptionsLayout);
							jPanelBuilder
									.add(jPanelOptions, BorderLayout.SOUTH);
							/*
							jPanelOptions
									.setPreferredSize(new java.awt.Dimension(350, 75));
									*/
							{
								jButtonNew = new WopedButton();
								jPanelOptions.add(jButtonNew);
								jButtonNew.setIcon(Messages.getImageIcon("Metrics.Builder.Button.New"));
								jButtonNew.setText(Messages.getString("Metrics.Builder.Button.New.Text"));
								jButtonNew.setName(NEW_BTN);
								jButtonNew.setToolTipText(Messages.getString("Metrics.Builder.Button.New.Tooltip"));
								jButtonNew.addActionListener(new MetricsUpdateListener());
							}
							{
								jButtonCheck = new WopedButton();
								jPanelOptions.add(jButtonCheck);
								jButtonCheck.setIcon(Messages.getImageIcon("Metrics.Builder.Button.Check"));
								jButtonCheck.setText(Messages.getString("Metrics.Builder.Button.Check.Text"));
								jButtonCheck.setName(CHECK_BTN);
								jButtonCheck.setToolTipText(Messages.getString("Metrics.Builder.Button.Check.Tooltip"));
								jButtonCheck
								.addActionListener(new MetricsBuilderListener());
							}
							{
								jButtonImport = new WopedButton();
								jPanelOptions.add(jButtonImport);
								jButtonImport.setIcon(Messages.getImageIcon("Metrics.Builder.Button.Import"));
								jButtonImport.setText(Messages.getString("Metrics.Builder.Button.Import.Text"));
								jButtonImport.setName(IMPORT_BTN);
								jButtonImport.setToolTipText(Messages.getString("Metrics.Builder.Button.Import.Tooltip"));
								jButtonImport
								.addActionListener(new MetricsUpdateListener());
							}
							{
								jButtonExport = new WopedButton();
								jPanelOptions.add(jButtonExport);
								jButtonExport.setIcon(Messages.getImageIcon("Metrics.Builder.Button.Export"));
								jButtonExport.setText(Messages.getString("Metrics.Builder.Button.Export.Text"));
								jButtonExport.setName(EXPORT_BTN);
								jButtonExport.setToolTipText(Messages.getString("Metrics.Builder.Button.Export.Tooltip"));
								
								jButtonExport
								.addActionListener(new MetricsBuilderListener());
							}
							{
								jButtonSave = new WopedButton();
								jPanelOptions.add(jButtonSave);
								jButtonSave.setIcon(Messages.getImageIcon("Metrics.Builder.Button.Save"));
								jButtonSave.setText(Messages.getString("Metrics.Builder.Button.Save.Text"));
								jButtonSave.setName(SAVE_BTN);
								jButtonSave.setToolTipText(Messages.getString("Metrics.Builder.Button.Save.Tooltip"));
								
								jButtonSave
								.addActionListener(new MetricsUpdateListener());
							}
							{
								jButtonExit = new WopedButton();
								jPanelOptions.add(jButtonExit);
								jButtonExit.setIcon(Messages.getImageIcon("Metrics.Builder.Button.Exit"));
								jButtonExit.setText(Messages.getString("Metrics.Builder.Button.Exit.Text"));
								jButtonExit.setName(EXIT_BTN);
								jButtonExit.setToolTipText(Messages.getString("Metrics.Builder.Button.Exit.Tooltip"));
								
								jButtonExit
										.addActionListener(new MetricsCloseListener());
							}
						}
					}
				}
				{
					jTabbedPaneThresholds = new JPanel();
					jTabbedPane.addTab("Thresholds", null, jTabbedPaneThresholds, null);
					BorderLayout jTabbedPaneValuesLayout1 = new BorderLayout();
					jTabbedPaneThresholds.setLayout(jTabbedPaneValuesLayout1);
					jTabbedPaneThresholds.setPreferredSize(new java.awt.Dimension(543, 35));
					{
						jListPanelTab2 = new JPanel();
						jTabbedPaneThresholds.add(jListPanelTab2, BorderLayout.WEST);
						BorderLayout jPanel9Layout = new BorderLayout();
						jListPanelTab2.setPreferredSize(new java.awt.Dimension(200, 400));
						jListPanelTab2.setLayout(jPanel9Layout);
						jListPanelTab2.setSize(200, 400);
						{
							jPanelSpacerTab2 = new JPanel();
							jListPanelTab2.add(jPanelSpacerTab2, BorderLayout.EAST);
							jPanelSpacerTab2.setPreferredSize(new java.awt.Dimension(49,372));
							jPanelSpacerTab2.setInheritsPopupMenu(true);
						}
						{
							jPanel11 = new JPanel();
							GridBagLayout jPanel11Layout = new GridBagLayout();
							jPanel11Layout.columnWidths = new int[] {7};
							jPanel11Layout.rowHeights = new int[] {20, 195, 20, 7};
							jPanel11Layout.columnWeights = new double[] {0.1};
							jPanel11Layout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.1};
							jListPanelTab2.add(jPanel11, BorderLayout.CENTER);
							jPanel11.setPreferredSize(new java.awt.Dimension(135,357));
							jPanel11.setLayout(jPanel11Layout);
							{
								jLabel4 = new JLabel();
								jPanel11.add(jLabel4, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
								jLabel4.setText(Messages.getString("Metrics.Builder.Algorithms"));
							}
							{
								jLabel5 = new JLabel();
								jPanel11.add(jLabel5, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
								jLabel5.setText(Messages.getString("Metrics.Builder.Variables"));
								jLabel5.setPreferredSize(new java.awt.Dimension(135,12));
							}
							{
								jScrollPaneAlgorithmsTab2 = new JScrollPane();
								jPanel11.add(jScrollPaneAlgorithmsTab2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
								jScrollPaneAlgorithmsTab2.setPreferredSize(new java.awt.Dimension(41,137));
								jScrollPaneAlgorithmsTab2.setAutoscrolls(true);
								
								{
									jAlgorithmListTab2 = new JList();
									jScrollPaneAlgorithmsTab2.setViewportView(jAlgorithmListTab2);
//									jAlgorithmListTab2.setName(ALGO_LIST);
									jAlgorithmListTab2.setDragEnabled(true);
									jAlgorithmListTab2.setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
									jAlgorithmListTab2.addMouseListener(new MetricsBuilderListener());

									jAlgorithmListTab2.setName(ALGORITHM_LIST_TAB2);

								}
							}
							{
								jScrollPaneVariablesTab2 = new JScrollPane();
								jPanel11.add(jScrollPaneVariablesTab2, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
								jScrollPaneVariablesTab1.setAutoscrolls(true);
								{
									jVariableListTab2 = new JList();
									jScrollPaneVariablesTab2.setViewportView(jVariableListTab2);
									jVariableListTab2.setName(VARIABLE_LIST_TAB2);
									jVariableListTab2.setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
									jVariableListTab2.addMouseListener(new MetricsBuilderListener());
									jVariableListTab2.setDragEnabled(true);
								}
							}

						}
					}
					{
						jThresholdPanel = new JPanel();
						jTabbedPaneThresholds.add(jThresholdPanel, BorderLayout.CENTER);
						BorderLayout jPanelValueWestLayout = new BorderLayout();
						jThresholdPanel.setLayout(jPanelValueWestLayout);
						jThresholdPanel.setPreferredSize(new java.awt.Dimension(350, 400));
						jThresholdPanel.setSize(395, 402);
						{
							jPanel1 = new JPanel();
							BorderLayout jPanel1Layout = new BorderLayout();
							jPanel1.setLayout(jPanel1Layout);
							jThresholdPanel.add(jPanel1, BorderLayout.CENTER);
							jPanel1.setPreferredSize(new java.awt.Dimension(
									223, 180));
							{
								jPanelValueCenter = new JPanel();
								BorderLayout jPanelValueCenterLayout = new BorderLayout();
								jPanelValueCenter
								.setLayout(jPanelValueCenterLayout);
								jPanel1.add(jPanelValueCenter,
										BorderLayout.CENTER);
								{
									jLabelInput = new JTextArea();
									jPanelValueCenter.add(jLabelInput,
											BorderLayout.NORTH);
									jLabelInput.setText(Messages.getString("Metrics.Builder.table.description"));
									Font font = new Font("Verdana", Font.PLAIN, 11);
									jLabelInput.setFont(font);
									jLabelInput.setPreferredSize(new java.awt.Dimension(395, 78));
									jLabelInput.setLineWrap(true);
									jLabelInput.setWrapStyleWord(true);
									jLabelInput.setEditable(false);
								}
								{
									jPanelValueSouth = new JPanel();
									GridLayout jPanelValueSouthLayout = new GridLayout(1, 1);
									jPanelValueSouthLayout.setHgap(5);
									jPanelValueSouthLayout.setVgap(5);
									jPanelValueSouthLayout.setColumns(1);
									jPanelValueSouth.setLayout(jPanelValueSouthLayout);
									jPanelValueCenter.add(jPanelValueSouth,
											BorderLayout.SOUTH);
									jPanelValueSouth.setPreferredSize(new java.awt.Dimension(395, 50));
									{
										jButtonSave = new WopedButton();
										jPanelValueSouth.add(jButtonSave);
										jButtonSave.setName(SAVE_TAB2_BTN);
										jButtonSave.setIcon(Messages.getImageIcon("Metrics.Builder.Save"));
										jButtonSave.setText(Messages.getString("Metrics.Builder.Save.Text"));
										jButtonSave.setToolTipText(Messages.getString("Metrics.Builder.Save.Tooltip"));
										jButtonSave.setPreferredSize(new java.awt.Dimension(43, 67));
										
										jButtonSave.addActionListener(new MetricsBuilderListener());
										
									}
									{
										jButtonAddRow = new WopedButton();
										jPanelValueSouth.add(jButtonAddRow);
										jButtonAddRow.setName(ADD_TAB2_BTN);
										jButtonAddRow.setIcon(Messages.getImageIcon("Metrics.Builder.Button.New"));
										jButtonAddRow.setText(Messages.getString("Metrics.Builder.Button.New.Text"));
										jButtonAddRow.setToolTipText(Messages.getString("Metrics.Builder.Button.New.Tooltip"));
										jButtonAddRow.setPreferredSize(new java.awt.Dimension(43, 67));
										
										jButtonAddRow.addActionListener(new MetricsBuilderListener());
										
									}
									{
										deleteRowButton = new WopedButton();
										jPanelValueSouth.add(deleteRowButton);
										deleteRowButton.setName(DELETE_TAB2_BTN);
										deleteRowButton.setIcon(Messages.getImageIcon("Metrics.Builder.delete"));
										deleteRowButton.setText(Messages.getString("Metrics.Builder.delete.Text"));
										deleteRowButton.setToolTipText(Messages.getString("Metrics-Builder.delete.Tooltip"));
										deleteRowButton.setPreferredSize(new java.awt.Dimension(43, 67));
										
										deleteRowButton.addActionListener(new MetricsCloseListener());
									}
									{
										jButtonExit = new WopedButton();
										jPanelValueSouth.add(jButtonExit);
										jButtonExit.setName(EXIT_TAB2_BTN);
										jButtonExit.setIcon(Messages.getImageIcon("Metrics.Builder.Exit"));
										jButtonExit.setText(Messages.getString("Metrics.Builder.Exit.Text"));
										jButtonExit.setToolTipText(Messages.getString("Metrics.Builder.Exit.Tooltip"));
										jButtonExit.setPreferredSize(new java.awt.Dimension(43, 67));
										
										jButtonExit.addActionListener(new MetricsCloseListener());
									}
									
								}
								{
									// JComboBox for threshold state
									jComboBoxColor = new JComboBox();	
									String[] colors = new String[]{"red", "yellow", "green"};									
									for (String s : colors)
										jComboBoxColor.addItem(s);
									jComboBoxColor.setSelectedIndex(0);
									jComboBoxColor.addItemListener(new MetricsBuilderListener());
									
									// JTableHeader
									columns = new Vector<String>();
									columns.add(Messages.getString("Metrics.Builder.Header.color"));
									columns.add	(Messages.getString("Metrics.Builder.Header.low"));
									columns.add(Messages.getString("Metrics.Builder.Header.up")); 
//									columns.add(Messages.getString("Metrics.Builder.Header.del"));
									
									jMetricsLowThresholdTextField = new JTextField();
									jMetricsHighThresholdTextField = new JTextField();
									
//									deleteRowButton = new JButton();
//									deleteRowButton.setIcon(Messages.getImageIcon("Metrics.Builder.delete"));
//									deleteRowButton.setToolTipText(Messages.getString("Metrics.Builder.delete.Tooltip"));
//									deleteRowButton.setPreferredSize(new java.awt.Dimension(43, 67));									
									
									combo = new Vector<String>();
									combo.add(""+ jComboBoxColor.getSelectedItem());
									
//									del = new Vector<String>();
//									del.add(""+ deleteRowButton);
									
									//data.add(combo);
									//data.add(fieldsLow);
									//data.add(fieldsHigh);		
									 
									model = new CustomTableModel(null,columns);
									model.addRow(DEFAULT_CELL_VALUES);
									 
									 jTableValues = new JTable(model);
									 jTableValues.setPreferredSize(new java.awt.Dimension(392, 232));	
									 jTableValues.addMouseListener(new TableListener());
									 
									 
									 colmModel = jTableValues.getColumnModel();
										tc1 = colmModel.getColumn(0);
								        tc1.setCellEditor(new DefaultCellEditor(jComboBoxColor));
								        tc2 = colmModel.getColumn(1);
								        tc2.setCellEditor(new DefaultCellEditor(jMetricsLowThresholdTextField));
								        tc3 = colmModel.getColumn(2);
								        tc3.setCellEditor(new DefaultCellEditor(jMetricsHighThresholdTextField));
//								        tc4 = colmModel.getColumn(3);
//								        tc4.setCellRenderer(new ButtonEditor(deleteRowButton));
								        
									setVisible(true);
									jScrollPanelValue = new JScrollPane(jTableValues);
									jPanelValueCenter.add(jScrollPanelValue, BorderLayout.CENTER);
									jScrollPanelValue.setPreferredSize(new java.awt.Dimension(395, 261));
									
								}
							}
						}
					}
				}
				{
					jTabbedPaneGroups = new JPanel();
					//jTabbedPane.addTab("Groups", null, jTabbedPaneGroups, null);
					BorderLayout jPanelSecondTabLayout = new BorderLayout();
					jTabbedPaneGroups.setLayout(jPanelSecondTabLayout);
					{
						jListPanelTab3 = new JPanel();
						jTabbedPaneGroups.add(jListPanelTab3, BorderLayout.WEST);
						BorderLayout jPanel2Layout = new BorderLayout();
						jListPanelTab3.setPreferredSize(new java.awt.Dimension(200,400));
						jListPanelTab3.setLayout(jPanel2Layout);
						jListPanelTab3.setSize(200, 400);
						{
							jPanel12 = new JPanel();
							jListPanelTab3.add(jPanel12, BorderLayout.EAST);
							jPanel12.setPreferredSize(new java.awt.Dimension(49,372));
							jPanel12.setInheritsPopupMenu(true);
						}
						{
							jPanel13 = new JPanel();
							GridBagLayout jPanel13Layout = new GridBagLayout();
							jPanel13Layout.columnWidths = new int[] {7};
							jPanel13Layout.rowHeights = new int[] {20, 195, 20, 7};
							jPanel13Layout.columnWeights = new double[] {0.1};
							jPanel13Layout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.1};
							jListPanelTab3.add(jPanel13, BorderLayout.CENTER);
							jPanel13.setPreferredSize(new java.awt.Dimension(135,357));
							jPanel13.setLayout(jPanel13Layout);
							{
								jLabel7 = new JLabel();
								jPanel13.add(jLabel7, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
								jLabel7.setText(Messages.getString("Metrics.Builder.Algorithms"));
							}
							{
								jLabel8 = new JLabel();
								jPanel13.add(jLabel8, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
								jLabel8.setText(Messages.getString("Metrics.Builder.Variables"));
								jLabel8.setPreferredSize(new java.awt.Dimension(135,12));
							}
							{
								jScrollPaneAlgorithmsTab3 = new JScrollPane();
								jPanel13.add(jScrollPaneAlgorithmsTab3, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
								jScrollPaneAlgorithmsTab3.setPreferredSize(new java.awt.Dimension(41,137));
								jScrollPaneAlgorithmsTab3.setAutoscrolls(true);
								{
									ListModel jList1Model = 
										new DefaultComboBoxModel(
												new String[] { "Item One", "Item Two" });
									jAlgorithmListTab3 = new JList();
									jScrollPaneAlgorithmsTab3.setViewportView(jAlgorithmListTab3);
									jAlgorithmListTab3.setModel(jList1Model);
									jAlgorithmListTab3.setDragEnabled(true);
									jAlgorithmListTab3.setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
									jAlgorithmListTab3.setName(ALGORITHM_LIST);
								}
							}
							{
								jScrollPaneVariablesTab3 = new JScrollPane(
										jVariableListTab3);
								jPanel13.add(jScrollPaneVariablesTab3, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
								jScrollPaneVariablesTab3.setPreferredSize(new java.awt.Dimension(125,125));
								jScrollPaneVariablesTab3.setAutoscrolls(true);
								{
									jVariableListTab3 = new JList();
									jScrollPaneVariablesTab3.setViewportView(jVariableListTab3);
									jVariableListTab3.setName(VARIABLE_LIST_TAB2);
									jVariableListTab3.setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
									jVariableListTab3.setDragEnabled(true);
								}
							}
						}
					}
					{
						jPanelAGCenter = new JPanel();
						jTabbedPaneGroups.add(jPanelAGCenter, BorderLayout.EAST);
						BorderLayout jPanelAGCenterLayout = new BorderLayout();
						jPanelAGCenter.setLayout(jPanelAGCenterLayout);
						jPanelAGCenter.setPreferredSize(new java.awt.Dimension(395, 402));
						jPanelAGCenter.setSize(395, 402);
						{
							jPanelAGCenterNorth = new JPanel();
							BorderLayout jPanelAGCenterNorthLayout = new BorderLayout();
							jPanelAGCenterNorth.setLayout(jPanelAGCenterNorthLayout);
							jPanelAGCenter.add(jPanelAGCenterNorth,
									BorderLayout.NORTH);
							jPanelAGCenterNorth
									.setPreferredSize(new java.awt.Dimension(
											350, 85));
							{
								jPanelAGNorthCenter = new JPanel();
								jPanelAGCenterNorth.add(jPanelAGNorthCenter, BorderLayout.CENTER);
								jPanelAGNorthCenter.setPreferredSize(new java.awt.Dimension(395, -5));
								{
									jTextFieldAGDescription = new JTextField();
									jPanelAGNorthCenter.add(jTextFieldAGDescription);
									jTextFieldAGDescription.setPreferredSize(new java.awt.Dimension(393, 48));
								}
							}
							{
								jPanel4 = new JPanel();
								jPanelAGCenterNorth.add(jPanel4, BorderLayout.NORTH);
								jPanel4.setPreferredSize(new java.awt.Dimension(395, 30));
								{
									jTextField1 = new JTextField();
									jPanel4.add(jTextField1);
									jTextField1.setPreferredSize(new java.awt.Dimension(157, 21));
									jTextField1.setPreferredSize(new java.awt.Dimension(169, 25));
								}

								{
									jTextField3 = new JTextField();
									jPanel4.add(jTextField3);
									jTextField3.setPreferredSize(new java.awt.Dimension(176, 22));
									jTextField3.setPreferredSize(new java.awt.Dimension(216, 26));

								}
							}
						}
						{
							jPanelMBCebter = new JPanel();
							jPanelBuilder.add(jPanelMBCebter,
									BorderLayout.CENTER);
							GridBagLayout jPanelMBCebterLayout = new GridBagLayout();
							jPanelMBCebterLayout.columnWidths = new int[] { 7,
									7, 7, 7, 7, 7, 7 };
							jPanelMBCebterLayout.rowHeights = new int[] { 7, 7,
									7, 7 };
							jPanelMBCebterLayout.columnWeights = new double[] {
									0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1 };
							jPanelMBCebterLayout.rowWeights = new double[] {
									0.1, 0.1, 0.1, 0.1 };
							jPanelMBCebter.setLayout(jPanelMBCebterLayout);
							jPanelMBCebter.setPreferredSize(new java.awt.Dimension(395, 203));
							{
								jButtonOne = new JButton();
								jPanelMBCebter.add(jButtonOne,
										new GridBagConstraints(0, 0, 1, 1, 0.0,
												0.0, GridBagConstraints.CENTER,
												GridBagConstraints.NONE,
												new Insets(0, 0, 0, 0), 0, 0));
								jButtonOne.setText("1");
								jButtonOne
										.addActionListener(new MetricsBuilderListener());
							}
							{
								jButtonTwo = new JButton();
								jPanelMBCebter.add(jButtonTwo,
										new GridBagConstraints(1, 0, 1, 1, 0.0,
												0.0, GridBagConstraints.CENTER,
												GridBagConstraints.NONE,
												new Insets(0, 0, 0, 0), 0, 0));
								jButtonTwo.setText("2");
								jButtonTwo
										.addActionListener(new MetricsBuilderListener());
							}
							{
								jButtonThree = new JButton();
								jPanelMBCebter.add(jButtonThree,
										new GridBagConstraints(2, 0, 1, 1, 0.0,
												0.0, GridBagConstraints.CENTER,
												GridBagConstraints.NONE,
												new Insets(0, 0, 0, 0), 0, 0));
								jButtonThree.setText("3");
								jButtonThree
										.addActionListener(new MetricsBuilderListener());
							}
							{
								jButtonPlus = new JButton();
								jPanelMBCebter.add(jButtonPlus,
										new GridBagConstraints(3, 0, 1, 1, 0.0,
												0.0, GridBagConstraints.CENTER,
												GridBagConstraints.NONE,
												new Insets(0, 0, 0, 0), 0, 0));
								jButtonPlus.setText("+");
								jButtonPlus
										.addActionListener(new MetricsBuilderListener());
							}
							{
								jButtonSqrt = new JButton();
								jPanelMBCebter.add(jButtonSqrt,
										new GridBagConstraints(4, 0, 1, 1, 0.0,
												0.0, GridBagConstraints.CENTER,
												GridBagConstraints.NONE,
												new Insets(0, 0, 0, 0), 0, 0));
								jButtonSqrt.setText("SQRT");
								jButtonSqrt.setName(FUNC_BTN);
								jButtonSqrt.setToolTipText(Messages.getString("Metrics.Builder.Function.SQRT"));
								jButtonSqrt
										.addActionListener(new MetricsBuilderListener());
							}
							{
								jButtonSin = new JButton();
								jPanelMBCebter.add(jButtonSin,
										new GridBagConstraints(5, 0, 1, 1, 0.0,
												0.0, GridBagConstraints.CENTER,
												GridBagConstraints.NONE,
												new Insets(0, 0, 0, 0), 0, 0));
								jButtonSin.setText("SIN");
								jButtonSin.setName(FUNC_BTN);
								jButtonSin.setToolTipText(Messages.getString("Metrics.Builder.Function.SIN"));
								jButtonSin
										.addActionListener(new MetricsBuilderListener());
							}
							{
								jButtonCeil = new JButton();
								jPanelMBCebter.add(jButtonCeil,
										new GridBagConstraints(6, 0, 1, 1, 0.0,
												0.0, GridBagConstraints.CENTER,
												GridBagConstraints.NONE,
												new Insets(0, 0, 0, 0), 0, 0));
								jButtonCeil.setText("CEIL");
								jButtonCeil.setName(FUNC_BTN);
								jButtonCeil.setToolTipText(Messages.getString("Metrics.Builder.Function.CEIL"));
								jButtonCeil
										.addActionListener(new MetricsBuilderListener());
							}
							{
								jButtonFour = new JButton();
								jPanelMBCebter.add(jButtonFour,
										new GridBagConstraints(0, 1, 1, 1, 0.0,
												0.0, GridBagConstraints.CENTER,
												GridBagConstraints.NONE,
												new Insets(0, 0, 0, 0), 0, 0));
								jButtonFour.setText("4");
								jButtonFour
										.addActionListener(new MetricsBuilderListener());
							}
							{
								jButtonFive = new JButton();
								jPanelMBCebter.add(jButtonFive,
										new GridBagConstraints(1, 1, 1, 1, 0.0,
												0.0, GridBagConstraints.CENTER,
												GridBagConstraints.NONE,
												new Insets(0, 0, 0, 0), 0, 0));
								jButtonFive.setText("5");
								jButtonFive
										.addActionListener(new MetricsBuilderListener());
							}
							{
								jButtonSix = new JButton();
								jPanelMBCebter.add(jButtonSix,
										new GridBagConstraints(2, 1, 1, 1, 0.0,
												0.0, GridBagConstraints.CENTER,
												GridBagConstraints.NONE,
												new Insets(0, 0, 0, 0), 0, 0));
								jButtonSix.setText("6");
								jButtonSix
										.addActionListener(new MetricsBuilderListener());
							}
							{
								jButtonMinus = new JButton();
								jPanelMBCebter.add(jButtonMinus,
										new GridBagConstraints(3, 1, 1, 1, 0.0,
												0.0, GridBagConstraints.CENTER,
												GridBagConstraints.NONE,
												new Insets(0, 0, 0, 0), 0, 0));
								jButtonMinus.setText("-");
								jButtonMinus
										.addActionListener(new MetricsBuilderListener());
							}
							{
								jButtonLog = new JButton();
								jPanelMBCebter.add(jButtonLog,
										new GridBagConstraints(4, 1, 1, 1, 0.0,
												0.0, GridBagConstraints.CENTER,
												GridBagConstraints.NONE,
												new Insets(0, 0, 0, 0), 0, 0));
								jButtonLog.setText("LOG");
								jButtonLog.setName(FUNC_BTN);
								jButtonLog.setToolTipText(Messages.getString("Metrics.Builder.Function.LOG"));
								jButtonLog
										.addActionListener(new MetricsBuilderListener());
							}
							{
								jButtonCos = new JButton();
								jPanelMBCebter.add(jButtonCos,
										new GridBagConstraints(5, 1, 1, 1, 0.0,
												0.0, GridBagConstraints.CENTER,
												GridBagConstraints.NONE,
												new Insets(0, 0, 0, 0), 0, 0));
								jButtonCos.setText("COS");
								jButtonCos.setName(FUNC_BTN);
								jButtonCos.setToolTipText(Messages.getString("Metrics.Builder.Function.COS"));
								jButtonCos
										.addActionListener(new MetricsBuilderListener());
							}
							{
								jButtonFloor = new JButton();
								jPanelMBCebter.add(jButtonFloor,
										new GridBagConstraints(6, 1, 1, 1, 0.0,
												0.0, GridBagConstraints.CENTER,
												GridBagConstraints.NONE,
												new Insets(0, 0, 0, 0), 0, 0));
								jButtonFloor.setText("FLOOR");
								jButtonFloor.setName(FUNC_BTN);
								jButtonFloor.setToolTipText(Messages.getString("Metrics.Builder.Function.FLOOR"));
								jButtonFloor
										.addActionListener(new MetricsBuilderListener());
							}
							{
								jButtonSeven = new JButton();
								jPanelMBCebter.add(jButtonSeven,
										new GridBagConstraints(0, 2, 1, 1, 0.0,
												0.0, GridBagConstraints.CENTER,
												GridBagConstraints.NONE,
												new Insets(0, 0, 0, 0), 0, 0));
								jButtonSeven.setText("7");
								jButtonSeven
										.addActionListener(new MetricsBuilderListener());
							}
							{
								jButtonEight = new JButton();
								jPanelMBCebter.add(jButtonEight,
										new GridBagConstraints(1, 2, 1, 1, 0.0,
												0.0, GridBagConstraints.CENTER,
												GridBagConstraints.NONE,
												new Insets(0, 0, 0, 0), 0, 0));
								jButtonEight.setText("8");
								jButtonEight
										.addActionListener(new MetricsBuilderListener());
							}
							{
								jButtonNine = new JButton();
								jPanelMBCebter.add(jButtonNine,
										new GridBagConstraints(2, 2, 1, 1, 0.0,
												0.0, GridBagConstraints.CENTER,
												GridBagConstraints.NONE,
												new Insets(0, 0, 0, 0), 0, 0));
								jButtonNine.setText("9");
								jButtonNine
										.addActionListener(new MetricsBuilderListener());
							}
							{
								jButtonMulti = new JButton();
								jPanelMBCebter.add(jButtonMulti,
										new GridBagConstraints(3, 2, 1, 1, 0.0,
												0.0, GridBagConstraints.CENTER,
												GridBagConstraints.NONE,
												new Insets(0, 0, 0, 0), 0, 0));
								jButtonMulti.setText("*");
								jButtonMulti
										.addActionListener(new MetricsBuilderListener());
							}
							{
								jButtonPow = new JButton();
								jPanelMBCebter.add(jButtonPow,
										new GridBagConstraints(4, 2, 1, 1, 0.0,
												0.0, GridBagConstraints.CENTER,
												GridBagConstraints.NONE,
												new Insets(0, 0, 0, 0), 0, 0));
								jButtonPow.setText("POW");
								jButtonPow.setName(FUNC_BTN);
								jButtonPow.setToolTipText(Messages.getString("Metrics.Builder.Function.POW"));
								jButtonPow
										.addActionListener(new MetricsBuilderListener());
							}
							{
								jButtonTan = new JButton();
								jPanelMBCebter.add(jButtonTan,
										new GridBagConstraints(5, 2, 1, 1, 0.0,
												0.0, GridBagConstraints.CENTER,
												GridBagConstraints.NONE,
												new Insets(0, 0, 0, 0), 0, 0));
								jButtonTan.setText("TAN");
								jButtonTan.setName(FUNC_BTN);
								jButtonTan.setToolTipText(Messages.getString("Metrics.Builder.Function.TAN"));
								jButtonTan
										.addActionListener(new MetricsBuilderListener());
							}
							{
								jButtonMin = new JButton();
								jPanelMBCebter.add(jButtonMin,
										new GridBagConstraints(6, 2, 1, 1, 0.0,
												0.0, GridBagConstraints.CENTER,
												GridBagConstraints.NONE,
												new Insets(0, 0, 0, 0), 0, 0));
								jButtonMin.setText("MIN");
								jButtonMin.setName(FUNC_BTN);
								jButtonMin.setToolTipText(Messages.getString("Metrics.Builder.Function.MIN"));
								jButtonMin
										.addActionListener(new MetricsBuilderListener());
							}
							{
								jButtonZero = new JButton();
								jPanelMBCebter.add(jButtonZero,
										new GridBagConstraints(0, 3, 1, 1, 0.0,
												0.0, GridBagConstraints.CENTER,
												GridBagConstraints.NONE,
												new Insets(0, 0, 0, 0), 0, 0));
								jButtonZero.setText("0");
								jButtonZero
										.addActionListener(new MetricsBuilderListener());
							}
							{
								jButtonOpenBracket = new JButton();
								jPanelMBCebter.add(jButtonOpenBracket,
										new GridBagConstraints(1, 3, 1, 1, 0.0,
												0.0, GridBagConstraints.CENTER,
												GridBagConstraints.NONE,
												new Insets(0, 0, 0, 0), 0, 0));
								jButtonOpenBracket.setText("(");
								jButtonOpenBracket
										.addActionListener(new MetricsBuilderListener());
							}
							{
								jButtonCloseBracket = new JButton();
								jPanelMBCebter.add(jButtonCloseBracket,
										new GridBagConstraints(2, 3, 1, 1, 0.0,
												0.0, GridBagConstraints.CENTER,
												GridBagConstraints.NONE,
												new Insets(0, 0, 0, 0), 0, 0));
								jButtonCloseBracket.setText(")");
								jButtonCloseBracket
										.addActionListener(new MetricsBuilderListener());
							}
							{
								jButtonDiv = new JButton();
								jPanelMBCebter.add(jButtonDiv,
										new GridBagConstraints(3, 3, 1, 1, 0.0,
												0.0, GridBagConstraints.CENTER,
												GridBagConstraints.NONE,
												new Insets(0, 0, 0, 0), 0, 0));
								jButtonDiv.setText("/");
								jButtonDiv
										.addActionListener(new MetricsBuilderListener());
							}
							{
								jButtonAbs = new JButton();
								jPanelMBCebter.add(jButtonAbs,
										new GridBagConstraints(4, 3, 1, 1, 0.0,
												0.0, GridBagConstraints.CENTER,
												GridBagConstraints.NONE,
												new Insets(0, 0, 0, 0), 0, 0));
								jButtonAbs.setText("ABS");
								jButtonAbs.setName(FUNC_BTN);
								jButtonAbs.setToolTipText(Messages.getString("Metrics.Builder.Function.ABS"));
								jButtonAbs
										.addActionListener(new MetricsBuilderListener());
							}
							{
								jButtonE = new JButton();
								jPanelMBCebter.add(jButtonE,
										new GridBagConstraints(5, 3, 1, 1, 0.0,
												0.0, GridBagConstraints.CENTER,
												GridBagConstraints.NONE,
												new Insets(0, 0, 0, 0), 0, 0));
								jButtonE.setText("E");
								jButtonE.setName(FUNC_BTN);
								jButtonE.setToolTipText(Messages.getString("Metrics.Builder.Function.E"));
								jButtonE.addActionListener(new MetricsBuilderListener());
							}
							{
								jButtonMax = new JButton();
								jPanelMBCebter.add(jButtonMax,
										new GridBagConstraints(6, 3, 1, 1, 0.0,
												0.0, GridBagConstraints.CENTER,
												GridBagConstraints.NONE,
												new Insets(0, 0, 0, 0), 0, 0));
								jButtonMax.setText("MAX");
								jButtonMax.setName(FUNC_BTN);
								jButtonMax.setToolTipText(Messages.getString("Metrics.Builder.Function.MAX"));
								jButtonMax
										.addActionListener(new MetricsBuilderListener());
							}
						}
						{
							jPanelAGCenterSouth = new JPanel();
							GridLayout jPanelAGCenterSouthLayout = new GridLayout(2, 2);
							jPanelAGCenterSouthLayout.setHgap(5);
							jPanelAGCenterSouthLayout.setVgap(5);
							jPanelAGCenterSouthLayout.setColumns(2);
							jPanelAGCenterSouthLayout.setRows(2);
							jPanelAGCenter.add(jPanelAGCenterSouth,
									BorderLayout.SOUTH);
							jPanelAGCenterSouth.setLayout(jPanelAGCenterSouthLayout);
							jPanelAGCenterSouth
									.setPreferredSize(new java.awt.Dimension(
											350, 43));
							{
								jButtonGroupSave = new JButton();
								jPanelAGCenterSouth.add(jButtonGroupSave);
								jButtonGroupSave.setName(SAVE_GROUP_BTN);
								jButtonGroupSave.setIcon(Messages.getImageIcon("Metrics.Builder.Group.Save"));
								jButtonGroupSave.setText(Messages.getString("Metrics.Builder.Group.Save.Text"));
								jButtonGroupSave.setToolTipText(Messages.getString("Metrics.Builder.Group.Save.Tooltip"));
								jButtonGroupSave.setPreferredSize(new java.awt.Dimension(43, 67));
								jButtonGroupSave.setBounds(0, 0, 195, 23);
								jButtonGroupSave.addActionListener(new MetricsBuilderListener());
							}
							{
								jButtonGroupNew = new JButton();
								jPanelAGCenterSouth.add(jButtonGroupNew);
								jButtonGroupNew.setName(NEW_GROUP_BTN);
								jButtonGroupNew.setIcon(Messages.getImageIcon("Metrics.Builder.Group.New"));
								jButtonGroupNew.setText(Messages.getString("Metrics.Builder.Group.New.Text"));
								jButtonGroupNew.setToolTipText(Messages.getString("Metrics.Builder.Group.New.Tooltip"));
								jButtonGroupNew.setPreferredSize(new java.awt.Dimension(43, 67));
								jButtonGroupNew.setBounds(200, 0, 195, 21);
								jButtonGroupNew.addActionListener(new MetricsBuilderListener());
								
							}
							{
								jButtonGroupClear = new JButton();
								jPanelAGCenterSouth.add(jButtonGroupClear);
								jButtonGroupClear.setName(CLEAR_GROUP_BTN);
								jButtonGroupClear.setIcon(Messages.getImageIcon("Metrics.Builder.Group.Clear"));
								jButtonGroupClear.setText(Messages.getString("Metrics.Builder.Group.Clear.Text"));
								jButtonGroupClear.setToolTipText(Messages.getString("Metrics.Builder.Group.Clear.Tooltip"));
								jButtonGroupClear.setPreferredSize(new java.awt.Dimension(43, 67));
								jButtonGroupClear.setBounds(1, 0, 195, 35);
								jButtonGroupClear.addActionListener(new MetricsBuilderListener());
							}
							{
								jButtonGroupClose = new JButton();
								jPanelAGCenterSouth.add(jButtonGroupClose);
								jButtonGroupClear.setName(EXIT_GROUP_BTN);
								jButtonGroupClose.setIcon(Messages.getImageIcon("Metrics.Builder.Group.Exit"));
								jButtonGroupClose.setText(Messages.getString("Metrics.Builder.Group.Exit.Text"));
								jButtonGroupClose.setToolTipText(Messages.getString("Metrics.Builder.Group.Exit.Tooltip"));
								jButtonGroupClose.setPreferredSize(new java.awt.Dimension(43, 67));
								jButtonGroupClose.addActionListener(new MetricsCloseListener());
								
							}
						}
						{

							jPanelAGCenterEast = new JPanel();
							jPanelAGCenter.add(jPanelAGCenterEast,
									BorderLayout.EAST);
						}
						{
							jPanelAlgo = new JPanel();
							BorderLayout jPanelAlgoLayout = new BorderLayout();
							jPanelAGCenter.add(jPanelAlgo, BorderLayout.CENTER);
							jPanelAlgo.setLayout(jPanelAlgoLayout);
							{
								jPanel7 = new JPanel();
								jPanelAlgo.add(jPanel7, BorderLayout.CENTER);
								jPanel7.setPreferredSize(new java.awt.Dimension(132, 271));
								{
									jListGroup = new JList();
									jPanel7.add(jListGroup);
									jListGroup.setPreferredSize(new java.awt.Dimension(139, 260));
								}
							}
							{
								jPanel3 = new JPanel();
								BorderLayout jPanel3Layout1 = new BorderLayout();
								jPanel3.setLayout(jPanel3Layout1);
								jPanelAlgo.add(jPanel3, BorderLayout.EAST);
								jPanel3.setPreferredSize(new java.awt.Dimension(95, 244));
								{
									jPanelEastW = new JPanel();
									jPanel3.add(jPanelEastW, BorderLayout.WEST);
									jPanelEastW.setPreferredSize(new java.awt.Dimension(87, 244));
									{
										jLabel3 = new JLabel();
										jPanelEastW.add(jLabel3);
										jLabel3.setPreferredSize(new java.awt.Dimension(70, 60));
									}
									{
										jButtonRight = new JButton(Messages.getImageIcon("Metrics.Builder.right"));
										jButtonRight.addActionListener(new MetricsBuilderListener());
										jPanelEastW.add(jButtonRight);
										jButtonRight.setPreferredSize(new java.awt.Dimension(62, 31));
									}
									{
										jButtonLeft = new JButton(Messages.getImageIcon("Metrics.Builder.left"));
										jButtonLeft.addActionListener(new MetricsBuilderListener());
										jPanelEastW.add(jButtonLeft);
										jButtonLeft.setPreferredSize(new java.awt.Dimension(60, 34));
									}
								}
							}

							jPanel6 = new JPanel();
							jPanelAGCenter.add(jPanel6, BorderLayout.EAST);
							jPanel6.setPreferredSize(new java.awt.Dimension(168, 270));
							{
								
								jListAlgoGroup = new JList();
								jPanel6.add(jListAlgoGroup);
								jListAlgoGroup.setPreferredSize(new java.awt.Dimension(156, 258));
							}

						}
					}
				}
		     
		}catch (Exception e) {
			e.printStackTrace();
		}
	 }

 	
	/**
	 * Fills the GUI with matching values.
	 */
	private void setValues() {
		IMetricsConfiguration metricsConfig = ConfigurationManager
				.getMetricsConfiguration();

		// Add algorithms to list
		DefaultListModel algorithmListModel = new DefaultListModel();
		ArrayList<NameIDListEntry> algorithmList = new ArrayList<NameIDListEntry>();
		for (String s : metricsConfig.getAlgorithmIDs())
			algorithmList.add(new NameIDListEntry(s, metricsConfig.getAlgorithmName(s)));
		Collections.sort(algorithmList);
		for (NameIDListEntry entry : algorithmList)	
			algorithmListModel.addElement(entry);
		jAlgorithmListTab1.setModel(algorithmListModel);
		jAlgorithmListTab2.setModel(algorithmListModel);
		jAlgorithmListTab3.setModel(algorithmListModel);
		
		// Add variables to list
		DefaultListModel variableListModel = new DefaultListModel();
		ArrayList<NameIDListEntry> variableList = new ArrayList<NameIDListEntry>();
		for (String s : metricsConfig.getVariableIDs())
			variableList.add(new NameIDListEntry(s, metricsConfig.getVariableName(s)));
		Collections.sort(variableList);
		for (NameIDListEntry entry : variableList)
			variableListModel.addElement(entry);
		jVariableListTab1.setModel(variableListModel);
		jVariableListTab2.setModel(variableListModel);
		jVariableListTab3.setModel(variableListModel);
	}
	
	/**
	 * Called when a specific metric is to be displayed, so that all UI elements
	 * are filled correctly.
	 * @param metricID of the metric that is to be displayed
	 * @return 
	 */
	public static void displayMetric(String metricID){
		IMetricsConfiguration metricsConfig = ConfigurationManager.getMetricsConfiguration();
		
		if(metricsConfig.isAlgorithm(metricID)){
			for(int i = 0; i < MetricsBuilderPanel.jAlgorithmListTab1.getModel().getSize(); i++)
				if(((NameIDListEntry)MetricsBuilderPanel.jAlgorithmListTab1.getModel().getElementAt(i)).getMetricID().equals(metricID)){
					MetricsBuilderPanel.jAlgorithmListTab1.setSelectedIndex(i);
					MetricsBuilderPanel.jAlgorithmListTab2.setSelectedIndex(i);
					MetricsBuilderPanel.jAlgorithmListTab3.setSelectedIndex(i);
					break;
				}
			
			MetricsBuilderPanel.jMetricsIDTextField.setText(metricID);
			MetricsBuilderPanel.jMetricsFormulaTextField.setText(metricsConfig.getAlgorithmFormula(metricID));
			MetricsBuilderPanel.jMetricsNameTextField.setText(metricsConfig.getAlgorithmName(metricID));
			MetricsBuilderPanel.jMetricsDescriptionTextField.setText(metricsConfig.getAlgorithmDescription(metricID));
			MetricsBuilderPanel.jMetricsFilePathTextField.setText(metricsConfig.getMetricOrigin(metricID));
		} else if(metricsConfig.isVariable(metricID)){
	    	for(int i = 0; i < MetricsBuilderPanel.jVariableListTab1.getModel().getSize(); i++)
				if(((NameIDListEntry)MetricsBuilderPanel.jVariableListTab1.getModel().getElementAt(i)).getMetricID().equals(metricID)){
					MetricsBuilderPanel.jVariableListTab1.setSelectedIndex(i);
					MetricsBuilderPanel.jVariableListTab2.setSelectedIndex(i);
					MetricsBuilderPanel.jVariableListTab3.setSelectedIndex(i);
					break;
				}
	    	MetricsBuilderPanel.jMetricsIDTextField.setText(metricID);
			MetricsBuilderPanel.jMetricsFormulaTextField.setText(metricsConfig.getVariableFormula(metricID));
			MetricsBuilderPanel.jMetricsNameTextField.setText(metricsConfig.getVariableName(metricID));
			MetricsBuilderPanel.jMetricsDescriptionTextField.setText(metricsConfig.getVariableDescription(metricID));
			MetricsBuilderPanel.jMetricsFilePathTextField.setText(metricsConfig.getMetricOrigin(metricID));
	    }
	}	
		
	private class MetricsCloseListener extends MetricsBuilderListener{
		protected void closeFrame(){
			getParent().getParent().getParent().getParent().setVisible(false);
		}
	}
	
	private class MetricsUpdateListener extends MetricsBuilderListener{
		protected void update(){
			setValues();
		}
	}
	
	/**
	 * Class for the variables and algorithm list entries in the metric builder,
	 * to be able to show metric names and still have internally a connection to the
	 * unique metric ID.
	 *
	 */
	class NameIDListEntry implements Comparable<NameIDListEntry>
	{
		private String metricID;
		private String metricName;
		
		public NameIDListEntry(String metricID, String metricName){
			this.metricID = metricID;
			this.metricName = metricName;
		}
		
		/**
		 * Depending on the preferences settings return either the metric name or the ID
		 * The toString() will get automatically called by the lists controls.
		 */
		public String toString(){
			if(ConfigurationManager.getConfiguration().isShowNamesInBuilder())
				return metricName;
			else
				return metricID;
		}
		
		public String getMetricID(){return metricID;}
		
		public String getMetricName(){return metricName;}

		public int compareTo(NameIDListEntry entry) {
			if(ConfigurationManager.getConfiguration().isShowNamesInBuilder())
				return metricName.compareTo(entry.metricName);
			else
				return metricID.compareTo(entry.metricID);
		}
	}
	
	public static void setElementsEditable(boolean editable){
		jMetricsNameTextField.setEditable(editable);
		jMetricsDescriptionTextField.setEditable(editable);
		jMetricsFormulaTextField.setEditable(editable);
		jMetricsFilePathTextField.setEditable(editable);
		if(!editable){
			jMetricsNameTextField.setBackground(COLOR_DEACTIVATED);
			jMetricsDescriptionTextField.setBackground(COLOR_DEACTIVATED);
			jMetricsFormulaTextField.setBackground(COLOR_DEACTIVATED);
			jMetricsFilePathTextField.setBackground(COLOR_DEACTIVATED);
			
			jMetricsNameTextField.setForeground(Color.DARK_GRAY);
			jMetricsDescriptionTextField.setForeground(Color.DARK_GRAY);
			jMetricsFormulaTextField.setForeground(Color.DARK_GRAY);
			jMetricsFilePathTextField.setForeground(Color.DARK_GRAY);
		}
		else{
			jMetricsNameTextField.setBackground(Color.WHITE);
			jMetricsDescriptionTextField.setBackground(Color.WHITE);
			jMetricsFormulaTextField.setBackground(Color.WHITE);
			jMetricsFilePathTextField.setBackground(Color.WHITE);
			
			jMetricsNameTextField.setForeground(Color.BLACK);
			jMetricsDescriptionTextField.setForeground(Color.BLACK);
			jMetricsFormulaTextField.setForeground(Color.BLACK);
			jMetricsFilePathTextField.setForeground(Color.BLACK);
		}		
	}
	
	public static void fillTable(String metricsID){
		
		List<UIThreshold> threshold = MetricsUIRequestHandler.getUIThresholds(metricsID);	
		while(model.getRowCount() > 0)
			model.removeRow(0);
		
		for(UIThreshold thresh:threshold){
			MetricThresholdState state = thresh.getThreshold();
			String color = "red";
			if(state == MetricThresholdState.YELLOW)
				color = "yellow";
			else if(state == MetricThresholdState.GREEN)
				color = "green";
			Object[] values = {color, convertThresh(thresh.getFrom()), convertThresh(thresh.getTo())};
			model.addRow( values );
		}

		if(MetricsUIRequestHandler.canBeEdited(metricsID))
			model.setEditable(true);
		else
			model.setEditable(false);
		model.fireTableDataChanged();
	}
	
	private static String convertThresh(double val){
		if(val <= Integer.MIN_VALUE)
			return "-infinity";
		else if(val >= Integer.MAX_VALUE)
			return "infinity";
		else return ""+val;
	}
	
	public static void clear(){
		model.setRowCount(0);
	}
}