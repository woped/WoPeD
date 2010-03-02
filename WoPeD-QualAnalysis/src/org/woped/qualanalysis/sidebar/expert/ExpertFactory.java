package org.woped.qualanalysis.sidebar.expert;

import java.util.Collection;

import javax.swing.tree.DefaultMutableTreeNode;

import org.woped.qualanalysis.service.IQualanalysisService;
import org.woped.qualanalysis.sidebar.expert.components.NetInfo;
import org.woped.qualanalysis.sidebar.expert.components.NodeGroupListNetInfo;
import org.woped.qualanalysis.sidebar.expert.components.NodeGroupNetInfo;
import org.woped.translations.Messages;

/**
 * factory containing methods to create the expert page
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
@SuppressWarnings( { "serial", "unchecked" })
public class ExpertFactory extends DefaultMutableTreeNode {
	
	private final static String prefix = "AnalysisSideBar.";

	/**
	 * 
	 * @return node containing tree title
	 */
	protected static DefaultMutableTreeNode getAnalysisInfo() {
		return new NetInfo(Messages.getString(prefix + "Title"));
	}

	/**
	 * 
	 * @return node containing all information from structural analysis of the petri net
	 */
	protected static DefaultMutableTreeNode getStructuralInfo() {
		return new NetInfo(Messages.getString(prefix + "StructuralAnalysis"));
	}

	/**
	 * 
	 * @return node containing all basic information about the petri net
	 */
	protected static DefaultMutableTreeNode getNetStatisticsInfo() {
		return new NetInfo(Messages.getString(prefix + "NetStatistics"));
	}

	/**
	 * 
	 * @return node containing information about places
	 */
	protected static DefaultMutableTreeNode getPlacesInfo(IQualanalysisService qualanService) {
		return new NodeGroupNetInfo(
				Messages.getString(prefix + "NumPlaces") + ": " + qualanService.getNumPlaces(), qualanService
						.getPlacesIterator());
	}

	/**
	 * 
	 * @return node containing information about transitions
	 */
	protected static DefaultMutableTreeNode getTransitionsInfo(IQualanalysisService qualanService) {
		return new NodeGroupNetInfo(Messages.getString(prefix + "NumTransitions") + ": "
				+ qualanService.getNumTransitions(), qualanService.getTransitionsIterator());
	}

	/**
	 * 
	 * @return node containing information about operators
	 */
	protected static DefaultMutableTreeNode getOperatorsInfo(IQualanalysisService qualanService) {
		return new NodeGroupNetInfo(Messages.getString(prefix + "NumOperators") + ": "
				+ qualanService.getNumOperators(), qualanService.getOperatorsIterator());
	}

	/**
	 * 
	 * @return node containing information about subprocesses
	 */
	protected static DefaultMutableTreeNode getSubprocessesInfo(IQualanalysisService qualanService) {
		return new NodeGroupNetInfo(Messages.getString(prefix + "NumSubprocesses") + ": "
				+ qualanService.getNumSubprocesses(), qualanService.getSubprocessesIterator());
	}

	/**
	 * 
	 * @return node containing information about arcs
	 */
	protected static DefaultMutableTreeNode getArcsInfo(IQualanalysisService qualanService) {
		return new NetInfo(Messages.getString(prefix + "NumArcs") + ": " + qualanService.getNumArcs());
	}

	/**
	 * 
	 * @return node containing information about wrongly used operators
	 */
	protected static DefaultMutableTreeNode getWronglyUsedOperatorsInfo(IQualanalysisService qualanService) {
		return new NodeGroupNetInfo(Messages.getString(prefix + "NumWronglyUsedOperators") + ": "
				+ qualanService.getNumWronglyUsedOperators(), qualanService.getWronglyUsedOperatorsIterator()) {
			// wrongly used operators are not good and should trigger an error
			public int getInfoState() {
				if (getChildCount() > 0)
					return InfoStateERROR;
				else
					return InfoStateOK;
			}
		};
	}

	/**
	 * 
	 * @return node containing information about free-choice violations in the petri net
	 */
	protected static DefaultMutableTreeNode getFreeChoiceViolationsInfo(IQualanalysisService qualanService) {
		return new NodeGroupListNetInfo(Messages.getString(prefix + "NumFreeChoiceViolations") + ": "
				+ qualanService.getNumFreeChoiceViolations(), qualanService.getFreeChoiceViolationsIterator()) {
			public String getGroupDisplayString(int nIndex, Collection group) {
				return Messages.getString(prefix + "FreeChoiceViolationGroup") + " " + (nIndex + 1);
			}

			// Free-choice violations are not good and should trigger an error
			public int getInfoState() {
				if (getChildCount() > 0)
					return InfoStateERROR;
				else
					return InfoStateOK;
			}
		};
	}

	/**
	 * 
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
				+ qualanService.getNumSComponents(), qualanService.getSComponentsIterator()) {
			public String getGroupDisplayString(int nIndex, Collection group) {
				return Messages.getString(prefix + "SComponent") + ":" + group.size();
			}

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
				+ qualanService.getNumNotSCovered(), qualanService.getNotSCoveredIterator()) {
			public int getInfoState() {
				if (getChildCount() > 0)
					return InfoStateERROR;
				else
					return InfoStateOK;
			}
		};
	}

	/**
	 * 
	 * @return node containing information about the wellStructuredness of the net
	 */
	protected static DefaultMutableTreeNode getWellStructurednessInfo() {
		return new NetInfo(Messages.getString(prefix + "WellStructurednessAnalysis"));
	}

	/**
	 * 
	 * @return node containing information about place-transitions-handles
	 */
	protected static DefaultMutableTreeNode getPTHandlesInfo(IQualanalysisService qualanService) {
		return new NodeGroupListNetInfo(Messages.getString(prefix + "NumPTHandles") + ": "
				+ qualanService.getNumPTHandles(), qualanService.getPTHandlesIterator()) {
			public String getGroupDisplayString(int nIndex, Collection group) {
				return Messages.getString(prefix + "PTHandlePair") + " #" + (nIndex + 1);
			}

			public int getInfoState() {
				if (getChildCount() > 0)
					return InfoStateERROR;
				else
					return InfoStateOK;
			}
		};
	}

	/**
	 * 
	 * @return node containing information about transition-places-handles
	 */
	protected static DefaultMutableTreeNode getTPHandlesInfo(IQualanalysisService qualanService) {
		return new NodeGroupListNetInfo(Messages.getString(prefix + "NumTPHandles") + ": "
				+ qualanService.getNumTPHandles(), qualanService.getTPHandlesIterator()) {
			public String getGroupDisplayString(int nIndex, Collection group) {
				return Messages.getString(prefix + "TPHandlePair") + " #" + (nIndex + 1);
			}

			public int getInfoState() {
				if (getChildCount() > 0)
					return InfoStateERROR;
				else
					return InfoStateOK;
			}
		};
	}

	/**
	 * 
	 * @return node containing information about soundness
	 */
	protected static DefaultMutableTreeNode getSoundnessInfo() {
		return new NetInfo(Messages.getString(prefix + "SoundnessAnalysis"));
	}

	/**
	 * 
	 * @return node containing information about the workflow-property of the petri net
	 */
	protected static DefaultMutableTreeNode getWorkflowInfo(IQualanalysisService qualanService) {
		return new NetInfo(Messages.getString(prefix + "WorkflowAnalysis"));
	}

	/**
	 * 
	 * @return node containing information about source-places
	 */
	protected static DefaultMutableTreeNode getSourcePlacesInfo(IQualanalysisService qualanService) {
		return new NodeGroupNetInfo(Messages.getString(prefix + "NumSourcePlaces") + ": "
				+ qualanService.getNumSourcePlaces(), qualanService.getSourcePlacesIterator()) {
			// We want exactly one source place
			public int getInfoState() {
				if (getChildCount() != 1)
					return InfoStateERROR;
				else
					return InfoStateOK;
			}
		};
	}

	/**
	 * 
	 * @return node containing information about sink-places
	 */
	protected static DefaultMutableTreeNode getSinkPlacesInfo(IQualanalysisService qualanService) {
		return new NodeGroupNetInfo(Messages.getString(prefix + "NumSinkPlaces") + ": "
				+ qualanService.getNumSinkPlaces(), qualanService.getSinkPlacesIterator()) {
			// We want exactly one sink place
			public int getInfoState() {
				if (getChildCount() != 1)
					return InfoStateERROR;
				else
					return InfoStateOK;
			}
		};
	}

	/**
	 * 
	 * @return node containing information about source-transitions
	 */
	protected static DefaultMutableTreeNode getSourceTransitionsInfo(IQualanalysisService qualanService) {
		return new NodeGroupNetInfo(Messages.getString(prefix + "NumSourceTransitions") + ": "
				+ qualanService.getNumSourceTransitions(), qualanService.getSourceTransitionsIterator()) {
			// Source transitions are not good and should trigger an error
			public int getInfoState() {
				if (getChildCount() > 0)
					return InfoStateERROR;
				else
					return InfoStateOK;
			}
		};
	}

	/**
	 * 
	 * @return node containing information about sink-transitions
	 */
	protected static DefaultMutableTreeNode getSinkTransitionsInfo(IQualanalysisService qualanService) {
		return new NodeGroupNetInfo(Messages.getString(prefix + "NumSinkTransitions") + ": "
				+ qualanService.getNumSinkTransitions(), qualanService.getSinkTransitionsIterator()) {
			// Sink transitions are not good and should trigger an error
			public int getInfoState() {
				if (getChildCount() > 0)
					return InfoStateERROR;
				else
					return InfoStateOK;
			}
		};
	}

	/**
	 * 
	 * @return node containing information about unconnected nodes in the petri net
	 */
	protected static DefaultMutableTreeNode getUnconnectedNodesInfo(IQualanalysisService qualanService) {
		return new NodeGroupNetInfo(Messages.getString(prefix + "NumUnconnectedNodes") + ": "
				+ +qualanService.getNumNotConnectedNodes(), qualanService.getNotConnectedNodesIterator()) {
			// Any unconnected nodes must trigger an error
			public int getInfoState() {
				if (getChildCount() > 0)
					return InfoStateERROR;
				else
					return InfoStateOK;
			}
		};
	}

	/**
	 * 
	 * @return node containing information about not strongly connected nodes in the petri net
	 */
	protected static DefaultMutableTreeNode getNotStronglyConnectedInfo(IQualanalysisService qualanService) {
		return new NodeGroupNetInfo(Messages.getString(prefix + "NumNotStronglyConnectedNodes") + ": "
				+ qualanService.getNumNotStronglyConnectedNodes(), qualanService.getNotStronglyConnectedNodesIterator()) {
			// Any nodes that are not strongly connected must trigger an error
			public int getInfoState() {
				if (getChildCount() > 0)
					return InfoStateERROR;
				else
					return InfoStateOK;
			}
		};
	}

	/**
	 * 
	 * @return node containing information about the initial marking
	 */
	protected static DefaultMutableTreeNode getInitialMarkingInfo() {
		return new NetInfo(Messages.getString(prefix + "InitialMarkingAnalysis"));
	}

	/**
	 * 
	 * @return node containing information mistakes in the initial marking (source place should contain one token, all
	 *         other places should contain no token)
	 */
	protected static DefaultMutableTreeNode getWronglyMarkedPlacesInfo(IQualanalysisService qualanService) {
		return new NodeGroupNetInfo(Messages.getString(prefix + "NumWronglyMarkedPlaces") + ": "
				+ qualanService.getNumWronglyMarkedPlaces(), qualanService.getWronglyMarkedPlacesIterator()) {
			// wrongly marked places are not good and should trigger an error
			public int getInfoState() {
				if (getChildCount() > 0)
					return InfoStateERROR;
				else
					return InfoStateOK;
			}
		};
	}

	/**
	 * 
	 * @return node containing information about boundedness of the petri net
	 */
	protected static DefaultMutableTreeNode getBoundednessInfo() {
		return new NetInfo(Messages.getString(prefix + "BoundednessAnalysis"));
	}

	/**
	 * 
	 * @return node containing information about boundedness of places
	 */
	protected static DefaultMutableTreeNode getUnboundedPlacesInfo(IQualanalysisService qualanService) {
		return new NodeGroupNetInfo(Messages.getString(prefix + "NumUnboundedPlaces") + ": "
				+ qualanService.getNumUnboundedPlaces(), qualanService.getUnboundedPlacesIterator()) {
			// Unbounded places are not good and should trigger an error
			public int getInfoState() {
				if (getChildCount() > 0)
					return InfoStateERROR;
				else
					return InfoStateOK;
			}
		};
	}

	/**
	 * 
	 * @return node containing information about liveness of the petri net
	 */
	protected static DefaultMutableTreeNode getLivenessInfo() {
		return new NetInfo(Messages.getString(prefix + "LivenessAnalysis"));
	}

	/**
	 * 
	 * @return node containing information about dead transitions
	 */
	protected static DefaultMutableTreeNode getDeadTransitionsInfo(IQualanalysisService qualanService) {
		return new NodeGroupNetInfo(Messages.getString(prefix + "NumDeadTransitions") + ": "
				+ qualanService.getNumDeadTransitions(), qualanService.getDeadTransitionsIterator()) {
			// dead transitions are not good and should trigger an error
			public int getInfoState() {
				if (getChildCount() > 0)
					return InfoStateERROR;
				else
					return InfoStateOK;
			}
		};
	}

	/**
	 * 
	 * @return node containing information about non-live transitions
	 */
	protected static DefaultMutableTreeNode getNonLiveTransitionsInfo(IQualanalysisService qualanService) {
		return new NodeGroupNetInfo(Messages.getString(prefix + "NumNonLiveTransitions") + ": "
				+ qualanService.getNumNonLiveTransitions(), qualanService.getNonLiveTransitionsIterator()) {
			// non-live transitions are not good and should trigger an error
			public int getInfoState() {
				if (getChildCount() > 0)
					return InfoStateERROR;
				else
					return InfoStateOK;
			}
		};
	}
}
