package org.woped.quantana.utilities;

import java.util.ResourceBundle;

import org.woped.editor.utilities.Messages;
import org.woped.quantana.gui.QuantitativeSimulationDialog;
import org.woped.quantana.model.ReportServerStats;
import org.woped.quantana.model.ReportStats;
import org.woped.quantana.model.ResourceStats;
import org.woped.quantana.model.RunStats;
import org.woped.quantana.model.ServerStats;
import org.woped.quantana.simulation.Server;
import org.woped.quantana.simulation.Simulator;

public class ExportStatistics {
	
//	private QuantitativeSimulationDialog qsd;
	private Simulator sim;
	private RunStats runStats;
//	private byte[] protocol;
//	private String text;
//	private HashMap<String, String> resList = new HashMap<String, String>();
	
	private static final ResourceBundle ENTRY = Simulator.getENTRY();
	
	public ExportStatistics(QuantitativeSimulationDialog qsd){
//		this.qsd = qsd;
		this.sim = qsd.getSimulator();
//		this.protocol = sim.getProtocolContent();
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
//		hdg1 += "\n\n";
		
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
	
	/*public String getResourceStats(){
		text = "";
		
		try {
			XMLReader xr = XMLReaderFactory.createXMLReader();
			DefaultHandler handler = new DefaultHandler(){
				
				private boolean msg = false;
				
				public void startDocument(){
					
				}
				
				public void endDocument(){
					
				}
				
				public void startElement(String uri, String lname,
						String qname, Attributes attr) {
					if (lname.equalsIgnoreCase("message")) msg = true;
				}
				
				public void endElement(String uri, String lname, String qname) {
					if (lname.equalsIgnoreCase("message")) msg = false;
				}
				
				public void characters(char[] ch, int start, int length) {
					if (msg){
						String s = String.copyValueOf(ch, start, length);
						int t1 = s.indexOf("(");
						
						if (t1 < 0) return;
						
						int t2 = s.indexOf(")", t1+1);
						
						if (t2 < 0) return;
						
						String time = s.substring(t1+1, t2);
						int d = s.indexOf(":");
						s = s.substring(d+2);
						
						if (s.startsWith(ENTRY.getString("Export.Resource"))){
							int i = s.indexOf('"');
							
							if (i < 0) return;
							
							int j = s.indexOf('"', i+1);
							String name = s.substring(i+1, j);

							if (s.endsWith(ENTRY.getString("Export.Freed"))) {
								String line = resList.get(name);
								line += ENTRY.getString("Export.Stop") + time + ";";
								
								text += line + "\n";
								resList.remove(name);
							} else {
								i = s.indexOf('"', j+1);
								j = s.indexOf('"', i+1);
								String server = s.substring(i+1, j);

								if (!resList.containsKey(name)) {
									String line = ENTRY.getString("Export.Server") + server + ";";
									resList.put(name, line);
								} else {
									
								}
							}
						}
						
						if (s.startsWith(ENTRY.getString("Export.Service"))) {
							int i = s.indexOf("# ");
							int j = s.indexOf(" ", i+2);
							String cNo = s.substring(i+2, j);
							
							i = s.indexOf('"');
							j = s.indexOf('"', i+1);
//							i = s.indexOf('"', j+1);
//							j = s.indexOf('"', i+1);
							String name = s.substring(i+1, j);
							
							String line = resList.get(name);
							line += ENTRY.getString("Export.Case") + cNo + ";";
						}
					}
				}
				
			};
			xr.setContentHandler(handler);
			xr.setErrorHandler(handler);
			ByteArrayInputStream in = new ByteArrayInputStream(protocol);
			xr.parse(new InputSource(in));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return text;
	}
	
	public String getServerStats(){
		String text = "";
		
		try {
			XMLReader xr = XMLReaderFactory.createXMLReader();
			DefaultHandler handler = new DefaultHandler(){
				public void startDocument(){
					
				}
				
				public void endDocument(){
					
				}
				
				public void startElement(String uri, String lname,
						String qname, Attributes attr) {
					
				}
				
				public void endElement(String uri, String lname, String qname) {
					
				}
				
				public void characters(char[] ch, int start, int length) {
					
				}
				
			};
			xr.setContentHandler(handler);
			xr.setErrorHandler(handler);
			ByteArrayInputStream in = new ByteArrayInputStream(protocol);
			xr.parse(new InputSource(in));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return text;
	}*/
}
