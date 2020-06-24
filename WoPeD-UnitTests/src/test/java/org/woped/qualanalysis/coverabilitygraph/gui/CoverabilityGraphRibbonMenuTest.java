package org.woped.qualanalysis.coverabilitygraph.gui;

import org.junit.Before;
import org.junit.Test;
import org.woped.config.general.WoPeDGeneralConfiguration;
import org.woped.core.config.ConfigurationManager;
import org.woped.gui.translations.Messages;

import static junit.framework.TestCase.assertTrue;

public class CoverabilityGraphRibbonMenuTest {
    @Before
    public void setup() {
        WoPeDGeneralConfiguration woPeDGeneralConfiguration = new WoPeDGeneralConfiguration();
        woPeDGeneralConfiguration.initConfig();
        ConfigurationManager.setConfiguration(woPeDGeneralConfiguration);
    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @Test
    public void resourcesExist() throws Exception {

        testResource("CoverabilityGraph.Ribbon.Task.Title");
        testResource("CoverabilityGraph.Ribbon.MainBand.Title");
        testButtonResources("CoverabilityGraph.Ribbon.CloseButton");
        testButtonResources("CoverabilityGraph.Ribbon.RefreshButton");
        testButtonResources("CoverabilityGraph.Ribbon.UnselectButton");
        testButtonResources("CoverabilityGraph.Ribbon.ShowSettingsButton");
        testButtonResources("CoverabilityGraph.Ribbon.ExportButton");
        testButtonResources("CoverabilityGraph.Ribbon.ZoomInButton");
        testButtonResources("CoverabilityGraph.Ribbon.ZoomOutButton");
        testButtonResources("CoverabilityGraph.Ribbon.ZoomChooserButton");
    }

    private void testButtonResources(String prefix){
        testResource(prefix + ".Title");
        testResource(prefix + ".Tooltip.Title");
        testResource(prefix + ".Tooltip.Description");
    }

    private void testResource(String key) {
        assertTrue(String.format("The resource for the key %s is missing", key), Messages.exists(key));
    }

}