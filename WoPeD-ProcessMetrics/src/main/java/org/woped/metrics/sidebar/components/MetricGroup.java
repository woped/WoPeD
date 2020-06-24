package org.woped.metrics.sidebar.components;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import org.woped.core.controller.IEditor;
import org.woped.metrics.metricsCalculation.MetricsUIRequestHandler;
import org.woped.metrics.metricsCalculation.StringPair;
import org.woped.metrics.metricsCalculation.UITypes.UIMetricsGroup;
import org.woped.metrics.sidebar.SideBar;

/**
 * @author Mathias Gruschinske
 * A metric group contains metric items and combine them to a group
 */
public class MetricGroup extends JPanel {

	private static final long serialVersionUID = 1L;

	private JPanel container;
	private List<MetricItem> metricItems;

	public MetricGroup(MetricsUIRequestHandler uiHandler, IEditor editor,
			UIMetricsGroup metricGroup, SideBar parent) {

		super(new BorderLayout());
		// create a container to set a border
		container = new JPanel(new GridBagLayout());
		container.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED));

		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// collection of metric items		
		metricItems = new ArrayList<MetricItem>();
		List<StringPair> values = metricGroup.getValues();

		GridBagConstraints c = new GridBagConstraints();
		int rowCount = 0;

		// for all metrics in a group crate a metric item and add it to the collection
		for (StringPair item : values) {

			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = rowCount++;
			c.weightx = 1.0;

			MetricItem metricItem = new MetricItem(item.getKey(), item
					.getValue(), uiHandler, editor, parent);
			metricItems.add(metricItem);
			container.add(metricItem, c);
		}

		// the caption of the group
		JLabel caption = new JLabel(metricGroup.getName());
		caption.setFont(new Font(caption.getFont().getFamily(), Font.BOLD,
				caption.getFont().getSize()));

		this.add(caption, BorderLayout.NORTH);
		this.add(container, BorderLayout.CENTER);

	}

	/**
	 * collapses all item detail panels
	 */
	public void setItemsUnvisible() {
		for (MetricItem item : metricItems) {
			item.setDetailsVisiblity(false);
		}
	}
	
	public double getMaxKeyLabelWidth()
	{
		double width = 0;		
		for (MetricItem item : metricItems) {
			if (item.getKeyLabelWidth() > width) {
				width = item.getKeyLabelWidth();
			}
		}		
		return width;
	}
	
	public void setMaxKeyLabelWidth(double width)
	{
		for (MetricItem item : metricItems) {
			item.setKeyLabelWidth(width);
		}		
	}

}
