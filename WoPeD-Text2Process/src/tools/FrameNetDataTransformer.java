/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package tools;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class FrameNetDataTransformer {
	
	private static String targetFolder = "FrameNet/fndata-1.5/lu-reduced/";
	private static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	static {
		try {
			dbf.newDocumentBuilder();
			File target = new File(targetFolder);
			if(!target.exists()) {
				target.mkdir();
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
}
