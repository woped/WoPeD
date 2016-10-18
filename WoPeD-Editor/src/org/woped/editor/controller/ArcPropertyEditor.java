package org.woped.editor.controller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.woped.core.model.ArcModel;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.gui.lookAndFeel.WopedButton;
import org.woped.gui.translations.Messages;

@SuppressWarnings("serial")
public class ArcPropertyEditor extends JDialog {

    // General
    private ArcModel    arc            = null;
    private EditorVC    editor         = null;
    private JPanel      contentPanel   = null;

    // Name
    private JPanel        namePanel        = null;
    private JLabel        nameLabel        = null;
    private JTextField    valueTextField    = null;
    private JLabel        idLabel          = null;
    private JTextField    idTextField      = null;

    // Display
    private JPanel        displayPanel     = null;
    private JCheckBox	  displayToggle     = null;

    // Buttons
    private JPanel      buttonPanel      = null;
    private WopedButton     buttonOk         = null;
    private WopedButton     buttonCancel     = null;
    
    private int oldValue;
    private boolean oldDisplayOn;

    public ArcPropertyEditor(Frame owner, Point position, ArcModel arc, EditorVC editor)
    {
        super(owner, true);
        this.arc = arc;
        this.editor = editor;
        this.setVisible(false);
        initialize();
        this.setSize(350, 190);
		this.setLocation(new Point(position.x + 50, position.y));
        this.setVisible(true);
    }

    private void initialize()
    {
        this.setTitle(Messages.getString("Arc.Properties"));
        this.getContentPane().add(getContentPanel(), BorderLayout.NORTH);
        this.getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);
        getValueTextField().requestFocus();
        
