package org.woped.qualanalysis.coverabilitygraph.gui;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.IEditor;
import org.woped.core.gui.IEditorFrame;
import org.woped.core.gui.IUserInterface;
import org.woped.core.utilities.FileFilterImpl;
import org.woped.core.utilities.LoggerManager;
import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.Constants;
import org.woped.qualanalysis.coverabilitygraph.assistant.CoverabilityGraphAssistantView;
import org.woped.qualanalysis.coverabilitygraph.events.CoverabilityGraphEventListener;
import org.woped.qualanalysis.coverabilitygraph.gui.dialogs.CoverabilityGraphSettingsDialog;
import org.woped.qualanalysis.coverabilitygraph.gui.dialogs.CoverabilityGraphWarning;
import org.woped.qualanalysis.coverabilitygraph.gui.views.CoverabilityGraphResultView;
import org.woped.qualanalysis.soundness.marking.IMarking;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/**
 * This class manages the internal frame that is in charge of displaying the coverability graph.
 * <p>
 * It is a WoPeD design decision that maximum one coverability graph is visible at the same time.
 * That is why this class has been implemented as singleton.
 * <p>
 * This class holds a view controller for each coverability graph that has been generated in the current session.
 * It is responsible for displaying the related graph when an editor frame has been activated
 * and displaying dialogs related to the coverability graph.
 **/
public class CoverabilityGraphFrameController extends JInternalFrame {

    private static final long serialVersionUID = 1L;
    private static CoverabilityGraphFrameController myInstance = null;

    private IUserInterface userInterface;
    private Map<IEditor, CoverabilityGraphVC> viewController;
    private IEditor activatedNet;
    private GraphEventTrigger eventTrigger;
    private GraphHighlightListener highlightListener;

    /**
     * Gets the responsible instance for the provided user interface.
     *
     * @param dui the active user interface
     * @return the responsible instance for this user interface
     */
    public static CoverabilityGraphFrameController getInstance(IUserInterface dui) {
        if (CoverabilityGraphFrameController.myInstance == null) {
            CoverabilityGraphFrameController.myInstance = new CoverabilityGraphFrameController(dui);
        }
        return myInstance;
    }

    private CoverabilityGraphFrameController(IUserInterface userInterface) {
        super();
        this.userInterface = userInterface;

        initialize();
    }

    /**
     * Returns if the coverability graph for the petri net in the provided editor has been generated.
     *
     * @param editor the editor of the petri net to check
     * @return true if the graph has been generated, otherwise false
     */
    public boolean containsGraphForNet(IEditor editor){
        return viewController.keySet().contains(editor);
    }

    /**
     * Removes all highlighting from the graph for the provided petri net.
     *
     * @param editor the editor of the petri net
     */
    public void removeHighlightingFromGraph(IEditor editor){
        CoverabilityGraphVC graphVC = viewController.get(editor);

        if(graphVC == null) return;
        graphVC.removeHighlighting();
    }

    /**
     * Highlight all markings which covers the provide marking in the coverability graph
     * of the provided petri net.
     *
     * @param editor the editor of the petri net
     * @param marking the marking to highlight
     */
    public void highlightMarking(IEditor editor, IMarking marking){
        CoverabilityGraphVC graphVC = viewController.get(editor);
        if(graphVC == null) return;
        graphVC.highlightMarking(marking);
    }

    /**
     * Adds the specified graph event listener to receive events from the active coverability graph.
     *
     * @param listener the graph listener
     */
    void addGraphEventListener(CoverabilityGraphEventListener listener) {
        eventTrigger.addEventListener(listener);
    }

    /**
     * Activates the coverability graph for the petri net.
     *
     * @param editor the editor of the petri net
     */
    void showGraph(IEditor editor) {

        if (editor == null) return;

        if (editor.isTokenGameEnabled()) {
            showTokenGameRunningWarning();
            return;
        }

        if (!viewController.containsKey(editor)) createViewController(editor);

        CoverabilityGraphVC activeGraph = getActiveGraph();
        if (activeGraph != null) {
            activeGraph.removeHighlighting();
            this.remove(activeGraph);
        }

        this.activatedNet = editor;

        activeGraph = getActiveGraph();
        this.add(activeGraph, BorderLayout.CENTER);
        this.setTitle(Messages.getString("ToolBar.ReachabilityGraph.Title") + " - " + editor.getName());

        refresh();
    }

