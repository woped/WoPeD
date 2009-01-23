package org.woped.core.analysis;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.security.auth.login.Configuration;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.IEditor;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.CreationMap;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.AbstractPetriNetModelElement;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.utilities.LoggerManager;

public class StructuralAnalysis {

	// ! Construct static analysis object from
	// ! a petri-net editor
	// ! Note that this object will not perform all
	// ! its calculations at construction time
	// ! Rather, each getter method knows which calculations
	// ! need to be done and will trigger them
	public StructuralAnalysis(IEditor currentEditor) {
		m_currentEditor = currentEditor;
	}

	public int getNumPlaces() {
		calculateBasicNetInfo();
		return m_places.size();
	}

	// ! Returns an iterator over all places of the net
	public Iterator getPlacesIterator() {
		calculateBasicNetInfo();
		return m_places.iterator();
	}

	public int getNumTransitions() {
		calculateBasicNetInfo();
		return m_transitions.size();
	}

	public Iterator getTransitionsIterator() {
		calculateBasicNetInfo();
		return m_transitions.iterator();
	}

	public int getNumOperators() {
		calculateBasicNetInfo();
		return m_operators.size();
	}

	public Iterator getOperatorsIterator() {
		calculateBasicNetInfo();
		return m_operators.iterator();
	}

	public int getNumArcs() {
		return m_nNumArcs;
	}

	public int getNumSourcePlaces() {
		calculateBasicNetInfo();
		return m_sourcePlaces.size();
	}

	public Iterator getSourcePlacesIterator() {
		calculateBasicNetInfo();
		return m_sourcePlaces.iterator();
	}

	public int getNumSourceTransitions() {
		calculateBasicNetInfo();
		return m_sourceTransitions.size();
	}

	public Iterator getSourceTransitionsIterator() {
		calculateBasicNetInfo();
		return m_sourceTransitions.iterator();
	}

	public int getNumSinkPlaces() {
		calculateBasicNetInfo();
		return m_sinkPlaces.size();
	}

	public Iterator<AbstractElementModel> getSinkPlacesIterator() {
		calculateBasicNetInfo();
		return m_sinkPlaces.iterator();
	}

	public int getNumSinkTransitions() {
		calculateBasicNetInfo();
		return m_sinkTransitions.size();
	}

	public Iterator getSinkTransitionsIterator() {
		calculateBasicNetInfo();
		return m_sinkTransitions.iterator();
	}

	public int getNumMisusedOperators() {
		calculateBasicNetInfo();
		return m_misusedOperators.size();
	}

	public Iterator getMisusedOperatorsIterator() {
		calculateBasicNetInfo();
		return m_misusedOperators.iterator();
	}

	public int getNumNotConnectedNodes() {
		calculateConnections();
		return m_notConnectedNodes.size();
	}

	// ! Return all nodes of the current net that
	// ! are not connected
	public Iterator getNotConnectedNodes() {
		calculateConnections();
		return m_notConnectedNodes.iterator();
	}

	public int getNumNotStronglyConnectedNodes() {
		calculateConnections();
		return m_notStronglyConnectedNodes.size();
	}

	// ! Return all nodes of the current net that
	// ! are not strongly connected
	public Iterator getNotStronglyConnectedNodes() {
		calculateConnections();
		return m_notStronglyConnectedNodes.iterator();
	}

	public int getNumFreeChoiceViolations() {
		calculateFreeChoice();
		return m_freeChoiceViolations.size();
	}

	// ! Return a list of free-choice violations
	// ! Each free-choice violation is represented
	// ! by a Set of nodes defining the violation
	// ! @return Iterator through a list of sets
	// ! of nodes violating the free-choice property
	public Iterator getFreeChoiceViolations() {
		calculateFreeChoice();
		return m_freeChoiceViolations.iterator();
	}

	//! Detect PT handles in the current net
	//! and return their number
	//! @return Number of PT handles found in the net
	public int getNumPTHandles() {
		calculatePTHandles();
		return m_PTHandles.size();
	}
	
	//! Detect PT handles in the current net
	//! and return an iterator over the result
	//! @return Iterator over PT handles found in the net
	public Iterator<Set<AbstractElementModel>> getPTHandlesIterator() {
		calculatePTHandles();
		return m_PTHandles.iterator();
	}

	//! Detect TP handles in the current net
	//! and return their number
	//! @return Number of TP handles found in the net
	public int getNumTPHandles() {
		calculateTPHandles();
		return m_TPHandles.size();
	}
	
