package org.woped.t2p;


import java.util.Scanner;

import javax.swing.JFileChooser;

public class OpenFile {
	JFileChooser chooser = new JFileChooser();
	StringBuilder sb = new StringBuilder();
	public void PickMe() throws Exception{
	
	if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
		
		java.io.File file = chooser.getSelectedFile();
		
		Scanner input = new Scanner(file);
		
		while(input.hasNext()){
			sb.append(input.nextLine());
			sb.append("\n");
		}
		input.close();
	}
//	else {
//		sb.append("No file was selected");
//	}
	}
}
