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
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.file.t2p.JTextAreaWithHint;
import org.woped.gui.lookAndFeel.WopedButton;
import org.woped.gui.translations.Messages;

public class P2TUI extends JDialog {
    private JDialog loadDialog;
    private AbstractApplicationMediator mediator;
    private boolean requested = false;

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

        // Add switch button panel to the top left
        this.getContentPane().add(initializeSwitchButtonPanel(), BorderLayout.NORTH);

        // Add a single button to the bottom center
        this.getContentPane().add(initializeSingleButtonPanel(), BorderLayout.SOUTH);

        this.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - this.getWidth()) / 3, (screenSize.height - this.getHeight()) / 3);
        Dimension size = new Dimension(600, 140);
        this.setSize(size);
    }

    private JPanel initializeSwitchButtonPanel() {
        JPanel switchButtonPanel = new JPanel();
        switchButtonPanel.setLayout(new BoxLayout(switchButtonPanel, BoxLayout.LINE_AXIS));
        switchButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JRadioButton oldRadioButton = new JRadioButton(Messages.getString("P2T.oldservice.title"));
        JRadioButton newRadioButton = new JRadioButton(Messages.getString("P2T.newservice.title"));
        ButtonGroup group = new ButtonGroup();
        group.add(oldRadioButton);
        group.add(newRadioButton);

        JLabel apiKeyLabel = new JLabel(Messages.getString("P2T.apikey.title"));
        JTextField apiKeyField = new JTextField();
        apiKeyField.setPreferredSize(new Dimension(200, 25)); // Set the preferred size to make it wider
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

        switchButtonPanel.add(oldRadioButton);
        switchButtonPanel.add(newRadioButton);
        switchButtonPanel.add(apiKeyLabel);
        switchButtonPanel.add(apiKeyField);

        return switchButtonPanel;
    }

    private JPanel initializeSingleButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton singleButton = new JButton(new AbstractAction() {
            public void actionPerformed(ActionEvent arg0) {
                // Implement your action here
                request();
            }
        });

        singleButton.setMnemonic(KeyEvent.VK_A);
        singleButton.setText(Messages.getString("P2T.tooltip"));
        singleButton.setIcon(loadIcon(Messages.getString("P2TUI.Button.Generate.Icon")));

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(singleButton);
        buttonPanel.add(Box.createHorizontalGlue());

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

        // Implement your request handling here
        showLoadingBox();

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
}
