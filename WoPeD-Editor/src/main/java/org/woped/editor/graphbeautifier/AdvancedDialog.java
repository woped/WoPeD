package org.woped.editor.graphbeautifier;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.woped.editor.controller.vc.EditorVC;
import org.woped.gui.lookAndFeel.WopedButton;
import org.woped.gui.translations.Messages;
/**
 * Class <code>AdvancedDialog</code> is a dialog for the beautification with advanced configuration.
 * 
 * @author Team:GraphBeautifier '08
 * @version 0.1
 */
@SuppressWarnings("serial")
public class AdvancedDialog extends JDialog {
	
	private JFrame		frame					= null;
	private JPanel 		outerPanel 				= null;
	private JPanel 		buttonPanel 			= null;
	private JPanel		gridPanel				= null;
	private JPanel 		xPanel 					= null;
	private JPanel 		yPanel 					= null;
	private JPanel		emptyPanel				= null;
	private JPanel 		counterPanel			= null;
	private JPanel 		counterPanelWarning		= null;
	private WopedButton		okButton				= null;
	private WopedButton		cancelButton			= null;
	private EditorVC	editor					= null;
	private JSpinner	counter					= null;
	private JLabel		labelCounter			= new JLabel(Messages.getTitle("GraphBeautifier.Config.Label.Counter"));
	private JLabel		labelCounter2			= new JLabel(Messages.getTitle("GraphBeautifier.Config.Label.Counter2"));
	private JLabel		labelGrid				= new JLabel(Messages.getTitle("GraphBeautifier.Config.Label.Grid"));
	private JLabel		labelX					= new JLabel(Messages.getTitle("GraphBeautifier.Config.Label.X"));
	private JLabel		labelY					= new JLabel(Messages.getTitle("GraphBeautifier.Config.Label.Y"));
	private JSlider		sliderX					= null;
	private JSlider		sliderY					= null;
	private FlowLayout 	innerLayoutXY 			= null;
	private FlowLayout 	innerLayoutCounter 		= null;
	private int			x						= 0;
	private int			y						= 0;
	private int			c						= 0;
	
	public AdvancedDialog(JFrame frame, EditorVC editor){
		super(frame);
		this.frame = frame;
		this.editor = editor;
		initialize();
	}