    /**
     * Forces the active graph view controller to synchronize with its underlying petri net.
     */
    void synchronizeEditor() {
        CoverabilityGraphVC activeGraph = getActiveGraph();
        if (activeGraph == null) return;

        activeGraph.reset();
        eventTrigger.fireEditorSyncEstablishedEvent();
    }

    /**
     * Gets the view controller of the currently active coverability graph.
     *
     * @return the view controller of the active coverability graph
     */
    CoverabilityGraphVC getActiveGraph() {
        return viewController.get(activatedNet);
    }

    /**
     * Closes the coverability graph for the provided petri net.
     *
     * @param editor the edition of the petri net
     */
    void removeGraph(IEditor editor){
        removeViewController(editor);
    }

    /**
     * Shows the settings dialog for the active coverability graph
     */
    void showSettingsDialog() {
        CoverabilityGraphVC activePanel = getActiveGraph();
        CoverabilityGraphSettingsDialog dialog = new CoverabilityGraphSettingsDialog(activePanel);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    /**
     * Enables the coverability graph result view for the current petri net
     */
    void enableResultView() {
        enableResultView(activatedNet);
    }

    /**
     * Enables the coverability graph assistant view for the current petri net
     */
    void enableAssistantView() {
        CoverabilityGraphVC activeGraph = getActiveGraph();
        if (activeGraph == null) return;

        if (activatedNet.isTokenGameEnabled()) {
            showTokenGameRunningWarning();
            return;
        }

        if (!activeGraph.containsView(CoverabilityGraphAssistantView.VIEW_NAME)) {
            CoverabilityGraphAssistantView assistantView = new CoverabilityGraphAssistantView(activatedNet);
            activeGraph.registerView(CoverabilityGraphAssistantView.VIEW_NAME, assistantView);
        }

        activeGraph.activateView(CoverabilityGraphAssistantView.VIEW_NAME);
    }

    /**
     * Exports the current coverability graph as image
     */
    void exportImage() {
        CoverabilityGraphVC activeGraph = getActiveGraph();
        if (activeGraph == null) return;

        // Set the graph scale to 150% to increase quality
        CoverabilityGraph graph = activeGraph.getGraph();
        double previousScale = graph.getScale();
        graph.setScale(1.5);

        ExportImageDialog dlg = new ExportImageDialog();
        dlg.showSaveDialog(this);

        if (dlg.getSelectedFile() == null) return;
        FileFilterImpl fileFilter = (FileFilterImpl) dlg.getFileFilter();
        File file = null;
        try {
            String path = dlg.getSelectedFile().getCanonicalPath();
            String extension = fileFilter.getDefaultExtension();
            file = new File(String.format("%s.%s", path, extension));
        } catch (IOException e) {
            e.printStackTrace();
        }


        RenderedImage image = ImageExport.getRenderedImage(activeGraph);
        switch (fileFilter.getFilterType()) {
            case FileFilterImpl.PNGFilter:
                ImageExport.savePNG(image, file);
                break;
            case FileFilterImpl.JPGFilter:
                ImageExport.saveJPG(image, file);
                break;
            case FileFilterImpl.BMPFilter:
                ImageExport.saveBMP(image, file);
                break;
            default:
                LoggerManager.warn(Constants.QUALANALYSIS_LOGGER, "Unable to save File. Unknown file type: " + fileFilter.getFilterType());
        }

        graph.setScale(previousScale);
    }

    /**
     * Sets the view controller mapping for testing purposes.
     *
     * @param testViewController the view controller mapping
     */
    void setViewControllerForTesting(Map<IEditor, CoverabilityGraphVC> testViewController){
        this.viewController = testViewController;
    }

    private void initialize() {
        LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-> init() " + this.getClass().getName());
        this.eventTrigger = new GraphEventTrigger();
        this.highlightListener = new GraphHighlightListener();

        this.setFrameIcon(Messages.getImageIcon("ToolBar.ReachabilityGraph"));
        Dimension dimension = new Dimension(400, 300);
        this.setSize(dimension);
        this.setMinimumSize(dimension);
        this.setTitle(Messages.getString("ToolBar.ReachabilityGraph.Title"));
        this.setClosable(true);
        this.setResizable(true);
        this.setMaximizable(true);
        this.setIconifiable(false);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);

        this.viewController = new HashMap<>();
        this.addInternalFrameListener(new GraphFrameListener());

        JDesktopPane desktop = (JDesktopPane) userInterface.getPropertyChangeSupportBean();
        desktop.add(this);

        LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- init() " + this.getClass().getName());
    }

