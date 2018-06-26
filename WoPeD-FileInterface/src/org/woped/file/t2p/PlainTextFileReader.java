package org.woped.file.t2p;

import java.util.Scanner;
import javax.swing.JFileChooser;
import java.io.File;

public class PlainTextFileReader implements FileReader {
	private JFileChooser chooser = new JFileChooser();
	private StringBuilder sb; 
	
	@Override
	public String read() throws NoFileException {
		sb = new StringBuilder();
		
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			try {
				Scanner input = new Scanner(file);
				while (input.hasNext()) {
					sb.append(input.nextLine());
					sb.append('\n');
				}
				input.close();
			} catch (Exception e) {
				throw new NoFileException();
			}
		} else {
			throw new NoFileException();
		}
		
		return sb.toString();
	}
}


