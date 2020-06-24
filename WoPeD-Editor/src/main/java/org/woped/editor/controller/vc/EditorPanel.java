package org.woped.editor.controller.vc;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.config.DefaultStaticConfiguration;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.IEditor;
import org.woped.core.gui.IEditorAware;
import org.woped.core.model.petrinet.EditorLayoutInfo;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.Constants;
import org.woped.editor.controller.VisualController;
import org.woped.editor.controller.WoPeDUndoManager;
import org.woped.editor.gui.OverviewPanel;
import org.woped.editor.orientation.EditorSize;
import org.woped.editor.orientation.Orientation;
import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.p2t.P2TSideBar;
import org.woped.qualanalysis.sidebar.SideBar;
import org.woped.qualanalysis.sidebar.expert.components.GraphTreeModelSelector;
import org.woped.qualanalysis.understandability.NetColorScheme;

@SuppressWarnings("serial")
public class EditorPanel extends JPanel {

	// Headers of the different Panes
	private static final Font HEADER_FONT = DefaultStaticConfiguration.DEFAULT_LABEL_BOLDFONT;
	private static final int m_splitPosition = 600;
	private static final int m_splitSize = 10;
	private final int m_splitHeightOverviewPosition = 100;
	/**
	 * TODO: These members are public for now while we are still in the process
	 * or factoring out their uses from EditorVC
	 */
	// ! Stores a reference to the tree view and overview window
	// ! for the net
	// ! It is kept as a member to be able to show / hide this part of the
	// ! editor window as required
	public EditorStatusBarVC m_statusbar;
	public JSplitPane m_rightSideTreeView = null;
	public JSplitPane m_mainSplitPane = null;
	public JSplitPane mainsplitPaneWithAnalysisBar = null;
	public JSplitPane mainsplitPaneWithP2TBar = null;
	public JSplitPane m_rightSideTreeViewWithAnalysisBar = null;
	public JTree m_treeObject = null;
	public JPanel overviewPanel = null;
	public JPanel treeviewPanel = null;
	public JScrollPane m_scrollPane = null;
	// rotate
	public Orientation m_orientation = null;
	public EditorSize editorSize = null;
	public GraphTreeModel m_treeModel = null;
	public EditorLayoutInfo m_EditorLayoutInfo = null;
    private JComponent container = null;
    private IEditor editor;
    private AbstractApplicationMediator centralMediator;
    private PropertyChangeSupport propertyChangeSupport;
    private boolean metricsBarVisible = false;
    private boolean analysisBarVisible = false;
    private boolean p2TBarVisible = false;
    private SideBar qualitativeAnalysisSideBar = null;
    private P2TSideBar p2tSideBar = null;
    private JLabel semanticLabel = null;
    private JLabel p2tLabel = null;
    private JPanel analysisSideBarPanel = new JPanel(new GridBagLayout());
    private JPanel p2tSideBarPanel = new JPanel(new GridBagLayout());
    private JCheckBox autoRefresh = null;
    private JCheckBox tStarCheckBox = null;
    private boolean tStarEnabled = false;
    private boolean m_UnderstandabilityColoringEnabled = false;
    // Metrics team variables
    private boolean automaticResize = false;
    private org.woped.metrics.sidebar.SideBar metricsSideBar = null;
    private NetColorScheme m_understandColoring = null;
    
    private JSplitPane mainPaneWithT2PBar;
    private JPanel t2pBar;
    private String t2pText;
    
	public EditorPanel(IEditor editor, AbstractApplicationMediator centralMediator,
			PropertyChangeSupport propertyChangeSupport, boolean loadUI) {
		this.editor = editor;
		this.centralMediator = centralMediator;
		this.propertyChangeSupport = propertyChangeSupport;
		this.setLayout(new BorderLayout());
		// Set some default size for the subprocess window
		setPreferredSize(new Dimension(600, 400));

		m_orientation = new Orientation();
		m_EditorLayoutInfo = new EditorLayoutInfo();
		editorSize = new EditorSize(editor);

		m_EditorLayoutInfo.setVerticalLayout(isRotateSelected());

		if (loadUI) {
			// Big editor panel
			m_scrollPane = new JScrollPane(editor.getGraph());
			Dimension scrollPaneMinimumSize = new Dimension(0, 0);
			m_scrollPane.setMinimumSize(scrollPaneMinimumSize);
			
			// TreePanel
			treeviewPanel = getTreeviewPanel();
			// Overview Panel
			overviewPanel = getOverviewPanel();

			// Splits
			m_rightSideTreeView = new JSplitPane(JSplitPane.VERTICAL_SPLIT, overviewPanel, treeviewPanel);
			m_rightSideTreeView.setDividerLocation(100);

			m_mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, m_scrollPane, m_rightSideTreeView);

			// Initially, show side tree to correctly initialize default divider
			// position
			m_mainSplitPane.setOneTouchExpandable(true);
			m_mainSplitPane.setDividerSize(m_splitSize);
			add(m_mainSplitPane);
			m_mainSplitPane.addPropertyChangeListener(VisualController.getInstance());

			Dimension d = m_rightSideTreeView.getMinimumSize();
			d.width = 0;
			m_rightSideTreeView.setMinimumSize(d);
			m_mainSplitPane.setResizeWeight(0.85);

			// NetColorScheme
			m_understandColoring = new NetColorScheme();
		}

