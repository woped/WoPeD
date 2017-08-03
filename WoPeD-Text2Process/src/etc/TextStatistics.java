/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package etc;

public class TextStatistics {
	
	private int f_numberOfSentences;
	private double f_avgSentenceLength;
	private int f_numOfReferences;
	private int f_numOfLinks;
	
	/**
	 * @return the numberOfSentences
	 */
	public int getNumberOfSentences() {
		return f_numberOfSentences;
	}
	/**
	 * @return the avgSentenceLength
	 */
	public double getAvgSentenceLength() {
		return f_avgSentenceLength;
	}
	/**
	 * @return the numOfReferences
	 */
	public int getNumOfReferences() {
		return f_numOfReferences;
	}
	/**
	 * @return the numOfLinks
	 */
	public int getNumOfLinks() {
		return f_numOfLinks;
	}
	/**
	 * @param numberOfSentences the numberOfSentences to set
	 */
	public void setNumberOfSentences(int numberOfSentences) {
		this.f_numberOfSentences = numberOfSentences;
	}
	/**
	 * @param avgSentenceLength the avgSentenceLength to set
	 */
	public void setAvgSentenceLength(double avgSentenceLength) {
		this.f_avgSentenceLength = avgSentenceLength;
	}
	/**
	 * @param numOfReferences the numOfReferences to set
	 */
	public void setNumOfReferences(int numOfReferences) {
		this.f_numOfReferences = numOfReferences;
	}
	/**
	 * @param numOfLinks the numOfLinks to set
	 */
	public void setNumOfLinks(int numOfLinks) {
		this.f_numOfLinks = numOfLinks;
	}
	

}
