package org.woped.file.p2t;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import javax.swing.*;

import org.json.simple.parser.ParseException;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.ViewEvent;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.Constants;
import org.woped.editor.action.WoPeDAction;
import org.woped.editor.controller.ActionFactory;
import org.woped.editor.tools.ApiHelper;
import org.woped.gui.translations.Messages;

/**
 * P2TUI
 * <p>
 *     The P2TUI class is a dialog to select the P2T service.
 */
public class P2TUI extends JDialog {

    private JDialog loadDialog;
    private JTextField apiKeyField;
    private JTextArea promptField;
    private JCheckBox enablePromptCheckBox;
    private JCheckBox showAgainCheckBox;
    private JRadioButton newRadioButton = null;
    private JRadioButton oldRadioButton = null;
    private JComboBox<String> modelComboBox;
    private JComboBox<String> providerComboBox;
    private JLabel apiKeyLabel;
    private JLabel promptLabel;
    private JLabel gptModelLabel;
    private JLabel providerLabel; 
    private JButton fetchModelsButton;
    private JScrollPane promptScrollPane;

    private static final String DEFAULT_PROMPT = Messages.getString("P2T.prompt.text");
    private static final String PROVIDER_OPENAI = "openAi";
    private static final String PROVIDER_GEMINI = "gemini";
    private static final String PROVIDER_LMSTUDIO = "lmStudio";

    public P2TUI() {
        initialize();
    }

    public P2TUI(AbstractApplicationMediator mediator) {
        this(null, mediator);
    }

    public P2TUI(Frame owner, AbstractApplicationMediator mediator) throws HeadlessException {
        super(owner, true);
        initialize();
    }

