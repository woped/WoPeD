package org.woped.qualanalysis.woflan;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.woped.core.controller.IEditor;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.qualanalysis.Constants;
import org.woped.qualanalysis.algorithms.Woflan;
import org.woped.qualanalysis.service.interfaces.ISComponent;
import org.woped.qualanalysis.service.interfaces.ISoundnessCheck;

public class WoflanAnalysis implements ISoundnessCheck, ISComponent {

    private IEditor m_currentEditor = null;

    public WoflanAnalysis(IEditor currentEditor) {

        m_currentEditor = currentEditor;

        // This code will try to talk to WofLan
        // through the JNI
        m_tempFile = new WoflanFileFactory().createFile(currentEditor);
        m_myWofLan = new Woflan();

        // Open previously exported petri-net
        m_netHandle = m_myWofLan.Open(m_tempFile.getAbsolutePath());
    }

    public int getNumPTHandles() {
        calculateWellStructuredness();
        return getIntInfo(m_myWofLan.InfoNofPTH, 0, 0);
    }

    // ! Return a list of P/T handles
    // ! Each violation is represented by a pair of nodes
    // ! defining the violation
    // ! @return Iterator through a list of P/T handle pairs
    public Iterator<List<AbstractPetriNetElementModel>> getPTHandlesIterator() {
        calculateWellStructuredness();
        Iterator<List<AbstractPetriNetElementModel>> handles = getListOfListsIterator(getNumPTHandles(),
                m_myWofLan.InfoPTHNofN1, m_myWofLan.InfoPTHN1Name);
        // We are not interested in the path,
        // we really want to have the beginning and the end of it
        ArrayList<List<AbstractPetriNetElementModel>> result = new ArrayList<List<AbstractPetriNetElementModel>>();
        while (handles.hasNext()) {
            List<AbstractPetriNetElementModel> current = handles.next();
            int listLength = current.size();
            if (listLength >= 2) {
                AbstractPetriNetElementModel first = current.get(0);
                AbstractPetriNetElementModel last = current.get(listLength - 1);
                current.clear();
                current.add(first);
                current.add(last);
                result.add(current);
            }
        }
        return result.iterator();
    }

    public int getNumTPHandles() {
        calculateWellStructuredness();
        return getIntInfo(m_myWofLan.InfoNofTPH, 0, 0);
    }

    // ! Return a list of T/P handles
    // ! Each violation is represented by a pair of nodes
    // ! defining the violation
    // ! @return Iterator through a list of T/P handle pairs
    public Iterator<List<AbstractPetriNetElementModel>> getTPHandlesIterator() {
        calculateWellStructuredness();
        Iterator<List<AbstractPetriNetElementModel>> handles = getListOfListsIterator(getNumTPHandles(),
                m_myWofLan.InfoTPHNofN1, m_myWofLan.InfoTPHN1Name);
        // We are not interested in the path,
        // we really want to have the beginning and the end of it
        ArrayList<List<AbstractPetriNetElementModel>> result = new ArrayList<List<AbstractPetriNetElementModel>>();
        while (handles.hasNext()) {
            List<AbstractPetriNetElementModel> current = handles.next();
            int listLength = current.size();
            if (listLength >= 2) {
                AbstractPetriNetElementModel first = current.get(0);
                AbstractPetriNetElementModel last = current.get(listLength - 1);
                current.clear();
                current.add(first);
                current.add(last);
                result.add(current);
            }
        }
        return result.iterator();
    }

    public int getNumNotSCovered() {
        calculateSComponents();
        return uncoveredPlaces.size();
    }

    public Set<AbstractPetriNetElementModel> getNotSCovered() {
        calculateSComponents();
        return uncoveredPlaces;
    }

    public int getNumSComponents() {
        calculateSComponents();
        return getIntInfo(m_myWofLan.InfoNofSCom, 0, 0);
    }

    public Set<List<AbstractPetriNetElementModel>> getSComponents() {
        calculateSComponents();
        return getSetOfLists(getNumSComponents(), m_myWofLan.InfoSComNofN, m_myWofLan.InfoSComNName);
    }

