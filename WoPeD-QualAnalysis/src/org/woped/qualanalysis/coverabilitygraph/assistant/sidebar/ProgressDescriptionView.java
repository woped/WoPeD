package org.woped.qualanalysis.coverabilitygraph.assistant.sidebar;

import javax.swing.*;

/**
 * This class can be used to display sub headers in the sidebar.
 */
public class ProgressDescriptionView extends JPanel {

    private JLabel title;

    /**
     * Constructs a new empty progress description view.
     */
    public  ProgressDescriptionView(){

        this.setLayout( new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setOpaque(false);

        this.title = new JLabel("");
        this.add(title);
    }

    /**
     * Sets the provided text as title in the process description view.
     *
     * @param text the text to set as title
     */
    public void setTitle(String text){
        this.title.setText(String.format("<html><h4>%s</h4></html>", text));
    }
}
