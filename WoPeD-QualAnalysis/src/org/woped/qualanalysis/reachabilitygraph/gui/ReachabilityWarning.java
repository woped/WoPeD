package org.woped.qualanalysis.reachabilitygraph.gui;

import java.awt.Component;

import javax.swing.JOptionPane;

import org.woped.translations.Messages;

public class ReachabilityWarning {
	public static void showSimulationRunningWarning(Component parentComponent){
		JOptionPane.showMessageDialog(parentComponent,
				Messages.getString("QuanlAna.ReachabilityGraph.SimulationWarning.Message"),  // message
				Messages.getString("QuanlAna.ReachabilityGraph.SimulationWarning.Title"), // title
			    JOptionPane.WARNING_MESSAGE); // type
	}
}