    public int getNumNotNonNegPInvariantCovered() {
        calculateInvariants();
        return getIntInfo(m_myWofLan.InfoNofPotSPIn, 0, 0);
    }

    public Iterator<AbstractPetriNetElementModel> getNotNonNegPInvariantCoveredIterator() {
        calculateInvariants();
        return getListIterator(getNumNotNonNegPInvariantCovered(), m_myWofLan.InfoNotSPInPName);
    }

    public int getNumNonNegPInvariants() {
        calculateInvariants();
        return getIntInfo(m_myWofLan.InfoNofSPIn, 0, 0);
    }

    public Iterator<List<AbstractPetriNetElementModel>> getNonNegPInvariantsIterator() {
        calculateInvariants();
        return getListOfListsIterator(getNumPInvariants(), m_myWofLan.InfoSPInNofP, m_myWofLan.InfoSPInPName);
    }

    public int getNumNotPInvariantCovered() {
        calculateInvariants();
        return getIntInfo(m_myWofLan.InfoNofPotPInv, 0, 0);
    }

    public Iterator<AbstractPetriNetElementModel> getNotPInvariantCoveredIterator() {
        calculateInvariants();
        return getListIterator(getNumNotPInvariantCovered(), m_myWofLan.InfoNotPInvPName);
    }

    public int getNumPInvariants() {
        calculateInvariants();
        return getIntInfo(m_myWofLan.InfoNofPInv, 0, 0);
    }

    public Iterator<List<AbstractPetriNetElementModel>> getPInvariantsIterator() {
        calculateInvariants();
        return getListOfListsIterator(getNumPInvariants(), m_myWofLan.InfoPInvNofP, m_myWofLan.InfoPInvPName);
    }

    public int getNumDeadTransitions() {
        calculateLivenessInfo();
        return getIntInfo(m_myWofLan.InfoNofDeadT, 0, 0);
    }

    public Set<AbstractPetriNetElementModel> getDeadTransitions() {
        calculateLivenessInfo();
        return getSet(getNumDeadTransitions(), m_myWofLan.InfoDeadTName);
    }

    public int getNumNonLiveTransitions() {
        calculateLivenessInfo();
        return getIntInfo(m_myWofLan.InfoNofNLiveT, 0, 0);
    }

    public Set<AbstractPetriNetElementModel> getNonLiveTransitions() {
        calculateLivenessInfo();
        return getSet(getNumNonLiveTransitions(), m_myWofLan.InfoNLiveTName);
    }

    public int getNumNonLiveSequences() {
        calculateLivenessInfo();
        return getIntInfo(m_myWofLan.InfoNofNLiveS, 0, 0);
    }

    public Iterator<List<AbstractPetriNetElementModel>> getNonLiveSequencesIterator() {
        calculateLivenessInfo();
        return getListOfListsIterator(getNumNonLiveSequences(), m_myWofLan.InfoNLiveSNofT, m_myWofLan.InfoNLiveSTName);
    }

    public int getNumUnboundedSequences() {
        calculateBoundednessInfo();
        return getIntInfo(m_myWofLan.InfoNofUnbS, 0, 0);
    }

    public Iterator<List<AbstractPetriNetElementModel>> getUnboundedSequencesIterator() {
        calculateBoundednessInfo();
        return getListOfListsIterator(getNumUnboundedSequences(), m_myWofLan.InfoUnbSNofT, m_myWofLan.InfoUnbSTName);
    }

    public int getNumUnboundedPlaces() {
        calculateBoundednessInfo();
        return getIntInfo(m_myWofLan.InfoNofUnbP, 0, 0);
    }

    // ! Returns an iterator over all unbounded places of the net
    public Set<AbstractPetriNetElementModel> getUnboundedPlaces() {
        calculateBoundednessInfo();
        return getSet(getNumUnboundedPlaces(), m_myWofLan.InfoUnbPName);
    }

    public Iterator<AbstractPetriNetElementModel> getListIterator(int nNumElements, int nElementNameCommand) {
        ArrayList<AbstractPetriNetElementModel> result = new ArrayList<AbstractPetriNetElementModel>();
        for (int i = 0; i < nNumElements; ++i) {
            AbstractPetriNetElementModel current = getElementInfo(nElementNameCommand, i, 0);
            if (current != null) {
                result.add(current);
            }
        }
        return result.iterator();
    }

