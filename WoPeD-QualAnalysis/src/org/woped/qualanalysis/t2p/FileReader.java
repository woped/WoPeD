package org.woped.qualanalysis.t2p;

public interface FileReader {
	String read() throws NoFileException;
	class NoFileException extends Exception {}
}