	//! Detect TP handles in the current net
	//! and return an iterator over the result
	//! @return Iterator over TP handles found in the net
	public Iterator<Set<AbstractElementModel>> getTPHandlesIterator() {
		calculateTPHandles();
		return m_TPHandles.iterator();
	}	
	
	//! Detect existing handle clusters
	//! and return them
	//! @return handle clusters found for the current net
	public HashSet<Set<AbstractElementModel>> getM_handleClusters() {				
		calculateHandleClusters();
		return m_handleClusters;
	}

	// ! Remember a reference to the current editor
	// ! as we need it to access the net
	private IEditor m_currentEditor;

	// ! Will become true once
	// ! the basic net info has been calculated
	boolean m_bBasicNetInfoAvailable = false;

	// ! Stores a set of all the places of
	// ! the processed net
	HashSet<AbstractElementModel> m_places = new HashSet<AbstractElementModel>();
	// ! Stores a set of all the transitions of
	// ! the processed net
	HashSet<AbstractElementModel> m_transitions = new HashSet<AbstractElementModel>();

	// ! Stores a set of all operators.
	// ! Operators are AND-split, AND-join
	// ! XOR-Split, XOR-Join, AND-Split-Join
	// ! XOR-Split-Join
	HashSet<AbstractElementModel> m_operators = new HashSet<AbstractElementModel>();
	
	//! Remember XOR-split operators and operators that 
	//! function as an XOR-split (e.g. xor-split-join)
	HashSet<AbstractElementModel> m_xorsplits = new HashSet<AbstractElementModel>();
	//! Remember XOR-join operators and operators that 
	//! function as an XOR-join (e.g. xor-split-join)	
	HashSet<AbstractElementModel> m_xorjoins = new HashSet<AbstractElementModel>();
	
	//! Remember AND-split operators and operators that 
	//! function as an AND-split (e.g. and-split-join)
	HashSet<AbstractElementModel> m_andsplits = new HashSet<AbstractElementModel>();
	//! Remember AND-join operators and operators that 
	//! function as an AND-join (e.g. and-split-join)	
	HashSet<AbstractElementModel> m_andjoins = new HashSet<AbstractElementModel>();

	
	// ! Stores the number of arcs contained in this net
	int m_nNumArcs = 0;

	// ! Stores a set of source places
	HashSet<AbstractElementModel> m_sourcePlaces = new HashSet<AbstractElementModel>();
	// ! Stores a set of sink places
	HashSet<AbstractElementModel> m_sinkPlaces = new HashSet<AbstractElementModel>();

	// ! Stores a set of source transitions
	HashSet<AbstractElementModel> m_sourceTransitions = new HashSet<AbstractElementModel>();
	// ! Stores a set of sink transitions
	HashSet<AbstractElementModel> m_sinkTransitions = new HashSet<AbstractElementModel>();

	// ! Misused operators are operators that
	// ! do not have a specific minimum or maximum of
	// ! input/output arcs
	// ! as is required by their operator type
	HashSet<AbstractElementModel> m_misusedOperators = new HashSet<AbstractElementModel>();

	boolean m_bConnectionInfoAvailable = false;

	// ! Stores a list of all nodes (transitions
	// ! and places that are not connected)
	HashSet<AbstractElementModel> m_notConnectedNodes = new HashSet<AbstractElementModel>();
	// ! Stores a list of all nodes (transitions
	// ! and places that are not strongly connected)
	HashSet<AbstractElementModel> m_notStronglyConnectedNodes = new HashSet<AbstractElementModel>();

	boolean m_bFreeChoiceInfoAvailable = false;
	// ! Stores a list of free-choice violations
	// ! consisting of node sets
	HashSet<Set<AbstractElementModel>> m_freeChoiceViolations = new HashSet<Set<AbstractElementModel>>();

	//! Becomes true once TP handles have been detected
	boolean m_bTPHandlesAvailable = false;
	// ! Stores a list of TP handles
	// ! consisting of node sets
	HashSet<Set<AbstractElementModel>> m_TPHandles = new HashSet<Set<AbstractElementModel>>();

	//! Becomes true once PT handles have been detected
	boolean m_bPTHandlesAvailable = false;
	// ! Stores a list of TP handles
	// ! consisting of node sets	
	HashSet<Set<AbstractElementModel>> m_PTHandles = new HashSet<Set<AbstractElementModel>>();
	
	private LowLevelNet lolnet = null;
	
	HashSet<Set<AbstractElementModel>> m_handles = new HashSet<Set<AbstractElementModel>>();
	
