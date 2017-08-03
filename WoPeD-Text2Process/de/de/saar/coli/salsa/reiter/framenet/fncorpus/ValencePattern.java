package de.saar.coli.salsa.reiter.framenet.fncorpus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

import de.saar.coli.salsa.reiter.framenet.LexicalUnit;

/**
 * @author Fabian Friedrich
 *
 */
public class ValencePattern {

	private int totalCount;
	private List<ValenceUnit> units = new ArrayList<ValenceUnit>();
	private List<Integer> annotSetIDS = new ArrayList<Integer>();
	private LexicalUnit lexicalUnit;
	
	
	@SuppressWarnings("unchecked")
	public ValencePattern(Element element, LexicalUnit lu){
		this.lexicalUnit = lu;
		totalCount = Integer.parseInt(element.attributeValue("total"));
		//adding all ValenceUnits
		Iterator vu_iter = element.elementIterator("valenceUnit");
		while(vu_iter.hasNext()) {
			Element vu = (Element) vu_iter.next();
			units.add(new ValenceUnit(vu,lexicalUnit));
		}
		//adding all AnnoSet IDs
		Iterator anno_iter = element.elementIterator("annoSet");
		while(anno_iter.hasNext()) {
			Element as = (Element) anno_iter.next();
			annotSetIDS.add(Integer.parseInt(as.attributeValue("ID")));
		}
		
	}


	/**
	 * @return the totalCount
	 */
	public int getTotalCount() {
		return totalCount;
	}


	/**
	 * @return the units
	 */
	public List<ValenceUnit> getUnits() {
		return units;
	}


	/**
	 * @return the annotSetIDS
	 */
	public List<Integer> getAnnotSetIDS() {
		return annotSetIDS;
	}
	
}