    public Iterator<List<AbstractPetriNetElementModel>> getListOfListsIterator(int nNumLists, int nNumElementsCommand,
            int nElementNameCommand) {
        ArrayList<List<AbstractPetriNetElementModel>> sequences = new ArrayList<List<AbstractPetriNetElementModel>>();
        for (int i = 0; i < nNumLists; ++i) {
            ArrayList<AbstractPetriNetElementModel> currentSequence = new ArrayList<AbstractPetriNetElementModel>();

            int nNumElementsInSequence = getIntInfo(nNumElementsCommand, i, 0);
            for (int j = 0; j < nNumElementsInSequence; ++j) {
                AbstractPetriNetElementModel element = getElementInfo(nElementNameCommand, i, j);
                if (element != null) {
                    currentSequence.add(element);
                }
            }
            sequences.add(currentSequence);
        }
        return sequences.iterator();
    }

    public Set<AbstractPetriNetElementModel> getSet(int nNumElements, int nElementNameCommand) {
        Set<AbstractPetriNetElementModel> result = new HashSet<AbstractPetriNetElementModel>();
        for (int i = 0; i < nNumElements; ++i) {
            AbstractPetriNetElementModel current = getElementInfo(nElementNameCommand, i, 0);
            if (current != null) {
                result.add(current);
            }
        }
        return result;
    }

    public Set<List<AbstractPetriNetElementModel>> getSetOfLists(int nNumLists, int nNumElementsCommand, int nElementNameCommand) {
        Set<List<AbstractPetriNetElementModel>> sequences = new HashSet<List<AbstractPetriNetElementModel>>();
        for (int i = 0; i < nNumLists; ++i) {
            ArrayList<AbstractPetriNetElementModel> currentSequence = new ArrayList<AbstractPetriNetElementModel>();

            int nNumElementsInSequence = getIntInfo(nNumElementsCommand, i, 0);
            for (int j = 0; j < nNumElementsInSequence; ++j) {
                AbstractPetriNetElementModel element = getElementInfo(nElementNameCommand, i, j);
                if (element != null) {
                    currentSequence.add(element);
                }
            }
            sequences.add(currentSequence);
        }
        return sequences;
    }

    private int getIntInfo(int nInfo, int nIndex, int nSubIndex) {
        String resultString = m_myWofLan.Info(m_netHandle, nInfo, nIndex, nSubIndex);
        int result = 0;
        try {
            result = Integer.parseInt(resultString);
        } catch (NumberFormatException e) {
        }
        return result;
    }

    private AbstractPetriNetElementModel getElementInfo(int nInfo, int nIndex, int nSubIndex) {
        String current = m_myWofLan.Info(m_netHandle, nInfo, nIndex, nSubIndex);
        AbstractPetriNetElementModel element = translateToNetObject(current);
        return element;
    }

    private boolean m_structurednessCalculated = false;

    private void calculateWellStructuredness() {
        if (m_structurednessCalculated) {
            return;
        }

        // Yes, create well-handledness info
        m_myWofLan.Info(m_netHandle, m_myWofLan.SetPTH, 0, 0);
        m_myWofLan.Info(m_netHandle, m_myWofLan.SetTPH, 0, 0);

        m_structurednessCalculated = true;
    }

    private boolean m_invariantsCalculated = false;

    private void calculateInvariants() {
        if (m_invariantsCalculated) {
            return;
        }

        // Calculate all invariants information we can get
        m_myWofLan.Info(m_netHandle, m_myWofLan.SetPInv, 0, 0);
        m_myWofLan.Info(m_netHandle, m_myWofLan.SetSPIn, 0, 0);
        m_myWofLan.Info(m_netHandle, m_myWofLan.SetTInv, 0, 0);
        m_myWofLan.Info(m_netHandle, m_myWofLan.SetSTIn, 0, 0);

        m_invariantsCalculated = true;

    }

