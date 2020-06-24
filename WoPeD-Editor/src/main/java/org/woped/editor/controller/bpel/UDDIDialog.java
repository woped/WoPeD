package org.woped.editor.controller.bpel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.woped.core.model.ModelElementContainer;
import org.woped.editor.controller.TransitionPropertyEditor;
import org.woped.gui.translations.Messages;
/**
 * 
 * @author Alexander Roßwog
 * 
 * This is the dialog which enables the user to choose a webservice and import
 * the according wsdl-url to the partner-link-site
 *
 */
@SuppressWarnings("serial")
public class UDDIDialog extends JDialog
{
	//initial UDDI-URL 
	String uddiUrl = "http://uddi.sap.com/uddi/api/inquiry/";
	//initial search-index
	String businessName = "a%";
	
	JDialog errorPopup 							= null;
	ModelElementContainer modelElementContainer = null;
	JTextField wsdlTextField					= null;
	
	JLabel	LuddiServer							= null;
	JComboBox CBuddiServer						= null;
	JButton BcreateUddi							= null;
	
	JLabel LBusiness							= null;
	JTextField TFBusiness						= null;
	JButton	BBusiness							= null;
	
	JLabel LfindBusiness						= null;
	JLabel LfindService							= null;
	JList LIfindBusiness						= null;
	JScrollPane SPfindBusiness					= null;
	JList LIfindService							= null;
	JScrollPane SPfindService					= null;
	JLabel Larc									= null;
	
	JButton	Bok									= null;
	JButton Bcancel								= null;
	
	
	/*
	 * constructor for the dialog
	 */
	public UDDIDialog(TransitionPropertyEditor t_editor, JTextField wsdlTextField, ModelElementContainer modelElementContainer)
	{		
		super(t_editor, true);
		this.wsdlTextField = wsdlTextField;
		this.modelElementContainer = modelElementContainer;
		
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
		
		String[] uddibuslist = modelElementContainer.getUddiVariableNameList();
		CBuddiServer.removeAllItems();
		for(int i=0; i<uddibuslist.length; i++)
		{
			CBuddiServer.addItem(uddibuslist[i]);
		}
		CBuddiServer.setSelectedItem("SAP");
		add(CBuddiServer);
		CBuddiServer.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent arg0) 
			{
				DefaultListModel model = new DefaultListModel();
				model.add(0, "");
				LIfindService.setModel(model);
				LIfindBusiness.setModel(model);
			}	
		});
		
		BcreateUddi = new JButton();
		BcreateUddi.setBounds(215,15,80,20);
		BcreateUddi.setText("Create");
		BcreateUddi.addActionListener(new CreateDialog(this, modelElementContainer));
		add(BcreateUddi);
		
		//Business-Panel
		LBusiness = new JLabel("Business:");
		LBusiness.setBounds(15,80,85,20);
		add(LBusiness);
		
		TFBusiness = new JTextField();
		TFBusiness.setText("a%");
		TFBusiness.setBounds(100,80,105,20);
		add(TFBusiness);
		
		BBusiness = new JButton();
		BBusiness.setBounds(215,80,80,20);
		BBusiness.setText("Find");
		add(BBusiness);
		
		
		//PChooseService-Panel
		LfindBusiness = new JLabel(Messages.getString("Transition.Properties.BPEL.UDDI.findBusiness"));
		LfindBusiness.setBounds(15,120,85,20);
		add(LfindBusiness);
		
		LfindService = new JLabel(Messages.getString("Transition.Properties.BPEL.UDDI.findService"));
		LfindService.setBounds(215,120,85,20);
		add(LfindService);
		
		LIfindBusiness = new JList(UDDI.find_business(uddiUrl, businessName));
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
		Bok.setText(Messages.getString("Transition.Properties.BPEL.Buttons.OK"));
		add(Bok);
		
		Bcancel = new JButton();
		Bcancel.setBounds(205,370,80,25);
		Bcancel.setText(Messages.getString("Transition.Properties.BPEL.Buttons.Cancel"));
		add(Bcancel);
		
		Bok.addActionListener(new SetUrl(this));
		
		Bcancel.addActionListener(new WindowCloser(this));
				
		BBusiness.addActionListener(new FindBusiness(this));
		LIfindBusiness.addListSelectionListener(new FindService(this));
		
		setVisible(true);
	}
	
	/*
	 * standard-method to show error-popups
	 */
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
/*
 * Listener
 */

class WindowCloser implements ActionListener
{
	private JDialog d;
	
	public WindowCloser(JDialog d)
	{
		super();
		this.d = d;
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		d.dispose();				
	}	
}