    private void close() {

        try {
            this.setSelected(false);
            this.hide();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    private void refresh() {
        if (!isVisible())
            setVisible(true);

        this.validate();
        this.repaint();
    }

    private void createViewController(IEditor editor) {

        @SuppressWarnings("SpellCheckingInspection")
        CoverabilityGraphVC cgvc = new CoverabilityGraphVC(editor);
        viewController.put(editor, cgvc);
        JInternalFrame editorFrame = (JInternalFrame) getEditorFrame(editor);

        if (editorFrame != null)
            editorFrame.addInternalFrameListener(new EditorFrameListener(editor));

        cgvc.addGraphHighlightListener(this.highlightListener);

        enableResultView(editor);
        eventTrigger.fireEditorSyncEstablishedEvent();
        eventTrigger.fireGraphHighlightingRemovedEvent();
    }

    private void enableResultView(IEditor editor) {
        CoverabilityGraphVC graphVC = viewController.get(editor);
        if (graphVC == null) return;

        if (!graphVC.containsView(CoverabilityGraphResultView.VIEW_NAME)) {
            CoverabilityGraphResultView resultView = new CoverabilityGraphResultView(editor);
            graphVC.registerView(CoverabilityGraphResultView.VIEW_NAME, resultView);
        }

        graphVC.activateView(CoverabilityGraphResultView.VIEW_NAME);
    }

    private IEditorFrame getEditorFrame(IEditor editor) {
        IEditorFrame result = null;

        JDesktopPane desktop = (JDesktopPane) userInterface.getPropertyChangeSupportBean();
        JInternalFrame[] allFrames = desktop.getAllFrames();
        for (JInternalFrame frame : allFrames) {

            if (!(frame instanceof IEditorFrame)) continue;
            IEditorFrame editorFrame = (IEditorFrame) frame;
            if (editorFrame.getEditor().equals(editor)) {
                result = editorFrame;
            }
        }

        return result;
    }

    private void removeViewController(IEditor editor) {
        if (!viewController.containsKey(editor)) return;

        CoverabilityGraphVC activeGraph = getActiveGraph();
        CoverabilityGraphVC controller = viewController.remove(editor);
        if (controller != null && activeGraph.equals(controller)) {
            controller.removeHighlighting();
            this.remove(controller);
        }

        if(activatedNet.equals(editor)){
            activatedNet = null;
        }

        if (viewController.isEmpty()) {
            this.close();
        } else {
            showGraph(viewController.keySet().iterator().next());
        }
    }

    private void removeHighlightingFromActiveGraph() {
        CoverabilityGraphVC vc = getActiveGraph();
        if (vc != null) vc.removeHighlighting();
    }

    private void showTokenGameRunningWarning() {
        JDesktopPane desktop = (JDesktopPane) userInterface.getPropertyChangeSupportBean();
        //noinspection SpellCheckingInspection
        CoverabilityGraphWarning.showReachabilityWarning(desktop, "QuanlAna.ReachabilityGraph.SimulationWarning");
    }

    private void showGraphOutOfSyncWarning() {
        //noinspection SpellCheckingInspection
        CoverabilityGraphWarning.showReachabilityWarning(this, "QuanlAna.ReachabilityGraph.RefreshWarning");
    }

    private void updateSyncState() {
        CoverabilityGraphVC activeGraph = getActiveGraph();
        if (activeGraph != null) {
            if (activeGraph.isGraphOutOfSync()) {
                showGraphOutOfSyncWarning();
                eventTrigger.fireEditorSyncLostEvent();
            } else {
                eventTrigger.fireEditorSyncEstablishedEvent();
            }
        }
    }

    private void ensureTokenGameNotRunning() {
        CoverabilityGraph graph = getActiveGraph().getGraph();

        if (activatedNet.isTokenGameEnabled()) {
            graph.setEnabled(false);
            this.getActiveGraph().setBorder(BorderFactory.createLineBorder(Color.red, 3));
            showTokenGameRunningWarning();
        }

        else if(!graph.isEnabled()){
            graph.setEnabled(true);
            this.getActiveGraph().setBorder(BorderFactory.createEmptyBorder());
        }
    }

    /**
     * A file chooser dialog with set file filters.
     */
    private class ExportImageDialog extends JFileChooser {

        ExportImageDialog() {
            super();

            this.setDialogTitle(Messages.getString("Action.Export.Title"));
            setHomeDir();
            addFilters();
        }

        private void setHomeDir() {
            String homePath = ConfigurationManager.getConfiguration().getHomedir();
            if (homePath == null) return;

            File homeDir = new File(homePath);
            if (Files.isDirectory(homeDir.toPath())) this.setCurrentDirectory(homeDir);
        }

        private void addFilters() {
            addBmpFilter();
            addJpgFilter();
            addPngFilter();
        }

        private void addJpgFilter() {
            Vector<String> jpgExtensions = new Vector<>();
            jpgExtensions.add("jpg");
            jpgExtensions.add("jpeg");

            FileFilterImpl JPGFilter = new FileFilterImpl(FileFilterImpl.JPGFilter, "JPG (*.jpg)", jpgExtensions);
            this.setFileFilter(JPGFilter);
        }

        private void addBmpFilter() {
            Vector<String> bmpExtensions = new Vector<>();
            bmpExtensions.add("bmp");

            FileFilterImpl BMPFilter = new FileFilterImpl(FileFilterImpl.BMPFilter, "BMP (*.bmp)", bmpExtensions);
            this.setFileFilter(BMPFilter);
        }

        private void addPngFilter() {
            Vector<String> pngExtensions = new Vector<>();
            pngExtensions.add("png");
            FileFilterImpl PNGFilter = new FileFilterImpl(FileFilterImpl.PNGFilter, "PNG (*.png)", pngExtensions);
            this.setFileFilter(PNGFilter);
        }

    }

    /**
     * This class listens to the frame events of the coverability graph frame
     */
    private class GraphFrameListener extends InternalFrameAdapter {

        @Override
        public void internalFrameClosing(InternalFrameEvent e) {
            removeHighlightingFromActiveGraph();
            userInterface.refreshFocusOnFrames();
        }

        @Override
        public void internalFrameActivated(InternalFrameEvent e) {
            updateSyncState();

            ensureTokenGameNotRunning();
        }

        @Override
        public void internalFrameDeactivated(InternalFrameEvent e) {
            removeHighlightingFromActiveGraph();
        }

    }

    /**
     * This class listens to the frame events of the petri net editor.
     */
    private class EditorFrameListener extends InternalFrameAdapter {
        private IEditor editor;

        EditorFrameListener(IEditor editor) {
            this.editor = editor;
        }

        @Override
        public void internalFrameActivated(InternalFrameEvent e) {
            showGraph(editor);
        }

        @Override
        public void internalFrameClosed(InternalFrameEvent e) {
            removeViewController(editor);
        }

    }

    /**
     * This class triggers graph events.
     */
    private class GraphEventTrigger {

        private Collection<CoverabilityGraphEventListener> listeners;

        GraphEventTrigger() {
            this.listeners = new LinkedList<>();
        }

        void addEventListener(CoverabilityGraphEventListener listener) {
            this.listeners.add(listener);
        }

        void fireEditorSyncLostEvent() {
            for (CoverabilityGraphEventListener listener : listeners) {
                listener.editorSyncLost();
            }
        }

        void fireEditorSyncEstablishedEvent() {
            for (CoverabilityGraphEventListener listener : listeners) {
                listener.editorSyncEstablished();
            }
        }

        void fireGraphHighlightingAddedEvent() {
            for (CoverabilityGraphEventListener listener : listeners) {
                listener.graphHighlightingAdded();
            }
        }

        void fireGraphHighlightingRemovedEvent() {
            for (CoverabilityGraphEventListener listener : listeners) {
                listener.graphHighlightingRemoved();
            }
        }
    }

    /**
     * This class listens for graph highlight events
     */
    private class GraphHighlightListener implements HighlightListener {

        /**
         * Invoked when a highlight has been added to the graph
         *
         * @param source the view controller of the highlighted graph
         */
        @Override
        public void highlightAdded(CoverabilityGraphVC source) {
            CoverabilityGraphVC activeGraph = getActiveGraph();
            if (activeGraph != null && activeGraph.equals(source))
                eventTrigger.fireGraphHighlightingAddedEvent();
        }

        /**
         * Invoked when the highlighting has been removed from the graph
         *
         * @param source the view controller of the highlighted graph
         */
        @Override
        public void highlightRemoved(CoverabilityGraphVC source) {
            CoverabilityGraphVC activeGraph = getActiveGraph();
            if (activeGraph != null && activeGraph.equals(source))
                eventTrigger.fireGraphHighlightingRemovedEvent();
        }
    }
}
