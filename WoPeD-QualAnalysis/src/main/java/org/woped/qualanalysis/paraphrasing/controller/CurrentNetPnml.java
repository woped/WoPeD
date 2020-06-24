package org.woped.qualanalysis.paraphrasing.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.xmlbeans.XmlOptions;
import org.woped.core.controller.IEditor;
import org.woped.core.model.ArcModel;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.NameModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.pnml.ArcNameType;
import org.woped.pnml.ArcType;
import org.woped.pnml.NetType;
import org.woped.pnml.NodeNameType;
import org.woped.pnml.PlaceType;
import org.woped.pnml.PnmlDocument;
import org.woped.pnml.PnmlType;
import org.woped.pnml.TransitionType;
import org.woped.qualanalysis.paraphrasing.Constants;

public class CurrentNetPnml {

	private PnmlDocument pnmlDoc = null;

	private String pnmlString = null;

	private IEditor editor = null;

	private int arcCounter = 0;

	private int root = 0;

	private int sink = 0;

	private boolean isProcessable = false;

	public CurrentNetPnml() {
	}

	public CurrentNetPnml(IEditor editor) {
		this.editor = editor;
	}

	/**
	 * Reads all objects from the editor and creates a string with all necessary
	 * informations in the PNML format. Required for webservice.
	 * 
	 * @author Martin Meitz
	 * 
	 */
	public void setPnmlString() {
		ModelElementContainer elementContainer = editor.getModelProcessor().getElementContainer();
        PetriNetModelProcessor petrinetModel = editor.getModelProcessor();
        pnmlDoc = PnmlDocument.Factory.newInstance();
		PnmlType iPnml = pnmlDoc.addNewPnml();
		NetType iNet = iPnml.addNewNet();
		iNet.setType(petrinetModel.getType());
		iNet.setId(petrinetModel.getId());

		try {
			saveModelElementContainer(iNet, elementContainer);

			XmlOptions opt = new XmlOptions();
			opt.setUseDefaultNamespace();
			opt.setSavePrettyPrint();
			opt.setSavePrettyPrintIndent(2);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			pnmlDoc.save(baos, opt);

			this.pnmlString = baos.toString();

		} catch (IOException e) {
			LoggerManager.error(Constants.PARAPHRASING_LOGGER, "Could not write to the stream. " + e.getMessage());

		}
	}

