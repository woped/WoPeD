package org.woped.metrics.helpers;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public abstract class LabeledFileFilter extends FileFilter{

	public abstract boolean accept(File arg0);

	public abstract String getDescription();
	
	public abstract String getExtension();

}
