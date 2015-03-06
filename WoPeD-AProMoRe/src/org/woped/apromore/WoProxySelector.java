package org.woped.apromore;
import java.io.IOException;
import java.net.*;
import java.util.*;

import javax.swing.JOptionPane;

import org.woped.gui.translations.Messages;
 
public class WoProxySelector extends ProxySelector {
 
	Proxy proxy;
    public WoProxySelector(String address, int port)
    {
    	proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(address, port));
    }
	
	@Override
    public List<Proxy> select(URI uri)
    {
        ArrayList<Proxy> list = new ArrayList<Proxy>();
        list.add(proxy);
        return list;
    }
 
    @Override
    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
    	
		String[] options = { "OK" };
    	
		JOptionPane.showOptionDialog(null, 
				Messages.getString("Apromore.Export.UI.Error.AproNoConn"), 
				Messages.getString("Apromore.Export.UI.Error.AproNoConnTitle"),
		        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,

		        null, options, options[0]);
    }
}