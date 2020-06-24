package org.woped.core.model.bpel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TVariable;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TVariables;

public class BpelVariableList {

	private HashSet<BpelVariable> _list = new HashSet<BpelVariable>();

	public BpelVariableList() {
		// TODO Auto-generated constructor stub
	}

	private TVariables genVariableList() {
		return TVariables.Factory.newInstance();
	}

	/* Inserts a variable to a consisting list of TVariables */
	public void addVariable(TVariable arg) {
		this._list.add(new BpelVariable(arg));
	}

	/* Inserts a variable to a consisting list of TVariables */
	public void addVariable(String name, String type) {
		this._list.add(new BpelVariable(name, type));
	}

	/*
	 * Inserts a variable from WSDL-File to a consisting list of TVariables
	 * method is used to create variables in WSDL-tab
	 */
	public void addWSDLVariable(String name, String namespace, String type) {
		this._list.add(new BpelVariable(name, namespace, type));
	}

	/*
	 * Returns a array (type=String) of created variables method is used to
	 * create drop-down list in GUI
	 */
	public String[] getVariableNameArray() {
		String[] list = new String[this._list.size()];
		Iterator<BpelVariable> iter = this._list.iterator();
		int i = 0;
		while (iter.hasNext()) {
			list[i] = iter.next().getName();
			i++;
		}
		return list;
	}
	
	/**
	 * 
	 * @return
	 */
	public HashSet<BpelVariable> getBpelVariableList()
	{
		return this._list;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, String> getListOfVariablesWithType() {
		HashMap<String, String> list = new HashMap<String, String>();
		Iterator<BpelVariable> iter = this._list.iterator();
		while (iter.hasNext()) {
			BpelVariable tmp = iter.next();
			list.put(tmp.getName(), tmp.getType());
		}
		return list;
	}

	/**
	 * 
	 * @return
	 */
	public TVariables getBpelCode() {
		TVariables list = this.genVariableList();
		Iterator<BpelVariable> iter = this._list.iterator();
		while (iter.hasNext()) {
			list.addNewVariable().set(iter.next().getBpelCode());
		}
		return list;
	}

	/**
	 * 
	 * @param arg
	 */
	public void removeVariable(TVariable arg) {
		// TODO noch schreiben
	}
	
	/**
	 * 
	 * @param Name
	 * @return
	 */
	public BpelVariable findBpelVaraibleByName(String Name)
	{
		Iterator<BpelVariable> iter = this._list.iterator();
		while (iter.hasNext()) {
			BpelVariable tmp = iter.next();
			if(tmp.equalByName(Name)) return tmp;
		}		
		return null;
	}

}
