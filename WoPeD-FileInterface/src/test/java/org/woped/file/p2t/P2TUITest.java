package org.woped.file.p2t;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.woped.core.config.ConfigurationManager;
import org.woped.gui.translations.Messages;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class P2TUITest {

    private P2TUI p2tui;
    private MockedStatic<Messages> messagesMock;

    @BeforeEach
    public void setUp() {
        initializeMockMessages();
        p2tui = new P2TUI();
    }

    private void initializeMockMessages() {
        messagesMock = mockStatic(Messages.class);
        messagesMock.when(() -> Messages.getString("P2T.openP2T.text")).thenReturn("Prozess zu Text");
        messagesMock.when(() -> Messages.getString("P2T.newservice.title")).thenReturn("LLM");
        messagesMock.when(() -> Messages.getString("P2T.provider.title")).thenReturn("Anbieter");
        messagesMock.when(() -> Messages.getString("P2T.apikey.title")).thenReturn("API Schl\\u00FCssel");
        messagesMock.when(() -> Messages.getString("P2T.prompt.title")).thenReturn("Prompt");
        messagesMock.when(() -> Messages.getString("P2T.prompt.text")).thenReturn("Default prompt");
        messagesMock.when(() -> Messages.getString("P2T.prompt.checkbox.enable.title")).thenReturn("Bearbeitung aktivieren");
        messagesMock.when(() -> Messages.getString("P2T.rag.checkbox.enable.title")).thenReturn("RAG aktivieren");
        messagesMock.when(() -> Messages.getString("P2T.get.GPTmodel.title")).thenReturn("GPT-Model:");
        messagesMock.when(() -> Messages.getString("P2T.popup.show.again.title")).thenReturn("Erneut anzeigen");
        messagesMock.when(() -> Messages.getString("P2T.popup.tool.tip.text")).thenReturn("Tooltip");
        messagesMock.when(() -> Messages.getString("P2T.fetchmodels.button")).thenReturn("Modelle laden");
        messagesMock.when(() -> Messages.getString("P2T.text")).thenReturn("Text generieren");
    }

    @AfterEach
    public void tearDown() {
        messagesMock.close();
    }

    @Test
    public void testInitialize() {
        assertFalse(p2tui.isVisible(), "P2TUI should not be visible initially");
        assertTrue(p2tui.getLayout() instanceof BorderLayout, "P2TUI layout should be BorderLayout");
        assertFalse(p2tui.isUndecorated(), "P2TUI shouldn't be undecorated");
        assertTrue(p2tui.isResizable(), "P2TUI should be resizable");
        assertEquals(Messages.getString("P2T.openP2T.text"), p2tui.getTitle(), "P2TUI title should be correct");
        assertEquals(new Dimension(650, 500), p2tui.getSize(), "Dialog should have size 650x500");
    }

    @Test
    public void initializeSettingsPanel() {
        JPanel settingsPanel = p2tui.initializeSettingsPanel();
        assertNotNull(settingsPanel, "SettingsPanel should not be null");
        assertTrue(settingsPanel.getLayout() instanceof GridBagLayout, "Layout should be GridBagLayout");
        assertEquals(1, settingsPanel.getComponentCount(), "SettingsPanel should contain the fields panel");

        JPanel fieldsPanel = (JPanel) settingsPanel.getComponent(0);
        assertTrue(fieldsPanel.getComponentCount() >= 10, "Fields panel should have at least 10 components");

        JLabel providerLabel = (JLabel) fieldsPanel.getComponent(0);
        assertEquals(Messages.getString("P2T.provider.title") + ":", providerLabel.getText());

        JComboBox<?> providerComboBox = (JComboBox<?>) fieldsPanel.getComponent(1);
        assertEquals(3, providerComboBox.getItemCount(), "Provider combo should have 3 items");

        JLabel apiKeyLabel = (JLabel) fieldsPanel.getComponent(2);
        assertEquals(Messages.getString("P2T.apikey.title") + ":", apiKeyLabel.getText());

        JTextField apiKeyField = (JTextField) fieldsPanel.getComponent(3);
        assertEquals(new Dimension(300, 25), apiKeyField.getPreferredSize());

        JScrollPane promptScrollPane = (JScrollPane) fieldsPanel.getComponent(5);
        JTextArea promptField = (JTextArea) promptScrollPane.getViewport().getView();
        assertFalse(promptField.isEnabled(), "promptField should be disabled initially");

        JCheckBox enablePromptCheckBox = (JCheckBox) fieldsPanel.getComponent(6);
        enablePromptCheckBox.doClick();
        assertTrue(promptField.isEnabled(), "promptField should be enabled after checking enablePromptCheckBox");

        JCheckBox ragEnabledCheckBox = (JCheckBox) fieldsPanel.getComponent(7);
        boolean initialRagState = ragEnabledCheckBox.isSelected();
        ragEnabledCheckBox.doClick();
        assertEquals(!initialRagState, ragEnabledCheckBox.isSelected());
        assertEquals(ragEnabledCheckBox.isSelected(), ConfigurationManager.getConfiguration().getRagOption());

        JCheckBox showAgainCheckBox = (JCheckBox) fieldsPanel.getComponent(11);
        assertEquals(
                ConfigurationManager.getConfiguration().getGptShowAgain(), showAgainCheckBox.isSelected());
    }

    @Test
    public void testInitializeSingleButtonPanel() {
        JPanel buttonPanel = p2tui.initializeSingleButtonPanel();
        assertNotNull(buttonPanel, "ButtonPanel should not be null");
        assertTrue(buttonPanel.getLayout() instanceof BorderLayout, "ButtonPanel layout should be BorderLayout");

        JButton singleButton = (JButton) buttonPanel.getComponent(0);
        assertTrue(singleButton.getMnemonic() == KeyEvent.VK_A, "SingleButton mnemonic should be correct");
        assertEquals(Messages.getString("P2T.text"), singleButton.getText(), "SingleButton text should be correct");
    }

    @Test
    public void testIsAPIKeyValid_withInvalidKey() {
        assertFalse(P2TUI.isAPIKeyValid("invalidApiKey"), "Invalid API key should return false");
    }

    @Test
    public void testIsOpenAiAPIKeyValid_withInvalidKey() {
        assertFalse(P2TUI.isOpenAiAPIKeyValid("invalidApiKey"), "Invalid OpenAI API key should return false");
    }

    @Test
    public void testIsGeminiAPIKeyValid_withInvalidKey() {
        assertFalse(P2TUI.isGeminiAPIKeyValid("invalidApiKey"), "Invalid Gemini API key should return false");
    }
}