	//! Becomes true once handle clusters have been detected
	boolean m_bHandleClustersAvailable = false;
	HashSet<Set<AbstractElementModel>> m_handleClusters = new HashSet<Set<AbstractElementModel>>();
	
	// ! Trigger the calculation of basic net information
	private void calculateBasicNetInfo() {
		// We cache all calculated information
		// Check if we already know what we need to know
		if (m_bBasicNetInfoAvailable)
			return;
		m_bBasicNetInfoAvailable = true;

		// Get the element container containing all our elements
		ModelElementContainer elements = m_currentEditor.getModelProcessor()
				.getElementContainer();
		// Iterate through all elements and
		// take notes
		m_nNumArcs = 0;
		Iterator i = elements.getRootElements().iterator();
		updateStatistics(i);
		// Just ask the arc map for its size...
		m_nNumArcs += elements.getArcMap().size();
	}

	private void updateStatistics(Iterator i) {
		NetAlgorithms.ArcConfiguration arcConfig = new NetAlgorithms.ArcConfiguration();
		while (i.hasNext()) {
			try {
				AbstractElementModel currentNode = (AbstractElementModel) i
						.next();
				NetAlgorithms.GetArcConfiguration(currentNode, arcConfig);
				switch (currentNode.getType()) {
				case AbstractPetriNetModelElement.PLACE_TYPE:
					m_places.add(currentNode);
					if (arcConfig.m_numIncoming == 0)
						m_sourcePlaces.add(currentNode);
					if (arcConfig.m_numOutgoing == 0)
						m_sinkPlaces.add(currentNode);
					break;
				case AbstractPetriNetModelElement.TRANS_OPERATOR_TYPE: {
					OperatorTransitionModel operator = (OperatorTransitionModel) currentNode;
					// Remember the operator
					// A list of operators is provided for
					// statistical reasons
					m_operators.add(operator);
					int operatorType = operator.getOperatorType();
					if ((operatorType==OperatorTransitionModel.AND_SPLIT_TYPE)||
						(operatorType==OperatorTransitionModel.AND_SPLITJOIN_TYPE)||
						(operatorType==OperatorTransitionModel.XORJOIN_ANDSPLIT_TYPE))
					{
						m_andsplits.add(operator);						
					}
					if ((operatorType==OperatorTransitionModel.AND_JOIN_TYPE)||
							(operatorType==OperatorTransitionModel.AND_SPLITJOIN_TYPE)||
							(operatorType==OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE))					
					{
						m_andjoins.add(operator);
							
					}
					if ((operatorType==OperatorTransitionModel.XOR_SPLIT_TYPE)||
						(operatorType==OperatorTransitionModel.XOR_SPLITJOIN_TYPE)||
						(operatorType==OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE))
					{
						m_xorsplits.add(operator);
					}
					if ((operatorType==OperatorTransitionModel.XOR_JOIN_TYPE)||
							(operatorType==OperatorTransitionModel.XOR_SPLITJOIN_TYPE)||
							(operatorType==OperatorTransitionModel.XORJOIN_ANDSPLIT_TYPE))					
					{
						m_xorjoins.add(operator);							
					}
											
					
					// Verify that the operator has the correct arc
					// configuration
					// If this is not the case it will be added to the
					// misused operators list
					verifyOperatorArcConfiguration(operator, arcConfig);
					ModelElementContainer simpleTransContainer = operator
							.getSimpleTransContainer();
					// Recursively call ourselves to add inner nodes
					Iterator innerIterator = simpleTransContainer
							.getRootElements().iterator();
					updateStatistics(innerIterator);
					// To have the total number of arcs we must subtract
					// the number of incoming and outgoing arcs from the
					// number of inner arcs of the operator
					m_nNumArcs += simpleTransContainer.getArcMap().size()
							- (arcConfig.m_numIncoming + arcConfig.m_numOutgoing);
				}
					break;
				case AbstractPetriNetModelElement.TRANS_SIMPLE_TYPE:
					// Default behaviour for sub processes is to treat them as a
					// single transition
				case AbstractPetriNetModelElement.SUBP_TYPE:
					m_transitions.add(currentNode);
					if (arcConfig.m_numIncoming == 0)
						m_sourceTransitions.add(currentNode);
					if (arcConfig.m_numOutgoing == 0)
						m_sinkTransitions.add(currentNode);
					break;
				default:
					// Ignore all the rest
				}

			} catch (Exception e) {
			}
		}
	}

