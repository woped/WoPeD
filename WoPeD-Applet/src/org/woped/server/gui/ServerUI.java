package org.woped.server.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.woped.server.IServer;
import org.woped.server.ServerImpl;

public class ServerUI extends JFrame {

	private IServer server;
	
	
	private JButton close;
	private JButton start;
	
	
	private JTextField port;
	private JTextField servicename;
	private JTextField host;
	
	
	
	public ServerUI() {
		init();
	}
	
	
	/**
	 * Init the UI
	 */
	private void init() {
		setVisible(false);
		setUndecorated(false);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		getContentPane().setLayout(new BorderLayout());
		
		add(getMasterPane(),BorderLayout.CENTER);
		add(getButtonPane(),BorderLayout.SOUTH);
		
		pack();
		
		
		
		setSize(300, 200);
		
		setVisible(true);
						
	}
	
	
	private Component getButtonPane() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(getStartButton());
		return buttonPanel;
	}

	private JButton getStartButton() {
		
		if (start == null) {
			start = new JButton("Start");
			start.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (host.getText().equals("") || port.getText().equals("") || 
							servicename.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "Host, Port, Servicename are not specific!");
					}
					initServer();
					
				}

				private void initServer() {
					try {
						LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
						Naming.bind("//"+host.getText()+":"+port.getText()+"/"+servicename.getText(), new ServerImpl());
						start.setEnabled(false);
					} catch (RemoteException e) {
						JOptionPane.showMessageDialog(null, e.getMessage());
					} catch (MalformedURLException e) {
						JOptionPane.showMessageDialog(null, e.getMessage());
					} catch (AlreadyBoundException e) {
						JOptionPane.showMessageDialog(null, e.getMessage());
					}
					
				}
				
			});
			start.setActionCommand("START");
			//start.setIcon(Messages.getImageIcon("Button.OK"));
		}

		return start;
	}


	private Component getMasterPane() {
		JPanel pane = new JPanel();
		
		pane.setLayout(new BorderLayout());
		pane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		pane.add(getHostPane(),BorderLayout.CENTER);
		
		return pane;
	}


	private Component getHostPane() {
		JPanel pane = new JPanel();
		pane.setLayout(new GridLayout(6,1));
		
		JLabel label = new JLabel("Host");
		pane.add(label);
		
		String ip = "";
		
		try {
			InetAddress host = InetAddress.getByName("localhost");
			ip = host.getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		host = new JTextField(ip);
		pane.add(host);
		
		label = new JLabel("Port");
		pane.add(label);
		
		port = new JTextField("1099");
		pane.add(port);
		
		label = new JLabel("Servicename");
		pane.add(label);
		
		servicename = new JTextField("WopedService");
		pane.add(servicename);
		
		return pane;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ServerUI();

	}

}	