package org.woped.editor.gui.config;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import javax.swing.*;

import org.json.simple.parser.ParseException;
import org.woped.core.config.ConfigurationManager;
import org.woped.editor.tools.ApiHelper;
import org.woped.gui.lookAndFeel.WopedButton;
import org.woped.gui.translations.Messages;

public class ConfNLPToolsPanel extends AbstractConfPanel {
    private JCheckBox useBox = null;
    private JPanel enabledPanel = null;
    private JPanel settingsPanel = null;
    private JPanel settingsPanel_T2P = null;
    private JPanel settingsPanel_GPT = null;

    private JTextField serverURLText = null;
    private JLabel serverURLLabel = null;
    private JLabel serverPortLabel = null;
    private JTextField serverPortText = null;
    private JTextField managerPathText = null;
    private JLabel managerPathLabel = null;
    private WopedButton testButton = null;
    private WopedButton defaultButton = null;
    private JTextField serverURLText_T2P = null;
    private JLabel serverURLLabel_T2P = null;
    private JLabel serverPortLabel_T2P = null;
    private JTextField serverPortText_T2P = null;
    private JTextField managerPathText_T2P = null;
    private JLabel managerPathLabel_T2P = null;
    private WopedButton testButton_T2P = null;
    private WopedButton defaultButton_T2P = null;

    // Components for additionalPanel
    private JTextField apiKeyText = null;
    private JCheckBox showAgainBox = null;
    private WopedButton resetButton = null;
    private JTextArea promptText = null;
    private WopedButton fetchGPTModelsButton = null;
    private WopedButton checkConnectionButton = null;
    private JComboBox<String> modelComboBox = new JComboBox<String>();

    // Components for LLM Panel
    private JPanel settingsPanel_LLM = null;
    private JTextField serverURLText_LLM = null;
    private JLabel serverURLLabel_LLM = null;
    private WopedButton testButton_LLM = null;
    private WopedButton defaultButton_LLM = null;

    public ConfNLPToolsPanel(String name) {
        super(name);
        initialize();
    }

    public boolean applyConfiguration() {
        boolean newsetting = useBox.isSelected();
        boolean oldsetting = ConfigurationManager.getConfiguration().getProcess2TextUse();

        if (newsetting != oldsetting) {
            ConfigurationManager.getConfiguration().setProcess2TextUse(newsetting);
            JOptionPane.showMessageDialog(
                    this,
                    Messages.getString("Configuration.P2T.Dialog.Restart.Message"),
                    Messages.getString("Configuration.P2T.Dialog.Restart.Title"),
                    JOptionPane.INFORMATION_MESSAGE);
        }
        ConfigurationManager.getConfiguration().setProcess2TextServerHost(getServerURLText().getText());
        ConfigurationManager.getConfiguration().setProcess2TextServerURI(getManagerPathText().getText());
        if (getServerPortText().getText().isEmpty()) {
            ConfigurationManager.getConfiguration().setProcess2TextServerPort(0);
        } else {
            ConfigurationManager.getConfiguration()
                    .setProcess2TextServerPort(Integer.parseInt(getServerPortText().getText()));
        }
        ConfigurationManager.getConfiguration().setProcess2TextUse(useBox.isSelected());

        ConfigurationManager.getConfiguration().setText2ProcessServerHost(getServerURLText_T2P().getText());

        ConfigurationManager.getConfiguration().setText2ProcessServerURI(getManagerPathText_T2P().getText());

        if (getServerPortText_T2P().getText().isEmpty()) {
            ConfigurationManager.getConfiguration().setText2ProcessServerPort(0);
        } else {
            ConfigurationManager.getConfiguration()
                    .setText2ProcessServerPort(Integer.parseInt(getServerPortText_T2P().getText()));
        }
        ConfigurationManager.getConfiguration().setGptApiKey(getApiKeyText().getText());
        ConfigurationManager.getConfiguration().setGptShowAgain(true);
        ConfigurationManager.getConfiguration().setGptPrompt(getPromptText().getText());
        ConfigurationManager.getConfiguration().setGptModel(modelComboBox.getSelectedItem().toString());
        ConfigurationManager.getConfiguration().setT2P2ServerHost(getServerURLText_LLM().getText());
        return true;
    }

