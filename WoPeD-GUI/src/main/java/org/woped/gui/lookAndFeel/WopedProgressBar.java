package org.woped.gui.lookAndFeel;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JProgressBar;

import org.woped.gui.translations.Messages;

/**
 * 
 * test
 *
 */
public class WopedProgressBar extends JProgressBar {

	public WopedProgressBar() {
		super();
		super.setFont(new Font("Lucia Grande", Font.PLAIN, 13));
		setForeground(Color.DARK_GRAY);
		this.setStringPainted(true);
		setValue(this.getMaximum());
		setVisible(true);

	}

	public void connectingToSeverFailed() {

		this.setIndeterminate(false);
		this.setString(Messages
				.getString("Apromore.UI.ServerConnectionFailure"));
	}

	public void connectingToSeverSuccess() {
		this.setIndeterminate(false);
		this.setString(Messages.getString("Apromore.UI.ServerConnectionSucess"));
	}

	public void connectToServerLoading() {
		this.setIndeterminate(true);
		this.setString(Messages.getString("Apromore.UI.ServerConnectionTry"));

	}

}
