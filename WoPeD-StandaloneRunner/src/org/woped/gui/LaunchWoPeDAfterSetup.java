package org.woped.gui;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.woped.core.utilities.Platform;

public class LaunchWoPeDAfterSetup {
	public static void main(String[] args) {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		
		String lang = "eng", path = "";
		
		if (Platform.isMac())			
			lang = System.getProperty("user.language");

		if (args.length == 1) {
			lang = args[0]; 
		}

		if (args.length == 2) {
			lang = args[0]; 
			path = args[1];
		}	
		
/*		JOptionPane.showMessageDialog(
				null, 
				"  Path: " + path + " - Lang: " + lang,
				"Settings",
                JOptionPane.INFORMATION_MESSAGE);*/
		
		new WoPeDDialog(lang, path);
	}
}
	
class WoPeDDialog {
	public WoPeDDialog(String lang, String path) {
		
		// Wait until WoPeD setup utility has completely terminated
		try {
			while (!setupHasFinished()) {
				Thread.sleep(1000);
			}
		}
		catch(InterruptedException e){ } 


		String quest = lang.equals("deu") || lang.equals("de") ? 
				"Soll WoPeD jetzt gestartet werden?" : "Shall WoPeD be launched now?";
		
		String title = lang.equals("deu") || lang.equals("de") ? 
				"WoPeD starten" : "Launch WoPeD";

		int result = JOptionPane.showConfirmDialog(
				null, 
				quest,
				title,
                JOptionPane.YES_NO_OPTION);

		if (result == JOptionPane.YES_OPTION) {
			final String args[] = {};
			RunWoPeD.main(args);
		}
	}
	
	private boolean setupHasFinished() {
		try {
			String 	line;	
			String  pattern;
			Process p;
			
			if (Platform.isWindows()) {		
				p = Runtime.getRuntime().exec
			        	(System.getenv("windir") +"\\system32\\"+"tasklist.exe -v");
				pattern = "IzPack -";
			}
			else if (Platform.isMac()) {		
				p = Runtime.getRuntime().exec("ps -e");
				pattern = "Installer";
			}
			else {
				p = Runtime.getRuntime().exec("ps -e");
				pattern = "WoPeD-install-";				
			}

			BufferedReader input = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			while ((line = input.readLine()) != null) {
				if (line.contains(pattern))
					return false;
			}
			input.close();
			
		} catch (Exception err) {
			err.printStackTrace();
		}
		
		return true;
	}
}
