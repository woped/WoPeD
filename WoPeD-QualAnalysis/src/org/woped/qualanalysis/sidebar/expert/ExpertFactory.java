package org.woped.qualanalysis.sidebar.expert;

import java.util.Collection;
import java.util.Iterator;

import javax.swing.tree.DefaultMutableTreeNode;

import org.woped.core.model.AbstractElementModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.qualanalysis.Constants;
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
@SuppressWarnings({"serial","unchecked"})
public class ExpertFactory extends DefaultMutableTreeNode{
	
	/**
	 * 
	 * @return node containing tree title
	 */
	protected static DefaultMutableTreeNode getAnalysisInfo(){
		return new NetInfo(Messages.getString("Analysis.Tree.Title"));
	}
	
	/**
	 * 
	 * @return node containing all basic information about the petri net
	 */
	protected static DefaultMutableTreeNode getBasicInfo() {
		return new NetInfo(Messages.getString("Analysis.Tree.BasicInfo"));
	}

	/**
	 * 
	 * @return node containing information about places
	 */
	protected static DefaultMutableTreeNode getPlacesInfo(IQualanalysisService qualanService) {
		return new NodeGroupNetInfo(
				Messages.getString("Analysis.Tree.NumPlaces") + ": " + qualanService.getNumPlaces(), qualanService
						.getPlacesIterator());
	}

	/**
	 * 
	 * @return node containing information about transitions
	 */
	protected static DefaultMutableTreeNode getTransitionsInfo(IQualanalysisService qualanService) {
		return new NodeGroupNetInfo(Messages.getString("Analysis.Tree.NumTransitions") + ": "
				+ qualanService.getNumTransitions(), qualanService.getTransitionsIterator());
	}

	/**
	 * 
	 * @return node containing information about operators
	 */
	protected static DefaultMutableTreeNode getOperatorsInfo(IQualanalysisService qualanService) {
		return new NodeGroupNetInfo(Messages.getString("Analysis.Tree.NumOperators") + ": "
				+ qualanService.getNumOperators(), qualanService.getOperatorsIterator());
	}

	/**
	 * 
	 * @return node containing information about subprocesses
	 */
	protected static DefaultMutableTreeNode getSubprocessesInfo(IQualanalysisService qualanService) {
		return new NodeGroupNetInfo(Messages.getString("Analysis.Tree.NumSubprocesses") + ": "
				+ qualanService.getNumSubprocesses(), qualanService.getSubprocessesIterator());
	}

	/**
	 * 
	 * @return node containing information about arcs
	 */
	protected static DefaultMutableTreeNode getArcsInfo(IQualanalysisService qualanService) {
		return new NetInfo(Messages.getString("Analysis.Tree.NumArcs") + ": " + qualanService.getNumArcs());
	}

	/**
	 * 
	 * @return node containing all information from structural analysis of the
	 *         petri net
	 */
	protected static DefaultMutableTreeNode getStructuralInfo() {
		return new NetInfo(Messages.getString("Analysis.Tree.Structural"));
	}

	/**
	 * 
	 * @return node containing information about the workflow-property of the
	 *         petri net
	 */
	protected static DefaultMutableTreeNode getWorkflowInfo(IQualanalysisService qualanService) {
		Iterator<AbstractElementModel> i = qualanService.getNotStronglyConnectedNodesIterator();
		LoggerManager.info(Constants.QUALANALYSIS_LOGGER, Messages.getString("Analysis.Tree.NotStronglyConnected")
				+ " {");
		while (i.hasNext()) {
			AbstractElementModel element = (AbstractElementModel) i.next();
			LoggerManager.info(Constants.QUALANALYSIS_LOGGER, element.getId() + "," + element.getNameValue());
		}
		LoggerManager.info(Constants.QUALANALYSIS_LOGGER, "}");

		return new NetInfo(Messages.getString("Analysis.Tree.WorkflowNet"));
	}

