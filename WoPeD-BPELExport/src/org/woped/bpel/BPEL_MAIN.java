package org.woped.bpel;

import java.io.File;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.utilities.Utils;
import org.woped.editor.utilities.FileFilterImpl;

public class BPEL_MAIN {

	private static BPEL_MAIN _BPEL_MAIN_CLASS;

	private Vector<String> _extensions;

	private FileFilter _filter;

	public BPEL_MAIN() {
		this.INIT_FILEFILTER();
		_BPEL_MAIN_CLASS = this;
	}

	private final void INIT_FILEFILTER() {
		this._extensions = new Vector<String>();
		this._extensions.add("bpek");
		this._filter = new FileFilterImpl(FileFilterImpl.BPELFilter,
				"BPEL (*.bpel)", this._extensions);
	}

	public static final BPEL_MAIN get_BPEL_MAIN_CLASS() {
		if (_BPEL_MAIN_CLASS == null) {
			new BPEL_MAIN();
		}
		return _BPEL_MAIN_CLASS;
	}

	public FileFilter get_filefilter() {
		return this._filter;
	}
	
	public boolean check_file_extension(JFileChooser jfc)
	{
		return ((FileFilterImpl) jfc.getFileFilter()).getFilterType() == FileFilterImpl.BPELFilter;
	}
	
	public String get_SavePath(String basicPath,JFileChooser jfc)
	{
		return basicPath + Utils.getQualifiedFileName(jfc.getSelectedFile().getName(), this._extensions);
	}
	
	public boolean save_file(String Path, PetriNetModelProcessor pnp)
	{
		new File(Path);
		return true;
	}

}
