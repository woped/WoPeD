package export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import models.BPMNModel;
import bpmn.ComplexGateway;
import bpmn.EventBasedGateway;
import bpmn.ExclusiveGateway;
import bpmn.InclusiveGateway;
import bpmn.Lane;
import bpmn.Message;
import bpmn.ParallelGateway;
import bpmn.Pool;
import bpmn.SequenceFlow;
import bpmn.Task;

public class BPMNExporter extends Exporter{
	
	private String start = "<definitions id=\"Definition\"" + 
            "\ntargetNamespace=\"http://www.example.org/MinimalExample\"" +
            "\ntypeLanguage=\"http://www.java.com/javaTypes\"" + 
            "\nexpressionLanguage=\"http://www.mvel.org/2.0\"" + 
            "\nxmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"" + 
            "\nxmlns:xs=\"http://www.w3.org/2001/XMLSchema-instance\"" + 
            "\nxs:schemaLocation=\"http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd\"" +
            "\nxmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\"" +
            "\nxmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\"" +
            "\nxmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\"" +
            "\nxmlns:tns=\"http://www.jboss.org/drools\">";
	
	private String end = "\n</process>"
			+ "\n</definitions>";
	
	private StringBuffer bpmn = new StringBuffer(start);
	
	private BPMNModel model = null;
	
	private File outputFile;
	
	public BPMNExporter (BPMNModel bpmnm){
		model = bpmnm;
		init(model);
	}
	
	private void init (BPMNModel bpmnm){
		String id = bpmnm.getId();
		String name = bpmnm.getProcessName();
		bpmn.append("\n\n<process processType=\"Private\" isExecutable=\"true\" id=\"" + id + "\""
					+" name=\"" + name + "\">");
	}
	
	public void addFlowObjects (ArrayList<Task> tasks){
		for (Task t : tasks){
			String id = t.getId();
			String name = t.getName();
			bpmn.append("\n<task id=\"" + id + "\" name=\"" + name + "\"/>");
		}
	}
	
	public void addFlows(ArrayList<SequenceFlow> flows){
		for (SequenceFlow f : flows){
			if(f.getSource()!=null && f.getTarget()!=null){
				String id = f.getId();
				String source = f.getSource().getId();
				String target = f.getTarget().getId();
				bpmn.append("\n<sequenceFlow id=\"" + id + "\" sourceRef=\"" + source + "\" targetRef=\"" + target + "\"/>");
			}
		}
	}
	
	public void export(File outputFile){
		if (outputFile == null){
			this.outputFile = new File("BPMN.bpmn");
		} else {
			this.outputFile = outputFile;
		}
		try {
			  BufferedWriter out = new BufferedWriter(
			                       new FileWriter(this.outputFile));
			  String outText = bpmn.toString();
			  out.write(outText);
			  out.close();
			    }
			catch (IOException e)
			    {
			    e.printStackTrace();
			    }
	}

	public String export(){

			String outText = bpmn.toString();
			return outText;
	}

	
	public void addLanes(ArrayList<Lane> lanes){
		bpmn.append("\n<laneSet id=\"4711\">");
		for (Lane l : lanes){
			String id = l.getId();
			String name = l.getName();
			bpmn.append("\n<lane name=\"" + name + "\" id=\"" + id + "\"/>");
		}
		bpmn.append("\n</laneSet>");
	}
	
	public void addPools(ArrayList<Pool> pools){
		for (Pool p : pools){
			String id = p.getId();
			String name = p.getName();
			bpmn.append("\n<collaboration id=\"" + id + "\">"
					+ "\nparticipant name=\"" + name + "\" processRef=\""
					+ model.getId() + "\" id=\"" + id + "\"/>"
					+ "\n</collaboration>");
		}
	}
	
	public void addGateways(ArrayList<ComplexGateway> com, ArrayList<EventBasedGateway> eBas,
			ArrayList<ExclusiveGateway> ex, ArrayList<InclusiveGateway> inc, ArrayList<ParallelGateway> par){
		for (ComplexGateway g:com){
			String id = g.getId();
			bpmn.append("\n<complexGateway id=\"" + id + "\"/>");
		}
		for (EventBasedGateway g:eBas){
			String id = g.getId();
			bpmn.append("\n<eventBasedGateway id=\"" + id + "\"/>");
		}
		for (ExclusiveGateway g:ex){
			String id = g.getId();
			bpmn.append("\n<exclusiveGateway id=\"" + id + "\"/>");
		}
		for (InclusiveGateway g:inc){
			String id = g.getId();
			bpmn.append("\n<inclusiveGateway id=\"" + id + "\"/>");
		}
		for (ParallelGateway g:par){
			String id = g.getId();
			bpmn.append("\n<parallelGateway id=\"" + id + "\"/>");
		}		
	}
	
	public void end (){
		bpmn.append(end);
	}

}
