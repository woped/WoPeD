package org.woped.editor.controller.vc;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.woped.core.controller.IEditor;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.editor.utilities.Messages;

@SuppressWarnings("serial")
public class EditorStatusBarVC extends JPanel implements Observer
{
	private ModelElementContainer m_elementContainer = null;

	private JLabel m_counterLabel = null;

	private JLabel m_saveIcon = null;

	private EditorVC m_editor = null;

	private JLabel m_zoomLabel = null;

	public EditorStatusBarVC(IEditor editor)
	{
		m_editor = (EditorVC) editor;
		m_elementContainer = editor.getModelProcessor().getElementContainer();
		setLayout(new BorderLayout());
		// setBorder(new BevelBorder(0));
		add(getCounterLabel(), BorderLayout.WEST);
		JPanel eastPanel = new JPanel(new GridBagLayout());
		eastPanel.add(getZoomLabel(), new GridBagConstraints(0, 0, 1, 1, 1.0,
				1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 20), 0, 0));
		if (!editor.isSubprocessEditor())
			eastPanel.add(getSaveIcon(), new GridBagConstraints(1, 0, 1, 1,
					1.0, 1.0, GridBagConstraints.NORTHWEST,
					GridBagConstraints.NONE, new Insets(0, 0, 2, 3), 0, 0));
		add(eastPanel, BorderLayout.EAST);
		m_editor.registerStatusBar(this);
	}

	private JLabel getCounterLabel()
	{
		if (m_counterLabel == null)
		{
			m_counterLabel = new JLabel();
			m_counterLabel.setBounds(5, 5, 1, 1);
		}
		return m_counterLabel;
	}

	private JLabel getZoomLabel()
	{
		if (m_zoomLabel == null)
		{
			m_zoomLabel = new JLabel();
		}
		return m_zoomLabel;
	}

	private JLabel getSaveIcon()
	{
		if (m_saveIcon == null)
		{
			m_saveIcon = new JLabel();
		}
		return m_saveIcon;
	}

	public void updateStatus()
	{
		int placeC = m_elementContainer.getElementsByType(
				OperatorTransitionModel.PLACE_TYPE).size();
		int subPC = m_elementContainer.getElementsByType(
				OperatorTransitionModel.SUBP_TYPE).size();
		int transOpC = m_elementContainer.getElementsByType(
				OperatorTransitionModel.TRANS_OPERATOR_TYPE).size();
		int transSimpleC = m_elementContainer.getElementsByType(
				OperatorTransitionModel.TRANS_SIMPLE_TYPE).size();

		StringBuilder builder = new StringBuilder();
		builder.append(" ");
		builder.append(Messages.getString("Statusbar.Places"));
		builder.append(": ");
		builder.append(placeC);
		builder.append("  ");
		builder.append(Messages.getString("Statusbar.Transitions"));
		builder.append(": ");
		builder.append(transSimpleC + transOpC);
		builder.append("  ");
		builder.append(Messages.getString("Statusbar.Subprocesses"));
		builder.append(": ");
		builder.append(subPC);
		builder.append("   ");

		getCounterLabel().setText(builder.toString());

		getZoomLabel().setText(
				"Zoom: "
						+ DecimalFormat.getPercentInstance().format(
								m_editor.getGraph().getScale()));

		if (m_editor.isSaved())
		{
			getSaveIcon().setText(Messages.getString("Button.Saved.Title"));
			getSaveIcon().setIcon(Messages.getImageIcon("Button.Saved"));
		} else
		{
			getSaveIcon().setText(Messages.getString("Button.NotSaved.Title"));
			getSaveIcon().setIcon(Messages.getImageIcon("Button.NotSaved"));
		}
	}

	public void update(Observable arg0, Object arg1)
	{
		updateStatus();
	}
}
