package org.woped.quantana.simulation.output;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("unused")
public class ProtocolPanel extends JPanel {
	
	private static final long serialVersionUID = 3L;
	
	private SimOutputDialog sod;
	
	private JLabel lblHeading = new JLabel("Protocol: ");
	private JTextArea txtProtocol = new JTextArea();
	private JScrollPane scrollPane;
	
	public ProtocolPanel(SimOutputDialog sod){
		this.setPreferredSize(new Dimension(500,500));
		this.sod = sod;
		
		init();
	}
	
	private void init(){
		txtProtocol.setPreferredSize(new Dimension(460, 400));
		txtProtocol.setEditable(false);
		txtProtocol.setText(sod.getProtocol());
		
		scrollPane = new JScrollPane(txtProtocol);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
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
		add(lblHeading, constraints);
		
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(scrollPane, constraints);
	}
}
