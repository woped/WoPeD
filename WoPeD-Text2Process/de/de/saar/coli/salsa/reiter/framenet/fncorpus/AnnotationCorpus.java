package de.saar.coli.salsa.reiter.framenet.fncorpus;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jaxen.JaxenException;

import de.saar.coli.salsa.reiter.framenet.CorpusReader;
import de.saar.coli.salsa.reiter.framenet.FrameNet;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.saar.coli.salsa.reiter.framenet.LexicalUnit;

/**
 * This class can be used to access the luXML files, i.e., the examples in the
 * FrameNet annotation. It's heavily inter- weaved with and based on the
 * FrameNetCorpus class.
 * 
 * 
 * @author reiter
 * @since 0.4
 * 
 */
public class AnnotationCorpus extends CorpusReader {

	/**
	 * An index of AnnotatedLexicalUnits
	 */
	Map<LexicalUnit, AnnotatedLexicalUnit> annotations;

	/**
	 * parameter which decides if the corpus and its annotation labels
	 * are loaded into the annotatedLexicalUnits
	 * It can be deactivated to speed up loading when only ValencePatterns are
	 * needed.
	 */
	boolean scanSubCorpuses = true;
		
	/**
	 * A constructor, just as in FrameNetCorpus.
	 * 
	 * @param frameNet
	 * @param logger
	 */
	public AnnotationCorpus(FrameNet frameNet, Logger logger) {
		super(frameNet, logger);
		annotations = new HashMap<LexicalUnit, AnnotatedLexicalUnit>();
	}

	/**
	 * Returns the annotated examples with the given lexical unit.
	 * 
	 * @param lu
	 *            The lexical unit
	 * @return An object of the class AnnotatedLexicalUnit
	 */
	public AnnotatedLexicalUnit getAnnotation(LexicalUnit lu) {
		return annotations.get(lu);
	}

	@Override
	public void parse(File directory) {
		parse(directory, ".*");
	}

	/**
	 * An extension of {@link #parse(File)}. Allows the specification of a
	 * pattern, which must be contained in the filename of the files to be
	 * loaded.
	 * 
	 * Example: If you specify "2442" as pattern string, only lu-files with 2442
	 * in their names will be loaded.
	 * 
	 * @param directory
	 *            The luXML directory
	 * @param pattern
	 *            A pattern
	 */
	public void parse(File directory, String pattern) {
		if (directory.isDirectory() && directory.canRead()) {
			for (File file : directory.listFiles()) {
				if (file.isFile() && !file.getName().endsWith(".xsl") && file.getName().matches(pattern))
					parseFile(file);
			}
		}
	}

	
	
	private void parseFile(File file) {		  
		// Make document
		Document document = null;
		try {
			SAXReader reader = new SAXReader(false);
			document = reader.read(file);
		} catch (DocumentException e) {
			getLogger().severe(e.getMessage());
		}		
		getLogger().info(
				"XML Document (" + file.getAbsolutePath() + ") has been read.");
		
		try { 
			Element luanno = document.getRootElement();			
			LexicalUnit lu = frameNet.getLexicalUnit(Integer.valueOf(luanno.attributeValue("ID"))); //the ID
			annotations.put(lu, new AnnotatedLexicalUnit(this, luanno, lu));

		} catch (JaxenException e) {
			e.printStackTrace();
		} catch (FrameNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * retrieves a parameter which decides if the corpus and its annotation labels
	 * are loaded into the annotatedLexicalUnits
	 * It can be deactivated to speed up loading when only ValencePatterns are
	 * needed.
	 * @return the scanSubCorpuses
	 */
	public boolean isScanSubCorpuses() {
		return scanSubCorpuses;
	}

	/**
	 * sets a parameter which decides if the corpus and its annotation labels
	 * are loaded into the annotatedLexicalUnits
	 * It can be deactivated to speed up loading when only ValencePatterns are
	 * needed.
	 * @param scanSubCorpuses the scanSubCorpuses to set
	 */
	public void setScanSubCorpuses(boolean scanSubCorpuses) {
		this.scanSubCorpuses = scanSubCorpuses;
	}

}
