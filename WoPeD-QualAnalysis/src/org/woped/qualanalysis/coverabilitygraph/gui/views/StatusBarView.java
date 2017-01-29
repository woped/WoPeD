package org.woped.qualanalysis.coverabilitygraph.gui.views;

import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * This class encapsulates the status bar of the coverability graph view.
 */
public class StatusBarView extends JPanel{

    private final CoverabilityGraphModel graphModel;
    private boolean outOfSync;
    private MarkingLegend markingLegend;
    private GraphInfo graphInfo;
    private OutOfSyncWarning outOfSyncWarning;

    /**
     * Constructs a new instance of a status bar.
     *
     * @param graphModel the graph model of the coverability graph
     */
    public StatusBarView(CoverabilityGraphModel graphModel) {

        this.graphModel = graphModel;
        this.outOfSync = false;
        initialize();
    }

    /**
     * Refreshes the status bar
     */
    public void refresh() {

        markingLegend.refresh();
        graphInfo.refresh();
        outOfSyncWarning.refresh();
    }

    /**
     * Sets the out of sync warning according to the provided value.
     *
     * @param outOfSync true if the warning should be visible, otherwise false
     */
    public void setOutOfSync(boolean outOfSync) {
        this.outOfSync = outOfSync;
    }

    private void initialize() {
        this.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        markingLegend = new MarkingLegend();
        graphInfo = new GraphInfo();
        outOfSyncWarning = new OutOfSyncWarning();

        this.add(markingLegend);
        this.add(graphInfo);
        this.add(outOfSyncWarning);
    }

    private class MarkingLegend extends JPanel {

        private boolean useId;
        private JLabel legend;

        MarkingLegend() {
            useId = false;

            initialize();
        }

        private void refresh() {
            setLegendText();
        }

        private void initialize() {
            this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));

            String legendLabelText = Messages.getString("QuanlAna.ReachabilityGraph.Legend");
            JLabel legendLabel = new JLabel(legendLabelText);

            legend = new JLabel("");
            setLegendText();

            JButton toggleLegendButton = new JButton(new MarkingLegend.ToggleLegendAction());
            toggleLegendButton.setBorder(BorderFactory.createEmptyBorder());

            this.add(legendLabel);
            this.add(legend);
            this.add(toggleLegendButton);
        }

        private void setLegendText() {
            String text = useId ? graphModel.getLegendByID() : graphModel.getLegendByName();
            legend.setText(String.format("( %s )", text));
        }

        private void toggleLegend() {
            useId = !useId;
            setLegendText();
            repaint();
        }

        private class ToggleLegendAction extends AbstractAction {

            ToggleLegendAction() {
                ImageIcon icon = Messages.getImageIcon("Action.Browser.Refresh");
                this.putValue(Action.SMALL_ICON, icon);

                String tooltip = Messages.getString("QuanlAna.ReachabilityGraph.Legend.Toggle");
                this.putValue(Action.SHORT_DESCRIPTION, tooltip);
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                toggleLegend();
            }
        }
    }

    private class GraphInfo extends JPanel {

        private JLabel nodes;
        private JLabel edges;

        GraphInfo() {
            initialize();
        }

        private void initialize() {

            this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));

            String nodesLabelText = Messages.getString("QuanlAna.ReachabilityGraph.Vertices");
            JLabel nodesLabel = new JLabel(nodesLabelText);
            nodes = new JLabel("");

            String edgesLabelText = Messages.getString("QuanlAna.ReachabilityGraph.Edges");
            JLabel edgesLabel = new JLabel(edgesLabelText);
            edges = new JLabel("");

            this.add(nodesLabel);
            this.add(nodes);
            this.add(edgesLabel);
            this.add(edges);

            refresh();
        }

        public void refresh() {
            nodes.setText(String.valueOf(graphModel.getNodes().size()));
            edges.setText(String.valueOf(graphModel.getEdges().size()));
        }
    }

    private class OutOfSyncWarning extends JLabel {

        OutOfSyncWarning() {
            super(null, null, CENTER);
            initialize();
        }

        public void refresh() {
            this.setVisible(outOfSync);
        }

        private void initialize() {
            ImageIcon icon = Messages.getImageIcon("QuanlAna.ReachabilityGraph.GraphOutOfSync");
            setIcon(icon);

            String tooltipText = Messages.getString("QuanlAna.ReachabilityGraph.GraphOutOfSync");
            this.setToolTipText(tooltipText);

            updateUI();
        }
    }
}
