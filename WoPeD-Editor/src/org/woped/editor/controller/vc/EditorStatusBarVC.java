package org.woped.editor.controller.vc;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
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
import org.woped.core.utilities.Platform;
import org.woped.gui.translations.Messages;

@SuppressWarnings("serial")
public class EditorStatusBarVC extends JPanel implements Observer {
	
	private JLabel m_counterLabel = null;
	
	private JLabel m_saveIcon = null;
	
	private EditorVC m_editor = null;
	
	private JLabel m_zoomLabel = null;
	
	private JSlider m_zoom = null;
	
	private JLabel m_modelingDirection = null;
	
	public EditorStatusBarVC(IEditor editor) {
		m_editor = (EditorVC) editor;
		JPanel bar = new JPanel();
		bar.setLayout(new BorderLayout());
		bar.add(getModelingDirection(), BorderLayout.WEST);
		
		JPanel jp = new JPanel();
		jp.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0,5,5,0);
		jp.add(getZoomLabel(), c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_END;
		c.gridx = 2;
		c.gridy = 0;
		c.insets = new Insets(0,5,5,0);
		jp.add(getZoom(), c);
		
		bar.add(jp, BorderLayout.CENTER);
		bar.add(getSaveIcon(), BorderLayout.EAST);
		
		setLayout(new BorderLayout(5,5));
		add(bar);
		m_editor.registerStatusBar(this);
		updateStatus();
	}
	
	private JSlider getZoom() {
		if (m_zoom == null) {
			// slider for zoom from 25 up to 400 percent of original size
			m_zoom = new JSlider(JSlider.HORIZONTAL, 25, 400, 100);
			m_zoom.setToolTipText("Zoom");
			m_zoom.putClientProperty("JSlider.isFilled", Boolean.TRUE);
			m_zoom.addChangeListener(new SliderComboListener());
			m_zoom.addMouseWheelListener(new MouseWheelListener() {
				@Override
				public void mouseWheelMoved(MouseWheelEvent e) {
					int notches = e.getWheelRotation();
					if (!Platform.isMac()) {
						if (notches < 0) {
							m_zoom.setValue(m_zoom.getValue() + 10);
						} else if (notches > 0) {
							m_zoom.setValue(m_zoom.getValue() - 10);
						}
					}
				}
			});
		}
		return m_zoom;
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
	
	private JLabel getModelingDirection(){
		
		if (m_modelingDirection == null){
			m_modelingDirection = new JLabel();
		}
		return m_modelingDirection;
	}
	
	// end of adaption
	
	public void updateStatus() {
		
		if (m_editor.isRotateSelected()) {
			// set icon for vertical direction
			getModelingDirection().setIcon(Messages.getImageIcon("Button.Vertical"));
			getModelingDirection().setText(Messages.getString("Button.Vertical.Title"));
			
		} else {
			// set icon for horizontal direction
			getModelingDirection().setIcon(Messages.getImageIcon("Button.Horizontal"));
			getModelingDirection().setText(Messages.getString("Button.Horizontal.Title"));
		}
		
		StringBuilder builder = new StringBuilder();
		
		getZoomLabel().setText("Zoom: " + DecimalFormat.getPercentInstance().format(
																					m_editor.getGraph().getScale()));
		
		if (!(m_editor instanceof SubprocessEditorVC)) {	
			if (m_editor.isSaved()) {
				getSaveIcon().setText(Messages.getString("Button.Saved.Title"));
				getSaveIcon().setIcon(Messages.getImageIcon("Button.Saved"));
			} else {
				getSaveIcon().setText(Messages.getString("Button.NotSaved.Title"));
				getSaveIcon().setIcon(Messages.getImageIcon("Button.NotSaved"));
			}
		}
		
		// shorten the counterlabel text when the window is too small
		int barWidth = this.getWidth();
		int zoomAndSaveWidth = getModelingDirection().getWidth() + getZoomLabel().getWidth() + getZoom().getWidth() + getSaveIcon().getWidth()+15;
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
}
