package org.woped.qualanalysis.reachabilitygraph.gui;

import java.awt.Component;
import javax.swing.JOptionPane;
import org.woped.translations.Messages;

/**
 * ReachabilityGraph implementation was done by Manuel Fladt and Benjamin Geiger.
 * The code was written for a project at BA Karlsruhe in 2007/2008 under authority
 * of Prof. Dr. Thomas Freytag and Andreas Eckleder.
 * 
 * This class was written by
 * @author Benjamin Geiger
 */

/**
 * New methods "showRefreshWarning" and "showUnselectWarning"
 * 
 * @author <a href="mailto:b.joerger@gmx.de">Benjamin Joerger</a>
 * @since 02.01.2009 
 */

public class ReachabilityWarning {
	public static void showSimulationRunningWarning(Component parentComponent){
		JOptionPane.showMessageDialog(parentComponent,
				Messages.getString("QuanlAna.ReachabilityGraph.SimulationWarning.Message"),  // message
				Messages.getString("QuanlAna.ReachabilityGraph.SimulationWarning.Title"), // title
			    JOptionPane.WARNING_MESSAGE); // type
	}
	public static void showRefreshWarning(Component parentComponent){
		JOptionPane.showMessageDialog(parentComponent,
				Messages.getString("QuanlAna.ReachabilityGraph.RefreshWarning.Message"),  // message
				Messages.getString("QuanlAna.ReachabilityGraph.RefreshWarning.Title"), // title
				
			    JOptionPane.WARNING_MESSAGE); // type
	}
	public static void showUnselectWarning(Component parentComponent){
		JOptionPane.showMessageDialog(parentComponent,
				Messages.getString("QuanlAna.ReachabilityGraph.UnselectWarning.Message"),  // message
				Messages.getString("QuanlAna.ReachabilityGraph.UnselectWarning.Title"), // title
				
			    JOptionPane.WARNING_MESSAGE); // type
	}
}
