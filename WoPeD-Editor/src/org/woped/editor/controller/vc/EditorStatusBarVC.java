package org.woped.editor.controller.vc;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.woped.core.controller.IEditor;
import org.woped.translations.Messages;


@SuppressWarnings("serial")
public class EditorStatusBarVC extends JPanel implements Observer {

	private JLabel m_counterLabel = null;

	private JLabel m_saveIcon = null;

	private EditorVC m_editor = null;

	private JLabel m_zoomLabel = null;

	// start of adaption
	// Saskia Kurz, Jan 2012

	private JSlider zoom = null;
	
	private JLabel m_modelingDirection = null;

	// end of adaption

	public EditorStatusBarVC(IEditor editor) {
		m_editor = (EditorVC) editor;
		setLayout(new GridBagLayout());

		// add description of modeling direction and icon
		add(getModelingDirection(), new GridBagConstraints(0, 0, 1, 1, 1.0,
				1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE,
				new Insets(1, 0, 0, 1), 0, 0));


		// start of adaption
		// Saskia Kurz, Jan 2012

		// slider for zoom from 25 up to 400 percent of original size
		zoom = new JSlider(JSlider.HORIZONTAL, 25, 400, 100);
		zoom.setToolTipText("Zoom");	
		
		zoom.addChangeListener(new SliderComboListener());
		
		
		add(getZoomLabel(), new GridBagConstraints(0, 0, 1, 1, 1.0,
				1.0, GridBagConstraints.FIRST_LINE_END, GridBagConstraints.NONE,
				new Insets(1, 0, 0, 330), 0, 0));
		add(zoom, new GridBagConstraints(0, 0, 1, 1, 1.0,
				1.0, GridBagConstraints.FIRST_LINE_END, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 120), 0, 0));
		
		// end of adaption
		

		if (!(editor instanceof SubprocessEditorVC)) {
		
			add(getSaveIcon(), new GridBagConstraints(0, 0, 1, 1, 1.0,
					1.0, GridBagConstraints.FIRST_LINE_END, GridBagConstraints.NONE,
					new Insets(1, 0, 0, 2), 0, 0));
		}
		m_editor.registerStatusBar(this);
	}
	
	

	private JLabel getCounterLabel() {
		if (m_counterLabel == null) {
			m_counterLabel = new JLabel();
			m_counterLabel.setBounds(5, 5, 1, 1);
		}
		return m_counterLabel;
	}

	private JLabel getZoomLabel() {
		if (m_zoomLabel == null) {
			m_zoomLabel = new JLabel();
		}
		return m_zoomLabel;
	}

	private JLabel getSaveIcon() {
		if (m_saveIcon == null) {
			m_saveIcon = new JLabel();
		}
		return m_saveIcon;
	}
	
	// start of adaption
	// Saskia Kurz, Jan 2012
	private JLabel getModelingDirection(){
		
		if (m_modelingDirection == null){
			m_modelingDirection = new JLabel();
		}
		return m_modelingDirection;
	}
	
	// end of adaption

	public void updateStatus() {
		
		// Saskia Kurz, Jan 2012
		// net statistics have been removed from the status bar!
		// Saskia Kurz, Jan 2012
		// adaption: depending on modeling direction the respective icon is added
		
		if (m_editor.isRotateSelected()) {
			Messages.getString("Statusbar.Vertical");
			
			// set icon for vertical direction
			getModelingDirection().setIcon(Messages.getImageIcon("Button.Vertical"));
			getModelingDirection().setText(Messages.getString("Button.Vertical.Title"));
			
		} else {
			Messages.getString("Statusbar.Horizontal");
			
			// set icon for horizontal direction
			getModelingDirection().setIcon(Messages.getImageIcon("Button.Horizontal"));
			getModelingDirection().setText(Messages.getString("Button.Horizontal.Title"));
		}

		StringBuilder builder = new StringBuilder();

		getZoomLabel().setText(
				"Zoom: "
						+ DecimalFormat.getPercentInstance().format(
								m_editor.getGraph().getScale()));

		if (m_editor.isSaved()) {
			getSaveIcon().setText(Messages.getString("Button.Saved.Title"));
			getSaveIcon().setIcon(Messages.getImageIcon("Button.Saved"));
		} else {
			getSaveIcon().setText(Messages.getString("Button.NotSaved.Title"));
			getSaveIcon().setIcon(Messages.getImageIcon("Button.NotSaved"));
		}
		
		//Nils Lamb, Jan 2012
		// adapted by Saskia Kurz, Feb 2012
		
		// shorten the counterlabel text when the window is too small
		int barWidth = this.getWidth();
		int zoomAndSaveWidth = getModelingDirection().getWidth() + getZoomLabel().getWidth() + zoom.getWidth() + getSaveIcon().getWidth()+15;
		java.awt.FontMetrics fm = getCounterLabel().getFontMetrics(getCounterLabel().getFont());
		int counterLabelWidth = 0;
		
		builder.append(getModelingDirection().getText());
			
		if(barWidth > 0 && fm.stringWidth(builder.toString()+"...") + zoomAndSaveWidth > barWidth){
			// shorten it until it fits
			counterLabelWidth = fm.stringWidth(builder.toString());
			while(counterLabelWidth + zoomAndSaveWidth > barWidth && builder.length() > 1){
				builder.delete(Math.max(builder.length()-2,0), Math.max(builder.length()-1,0));
				counterLabelWidth = fm.stringWidth(builder.toString());
			}
			builder.delete(Math.max(builder.length()-10, 0), Math.max(builder.length(), 0));
			builder.append("...");
			// as text is displayed by label of an icon now, set icon text
			getModelingDirection().setText(builder.toString());
		}

	}

	public void update(Observable arg0, Object arg1) {
		updateStatus();
	}

	// start of adaption
	// Saskia Kurz, Jan 2012

	// listener class for JSlider (zoom functionality)
	class SliderComboListener implements ChangeListener {

		JSlider slide;
		JComboBox combo;

		// JSlider event
		public void stateChanged(ChangeEvent ce) {
			if (ce.getSource() instanceof JSlider) {
				slide = (JSlider) ce.getSource();

				// find out zoom factor
				int factor = slide.getValue();

				// call zoom method
				m_editor.zoom(factor, true);

			}
		}

	}
	


	// end of adaption
}
