package org.woped.file.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.woped.applet.Constants;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.action.DisposeWindowAction;
import org.woped.editor.controller.vc.ConfigVC;
import org.woped.server.ServerLoader;
import org.woped.server.holder.ModellHolder;
import org.woped.server.holder.UserHolder;
import org.woped.translations.Messages;

import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;

public class OpenWebEditorUI extends JDialog {

	private JPanel  buttonPanel = null;
	private JButton b_OK = null;
	private JButton b_Cancel = null;
	// Filter Buttons
	private JPanel filterpanel = null;
	private JComboBox comboBox = null;
	
	
	private JPanel listpanel = null;
	private JList l_models = null;
	
	private DefaultListModel model = null;
	
	private ModellHolder selected = null;
	
	public OpenWebEditorUI() {
		this(null);
	}
	
	public OpenWebEditorUI(Frame owner) {
		super(owner,true);
		initialization();
	}

	private void initialization() {
		setVisible(false);
		getContentPane().setLayout(new BorderLayout());
		setUndecorated(false);
		setResizable(false);
		getContentPane().add(getButtonBar(),BorderLayout.SOUTH);
		getContentPane().add(getListBar(),BorderLayout.NORTH);
		setTitle(Messages.getTitle("OpenWebServiceEditor"));
		pack();
		
		if (getOwner() != null)
        {
            this.setLocation(getOwner().getX() + ((getOwner().getWidth() - this.getWidth()) / 2), getOwner().getY() + ((getOwner().getHeight() - this.getHeight()) / 2));
        } else
        {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            this.setLocation((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2);
        }
		
		this.setSize(this.getWidth(), this.getHeight());
		
		comboBox.setSelectedIndex(0);
		
	}
	
	private JPanel getFilterBar() {
		
		if (filterpanel == null) {
			
			filterpanel = new JPanel(new FlowLayout());
			
			Object[] values = new Object[3];
			values[0] = Messages.getTitle("OpenWebServiceEditor.Owns");
			values[1] = Messages.getTitle("OpenWebServiceEditor.Shared");
			values[2] = Messages.getTitle("OpenWebServiceEditor.Samples");
		
			comboBox = new JComboBox(values);
			comboBox.addActionListener(new AbstractAction() {

				public void actionPerformed(ActionEvent e) {
					performSelectChange();					
				}
				
			});
			
			filterpanel.add(comboBox);		
				
		}
		
		
		return filterpanel;
	}

	private JPanel getListBar() {
		
		if (listpanel == null) {
			
		listpanel = new JPanel(new BorderLayout());
		listpanel.setBorder(BorderFactory.createEmptyBorder());
		
		model = new DefaultListModel();
		l_models = new JList(model);
//		l_models.addListSelectionListener(new ListSelectionListener() {
//
//			public void valueChanged(ListSelectionEvent e) {
//				if (e.getValueIsAdjusting() == false) {
//					if (l_models.getSelectedIndex() != -1) {
//						selected
//					}
//				}
//				
//			}
//			
//		})
		
		
		JScrollPane scrollPane = new JScrollPane(l_models);
		
		
		
		listpanel.add(scrollPane,BorderLayout.NORTH);
		listpanel.add(getFilterBar(),BorderLayout.SOUTH);
		
		}
		return listpanel;
	}
	
	
	@SuppressWarnings("serial")
	private JPanel getButtonBar() {
		
		if (buttonPanel == null)
        {
         
			buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(1, 2));
            JPanel innerPanelLeft = new JPanel();
            innerPanelLeft.setLayout(new FlowLayout(FlowLayout.LEFT));

            JPanel innerPanelRight = new JPanel();
            innerPanelRight.setLayout(new FlowLayout(FlowLayout.RIGHT));
            innerPanelRight.add(getOkButton());
            innerPanelRight.add(getCancelButton());
            
            buttonPanel.add(innerPanelLeft);
            buttonPanel.add(innerPanelRight);
        }
        return buttonPanel;

	}
	
	
	private JButton getOkButton()
    {
        if (b_OK == null)
        {
            b_OK = new JButton();
            b_OK.setText(Messages.getTitle("Button.Ok"));
            b_OK.setIcon(Messages.getImageIcon("Button.Ok"));
            b_OK.setMnemonic(Messages.getMnemonic("Button.Ok"));
            b_OK.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent arg0)
                {
                    if (l_models.getSelectedIndex() != -1) {
                    	selected = (ModellHolder)l_models.getSelectedValue();
                    	setVisible(false);
                    }                	
                }
            });
        }
        return b_OK;
    }
	
	@SuppressWarnings("serial")
	private JButton getCancelButton()
    {
        if (b_Cancel == null)
        {
        	b_Cancel = new JButton();
        	b_Cancel.setText(Messages.getTitle("Button.Cancel"));
        	b_Cancel.setIcon(Messages.getImageIcon("Button.Cancel"));
        	b_Cancel.setMnemonic(Messages.getMnemonic("Button.Cancel"));
        	b_Cancel.addActionListener(new AbstractAction()
            {
                public void actionPerformed(ActionEvent arg0)
                {
                    selected = null;
                	setVisible(false);
                }
            });
        }
        return b_Cancel;
    }
	
	

	private void performSelectChange() {
		try {
			ArrayList<ModellHolder> values = null;
			model.removeAllElements();
			switch (comboBox.getSelectedIndex()) {
			case 0:
				// Own Petri Net Models
				values = ServerLoader.getInstance().getList(UserHolder.getUserID(),false);
				break;
			case 1:
				// Shared Petri Net Models
				values = ServerLoader.getInstance().getList(UserHolder.getUserID(),true);
				break;
			case 2:
				// Sample Petri Net Models
				values = ServerLoader.getInstance().getList(UserHolder.getSampleID(),false);
				break;
			default:
				values = null;
				break;
			}			
			if (values != null) {
				for (int i = 0; i < values.size(); i++) {
					model.addElement(values.get(i));
				}
			}
		} catch (RemoteException re) {
			LoggerManager.error(Constants.APPLET_LOGGER,"Error while listing Petri Net Models from Web " + re.getMessage());
		}
	}

	public ModellHolder execute() {
		setVisible(true);
		// wait for reply
		while (isVisible()) {}
		return selected;
	}

	
	
	
	
	
	
}
