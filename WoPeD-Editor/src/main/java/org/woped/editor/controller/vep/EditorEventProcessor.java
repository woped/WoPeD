package org.woped.editor.controller.vep;

import java.awt.MouseInfo;
import java.awt.Point;
import java.util.Iterator;

import javax.swing.JFrame;

import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.AbstractEventProcessor;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.model.ArcModel;
import org.woped.core.model.CreationMap;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.GroupModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.model.petrinet.TriggerModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.Constants;
import org.woped.editor.controller.ArcPropertyEditor;
import org.woped.editor.controller.PlacePropertyEditor;
import org.woped.editor.controller.TransitionPropertyEditor;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.qualanalysis.simulation.TokenGameBarListener;
import org.woped.qualanalysis.simulation.TokenGameSession;

public class EditorEventProcessor extends AbstractEventProcessor {
    public EditorEventProcessor(AbstractApplicationMediator mediator) {
        super(mediator);
    }

    public void processViewEvent(AbstractViewEvent event) {
        EditorVC editor;
        if ( event.getSource() instanceof EditorVC ) {
            editor = (EditorVC) event.getSource();
        } else {
            editor = (EditorVC) getMediator().getUi().getEditorFocus();
        }
        Object cell;
        ArcModel anArc;
        Iterator<String> anIter;
        if ( editor != null ) {
            CreationMap map = CreationMap.createMap();
            if ( editor.getLastMousePosition() != null ) {
                map.setPosition((int) (editor.getLastMousePosition().getX() / editor.getGraph().getScale()), (int) (editor.getLastMousePosition().getY() / editor.getGraph().getScale()));
            }

            switch (event.getOrder()) {
                // Petrinet
                case AbstractViewEvent.ADD_PLACE:
                    map.setType(AbstractPetriNetElementModel.PLACE_TYPE);
                    editor.create(map);
                    break;
                case AbstractViewEvent.ADD_TRANSITION:
                    map.setType(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
                    editor.create(map);
                    break;
                case AbstractViewEvent.ADD_ANDJOIN:
                    map.setType(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE);
                    map.setOperatorType(OperatorTransitionModel.AND_JOIN_TYPE);
                    editor.create(map);
                    break;
                case AbstractViewEvent.ADD_ANDSPLIT:
                    map.setType(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE);
                    map.setOperatorType(OperatorTransitionModel.AND_SPLIT_TYPE);
                    editor.create(map);
                    break;
                case AbstractViewEvent.ADD_ANDSPLITJOIN:
                    map.setType(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE);
                    map.setOperatorType(OperatorTransitionModel.AND_SPLITJOIN_TYPE);
                    editor.create(map);
                    break;
                case AbstractViewEvent.ADD_XORJOIN:
                    map.setType(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE);
                    map.setOperatorType(OperatorTransitionModel.XOR_JOIN_TYPE);
                    editor.create(map);
                    break;
                case AbstractViewEvent.ADD_XORSPLIT:
                    map.setType(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE);
                    map.setOperatorType(OperatorTransitionModel.XOR_SPLIT_TYPE);
                    editor.create(map);
                    break;
                case AbstractViewEvent.ADD_XORSPLITJOIN:
                    map.setType(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE);
                    map.setOperatorType(OperatorTransitionModel.XORJOIN_XORSPLIT_TYPE);
                    editor.create(map);
                    break;
                case AbstractViewEvent.ADD_ANDJOINXORSPLIT:
                    map.setType(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE);
                    map.setOperatorType(OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE);
                    editor.create(map);
                    break;
                case AbstractViewEvent.ADD_XORJOINANDSPLIT:
                    map.setType(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE);
                    map.setOperatorType(OperatorTransitionModel.XORJOIN_ANDSPLIT_TYPE);
                    editor.create(map);
                    break;
                // General
                case AbstractViewEvent.RENAME:
                    cell = editor.getGraph().getSelectionCell();
                    if ( cell instanceof TriggerModel ) {
                        cell = ((TriggerModel) cell).getParent();
                    }
                    if ( cell instanceof GroupModel ) {
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
                    editor.paste();
                    break;
                case AbstractViewEvent.PASTE_AT:
                    editor.pasteAtMousePosition();
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
                    AbstractPetriNetElementModel element = null;

                    if ( cell instanceof ArcModel ) {
                        new ArcPropertyEditor((JFrame) getMediator().getUi(), (Point) MouseInfo.getPointerInfo().getLocation(), (ArcModel) cell, editor);
                    }

                    if ( cell instanceof TriggerModel ) {
                        cell = ((TriggerModel) cell).getParent();
                    }
                    if ( cell instanceof GroupModel ) {
                        element = ((GroupModel) cell).getMainElement();
                    } else if ( cell instanceof AbstractPetriNetElementModel ) {
                        element = (AbstractPetriNetElementModel) cell;
                    }
                    if ( element != null ) {
                        if ( element instanceof TransitionModel ) {
                            new TransitionPropertyEditor((JFrame) getMediator().getUi(), (Point) editor.getLastMousePosition(), (TransitionModel) element, editor);

                        }
                        if ( element instanceof PlaceModel ) {
                            new PlacePropertyEditor((JFrame) getMediator().getUi(), (Point) editor.getLastMousePosition(), (PlaceModel) element, editor);
                        }
                    }
                    break;
                case AbstractViewEvent.ADD_POINT:
                    editor.addPointToSelectedArc();
                    break;
                case AbstractViewEvent.REMOVE_POINT:
                    editor.removeSelectedPoint();
                    break;
                case AbstractViewEvent.INCREASE_ARC_WEIGHT:
                    editor.increaseWeightOfSelectedArc();
                    break;
                case AbstractViewEvent.DECREASE_ARC_WEIGHT:
                    editor.decreaseWeightOfSelectedArc();
                    break;
                case AbstractViewEvent.ADD_EXT_TRIGGER:
                    cell = editor.getGraph().getSelectionCell();
                    removeResources(editor, cell);
                    editor.createTrigger(getCreateTriggerMap(editor.getGraph().getSelectionCell(), TriggerModel.TRIGGER_MESSAGE));
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
                    if ( cell instanceof GroupModel ) {
                        cell = ((GroupModel) cell).getMainElement();
                    }
                    if ( cell instanceof TransitionModel ) {
                        editor.deleteCell(((TransitionModel) cell).getToolSpecific().getTrigger(), true);
                    }
                    break;
                case AbstractViewEvent.ADD_SUBPROCESS:
                    editor.createElement(AbstractPetriNetElementModel.SUBP_TYPE, -1, editor.getLastMousePosition(), false);
                    break;
                case AbstractViewEvent.ROUTING_ACTIVE:
                    cell = editor.getGraph().getSelectionCell();
                    ((ArcModel) cell).setRoute(true);
                    editor.getGraph().connect(((ArcModel) cell), true);
                    break;
                case AbstractViewEvent.ROUTING_DEACTIVE:
                    cell = editor.getGraph().getSelectionCell();
                    ((ArcModel) cell).setRoute(false);
                    editor.getGraph().connect(((ArcModel) cell), true);
                    break;
                case AbstractViewEvent.ROUTING_ALL_ACTIVE:
                    anIter = editor.getModelProcessor().getElementContainer().getArcMap().keySet().iterator();

                    while ( anIter.hasNext() ) {
                        anArc = editor.getModelProcessor().getElementContainer().getArcById(anIter.next());
                        anArc.setRoute(true);
                        editor.getGraph().connect(anArc, true);
                    }
                    editor.updateNet();
                    break;
                case AbstractViewEvent.ROUTING_ALL_DEACTIVE:
                    anIter = editor.getModelProcessor().getElementContainer().getArcMap().keySet().iterator();
                    while ( anIter.hasNext() ) {
                        anArc = editor.getModelProcessor().getElementContainer().getArcById(anIter.next());
                        anArc.setRoute(false);
                        editor.getGraph().connect(anArc, true);
                    }
                    editor.updateNet();
                    break;
                case AbstractViewEvent.ADD_TOKEN:
                    if ( (cell = editor.getGraph().getSelectionCell()) instanceof GroupModel ) {
                        cell = ((GroupModel) cell).getMainElement();
                    }
                    if ( cell instanceof PlaceModel ) {
                        ((PlaceModel) cell).addToken();
                    }
                    editor.updateNet();
                    editor.getEditorPanel().autoRefreshAnalysisBar();
                    editor.setSaved(false);
                    break;
                case AbstractViewEvent.REMOVE_TOKEN:
                    if ( (cell = editor.getGraph().getSelectionCell()) instanceof GroupModel ) {
                        cell = ((GroupModel) cell).getMainElement();
                    }
                    if ( cell instanceof PlaceModel ) {
                        ((PlaceModel) cell).removeToken();
                    }
                    editor.updateNet();
                    editor.getEditorPanel().autoRefreshAnalysisBar();
                    editor.setSaved(false);
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
                case AbstractViewEvent.COLORING:
                    editor.toggleUnderstandColoring();
                    break;
                case AbstractViewEvent.OPEN_TOKENGAME:
                    editor.toggleTokenGame();
                    break;
                case AbstractViewEvent.CLOSE_TOKENGAME:
                    editor.terminateTokenGameSession();
                    break;
                case AbstractViewEvent.SHOWSIDEBAR:
                    // Toggle visibility of side tree view
                    editor.getEditorPanel().setSideTreeViewVisible(!editor.getEditorPanel().isSideTreeViewVisible());
                    break;
                case AbstractViewEvent.SHOWOVERVIEW:
                    // visbility of overview panel
                    editor.getEditorPanel().setOverviewPanelVisible(!editor.getEditorPanel().isOverviewPanelVisible());
                    break;
                case AbstractViewEvent.SHOWTREEVIEW:
                    // visibility of elements tree panel
                    editor.getEditorPanel().setTreeviewPanelVisible(!editor.getEditorPanel().isTreeviewPanelVisible());
                    break;
                case AbstractViewEvent.PRESS:
                    editor.scaleNet(0.5);
                    break;
                case AbstractViewEvent.STRETCH:
                    editor.scaleNet(2);
                    break;
                case AbstractViewEvent.ROTATEVIEW:
                    editor.getEditorPanel().rotateLayout(editor);
                    break;
                case AbstractViewEvent.ROTATETRANSLEFT:
                    cell = editor.getGraph().getSelectionCell();
                    if ( cell instanceof TriggerModel ) {
                        cell = ((TriggerModel) cell).getParent();
                    }
                    if ( cell instanceof GroupModel ) {
                        cell = ((GroupModel) cell).getMainElement();
                    }
                    //TODO auf die Methoden von Orientation zugreifen, nicht von EditorVC
                    editor.rotateTransLeft(cell);
                    break;
                case AbstractViewEvent.ROTATETRANSRIGHT:
                    cell = editor.getGraph().getSelectionCell();
                    if ( cell instanceof TriggerModel ) {
                        cell = ((TriggerModel) cell).getParent();
                    }
                    if ( cell instanceof GroupModel ) {
                        cell = ((GroupModel) cell).getMainElement();
                    }
                    editor.rotateTransRight(cell);
                    break;
                case AbstractViewEvent.GRAPHBEAUTIFIER:
                    editor.startBeautify(0, 0, 0);
                    break;
                case AbstractViewEvent.GRAPHBEAUTIFIER_ADV:
                    editor.advancedBeautifyDialog();
                    break;

                case AbstractViewEvent.TOKENGAME_BACKWARD:
                case AbstractViewEvent.TOKENGAME_JUMPINTO:
                case AbstractViewEvent.TOKENGAME_FORWARD:
                case AbstractViewEvent.TOKENGAME_PAUSE:
                case AbstractViewEvent.TOKENGAME_START:
                case AbstractViewEvent.TOKENGAME_STOP:
                case AbstractViewEvent.TOKENGAME_LEAVE:
                case AbstractViewEvent.TOKENGAME_STEP:
                case AbstractViewEvent.TOKENGAME_AUTO:
                    TokenGameSession tgbController = editor.getTokenGameController().getRemoteControl();
                    if ( tgbController != null ) {
                        // Adaptor from view event produced by toolbar button to the legacy
                        // action id of the token game bar controller. Generate "listener" for these
                        // and forward to the token game bar controller
                        int tgbAction = createTgbActionFromViewEvent(event.getOrder());
                        TokenGameBarListener listener = new TokenGameBarListener(tgbAction, tgbController);
                        listener.actionPerformed(null);
                    } else LoggerManager.error(Constants.EDITOR_LOGGER, "Tokengame controls used while tokengame was inactive");
                    break;
                //TODO(P2T):
                case AbstractViewEvent.P2T:
                    if ( !editor.getEditorPanel().isP2TBarVisible() ) {
                        editor.getEditorPanel().showP2TBar();
                    } else {
                        editor.getEditorPanel().hideP2TBar();
                    }
                	break;
    			
                default:
                    break;
            }
        }
    }

    private int createTgbActionFromViewEvent(int order) {
        int result = 0;
        switch (order) {
            case AbstractViewEvent.TOKENGAME_BACKWARD:
                result = TokenGameBarListener.CLICK_BACKWARD;
                break;
            case AbstractViewEvent.TOKENGAME_JUMPINTO:
                result = TokenGameBarListener.CLICK_STEP_DOWN;
                break;
            case AbstractViewEvent.TOKENGAME_FORWARD:
                result = TokenGameBarListener.CLICK_FORWARD;
                break;
            case AbstractViewEvent.TOKENGAME_PAUSE:
                result = TokenGameBarListener.CLICK_PAUSE;
                break;
            case AbstractViewEvent.TOKENGAME_START:
                result = TokenGameBarListener.CLICK_PLAY;
                break;
            case AbstractViewEvent.TOKENGAME_STOP:
                result = TokenGameBarListener.CLICK_STOP;
                break;
            case AbstractViewEvent.TOKENGAME_LEAVE:
                result = TokenGameBarListener.CLICK_STEP_UP;
                break;
            case AbstractViewEvent.TOKENGAME_STEP:
                result = TokenGameBarListener.CHOOSE_STEPWISE;
                break;
            case AbstractViewEvent.TOKENGAME_AUTO:
                result = TokenGameBarListener.CHOOSE_PLAYBACK;
                break;
            default:
                LoggerManager.error(Constants.EDITOR_LOGGER, "Unknown view event type passed for conversion to token game action");
                break;
        }
        return result;
    }

    private void removeResources(EditorVC editor, Object cell) {
        if ( cell instanceof GroupModel ) {
            cell = ((GroupModel) cell).getMainElement();
        }
        if ( cell instanceof TransitionModel ) {
            TransitionModel trans = (TransitionModel) cell;
            if ( trans.hasResource() ) {
                editor.deleteCell(trans.getToolSpecific().getTransResource(), true);
            }
        }
    }

    private CreationMap getCreateTriggerMap(Object cell, int triggertype) {
        if ( cell != null ) {
            CreationMap map = null;
            if ( cell instanceof TriggerModel ) {
                cell = ((TriggerModel) cell).getParent();
            }
            if ( cell instanceof GroupModel ) {
                cell = ((GroupModel) cell).getMainElement();
            }
            if ( cell instanceof TransitionModel ) {
                map = ((TransitionModel) cell).getCreationMap();
            }
            if ( map != null ) {
                map.setTriggerType(triggertype);
            }
            return map;
        }
        return null;
    }
}