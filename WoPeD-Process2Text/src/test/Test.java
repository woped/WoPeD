package test;

import textGenerator.TextGenerator;

public class Test {

	public static void main(String[] args) throws Exception {
		TextGenerator textGenerator = new TextGenerator();
		System.out.println(textGenerator.toText("/Users/freytag/Daten/Dokumente/Entwicklung/Testdaten/LoanApplicationResources.pnml"));
	}
}
