package export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import nodes.ProcessNode;
import models.EPCModel;
import epc.Connector;
import epc.Event;
import epc.Function;
import epc.Organisation;
import epc.SequenceFlow;

public class EPCExporter extends Exporter{
	
	private String start = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "\n<epml:epml xmlns:epml=\"http://www.epml.de\">";
	private String end = "\n</epc>\n</directory>\n</epml:epml>";
	
	private StringBuffer epml = new StringBuffer(start);
	
	private EPCModel model = null;
	
	private File outputFile;
	
	public EPCExporter (EPCModel epcm){
		model = epcm;
		init();
	}
	
	public void init (){
		String name = model.getProcessName();
		epml.append("\n<directory name=\"Root\">"
				+ "\n<epc epcId=\"1\" name=\"" + name + "\">");
	}
	
	public void export(File outputFile){
		if (outputFile == null){
			this.outputFile = new File("EPK.epml");
		} else {
			this.outputFile = outputFile;
		}
		try {
			  BufferedWriter out = new BufferedWriter(
			                       new FileWriter(this.outputFile));
			  String outText = epml.toString();
			  out.write(outText);
			  out.close();
			    }
			catch (IOException e)
			    {
			    e.printStackTrace();
			    }
	}

	public String export(){
		return epml.toString();
	}
	
	public void addFunctions(ArrayList<Function> functions){
		for (Function f : functions){
			String id = f.getId();
			String text = f.getText();
			epml.append("\n<function id=\"" + id + "\">"
					+ "\n<name>" + text + "</name>");
			if (model.getClusterForNode(f) != null){
				String refID = model.getClusterForNode(f).getId();
				epml.append("\n<relation from =\"" + id +
						"\" to =\"" + refID + "\"/>");
			}
			epml.append("\n</function>");
		}
	}
	
	public void addOrgs(ArrayList<Organisation> orgs){
		for (Organisation o : orgs){
			String id = o.getId();
			String text = o.getText();
			epml.append("\n<participant id=\"" + id + "\">"
					+ "\n<name>" + text + "</name>"
					+ "\n</participant>");
		}
	}
	
	public void addFlows(ArrayList<epc.SequenceFlow> arcs){
		for (SequenceFlow flow : arcs){
			ProcessNode source = flow.getSource();
			ProcessNode target = flow.getTarget();
			if (source != null && target != null){
				String sid = source.getId();
				String tid = target.getId();
				String id = flow.getId();
				epml.append("\n<arc id=\"" + id + "\">"
						+ "\n<flow source=\"" + sid + "\" target=\"" + tid + "\"/>"
								+ "\n</arc>");
			}
		}
	}
	
	public void addEvents(ArrayList<Event> events){
		for (Event e : events){
			String id = e.getId();
			String text = e.getText();
			epml.append("\n<event id=\"" + id + "\">"
					+ "\n<name>" + text + "</name>\n</event>");
		}
	}
	
	public void addConnectors(ArrayList<Connector> ca, ArrayList<Connector> co, ArrayList<Connector> cx){
		for (Connector and : ca){
			String id = and.getId();
			epml.append("\n<and id=\"" + id + "\"/>");
		}
		for (Connector or : co){
			String id = or.getId();
			epml.append("\n<or id=\"" + id + "\"/>");
		}
		for (Connector xor : cx){
			String id = xor.getId();
			epml.append("\n<xor id=\"" + id + "\"/>");
		}
	}
	
	public void end (){
		epml.append(end);
	}
	
	

}
