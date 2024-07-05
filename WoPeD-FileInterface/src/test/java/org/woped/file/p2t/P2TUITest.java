package org.woped.file.p2t;

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

    @Test
    public void testInitialize() {
        assertFalse(p2tui.isVisible(), "P2TUI should be visible");
        assertTrue(p2tui.getLayout() instanceof BorderLayout, "P2TUI layout should be BorderLayout");
        assertFalse(p2tui.isUndecorated(), "P2TUI shouldn't be undecorated");
        assertTrue(p2tui.isResizable(), "P2TUI should be resizable");

        assertEquals(Messages.getString("P2T.openP2T.text"), p2tui.getTitle(), "P2TUI title should be 'Open P2T'");

        // Überprüfe die Größe des Dialogs
        assertEquals(new Dimension(600, 375), p2tui.getSize(), "Dialog should have size 600x375");

    }
i}