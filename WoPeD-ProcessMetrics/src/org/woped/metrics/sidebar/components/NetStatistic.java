package org.woped.metrics.sidebar.components;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.woped.core.config.DefaultStaticConfiguration;
import org.woped.core.controller.IEditor;
import org.woped.metrics.metricsCalculation.MetricsCalculator;
import org.woped.qualanalysis.service.IQualanalysisService;
import org.woped.qualanalysis.service.QualAnalysisServiceFactory;
import org.woped.qualanalysis.sidebar.assistant.components.ClickLabel;
import org.woped.gui.translations.Messages;

/**
  * @author Mathias Gruschinske
 * own class for Net Statistics from QualAnalysis
 */
public class NetStatistic extends JPanel {

	private static final long serialVersionUID = 1L;

	private IEditor editor;

	protected static final String PREFIX_QUALANALYSIS = "AnalysisSideBar.";
	protected static final Font HEADER_FONT = DefaultStaticConfiguration.DEFAULT_HUGELABEL_BOLDFONT;
	protected static final Font ITEMS_FONT = DefaultStaticConfiguration.DEFAULT_LABEL_FONT;
	protected static final String SUB_POINT = " - ";


	/**
	 * @param editor
	 * sets the instance variable for the statistics
	 */
	public NetStatistic(IEditor editor) {

		super(new BorderLayout());

		this.editor = editor;
		this.qualanalysisService = getQualanalysisService();

		addComponents();

	}

	/**
	 * creates the statistics with the individual components and adds there to the panel
	 */
	private void addComponents() {

		// instance for the calculation of the values
		MetricsCalculator request = new MetricsCalculator(editor);

		// main container for the statistic
		JPanel statistics = new JPanel(new GridLayout(0, 2));

		// header label
		JLabel netStatisticLabel = new JLabel(
				Messages.getString(PREFIX_QUALANALYSIS + "NetStatistics"));
		netStatisticLabel.setFont(HEADER_FONT);
		netStatisticLabel
				.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

		this.add(netStatisticLabel, BorderLayout.NORTH);

		// places

		ClickLabel clickLabel = new ClickLabel(
				Messages.getString(PREFIX_QUALANALYSIS + "NumPlaces") + ":",
				qualanalysisService.getPlaces().iterator(), editor);
		clickLabel.setFont(ITEMS_FONT);
		statistics.add(clickLabel);

		JLabel count = new JLabel(String.valueOf(qualanalysisService
				.getPlaces().size()), JLabel.RIGHT);
		count.setFont(ITEMS_FONT);
		statistics.add(count);

		// transitions

		clickLabel = new ClickLabel(Messages.getString(PREFIX_QUALANALYSIS
				+ "NumTransitions")
				+ ":", qualanalysisService.getTransitions().iterator(), editor);
		clickLabel.setFont(ITEMS_FONT);
		statistics.add(clickLabel);

		count = new JLabel((int) request.calculateT() + "", JLabel.RIGHT);
		count.setFont(ITEMS_FONT);
		statistics.add(count);

		// operators

		clickLabel = new ClickLabel(SUB_POINT
				+ Messages.getString(PREFIX_QUALANALYSIS + "NumOperators")
				+ ":", qualanalysisService.getOperators().iterator(), editor);
		clickLabel.setFont(ITEMS_FONT);
		statistics.add(clickLabel);

		count = new JLabel(String.valueOf(qualanalysisService.getOperators()
				.size()), JLabel.RIGHT);
		count.setFont(ITEMS_FONT);
		statistics.add(count);

		// subprocesses

		clickLabel = new ClickLabel(SUB_POINT
				+ Messages.getString(PREFIX_QUALANALYSIS + "NumSubprocesses")
				+ ":", qualanalysisService.getSubprocesses().iterator(), editor);
		clickLabel.setFont(ITEMS_FONT);
		statistics.add(clickLabel);

		count = new JLabel(String.valueOf(qualanalysisService.getSubprocesses()
				.size()), JLabel.RIGHT);
		count.setFont(ITEMS_FONT);
		statistics.add(count);

		// arcs

		JLabel arcLabel = new JLabel(Messages.getString(PREFIX_QUALANALYSIS
				+ "NumArcs")
				+ ":");
		arcLabel.setFont(ITEMS_FONT);
		statistics.add(arcLabel);

		count = new JLabel((int) request.calculateA() + "", JLabel.RIGHT);
		count.setFont(ITEMS_FONT);
		statistics.add(count);

		this.add(statistics, BorderLayout.CENTER);

	}

	private IQualanalysisService qualanalysisService = null;

	/**
	 * @return
	 * singleton pattern for generation of an instance of QualanalysisService
	 * needs for the calculation of some values
	 */
	private IQualanalysisService getQualanalysisService() {
		if (qualanalysisService != null) {
			return qualanalysisService;
		} else {
			return QualAnalysisServiceFactory
					.createNewQualAnalysisService(editor);
		}
	}

}
