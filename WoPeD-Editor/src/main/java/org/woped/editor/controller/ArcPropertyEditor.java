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
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import org.woped.core.model.ArcModel;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.gui.lookAndFeel.WopedButton;
import org.woped.gui.translations.Messages;

@SuppressWarnings("serial")
public class ArcPropertyEditor extends JDialog {

    // General
    private ArcModel arc = null;
    private EditorVC editor = null;
    private JPanel contentPanel = null;

    // General section
    private JPanel generalSection = null;
    private JLabel weightLabel = null;
    private JSpinner weightSpinner = null;
    private JLabel idLabel = null;
    private JTextField idTextField = null;

    // Probability Section
    private JPanel probabilitySection = null;
    private JLabel probabilityLabel = null;
    private JSpinner probabilitySpinner = null;
    private JCheckBox displayProbabilityCheckbox = null;

    // Buttons
    private JPanel buttonPanel = null;
    private WopedButton buttonOk = null;
    private WopedButton buttonCancel = null;
    private KeyListener defaultKeyListener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {

            if ( e.getKeyCode() == KeyEvent.VK_ESCAPE ) {
                e.consume();
                buttonCancel.doClick();
            }

            if ( e.getKeyCode() == KeyEvent.VK_ENTER ) {
                e.consume();
                buttonOk.doClick();
            }
        }
    };

    public ArcPropertyEditor(Frame owner, Point position, ArcModel arc, EditorVC editor) {
        super(owner, true);
        this.arc = arc;
        this.editor = editor;

        initialize(position);
    }

    private void initialize(Point position) {

        this.setVisible(false);

        this.setTitle(Messages.getString("Arc.Properties"));
        this.setSize(350, 250);
        this.setResizable(false);
        this.getContentPane().add(getContentPanel(), BorderLayout.NORTH);
        this.getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);
        this.setLocation(new Point(position.x, position.y));
        this.setAlwaysOnTop(true);
        this.pack();

        // setup default keys
        ((JSpinner.DefaultEditor) weightSpinner.getEditor()).getTextField().addKeyListener(defaultKeyListener);
        ((JSpinner.DefaultEditor) probabilitySpinner.getEditor()).getTextField().addKeyListener(defaultKeyListener);
        displayProbabilityCheckbox.addKeyListener(defaultKeyListener);

        this.setVisible(true);
        getWeightSpinner().requestFocus();
    }

    private JPanel getContentPanel() {
        if ( contentPanel == null ) {
            contentPanel = new JPanel();
            contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            contentPanel.setLayout(new GridBagLayout());

            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1;
            c.weighty = 1;

            c.gridy = 0;
            contentPanel.add(getGeneralSection(), c);

            c.gridy = 1;
            contentPanel.add(getProbabilitySection(), c);
        }

        return contentPanel;
    }

    private JPanel getGeneralSection() {
        if ( this.generalSection == null ) {
            this.generalSection = new JPanel();
            generalSection.setLayout(new GridBagLayout());
            generalSection.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("Arc.Properties.General")), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1;
            c.weighty = 1;
            c.insets = new Insets(0, 10, 0, 0);
            c.gridwidth = 1;

            c.gridx = 0;
            generalSection.add(getWeightLabel(), c);

            c.gridx = 1;
            generalSection.add(getWeightSpinner(), c);

            c.gridx = 3;
            generalSection.add(getIdLabel(), c);

            c.gridx = 4;
            generalSection.add(getIdTextField(), c);
        }

        return this.generalSection;
    }

    private JPanel getProbabilitySection() {
        if ( probabilitySection == null ) {
            probabilitySection = new JPanel();
            probabilitySection.setLayout(new GridBagLayout());
            probabilitySection.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("Arc.Properties.Probability")), BorderFactory.createEmptyBorder(5, 5, 0, 5)));

            GridBagConstraints c = new GridBagConstraints();
            c.weightx = 1;
            c.weighty = 1;
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridwidth = 1;
            c.insets = new Insets(0, 10, 0, 0);

            c.gridx = 0;
            c.gridy = 0;
            probabilitySection.add(getProbabilityLabel(), c);

            c.gridx = 1;
            probabilitySection.add(getProbabilitySpinner(), c);

            c.gridx = 2;
            probabilitySection.add(getCheckBox(), c);
        }

        return probabilitySection;
    }

    private JLabel getWeightLabel() {
        if ( weightLabel == null ) {
            weightLabel = new JLabel(Messages.getString("Arc.Properties.Weight") + ":");
        }

        return weightLabel;
    }

    private JSpinner getWeightSpinner() {

        if ( weightSpinner == null ) {
            SpinnerModel weightModel = new SpinnerNumberModel(arc.getInscriptionValue(), 1, Integer.MAX_VALUE, 1);
            weightSpinner = new JSpinner(weightModel);
            weightSpinner.setPreferredSize(new Dimension(80, 20));
        }

        return weightSpinner;
    }

    private JLabel getProbabilityLabel() {
        if ( probabilityLabel == null ) {
            probabilityLabel = new JLabel(Messages.getString("Arc.Properties.Name") + ":");
        }

        return probabilityLabel;
    }

    private JSpinner getProbabilitySpinner() {
        if ( probabilitySpinner == null ) {
            SpinnerModel probabilitySpinnerModel = new SpinnerNumberModel(arc.getProbability() * 100, 0, 100, 5);
            probabilitySpinner = new JSpinner(probabilitySpinnerModel);
            probabilitySpinner.setPreferredSize(new Dimension(80, 20));
        }

        return probabilitySpinner;
    }

    private JLabel getIdLabel() {
        if ( idLabel == null ) {
            idLabel = new JLabel("Id#: ");
        }

        return idLabel;
    }

    private JTextField getIdTextField() {
        if ( idTextField == null ) {
            idTextField = new JTextField();
            idTextField.setPreferredSize(new Dimension(80, 20));
            idTextField.setMinimumSize(new Dimension(80, 20));
            idTextField.setMaximumSize(new Dimension(80, 20));
            idTextField.setText("" + arc.getId());
            idTextField.setHorizontalAlignment(SwingConstants.CENTER);
            idTextField.setEditable(false);
            idTextField.setFocusable(false);
        }

        return idTextField;
    }

    private JCheckBox getCheckBox() {
        if ( displayProbabilityCheckbox == null ) {
            displayProbabilityCheckbox = new JCheckBox(Messages.getString("Arc.Properties.Display.Check"), arc.displayProbability());
            displayProbabilityCheckbox.setHorizontalAlignment(SwingConstants.LEFT);
            displayProbabilityCheckbox.setMargin(new Insets(0, 0, 0, 0));
        }
        return displayProbabilityCheckbox;
    }

    private JPanel getButtonPanel() {
        if ( buttonPanel == null ) {
            buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(getButtonOk());
            buttonPanel.add(getButtonCancel());
            //            buttonPanel.add(getButtonApply());
        }
        return buttonPanel;
    }

    private JButton getButtonOk() {
        if ( buttonOk == null ) {
            buttonOk = new WopedButton();
            buttonOk.setIcon(Messages.getImageIcon("Button.Ok"));
            buttonOk.setText(Messages.getString("Button.Ok.Title"));


            buttonOk.setMnemonic(KeyEvent.VK_O);
            buttonOk.setPreferredSize(new Dimension(100, 25));
            buttonOk.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    apply();
                    ArcPropertyEditor.this.dispose();
                }
            });

        }
        return buttonOk;
    }

    private JButton getButtonCancel() {
        if ( buttonCancel == null ) {
            buttonCancel = new WopedButton();
            buttonCancel.setText(Messages.getString("Button.Cancel.Title"));
            buttonCancel.setIcon(Messages.getImageIcon("Button.Cancel"));
            buttonCancel.setMnemonic(KeyEvent.VK_C);
            buttonCancel.setPreferredSize(new Dimension(120, 25));
            buttonCancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    ArcPropertyEditor.this.dispose();
                }
            });
        }

        return buttonCancel;
    }

    private void apply() {

        int newWeight = (Integer) weightSpinner.getValue();
        if ( newWeight != arc.getInscriptionValue() ) arc.setInscriptionValue(newWeight);

        Double newProbability = (Double) probabilitySpinner.getValue() / 100;
        if ( !newProbability.equals(arc.getProbability()) ) arc.setProbability(newProbability);

        boolean newProbabilityVisibility = displayProbabilityCheckbox.isSelected();
        if ( newProbabilityVisibility != arc.displayProbability() ) arc.displayProbability(newProbabilityVisibility);

        getEditor().setSaved(false);
        getEditor().updateNet();
    }

    /**
     * @return Returns the editor.
     */
    public EditorVC getEditor() {
        return editor;
    }
}