	// ! Check whether the specified operator has the correct arc configuration
	// ! and add it to the misused operators list if it doesn't
	// ! @param operator the operator to be verified
	// ! @param arcConfig specifies the previously determined arc configuration
	void verifyOperatorArcConfiguration(OperatorTransitionModel operator,
			NetAlgorithms.ArcConfiguration arcConfig) {
		boolean isCorrectConfiguration = true;
		switch (operator.getOperatorType()) {
		// All pure split operators must have exactly one input
		// and at least two outputs
		case OperatorTransitionModel.AND_SPLIT_TYPE:
		case OperatorTransitionModel.XOR_SPLIT_TYPE:
			isCorrectConfiguration = ((arcConfig.m_numIncoming == 1) && (arcConfig.m_numOutgoing > 1));
			break;
		// All pure join operators must have exactly one output
		// and at least two inputs
		case OperatorTransitionModel.AND_JOIN_TYPE:
		case OperatorTransitionModel.XOR_JOIN_TYPE:
			isCorrectConfiguration = ((arcConfig.m_numIncoming > 1) && (arcConfig.m_numOutgoing == 1));
			break;
		// All split-join types must have at least two inputs
		// as well as at least two outputs
		case OperatorTransitionModel.XOR_SPLITJOIN_TYPE:
		case OperatorTransitionModel.AND_SPLITJOIN_TYPE:
		case OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE:
		case OperatorTransitionModel.XORJOIN_ANDSPLIT_TYPE:
			isCorrectConfiguration = ((arcConfig.m_numIncoming > 1) && (arcConfig.m_numOutgoing > 1));
			break;
		}
		if (!isCorrectConfiguration)
			m_misusedOperators.add(operator);
	}

	private void calculateConnections() {
		if (m_bConnectionInfoAvailable == true)
			return;
		m_bConnectionInfoAvailable = true;

		// First, calculate basic net information
		calculateBasicNetInfo();
		LinkedList<AbstractElementModel> netElements = new LinkedList<AbstractElementModel>();
		// A WoPeD graph contains more than just places
		// and transitions. We are only interested in those
		// however
		netElements.addAll(m_places);
		netElements.addAll(m_transitions);

		if (m_transitions.size() == 0)
			return;

		// Add temporary transition t*, connecting sink to source
		AbstractElementModel ttemp = addTStar();
		if (ttemp != null)
			netElements.add(ttemp);

		// First check for connectedness:
		// Return connection map presuming that all arcs may be
		// used in both directions
		NetAlgorithms.RouteInfo[][] connectionGraph = NetAlgorithms
				.getAllConnections(netElements, true);
		if (connectionGraph != null)
			NetAlgorithms.getUnconnectedNodes(ttemp, connectionGraph,
					m_notConnectedNodes);

		// Now get the graph for strong connectedness
		// This will also give us all shortest distances
		// according to Moore's algorithm (no arc weights)
		NetAlgorithms.RouteInfo[][] strongConnectionGraph = NetAlgorithms
				.getAllConnections(netElements, false);
		if (strongConnectionGraph != null)
			NetAlgorithms.getUnconnectedNodes(ttemp, strongConnectionGraph,
					m_notStronglyConnectedNodes);

		if (ttemp != null)
			removeTStar(ttemp);
	}

	AbstractElementModel addTStar() {
		AbstractElementModel ttemp = null;
		// Create transition 't*'
		Iterator i = m_transitions.iterator();
		AbstractElementModel transitionTemplate = ((i.hasNext()) ? (AbstractElementModel) i
				.next()
				: null);
		if (transitionTemplate != null) {
			CreationMap tempMap = transitionTemplate.getCreationMap();
			tempMap.setType(AbstractPetriNetModelElement.TRANS_SIMPLE_TYPE);
			String tempID = "t*";
			tempMap.setName(tempID);
			tempMap.setId(tempID);
			tempMap.setEditOnCreation(false);
			ttemp = m_currentEditor.getModelProcessor().createElement(tempMap);

			// Now connect the new transition 't*' to
			// the source and the target
			// For this to be possible, we will need
			// a unique source and a unique sink
			if ((m_sourcePlaces.size() == 1) && (m_sinkPlaces.size() == 1)) {
				AbstractElementModel source = (AbstractElementModel) m_sourcePlaces
						.iterator().next();
				String sourceID = source.getId();
				AbstractElementModel target = (AbstractElementModel) m_sinkPlaces
						.iterator().next();
				String targetID = target.getId();
				Object newEdge = m_currentEditor.getModelProcessor().createArc(
						tempID, sourceID);
				ttemp.getPort().addEdge(newEdge);
				source.getPort().addEdge(newEdge);
				newEdge = m_currentEditor.getModelProcessor().createArc(
						targetID, tempID);
				if (newEdge != null) {
					ttemp.getPort().addEdge(newEdge);
					target.getPort().addEdge(newEdge);
				}
			}
		}
		return ttemp;
	}

