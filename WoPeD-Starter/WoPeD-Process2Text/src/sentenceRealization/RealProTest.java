package sentenceRealization;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class RealProTest {
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		SurfaceRealizer sr = new SurfaceRealizer();
		System.out.println(sr.realizeSentence(1, 5));
	}
}
