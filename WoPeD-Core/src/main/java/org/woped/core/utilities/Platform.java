package org.woped.core.utilities;

/**
 * @author <a href="mailto:freytag@woped.org">Thomas Freytag </a> <br>
 *         <br>
 * 
 * Some static stuff for querying the current platform.
 *  
 */
public class Platform {

	public static boolean isMac() {
		return System.getProperty("os.name").toLowerCase().startsWith("mac");
	}
	
	public static boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().startsWith("win");
	}
	
	public static boolean isUnix() {
		return System.getProperty("os.name").toLowerCase().contains("nix") ||
				System.getProperty("os.name").toLowerCase().contains("nux");
	}
	
	public static String getOSName() {
		return System.getProperty("os.name");
	}
	
	public static String getSystemLookAndFeel() {
		if (Platform.isMac()) 
			return "com.apple.laf.AquaLookAndFeel";
		else if (Platform.isWindows()) 
			return "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
			else 
				return "javax.swing.plaf.metal.MetalLookAndFeel";
	}
}