	void removeTStar(AbstractElementModel tstar) {
		// Remove the element from the graph
		m_currentEditor.getModelProcessor().removeElement(tstar.getId());
	}

	void calculateFreeChoice() {
		if (m_bFreeChoiceInfoAvailable)
			return;
		m_bFreeChoiceInfoAvailable = true;

		// First, calculate basic net information
		calculateBasicNetInfo();

		// The first thing we look for are forward-branched places (conflicts)
		// and their follow-up transitions
		Set<Set<AbstractElementModel>> placeResults = getNonFreeChoiceGroups(
				m_places.iterator(), false);
		// Now look for backward-branched transitions (synchronization)
		// and their preceeding places
		// Commented out: Probably not really needed as this condition is
		// already covered with
		// the detection of forward-branched places 2007-01-15, AE
		// Set<Set<AbstractElementModel>> transitionResults =
		// getNonFreeChoiceGroups(m_transitions.iterator(),true);

		m_freeChoiceViolations.addAll(placeResults);
		// m_freeChoiceViolations.addAll(transitionResults);
	}

	Set<Set<AbstractElementModel>> getNonFreeChoiceGroups(Iterator i,
			boolean swapArcDirection) {
		Set<Set<AbstractElementModel>> result = new HashSet<Set<AbstractElementModel>>();
		// Look for forward-branched places (conflicts)
		// and their follow-up transitions
		while (i.hasNext()) {
			// Determine the arc configuration of the current place
			AbstractElementModel currentPlace = (AbstractElementModel) i.next();

			// Have a closer look at the follow-up transitions
			// Collect all affected nodes a priori just in case
			HashSet<AbstractElementModel> violationGroup = new HashSet<AbstractElementModel>();
			boolean violation = false;
			Set<AbstractElementModel> compareSet = null;
			Set<AbstractElementModel> successors = NetAlgorithms
					.getDirectlyConnectedNodes(
							currentPlace,
							swapArcDirection ? NetAlgorithms.connectionTypeINBOUND
									: NetAlgorithms.connectionTypeOUTBOUND);
			for (Iterator s = successors.iterator(); s.hasNext();) {
				AbstractElementModel successor = (AbstractElementModel) s
						.next();
				Set<AbstractElementModel> predecessors = NetAlgorithms
						.getDirectlyConnectedNodes(
								successor,
								swapArcDirection ? NetAlgorithms.connectionTypeOUTBOUND
										: NetAlgorithms.connectionTypeINBOUND);
				if (compareSet == null)
					compareSet = predecessors;
				else {
					// All predecessors of all successors of our
					// original place must be the same
					violation = violation || (!compareSet.equals(predecessors));
				}
				// Add the element and all its predecessors
				violationGroup.addAll(predecessors);
				violationGroup.add(successor);
			}
			if (violation) {
				// We have a violation, store the group in the list
				result.add(violationGroup);
			}
		}
		return result;
	}
	
	
	//! Detect PT handles using modified max flow / min cut
	//! algorithm
	void calculatePTHandles()
	{
		if (m_bPTHandlesAvailable)
			return;
		m_bPTHandlesAvailable = true;
		
		calculateBasicNetInfo();
		
		// Add the temporary transition 't*'
		// and consider it as another transition
		// This is necessary to detect 
		// handles in the short-circuited net
		AbstractElementModel tStar = addTStar();
		HashSet<AbstractElementModel> transitionsWithTStar
			= new HashSet<AbstractElementModel>(m_transitions);
		transitionsWithTStar.add(tStar);
		
 		// Detect all PT handles in the short-circuited net 
		LowLevelNet myNet = CreateFlowNet(m_places, transitionsWithTStar);
		Set<Set<AbstractElementModel>> handleRun = 
			getHandlePairs(myNet, m_places, transitionsWithTStar, false);
		m_PTHandles.addAll(handleRun);
		
		
		// Remove temporary transition from the net
		if (tStar != null)
			removeTStar(tStar);
		
		
	}
	
