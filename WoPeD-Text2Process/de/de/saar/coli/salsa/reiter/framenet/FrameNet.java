/**
 * 
 * Copyright 2007-2010 by Nils Reiter.
 * 
 * This FrameNet API is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3.
 *
 * This FrameNet API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this FrameNet API.  If not, see www.gnu.org/licenses/gpl.html.
 * 
 */
package de.saar.coli.salsa.reiter.framenet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jaxen.SimpleNamespaceContext;

/**
 * The basic class representing the FrameNet database. It allows access to the
 * frames, the frame relations, the frame elements, lexical units and semantic
 * types.
 * 
 * <h2>Usage</h2>
 * 
 * <pre>
 * // Create a new FrameNet object
 * FrameNet fn = new FrameNet();
 * 
 * // Load the FrameNet data
 * DatabaseReader reader = new FNDatabaseReader(...);
 * fn.readData(reader);
 * 
 * 
 * // Iterate over all frames
 * for (Frame frame : fn.allFrames()) {
 * 
 *     // Print out the name of each frame
 *     System.out.println(frame.getName());
 *     
 *     // Iterate over all frame elements of the frame
 *     for (FrameElement fe : frame.getFrameElements()) {
 *     
 *        // Print out the name and semantic types of the frame element
 *        System.out.println("  " + fe.getName() + " / " + fe.getSemanticTypes());
 *     }
 *     
 *     
 *     System.out.println(" This frame uses the frames: ");
 *     
 *     // Iterate over all frames that are used by this frame
 *     for (Frame uses : frame.uses()) {
 *        
 *        // Print out their names
 *     	  System.out.println("  " + uses.getName());
 *     }
 *     
 * 
 * }
 * </pre>
 * 
 * @author Nils Reiter
 * @version 0.4
 * 
 */
public class FrameNet implements Serializable {

	/**
	 * A string to store information about the version of the FrameNet data.
	 * This information can not be retrieved from the database files, but if it
	 * is provided, the object can control that the cache version is the right
	 * one.
	 */
	String frameNetVersion;

	/**
	 * A map of all frames, indexed by their names.
	 */
	Map<String, Frame> allFrames = null;

	/**
	 * A map of all frame relations. They are indexed by the name of the
	 * relation as found in the XML data file.
	 */
	Map<String, FrameNetRelation> frameRelations = null;

	/**
	 * The set of root frames. Root frames are the frames that do not inherit
	 * from any other frame.
	 */
	Set<Frame> rootFrames = null;

	private static final long serialVersionUID = 17L;

	/**
	 * An index of all lexical units defined in FrameNet
	 */
	Map<Integer, LexicalUnit> luIndex = null;

	/**
	 * 
	 */
	Collection<SemanticType> semanticTypeIndex = null;

	public static DateFormat dateFormat = new SimpleDateFormat(
			"MM/dd/yyyy HH:mm:ss z EEE",Locale.US);

	transient Logger logger;
	

	private SimpleNamespaceContext simpleNSC;

	/**
	 * Creates a new FrameNet object. This constructor takes an optional
	 * {@link java.util.Logger} as parameter, which is then used for logging
	 * purposes.
	 * 
	 * @param logger
	 *            The logger
	 */
	public FrameNet(Logger logger) {
		super();
		init(logger);
	}

	/**
	 * Creates a new FrameNet object
	 */
	public FrameNet() {
		super();
		init(null);
	}

	private void init(Logger logger) {
		this.logger = logger;

		flush();

		
		//create a namespace mapping for the default namespace
		HashMap<String, String> namespaceMapping = new HashMap<String, String>();
		namespaceMapping.put("berk", "http://framenet.icsi.berkeley.edu");
		simpleNSC = new SimpleNamespaceContext(namespaceMapping);
		// this.dbInterface = new FrameNetDatabaseInterface(fnhome, false,
		// this.getLogger(), new HashSet<String>());

	};
	
	public SimpleNamespaceContext getNameSpaceContext() {
		return simpleNSC;
	}

	/**
	 * Cleans the complete frame database by emptying all indices. After this
	 * method has been run, all frames, frame elements, frame relations, lexical
	 * units and semantic types are gone.
	 * 
	 * @since 0.4
	 */
	public void flush() {
		allFrames = new HashMap<String, Frame>();
		frameRelations = new HashMap<String, FrameNetRelation>();
		luIndex = new HashMap<Integer, LexicalUnit>();
		semanticTypeIndex = new HashSet<SemanticType>();
	}

