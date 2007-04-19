package org.woped.quantana.simulation.output;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.woped.quantana.simulation.Server;
import org.woped.quantana.simulation.Simulator;

public class SimOutputDialog extends JDialog{
	
	private static final long serialVersionUID = 2L;
	private JPanel jContentPane = null;
	private JTree itemTree = null;
//	private JPanel content = new JPanel();
	private JPanel contentPanel = new JPanel();
	private JScrollPane contentPane = new JScrollPane(contentPanel);
	private JPanel curPanel = null;
//	private JScrollPane curPanel = null;
	private JSplitPane splitPane = null;
	private HashMap<String, JPanel> panelList = new HashMap<String, JPanel>();
//	private HashMap<String, JScrollPane> panelList = new HashMap<String, JScrollPane>();
	
	private Simulator simulator;
	
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
	
	/*private JList getList(){
		if (itemList == null){
			Vector<String> items = new Vector<String>();
			items.add("Protocol");
			panelList.add(new ProtocolPanel());
			items.add("Process");
			panelList.add(new ProcessPanel());
			for (Server s : simulator.getServerList().values()){
				items.add(s.toString());
				panelList.add(new ServerPanel(s.toString()));
			}
			
			generatePanelContent();

			itemList = new JList(items);
			itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
			itemList.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent event){
					//String select = (String)itemList.getSelectedValue();
					int idx = itemList.getSelectedIndex();
					updatePanelView(idx);
				}
			});
		}
		
		return itemList;
	}*/
	
	private JTree getTree(){
		if (itemTree == null){
			DefaultMutableTreeNode root = new DefaultMutableTreeNode("Items");
			DefaultMutableTreeNode curRoot = root;
			
			DefaultMutableTreeNode proto = new DefaultMutableTreeNode("Protocol");
			curRoot.add(proto);
//			panelList.add(new ProtocolPanel());
			panelList.put("Protocol", new ProtocolPanel());
			DefaultMutableTreeNode proc = new DefaultMutableTreeNode("Process");
			curRoot.add(proc);
//			panelList.add(new ProcessPanel());
			panelList.put("Process", new ProcessPanel());
			curRoot = proc;
			
			for (Server s : simulator.getServerList().values()){
				String name = s.getName();
				String id = s.getId();
				curRoot.add(new DefaultMutableTreeNode(s.toString()));
//				panelList.add(new ServerPanel(s.toString()));
				panelList.put(s.getId(), new ServerPanel(id, name));
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
			JTextField txtOutput = new JTextField("anonym");
//			JPanel p = (JPanel)(((JViewport)s.getComponent(0)).getView());
			
			if (p instanceof ProtocolPanel){
				txtOutput.setText("Protocol");
				p.add(txtOutput);
			} else if (p instanceof ProcessPanel){
				txtOutput.setText("Process");
				p.add(txtOutput);
			} else {
//				txtOutput.setText(p.getName());
				ServerPanel q = (ServerPanel)p;
				Server s = simulator.getServerList().get(q.getId());
				q.setValues(
						s.getNumCalls(),
						s.getBusy(),
						s.getQueueLen(),
						s.getMaxWaitTimeOfCase(),
						s.getMaxQueueLength(),
						s.getZeroDelays(),
						s.getNumAccess(),
						s.getNumDeparture(),
						s.getMaxNumCasesInParallel(),
						s.getNumCasesInParallel());
			}
			
//			p.add(txtOutput);
		}
	}
	
//	private void updatePanelView(int index){
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
}