	//! Detect TP handles using modified max flow / min cut
	//! algorithm
	void calculateTPHandles()
	{
		if (m_bTPHandlesAvailable)
			return;
		m_bTPHandlesAvailable = true;

		calculateBasicNetInfo();
		
		// Add the temporary transition 't*'
		// and consider it as another transition
		// This is necessary to detect 
		// handles in the short-circuited net
		AbstractElementModel tStar = addTStar();
		HashSet<AbstractElementModel> transitionsWithTStar
			= new HashSet<AbstractElementModel>(m_transitions);
		transitionsWithTStar.add(tStar);
		
 		// Detect all PT handles in the short-circuited net 
		LowLevelNet myNet = CreateFlowNet(m_places, transitionsWithTStar);
		Set<Set<AbstractElementModel>> handleRun = 
			getHandlePairs(myNet, transitionsWithTStar, m_places, false);
		m_TPHandles.addAll(handleRun);
				
		// Remove temporary transition from the net
		if (tStar != null)
			removeTStar(tStar);
		
	}

	private void ExpandAndAddNode(LowLevelNet lolnet, AbstractElementModel i) {
		FlowNode k1 = new FlowNode(i, true);
		lolnet.addNode(k1);
		FlowNode k2 = new FlowNode(i, false);
		lolnet.addNode(k2);
		lolnet.addArc(k1, k2);
	}

	private void AddOutgoingArcs(LowLevelNet lolnet, AbstractElementModel i) {
		Set<AbstractElementModel> successors = NetAlgorithms
				.getDirectlyConnectedNodes(i,
						NetAlgorithms.connectionTypeOUTBOUND);
		FlowNode source = lolnet.getNodeForElement(i, false);
		for (Iterator s = successors.iterator(); s.hasNext();) {
			AbstractElementModel t = (AbstractElementModel) s.next();
			FlowNode target = lolnet.getNodeForElement(t, true);

			lolnet.addArc(source, target);
		}
	}

	//! Create the low level net used for the max-flow/min-cut algorithm
	//! from given sets of places and transitions
	//! @param places 		Specifies the set of places to be used
	//! @param transitions 	Specifies the set of transitions to be used	
	//! @return LowLevelNet structure
	private LowLevelNet CreateFlowNet(
			Set<AbstractElementModel> places,
			Set<AbstractElementModel> transitions
			) {
		lolnet = new LowLevelNet();
		for (Iterator<AbstractElementModel> i = places.iterator(); i.hasNext();) {
			ExpandAndAddNode(lolnet, (AbstractElementModel) i.next());
		}
		for (Iterator<AbstractElementModel> i = transitions.iterator(); i.hasNext();) {
			ExpandAndAddNode(lolnet, (AbstractElementModel) i.next());
		}

		for (Iterator<AbstractElementModel> i = places.iterator(); i.hasNext();) {
			AddOutgoingArcs(lolnet, (AbstractElementModel) i.next());
		}
		for (Iterator<AbstractElementModel> i = transitions.iterator(); i.hasNext();) {
			AddOutgoingArcs(lolnet, (AbstractElementModel) i.next());
		}
		return lolnet;
	}
	
	
	private void AddAalstNetOutgoingArcs(LowLevelNet lolnet, AbstractElementModel i) {
		Map<String,AbstractElementModel> successors = 
			this.m_currentEditor.getModelProcessor().
			getElementContainer().getTargetElements(i.getId());			
		FlowNode source = lolnet.getNodeForElement(i, false);
		for (Iterator<Map.Entry<String,AbstractElementModel>> s = successors.entrySet().iterator(); s.hasNext();) {
			AbstractElementModel t = (AbstractElementModel) s.next().getValue();
			FlowNode target = lolnet.getNodeForElement(t, true);

			lolnet.addArc(source, target);
		}
	}
	
	//! Create the low level net used for the max-flow/min-cut algorithm
	//! from given sets of places and transitions for the case
	//! where we want to analyze a van der Aalst net
	//! @param places 		Specifies the set of places to be used
	//! @param transitions 	Specifies the set of transitions to be used	
	//! @return LowLevelNet structure
	private LowLevelNet CreateAalstFlowNet() {
		lolnet = new LowLevelNet();
		
		List<AbstractElementModel> rootElements = m_currentEditor.getModelProcessor().getElementContainer().getRootElements();
		for (Iterator<AbstractElementModel> i = rootElements.iterator(); i.hasNext();) {
			ExpandAndAddNode(lolnet, i.next());
		}
		for (Iterator<AbstractElementModel> i = rootElements.iterator(); i.hasNext();) {
			AddAalstNetOutgoingArcs(lolnet, i.next());
		}
		
		
		return lolnet;
	}	
	
