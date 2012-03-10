package org.woped.editor.controller.wsdl;

public class NoMessageFoundException extends Exception {

	private static final long	serialVersionUID	= -7440604457708551075L;

	public NoMessageFoundException(){
		super ("There isn't a Message with the exported name.");
	}

}
