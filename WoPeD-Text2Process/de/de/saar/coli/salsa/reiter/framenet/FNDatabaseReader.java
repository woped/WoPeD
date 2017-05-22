package de.saar.coli.salsa.reiter.framenet;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.jaxen.JaxenException;
import org.jaxen.dom4j.Dom4jXPath;

/**
 * This class is used to read the original FrameNet data. The original FrameNet
 * data is usually stored in four different files:
 * <ul>
 * <li>frames.xml: The file containing the frame definitions, frame elements,
 * descriptions etc.</li>
 * <li>frRelation.xml: The file containing the links between the frames and
 * frame elements.</li>
 * <li>semtypes.xml: The file containing the semantic type hierarchy</li>
 * <li>framesDiff.xml: The file contains the differences between versions of
 * FrameNet</li>
 * </ul>
 * 
 * This class reads all of them and stores some of the information in the
 * appropriate objects.
 * 
 * @author Nils Reiter
 * @since 0.4
 * 
 */
public class FNDatabaseReader extends DatabaseReader {

	/**
	 * Path and name of the file "frames.xml"
	 */
	List<URI> framesFile = null;

	/**
	 * Path and name of the file "frRelation.xml"
	 */
	List<URI> frRelationFile = null;

	/**
	 * Path and name of the file "framesDiff.xml"
	 */
	List<URI> framesDiffFile = null;
	/**
	 * Path and name of the file "semtypes.xml"
	 */
	List<URI> semtypesFile = null;

	/**
	 * The home of the FrameNet data files.
	 */
	File fnhome = null;

	/**
	 * A set of subdirectories of $FNHOME, in which we search
	 */
	Set<String> xmldirs = null;

	FrameNetFilesNotFoundException exc;

	boolean validate = true;

	private final static long serialVersionUID = 2L;
	
	

	/**
	 * The constructor of the database interface
	 * 
	 * @param fnhome
	 *            The main directory of FrameNet
	 * @param validate
	 *            If set to true, the class will validate the XML files before
	 *            loading them
	 * @throws FileNotFoundException
	 *             If the main FrameNet directory does not exist
	 * @throws SecurityException
	 *             If the main FrameNet directory can not be read
	 */
	public FNDatabaseReader(File fnhome, boolean validate)
			throws FileNotFoundException, SecurityException {
		super();
		init(fnhome, validate, new HashSet<String>());
	}

	/**
	 * This method creates an object specifying the four important files
	 * directly. This way, we can use URIs pointing to remote sites.
	 * 
	 * @param frames
	 *            Location of the file frames.xml
	 * @param frRelation
	 *            Location of the file frRelation.xml
	 * @param framesDiff
	 *            Location of the file framesDiff.xml
	 * @param semtypes
	 *            Location of the file semtypes.xml
	 */
	public FNDatabaseReader(URI frames, URI frRelation, URI framesDiff,
			URI semtypes) {
		fnhome = null;
		validate = false;
		xmldirs = null;

		framesFile = new LinkedList<URI>();
		frRelationFile = new LinkedList<URI>();
		framesDiffFile = new LinkedList<URI>();
		semtypesFile = new LinkedList<URI>();

		framesFile.add(frames);
		frRelationFile.add(frRelation);
		framesDiffFile.add(framesDiff);
		semtypesFile.add(semtypes);
	}

	private void init(File fnhome, boolean validate, Set<String> subdirs)
			throws FileNotFoundException, SecurityException {
		if (!fnhome.exists())
			throw new FileNotFoundException(fnhome.getAbsolutePath()
					+ " does not exist.");
		if (!fnhome.isDirectory())
			throw new FileNotFoundException(fnhome.getAbsolutePath()
					+ " is not a directory.");
		if (!fnhome.canRead())
			throw new SecurityException(fnhome.getAbsolutePath()
					+ " is not readable.");

		this.fnhome = fnhome;
		this.validate = validate;
		xmldirs = subdirs;

		xmldirs.add("");
		xmldirs.add("frXML");
		// Removed for compatibility with framenet-1.4alpha
		// xmldirs.add("frDiffXML");
		xmldirs.add("xml");

		exc = new FrameNetFilesNotFoundException();

		framesFile = new LinkedList<URI>();
		frRelationFile = new LinkedList<URI>();
		framesDiffFile = new LinkedList<URI>();
		semtypesFile = new LinkedList<URI>();

		File frames = initFile("frameIndex.xml");
		if (frames != null)
			framesFile.add(frames.toURI());

		File frRelation = initFile("frRelation.xml");
		if (frRelation != null)
			frRelationFile.add(frRelation.toURI());
		// Removed for compatibility with framenet-1.4alpha
		// framesDiffFile.add(this.initFile("framesDiff.xml").toURI());

		File semtypes = initFile("semTypes.xml");
		if (semtypes != null)
			semtypesFile.add(semtypes.toURI());

		
		
		if (!exc.getNotFound().isEmpty())
			throw exc;
		else
			exc = null;
	}