    public void readConfiguration() {
        getServerURLText().setText(ConfigurationManager.getConfiguration().getProcess2TextServerHost());
        getManagerPathText().setText(ConfigurationManager.getConfiguration().getProcess2TextServerURI());
        getServerPortText().setText("" + ConfigurationManager.getConfiguration().getProcess2TextServerPort());
        getUseBox().setSelected(ConfigurationManager.getConfiguration().getProcess2TextUse());

        getServerURLText_T2P().setText(ConfigurationManager.getConfiguration().getText2ProcessServerHost());
        getManagerPathText_T2P().setText(ConfigurationManager.getConfiguration().getText2ProcessServerURI());
        getServerPortText_T2P().setText("" + ConfigurationManager.getConfiguration().getText2ProcessServerPort());
        getApiKeyText().setText(ConfigurationManager.getConfiguration().getGptApiKey());
        getShowAgainBox().setSelected(ConfigurationManager.getConfiguration().getGptShowAgain());
        getPromptText().setText(ConfigurationManager.getConfiguration().getGptPrompt());
        getServerURLText_LLM().setText(ConfigurationManager.getConfiguration().getT2PLLMServerHost());
    }

    private void initialize() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(2, 0, 2, 0);

        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        contentPanel.add(getEnabledPanel(), c);

        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 1;
        contentPanel.add(getSettingsPanel(), c);

        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 2;
        contentPanel.add(getSettingsPanel_T2P(), c);

        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 3;
        contentPanel.add(getSettingsPanel_LLM(), c);

        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 4;
        contentPanel.add(getGPTPanel(), c);

        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 1;
        c.gridy = 5;
        contentPanel.add(new JPanel(), c);

