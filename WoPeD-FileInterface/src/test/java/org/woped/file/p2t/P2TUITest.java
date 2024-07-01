package org.woped.file.p2t;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.woped.core.controller.AbstractApplicationMediator;

import javax.swing.*;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class P2TUITest {
    private P2TUI p2tui;
    @Mock
    private JTextField mockApiKeyField;
    @Mock
    private AbstractApplicationMediator mockMediator;

    @BeforeEach
    public void setUp() throws IOException {
        mockMediator = mock(AbstractApplicationMediator.class);
        mockApiKeyField = mock(JTextField.class);
        p2tui = new P2TUI(mockMediator);
        p2tui.apiKeyField = mockApiKeyField;

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

    @Test
    public void testValidateAPIKey_withValidKey() {
        // Stubbing behavior of mockApiKeyField
        when(mockApiKeyField.getText()).thenReturn("your_actual_valid_api_key");
        // Call the method you want to test
        boolean isValid = p2tui.validateAPIKey();

        // Assert the expected outcome
        assertTrue(isValid);
    }

    @Test
    public void testValidateAPIKey_withInvalidKey() {
        // Stubbing behavior of mockApiKeyField
        when(mockApiKeyField.getText()).thenReturn("invalidApiKey");
        // Call the method you want to test
        boolean isValid = p2tui.validateAPIKey();

        // Assert the expected outcome
        assertFalse(isValid);
    }





}

