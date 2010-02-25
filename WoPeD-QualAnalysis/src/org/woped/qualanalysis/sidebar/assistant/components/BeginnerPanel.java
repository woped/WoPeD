package org.woped.qualanalysis.sidebar.assistant.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;

import org.woped.core.controller.IEditor;
import org.woped.core.model.AbstractElementModel;
import org.woped.qualanalysis.service.IQualanalysisService;
import org.woped.qualanalysis.sidebar.SideBar;
import org.woped.translations.Messages;

@SuppressWarnings("serial")
public abstract class BeginnerPanel extends JPanel implements MouseListener {

	// Constants for Fonts and Icons
	protected static final Font HEADER_FONT = new Font(Font.DIALOG, Font.BOLD,
			14);

	protected static final Font SUBHEADER_FONT = new Font(Font.DIALOG,
			Font.PLAIN, 14);

	protected static final Font ITEMS_FONT = new Font(Font.DIALOG, Font.PLAIN,
			12);

	protected static final Font HELP_TEXT_FONT = new Font(Font.DIALOG,
			Font.PLAIN, 12);

	// String for Details - Icon
	protected static final String DETAILS_ICON = "AnalysisSideBar.Beginner.Details";

	// String for Icon if status is correct (green)
	protected static final String CORRECT_ICON = "AnalysisSideBar.Beginner.Correct";

	// String for Icon if status is incorrect (red)
	protected static final String INCORRECT_ICON = "AnalysisSideBar.Beginner.Incorrect";

	// String for Warning Icon (orange)
	protected static final String WARNING_ICON = "AnalysisSideBar.Beginner.Warning";

	// String for Icon with questionmark (blue-white)
	protected static final String HELP_ICON = "AnalysisSideBar.Beginner.Help";

	protected static final String SUB_POINT = " - ";

	private static final Cursor DEFAULT_CURSOR = new Cursor(
			Cursor.DEFAULT_CURSOR);

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

	private String header = "";

	private SideBar sideBar = null;

	public IEditor editor = null;

	protected boolean status = true;

