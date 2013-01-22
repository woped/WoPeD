package org.woped.metrics.builder;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.config.DefaultStaticConfiguration;
import org.woped.core.config.IMetricsConfiguration;
import org.woped.core.controller.IEditor;
import org.woped.translations.Messages;

public class MetricsBuilder extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected static final Font HEADER_FONT = DefaultStaticConfiguration.DEFAULT_HUGELABEL_BOLDFONT;
	protected static final Font ITEMS_FONT = DefaultStaticConfiguration.DEFAULT_BIGLABEL_FONT;
	
	private MetricsBuilderPanel mbp;
	
	public MetricsBuilder(IEditor editor, String metricID) {
		super(Messages.getString("Metrics.Builder.Title"));
		setLayout(new BorderLayout());
		addComponents(metricID);
		setVisible(true);
		setSize(getPreferredSize());
		setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getSize().width) / 2,
				(Toolkit.getDefaultToolkit().getScreenSize().height - getSize().height) / 2);
		setResizable(false);
		
		if(!metricID.equals(""))
				MetricsBuilderPanel.displayMetric(metricID);
	}

	private void addComponents(String metricsID) {
		add(createMainPanel(metricsID), BorderLayout.CENTER);
	}
	
	private JPanel createMainPanel(String metricsID) {		
		mbp = new MetricsBuilderPanel();
		IMetricsConfiguration metricsConfig = ConfigurationManager.getMetricsConfiguration();
		MetricsBuilderPanel.setElementsEditable(metricsConfig.isCustomMetric(metricsID));
		metricsConfig.startEditSession();
		return mbp;
	}
}
