package org.woped.quantana.utilities;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.woped.editor.utilities.Messages;
import org.woped.quantana.gui.QuantitativeSimulationDialog;
import org.woped.quantana.model.ResUtilTableModel;
import org.woped.quantana.model.ServerTableModel;
import org.woped.quantana.simulation.Server;
import org.woped.quantana.simulation.Simulator;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class ExportStatistics {
	
	private QuantitativeSimulationDialog qsd;
	private Simulator sim;
	private byte[] protocol;
	private String text;
	private HashMap<String, String> resList = new HashMap<String, String>();
	
	private static final ResourceBundle ENTRY = Simulator.getENTRY();
	
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
			
			for (int j = 1; j < stm.getColumnCount() - 1; j++)
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
							int j = s.indexOf(" ", i+1);
							String cNo = s.substring(i+1, j);
							
							i = s.indexOf('"');
							j = s.indexOf('"', i+1);
							i = s.indexOf('"', j+1);
							j = s.indexOf('"', i+1);
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
	}
}
