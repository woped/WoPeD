package org.woped.bpel.gui.transitionproperties;

import javax.swing.*;

import org.woped.editor.controller.TransitionPropertyEditor;

import java.awt.*;
/**
 * 
 * @author Alexander Rosswog
 *
 */

public class UDDIDialog extends JDialog
{
	String uddiUrl = "http://udditest.sap.com/uddi/api/inquiry/";
	String businessName = "s%";
	
	TransitionPropertyEditor t_editor;
	
	JPanel PChooseUddi			= null;
	JLabel	LuddiServer			= null;
	JComboBox CBuddiServer		= null;
	JButton BcreateUddi			= null;
	
	JPanel PChooseService		= null;
	JLabel LfindBusiness		= null;
	JLabel LfindService			= null;
	JList LIfindBusiness		= null;
	JScrollPane SPfindBusiness	= null;
	JList LIfindService			= null;
	JScrollPane SPfindService	= null;
	JLabel Larc					= null;
	
	JPanel PReady				= null;
	JButton	Bok					= null;
	JButton Bcancel				= null;
	
	public UDDIDialog(TransitionPropertyEditor t_editor)
	{
		super(t_editor, true);
		
		setTitle("UDDI");
		setSize(600,300);
		setLocation(300,200);
		setLayout(new GridBagLayout());
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 1;
		c.weighty = 1;
		
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0,0,0,0);
		PChooseUddi = new JPanel();
		add(PChooseUddi, c);
		PChooseUddi.setLayout(new GridBagLayout());
		
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(0,0,0,0);		
		PChooseService = new JPanel();
		add(PChooseService,c);
		PChooseService.setLayout(new GridBagLayout());
		
		/*c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(0,0,0,0);		
		PReady = new JPanel();
		add(PReady,c);
		PReady.setLayout(new GridBagLayout());*/
		
		//PChooseUddi-Panel
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 1;
		c.weighty = 1;
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0,5,0,0);
		LuddiServer = new JLabel("UDDI-Server:");
		PChooseUddi.add(LuddiServer, c);
		
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0,5,0,0);
		CBuddiServer = new JComboBox();
		CBuddiServer.addItem("SAP");
		CBuddiServer.addItem("Microsoft");
		PChooseUddi.add(CBuddiServer, c);
		
		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0,5,0,0);
		BcreateUddi = new JButton();
		BcreateUddi.setText("Create");
		PChooseUddi.add(BcreateUddi,c);
				
		
		//PChooseService-Panel
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0,5,0,0);
		LfindBusiness = new JLabel("find Business:");
		PChooseService.add(LfindBusiness,c);
		
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0,5,0,0);
		LfindService = new JLabel("find Service:");
		PChooseService.add(LfindService,c);
		
		c.gridx = 0;
		c.gridy = 2;
		c.gridheight = 4;
		c.gridwidth = 1;
		c.insets = new Insets(5,5,0,0);
		LIfindBusiness = new JList(org.woped.bpel.uddi.UDDI.find_business(uddiUrl, businessName));
		SPfindBusiness = new JScrollPane(LIfindBusiness);
		PChooseService.add(SPfindBusiness,c);
		
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(5,5,0,0);
		Larc = new JLabel(">>");
		PChooseService.add(Larc,c);
		
		c.gridx = 2;
		c.gridy = 2;
		c.gridheight = 4;
		c.gridwidth = 1;
		c.insets = new Insets(5,5,0,0);
		LIfindService = new JList(org.woped.bpel.uddi.UDDI.find_business(uddiUrl, businessName));
		SPfindService = new JScrollPane(LIfindService);
		PChooseService.add(SPfindService,c);
		
		/*//PReady-Panel
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 1;
		c.weighty = 1;
		
		c.gridx = 0;
		c.insets = new Insets(0,0,0,0);
		Bok = new JButton("OK");
		PChooseUddi.add(Bok, c);
		
		c.gridx = 0;
		c.insets = new Insets(0,0,0,0);
		Bcancel = new JButton("Cancel");
		PChooseUddi.add(Bcancel, c);*/
		
		setVisible(true);
	}
}
