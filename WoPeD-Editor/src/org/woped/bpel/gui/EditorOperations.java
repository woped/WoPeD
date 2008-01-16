package org.woped.bpel.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionListener;

import org.woped.translations.Messages;
import org.woped.core.utilities.SwingUtils;
import org.woped.core.controller.IEditor;

/**
 * @author Lavinia Posler
 * 
 * This is the class for maintaining operations which are needed for BPEL Export.
 *
 * Created on 15.01.2008
 */

public class EditorOperations extends JPanel {
	
	private JPanel                 operationBpelPanel                   = null;
	
	private JPanel                 operationBpelPreviewPanel            = null;
	private JLabel                 operationBpelPreviewLabel            = null;
	private JButton                operationBpelPreviewButton           = null;
	
	//private GridBagConstraints 		c_c = null;
	//private JPanel                 operationBpelTextPanel               = null;
	//private JTextField             operationBpelTextField       	    = null;
	
	private IEditor               editor;
    
	public IEditor getEditor()
    {
        return editor;
    }
    

    public EditorOperations(IEditor editor)
    {
        this.editor = editor;
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = 0;
        add(getOperationBpelPreviewPanel(), c);
        this.setVisible(true);
        
    }

    
   /* private JPanel getOperationBpelPanel()
    {
        if (operationBpelPanel == null)
        {
        	operationBpelPanel = new JPanel();
        	operationBpelPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            c.weightx = 1;
            c.weighty = 1;
            c.gridx = 0;
            c.gridy = 0;
            c.fill = GridBagConstraints.BOTH;
            c.insets = new Insets(0, 0, 0, 0);
            operationBpelPanel.add(getOperationBpelPreviewPanel(), c);
            /*
            c.gridx = 1;
            c.gridy = 0;
            c.fill = GridBagConstraints.BOTH;
            c.insets = new Insets(0, 0, 0, 0);
            operationBpelPanel.add(getOperationBpelTextPanel, c);
            */
   /*     }

        return operationBpelPanel;
    }
   */
    private JPanel getOperationBpelPreviewPanel()
    {
    	System.out.println("gen bpel-prev-pane");
        if (operationBpelPreviewPanel == null)
        {
        	operationBpelPreviewPanel = new JPanel();
        	operationBpelPreviewPanel.setBorder(BorderFactory
                    .createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("BPEL Preview ?")), BorderFactory.createEmptyBorder()));
            operationBpelPreviewPanel.setLayout(new GridBagLayout());
        	GridBagConstraints c = new GridBagConstraints();

            c.weightx = 1;
            c.weighty = 1;
            c.gridx = 0;
            c.gridy = 0;
            c.fill = GridBagConstraints.BOTH;
            c.insets = new Insets(0, 0, 0, 0);
            operationBpelPreviewPanel.add(getOperationBpelPreviewLabel(), c);
            
            c.gridx = 1;
            c.gridy = 0;
            c.fill = GridBagConstraints.BOTH;
            c.insets = new Insets(0, 0, 0, 0);
            operationBpelPreviewPanel.add(getOperationBpelPreviewButton(), c);
            
        }
        this.operationBpelPreviewPanel.setVisible(true);

        return operationBpelPreviewPanel;
    }
    
    private JLabel getOperationBpelPreviewLabel()
    {
        if (operationBpelPreviewLabel == null)
        {
        	operationBpelPreviewLabel = new JLabel("Test");
        }
        
        return operationBpelPreviewLabel;
    }
	
    private JButton getOperationBpelPreviewButton()
    {
        if (operationBpelPreviewButton == null)
        {
        	operationBpelPreviewButton = new JButton();
        	operationBpelPreviewButton.setText("Preview BPEL");
        	operationBpelPreviewButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                	
                	
                	/*
                	c_c.gridx = 1;
                    c_c.gridy = 0;
                    c_c.fill = GridBagConstraints.BOTH;
                    c_c.insets = new Insets(0, 10, 0, 0);
                    operationBpelPanel.add(getOperationBpelTextPanel(), c_c);
                    */
                }
            });
        }
        
        return operationBpelPreviewButton;
    }
    
    
    /*
    private JPanel getOperationBpelTextPanel()
    {
        if (operationBpelTextPanel == null)
        {
        	operationBpelTextPanel = new JPanel();
        	operationBpelTextPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            c.weightx = 1;
            c.weighty = 1;
            c.gridx = 0;
            c.gridy = 0;
            c.fill = GridBagConstraints.BOTH;
            c.insets = new Insets(0, 0, 0, 0);
            operationBpelPreviewPanel.add(getOperationBpelTextField(), c);
        }

        return operationBpelTextPanel;
    }
    
    private JTextField getOperationBpelTextField()
    {
        if (operationBpelTextField == null)
        {
        	operationBpelTextField = new JTextField();
            SwingUtils.setFixedWidth(operationBpelTextField, 400);
            operationBpelTextField.setText("test...lavi");
            operationBpelTextField.setEditable(false);
        }
            return operationBpelTextField;
        }
        */
}
