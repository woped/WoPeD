package org.woped.starter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.woped.editor.action.DisposeWindowAction;
import org.woped.editor.help.action.LaunchDefaultBrowserAction;
import org.woped.gui.lookAndFeel.WopedButton;
import org.woped.gui.translations.Messages;

@SuppressWarnings("serial")
public class BugReportUI extends JDialog
{
    private JLabel              logoLabel      = null;
    private JLabel              bugReportLabel = null;
    private JLabel              bugPageLabel   = null;
    private WopedButton             closeButton    = null;
    private JScrollPane         bugReportPanel = null;
    private JPanel              buttonPanel    = null;

    private static final String bugReportText  = Messages.getString("BugReport.Text"); ;

    public BugReportUI()
    {
        this(null);
    }

    /**
     * Constructor for AboutUI.
     * 
     * @param owner
     * @throws HeadlessException
     */
    public BugReportUI(JFrame owner) throws HeadlessException
    {
        super(owner, true);
        initialize();
    }

    /**
     * This method initializes and layouts the about information
     * 
     * @return void
     */
    private void initialize()
    {
        this.setVisible(false);
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(getBugReportPanel(), BorderLayout.NORTH);
        this.getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);
        this.setUndecorated(false);
        this.setTitle(Messages.getTitle("Action.ShowBugReport"));
        this.pack();

        if (getOwner() != null)
        {
            this.setLocation(getOwner().getX() + ((getOwner().getWidth() - this.getWidth()) / 2), getOwner().getY() + ((getOwner().getHeight() - this.getHeight()) / 2));
        } else
        {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            this.setLocation((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2);
        }

        this.setSize(this.getWidth(), this.getHeight());
    }

    private JScrollPane getBugReportPanel()
    {
        if (bugReportPanel == null)
        {
            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            c.gridx = 0;
            c.gridy = 0;
            c.anchor = GridBagConstraints.CENTER;
            c.insets = new Insets(10, 10, 0, 10);
            logoLabel = new JLabel(new ImageIcon(getClass().getResource(Messages.getString("BugReport.Image"))));
            panel.add(logoLabel, c);

            c.gridy = 1;
            c.anchor = GridBagConstraints.WEST;
            c.insets = new Insets(0, 10, 0, 10);
            bugReportLabel = new JLabel(bugReportText);
            panel.add(bugReportLabel, c);

            c.gridy = 2;
            c.insets = new Insets(0, 10, 10, 10);
            bugPageLabel = new JLabel(Messages.getString("BugReport.Link"));
            bugPageLabel.addMouseListener(new LaunchDefaultBrowserAction(Messages.getString("BugReport.URL"), bugPageLabel));
            panel.add(bugPageLabel, c);

            bugReportPanel = new JScrollPane(panel);
        }
        return bugReportPanel;
    }

    private JPanel getButtonPanel()
    {
        if (buttonPanel == null)
        {
            buttonPanel = new JPanel();
            GridBagConstraints c = new GridBagConstraints();

            closeButton = new WopedButton(new DisposeWindowAction());
            closeButton.setIcon(new ImageIcon(getClass().getResource(Messages.getString("BugReport.Close.Icon"))));
            closeButton.requestFocus();
            c.insets = new Insets(20, 0, 20, 0);
            c.anchor = GridBagConstraints.CENTER;
            buttonPanel.add(closeButton, c);
        }
        return buttonPanel;
    }
}
