package org.woped.quantana.simulation.output;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.woped.quantana.gui.QuantitativeSimulationDialog;
import org.woped.quantana.resourcealloc.Resource;
import org.woped.quantana.resourcealloc.ResourceUtilization;
import org.woped.quantana.simulation.Simulator;

public class ProcessPanel extends JPanel {
	
	private static final long serialVersionUID = 4L;
	
	private JLabel lblHeading = new JLabel("Resource Utilization: ");
	private JTextArea txtResources = new JTextArea();
	private JLabel lblClock = new JLabel("Time Simulation Stopped: ");
	private JTextField txtClock = new JTextField();
	private JLabel lblCasesService = new JLabel("Avg. Processing Time: ");
	private JTextField txtCasesService = new JTextField();
	private JLabel lblCasesWait = new JLabel("Avg. Waiting Time: ");
	private JTextField txtCasesWait = new JTextField();
	private JLabel lblThroughPut = new JLabel("Throughput: ");
	private JTextField txtThroughPut = new JTextField();
	private JLabel lblAvgCases = new JLabel("Avg. # Cases in System: ");
	private JTextField txtAvgCases = new JTextField();
	
	private QuantitativeSimulationDialog sod;
	private ResourceUtilization util;
	
	public ProcessPanel(QuantitativeSimulationDialog sod){
		this.setPreferredSize(new Dimension(500,400));
		this.sod = sod;
		this.util = sod.getSimulator().getResUtil();
		
		init();
	}
	
	private void init(){
		JLabel lblDummy = new JLabel();
		JLabel lblRight = new JLabel();
		
		txtResources.setPreferredSize(new Dimension(200, 140));
		txtResources.setBorder(BorderFactory.createEmptyBorder());
		txtResources.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
		txtResources.setEditable(false);
		
		txtClock.setPreferredSize(new Dimension(100, 20));
		txtClock.setEditable(false);
		
		txtThroughPut.setPreferredSize(new Dimension(100, 20));
		txtThroughPut.setEditable(false);
		
		txtCasesService.setPreferredSize(new Dimension(100, 20));
		txtCasesService.setEditable(false);
		
		txtCasesWait.setPreferredSize(new Dimension(100, 20));
		txtCasesWait.setEditable(false);
		
		txtAvgCases.setPreferredSize(new Dimension(100, 20));
		txtAvgCases.setEditable(false);
		
		setUtilValues();
		
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(5,5,5,5);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(lblClock, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(txtClock, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(lblCasesService, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(txtCasesService, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(lblCasesWait, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(txtCasesWait, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(lblThroughPut, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(txtThroughPut, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(lblAvgCases, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(txtAvgCases, constraints);
		
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(lblHeading, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 6;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		add(new JScrollPane(txtResources), constraints);
		
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.gridx = 2;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(lblRight, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 1;
		constraints.gridx = 0;
		constraints.gridy = 7;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(lblDummy, constraints);
	}
	
	private void setUtilValues(){
		HashMap<String, Resource> res = util.getResAlloc().getResources();
		String text = "";
		Simulator sim = sod.getSimulator();
		double curTime = sim.getClock();
		int caseCnt = sim.getCaseCount();
		
		for (Resource r : res.values()){
			text += r.getName() + ": " + String.format("%7.2f", r.getBusyTime() / curTime * 100) + " %\n";
		}
		
		txtResources.setText(text);
		
		txtClock.setText(String.format("%7.2f", curTime));
		
		txtThroughPut.setText(String.format("%7.2f", sim.getThroughPut() / caseCnt));
		txtCasesService.setText(String.format("%7.2f", sim.getCaseBusy() / caseCnt));
		txtCasesWait.setText(String.format("%7.2f", sim.getCaseWait() / caseCnt));
		
		txtAvgCases.setText(String.format("%7.2f", sim.getAvgCasesInSystem() / curTime));
	}
}
