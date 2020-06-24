package org.woped.file.t2p;

import java.awt.FileDialog;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.utilities.FileFilterImpl;
import org.woped.core.utilities.Platform;

public class PlainTextFileReader implements FileReader {
	private JFileChooser chooser = new JFileChooser();
	private StringBuilder sb; 
	
	@Override
	public String read()  {
		sb = new StringBuilder();
		int res = 0;
		File file;
		String lastdir = "";
		String abspath;

        if (ConfigurationManager.getConfiguration().isHomedirSet()) {
            lastdir = ConfigurationManager.getConfiguration().getHomedir();
        }

        if (ConfigurationManager.getConfiguration().isCurrentWorkingdirSet()) {
            lastdir = ConfigurationManager.getConfiguration().getCurrentWorkingdir();
        }

        if (!Platform.isMac()) {
            chooser.setCurrentDirectory(new File(lastdir));
            chooser.addChoosableFileFilter(new FileFilterImpl(FileFilterImpl.SAMPLEFilter, "ASCII text", "txt"));

            res = chooser.showOpenDialog(null);

			if (res == JFileChooser.APPROVE_OPTION) {
				file = chooser.getSelectedFile();
				abspath = file.getAbsolutePath();
            } else {
				return null;
			}
		}
		else {
            // Mac part, let's do the same with the awt.FileDialog
            JFrame frame = null;
			String fn;
			FileDialog fileDialog;

            fileDialog = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
            fileDialog.setDirectory(lastdir);

            // Set fileFilter to txt files here
			fileDialog.setFilenameFilter(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".txt");
				}
			});

			fileDialog.setVisible(true);

			fn = fileDialog.getFile();
			if (fn != null) {
				abspath = fileDialog.getDirectory() + fn;
			}
			else {
			    return null;
            }

			file = new File(abspath);
		}

		try {
			Scanner input = new Scanner(file);
            while (input.hasNext()) {
				sb.append(input.nextLine());
				sb.append('\n');
			}
			input.close();
		} catch (Exception e) {
			return null;
		}

        ConfigurationManager.getConfiguration().setCurrentWorkingdir(abspath.substring(0, abspath.lastIndexOf(File.separator)));
		return sb.toString();
	}
}


