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
import javax.swing.border.BevelBorder;

import org.woped.core.controller.IEditor;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.editor.utilities.Messages;

public class EditorStatusBarVC extends JPanel implements Observer
{
    private ModelElementContainer m_elementContainer = null;
    private JLabel                m_counterLabel     = null;
    private JLabel                m_saveIcon         = null;
    private EditorVC              m_editor           = null;
    private JLabel                m_zoomLabel        = null;

    public EditorStatusBarVC(IEditor editor)
    {
        m_editor = (EditorVC) editor;
        m_elementContainer = editor.getModelProcessor().getElementContainer();
        setLayout(new BorderLayout());
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        add(getCounterLabel(), BorderLayout.WEST);
        JPanel eastPanel = new JPanel(new GridBagLayout());
        eastPanel.add(getZoomLabel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 20), 0, 0));
        eastPanel.add(getSaveIcon(), new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 10), 0, 0));
        add(eastPanel, BorderLayout.EAST);
        m_editor.registerStatusBar(this);
    }

    private JLabel getCounterLabel()
    {
        if (m_counterLabel == null)
        {
            m_counterLabel = new JLabel();
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
        int placeC = m_elementContainer.getElementsByType(OperatorTransitionModel.PLACE_TYPE).size();
        int subPC = m_elementContainer.getElementsByType(OperatorTransitionModel.SUBP_TYPE).size();
        int transOpC = m_elementContainer.getElementsByType(OperatorTransitionModel.TRANS_OPERATOR_TYPE).size();
        int transSimpleC = m_elementContainer.getElementsByType(OperatorTransitionModel.TRANS_SIMPLE_TYPE).size();

        getCounterLabel().setText(
                Messages.getString("Statusbar.Places") + ": " + placeC + "  " + Messages.getString("Statusbar.Transitions") + ": " + (transSimpleC + transOpC) + "  "
                        + Messages.getString("Statusbar.Subprocesses") + ": " + subPC + "     ");

        getZoomLabel().setText("Zoom: " + DecimalFormat.getPercentInstance().format(m_editor.getGraph().getScale()));

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