	/**
	 * This method reads the data from the given database reader. If the read
	 * went successful, the method returns true.
	 * 
	 * @param reader
	 *            The database reader. Currently, either FNDatabaseReader or
	 *            STXDatabaseReader)
	 * @return true if the reading went well. false otherwise
	 */
	public boolean readData(DatabaseReader reader) {
		boolean b = reader.read(this);
		if (b)
			reader.cleanup();
		return b;

	}

	/**
	 * This method is used to get a FrameNet object. The method reads its data
	 * from the environment variable FNHOME, creates a new cache file and
	 * assumes FrameNet version 1.3. It does not display any debug information.
	 * This is a shorthand version for
	 * {@link FrameNet#getInstance(File, String, Logger)}.
	 * 
	 * @return Returns a FrameNet object.
	 * @deprecated Use {@link #FrameNet()} instead.
	 */
	@Deprecated
	public static FrameNet getInstance(){
		Logger logger = Logger.getAnonymousLogger();
		logger.addHandler(new ConsoleHandler());
		if (System.getenv("FNHOME") == null)
			return null;
		FrameNet fn = FrameNet.getInstance(new File(System.getenv("FNHOME")),

		"1.3", logger);
		return fn;
	}

	/**
	 * See {@link FrameNet#getInstance(File, String, Logger)}. This method is
	 * exactly the same, except expecting String arguments.
	 * 
	 * @param fnhome
	 *            The absolute path to the FrameNet home directory
	 * @param version
	 *            The FrameNet version
	 * @return The method returns a FrameNet object
	 * @deprecated Use {@link #FrameNet()} instead.
	 */
	@Deprecated
	public static FrameNet getInstance(String fnhome, String version){
		Logger logger = Logger.getAnonymousLogger();
		logger.addHandler(new ConsoleHandler());
		return FrameNet.getInstance(new File(fnhome), version, logger);
	}

	/**
	 * 
	 * @param fnhome
	 * @param version
	 * @param debug
	 * @return A FrameNet instance
	 * @throws FrameNetFilesNotFoundException
	 * @throws IOException
	 * @deprecated Use {@link #FrameNet()} instead.
	 */
	@Deprecated
	public static FrameNet getInstance(String fnhome, String version,
			boolean debug) throws FrameNetFilesNotFoundException, IOException {
		Logger logger = Logger.getAnonymousLogger();
		logger.addHandler(new ConsoleHandler());
		if (debug)
			logger.setLevel(Level.FINE);
		else
			logger.setLevel(Level.SEVERE);

		return FrameNet.getInstance(new File(fnhome), version, logger);
	}

	/**
	 * This method returns a new FrameNet object. It is built from the cache or,
	 * if that is not possible, from the files in fnhome.
	 * 
	 * @param fnhome
	 *            The home directory of the FrameNet files, e.g.,
	 *            /usr/local/FrameNet
	 * @param version
	 *            The FrameNet version
	 * @param logger
	 *            A Logger object to retrieve log messages
	 * @return Returns a FrameNet object
	 * @deprecated Use {@link #FrameNet()} instead.
	 */
	@Deprecated
	public static FrameNet getInstance(File fnhome, String version,
			Logger logger){
		if (logger != null)
			logger.entering("de.saar.coli.salsa.reiter.framenet.FrameNet",
					"getInstance");

		FrameNet fn = new FrameNet(logger);

		fn.setFrameNetVersion(version);
		return fn;
	}

	/**
	 * This method returns the given frame element as FrameElement object.
	 * 
	 * 
	 * @param name
	 *            The name of the frame element. It has to be fully qualified,
	 *            i.e., be of the form <code>frame.frame-element</code>. For
	 *            instance: <code>Attack.Victim</code>
	 * @return The frame element
	 * @throws ParsingException
	 *             If the name could not be parsed
	 * @throws FrameElementNotFoundException
	 *             If the frame element does not belong to the given frame
	 * @throws FrameNotFoundException
	 *             If the frame does not exist in the database
	 */
	public FrameElement getFrameElement(String name) throws ParsingException,
			FrameElementNotFoundException, FrameNotFoundException {
		String[] parts = name.split("\\.");
		if (parts.length == 2) {
			Frame f = this.getFrame(parts[0]);
			FrameElement fe = f.getFrameElement(parts[1]);
			if (fe != null)
				return fe;

		}
		throw new ParsingException(name + " could not be parsed.");
	}