	private File initFile(String filename){
		for (String xmldirname : xmldirs) {
			File xmldir = new File(fnhome, xmldirname);
			File file = new File(xmldir, filename);
			if (!file.exists())
				continue;
			if (!file.canRead())
				continue;
			if (validate)
				if (!validate(file))
					continue;
			return file;
		}
		exc.notFound(filename);
		return null;
	}

	/**
	 * Returns the semantic types as XML document
	 * 
	 * @return An XML document
	 * @throws ParsingException
	 * @throws FrameNetFilesNotFoundException
	 */
	public Document getSemTypesDocument() throws ParsingException,
			FrameNetFilesNotFoundException {
		return parse(getSemtypesURI());
	}

	/**
	 * Returns the frame relations as XML document
	 * 
	 * @return An XML document
	 * @throws ParsingException
	 * @throws FrameNetFilesNotFoundException
	 */
	public Document getFrRelationDocument() throws ParsingException,
			FrameNetFilesNotFoundException {
		return parse(getFrRelationURI());
	}

	/**
	 * Returns the frames-Index as XML document
	 * 
	 * @return An XML document
	 * @throws ParsingException
	 * @throws FrameNetFilesNotFoundException
	 */
	public Document getFramesDocument() throws ParsingException,
			FrameNetFilesNotFoundException {
		return parse(getFramesURI());
	}

	private Document parse(URI uri) throws ParsingException {

		try {

			SAXReader reader = new SAXReader(false);
			reader.setValidation(false);
						reader.setIncludeExternalDTDDeclarations(false);
			Document document;
			// if (uri.getScheme().equals("file")) {
			document = reader.read(uri.toURL());
			/*
			 * } else { URLConnection conn = uri.toURL().openConnection();
			 * document = reader.read(conn.getInputStream()); }
			 */
			return document;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new ParsingException("Could not parse " + uri.toString());
		}
		return null;
	}

	private boolean validate(File file) {
		try {
			parse(file.toURI());
		} catch (ParsingException e) {
			return false;
		}
		return true;
	}

