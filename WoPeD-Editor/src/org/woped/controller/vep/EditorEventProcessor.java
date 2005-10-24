package org.woped.controller.vep;

import java.awt.Point;
import java.util.Iterator;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.woped.controller.AbstractApplicationMediator;
import org.woped.controller.AbstractEventProcessor;
import org.woped.controller.AbstractViewEvent;
import org.woped.controller.vc.EditorVC;
import org.woped.gui.DefaultPropertiesDialog;
import org.woped.model.ArcModel;
import org.woped.model.CreationMap;
import org.woped.model.petrinet.AbstractPetriNetModelElement;
import org.woped.model.petrinet.GroupModel;
import org.woped.model.petrinet.OperatorTransitionModel;
import org.woped.model.petrinet.TransitionModel;
import org.woped.model.petrinet.TriggerModel;
import org.woped.model.uml.AbstractUMLElementModel;
import org.woped.model.uml.OperatorModel;
import org.woped.model.uml.StateModel;
import org.woped.utilities.Utils;
import org.woped.utilities.WoPeDLogger;

public class EditorEventProcessor extends AbstractEventProcessor implements WoPeDLogger
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
            map.setPosition((int) editor.getLastMousePosition().getX(), (int) editor.getLastMousePosition().getY());
            map.setEditOnCreation(false);
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
            case AbstractViewEvent.EDIT:
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
                Point screenPoint = new Point((int) editor.getLastMousePosition().getX(), (int) editor.getLastMousePosition().getY());
                SwingUtilities.convertPointToScreen(screenPoint, editor);
                JDialog prop;

                if (getMediator().getUi() != null && getMediator().getUi().getComponent() instanceof JFrame)
                {
                    prop = new DefaultPropertiesDialog((JFrame) getMediator().getUi());
                } else
                {
                    prop = new DefaultPropertiesDialog();
                }
                prop.setLocation(Utils.getCenterPoint(editor.getBounds(), prop.getSize()));
                prop.setVisible(true);
                // TODO:
                // m_selection = selection;
                // m_parameters = new Hashtable();
                //            
                // if (selection != null)
                // {
                // if (selection.length == 1)
                // {
                // if (selection[0] instanceof GroupModel)
                // {
                // selection[0] = ((GroupModel) selection[0]).getMainElement();
                // }
                // if (selection[0] instanceof PetriNetModelElement)
                // {
                // PetriNetModelElement element = (PetriNetModelElement)
                // selection[0];
                // if (element instanceof PlaceModel)
                // {
                // getContentPane().setLayout(new GridLayout(4, 2));
                // getContentPane().add(new JLabel("ID:"));
                // getContentPane().add(new JLabel(element.getId()));
                // getContentPane().add(new JLabel("Name:"));
                // JTextField name = new JTextField(element.getNameValue());
                // m_parameters.put(NAME, name);
                // getContentPane().add(name);
                // getContentPane().add(new JLabel("Tokens:"));
                // JTextField tokens = new JTextField("" + ((PlaceModel)
                // element).getTokenCount());
                // m_parameters.put(TOKENS, tokens);
                // getContentPane().add(tokens);
                // getContentPane().add(new JButton(new
                // SavePropertiesAction()));
                // getContentPane().add(new JButton(new DisposeWindowAction()));
                //
                // } else if (element instanceof TransitionModel)
                // {
                // getContentPane().setLayout(new GridLayout(3, 2));
                // getContentPane().add(new JLabel("ID:"));
                // getContentPane().add(new JLabel(element.getId()));
                // getContentPane().add(new JLabel("Name:"));
                // JTextField name = new JTextField(element.getNameValue());
                // m_parameters.put(NAME, name);
                // getContentPane().add(name);
                // getContentPane().add(new JButton(new
                // SavePropertiesAction()));
                // getContentPane().add(new JButton(new DisposeWindowAction()));
                // }
                // } else
                // {
                // throw new IllegalArgumentException("Not suported!");
                // }
                // } else if (selection.length > 1)
                // {
                // throw new IllegalArgumentException("Not suported!");
                // } else
                // {
                // throw new IllegalArgumentException("No Selection!");
                // }
                // } else
                // {
                // throw new IllegalArgumentException("No Selection!");
                // }
                // pack();
                break;
            case AbstractViewEvent.ADD_POINT:
                editor.addPointToSelectedArc();
                break;
            case AbstractViewEvent.REMOVE_POINT:
                editor.removePoint();
                break;
            case AbstractViewEvent.ADD_EXT_TRIGGER:
                editor.createTrigger(getCreateTriggerMap(editor.getGraph().getSelectionCell(), TriggerModel.TRIGGER_EXTERNAL));
                break;
            case AbstractViewEvent.ADD_RES_TRIGGER:
                editor.createTrigger(getCreateTriggerMap(editor.getGraph().getSelectionCell(), TriggerModel.TRIGGER_RESOURCE));
                break;
            case AbstractViewEvent.ADD_TIME_TRIGGER:
                editor.createTrigger(getCreateTriggerMap(editor.getGraph().getSelectionCell(), TriggerModel.TRIGGER_TIME));
                break;
            case AbstractViewEvent.REMOVE_TRIGGER:
                cell = editor.getGraph().getSelectionCell();
                if (cell instanceof GroupModel)
                {
                    cell = ((GroupModel) cell).getMainElement();
                }
                if (cell instanceof TransitionModel)
                {
                    editor.deleteCell(((TransitionModel) cell).getToolSpecific().getTrigger(), true);
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
                // TODO:
                /*
                 * EditorVC editor = m_controlledWindow.getActiveEditor();
                 * WoPeDJGraph graph = editor.getGraph(); Object cell =
                 * graph.getSelectionCell(); if (cell instanceof GroupModel) {
                 * cell = ((GroupModel) cell).getMainElement(); }
                 * 
                 * ((PlaceModel) cell).addToken();
                 */
                break;
            case AbstractViewEvent.REMOVE_TOKEN:
                // TODO:
                /*
                 * EditorVC editor = m_controlledWindow.getActiveEditor();
                 * WoPeDJGraph graph = editor.getGraph(); Object cell =
                 * graph.getSelectionCell(); if (cell instanceof GroupModel) {
                 * cell = ((GroupModel) cell).getMainElement(); }
                 * 
                 * ((PlaceModel) cell).removeToken();
                 */
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
            case AbstractViewEvent.STRECH:
                editor.scaleNet(2);
                break;
            default:
                break;
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
