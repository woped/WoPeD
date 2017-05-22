package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import processing.FrameNetWrapper;
import processing.WordNetWrapper;
import etc.TextToProcess;

public class Initiator {
	
	private TextToProcess t2p = new TextToProcess();
//	private File file = new File("data.txt");
	
	public Initiator(){
		WordNetWrapper.init();
		FrameNetWrapper.init();
	}
	
	public void convert (String input, boolean bpmn, File outputFile){
		t2p.parseText(input, bpmn, outputFile);
	   }
	
	public void convert (File file, boolean bpmn, File outputFile) throws IOException{
		t2p.parseFile(file, bpmn, outputFile);
	}
	
	//for GUI
	public TextToProcess getT2P (){
		return t2p;
	}
	
//	public void saveInput (String input) throws FileNotFoundException{
//		   PrintWriter out = new PrintWriter(file);
//		   out.println(input);
//		   out.close();
//	}
	
	

}