    private boolean m_sComponentsCalculated = false;
    private Set<AbstractPetriNetElementModel> uncoveredPlaces = new HashSet<AbstractPetriNetElementModel>();

    private void calculateSComponents() {
        if (m_sComponentsCalculated) {
            return;
        }

        // Create S-Component information
        m_myWofLan.Info(m_netHandle, m_myWofLan.SetSCom, 0, 0);

        // Retrieve Woflan results immediately and store after some post-processing
        uncoveredPlaces.clear();
        Iterator<AbstractPetriNetElementModel> i = getListIterator(getIntInfo(m_myWofLan.InfoNofNotSCom, 0, 0),
                m_myWofLan.InfoNotSComNName);
        while (i.hasNext()) {
            AbstractPetriNetElementModel current = i.next();
            // Add only places to the uncovered list
            if (current.getType() == AbstractPetriNetElementModel.PLACE_TYPE) {
                uncoveredPlaces.add(current);
            }
        }

        m_sComponentsCalculated = true;
    }

    private boolean m_boundednessCalculated = false;

    private void calculateBoundednessInfo() {
        if (m_boundednessCalculated) {
            return;
        }

        // Calculate boundedness information
        m_myWofLan.Info(m_netHandle, m_myWofLan.SetUnb, 0, 0);
        m_boundednessCalculated = true;
    }

    private boolean m_livenessCalculated = false;

    private void calculateLivenessInfo() {
        if (m_livenessCalculated) {
            return;
        }
        // Calculate Liveness information
        m_myWofLan.Info(m_netHandle, m_myWofLan.SetNLive, 0, 0);
        m_livenessCalculated = true;
    }

    // ! Clean up when done...
    // ! Call this method to manually dispose Woflan information
    // ! Do not use this object afterwards
    public void cleanup() {
        if (m_netHandle != -1) {
            m_myWofLan.Close(m_netHandle);
            m_netHandle = -1;
        }
        // Delete the temporary file containing the petri-net dump
        if (m_tempFile != null) {
            m_tempFile.delete();
            m_tempFile = null;
        }

        LoggerManager.info(Constants.QUALANALYSIS_LOGGER, "Deleted temporary file for Workflow analysis.");

    }

    @Override
    protected void finalize() {
        // Call cleanup if we happen to receive a finalize() call from the garbage collector
        cleanup();
    }

    // ! Translate a string identifier as returned by the WOFLAN
    // ! analyser DLL into an object within the net that is currently
    // ! opened in the editor
    private AbstractPetriNetElementModel translateToNetObject(String name) {
        ModelElementContainer elements = m_currentEditor.getModelProcessor().getElementContainer();
        String objectID = null;
        int nOperator = name.lastIndexOf(OperatorTransitionModel.OPERATOR_SEPERATOR_TRANSITION);
        if (nOperator != -1) {
            // Cut any operator numbers at the end (operator
            // numbers are used for complex transitions such as
            // xor split / xor join
            objectID = name.substring(0, nOperator);
            int nRealIDIndex = objectID.lastIndexOf(OperatorTransitionModel.INNERID_SEPERATOR);
            if (nRealIDIndex != -1) {
                objectID = objectID.substring(nRealIDIndex + 1);
            }
        } else {
            // This is an ordinary element, just take its ID
            int nRealIDIndex = name.lastIndexOf(OperatorTransitionModel.INNERID_SEPERATOR);
            if (nRealIDIndex != -1) {
                objectID = name.substring(nRealIDIndex + 1);
            }
        }

        // Cut any hash sign that might exist
        if ((objectID != null) && (objectID.charAt(0) == '#')) {
            objectID = objectID.substring(1);
        }

        AbstractPetriNetElementModel current = ((objectID != null) ? elements.getElementById(objectID) : null);
        return current;
    }

    // ! Stores a private reference to the WOFLAN analysis DLL
    private Woflan m_myWofLan = null;
    // ! Remember a handle to the petri-net that is currently open
    private int m_netHandle = -1;
    // ! Remember a reference to the temporary file
    // ! containing a dump of our net
    // ! It will be deleted when no longer needed (when this object is destroyed)
    private File m_tempFile;

}
