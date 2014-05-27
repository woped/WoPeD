package org.woped.gui.translations.analytics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandMenuButton;
import org.pushingpixels.flamingo.api.common.popup.JCommandPopupMenu;
import org.pushingpixels.flamingo.api.common.popup.JPopupPanel;
import org.pushingpixels.flamingo.api.common.popup.PopupPanelCallback;
import org.pushingpixels.flamingo.api.ribbon.JRibbon;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;
import org.pushingpixels.flamingo.api.ribbon.RibbonTask;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import org.pushingpixels.flamingo.api.ribbon.resize.IconRibbonBandResizePolicy;
import org.woped.gui.images.svg.analyze_quantitative_simulation;
import org.woped.gui.images.svg.file_close;
import org.woped.gui.images.svg.file_save;
import org.woped.gui.images.svg.flag_of_germany;
import org.woped.gui.images.svg.flag_of_us;
import org.woped.gui.images.svg.zoom_chooser;

@SuppressWarnings("serial")
public class AnalysisRibbonBar extends JRibbon {

	private RibbonTask analysisTask = null;
	private JRibbonBand analysisBand, actionBand = null;
	private JCommandButton refreshButton, saveButton, deleteButton, fileChooserButton = null;
	private JCommandMenuButton de_Button, en_Button = null;

	private TB_Listener	listener = null;

	public AnalysisRibbonBar(unusedKeysUI panel){
        listener = new TB_Listener(panel);
		this.addTask(getAnalysisTask());
	}
	/*********/
	/* TASK */
	/*********/
	private RibbonTask getAnalysisTask() {
		if (analysisTask == null) {
			analysisTask = new RibbonTask("Analysis", getAnalysisBand(), getActionBand());
		}
		return analysisTask;
	}
	/*********/
	/* BANDS */
	/*********/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JRibbonBand getAnalysisBand() {
    	if (analysisBand == null) {
    		analysisBand = new JRibbonBand("Analysis", null);

    		analysisBand.addCommandButton(getZoomChooser(), RibbonElementPriority.LOW);
    		analysisBand.addCommandButton(getRefreshButton(), RibbonElementPriority.LOW);

            analysisBand.setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(analysisBand.getControlPanel()), new IconRibbonBandResizePolicy(analysisBand.getControlPanel())));
        	analysisBand.setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(analysisBand.getControlPanel()), new IconRibbonBandResizePolicy(analysisBand.getControlPanel())));
		}
		return analysisBand;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JRibbonBand getActionBand() {
    	if (actionBand == null) {
    		actionBand = new JRibbonBand("Actions", null);

    		actionBand.addCommandButton(getDeleteButton(), RibbonElementPriority.LOW);
    		actionBand.addCommandButton(getSaveButton(), RibbonElementPriority.LOW);

    		actionBand.setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(actionBand.getControlPanel()), new IconRibbonBandResizePolicy(actionBand.getControlPanel())));
    		actionBand.setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(actionBand.getControlPanel()), new IconRibbonBandResizePolicy(actionBand.getControlPanel())));
		}
		return actionBand;
	}

	/*********/
	/* BUTTONS */
	/*********/
	private JCommandButton getSaveButton() {

		if (saveButton == null) {
			saveButton = new JCommandButton("Copy", new file_save());
			saveButton.addActionListener(listener);
		}

		return saveButton;
	}

	private JCommandButton getZoomChooser() {
		if (fileChooserButton == null) {
	        fileChooserButton = new JCommandButton("Choose", new zoom_chooser());
	        fileChooserButton.setCommandButtonKind(JCommandButton.CommandButtonKind.POPUP_ONLY);
	        fileChooserButton.setPopupCallback(new PopupPanelCallback() {
				@Override
				public JPopupPanel getPopupPanel(JCommandButton commandButton) {
					JCommandPopupMenu zoomOptions = new JCommandPopupMenu();
					de_Button = new JCommandMenuButton("German", new flag_of_germany());
					de_Button.addActionListener(listener);
					en_Button = new JCommandMenuButton("English", new flag_of_us());
					en_Button.addActionListener(listener);
					zoomOptions.addMenuButton(de_Button);
					zoomOptions.addMenuButton(en_Button);
					return zoomOptions;
				}
			});
		}
		return fileChooserButton;
	}

	private JCommandButton getRefreshButton() {
		if (refreshButton == null) {
			refreshButton	= new JCommandButton("Check", new analyze_quantitative_simulation());
			refreshButton.addActionListener(listener);
		}
		return refreshButton;
	}

	private JCommandButton getDeleteButton() {
	if (deleteButton == null) {
		deleteButton	= new JCommandButton("Delete", new file_close());
		deleteButton.addActionListener(listener);
	}
	return deleteButton;
}


class TB_Listener implements ActionListener {
		 unusedKeysUI controller = null;

			public TB_Listener(unusedKeysUI panel){
				this.controller = panel;
			}

			public void actionPerformed(ActionEvent e) {
				if	(e.getSource() == deleteButton){
					controller.deleteKeys();
				}
				if	(e.getSource() == saveButton){
					controller.copyToClipboard();
				}
				if	(e.getSource() == refreshButton){
					controller.doAct();
				}
				if	(e.getSource() == de_Button){
					controller.setLocaleDE();;
				}
				if	(e.getSource() == en_Button){
					controller.setLocaleEN();;
				}
			}
	 }
}
