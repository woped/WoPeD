package org.woped.quantana.gui;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;

import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JLabel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.woped.gui.translations.Messages;
import org.woped.core.config.DefaultStaticConfiguration;
import org.woped.quantana.sim.SimReportServerStats;
import org.woped.quantana.sim.SimReportStats;
import org.woped.quantana.sim.SimRunner;
import org.woped.quantana.sim.SimServer;
import org.woped.quantana.sim.SimServerStats;

public class DetailsDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	private static final int WIDTH	= 400;
	
	private static final int HEIGHT	= 600;
	
	private Dialog owner;
	
	private Container contentPane;
	
	private String servName;
	
	private SimRunner sim;
	
	private SimReportStats repStats;
	
	private SimServer server;
	
	private HashMap<SimServer, SimServerStats> servStatsList;
	
	private SimReportServerStats servStats;
	
	private boolean isProcess = false;
	
	private JLabel lblDesc_1;
	private JLabel lblDetail_1;
	
	private JLabel lblDesc_2;
	private JLabel lblDetail_2;
	
	private JLabel lblDesc_3;
	private JLabel lblDetail_3;
	
	private JLabel lblDesc_4;
	private JLabel lblDetail_4;
	
	private JLabel lblDesc_5;
	private JLabel lblDetail_5;
	
	private JLabel lblDesc_6;
	private JLabel lblDetail_6;
	
	private JLabel lblDesc_7;
	private JLabel lblDetail_7;
	
	private JLabel lblDesc_8;
	private JLabel lblDetail_8;
	
	private JLabel lblDesc_9;
	private JLabel lblDetail_9;
	
	public DetailsDialog(Dialog owner, String sname){
		super(owner, Messages.getString("QuantAna.Simulation.Details") + " " + sname, true);
		this.owner = owner;
		servName = sname;		
		sim = ((QuantitativeSimulationDialog)owner).getSimulator();
		repStats = sim.getRepStats();		
		servStatsList = repStats.getServStats();		
		getServer();		
		initialize();
	}
	
	static JFreeChart createDistributionChart(XYDataset dataset, String title, String xtitle, String ytitle) {
        JFreeChart chart = ChartFactory.createXYLineChart(
        	title,xtitle,ytitle,        		        		
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        ); 
        return chart;
    }
	
	XYDataset createDataset(int[] values) {
        XYSeries series = new XYSeries("");  
        int j=0, left = 0, right=values.length-1;
        // left trim
        while ((j+1)<values.length){
        	if(values[j+1]>0)break;
        	left=j++;        	
        }
        
        j = values.length-1;
        // right trim
        while((j-1)>0){
        	if(values[j-1]>0)break;
        	right=j--;
        }
        for(int i=left; i<right;i++)
        	series.add(i-100,values[i]);        
        return new XYSeriesCollection(series);
    }	
	
	JFreeChart createPieChart(PieDataset dataset) {

        JFreeChart chart = ChartFactory.createPieChart(
        	Messages.getString("QuantAna.Simulation.Details.Wdistribution"),
            dataset,             // data
            true,                // include legend
            true,
            false
        );
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(DefaultStaticConfiguration.DEFAULT_BIGLABEL_FONT);
        plot.setCircular(false);
        plot.setLabelGap(0.02);
        return chart;

    }
	
	JLabel makeLabel(int width, int height){
		JLabel l = new JLabel();
		l.setPreferredSize(new Dimension(width,height));
		return l;
	}
	
	
	private void initialize(){
		final int bigwidth = 200;
		final int smallwidth = 80;
		final int lheight = 20;
		
		
		contentPane = this.getContentPane();
		
		lblDesc_1 = makeLabel(bigwidth, lheight); 
		lblDesc_2 = makeLabel(bigwidth, lheight);
		lblDesc_3 = makeLabel(bigwidth, lheight);
		lblDesc_4 = makeLabel(bigwidth, lheight);
		lblDesc_5 = makeLabel(bigwidth, lheight);
		lblDesc_6 = makeLabel(bigwidth, lheight);
		lblDesc_7 = makeLabel(bigwidth, lheight);
		lblDesc_8 = makeLabel(bigwidth, lheight);		
		lblDesc_9 = makeLabel(bigwidth, lheight);
		lblDetail_1 = makeLabel(smallwidth,lheight);
		lblDetail_2 = makeLabel(smallwidth, lheight);
		lblDetail_3 = makeLabel(smallwidth, lheight);
		lblDetail_4 = makeLabel(smallwidth, lheight);
		lblDetail_5 = makeLabel(smallwidth, lheight);
		lblDetail_6 = makeLabel(smallwidth, lheight);
		lblDetail_7 = makeLabel(smallwidth, lheight);
		lblDetail_8 = makeLabel(smallwidth, lheight);
		lblDetail_9 = makeLabel(smallwidth, lheight);
		
		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 0;
		contentPane.add(lblDesc_1, constraints);
		
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 0;
		contentPane.add(lblDetail_1, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 1;
		contentPane.add(lblDesc_2, constraints);
		
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 1;
		contentPane.add(lblDetail_2, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 2;
		contentPane.add(lblDesc_3, constraints);
		
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 2;
		contentPane.add(lblDetail_3, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 3;
		contentPane.add(lblDesc_4, constraints);
		
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 3;
		contentPane.add(lblDetail_4, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 4;
		contentPane.add(lblDesc_5, constraints);
		
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 4;
		contentPane.add(lblDetail_5, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 5;
		contentPane.add(lblDesc_6, constraints);
		
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 5;
		contentPane.add(lblDetail_6, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 6;
		contentPane.add(lblDesc_7, constraints);
		
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 6;
		contentPane.add(lblDetail_7, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 7;
		contentPane.add(lblDesc_8, constraints);
		
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 7;
		contentPane.add(lblDetail_8, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 8;
		contentPane.add(lblDesc_9, constraints);
		
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 8;
		contentPane.add(lblDetail_9, constraints);
		
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.gridx = 0;
		constraints.gridy = 9;
		constraints.gridwidth = 2;
		contentPane.add(new JLabel(), constraints);
		
		if (server == null && isProcess){
			// process-statistics
			lblDesc_1.setText(Messages.getString("QuantAna.Simulation.Details.Clock"));
			lblDetail_1.setText(String.format("%,.2f", repStats.getDuration()));
			lblDesc_2.setText(Messages.getString("QuantAna.Simulation.Details.FinishedCases"));
			lblDetail_2.setText(String.format("%,.2f", repStats.getAvgFinishedCases()));
			lblDesc_3.setText(Messages.getString("QuantAna.Simulation.Details.Throughput"));
			lblDetail_3.setText(String.format("%,.2f", repStats.getThroughPut()));
			lblDesc_4.setText(Messages.getString("QuantAna.Simulation.Details.ProcCompletionTime"));
			lblDetail_4.setText(String.format("%,.2f", repStats.getProcCompTime()));
			lblDesc_5.setText(Messages.getString("QuantAna.Simulation.Details.ProcServiceTime"));
			lblDetail_5.setText(String.format("%,.2f", repStats.getProcServTime()));
			lblDesc_6.setText(Messages.getString("QuantAna.Simulation.Details.ProcWaitTime"));
			lblDetail_6.setText(String.format("%,.2f", repStats.getProcWaitTime()));
			
			constraints.weightx = 1;
			constraints.weighty = 1;
			constraints.gridx = 0;
			constraints.gridy = 9;	
			constraints.gridwidth = 2;
			constraints.gridheight = 2;
			ChartPanel p = new ChartPanel(createDistributionChart(
								createDataset(	sim.getDistributionValues()),
												Messages.getString("QuantAna.Distribution.Headline"),
												Messages.getString("QuantAna.Distribution.XAxis"),
												Messages.getString("QuantAna.Distribution.YAxis")));
			p.setRangeZoomable(false);
			contentPane.add(p,constraints);
			
			
			
		} else if (server != null){
			// server-statistics
			double zd = servStats.getAvgZeroDelays();
			lblDesc_1.setText(Messages.getString("QuantAna.Simulation.Details.ZeroDelays"));
			lblDetail_1.setText(String.format("%,.2f", zd));			
			lblDesc_2.setText(Messages.getString("QuantAna.Simulation.Details.NumCalls"));
			lblDetail_2.setText(String.format("%,.2f", servStats.getAvgCalls()));
			lblDesc_3.setText(Messages.getString("QuantAna.Simulation.Details.NumAccess"));
			lblDetail_3.setText(String.format("%,.2f", servStats.getAvgAccesses()));
			lblDesc_4.setText(Messages.getString("QuantAna.Simulation.Details.NumDeparture"));
			lblDetail_4.setText(String.format("%,.2f", servStats.getAvgDepartures()));
			lblDesc_5.setText(Messages.getString("QuantAna.Simulation.Details.MaxQueueLength"));
			lblDetail_5.setText(String.format("%,.2f", servStats.getAvgMaxQLength()));
			lblDesc_6.setText(Messages.getString("QuantAna.Simulation.Details.MaxCasesParallel"));
			lblDetail_6.setText(String.format("%,.2f", servStats.getAvgMaxResNumber()));
			lblDesc_7.setText(Messages.getString("QuantAna.Simulation.Details.MaxWaitTime"));
			lblDetail_7.setText(String.format("%,.2f", servStats.getMaxWaitTime()));
			lblDesc_8.setText(Messages.getString("QuantAna.Simulation.Details.NumServedWhenStopped"));
			lblDetail_8.setText(String.format("%,.2f", servStats.getAvgNumServedWhenStopped()));
			lblDesc_9.setText(Messages.getString("QuantAna.Simulation.Details.QLenWhenStopped"));
			lblDetail_9.setText(String.format("%,.2f", servStats.getAvgQLengthWhenStopped()));
			
			
			DefaultPieDataset dataset = new DefaultPieDataset();			
	        dataset.setValue("Ws", new Double(servStats.getAvgServTime()));
	        if(servStats.getAvgWaitTime()>0)
	        	dataset.setValue("Wq", new Double(servStats.getAvgWaitTime()));	        
	        constraints.weightx = 1;
			constraints.weighty = 1;
			constraints.gridx = 0;
			constraints.gridy = 9;
			constraints.gridwidth = 2;
	        contentPane.add(new ChartPanel(createPieChart(dataset)),constraints);			
	        if(servStats.getDistributionLogger()!=null){
				constraints.weightx = 1;
				constraints.weighty = 1;
				constraints.gridx = 0;
				constraints.gridy = 10;	
				constraints.gridwidth = 2;
				ChartPanel p = new ChartPanel(createDistributionChart(
						createDataset(	servStats.getDistributionLogger().getValues()),
						Messages.getString("QuantAna.Simulation.Details.DistHeadline"),
			        	Messages.getString("QuantAna.Simulation.Details.Deviation.XAxis"),
			        	Messages.getString("QuantAna.Simulation.Details.Deviation.YAxis")));
				contentPane.add(p,constraints);				
			}
			
		}
		
		Rectangle bounds = owner.getBounds();
		int x = (bounds.width - WIDTH)/2 + bounds.x;
		int y = (bounds.height - HEIGHT)/2 + bounds.y;
		this.setBounds(x, y, WIDTH, HEIGHT);
		this.setVisible(true);
	}
	
	private void getServer(){
		String key = produceID(servName);
		
		if (key.equals(Messages.getString("QuantAna.Simulation.Process"))){
			server = null;
			isProcess = true;
		} else {
			isProcess = false;
			server = sim.getServerList().get(key);
		}
		
		servStats = (SimReportServerStats)servStatsList.get(server);
	}
	
	private String produceID(String key) {
		if (key.equals(Messages.getString("QuantAna.Simulation.Process")))
			return key;
		else
			return key.substring(key.indexOf("(") + 1, key.indexOf(")"));
	}
}
