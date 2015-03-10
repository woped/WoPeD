package org.woped.qualanalysis.p2t;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;

import org.woped.core.controller.IEditor;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.paraphrasing.controller.WebServiceThread;
//problems caused by build path conflicts
//import org.woped.metrics.metricsCalculation.MetricHighlighting;
//import org.woped.metrics.metricsCalculation.MetricsUIRequestHandler;

@SuppressWarnings("serial")
public class P2TSideBar extends JPanel implements ActionListener{
//TODO: M-C Trennung durchführen
	private IEditor editor = null;
//	private MetricsUIRequestHandler metricsReqHandler = null;
	private JEditorPane textpane = null;
	private org.woped.qualanalysis.p2t.Process2Text naturalTextParser = null;
	JButton reload = null;
	private WebServiceThread webService;
	private boolean threadInProgress;
	
	public org.woped.qualanalysis.p2t.Process2Text getNaturalTextParser() {
		return naturalTextParser;
	}

	public void setNaturalTextParser(
			org.woped.qualanalysis.p2t.Process2Text naturalTextParser) {
		this.naturalTextParser = naturalTextParser;
	}

	public IEditor getEditor() {
		return editor;
	}
	
    public P2TSideBar(IEditor currentEditor){
    	super();
    	editor = currentEditor;
    	addComponents();
    }

    public void refresh(){
                 	
    }
    
    private void addComponents(){
    	JLabel header = new JLabel(Messages.getString("P2T.textBandTitle"));
    	this.add(header);
    	
    	reload = new JButton("Reload"); //TODO replace with an reload-image
    	reload.addActionListener(this);
    	this.add(reload);
    	
    	this.add(new HideLabel(Messages.getImageIcon("AnalysisSideBar.Cancel"),Messages.getString("Metrics.SideBar.Hide")));
    	
    	textpane =  new JEditorPane();
    	textpane.addHyperlinkListener(new HyperlinkListener() {
			
			@Override
			public void hyperlinkUpdate(HyperlinkEvent hle) {
				if (hle.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
					highlightElement(hle.getDescription());
				}
				
			}

			
		});
		textpane.setAutoscrolls(true);
		textpane.setEditable(false);
    	textpane.setContentType("text/html");
    	this.add(textpane);
    	
    }
    
    private void highlightElement(String ids) {
    	String[] singleIDs = ids.split(",");
    	for (String id : singleIDs){
    		id = id.split("_op_")[0]; //ignore the path option
//    		metricsReqHandler.setHighlight(id);
    	}
	}
    
    //highlights all references to an id within the displayed text
    public void highlightID(String id){
    	if (naturalTextParser != null){
    		String[] textsToHighlight = naturalTextParser.getLinkedTexts(id);
			for (String find : textsToHighlight){
	
				for (int index = 0; index + find.length() < textpane.getText().length(); index++) {
					String match = null;
				
					try {
						match = textpane.getText(index, find.length());
					} catch (BadLocationException e1) {
						break; //the end of the displayed Text is reached
					}
					if (find.equals(match)) {
						textpane.getHighlighter().removeAllHighlights();
						javax.swing.text.DefaultHighlighter.DefaultHighlightPainter highlightPainter = null;
						highlightPainter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
		
						try {
							textpane.getHighlighter().addHighlight(index,index + find.length(), highlightPainter);
						} catch (BadLocationException e) {
							// ignore
						}
					}
				}
			}
    	}
    }
    

	/**
	 * @author Mathias Gruschinske
	 * label with mouse listener and icon to hide the sidebar
	 */
	class HideLabel extends JLabel {

		private static final long serialVersionUID = 1L;

		public HideLabel(Icon icon, String toolTip) {
			super(icon);

			this.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
			this.setToolTipText(toolTip);

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
					editor.hideP2TBar();
				}
			});
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == reload){
			new WebServiceThread(this);
			
			if(JOptionPane.showConfirmDialog(null, Messages.getString("Paraphrasing.Load.Question.Content"), Messages.getString("Paraphrasing.Load.Question.Title"), JOptionPane.YES_NO_OPTION)  == JOptionPane.YES_OPTION){
				
				if(this.getThreadInProgress() == false){
//					this.paraphrasingOutput.setAnimationVisible();

					clean();
					
					this.setThreadInProgress(true);
					webService = new WebServiceThread(this);
					webService.start();
					while(!webService.getIsFinished()){
						try {
							Thread.sleep(500);
						} catch (InterruptedException e1) {
							//ignore
						}
					}
//					DefaultTableModel defaultTableModel = paraphrasingPanel.getParaphrasingOutput().getDefaultTableModel();
//					defaultTableModel.setRowCount(0);
//					String[] tmp = {"", paraphrasingPanel.getP2tInstance().getHtmlText()};						
//					defaultTableModel.addRow(tmp);						
//					paraphrasingPanel.getParaphrasingOutput().updateElementContainer();
					this.textpane.setText(naturalTextParser.getHtmlText());
					webService = null;
				}				
				else{
					JOptionPane.showMessageDialog(null, Messages.getString("Paraphrasing.Webservice.ThreadInProgress.Message"),
							Messages.getString("Paraphrasing.Webservice.Error.Title"), JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
		
	}

	private void setThreadInProgress(boolean b) {
		threadInProgress = b;
		
	}

	private boolean getThreadInProgress() {
		return threadInProgress;
	}
	

//	public MetricsUIRequestHandler getMetricsUIRequestHandler() {
//		return metricsReqHandler;
//	}

//	public void setMetricsUIRequestHandler(MetricsUIRequestHandler metricsReqHandler) {
//		this.metricsReqHandler = metricsReqHandler;
//	}
	
	public void clean() {
//		metricsReqHandler.removeHighlights();
		editor.getGraph().refreshNet();
		editor.getGraph().repaint();
	}

}
