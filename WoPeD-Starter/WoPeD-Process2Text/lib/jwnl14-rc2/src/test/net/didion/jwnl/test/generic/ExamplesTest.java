package test.net.didion.jwnl.test.generic;

import junit.framework.TestCase;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.dictionary.Dictionary;

public class ExamplesTest extends TestCase {

	public void testMorphological() {
		try {  
			JWNL.initialize(TestDefaults.getInputStream());
			IndexWord iw = Dictionary.getInstance().lookupIndexWord(POS.VERB, "running-away");
          
			System.out.println("Index word : " + iw.toString());
			
		} catch (JWNLException e) {
			e.printStackTrace();
		}
		
		
	}
	
}