	private void saveModelElementContainer(NetType iNet, ModelElementContainer elementContainer) {

		Iterator<AbstractPetriNetElementModel> root2Iter = elementContainer.getRootElements().iterator();
		while (root2Iter.hasNext()) {
            AbstractPetriNetElementModel currentModel = root2Iter.next();

			/* ##### PLACES ##### */
			if (currentModel.getType() == AbstractPetriNetElementModel.PLACE_TYPE) {
				initPlace(iNet.addNewPlace(), (PlaceModel) currentModel);
			}
			/* ##### NORMAL TRANSITION ##### */
			else if (currentModel.getType() == AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE) {
				initTransition(iNet.addNewTransition(), (TransitionModel) currentModel, null);

			}
			/* ##### SUB TRANSITION ##### */
			else if (currentModel.getType() == AbstractPetriNetElementModel.SUBP_TYPE) {
				initTransition(iNet.addNewTransition(), (TransitionModel) currentModel, null);
			}
			/* ##### OPERATOR TRANSITION ##### */
			else if (currentModel.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE) {
				OperatorTransitionModel operatorModel = (OperatorTransitionModel) currentModel;
				Iterator<AbstractPetriNetElementModel> simpleTransIter = operatorModel.getSimpleTransContainer()
						.getElementsByType(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE).values().iterator();
				while (simpleTransIter.hasNext()) {
                    AbstractPetriNetElementModel simpleTransModel = simpleTransIter
                            .next();
					if (simpleTransModel != null
							&& operatorModel.getSimpleTransContainer().getElementById(simpleTransModel.getId())
									.getType() == AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE) {
						initTransition(iNet.addNewTransition(), (TransitionModel) operatorModel
								.getSimpleTransContainer().getElementById(simpleTransModel.getId()), operatorModel);
					}

				}
				if (operatorModel.getCenterPlace() != null) {
					initPlace(iNet.addNewPlace(), operatorModel.getCenterPlace());
				}
			}
		}

		/* ##### ARCS ##### */
		this.arcCounter = 0;
		Set<AbstractPetriNetElementModel> connectedTransitions = new HashSet<AbstractPetriNetElementModel>();
		Iterator<String> arcIter = elementContainer.getArcMap().keySet().iterator();
		while (arcIter.hasNext()) {
			ArcModel currentArc = elementContainer.getArcById(arcIter.next());
            AbstractPetriNetElementModel currentTargetModel = elementContainer
                    .getElementById(currentArc.getTargetId());
            AbstractPetriNetElementModel currentSourceModel = elementContainer
                    .getElementById(currentArc.getSourceId());

			if (currentTargetModel.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE)
				connectedTransitions.add(currentTargetModel);
			else if (currentSourceModel.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE)
				connectedTransitions.add(currentSourceModel);
			else {
				initArc(iNet.addNewArc(), currentArc, null);
			}
		}

		Iterator<AbstractPetriNetElementModel> currentTransition = connectedTransitions.iterator();
		while (currentTransition.hasNext()) {
			OperatorTransitionModel currentConnectedModel = (OperatorTransitionModel) currentTransition.next();
			Iterator<String> innerArcIter = currentConnectedModel.getSimpleTransContainer().getArcMap().keySet()
					.iterator();
			while (innerArcIter.hasNext()) {
                ArcModel currentInnerArc = currentConnectedModel.getSimpleTransContainer().getArcMap()
                        .get(innerArcIter.next());

				ArcModel currentOuterArc = null;
				if (elementContainer.getElementById(currentInnerArc.getSourceId()) != null) {
					currentOuterArc = elementContainer.findArc(currentInnerArc.getSourceId(),
							currentConnectedModel.getId());
				}
				if (elementContainer.getElementById(currentInnerArc.getTargetId()) != null) {
					currentOuterArc = elementContainer.findArc(currentConnectedModel.getId(),
							currentInnerArc.getTargetId());
				}

				initArc(iNet.addNewArc(), (currentOuterArc != null) ? currentOuterArc : currentInnerArc,
						currentInnerArc);
			}
		}

		// Webservice can only process graphs that have one root and one sink
		// element
        this.isProcessable = this.root == 1 && this.sink == 1;
    }

	private PlaceType initPlace(PlaceType iPlace, PlaceModel currentModel) {
		// Name
		initNodeName(iPlace.addNewName(), currentModel.getNameModel());
		// Attribute ID
		iPlace.setId(currentModel.getId());

		if (currentModel.isRoot()) {
			root++;
		}
		if (currentModel.isSink()) {
			sink++;
		}
		return iPlace;
	}

	private NodeNameType initNodeName(NodeNameType nodeName, NameModel element) {
		// Name
		nodeName.setText(element.getNameValue());

		return nodeName;
	}

	private TransitionType initTransition(TransitionType iTransition, TransitionModel currentModel,
			OperatorTransitionModel operatorModel) {
		TransitionModel takenModel = operatorModel == null ? currentModel : operatorModel;
		// Name
		initNodeName(iTransition.addNewName(), takenModel.getNameModel());
		// Attribute ID
		iTransition.setId(currentModel.getId());

		return iTransition;
	}

	private ArcType initArc(ArcType iArc, ArcModel outerArc, ArcModel innerArc) {
		ArcModel useArc = innerArc == null ? outerArc : innerArc;
		// Inscription
		initNodeName(iArc.addNewInscription(), useArc);

		this.arcCounter++;

		// Attribute ID
		// iArc.setId(outerArc.getId());
		// For the webservice the arc ID is not important. To avoid errors
		// because of two same arc numbers, a simple arc counter
		// variable is used.
		iArc.setId("a" + this.arcCounter);
		// Attribute source
		iArc.setSource(useArc.getSourceId());

		// Attribute target
		iArc.setTarget(useArc.getTargetId());

		return iArc;
	}

	private ArcNameType initNodeName(ArcNameType nodeName, ArcModel element) {
		// Name
		nodeName.setText(element.getInscriptionValue());
		return nodeName;
	}

	/**
	 * Returns the current net in the PNML format as a string
	 * 
	 * @author Martin Meitz
	 * 
	 */
	public String getPnmlString() {
		return this.pnmlString;
	}

	/**
	 * Returns true if the webservice can process the net
	 * 
	 * @author Martin Meitz
	 * 
	 */
	public boolean isProcessable() {
		return this.isProcessable;
	}

}
