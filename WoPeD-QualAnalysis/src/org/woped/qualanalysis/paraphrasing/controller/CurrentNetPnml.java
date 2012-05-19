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

        Set<AbstractPetriNetElementModel> connectedTransitions = new HashSet<AbstractPetriNetElementModel>();  
        Iterator<String> arcIter = elementContainer.getArcMap().keySet().iterator();
        while (arcIter.hasNext())
        {
            ArcModel currentArc = elementContainer.getArcById(arcIter.next());
            AbstractPetriNetElementModel currentTargetModel = (AbstractPetriNetElementModel) elementContainer.getElementById(currentArc.getTargetId());
            AbstractPetriNetElementModel currentSourceModel = (AbstractPetriNetElementModel) elementContainer.getElementById(currentArc.getSourceId());
            if (currentTargetModel.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE)
            	connectedTransitions.add(currentTargetModel);
            else if (currentSourceModel.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE)
            	connectedTransitions.add(currentSourceModel);
            else
            {
                initArc(iNet.addNewArc(), currentArc, null);
            }

        }

        Iterator<AbstractPetriNetElementModel> currentTransition = connectedTransitions.iterator();
        while (currentTransition.hasNext())
        {
        	OperatorTransitionModel currentConnectedModel = (OperatorTransitionModel)currentTransition.next();
        	Iterator<String> innerArcIter = currentConnectedModel.getSimpleTransContainer().getArcMap().keySet().iterator();
        	while (innerArcIter.hasNext())
        	{
        		ArcModel currentInnerArc = (ArcModel) currentConnectedModel.getSimpleTransContainer().getArcMap().get(innerArcIter.next());
        		ArcModel currentOuterArc = null;
        		if (elementContainer.getElementById(currentInnerArc.getSourceId())!=null)
        		{
        			currentOuterArc = elementContainer.findArc(currentInnerArc.getSourceId(),
        					currentConnectedModel.getId()); 
        		}
        		if (elementContainer.getElementById(currentInnerArc.getTargetId())!=null)
        		{
        			currentOuterArc = elementContainer.findArc(currentConnectedModel.getId(), 
        					currentInnerArc.getTargetId()); 
        		}
        		initArc(iNet.addNewArc(), (currentOuterArc!=null)?currentOuterArc:currentInnerArc, 
        				currentInnerArc);
        	}
        }	
	}

	private PlaceType initPlace(PlaceType iPlace, PlaceModel currentModel)
    {
        // Name
        initNodeName(iPlace.addNewName(), currentModel.getNameModel());       
        // attr. id
        iPlace.setId(currentModel.getId());
        //LoggerManager.debug(Constants.FILE_LOGGER, "   ... Place (ID:" + currentModel.getId() + ") set");

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
	
	

    private ArcType initArc(ArcType iArc, ArcModel outerArc, ArcModel innerArc)
    {
        ArcModel useArc = innerArc == null ? outerArc : innerArc;
        // inscription
        initNodeName(iArc.addNewInscription(), useArc);
        // attr. id
        iArc.setId(outerArc.getId());
        // attr. source
        iArc.setSource(useArc.getSourceId());
        // attr. target
        iArc.setTarget(useArc.getTargetId());

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
    
    
//    public static String[][] getDescription(String xmlString){
//    	String[][] result = null;
//    	try{
//        	
//        	PnmlDocument pnmlDoc = PnmlDocument.Factory.parse(xmlString); 	
//        	
//        	PnmlType pnmlTag = pnmlDoc.getPnml();
//        	        	
//        	if(pnmlTag.getNetArray().length > 0){
//        		NetType netTag = pnmlTag.getNetArray(0);
//            	
//            	if(netTag.isSetText()){
//            		TextType textTag = netTag.getText();
//            		
//                	if(textTag.getPhraseArray().length > 0){
//                		PhraseType[] phraseTag = textTag.getPhraseArray();
//                    	
//                    	result = new String[phraseTag.length][2];        	
//                    	for(int i = 0; i < phraseTag.length; i++){
//                    		result[i][0] = phraseTag[i].getIds().trim();
//                    		result[i][1] = phraseTag[i].getStringValue().trim();	
//                    	}
//                	}                	
//            	}
//        	}
//        	
//    	}
//    	catch(Exception ex){
//    		JOptionPane.showMessageDialog(null, Messages.getString("Paraphrasing.CurrentNetPnml.Parsing.Error.Message"),
//					Messages.getString("Paraphrasing.CurrentNetPnml.Parsing.Error.Title"), JOptionPane.INFORMATION_MESSAGE);
//    	}
//    	
//    	return result;
//    	
//    }
	
}
