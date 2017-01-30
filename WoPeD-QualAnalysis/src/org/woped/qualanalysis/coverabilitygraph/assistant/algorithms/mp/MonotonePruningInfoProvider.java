package org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp;

import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.coverabilitygraph.assistant.event.CoverabilityGraphAdapter;
import org.woped.qualanalysis.coverabilitygraph.events.NodeEvent;
import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.event.*;
import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.model.MpNode;
import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.model.MpNodeState;
import org.woped.qualanalysis.coverabilitygraph.assistant.sidebar.*;
import org.woped.qualanalysis.coverabilitygraph.gui.views.formatters.NodeFormatter;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;


/**
 * This class creates the textual representation of the node analysis steps that are displayed in the sidebar of the assistant view.
 */
class MonotonePruningInfoProvider {

    private static final String EMPTY_SET = "\u2205";
    private static final String IMPLIES = "\u21D2";
    private final NodeFormatter nodeFormatter;

    private MonotonePruningEventTrigger eventTrigger;
    private SidebarVC sidebar;

    private boolean mainTaskChanged;
    private AnalysisState currentState;
    private Map<AnalysisState, MainTaskView> mainTasks;
    private Map<AnalysisState, ProgressDetailsView> instructions;

    /**
     * Constructs a new info provider.
     *
     * @param eventTrigger the event trigger to fire events
     * @param sidebar the sidebar to display information
     */
    MonotonePruningInfoProvider(MonotonePruningEventTrigger eventTrigger, SidebarVC sidebar, NodeFormatter nodeFormatter) {
        this.eventTrigger = eventTrigger;
        this.sidebar = sidebar;
        this.nodeFormatter = nodeFormatter;

        createMainTasks();
        createInstructions();

        eventTrigger.addGraphListener(new GraphListener());
        eventTrigger.addAnalysisListener(new AnalysisListener());

        reset();
        clear();
    }

    /**
     * Clears the info view.
     */
    void clear() {
        sidebar.clear();

        if (mainTaskChanged) {
            sidebar.setHeader(mainTasks.get(currentState));
            mainTaskChanged = false;
        }

        sidebar.addComponent(instructions.get(currentState));
        sidebar.refresh();
    }

    private void reset() {
        currentState = AnalysisState.START;
        mainTaskChanged = true;

        clear();
    }

    private void createMainTasks() {

        mainTasks = new HashMap<>();
        String prefix = "CoverabilityGraph.Assistant.MP.Tasks.";

        AnalysisState state = AnalysisState.START;
        registerMainTask(state, prefix + state);

        state = AnalysisState.BUILD_TREE;
        registerMainTask(state, prefix + state);

        state = AnalysisState.CONVERT_TO_GRAPH;
        registerMainTask(state, prefix + state);

        state = AnalysisState.FINISHED;
        registerMainTask(state, prefix + state);
    }

    private void registerMainTask(AnalysisState state, String resourcePrefix) {
        MainTaskView view = new MainTaskView();

        String title = Messages.getString(resourcePrefix + ".title");
        view.setTitle(title);

        String description = Messages.getString(resourcePrefix + ".description");
        view.setDescription(description);

        mainTasks.put(state, view);
    }

    private void createInstructions() {
        instructions = new HashMap<>();
        String prefix = "CoverabilityGraph.Assistant.MP.Tasks.";

        AnalysisState state = AnalysisState.START;
        String buttonText = "CoverabilityGraph.Assistant.Sidebar.MP.Buttons.StartAssistant";
        registerInstruction(state, prefix + state, new SelectRandomNodeAction(buttonText));

        state = AnalysisState.BUILD_TREE;
        buttonText = "CoverabilityGraph.Assistant.Sidebar.MP.Buttons.SelectRandomNode";
        registerInstruction(state, prefix + state, new SelectRandomNodeAction(buttonText));

        state = AnalysisState.CONVERT_TO_GRAPH;
        registerInstruction(state, prefix + state, new Convert2GraphAction());

        state = AnalysisState.FINISHED;
        registerInstruction(state, prefix + state, new ResetAssistantAction());
    }

