package org.woped.qualanalysis.coverabilitygraph.assistant.sidebar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

/**
 * This class provides functionality to display textual information in the sidebar of the coverability graph assistant view.
 */
public class SidebarVC {

    private JPanel sidebar;
    private JPanel content;

    private JComponent header;
    private Set<Component> components;

    /**
     * Constructs a new sidebar.
     */
    public SidebarVC(){
        createInfoPanel();
        this.components = new HashSet<>();
    }

    /**
     * Gets the sidebar component.
     *
     * @return the sidebar view
     */
    public JComponent getView(){
        return sidebar;
    }

    /**
     * Adds the provided component to the sidebar.
     *
     * @param component the component to add
     */
    public void addComponent(JComponent component) {

        this.components.add(component);
        component.setAlignmentX(Component.LEFT_ALIGNMENT);
        component.setAlignmentY(Component.TOP_ALIGNMENT);
        int ndx = content.getComponentCount() - 1;
        this.content.add(component, ndx);
        refresh();
    }

    /**
     * Removes all components from the sidebar
     */
    public void clear() {
        removeComponents();
        refresh();
    }

    /**
     * Sets the provided component as header of the sidebar.
     *
     * @param header the component to set as header
     */
    public void setHeader(JComponent header){
        this.header.removeAll();
        this.header.add(header, BorderLayout.CENTER);
    }

    /**
     * Refreshed the sidebar
     */
    public void refresh() {
        sidebar.validate();
        sidebar.repaint();
    }

    private void createInfoPanel() {

        sidebar = new JPanel();
        sidebar.setMinimumSize(new Dimension(300, 0));
        sidebar.setBorder(new EmptyBorder(10, 10, 10, 10));
        sidebar.setLayout(new BorderLayout(0, 10));

        header = new JPanel(new BorderLayout());
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.setAlignmentY(Component.TOP_ALIGNMENT);

        content = new JPanel();
        JScrollPane contentScroller = new JScrollPane(content);
        contentScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.white);
        content.setBorder(new EmptyBorder(10,10,10,10));

        content.add(Box.createVerticalGlue());

        content.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.setAlignmentY(Component.TOP_ALIGNMENT);

        sidebar.add(header, BorderLayout.NORTH);
        sidebar.add(contentScroller, BorderLayout.CENTER);
    }

    private void removeComponents() {

        // the box layout of the component contains a filler element for visibility reasons.
        // so don't use #removeAll()
        for (Component c : components) {
            this.content.remove(c);
        }

        this.components.clear();
    }
}
