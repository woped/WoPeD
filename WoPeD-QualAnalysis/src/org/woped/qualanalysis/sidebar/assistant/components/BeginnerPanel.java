package org.woped.qualanalysis.sidebar.assistant.components;

import org.woped.core.config.DefaultStaticConfiguration;
import org.woped.core.controller.IEditor;
import org.woped.core.model.ArcModel;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.service.IQualanalysisService;
import org.woped.qualanalysis.sidebar.SideBar;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

@SuppressWarnings("serial")
public abstract class BeginnerPanel extends JPanel implements MouseListener {

	// Prefix for message-String
	protected static final String PREFIX = "AnalysisSideBar.";
	protected static final String PREFIX_BEGINNER = PREFIX + "Beginner.";
	protected static final String PREFIX_HELP = PREFIX_BEGINNER + "Help.";
	protected static final String PREFIX_EXAMPLE = PREFIX_BEGINNER + "Example.";
	protected static final String PREFIX_BUTTON = PREFIX_BEGINNER + "Button.";

	protected static final String COLON = ":";

	// Constants for Fonts and Icons
	protected static final Font HEADER_FONT = DefaultStaticConfiguration.DEFAULT_LABEL_BOLDFONT;

	protected static final Font SUBHEADER_FONT = DefaultStaticConfiguration.DEFAULT_LABEL_FONT;

	protected static final Font ITEMS_FONT = DefaultStaticConfiguration.DEFAULT_SMALLLABEL_FONT;

	protected static final Font HELP_TEXT_FONT = DefaultStaticConfiguration.DEFAULT_TINYLABEL_FONT;;

	// String for Details - Icon
	protected static final String DETAILS_ICON = PREFIX_BUTTON + "Details";

	// String for Icon if status is correct (green)
	protected static final String CORRECT_ICON = PREFIX_BUTTON + "Correct";

	// String for Icon if status is incorrect (red)
	protected static final String INCORRECT_ICON = PREFIX_BUTTON + "Incorrect";

	// String for Warning Icon (orange)
	protected static final String WARNING_ICON = PREFIX_BUTTON + "Warning";

	// String for Icon with questionmark (blue-white)
	protected static final String HELP_ICON = PREFIX_BUTTON + "Help";

	protected static final String SUB_POINT = " - ";

	// String for new home icon (navigation)
	protected static final String NAV_HOME = "QuanlAna.Navigation.Home";

	// String for first details button (navigation)
	protected static final String NAV_DETAILS1 = "QuanlAna.Navigation.Details1";

	// String for second details button (navigation)
	protected static final String NAV_DETAILS2 = "QuanlAna.Navigation.Details2";

