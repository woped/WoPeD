package test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import gui.GUI;

public class Tests {
	List<File> resultFiles = new ArrayList<File>();
	List<File> commandFiles = new ArrayList<File>();
	String path = "Testdaten";
	
	@Test
	public void test() {
		File folder = new File(path);
		this.searchFilesForFolder(folder);
		
		// delete old result + command files
//		File resultFile = new File("test.epml");
//		File commandFile = new File("test.command");
//		resultFile.delete();
//		resultFile = new File("test.bpmn");
//		resultFile.delete();
//		commandFile.delete();
		
		if (resultFiles.size() != commandFiles.size()){
			fail("sizes of resultFiles and commandFiles lists do not match!");
		}
		
		for (int i = 0; i < commandFiles.size(); i++) {
			File commandFile = commandFiles.get(i);
			File shouldResultFile = resultFiles.get(i);
			
			List<String> commandLines = null;
			try {
				commandLines = Files.readAllLines(commandFile.toPath());
			} catch (IOException e) {
//				fail("command file is corrupted?");
				e.printStackTrace();
			}
			if (commandLines.size() != 1){
				fail("command file is corrupted!");
			}
			
			// source: http://stackoverflow.com/questions/18893390/splitting-on-comma-outside-quotes
			String[] args = commandLines.get(0).split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
			
			File resultFile = new File(args[2].replace("output=", ""));
			
			GUI.main(args);
			
			// compare results
			boolean equal = true;
			List<String> realResultLines = null;
			List<String> shouldResultLines = null;
			try {
				realResultLines = Files.readAllLines(resultFile.toPath());
				shouldResultLines = Files.readAllLines(shouldResultFile.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (realResultLines.size() != shouldResultLines.size()){
				fail("files do not match (size)!");
			}
			
			for (int j = 0; j < realResultLines.size(); j++) {
				if (!equal) {
					System.out.println("Do not equal!");
					break;
				}
				if (! (realResultLines.get(j).length() == shouldResultLines.get(j).length())){
					equal = false;
					break;
				}
			}
			
			// delete files
//			resultFile.delete();
			
			assertTrue("Result", equal);
		}
		
		
		
	}

	// source: http://stackoverflow.com/questions/1844688/read-all-files-in-a-folder
	public void searchFilesForFolder(final File folder) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				searchFilesForFolder(fileEntry);
			} else {
				if (fileEntry.getName().endsWith(".command")) {
					this.commandFiles.add(fileEntry);
				} else {
					this.resultFiles.add(fileEntry);
				}
			}
		}
	}

}
