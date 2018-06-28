package org.woped.gui.translations.analytics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 *  Class AnalysisTools.
 *  Provides static methods for analyzing Messages.properties files
 */
public class AnalysisTools {

	/** available key list. */
	public static ArrayList<String> keyList = new ArrayList<String>();

	/** used keys list. */
	public static ArrayList<String> usedKeys = new ArrayList<String>();

	/**
	 * Check usage of keys in file.
	 *
	 * @param file the java sourcecode-file to check usage of keys in
	 */
	static void checkUsageInFileExtend(File file) {
		try {
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().toLowerCase();
				for (Iterator<String> iterator = keyList.iterator(); iterator.hasNext(); ) {
					  String keyElement = iterator.next();

				String key = keyElement.toLowerCase();

				String keyNoExt = substringBeforeLastSeperator(key, ".").toLowerCase();
				if (key.endsWith(".title")){
					if(line.contains("gettitle(\"" + keyNoExt + "\")")){
						usedKeys.add(keyElement);
						iterator.remove();
					}
				}
				else if (key.endsWith(".icon")){
					if(line.contains("geticonlocation(\"" + keyNoExt + "\")")){
						usedKeys.add(keyElement);
						iterator.remove();
					}
				}
				else if (key.endsWith(".image")){
					if(line.contains("getimagelocation(\"" + keyNoExt + "\")")){
						usedKeys.add(keyElement);
						iterator.remove();
					}
				}
				else if (key.endsWith(".cursoricon")){
					if(line.contains("getcursoriconlocation(\"" + keyNoExt + "\")")){
						usedKeys.add(keyElement);
						iterator.remove();
					}
				}
				else if (key.endsWith(".mnemonic")){
					if(line.contains("getmnemonic(\"" + keyNoExt + "\")")){
						usedKeys.add(keyElement);
						iterator.remove();
					}
				}
				else if (key.endsWith(".shortcut")){
					if(line.contains("getshortcut(\"" + keyNoExt + "\")")){
						usedKeys.add(keyElement);
						iterator.remove();
					}
					else if(line.contains("getshortcutkey(\"" + keyNoExt )){
						usedKeys.add(keyElement);
						iterator.remove();
					}
					else if(line.contains("addshortcuttojcommandbutton(\"" + keyNoExt)) {
						usedKeys.add(keyElement);;
						iterator.remove();
					}
					else if(line.contains("addshortcuttomousebutton(\"" + keyNoExt)) {
						usedKeys.add(keyElement);;
						iterator.remove();
					}
				}
				else if (key.contains(".shortcutmodifier")){
					if(line.contains("getshortcutmodifier(\"" + keyNoExt )){
						usedKeys.add(keyElement);
						iterator.remove();
					}
					else if(line.contains("addshortcuttojcommandbutton(\"" + keyNoExt)) {
						usedKeys.add(keyElement);;
						iterator.remove();
					}
					else if(line.contains("addshortcuttomousebutton(\"" + keyNoExt)) {
						usedKeys.add(keyElement);;
						iterator.remove();
					}
				}
				else if (key.endsWith(".text")){
					if(line.contains(keyNoExt)){
						usedKeys.add(keyElement);
						iterator.remove();
					}
				}
				else if (key.endsWith(".tooltip")){
					if(line.contains(keyNoExt)){
						usedKeys.add(keyElement);
						iterator.remove();
					}
				}
				else if(line.contains(keyNoExt) && line.contains("actionid_")) {
					usedKeys.add(substringBeforeLastSeperator(keyElement, ".") + ".Title");
					usedKeys.add(substringBeforeLastSeperator(keyElement, ".") + ".title");
					usedKeys.add(substringBeforeLastSeperator(keyElement, ".") + ".Image");
					usedKeys.add(substringBeforeLastSeperator(keyElement, ".") + ".image");
					usedKeys.add(substringBeforeLastSeperator(keyElement, ".") + ".Icon");
					usedKeys.add(substringBeforeLastSeperator(keyElement, ".") + ".icon");
					usedKeys.add(substringBeforeLastSeperator(keyElement, ".") + ".Mnemonic");
					usedKeys.add(substringBeforeLastSeperator(keyElement, ".") + ".mnemonic");
					usedKeys.add(substringBeforeLastSeperator(keyElement, ".") + ".Shortcut");
					usedKeys.add(substringBeforeLastSeperator(keyElement, ".") + ".shortcut");
				}
				else{
					if(line.contains(key)) {
						usedKeys.add(keyElement);
						iterator.remove();
					}
				}
			}
			}
		} catch(Exception e) {
			//handle this
		}

	keyList.removeAll(usedKeys);
}

	/**
	 * Substring before last seperator.
	 * Returns substring before last occurrence of a given seperator.
	 *
	 * @param str the original string
	 * @param separator to split
	 * @return string after modification
	 */
	public static String substringBeforeLastSeperator(String str, String separator) {
		if (isEmptyString(str) || isEmptyString(separator)) {
			return str;
		}
		int pos = str.lastIndexOf(separator);
		if (pos == -1) {
			return str;
		}
		return str.substring(0, pos);
	}

	/**
	 * Checks if is empty string.
	 *
	 * @param str the original string
	 * @return true, if it is a empty string
	 */
	public static boolean isEmptyString(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * List files.
	 * retruns all .java files in a given directory (incl. subdirectories)
	 *
	 * @param directoryName the directory name
	 * @param files the files
	 */
	public static void listFiles(String directoryName, ArrayList<File> files) {
		File directory = new File(directoryName);
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile()) {
				String filename = file.getName();
				String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
				String java = "java";
				if (extension.equals(java)) {
					files.add(file);
				}
			} else if (file.isDirectory()) {
				listFiles(file.getAbsolutePath(), files);
			}
		}
	}

	/**
	 * Removes the key from file.
	 *
	 * @param file the file
	 * @param keyToRemove the key to remove
	 */
	public static void removeKeyFromFile(String file, String keyToRemove) {
		try {
		  File inFile = new File(file);
		  if (!inFile.isFile()) {
		    return;
		  }

		  File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

		  BufferedReader br = new BufferedReader(new FileReader(file));
		  PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

		  String line = null;


		  while ((line = br.readLine()) != null) {
		    if (!line.trim().contains(keyToRemove)) {
		      pw.println(line);
		      pw.flush();
		    }
		  }
		  pw.close();
		  br.close();

		  if (!inFile.delete()) {
		    return;
		  }

		}
		catch (FileNotFoundException ex) {
		  ex.printStackTrace();

		}
		catch (IOException ex) {
		  ex.printStackTrace();
		}
		}
}
