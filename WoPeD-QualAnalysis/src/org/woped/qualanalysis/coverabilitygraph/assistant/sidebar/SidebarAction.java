package org.woped.qualanalysis.coverabilitygraph.assistant.sidebar;

import org.woped.gui.translations.Messages;

import javax.swing.*;

/**
 * This class is in charge of displaying the internationalized button text and tooltip for sidebar actions.
 * Sidebar actions are actions (mostly buttons) that can be triggered from the sidebar.
 * Custom sidebar actions should extend this class.
 */
public abstract class SidebarAction extends AbstractAction {

    /**
     * Constructs a new sidebar action that uses the provided properties prefix to load internationalized action and tooltip messages.
     *
     * @param propertiesPrefix the property prefix to look for the internationalized messages
     */
    protected SidebarAction(String propertiesPrefix){
        String text = Messages.getString(propertiesPrefix + ".text");
        putValue(Action.NAME, text);

        String description = Messages.getString(propertiesPrefix + ".description");
        putValue(SHORT_DESCRIPTION, description);

        int mnemonic = Messages.getMnemonic(propertiesPrefix);
        putValue(MNEMONIC_KEY, mnemonic);
    }
}
