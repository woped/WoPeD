package de.saar.coli.salsa.reiter.framenet;

/**
 * This class is the abstract base class for all methods to read Frame data
 * (not: annotated data) into the frame database.
 * 
 * @author Nils Reiter
 * @since 0.4
 */
public abstract class DatabaseReader {

	/**
	 * Every class providing a data reading technique must implement this
	 * method. It reads the data in and stores them in the given FrameNet
	 * object.
	 * 
	 * @param fn
	 *            The FrameNet object
	 * @return Returns true if the reading went well, returns false if an error
	 *         occurred.
	 */
	protected abstract boolean read(FrameNet fn);

	/**
	 * After the reading, some cleanup might be necessary.
	 */
	protected abstract void cleanup();
}