	/**
	 * Reads all data from the XML files into the FrameNet object.
	 */
	@Override
	protected boolean read(FrameNet fn) {
		try {
			loadAllSemanticTypes(fn);
			loadAllFrames(fn);
			loadAllFrameRelations(fn);
		} catch (FileNotFoundException e) {
			fn.log(Level.SEVERE, e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Called internally to load the frame relations.
	 * 
	 * @throws FileNotFoundException
	 *             If the FrameNet database files can not be found.
	 */
	@SuppressWarnings("unchecked")
	protected void loadAllFrameRelations(FrameNet fn)
			throws FileNotFoundException {
		try {
			Dom4jXPath xpath = new Dom4jXPath("/berk:frameRelations/berk:frameRelationType");
			xpath.setNamespaceContext(fn.getNameSpaceContext());
			List nodes = xpath.selectNodes(getFrRelationDocument());
			for (Object node : nodes) {
				loadFrameRelationNode(fn, (org.dom4j.Element) node);
			}
		} catch (JaxenException e) {
			e.printStackTrace();
		} catch (ParsingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method loads all frames in the frames.xml file. For each frame, a
	 * complete Frame-object is created and stores all its data. It is *much*
	 * faster to run this method before doing any framewise work. So it is
	 * recommended to run this method directly after the start.
	 * 
	 * @return The number of frames loaded from the XML file.
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("unchecked")
	protected int loadAllFrames(FrameNet fn) throws FileNotFoundException {
		try {
			Dom4jXPath xpath = new Dom4jXPath("/berk:frameIndex/berk:frame");
			xpath.setNamespaceContext(fn.getNameSpaceContext());
			List frames = xpath.selectNodes(getFramesDocument());
			for (Object frame : frames) {
				//this is just a part of the index
				Element el = (Element) frame;
				String frameName = el.attributeValue("name"); //corresponds to the file name
				File frameFile = initFile("frame/"+frameName+".xml");
				Document frameDoc = parse(frameFile.toURI());
				loadFrameNode(fn, frameDoc.getRootElement());
			}
			return frames.size();
		} catch (JaxenException e) {
			e.printStackTrace();
		} catch (ParsingException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	protected void loadAllSemanticTypes(FrameNet fn)
			throws FrameNetFilesNotFoundException {
		try {
			Dom4jXPath xpath = new Dom4jXPath("/berk:semTypes/berk:semType");
			xpath.setNamespaceContext(fn.getNameSpaceContext());
			List frames = xpath.selectNodes(getSemTypesDocument());
			for (Object frame : frames) {
				loadSemanticTypeNode(fn, (org.dom4j.Element) frame);
			}
		} catch (JaxenException e) {
			e.printStackTrace();
		} catch (ParsingException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method is exactly like {@link FrameNet#loadAllFrames()}, except that
	 * it takes a Thread object as argument that implements the
	 * {@link IPingable} interface. The method calls the method
	 * {@link IPingable#ping()} for every frame it successfully loads. This
	 * allows the display of progress bar, for instance.
	 * 
	 * 
	 * 
	 * @param thread
	 *            The thread to be pinged
	 * @return
	 * @throws FileNotFoundException
	 *             If the FrameNet files can not be found
	 * @throws XPathExpressionException
	 *             Should not happen
	 */
	/*
	 * public int loadAllFrames(IPingable thread) throws FileNotFoundException,
	 * XPathExpressionException { XPath xpath =
	 * XPathFactory.newInstance().newXPath(); NodeList frames = (NodeList)
	 * xpath.evaluate("/frames/frame", new
	 * InputSource(framesFile.getAbsolutePath()), XPathConstants.NODESET);
	 * thread.setMax(frames.getLength()+1); thread.ping(); for (int i = 0; i <
	 * frames.getLength(); i++ ) { loadFrameNode((Element) frames.item(i));
	 * thread.ping(); } return frames.getLength(); }
	 */
	/**
	 * Loads a frame from a xml node out of the frames.xml file. The method
	 * creates the frame and adds it to the internal cache.
	 * 
	 * @param node
	 *            The node from the XML document.
	 */
	private Frame loadFrameNode(FrameNet frameNet, Node node)
			throws JaxenException {
		if (node.getNodeType() != Node.ELEMENT_NODE)
			return null;
		org.dom4j.Element elem = (org.dom4j.Element) node;
		if (!frameNet.allFrames.containsKey(elem.attributeValue("name"))) {

			Frame f = new FNFrame(elem, frameNet);

			frameNet.allFrames.put(f.getName(), f);
			frameNet
					.log(Level.FINEST, "Framenode loaded. Frame " + f.getName());
			node = null;
			return f;

		}
		return null;
	}

	private FrameNetRelation loadFrameRelationNode(FrameNet frameNet,
			Element node) throws JaxenException {
		FrameNetRelation frel = new FNFrameNetRelation(frameNet, node);
		frameNet.frameRelations.put(frel.getName(), frel);
		return frel;
	}

	private void loadSemanticTypeNode(FrameNet frameNet, Element node)
			throws JaxenException {
		if (frameNet.semanticTypeIndex != null) {
			for (SemanticType st : frameNet.semanticTypeIndex) {
				if (st.getName().equalsIgnoreCase(node.attributeValue("name"))) {
					// st.supplyNode(node);
					return;
				}
			}
		} else {
			frameNet.semanticTypeIndex = new HashSet<SemanticType>();
		}
		if (frameNet.semanticTypeIndex == null)
			frameNet.semanticTypeIndex = new HashSet<SemanticType>();
		SemanticType st = new FNSemanticType(frameNet, node);
		frameNet.semanticTypeIndex.add(st);
		frameNet.log(Level.INFO, "SemanticTypeNode loaded. Semantic Type: "
				+ st.getName());
	}

	/**
	 * Returns a URI for the frames diff file
	 * 
	 * @return the framesDiffFile
	 */
	public URI getFramesDiffURI() {
		return framesDiffFile.get(0);
	}

	/**
	 * Returns a URI for the frames file
	 * 
	 * @return the framesFile
	 */
	public URI getFramesURI() {
		return framesFile.get(0);
	}

	/**
	 * Returns a URI for the frame relation file
	 * 
	 * @return the frRelationFile
	 */
	public URI getFrRelationURI() {
		return frRelationFile.get(0);
	}

	/**
	 * Returns a URI for the semantic types file
	 * 
	 * @return the semtypesFile
	 */
	public URI getSemtypesURI() {
		return semtypesFile.get(0);
	};

	/**
	 * Adds a subdirectory to the list, such that we search also in
	 * <code>dir</code> for the XML data files
	 * 
	 * @param dir
	 *            The new directory
	 */
	public void addSubDirectory(String dir) {
		xmldirs.add(dir);
	}

	/**
	 * Manually sets the file framesDiff.xml
	 * 
	 * @param framesDiffFile
	 *            The file
	 */
	public void addFramesDiffFile(File framesDiffFile) {
		this.framesDiffFile.add(framesDiffFile.toURI());
	}

	/**
	 * Manually sets the file frames.xml
	 * 
	 * @param framesFile
	 *            The file
	 */
	public void addFramesFile(File framesFile) {
		this.framesFile.add(framesFile.toURI());
	}

	/**
	 * Manually sets the file frRelation.xml
	 * 
	 * @param frRelationFile
	 *            The file
	 */
	public void addFrRelationFile(File frRelationFile) {
		this.frRelationFile.add(frRelationFile.toURI());
	}

	/**
	 * Manually sets the file semtypes.xml
	 * 
	 * @param semtypesFile
	 *            The file
	 */
	public void addSemtypesFile(File semtypesFile) {
		this.semtypesFile.add(semtypesFile.toURI());
	}

	@Override
	protected void cleanup() {
		exc = null;
		System.gc();
	}
}
