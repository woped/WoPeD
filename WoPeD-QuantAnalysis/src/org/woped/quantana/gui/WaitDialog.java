package org.woped.quantana.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import org.woped.quantana.Constants;
import org.woped.quantana.sim.SimRunner;
import org.woped.gui.translations.Messages;

public class WaitDialog extends JDialog{

	static final long serialVersionUID = 22L;
	static final int WIDTH = 400;
	static final int HEIGHT = 200;
	JDialog owner;
	double finish, start;
	Container contentPane;
	SimRunner sim = null;
	JProgressBar jProgressBar;
	JLabel lruntime;
	JLabel lrunno;
	JLabel lfinish;
	JLabel lsimtime;	
	
	public WaitDialog(JDialog owner, String msg,final double lambda, SimRunner sim) {		
		super(owner, msg, false);
		this.sim = sim;
		this.owner = owner;
		this.owner.setEnabled(false);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screenSize.width - WIDTH) / 2;
		int y = (screenSize.height - HEIGHT) / 2;
		setBounds(x, y, WIDTH, HEIGHT);
		setResizable(false);
		contentPane = this.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		addComponentsToPane(contentPane, lambda);
		owner.setCursor(new Cursor(Cursor.WAIT_CURSOR));		
		setVisible(true);		
		start = new Date().getTime();
		startWorker();		
	}	
	
	public void addComponentsToPane(Container pane, double lambda) {
		pane.add(getLabelPanel(),BorderLayout.PAGE_START);
		JPanel buttonPanel = new JPanel();
		JButton stopButton;		
		stopButton = new JButton(Messages.getString("QuantAna.WaitDlg.Abort"));
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sim.setAbort(true);				
			}
		});
		JPanel prog = new JPanel();		
		prog.add(getJProgressBar());		
		pane.add(prog,BorderLayout.PAGE_END);
		buttonPanel.add(stopButton);
	    pane.add(buttonPanel,BorderLayout.PAGE_END);
		
	}	
	
	private Component getLabelPanel() {
		JPanel p = new JPanel(), p1 = null;
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.setPreferredSize(new Dimension(WIDTH,100));
		
		p1 = new JPanel();
		p1.setPreferredSize(new Dimension(10,100));		
		p.add(p1);
		
		p1 = new JPanel();
		p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
		p1.setPreferredSize(new Dimension((WIDTH/2)-10,20));
		p.add(p1);
		
		JLabel l = new JLabel(Messages.getString("QuantAna.WaitDlg.RealTime"));
		p1.add(l);
		
		l = new JLabel(Messages.getString("QuantAna.WaitDlg.NoRun"));
		p1.add(l);
		l = new JLabel(Messages.getString("QuantAna.WaitDlg.Finished"));
		p1.add(l);
		l = new JLabel(Messages.getString("QuantAna.WaitDlg.SimulationTime"));
		p1.add(l);
		
		p1 = new JPanel();
		p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
		p1.setPreferredSize(new Dimension((WIDTH/2)-10,20));
		p.add(p1);
		
		lruntime = new JLabel();
		p1.add(lruntime);
		lrunno = new JLabel();
		p1.add(lrunno);
		lfinish = new JLabel();
		p1.add(lfinish);
		lsimtime = new JLabel();
		p1.add(lsimtime);
		
		
		return p;
	}

	JProgressBar getJProgressBar()
    {
		if (jProgressBar == null){
                    jProgressBar = new JProgressBar();
                    jProgressBar.setPreferredSize(new Dimension(300, 14));
            }
            return jProgressBar;
    }

	
	void startWorker() {
        SwingWorker<Integer, String> worker = new SwingWorker<Integer, String>() {
            @Override
            protected Integer doInBackground() throws Exception {
            	sim.start();
            	while(!sim.getAbort()){
            		try{
            			publish(getUpdateTxt());
            			Thread.sleep(500);
            		}catch (InterruptedException e) {}
            	}
            	return 0;
            }
            
            @Override
            protected void process(List<String> chunks){                	
            	lruntime.setText(chunks.get(0).split("::")[0]);
            	lrunno.setText(chunks.get(0).split("::")[1]);
            	lfinish.setText(chunks.get(0).split("::")[2]);
            	lsimtime.setText(chunks.get(0).split("::")[3]);
            	getJProgressBar().setMaximum(sim.getMaxRun());
            	getJProgressBar().setValue(Integer.valueOf((sim.getRunNumber())));
            }
            
            @Override
            protected void done(){
                try {
                	
                	owner.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                	owner.setEnabled(true);
                	setVisible(false);             	
                	((QuantitativeSimulationDialog)owner).updContents();
                	((QuantitativeSimulationDialog)owner).activateDetails();
                    get();
                    
                } 
                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } 
                catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        worker.execute();
    }
	
	private String getUpdateTxt() {
		int ms = (int) (System.currentTimeMillis() - start) / 1000;
		int sec = (ms % 60);
		int min = ((ms / 60) % 60);		
		int hour = (min % 60);				
		return 	String.format("%02d:%02d:%02d", hour, min, sec)+"::"+
				sim.getRunNumber()+"::"+
				sim.getFinished()+"::"+
				String.format("%.2f",sim.getRunClock())+" "+Constants.TIMEUNITS[((QuantitativeSimulationDialog)this.owner).getTimeModel().getStdUnit()];
	}
	
	public double getDuration() {
		return (finish - start);
	}
}