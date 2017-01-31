package org.woped.qualanalysis.sidebar.expert;

import org.woped.core.model.ArcModel;
import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.service.IQualanalysisService;
import org.woped.qualanalysis.sidebar.expert.components.ArcGroupInfo;
import org.woped.qualanalysis.sidebar.expert.components.NetInfo;
import org.woped.qualanalysis.sidebar.expert.components.NodeGroupListNetInfo;
import org.woped.qualanalysis.sidebar.expert.components.NodeGroupNetInfo;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Collection;
import java.util.Set;

/**
 * factory containing methods to create the expert page
 *
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 */
@SuppressWarnings({"serial"})
public class ExpertFactory extends DefaultMutableTreeNode {

    private final static String prefix = "AnalysisSideBar.";

    /**
     * @return node containing tree title
     */
    protected static DefaultMutableTreeNode getAnalysisInfo() {
        return new NetInfo(Messages.getString(prefix + "Title"));
    }

    /**
     * @return node containing all information from structural analysis of the petri net
     */
    protected static DefaultMutableTreeNode getStructuralInfo() {
        return new NetInfo(Messages.getString(prefix + "StructuralAnalysis"));
    }

    /**
     * @return node containing all basic information about the petri net
     */
    protected static DefaultMutableTreeNode getNetStatisticsInfo() {
        return new NetInfo(Messages.getString(prefix + "NetStatistics"));
    }

    /**
     * @return node containing information about places
     */
    protected static DefaultMutableTreeNode getPlacesInfo(IQualanalysisService qualanService) {
        return new NodeGroupNetInfo(Messages.getString(prefix + "NumPlaces") + ": " + qualanService.getPlaces().size(),
                qualanService.getPlaces().iterator());
    }

    /**
     * @return node containing information about transitions
     */
    protected static DefaultMutableTreeNode getTransitionsInfo(IQualanalysisService qualanService) {
        return new NodeGroupNetInfo(Messages.getString(prefix + "NumTransitions") + ": "
                + qualanService.getTransitions().size(), qualanService.getTransitions().iterator());
    }

    /**
     * @return node containing information about operators
     */
    protected static DefaultMutableTreeNode getOperatorsInfo(IQualanalysisService qualanService) {
        return new NodeGroupNetInfo(Messages.getString(prefix + "NumOperators") + ": "
                + qualanService.getOperators().size(), qualanService.getOperators().iterator());
    }

    /**
     * @return node containing information about subprocesses
     */
    protected static DefaultMutableTreeNode getSubprocessesInfo(IQualanalysisService qualanService) {
        return new NodeGroupNetInfo(Messages.getString(prefix + "NumSubprocesses") + ": "
                + qualanService.getSubprocesses().size(), qualanService.getSubprocesses().iterator());
    }

    /**
     * @return node containing information about arcs
     */
    protected static DefaultMutableTreeNode getArcsInfo(IQualanalysisService qualanService) {
        return new NetInfo(Messages.getString(prefix + "NumArcs") + ": " + qualanService.getNumArcs());
    }

    /**
     * @return node containing information about wrongly used operators
     */
    protected static DefaultMutableTreeNode getWronglyUsedOperatorsInfo(IQualanalysisService qualanService) {
        return new NodeGroupNetInfo(Messages.getString(prefix + "NumWronglyUsedOperators") + ": "
                + qualanService.getWronglyUsedOperators().size(), qualanService.getWronglyUsedOperators().iterator()) {
            // wrongly used operators are not good and should trigger an error
            @Override
            public int getInfoState() {
                if (getChildCount() > 0) {
                    return InfoStateERROR;
                } else {
                    return InfoStateOK;
                }
            }
        };
    }

    /**
     * @return node containing information about free-choice violations in the petri net
     */
    protected static DefaultMutableTreeNode getFreeChoiceViolationsInfo(IQualanalysisService qualanService) {
        return new NodeGroupListNetInfo(Messages.getString(prefix + "NumFreeChoiceViolations") + ": "
                + qualanService.getFreeChoiceViolations().size(), qualanService.getFreeChoiceViolations().iterator()) {
            @Override
            public String getGroupDisplayString(int nIndex, Collection<?> group) {
                return Messages.getString(prefix + "FreeChoiceViolationGroup") + " " + (nIndex + 1);
            }

            // Free-choice violations are not good and should trigger an error
            @Override
            public int getInfoState() {
                if (getChildCount() > 0) {
                    return InfoStateERROR;
                } else {
                    return InfoStateOK;
                }
            }
        };
    }

