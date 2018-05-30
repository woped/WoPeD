package sentenceRealization;

/**
 * @author prasanna
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.pdfbox.cos.COSDocument;
import org.pdfbox.pdfparser.PDFParser;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.pdmodel.PDDocumentInformation;
import org.pdfbox.util.PDFTextStripper;

public class PDFTextParser {

	PDFParser parser;
	String parsedText;
	PDFTextStripper pdfStripper;
	PDDocument pdDoc;
	COSDocument cosDoc;
	PDDocumentInformation pdDocInfo;

	// PDFTextParser Constructor
	public PDFTextParser() {
	}

	// Extract text from PDF Document
	String pdftoText(String fileName) {

		System.out.println("Parsing text from PDF file " + fileName + "....");
		File f = new File(fileName);

		if (!f.isFile()) {
			System.out.println("File " + fileName + " does not exist.");
			return null;
		}

		try {
			parser = new PDFParser(new FileInputStream(f));
		} catch (Exception e) {
			System.out.println("Unable to open PDF Parser.");
			return null;
		}

		try {
			parser.parse();
			cosDoc = parser.getDocument();
			pdfStripper = new PDFTextStripper();
			pdDoc = new PDDocument(cosDoc);
			parsedText = pdfStripper.getText(pdDoc);
		} catch (Exception e) {
			System.out.println("An exception occured in parsing the PDF Document.");
			e.printStackTrace();
			try {
				if (cosDoc != null)
					cosDoc.close();
				if (pdDoc != null)
					pdDoc.close();
			} catch (Exception e1) {
				e.printStackTrace();
			}
			return null;
		}
		System.out.println("Done.");
		return parsedText;
	}

	// Write the parsed text from PDF to a file
	void writeTexttoFile(String pdfText, String fileName) {

		System.out.println("\nWriting PDF text to output text file " + fileName
				+ "....");
		try {
			PrintWriter pw = new PrintWriter(fileName);
			pw.print(pdfText);
			pw.close();
		} catch (Exception e) {
			System.out
					.println("An exception occured in writing the pdf text to file.");
			e.printStackTrace();
		}
		System.out.println("Done.");
	}

	// Extracts text from a PDF Document and writes it to a text file
	public static void main(String args[]) {

//		PDFTextParser pdfTextParserObj = new PDFTextParser();
//		String pdfToText = pdfTextParserObj
//				.pdftoText("/Users/henrikleopold/Documents/Uni/current projects/Process to Text/Duden.pdf");
//
//		if (pdfToText == null) {
//			System.out.println("PDF to Text Conversion failed.");
//		} else {
//			System.out.println("\nThe text parsed from the PDF Document....\n"
//					+ pdfToText);
//			pdfTextParserObj.writeTexttoFile(pdfToText, "pdfoutput.txt");
//		}
		
//		try {
//		    BufferedReader in = new BufferedReader(new FileReader("pdfoutput.txt"));
//		    String str;
//		    while ((str = in.readLine()) != null) {
//		        
//		    	if (str.contains(", der:")) {
//		    		System.out.println(str.substring(0, str.indexOf(", der:")) + "\tm");
//		    	}
//		    	if (str.contains(", die:")) {
//		    		System.out.println(str.substring(0, str.indexOf(", die:")) + "\tf");
//		    	}
//		    	if (str.contains(", das:")) {
//		    		System.out.println(str.substring(0, str.indexOf(", das:")) + "\tn");
//		    	}
//		    	
//		    }
//		    in.close();
//		} catch (IOException e) {
//		}
		
		try {
			PrintWriter out = new PrintWriter("German_Verbs_raw.txt");
			
			for (int filenr = 1; filenr <=8688; filenr++) {
			    BufferedReader in = new BufferedReader(new FileReader("/Users/henrikleopold/Desktop/Extracted Data/" + filenr + ".txt"));
			    String str;
			    int c = 0;
			    String totalOutput = "";
			    while ((str = in.readLine()) != null) {
			    	c++;
			    	str = str.replaceAll("ä", "Š");
			    	str = str.replaceAll("ö", "š");
			    	str = str.replaceAll("ü", "Ÿ");
			    	str = str.replaceAll("\\\\\"a", "Š");
			    	str = str.replaceAll("\\\\\"u", "Ÿ");
			    	str = str.replaceAll("\\\\\"o", "š");
			    	
			    	if (str.contains("Verb: ")) {
//			    		System.out.println(filenr + " " + str.substring(str.indexOf("Verb: ")+6,str.length()));
			    		totalOutput = totalOutput + filenr + "\t" + str.substring(str.indexOf("Verb: ")+6,str.length());
			    	}
			    	if (c == 9 || c == 14 || c == 21 || c == 26 || c == 33 || c == 38 || c == 57 || c == 62 || c == 93 || c == 98 || c == 105 || c == 110 || c == 129 || c == 134) {
			    		String[] split = str.split(" ");
			    		String output = "";
			    		for (int i = 1; i < split.length; i++) {
			    			output = output + " " + split[i];
			    		}
			    		output = output.trim();
			    		
			    		if (output.contains(", ")) {
			    			output = output.split(",")[0];
			    		}
//			    		System.out.println("\t" + output);
			    		totalOutput = totalOutput + "\t" + output;
			    	}
			    	
			    	if (c== 176 || c == 177 || c == 180) {
			    		String output = str;
			    		if (output.contains(", ")) {
			    			output = output.split(",")[0];
			    		}
//			    		System.out.println("\t" + output);
			    		totalOutput = totalOutput + "\t" + output;
			    	}
			    }
			    System.out.println(totalOutput);
			    out.println(totalOutput);
			    in.close();
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}
}