	//! Detect handle pairs were the first partner is from
	//! the set of first node types and the second partner is
	//! from the set of second node types (e.g. TP, PT, PP, TT handles).
	//! First nodes will be filtered for nodes with more than one successor
	//! Second nodes will be filtered for nodes with more than one
	//! predecessor
	//! @param n					Specifies the (pre-built) low-level net to be used 
	//! @param firstNodeType 		Specifies a set of first nodes
	//! @param secondNodeType 		Specifies a set of second nodes
	//! @param useVanderAalstModel	True if handles should be detected based on operators rather than 
	//!								a low-level net
	private Set<Set<AbstractElementModel>> getHandlePairs(
			LowLevelNet n,
			Set<AbstractElementModel> firstNodeType,
			Set<AbstractElementModel> secondNodeType,
			boolean useVanDerAalstModel) {

		NetAlgorithms.ArcConfiguration arcConfig = new NetAlgorithms.ArcConfiguration();
		
		Set<Set<AbstractElementModel>> result = 
			new HashSet<Set<AbstractElementModel>>();
		
		Iterator<AbstractElementModel> i = firstNodeType.iterator();
		long time1 = System.nanoTime();
		while (i.hasNext()){
			AbstractElementModel firstNode = i.next();
			if (useVanDerAalstModel)
			{
				Map<String,AbstractElementModel> elementMapRef = m_currentEditor.getModelProcessor().getElementContainer().getSourceElements(firstNode.getId());				
				arcConfig.m_numIncoming = (elementMapRef!=null)?elementMapRef.size():0;
				elementMapRef = m_currentEditor.getModelProcessor().getElementContainer().getTargetElements(firstNode.getId());
				arcConfig.m_numOutgoing = (elementMapRef!=null)?elementMapRef.size():0;				
			}
			else			
				NetAlgorithms.GetArcConfiguration(firstNode, arcConfig);
			if (arcConfig.m_numOutgoing > 1) {
				Iterator<AbstractElementModel> j = secondNodeType.iterator();
				while(j.hasNext()){
					AbstractElementModel secondNode = j.next();
					if (useVanDerAalstModel)
					{
						Map<String,AbstractElementModel> elementMapRef = m_currentEditor.getModelProcessor().getElementContainer().getSourceElements(secondNode.getId());
						arcConfig.m_numIncoming = (elementMapRef!=null)?elementMapRef.size():0;
						elementMapRef = m_currentEditor.getModelProcessor().getElementContainer().getTargetElements(secondNode.getId());
						arcConfig.m_numOutgoing = (elementMapRef!=null)?elementMapRef.size():0;				
					}
					else			
						NetAlgorithms.GetArcConfiguration(secondNode, arcConfig);
					
					if (arcConfig.m_numIncoming > 1) {
						FlowNode source = n.getNodeForElement(firstNode, false);
						FlowNode sink = n.getNodeForElement(secondNode, true);
						// Depending on the mode of operation, 
						// a corresponding flow node might not exist
						// (e.g. inner places and transition when analyzing van der Aalst nets)
						// Just ignore those cases
						if ((source!=null)&&(sink!=null))
						{
							int flow = n.getMaxFlow(source, sink);
							//flow = 2; //dummy Zeile zum Testen
							if (flow > 1) {
								// Handle gefunden
								Set<AbstractElementModel> handlePair = new HashSet<AbstractElementModel>();
								handlePair.add(firstNode);
								handlePair.add(secondNode);
								result.add(handlePair);
							}
						}
					}
				}
			}
		}
    	long time2 = System.nanoTime();
    	time2 = (time2-time1)/1000;
    	LoggerManager.debug(Constants.STRUCT_LOGGER, "Handle Pairs calculated. (" + time2 + " ms)");
		return result;
	}
	

	private void detectHandles(LowLevelNet n, boolean useVanDerAalstNet) {
		
		m_handles.clear();		
		Set<Set<AbstractElementModel>> handleRun = null;

		if (useVanDerAalstNet)
		{
			// When using van der Aalst nets we are looking
			// for handles consisting of specific operator types
			
			// Detect all PXORJOIN handles
			handleRun = getHandlePairs(n, m_places, m_xorjoins, true);
			m_handles.addAll(handleRun);
			// Detect all XORSPLITP handles
			handleRun = getHandlePairs(n, m_xorsplits, m_places, true);
			m_handles.addAll(handleRun);
			// Detect all XORSPLITJOIN handles
			handleRun = getHandlePairs(n, m_xorsplits, m_xorjoins, true);
			m_handles.addAll(handleRun);		
			// Detect all ANDSPLITJOIN handles
			handleRun = getHandlePairs(n, m_andsplits, m_andjoins, true);
			m_handles.addAll(handleRun);
			// Detect all ANDSPLITT handles
			handleRun = getHandlePairs(n, m_andsplits, m_transitions, true);
			m_handles.addAll(handleRun);
			// Detect all TANDJOIN handles
			handleRun = getHandlePairs(n, m_transitions, m_andjoins, true);
			m_handles.addAll(handleRun);
			// Detect all TT handles
			handleRun = getHandlePairs(n, m_transitions, m_transitions, true);
			m_handles.addAll(handleRun);
			// Detect all PP handles
			handleRun = getHandlePairs(n, m_places, m_places, true);
			m_handles.addAll(handleRun);

			
			
		}
		else
		{
			// Detect PP / TT handles within the low-level petri-net
			
			handleRun = getHandlePairs(n, m_places, m_places, false);
			m_handles.addAll(handleRun);
			handleRun = getHandlePairs(n, m_transitions, m_transitions, false);
			m_handles.addAll(handleRun);			
		}
		
		//3. Bilde HandleCluster
		createHandleClusters();
	}
	
