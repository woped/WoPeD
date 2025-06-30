package org.woped.file.p2t;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.woped.core.config.ConfigurationManager;
import org.woped.gui.translations.Messages;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class P2TUITest {

    private P2TUI p2tui;
    private MockedStatic<Messages> messagesMock;

    // This method is called before each test
    @BeforeEach
    public void setUp() {
        p2tui = new P2TUI();

        initializeMockMessages();
    }

    // This method initializes the mock for the Messages class
    private void initializeMockMessages() {
        messagesMock = mockStatic(Messages.class);
        messagesMock.when(() -> Messages.getString("P2T.openP2T.text")).thenReturn("Prozess zu Text");
        messagesMock.when(() -> Messages.getString("P2T.oldservice.title")).thenReturn("Algorithmus");
        messagesMock.when(() -> Messages.getString("P2T.newservice.title")).thenReturn("LLM");
        messagesMock.when(() -> Messages.getString("P2T.apikey.title")).thenReturn("API Schl\\u00FCssel");
        messagesMock.when(() -> Messages.getString("P2T.prompt.title")).thenReturn("Prompt");
        messagesMock.when(() -> Messages.getString("P2T.prompt.checkbox.enable.title")).thenReturn("Bearbeitung aktivieren");
        messagesMock.when(() -> Messages.getString("P2T.rag.checkbox.enable.title")).thenReturn("RAG aktivieren");
        messagesMock.when(() -> Messages.getString("P2T.get.GPTmodel.title")).thenReturn("GPT-Model:");
        messagesMock.when(() -> Messages.getString("P2T.popup.show.again.title")).thenReturn("Erneut anzeigen");
        messagesMock.when(() -> Messages.getString("P2T.fetchmodels.button")).thenReturn("Modelle laden");
        messagesMock.when(() -> Messages.getString("P2T.text")).thenReturn("Text generieren");
    }

    // This method is called after each test
    @AfterEach
    public void tearDown() {
        // Close the static mock
        messagesMock.close();
    }

    // This method tests the initialize method
    @Test
    public void testInitialize() {
        assertFalse(p2tui.isVisible(), "P2TUI should not be visible initially");
        assertTrue(p2tui.getLayout() instanceof BorderLayout, "P2TUI layout should be BorderLayout");
        assertFalse(p2tui.isUndecorated(), "P2TUI shouldn't be undecorated");
        assertTrue(p2tui.isResizable(), "P2TUI should be resizable");

        assertEquals(Messages.getString("P2T.openP2T.text"), p2tui.getTitle(), "P2TUI title should be correct");

        assertEquals(new Dimension(650, 500), p2tui.getSize(), "Dialog should have size 650x500");

    }

    // This method tests the initializeSwitchButtonPanel method
    @Test
    public void initializeSwitchButtonPanel() {
        JPanel switchButtonPanel = p2tui.initializeSwitchButtonPanel();
        assertNotNull(switchButtonPanel, "SwitchButtonPanel should not be null");
        assertTrue(switchButtonPanel.getLayout() instanceof GridBagLayout, "Layout should be GridBagLayout");

        Component[] components = switchButtonPanel.getComponents();
        assertEquals(2, components.length, "SwitchButtonPanel should have 2 components");

        JPanel radioPanel = (JPanel) components[0];
        assertEquals(2, radioPanel.getComponentCount(), "Radio panel should have 2 buttons");

        JRadioButton oldRadioButton = (JRadioButton) radioPanel.getComponent(0);
        JRadioButton newRadioButton = (JRadioButton) radioPanel.getComponent(1);

        assertEquals(Messages.getString("P2T.oldservice.title"), oldRadioButton.getText(), "Old service radio button should have correct text");
        assertEquals(Messages.getString("P2T.newservice.title"), newRadioButton.getText(), "New service radio button should have correct text");
        assertTrue(oldRadioButton.isSelected(), "Old service should be selected by default");

        JPanel fieldsPanel = (JPanel) components[1];
        assertTrue(fieldsPanel.getComponentCount() >= 10, "Fields panel should have at least 10 components");

        // Provider label and combo (components 0, 1)
        JLabel providerLabel = (JLabel) fieldsPanel.getComponent(0);
        assertNotNull(providerLabel, "providerLabel should not be null");
        assertEquals("Provider:", providerLabel.getText(), "providerLabel text should be correct");

        JComboBox<?> providerComboBox = (JComboBox<?>) fieldsPanel.getComponent(1);
        assertNotNull(providerComboBox, "providerComboBox should not be null");
        assertEquals(3, providerComboBox.getItemCount(), "Provider combo should have 3 items");
        assertEquals("openAi", providerComboBox.getItemAt(0));
        assertEquals("gemini", providerComboBox.getItemAt(1));
        assertEquals("lmStudio", providerComboBox.getItemAt(2));

        // API Key label and field (components 2, 3)
        JLabel apiKeyLabel = (JLabel) fieldsPanel.getComponent(2);
        assertNotNull(apiKeyLabel, "apiKeyLabel should not be null");
        assertEquals(Messages.getString("P2T.apikey.title") + ":", apiKeyLabel.getText(), "apiKeyLabel text should be correct");

        JTextField apiKeyField = (JTextField) fieldsPanel.getComponent(3);
        assertNotNull(apiKeyField, "apiKeyField should not be null");
        assertEquals(new Dimension(300, 25), apiKeyField.getPreferredSize(), "apiKeyField preferred size should be correct");

        // Prompt label and scroll pane (components 4, 5)
        JLabel promptLabel = (JLabel) fieldsPanel.getComponent(4);
        assertNotNull(promptLabel, "promptLabel should not be null");
        assertEquals(Messages.getString("P2T.prompt.title") + ":", promptLabel.getText(), "promptLabel text should be correct");

        JScrollPane promptScrollPane = (JScrollPane) fieldsPanel.getComponent(5);
        assertNotNull(promptScrollPane, "promptScrollPane should not be null");
        assertEquals(new Dimension(200, 100), promptScrollPane.getPreferredSize(), "promptScrollPane preferred size should be correct");

        JTextArea promptField = (JTextArea) promptScrollPane.getViewport().getView();
        assertNotNull(promptField, "promptField should not be null");
        assertTrue(promptField.getLineWrap(), "promptField line wrap should be enabled");
        assertTrue(promptField.getWrapStyleWord(), "promptField wrap style should be word");
        assertFalse(promptField.isEnabled(), "promptField should be disabled initially");
        assertEquals(5, promptField.getRows(), "promptField rows should be 5");

        // Enable prompt checkbox (component 6)
        JCheckBox enablePromptCheckBox = (JCheckBox) fieldsPanel.getComponent(6);
        assertNotNull(enablePromptCheckBox, "enablePromptCheckBox should not be null");
        assertFalse(enablePromptCheckBox.isSelected(), "enablePromptCheckBox should not be selected initially");
        assertEquals(Messages.getString("P2T.prompt.checkbox.enable.title"), enablePromptCheckBox.getText(), "enablePromptCheckBox text should be correct");

        // RAG enabled checkbox (component 7)
        JCheckBox ragEnabledCheckBox = (JCheckBox) fieldsPanel.getComponent(7);
        assertNotNull(ragEnabledCheckBox, "ragEnabledCheckBox should not be null");
        assertEquals(Messages.getString("P2T.rag.checkbox.enable.title"), ragEnabledCheckBox.getText(), "ragEnabledCheckBox text should be correct");
        assertEquals(ConfigurationManager.getConfiguration().getRagOption(), ragEnabledCheckBox.isSelected(), "ragEnabledCheckBox initial state should match configuration");

        // GPT Model label and combo (components 8, 9)
        JLabel gptModelLabel = (JLabel) fieldsPanel.getComponent(8);
        assertNotNull(gptModelLabel, "gptModelLabel should not be null");
        assertEquals(Messages.getString("P2T.get.GPTmodel.title"), gptModelLabel.getText(), "gptModelLabel text should be correct");

        JComboBox<?> modelComboBox = (JComboBox<?>) fieldsPanel.getComponent(9);
        assertNotNull(modelComboBox, "modelComboBox should not be null");

        // Fetch models button (component 10)
        JButton fetchModelsButton = (JButton) fieldsPanel.getComponent(10);
        assertNotNull(fetchModelsButton, "fetchModelsButton should not be null");
        assertEquals(Messages.getString("P2T.fetchmodels.button"), fetchModelsButton.getText(), "fetchModelsButton text should be correct");

        // Show again checkbox (component 11)
        JCheckBox showAgainCheckBox = (JCheckBox) fieldsPanel.getComponent(11);
        assertNotNull(showAgainCheckBox, "showAgainCheckBox should not be null");
        assertEquals(Messages.getString("P2T.popup.show.again.title"), showAgainCheckBox.getText(), "showAgainCheckBox text should be correct");
        assertEquals(ConfigurationManager.getConfiguration().getGptShowAgain(), showAgainCheckBox.isSelected(), "showAgainCheckBox selected state should be correct");

        // Test initial visibility (LLM fields should be hidden)
        assertFalse(providerLabel.isVisible(), "providerLabel should be hidden initially");
        assertFalse(providerComboBox.isVisible(), "providerComboBox should be hidden initially");
        assertFalse(apiKeyLabel.isVisible(), "apiKeyLabel should be hidden initially");
        assertFalse(apiKeyField.isVisible(), "apiKeyField should be hidden initially");
        assertFalse(promptLabel.isVisible(), "promptLabel should be hidden initially");
        assertFalse(promptScrollPane.isVisible(), "promptScrollPane should be hidden initially");
        assertFalse(enablePromptCheckBox.isVisible(), "enablePromptCheckBox should be hidden initially");
        assertFalse(ragEnabledCheckBox.isVisible(), "ragEnabledCheckBox should be hidden initially");
        assertFalse(gptModelLabel.isVisible(), "gptModelLabel should be hidden initially");
        assertFalse(modelComboBox.isVisible(), "modelComboBox should be hidden initially");
        assertFalse(fetchModelsButton.isVisible(), "fetchModelsButton should be hidden initially");
        assertTrue(showAgainCheckBox.isVisible(), "showAgainCheckBox should always be visible");

        // Test switching to new service (LLM)
        newRadioButton.doClick();
        assertTrue(providerLabel.isVisible(), "providerLabel should be visible after selecting LLM");
        assertTrue(providerComboBox.isVisible(), "providerComboBox should be visible after selecting LLM");
        assertTrue(apiKeyLabel.isVisible(), "apiKeyLabel should be visible after selecting LLM");
        assertTrue(apiKeyField.isVisible(), "apiKeyField should be visible after selecting LLM");
        assertTrue(promptLabel.isVisible(), "promptLabel should be visible after selecting LLM");
        assertTrue(promptScrollPane.isVisible(), "promptScrollPane should be visible after selecting LLM");
        assertTrue(enablePromptCheckBox.isVisible(), "enablePromptCheckBox should be visible after selecting LLM");
        assertTrue(ragEnabledCheckBox.isVisible(), "ragEnabledCheckBox should be visible after selecting LLM");
        assertTrue(gptModelLabel.isVisible(), "gptModelLabel should be visible after selecting LLM");
        assertTrue(modelComboBox.isVisible(), "modelComboBox should be visible after selecting LLM");
        assertTrue(fetchModelsButton.isVisible(), "fetchModelsButton should be visible after selecting LLM");

        // Test switching back to old service (Algorithm)
        oldRadioButton.doClick();
        assertFalse(providerLabel.isVisible(), "providerLabel should be hidden after selecting Algorithm");
        assertFalse(providerComboBox.isVisible(), "providerComboBox should be hidden after selecting Algorithm");
        assertFalse(apiKeyLabel.isVisible(), "apiKeyLabel should be hidden after selecting Algorithm");
        assertFalse(apiKeyField.isVisible(), "apiKeyField should be hidden after selecting Algorithm");
        assertFalse(promptLabel.isVisible(), "promptLabel should be hidden after selecting Algorithm");
        assertFalse(promptScrollPane.isVisible(), "promptScrollPane should be hidden after selecting Algorithm");
        assertFalse(enablePromptCheckBox.isVisible(), "enablePromptCheckBox should be hidden after selecting Algorithm");
        assertFalse(ragEnabledCheckBox.isVisible(), "ragEnabledCheckBox should be hidden after selecting Algorithm");
        assertFalse(gptModelLabel.isVisible(), "gptModelLabel should be hidden after selecting Algorithm");
        assertFalse(modelComboBox.isVisible(), "modelComboBox should be hidden after selecting Algorithm");
        assertFalse(fetchModelsButton.isVisible(), "fetchModelsButton should be hidden after selecting Algorithm");

        // Test showAgainCheckBox toggle
        showAgainCheckBox.doClick();
        assertFalse(showAgainCheckBox.isSelected(), "showAgainCheckBox should be unchecked after click");
        showAgainCheckBox.doClick();
        assertTrue(showAgainCheckBox.isSelected(), "showAgainCheckBox should be checked after click");

        // Switch back to LLM for prompt editing test
        newRadioButton.doClick();
        
        // Test enablePromptCheckBox functionality
        enablePromptCheckBox.doClick();
        assertTrue(promptField.isEnabled(), "promptField should be enabled after checking enablePromptCheckBox");
        enablePromptCheckBox.doClick();
        assertFalse(promptField.isEnabled(), "promptField should be disabled after unchecking enablePromptCheckBox");

        // Test ragEnabledCheckBox functionality
        boolean initialRagState = ragEnabledCheckBox.isSelected();
        ragEnabledCheckBox.doClick();
        assertEquals(!initialRagState, ragEnabledCheckBox.isSelected(), "ragEnabledCheckBox state should toggle after click");
        assertEquals(ragEnabledCheckBox.isSelected(), ConfigurationManager.getConfiguration().getRagOption(), "Configuration should be updated after ragEnabledCheckBox click");
        
        // Toggle back to verify state is persisted
        ragEnabledCheckBox.doClick();
        assertEquals(initialRagState, ragEnabledCheckBox.isSelected(), "ragEnabledCheckBox state should return to initial after second click");
        assertEquals(ragEnabledCheckBox.isSelected(), ConfigurationManager.getConfiguration().getRagOption(), "Configuration should be reverted after second ragEnabledCheckBox click");
    }

    // This method tests the initializeButtonPanel method
    @Test
    public void testInitializeSingleButtonPanel(){
        JPanel buttonPanel = p2tui.initializeSingleButtonPanel();
        assertNotNull(buttonPanel, "ButtonPanel should not be null");
        assertTrue(buttonPanel.getLayout() instanceof BorderLayout, "ButtonPanel layout should be BorderLayout");

        JButton singleButton = (JButton) buttonPanel.getComponent(0);
        assertNotNull(singleButton, "SingleButton should not be null");
        assertTrue(singleButton.getMnemonic() == KeyEvent.VK_A, "SingleButton mnemonic should be correct");
        assertEquals(Messages.getString("P2T.text"), singleButton.getText(), "SingleButton text should be correct");
    }

    // This method tests the isAPIKeyValid method with an invalid key
    @Test
    public void testIsAPIKeyValid_withInvalidKey() {
        String invalidApiKey = "invalidApiKey";

        boolean isValid = P2TUI.isAPIKeyValid(invalidApiKey);

        assertFalse(isValid, "Invalid API key should return false");
    }

    @Test
    public void testIsOpenAiAPIKeyValid_withInvalidKey() {
        String invalidApiKey = "invalidApiKey";
        boolean isValid = P2TUI.isOpenAiAPIKeyValid(invalidApiKey);
        assertFalse(isValid, "Invalid OpenAI API key should return false");
    }

    @Test
    public void testIsGeminiAPIKeyValid_withInvalidKey() {
        String invalidApiKey = "invalidApiKey";
        boolean isValid = P2TUI.isGeminiAPIKeyValid(invalidApiKey);
        assertFalse(isValid, "Invalid Gemini API key should return false");
    }
}