class SetUrl implements ActionListener
{
	UDDIDialog adaptee;
	
	public SetUrl(UDDIDialog adaptee)
	{
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) 
	{
		adaptee.showErrorPopup(Messages.getString("Transition.Properties.BPEL.Error"), Messages.getString("Transition.Properties.BPEL.UDDI.NoWSDL"));
		//adaptee.dispose();
	}
}

class CreateDialog implements ActionListener
{
	UDDIDialog adaptee								= null;
	ModelElementContainer modelElementContainer		= null;
	
	
	public CreateDialog(UDDIDialog adaptee, ModelElementContainer modelElementContainer)
	{
		this.adaptee = adaptee;
		this.modelElementContainer = modelElementContainer;
	}
	
	public void actionPerformed(ActionEvent arg0) 
	{
		NewUddiVariableDialog window = new NewUddiVariableDialog(adaptee);
		if(NewUddiVariableDialog._OKBUTTON == window.getActivButton())
		{
			modelElementContainer.addUddiVariable(window.getVariableName(), window.getVariableURL());
			String[] uddibuslist = modelElementContainer.getUddiVariableNameList();
			adaptee.CBuddiServer.removeAllItems();
			for(int i=0; i<uddibuslist.length; i++)
			{
				adaptee.CBuddiServer.addItem(uddibuslist[i]);
			}
			adaptee.CBuddiServer.setSelectedItem(window.getVariableName());
		}
	}
}

class FindBusiness implements ActionListener
{
	private UDDIDialog adaptee;
	
	public FindBusiness(UDDIDialog adaptee)
	{
		this.adaptee = adaptee;
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		DefaultListModel model  = null;
		DefaultListModel model2 = null;
		
		String busname = adaptee.TFBusiness.getText();
		if(busname.equals("") || busname == null)
		{
			adaptee.showErrorPopup(Messages.getString("Transition.Properties.BPEL.Error"), Messages.getString("Transition.Properties.BPEL.UDDI.WrongValueError"));
		}
		else
		{
			//get UDDI-Server-URL
			String UDDIServerName = (String) adaptee.CBuddiServer.getSelectedItem();
			String UDDIServerURL = adaptee.modelElementContainer.findUddiVariableByName(UDDIServerName).getURL();
			
			String[] buslist = UDDI.find_business(UDDIServerURL, busname);
			if(buslist == null || buslist.length == 0)
			{
				adaptee.showErrorPopup(Messages.getString("Transition.Properties.BPEL.Error"), Messages.getString("Transition.Properties.BPEL.UDDI.NoResult"));
			}
			else
			{
				if(buslist.length >= 40)
				{
					adaptee.showErrorPopup(Messages.getString("Transition.Properties.BPEL.UDDI.ResultWarning"), Messages.getString("Transition.Properties.BPEL.UDDI.fortyResultWarning"));
				}
				model = new DefaultListModel();
			    for (int i=0; i<buslist.length; i++) {
			        model.add(i, buslist[i]);
			    }
			    adaptee.LIfindBusiness.setModel(model);
				model2 = new DefaultListModel();
				model2.add(0, "-none-");
				adaptee.LIfindService.setModel(model2);
			}
		}
	}	
}

class FindService implements ListSelectionListener
{
	private String chosen = "";
	private String business = "none";
	
	private UDDIDialog adaptee = null;
	
	public FindService(UDDIDialog adaptee)
	{
		this.adaptee = adaptee;
	}
	
	public void valueChanged(ListSelectionEvent e)
	{
		business = (String) adaptee.LIfindBusiness.getSelectedValue();
		if (!adaptee.LIfindBusiness.isSelectionEmpty() && !chosen.equalsIgnoreCase(business))
		{
			chosen = business;
			
			//get UDDI-Server-URL
			String UDDIServerName = (String) adaptee.CBuddiServer.getSelectedItem();
			String UDDIServerURL = adaptee.modelElementContainer.findUddiVariableByName(UDDIServerName).getURL();
			
			String[] blist = UDDI.find_services(UDDI.getBusinessInfoFromName(UDDIServerURL, business));
			if(blist.length > 0)
			{
				DefaultListModel model = new DefaultListModel();
			    for (int i=0; i<blist.length; i++) {
			        model.add(i, blist[i]);
			    }
			    adaptee.LIfindService.setModel(model);
			}
			else
			{
				DefaultListModel model = new DefaultListModel();
				model.add(0, "-none-");
				adaptee.LIfindService.setModel(model);
			}
		}
	}
}
