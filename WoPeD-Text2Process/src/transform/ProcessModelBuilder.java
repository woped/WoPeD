/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package transform;


import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nodes.ProcessNode;
import models.ProcessModel;
import bpmn.DataObject;
import bpmn.SequenceFlow;
import processing.ProcessingUtils;
import processing.WordNetWrapper;



//import com.inubit.research.layouter.gridLayouter.GridLayouter;

import worldModel.Action;
import worldModel.Actor;
import worldModel.ExtractedObject;
import worldModel.Resource;
import worldModel.SpecifiedElement;
import worldModel.Specifier;
import worldModel.WorldModel;
import worldModel.Specifier.SpecifierType;

public abstract class ProcessModelBuilder {
	
/**
 * @param os
 * @param _a
 * @param _b
 * @param s
 */

protected void put(HashMap<Action, List<String>> os, Action a, String dataObj) {
	if(!os.containsKey(a)) {
		LinkedList<String> _list = new LinkedList<String>();
		os.put(a, _list);
	}
	os.get(a).add(dataObj);
}

/**
 * @param _a
 * @return
 */
protected Set<String> getDataObjectCandidates(SpecifiedElement ob) {
	if(ob == null) {
		return new HashSet<String>(0);
	}		
	HashSet<String> _result = new HashSet<String>();
	if(ob instanceof Resource) {
		if(((Resource) ob).needsResolve()) {
			_result.addAll(getDataObjectCandidates(((ExtractedObject)ob).getReference()));
			return _result;
		}else {
			String _name = getName((ExtractedObject)ob,false, 1, true);
			if(WordNetWrapper.canBeDataObject(_name,ob.getName())) {
				_result.add(_name);		
				return _result;
			}
		}
	}else if(ob instanceof Actor) {
		Actor _actor = (Actor) ob;
		if(_actor.isUnreal()) {
			String _name = getName(_actor,false, 1, true);
			if(WordNetWrapper.canBeDataObject(_name,_actor.getName())) {
				_result.add(_name);
				return _result;
			}
		}else if(_actor.needsResolve()) {
			_result.addAll(getDataObjectCandidates(_actor.getReference()));
			return _result;
		}
	}else if(ob instanceof Action) {
		Action _action = (Action) ob;
		_result.addAll(getDataObjectCandidates(_action.getActorFrom()));
		_result.addAll(getDataObjectCandidates(_action.getObject()));
		_result.addAll(getDataObjectCandidates(_action.getXcomp()));
	}
	//checking specifiers
	for(Specifier spec:ob.getSpecifiers()) {
		if(spec.getObject() != null && !"of".equals(spec.getHeadWord())) {
			_result.addAll(getDataObjectCandidates(spec.getObject()));
		}
	}
	return _result;
}


public abstract Map<ProcessNode, String> getCommLinks();


/**
 * @param specifiers
 * @return
 */
protected Specifier containedReceiver(List<Specifier> specifiers) {
	return containsFrameElement(specifiers,ListUtils.getList("Donor","Source"));
}

/**
 * @param specifiers
 * @return
 */
protected Specifier containedSender(List<Specifier> specifiers) {
	return containsFrameElement(specifiers,ListUtils.getList("Addressee","Recipient"));
}

/**
 * @param specifiers
 * @param list
 * @return
 */
private Specifier containsFrameElement(List<Specifier> specifiers,
		List<String> list) {
	for(Specifier sp:specifiers) {
		if(sp.getFrameElement() != null) {
			if(list.contains(sp.getFrameElement().getName())) {
				return sp;
			}
		}
	}
	return null;
	
}

/**
 * @param world 
 * 
 */
protected abstract void processMetaActivities(WorldModel world);

/**
 * @param world 
 * 
 */
protected abstract void removeDummies(WorldModel world);

/**
 * @param a
 * @return
 */
protected String getEventText(Action a) {
	StringBuilder _b = new StringBuilder();
	boolean _actorPlural = false;
	if(a.getActorFrom() != null) {
		_b.append(getName(a.getActorFrom(),true));
		_b.append(' ');
		_actorPlural = a.getActorFrom().getName().endsWith("s");
	}	
	if(!WordNetWrapper.isWeakVerb(a.getName()) || (a.getCop() != null || 
			a.getObject() != null || a.getSpecifiers().size()>0 || a.isNegated())) {
		boolean _auxIsDo = (a.getAux() != null && WordNetWrapper.getBaseForm(a.getAux()).equals("do"));
		if(a.isNegated() && (!WordNetWrapper.isWeakVerb(a.getName())||_auxIsDo)) {
			if(a.getAux() != null && !WordNetWrapper.getBaseForm(a.getAux()).equals("be")) {
				_b.append(a.getAux());
			}else {
				_b.append(_actorPlural ? "do" : ProcessingUtils.get3rdPsing("do"));	
			}
			_b.append(" not ");
			_b.append(WordNetWrapper.getBaseForm(a.getName()));
			_b.append(' ');
		}else {
			if(a.getAux() != null) {
				if(a.getActorFrom() != null && !a.getActorFrom().getPassive()) {
					_b.append(a.getAux());
					_b.append(' ');
					_b.append(a.getName());
				}else{
					_b.append(ProcessingUtils.get3rdPsing(a.getName()));
				}
				
			}else {
				_b.append(_actorPlural ? WordNetWrapper.getBaseForm(a.getName()) : ProcessingUtils.get3rdPsing(a.getName()));
			}		
			if(a.isNegated()) {
				_b.append(" not ");
			}
		}
		_b.append(' ');
	}
	
	
	if(a.getCop() != null) {
		_b.append(a.getCop());
	}else {
		if(a.getObject() != null) {
			_b.append(getName(a.getObject(),true));
		}else {
			if(a.getSpecifiers().size() > 0) {
				_b.append(a.getSpecifiers().get(0).getPhrase());
			}
		}
	}
	return _b.toString();
}

/**
 * 
 */
protected abstract void eventsToLabels();


/**
 * @param actorFrom
 * @return
 */
protected boolean hasHiddenAction(ExtractedObject obj) {
	boolean _canBeGerund = false;
	for(Specifier spec:obj.getSpecifiers(SpecifierType.PP)) {
		if(spec.getName().startsWith("of")) {
			_canBeGerund = true;
		}
	}
	if(!_canBeGerund) {
		return false;
	}
	if(obj != null) {
		for(String s:obj.getName().split(" ")) {
			if(WordNetWrapper.deriveVerb(s) != null) {
				return true;
			}
		}
	}
	return false;
}




/**
 * @param spec
 * @return
 */
protected abstract boolean considerPhrase(Specifier spec);



protected abstract String getName(ExtractedObject a,boolean addDet);


protected void addSpecifier(int level, StringBuilder _b, Specifier s,boolean compact) {
	_b.append(' ');
	if(s.getObject() != null) {
		_b.append(s.getHeadWord());
		_b.append(' ');
		_b.append(getName(s.getObject(),true,level+1,compact));
			
	}else {
		_b.append(s.getName());
	}
}


public abstract void layoutModel(ProcessModel _result);


public abstract ProcessModel createProcessModel(WorldModel world);

public abstract void buildDataObjects(WorldModel world);

public abstract String getName(ExtractedObject a,boolean addDet,int level,boolean compact);

/**
 * @param a
 * @return
 */
protected boolean canBeTransformed(Action a) {
	if(a.getObject() != null && !ProcessingUtils.isUnrealActor(a.getObject()) 
			&& !a.getObject().needsResolve() && hasHiddenAction(a.getObject())) {
		return true;
	}	
	if(a.getActorFrom() != null && ProcessingUtils.isUnrealActor(a.getActorFrom()) 
			&& hasHiddenAction(a.getActorFrom())) {
		return true;
	}
	return false;
}

/**
 * @param actorFrom
 * @return
 */
protected String transformToAction(ExtractedObject obj) {
	StringBuilder _b = new StringBuilder();
	for(String s:obj.getName().split(" ")) {
		String _der = WordNetWrapper.deriveVerb(s);
		if(_der != null) {
			_b.append(_der);
			break;
		}
	}
	for(Specifier spec:obj.getSpecifiers(SpecifierType.PP)) {
		if(spec.getPhrase().startsWith("of") && spec.getObject() != null) {
			_b.append(' ');
			_b.append(getName(spec.getObject(),true));
		}
	}
	return _b.toString();
}

/**
 * @param a
 * @param _b
 */
protected void addMod(Action a, StringBuilder _b) {
	if(a.getMod() != null){
		_b.append(' ');
		_b.append(a.getMod());
	}
}

protected abstract void addXComp(Action a, StringBuilder _b,boolean needsAux);

protected void addSpecifiers(Action a, StringBuilder _b, int limit,boolean smaller) {
	if(a.getObject() == null) {
		List<Specifier> _specs = a.getSpecifiers(SpecifierType.PP);
		if(a.getXcomp() == null) {
			_specs.addAll(a.getSpecifiers(SpecifierType.SBAR));
		}
		Collections.sort(_specs);			
		boolean _foundSth = false;
		for(Specifier spec:_specs) {
			if(spec.getType() == SpecifierType.SBAR && _foundSth == true) {
				break;
			}
			if(spec.getWordIndex() > a.getWordIndex()) {
				boolean _smaller = spec.getWordIndex() < limit;
				if(!(_smaller^smaller)) {
					if(considerPhrase(spec)){
						_foundSth = true;
						_b.append(' ');
						if(spec.getObject() != null) {
							_b.append(spec.getHeadWord());
							_b.append(' ');
							_b.append(getName(spec.getObject(),true));
						}else {
							_b.append(spec.getName());
						}
					}
				}
			}
		}
	}
}

protected int getXCompPos(Action xcomp) {
	if(xcomp == null) {
		return -1;
	}
	return xcomp.getWordIndex();
}

}