	private static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);

	private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);

	private static int bottomGap = 10;

	private static int rightGap = 15;

	protected static final Border BOTTOM_BORDER = BorderFactory
			.createEmptyBorder(0, 0, bottomGap, 0);

	protected static final Border BOTTOM_RIGHT_BORDER = BorderFactory
			.createEmptyBorder(0, 0, bottomGap, rightGap);

	private BeginnerPanel previous = null;

	private JLabel back = null;

	private JLabel startPage = null;

	protected JPanel contentPane = null;

	private SideBar sideBar = null;

	public IEditor editor = null;

	protected boolean status = true;

	protected IQualanalysisService qualanalysisService = null;

	// adaption
	// (Saskia Kurz, Dec 2011)
	public JPanel jp;
	public JToggleButton j1, j2, j3;
	BeginnerPanel helpPanel;
	BeginnerPanel helpPanel2;

	/**
	 * 
	 * @param previous
	 *            - reference to the previous page for back button
	 * @param sideBar
	 *            - reference to the sidebar
	 * @param header
	 *            - String for the header of the page
	 */
	public BeginnerPanel(BeginnerPanel previous, SideBar sideBar, String header) {
		this.sideBar = sideBar;
		this.editor = sideBar.getEditor();
		qualanalysisService = sideBar.getQualanalysisService();
		this.setLayout(new BorderLayout());
		this.previous = previous;
		this.contentPane = new JPanel();
		// create a scrollpane for vertical scrolling
		JScrollPane scrollPane = new JScrollPane(contentPane);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(null);
		this.contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(BorderFactory.createEmptyBorder(25, 5, 0, 20));
		this.contentPane.setLayout(new GridBagLayout());

		// start of adaption
		// (Saskia Kurz, Dec 2011/Feb 2012)
		
		JLabel headLabel = new JLabel(header, JLabel.LEFT);
		headLabel.setFont(HEADER_FONT);
		this.add(headLabel, BorderLayout.NORTH);
		
		// JPanel which is meant to contain the three toggle buttons
		jp = new JPanel();
		jp.setLayout(new GridLayout(1, 3));
		
		// toggle buttons for navigation
		j1 = new JToggleButton(Messages.getImageIcon(NAV_HOME));
		j1.setBackground(Color.WHITE);
		j2 = new JToggleButton(Messages.getImageIcon(NAV_DETAILS1));
		j2.setBackground(Color.WHITE);
		j3 = new JToggleButton(Messages.getImageIcon(NAV_DETAILS2));
		j3.setBackground(Color.WHITE);
		
		
		// initial selection status of the toggle buttons
		j1.setSelected(true);
		j2.setEnabled(false);
		j3.setEnabled(false);

		// add toggle buttons to JPanel
		jp.add(j1);
		jp.add(j2);
		jp.add(j3);
		
		// creation registration of listener for toggle buttons
		ToggleListener tl = new ToggleListener(this);
		j1.addItemListener(tl);
		j2.addItemListener(tl);
		j3.addItemListener(tl);
		
		// add JPanel with toggle buttons to sidebar
		this.add(jp, BorderLayout.SOUTH);

		// end of adaption

		this.add(scrollPane, BorderLayout.CENTER);
	}

	// start of adaption
	// Saskia Kurz, Jan 2012
	
	// Listener class for toggle buttons
	class ToggleListener implements ItemListener {
		
		BeginnerPanel bp;
		
		// constructor
		public ToggleListener(BeginnerPanel bp){
			
			this.bp = bp;
			
		}

		public void itemStateChanged(ItemEvent ie) {
		
			boolean changed = false;

			helpPanel = previous;
			
			//click on first button (Home)
			if (((JToggleButton) ie.getSource()) == j1) {
				if (ie.getStateChange() == ItemEvent.SELECTED) {
					
					j2.setSelected(false);
					j3.setSelected(false);
					
					// navigate to Start Page
					while (helpPanel != null && helpPanel.hasPrevious()) {
						helpPanel = helpPanel.getPrevious();
					}
					j2.setEnabled(false);
					j3.setEnabled(false);
					
					changed = true;
				}
				
			//click on second button (Details1)
			} else if (((JToggleButton) ie.getSource()) == j2) {
				if (ie.getStateChange() == ItemEvent.SELECTED) {
					
					if(!bp.getClass().toString().endsWith("SoundnessPage") &&
							!bp.getClass().toString().endsWith("StartPage")){
						j1.setSelected(false);
						j3.setSelected(false);
						j3.setEnabled(false);
						changed = true;
					}
				}
				
			/* clicks on third button (Details2) do not have to be handled!
			 * the button is only needed for visualization purposes!
			 */
				
			}

			/* not every click on the buttons should trigger a repaint of the sidebar
			 * only if the change flag is set, repaint will take place
			 * otherwise the following piece of code could cause exceptions
			 */
			if (changed == true) {
				JPanel beginnerContainer = sideBar.getBeginnerContainer();
				beginnerContainer.removeAll();
				beginnerContainer.add(helpPanel);
				sideBar.repaint();
				cleanHighlights();
				setCursor(DEFAULT_CURSOR);
			}

		}

	}

	// end of adaption

	/**
	 * 
	 * @return navigation for assistant analysis sidebar
	 */

	/**
	 * add components to the contentpane of the beginnerpanel
	 * 
	 * @param c
	 *            - component
	 * @param x
	 *            - x position
	 * @param y
	 *            - y position
	 * @param width
	 *            - component width
	 * @param height
	 *            - component height
	 * @param weightx
	 *            - component weight in x direction
	 * @param weighty
	 *            - component weight in y direction
	 */
	protected void addComponent(Component c, int x, int y, int width,
			int height, double weightx, double weighty) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		contentPane.add(c, gbc);
	}

	// counter for y position of components
	private int counter = 0;

	private Border border5pix = BorderFactory.createEmptyBorder(5, 5, 5, 5);

	/**
	 * creates an entry without an image
	 * 
	 * @param headerString
	 *            - String of entry
	 * @param nodeIterator
	 *            - iterator of nodes for highlighting
	 * @param number
	 *            - number of elements / errors
	 * @param helpString
	 *            - string to help the user with the error
	 */
	protected void createEntry(String headerString, Iterator<?> nodeIterator,
			int number, String helpString) {
		createEntry(headerString, nodeIterator, number, helpString, null);
	}

	/**
	 * creates an entry without an image
	 * 
	 * @param headerString
	 *            - String of entry
	 * @param nodeIterator
	 *            - iterator of nodes for highlighting
	 * @param number
	 *            - number of elements / errors
	 * @param helpString
	 *            - string to help the user with the error
	 * @param imageIcon
	 *            - String of image icon to show under the helptext
	 */
	@SuppressWarnings("unchecked")
	protected void createEntry(String headerString, Iterator<?> nodeIterator,
			int number, String helpString, String imageIcon) {

		ArrayList<ClickLabel> labelSet = new ArrayList<ClickLabel>();

		ArrayList<AbstractPetriNetElementModel> elements = new ArrayList<AbstractPetriNetElementModel>();
		if (nodeIterator != null && nodeIterator.hasNext()) {
			int secCounter = 0;
			do {
				Object aem = nodeIterator.next();
				if (aem instanceof AbstractPetriNetElementModel)
					if (((AbstractPetriNetElementModel) aem).getHierarchyLevel() == 0) {
						elements.add(((AbstractPetriNetElementModel) aem));
					} else {
						elements.add(((AbstractPetriNetElementModel) aem)
								.getRootOwningContainer().getOwningElement());
					}
				else if (aem instanceof Set<?>) {
					Collection<AbstractPetriNetElementModel> nodeSet = (Collection<AbstractPetriNetElementModel>) aem;
					secCounter++;
					String groupOrPair = Messages.getString(PREFIX_BEGINNER	 + "Pair");
					if (nodeSet.size() > 2) {
						groupOrPair = Messages.getString(PREFIX_BEGINNER + "Group");
					}
					ClickLabel cLabel = new ClickLabel(SUB_POINT + groupOrPair
							+ " # " + secCounter, nodeSet.iterator(), editor);
					cLabel.setForeground(Color.RED);
					cLabel.setFont(ITEMS_FONT);
					labelSet.add(cLabel);
				}
			} while (nodeIterator.hasNext());
		}

		addClickLabel(headerString, number, labelSet, elements);

		if (!labelSet.isEmpty()) {
			Iterator<ClickLabel> iter = labelSet.iterator();
			while (iter.hasNext()) {
				addComponent(iter.next(), 0, counter, 1, 1, 0, 0);
				counter++;
			}
		}

		JTextArea helpText = addHelpText(helpString);

		if (imageIcon != null && !("".equals(imageIcon))) {
			JLabel helpImage = new JLabel(Messages.getImageIcon(imageIcon));
			helpImage.setBorder(BOTTOM_BORDER);
			addComponent(helpImage, 0, counter, 2, 1, 1, 0);

			counter++;
		} else {
			helpText.setBorder(BOTTOM_BORDER);
		}
	}

	private void addClickLabel(String headerString, int number, ArrayList<ClickLabel> labelSet, ArrayList<AbstractPetriNetElementModel> elements) {
		JLabel clickLabel;
		if (labelSet.isEmpty())
			clickLabel = new ClickLabel(Messages.getString(headerString) + COLON, elements, editor);
		else
			clickLabel = new JLabel(Messages.getString(headerString) + COLON);

        addLabel(number, clickLabel);
	}

	private void addClickLabel(String header, Collection<ArcModel> arcs){
	    JLabel label = new ClickLabel(header, editor, arcs);
	    addLabel(arcs.size(), label);
    }

    private void addLabel(int number, JLabel clickLabel) {
        clickLabel.setFont(ITEMS_FONT);
        clickLabel.setForeground(Color.RED);
        JLabel numberLabel = new JLabel(String.valueOf(number));
        numberLabel.setFont(ITEMS_FONT);
        numberLabel.setForeground(Color.RED);
        numberLabel.setBorder(border5pix);
        addComponent(clickLabel, 0, counter, 2, 1, 1, 0);
        addComponent(numberLabel, 2, counter, 1, 1, 0, 0);

        counter++;
    }

    private JTextArea addHelpText(String helpString) {
		JLabel helpIcon = new JLabel(Messages.getImageIcon(HELP_ICON));
		helpIcon.setBorder(border5pix);
		JTextArea helpText = new JTextArea();
		helpText.setLineWrap(true);
		helpText.setWrapStyleWord(true);
		helpText.setEditable(false);
		helpText.setFont(HELP_TEXT_FONT);
		helpText.setText(Messages.getString(helpString));
		addComponent(helpIcon, 0, counter, 1, 1, 0, 0);
		addComponent(helpText, 1, counter, 1, 1, 1, 0);

		counter++;
		return helpText;
	}

	protected void createEntry(String headerString, Collection<ArcModel> arcs, String helpString){

	    addClickLabel(Messages.getString(headerString), arcs);
		JTextArea helpText = addHelpText(helpString);
		helpText.setBorder(BOTTOM_BORDER);
	}

	/**
	 * method to create the subheaders like "Liveness", "Boundedness"...
	 * 
	 * @param subHeader
	 *            - String for Subheader
	 * @param dml
	 *            - the detailsmouselistener for the details icon
	 * @param status
	 *            - the status of the analysis (true - correct, false -
	 *            incorrect)
	 */
	protected void createAnalysisEntry(String subHeader,
			DetailsMouseListener dml, boolean status) {
		// border for gap between correctness and details icon
		JLabel subheaderLabel = new JLabel(Messages.getString(subHeader));
		subheaderLabel.setFont(SUBHEADER_FONT);
		subheaderLabel.setBorder(BOTTOM_BORDER);
		addComponent(subheaderLabel, 0, counter, 1, 1, 1, 0);
		JLabel correct = null;
		if (status)
			correct = new JLabel(Messages.getImageIcon(CORRECT_ICON));
		else {
			JLabel details = new JLabel(Messages.getImageIcon(DETAILS_ICON));
			details.addMouseListener(dml);
			details.setBorder(BOTTOM_BORDER);
			correct = new JLabel(Messages.getImageIcon(INCORRECT_ICON));
			addComponent(details, 2, counter, 1, 1, 0, 0);
		}
		correct.setBorder(BOTTOM_RIGHT_BORDER);
		addComponent(correct, 1, counter, 1, 1, 0, 0);
		counter++;
	}

	/**
	 * creates and place an empty entry under the other components to fill the
	 * place
	 */
	protected void createEmptyEntry() {
		addComponent(new JLabel(), 0, counter, 1, 1, 1, 1);
	}

	/**
	 * method to clean all highlights in the net
	 */
	public void cleanHighlights() {
		IEditor editor = sideBar.getEditor();

		editor.getModelProcessor().removeHighlighting();
		editor.getGraph().repaint();
	}

    public boolean getStatus() {
		return status;
	}

	public BeginnerPanel getPrevious() {
		return previous;
	}

	public boolean hasPrevious() {
		if (previous != null)
			return true;
		return false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		BeginnerPanel helpPanel = this;
		if (e.getSource().equals(back)) {
			helpPanel = previous;
		} else if (e.getSource().equals(startPage)) {
			helpPanel = previous;
			while (helpPanel != null && helpPanel.hasPrevious()) {
				helpPanel = helpPanel.getPrevious();
			}
		}
		JPanel beginnerContainer = sideBar.getBeginnerContainer();
		beginnerContainer.removeAll();
		beginnerContainer.add(helpPanel);
		sideBar.repaint();
		cleanHighlights();
		setCursor(DEFAULT_CURSOR);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		setCursor(HAND_CURSOR);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		setCursor(DEFAULT_CURSOR);
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	public abstract void addComponents();
}
