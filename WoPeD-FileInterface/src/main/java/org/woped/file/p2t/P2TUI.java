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

public class P2TUI extends JDialog {

    private JDialog loadDialog;
    private JTextField apiKeyField;
    private JTextArea promptField;
    private JCheckBox enablePromptCheckBox;
    private JCheckBox showAgainCheckBox;
    private JRadioButton newRadioButton = null;
    private JRadioButton oldRadioButton = null;
    JComboBox<String> modelComboBox;

    private static final String DEFAULT_PROMPT = Messages.getString("P2T.prompt.text");

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

    public Object getApiKeyField() {
        return apiKeyField;
    }

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

        Dimension size = new Dimension(600, 375);
        this.setSize(size);

        LoggerManager.info(Constants.EDITOR_LOGGER, "P2TUI initialized");

    }

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

        JLabel apiKeyLabel = new JLabel(Messages.getString("P2T.apikey.title") + ":");
        apiKeyField = new JTextField();
        apiKeyField.setPreferredSize(new Dimension(300, 25));

        JLabel promptLabel = new JLabel(Messages.getString("P2T.prompt.title") + ":");
        promptField = new JTextArea(DEFAULT_PROMPT);
        promptField.setLineWrap(true);
        promptField.setWrapStyleWord(true);
        promptField.setRows(5);
        promptField.setEnabled(false);

        promptField.setText(ConfigurationManager.getConfiguration().getGptPrompt());

        JScrollPane promptScrollPane = new JScrollPane(promptField);
        promptScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        promptScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        promptScrollPane.setPreferredSize(new Dimension(200, 100));

        enablePromptCheckBox = new JCheckBox(Messages.getString("P2T.prompt.checkbox.enable.title"));
        enablePromptCheckBox.setSelected(false);
        enablePromptCheckBox.addActionListener(e -> {
            promptField.setEnabled(enablePromptCheckBox.isSelected());
            if (!enablePromptCheckBox.isSelected()) {
                promptField.revalidate();
                LoggerManager.info(Constants.EDITOR_LOGGER, "Prompt Editing Disabled");
            } else {
                LoggerManager.info(Constants.EDITOR_LOGGER, "Prompt Editing Enabled");
            }

        });

        // Add JComboBox
        JLabel gptModelLabel = new JLabel(Messages.getString("P2T.get.GPTmodel.title"));
        gptModelLabel.setVisible(false); // Initially hidden
        modelComboBox = new JComboBox<>();
        modelComboBox.setPreferredSize(new Dimension(150, 25));
        modelComboBox.setVisible(false); // Initially hidden

        // Add fetchModels Button
        JButton fetchModelsButton = new JButton("fetchModels");
        fetchModelsButton.setPreferredSize(new Dimension(120, 25));
        fetchModelsButton.setVisible(false); // Initially hidden
        fetchModelsButton.addActionListener(new ActionListener() {
                                                public void actionPerformed(ActionEvent e) {
                                                    fetchAndFillModels();
                                                }
                                            }
        );

        showAgainCheckBox = new JCheckBox(Messages.getString("P2T.popup.show.again.title"));
        showAgainCheckBox.setToolTipText("Durch das Entfernen dieses Hakens wird das Popup-Fenster nicht erneut angezeigt, der Client merkt sich jedoch den zuletzt ausgewählten Modus," +
                "unter den NLP Einstellungen kann das Fenster wieder aktiviert werden");
        showAgainCheckBox.setSelected(ConfigurationManager.getConfiguration().getGptShowAgain());
        showAgainCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showAgainCheckBox.isSelected()) {
                    LoggerManager.info(Constants.EDITOR_LOGGER,"Show Again Checkbox Selected");
                }
                else {
                    LoggerManager.info(Constants.EDITOR_LOGGER,"Show Again Checkbox not Selected");
                }
            }
        });
        apiKeyLabel.setVisible(false);
        apiKeyField.setText(ConfigurationManager.getConfiguration().getGptApiKey());
        apiKeyField.setVisible(false);
        promptLabel.setVisible(false);
        promptScrollPane.setVisible(false);
        enablePromptCheckBox.setVisible(false);

        showAgainCheckBox.setVisible(true);

        newRadioButton.addActionListener(e -> {
            apiKeyLabel.setVisible(true);
            apiKeyField.setVisible(true);
            promptLabel.setVisible(true);
            promptScrollPane.setVisible(true);
            enablePromptCheckBox.setVisible(true);
            gptModelLabel.setVisible(true);
            modelComboBox.setVisible(true);
            fetchModelsButton.setVisible(true);
            for (int i = 0; i < modelComboBox.getItemCount(); i++) {
                if (modelComboBox.getItemAt(i).equals(ConfigurationManager.getConfiguration().getGptModel())) {
                    modelComboBox.setSelectedIndex(i);
                    break;
                }
            }
            showAgainCheckBox.setVisible(true);

            apiKeyField.requestFocusInWindow();

            LoggerManager.info(Constants.EDITOR_LOGGER, "LLM Service Selected");
        });

        oldRadioButton.addActionListener(e -> {
            apiKeyLabel.setVisible(false);
            apiKeyField.setVisible(false);
            promptLabel.setVisible(false);
            promptScrollPane.setVisible(false);
            enablePromptCheckBox.setVisible(false);
            gptModelLabel.setVisible(false);
            modelComboBox.setVisible(false);
            fetchModelsButton.setVisible(false);

            showAgainCheckBox.setVisible(true);

            LoggerManager.info(Constants.EDITOR_LOGGER, "Algorithm Service Selected");
        });

        oldRadioButton.setSelected(true);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        fieldsPanel.add(apiKeyLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(apiKeyField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        fieldsPanel.add(promptLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        fieldsPanel.add(promptScrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        fieldsPanel.add(enablePromptCheckBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        fieldsPanel.add(gptModelLabel, gbc); // Add label before JComboBox

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(modelComboBox, gbc); // Add JComboBox to the panel

        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        fieldsPanel.add(fetchModelsButton, gbc); // Add fetchModels button to the panel

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        fieldsPanel.add(showAgainCheckBox, gbc); // Add "Show Again" checkbox

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

    JPanel initializeSingleButtonPanel() {
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton singleButton = new JButton();

        singleButton.setMnemonic(KeyEvent.VK_A);
        singleButton.setText(Messages.getString("P2T.text"));

        buttonPanel.add(singleButton, BorderLayout.CENTER);

        singleButton.addActionListener(e -> {
            if (newRadioButton.isSelected()) {
                if (validateAPIKey()) {

                    ConfigurationManager.getConfiguration().setGptApiKey(apiKeyField.getText());
                    ConfigurationManager.getConfiguration().setGptPrompt(promptField.getText());
                    ConfigurationManager.getConfiguration().setGptModel(modelComboBox.getSelectedItem().toString());
                    ConfigurationManager.getConfiguration().setGptUseNew(true);

                    if (!showAgainCheckBox.isSelected()) {
                        ConfigurationManager.getConfiguration().setGptShowAgain(false);
                        ConfigurationManager.getConfiguration().setGptUseNew(true);
                    }
                    executeAction();
                    dispose();
                }
            } else {
                ConfigurationManager.getConfiguration().setGptUseNew(false);
                if (!showAgainCheckBox.isSelected()) {
                    ConfigurationManager.getConfiguration().setGptShowAgain(false);
                    ConfigurationManager.getConfiguration().setGptUseNew(false);

                }
                executeAction();
                dispose();
            }
        });
        return buttonPanel;
    }

    private void executeAction() {
        WoPeDAction action = ActionFactory.getStaticAction(ActionFactory.ACTIONID_P2T_OLD);
        action.actionPerformed(new ViewEvent(this, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.P2T, null));
    }

    private void fetchAndFillModels() {
        new Thread(() -> {
            try {
                List<String> models = ApiHelper.fetchModels(apiKeyField.getText());
                SwingUtilities.invokeLater(() -> {
                    for (String model : models) {
                        modelComboBox.addItem(model);
                    }
                    modelComboBox.setSelectedItem(ConfigurationManager.getConfiguration().getGptModel());
                });
            } catch (IOException | ParseException e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, Messages.getString("P2T.exception.fail.fetch.models") + e.getMessage(), Messages.getString("P2T.exception.fetch.models"), JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    boolean validateAPIKey() {
        String apiKey = apiKeyField.getText();
        boolean apiKeyValid = isAPIKeyValid(apiKey);
        if (!apiKeyValid) {
            JOptionPane.showMessageDialog(this, Messages.getString("P2T.apikey.invalid"), Messages.getString("P2T.apikey.invalid.title"), JOptionPane.ERROR_MESSAGE);
        }
        return apiKeyValid;
    }

    public static boolean isAPIKeyValid(String apiKey) {
        final String TEST_URL = "https://api.openai.com/v1/engines";
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

    private void showLoadingBox() {
        JOptionPane jop = new JOptionPane();
        jop.setMessageType(JOptionPane.INFORMATION_MESSAGE);
        jop.setMessage(Messages.getString("P2TUI.Loading.Text"));

        loadDialog = jop.createDialog(this, Messages.getString("P2TUI.Loading.Title"));
        jop.setOptions(new String[]{Messages.getString("P2TUI.Loading.Cancel")});
        loadDialog.setVisible(true);

        // Thread gets blocked and awaits an UI action.
    }

    private void showErrorPopUp(String titleId, String msgId) {
        String text[] = {Messages.getString("Dialog.Ok")};

        String msg = Messages.getStringReplaced(msgId, null);
        String title = Messages.getString(titleId);
        int optionType = JOptionPane.YES_NO_CANCEL_OPTION;
        int messageType = JOptionPane.ERROR_MESSAGE;

        JOptionPane.showOptionDialog(null, msg, title, optionType, messageType, null, text, text[0]);
    }


}