    /**
     * @return node containing information about sComponents in the petri net
     */
    protected static DefaultMutableTreeNode getSComponentInfo() {
        return new NetInfo(Messages.getString(prefix + "SComponentAnalysis"));
    }

    /**
     * Display the S-Components of this net
     *
     * @return node containing information about sComponents
     */
    protected static DefaultMutableTreeNode getSComponentsInfo(IQualanalysisService qualanService) {
        return new NodeGroupListNetInfo(Messages.getString(prefix + "NumSComponents") + ": "
                + qualanService.getSComponents().size(), qualanService.getSComponents().iterator()) {
            @Override
            public String getGroupDisplayString(int nIndex, Collection<?> group) {
                return Messages.getString(prefix + "SComponent") + ":" + group.size();
            }

            @Override
            public int getInfoState() {
                return InfoStateInfo;
            }
        };
    }

    /**
     * Show the places that are not covered by any S-Component of the net. If such a place exists the net is not
     * s-coverable
     *
     * @return node containing information about uncovered places
     */
    protected static DefaultMutableTreeNode getSCompUncoveredInfo(IQualanalysisService qualanService) {
        return new NodeGroupNetInfo(Messages.getString(prefix + "NotSCovered") + ": "
                + qualanService.getNotSCovered().size(), qualanService.getNotSCovered().iterator()) {
            @Override
            public int getInfoState() {
                if (getChildCount() > 0) {
                    return InfoStateERROR;
                } else {
                    return InfoStateOK;
                }
            }
        };
    }

    /**
     * @param qualanService
     * @return information about
     */
    protected static DefaultMutableTreeNode getStronglyConnectedComponentsInfo(IQualanalysisService qualanService) {
        return new NodeGroupListNetInfo(Messages.getString(prefix + "stronglyConnectedComponents") + ": "
                + qualanService.getStronglyConnectedComponents().size(), qualanService.getStronglyConnectedComponents()
                .iterator()) {
            @Override
            public String getGroupDisplayString(int nIndex, Collection<?> group) {
                return Messages.getString(prefix + "stronglyConnectedNodes") + ":" + group.size();
            }

            @Override
            public int getInfoState() {
                if (getChildCount() > 1) {
                    return InfoStateERROR;
                }
                return InfoStateOK;
            }
        };
    }

    /**
     * @param qualanService
     * @return information about
     */
    protected static DefaultMutableTreeNode getConnectedComponentsInfo(IQualanalysisService qualanService) {
        return new NodeGroupListNetInfo(Messages.getString(prefix + "connectedComponents") + ": "
                + qualanService.getConnectedComponents().size(), qualanService.getConnectedComponents().iterator()) {
            @Override
            public String getGroupDisplayString(int nIndex, Collection<?> group) {
                return Messages.getString(prefix + "connectedComponent") + ":" + group.size();
            }

            @Override
            public int getInfoState() {
                if (getChildCount() > 1) {
                    return InfoStateERROR;
                }
                return InfoStateOK;
            }
        };
    }

    /**
     * @return node containing information about the wellStructuredness of the net
     */
    protected static DefaultMutableTreeNode getWellStructurednessInfo() {
        return new NetInfo(Messages.getString(prefix + "WellStructurednessAnalysis"));
    }

    /**
     * @return node containing information about place-transitions-handles
     */
    protected static DefaultMutableTreeNode getPTHandlesInfo(IQualanalysisService qualanService) {
        return new NodeGroupListNetInfo(Messages.getString(prefix + "NumPTHandles") + ": "
                + qualanService.getPTHandles().size(), qualanService.getPTHandles().iterator()) {
            @Override
            public String getGroupDisplayString(int nIndex, Collection<?> group) {
                return Messages.getString(prefix + "PTHandlePair") + " #" + (nIndex + 1);
            }

            @Override
            public int getInfoState() {
                if (getChildCount() > 0) {
                    return InfoStateERROR;
                } else {
                    return InfoStateOK;
                }
            }
        };
    }

