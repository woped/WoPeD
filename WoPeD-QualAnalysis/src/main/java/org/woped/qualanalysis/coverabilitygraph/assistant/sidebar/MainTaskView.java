package org.woped.qualanalysis.coverabilitygraph.assistant.sidebar;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class can be used to display main task steps in the sidebar of the coverability graph assistant view.
 * It should be used as header of the sidebar.
 */
public class MainTaskView extends JPanel {

    private JLabel title;
    private JLabel description;

    /**
     * Constructs a new empty main task view.
     */
    public MainTaskView() {
        initialize();
    }

    /**
     * Sets the provided text as title of the main task view.
     *
     * @param text the text to set as title
     */
    public void setTitle(String text) {
        this.title.setText(String.format("<html><h2>%s</h2></html>", text));
    }

    /**
     * Sets the provided text as description of the main task view.
     *
     * @param text the text to set as description
     */
    public void setDescription(String text) {
        this.description.setText(String.format("<html><p>%s</p></html>", text));
    }

    private void initialize() {
        this.setLayout(new BorderLayout());

        this.title = new JLabel("");
        this.add(title, BorderLayout.NORTH);

        this.description = new JLabel("");
        this.add(description, BorderLayout.CENTER);
    }
}
