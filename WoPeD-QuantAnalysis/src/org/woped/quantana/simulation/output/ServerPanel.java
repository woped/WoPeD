package org.woped.quantana.simulation.output;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.woped.quantana.gui.QuantitativeSimulationDialog;

@SuppressWarnings("unused")
public class ServerPanel extends JPanel {
	
	private static final long serialVersionUID = 5L;
	
	private String name = "";
	private String id = "";
	private QuantitativeSimulationDialog sod;
	
	JLabel lblHeading = new JLabel("Server Statistics:");
	JTextArea txtCounters = new JTextArea(10, 30);
	
	public ServerPanel(QuantitativeSimulationDialog sod, String id, String name){
		this.id = id;
		this.name = name;
		this.sod = sod;
		
		this.setPreferredSize(new Dimension(500,400));
		init();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	private void init(){
		JLabel lblDummy = new JLabel();
		JLabel lblRight = new JLabel();
		
		txtCounters.setPreferredSize(new Dimension(250, 200));
		txtCounters.setBorder(BorderFactory.createEtchedBorder());
		txtCounters.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
		txtCounters.setEditable(false);
		
		/*JLabel lblMiddle = new JLabel();
		statsPanel = new JPanel();
		graphPanel = new JPanel();
		
		txtNumCalls.setPreferredSize(new Dimension(100, 20));
		txtBusy.setPreferredSize(new Dimension(100, 20));
		txtQueueLength.setPreferredSize(new Dimension(100, 20));
		txtMaxWaitTimeOfCase.setPreferredSize(new Dimension(100, 20));
		txtMaxQueueLength.setPreferredSize(new Dimension(100, 20));
		txtZeroDelays.setPreferredSize(new Dimension(100, 20));
		txtNumAccess.setPreferredSize(new Dimension(100, 20));
		txtNumDeparture.setPreferredSize(new Dimension(100, 20));
		txtMaxNumCasesInParallel.setPreferredSize(new Dimension(100, 20));
		txtNumCasesInParallel.setPreferredSize(new Dimension(100, 20));
		*/
		/*GridBagLayout genLayout = new GridBagLayout();
		statsPanel.setLayout(genLayout);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(5,5,5,5);
		
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		statsPanel.add(lblHeading, constraints);
		
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		statsPanel.add(lblMiddle, constraints);
		
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		statsPanel.add(lblRight, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		statsPanel.add(lblNumCalls, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		statsPanel.add(txtNumCalls, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		statsPanel.add(lblBusy, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		statsPanel.add(txtBusy, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		statsPanel.add(lblQueueLength, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		statsPanel.add(txtQueueLength, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		statsPanel.add(lblMaxWaitTimeOfCase, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		statsPanel.add(txtMaxWaitTimeOfCase, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		statsPanel.add(lblMaxQueueLength, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		statsPanel.add(txtMaxQueueLength, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 6;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		statsPanel.add(lblZeroDelays, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 6;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		statsPanel.add(txtZeroDelays, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 7;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		statsPanel.add(lblNumAccess, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 7;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		statsPanel.add(txtNumAccess, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 8;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		statsPanel.add(lblNumDeparture, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 8;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		statsPanel.add(txtNumDeparture, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 9;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		statsPanel.add(lblMaxNumCasesInParallel, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 9;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		statsPanel.add(txtMaxNumCasesInParallel, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 10;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		statsPanel.add(lblNumCasesInParallel, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 10;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		statsPanel.add(txtNumCasesInParallel, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 1;
		constraints.gridx = 0;
		constraints.gridy = 11;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		statsPanel.add(lblDummy, constraints);
		
		statsPanel.setPreferredSize(new Dimension(600, 350));
		graphPanel.setPreferredSize(new Dimension(600, 250));
		
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, statsPanel, graphPanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerSize(8);
		splitPane.setDividerLocation(350);
		splitPane.setResizeWeight(1);
		
		scrollPane = new JScrollPane(splitPane);
		scrollPane.setWheelScrollingEnabled(true);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
//		scrollPane.setVisible(true);
		
		add(scrollPane);*/
		
		setValues(0,0,0,0,0,0,0,0,0,0);
		
		/*setLayout(new GridLayout(2,1));
		add(lblHeading);
		add(txtCounters);*/
		
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(5,5,5,5);
		
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(lblHeading, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(txtCounters, constraints);
		
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(lblRight, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 1;
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(lblDummy, constraints);
	}

	/*public void setTxtBusy(String text) {
		this.txtBusy.setText(text);
	}

	public void setTxtMaxNumCasesInParallel(String text) {
		this.txtMaxNumCasesInParallel.setText(text);
	}

	public void setTxtMaxQueueLength(String text) {
		this.txtMaxQueueLength.setText(text);
	}

	public void setTxtMaxWaitTimeOfCase(String text) {
		this.txtMaxWaitTimeOfCase.setText(text);
	}

	public void setTxtNumAccess(String text) {
		this.txtNumAccess.setText(text);
	}

	public void setTxtNumCalls(String text) {
		this.txtNumCalls.setText(text);
	}

	public void setTxtNumCasesInParallel(String text) {
		this.txtNumCasesInParallel.setText(text);
	}

	public void setTxtNumDeparture(String text) {
		this.txtNumDeparture.setText(text);
	}

	public void setTxtQueueLength(String text) {
		this.txtQueueLength.setText(text);
	}

	public void setTxtZeroDelays(String text) {
		this.txtZeroDelays.setText(text);
	}*/
	
	public void setValues(
			int 	numCalls,
			double	busy,
			double	queueLength,
			double	maxWaitTimeOfCase,
			int		maxQueueLength,
			int		zeroDelays,
			int		numAccess,
			int		numDeparture,
			int		maxNumCasesInParallel,
			int		numCasesInParallel){
		
		String text = "";
		text += String.format("numCalls:\t\t%d\n", numCalls);
		text += String.format("busy:\t\t\t%7.2f\n", busy);
		text += String.format("queueLength:\t\t%7.2f\n", queueLength);
		text += String.format("maxWaitTimeOfCase:\t%7.2f\n", maxWaitTimeOfCase);
		text += String.format("maxQueueLength:\t\t%d\n", maxQueueLength);
		text += String.format("zeroDelays:\t\t%d\n", zeroDelays);
		text += String.format("numAccess:\t\t%d\n", numAccess);
		text += String.format("numDepature:\t\t%d\n", numDeparture);
		text += String.format("maxNumCasesInParallel:\t%d\n", maxNumCasesInParallel);
		text += String.format("numCasesInParallel:\t%d\n", numCasesInParallel);
		
		txtCounters.setText(text);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