    private void registerInstruction(AnalysisState state, String resourcePrefix, Action action) {
        String header = Messages.getString(resourcePrefix + ".instruction.header");
        String message = Messages.getString(resourcePrefix + ".instruction.message");

        ProgressDetailsView actionView = new ProgressDetailsView();
        actionView.setHeaderText(header);
        actionView.addDetail(message, 0, 0);

        if (action != null) {
            actionView.addAction(action);
        }

        instructions.put(state, actionView);
    }

    private enum AnalysisState {
        START,
        BUILD_TREE,
        CONVERT_TO_GRAPH,
        FINISHED
    }

    private class GraphListener extends CoverabilityGraphAdapter{
        @Override
        public void restartRequested() {
            reset();
        }
    }

    private class AnalysisListener extends MonotonePruningAnalysisAdapter {

        @Override
        public void nodeSelected(NodeEvent event) {
            sidebar.clear();

            ProgressDescriptionView header = new ProgressDescriptionView();
            ProgressDetailsView view = new ProgressDetailsView();
            MpNode node = (MpNode) event.getNode();

            String headerText = Messages.getString("CoverabilityGraph.Assistant.MP.NodeInfo.Header");
            header.setTitle(headerText);

            if (node.getState() == MpNodeState.UNPROCESSED) {
                String msg = Messages.getString("CoverabilityGraph.Assistant.MP.NodeInfo.Unprocessed");
                view.addDetail(msg, 0, 0);

                view.addAction(new AnalyseNodeAction(node));
            } else {
                String active = Messages.getString("CoverabilityGraph.Assistant.MP.NodeInfo.State");
                view.addDetail(active, 0, 0);
                view.addDetail(String.format("<code>%s</code>", node.getState()), 0, 1);

                String processed = Messages.getString("CoverabilityGraph.Assistant.MP.NodeInfo.ProcessStep");
                view.addDetail(processed, 1, 0);
                view.addDetail(String.valueOf(node.getProcessedInStep()), 1, 1);

                if (node.getState() == MpNodeState.INACTIVE) {

                    String deactivationStep =Messages.getString("CoverabilityGraph.Assistant.MP.NodeInfo.DeactivationStep");
                    view.addDetail(deactivationStep, 2, 0);
                    view.addDetail(String.valueOf(node.getDeactivatedInStep()), 2, 1);

                    String deactivationNode = Messages.getString("CoverabilityGraph.Assistant.MP.NodeInfo.DeactivationNode");
                    view.addDetail(deactivationNode, 3, 0);
                    String marking = nodeFormatter.getNodeTextFormatter().getText(node.getDeactivationNode());
                    view.addDetail(marking, 3, 1);
                }
            }

            sidebar.addComponent(header);
            sidebar.addComponent(view);
        }

        @Override
        public void nodeProcessingStarted(NodeEvent event) {

            if(currentState == AnalysisState.START){
                currentState = AnalysisState.BUILD_TREE;
                mainTaskChanged = true;
                clear();
            }
            sidebar.clear();

            MpNode node = (MpNode) event.getNode();
            String headerText = Messages.getString("CoverabilityGraph.Assistant.MP.NodeAnalysis.Header");
            ProgressDescriptionView header = new ProgressDescriptionView();
            header.setTitle(String.format(headerText, node.getProcessedInStep()));

            sidebar.addComponent(header);
        }

        @Override
        public void parentCheckComplete(ParentCheckCompletedEvent event) {

            ProgressDetailsView view = new ProgressDetailsView();
            view.setHeaderText(Messages.getString("CoverabilityGraph.Assistant.MP.ParentCheck.Header"));

            String msg;

            if (event.getParent() == null) {
                msg = Messages.getString("CoverabilityGraph.Assistant.MP.ParentCheck.Root");
            } else {
                String neg = event.isParentActive() ? "" : Messages.getString("CoverabilityGraph.Assistant.MP.ParentCheck.Neg");
                String format = Messages.getString("CoverabilityGraph.Assistant.MP.ParentCheck.MsgFormat");
                String marking = nodeFormatter.getNodeTextFormatter().getMarkingFormatter().getText(event.getParent().getMarking());
                msg = String.format(format, marking, neg);
            }
            view.addDetail(msg, 0, 0);

            if (!event.isParentActive()) {
                view.addAction(Messages.getString("CoverabilityGraph.Assistant.MP.ParentCheck.Action"));
            }

            sidebar.addComponent(view);
        }

