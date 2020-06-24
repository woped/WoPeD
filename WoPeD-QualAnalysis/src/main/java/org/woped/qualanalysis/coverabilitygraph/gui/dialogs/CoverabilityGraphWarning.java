package org.woped.qualanalysis.coverabilitygraph.gui.dialogs;

import java.awt.Component;

import javax.swing.JOptionPane;

import org.woped.gui.translations.Messages;

/**
 * ReachabilityGraph implementation was done by Manuel Fladt and Benjamin Geiger.
 * The code was written for a project at BA Karlsruhe in 2007/2008 under authority
 * of Prof. Dr. Thomas Freytag and Andreas Eckleder.
 * 
 * This class was written by
 * @author Benjamin Geiger
 */
public class CoverabilityGraphWarning {

	public static void showReachabilityWarning(Component parentComponent, String message){
		JOptionPane.showMessageDialog(parentComponent,
				Messages.getString(message + ".Message"),  // message
				Messages.getString(message + ".Title"), // title
			    JOptionPane.WARNING_MESSAGE); // type
	}
}
