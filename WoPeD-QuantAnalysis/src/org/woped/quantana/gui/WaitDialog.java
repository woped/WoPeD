package org.woped.quantana.gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Date;

import javax.swing.JDialog;

public class WaitDialog extends JDialog implements Runnable {//, ActionListener {

	private static final long serialVersionUID = 22L;

	private static final int WIDTH = 200;

	private static final int HEIGHT = 80;

	private Thread thr;
	
	private JDialog owner;
	
	private double start;
	
	private double finish;
	
//	private JLabel lblTime;
//	
//	private double tcount = 0;
//	
//	private Timer timer;
	
	public WaitDialog(JDialog owner, String msg) {
		super(owner, msg, false);
		this.owner = owner;
		owner.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screenSize.width - WIDTH) / 2;
		int y = (screenSize.height - HEIGHT) / 2;
		setBounds(x, y, WIDTH, HEIGHT);
		
//		lblTime = new JLabel();
//		lblTime.setHorizontalAlignment(SwingConstants.CENTER);
//		add(lblTime);
		
		setVisible(true);
	}
		
	public void start() {
		start = new Date().getTime();
//		timer = new Timer(1000, this);
		thr = new Thread(this);
		
//		timer.start();
		thr.start();
	}

	public void run() {
	}

	public void stop() {
		finish = new Date().getTime();
		owner.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		setVisible(false); 
//		timer.stop();
		dispose();
	}
	
	public double getDuration(){
		return (finish - start);
	}
	
//	public void actionPerformed(ActionEvent e){
//		lblTime.setText(++tcount + " sec");
//	}
}