        @Override
        public void omegaCheckCompleted(OmegaCheckEvent event) {

            ProgressDetailsView view = new ProgressDetailsView();
            view.setHeaderText(Messages.getString("CoverabilityGraph.Assistant.MP.OmegaCheck.Header"));

            String prevMarkingLabel = Messages.getString("CoverabilityGraph.Assistant.MP.OmegaCheck.CalculatedMarking");
            view.addDetail(prevMarkingLabel, 0, 0);

            String prevMarkingValue = nodeFormatter.getNodeTextFormatter().getMarkingFormatter().getText(event.getPreviousMarking());
            view.addDetail(prevMarkingValue, 0, 1);

            String smallerMarkingsLabel = Messages.getString("CoverabilityGraph.Assistant.MP.OmegaCheck.SmallerMarkings");
            view.addDetail(smallerMarkingsLabel, 1, 0);

            int row = 1;
            for (MpNode n : event.getSmallerNodes()) {
                String marking = nodeFormatter.getNodeTextFormatter().getMarkingFormatter().getText(n.getMarking());
                view.addDetail(marking, row++, 1);
            }

            if (event.getSmallerNodes().isEmpty()) {
                view.addDetail(EMPTY_SET, row++, 1);
            }

            String resultingMarkingLabel = Messages.getString("CoverabilityGraph.Assistant.MP.OmegaCheck.ResultingMarking");
            view.addDetail(resultingMarkingLabel, row, 0);

            if (!event.getSmallerNodes().isEmpty()) {
                String action = Messages.getString("CoverabilityGraph.Assistant.MP.OmegaCheck.Action");
                view.addAction(action);
            }


            String resultingMarkingValue =  nodeFormatter.getNodeTextFormatter().getMarkingFormatter().getText(event.getResultingMarking());
            view.addDetail(resultingMarkingValue, row, 1);

            sidebar.addComponent(view);
        }

        @Override
        public void coverCheckCompleted(CoverCheckCompletedEvent event) {

            ProgressDetailsView view = new ProgressDetailsView();

            String headerText = Messages.getString("CoverabilityGraph.Assistant.MP.CoverCheck.Header");
            view.setHeaderText(headerText);

            String largerNodesLabel = Messages.getString("CoverabilityGraph.Assistant.MP.CoverCheck.LargerNodes");
            view.addDetail(largerNodesLabel, 0, 0);

            int row = 0;
            for (MpNode n : event.getLargerNodes()) {
                String node = nodeFormatter.getNodeTextFormatter().getMarkingFormatter().getText(n.getMarking());
                view.addDetail(node, row++, 1);
            }

            if (event.getLargerNodes().isEmpty()) {
                view.addDetail(EMPTY_SET, row, 1);
            } else {
                String action = Messages.getString("CoverabilityGraph.Assistant.MP.CoverCheck.Action");
                view.addAction(action);
            }

            sidebar.addComponent(view);
        }

