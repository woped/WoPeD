package org.woped.gui.translations;

import org.junit.Before;
import org.junit.Test;
import org.woped.config.general.WoPeDGeneralConfiguration;
import org.woped.core.config.ConfigurationManager;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MessagesTest {

    @Before
    public void setup() {
        WoPeDGeneralConfiguration woPeDGeneralConfiguration = new WoPeDGeneralConfiguration();
        woPeDGeneralConfiguration.initConfig();
        ConfigurationManager.setConfiguration(woPeDGeneralConfiguration);
    }

    @Test
    public void getString_keyExists_returnsExpectedValue() {
        String key = "Init.Config.Error";
        assertTrue(Messages.exists(key));
        assertEquals("German translation", "Initialisierungsfehler", Messages.getStringForLocale(key, Locale.GERMAN));
    }
}
