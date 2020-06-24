package org.woped.file.controller.vep;

import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import org.woped.gui.translations.Messages;

public class MassMetricsStatus extends Thread{

	private MassMetricsCalculator mass;
	
	/**
	 * Threaded status bar used for mass metrics calculation
	 * 
	 * @param mass	Mass Calculator from which status information is gathered
	 */
	public MassMetricsStatus(MassMetricsCalculator mass){
		this.mass = mass;
	}
	
	/**
	 * Separate Thread to display the proper status of the calculation
	 */
	@Override
	public void run(){
		int max = mass.getMetricsCount();
		int current = 0;
		
		JFrame jfUI = new JFrame();
		jfUI.setTitle(Messages.getString("Metrics.Mass.StatusTitle"));
		jfUI.setSize(300,100);
		jfUI.setLayout(new GridLayout(1,1));
		jfUI.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2-jfUI.getWidth()/2, Toolkit.getDefaultToolkit().getScreenSize().height/2-jfUI.getHeight()/2);
		JProgressBar jpb = new JProgressBar();
		jpb.setStringPainted(true);
		jpb.setMaximum(max);
		jfUI.add(jpb);
		jfUI.setAlwaysOnTop(true);
		jfUI.setVisible(true);
		
		while(current < max){
			try{Thread.sleep(100);}catch(InterruptedException ie){}
			current = mass.getStatus();
			jpb.setValue(current);
		}
		jfUI.setVisible(false);
		JOptionPane.showMessageDialog(null, Messages.getString("Metrics.Mass.Done"));
	}
	
}
