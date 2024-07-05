package org.woped.file.p2t;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.config.IGeneralConfiguration;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.gui.translations.Messages;


import javax.swing.*;

import java.awt.*;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class P2TUITest {

    private P2TUI p2tui;
    private MockedStatic<Messages> messagesMock;
    private MockedStatic<ConfigurationManager> configManagerMock;


    @BeforeEach
    public void setUp() {
        p2tui = new P2TUI();
        // Mock the static Messages class
        messagesMock = mockStatic(Messages.class);
        messagesMock.when(() -> Messages.getString("P2T.openP2T.text")).thenReturn("Prozess zu Text");
        messagesMock.when(() -> Messages.getString("P2T.oldservice.title")).thenReturn("Algorithmus");
        messagesMock.when(() -> Messages.getString("P2T.newservice.title")).thenReturn("LLM");
        messagesMock.when(() -> Messages.getString("P2T.apikey.title")).thenReturn("API Schl\\u00FCssel");
        messagesMock.when(() -> Messages.getString("P2T.prompt.title")).thenReturn("Prompt");
        messagesMock.when(() -> Messages.getString("P2T.prompt.checkbox.enable.title")).thenReturn("Bearbeitung aktivieren");
        messagesMock.when(() -> Messages.getString("P2T.get.GPTmodel.title")).thenReturn("GPT-Model:");
        messagesMock.when(() -> Messages.getString("P2T.popup.show.again.title")).thenReturn("Erneut anzeigen");

    }

    @AfterEach
    public void tearDown() {
        // Close the static mock
        messagesMock.close();
    }

    @Test
    public void testInitialize() {
        assertFalse(p2tui.isVisible(), "P2TUI should be visible");
        assertTrue(p2tui.getLayout() instanceof BorderLayout, "P2TUI layout should be BorderLayout");
        assertFalse(p2tui.isUndecorated(), "P2TUI shouldn't be undecorated");
        assertTrue(p2tui.isResizable(), "P2TUI should be resizable");

        assertEquals(Messages.getString("P2T.openP2T.text"), p2tui.getTitle(), "P2TUI title should be 'Open P2T'");

        assertEquals(new Dimension(600, 375), p2tui.getSize(), "Dialog should have size 600x375");

    }

    @Test
    public void initializeSwitchButtonPanel() {
        JPanel switchButtonPanel = p2tui.initializeSwitchButtonPanel();
        assertNotNull(switchButtonPanel, "SwitchButtonPanel should be null");
        assertTrue(switchButtonPanel.getLayout() instanceof GridBagLayout, "Layout should be GridBagLayout");

        Component[] components = switchButtonPanel.getComponents();
        assertEquals(2, components.length, "SwitchButtonPanel should have 2 components");

        JPanel radioPanel = (JPanel) components[0];
        assertEquals(2, radioPanel.getComponentCount());

        JRadioButton oldRadioButton = (JRadioButton) radioPanel.getComponent(0);
        JRadioButton newRadioButton = (JRadioButton) radioPanel.getComponent(1);

        assertEquals(Messages.getString("P2T.oldservice.title"), oldRadioButton.getText(), "Old service radio button should have text 'Algorithmus'");
        assertEquals(Messages.getString("P2T.newservice.title"), newRadioButton.getText(), "New service radio button should have text 'LLM'");

        JPanel fieldsPanel = (JPanel) components[1];
        assertTrue(fieldsPanel.getComponentCount() > 0, "Fields panel should have at least one component");

        JLabel apiKeyLabel = (JLabel) fieldsPanel.getComponent(0);
        assertNotNull(apiKeyLabel, "apiKeyLabel should not be null");
        assertEquals(Messages.getString("P2T.apikey.title") + ":", apiKeyLabel.getText(), "apiKeyLabel text should be correct");

        JTextField apiKeyField = (JTextField) fieldsPanel.getComponent(1);
        assertNotNull(apiKeyField, "apiKeyField should not be null");
        assertEquals(new Dimension(300, 25), apiKeyField.getPreferredSize(), "apiKeyField preferred size should be correct");

        JLabel promptLabel = (JLabel) fieldsPanel.getComponent(2);
        assertNotNull(promptLabel, "promptLabel should not be null");
        assertEquals(Messages.getString("P2T.prompt.title") + ":", promptLabel.getText(), "promptLabel text should be correct");

        JScrollPane promptScrollPane = (JScrollPane) fieldsPanel.getComponent(3);
        assertNotNull(promptScrollPane, "promptScrollPane should not be null");
        assertEquals(new Dimension(200, 100), promptScrollPane.getPreferredSize(), "promptScrollPane preferred size should be correct");

        JTextArea promptField = (JTextArea) promptScrollPane.getViewport().getView();
        assertNotNull(promptField, "promptField should not be null");

        assertTrue(promptField.getLineWrap(), "promptField line wrap should be enabled");
        assertTrue(promptField.getWrapStyleWord(), "promptField wrap style should be word");

        assertFalse(promptField.isEnabled(), "promptField should be disabled");
        assertEquals(5, promptField.getRows(), "promptField rows should be 5");


        JCheckBox enablePromptCheckBox = (JCheckBox) fieldsPanel.getComponent(4);
        assertNotNull(enablePromptCheckBox, "enablePromptCheckBox should not be null");
        assertFalse(enablePromptCheckBox.isSelected(), "enablePromptCheckBox should not be selected");

        JLabel gptModelLabel = (JLabel) fieldsPanel.getComponent(5);
        assertNotNull(gptModelLabel, "gptModelLabel should not be null");

        JComboBox<?> modelComboBox = (JComboBox<?>) fieldsPanel.getComponent(6);
        assertNotNull(modelComboBox, "modelComboBox should not be null");

        JButton fetchModelsButton = (JButton) fieldsPanel.getComponent(7);
        assertNotNull(fetchModelsButton, "fetchModelsButton should not be null");
        assertEquals("fetchModels", fetchModelsButton.getText(), "fetchModelsButton text should be correct");

        JCheckBox showAgainCheckBox = (JCheckBox) fieldsPanel.getComponent(8);
        assertNotNull(showAgainCheckBox, "showAgainCheckBox should not be null");
        assertEquals(Messages.getString("P2T.popup.show.again.title"), showAgainCheckBox.getText(), "showAgainCheckBox text should be correct");
        assertEquals(ConfigurationManager.getConfiguration().getGptShowAgain(), showAgainCheckBox.isSelected(), "showAgainCheckBox selected state should be correct");

        // Simulate selecting the new radio button
        newRadioButton.doClick();
        assertTrue(apiKeyLabel.isVisible(), "apiKeyLabel should be visible after selecting new service");
        assertTrue(apiKeyField.isVisible(), "apiKeyField should be visible after selecting new service");
        assertTrue(promptLabel.isVisible(), "promptLabel should be visible after selecting new service");
        assertTrue(promptScrollPane.isVisible(), "promptScrollPane should be visible after selecting new service");
        assertTrue(enablePromptCheckBox.isVisible(), "enablePromptCheckBox should be visible after selecting new service");
        assertTrue(gptModelLabel.isVisible(), "gptModelLabel should be visible after selecting new service");
        assertTrue(modelComboBox.isVisible(), "modelComboBox should be visible after selecting new service");
        assertTrue(fetchModelsButton.isVisible(), "fetchModelsButton should be visible after selecting new service");

        // Simulate selecting the old radio button
        oldRadioButton.doClick();
        assertFalse(apiKeyLabel.isVisible(), "apiKeyLabel should be hidden after selecting old service");
        assertFalse(apiKeyField.isVisible(), "apiKeyField should be hidden after selecting old service");
        assertFalse(promptLabel.isVisible(), "promptLabel should be hidden after selecting old service");
        assertFalse(promptScrollPane.isVisible(), "promptScrollPane should be hidden after selecting old service");
        assertFalse(enablePromptCheckBox.isVisible(), "enablePromptCheckBox should be hidden after selecting old service");
        assertFalse(gptModelLabel.isVisible(), "gptModelLabel should be hidden after selecting old service");
        assertFalse(modelComboBox.isVisible(), "modelComboBox should be hidden after selecting old service");
        assertFalse(fetchModelsButton.isVisible(), "fetchModelsButton should be hidden after selecting old service");

        showAgainCheckBox.doClick();
        assertFalse(showAgainCheckBox.isSelected(), "showAgainCheckBox should be unchecked after click");
        showAgainCheckBox.doClick();
        assertTrue(showAgainCheckBox.isSelected(), "showAgainCheckBox should be checked after click");

        // Test enablePromptCheckBox
        // Simulate enabling prompt editing
        enablePromptCheckBox.doClick();
        assertTrue(promptField.isEnabled(), "promptField should be enabled after checking enablePromptCheckBox");
        // Simulate disabling prompt editing
        enablePromptCheckBox.doClick();
        assertFalse(promptField.isEnabled(), "promptField should be disabled after unchecking enablePromptCheckBox");
    }


}