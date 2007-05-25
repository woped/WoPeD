package org.woped.quantana.utilities;

import org.woped.editor.utilities.Messages;
import org.woped.quantana.gui.QuantitativeSimulationDialog;
import org.woped.quantana.model.ResUtilTableModel;
import org.woped.quantana.model.ServerTableModel;
import org.woped.quantana.simulation.Server;
import org.woped.quantana.simulation.Simulator;

public class ExportStatistics {
	
	private QuantitativeSimulationDialog qsd;
	private Simulator sim;
	private byte[] protocol;
	
	public ExportStatistics(QuantitativeSimulationDialog qsd){
		this.qsd = qsd;
		this.sim = qsd.getSimulator();
		this.protocol = sim.getProtocolContent();
	}
	
	public String getStatsTable(){
		String text = "";
		ServerTableModel stm = qsd.getServerTableModel();
		ResUtilTableModel rtm = qsd.getResUtilTableModel();
		
		String pro = Messages.getString("QuantAna.Simulation.Process") + ";";
		pro += Messages.getString("QuantAna.Simulation.Column.W") + ";";
		pro += Messages.getString("QuantAna.Simulation.Column.Wq") + ";";
		pro += Messages.getString("QuantAna.Simulation.Details.Clock") + ";";
		pro += Messages.getString("QuantAna.Simulation.Details.FinishedCases") + ";";
		pro += Messages.getString("QuantAna.Simulation.Details.MaxCasesInSystem") + "\n";
		
		String proD = ";" + Double.parseDouble((String)stm.getValueAt(0, 4)) + ";";
		proD += Double.parseDouble((String)stm.getValueAt(0, 5)) + ";";
		proD += Double.parseDouble(String.format("%,.2f", sim.getClock())) + ";";
		proD += Double.parseDouble(String.format("%d", sim.getFinishedCases())) + ";";
		proD += Double.parseDouble(String.format("%d", sim.getMaxNumCasesInSystem())) + "\n";
		
		String hdg = Messages.getString("QuantAna.Simulation.Server") + ";";
		hdg += Messages.getString("QuantAna.Simulation.Column.L") + ";";
		hdg += Messages.getString("QuantAna.Simulation.Column.Lq") + ";";
		hdg += Messages.getString("QuantAna.Simulation.Column.Ls") + ";";
		hdg += Messages.getString("QuantAna.Simulation.Column.W") + ";";
		hdg += Messages.getString("QuantAna.Simulation.Column.Wq") + ";";
		hdg += Messages.getString("QuantAna.Simulation.Details.Utilization") + ";";
		hdg += Messages.getString("QuantAna.Simulation.Details.ZeroDelays") + ";";
		hdg += Messages.getString("QuantAna.Simulation.Details.NumCalls") + ";";
		hdg += Messages.getString("QuantAna.Simulation.Details.NumAccess") + ";";
		hdg += Messages.getString("QuantAna.Simulation.Details.NumDeparture") + ";";
		hdg += Messages.getString("QuantAna.Simulation.Details.MaxQueueLength") + ";";
		hdg += Messages.getString("QuantAna.Simulation.Details.MaxCasesParallel") + ";";
		hdg += Messages.getString("QuantAna.Simulation.Details.MaxWaitTime") + "\n";
		
		String srvD = "";
		String servName;
		Server server;
		for (int i = 1; i < stm.getRowCount(); i++){
			servName = (String)stm.getValueAt(i, 0);
			srvD += servName + ";";
			
			String servID = servName.substring(servName.indexOf("(") + 1, servName.indexOf(")"));
			server = sim.getServerList().get(servID);
			
			for (int j = 1; j < stm.getColumnCount(); j++)
				srvD += Double.parseDouble((String)stm.getValueAt(i, j)) + ";";
			
			srvD += Double.parseDouble(String.format("%,.2f", server.getBusy() / sim.getClock() * 100)) + ";";
			srvD += Double.parseDouble(String.format("%d", server.getZeroDelays())) + ";";
			srvD += Double.parseDouble(String.format("%d", server.getNumCalls())) + ";";
			srvD += Double.parseDouble(String.format("%d", server.getNumAccess())) + ";";
			srvD += Double.parseDouble(String.format("%d", server.getNumDeparture())) + ";";
			srvD += Double.parseDouble(String.format("%d", server.getMaxQueueLength())) + ";";
			srvD += Double.parseDouble(String.format("%d", server.getMaxNumCasesInParallel())) + ";";
			srvD += Double.parseDouble(String.format("%,.2f", server.getMaxWaitTimeOfCase())) + "\n";
		}
		
		String res = "\n\n";
		res += Messages.getString("QuantAna.Simulation.Column.Object") + ";";
		res += Messages.getString("QuantAna.Simulation.Column.Util") + "\n";
		for (int i = 0; i < rtm.getRowCount(); i++)
			res += (String)rtm.getValueAt(i, 0) + ";" + Double.parseDouble((String)rtm.getValueAt(i, 1)) + "\n";
		
		text += pro + proD + "\n" + hdg + srvD + "\n" + res;
		
		return text;
	}
	
	public String getResourceStats(){
		String text = "";
		
		return text;
	}
	
	public String getServerStats(){
		String text = "";
		
		return text;
	}
}
