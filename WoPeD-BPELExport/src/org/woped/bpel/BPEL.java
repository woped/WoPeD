package org.woped.bpel;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.woped.core.model.ArcModel;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.utilities.FileFilterImpl;
import org.woped.core.utilities.Utils;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlOptions;
import org.woped.bpel.ProcessDocument;
import org.woped.bpel.TProcess;


//TODO class description
public class BPEL {

	private static BPEL bpelMainClass;

	private Vector<String> _extensions;

	private ProcessDocument bpelDoc;

	private FileFilter _filter;


	//TODO method description
	public BPEL() {
		if(BPEL.bpelMainClass != null) return;
		this.initFilefilter();
		bpelMainClass = this;
	}

	//TODO method description
	private final void initFilefilter() {
		this._extensions = new Vector<String>();
		this._extensions.add("bpel");
		this._filter = new FileFilterImpl(FileFilterImpl.BPELFilter,
				"BPEL (*.bpel)", this._extensions);
	}

	//TODO method description
	public static final BPEL getBPELMainClass() {
		if (bpelMainClass == null) {
			new BPEL();
		}
		return bpelMainClass;
	}

	//TODO method description
	public FileFilter getFilefilter() {
		return this._filter;
	}

	//TODO method description
	public boolean checkFileExtension(JFileChooser jfc)
	{
		return ((FileFilterImpl) jfc.getFileFilter()).getFilterType() == FileFilterImpl.BPELFilter;
	}

	//TODO method description
	public String getSavePath(String basicPath, JFileChooser jfc)
	{
		return basicPath + Utils.getQualifiedFileName(jfc.getSelectedFile().getName(), this._extensions);
	}

	//TODO method description
	public boolean saveFile(String Path, PetriNetModelProcessor pnp)
	{
		this.test(pnp);
		//new File(Path);
		bpelDoc = ProcessDocument.Factory.newInstance();
		TProcess iProcess = bpelDoc.addNewProcess();
		XmlCursor cursor = iProcess.newCursor();
		cursor.insertComment("Hello World");
		
        XmlOptions opt = new XmlOptions();
        opt.setUseDefaultNamespace();
        opt.setSavePrettyPrint();
        opt.setSavePrettyPrintIndent(2);
        Map<String, String> map = new HashMap<String, String>();
        map.put("", "bpel.woped.org");
        opt.setSaveImplicitNamespaces(map);
        try
        {
        	bpelDoc.save(new File(Path), opt);
        	return true;
        } catch (IOException e)
        {
            return false;
        }
	}
	
	/**
	 * Testing method
	 */
	private void test(PetriNetModelProcessor pnp)
	{
		System.out.println("begin test");
		Map<String, ArcModel> map = pnp.getElementContainer().getArcMap();
		System.out.println(map.size());
		Collection<ArcModel> test = map.values();
		Iterator<ArcModel> list = test.iterator();
		while(list.hasNext())
		{	
			ArcModel arc = list.next();
			System.out.println("Source: " + arc.getSourceId());
			System.out.println("Target: " + arc.getTargetId());
		}
		System.out.println("end test");
		
	}

}