        @Override
        public void deactivationCheckCompleted(DeactivationCheckCompletedEvent event) {

            ProgressDetailsView view = new ProgressDetailsView();

            String headerText = Messages.getString("CoverabilityGraph.Assistant.MP.DeactivationCheck.Header");
            view.setHeaderText(headerText);

            String deactivatedNodesLabel = Messages.getString("CoverabilityGraph.Assistant.MP.DeactivationCheck.DeactivatedNodes");
            view.addDetail(deactivatedNodesLabel, 0, 0);

            int row = 0;
            if (event.getDeactivatedNodes().isEmpty()) {
                view.addDetail(EMPTY_SET, row, 1);
            } else {
                for (MpNode n : event.getDeactivatedNodes()) {

                    String smallerNode = n.getDeactivationNode().getMarking().asMultiSetString();
                    view.addDetail(smallerNode, row, 1);

                    String arrow = "&nbsp;" + IMPLIES + "&nbsp;";
                    view.addDetail(arrow, row, 2);

                    String deactivatedNode = nodeFormatter.getNodeTextFormatter().getMarkingFormatter().getText(n.getMarking());
                    view.addDetail(deactivatedNode, row++, 3);
                }
            }

            sidebar.addComponent(view);
        }

        @Override
        public void descendantsCheckCompleted(DescendantsCheckCompletedEvent event) {

            ProgressDetailsView view = new ProgressDetailsView();

            String headerText = Messages.getString("CoverabilityGraph.Assistant.MP.DescendantsCheck.Header");
            view.setHeaderText(headerText);

            String activeTransitionsLabel = Messages.getString("CoverabilityGraph.Assistant.MP.DescendantsCheck.ActiveTransitions");
            view.addDetail(activeTransitionsLabel, 0, 0);

            int row = 0;
            if (event.getActiveTransitions().isEmpty()) {
                view.addDetail(EMPTY_SET, row, 1);
            } else {
                for (TransitionNode t : event.getActiveTransitions()) {

                    String transition = t.toString();
                    view.addDetail(transition, row++, 1);
                }
            }
            String action = Messages.getString("CoverabilityGraph.Assistant.MP.DescendantsCheck.Action");
            view.addAction(action);

            sidebar.addComponent(view);
        }

        @Override
        public void nodeProcessingFinished(NodeEvent event) {

            String title = Messages.getString("CoverabilityGraph.Assistant.MP.NodeAnalysisFinished.Header");
            ProgressDetailsView view = new ProgressDetailsView();
            view.setHeaderText(title);

            String message = Messages.getString("CoverabilityGraph.Assistant.MP.NodeAnalysisFinished.Description");
            view.addDetail(message, 0,0);
            view.addAction(new UnselectAction());

            sidebar.addComponent(view);
        }

        @Override
        public void coverabilityTreeCompleted() {
            currentState = AnalysisState.CONVERT_TO_GRAPH;
            mainTaskChanged = true;
        }

        @Override
        public void coverabilityGraphCompleted() {
            currentState = AnalysisState.FINISHED;
            mainTaskChanged = true;
            clear();
        }
    }

    private class AnalyseNodeAction extends SidebarAction {

        private MpNode node;

        AnalyseNodeAction(MpNode node) {

            super("CoverabilityGraph.Assistant.Sidebar.MP.Buttons.AnalyseNode");
            this.node = node;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            eventTrigger.fireNodeProcessingRequest(node);
        }
    }

    private class SelectRandomNodeAction extends SidebarAction {

        SelectRandomNodeAction(String resourcePrefix) {
            super(resourcePrefix);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            eventTrigger.fireProcessRandomNodeRequestEvent();
        }
    }

    private class Convert2GraphAction extends SidebarAction {

        Convert2GraphAction() {
            super("CoverabilityGraph.Assistant.Sidebar.MP.Buttons.Convert2Graph");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            eventTrigger.fireConvert2GraphRequestEvent();
            clear();
        }
    }

    private class ResetAssistantAction extends SidebarAction {

        ResetAssistantAction() {
            super("CoverabilityGraph.Assistant.Sidebar.MP.Buttons.ResetAssistant");
        }


        @Override
        public void actionPerformed(ActionEvent e) {

            eventTrigger.firePrepareRestartEvent();
            eventTrigger.fireResetAssistantRequestEvent();
        }
    }

    private class UnselectAction extends SidebarAction{

        UnselectAction(){
            super("CoverabilityGraph.Assistant.Sidebar.MP.Buttons.Unselect");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            eventTrigger.fireDeselectGraphEvent();
        }
    }
}