    /**
     * @return node containing information about transition-places-handles
     */
    protected static DefaultMutableTreeNode getTPHandlesInfo(IQualanalysisService qualanService) {
        return new NodeGroupListNetInfo(Messages.getString(prefix + "NumTPHandles") + ": "
                + qualanService.getTPHandles().size(), qualanService.getTPHandles().iterator()) {
            @Override
            public String getGroupDisplayString(int nIndex, Collection<?> group) {
                return Messages.getString(prefix + "TPHandlePair") + " #" + (nIndex + 1);
            }

            @Override
            public int getInfoState() {
                if (getChildCount() > 0) {
                    return InfoStateERROR;
                } else {
                    return InfoStateOK;
                }
            }
        };
    }

    /**
     * @return node containing information about soundness
     */
    protected static DefaultMutableTreeNode getSoundnessInfo() {
        return new NetInfo(Messages.getString(prefix + "SoundnessAnalysis"));
    }

    /**
     * @return node containing information about the workflow-property of the petri net
     */
    protected static DefaultMutableTreeNode getWorkflowInfo(IQualanalysisService qualanService) {
        return new NetInfo(Messages.getString(prefix + "WorkflowAnalysis"));
    }

    /**
     * Gets a node containing information about arcs that violate the weight restriction.
     *
     * @param qualanService the service to determine the violating arcs
     * @return a tree of violating arcs
     */
    static DefaultMutableTreeNode getArcWeightViolationInfo(IQualanalysisService qualanService) {
        Set<ArcModel> arcWeightViolations = qualanService.getArcWeightViolations();
        String text = Messages.getString(prefix + "NumArcWeightViolations");

        return new ArcGroupInfo(text, arcWeightViolations) {

            @Override
            public int getInfoState() {
                if (getChildCount() == 0) {
                    return InfoStateOK;
                } else {
                    return InfoStateERROR;
                }
            }
        };
    }

    /**
     * @return node containing information about source-places
     */
    protected static DefaultMutableTreeNode getSourcePlacesInfo(IQualanalysisService qualanService) {
        return new NodeGroupNetInfo(Messages.getString(prefix + "NumSourcePlaces") + ": "
                + qualanService.getSourcePlaces().size(), qualanService.getSourcePlaces().iterator()) {
            // We want exactly one source place
            @Override
            public int getInfoState() {
                if (getChildCount() != 1) {
                    return InfoStateERROR;
                } else {
                    return InfoStateOK;
                }
            }
        };
    }

    /**
     * @return node containing information about sink-places
     */
    protected static DefaultMutableTreeNode getSinkPlacesInfo(IQualanalysisService qualanService) {
        return new NodeGroupNetInfo(Messages.getString(prefix + "NumSinkPlaces") + ": "
                + qualanService.getSinkPlaces().size(), qualanService.getSinkPlaces().iterator()) {
            // We want exactly one sink place
            @Override
            public int getInfoState() {
                if (getChildCount() != 1) {
                    return InfoStateERROR;
                } else {
                    return InfoStateOK;
                }
            }
        };
    }

    /**
     * @return node containing information about source-transitions
     */
    protected static DefaultMutableTreeNode getSourceTransitionsInfo(IQualanalysisService qualanService) {
        return new NodeGroupNetInfo(Messages.getString(prefix + "NumSourceTransitions") + ": "
                + qualanService.getSourceTransitions().size(), qualanService.getSourceTransitions().iterator()) {
            // Source transitions are not good and should trigger an error
            @Override
            public int getInfoState() {
                if (getChildCount() > 0) {
                    return InfoStateERROR;
                } else {
                    return InfoStateOK;
                }
            }
        };
    }

    /**
     * @return node containing information about sink-transitions
     */
    protected static DefaultMutableTreeNode getSinkTransitionsInfo(IQualanalysisService qualanService) {
        return new NodeGroupNetInfo(Messages.getString(prefix + "NumSinkTransitions") + ": "
                + qualanService.getSinkTransitions().size(), qualanService.getSinkTransitions().iterator()) {
            // Sink transitions are not good and should trigger an error
            @Override
            public int getInfoState() {
                if (getChildCount() > 0) {
                    return InfoStateERROR;
                } else {
                    return InfoStateOK;
                }
            }
        };
    }

