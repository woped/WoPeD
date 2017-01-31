package org.woped.qualanalysis.coverabilitygraph.assistant.sidebar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * This class is responsible for displaying process steps in the info panel of the coverability graph assistant view
 */
public class ProgressDetailsView extends JPanel {

    private JLabel header;
    private JPanel detailsPanel;
    private JPanel actionsPanel;

    private GridBagConstraints c;

    public ProgressDetailsView() {
        this.setOpaque(false);
        this.setLayout(new GridBagLayout());

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.weightx = 1;
        c.weighty = 0;
        c.gridx = 0;

        createHeader();
        createDetailPanel();
        createActionsPanel();
    }

    /**
     * Sets the header text of the process step.
     * <p>
     * Valid html tags can be used to format the output.
     * The enclosing html tag is added automatically.
     *
     * @param content the text to display as header.
     */
    public void setHeaderText(String content) {
        header.setText(String.format("<html><h5>%s</h5></html>", content));
        updateSize();
    }

    /**
     * Adds an text to the details section of the view.
     * <p>
     * The details section has a dynamic table layout.
     * The rows and columns are added as needed.
     *
     * @param content the text to display
     * @param row     the desired row
     * @param column  the desired column
     */
    public void addDetail(String content, int row, int column) {
        JLabel detailLabel = new JLabel(String.format("<html><p>%s</p></html>", content));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = column;
        c.gridy = row;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        detailsPanel.add(detailLabel, c);

        updateSize();
    }

    /**
     * Adds an text to the actions section of the view.
     * <p>
     * Actions have a different design than details and should be used to describe the resulting action to the currently processed node
     * after the completion of the process step.
     * <p>
     * The actions section has a vertical stack layout. (Elements are stacked based on their insertion)
     *
     * @param action the textual description of the action
     */
    public void addAction(String action) {
        JLabel actionLabel = new JLabel(String.format("<html><strong>\u21D2&nbsp;%s</strong></html>", action));
        actionLabel.setForeground(new Color(0, 0, 200));
        actionsPanel.add(actionLabel);
        updateSize();

    }

    public void addAction(Action action) {
        JButton trigger = new JButton(action);
        actionsPanel.add(trigger);
        updateSize();
    }

    private void createHeader() {
        header = new JLabel();

        c.gridy = 0;
        this.add(header, c);
    }

    private void createDetailPanel() {
        detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setOpaque(false);

        c.gridy = 1;
        this.add(detailsPanel, c);
    }

    private void createActionsPanel() {
        actionsPanel = new JPanel();
        actionsPanel.setOpaque(false);
        actionsPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.PAGE_AXIS));

        c.gridy = 2;
        c.weighty = 1;
        this.add(actionsPanel, c);
    }

    private void updateSize() {
        this.validate();
        this.repaint();

        Dimension preferredSize = this.getPreferredSize();
        int MAX_WIDTH = 248; // Available width with active scrollbar if the sidebar has a width of 300
        this.setMaximumSize(new Dimension(MAX_WIDTH, preferredSize.height));
    }
}
