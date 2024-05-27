package org.woped.file.p2t;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.file.t2p.JTextAreaWithHint;
import org.woped.file.t2p.PlainTextFileReader;
import org.woped.gui.lookAndFeel.WopedButton;
import org.woped.gui.translations.Messages;

public class P2TUI extends JDialog {
    private JTextAreaWithHint textArea;
    private JDialog loadDialog;
    private AbstractApplicationMediator mediator;
    private boolean requested = false;
    private String inputText;

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

        textArea = new JTextAreaWithHint();
        this.setTitle(Messages.getString("P2T.tooltip"));
        this.getContentPane().add(wrapTextArea(initializeTextArea(textArea)), BorderLayout.CENTER);
        this.getContentPane().add(initializeButtonsPanel(), BorderLayout.SOUTH);

        this.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - this.getWidth()) / 3, (screenSize.height - this.getHeight()) / 3);
        Dimension size = new Dimension(600, 440);
        this.setSize(size);

        // Set previous text if available
        int index = 0;
        boolean doesContain = false;
        if (mediator.getViewControllers().containsKey("EDITOR_VC_" + index)) {
            doesContain = true;
            while (mediator.getViewControllers().containsKey("EDITOR_VC_" + index)) {
                index++;
            }
            index--;
        }

        if (doesContain) {
            String lastTextInput = ((EditorVC) mediator.getViewControllers().get("EDITOR_VC_" + index)).getEditorPanel().getT2PText();
            textArea.setText(lastTextInput);
        }
    }

    private JTextAreaWithHint initializeTextArea(JTextAreaWithHint ta) {
        Font f = new Font("Lucia Grande", Font.PLAIN, 13);
        String hint = Messages.getString("P2TUI.HowTo");
        ta.setFont(f);
        ta.changeHintText(hint);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.requestFocus();
        ta.requestFocusInWindow();
        ta.setMargin(new Insets(10, 10, 10, 10));
        return ta;
    }

    private JScrollPane wrapTextArea(JTextAreaWithHint ta) {
        JScrollPane scrollPane = new JScrollPane(ta);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    private JPanel initializeButtonsPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        String[] lang = {Messages.getString("P2TUI.Lang"), Messages.getString("P2TUI.Lang.English")};
        JComboBox<String> langBox = new JComboBox<>(lang);
        langBox.setSelectedIndex(1);

        // Radio buttons for "alt" and "neu"
        JRadioButton oldRadioButton = new JRadioButton("Alt");
        JRadioButton newRadioButton = new JRadioButton("Neu");
        ButtonGroup group = new ButtonGroup();
        group.add(oldRadioButton);
        group.add(newRadioButton);

        // API Key input field
        JLabel apiKeyLabel = new JLabel("API Key:");
        JTextField apiKeyField = new JTextField();
        apiKeyLabel.setVisible(false);
        apiKeyField.setVisible(false);

        newRadioButton.addActionListener(e -> {
            apiKeyLabel.setVisible(true);
            apiKeyField.setVisible(true);
        });

        oldRadioButton.addActionListener(e -> {
            apiKeyLabel.setVisible(false);
            apiKeyField.setVisible(false);
        });

        // Set "alt" as default selection
        oldRadioButton.setSelected(true);

        WopedButton btnGenerate = new WopedButton(new AbstractAction() {
            public void actionPerformed(ActionEvent arg0) {
                request();
            }
        });

        btnGenerate.setMnemonic(KeyEvent.VK_A);
        btnGenerate.setText(Messages.getString("P2TUI.Button.Generate.Text"));
        btnGenerate.setIcon(loadIcon(Messages.getString("P2TUI.Button.Generate.Icon")));

        WopedButton btnErase = new WopedButton(new AbstractAction() {
            public void actionPerformed(ActionEvent arg0) {
                clearTextArea();
            }
        });

        btnErase.setMnemonic(KeyEvent.VK_L);
        btnErase.setText(Messages.getString("P2TUI.Button.Clear.Text"));
        btnErase.setIcon(loadIcon(Messages.getString("P2TUI.Button.Clear.Icon")));

        WopedButton btnUpload = new WopedButton(new AbstractAction() {
            public void actionPerformed(ActionEvent arg0) {
                readFile();
            }
        });

        btnUpload.setMnemonic(KeyEvent.VK_C);
        btnUpload.setText(Messages.getString("P2TUI.Button.Read.Text"));
        btnUpload.setIcon(loadIcon(Messages.getString("P2TUI.Button.Read.Icon")));

        buttonPanel.add(btnUpload);
        buttonPanel.add(btnErase);
        buttonPanel.add(langBox);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(oldRadioButton);
        buttonPanel.add(newRadioButton);
        buttonPanel.add(apiKeyLabel);
        buttonPanel.add(apiKeyField);
        buttonPanel.add(btnGenerate);

        return buttonPanel;
    }

    private ImageIcon loadIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null; // or a default icon if you prefer
        }
    }

    private void request() {
        if (requested) return;
        requested = true;

        inputText = textArea.getText();

        if (!inputText.isEmpty()) {
            // Implement your request handling here
            showLoadingBox();
        } else {
            showErrorPopUp("P2TUI.NoText.Title", "P2TUI.NoText.Text");
        }

        requested = false;
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

    public void clearTextArea() {
        if (textArea.getText() != null) {
            textArea.setText(null);
        }
    }

    public void readFile() {
        PlainTextFileReader r = new PlainTextFileReader();
        String txt = r.read();
        if (txt != null) textArea.setText(txt);
    }
}
