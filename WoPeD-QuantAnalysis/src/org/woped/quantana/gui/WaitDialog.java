package org.woped.quantana.gui;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class WaitDialog extends JDialog implements Runnable {

	private static final long serialVersionUID = 22L;

	private static final int WIDTH = 200;

	private static final int HEIGHT = 80;

	private Thread thr;
	
	private JDialog owner;
	
	private double start;
	
	private double finish;
		
	private JTextArea txtArea;
	private Container contentPane;
	
	public WaitDialog(JDialog owner, String msg) {
		super(owner, msg, false);
		this.owner = owner;
		
		contentPane = this.getContentPane();
		txtArea = new JTextArea();
		contentPane.add(new JScrollPane(txtArea));
		
		owner.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screenSize.width - WIDTH) / 2;
		int y = (screenSize.height - HEIGHT) / 2;
		setBounds(x, y, WIDTH, HEIGHT);
		
		setVisible(true);
	}
		
	public void start() {
		start = new Date().getTime();
		thr = new Thread(this);
		thr.start();
	}

	public void run() {
	}

	public void stop() {
		finish = new Date().getTime();
		owner.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		setVisible(false);
		dispose();
	}
	
	public double getDuration(){
		return (finish - start);
	}

	public JTextArea getTxtArea() {
		return txtArea;
	}
}
