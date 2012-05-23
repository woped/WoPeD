package org.woped.qualanalysis.paraphrasing.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
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

	private PnmlDocument pnmlDoc    = null;
	
	private String pnmlString = null;

	private IEditor editor = null;
	
	public CurrentNetPnml(){       
	}
	
	public CurrentNetPnml(IEditor editor){
		this.editor = editor;        
	}
	
	/**
	 * Reads all objects from the editor and creates
	 * a string with all necessary informations
	 * in the pnml format. Required for webservice.
	 * 
	 * @author Martin Meitz
	 * 
	 */
	public void setPnmlString(){
		ModelElementContainer elementContainer = editor.getModelProcessor().getElementContainer();
        PetriNetModelProcessor petrinetModel = (PetriNetModelProcessor) editor.getModelProcessor();
        pnmlDoc = PnmlDocument.Factory.newInstance();
        PnmlType iPnml = pnmlDoc.addNewPnml();
        NetType iNet = iPnml.addNewNet();
        iNet.setType(petrinetModel.getType());
        iNet.setId(petrinetModel.getId());
              
        
        try
        {
        	saveModelElementContainer(iNet, elementContainer);
            
            XmlOptions opt = new XmlOptions();
            opt.setUseDefaultNamespace();
            opt.setSavePrettyPrint();
            opt.setSavePrettyPrintIndent(2);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            pnmlDoc.save(baos, opt);
            
            this.pnmlString = baos.toString();
            
        }catch (IOException e)
        {
            LoggerManager.error(Constants.PARAPHRASING_LOGGER, "Could not write to the Stream. " + e.getMessage());

        }
	}
	
	private void saveModelElementContainer(NetType iNet, ModelElementContainer elementContainer){
	
		Iterator<AbstractPetriNetElementModel> root2Iter = elementContainer.getRootElements().iterator();
        while (root2Iter.hasNext())
        {
            AbstractPetriNetElementModel currentModel = (AbstractPetriNetElementModel) root2Iter.next();
            
            
            /* ##### PLACES ##### */
            if (currentModel.getType() == AbstractPetriNetElementModel.PLACE_TYPE)
            {
                initPlace(iNet.addNewPlace(), (PlaceModel) currentModel);
            } else if (currentModel.getType() == AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE)
            /* ##### NORMAL TRANSITION ##### */
            {
                initTransition(iNet.addNewTransition(), (TransitionModel) currentModel, null);

            }else if (currentModel.getType() == AbstractPetriNetElementModel.SUBP_TYPE || 
            		currentModel.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE)
            	/* ##### SUB TRANSITION ##### */
            {
                initTransition(iNet.addNewTransition(), (TransitionModel) currentModel, null);
            }
         
            
        }
        /* ##### ARCS ##### */
        
        Iterator<String> arcIter = elementContainer.getArcMap().keySet().iterator();
        
        while (arcIter.hasNext())
        {
            ArcModel currentArc = elementContainer.getArcById(arcIter.next());

    		initArc(iNet.addNewArc(), currentArc);

        }
        

        
	}

	private PlaceType initPlace(PlaceType iPlace, PlaceModel currentModel)
    {
        // Name
        initNodeName(iPlace.addNewName(), currentModel.getNameModel());       
        // attr. id
        iPlace.setId(currentModel.getId());

        return iPlace;
    }
	
	
	private NodeNameType initNodeName(NodeNameType nodeName, NameModel element)
    {
        // name
        nodeName.setText(element.getNameValue());

        return nodeName;
    }
	
	
	private TransitionType initTransition(TransitionType iTransition, TransitionModel currentModel, OperatorTransitionModel operatorModel)
    {
        TransitionModel takenModel = operatorModel == null ? currentModel : operatorModel;
        // name
        initNodeName(iTransition.addNewName(), takenModel.getNameModel());
        // attr. id
        iTransition.setId(currentModel.getId());

        return iTransition;
    }
	

	private ArcType initArc(ArcType iArc, ArcModel outerArc)
    {
		ArcModel useArc = outerArc;

		// inscription
        initNodeName(iArc.addNewInscription(), useArc);
        
		// attr. id
        iArc.setId(useArc.getId());

        // attr. source
        
        iArc.setSource(useArc.getSourceId());
        
        String sourceId = useArc.getSourceId();   
        if (sourceId.indexOf(
				OperatorTransitionModel.INNERID_SEPERATOR) != 0) {
        	sourceId = sourceId.split(OperatorTransitionModel.INNERID_SEPERATOR)[0];
		} 	
        iArc.setSource(sourceId);
        
        // attr. target
        String targetId = useArc.getTargetId();
        if (targetId.indexOf(
				OperatorTransitionModel.INNERID_SEPERATOR) != 0) {
        	targetId = targetId.split(OperatorTransitionModel.INNERID_SEPERATOR)[0];
		}
        iArc.setTarget(targetId);
        return iArc;
    }
    
    
    private ArcNameType initNodeName(ArcNameType nodeName, ArcModel element)
    {
        // name
        nodeName.setText(element.getInscriptionValue());
        return nodeName;
    }
	
    
	/**
	 * Returns the current net in the pnml format
	 * as a string
	 * 
	 * @author Martin Meitz
	 * 
	 */
    public String getPnmlString(){
    	return this.pnmlString;
    }
   
	
}
