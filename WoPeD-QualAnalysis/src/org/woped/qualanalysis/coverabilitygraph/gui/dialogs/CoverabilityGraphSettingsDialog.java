/**
 * ReachabilityGraph implementation was done by Manuel Fladt and Benjamin Geiger.
 * The code was written for a project at BA Karlsruhe in 2007/2008 under authority
 * of Prof. Dr. Thomas Freytag and Andreas Eckleder.
 * <p>
 * This class was written by
 *
 * @author Benjamin Geiger
 */

package org.woped.qualanalysis.coverabilitygraph.gui.dialogs;

import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.Edge;
import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.coverabilitygraph.gui.CoverabilityGraphSettings;
import org.woped.qualanalysis.coverabilitygraph.gui.CoverabilityGraphVC;
import org.woped.qualanalysis.coverabilitygraph.gui.ParallelRouter;
import org.woped.qualanalysis.coverabilitygraph.gui.views.CoverabilityGraphColorScheme;
import org.woped.qualanalysis.coverabilitygraph.gui.views.formatters.MarkingFormatter;
import org.woped.qualanalysis.coverabilitygraph.gui.views.formatters.MultiSetMarkingFormatter;
import org.woped.qualanalysis.coverabilitygraph.gui.views.formatters.TokenVectorMarkingFormatter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class CoverabilityGraphSettingsDialog extends JDialog {

    private static final long serialVersionUID = -1141097444949417968L;
    private final String NOTATION_MULITISET = "MultiSet";
    private final String NOTATION_TOKENVECTOR = "TokenVector";

    CoverabilityGraphVC rgp = null;
    // Buttons
    private JButton saveButton = null;
    private JButton cancelButton = null;
    private JLabel hierarchicLabel = null;
    private JLabel placeWidthLabel = null;
    private JLabel placeHeightLabel = null;
    private JLabel hierarchicSpaceVerticalLabel = null;
    private JLabel hierarchicSpaceHorizontalLabel = null;
    // Textfields
    private JTextField placeWidthTf = null;
    private JTextField placeHeightTf = null;
    private JTextField hierarchicSpaceHorizontalTf = null;
    private JTextField hierarchicSpaceVerticalTf = null;
    private JRadioButton grayGraphRb = null;
    private JRadioButton colorGraphRb = null;
    // Checkboxes
    private JCheckBox parallelRoutingCb = null;
    // RadioButtons
    private ButtonGroup colorButtonGroup;
    private ButtonGroup placeStyleGroup;
    private JRadioButton tokenVectorOption;
    // GraphAttributes
    private JRadioButton multiSetOption;
    private CoverabilityGraphSettings settings;

    public CoverabilityGraphSettingsDialog(CoverabilityGraphVC graphVC) {
        super();
        this.rgp = graphVC;
        this.settings = rgp.getActiveView().getSettings();

        initComponents();
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setTitle(Messages.getString("QuanlAna.ReachabilityGraph.Settings.Title"));
        int height = 410;
        int width = 240;
        this.setSize(new Dimension(width, height));
        Point location = new Point((int) rgp.getLocationOnScreen().getX() + rgp.getWidth() / 2 - width / 2, (int) rgp
                .getLocationOnScreen().getY()
                + rgp.getHeight() / 2 - height / 2);
        this.setLocation(location);
        this.setResizable(false);
        this.setModal(true);
    }

    private void initComponents() {

        // TODO: 16.01.2017 convert into dynamic layout
        JLabel graphVisual = new JLabel("<html><b>" + Messages.getString("QuanlAna.ReachabilityGraph.Settings.GraphSection")
                + "</b></html>");
        graphVisual.setBounds(new Rectangle(20, 15, 220, 16));


        JLabel placeStyleHeader = new JLabel(Messages.getString("CoverabilityGraph.SettingsDialog.MarkingNotation.Header"));
        placeStyleHeader.setBounds(new Rectangle(20, 40, 180, 16));

        multiSetOption = new JRadioButton(Messages.getString("CoverabilityGraph.SettingsDialog.MarkingNotation.MultiSet"));
        multiSetOption.setActionCommand(NOTATION_MULITISET);
        multiSetOption.setBounds(new Rectangle(30, 60, 90, 22));
        multiSetOption.setToolTipText("e.g. ( p2 2p3 )");

        tokenVectorOption = new JRadioButton(Messages.getString("CoverabilityGraph.SettingsDialog.MarkingNotation.TokenVector"));
        tokenVectorOption.setActionCommand(NOTATION_TOKENVECTOR);
        tokenVectorOption.setBounds(new Rectangle(130, 60, 90, 22));
        tokenVectorOption.setToolTipText("e.g. ( 0 1 2 )");

        placeStyleGroup = new ButtonGroup();
        placeStyleGroup.add(multiSetOption);
        placeStyleGroup.add(tokenVectorOption);

        if (getMarkingNotation().equals("MultiSet")) {
            multiSetOption.setSelected(true);
        } else if (getMarkingNotation().equals("TokenVector")) {
            tokenVectorOption.setSelected(true);
        }

        JLabel colorSchemeHeader = new JLabel(Messages.getString("CoverabilityGraph.SettingsDialog.ColorScheme.Header"));
        colorSchemeHeader.setBounds(new Rectangle(20, 90, 180, 16));


        grayGraphRb = new JRadioButton(Messages.getString("QuanlAna.ReachabilityGraph.Settings.Color.Grayscale"));
        grayGraphRb.setBounds(new Rectangle(30, 110, 90, 22));

        colorGraphRb = new JRadioButton(Messages.getString("QuanlAna.ReachabilityGraph.Settings.Color.Colored"));
        colorGraphRb.setBounds(new Rectangle(130, 110, 90, 22));

        colorButtonGroup = new ButtonGroup();
        colorButtonGroup.add(grayGraphRb);
        colorButtonGroup.add(colorGraphRb);

        if (getColored()) {
            colorGraphRb.setSelected(true);
        } else {
            grayGraphRb.setSelected(true);
        }

        String notSupportedText = Messages.getString("QuanlAna.ReachabilityGraph.Settings.Color.NotSupported");
        JLabel colorSchemesNotSupportedLabel = new JLabel(String.format("<html><p><small>%s</small></p></html>", notSupportedText));
        colorSchemesNotSupportedLabel.setBounds(20, 110, 180, 22);
        colorSchemesNotSupportedLabel.setForeground(Color.DARK_GRAY);

        parallelRoutingCb = new JCheckBox(Messages.getString("QuanlAna.ReachabilityGraph.Settings.ParallelRouting"));
        parallelRoutingCb.setSelected(getParallelRoutingEnabled());
        parallelRoutingCb.setBounds(new Rectangle(30, 140, 180, 22));

        placeHeightLabel = new JLabel(Messages.getString("QuanlAna.ReachabilityGraph.Settings.Place.Height"));
        placeHeightLabel.setBounds(new Rectangle(30, 175, 130, 22));
        placeHeightTf = new JTextField();
        placeHeightTf.setHorizontalAlignment(JTextField.CENTER);
        placeHeightTf.setText(Integer.toString(getPlaceHeight()));
        placeHeightTf.setBounds(new Rectangle(160, 175, 50, 22));

        placeWidthLabel = new JLabel(Messages.getString("QuanlAna.ReachabilityGraph.Settings.Place.Width"));
        placeWidthLabel.setBounds(new Rectangle(30, 205, 130, 22));
        placeWidthTf = new JTextField();
        placeWidthTf.setHorizontalAlignment(JTextField.CENTER);
        placeWidthTf.setText(Integer.toString(getPlaceWidth()));
        placeWidthTf.setBounds(new Rectangle(160, 205, 50, 22));

        hierarchicLabel = new JLabel("<html><b>"
                + Messages.getString("QuanlAna.ReachabilityGraph.Settings.HierarchicSection") + "</b></html>");
        hierarchicLabel.setBounds(new Rectangle(20, 240, 200, 22));

        hierarchicSpaceHorizontalLabel = new JLabel(Messages
                .getString("QuanlAna.ReachabilityGraph.Settings.Hierarchic.Horizontal"));
        hierarchicSpaceHorizontalLabel.setBounds(new Rectangle(30, 270, 130, 22));
        hierarchicSpaceHorizontalTf = new JTextField();
        hierarchicSpaceHorizontalTf.setHorizontalAlignment(JTextField.CENTER);
        hierarchicSpaceHorizontalTf.setText(Integer.toString(getHierarchicSpacingHorizontal()));
        hierarchicSpaceHorizontalTf.setBounds(new Rectangle(160, 270, 50, 22));

        hierarchicSpaceVerticalLabel = new JLabel(Messages
                .getString("QuanlAna.ReachabilityGraph.Settings.Hierarchic.Vertical"));
        hierarchicSpaceVerticalLabel.setBounds(new Rectangle(30, 300, 150, 22));
        hierarchicSpaceVerticalTf = new JTextField();
        hierarchicSpaceVerticalTf.setHorizontalAlignment(JTextField.CENTER);
        hierarchicSpaceVerticalTf.setText(Integer.toString(getHierarchicSpacingVertical()));
        hierarchicSpaceVerticalTf.setBounds(new Rectangle(160, 300, 50, 22));

        // TODO: 16.01.2017 Create custom action instead of action listener
        saveButton = new JButton(Messages.getString("QuanlAna.ReachabilityGraph.Settings.Button.Save"));
        saveButton.setBounds(new Rectangle(20, 340, 90, 29));
        saveButton.addActionListener(new SaveButtonListener());
        cancelButton = new JButton(Messages.getString("QuanlAna.ReachabilityGraph.Settings.Button.Cancel"));
        cancelButton.setBounds(new Rectangle(130, 340, 90, 29));
        cancelButton.addActionListener(new CancelButtonListener());

        // Set "Escape" and "Enter" as action keys for Save/Cancel-Buttons
        ActionMap am = getRootPane().getActionMap();
        InputMap im = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        Object windowOkKey = new Object();
        KeyStroke windowOkStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        Action windowOkAction = new AbstractAction() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                CoverabilityGraphSettingsDialog.this.saveButton.doClick();
            }
        };

        Object windowCancelKey = new Object();
        KeyStroke windowCancelStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        Action windowCancelAction = new AbstractAction() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                CoverabilityGraphSettingsDialog.this.cancelButton.doClick();
            }
        };

        im.put(windowOkStroke, windowOkKey);
        am.put(windowOkKey, windowOkAction);

        im.put(windowCancelStroke, windowCancelKey);
        am.put(windowCancelKey, windowCancelAction);

        // Add all components to the panel
        this.setLayout(null);
        this.add(graphVisual);
        this.add(placeStyleHeader);
        this.add(multiSetOption);
        this.add(tokenVectorOption);

        this.add(colorSchemeHeader);
        if (settings.colorSchemeSupported) {
            this.add(grayGraphRb);
            this.add(colorGraphRb);
        } else {
            this.add(colorSchemesNotSupportedLabel);
        }

        this.add(parallelRoutingCb);

        this.add(placeHeightLabel);
        this.add(placeHeightTf);
        this.add(placeWidthLabel);
        this.add(placeWidthTf);

        this.add(hierarchicLabel);
        this.add(hierarchicSpaceHorizontalLabel);
        this.add(hierarchicSpaceHorizontalTf);
        this.add(hierarchicSpaceVerticalLabel);
        this.add(hierarchicSpaceVerticalTf);

        this.add(saveButton);
        this.add(cancelButton);
    }

    private boolean getParallelRoutingEnabled() {
        Edge.Routing routing = rgp.getActiveView().getSettings().edgeRouting;
        return routing instanceof ParallelRouter;
    }

    private void setParallelRoutingEnabled(boolean enabled) {

        if (enabled)
            settings.edgeRouting = ParallelRouter.getSharedInstance(rgp.getActiveView().getGraphModel().getGraph().getGraphLayoutCache());
        else settings.edgeRouting = new DefaultEdge.LoopRouting();
    }

    private int getHierarchicSpacingVertical() {
        return settings.verticalGap;
    }

    private void setHierarchicSpacingVertical(int newValue) {
        this.settings.verticalGap = newValue;
    }

    private int getHierarchicSpacingHorizontal() {
        return this.settings.horizontalGap;
    }

    private void setHierarchicSpacingHorizontal(int newValue) {
        this.settings.horizontalGap = newValue;
    }

    private int getPlaceWidth() {
        return this.settings.minNodeSize.width;
    }

    private void setPlaceWidth(int width) {
        Dimension oldDimension = settings.minNodeSize;
        this.settings.minNodeSize = new Dimension(width, oldDimension.height);
    }

    private int getPlaceHeight() {
        return this.settings.minNodeSize.height;
    }

    private void setPlaceHeight(int height) {
        Dimension oldDimension = settings.minNodeSize;
        this.settings.minNodeSize = new Dimension(oldDimension.width, height);
    }

    private boolean getColored() {
        return this.settings.colorSchemeSupported && this.settings.colorScheme.equals(CoverabilityGraphColorScheme.COLORED_SCHEME());
    }

    private void setColor(boolean isColored) {
        if (isColored) this.settings.colorScheme = CoverabilityGraphColorScheme.COLORED_SCHEME();
        else this.settings.colorScheme = CoverabilityGraphColorScheme.GRAY_SCALED_SCHEME();
    }

    private String getMarkingNotation() {
        if (this.settings.markingFormatter instanceof MultiSetMarkingFormatter) return NOTATION_MULITISET;
        else return NOTATION_TOKENVECTOR;
    }

    private void setMarkingNotation(String notation) {

        MarkingFormatter newFormatter = null;
        switch (notation) {
            case NOTATION_TOKENVECTOR:
                newFormatter = new TokenVectorMarkingFormatter();
                break;
            case NOTATION_MULITISET:
                newFormatter = new MultiSetMarkingFormatter();
        }

        this.settings.markingFormatter = newFormatter;
    }

    private class CancelButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            CoverabilityGraphSettingsDialog.this.dispose();
        }
    }

    private class SaveButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            if (integerValueChecker(CoverabilityGraphSettingsDialog.this.hierarchicSpaceHorizontalTf.getText())) {
                int oldValue = getHierarchicSpacingHorizontal();
                int newValue = Integer.parseInt(CoverabilityGraphSettingsDialog.this.hierarchicSpaceHorizontalTf.getText());
                if (oldValue != newValue) {
                    setHierarchicSpacingHorizontal(newValue);
                }
            } else {
                // TODO: 16.01.2017 show validation error
                // do default
            }

            if (integerValueChecker(CoverabilityGraphSettingsDialog.this.hierarchicSpaceVerticalTf.getText())) {
                int oldValue = getHierarchicSpacingVertical();
                int newValue = Integer.parseInt(CoverabilityGraphSettingsDialog.this.hierarchicSpaceVerticalTf.getText());
                if (oldValue != newValue) {
                    setHierarchicSpacingVertical(newValue);
                }
            } else {
                // TODO: 16.01.2017 show validation error
                // do default
            }

            if (integerValueChecker(CoverabilityGraphSettingsDialog.this.placeWidthTf.getText())) {
                int oldValue = getPlaceWidth();
                int newValue = Integer.parseInt(CoverabilityGraphSettingsDialog.this.placeWidthTf.getText());
                if (oldValue != newValue) {
                    setPlaceWidth(newValue);
                }
            } else {
                // TODO: 16.01.2017 show validation error
                // do default
            }

            if (integerValueChecker(CoverabilityGraphSettingsDialog.this.placeHeightTf.getText())) {
                if (getPlaceHeight() != Integer.parseInt(CoverabilityGraphSettingsDialog.this.placeHeightTf.getText())) {
                    setPlaceHeight(Integer.parseInt(CoverabilityGraphSettingsDialog.this.placeHeightTf.getText()));
                }
            } else {
                // TODO: 16.01.2017 show validation error
                // do default
            }

            if (getParallelRoutingEnabled() != CoverabilityGraphSettingsDialog.this.parallelRoutingCb.isSelected()) {
                setParallelRoutingEnabled(CoverabilityGraphSettingsDialog.this.parallelRoutingCb.isSelected());
            }

            if (getColored() != CoverabilityGraphSettingsDialog.this.colorGraphRb.isSelected()) {
                setColor(CoverabilityGraphSettingsDialog.this.colorGraphRb.isSelected());
            }

            if (!getMarkingNotation().equals(placeStyleGroup.getSelection().getActionCommand())) {
                setMarkingNotation(placeStyleGroup.getSelection().getActionCommand());
            }

            rgp.getActiveView().applySettings(settings);
            CoverabilityGraphSettingsDialog.this.dispose();
        }

        private boolean integerValueChecker(String integer) {
            try {
                Integer.parseInt(integer);
                return true;
            } catch (NumberFormatException nfe) {
                return false;
            }
        }
    }
}
