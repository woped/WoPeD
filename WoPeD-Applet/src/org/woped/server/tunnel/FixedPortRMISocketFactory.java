package org.woped.server.tunnel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMISocketFactory;

public class FixedPortRMISocketFactory extends RMISocketFactory {

/**
* Creates a client socket connected to the specified host and port
* @param host  the host name
* @param port  the port number
* @return a socket connected to the specified host and port.
* @exception IOException if an I/O error occurs during socket creation
*/
public Socket createSocket(String host, int port) throws IOException 
{
	System.out.println("creating socket to host : " + host + " on port " + port);
	return new Socket(host, port);
}
 
/**
* Create a server socket on the specified port (port 0 indicates
* an anonymous port) and writes out some debugging info
* @param port  the port number
* @return  the server socket on the specified port
* @exception IOException  if an I/O error occurs during server socket
* creation
*/
public ServerSocket createServerSocket(int port) throws IOException
{
	port = (port == 0 ? 1098 : port);
	System.out.println("creating ServerSocket on port " + port);
	return new ServerSocket(port);
}

}
