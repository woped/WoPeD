/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package textModel;

import java.util.ArrayList;
import java.util.List;

import nodes.Cluster;
import nodes.ProcessEdge;
import nodes.ProcessNode;
import nodes.ProcessObject;
import models.ProcessModel;
import processing.ProcessUtils;
import etc.SentenceWordID;
import etc.TextToProcess;
import transform.AnalyzedSentence;
import transform.DummyAction;
import transform.TextAnalyzer;
import transform.TextModelBuilder;
import worldModel.Action;
import worldModel.Actor;
import worldModel.ExtractedObject;
import worldModel.Resource;
import worldModel.SpecifiedElement;
import worldModel.Specifier;
import worldModel.Specifier.SpecifierType;

public class TextModelControler extends ProcessUtils {

	
	private TextAnalyzer f_analyzer;
	private TextModel f_model;
	private TextModelBuilder f_builder;
	
	private ArrayList<ProcessNode> f_highlightCache = new ArrayList<ProcessNode>();
	private TextEdge f_edge;
	private TextToProcess f_parent;
	private TextToProcess f_processor;
	private boolean f_showRefs = true;
	private boolean f_showLinks = true;
	
	
	public void setTextToprocess(TextToProcess parent) {
		f_parent = parent;	
	}

	public void processObjectClicked(ProcessObject o) {
		if(o instanceof ProcessNode) {
			if(o instanceof WordNode) {
				SpecifiedElement _element = getElement((ProcessNode)o);
				if(_element != null) {
					highlightAll();
					if(f_parent != null) {
						f_parent.textElementClicked(_element);
					}
				}
			}else if(o instanceof SentenceNode) {
				SentenceNode _sn = (SentenceNode) o;
				highlightComponents(_sn);
			}			
		}
		
		
	}

	/**
	 * 
	 */
	private void highlightAll() {
		for(Actor ac:f_analyzer.getWorld().getActors()) {
			highlightElement(f_builder.getSentenceNode(ac.getOrigin()), ac,false);
		}
		for(Resource ac:f_analyzer.getWorld().getResources()) {
			highlightElement(f_builder.getSentenceNode(ac.getOrigin()), ac,false);
		}
		for(Action ac:f_analyzer.getWorld().getActions()) {
			if(!(ac instanceof DummyAction)) {
				highlightAction(f_builder.getSentenceNode(ac.getOrigin()), ac,false);
			}
		}
	}

	/**
	 * @param _sn
	 */
	private void highlightComponents(SentenceNode _sn) {
		AnalyzedSentence _sentence = f_analyzer.getAnalyzedSentence(_sn.getIndex());
		List<Action> _actions = _sentence.getExtractedActions();
		for(int i=0;i<_actions.size();i++) {
			Action _a =_actions.get(i);
			highlightAction(_sn, _a,true);
			highlightElement(_sn, _a.getActorFrom(),true);
			highlightElement(_sn, _a.getObject(),true);
		}
	}

	private void highlightElement(SentenceNode _sn, ExtractedObject a,boolean highlightDependants) {
		//highlighting Element
		if(a != null) {
			if(highlightDependants) {
				ProcessNode _pn = getElement(_sn,a.getDeterminer(),a.getWordIndex()-1);
				if(_pn != null) {
					f_highlightCache.add(_pn);
				}			
			}
			highlightSpecifiedElement(_sn, a,highlightDependants);
		}
	}
	
	public void highlightAction (Action a) {
		SentenceNode _sn = f_builder.getSentenceNode(a.getOrigin());
		highlightAction(_sn, a, true );
	}
	
	private void highlightAction(SentenceNode _sn, Action a,boolean highlightDependants) {
		//highlighting Element
		if(a != null) {
			if(highlightDependants) {
				ProcessNode _pn = getElement(_sn,a.getAux(),a.getWordIndex()-1);
				if(_pn != null) {
					f_highlightCache.add(_pn);
				}	
				_pn = getElement(_sn,a.getCop(),a.getWordIndex()-1);
				if(_pn != null) {
					f_highlightCache.add(_pn);
				}
				_pn = getElement(_sn,a.getPrt(),a.getWordIndex()-1);
				if(_pn != null) {
					f_highlightCache.add(_pn);
				}
				_pn = getElement(_sn,a.getMod(),a.getModPos());
				if(_pn != null) {
					f_highlightCache.add(_pn);
				}
				if(a.getXcomp() != null) {
					highlightAction(_sn, a.getXcomp(),highlightDependants);
				}
			}
			highlightSpecifiedElement(_sn, a,highlightDependants);
		}
	}
	

	private void highlightSpecifiedElement(SentenceNode _sn, SpecifiedElement a,boolean highlightDependants) {
		ProcessNode _pn;
		
		_pn = _sn.getProcessNodes().get(a.getWordIndex()-1);;
		f_highlightCache.add(_pn);			
		
		if(highlightDependants) {
			List<Specifier> _hglt = new ArrayList<Specifier>(a.getSpecifiers());
			_hglt.removeAll(a.getSpecifiers(SpecifierType.SBAR)); //do not highlight those
			for(Specifier sp:_hglt) {
				if(sp.getWordIndex() >= _sn.getProcessNodes().size()) {
					System.out.println("error");
				}
				for(String str:sp.getName().split(" ")) {
					_pn = getElement(_sn,str,sp.getWordIndex()-1);
					if(_pn == null) {
						System.err.println("error! Could not find node for: "+str);
					}else {
						f_highlightCache.add(_pn);
					}
				}
			}
		}
	}

