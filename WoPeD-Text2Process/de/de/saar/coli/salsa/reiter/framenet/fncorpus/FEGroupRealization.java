package de.saar.coli.salsa.reiter.framenet.fncorpus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;
import org.jaxen.JaxenException;

import de.saar.coli.salsa.reiter.framenet.FrameElementNotFoundException;
import de.saar.coli.salsa.reiter.framenet.LexicalUnit;

/**
 * @author Fabian Friedrich
 *
 */
public class FEGroupRealization {
	
	private int totalCount;
	private List<ValencePattern> patterns = new ArrayList<ValencePattern>();
	private LexicalUnit lexicalUnit;
	
	/**
	 * @throws JaxenException 
	 * @throws FrameElementNotFoundException 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public FEGroupRealization(Element element, LexicalUnit lu){
		this.lexicalUnit = lu;
		totalCount = Integer.parseInt(element.attributeValue("total"));
		Iterator pat_iter = element.elementIterator("pattern");
		while(pat_iter.hasNext()) {
			Element patternEle = (Element) pat_iter.next();
			patterns.add(new ValencePattern(patternEle,this.lexicalUnit));
		}
	}	
		
	@SuppressWarnings("unchecked")
	public List<ValenceUnit> getAllValenceUnits(){
		List<ValenceUnit> result = new ArrayList<ValenceUnit>();
		Iterator ptr_iter = patterns.iterator();
		while(ptr_iter.hasNext()) {
			result.addAll(((ValencePattern) ptr_iter.next()).getUnits());
		}
		return result;
	}

	/**
	 * @return the totalCount
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * @return the patterns
	 */
	public List<ValencePattern> getPatterns() {
		return patterns;
	}
}
