package org.woped.quantana.utilities;

import java.util.ResourceBundle;

import org.woped.translations.Messages;
import org.woped.quantana.gui.QuantitativeSimulationDialog;
import org.woped.quantana.model.ReportServerStats;
import org.woped.quantana.model.ReportStats;
import org.woped.quantana.model.ResourceStats;
import org.woped.quantana.model.RunStats;
import org.woped.quantana.model.ServerStats;
import org.woped.quantana.simulation.Server;
import org.woped.quantana.simulation.Simulator;

public class ExportStatistics {
	
	private Simulator sim;
	private RunStats runStats;
	
	private static final ResourceBundle ENTRY = Simulator.getENTRY();
	
	public ExportStatistics(QuantitativeSimulationDialog qsd){
		this.sim = qsd.getSimulator();
	}
	
	public String getStatsTable(int run, boolean isReport){
		runStats = sim.getRunStats().get(run);
		ReportStats repStats = null;
		
		String text = "";
		String hdg1 = "";
		if (isReport)
			hdg1 = ENTRY.getString("Export.Data") + "\n\n";
		else
			hdg1 = ENTRY.getString("Export.Run") + ";" + (run+1) + "\n\n";
		
		String pro = Messages.getString("QuantAna.Simulation.Process") + "\n";
		pro += Messages.getString("QuantAna.Simulation.Column.L") + ";";
		pro += Messages.getString("QuantAna.Simulation.Column.Lq") + ";";
		pro += Messages.getString("QuantAna.Simulation.Column.Ls") + ";";
		pro += Messages.getString("QuantAna.Simulation.Column.W") + ";";
		pro += Messages.getString("QuantAna.Simulation.Column.Wq") + ";";
		pro += Messages.getString("QuantAna.Simulation.Details.Clock") + ";";
		pro += Messages.getString("QuantAna.Simulation.Details.FinishedCases") + ";";
		pro += Messages.getString("QuantAna.Simulation.Details.Throughput") + ";";
		pro += Messages.getString("QuantAna.Simulation.Details.ProcCompletionTime") + ";";
		pro += Messages.getString("QuantAna.Simulation.Details.ProcServiceTime") + ";";
		pro += Messages.getString("QuantAna.Simulation.Details.ProcWaitTime") + "\n";
		
		String proD;
		double l_ = sim.getLambda() / sim.getPeriod();
		if (isReport){
			repStats = (ReportStats)runStats;
			
			proD = Double.parseDouble(String.format("%,.2f", repStats.getProcCompTime() * l_)) + ";";
			proD += Double.parseDouble(String.format("%,.2f", repStats.getProcWaitTime() * l_)) + ";";
			proD += Double.parseDouble(String.format("%,.2f", repStats.getProcServTime() * l_)) + ";";
			proD += Double.parseDouble(String.format("%,.2f", repStats.getProcCompTime())) + ";";
			proD += Double.parseDouble(String.format("%,.2f", repStats.getProcCompTime() - repStats.getProcServTime())) + ";";
			proD += Double.parseDouble(String.format("%,.2f", repStats.getDuration())) + ";";
			proD += Double.parseDouble(String.format("%,.2f", repStats.getAvgFinishedCases())) + ";";
			proD += Double.parseDouble(String.format("%,.2f", repStats.getThroughPut())) + ";";
			proD += Double.parseDouble(String.format("%,.2f", repStats.getProcCompTime())) + ";";
			proD += Double.parseDouble(String.format("%,.2f", repStats.getProcServTime())) + ";";
			proD += Double.parseDouble(String.format("%,.2f", repStats.getProcWaitTime())) + "\n";
		} else {
			proD = Double.parseDouble(String.format("%,.2f", runStats.getProcCompTime() * l_)) + ";";
			proD += Double.parseDouble(String.format("%,.2f", runStats.getProcWaitTime() * l_)) + ";";
			proD += Double.parseDouble(String.format("%,.2f", runStats.getProcServTime() * l_)) + ";";
			proD += Double.parseDouble(String.format("%,.2f", runStats.getProcCompTime())) + ";";
			proD += Double.parseDouble(String.format("%,.2f", runStats.getProcCompTime() - runStats.getProcServTime())) + ";";
			proD += Double.parseDouble(String.format("%,.2f", runStats.getDuration())) + ";";
			proD += Double.parseDouble(String.format("%d", runStats.getFinishedCases())) + ";";
			proD += Double.parseDouble(String.format("%,.2f", runStats.getThroughPut())) + ";";
			proD += Double.parseDouble(String.format("%,.2f", runStats.getProcCompTime())) + ";";
			proD += Double.parseDouble(String.format("%,.2f", runStats.getProcServTime())) + ";";
			proD += Double.parseDouble(String.format("%,.2f", runStats.getProcWaitTime())) + "\n";
		}
		
		String hdg = "";
		hdg += Messages.getString("QuantAna.Simulation.Server") + ";";
		hdg += Messages.getString("QuantAna.Simulation.Column.L") + ";";
		hdg += Messages.getString("QuantAna.Simulation.Column.Lq") + ";";
		hdg += Messages.getString("QuantAna.Simulation.Column.Ls") + ";";
		hdg += Messages.getString("QuantAna.Simulation.Column.W") + ";";
		hdg += Messages.getString("QuantAna.Simulation.Column.Wq") + ";";
		hdg += Messages.getString("QuantAna.Simulation.Details.ZeroDelays") + ";";
		hdg += Messages.getString("QuantAna.Simulation.Details.NumCalls") + ";";
		hdg += Messages.getString("QuantAna.Simulation.Details.NumAccess") + ";";
		hdg += Messages.getString("QuantAna.Simulation.Details.NumDeparture") + ";";
		hdg += Messages.getString("QuantAna.Simulation.Details.MaxQueueLength") + ";";
		hdg += Messages.getString("QuantAna.Simulation.Details.MaxCasesParallel") + ";";
		hdg += Messages.getString("QuantAna.Simulation.Details.MaxWaitTime") + ";";
		hdg += Messages.getString("QuantAna.Simulation.Details.NumServedWhenStopped") + ";";
		hdg += Messages.getString("QuantAna.Simulation.Details.QLenWhenStopped") + "\n";
		
		String srvD = "\n";
		if (isReport){
			Object[] sstAry = repStats.getServStats().values().toArray();
			Object[] srvAry = repStats.getServStats().keySet().toArray();
			
			for (int i = 0; i < repStats.getServStats().size(); i++){
				ReportServerStats sst = (ReportServerStats)sstAry[i];
				Server s = (Server)srvAry[i];
				
				srvD += s.getName() + ";";
				srvD += Double.parseDouble(String.format("%,.2f", sst.getAvgQLength() + sst.getAvgResNumber())) + ";";
				srvD += Double.parseDouble(String.format("%,.2f", sst.getAvgQLength())) + ";";
				srvD += Double.parseDouble(String.format("%,.2f", sst.getAvgResNumber())) + ";";
				srvD += Double.parseDouble(String.format("%,.2f", sst.getAvgServTime() + sst.getAvgWaitTime())) + ";";
				srvD += Double.parseDouble(String.format("%,.2f", sst.getAvgWaitTime())) + ";";
				srvD += Double.parseDouble(String.format("%,.2f", sst.getAvgZeroDelays())) + ";";
				srvD += Double.parseDouble(String.format("%,.2f", sst.getAvgCalls())) + ";";
				srvD += Double.parseDouble(String.format("%,.2f", sst.getAvgAccesses())) + ";";
				srvD += Double.parseDouble(String.format("%,.2f", sst.getAvgDepartures())) + ";";
				srvD += Double.parseDouble(String.format("%,.2f", sst.getAvgMaxQLength())) + ";";
				srvD += Double.parseDouble(String.format("%,.2f", sst.getAvgMaxResNumber())) + ";";
				srvD += Double.parseDouble(String.format("%,.2f", sst.getMaxWaitTime())) + ";";
				srvD += Double.parseDouble(String.format("%,.2f", sst.getAvgNumServedWhenStopped())) + ";";
				srvD += Double.parseDouble(String.format("%,.2f", sst.getAvgQLengthWhenStopped())) + "\n";
			}
		} else {
			Object[] sstAry = runStats.getServStats().values().toArray();
			Object[] srvAry = runStats.getServStats().keySet().toArray();
			
			for (int i = 0; i < runStats.getServStats().size(); i++){
				ServerStats sst = (ServerStats)sstAry[i];
				Server s = (Server)srvAry[i];
				
				srvD += s.getName() + ";";
				srvD += Double.parseDouble(String.format("%,.2f", sst.getAvgQLength() + sst.getAvgResNumber())) + ";";
				srvD += Double.parseDouble(String.format("%,.2f", sst.getAvgQLength())) + ";";
				srvD += Double.parseDouble(String.format("%,.2f", sst.getAvgResNumber())) + ";";
				srvD += Double.parseDouble(String.format("%,.2f", sst.getAvgServTime() + sst.getAvgWaitTime())) + ";";
				srvD += Double.parseDouble(String.format("%,.2f", sst.getAvgWaitTime())) + ";";
				srvD += Double.parseDouble(String.format("%d", sst.getZeroDelays())) + ";";
				srvD += Double.parseDouble(String.format("%d", sst.getCalls())) + ";";
				srvD += Double.parseDouble(String.format("%d", sst.getAccesses())) + ";";
				srvD += Double.parseDouble(String.format("%d", sst.getDepartures())) + ";";
				srvD += Double.parseDouble(String.format("%d", sst.getMaxQLength())) + ";";
				srvD += Double.parseDouble(String.format("%d", sst.getMaxResNumber())) + ";";
				srvD += Double.parseDouble(String.format("%,.2f", sst.getMaxWaitTime())) + ";";
				srvD += Double.parseDouble(String.format("%d", sst.getNumServedWhenStopped())) + ";";
				srvD += Double.parseDouble(String.format("%d", sst.getQLengthWhenStopped())) + "\n";
			}
		}
		
		String res = "\n";
		res += Messages.getString("QuantAna.Simulation.Column.Object") + ";";
		res += Messages.getString("QuantAna.Simulation.Column.Util") + "\n";

		Object[] rstAry = runStats.getResStats().values().toArray();
		for (int i = 0; i < rstAry.length; i++){
			ResourceStats rst = (ResourceStats)rstAry[i];
			res += rst.getName() + ";";
			res += Double.parseDouble(String.format("%,.2f", rst.getUtilizationRatio())) + "\n";
		}
		
		text += hdg1 + pro + proD + "\n" + hdg + srvD + "\n" + res;
		
		return text;
	}
}