	/**
	 * 
	 * @return node containing information about source-places
	 */
	protected static DefaultMutableTreeNode getSourcePlacesInfo(IQualanalysisService qualanService) {
		return new NodeGroupNetInfo(Messages.getString("Analysis.Tree.NumSourcePlaces") + ": "
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
		return new NodeGroupNetInfo(Messages.getString("Analysis.Tree.NumSinkPlaces") + ": "
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
		return new NodeGroupNetInfo(Messages.getString("Analysis.Tree.NumSourceTrans") + ": "
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
		return new NodeGroupNetInfo(Messages.getString("Analysis.Tree.NumSinkTrans") + ": "
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
	 * @return node containing information about unconnected nodes in the petri
	 *         net
	 */
	protected static DefaultMutableTreeNode getUnconnectedNodesInfo(IQualanalysisService qualanService) {
		return new NodeGroupNetInfo(Messages.getString("Analysis.Tree.NumUnconnectedNodes") + ": "
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
	 * @return node containing information about not strongly connected nodes in
	 *         the petri net
	 */
	protected static DefaultMutableTreeNode getNotStronglyConnectedInfo(IQualanalysisService qualanService) {
		return new NodeGroupNetInfo(Messages.getString("Analysis.Tree.NumNotStronglyConnectedNodes") + ": "
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
	 * @return node containing information about free-choice violations in the
	 *         petri net
	 */
	protected static DefaultMutableTreeNode getFreeChoiceViolationsInfo(IQualanalysisService qualanService) {
		return new NodeGroupListNetInfo(Messages.getString("Analysis.Tree.NumFreechoiceViolations") + ": "
				+ qualanService.getNumFreeChoiceViolations(), qualanService.getFreeChoiceViolationsIterator()) {
			public String getGroupDisplayString(int nIndex, Collection group) {
				return Messages.getString("Analysis.Tree.NonFreeChoiceGroup") + " " + (nIndex + 1);
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
	 * @return node containing information about misused operators
	 */
	protected static DefaultMutableTreeNode getWrongOperatorsInfo(IQualanalysisService qualanService) {
		return new NodeGroupNetInfo(Messages.getString("Analysis.Tree.NumWrongOperators") + ": "
				+ qualanService.getNumMisusedOperators(), qualanService.getMisusedOperatorsIterator()) {
			// We want exactly one source place
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
	 * @return node containing information about wrong handles in the petri net
	 */
	protected static DefaultMutableTreeNode getHandleInfo() {
		return new NetInfo(Messages.getString("Analysis.Tree.Wellstructuredness"));
	}

	/**
	 * 
	 * @return node containing information about place-transitions-handles
	 */
	protected static DefaultMutableTreeNode getPTHandlesInfo(IQualanalysisService qualanService) {
		return new NodeGroupListNetInfo(Messages.getString("Analysis.Tree.NumPTHandles") + ": "
				+ qualanService.getNumPTHandles(), qualanService.getPTHandlesIterator()) {
			public String getGroupDisplayString(int nIndex, Collection group) {
				return Messages.getString("Analysis.Tree.PTHandlePair") + " #" + (nIndex + 1);
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
		return new NodeGroupListNetInfo(Messages.getString("Analysis.Tree.NumTPHandles") + ": "
				+ qualanService.getNumTPHandles(), qualanService.getTPHandlesIterator()) {
			public String getGroupDisplayString(int nIndex, Collection group) {
				return Messages.getString("Analysis.Tree.TPHandlePair") + " #" + (nIndex + 1);
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
	 * @return node containing information about sComponents in the petri net
	 */
	protected static DefaultMutableTreeNode getSComponentInfo() {
		return new NetInfo(Messages.getString("Analysis.Tree.SComponents"));
	}

	/**
	 * Display the S-Components of this net
	 * 
	 * @return node containing information about sComponents
	 */
	protected static DefaultMutableTreeNode getSComponentsInfo(IQualanalysisService qualanService) {
		return new NodeGroupListNetInfo(Messages.getString("Analysis.Tree.NumSComponents") + ": "
				+ qualanService.getNumSComponents(), qualanService.getSComponentsIterator()) {
			public String getGroupDisplayString(int nIndex, Collection group) {
				return Messages.getString("Analysis.Tree.SComponentNumPlaces") + ":" + group.size();
			}

			public int getInfoState() {
				return InfoStateInfo;
			}
		};
	}

	/**
	 * Show the places that are not covered by any S-Component of the net. If
	 * such a place exists the net is not s-coverable
	 * 
	 * @return node containing information about uncovered places
	 */
	protected static DefaultMutableTreeNode getSCompUncoveredInfo(IQualanalysisService qualanService) {
		return new NodeGroupNetInfo(Messages.getString("Analysis.Tree.SCompUncoveredPlaces") + ": "
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
	 * @return node containing all information from semantical analysis of the
	 *         petri net
	 */
	protected static DefaultMutableTreeNode getSemanticalInfo() {
		return new NetInfo(Messages.getString("Analysis.Tree.Behaviour"));
	}

	/**
	 * 
	 * @return node containing information about soundness
	 */
	protected static DefaultMutableTreeNode getSoundnessInfo() {
		return new NetInfo(Messages.getString("Analysis.Tree.Soundness"));
	}

	/**
	 * 
	 * @return node containing information about tokens of the petri net
	 */
	protected static DefaultMutableTreeNode getTokenInfo() {
		return new NetInfo(Messages.getString("Analysis.Tree.Tokens"));
	}
	
	/**
	 * 
	 * @return node containing information about source places without tokens
	 * (source places should contain a token)
	 */
	protected static DefaultMutableTreeNode getEmptySourcePlacesInfo(IQualanalysisService qualanService) {
		return new NodeGroupNetInfo(Messages.getString("Analysis.Tree.NumEmptySourcePlaces") + ": "
				+ qualanService.getNumInnerTokens(), qualanService.getEmptySourcePlacesIterator()) {
			// source places without tokens are not good and should trigger an error
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
	 * @return node containing information about inner tokens
	 * (places not being source should not contain a token)
	 */
	protected static DefaultMutableTreeNode getInnerTokenInfo(IQualanalysisService qualanService) {
		return new NodeGroupNetInfo(Messages.getString("Analysis.Tree.NumInnerTokens") + ": "
				+ qualanService.getNumInnerTokens(), qualanService.getInnerTokensIterator()) {
			// inner tokens are not good and should trigger an error
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
		return new NetInfo(Messages.getString("Analysis.Tree.Boundedness"));
	}

	/**
	 * 
	 * @return node containing information about boundedness of places
	 */
	protected static DefaultMutableTreeNode getUnboundedPlacesInfo(IQualanalysisService qualanService) {
		return new NodeGroupNetInfo(Messages.getString("Analysis.Tree.NumUnboundedPlaces") + ": "
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
		return new NetInfo(Messages.getString("Analysis.Tree.Liveness"));
	}

	/**
	 * 
	 * @return node containing information about dead transitions
	 */
	protected static DefaultMutableTreeNode getDeadTransitionsInfo(IQualanalysisService qualanService) {
		return new NodeGroupNetInfo(Messages.getString("Analysis.Tree.NumDeadTrans") + ": "
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
		return new NodeGroupNetInfo(Messages.getString("Analysis.Tree.NumNonLiveTrans") + ": "
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
