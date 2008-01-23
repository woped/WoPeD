package org.woped.bpel.gui.transitionproperties;

import java.util.HashMap;
import java.util.Map;

import org.apache.xmlbeans.XmlOptions;
import org.oasisOpen.docs.wsbpel.x20.process.executable.ProcessDocument;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TProcess;

public abstract class BaseActivity
{
	private TActivity Data = null;

	public TActivity getActivity()
	{
		return this.Data;
	}
	
	public void setActivity(TActivity ta)
	{
		this.Data = ta;
	}
	
	public BaseActivity()
	{
		// TODO Auto-generated constructor stub
	}
	
	public static TProcess genBpelProcess()
	{
		//if(BPEL.Process != null)return BPEL.Process;
		ProcessDocument bpelDoc;
		XmlOptions opt = new XmlOptions();
        opt.setUseDefaultNamespace();
        opt.setSavePrettyPrint();
        opt.setSavePrettyPrintIndent(2);
        Map<String, String> map = new HashMap<String, String>();
        map.put("xmlns:bpel", "bpel.woped.org");
        map.put("xmlns:bpel","");
        map.put("xmlns:xsd","http://www.w3.org/2001/XMLSchema");
        //opt = opt.setSaveImplicitNamespaces(map);
        bpelDoc = ProcessDocument.Factory.newInstance(opt);
		return bpelDoc.addNewProcess();
	}

}