	/**
	 * Returns the given frame as a Frame object.
	 * 
	 * @param name
	 *            The name of the frame (case sensitive!)
	 * @return The frame
	 * @throws FileNotFoundException
	 *             If the FrameNet files are not found.
	 * @throws FrameNotFoundException
	 *             If the frame does not exist in the FrameNet database
	 */
	public Frame getFrame(String name) throws FrameNotFoundException {
		return getFrame(name, true);
	}

	/**
	 * This method is used to create and get a Frame. The frame is identified by
	 * its name. The use of this method ensures, that every frame exists only
	 * once in the memory.
	 * 
	 * @param name
	 * @param follow 
	 * @throws FrameNotFoundException
	 *             If the frame does not exist in the FrameNet database.
	 * @return A frame object representing the frame.
	 * @see Frame
	 */
	private Frame getFrame(String name, boolean follow)
			throws FrameNotFoundException {
		Frame frame = allFrames.get(name);
		if (frame == null)
			throw new FrameNotFoundException(name);
		return frame;
	}

	/**
	 * All frames that are defined in the FrameNet database.
	 * 
	 * @return a collection of all frames in the database.
	 * @deprecated Use {@link #getFrames()} instead.
	 */
	@Deprecated
	public Collection<Frame> allFrames() {
		return getFrames();
	}

	/**
	 * All frames that are defined in the FrameNet database.
	 * 
	 * @return a collection of all frames in the database.
	 * @since 0.3.1
	 */
	public Collection<Frame> getFrames() {
		return allFrames.values();
	}

	/**
	 * Collects the root frames in the FrameNet database. A frame is a root
	 * frame, if no frame exists that is inherited.
	 * 
	 * @return A collection of root frames.
	 */
	public Collection<Frame> getRootFrames() {

		if (rootFrames == null) {
			rootFrames = new HashSet<Frame>();

			for (Frame f : getFrames()) {
				if (f.isRootFrame()) {
					rootFrames.add(f);
					log(Level.FINE, f + " is a root frame");
				}

			}

		}
		return rootFrames;
	}

	/**
	 * This method returns the version of the FN database. This is basically the
	 * number once provided with the method {@link #setFrameNetVersion(String)}
	 * Unfortunately, there is no way of detecting the FrameNet version directly
	 * inside the database files.
	 * 
	 * @return The version of the FrameNet database
	 * @deprecated Use {@link FrameNet#getFrameNetVersion()} instead.
	 */
	@Deprecated
	public String getVersion() {
		return frameNetVersion;
	}

	/**
	 * Destructor. Tries to save the object in a cache.
	 */
	@Override
	protected void finalize() {
		try {

		} catch (Exception e) {

		}
	}

	/**
	 * This method returns the version of the FN database. This is basically the
	 * number once provided with the method {@link #setFrameNetVersion(String)}
	 * Unfortunately, there is no way of detecting the FrameNet version directly
	 * inside the database files.
	 * 
	 * @return the frameNetVersion
	 */
	public String getFrameNetVersion() {
		return frameNetVersion;
	}

	/**
	 * Collects all frame relations that are defined in the FrameNet XML
	 * database.
	 * 
	 * @return A collection of strings, denoting the frame relations.
	 */
	public Collection<FrameNetRelation> getFrameNetRelations() {
		// this.loadAllFrameRelations();
		return frameRelations.values();
	}

	/**
	 * Returns the given frame relation as a FrameNetRelation object.
	 * 
	 * @param name
	 *            The name of the relation (case sensitive)
	 * @return A FrameNetRelation object or null if the relation does not exist
	 */
	public FrameNetRelation getFrameNetRelation(String name) {
		return frameRelations.get(name);
	}

	/*
	 * private void writeObject(java.io.ObjectOutputStream out) throws
	 * IOException {
	 * 
	 * }
	 */

