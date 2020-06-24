package org.woped.quantana.dashboard.storage;

import java.io.File;
import java.io.InputStream;

import org.apache.xmlbeans.XmlObject;
import org.woped.config.WoPeDConfiguration;


public class WoPeDDashboardConfiguration extends WoPeDConfiguration {

	@Override
	public boolean initConfig() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getConfigFilePath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected XmlObject getConfDocument() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean readConfig() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean readConfig(InputStream is) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean readConfig(File file) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean readConfig(XmlObject configDoc) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveConfig(File file) {
		// TODO Auto-generated method stub
		return false;
	}

}
