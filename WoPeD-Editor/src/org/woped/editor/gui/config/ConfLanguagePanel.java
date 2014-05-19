/*
 * 
 * Copyright (C) 2004-2005, see @author in JavaDoc for the author 
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *
 * For contact information please visit http://woped.dhbw-karlsruhe.de
 *
 */
package org.woped.editor.gui.config;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.woped.core.config.ConfigurationManager;
import org.woped.gui.translations.Messages;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 *         The <code>ConfLanguagePanel</code> is the
 *         <code>AbstractConfPanel</code> for the configuration of the
 *         language. <br>
 *         Created on: 26.11.2004 Last Change on: 14.11.2005
 */

@SuppressWarnings("serial")
public class ConfLanguagePanel extends AbstractConfPanel
{
    //private Component      guiObject;
    // Language selection
    private JPanel         languagePanel    = null;
    private JComboBox      languageComboBox = null;
    private JLabel         languageLabel    = null;

    private final Locale[] language         = { Locale.ENGLISH, Locale.GERMAN };

    /**
     * Constructor for ConfToolsPanel.
     */
    public ConfLanguagePanel(String name)
    {
        super(name);
        initialize();
    }

    /**
     * @see AbstractConfPanel#applyConfiguration()
     */
    public boolean applyConfiguration()
    {
        int selected = getLanguageComboBox().getSelectedIndex();
        Locale locale = language[selected];

        boolean changed = !locale.getLanguage().equals(ConfigurationManager.getConfiguration().getLocale().getLanguage());

        if (changed)
        {
            ConfigurationManager.getConfiguration().setLocaleLanguage(locale.getLanguage());
            ConfigurationManager.getConfiguration().setLocaleCountry(locale.getCountry());
            ConfigurationManager.getConfiguration().setLocaleVariant(locale.getVariant());
            ConfigurationManager.getConfiguration().setLocale();
            JOptionPane.showMessageDialog(this, 
            							  Messages.getString("Configuration.Language.Dialog.Restart.Message"), 
            							  Messages.getString("Configuration.Language.Dialog.Restart.Title"),
            							  JOptionPane.INFORMATION_MESSAGE);
        }
        return true;
    }

    /**
     * @see AbstractConfPanel#readConfiguration()
     */
    public void readConfiguration()
    {
        int selected = 0;
    	Locale loc = ConfigurationManager.getConfiguration().getLocale();

        for (int i = 0; i < language.length; i++)
        {
            if (language[i].getLanguage().equals(loc.getLanguage()))
            {
                selected = i;
                break;
            }
        }
        getLanguageComboBox().setSelectedIndex(selected);
    }

    private void initialize()
    {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        contentPanel.add(getLanguagePanel(), c);
        // dummy
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 1;
        c.gridy = 1;
        contentPanel.add(new JPanel(), c);
        setMainPanel(contentPanel);
    }

    // ################## GUI COMPONENTS #################### */

    private JComboBox getLanguageComboBox()
    {
        if (languageComboBox == null)
        {
            String[] languageBoxItems = new String[language.length];
            
            for (int i = 0; i < language.length; i++)
            {
                languageBoxItems[i] = language[i].getDisplayLanguage(language[i]);
            }
            
            languageComboBox = new JComboBox(languageBoxItems);
            languageComboBox.setMinimumSize(new Dimension(300, 20));
            languageComboBox.setToolTipText("<html>" + Messages.getString("Configuration.Language.Tooltip") + "</html>");
        }
        
        return languageComboBox;
    }

    private JPanel getLanguagePanel()
    {
        if (languagePanel == null)
        {
            languagePanel = new JPanel();
            languagePanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;

            languagePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("Configuration.Language.Panel.Language.Title")), BorderFactory
                    .createEmptyBorder(5, 5, 10, 5)));

            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 0;
            languagePanel.add(getLanguageLabel(), c);

            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 0;
            c.fill = GridBagConstraints.HORIZONTAL;
            languagePanel.add(getLanguageComboBox(), c);

        }
        return languagePanel;
    }

    private JLabel getLanguageLabel()
    {
        if (languageLabel == null)
        {
            languageLabel = new JLabel("<html>" + Messages.getString("Configuration.Language.Label.SelectLanguage") + "</html>");
            languageLabel.setHorizontalAlignment(JLabel.RIGHT);
        }
        return languageLabel;
    }
}