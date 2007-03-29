package org.woped.simulation.output;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.woped.simulation.Server;
import org.woped.simulation.Simulator;

public class SimOutputDialog extends JDialog{
	
	private static final long serialVersionUID = 2L;
	private JPanel jContentPane = null;
	private JList itemList = null;
	private JPanel content = new JPanel();
	private JPanel curPanel = null;
	private JSplitPane splitPane = null;
	private Vector<JPanel> panelList = new Vector<JPanel>();
	
	private Simulator simulator;
	
	public SimOutputDialog(Frame owner, boolean modal, Simulator sim) {
		super(owner, modal);
		simulator = sim;
		
		initialize();
		
		curPanel = panelList.get(0);
		content.add(curPanel);
		curPanel.setVisible(true);
		
		validate();
	}
	
	private void initialize() {
		this.setSize(800, 600);
		this.setResizable(false);
		this.setLocation(50, 40);
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
			JScrollPane scrollPane = new JScrollPane(getList());
			splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, content);
			splitPane.setOneTouchExpandable(true);
			splitPane.setDividerSize(8);
			splitPane.setDividerLocation(150);
			splitPane.setResizeWeight(1);
			//splitPane.setMinimumSize(new Dimension(100, (int)splitPane.getSize().getHeight()));
		}
		
		return splitPane;
	}
	
	private JList getList(){
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
		
		//itemList.validate();
		
		return itemList;
	}
	
	private void generatePanelContent(){
		for (JPanel p : panelList){
			JTextField txtOutput = new JTextField("anonym");
			if (p instanceof ProtocolPanel){
				txtOutput.setText("Protocol");
			} else if (p instanceof ProcessPanel){
				txtOutput.setText("Process");
			} else {
				txtOutput.setText(p.getName());
			}
			
			p.add(txtOutput);
		}
	}
	
	private void updatePanelView(int index){
		content.getComponent(0).setVisible(false);
		content.remove(curPanel);
		curPanel = panelList.get(index);
		content.add(curPanel);
		curPanel.setVisible(true);
	}
}
