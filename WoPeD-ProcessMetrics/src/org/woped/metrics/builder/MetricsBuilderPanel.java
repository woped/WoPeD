package org.woped.metrics.builder;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListModel;

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
	private static final long serialVersionUID = 3595903611243145797L;
	private JTabbedPane jTabbedPane;
	private JPanel tabMb1;
	private JPanel jPanelId;
	private JPanel jPanelOptions;
	private JButton jButtonLogicalCheck;
	private JButton jButtonExport;
	private JButton jButtonSave;
	private JButton jButtonExit;
	private JButton jButtonFloor;
	private JButton jButtonPlus;
	private JButton jButtonLeft;
	private JButton jButtonRight;
	private JPanel jPanelAGCCenter;
	private JList jListExAG;
	private JPanel jPanel6;
	private JPanel jPanelAGCenterEast;
	private JList jListAG;
	private JPanel jPanelAGCenterWest;
	private JButton jButton8;
	private JButton jButton7;
	private JButton jButton6;
	private JButton jButton4;
	private JPanel jPanel5;
	private JPanel jPanelAGCenterSouth;
	private JTextField jTextFieldAGDescription;
	private JTextField jTextField3;
	private JTextField jTextField2;
	private JTextField jTextField1;
	private JPanel jPanel4;
	private JPanel jPanelAGCenterNorth;
	private JPanel jPanelAGCenter;
	private JList jList1;
	private JPanel jPanelAGWestCenter;
	private JButton jButton2;
	private JButton jButton1;
	private JPanel jPanel3;
	private JPanel jPanelAGWestSouth;
	private JPanel jPanelAGWest;
	private JPanel jPanelSecondTab;
	private JButton jButtonOut;
	private JButton jButtonIn;
	private JLabel jLabel3;
	private JLabel jLabel2;
	private JLabel jLabel1;
	private JLabel jLabelTab;
	private JLabel jLabelFiller;
	private JButton jButtonDiv;
	private JLabel jLabelLeer2;
	private JLabel jLabelLeer1;
	private JButton jButtonZero;
	private JButton jButtonMulti;
	private JButton jButtonNine;
	private JButton jButtonEigth;
	private JButton jButtonSeven;
	private JButton jButtonMinus;
	private JButton jButtonSix;
	private JButton jButtonFive;
	private JButton jButtonFour;
	private JButton jButtonThree;
	private JButton jButtonTwo;
	private JButton jButtonOne;
	private JButton jButtonMin;
	private JButton jButtonMas;
	private JButton jButtonE;
	private JButton jButtonAbs;
	private JButton jButtonTan;
	private JButton jButtonPow;
	private JButton jButtonCos;
	private JButton jButtonLog;
	private JButton jButtonCeil;
	private JButton jButtonSin;
	private JButton jButtonSqrt;
	private JPanel jPanelMBEast;
	private JPanel jPanel2;
	private JPanel jPanel1;
	private JTextField jTextFieldEdtor;
	private JTextField jTextFieldName;
	private JTextField jTextFieldDescription;
	private JPanel jPanelMBCenter;
	private JTextField jTextFieldLang;
	private JTextField jTextFieldID;
	private JPanel jPanelMBNorth;
	private JButton jButtonImport;
	private JButton jButtonMathCheck;
	private JPanel jPanelBuilder;
	private JPanel jPanelEast;
	private JButton jButtonDelete;
	private JButton jButtonNew;
	private JPanel jPanelSouth;
	private JList metricList;
	private JPanel jPanelWest;

	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
		
	public MetricsBuilderPanel() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			this.setPreferredSize(new java.awt.Dimension(584, 382));
			{
				jTabbedPane = new JTabbedPane();
				this.add(jTabbedPane);
				jTabbedPane.setPreferredSize(new java.awt.Dimension(460, 357));
				{
					tabMb1 = new JPanel();
					jTabbedPane.addTab("jPanel1", null, tabMb1, null);
					BorderLayout tabMb1Layout = new BorderLayout();
					tabMb1.setLayout(tabMb1Layout);
					tabMb1.setPreferredSize(new java.awt.Dimension(455, 349));
					{
						jPanelWest = new JPanel();
						BorderLayout jPanelWestLayout = new BorderLayout();
						tabMb1.add(jPanelWest, BorderLayout.WEST);
						jPanelWest.setLayout(jPanelWestLayout);
						jPanelWest.setPreferredSize(new java.awt.Dimension(106, 261));
						{
							jPanelSouth = new JPanel();
							jPanelWest.add(jPanelSouth, BorderLayout.SOUTH);
							jPanelSouth.setPreferredSize(new java.awt.Dimension(112, 33));
							{
								jButtonNew = new JButton();
								jPanelSouth.add(jButtonNew);
								jButtonNew.setText("New");
								jButtonNew.setPreferredSize(new java.awt.Dimension(39, 21));
							}
							{
								jButtonDelete = new JButton();
								jPanelSouth.add(jButtonDelete);
								jButtonDelete.setText("Delete");
							}
						}
						{
							ListModel metricListModel = 
								new DefaultComboBoxModel(
										new String[] { "Item One", "Item Two" });
							metricList = new JList();
							jPanelWest.add(metricList, BorderLayout.CENTER);
							metricList.setModel(metricListModel);
							metricList.setPreferredSize(new java.awt.Dimension(93, 309));
						}
						{
							jPanelEast = new JPanel();
							jPanelWest.add(jPanelEast, BorderLayout.EAST);
							jPanelEast.setPreferredSize(new java.awt.Dimension(30, 296));
							{
								jLabelFiller = new JLabel();
								jPanelEast.add(jLabelFiller);
								jLabelFiller.setPreferredSize(new java.awt.Dimension(-4, -60));
							}
							{
								jLabelTab = new JLabel();
								jPanelEast.add(jLabelTab);
							}
							{
								jLabel1 = new JLabel();
								jPanelEast.add(jLabel1);
							}
							{
								jLabel2 = new JLabel();
								jPanelEast.add(jLabel2);
							}
							{
								jLabel3 = new JLabel();
								jPanelEast.add(jLabel3);
							}
							{
								jButtonIn = new JButton();
								jPanelEast.add(jButtonIn);
								jButtonIn.setText("In");
								jButtonIn.setPreferredSize(new java.awt.Dimension(35, 35));
							}
							{
								jButtonOut = new JButton();
								jPanelEast.add(jButtonOut);
								jButtonOut.setText("out");
							}
						}
					}
					{
						jPanelBuilder = new JPanel();
						BorderLayout jPanelBuilderLayout = new BorderLayout();
						jPanelBuilder.setLayout(jPanelBuilderLayout);
						tabMb1.add(jPanelBuilder, BorderLayout.CENTER);
						{
							jPanelId = new JPanel();
							BorderLayout jPanelIdLayout = new BorderLayout();
							jPanelId.setLayout(jPanelIdLayout);
							jPanelBuilder.add(jPanelId, BorderLayout.NORTH);
							jPanelId.setPreferredSize(new java.awt.Dimension(255, 84));
							{
								jPanelMBCenter = new JPanel();
								GridLayout jPanelMBCenterLayout = new GridLayout(2, 1);
								jPanelMBCenterLayout.setHgap(5);
								jPanelMBCenterLayout.setVgap(5);
								jPanelMBCenterLayout.setColumns(1);
								jPanelMBCenterLayout.setRows(2);
								jPanelMBCenter.setLayout(jPanelMBCenterLayout);
								jPanelId.add(jPanelMBCenter, BorderLayout.CENTER);
								{
									jTextFieldDescription = new JTextField();
									jPanelMBCenter.add(jTextFieldDescription);
									jTextFieldDescription.setText("Description");
								}
								{
									jTextFieldEdtor = new JTextField();
									jPanelMBCenter.add(jTextFieldEdtor);
									jTextFieldEdtor.setText("Editor");
									jTextFieldEdtor.setPreferredSize(new java.awt.Dimension(338, 49));
								}
							}
							{
								jPanelMBNorth = new JPanel();
								jPanelId.add(jPanelMBNorth, BorderLayout.NORTH);
								{
									jTextFieldID = new JTextField();
									jPanelMBNorth.add(jTextFieldID);
									jTextFieldID.setText("ID");
									jTextFieldID.setPreferredSize(new java.awt.Dimension(117, 21));
								}
								{
									jTextFieldLang = new JTextField();
									jPanelMBNorth.add(jTextFieldLang);
									jTextFieldLang.setText("Lang");
									jTextFieldLang.setPreferredSize(new java.awt.Dimension(66, 23));
								}
								{
									jTextFieldName = new JTextField();
									jPanelMBNorth.add(jTextFieldName);
									jTextFieldName.setText("Name");
									jTextFieldName.setPreferredSize(new java.awt.Dimension(147, 22));
								}
							}
						}
						{
							jPanelOptions = new JPanel();
							GridLayout jPanelOptionsLayout = new GridLayout(3, 2);
							jPanelOptionsLayout.setHgap(5);
							jPanelOptionsLayout.setVgap(5);
							jPanelOptionsLayout.setColumns(2);
							jPanelOptionsLayout.setRows(3);
							jPanelOptions.setLayout(jPanelOptionsLayout);
							jPanelBuilder.add(jPanelOptions, BorderLayout.SOUTH);
							jPanelOptions.setPreferredSize(new java.awt.Dimension(255, 49));
							{
								jButtonMathCheck = new JButton();
								jPanelOptions.add(jButtonMathCheck);
								jButtonMathCheck.setText("MathCheck");
							}
							{
								jButtonImport = new JButton();
								jPanelOptions.add(jButtonImport);
								jButtonImport.setText("Import");
							}
							{
								jButtonLogicalCheck = new JButton();
								jPanelOptions.add(jButtonLogicalCheck);
								jButtonLogicalCheck.setText("Logical Check");
							}
							{
								jButtonExport = new JButton();
								jPanelOptions.add(jButtonExport);
								jButtonExport.setText("Export");
							}
							{
								jButtonSave = new JButton();
								jPanelOptions.add(jButtonSave);
								jButtonSave.setText("Save");
							}
							{
								jButtonExit = new JButton();
								jPanelOptions.add(jButtonExit);
								jButtonExit.setText("Exit");
							}
						}
						{
							jPanel1 = new JPanel();
							jPanelBuilder.add(jPanel1, BorderLayout.CENTER);
							BorderLayout jPanel1Layout = new BorderLayout();
							jPanel1.setLayout(jPanel1Layout);
							jPanel1.setPreferredSize(new java.awt.Dimension(338, 133));
							{
								jPanel2 = new JPanel();
								GridBagLayout jPanel2Layout = new GridBagLayout();
								jPanel2Layout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
								jPanel2Layout.rowHeights = new int[] {7, 7, 7, 7};
								jPanel2Layout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
								jPanel2Layout.columnWidths = new int[] {7, 7, 7, 7};
								jPanel2.setLayout(jPanel2Layout);
								jPanel1.add(jPanel2, BorderLayout.WEST);
								jPanel2.setPreferredSize(new java.awt.Dimension(144, 196));
								{
									jButtonOne = new JButton();
									jPanel2.add(jButtonOne, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
									jButtonOne.setText("1");
								}
								{
									jButtonTwo = new JButton();
									jPanel2.add(jButtonTwo, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
									jButtonTwo.setText("2");
								}
								{
									jButtonThree = new JButton();
									jPanel2.add(jButtonThree, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
									jButtonThree.setText("3");
								}
								{
									jButtonPlus = new JButton();
									jPanel2.add(jButtonPlus, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
									jButtonPlus.setText("+");
								}
								{
									jButtonFour = new JButton();
									jPanel2.add(jButtonFour, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
									jButtonFour.setText("4");
								}
								{
									jButtonFive = new JButton();
									jPanel2.add(jButtonFive, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
									jButtonFive.setText("5");
								}
								{
									jButtonSix = new JButton();
									jPanel2.add(jButtonSix, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
									jButtonSix.setText("6");
								}
								{
									jButtonMinus = new JButton();
									jPanel2.add(jButtonMinus, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
									jButtonMinus.setText("-");
								}
								{
									jButtonSeven = new JButton();
									jPanel2.add(jButtonSeven, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
									jButtonSeven.setText("7");
								}
								{
									jButtonEigth = new JButton();
									jPanel2.add(jButtonEigth, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
									jButtonEigth.setText("8");
								}
								{
									jButtonNine = new JButton();
									jPanel2.add(jButtonNine, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
									jButtonNine.setText("9");
								}
								{
									jButtonMulti = new JButton();
									jPanel2.add(jButtonMulti, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
									jButtonMulti.setText("*");
								}
								{
									jButtonZero = new JButton();
									jPanel2.add(jButtonZero, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
									jButtonZero.setText("0");
								}
								{
									jLabelLeer1 = new JLabel();
									jPanel2.add(jLabelLeer1, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
								}
								{
									jLabelLeer2 = new JLabel();
									jPanel2.add(jLabelLeer2, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
								}
								{
									jButtonDiv = new JButton();
									jPanel2.add(jButtonDiv, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
									jButtonDiv.setText("/");
								}
							}
							{
								jPanelMBEast = new JPanel();
								GridLayout jPanelMBEastLayout = new GridLayout(4, 3);
								jPanelMBEastLayout.setHgap(5);
								jPanelMBEastLayout.setVgap(5);
								jPanelMBEastLayout.setColumns(3);
								jPanelMBEastLayout.setRows(4);
								jPanelMBEast.setLayout(jPanelMBEastLayout);
								jPanel1.add(jPanelMBEast, BorderLayout.EAST);
								jPanelMBEast.setPreferredSize(new java.awt.Dimension(165, 147));
								{
									jButtonSqrt = new JButton();
									jPanelMBEast.add(jButtonSqrt);
									jButtonSqrt.setText("sqrt");
								}
								{
									jButtonSin = new JButton();
									jPanelMBEast.add(jButtonSin);
									jButtonSin.setText("sin");
								}
								{
									jButtonCeil = new JButton();
									jPanelMBEast.add(jButtonCeil);
									jButtonCeil.setText("ceil");
								}
								{
									jButtonLog = new JButton();
									jPanelMBEast.add(jButtonLog);
									jButtonLog.setText("log");
								}
								{
									jButtonCos = new JButton();
									jPanelMBEast.add(jButtonCos);
									jButtonCos.setText("cos");
								}
								{
									jButtonFloor = new JButton();
									jPanelMBEast.add(jButtonFloor);
									jButtonFloor.setText("floor");
								}
								{
									jButtonPow = new JButton();
									jPanelMBEast.add(jButtonPow);
									jButtonPow.setText("pow");
								}
								{
									jButtonTan = new JButton();
									jPanelMBEast.add(jButtonTan);
									jButtonTan.setText("tan");
								}
								{
									jButtonMas = new JButton();
									jPanelMBEast.add(jButtonMas);
									jButtonMas.setText("max");
								}
								{
									jButtonAbs = new JButton();
									jPanelMBEast.add(jButtonAbs);
									jButtonAbs.setText("abs");
								}
								{
									jButtonE = new JButton();
									jPanelMBEast.add(jButtonE);
									jButtonE.setText("e");
								}
								{
									jButtonMin = new JButton();
									jPanelMBEast.add(jButtonMin);
									jButtonMin.setText("min");
								}
							}
						}
					}
				}
				{
					jPanelSecondTab = new JPanel();
					BorderLayout jPanelSecondTabLayout = new BorderLayout();
					jPanelSecondTab.setLayout(jPanelSecondTabLayout);
					jTabbedPane.addTab("AlgoGroup", null, jPanelSecondTab, null);
					{
						jPanelAGWest = new JPanel();
						BorderLayout jPanelAGWestLayout = new BorderLayout();
						jPanelSecondTab.add(jPanelAGWest, BorderLayout.WEST);
						jPanelAGWest.setLayout(jPanelAGWestLayout);
						jPanelAGWest.setPreferredSize(new java.awt.Dimension(105, 329));
						{
							jPanelAGWestSouth = new JPanel();
							jPanelAGWest.add(jPanelAGWestSouth, BorderLayout.SOUTH);
							jPanelAGWestSouth.setPreferredSize(new java.awt.Dimension(105, 34));
							{
								jPanel3 = new JPanel();
								jPanelAGWestSouth.add(jPanel3);
								jPanel3.setPreferredSize(new java.awt.Dimension(112,33));
								{
									jButton1 = new JButton();
									jPanel3.add(jButton1);
									jButton1.setText("New");
									jButton1.setPreferredSize(new java.awt.Dimension(39,21));
								}
								{
									jButton2 = new JButton();
									jPanel3.add(jButton2);
									jButton2.setText("Delete");
								}
							}
						}
						{
							jPanelAGWestCenter = new JPanel();
							jPanelAGWest.add(jPanelAGWestCenter, BorderLayout.CENTER);
							{
								ListModel jList1Model = 
									new DefaultComboBoxModel(
											new String[] { "Item One", "Item Two" });
								jList1 = new JList();
								jPanelAGWestCenter.add(jList1);
								jList1.setModel(jList1Model);
								jList1.setPreferredSize(new java.awt.Dimension(92, 296));
							}
						}
					}
					{
						jPanelAGCenter = new JPanel();
						jPanelSecondTab.add(jPanelAGCenter, BorderLayout.CENTER);
						BorderLayout jPanelAGCenterLayout = new BorderLayout();
						jPanelAGCenter.setLayout(jPanelAGCenterLayout);
						{
							jPanelAGCenterNorth = new JPanel();
							jPanelAGCenter.add(jPanelAGCenterNorth, BorderLayout.NORTH);
							jPanelAGCenterNorth.setPreferredSize(new java.awt.Dimension(350, 85));
							{
								jPanel4 = new JPanel();
								jPanelAGCenterNorth.add(jPanel4);
								jPanel4.setPreferredSize(new java.awt.Dimension(349, 79));
								{
									jTextField1 = new JTextField();
									jPanel4.add(jTextField1);
									jTextField1.setText("ID");
									jTextField1.setPreferredSize(new java.awt.Dimension(117,21));
								}
								{
									jTextField2 = new JTextField();
									jPanel4.add(jTextField2);
									jTextField2.setText("Lang");
									jTextField2.setPreferredSize(new java.awt.Dimension(66,23));
								}
								{
									jTextField3 = new JTextField();
									jPanel4.add(jTextField3);
									jTextField3.setText("Name");
									jTextField3.setPreferredSize(new java.awt.Dimension(147,22));
								}
								{
									jTextFieldAGDescription = new JTextField();
									jPanel4.add(jTextFieldAGDescription);
									jTextFieldAGDescription.setText("Description");
									jTextFieldAGDescription.setPreferredSize(new java.awt.Dimension(338, 37));
								}
							}
						}
						{
							jPanelAGCenterSouth = new JPanel();
							FlowLayout jPanelAGCenterSouthLayout = new FlowLayout();
							jPanelAGCenter.add(jPanelAGCenterSouth, BorderLayout.SOUTH);
							jPanelAGCenterSouth.setLayout(jPanelAGCenterSouthLayout);
							jPanelAGCenterSouth.setPreferredSize(new java.awt.Dimension(350, 43));
							{
								jPanel5 = new JPanel();
								jPanelAGCenterSouth.add(jPanel5);
								GridLayout jPanel5Layout = new GridLayout(3, 2);
								jPanel5Layout.setHgap(5);
								jPanel5Layout.setVgap(5);
								jPanel5Layout.setColumns(2);
								jPanel5Layout.setRows(3);
								jPanel5.setPreferredSize(new java.awt.Dimension(255,49));
								jPanel5.setLayout(jPanel5Layout);
								{
									jButton4 = new JButton();
									jPanel5.add(jButton4);
									jButton4.setText("Import");
								}
								{
									jButton6 = new JButton();
									jPanel5.add(jButton6);
									jButton6.setText("Export");
								}
								{
									jButton7 = new JButton();
									jPanel5.add(jButton7);
									jButton7.setText("Save");
								}
								{
									jButton8 = new JButton();
									jPanel5.add(jButton8);
									jButton8.setText("Exit");
								}
							}
						}
						{
							jPanelAGCenterWest = new JPanel();
							jPanelAGCenter.add(jPanelAGCenterWest, BorderLayout.WEST);
							jPanelAGCenterWest.setPreferredSize(new java.awt.Dimension(122, 201));
							{
								ListModel jListAGModel = 
									new DefaultComboBoxModel(
											new String[] { "Item One", "Item Two" });
								jListAG = new JList();
								jPanelAGCenterWest.add(jListAG);
								jListAG.setModel(jListAGModel);
								jListAG.setPreferredSize(new java.awt.Dimension(148, 195));
							}
						}
						{
							jPanelAGCenterEast = new JPanel();
							jPanelAGCenter.add(jPanelAGCenterEast, BorderLayout.EAST);
						}
						{
							jPanel6 = new JPanel();
							jPanelAGCenter.add(jPanel6, BorderLayout.EAST);
							jPanel6.setPreferredSize(new java.awt.Dimension(119, 201));
							{
								ListModel jListExAGModel = 
									new DefaultComboBoxModel(
											new String[] { "Item One", "Item Two" });
								jListExAG = new JList();
								jPanel6.add(jListExAG);
								jListExAG.setModel(jListExAGModel);
								jListExAG.setPreferredSize(new java.awt.Dimension(129, 196));
							}
						}
						{
							jPanelAGCCenter = new JPanel();
							jPanelAGCenter.add(jPanelAGCCenter, BorderLayout.CENTER);
							{
								jButtonRight = new JButton();
								jPanelAGCCenter.add(jButtonRight);
								jButtonRight.setText("Right");
								jButtonRight.setPreferredSize(new java.awt.Dimension(68, 32));
							}
							{
								jButtonLeft = new JButton();
								jPanelAGCCenter.add(jButtonLeft);
								jButtonLeft.setText("Left");
								jButtonLeft.setPreferredSize(new java.awt.Dimension(67, 30));
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
