package org.woped.bpel;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.apache.xmlbeans.XmlOptions;
import org.oasisOpen.docs.wsbpel.x20.process.executable.ProcessDocument;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TPartnerLink;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TProcess;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TVariables;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TPartnerLinks;
import org.woped.bpel.datamodel.BpelParserModel;
import org.woped.core.controller.IEditor;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.utilities.FileFilterImpl;
import org.woped.core.utilities.Utils;

//TODO class description
public class BPEL
{

	private static BPEL				bpelMainClass;

	private Vector<String>			_extensions;

	private static ProcessDocument	bpelDoc;
	private static TProcess			Process;

	private FileFilter				_filter;

	// TODO method description
	public BPEL()
	{
		if (BPEL.bpelMainClass != null)
			return;
		this.initFilefilter();
		bpelMainClass = this;
	}

	// TODO method description
	private final void initFilefilter()
	{
		this._extensions = new Vector<String>();
		this._extensions.add("bpel");
		this._filter = new FileFilterImpl(FileFilterImpl.BPELFilter,
				"BPEL (*.bpel)", this._extensions);
	}

	// TODO method description
	public static final BPEL getBPELMainClass()
	{
		if (bpelMainClass == null)
		{
			new BPEL();
		}
		return bpelMainClass;
	}

	// TODO method description
	public FileFilter getFilefilter()
	{
		return this._filter;
	}

	// TODO method description
	public boolean checkFileExtension(JFileChooser jfc)
	{
		return ((FileFilterImpl) jfc.getFileFilter()).getFilterType() == FileFilterImpl.BPELFilter;
	}

	// TODO method description
	public String getSavePath(String basicPath, JFileChooser jfc)
	{
		return basicPath
				+ Utils.getQualifiedFileName(jfc.getSelectedFile().getName(),
						this._extensions);
	}

	// TODO method description
	public boolean saveFile(String Path, IEditor editor)
	{
		PetriNetModelProcessor pnp = (PetriNetModelProcessor) editor
				.getModelProcessor();

		// Generate BPEL Model
		BpelParserModel m = new BpelParserModel();
		System.out.println(m.createModel(pnp.getElementContainer()));
		System.out.println(m.count_elements());
		System.out.println("********last element*************\n "
				+ m.generate_bpel());
		System.out.println(m.count_elements());
		BPEL.genBpelProcess();
		BPEL.Process.set(m.generate_bpel());
		setGlobals(BPEL.Process, pnp);
		System.out.println(BPEL.bpelDoc.toString());
		// File Output
		new File(Path);
		XmlOptions opt = new XmlOptions();
		
		//opt.setSavePrettyPrintIndent(2);
		//opt.setUseDefaultNamespace();
		Map<String, String> map = new HashMap<String, String>();
		map.put("http://docs.oasis-open.org/wsbpel/2.0/process/executable","bpel");
		map.put("http://www.w3.org/2001/XMLSchema","xs");
		opt = opt.setSaveSuggestedPrefixes(map);
		opt.setSavePrettyPrint();
		try
		{
			bpelDoc.save(new File(Path), opt);
			return true;
		} catch (IOException e)
		{
			return false;
		}
	}

	public String genPreview(IEditor editor)
	{
		PetriNetModelProcessor pnp = (PetriNetModelProcessor) editor
				.getModelProcessor();

		// Generate BPEL Model
		BpelParserModel m = new BpelParserModel();
		System.out.println(m.createModel(pnp.getElementContainer()));
		System.out.println(m.count_elements());
		System.out.println("********last element*************\n "
				+ m.generate_bpel());
		System.out.println(m.count_elements());
		BPEL.genBpelProcess();
		BPEL.Process.set(m.generate_bpel());
		setGlobals(BPEL.Process, pnp);
		XmlOptions opt = new XmlOptions();
		//opt.setSavePrettyPrintIndent(2);
		//opt.setUseDefaultNamespace();
		Map<String, String> map = new HashMap<String, String>();
		map.put("http://docs.oasis-open.org/wsbpel/2.0/process/executable","bpel");
		map.put("http://www.w3.org/2001/XMLSchema","xs");
		opt = opt.setSaveSuggestedPrefixes(map);
		opt.setSavePrettyPrint();
		return BPEL.bpelDoc.xmlText(opt);
	}

	public static TProcess genBpelProcess()
	{
		// if(BPEL.Process != null)return BPEL.Process;
		XmlOptions opt = new XmlOptions();
		opt.setUseDefaultNamespace();
		opt.setSavePrettyPrint();
		opt.setSavePrettyPrintIndent(2);
		Map<String, String> map = new HashMap<String, String>();
		map.put("xmlns:bpel", "bpel.woped.org");
		map.put("xmlns:bpel", "");
		map.put("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
		// opt = opt.setSaveImplicitNamespaces(map);
		BPEL.bpelDoc = ProcessDocument.Factory.newInstance(opt);
		BPEL.Process = bpelDoc.addNewProcess();
		return BPEL.Process;
	}

	public void setGlobals(TProcess iProcess, PetriNetModelProcessor pnp){
		TVariables itempVars = (TVariables)pnp.getElementContainer().getTVariablesList();
		TPartnerLinks itempLinks = (TPartnerLinks)pnp.getElementContainer().getTPartnerLinkList(); 
		if ((itempVars.sizeOfVariableArray()>0)&(itempVars != null)){
			TVariables iVars = iProcess.addNewVariables();
			for (int i=0;itempVars.sizeOfVariableArray()>i;i++){
				iVars.addNewVariable().set(itempVars.getVariableArray(i));
			}
		}
		System.out.println("Size: "+itempLinks.sizeOfPartnerLinkArray());
		if ((itempLinks.sizeOfPartnerLinkArray()>0)&(itempLinks != null)){
			TPartnerLinks iLinks = iProcess.addNewPartnerLinks();
			for (int j=0;itempLinks.sizeOfPartnerLinkArray()>j;j++){
				iLinks.addNewPartnerLink().set((TPartnerLink)itempLinks.getPartnerLinkArray(j));
			}
		}
	}
}