package org.woped.editor.controller.vc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.border.LineBorder;
import javax.swing.event.InternalFrameEvent;

import org.jgraph.event.GraphModelEvent;
import org.woped.core.config.DefaultStaticConfiguration;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.IEditor;
import org.woped.core.model.CreationMap;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.EditorLayoutInfo;
import org.woped.core.model.petrinet.ResourceClassModel;
import org.woped.core.model.petrinet.ResourceModel;
import org.woped.core.model.petrinet.SubProcessModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.editor.controller.EditorClipboard;
import org.woped.editor.controller.WoPeDUndoManager;
import org.woped.qualanalysis.structure.NetAlgorithms;

public class SubprocessEditorVC extends EditorVC {

	private SubProcessModel model = null;
	// for subprocess
	private AbstractPetriNetElementModel m_subprocessInput = null;
	private AbstractPetriNetElementModel m_subprocessOutput = null;
	private IEditor m_parentEditor = null;

	public SubprocessEditorVC(String string, EditorClipboard clipboard,
			boolean undoSupport, IEditor parentEditor,
			SubProcessModel model, AbstractApplicationMediator mediator) {
		super(string, clipboard, undoSupport, mediator);

		boolean origStatus = parentEditor.isSaved();
		this.model = model;

		setParentEditor(parentEditor);
		m_graph.setBorder(new LineBorder(
				DefaultStaticConfiguration.DEFAULT_SUBPROCESS_FRAME_COLOR, 3,
				false));

		setName("Subprocess " + model.getNameValue());

		// Switch editor to use the model element container of the subprocess
		ModelElementContainer container = model.getSimpleTransContainer();

		getModelProcessor().setElementContainer(container);
		model.getCreationMap().setSubElementContainer(container);

		// Get list of input nodes, presume that there is exactly one source and one sink,
		// which is a valid assumption for sub processes and should have been verified before
		// this constructor is called
		Set<AbstractPetriNetElementModel> sources = NetAlgorithms
				.getDirectlyConnectedNodes(model,
						NetAlgorithms.connectionTypeINBOUND);
		Iterator<AbstractPetriNetElementModel> sourceKeyIterator = sources.iterator();
		AbstractPetriNetElementModel sourceModel = sourceKeyIterator.next();
		setSubprocessInput(sourceModel);

		// Check whether the source element already exists
		if (container.getElementById(sourceModel.getId()) == null) {
			CreationMap sourceCreationMap = sourceModel.getCreationMap();
			if (((EditorVC) parentEditor).isCopyFlag() == false) {
			container.getArcMap().clear();
			container.getIdMap().clear();
			}
			sourceCreationMap.setPosition(10, 160);
			sourceCreationMap.setReadOnly(true);
			sourceCreationMap.setNamePosition(30, 200);
			sourceCreationMap.setEditOnCreation(false);
			sourceCreationMap.setUpperElement(sourceModel);
			createElement(sourceCreationMap, true, true);
		} else {
			container.getElementById(sourceModel.getId()).setNameValue(
					getSubprocessInput().getNameValue());
			container.getElementById(sourceModel.getId()).setReadOnly(true);
		}

		// Get list of output nodes
		Set<AbstractPetriNetElementModel> targets = NetAlgorithms
				.getDirectlyConnectedNodes(model,
						NetAlgorithms.connectionTypeOUTBOUND);
		Iterator<AbstractPetriNetElementModel> targetKeyIterator = targets.iterator();
		AbstractPetriNetElementModel targetModel = targetKeyIterator.next();
		setSubprocessOutput(targetModel);
		// Check whether the target element already exists
		if (container.getElementById(targetModel.getId()) == null) {
			CreationMap targetCreationMap = targetModel.getCreationMap();
			targetCreationMap.setPosition(520, 160);
			targetCreationMap.setReadOnly(true);
			targetCreationMap.setNamePosition(540, 200);
			targetCreationMap.setEditOnCreation(false);
			targetCreationMap.setUpperElement(targetModel);
			createElement(targetCreationMap, true, true);

		} else {
			container.getElementById(targetModel.getId()).setNameValue(
					getSubprocessOutput().getNameValue());
			container.getElementById(targetModel.getId()).setReadOnly(true);
		}

		// We must create a JGraph model
		// representation of the new model element container
		// Disable undo while doing so just as we do for loading a PNML file
		if (getGraph().getUndoManager() != null) {
			((WoPeDUndoManager) getGraph().getUndoManager()).setEnabled(false);
		}
		getGraph().drawNet(getModelProcessor());
		if (getGraph().getUndoManager() != null) {
			((WoPeDUndoManager) getGraph().getUndoManager()).setEnabled(true);
		}
		updateNet();
		// Clear selection, we do not want newly created elements to be selected
		getGraph().clearSelection();

		// Keep the tree model of the parent editor up to date
		if ((parentEditor != null) && (getEditorPanel().GetTreeModel() != null)
				&& (parentEditor instanceof EditorVC)) {
			getEditorPanel().GetTreeModel().addTreeModelListener(((EditorVC) parentEditor)
					.getEditorPanel().GetTreeModel());
		}

		// Try to retrieve saved layout information from the Model Element
		// container
		// and set it for this editor window
		EditorLayoutInfo layoutInfo = container.getEditorLayoutInfo();
		if (layoutInfo != null) {
			getEditorPanel().setSavedLayoutInfo(layoutInfo);
		}
		// Copy resources from parentEditor to subprocessEditor
		Vector<ResourceModel> res = parentEditor.getModelProcessor().getResources();
		getModelProcessor().setResources(res);

		HashMap<String, Vector<String>> mapping = parentEditor.getModelProcessor().getResourceMapping();
		getModelProcessor()
				.setResourceMapping(mapping);

		Vector<ResourceClassModel> units = parentEditor.getModelProcessor().getOrganizationUnits();
		getModelProcessor()
				.setOrganizationUnits(units);

		Vector<ResourceClassModel> roles = parentEditor.getModelProcessor().getRoles();
		getModelProcessor().setRoles(roles);

		Map<String, AbstractPetriNetElementModel> transitions;
		TransitionModel trans;
		if (!(transitions = container.getElementsByType(2)).isEmpty()) {
			Iterator<AbstractPetriNetElementModel> iterTrans = transitions.values().iterator();
			while (iterTrans.hasNext()) {
				trans = (TransitionModel) iterTrans.next();
				if (trans.isIncomingTarget())
					createArc(this.m_subprocessInput.getId(), trans.getId());
				if (trans.isOutgoingSource())
					createArc(trans.getId(), this.m_subprocessOutput.getId());

			}
		}
		// Restore original "edited" status of parent editor
		// because creation of source and target places should not
		// influence the parent model
		parentEditor.setSaved(origStatus);
	}

