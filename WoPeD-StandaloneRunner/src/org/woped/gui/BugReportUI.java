package org.woped.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JDialog;

import org.woped.editor.action.DisposeWindowAction;
import org.woped.editor.utilities.Messages;
import org.woped.gui.action.help.LaunchDefaultBrowserAction;

public class BugReportUI extends JDialog
{
    private JLabel              logoLabel       = null;
    private JLabel              bugReportLabel  = null;
    private JLabel              bugPageLabel    = null;
    private JButton             closeButton     = null;
    private JScrollPane         bugReportPanel  = null;
    private JPanel              buttonPanel     = null;

    // TODO: move in propertie files (tfreytag)
    private static final String bugReportText       
        = "<html><p>" + "<b>WoPeD Bug Reporting</b><br>"
        + "Have you found a bug? Please click on the link below.</p></html>";

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
    public BugReportUI(Frame owner) throws HeadlessException
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
  
        this.setUndecorated(true);
        this.pack();

        if (getOwner() != null)
        {
            this.setLocation(getOwner().getX() + ((getOwner().getWidth() - this.getWidth()) / 2), getOwner().getY() + ((getOwner().getHeight() - this.getHeight()) / 2));
        } 
        else
        {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            this.setLocation((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2);
        }

        this.setSize(this.getWidth(), this.getHeight());
        this.setVisible(true);
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
            c.anchor = GridBagConstraints.WEST;
            c.insets = new Insets(0, 0, 0, 0);
            logoLabel = new JLabel(new ImageIcon(getClass().getResource(Messages.getString("Bugreport.Image"))));
            panel.add(logoLabel, c);

            c.gridy = 1;
            c.insets = new Insets(1, 5, 1, 1);
            bugReportLabel = new JLabel(bugReportText);
            panel.add(bugReportLabel, c);

            c.gridy = 2;
            // TODO: move in propertie files (tfreytag)
            bugPageLabel = new JLabel("<html><a href=woped.ba-karlsruhe.de/bugs>WoPeD Bug Reporting Page</a></html>");
            bugPageLabel.addMouseListener(new LaunchDefaultBrowserAction("http://woped.ba-karlsruhe.de/bugs", bugPageLabel));
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
            buttonPanel.setLayout(new BorderLayout());
 
            /* Close Button */
            closeButton = new JButton(new DisposeWindowAction());
            
            closeButton.setMnemonic(KeyEvent.VK_C);
            closeButton.setBorderPainted(false);

            buttonPanel.add(closeButton, BorderLayout.CENTER);
            closeButton.requestFocus();

        }
        return buttonPanel;
    }
}
