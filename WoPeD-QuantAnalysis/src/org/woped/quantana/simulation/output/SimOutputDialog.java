package org.woped.quantana.simulation.output;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.io.File;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.woped.core.utilities.LoggerManager;
import org.woped.quantana.gui.Constants;
import org.woped.quantana.simulation.Server;
import org.woped.quantana.simulation.Simulator;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class SimOutputDialog extends JDialog{
	
	private static final long serialVersionUID = 2L;
	
	private JPanel jContentPane = null;
	private JTree itemTree = null;
	private JPanel contentPanel = new JPanel();
	private JScrollPane contentPane = new JScrollPane(contentPanel);
	private JPanel curPanel = null;
	private JSplitPane splitPane = null;
	private HashMap<String, JPanel> panelList = new HashMap<String, JPanel>();
	
	private Simulator simulator;
	private String protocolText = "";
	private SAXParser parser;
	private DefaultHandler handler;
	
	public SimOutputDialog(Frame owner, boolean modal, Simulator sim) {
		super(owner, modal);
		simulator = sim;
		
		initialize();
		
		curPanel = panelList.get("Protocol");
		contentPanel.add(curPanel);
		curPanel.setVisible(true);
		
		validate();
	}
	
	private void initialize() {
		this.setSize(800, 600);
		this.setResizable(false);
		this.setLocation(150, 40);
		this.setContentPane(getJContentPane());
		this.setTitle("Simulation Output");
	}
	
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getSplitPane(), BorderLayout.CENTER);
		}
		return jContentPane;
	}
	
	private JSplitPane getSplitPane(){
		if (splitPane == null){
			JScrollPane scrollPane = new JScrollPane(getTree());
			scrollPane.setWheelScrollingEnabled(true);
			splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, contentPane);
			splitPane.setOneTouchExpandable(true);
			splitPane.setDividerSize(8);
			splitPane.setDividerLocation(200);
			splitPane.setResizeWeight(1);
			//splitPane.setMinimumSize(new Dimension(100, (int)splitPane.getSize().getHeight()));
		}
		
		return splitPane;
	}
	
	private JTree getTree(){
		if (itemTree == null){
			DefaultMutableTreeNode root = new DefaultMutableTreeNode("Items");
			DefaultMutableTreeNode curRoot = root;
			
			DefaultMutableTreeNode proto = new DefaultMutableTreeNode("Protocol");
			curRoot.add(proto);
			panelList.put("Protocol", new ProtocolPanel(this));
			DefaultMutableTreeNode proc = new DefaultMutableTreeNode("Process");
			curRoot.add(proc);
			panelList.put("Process", new ProcessPanel(this));
			curRoot = proc;
			
			for (Server s : simulator.getServerList().values()){
				String name = s.getName();
				String id = s.getId();
				curRoot.add(new DefaultMutableTreeNode(s.toString()));
				panelList.put(s.getId(), new ServerPanel(this, id, name));
			}
			
			generatePanelContent();

			itemTree = new JTree(root);
			itemTree.setRootVisible(false);
			itemTree.putClientProperty("JTree.lineStyle", "Angled");
			
			itemTree.setCellRenderer(new TreeRenderer());
			
			itemTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			itemTree.addTreeSelectionListener(new TreeSelectionListener(){
				public void valueChanged(TreeSelectionEvent e){
					TreePath path = itemTree.getSelectionPath();
					if (path == null) return;
					DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)path.getLastPathComponent();
					String s = (String)selectedNode.getUserObject();
					updatePanelView(s);
				}
			});
		}

		return itemTree;
	}
	
	private void generatePanelContent(){
		for (JPanel p : panelList.values()){
			if (p instanceof ProtocolPanel){
				
			} else if (p instanceof ProcessPanel){
				
			} else {
				ServerPanel q = (ServerPanel)p;
				Server s = simulator.getServerList().get(q.getId());
				double t = simulator.getClock();
				q.setValues(
						s.getNumCalls(),
						s.getBusy() / t,
						s.getQueueLen() / t,
						s.getMaxWaitTimeOfCase(),
						s.getMaxQueueLength(),
						s.getZeroDelays(),
						s.getNumAccess(),
						s.getNumDeparture(),
						s.getMaxNumCasesInParallel(),
						s.getNumCasesInParallel());
			}
		}
	}
	
	private void updatePanelView(String key){
		String id = produceID(key);
		
		contentPanel.getComponent(0).setVisible(false);
		contentPanel.remove(curPanel);
		curPanel = panelList.get(id);
		contentPanel.add(curPanel);
		curPanel.setVisible(true);
	}
	
	private String produceID(String key){
		if (key.equals("Protocol") || key.equals("Process")) return key;
		else return key.substring(key.indexOf("(") + 1, key.indexOf(")"));
	}

	public Simulator getSimulator() {
		return simulator;
	}

	public void setSimulator(Simulator simulator) {
		this.simulator = simulator;
	}
	
	public String getProtocol(){
		File f = new File(simulator.getProtocolName());
		
		LoggerManager.info(Constants.QUANTANA_LOGGER, "File: " + f.toString());
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		
		try {
			handler = new DefaultHandler(){
				
				private long min = new Date().getTime();
				private long max = 0;
				private int count = 0;
				private int rec = 0;
				
				public void startDocument(){
					protocolText += "--- Protocol Start ---\n\n";
				}
				
				public void startElement(String uri, String lname, String qname, Attributes attr){
//					long val = 0;
					
					try {
						/*if (lname.equalsIgnoreCase("millis")){
							val = Long.parseLong(attr.getValue(0));
							if (val < min) min = val;
							if (val > max) max = val;
							
//							protocolText += uri + " | " + lname + " | " + qname + " | " + attr.toString() + "\n";
						}
						
						if (lname.equalsIgnoreCase("message")){
							protocolText += attr.getValue(0) + "\n";
						}*/
						
						if (lname.equalsIgnoreCase("record")) rec++;
						
						if (lname.equalsIgnoreCase("date")) count = 0;
						
					} catch (Exception e) {
						//e.printStackTrace();
					}
				}
				
				public void characters(char[] ch, int start, int length){
					count++;
					if (rec == 1 && count == 2){
						String s = String.copyValueOf(ch);
						long l = Long.parseLong(s);
						min = l;
						max = l;
					}
					
					if (rec > 1 && count == 2){
						String s = String.copyValueOf(ch);
						long l = Long.parseLong(s);
						if (l > max) max = l;
					}
					
					if (count == 9){
						String s = String.copyValueOf(ch);
						protocolText += s + "\n";
					}
				}
				
				public void endElement(String uri, String lname, String qname){
					rec = 0;
				}

				/*public void endDocument(){
					protocolText += "\n\nsimulation took " + (max - min) + " ms";
					protocolText += "\n\n--- Protocol End ---";
				}*/
			};

			parser = factory.newSAXParser();
			
			try {
				parser.parse(f, handler);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return protocolText;
	}
}
