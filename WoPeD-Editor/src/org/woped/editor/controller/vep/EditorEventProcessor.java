package org.woped.editor.controller.vep;

import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.AbstractEventProcessor;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.ArcModel;
import org.woped.core.model.CreationMap;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.AbstractPetriNetModelElement;
import org.woped.core.model.petrinet.GroupModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.model.petrinet.TriggerModel;
import org.woped.core.model.uml.AbstractUMLElementModel;
import org.woped.core.model.uml.OperatorModel;
import org.woped.core.model.uml.StateModel;
import org.woped.editor.controller.PlacePropertyEditor;
import org.woped.editor.controller.TransitionPropertyEditor;
import org.woped.editor.controller.vc.EditorVC;

public class EditorEventProcessor extends AbstractEventProcessor
{
    public EditorEventProcessor(int vepID, AbstractApplicationMediator mediator)
    {
        super(vepID, mediator);
    }

    public void processViewEvent(AbstractViewEvent event)
    {
        EditorVC editor;
        if (event.getSource() instanceof EditorVC)
        {
            editor = (EditorVC) event.getSource();
        } else
        {
            editor = (EditorVC) getMediator().getUi().getEditorFocus();
        }
        Object cell;
        ArcModel anArc;
        Iterator anIter;
        if (editor != null)
        {
            CreationMap map = CreationMap.createMap();
            if (editor.getLastMousePosition() != null)
            {
                map.setPosition((int) editor.getLastMousePosition().getX(), (int) editor.getLastMousePosition().getY());
            }
//            map.setEditOnCreation(false);
            switch (event.getOrder())
            {
            // Petrinet
            case AbstractViewEvent.ADD_PLACE:
                map.setType(AbstractPetriNetModelElement.PLACE_TYPE);
                editor.createElement(map);
                break;
            case AbstractViewEvent.ADD_TRANSITION:
                map.setType(AbstractPetriNetModelElement.TRANS_SIMPLE_TYPE);
                editor.createElement(map);
                break;
            case AbstractViewEvent.ADD_ANDJOIN:
                map.setType(AbstractPetriNetModelElement.TRANS_OPERATOR_TYPE);
                map.setOperatorType(OperatorTransitionModel.AND_JOIN_TYPE);
                editor.createElement(map);
                break;
            case AbstractViewEvent.ADD_ANDSPLIT:
                map.setType(AbstractPetriNetModelElement.TRANS_OPERATOR_TYPE);
                map.setOperatorType(OperatorTransitionModel.AND_SPLIT_TYPE);
                editor.createElement(map);
                break;
            case AbstractViewEvent.ADD_XORJOIN:
                map.setType(AbstractPetriNetModelElement.TRANS_OPERATOR_TYPE);
                map.setOperatorType(OperatorTransitionModel.XOR_JOIN_TYPE);
                editor.createElement(map);
                break;
            case AbstractViewEvent.ADD_XORSPLIT:
                map.setType(AbstractPetriNetModelElement.TRANS_OPERATOR_TYPE);
                map.setOperatorType(OperatorTransitionModel.XOR_SPLIT_TYPE);
                editor.createElement(map);
                break;
            case AbstractViewEvent.ADD_XORSPLITJOIN:
                map.setType(AbstractPetriNetModelElement.TRANS_OPERATOR_TYPE);
                map.setOperatorType(OperatorTransitionModel.XOR_SPLITJOIN_TYPE);
                editor.createElement(map);
                break;
            // UML
            case AbstractViewEvent.ADD_ACTIVITY:
                map.setType(AbstractUMLElementModel.ACTIVITY_TYPE);
                editor.createElement(map);
                break;
            case AbstractViewEvent.ADD_START:
                map.setType(AbstractUMLElementModel.STATE_TYPE);
                map.setStateType(StateModel.STATE_START_TYPE);
                editor.createElement(map);
                break;
            case AbstractViewEvent.ADD_STOP:
                map.setType(AbstractUMLElementModel.STATE_TYPE);
                map.setStateType(StateModel.STATE_STOP_TYPE);
                editor.createElement(map);
                break;
            case AbstractViewEvent.ADD_AND:
                map.setType(AbstractUMLElementModel.OPERATOR_TYPE);
                map.setOperatorType(OperatorModel.AND_TYPE);
                editor.createElement(map);
                break;
            case AbstractViewEvent.ADD_XOR:
                map.setType(AbstractUMLElementModel.OPERATOR_TYPE);
                map.setOperatorType(OperatorModel.XOR_TYPE);
                editor.createElement(map);
                break;
            // General
            case AbstractViewEvent.RENAME:
                cell = editor.getGraph().getSelectionCell();
                if (cell instanceof GroupModel)
                {
                    cell = ((GroupModel) cell).getMainElement();
                }
                editor.edit(cell);
                break;
            case AbstractViewEvent.REMOVE:
                editor.deleteSelection();
                break;
            case AbstractViewEvent.CUT:
                editor.cutSelection();
                break;
            case AbstractViewEvent.COPY:
                editor.copySelection();
                break;
            case AbstractViewEvent.PASTE:
                editor.pasteAtLastMousePosition();
                break;
            case AbstractViewEvent.REDO:
                editor.redo();
                break;
            case AbstractViewEvent.UNDO:
                editor.undo();
                break;
            case AbstractViewEvent.UNGROUP:
                editor.getGraph().ungroupSelection();
                break;
            case AbstractViewEvent.GROUP:
                editor.getGraph().groupSelection();
                break;
            case AbstractViewEvent.OPEN_PROPERTIES:
                cell = editor.getGraph().getSelectionCell();
                AbstractElementModel element = null;
                if (cell instanceof GroupModel)
                {
                    element = ((GroupModel) cell).getMainElement();
                } else if (cell instanceof AbstractElementModel)
                {
                    element = (AbstractElementModel) cell;
                }
                if (element != null && editor.getModelProcessor().getProcessorType() == PetriNetModelProcessor.MODEL_PROCESSOR_PETRINET)
                {
                    if (element instanceof TransitionModel)
                    {
                        TransitionPropertyEditor prop = new TransitionPropertyEditor((JFrame) getMediator().getUi(), (TransitionModel) element, editor);
                    }
                    if (element instanceof PlaceModel)
                    {
                        PlacePropertyEditor prop = new PlacePropertyEditor((JFrame) getMediator().getUi(), (PlaceModel) element, editor);
                    }
                }
                break;
            case AbstractViewEvent.ADD_POINT:
                editor.addPointToSelectedArc();
                break;
            case AbstractViewEvent.REMOVE_POINT:
                editor.removeSelectedPoint();
                break;
            case AbstractViewEvent.ADD_EXT_TRIGGER:
                cell = editor.getGraph().getSelectionCell();
                removeResources(editor, cell);
                editor.createTrigger(getCreateTriggerMap(editor.getGraph().getSelectionCell(), TriggerModel.TRIGGER_EXTERNAL));
                break;
            case AbstractViewEvent.ADD_RES_TRIGGER:
                editor.createTrigger(getCreateTriggerMap(editor.getGraph().getSelectionCell(), TriggerModel.TRIGGER_RESOURCE));
                break;
            case AbstractViewEvent.ADD_TIME_TRIGGER:
                cell = editor.getGraph().getSelectionCell();
                removeResources(editor, cell);
                editor.createTrigger(getCreateTriggerMap(editor.getGraph().getSelectionCell(), TriggerModel.TRIGGER_TIME));
                break;
            case AbstractViewEvent.REMOVE_TRIGGER:
                cell = editor.getGraph().getSelectionCell();
                removeResources(editor, cell);
                if (cell instanceof GroupModel)
                {
                    cell = ((GroupModel)cell).getMainElement();
                }
                if (cell instanceof TransitionModel)
                {
                    editor.deleteCell(((TransitionModel)cell).getToolSpecific().getTrigger(), true);
                 }
                break;
            case AbstractViewEvent.ADD_SUBPROCESS:
                editor.createElement(AbstractPetriNetModelElement.SUBP_TYPE, -1, editor.getLastMousePosition(), false);
                break;
            case AbstractViewEvent.OPEN_SUBPROCESS:
                // logger.warn("opening Subprocess is not implemented, yet");
                // super("Action.Subprocess.Open");
                // VisualController.getInstance().addElement(this,
                // VisualController.NEVER, VisualController.IGNORE,
                // VisualController.IGNORE);
                JOptionPane.showMessageDialog(editor, "Subprocessing is not implemted, yet.", "Beta Version Error", JOptionPane.ERROR_MESSAGE);
                break;
            case AbstractViewEvent.ROUTING_ACTIVE:
                cell = editor.getGraph().getSelectionCell();
                ((ArcModel) cell).setRoute(true);
                editor.getGraph().connect(((ArcModel) cell));
                break;
            case AbstractViewEvent.ROUTING_DEACTIVE:
                cell = editor.getGraph().getSelectionCell();
                ((ArcModel) cell).setRoute(false);
                editor.getGraph().connect(((ArcModel) cell));
                break;
            case AbstractViewEvent.ROUTING_ALL_ACTIVE:
                anIter = editor.getModelProcessor().getElementContainer().getArcMap().keySet().iterator();

                while (anIter.hasNext())
                {
                    anArc = editor.getModelProcessor().getElementContainer().getArcById(anIter.next());
                    anArc.setRoute(true);
                    editor.getGraph().connect(anArc);
                }
                editor.updateNet();
                break;
            case AbstractViewEvent.ROUTING_ALL_DEACTIVE:
                anIter = editor.getModelProcessor().getElementContainer().getArcMap().keySet().iterator();
                while (anIter.hasNext())
                {
                    anArc = editor.getModelProcessor().getElementContainer().getArcById(anIter.next());
                    anArc.setRoute(false);
                    editor.getGraph().connect(anArc);
                }
                editor.updateNet();
                break;
            case AbstractViewEvent.ADD_TOKEN:
                if (editor.getModelProcessor().getProcessorType() == PetriNetModelProcessor.MODEL_PROCESSOR_PETRINET)
                {
                    if ((cell = editor.getGraph().getSelectionCell()) instanceof GroupModel)
                    {
                        cell = ((GroupModel) cell).getMainElement();
                    }
                    if (cell instanceof PlaceModel)
                    {
                        ((PlaceModel) cell).addToken();
                    }
                    editor.updateNet();
                    editor.setSaved(false);
                }
                break;
            case AbstractViewEvent.REMOVE_TOKEN:
                if (editor.getModelProcessor().getProcessorType() == PetriNetModelProcessor.MODEL_PROCESSOR_PETRINET)
                {
                    if ((cell = editor.getGraph().getSelectionCell()) instanceof GroupModel)
                    {
                        cell = ((GroupModel) cell).getMainElement();
                    }
                    if (cell instanceof PlaceModel)
                    {
                        ((PlaceModel) cell).removeToken();
                    }
                    editor.updateNet();
                    editor.setSaved(false);
                }
                break;
            case AbstractViewEvent.ZOOM_IN:
                editor.zoom(0.1, false);
                break;
            case AbstractViewEvent.ZOOM_OUT:
                editor.zoom(-0.1, false);
                break;
            case AbstractViewEvent.ZOOM_ABSOLUTE:
                editor.zoom(Integer.parseInt((String) event.getData()), true);
                break;
            case AbstractViewEvent.TOGGLE_TOKENGAME:
                editor.toggleTokenGame();
                break;
            case AbstractViewEvent.PRESS:
                editor.scaleNet(0.5);
                break;
            case AbstractViewEvent.STRETCH:
                editor.scaleNet(2);
                break;
            default:
                break;
            }
        }
    }

    private void removeResources(EditorVC editor, Object cell)
    {
        if (cell instanceof GroupModel)
        {
            cell = ((GroupModel)cell).getMainElement();
        }
        if (cell instanceof TransitionModel)
        {
            TransitionModel trans = (TransitionModel)cell;
            if (trans.hasResource())
            {
                editor.deleteCell(trans.getToolSpecific().getTransResource(), true);
            }
        }
    }

    private CreationMap getCreateTriggerMap(Object cell, int triggertype)
    {
        if (cell != null)
        {
            CreationMap map = null;
            if (cell instanceof GroupModel)
            {
                cell = ((GroupModel) cell).getMainElement();
            }
            if (cell instanceof TransitionModel)
            {
                map = ((TransitionModel) cell).getCreationMap();
            }
            if (map != null)
            {
                map.setTriggerType(triggertype);
            }
            return map;
        }
        return null;
    }    
}