        oldValue = Integer.valueOf(valueTextField.getText());
        oldDisplayOn = displayToggle.isSelected();
    }

    private JPanel getContentPanel()
    {
        if (contentPanel == null)
        {
            contentPanel = new JPanel();
            contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            contentPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1;
            c.weighty = 1;

            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(0, 0, 0, 0);
            contentPanel.add(getNamePanel(), c);

            c.gridx = 0;
            c.gridy = 1;
            c.insets = new Insets(0, 0, 0, 0);
            contentPanel.add(getDisplayPanel(), c);
        }

        return contentPanel;
    }

    // **************************NamePanel******************************
    private JPanel getNamePanel()
    {
        if (namePanel == null)
        {
            namePanel = new JPanel();
            namePanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            namePanel.setBorder(BorderFactory
                    .createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("Arc.Properties.Probability")), BorderFactory.createEmptyBorder(5, 5, 0, 5)));

            c.weightx = 1;
            c.weighty = 1;
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.HORIZONTAL;

            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 1;
            c.insets = new Insets(0, 0, 0, 0);
            namePanel.add(getNameLabel(), c);

            c.gridx = 1;
            c.gridy = 0;
            c.gridwidth = 2;
            c.insets = new Insets(0, 10, 0, 10);
            namePanel.add(getValueTextField(), c);

            c.gridx = 3;
            c.gridy = 0;
            c.gridwidth = 1;
            c.insets = new Insets(0, 10, 0, 0);
            namePanel.add(getIdLabel(), c);

            c.gridx = 4;
            c.gridy = 0;
            c.gridwidth = 1;
            c.insets = new Insets(0, 10, 0, 0);
            namePanel.add(getIdTextField(), c);
        }

        return namePanel;
    }

    private JLabel getNameLabel()
    {
        if (nameLabel == null)
        {
            nameLabel = new JLabel(Messages.getString("Arc.Properties.Name") + ":");
        }

        return nameLabel;
    }

    private JTextField getValueTextField()
    {
        if (valueTextField == null)
        {
            valueTextField = new JTextField(Integer.toString((Double.valueOf(arc.getProbability() * 100)).intValue()));
            valueTextField.setPreferredSize(new Dimension(80, 20));
            valueTextField.setMinimumSize(new Dimension(80, 20));
            valueTextField.setMaximumSize(new Dimension(80, 20));
            valueTextField.addKeyListener(new KeyListener()
            {
                public void keyPressed(KeyEvent e)
                {
                    keyReleased(e);
                }

                public void keyTyped(KeyEvent e)
                {
                    keyReleased(e);
                }

                public void keyReleased(KeyEvent e)
                {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    {
                        apply();
                        ArcPropertyEditor.this.dispose();
                    }
                }
            });
        }

        return valueTextField;
    }

    private JLabel getIdLabel()
    {
        if (idLabel == null)
        {
            idLabel = new JLabel("ID#: ");
        }

        return idLabel;
    }

    private JTextField getIdTextField()
    {
        if (idTextField == null)
        {
            idTextField = new JTextField();
            idTextField.setPreferredSize(new Dimension(80, 20));
            idTextField.setMinimumSize(new Dimension(80, 20));
            idTextField.setMaximumSize(new Dimension(80, 20));
            idTextField.setText("" + arc.getId());
            idTextField.setEditable(false);
         }

        return idTextField;
    }

    // ******************************Display Panel*****************************************
    private JPanel getDisplayPanel()
    {
        if (displayPanel == null)
        {
            displayPanel = new JPanel();
            displayPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            displayPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("Arc.Properties.Display")), BorderFactory.createEmptyBorder(5, 5, 0, 5)));

            c.weightx = 1;
            c.weighty = 1;
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.HORIZONTAL;

            c.gridx = 2;
            c.gridy = 0;
            c.gridwidth = 2;
            c.insets = new Insets(0, 0, 0, 0);
            displayPanel.add(getCheckBox(), c);
        }

        return displayPanel;
    }

    private JCheckBox getCheckBox()
    {
        if (displayToggle == null)
        {
            displayToggle = new JCheckBox(Messages.getString("Arc.Properties.Display.Check"), arc.isDisplayOn());
        }

        return displayToggle;
    }

    // *****************************************************ButtonPanel****************************************************
    private JPanel getButtonPanel()
    {
        if (buttonPanel == null)
        {
            buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(getButtonOk());
            buttonPanel.add(getButtonCancel());
            //            buttonPanel.add(getButtonApply());
        }
        return buttonPanel;
    }

    private JButton getButtonOk()
    {
        if (buttonOk == null)
        {
            buttonOk = new WopedButton();
            buttonOk.setIcon(Messages.getImageIcon("Button.Ok"));
            buttonOk.setText(Messages.getString("Button.Ok.Title"));
           

            buttonOk.setMnemonic(KeyEvent.VK_O);
            buttonOk.setPreferredSize(new Dimension(100, 25));
            buttonOk.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    apply();
                    ArcPropertyEditor.this.dispose();
                }
            });

        }
        return buttonOk;
    }

    private JButton getButtonCancel()
    {
        if (buttonCancel == null)
        {
            buttonCancel = new WopedButton();
            buttonCancel.setText(Messages.getString("Button.Cancel.Title"));
            buttonCancel.setIcon(Messages.getImageIcon("Button.Cancel"));
            buttonCancel.setMnemonic(KeyEvent.VK_C);
            buttonCancel.setPreferredSize(new Dimension(120, 25));
            buttonCancel.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    ArcPropertyEditor.this.dispose();
                }
            });
        }

        return buttonCancel;
    }

    private void apply()
    {
    	if (oldValue != Integer.valueOf(valueTextField.getText()))
    		arc.setProbability(Double.valueOf(valueTextField.getText()) / 100);
    	
    	if (oldDisplayOn != displayToggle.isSelected())
    		arc.setDisplayOn(displayToggle.isSelected());

        getEditor().setSaved(false);
        getEditor().updateNet();

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    /**
     * @return Returns the editor.
     */
    public EditorVC getEditor()
    {
        return editor;
    }
}