	/**
	 * Used to access the FrameNet database directly from the commandline.
	 * 
	 * The parameters -frameElements, -lexicalUnits, -relations and -definitions
	 * can be specified and show the corresponding data. Any number of frame
	 * names can be given after the parameters. For example: <code>
	 * java -Xmx1024M -jar FrameNet.jar -lexicalUnits Communication
	 * </code> shows the
	 * lexical units of the Communication frame.
	 * 
	 * @param args
	 *            The parameters and names of the frames.
	 */
	public static void main(String[] args) {
		String version = "1.3";
		String fnhome = System.getenv("FNHOME");

		Logger logger = Logger.getAnonymousLogger();
		logger.addHandler(new ConsoleHandler());
		logger.setUseParentHandlers(false);

		try {
			boolean fes = false;
			boolean cfes = false;
			boolean nfes = false;
			boolean lus = false;
			boolean relations = false;
			boolean definitions = false;
			boolean list = false;
			boolean treeUp = false;
			boolean treeDown = false;
			boolean help = false;

			Set<String> frames = new HashSet<String>();
			Set<String> frameElements = new HashSet<String>();

			for (String arg : args) {
				if (arg.startsWith("-")) {
					if (arg.equalsIgnoreCase("-frameElements"))
						fes = true;
					else if (arg.equalsIgnoreCase("-coreFrameElements"))
						cfes = true;
					else if (arg.equalsIgnoreCase("-newFrameElements"))
						nfes = true;
					else if (arg.equalsIgnoreCase("-lexicalUnits"))
						lus = true;
					else if (arg.equalsIgnoreCase("-relations"))
						relations = true;
					else if (arg.equalsIgnoreCase("-definitions"))
						definitions = true;
					else if (arg.equalsIgnoreCase("-list"))
						list = true;
					else if (arg.equalsIgnoreCase("-treeUp"))
						treeUp = true;
					else if (arg.equalsIgnoreCase("-treeDown"))
						treeDown = true;
					else if (arg.equalsIgnoreCase("-help"))
						help = true;
					else if (arg.startsWith("-cache")) {
						// String[] cachearg = arg.split("=");
						// cache = new File(cachearg[1]);
					} else if (arg.startsWith("-version")) {
						String[] versionarg = arg.split("=");
						version = versionarg[1];
					} else if (arg.startsWith("-fnhome")) {
						String[] fnhomearg = arg.split("=");
						fnhome = fnhomearg[1];
					} else {
						logger.warning("Unrecognized Option: " + arg);
					}
				} else if (arg.contains(".")) {
					frameElements.add(arg);
				} else {
					frames.add(arg);
				}
			}
			if (help) {
				System.out
						.println("FrameNet API 0.4.1 command line access tool");
				System.out.println();
				System.out.println("SYNOPSIS");
				System.out.println();
				System.out
						.println("   java de.saar.coli.salsa.reiter.framenet.FrameNet [Options] [Frames] [Frame Elements]");
				System.out.println();
				System.out.println("OPTIONS");
				System.out.println();
				System.out
						.println("  -frameElements: Shows the frame elements of a frame");
				System.out
						.println("  -coreFrameElements: Shows the core frame elements of a frame");
				System.out
						.println("  -newFrameElements: Shows the frame elements new in this frame");
				System.out
						.println("  -lexicalUnits: Shows the lexical units of a frame");
				System.out
						.println("  -relations: Shows the related frames of a frame");
				System.out
						.println("  -definitions: Shows the definition of a frame");
				System.out.println("  -debug: Prints out debug output");
				System.out
						.println("  -list: Shows a list of all frames and exits");
				System.out
						.println("  -treeUp: Shows a tree of inherited frames of a frame");
				System.out
						.println("  -treeDown: Shows a tree of inheriting frames of a frame");
				System.out.println("  -help: Prints this help and exits");
				// System.out.println("  -cache: Sets the program to use an existing cache file");
				System.out
						.println("  -fnhome=<FNHOME>: Sets the directory where FrameNet is located");
				System.out
						.println("  -version=<N>: Sets the FrameNet version.");

				System.exit(0);
			}

			FrameNet frameNet = new FrameNet();// .getInstance(new
			// File(System.getenv("FNHOME")),
			frameNet.setFrameNetVersion(version);
			DatabaseReader reader = new FNDatabaseReader(new File(fnhome),
					false);
			reader.read(frameNet);

			if (list) {
				for (Frame f : frameNet.getFrames()) {
					System.out.println(f.getName());
				}
				System.exit(0);
			}

			for (String arg : frames) {
				Frame frame = frameNet.getFrame(arg);
				System.out.println("Frame " + frame);
				if (fes) {
					System.out.println("  Frame Elements: "
							+ frame.getFrameElements().keySet());
				}
				if (cfes) {
					System.out.print("  Core Frame Elements: [");
					boolean first = true;
					for (FrameElement fe : frame.frameElements()) {
						if (fe.getCoreType() == CoreType.Core) {
							if (!first)
								System.out.print(", ");
							if (first)
								first = false;
							System.out.print(fe.getName());
						}
					}
					System.out.println("]");
				}
				if (nfes) {
					System.out.print("  New Frame Elements: [");
					boolean first = true;
					for (FrameElement fe : frame.frameElements()) {
						if (fe.isRootFrameElement()) {
							if (!first)
								System.out.print(", ");
							if (first)
								first = false;
							System.out.print(fe.getName());
						}
					}
					System.out.println("]");
				}
				if (lus) {
					System.out.println("  Lexical Units: "
							+ frame.getLexicalUnits());
				}
				if (definitions) {
					System.out
							.println("  Definition: " + frame.getDefinition());
				}
				if (relations) {
					System.out.println("  Frame Relations: ");
					for (FrameNetRelation frel : frameNet
							.getFrameNetRelations()) {
						System.out.println("   " + frel.getName() + " "
								+ frel.getSuperName() + ": "
								+ frel.getSuper(frame));
						System.out
								.println("   " + frel.getName() + " "
										+ frel.getSubName() + ": "
										+ frel.getSub(frame));
					}
				}
				if (treeUp)
					System.out.println(frame.treeUp());
				if (treeDown)
					System.out.println(frame.treeDown());
				System.out.println();
			}
			;

			for (String s : frameElements) {
				FrameElement fe = FrameElement.getFromString(s, frameNet);
				if (treeUp)
					System.out.println(fe.treeUp());
				if (treeDown)
					System.out.println(fe.treeDown());
			}

			frameNet.finalize();

		} catch (Exception e) {
			logger.severe(e.toString() + ": " + e.getMessage());
		}

	}