	protected IQualanalysisService qualanalysisService = null;

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
		this.header = header;
		this.contentPane = new JPanel();
		// create a scrollpane for vertical scrolling
		JScrollPane scrollPane = new JScrollPane(contentPane);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(null);
		this.contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(BorderFactory.createEmptyBorder(25, 5, 0, 20));
		this.contentPane.setLayout(new GridBagLayout());
		this.add(createNavigation(), BorderLayout.NORTH);
		this.add(scrollPane, BorderLayout.CENTER);

	}

	/**
	 * 
	 * @return navigation for assistant analysis sidebar
	 */
	private JPanel createNavigation() {
		JPanel navi = new JPanel();
		JPanel homeback = new JPanel();
		navi.setBackground(Color.WHITE);
		navi.setLayout(new BorderLayout());
		homeback.setBackground(Color.WHITE);
		homeback.setLayout(new BorderLayout());
		JLabel headLabel = new JLabel(header, JLabel.LEFT);
		headLabel.setFont(HEADER_FONT);
		back = new JLabel(Messages
				.getImageIcon("AnalysisSideBar.Beginner.Button.Empty"));
		startPage = new JLabel(Messages
				.getImageIcon("AnalysisSideBar.Beginner.Button.Empty"));
		if (this.hasPrevious()) {
			back = new JLabel(Messages
					.getImageIcon("AnalysisSideBar.Beginner.Button.Back"));
			back.setBackground(Color.WHITE);
			back.addMouseListener(this);
			if (previous.hasPrevious()) {
				startPage
						.setIcon(Messages
								.getImageIcon("AnalysisSideBar.Beginner.Button.Startpage"));
				startPage.setBackground(Color.WHITE);
				startPage.addMouseListener(this);
			}
		} else {
			headLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		}	
		navi.add(homeback, BorderLayout.EAST);
		homeback.add(back, BorderLayout.WEST);
		homeback.add(startPage, BorderLayout.CENTER);
		//navi.add(startPage, BorderLayout.WEST);
		//navi.add(back, BorderLayout.EAST);
		navi.add(headLabel, BorderLayout.CENTER);
		navi.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		return navi;
	}

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
	@SuppressWarnings("unchecked")
	protected void createEntry(String headerString, Iterator nodeIterator,
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
	protected void createEntry(String headerString, Iterator nodeIterator,
			int number, String helpString, String imageIcon) {

		ArrayList<ClickLabel> labelSet = new ArrayList<ClickLabel>();

		ArrayList<AbstractElementModel> elements = new ArrayList<AbstractElementModel>();
		if (nodeIterator != null && nodeIterator.hasNext()) {
			do {
				Object aem = nodeIterator.next();
				if (aem instanceof AbstractElementModel)
					if (((AbstractElementModel) aem).getHierarchyLevel() == 0) {
						elements.add(((AbstractElementModel) aem));
					} else {
						elements.add(((AbstractElementModel) aem)
								.getRootOwningContainer().getOwningElement());
					}
				else if (aem instanceof HashSet<?>) {
					int secCounter = 0;
					Collection<AbstractElementModel> nodeSet = (Collection) aem;
					secCounter++;
					String groupOrPair = Messages
							.getString("AnalysisSideBar.Beginner.Pair");
					if (nodeSet.size() > 2) {
						groupOrPair = Messages
								.getString("AnalysisSideBar.Beginner.Group");
					}
					ClickLabel cLabel = new ClickLabel(SUB_POINT + groupOrPair
							+ " # " + secCounter, nodeSet.iterator(), editor);
					cLabel.setForeground(Color.RED);
					cLabel.setFont(ITEMS_FONT);
					labelSet.add(cLabel);
				}
			} while (nodeIterator.hasNext());
		}

		JLabel clickLabel;
		if (labelSet.isEmpty())
			clickLabel = new ClickLabel(Messages.getString(headerString) + ":",
					elements, editor);
		else
			clickLabel = new JLabel(Messages.getString(headerString) + ":");
		clickLabel.setFont(ITEMS_FONT);
		clickLabel.setForeground(Color.RED);
		JLabel numberLabel = new JLabel(String.valueOf(number));
		numberLabel.setFont(ITEMS_FONT);
		numberLabel.setForeground(Color.RED);
		numberLabel.setBorder(border5pix);
		addComponent(clickLabel, 0, counter, 2, 1, 1, 0);
		addComponent(numberLabel, 2, counter, 1, 1, 0, 0);

		counter++;

		if (!labelSet.isEmpty()) {
			Iterator<ClickLabel> iter = labelSet.iterator();
			while (iter.hasNext()) {
				addComponent(iter.next(), 0, counter, 1, 1, 0, 0);
				counter++;
			}
		}

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

		if (imageIcon != null && !("".equals(imageIcon))) {
			JLabel helpImage = new JLabel(Messages.getImageIcon(imageIcon));
			helpImage.setBorder(BOTTOM_BORDER);
			addComponent(helpImage, 0, counter, 2, 1, 1, 0);

			counter++;
		} else {
			helpText.setBorder(BOTTOM_BORDER);
		}
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
	public void cleanHiglights() {
		IEditor editor = sideBar.getEditor();
		Iterator<AbstractElementModel> i = editor.getModelProcessor()
				.getElementContainer().getRootElements().iterator();
		while (i.hasNext()) {
			AbstractElementModel current = (AbstractElementModel) i.next();
			current.setHighlighted(false);
		}
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
			while (helpPanel.hasPrevious()) {
				helpPanel = helpPanel.getPrevious();
			}
		}
		JPanel beginnerContainer = sideBar.getBeginnerContainer();
		beginnerContainer.removeAll();
		beginnerContainer.add(helpPanel);
		sideBar.repaint();
		cleanHiglights();
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
