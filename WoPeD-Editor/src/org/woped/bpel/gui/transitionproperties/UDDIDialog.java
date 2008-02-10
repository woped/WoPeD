package org.woped.bpel.gui.transitionproperties;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.woped.editor.controller.TransitionPropertyEditor;
import org.woped.translations.Messages;

import java.awt.*;
import java.awt.event.*;
/**
 * 
 * @author Alexander Rosswog
 *
 */
@SuppressWarnings("serial")
public class UDDIDialog extends JDialog
{
	String uddiUrl = "http://udditest.sap.com/uddi/api/inquiry/";
	String businessName = "s%";
	
	JDialog errorPopup 			= null;
	
	JLabel	LuddiServer			= null;
	JComboBox CBuddiServer		= null;
	JButton BcreateUddi			= null;
	
	JLabel LBusiness			= null;
	JTextField TFBusiness		= null;
	JButton	BBusiness			= null;
	
	JLabel LfindBusiness		= null;
	JLabel LfindService			= null;
	JList LIfindBusiness		= null;
	JScrollPane SPfindBusiness	= null;
	JList LIfindService			= null;
	JScrollPane SPfindService	= null;
	JLabel Larc					= null;
	
	JButton	Bok					= null;
	JButton Bcancel				= null;
	
	public UDDIDialog(TransitionPropertyEditor t_editor)
	{		
		super(t_editor, true);
		
		setTitle("UDDI");
		setLayout(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(407,450);
		setLocation(300,200);
				
		LuddiServer = new JLabel("UDDI-Server:");
		LuddiServer.setBounds(15,15,75,20);
		add(LuddiServer);
		
		CBuddiServer = new JComboBox();
		CBuddiServer.setBounds(100,15,105,20);
		CBuddiServer.addItem("SAP");
		CBuddiServer.addItem("Microsoft");
		add(CBuddiServer);
		
		BcreateUddi = new JButton();
		BcreateUddi.setBounds(215,15,80,20);
		BcreateUddi.setText("Create");
		add(BcreateUddi);
		
		//Business-Panel
		LBusiness = new JLabel("Business:");
		LBusiness.setBounds(15,80,85,20);
		add(LBusiness);
		
		TFBusiness = new JTextField();
		TFBusiness.setText("s%");
		TFBusiness.setBounds(100,80,105,20);
		add(TFBusiness);
		
		BBusiness = new JButton();
		BBusiness.setBounds(215,80,80,20);
		BBusiness.setText("Find");
		add(BBusiness);
		
		
		//PChooseService-Panel
		LfindBusiness = new JLabel("find Business:");
		LfindBusiness.setBounds(15,120,85,20);
		add(LfindBusiness);
		
		LfindService = new JLabel("find Service:");
		LfindService.setBounds(215,120,85,20);
		add(LfindService);
		
		LIfindBusiness = new JList(org.woped.bpel.uddi.UDDI.find_business(uddiUrl, businessName));
		SPfindBusiness = new JScrollPane(LIfindBusiness);
		SPfindBusiness.setBounds(15,145,170,190);
		add(SPfindBusiness);
		
		Larc = new JLabel(">>");
		Larc.setBounds(194,232,20,20);
		add(Larc);
		
		String[] list = {"-none-"};
		LIfindService = new JList(list);
		SPfindService = new JScrollPane(LIfindService);
		SPfindService.setBounds(215,145,170,190);
		add(SPfindService);
		
		
		//PReady-Panel
		
		Bok = new JButton();
		Bok.setBounds(115,370,80,25);
		Bok.setText("OK");
		add(Bok);
		
		Bcancel = new JButton();
		Bcancel.setBounds(205,370,80,25);
		Bcancel.setText("Cancel");
		add(Bcancel);
		
		//implements Listener
		BBusiness.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				String busname = TFBusiness.getText();
				if(busname.equals("") || busname == null)
				{
					showErrorPopup("ERROR", "Unallowed value inserted");
				}
				else
				{
					String[] buslist = org.woped.bpel.uddi.UDDI.find_business(uddiUrl, busname);
					if(buslist.length > 0)
					{
						DefaultListModel model = new DefaultListModel();
					    for (int i=0; i<buslist.length; i++) {
					        model.add(i, buslist[i]);
					    }
						LIfindBusiness.setModel(model);
						DefaultListModel model2 = new DefaultListModel();
						model2.add(0, "-none-");
						LIfindService.setModel(model2);
					}
				}
			}		
		});
		
		
		LIfindBusiness.addListSelectionListener(new ListSelectionListener()
		{
			String chosen = "";
			String business = "none";
			
			public void valueChanged(ListSelectionEvent e)
			{
				business = (String) LIfindBusiness.getSelectedValue();
				if (!LIfindBusiness.isSelectionEmpty() && !chosen.equalsIgnoreCase(business))
				{
					chosen = business;
					//System.out.println(business);
					String[] blist = org.woped.bpel.uddi.UDDI.find_services(org.woped.bpel.uddi.UDDI.getBusinessInfoFromName(uddiUrl, business));
					if(blist.length > 0)
					{
						DefaultListModel model = new DefaultListModel();
					    for (int i=0; i<blist.length; i++) {
					        model.add(i, blist[i]);
					    }
						LIfindService.setModel(model);
					}
					else
					{
						DefaultListModel model = new DefaultListModel();
						model.add(0, "-none-");
						LIfindService.setModel(model);
					}
				}
			}	
		});
		setVisible(true);
	}
	
	protected void showErrorPopup(String title, String message) {
		errorPopup = new JDialog(this, true);
		errorPopup.setVisible(false);
		errorPopup.setTitle(title);
		errorPopup.setSize(300, 140);
		errorPopup.setLocation(this.getLocation().x + 90, this
				.getLocation().y + 50);
		errorPopup.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.CENTER;
		c.weightx = 1;
		c.weighty = 1;

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(10, 10, 0, 10);
		errorPopup.add(new JLabel(message), c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		c.fill = GridBagConstraints.CENTER;

		JButton okButton = new JButton(Messages
				.getString("Transition.Properties.BPEL.Buttons.OK"));
		okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				errorPopup.dispose();
			}

		});

		errorPopup.add(okButton, c);
		errorPopup.setVisible(true);
	}
}
