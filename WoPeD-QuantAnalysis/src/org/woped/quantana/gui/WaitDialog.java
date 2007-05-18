package org.woped.quantana.gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JDialog;

public class WaitDialog extends JDialog implements Runnable {

	private static final long serialVersionUID = 22L;

	private static final int WIDTH = 200;

	private static final int HEIGHT = 80;

	private Thread thr;
	
	private JDialog owner;
	
	public WaitDialog(JDialog owner, String msg) {
		super(owner, msg, false);
		this.owner = owner;
		owner.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screenSize.width - WIDTH) / 2;
		int y = (screenSize.height - HEIGHT) / 2;
		setBounds(x, y, WIDTH, HEIGHT);
		setVisible(true);
	}
		
	public void start() {
		thr = new Thread(this);
		thr.start();
	}

	public void run() {
	}

	public void stop() {
		owner.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		setVisible(false);
		dispose();
	}
}