    /**
     * Initialize the dialog
     */
    void initialize() {
        this.setVisible(false);
        this.getContentPane().setLayout(new BorderLayout());
        this.setUndecorated(false);
        this.setResizable(true);
        this.setTitle(Messages.getString("P2T.openP2T.text"));

        // Add switch button panel to the top
        this.getContentPane().add(initializeSwitchButtonPanel(), BorderLayout.NORTH);

        // Add a single button to the bottom center
        this.getContentPane().add(initializeSingleButtonPanel(), BorderLayout.SOUTH);

        this.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - this.getWidth()) / 3, (screenSize.height - this.getHeight()) / 3);

        Dimension size = new Dimension(650, 500);
        this.setSize(size);

        LoggerManager.info(Constants.EDITOR_LOGGER, "P2TUI initialized");
    }

    /**
     * Initialize the switch button panel with provider selection
     * <p>
     *     The switch button panel contains a radio button group to switch between the old and new services.
     *     The new service requires an API key and a prompt text.
     *     The prompt text is disabled by default and can be enabled by checking the enable prompt checkbox.
     *     The prompt text is a text area with a default text.
     *     The panel also contains a JComboBox to select the GPT model.
     *     The panel also contains a button to fetch the available models.
     *     The panel also contains a checkbox to show the popup again.
     *     The panel is initially hidden.
     * @return JPanel containing the switch button panel
     */
    JPanel initializeSwitchButtonPanel() {
        JPanel switchButtonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel radioPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcRadio = new GridBagConstraints();

        oldRadioButton = new JRadioButton(Messages.getString("P2T.oldservice.title"));
        newRadioButton = new JRadioButton(Messages.getString("P2T.newservice.title"));
        ButtonGroup group = new ButtonGroup();
        group.add(oldRadioButton);
        group.add(newRadioButton);

        gbcRadio.gridx = 0;
        gbcRadio.gridy = 0;
        gbcRadio.insets = new Insets(5, 5, 5, 5);
        radioPanel.add(oldRadioButton, gbcRadio);

        gbcRadio.gridx = 1;
        gbcRadio.insets = new Insets(5, 0, 5, 5);
        radioPanel.add(newRadioButton, gbcRadio);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;
        switchButtonPanel.add(radioPanel, gbc);

        JPanel fieldsPanel = new JPanel(new GridBagLayout());

        // Provider selection
        providerLabel = new JLabel("Provider:"); // Zu Klassenfeld machen
        providerComboBox = new JComboBox<>();
        providerComboBox.addItem(PROVIDER_OPENAI);
        providerComboBox.addItem(PROVIDER_GEMINI);
        providerComboBox.addItem(PROVIDER_LMSTUDIO);
        providerComboBox.setPreferredSize(new Dimension(150, 25));

        apiKeyLabel = new JLabel(Messages.getString("P2T.apikey.title") + ":");
        apiKeyField = new JTextField();
        apiKeyField.setPreferredSize(new Dimension(300, 25));

        promptLabel = new JLabel(Messages.getString("P2T.prompt.title") + ":");
        promptField = new JTextArea(DEFAULT_PROMPT);
        promptField.setLineWrap(true);
        promptField.setWrapStyleWord(true);
        promptField.setRows(5);
        promptField.setEnabled(false);

        promptField.setText(ConfigurationManager.getConfiguration().getGptPrompt());

        promptScrollPane = new JScrollPane(promptField);
        promptScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        promptScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        promptScrollPane.setPreferredSize(new Dimension(200, 100));

        enablePromptCheckBox = new JCheckBox(Messages.getString("P2T.prompt.checkbox.enable.title"));
        enablePromptCheckBox.setSelected(false);
        enablePromptCheckBox.addActionListener(e -> {
            promptField.setEnabled(enablePromptCheckBox.isSelected());
            LoggerManager.info(Constants.EDITOR_LOGGER, "Prompt Editing " + (enablePromptCheckBox.isSelected() ? "Enabled" : "Disabled"));
        });

        // Model selection
        gptModelLabel = new JLabel(Messages.getString("P2T.get.GPTmodel.title"));
        modelComboBox = new JComboBox<>();
        modelComboBox.setPreferredSize(new Dimension(150, 25));

        // Fetch models button
        fetchModelsButton = new JButton(Messages.getString("P2T.fetchmodels.button"));
        fetchModelsButton.setPreferredSize(new Dimension(120, 25));
        fetchModelsButton.addActionListener(e -> fetchAndFillModels());

        showAgainCheckBox = new JCheckBox(Messages.getString("P2T.popup.show.again.title"));
        showAgainCheckBox.setToolTipText("Durch das Entfernen dieses Hakens wird das Popup-Fenster nicht erneut angezeigt");
        showAgainCheckBox.setSelected(ConfigurationManager.getConfiguration().getGptShowAgain());

        // Initialize visibility - hide LLM fields initially
        setLLMFieldsVisibility(false);

        // Provider selection listener
        providerComboBox.addActionListener(e -> updateProviderDependentFields());

        // Radio button listeners
        newRadioButton.addActionListener(e -> {
            setLLMFieldsVisibility(true);
            updateProviderDependentFields();
            
            // Load saved provider
            String savedProvider = ConfigurationManager.getConfiguration().getLlmProvider();
            if (savedProvider != null && !savedProvider.isEmpty()) {
                providerComboBox.setSelectedItem(savedProvider);
            }
            
            LoggerManager.info(Constants.EDITOR_LOGGER, "LLM Service Selected");
        });

        oldRadioButton.addActionListener(e -> {
            setLLMFieldsVisibility(false);
            LoggerManager.info(Constants.EDITOR_LOGGER, "Algorithm Service Selected");
        });

        oldRadioButton.setSelected(true);
        setLLMFieldsVisibility(false); // Hierhin verschieben

        // Layout components
        int row = 0;
        
        // Provider row
        gbc.gridx = 0; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        fieldsPanel.add(providerLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(providerComboBox, gbc);

        // API Key row
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        fieldsPanel.add(apiKeyLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(apiKeyField, gbc);

        // Prompt row
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        fieldsPanel.add(promptLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        fieldsPanel.add(promptScrollPane, gbc);

        // Enable prompt checkbox row
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(enablePromptCheckBox, gbc);

        // Model selection row
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(gptModelLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(modelComboBox, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.5;
        fieldsPanel.add(fetchModelsButton, gbc);

        // Show again checkbox row
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        fieldsPanel.add(showAgainCheckBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 0, 0);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        switchButtonPanel.add(fieldsPanel, gbc);

        return switchButtonPanel;
    }

    /**
     * Set visibility of LLM-specific fields
     */
    private void setLLMFieldsVisibility(boolean visible) {
        // Alle Komponenten direkt referenzieren
        if (providerLabel != null) {
            providerLabel.setVisible(visible);
        }
        if (providerComboBox != null) {
            providerComboBox.setVisible(visible);
        }
        if (apiKeyLabel != null) {
            apiKeyLabel.setVisible(visible);
        }
        if (apiKeyField != null) {
            apiKeyField.setVisible(visible);
        }
        if (promptLabel != null) {
            promptLabel.setVisible(visible);
        }
        if (promptScrollPane != null) {
            promptScrollPane.setVisible(visible);
        }
        if (enablePromptCheckBox != null) {
            enablePromptCheckBox.setVisible(visible);
        }
        if (gptModelLabel != null) {
            gptModelLabel.setVisible(visible);
        }
        if (modelComboBox != null) {
            modelComboBox.setVisible(visible);
        }
        if (fetchModelsButton != null) {
            fetchModelsButton.setVisible(visible);
        }
        if (showAgainCheckBox != null) {
            showAgainCheckBox.setVisible(true); // Always visible
        }
        
        // UI aktualisieren
        if (this.isDisplayable()) {
            revalidate();
            repaint();
        }
    }

    /**
     * Update fields based on selected provider
     */
    private void updateProviderDependentFields() {
        String selectedProvider = (String) providerComboBox.getSelectedItem();
        boolean needsApiKey = !PROVIDER_LMSTUDIO.equals(selectedProvider);
        
        apiKeyLabel.setVisible(needsApiKey);
        apiKeyField.setVisible(needsApiKey);
        
        if (needsApiKey) {
            // Load provider-specific API key
            if (PROVIDER_OPENAI.equals(selectedProvider)) {
                apiKeyField.setText(ConfigurationManager.getConfiguration().getGptApiKey());
            } else if (PROVIDER_GEMINI.equals(selectedProvider)) {
                apiKeyField.setText(ConfigurationManager.getConfiguration().getGptApiKey());
            }
        }
        
        // Clear and enable model selection for all providers
        modelComboBox.removeAllItems();
        modelComboBox.setEnabled(true);
        
        revalidate();
        repaint();
    }

    /**
     * Initialize the single button panel
     */
    JPanel initializeSingleButtonPanel() {
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton singleButton = new JButton();
        singleButton.setMnemonic(KeyEvent.VK_A);
        singleButton.setText(Messages.getString("P2T.text"));
        buttonPanel.add(singleButton, BorderLayout.CENTER);

        singleButton.addActionListener(e -> {
            if (newRadioButton.isSelected()) {
                String selectedProvider = (String) providerComboBox.getSelectedItem();
                
                // Validate API key for providers that need it
                if (!PROVIDER_LMSTUDIO.equals(selectedProvider) && !validateAPIKey()) {
                    return;
                }

                // Save configuration
                saveConfiguration();
                executeAction();
                dispose();
            } else {
                ConfigurationManager.getConfiguration().setGptUseNew(false);
                if (!showAgainCheckBox.isSelected()) {
                    ConfigurationManager.getConfiguration().setGptShowAgain(false);
                }
                executeAction();
                dispose();
            }
        });
        
        return buttonPanel;
    }

    /**
     * Save current configuration
     */
    private void saveConfiguration() {
        String selectedProvider = (String) providerComboBox.getSelectedItem();
        
        // Save provider-specific API key
        if (PROVIDER_OPENAI.equals(selectedProvider)) {
            ConfigurationManager.getConfiguration().setGptApiKey(apiKeyField.getText());
        } else if (PROVIDER_GEMINI.equals(selectedProvider)) {
            ConfigurationManager.getConfiguration().setGptApiKey(apiKeyField.getText());
        }
        
        // Save other settings
        ConfigurationManager.getConfiguration().setLlmProvider(selectedProvider);
        ConfigurationManager.getConfiguration().setGptPrompt(promptField.getText());
        ConfigurationManager.getConfiguration().setGptUseNew(true);
        
        if (modelComboBox.getSelectedItem() != null) {
            ConfigurationManager.getConfiguration().setGptModel(modelComboBox.getSelectedItem().toString());
        }
        
        if (!showAgainCheckBox.isSelected()) {
            ConfigurationManager.getConfiguration().setGptShowAgain(false);
        }
    }

    /**
     * Execute the action
     */
    void executeAction() {
        WoPeDAction action = ActionFactory.getStaticAction(ActionFactory.ACTIONID_P2T_OLD);
        action.actionPerformed(new ViewEvent(this, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.P2T, null));
    }

    /**
     * Fetch and fill the models based on selected provider
     */
    void fetchAndFillModels() {
        String selectedProvider = (String) providerComboBox.getSelectedItem();
        
        new Thread(() -> {
            try {
                List<String> models;
                
                if (PROVIDER_LMSTUDIO.equals(selectedProvider)) {
                    models = ApiHelper.fetchModels(null, selectedProvider);
                } else if (PROVIDER_OPENAI.equals(selectedProvider)) {
                    models = ApiHelper.fetchModels(apiKeyField.getText(), selectedProvider);
                } else if (PROVIDER_GEMINI.equals(selectedProvider)) {
                    models = ApiHelper.fetchModels(apiKeyField.getText(), selectedProvider);
                } else {
                    throw new IOException("Unsupported provider: " + selectedProvider);
                }
                
                SwingUtilities.invokeLater(() -> {
                    modelComboBox.removeAllItems();
                    for (String model : models) {
                        modelComboBox.addItem(model);
                    }
                    String savedModel = ConfigurationManager.getConfiguration().getGptModel();
                    if (savedModel != null) {
                        modelComboBox.setSelectedItem(savedModel);
                    }
                });
            } catch (IOException | ParseException e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, 
                        "Failed to fetch models: " + e.getMessage(), 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    /**
     * Validate the API key based on selected provider
     */
    boolean validateAPIKey() {
        String selectedProvider = (String) providerComboBox.getSelectedItem();
        String apiKey = apiKeyField.getText();
        
        if (apiKey == null || apiKey.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "API Key is required for " + selectedProvider, 
                "Missing API Key", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        boolean apiKeyValid = false;
        
        if (PROVIDER_OPENAI.equals(selectedProvider)) {
            apiKeyValid = isOpenAiAPIKeyValid(apiKey);
        } else if (PROVIDER_GEMINI.equals(selectedProvider)) {
            apiKeyValid = isGeminiAPIKeyValid(apiKey);
        }
        
        if (!apiKeyValid) {
            JOptionPane.showMessageDialog(this, 
                Messages.getString("P2T.apikey.invalid"), 
                Messages.getString("P2T.apikey.invalid.title"), 
                JOptionPane.ERROR_MESSAGE);
        }
        
        return apiKeyValid;
    }

    /**
     * Check if OpenAI API key is valid
     */
    public static boolean isOpenAiAPIKeyValid(String apiKey) {
        final String TEST_URL = "https://api.openai.com/v1/models";
        try {
            URL url = new URL(TEST_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);

            int responseCode = connection.getResponseCode();
            return responseCode == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if Gemini API key is valid
     */
    public static boolean isGeminiAPIKeyValid(String apiKey) {
        final String TEST_URL = "https://generativelanguage.googleapis.com/v1beta/models?key=" + apiKey;
        try {
            URL url = new URL(TEST_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            return responseCode == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Legacy method for backward compatibility
     */
    public static boolean isAPIKeyValid(String apiKey) {
        return isOpenAiAPIKeyValid(apiKey);
    }
}
