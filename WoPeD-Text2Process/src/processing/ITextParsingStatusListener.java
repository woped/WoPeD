/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package processing;

public interface ITextParsingStatusListener {
	
	public void setNumberOfSentences(int number);
	public void sentenceParsed(int number);

}