	/**
	 * searches for an object with the given string in the given sentence
	 * index is a startPosition
	 * @param _sn
	 * @param determiner
	 * @param wordIndex
	 * @return
	 */
	private ProcessNode getElement(SentenceNode sn, String name,
			int index) {
		if(name != null) {

			name = name.replaceAll(",", "");
			name = name.replaceAll("\\(", "");
			name = name.replaceAll("\\)", "");
			name = name.replaceAll("\\$", "");
			name = name.replaceAll("\\/", "\\\\\\/");
			List<ProcessNode> _nodes = sn.getProcessNodes();
			for(int i=0;i<_nodes.size()-1;i++) {
				int idx = index-i;
				if(idx >= 0) {
					//backwards
					ProcessNode pn = sn.getProcessNodes().get(idx);
					if(pn.getText().equalsIgnoreCase(name)) {
						return pn;
					}
				}
				idx = index+i;
				if(idx < _nodes.size() && idx >= 0) {
					//forward
					ProcessNode pn = _nodes.get(idx);
					if(pn.getText().equalsIgnoreCase(name)) {
						return pn;
					}
				}
			}
		}
		return null;
	}

	/**
	 * @param o
	 * @return
	 */
	private SpecifiedElement getElement(ProcessNode o) {
		Cluster _cluster = f_model.getClusterForNode(o);
		if(_cluster instanceof SentenceNode) {
			SentenceNode _sn = (SentenceNode) _cluster;
			int index = _sn.getProcessNodes().indexOf(o)+1;
			AnalyzedSentence _origin = f_analyzer.getAnalyzedSentence(_sn.getIndex());
			for(Action ac:_origin.getExtractedActions()) {
				//checking the Action
				SpecifiedElement _result = checkContainmentOfIndex(index, ac);
				if(_result != null) {
					return _result;
				}
				//checking the xcomp
				_result = checkContainmentOfIndex(index, ac.getXcomp());
				if(_result != null) {
					return _result;
				}
				//checking actor
				_result = checkContainmentOfIndex(index, ac.getActorFrom());
				if(_result != null) {
					return _result;
				}
				//checking object
				_result = checkContainmentOfIndex(index, ac.getObject());
				if(_result != null) {
					return _result;
				}
			}
		}
		return null;
	}

	private SpecifiedElement checkContainmentOfIndex(int index, SpecifiedElement elem) {
		if(elem != null) {
			if(elem.getWordIndex() == index) {
				return elem;
			}
			for(Specifier sp:elem.getSpecifiers()) {
				if(sp.getType() != SpecifierType.SBAR) {
					if(sp.getWordIndex() == index) {
						if(sp.getObject() == null) {
							return elem;
						}else {
							return sp.getObject();
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * @param pgui 
	 * @param world
	 * @param _model
	 */
	public void setModels(TextToProcess processor,TextAnalyzer analyzer, TextModelBuilder builder, TextModel model) {
		f_analyzer = analyzer;
		f_builder = builder;
		f_model = model;
		f_model.setUtils(this);	
		f_processor = processor;
		setShowLinks(model, f_showLinks);
		setShowReferences(model, f_showRefs);
	}
	
	public void setShowReferences(ProcessModel model, boolean selected) {
    	f_showRefs = selected;
    }

	@Override
	public ProcessEdge createDefaultEdge(ProcessNode source, ProcessNode target) {
		SpecifiedElement _spec = getElement(source);
		if(_spec instanceof ExtractedObject) {
			ExtractedObject _obj = (ExtractedObject) _spec;
			if(_obj.needsResolve()) {
				_spec = getElement(target);
				if(_spec instanceof ExtractedObject) {
					f_edge = new TextEdge();
					f_edge.setSource(source);
					f_edge.setTarget(target);
					return f_edge;
				}
			}
		}
		return null;
	}

//	public void processEdgeAdded(ProcessEdge edge, boolean bpmn) {
//		addReferenceToTextAnalyzer(edge);		
//		//only possibility, an edge was added by our reference repointing
//		if(f_edge != null) {
//			for(ProcessEdge e:new ArrayList<ProcessEdge>(f_model.getEdges())) {
//				if(e != f_edge) {
//					if(e.getSource().equals(f_edge.getSource())) {
//						f_model.removeEdge(e);
//					}
//				}
//			}
//		}
//		f_processor.analyzeText(true, bpmn); //rebuild process model
//	}

	/**
	 * @param edge
	 */
	private void addReferenceToTextAnalyzer(ProcessEdge edge) {
		SpecifiedElement _from = getElement(edge.getSource());
		SpecifiedElement _to = getElement(edge.getTarget());
		f_processor.addManualReferenceResolution(new SentenceWordID(_from),new SentenceWordID(_to));
	}
	
	public void setShowLinks(ProcessModel model, boolean selected) {
		f_showLinks = selected;
    }


}
