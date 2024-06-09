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
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.gui.translations.Messages;

public class P2TUI extends JDialog {
    private JDialog loadDialog;
    private AbstractApplicationMediator mediator;
    private boolean requested = false;
    private JTextField apiKeyField;
    private JTextArea promptField;  // Changed to JTextArea for multiline
    private JCheckBox enablePromptCheckBox; // New Checkbox
    private JCheckBox showAgainCheckBox; // New Checkbox
    private JRadioButton newRadioButton = null;
    private JRadioButton oldRadioButton = null;
    private static final String DEFAULT_PROMPT = "Create a clearly structured and comprehensible continuous text from the given BPMN that is understandable for an uninformed reader. The text should be easy to read in the summary and contain all important content; if there are subdivided points, these are integrated into the text with suitable sentence beginnings in order to obtain a well-structured and easy-to-read text. Under no circumstances should the output contain sub-items or paragraphs, but should cover all processes in one piece!";

    public P2TUI(AbstractApplicationMediator mediator) {
        this(null, mediator);
    }

    public P2TUI(Frame owner, AbstractApplicationMediator mediator) throws HeadlessException {
        super(owner, true);
        this.mediator = mediator;
        initialize();
    }

    private void initialize() {
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
        Dimension size = new Dimension(600, 350); // Adjusted size to accommodate new text field and checkbox
        this.setSize(size);
    }

    private JPanel initializeSwitchButtonPanel() {
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
        apiKeyField.setPreferredSize(new Dimension(200, 25));

        JLabel promptLabel = new JLabel("Prompt:");
        promptField = new JTextArea(DEFAULT_PROMPT);  // Changed to JTextArea
        promptField.setLineWrap(true);
        promptField.setWrapStyleWord(true);
        promptField.setRows(5); // Set initial number of rows
        promptField.setEnabled(false); // Initially disabled

        JScrollPane promptScrollPane = new JScrollPane(promptField);
        promptScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        promptScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        promptScrollPane.setPreferredSize(new Dimension(200, 100));

        enablePromptCheckBox = new JCheckBox("Enable editing prompt");
        enablePromptCheckBox.setSelected(false);
        enablePromptCheckBox.addActionListener(e -> {
            promptField.setEnabled(enablePromptCheckBox.isSelected());
            if (!enablePromptCheckBox.isSelected()) {
                promptField.setText(DEFAULT_PROMPT); // Reset text when disabled
                promptField.revalidate();
            }
        });

        showAgainCheckBox = new JCheckBox("Don't show Again");
        showAgainCheckBox.setSelected(true);
        showAgainCheckBox.setToolTipText("Placeholder");

        apiKeyLabel.setVisible(false);
        apiKeyField.setVisible(false);
        promptLabel.setVisible(false);
        promptScrollPane.setVisible(false);
        enablePromptCheckBox.setVisible(false);
        showAgainCheckBox.setVisible(false); // Initially hidden

        newRadioButton.addActionListener(e -> {
            apiKeyLabel.setVisible(true);
            apiKeyField.setVisible(true);
            promptLabel.setVisible(true);
            promptScrollPane.setVisible(true);
            enablePromptCheckBox.setVisible(true);
            showAgainCheckBox.setVisible(true); // Show when new service is selected
            apiKeyField.requestFocusInWindow();
        });

        oldRadioButton.addActionListener(e -> {
            apiKeyLabel.setVisible(false);
            apiKeyField.setVisible(false);
            promptLabel.setVisible(false);
            promptScrollPane.setVisible(false);
            enablePromptCheckBox.setVisible(false);
            showAgainCheckBox.setVisible(false); // Hide when old service is selected
        });

        // Set "alt" as default selection
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
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(promptScrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        fieldsPanel.add(enablePromptCheckBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        fieldsPanel.add(showAgainCheckBox, gbc); // Add "Show Again" checkbox

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        switchButtonPanel.add(fieldsPanel, gbc);

        return switchButtonPanel;
    }

    private JPanel initializeSingleButtonPanel() {
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton singleButton = new JButton(new AbstractAction() {
            public void actionPerformed(ActionEvent arg0) {
                if (newRadioButton.isSelected()) {
                    validateAPIKey();
                    //GPT Aufrufen
                }
                else {
                    //GPT Aufrufen
                }

            }
        });

        singleButton.setMnemonic(KeyEvent.VK_A);
        singleButton.setText(Messages.getString("P2T.text"));

        buttonPanel.add(singleButton, BorderLayout.CENTER);

        return buttonPanel;
    }

    private void validateAPIKey() {
        String apiKey = apiKeyField.getText();
        if (!isAPIKeyValid(apiKey)) {
            JOptionPane.showMessageDialog(this, Messages.getString("P2T.apikey.invalid"), Messages.getString("P2T.apikey.invalid.title"), JOptionPane.ERROR_MESSAGE);
        }
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

    private void close() {
        this.dispose();
    }
}
