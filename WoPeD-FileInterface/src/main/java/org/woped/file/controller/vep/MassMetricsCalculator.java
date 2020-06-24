package org.woped.file.controller.vep;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.config.IMetricsConfiguration;
import org.woped.core.controller.IEditor;
import org.woped.core.controller.IStatusBar;
import org.woped.core.controller.IViewController;
import org.woped.editor.controller.ApplicationMediator;
import org.woped.file.PNMLImport;
import org.woped.metrics.metricsCalculation.MetricsUIRequestHandler;
import org.woped.metrics.metricsCalculation.StringPair;

public class MassMetricsCalculator {

	/**
	 * Used for mass calculation of WoPeD metrics. Contained in this project due
	 * to the need to open and load files
	 * 
	 * Mass calculation logic (Increasing performance >90%) has been added
	 * directly to the IEditor and loader classes
	 */

	private List<File> filesToCheck;
	private int index = 0;

	public int getMetricsCount() {
		return filesToCheck.size();
	}

	public int getStatus() {
		return index;
	}

	/**
	 * Prepares a number of files to be loaded (Checking which are valid)
	 * 
	 * @param files
	 *            List of files to be loaded
	 */
	public void prepareMetrics(File[] files) {
		if (files.length < 1)
			return;
		filesToCheck = new ArrayList<File>();
		for (File f : files) {
			if (!f.isFile() || !f.getName().endsWith(".pnml"))
				continue;
			filesToCheck.add(f);
		}
	}

	/**
	 * Runs the actual calculation, using a path to save the file and the
	 * mediator required to open files
	 * 
	 * @param saveFile
	 * @param medi
	 */
	public void calculateMetrics(File saveFile, ApplicationMediator medi) {
		if (filesToCheck == null || filesToCheck.size() < 1)
			return;
		new MetricsCalculation(saveFile, medi).start();
	}

	public void prepareMetrics(List<File> files) {
		prepareMetrics((File[]) files.toArray());
	}

	/**
	 * This Thread is used for the actual metrics calculation. It should not
	 * intervene with working with other nets or any UI-related functions
	 *
	 */
	private class MetricsCalculation extends Thread {

		private File saveFile;
		private ApplicationMediator medi;

		public MetricsCalculation(File saveFile, ApplicationMediator medi) {
			this.saveFile = saveFile;
			this.medi = medi;
		}

		/**
		 * Performs the task of opening the nets, calculating the metrics and
		 * storing the results
		 */
		@Override
		public void run() {
			index = 0;
			BufferedWriter write = null;
			IViewController[] iVC = medi.findViewController(IStatusBar.TYPE);
			IStatusBar[] iSB = new IStatusBar[iVC.length];
			for (int i = 0; i < iSB.length; i++) {
				iSB[i] = (IStatusBar) iVC[i];
			}

			PNMLImport pr = new PNMLImport(medi);
			// OLDPNMLImport2 oldpr = new OLDPNMLImport2(medi);
			IEditor edit = null;
			IMetricsConfiguration metricsConfig = ConfigurationManager
					.getMetricsConfiguration();
			try {
				// Write top line with variable and algorithm names
				write = new BufferedWriter(new FileWriter(saveFile));
				List<String> metricsList = MetricsUIRequestHandler
						.getLayeredAlgoNames("ALL_METRICS");
				write.write("File name");
				for (String s : metricsList)
					write.write("," + s);
				write.write("\r\n");

				// Loop over all selected files to analyze them
				for (File f : filesToCheck) {
					write.write(f.getName());
					if (pr.run(new FileInputStream(f), "", false))
						edit = pr.getEditor()[0];
					/*
					 * else if(oldpr.run(new FileInputStream(f),false)) edit =
					 * oldpr.getEditor()[0];
					 */
					else
						continue;
					MetricsUIRequestHandler ui = new MetricsUIRequestHandler(
							edit);
					for (String metric : metricsList) {
						StringPair result;
						if (metric != null)
							result = ui.calculateSingle(metricsConfig, metric);
						else
							result = new StringPair("", "");
						write.write("," + result.getValue());
					}
					write.write("\r\n");
					edit.closeEditor();
					index++;
				}

				write.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
