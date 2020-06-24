package org.woped.qualanalysis.sidebar.expert;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.IEditor;
import org.woped.qualanalysis.service.IQualanalysisService;
import org.woped.qualanalysis.sidebar.expert.components.GraphTreeModelSelector;
import org.woped.qualanalysis.sidebar.expert.components.NetInfoTreeRenderer;

/**
 * creates the expert page for the sideBar and the netAnalysisDialog not many changes to old NetAnalysisDialog but code refactored
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
@SuppressWarnings("serial")
public class ExpertPage extends JPanel {
    JTree treeObject;

    /**
     * 
     * @param editor to reference objects to
     * @param mediator
     * @param qualanService to get information from
     */
    public ExpertPage(IEditor editor, AbstractApplicationMediator mediator, IQualanalysisService qualanService) {

        super();
        this.setLayout(new GridLayout(1, 1));

        treeObject = new JTree(getNodeTree(qualanService));
        treeObject.setCellRenderer(new NetInfoTreeRenderer());
        treeObject.setShowsRootHandles(true);

        JScrollPane scrollPane = new JScrollPane(treeObject);
        scrollPane.setBorder(null);
        this.add(scrollPane);

        GraphTreeModelSelector treeSelectionChangeHandler = new GraphTreeModelSelector(editor, treeObject, mediator,
                true);
        // We need to know about selection changes inside the tree
        treeObject.addTreeSelectionListener(treeSelectionChangeHandler);
    }

    /**
     * 
     * @param qualanService to get information from
     * @return the analysisInfo-nodeTree
     */
    private DefaultMutableTreeNode getNodeTree(IQualanalysisService qualanService) {
        // Instantiate all nodes that are used
        DefaultMutableTreeNode analysisInfo = ExpertFactory.getAnalysisInfo();
        DefaultMutableTreeNode structuralInfo = ExpertFactory.getStructuralInfo();
        DefaultMutableTreeNode netStatisticsInfo = ExpertFactory.getNetStatisticsInfo();
        DefaultMutableTreeNode placesInfo = ExpertFactory.getPlacesInfo(qualanService);
        DefaultMutableTreeNode transitionsInfo = ExpertFactory.getTransitionsInfo(qualanService);
        DefaultMutableTreeNode operatorsInfo = ExpertFactory.getOperatorsInfo(qualanService);
        DefaultMutableTreeNode subprocessesInfo = ExpertFactory.getSubprocessesInfo(qualanService);
        DefaultMutableTreeNode arcsInfo = ExpertFactory.getArcsInfo(qualanService);
        DefaultMutableTreeNode wronglyUsedOperatorsInfo = ExpertFactory.getWronglyUsedOperatorsInfo(qualanService);
        DefaultMutableTreeNode freeChoiceViolationsInfo = ExpertFactory.getFreeChoiceViolationsInfo(qualanService);
        DefaultMutableTreeNode sComponentInfo = ExpertFactory.getSComponentInfo();
        DefaultMutableTreeNode sComponentsInfo = ExpertFactory.getSComponentsInfo(qualanService);
        DefaultMutableTreeNode sCompUncoveredInfo = ExpertFactory.getSCompUncoveredInfo(qualanService);
        DefaultMutableTreeNode wellStructurednessInfo = ExpertFactory.getWellStructurednessInfo();
        DefaultMutableTreeNode pTHandlesInfo = ExpertFactory.getPTHandlesInfo(qualanService);
        DefaultMutableTreeNode tPHandlesInfo = ExpertFactory.getTPHandlesInfo(qualanService);
        DefaultMutableTreeNode soundnessInfo = ExpertFactory.getSoundnessInfo();
        DefaultMutableTreeNode workflowInfo = ExpertFactory.getWorkflowInfo(qualanService);
        DefaultMutableTreeNode sourcePlacesInfo = ExpertFactory.getSourcePlacesInfo(qualanService);
        DefaultMutableTreeNode sinkPlacesInfo = ExpertFactory.getSinkPlacesInfo(qualanService);
        DefaultMutableTreeNode sourceTransitionsInfo = ExpertFactory.getSourceTransitionsInfo(qualanService);
        DefaultMutableTreeNode sinkTransitionsInfo = ExpertFactory.getSinkTransitionsInfo(qualanService);
        // DefaultMutableTreeNode unconnectedNodesInfo = ExpertFactory.getUnconnectedNodesInfo(qualanService);
        // DefaultMutableTreeNode notStronglyConnectedInfo = ExpertFactory.getNotStronglyConnectedInfo(qualanService);
        DefaultMutableTreeNode sccsInfo = ExpertFactory.getStronglyConnectedComponentsInfo(qualanService);
        DefaultMutableTreeNode arcWeightInfo = ExpertFactory.getArcWeightViolationInfo(qualanService);
        DefaultMutableTreeNode initialMarkingInfo = ExpertFactory.getInitialMarkingInfo();
        DefaultMutableTreeNode wronglyMarkedPlacesInfo = ExpertFactory.getWronglyMarkedPlacesInfo(qualanService);
        DefaultMutableTreeNode boundednessInfo = ExpertFactory.getBoundednessInfo();
        DefaultMutableTreeNode unboundedPlacesInfo = ExpertFactory.getUnboundedPlacesInfo(qualanService);
        DefaultMutableTreeNode livenessInfo = ExpertFactory.getLivenessInfo();
        DefaultMutableTreeNode deadTransitionsInfo = ExpertFactory.getDeadTransitionsInfo(qualanService);
        DefaultMutableTreeNode nonLiveTransitionsInfo = ExpertFactory.getNonLiveTransitionsInfo(qualanService);

        // Join nodes to create tree-view
        analysisInfo.add(structuralInfo);
        structuralInfo.add(netStatisticsInfo);
        netStatisticsInfo.add(placesInfo);
        netStatisticsInfo.add(transitionsInfo);
        netStatisticsInfo.add(operatorsInfo);
        netStatisticsInfo.add(subprocessesInfo);
        netStatisticsInfo.add(arcsInfo);
        structuralInfo.add(wronglyUsedOperatorsInfo);
        structuralInfo.add(freeChoiceViolationsInfo);
        structuralInfo.add(sComponentInfo);
        sComponentInfo.add(sComponentsInfo);
        sComponentInfo.add(sCompUncoveredInfo);
        structuralInfo.add(wellStructurednessInfo);
        wellStructurednessInfo.add(pTHandlesInfo);
        wellStructurednessInfo.add(tPHandlesInfo);
        analysisInfo.add(soundnessInfo);

        soundnessInfo.add(workflowInfo);
        workflowInfo.add(sourcePlacesInfo);
        workflowInfo.add(sinkPlacesInfo);
        workflowInfo.add(arcWeightInfo);
        workflowInfo.add(sourceTransitionsInfo);
        workflowInfo.add(sinkTransitionsInfo);
        // workflowInfo.add(unconnectedNodesInfo);
        // workflowInfo.add(notStronglyConnectedInfo);
        workflowInfo.add(ExpertFactory.getConnectedComponentsInfo(qualanService));
        workflowInfo.add(sccsInfo);
        soundnessInfo.add(initialMarkingInfo);
        initialMarkingInfo.add(wronglyMarkedPlacesInfo);
        soundnessInfo.add(boundednessInfo);
        boundednessInfo.add(unboundedPlacesInfo);
        soundnessInfo.add(livenessInfo);
        livenessInfo.add(deadTransitionsInfo);
        livenessInfo.add(nonLiveTransitionsInfo);

        return analysisInfo;
    }

    /**
     * 
     * @return the treeObejct
     */
    public JTree getTreeObject() {
        return treeObject;
    }
}
