package org.woped.bpel.gui.transitionproperties;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.woped.editor.controller.TransitionPropertyEditor;
import org.woped.translations.Messages;

/**
 * @author Esther Landes
 * 
 * This is a panel in the transition properties, which enables the user to maintain data for a "wait" BPEL activity.
 *
 * Created on 16.12.2007
 */

public class BPELwaitPanel extends JPanel{

	private TransitionPropertyEditor editor = null;
	private JPanel contentPanel = null;
	private GridBagConstraints c_ContentPanel = null;
	
	private ButtonGroup waitButtonGroup = null;
	private JPanel waitDurationEntry = null;
	private JPanel waitDeadlineEntry = null;
	private JRadioButton waitDurationRadioButton = null;
	private JRadioButton waitDeadlineRadioButton = null;
	private JLabel timeLabel = null;
	private JTextField timeTextField = null;
	
	private static final String WAIT_DURATION = Messages.getString("Transition.Properties.BPELActivityWait.Duration");
	private static final String WAIT_DEADLINE = Messages.getString("Transition.Properties.BPELActivityWait.Deadline");
	
	
	public BPELwaitPanel(TransitionPropertyEditor editor, JPanel transitionPropertiesPanel, GridBagConstraints transitionPropertiesConstraints){
		this.editor = editor;
		contentPanel = transitionPropertiesPanel;
		c_ContentPanel = transitionPropertiesConstraints;
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		setBorder(BorderFactory
						.createCompoundBorder(
								BorderFactory
										.createTitledBorder(Messages
												.getString("Transition.Properties.BPELActivityWait")),
								BorderFactory.createEmptyBorder(5, 5, 0, 5)));

		waitButtonGroup = new ButtonGroup();
		waitButtonGroup.add(getWaitDurationRadioButton());
		waitButtonGroup.add(getWaitDeadlineRadioButton());
		
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0, 2, 0, 0);
		add(getWaitDurationEntry(), c);

		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0, 2, 0, 10);
		add(getWaitDeadlineEntry(), c);
		
		c.gridx = 3;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0, 20, 0, 0);
		add(getTimeLabel(), c);
		
		c.gridx = 4;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0, 10, 0, 10);
		add(getTimeTextField(), c);
	}
	
	private JPanel getWaitDurationEntry() {
		if (waitDurationEntry == null) {
			waitDurationEntry = new JPanel();
			waitDurationEntry.setLayout(new BorderLayout());
			waitDurationEntry.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1;
			c.weighty = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.WEST;
			c.gridx = 0;
			c.gridy = 0;
			waitDurationEntry.add(getWaitDurationRadioButton(), c);
		}

		return waitDurationEntry;
	}
	
	private JPanel getWaitDeadlineEntry() {
		if (waitDeadlineEntry == null) {
			waitDeadlineEntry = new JPanel();
			waitDeadlineEntry.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1;
			c.weighty = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.WEST;
			c.gridx = 0;
			c.gridy = 0;
			waitDeadlineEntry.add(getWaitDeadlineRadioButton(), c);
		}

		return waitDeadlineEntry;
	}
	
	private JRadioButton getWaitDurationRadioButton(){
			if (waitDurationRadioButton == null) {
				waitDurationRadioButton = new JRadioButton(WAIT_DURATION);
				waitDurationRadioButton.setActionCommand(WAIT_DURATION);
//				waitDurationRadioButton.addActionListener(this);
			}
			return waitDurationRadioButton;
	}
	
	private JRadioButton getWaitDeadlineRadioButton(){
		if (waitDeadlineRadioButton == null) {
			waitDeadlineRadioButton = new JRadioButton(WAIT_DEADLINE);
			waitDeadlineRadioButton.setActionCommand(WAIT_DEADLINE);
//			waitDurationRadioButton.addActionListener(this);
		}
		return waitDeadlineRadioButton;
	}
	
	private JLabel getTimeLabel(){
		if (timeLabel == null)
        {
        	timeLabel = new JLabel(Messages.getString("Transition.Properties.BPELActivityWait.Time") + ":");
        }

        return timeLabel;
	}
	
	private JTextField getTimeTextField() {
		if (timeTextField == null) {
			timeTextField = new JTextField();
			timeTextField.setPreferredSize(new Dimension(100, 20));
			timeTextField.setMinimumSize(new Dimension(150, 20));
			timeTextField.setMaximumSize(new Dimension(150, 20));
			/*nameTextField.addKeyListener(new KeyListener() {
				public void keyPressed(KeyEvent e) {
					keyReleased(e);
				}

				public void keyTyped(KeyEvent e) {
					keyReleased(e);
				}

				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						apply();
						TransitionPropertyEditor.this.dispose();
					}
				}
			});*/
		}

		return timeTextField;
}
	
}