	/**
	 * Searches the lexical units defined in the FN database for the given
	 * lemma. The lemma can be given as a regular expression. See
	 * {@link FrameNet#getLexicalUnits(String, String)} for details, since this
	 * method is only a shorthand version.
	 * 
	 * @param lemma
	 *            The lemma to search
	 * @return A list of matching lexical units
	 */
	public Collection<LexicalUnit> getLexicalUnits(String lemma) {
		return this.getLexicalUnits(lemma, ".*");
	}

	/**
	 * This methods searches the lexical units defined in the FN database for
	 * the given lemma and part of speech (pos). Both can be given as regular
	 * expressions, since they are internally mapped using
	 * {@link java.lang.String#matches(String)}. Note, that the pos-pattern will
	 * be mapped against the abbreviation of the part of speech of the lexical
	 * units (the string returned by
	 * {@link LexicalUnit#getPartOfSpeechAbbreviation()}.
	 * 
	 * 
	 * @param lemma
	 *            The lemma to search
	 * @param pos
	 *            The part of speech
	 * @return A list of matching lexical units
	 */
	public Collection<LexicalUnit> getLexicalUnits(String lemma, String pos) {
		Collection<LexicalUnit> ret = new HashSet<LexicalUnit>();
		for (LexicalUnit lu : this.getLexicalUnits()) {
			if (lu.getName().matches(lemma + "\\." + pos)) {
				ret.add(lu);
			}
		}
		return ret;
	}

	/**
	 * Returns the lexical unit with the given id. Returns null if the LU does
	 * not exist.
	 * 
	 * @param id
	 *            The XML id of the lexical unit
	 * @return the lexical unit or null
	 */
	public LexicalUnit getLexicalUnit(Integer id) {
		return luIndex.get(id);
	}

