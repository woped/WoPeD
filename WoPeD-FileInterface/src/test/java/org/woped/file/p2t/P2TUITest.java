package org.woped.file.p2t;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.woped.core.controller.AbstractApplicationMediator;

import javax.swing.*;

import java.awt.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class P2TUITest {
    private P2TUI p2tui;
    private JTextField apiKeyField;

    @Before
    public void setUp() {
        AbstractApplicationMediator mockMediator = mock(AbstractApplicationMediator.class);
        p2tui = new P2TUI(mockMediator);
        apiKeyField = mock(JTextField.class);
        p2tui.apiKeyField = apiKeyField;
    }

    @Test
    public void testIsAPIKeyValid_withValidKey() {
        String validApiKey = "your_actual_valid_api_key";

        boolean isValid = p2tui.isAPIKeyValid(validApiKey);

        assertTrue(isValid);
    }

    @Test
    public void testIsAPIKeyValid_withInvalidKey() {
        String invalidApiKey = "invalidApiKey";

        boolean isValid = p2tui.isAPIKeyValid(invalidApiKey);

        assertFalse(isValid);
    }

}