	private void initialize() {
		this.setTitle(Messages.getTitle("GraphBeautifier.Config"));
		this.setIconImage(Messages.getImageIcon("Application").getImage());
	    this.setSize(new Dimension(340, 340));
	    this.setVisible(true);
	    this.setAlwaysOnTop(false);
	    // center dialog
	    this.setLocationRelativeTo(SwingUtilities.getRoot(frame));
	    // content
	    this.getContentPane().setLayout(new BorderLayout());
	    this.getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);
	    this.getContentPane().add(getOuterPanel(), BorderLayout.CENTER);
	    this.setResizable(false);
	    this.pack();
	}
	
	//############################## Panels ######################################*/
	
	private JPanel getOuterPanel() {
		if (outerPanel == null){
			GridLayout grid = new GridLayout(6,1);
			grid.setHgap(5);
			grid.setVgap(0);
			outerPanel = new JPanel(grid);
			outerPanel.add(getGridPanel());
			outerPanel.add(getXPanel());
			outerPanel.add(getYPanel());
			outerPanel.add(getEmptyPanel());
			outerPanel.add(getCounterPanel());
			outerPanel.add(getcounterPanelWarning());
		}
		return outerPanel;
	}
	
	private JPanel getEmptyPanel() {
		if(emptyPanel== null){
			emptyPanel = new JPanel();
		}
		return emptyPanel;
	}
	
	private JPanel getGridPanel() {
		if(gridPanel == null){
			gridPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			gridPanel.add(getLabelGrid());
		}
		return gridPanel;
	}
	
	private JPanel getCounterPanel() {
		if(counterPanel == null){
			counterPanel = new JPanel(getInnerLayoutCounter());
			counterPanel.add(getLabelCounter());
			counterPanel.add(getCounter());
		}
		return counterPanel;
	}	
	
	private JPanel getcounterPanelWarning() {
		if(counterPanelWarning == null){
			counterPanelWarning = new JPanel(getInnerLayoutCounter());
			counterPanelWarning.add(getLabelCounter2());
		}
		return counterPanelWarning;
	}	
	
	private JPanel getXPanel() {
		if(xPanel == null){
			xPanel = new JPanel(getInnerLayoutXY());
			xPanel.add(labelX);
			xPanel.add(getSliderX());
		}
		return xPanel;
	}		
	
	private JPanel getYPanel() {
		if(yPanel == null){
			yPanel = new JPanel(innerLayoutXY);
			yPanel.add(labelY);
			yPanel.add(getSliderY());
		}
		return yPanel;
	}
	
	private JPanel getButtonPanel() {
		if (buttonPanel == null)
        {
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(1, 2));
            JPanel innerPanelLeft = new JPanel();
            innerPanelLeft.setLayout(new FlowLayout(FlowLayout.LEFT));
            JPanel innerPanelRight = new JPanel();
            innerPanelRight.setLayout(new FlowLayout(FlowLayout.RIGHT));
            innerPanelRight.add(getOkButton());
            innerPanelRight.add(getCancelButton());
            buttonPanel.add(innerPanelLeft);
            buttonPanel.add(innerPanelRight);
        }
        return buttonPanel;
	}
	
	
	//################################# Layouts #################################*/
	
	private LayoutManager getInnerLayoutCounter() {
		if(innerLayoutCounter == null){
			innerLayoutCounter = new FlowLayout(FlowLayout.LEFT,5,5);
		}
		return innerLayoutCounter;
	}
	
	private LayoutManager getInnerLayoutXY() {
		if(innerLayoutXY == null){
			innerLayoutXY = new FlowLayout(FlowLayout.LEFT,5,5);
		}
		return innerLayoutXY;
	}
	
	
	//############################### Inner Items ###############################*/
	
	private JSpinner getCounter() {
		if(counter == null){
			counter = new JSpinner(new SpinnerNumberModel(100,50,5000,10));
			counter.addChangeListener(new ChangeListener(){
				public void stateChanged(ChangeEvent arg0) {
					c = ((SpinnerNumberModel) counter.getModel()).getNumber().intValue();
				}				
			});
		}
		return counter;
	}
	
	private JSlider getSliderX() {
		if(sliderX == null){
			sliderX = new JSlider(30,100,80);
			sliderX.setMajorTickSpacing(10);
			sliderX.setPaintTicks(true); 
			sliderX.setPaintLabels(true);
			sliderX.setSnapToTicks(true);
			sliderX.setSize(100, 5);
			sliderX.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					x = sliderX.getValue();
				}
			});
		}
		return sliderX;
	}
	
	private JSlider getSliderY() {
		if (sliderY == null){
			sliderY = new JSlider(30,100,80);
			sliderY.setMajorTickSpacing(10);
			sliderY.setPaintTicks(true); 
			sliderY.setPaintLabels(true);
			sliderY.setSnapToTicks(true);
			sliderY.setSize(100, 5);
			sliderY.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					y = sliderY.getValue();
				}
			});
		}
		return sliderY;
	}
	
	private JLabel getLabelGrid() {
		if(labelGrid == null){
			labelGrid = new JLabel("Grid:");
		}
		labelGrid.setSize(50, 5);
		return labelGrid;
	}
	
	private JLabel getLabelCounter() {
		if(labelCounter == null){
			labelCounter = new JLabel("Forgiveness Counter:");
			labelCounter.setSize(10, 5);
		}
		return labelCounter;
	}
	
	private Component getLabelCounter2() {
		return labelCounter2;
	}

	//################################# Buttons ################################*/
	
	private JButton getCancelButton() {
		if (cancelButton == null)
        {
            cancelButton = new WopedButton();
            cancelButton.setMnemonic(Messages.getMnemonic("Button.Cancel"));
            cancelButton.setText(Messages.getTitle("Button.Cancel"));
            cancelButton.setIcon(Messages.getImageIcon("Button.Cancel"));
            cancelButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent arg0)
                {
                	AdvancedDialog.this.dispose();
                }
            });
        }
        return cancelButton;
	}
	private JButton getOkButton() {
		if (okButton == null)
        {
            okButton = new WopedButton();
            okButton.setText(Messages.getTitle("Button.Ok"));
            okButton.setIcon(Messages.getImageIcon("Button.Ok"));
            okButton.setMnemonic(Messages.getMnemonic("Button.Ok"));
            okButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent arg0)
                {
                	editor.startBeautify(x,y,c);
                    AdvancedDialog.this.dispose();
                }
            });
        }
        return okButton;
	}
}