		initializeAnalysisSideBar();
		initializeP2TSideBar();
	}

	/**
	 * Returns the filename if the net was saved before or was opened from a
	 * file.
	 * 
	 * @return String
	 */
	public String getName() {
		return super.getName() == null ? Messages.getString("Document.Title.Untitled") : super.getName();
	}

	// ! Set the document name
	// ! Overridden to also update the title bar of the editor window
	// ! @param name specifies the name of the edited document
	@Override
	public void setName(String name) {
		super.setName(name);
		// Update document title of editor window
		JInternalFrame frame = (JInternalFrame) getContainer();
		if (frame != null) {
			frame.setTitle(name);
		}

		// notify the editor aware vc
		Iterator<?> editorIter = centralMediator.getEditorAwareVCs().iterator();
		while (editorIter.hasNext()) {
			((IEditorAware) editorIter.next()).renameEditor(editor);
		}
	}

    @SuppressWarnings("all")
    public JComponent getContainer() {
        return container;
    }

    public void setContainer(JComponent container) {
        this.container = container;
    }

	/**
     * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico
     *         Moeller, Sebastian Fuss
     * @see Component#getParent()
     */
    @Override
    public Container getParent() {
        return super.getParent();
    }

    public boolean isSideTreeViewVisible() {
        return (m_mainSplitPane.getDividerLocation() < m_mainSplitPane.getMaximumDividerLocation());
    }

    public void setSideTreeViewVisible(boolean showTreeView) {
        m_mainSplitPane.setDividerLocation(
                showTreeView ? m_mainSplitPane.getLastDividerLocation() : m_mainSplitPane.getMaximumDividerLocation());
    }

	public JPanel getTreeviewPanel() {
		if (treeviewPanel == null) {
			// creates TreeModel
			m_treeModel = new GraphTreeModel(editor);

			// Element Tree
			m_treeObject = new JTree(m_treeModel);
			m_treeObject.setCellRenderer(new NetInfoTreeRenderer());
			editor.getGraph().getModel().addGraphModelListener(m_treeModel);
			m_treeObject.setRootVisible(false);
			m_treeObject.setShowsRootHandles(true);
			// Handle selection of tree items
			// by selecting corresponding item in graph
			GraphTreeModelSelector selectionHandler = new GraphTreeModelSelector(editor, m_treeObject, centralMediator,
					false);
			m_treeObject.addTreeSelectionListener(selectionHandler);
			editor.getGraph().getSelectionModel().addGraphSelectionListener(selectionHandler);

			JScrollPane sTree = new JScrollPane(m_treeObject);

			treeviewPanel = new JPanel(new GridBagLayout());
			JLabel elementsLabel = new JLabel(Messages.getString("Sidebar.Treeview.Title"));
			elementsLabel.setFont(EditorPanel.HEADER_FONT);

			HideLabel treeviewPanelClose = new HideLabel(Messages.getImageIcon("AnalysisSideBar.Cancel"),
					treeviewPanel);

			treeviewPanel.add(elementsLabel, new GridBagConstraints(0, 0, 2, 2, 0.0, 0.0, GridBagConstraints.NORTHWEST,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			treeviewPanel.add(treeviewPanelClose, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.NORTHEAST, GridBagConstraints.REMAINDER, new Insets(0, 0, 0, 1), 0, 0));
			treeviewPanel.add(sTree, new GridBagConstraints(0, 1, 4, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return treeviewPanel;
	}

	public JPanel getOverviewPanel() {
		if (overviewPanel == null) {
			OverviewPanel overview = new OverviewPanel(editor, m_scrollPane);
			overviewPanel = new JPanel(new GridBagLayout());
			JLabel overviewLabel = new JLabel(Messages.getString("Sidebar.Overview.Title"));
			overviewLabel.setFont(HEADER_FONT);

			HideLabel overviewPanelClose = new HideLabel(Messages.getImageIcon("AnalysisSideBar.Cancel"),
					overviewPanel);
			overviewPanel.add(overviewLabel, new GridBagConstraints(0, 0, 2, 2, 0.0, 0.0, GridBagConstraints.NORTHWEST,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			overviewPanel.add(overviewPanelClose, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.NORTHEAST, GridBagConstraints.REMAINDER, new Insets(0, 0, 0, 1), 0, 0));
			overviewPanel.add(overview, new GridBagConstraints(0, 1, 4, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		}
		return overviewPanel;
	}

    public boolean isOverviewPanelVisible() {
        return overviewPanel.isVisible();
    }

	public void setOverviewPanelVisible(boolean show) {
		if (!m_rightSideTreeView.isVisible()) {
			m_rightSideTreeView.setVisible(true);
			m_rightSideTreeView.setEnabled(false);
		}
		overviewPanel.setVisible(show);
		m_rightSideTreeView.setDividerLocation(100);
		if (show) {
			if (!isAnalysisBarVisible() && !isMetricsBarVisible() && !isTreeviewPanelVisible() && !isP2TBarVisible()) {
				m_mainSplitPane.setDividerLocation(m_mainSplitPane.getLastDividerLocation());
				m_mainSplitPane.setOneTouchExpandable(true);
				m_mainSplitPane.setEnabled(true);
			}
			if (isAnalysisBarVisible() || isMetricsBarVisible() || isP2TBarVisible()) {
				m_rightSideTreeViewWithAnalysisBar.setEnabled(true);
				if (isTreeviewPanelVisible()) {
					m_rightSideTreeViewWithAnalysisBar.setDividerLocation(200);
				} else {
					m_rightSideTreeViewWithAnalysisBar.setDividerLocation(100);
				}
			}
			if (isTreeviewPanelVisible()) {
				m_rightSideTreeView.setEnabled(true);
			}
		} else {
			if (isTreeviewPanelVisible()) {
				m_rightSideTreeView.setEnabled(false);
				if (isAnalysisBarVisible() || isMetricsBarVisible() || isP2TBarVisible()) {
					m_rightSideTreeViewWithAnalysisBar.setDividerLocation(100);
				} else {
					m_rightSideTreeView.setDividerLocation(0);
				}
			} else {
				m_rightSideTreeView.setVisible(false);
				if (isAnalysisBarVisible() || isMetricsBarVisible() || isP2TBarVisible()) {
					m_rightSideTreeViewWithAnalysisBar.setDividerLocation(0);
					m_rightSideTreeViewWithAnalysisBar.setEnabled(false);
				}
			}
			if (!isAnalysisBarVisible() && !isMetricsBarVisible() && !isOverviewPanelVisible()
					&& !isTreeviewPanelVisible() && !isP2TBarVisible()) {
				m_mainSplitPane.setDividerLocation(m_mainSplitPane.getMaximumDividerLocation());
				m_mainSplitPane.setOneTouchExpandable(false);
				m_mainSplitPane.setEnabled(false);
			}
		}
	}

    public boolean isTreeviewPanelVisible() {
        return treeviewPanel.isVisible();
    }

	public void setTreeviewPanelVisible(boolean show) {
		if (!m_rightSideTreeView.isVisible()) {
			m_rightSideTreeView.setVisible(true);
			m_rightSideTreeView.setEnabled(false);
		}
		treeviewPanel.setVisible(show);
		m_rightSideTreeView.setDividerLocation(100);
		if (show) {
			if (!isOverviewPanelVisible() && !isAnalysisBarVisible() && !isMetricsBarVisible() && !isP2TBarVisible()) {
				m_mainSplitPane.setDividerLocation(m_mainSplitPane.getLastDividerLocation());
				m_mainSplitPane.setOneTouchExpandable(true);
				m_mainSplitPane.setEnabled(true);
			}
			if (isAnalysisBarVisible() || isMetricsBarVisible() || isP2TBarVisible()) {
				if (isOverviewPanelVisible()) {
					m_rightSideTreeViewWithAnalysisBar.setDividerLocation(200);
					m_rightSideTreeView.setEnabled(true);
				} else {
					m_rightSideTreeViewWithAnalysisBar.setDividerLocation(100);
					m_rightSideTreeViewWithAnalysisBar.setEnabled(true);
				}
			}
			if (isOverviewPanelVisible()) {
				m_rightSideTreeView.setEnabled(true);
			}
		} else {
			if (isOverviewPanelVisible()) {
				if (isAnalysisBarVisible() || isMetricsBarVisible() || isP2TBarVisible()) {
					m_rightSideTreeViewWithAnalysisBar.setDividerLocation(100);
					m_rightSideTreeView.setEnabled(false);
				}
			} else {
				m_rightSideTreeView.setVisible(false);
				if (isAnalysisBarVisible() || isMetricsBarVisible() || isP2TBarVisible()) {
					m_rightSideTreeViewWithAnalysisBar.setDividerLocation(0);
					m_rightSideTreeView.setEnabled(false);
					m_rightSideTreeViewWithAnalysisBar.setEnabled(false);
				}
			}

			if (!isAnalysisBarVisible() && !isMetricsBarVisible() && !isOverviewPanelVisible()
					&& !isTreeviewPanelVisible() && !isP2TBarVisible()) {
				m_mainSplitPane.setDividerLocation(m_mainSplitPane.getMaximumDividerLocation());
				m_mainSplitPane.setOneTouchExpandable(false);
				m_mainSplitPane.setEnabled(false);
			}
		}
	}

	public boolean isMetricsBarVisible() {
		return metricsBarVisible;
	}

	public boolean isP2TBarVisible() {
		return p2TBarVisible;
	}

	public boolean isAnalysisBarVisible() {
		return analysisBarVisible;
	}

	/**
	 * Method removes analysis sidebar
     *
     * @author Lennart Oess, Arthur Vetter, Jens Tessen, Heiko Herzog
	 * @editor Martin Meitz
	 */
	public void hideAnalysisBar() {
		if (analysisBarVisible) {
			tStarCheckBox.setSelected(false);
			qualitativeAnalysisSideBar.showTStarIfPossible();
			remove(mainsplitPaneWithAnalysisBar);
			mainsplitPaneWithAnalysisBar = null;
			m_mainSplitPane.setBottomComponent(m_rightSideTreeView);
			analysisBarVisible = false;
			qualitativeAnalysisSideBar.refresh();

			if (!isAnalysisBarVisible() && !isMetricsBarVisible() && !isOverviewPanelVisible()
					&& !isTreeviewPanelVisible() && !isP2TBarVisible()) {
				m_mainSplitPane.setDividerLocation(m_mainSplitPane.getMaximumDividerLocation());
				m_mainSplitPane.setOneTouchExpandable(false);
				m_mainSplitPane.setEnabled(false);
			} else {
				m_mainSplitPane.setResizeWeight(0.85);
			}
			add(m_mainSplitPane);

			revalidate();
		}
	}

	public EditorLayoutInfo getSavedLayoutInfo() {
		EditorLayoutInfo result = new EditorLayoutInfo();
		result.setTreeViewWidthRight(m_mainSplitPane.getDividerLocation());
		result.setOverviewPanelVisible(isOverviewPanelVisible());
		result.setTreeHeightOverview(m_rightSideTreeView.getDividerLocation());
		result.setTreePanelVisible(isTreeviewPanelVisible());
		result.setSavedSize(getSize());
		result.setSavedLocation(getLocation());

		return result;
	}

	public void setSavedLayoutInfo(EditorLayoutInfo layoutInfo) {
		if (layoutInfo != null) {
			if (m_mainSplitPane != null) {
				int divLoc;
				// if(layoutInfo.getTreeViewWidthRight()){
				divLoc = layoutInfo.getTreeViewWidthRight();
				// }//else{
				// divLoc = layoutInfo.getTreeViewWidth();
				// }
				m_mainSplitPane.setDividerLocation(divLoc);
				if (divLoc <= 1) {
					m_mainSplitPane.setLastDividerLocation(m_splitPosition);
				}
			}

			if (m_rightSideTreeView != null) {
				int heightLoc = layoutInfo.getTreeHeightOverview();
				m_rightSideTreeView.setDividerLocation(heightLoc);
				if (heightLoc <= 1) {
					m_rightSideTreeView.setLastDividerLocation(m_splitHeightOverviewPosition);
				}
			}

			if (overviewPanel != null) {
				setOverviewPanelVisible(layoutInfo.getOverviewPanelVisible());
			}
			if (treeviewPanel != null) {
				setTreeviewPanelVisible(layoutInfo.getTreePanelVisible());
			}
			// Size
			Dimension d = layoutInfo.getSavedSize();
			setPreferredSize(d);
			// Setting our own size won't help us much since
			// we're the child of a scrolling pane
			// Instead, look into our parents
			// to find the one that is a JInternalFrame
			Container currentParent = getParent();
			while ((currentParent != null) && (!(currentParent instanceof JInternalFrame))) {
				currentParent = currentParent.getParent();
			}

			if (currentParent != null) {
				Dimension editorDim = new Dimension();
				getSize(editorDim);
				Dimension frameDim = new Dimension();
				currentParent.getSize(frameDim);

				// There is some overhead that must be taken into account
				// here...
				d.width += frameDim.width - editorDim.width;
				d.height += frameDim.height - editorDim.height;

				currentParent.setSize(d);
			}

			VisualController.getInstance()
					.propertyChange(new PropertyChangeEvent(centralMediator, "InternalFrameCount", null, null));

			// Currently, we ignore the position
			// It's unwanted sometimes, especially if things like desktop
			// resolution change
		}
	}

	/**
	 *
     * @author Mathias Gruschinske, Stefan Hackenberg
     */
    public void hideMetricsBar() {
        if (metricsBarVisible) {
            remove(mainsplitPaneWithAnalysisBar);
            mainsplitPaneWithAnalysisBar = null;
            metricsSideBar.clean();
            m_mainSplitPane.setBottomComponent(m_rightSideTreeView);
            metricsBarVisible = false;
            if (!isAnalysisBarVisible() && !isMetricsBarVisible() && !isOverviewPanelVisible()
                    && !isTreeviewPanelVisible()) {
                m_mainSplitPane.setDividerLocation(m_mainSplitPane.getMaximumDividerLocation());
                m_mainSplitPane.setOneTouchExpandable(false);
                m_mainSplitPane.setEnabled(false);
            } else {
                m_mainSplitPane.setResizeWeight(0.85);
            }
            add(m_mainSplitPane);

            revalidate();
        }
    }

    /**
     * initializes analysis sidebar
     *
     * @author Martin Meitz
	 */
	public void initializeAnalysisSideBar() {
		semanticLabel = new JLabel(Messages.getString("Tools.semanticalAnalysis.text"));
		semanticLabel.setFont(HEADER_FONT);

		// settings for qualitative Analysis tab
		boolean autoRefreshStatus = true;
		tStarCheckBox = new JCheckBox(Messages.getString("AnalysisSideBar.Footer.TStar"));

		qualitativeAnalysisSideBar = new SideBar(editor, centralMediator, autoRefreshStatus, tStarCheckBox);

		JPanel bottomPanel = new JPanel(new BorderLayout());
		autoRefresh = new JCheckBox(Messages.getString("AnalysisSideBar.Footer.Autorefresh"));
		autoRefresh.setSelected(autoRefreshStatus);
		autoRefresh.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				boolean selected = ((JCheckBox) arg0.getSource()).isSelected();
				qualitativeAnalysisSideBar.setAutoRefreshStatus(selected);
				qualitativeAnalysisSideBar.repaint();
			}
		});
		bottomPanel.add(autoRefresh, BorderLayout.CENTER);
		// if t star checkbox is selected show t star in editor (only if net
		// is a workflow net)
		tStarCheckBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				qualitativeAnalysisSideBar.showTStarIfPossible();
			}
		});
		bottomPanel.add(tStarCheckBox, BorderLayout.SOUTH);

		// define close button
		HideLabel semanticPanelClose = new HideLabel(Messages.getImageIcon("AnalysisSideBar.Cancel"),
				analysisSideBarPanel);

		analysisSideBarPanel.add(semanticLabel, new GridBagConstraints(0, 0, 2, 2, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		analysisSideBarPanel.add(semanticPanelClose, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.REMAINDER, new Insets(0, 0, 0, 1), 0, 0));
		analysisSideBarPanel.add(qualitativeAnalysisSideBar, new GridBagConstraints(0, 1, 4, 1, 1.0, 1.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	}

	/**
     *
     * @author Lennart Oess, Arthur Vetter, Jens Tessen, Heiko Herzog
	 * @editor Martin Meitz
	 */
	public void showAnalysisBar() {
		if (analysisBarVisible) {
			hideAnalysisBar();
		}

		if (!analysisBarVisible) {
			if (metricsBarVisible) {
				hideMetricsBar();
			}

			if (p2TBarVisible) {
				hideP2TBar();
			}

			remove(m_mainSplitPane);
			getContainer();
			qualitativeAnalysisSideBar.refresh();

			m_rightSideTreeViewWithAnalysisBar = new JSplitPane(JSplitPane.VERTICAL_SPLIT, m_rightSideTreeView,
					analysisSideBarPanel);
			if (isOverviewPanelVisible() && isTreeviewPanelVisible()) {
				m_rightSideTreeViewWithAnalysisBar.setDividerLocation(200);
				m_rightSideTreeViewWithAnalysisBar.setEnabled(true);
			} else if (isOverviewPanelVisible() || isTreeviewPanelVisible()) {
				m_rightSideTreeViewWithAnalysisBar.setDividerLocation(100);
				m_rightSideTreeViewWithAnalysisBar.setEnabled(true);
			} else {
				m_rightSideTreeViewWithAnalysisBar.setDividerLocation(0);
				m_mainSplitPane.setOneTouchExpandable(true);
				m_mainSplitPane.setEnabled(true);
				m_rightSideTreeViewWithAnalysisBar.setEnabled(false);
			}

			Dimension rightSideTreeViewWithAnalysisBarMinimumSize = new Dimension(0, 0);
			m_rightSideTreeViewWithAnalysisBar.setMinimumSize(rightSideTreeViewWithAnalysisBarMinimumSize);

			mainsplitPaneWithAnalysisBar = m_mainSplitPane;
			mainsplitPaneWithAnalysisBar.setBottomComponent(m_rightSideTreeViewWithAnalysisBar);

			add(mainsplitPaneWithAnalysisBar);
			analysisBarVisible = true;
			revalidate();
			editorSize.resize(false);
            mainsplitPaneWithAnalysisBar.setDividerLocation(getWidth() - editorSize.SIDEBAR_WIDTH);
            mainsplitPaneWithAnalysisBar.setResizeWeight(1);
		}
	}

	/**
	 * Shows the metrics sidebar and resize the editor window. Replaces the
	 * normal EditorSplitPane with another SplitPane with the MetricsSidebar on
	 * the right side.
     *
     * @author Mathias Gruschinske, Stefan Hackenberg
	 */
	public void showMetricsBar() {
		if (metricsBarVisible) {
			hideMetricsBar();
		}

		if (!metricsBarVisible) {

			if (analysisBarVisible) {
				hideAnalysisBar();
			}

			if (p2TBarVisible) {
				hideP2TBar();
			}

			remove(m_mainSplitPane);

			// create the metrics sidebar
			metricsSideBar = new org.woped.metrics.sidebar.SideBar(editor);

			// create a Panel, which contains the sidebar
			JPanel sideBar = new JPanel(new BorderLayout());
			sideBar.add(metricsSideBar, BorderLayout.CENTER);

			// create a new SplitPane with a horizontal split
			m_rightSideTreeViewWithAnalysisBar = new JSplitPane(JSplitPane.VERTICAL_SPLIT, m_rightSideTreeView,
					sideBar);
			if (isOverviewPanelVisible() && isTreeviewPanelVisible() && isP2TBarVisible()) {
				m_rightSideTreeViewWithAnalysisBar.setDividerLocation(200);
				m_rightSideTreeViewWithAnalysisBar.setEnabled(true);
			} else if (isOverviewPanelVisible() || isTreeviewPanelVisible() || isP2TBarVisible()) {
				m_rightSideTreeViewWithAnalysisBar.setDividerLocation(100);
				m_rightSideTreeViewWithAnalysisBar.setEnabled(true);
			} else {
				m_rightSideTreeViewWithAnalysisBar.setDividerLocation(0);
				m_mainSplitPane.setOneTouchExpandable(true);
				m_mainSplitPane.setEnabled(true);
				m_rightSideTreeViewWithAnalysisBar.setEnabled(false);
			}

			Dimension rightSideTreeViewWithAnalysisBarMinimumSize = new Dimension(0, 0);
			m_rightSideTreeViewWithAnalysisBar.setMinimumSize(rightSideTreeViewWithAnalysisBarMinimumSize);

			mainsplitPaneWithAnalysisBar = m_mainSplitPane;
			mainsplitPaneWithAnalysisBar.setBottomComponent(m_rightSideTreeViewWithAnalysisBar);

			add(mainsplitPaneWithAnalysisBar);

			// analysisBarVisible = true;
			metricsBarVisible = true;
			revalidate();

			// new calculation of the size from editor window (only width)
			editorSize.resize(false);

            mainsplitPaneWithAnalysisBar.setDividerLocation(getWidth() - editorSize.SIDEBAR_WIDTH);
            mainsplitPaneWithAnalysisBar.setResizeWeight(1);

			metricsSideBar.setKeyLabelWidth();
		}
	}

    public boolean isRotateSelected() {
        return m_orientation.isRotateSelected();
    }

	public void setRotateSelected(boolean rotateSelected) {
		m_orientation.setRotateSelected(rotateSelected);
		if (analysisBarVisible && !metricsBarVisible)
			qualitativeAnalysisSideBar.showTStarIfPossible();
	}

	public void setTStarEnabled(boolean tStarEnabled) {
		this.tStarEnabled = tStarEnabled;
	}

	public boolean isShowingTStar() {
		return tStarEnabled;
	}

	public boolean isUnderstandabilityColoringEnabled() {
		return m_UnderstandabilityColoringEnabled;
	}

	public void setUnderstandabilityColoringEnabled(boolean active) {
		m_UnderstandabilityColoringEnabled = active;
		ConfigurationManager.getConfiguration().setColorOn(active);
	}

    public NetColorScheme getUnderstandColoring() {
        return m_understandColoring;
	}

    public void setUnderstandColoring(NetColorScheme newScheme) {
        m_understandColoring = newScheme;
    }

	/**
	 * @author Lennart Oess, Arthur Vetter, Jens Tessen, Heiko Herzog
	 */
	public void autoRefreshAnalysisBar() {
		if (analysisBarVisible && autoRefresh != null && autoRefresh.isSelected()) {
			qualitativeAnalysisSideBar.refresh();
			editorSize.resize(false);
		}
	}

	public SideBar getAnalysisSideBar() {
		return qualitativeAnalysisSideBar;
	}

	public boolean isAutomaticResize() {
		return automaticResize;
	}

    public void setAutomaticResize(boolean automaticresize) {
        automaticResize = automaticresize;
    }

	public JSplitPane getMainSplitPane() {
		return m_mainSplitPane;
	}

	public void checkMainSplitPaneDivider() {
		BasicSplitPaneUI ui = (BasicSplitPaneUI) getMainSplitPane().getUI();
		BasicSplitPaneDivider divider = ui.getDivider();
		JButton rightArrowButton = (JButton) divider.getComponent(1);
		JButton leftArrowButton = (JButton) divider.getComponent(0);

		JSplitPane mainSplit = getMainSplitPane();
		int minDividerLocation = mainSplit.getMinimumDividerLocation();
		int dividerLocation = mainSplit.getDividerLocation();
		int maxDividerLocation = mainSplit.getMaximumDividerLocation();

		if (minDividerLocation >= dividerLocation) {
			leftArrowButton.setVisible(false);
			rightArrowButton.setVisible(true);
		} else if ((minDividerLocation < dividerLocation) && (dividerLocation < maxDividerLocation)) {
			leftArrowButton.setVisible(true);
			rightArrowButton.setVisible(true);
		} else if (maxDividerLocation >= dividerLocation) {
			leftArrowButton.setVisible(true);
			rightArrowButton.setVisible(false);
		} else {
			leftArrowButton.setVisible(true);
			rightArrowButton.setVisible(true);
		}

	}

	public GraphTreeModel GetTreeModel() {
		return m_treeModel;
	}

	/**
	 * Calls the algorithms for rotating the view and the elements
     *
     * @param editorVC
	 */
	public void rotateLayout(EditorVC editorVC) {
		// Is necessary to switch off the undoManager
        if (editorVC.getGraph().getUndoManager() != null) {
            ((WoPeDUndoManager) editorVC.getGraph().getUndoManager()).setEnabled(false);
		}
		editorVC.getEditorPanel().m_orientation.rotateView(editorVC.getModelProcessor().getElementContainer());

		if (editorVC.isRotateSelected()) {
			LoggerManager.debug(Constants.EDITOR_LOGGER, "DEACTIVATE RotateSelected ");
			editorVC.setRotateSelected(false);
			editorVC.getEditorPanel().m_EditorLayoutInfo.setVerticalLayout(false);
		} else {
			LoggerManager.debug(Constants.EDITOR_LOGGER, "ACTIVATE RotateSelected ");
			editorVC.setRotateSelected(true);
			editorVC.getEditorPanel().m_EditorLayoutInfo.setVerticalLayout(true);
		}
		// Update the UI representation
		editorVC.getGraph().updateUI();
		editorVC.getGraph().drawNet(editorVC.getModelProcessor());
		editorVC.updateNet();

		// Nils Lamb, Jan 2012
		// prevent resizing if frame is empty (seems weird otherwise)
		if (editorVC.getModelProcessor().getElementContainer().getIdMap().isEmpty())
			editorVC.getEditorPanel().editorSize.resize(false);
		else
			editorVC.getEditorPanel().editorSize.resize(true);

		// Is necessary to switch on the undoManager
		if (editorVC.getGraph().getUndoManager() != null) {
			((WoPeDUndoManager) editorVC.getGraph().getUndoManager()).setEnabled(true);
		}
		editorVC.setSaved(false);
	}

	public void initializeP2TSideBar() {
		p2tLabel = new JLabel(Messages.getString("P2T.openP2T.text"));
		p2tLabel.setFont(HEADER_FONT);

		p2tSideBar = new org.woped.qualanalysis.p2t.P2TSideBar(editor);

		// Define close button
		HideLabel p2tPanelClose = new HideLabel(Messages.getImageIcon("AnalysisSideBar.Cancel"), p2tSideBarPanel);

		p2tSideBarPanel.add(p2tLabel, new GridBagConstraints(0, 0, 2, 2, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		p2tSideBarPanel.add(p2tPanelClose, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST,
				GridBagConstraints.REMAINDER, new Insets(0, 0, 0, 1), 0, 0));
		p2tSideBarPanel.add(p2tSideBar, new GridBagConstraints(0, 1, 4, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	}

	public void showP2TBar() {
		if (p2TBarVisible) {
			hideP2TBar();
		}

		if (!p2TBarVisible) {
			if (metricsBarVisible) {
				hideMetricsBar();
			}
			if (analysisBarVisible) {
				hideAnalysisBar();
			}

			remove(m_mainSplitPane);
			getContainer();

            m_rightSideTreeViewWithAnalysisBar = new JSplitPane(JSplitPane.VERTICAL_SPLIT, m_rightSideTreeView,
					p2tSideBarPanel);
			if (isOverviewPanelVisible() && isTreeviewPanelVisible()) {
				m_rightSideTreeViewWithAnalysisBar.setDividerLocation(200);
				m_rightSideTreeViewWithAnalysisBar.setEnabled(true);
			} else if (isOverviewPanelVisible() || isTreeviewPanelVisible()) {
				m_rightSideTreeViewWithAnalysisBar.setDividerLocation(100);
				m_rightSideTreeViewWithAnalysisBar.setEnabled(true);
			} else {
				m_rightSideTreeViewWithAnalysisBar.setDividerLocation(0);
				m_mainSplitPane.setOneTouchExpandable(true);
				m_mainSplitPane.setEnabled(true);
				m_rightSideTreeViewWithAnalysisBar.setEnabled(false);
			}

			Dimension rightSideTreeViewWithP2TBarMinimumSize = new Dimension(0, 0);
			m_rightSideTreeViewWithAnalysisBar.setMinimumSize(rightSideTreeViewWithP2TBarMinimumSize);

			mainsplitPaneWithP2TBar = m_mainSplitPane;
			mainsplitPaneWithP2TBar.setBottomComponent(m_rightSideTreeViewWithAnalysisBar);

			add(mainsplitPaneWithP2TBar);
			p2TBarVisible = true;
			revalidate();
			editorSize.resize(false);
            mainsplitPaneWithP2TBar.setDividerLocation(getWidth() - editorSize.SIDEBAR_WIDTH);
            mainsplitPaneWithP2TBar.setResizeWeight(1);

			// Trigger callback on P2T side bar.
			p2tSideBar.onSideBarShown(true);
		}
	}

	public void hideP2TBar() {

		if (p2TBarVisible) {

			qualitativeAnalysisSideBar.showTStarIfPossible();
			m_mainSplitPane.setBottomComponent(m_rightSideTreeView);
			p2TBarVisible = false;

			qualitativeAnalysisSideBar.refresh();

			if (!isAnalysisBarVisible() && !isMetricsBarVisible() && !isOverviewPanelVisible()
					&& !isTreeviewPanelVisible()) {
				m_mainSplitPane.setDividerLocation(m_mainSplitPane.getMaximumDividerLocation());
				m_mainSplitPane.setOneTouchExpandable(false);
				m_mainSplitPane.setEnabled(false);
			} else {
				m_mainSplitPane.setResizeWeight(0.85);
			}
			add(m_mainSplitPane);

			revalidate();

			// Trigger callback on P2T side bar.
			p2tSideBar.onSideBarShown(false);
        }
    }
	
	public String getT2PText() {
		return t2pText;
	}
	
	public void showT2PBar(String text) {
		t2pText = text;
		t2pBar = new JPanel(new GridBagLayout());
		JLabel l = new JLabel(Messages.getString("P2T.openP2T.header"));
		l.setFont(HEADER_FONT);
		JTextArea ta = new JTextArea(text);
		ta.setWrapStyleWord(true);
		ta.setLineWrap(true);
		ta.setEditable(false);
		
		HideLabel t2pBarClose = new HideLabel(Messages.getImageIcon("AnalysisSideBar.Cancel"), t2pBar);
		
		t2pBar.add(l, new GridBagConstraints(0, 0, 2, 2, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		t2pBar.add(t2pBarClose, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.REMAINDER, new Insets(0, 0, 0, 1), 0, 0));
		t2pBar.add(ta, new GridBagConstraints(0, 1, 4, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		JSplitPane scrollPaneWithT2PBar = new JSplitPane(JSplitPane.VERTICAL_SPLIT, m_scrollPane, t2pBar);
		scrollPaneWithT2PBar.setOneTouchExpandable(true);
		int position = (int) Math.max(m_scrollPane.getSize().getHeight() - 100.0d, 0);
		scrollPaneWithT2PBar.setDividerLocation(position);
		Dimension scrollPaneMinimumSize = new Dimension(0, 0);
		scrollPaneWithT2PBar.setMinimumSize(scrollPaneMinimumSize);
		m_mainSplitPane.setLeftComponent(scrollPaneWithT2PBar);
	}
	
	public void hideT2PBar() {
		if (t2pBar == null) return;
		m_mainSplitPane.setLeftComponent(m_scrollPane);
		t2pBar = null;
	}

    /**
     * @author Svenja label with mouse listener and icon to close the
     *         overviewpanel or the treeviewPanel
     */
    public class HideLabel extends JLabel {

        private static final long serialVersionUID = 1L;

        public HideLabel(Icon icon, final JPanel panel) {
            super(icon);

            this.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

            this.addMouseListener(new MouseListener() {

                public void mouseReleased(MouseEvent e) {
                }

                public void mousePressed(MouseEvent e) {
                }

                public void mouseEntered(MouseEvent arg0) {
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

                public void mouseExited(MouseEvent arg0) {
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }

                public void mouseClicked(MouseEvent e) {
                    if (panel == overviewPanel) {
                        setOverviewPanelVisible(false);
                    } else if (panel == treeviewPanel) {
                        setTreeviewPanelVisible(false);
                    } else if (panel == analysisSideBarPanel) {
                        hideAnalysisBar();
                    } else if (panel == p2tSideBarPanel) {
                        hideP2TBar();
                    } else if (panel == t2pBar) {
                    	hideT2PBar();
                    }
                    propertyChangeSupport.firePropertyChange("Sidebar", null, null);
                }
            });
        }
	}
}