        setMainPanel(contentPanel);

    }

    private JTextField getServerURLText() {
        if (serverURLText == null) {
            serverURLText = new JTextField();
            serverURLText.setColumns(40);
            serverURLText.setEnabled(true);
            serverURLText.setToolTipText("<html>" + Messages.getString("Configuration.P2T.Label.ServerHost") + "</html>");
        }
        return serverURLText;
    }

    private JTextField getServerURLText_T2P() {
        if (serverURLText_T2P == null) {
            serverURLText_T2P = new JTextField();
            serverURLText_T2P.setColumns(40);
            serverURLText_T2P.setEnabled(true);
            serverURLText_T2P.setToolTipText("<html>" + Messages.getString("Configuration.T2P.Label.ServerHost") + "</html>");
        }
        return serverURLText_T2P;
    }

    private JPanel getEnabledPanel() {
        if (enabledPanel == null) {
            enabledPanel = new JPanel();
            enabledPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;
            c.insets = new Insets(2, 0, 2, 0);

            enabledPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Messages.getTitle("Configuration.P2T.Enabled.Panel")), BorderFactory.createEmptyBorder(10, 10, 10, 10)));

            c.weightx = 1;
            c.gridx = 0; // Move further left
            c.gridy = 0;
            enabledPanel.add(getUseBox(), c);
        }
        return enabledPanel;
    }

    private JPanel getSettingsPanel() {
        if (settingsPanel == null) {
            settingsPanel = new JPanel();
            settingsPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;
            c.insets = new Insets(2, 0, 2, 0);

            settingsPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("Configuration.P2T.Settings.Panel.Title")), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 0;
            settingsPanel.add(getServerURLLabel(), c);

            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 0;
            c.gridwidth = 2;
            settingsPanel.add(getServerURLText(), c);

            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 1;
            c.gridwidth = 1;
            settingsPanel.add(getServerPortLabel(), c);

            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 1;
            settingsPanel.add(getServerPortText(), c);

            c.weightx = 1;
            c.gridx = 2;
            c.gridy = 1;
            settingsPanel.add(getTestButton(), c);

            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 2;
            settingsPanel.add(getManagerPathLabel(), c);

            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 2;
            c.gridwidth = 2;
            settingsPanel.add(getManagerPathText(), c);

            c.weightx = 1;
            c.gridx = 3;
            c.gridy = 1;
            settingsPanel.add(getDefaultButton(), c);
        }

        settingsPanel.setVisible(getUseBox().isSelected());
        return settingsPanel;
    }

    private JPanel getSettingsPanel_T2P() {
        if (settingsPanel_T2P == null) {
            settingsPanel_T2P = new JPanel();
            settingsPanel_T2P.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;
            c.insets = new Insets(2, 0, 2, 0);

            settingsPanel_T2P.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("Configuration.T2P.Settings.Panel.Title_NLP")), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 0;
            settingsPanel_T2P.add(getServerURLLabel_T2P(), c);

            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 0;
            c.gridwidth = 2;
            settingsPanel_T2P.add(getServerURLText_T2P(), c);

            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 1;
            c.gridwidth = 1;
            settingsPanel_T2P.add(getServerPortLabel_T2P(), c);

            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 1;
            settingsPanel_T2P.add(getServerPortText_T2P(), c);

            c.weightx = 1;
            c.gridx = 2;
            c.gridy = 1;
            settingsPanel_T2P.add(getTestButton_T2P(), c);

            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 2;
            settingsPanel_T2P.add(getManagerPathLabel_T2P(), c);

            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 2;
            c.gridwidth = 2;
            settingsPanel_T2P.add(getManagerPathText_T2P(), c);

            c.weightx = 1;
            c.gridx = 3;
            c.gridy = 1;
            settingsPanel_T2P.add(getDefaultButton_T2P(), c);
        }

        settingsPanel_T2P.setVisible(getUseBox_T2P().isSelected());
        return settingsPanel_T2P;
    }
    private JPanel getSettingsPanel_LLM() {
        if (settingsPanel_LLM == null) {
            settingsPanel_LLM = new JPanel();
            settingsPanel_LLM.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;
            c.insets = new Insets(2, 0, 2, 0);

            settingsPanel_LLM.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("Configuration.T2P.Settings.Panel.Title_LLM")), BorderFactory.createEmptyBorder(10, 10, 10, 10)));

            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 0;
            settingsPanel_LLM.add(getServerURLLabel_LLM(), c);

            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 0;
            c.gridwidth = 2;
            settingsPanel_LLM.add(getServerURLText_LLM(), c);

            c.weightx = 1;
            c.gridx = 2;
            c.gridy = 1;
            c.gridwidth = 1;
            settingsPanel_LLM.add(getTestButton_LLM(), c);

            c.weightx = 1;
            c.gridx = 3;
            c.gridy = 1;
            settingsPanel_LLM.add(getDefaultButton_LLM(), c);
        }

        settingsPanel_LLM.setVisible(getUseBox().isSelected());
        return settingsPanel_LLM;
    }

    private JPanel getGPTPanel() {
        if (settingsPanel_GPT == null) {
            settingsPanel_GPT = new JPanel();
            settingsPanel_GPT.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;
            c.insets = new Insets(2, 0, 2, 0);

            settingsPanel_GPT.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("Configuration.GPT.settings.Title")), BorderFactory.createEmptyBorder(10, 10, 10, 10)));

            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 0;
            settingsPanel_GPT.add(new JLabel(Messages.getString("Configuration.GPT.apikey.Title")), c);

            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 0;
            settingsPanel_GPT.add(getApiKeyText(), c);

            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 1;
            settingsPanel_GPT.add(new JLabel(Messages.getString("Configuration.GPT.prompt.Title")), c);

            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 2;
            settingsPanel_GPT.add(getPromptTextScrollPane(), c);

            // Add the new row with the label and combo box
            c.weightx = 0;
            c.gridx = 0;
            c.gridy = 2;
            settingsPanel_GPT.add(new JLabel(Messages.getString("Configuration.GPT.model.Title")), c);

            // Adjust the constraints for the combo box
            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 2;
            c.gridwidth = 1;
            c.insets = new Insets(2, 0, 2, 12); // Add padding to the right of the combo box
            c.fill = GridBagConstraints.HORIZONTAL;
            settingsPanel_GPT.add(getModelComboBox(), c);

            // Adjust the constraints for the fetchModels button
            c.weightx = 0;
            c.gridx = 2;
            c.gridy = 2;
            c.insets = new Insets(2, 0, 2, 10);
            settingsPanel_GPT.add(getFetchGPTModelsButton(), c);

            // Adjust the constraints for the show again checkbox
            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 3;
            c.insets = new Insets(2, 0, 2, 0);
            settingsPanel_GPT.add(getShowAgainBox(), c);

            // Adjust the constraints for the reset button
            c.weightx = 1;
            c.gridx = 2;
            c.gridy = 3;
            c.insets = new Insets(2, 0, 2, 10); // Add padding to the right of the checkbox
            settingsPanel_GPT.add(getResetButton(), c);

            // Adjust the constraints for the check connection button
            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 3;
            c.insets = new Insets(2, 0, 2, 12); // Reset insets for the button
            settingsPanel_GPT.add(getCheckConnectionButton(), c);
        }

        settingsPanel_GPT.setVisible(getUseBox().isSelected());
        for (int i = 0; i < modelComboBox.getItemCount(); i++){
            if (modelComboBox.getItemAt(i).equals(ConfigurationManager.getConfiguration().getGptModel())){
                modelComboBox.setSelectedIndex(i);
                break;
            }
        }
        return settingsPanel_GPT;
    }

    private JScrollPane getPromptTextScrollPane() {
        JScrollPane scrollPane = new JScrollPane(getPromptText());
        scrollPane.setPreferredSize(new Dimension(getApiKeyText().getPreferredSize().width, 100));
        return scrollPane;
    }

    private JTextField getApiKeyText() {
        if (apiKeyText == null) {
            apiKeyText = new JTextField();
            apiKeyText.setColumns(40);
            apiKeyText.setEnabled(true);
        }
        return apiKeyText;
    }

    private JTextArea getPromptText() {
        if (promptText == null) {
            promptText = new JTextArea();
            promptText.setColumns(40);
            promptText.setRows(5);
            promptText.setLineWrap(true);
            promptText.setWrapStyleWord(true);
            promptText.setEnabled(true);
            promptText.setText("Create a clearly structured and comprehensible continuous text from the given BPMN that is understandable for an uninformed reader. The text should be easy to read in the summary and contain all important content; if there are subdivided points, these are integrated into the text with suitable sentence beginnings in order to obtain a well-structured and easy-to-read text. Under no circumstances should the output contain sub-items or paragraphs, but should cover all processes in one piece!");
        }
        return promptText;
    }

    private JCheckBox getShowAgainBox() {
        if (showAgainBox == null) {
            showAgainBox = new JCheckBox(Messages.getString("Configuration.GPT.show.again.Title"));
            showAgainBox.setEnabled(true);
            showAgainBox.setToolTipText(Messages.getString("Configuration.GPT.tool.tip.text.Title"));
        }
        return showAgainBox;
    }

    private WopedButton getResetButton() {
        if (resetButton == null) {
            resetButton = new WopedButton();
            resetButton.setText(Messages.getString("Configuration.GPT.standard.Title"));
            resetButton.setPreferredSize(new Dimension(200, 25));
            resetButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setDefaultValuesGPT();
                }
            });
        }
        return resetButton;
    }

    private WopedButton getFetchGPTModelsButton(){
        if (fetchGPTModelsButton == null){
            fetchGPTModelsButton = new WopedButton();
            fetchGPTModelsButton.setText(Messages.getString("P2T.fetchmodels.button"));
            fetchGPTModelsButton.setPreferredSize(new Dimension(200, 25));
            fetchGPTModelsButton.addActionListener(e -> fetchAndFillModels());
        }
        return fetchGPTModelsButton;
    }

    private WopedButton getCheckConnectionButton() {
        if (checkConnectionButton == null) {
            checkConnectionButton = new WopedButton();
            checkConnectionButton.setText(Messages.getString("Configuration.GPT.connection.Title"));
            checkConnectionButton.setIcon(Messages.getImageIcon("Button.TestConnection"));
            checkConnectionButton.setMnemonic(Messages.getMnemonic("Button.TestConnection"));
            checkConnectionButton.setPreferredSize(new Dimension(170, 25));
            checkConnectionButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    testGPTConnection();
                }
            });
        }
        return checkConnectionButton;
    }

    private void testGPTConnection() {
        String apiKey = apiKeyText.getText();
        String urlString = "https://api.openai.com/v1/engines";

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.connect();

            int responseCode = connection.getResponseCode();
            String message;

            if (responseCode == 200) {
                message = Messages.getString("Configuration.GPT.connection.successful.Title") + responseCode;
            } else {
                message = Messages.getString("Configuration.GPT.connection.failed.Title") + responseCode;
            }

            JOptionPane.showMessageDialog(
                    this.getGPTPanel(),
                    message,
                    Messages.getString("Configuration.GPT.connection.test.Title"),
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    this.getGPTPanel(),
                    Messages.getString("Configuration.GPT.connection.test.failed.Title") + e.getMessage(),
                    Messages.getString("Configuration.GPT.connection.test.Title"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setDefaultValuesGPT() {
        getApiKeyText().setText(ConfigurationManager.getStandardConfiguration().getGptApiKey());
        getShowAgainBox().setSelected(ConfigurationManager.getStandardConfiguration().getGptShowAgain());
        getPromptText().setText(ConfigurationManager.getStandardConfiguration().getGptPrompt());
    }

    private JComboBox<String> getModelComboBox() {
        if (modelComboBox == null) {
            modelComboBox = new JComboBox<>();
            modelComboBox.setEnabled(true);
            modelComboBox.setToolTipText("Select a model");
        }
        return modelComboBox;
    }

    private JCheckBox getUseBox() {
        if (useBox == null) {
            useBox = new JCheckBox(Messages.getString("Configuration.P2T.Label.Use"));
            useBox.setEnabled(true);
            useBox.setToolTipText("<html>" + Messages.getString("Configuration.P2T.Label.Use") + "</html>");
            CheckboxListener cbl = new CheckboxListener();
            useBox.addItemListener(cbl);
        }

        return useBox;
    }

    private JCheckBox getUseBox_T2P() {
        if (useBox == null) {
            useBox = new JCheckBox(Messages.getString("Configuration.P2T.Label.Use"));
            useBox.setEnabled(true);
            useBox.setToolTipText("<html>" + Messages.getString("Configuration.T2P.Label.Use") + "</html>");
            CheckboxListener cbl = new CheckboxListener();
            useBox.addItemListener(cbl);
        }

        return useBox;
    }

    private JLabel getManagerPathLabel() {
        if (managerPathLabel == null) {
            managerPathLabel = new JLabel("<html>" + Messages.getString("Configuration.P2T.Label.ServerURI") + "</html>");
            managerPathLabel.setHorizontalAlignment(JLabel.RIGHT);
        }
        return managerPathLabel;
    }

    private JLabel getManagerPathLabel_T2P() {
        if (managerPathLabel_T2P == null) {
            managerPathLabel_T2P = new JLabel("<html>" + Messages.getString("Configuration.T2P.Label.ServerURI") + "</html>");
            managerPathLabel_T2P.setHorizontalAlignment(JLabel.RIGHT);
        }
        return managerPathLabel_T2P;
    }

    private JTextField getManagerPathText() {
        if (managerPathText == null) {
            managerPathText = new JTextField();
            managerPathText.setColumns(40);
            managerPathText.setEnabled(true);
            managerPathText.setToolTipText("<html>" + Messages.getString("Configuration.P2T.Label.ServerURI") + "</html>");
        }
        return managerPathText;
    }

    private JTextField getManagerPathText_T2P() {
        if (managerPathText_T2P == null) {
            managerPathText_T2P = new JTextField();
            managerPathText_T2P.setColumns(40);
            managerPathText_T2P.setEnabled(true);
            managerPathText_T2P.setToolTipText("<html>" + Messages.getString("Configuration.T2P.Label.ServerURI") + "</html>");
        }
        return managerPathText_T2P;
    }

    private WopedButton getTestButton() {
        if (testButton == null) {
            testButton = new WopedButton();
            testButton.setText(Messages.getTitle("Button.TestConnection"));
            testButton.setIcon(Messages.getImageIcon("Button.TestConnection"));
            testButton.setMnemonic(Messages.getMnemonic("Button.TestConnection"));
            testButton.setPreferredSize(new Dimension(160, 25));
            testButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    testProcess2TextConnection();
                }
            });
        }

        return testButton;
    }

    private WopedButton getTestButton_T2P() {
        if (testButton_T2P == null) {
            testButton_T2P = new WopedButton();
            testButton_T2P.setText(Messages.getTitle("Button.TestConnection"));
            testButton_T2P.setIcon(Messages.getImageIcon("Button.TestConnection"));
            testButton_T2P.setMnemonic(Messages.getMnemonic("Button.TestConnection"));
            testButton_T2P.setPreferredSize(new Dimension(160, 25));
            testButton_T2P.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    testText2ProcessConnection();
                }
            });
        }

        return testButton_T2P;
    }

    private WopedButton getDefaultButton() {
        if (defaultButton == null) {
            defaultButton = new WopedButton();
            defaultButton.setText(Messages.getTitle("Button.SetToDefault"));
            defaultButton.setPreferredSize(new Dimension(200, 25));
            defaultButton.addActionListener(e -> setDefaultValues());
        }
        return defaultButton;
    }

    private WopedButton getDefaultButton_T2P() {
        if (defaultButton_T2P == null) {
            defaultButton_T2P = new WopedButton();
            defaultButton_T2P.setText(Messages.getTitle("Button.SetToDefault"));
            defaultButton_T2P.setPreferredSize(new Dimension(200, 25));
            defaultButton_T2P.addActionListener(e -> setDefaultValues_T2P());
        }
        return defaultButton_T2P;
    }

    private JTextField getServerURLText_LLM() {
        if (serverURLText_LLM == null) {
            serverURLText_LLM = new JTextField();
            serverURLText_LLM.setColumns(40);
            serverURLText_LLM.setEnabled(true);
            serverURLText_LLM.setToolTipText("<html>" + Messages.getString("Configuration.T2P.Label.ServerHost") + "</html>");
        }
        return serverURLText_LLM;
    }

    private JLabel getServerURLLabel_LLM() {
        if (serverURLLabel_LLM == null) {
            serverURLLabel_LLM = new JLabel("<html>" + Messages.getString("Configuration.T2P.Label.ServerHost") + "</html>");
            serverURLLabel_LLM.setHorizontalAlignment(JLabel.RIGHT);
        }
        return serverURLLabel_LLM;
    }

    private WopedButton getTestButton_LLM() {
        if (testButton_LLM == null) {
            testButton_LLM = new WopedButton();
            testButton_LLM.setText(Messages.getTitle("Button.TestConnection"));
            testButton_LLM.setIcon(Messages.getImageIcon("Button.TestConnection"));
            testButton_LLM.setMnemonic(Messages.getMnemonic("Button.TestConnection"));
            testButton_LLM.setPreferredSize(new Dimension(160, 25));
            testButton_LLM.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    testLLMConnection();
                }
            });
        }
        return testButton_LLM;
    }

    private WopedButton getDefaultButton_LLM() {
        if (defaultButton_LLM == null) {
            defaultButton_LLM = new WopedButton();
            defaultButton_LLM.setText(Messages.getTitle("Button.SetToDefault"));
            defaultButton_LLM.setPreferredSize(new Dimension(200, 25));
            defaultButton_LLM.addActionListener(e -> setDefaultValues_LLM());
        }
        return defaultButton_LLM;
    }

    private void testLLMConnection() {
        String connection = getServerURLText_LLM().getText();
        String[] arg = {connection, ""};
        //TODO: Port from config
        try {
            URL url = new URL("http://" + connection + ":5000/test_connection");
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("GET");
            httpConnection.setConnectTimeout(10000);
            httpConnection.setReadTimeout(10000);

            int responseCode = httpConnection.getResponseCode();

            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(httpConnection.getInputStream()));
                String response = reader.readLine();
                reader.close();

                if (response != null && response.contains("Successful")) {
                    arg[1] = "LLM";
                    JOptionPane.showMessageDialog(
                            this.getSettingsPanel_LLM(),
                            Messages.getString("Paraphrasing.Webservice.Success.Message", arg),
                            Messages.getString("Paraphrasing.Webservice.Success.Title"),
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }

            throw new IOException("Server returned unexpected response: " + responseCode);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    this.getSettingsPanel_LLM(),
                    Messages.getString("Paraphrasing.Webservice.Error.WebserviceException.Message", arg),
                    Messages.getString("Paraphrasing.Webservice.Error.Title"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setDefaultValues_LLM() {
        getServerURLText_LLM().setText(ConfigurationManager.getStandardConfiguration().getT2PLLMServerHost());
    }

    private void testProcess2TextConnection() {
        URL url = null;
        String connection = "http://" + getServerURLText().getText() + ":" + getServerPortText().getText() + getManagerPathText().getText();
        String[] arg = {connection, ""};

        try {
            url = new URL(connection);
            URLConnection urlConnection = url.openConnection();

            if (urlConnection.getContent() != null) {
                arg[1] = "P2T";
                JOptionPane.showMessageDialog(this.getSettingsPanel(), Messages.getString("Paraphrasing.Webservice.Success.Message", arg), Messages.getString("Paraphrasing.Webservice.Success.Title"), JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this.getSettingsPanel(), Messages.getString("Paraphrasing.Webservice.Error.WebserviceException.Message", arg), Messages.getString("Paraphrasing.Webservice.Error.Title"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void testText2ProcessConnection() {
        URL url;
        String connection = "http://" + getServerURLText_T2P().getText() + ":" + getServerPortText_T2P().getText() + getManagerPathText_T2P().getText();
        String[] arg = {connection, ""};

        try {
            url = new URL(connection);
            URLConnection urlConnection = url.openConnection();
            if (urlConnection.getContent() != null) {
                arg[1] = "T2P";
                JOptionPane.showMessageDialog(this.getSettingsPanel_T2P(), Messages.getString("Paraphrasing.Webservice.Success.Message", arg), Messages.getString("Paraphrasing.Webservice.Success.Title"), JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this.getSettingsPanel_T2P(), Messages.getString("Paraphrasing.Webservice.Error.WebserviceException.Message", arg), Messages.getString("Paraphrasing.Webservice.Error.Title"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setDefaultValues() {
        getServerURLText().setText(ConfigurationManager.getStandardConfiguration().getProcess2TextServerHost());
        getManagerPathText().setText(ConfigurationManager.getStandardConfiguration().getProcess2TextServerURI());
        getServerPortText().setText("" + ConfigurationManager.getStandardConfiguration().getProcess2TextServerPort());
    }

    private void setDefaultValues_T2P() {
        getServerURLText_T2P().setText(ConfigurationManager.getStandardConfiguration().getText2ProcessServerHost());
        getManagerPathText_T2P().setText(ConfigurationManager.getStandardConfiguration().getText2ProcessServerURI());
        getServerPortText_T2P().setText("" + ConfigurationManager.getStandardConfiguration().getText2ProcessServerPort());
    }

    class CheckboxListener implements ItemListener {
        public void itemStateChanged(ItemEvent ie) {
            JCheckBox jcb = (JCheckBox) ie.getSource();
            if (jcb == useBox) {
                getSettingsPanel().setVisible(jcb.isSelected());
                getSettingsPanel_T2P().setVisible(jcb.isSelected());
                getGPTPanel().setVisible(jcb.isSelected());
                getSettingsPanel_LLM().setVisible(jcb.isSelected());
            }
        }
    }

    private JLabel getServerURLLabel() {
        if (serverURLLabel == null) {
            serverURLLabel = new JLabel("<html>" + Messages.getString("Configuration.P2T.Label.ServerHost") + "</html>");
            serverURLLabel.setHorizontalAlignment(JLabel.RIGHT);
        }
        return serverURLLabel;
    }

    private JLabel getServerURLLabel_T2P() {
        if (serverURLLabel_T2P == null) {
            serverURLLabel_T2P = new JLabel("<html>" + Messages.getString("Configuration.T2P.Label.ServerHost") + "</html>");
            serverURLLabel_T2P.setHorizontalAlignment(JLabel.RIGHT);
        }
        return serverURLLabel_T2P;
    }

    private JLabel getServerPortLabel() {
        if (serverPortLabel == null) {
            serverPortLabel = new JLabel("<html>" + Messages.getString("Configuration.P2T.Label.ServerPort") + "</html>");
            serverPortLabel.setHorizontalAlignment(JLabel.RIGHT);
        }
        return serverPortLabel;
    }

    private JLabel getServerPortLabel_T2P() {
        if (serverPortLabel_T2P == null) {
            serverPortLabel_T2P = new JLabel("<html>" + Messages.getString("Configuration.T2P.Label.ServerPort") + "</html>");
            serverPortLabel_T2P.setHorizontalAlignment(JLabel.RIGHT);
        }
        return serverPortLabel_T2P;
    }

    private JTextField getServerPortText() {
        if (serverPortText == null) {
            serverPortText = new JTextField();
            serverPortText.setColumns(4);
            serverPortText.setEnabled(true);
            serverPortText.setToolTipText("<html>" + Messages.getString("Configuration.P2T.Label.ServerPort") + "</html>");
        }
        return serverPortText;
    }

    private JTextField getServerPortText_T2P() {
        if (serverPortText_T2P == null) {
            serverPortText_T2P = new JTextField();
            serverPortText_T2P.setColumns(4);
            serverPortText_T2P.setEnabled(true);
            serverPortText_T2P.setToolTipText("<html>" + Messages.getString("Configuration.T2P.Label.ServerPort") + "</html>");
        }
        return serverPortText_T2P;
    }
    private void fetchAndFillModels() {
        new Thread(() -> {
            try {
                List<String> models = ApiHelper.fetchModels(apiKeyText.getText());
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

}
