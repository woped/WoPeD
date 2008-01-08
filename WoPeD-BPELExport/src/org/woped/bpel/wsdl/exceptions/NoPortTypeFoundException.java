package org.woped.bpel.wsdl.exceptions;

public class NoPortTypeFoundException extends Exception {

	private static final long	serialVersionUID	= 8747675171865072342L;

	public NoPortTypeFoundException(){
		super ("There isn't a Port Type with the exported name.");
	}
}