	/*
	//Testmethode zum Testen von createHandleCluster()
	private void handleTest(Set<Set<AbstractElementModel>> testset, String testart){
		int durchlaufzaehler = 0;
		
		System.out.println(testart + "\n");
		Iterator<Set<AbstractElementModel>> testsetIter = testset.iterator();
		while (testsetIter.hasNext()){
			System.out.println("Paar Nr. " + durchlaufzaehler + ":");
			Iterator<AbstractElementModel>  innerTestsetIter = testsetIter.next().iterator();
			while (innerTestsetIter.hasNext()){
				System.out.println(innerTestsetIter.next().getId());
			}
			System.out.println("\n");
			durchlaufzaehler++;
		}
		System.out.println("------------------------------");
	} */
	
	//! This method creates handle clusters on the basis of handle pairs
	private void createHandleClusters(){
		AbstractElementModel      currentNode = null;
		boolean 			      dirty       = true;
		Set<AbstractElementModel> clusterA    = null;
		Set<AbstractElementModel> clusterB    = null;
		
		//Test, Handle-Ausgabe:
		//handleTest(m_handles, "Liste aller Handle-Paare:");
		
		//1st step: Create a new cluster for every handle (1:1 ratio)
		Iterator<Set<AbstractElementModel>> handleIter = m_handles.iterator();
		while (handleIter.hasNext()){
			m_handleClusters.add(new HashSet<AbstractElementModel>(handleIter.next()));
		}
		
		//Test, Handle-> Cluster 1:1:
		//handleTest(m_handleClusters, "Liste aller Handle->Cluster 1:1:");
		
		//2nd action: Merge all clusters if there are intersections.
		//   		  If dirty -> start all over.
		while (dirty){
			dirty = false;
		    Iterator<Set<AbstractElementModel>> clusterIterA = m_handleClusters.iterator();
			while (clusterIterA.hasNext() && !dirty){
				clusterA = clusterIterA.next();
				Iterator<AbstractElementModel> clusterAIter = clusterA.iterator();
				while(clusterAIter.hasNext() && !dirty){
					currentNode = clusterAIter.next();
					
				    Iterator<Set<AbstractElementModel>> clusterIterB = m_handleClusters.iterator();
					while (clusterIterB.hasNext() && !dirty){
						clusterB = clusterIterB.next();
						if (clusterA != clusterB){ 
							if (clusterB.contains(currentNode)){
							    clusterA.addAll(clusterB);
							    m_handleClusters.remove(clusterB);
								dirty = true;
							}
						}
					}
				}
			}
		}
		//Test, Cluster:
		//handleTest(m_handleClusters, "Liste aller Cluster:");
	}
	
	
	//! Do everything that is required to calculate handle clusters, 
	//! but only do so if handle clusters have not yet been calculated
	private void calculateHandleClusters()
	{
		if (m_bHandleClustersAvailable)
			return;
		m_bHandleClustersAvailable = true;
		
		calculateBasicNetInfo();
		
		// Set this variable to true if 
		// cluster building should be performed directly on the
		// "van der Aalst" net.
		// Otherwise, the low-level petrinet will be used
		boolean useVanDerAalstNet = false;
		//MN: If algorithm mode is set to 2 (van der Aalst net) use van der Aalst net 
		if (ConfigurationManager.getConfiguration().getAlgorithmMode() == 2){
			useVanDerAalstNet = true;
		}
		
		
		LowLevelNet myNet = useVanDerAalstNet?CreateAalstFlowNet():
			CreateFlowNet(m_places, m_transitions);
		detectHandles(myNet, useVanDerAalstNet);
	}
	
}
