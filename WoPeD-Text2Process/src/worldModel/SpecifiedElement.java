/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package worldModel;

import java.util.ArrayList;
import java.util.List;

import worldModel.Specifier.SpecifierType;

public abstract class SpecifiedElement extends OriginatedElement implements Comparable<SpecifiedElement>{

	private int f_wordIndex = -1;
	private String f_name = null;
	private ArrayList<Specifier> f_specifiers = new ArrayList<Specifier>();
	
	
	/**
	 * @param origin
	 * @param wordInSentence
	 */
	public SpecifiedElement(T2PSentence origin, int wordInSentence,String name) {
		super(origin);
		f_wordIndex = wordInSentence;
		f_name = name;
	}

	public List<Specifier> getSpecifiers(SpecifierType type){
		ArrayList<Specifier> _result = new ArrayList<Specifier>();
		for(Specifier s:f_specifiers) {
			if(s.getType().equals(type)) {
				_result.add(s);
			}
		}
		return _result;
	}
	
	public void addSpecifiers(Specifier spec) {
		f_specifiers.add(spec);
	}

	public ArrayList<Specifier> getSpecifiers() {
		return f_specifiers;
	}
	
	

	/**
	 * returns the index of the main word
	 * @return the f_word
	 */
	public int getWordIndex() {
		return f_wordIndex;
	}

	
	
	/**
	 * removes the given specifiers
	 * @param remove
	 */
	public void removeSpecifiers(ArrayList<Specifier> remove) {
		f_specifiers.removeAll(remove);
	}	
	
	/**
	 * @param sp
	 */
	public void removeSpecifier(Specifier sp) {
		f_specifiers.remove(sp);
	}
	
	
	
	public String toString(String pre,String name) {
		StringBuilder _b = new StringBuilder();
		if(pre != null && !pre.isEmpty()) {
			_b.append(pre);
		}
		//print all nums first
		List<Specifier> _nums = getSpecifiers(SpecifierType.NUM);
		if(_nums.size() > 0) {
			for(Specifier s:_nums) {
				_b.append(s.toString());
				_b.append(" ");
			}
		}
		//print amods (adjectives) first
		List<Specifier> _amod = getSpecifiers(SpecifierType.AMOD);
		if(_amod.size() > 0) {
			for(Specifier s:_amod) {
				_b.append(s.toString());
				_b.append(" ");
			}
		}
		//print nns now
		List<Specifier> _nns = getSpecifiers(SpecifierType.NN);
		for(Specifier s:_nns) {
			_b.append(s.toString());
			_b.append(" ");
		}		
		if(name != null && !name.isEmpty()) {
			_b.append(name);
		}else {
			_b.append(getName());
		}
		//print nns now
		List<Specifier> _nnAfters = getSpecifiers(SpecifierType.NNAFTER);
		for(Specifier s:_nnAfters) {
			_b.append(" ");
			_b.append(s.toString());
		}		
		//print all other in separate lines with the type mentioned
		for(Specifier s:f_specifiers) {
			if(!_nns.contains(s) && !_amod.contains(s) && !_nnAfters.contains(s) && !_nums.contains(s)) {
				_b.append("\n\t");
				_b.append(s.getType()+": "+s.getPhrase()+" ("+s.getObject()+")");
			}
		}
		return _b.toString();
	}
	
	@Override
	public String toString() {
		return toString(null,"{Specified Element}");
	}

	public void setName(String name) {
		this.f_name = name;
	}

	public String getName() {
		return f_name;
	}
	
	@Override
	public int compareTo(SpecifiedElement o) {
		return getWordIndex()-o.getWordIndex();
	}
	
}