	public SubProcessModel getModel() {
		return model;
	}

	public boolean isSubprocessEditor() {
		return true;
	}

	public AbstractPetriNetElementModel getSubprocessInput() {
		return m_subprocessInput;
	}

	public void setSubprocessInput(AbstractPetriNetElementModel p_subprocessInput) {
		m_subprocessInput = p_subprocessInput;
	}

	public AbstractPetriNetElementModel getSubprocessOutput() {
		return m_subprocessOutput;
	}

	public void setSubprocessOutput(AbstractPetriNetElementModel p_subprocessOutput) {
		m_subprocessOutput = p_subprocessOutput;
	}

	protected void clearYourself() {
		super.clearYourself();

		m_subprocessInput = null;
		m_subprocessOutput = null;
		m_parentEditor = null;
	}
	
	public void graphChanged(GraphModelEvent e) {
		getParentEditor().setSaved(false);
		// Nils Lamb, Jan 2012
		// trigger update of status bar in sub prozess
		if (getEditorPanel().m_statusbar != null) getEditorPanel().m_statusbar.updateStatus();
		super.graphChanged(e);
	}

	public void internalFrameActivated(InternalFrameEvent e) {
		// Do nothing in case of sub process editor
	}
	
	public void internalFrameClosing(InternalFrameEvent e) {
		// Remember our layout if this is a sub-process editor
		this.getModelProcessor().getElementContainer()
		.setEditorLayoutInfo(getEditorPanel().getSavedLayoutInfo());
	}

	public void setSaved(boolean savedFlag) {
		// Ignore saved flag for subprocesses. This is dealt with by our parent
	}

	public IEditor getParentEditor() {
		return m_parentEditor;
	}

	public void setParentEditor(IEditor editor) {
		m_parentEditor = editor;
	}
}