	/**
	 * Returns the lexical unit with the given id. Returns null if the LU does
	 * not exist.
	 * 
	 * @param id
	 *            The XML id of the lexical unit
	 * @return the lexical unit or null
	 * @deprecated
	 */
	@Deprecated
	public LexicalUnit getLexicalUnit(String id) {
		return luIndex.get(Integer.valueOf(id));
	}

	/**
	 * Gets a semantic type.
	 * 
	 * @param nameOrId
	 *            Can be either the id or the name
	 * @return A semantic type
	 * @throws SemanticTypeNotFoundException
	 *             If the semantic type does not exist
	 */
	public SemanticType getSemanticType(String nameOrId)
			throws SemanticTypeNotFoundException {
		SemanticType st = this.getSemanticType(nameOrId, false);
		if (st == null)
			throw new SemanticTypeNotFoundException(
					"The semantic type with the name or id " + nameOrId
							+ " does not exist.");
		return st;
	}

	protected SemanticType getSemanticType(String nameOrId, boolean create) {
		for (SemanticType st : semanticTypeIndex) {
			if (st.getName().equalsIgnoreCase(nameOrId)
					|| st.getId().equalsIgnoreCase(nameOrId))
				return st;
		}
		if (create) {
			SemanticType st;
			if (nameOrId.matches("^\\d+$")) {
				st = new TempSemanticType(this, "", nameOrId);
			} else {
				st = new TempSemanticType(this, nameOrId, "");
			}
			semanticTypeIndex.add(st);
			return st;
		}
		return null;
	}

	/**
	 * Unfortunately, the actual framenet version cannot be extracted from the
	 * data files directly. If you want to set it, you should use this method.
	 * 
	 * @param frameNetVersion
	 *            the frameNetVersion to set
	 */
	public void setFrameNetVersion(String frameNetVersion) {
		this.frameNetVersion = frameNetVersion;
	}

	/**
	 * Returns a collection of all lexical units
	 * 
	 * @return the luIndex
	 */
	public Collection<LexicalUnit> getLexicalUnits() {
		return luIndex.values();
	}

	protected void addLexicalUnit(LexicalUnit lu) {
		luIndex.put(lu.getIntegerId(), lu);
	}

	/**
	 * Returns a list containing all semantic types defined in this vesion of
	 * FrameNet
	 * 
	 * @return the semanticTypeIndex
	 */
	public Collection<SemanticType> getAllSemanticTypes() {
		return semanticTypeIndex;
	}

	/**
	 * Gets the logger
	 * 
	 * @return The logger of the object
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * Sets the logger
	 * 
	 * @param logger
	 *            The new logger
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	protected void log(Level level, String message) {
		if (logger != null)
			logger.log(level, message);
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		// out.defaultWriteObject();
		out.writeObject(frameNetVersion);
		out.writeObject(allFrames);
		// out.writeObject(this.dbInterface);
		out.writeObject(frameRelations);
		out.writeObject(luIndex);
		out.writeObject(rootFrames);
		out.writeObject(semanticTypeIndex);
	}

	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {

		frameNetVersion = (String) in.readObject();
		Map allFrames = (Map) in.readObject();
		// this.dbInterface = (FrameNetDatabaseInterface) in.readObject();
		Map frameRelations = (Map) in.readObject();
		Collection luIndex = (Collection) in.readObject();
		Collection rootFrames = (Collection) in.readObject();
		Collection semanticTypeIndex = (Collection) in.readObject();

		this.allFrames = new HashMap<String, Frame>();
		this.frameRelations = new HashMap<String, FrameNetRelation>();
		this.luIndex = new HashMap<Integer, LexicalUnit>();
		this.rootFrames = new HashSet<Frame>();
		this.semanticTypeIndex = new HashSet<SemanticType>();

		for (Object key : allFrames.keySet()) {
			this.allFrames.put((String) key, (Frame) allFrames.get(key));
		}
		for (Object key : frameRelations.keySet()) {
			this.frameRelations.put((String) key,
					(FrameNetRelation) frameRelations.get(key));
		}
		for (Object key : luIndex) {
			this.luIndex.put(((LexicalUnit) key).getIntegerId(),
					(LexicalUnit) key);
		}
		for (Object key : rootFrames) {
			this.rootFrames.add((Frame) key);
		}
		for (Object key : semanticTypeIndex) {
			this.semanticTypeIndex.add((SemanticType) key);
		}
	}

}
