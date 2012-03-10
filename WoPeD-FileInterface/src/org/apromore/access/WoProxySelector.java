package org.apromore.access;
import java.io.IOException;
import java.net.*;
import java.util.*;
 
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
        System.err.println("Connection to " + uri + " failed.");
    }
}