    /**
     * @return node containing information about unconnected nodes in the petri net
     */
    protected static DefaultMutableTreeNode getUnconnectedNodesInfo(IQualanalysisService qualanService) {
        return new NodeGroupNetInfo(Messages.getString(prefix + "NumUnconnectedNodes") + ": "
                + +qualanService.getNotConnectedNodes().size(), qualanService.getNotConnectedNodes().iterator()) {
            // Any unconnected nodes must trigger an error
            @Override
            public int getInfoState() {
                if (getChildCount() > 0) {
                    return InfoStateERROR;
                } else {
                    return InfoStateOK;
                }
            }
        };
    }

    /**
     * @return node containing information about not strongly connected nodes in the petri net
     */
    protected static DefaultMutableTreeNode getNotStronglyConnectedInfo(IQualanalysisService qualanService) {
        return new NodeGroupNetInfo(Messages.getString(prefix + "NumNotStronglyConnectedNodes") + ": "
                + qualanService.getNotStronglyConnectedNodes().size(), qualanService.getNotStronglyConnectedNodes()
                .iterator()) {
            // Any nodes that are not strongly connected must trigger an error
            @Override
            public int getInfoState() {
                if (getChildCount() > 0) {
                    return InfoStateERROR;
                } else {
                    return InfoStateOK;
                }
            }
        };
    }

    /**
     * @return node containing information about the initial marking
     */
    protected static DefaultMutableTreeNode getInitialMarkingInfo() {
        return new NetInfo(Messages.getString(prefix + "InitialMarkingAnalysis"));
    }

    /**
     * @return node containing information mistakes in the initial marking (source place should contain one token, all
     * other places should contain no token)
     */
    protected static DefaultMutableTreeNode getWronglyMarkedPlacesInfo(IQualanalysisService qualanService) {
        return new NodeGroupNetInfo(Messages.getString(prefix + "NumWronglyMarkedPlaces") + ": "
                + qualanService.getWronglyMarkedPlaces().size(), qualanService.getWronglyMarkedPlaces().iterator()) {
            // wrongly marked places are not good and should trigger an error
            @Override
            public int getInfoState() {
                if (getChildCount() > 0) {
                    return InfoStateERROR;
                } else {
                    return InfoStateOK;
                }
            }
        };
    }

    /**
     * @return node containing information about boundedness of the petri net
     */
    protected static DefaultMutableTreeNode getBoundednessInfo() {
        return new NetInfo(Messages.getString(prefix + "BoundednessAnalysis"));
    }

    /**
     * @return node containing information about boundedness of places
     */
    protected static DefaultMutableTreeNode getUnboundedPlacesInfo(IQualanalysisService qualanService) {
        return new NodeGroupNetInfo(Messages.getString(prefix + "NumUnboundedPlaces") + ": "
                + qualanService.getUnboundedPlaces().size(), qualanService.getUnboundedPlaces().iterator()) {
            // Unbounded places are not good and should trigger an error
            @Override
            public int getInfoState() {
                if (getChildCount() > 0) {
                    return InfoStateERROR;
                } else {
                    return InfoStateOK;
                }
            }
        };
    }

    /**
     * @return node containing information about liveness of the petri net
     */
    protected static DefaultMutableTreeNode getLivenessInfo() {
        return new NetInfo(Messages.getString(prefix + "LivenessAnalysis"));
    }

    /**
     * @return node containing information about dead transitions
     */
    protected static DefaultMutableTreeNode getDeadTransitionsInfo(IQualanalysisService qualanService) {
        return new NodeGroupNetInfo(Messages.getString(prefix + "NumDeadTransitions") + ": "
                + qualanService.getDeadTransitions().size(), qualanService.getDeadTransitions().iterator()) {
            // dead transitions are not good and should trigger an error
            @Override
            public int getInfoState() {
                if (getChildCount() > 0) {
                    return InfoStateERROR;
                } else {
                    return InfoStateOK;
                }
            }
        };
    }

    /**
     * @return node containing information about non-live transitions
     */
    protected static DefaultMutableTreeNode getNonLiveTransitionsInfo(IQualanalysisService qualanService) {
        return new NodeGroupNetInfo(Messages.getString(prefix + "NumNonLiveTransitions") + ": "
                + qualanService.getNonLiveTransitions().size(), qualanService.getNonLiveTransitions().iterator()) {
            // non-live transitions are not good and should trigger an error
            @Override
            public int getInfoState() {
                if (getChildCount() > 0) {
                    return InfoStateERROR;
                } else {
                    return InfoStateOK;
                }
            }
        };
    }
}
