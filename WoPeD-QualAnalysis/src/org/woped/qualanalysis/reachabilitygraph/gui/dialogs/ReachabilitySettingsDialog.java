/**
 * ReachabilityGraph implementation was done by Manuel Fladt and Benjamin Geiger.
 * The code was written for a project at BA Karlsruhe in 2007/2008 under authority
 * of Prof. Dr. Thomas Freytag and Andreas Eckleder.
 * <p>
 * This class was written by
 *
 * @author Benjamin Geiger
 */

package org.woped.qualanalysis.reachabilitygraph.gui.dialogs;

import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.reachabilitygraph.controller.SimulationRunningException;
import org.woped.qualanalysis.reachabilitygraph.gui.ReachabilityGraphPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class ReachabilitySettingsDialog extends JDialog {

    private static final long serialVersionUID = -1141097444949417968L;

    ReachabilityGraphPanel rgp = null;

    // Buttons
    JButton saveButton = null;
    JButton cancelButton = null;

    // Labels
    JLabel graphVisual = null;
    JLabel hierarchicLabel = null;
    JLabel placeWidthLabel = null;
    JLabel placeHeightLabel = null;
    JLabel hierarchicSpaceVerticalLabel = null;
    JLabel hierarchicSpaceHorizontalLabel = null;

    // Textfields
    JTextField placeWidthTf = null;
    JTextField placeHeightTf = null;
    JTextField hierarchicSpaceHorizontalTf = null;
    JTextField hierarchicSpaceVerticalTf = null;

    // RadioButtons
    private ButtonGroup colorButtonGroup;
    JRadioButton grayGraphRb = null;
    JRadioButton colorGraphRb = null;

    private ButtonGroup placeStyleGroup;
    private JRadioButton tokenVectorOption;
    private JRadioButton multiSetOption;

    // Checkboxes
    JCheckBox parallelRoutingCb = null;
    // GraphAttributes
    HashMap<String, String> graphAttributes = null;

    public ReachabilitySettingsDialog(ReachabilityGraphPanel rgp) {
        this.rgp = rgp;
        graphAttributes = rgp.getGraph().getAttributeMap();
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
        graphVisual = new JLabel("<html><b>" + Messages.getString("QuanlAna.ReachabilityGraph.Settings.GraphSection")
                + "</b></html>");
        graphVisual.setBounds(new Rectangle(20, 15, 220, 16));


        JLabel placeStyleHeader = new JLabel(Messages.getString("CoverabilityGraph.SettingsDialog.MarkingNotation.Header"));
        placeStyleHeader.setBounds(new Rectangle(20, 40, 180, 16));

        multiSetOption = new JRadioButton(Messages.getString("CoverabilityGraph.SettingsDialog.MarkingNotation.MultiSet"));
        multiSetOption.setActionCommand("MultiSet");
        multiSetOption.setBounds(new Rectangle(30, 60, 90, 22));
        multiSetOption.setToolTipText("e.g. ( p2 2p3 )");

        tokenVectorOption = new JRadioButton(Messages.getString("CoverabilityGraph.SettingsDialog.MarkingNotation.TokenVector"));
        tokenVectorOption.setActionCommand("TokenVector");
        tokenVectorOption.setBounds(new Rectangle(130, 60, 90, 22));
        tokenVectorOption.setToolTipText("e.g. ( 0 1 2 )");

        placeStyleGroup = new ButtonGroup();
        placeStyleGroup.add(multiSetOption);
        placeStyleGroup.add(tokenVectorOption);

        if(getMarkingNotation().equals("MultiSet")){
            multiSetOption.setSelected(true);
        } else  if(getMarkingNotation().equals("TokenVector")){
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


        saveButton = new JButton(Messages.getString("QuanlAna.ReachabilityGraph.Settings.Button.Save"));
        saveButton.setBounds(new Rectangle(20, 340, 90, 29));
        saveButton.addActionListener(new SaveButtonListener(rgp));
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
                ReachabilitySettingsDialog.this.saveButton.doClick();
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
                ReachabilitySettingsDialog.this.cancelButton.doClick();
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
        this.add(grayGraphRb);
        this.add(colorGraphRb);

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
        boolean enabled;

        if (graphAttributes.containsKey("reachabilityGraph.parallel")) {
            if (graphAttributes.get("reachabilityGraph.parallel").equals("true")) {
                enabled = true;
            } else if (graphAttributes.get("reachabilityGraph.parallel").equals("false")) {
                enabled = false;
            } else {
                enabled = true;
            }
        } else {
            return true;
        }
        return enabled;
    }

    private void setParallelRoutingEnabled(boolean enabled) {
        String enabledStr = "";

        if (enabled) {
            enabledStr = "true";
        } else {
            enabledStr = "false";
        }

        if (graphAttributes.containsKey("reachabilityGraph.parallel")) {
            graphAttributes.put("reachabilityGraph.parallel", enabledStr);
        } else {
            graphAttributes.put("reachabilityGraph.parallel", enabledStr);
        }
    }

    private int getHierarchicSpacingVertical() {
        if (graphAttributes.containsKey("reachabilityGraph.hierarchic.verticalSpace")) {
            return Integer.parseInt(graphAttributes.get("reachabilityGraph.hierarchic.verticalSpace"));
        } else {
            return 60; // default
        }
    }

    private void setHierarchicSpacingVertical(int vertical) {
        if (vertical > 0 && vertical < 10000) {
            graphAttributes.put("reachabilityGraph.hierarchic.verticalSpace", Integer.toString(vertical));
        } else {
            graphAttributes.put("reachabilityGraph.hierarchic.verticalSpace", "60");
        }
    }

    private int getHierarchicSpacingHorizontal() {
        if (graphAttributes.containsKey("reachabilityGraph.hierarchic.horizontalSpace")) {
            return Integer.parseInt(graphAttributes.get("reachabilityGraph.hierarchic.horizontalSpace"));
        } else {
            return 20; // default
        }
    }

    private void setHierarchicSpacingHorizontal(int horizontal) {
        if (horizontal > 0 && horizontal < 10000) {
            graphAttributes.put("reachabilityGraph.hierarchic.horizontalSpace", Integer.toString(horizontal));
        } else {
            graphAttributes.put("reachabilityGraph.hierarchic.horizontalSpace", "20");
        }
    }

    private int getPlaceWidth() {
        if (graphAttributes.containsKey("reachabilityGraph.place.width")) {
            return Integer.parseInt(graphAttributes.get("reachabilityGraph.place.width"));
        } else {
            return 80; // default
        }
    }

    private void setPlaceWidth(int width) {
        if (width > 0 && width < 1000) {
            graphAttributes.put("reachabilityGraph.place.width", Integer.toString(width));
        } else {
            graphAttributes.put("reachabilityGraph.place.width", "80"); // default
        }
    }

    private int getPlaceHeight() {
        if (graphAttributes.containsKey("reachabilityGraph.place.height")) {
            return Integer.parseInt(graphAttributes.get("reachabilityGraph.place.height"));
        } else {
            return 20; // default
        }
    }

    private void setPlaceHeight(int height) {
        if (height > 0 && height < 200) {
            graphAttributes.put("reachabilityGraph.place.height", Integer.toString(height));
        } else {
            graphAttributes.put("reachabilityGraph.place.height", "20"); // default
        }
    }

    private boolean getColored() {
        boolean isColored = false;

        if (graphAttributes.containsKey("reachabilityGraph.color")) {
            if (graphAttributes.get("reachabilityGraph.color").equals("true")) {
                isColored = true;
            } else if (graphAttributes.get("reachabilityGraph.color").equals("false")) {
                isColored = false;
            } else {
                isColored = true;
            }
        } else {
            return true;
        }
        return isColored;
    }

    private void setColor(boolean isColored) {
        String enabledStr = "";

        if (isColored) {
            enabledStr = "true";
        } else {
            enabledStr = "false";
        }

        graphAttributes.put("reachabilityGraph.color", enabledStr);

    }

    private String getMarkingNotation(){

        String MarkingNotation = "MultiSet";

        String key = "coverabilityGraph.MarkingNotation";
        if(graphAttributes.containsKey(key)){
            MarkingNotation = graphAttributes.get(key);
        }

        return MarkingNotation;
    }

    private void setMarkingNotation(String notation){
        String key = "coverabilityGraph.MarkingNotation";
        graphAttributes.put(key, notation);
    }

    class CancelButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            ReachabilitySettingsDialog.this.dispose();
        }

    }

    class SaveButtonListener implements ActionListener {

        ReachabilityGraphPanel rgp = null;

        public SaveButtonListener(ReachabilityGraphPanel rgp) {
            this.rgp = rgp;
        }

        public void actionPerformed(ActionEvent e) {
            int haveToDoLayout = 0;

            if (integerValueChecker(ReachabilitySettingsDialog.this.hierarchicSpaceHorizontalTf.getText())) {
                if (getHierarchicSpacingHorizontal() != Integer
                        .parseInt(ReachabilitySettingsDialog.this.hierarchicSpaceHorizontalTf.getText())) {
                    setHierarchicSpacingHorizontal(Integer
                            .parseInt(ReachabilitySettingsDialog.this.hierarchicSpaceHorizontalTf.getText()));
                    haveToDoLayout++;
                }
            } else {
                // do default
            }

            if (integerValueChecker(ReachabilitySettingsDialog.this.hierarchicSpaceVerticalTf.getText())) {
                if (getHierarchicSpacingVertical() != Integer
                        .parseInt(ReachabilitySettingsDialog.this.hierarchicSpaceVerticalTf.getText())) {
                    setHierarchicSpacingVertical(Integer
                            .parseInt(ReachabilitySettingsDialog.this.hierarchicSpaceVerticalTf.getText()));
                    haveToDoLayout++;
                }
            } else {
                // do default
            }

            if (integerValueChecker(ReachabilitySettingsDialog.this.placeWidthTf.getText())) {
                if (getPlaceWidth() != Integer.parseInt(ReachabilitySettingsDialog.this.placeWidthTf.getText())) {
                    setPlaceWidth(Integer.parseInt(ReachabilitySettingsDialog.this.placeWidthTf.getText()));
                    haveToDoLayout++;
                }
            } else {
                // do default
            }

            if (integerValueChecker(ReachabilitySettingsDialog.this.placeHeightTf.getText())) {
                if (getPlaceHeight() != Integer.parseInt(ReachabilitySettingsDialog.this.placeHeightTf.getText())) {
                    setPlaceHeight(Integer.parseInt(ReachabilitySettingsDialog.this.placeHeightTf.getText()));
                    haveToDoLayout++;
                }
            } else {
                // do default
            }

            if (getParallelRoutingEnabled() != ReachabilitySettingsDialog.this.parallelRoutingCb.isSelected()) {
                setParallelRoutingEnabled(ReachabilitySettingsDialog.this.parallelRoutingCb.isSelected());
                rgp.setParallelRouting(getParallelRoutingEnabled());
            }

            if (getColored() != ReachabilitySettingsDialog.this.colorGraphRb.isSelected()) {
                setColor(ReachabilitySettingsDialog.this.colorGraphRb.isSelected());
                rgp.setGrayScale(!getColored());
            }

            if(!getMarkingNotation().equals(placeStyleGroup.getSelection().getActionCommand())){
                setMarkingNotation(placeStyleGroup.getSelection().getActionCommand());
                haveToDoLayout++;
            }

            // This must be last call !
            if (haveToDoLayout > 0) {
                try {
                    rgp.layoutGraph(rgp.getSelectedType(), false);
                } catch (SimulationRunningException e1) {
                    ReachabilityWarning.showReachabilityWarning(this.rgp,
                            "QuanlAna.ReachabilityGraph.SimulationWarning");
                }
            }
            ReachabilitySettingsDialog.this.dispose();
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
