package org.woped.qualanalysis.reachabilitygraph.gui;

import java.awt.Checkbox;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.apache.log4j.Hierarchy;
import org.woped.core.utilities.LoggerManager;
import org.woped.qualanalysis.Constants;
import org.woped.translations.Messages;

public class ReachabilitySettingsDialog extends JDialog {
	
	ReachabilityGraphPanel rgp = null;
	
	// Buttons
	JButton saveButton = null;
	JButton cancelButton = null;
	
	// Labels
	JLabel graphVisual = null;
	JLabel hierarchicLabel = null;
	JLabel placeWidthLabel = null;
	JLabel placeHeightLabel = null;
	JLabel hierarchicSpaceVerticalLabel = null;
	JLabel hierarchicSpaceHorizontalLabel = null;
	
	// Textfields
	JTextField placeWidthTf = null;
	JTextField placeHeightTf = null;
	JTextField hierarchicSpaceHorizontalTf = null;
	JTextField hierarchicSpaceVerticalTf = null;
	
	// RadioButtons
	JRadioButton grayGraphRb = null;
	JRadioButton colorGraphRb = null;
	
	// Checkboxes
	JCheckBox parallelRoutingCb = null;
	
	// GraphAttributes
	HashMap<String, String> graphAttributes = null;
	
	public ReachabilitySettingsDialog(ReachabilityGraphPanel rgp){
		this.rgp = rgp;
		graphAttributes = rgp.getGraph().getAttributeMap();
		initComponents();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setTitle(Messages.getString("QuanlAna.ReachabilityGraph.Settings.Title"));
		int height = 340;
		int width = 240;
		this.setSize(new Dimension(width,height));
		Point location = new Point((int)rgp.getLocationOnScreen().getX() + rgp.getWidth() / 2 - width / 2, 
								(int)rgp.getLocationOnScreen().getY() + rgp.getHeight() / 2 - height / 2);
		this.setLocation(location);
		this.setResizable(true);
		this.setModal(true);
	}
	
	private void initComponents(){
		graphVisual = new JLabel(Messages.getString("QuanlAna.ReachabilityGraph.Settings.GraphSection"));
		// Rect: (links->rechts,oben->unten,breite,hšhe)
		graphVisual.setBounds(new Rectangle(20, 15, 220, 16));
		
		grayGraphRb = new JRadioButton(Messages.getString("QuanlAna.ReachabilityGraph.Settings.Color.Grayscale"));
		grayGraphRb.setBounds(new Rectangle(30, 39, 110, 22));
		colorGraphRb = new JRadioButton(Messages.getString("QuanlAna.ReachabilityGraph.Settings.Color.Colored"));
		colorGraphRb.setBounds(new Rectangle(140, 39, 140, 22));
		ButtonGroup colorButtonGroup = new ButtonGroup(); 
		colorButtonGroup.add(grayGraphRb);
		colorButtonGroup.add(colorGraphRb);
		if(getColored()){
			colorGraphRb.setSelected(true);	
		} else {
			grayGraphRb.setSelected(true);
		}
		
		parallelRoutingCb = new JCheckBox(Messages.getString("QuanlAna.ReachabilityGraph.Settings.ParallelRouting"));
		parallelRoutingCb.setSelected(getParallelRoutingEnabled());
		parallelRoutingCb.setBounds(new Rectangle(30,69,250,22));
		
		placeHeightLabel = new JLabel(Messages.getString("QuanlAna.ReachabilityGraph.Settings.Place.Height"));
		placeHeightLabel.setBounds(new Rectangle(30,105,150,16));
		placeHeightTf = new JTextField();
		placeHeightTf.setText(Integer.toString(getPlaceHeight()));
		placeHeightTf.setBounds(new Rectangle(170,99,50,28));
		
		placeWidthLabel = new JLabel(Messages.getString("QuanlAna.ReachabilityGraph.Settings.Place.Width"));
		placeWidthLabel.setBounds(new Rectangle(30,135,150,16));
		placeWidthTf = new JTextField();
		placeWidthTf.setText(Integer.toString(getPlaceWidth()));
		placeWidthTf.setBounds(new Rectangle(170,129,50,28));
		
		hierarchicLabel = new JLabel(Messages.getString("QuanlAna.ReachabilityGraph.Settings.HierarchicSection"));
		hierarchicLabel.setBounds(new Rectangle(20,169,220,16));
		
		hierarchicSpaceHorizontalLabel = new JLabel(Messages.getString("QuanlAna.ReachabilityGraph.Settings.Hierarchic.Horizontal"));
		hierarchicSpaceHorizontalLabel.setBounds(new Rectangle(30,203,150,16));
		hierarchicSpaceHorizontalTf = new JTextField();
		hierarchicSpaceHorizontalTf.setText(Integer.toString(getHierarchicSpacingHorizontal()));
		hierarchicSpaceHorizontalTf.setBounds(new Rectangle(170,197,50,28));
		
		hierarchicSpaceVerticalLabel = new JLabel(Messages.getString("QuanlAna.ReachabilityGraph.Settings.Hierarchic.Vertical"));
		hierarchicSpaceVerticalLabel.setBounds(new Rectangle(30,231,150,16));
		hierarchicSpaceVerticalTf = new JTextField();
		hierarchicSpaceVerticalTf.setText(Integer.toString(getHierarchicSpacingVertical()));
		hierarchicSpaceVerticalTf.setBounds(new Rectangle(170,225,50,28));
		
		saveButton = new JButton(Messages.getString("QuanlAna.ReachabilityGraph.Settings.Button.Save"));
		saveButton.setBounds(new Rectangle(20,273,90,29));
		saveButton.addActionListener(new SaveButtonListener(rgp));
		cancelButton = new JButton(Messages.getString("QuanlAna.ReachabilityGraph.Settings.Button.Cancel"));
		cancelButton.setBounds(new Rectangle(120,273,90,29));
		cancelButton.addActionListener(new CancelButtonListener());
		
		this.setLayout(null);
		this.add(graphVisual);
		this.add(grayGraphRb);
		this.add(colorGraphRb);
		this.add(parallelRoutingCb);
		this.add(hierarchicLabel);
		this.add(placeHeightLabel);
		this.add(placeHeightTf);
		this.add(placeWidthLabel);
		this.add(placeWidthTf);
		this.add(hierarchicSpaceHorizontalLabel);
		this.add(hierarchicSpaceHorizontalTf);
		this.add(hierarchicSpaceVerticalLabel);
		this.add(hierarchicSpaceVerticalTf);
		this.add(saveButton);
		this.add(cancelButton);
	}

	private boolean getParallelRoutingEnabled(){
		boolean enabled;
		
		if(graphAttributes.containsKey("reachabilityGraph.parallel")){
			if(graphAttributes.get("reachabilityGraph.parallel").equals("true")){
				enabled = true;
			} else if  (graphAttributes.get("reachabilityGraph.parallel").equals("false")){
				enabled = false;
			} else {
				enabled = true;
			}
		} else {
			return true;
		}
		return enabled;
	}
	
	private void setParallelRoutingEnabled(boolean enabled){
		String enabledStr = "";
		
		if(enabled){
			enabledStr = "true";
		} else {
			enabledStr = "false";
		}
		
		if(graphAttributes.containsKey("reachabilityGraph.parallel")){
			graphAttributes.put("reachabilityGraph.parallel", enabledStr);
		} else {
			graphAttributes.put("reachabilityGraph.parallel", enabledStr);
		}
	}
	
	private int getHierarchicSpacingVertical(){
		if(graphAttributes.containsKey("reachabilityGraph.hierarchic.verticalSpace")){
			return Integer.parseInt(graphAttributes.get("reachabilityGraph.hierarchic.verticalSpace"));
		} else {
			return 60; // default
		}
	}
	
	private void setHierarchicSpacingVertical(int vertical){
		if(vertical > 0 && vertical < 10000){
			graphAttributes.put("reachabilityGraph.hierarchic.verticalSpace", Integer.toString(vertical));	
		} else {
			graphAttributes.put("reachabilityGraph.hierarchic.verticalSpace", "60");	
		}
	}
	
	private int getHierarchicSpacingHorizontal(){
		if(graphAttributes.containsKey("reachabilityGraph.hierarchic.horizontalSpace")){
			return Integer.parseInt(graphAttributes.get("reachabilityGraph.hierarchic.horizontalSpace"));
		} else {
			return 20; // default
		}
	}
	
	private void setHierarchicSpacingHorizontal(int horizontal){
		if(horizontal > 0 && horizontal < 10000){
			graphAttributes.put("reachabilityGraph.hierarchic.horizontalSpace", Integer.toString(horizontal));	
		} else {
			graphAttributes.put("reachabilityGraph.hierarchic.horizontalSpace", "20");	
		}
	}
	
	private int getPlaceWidth(){
		if(graphAttributes.containsKey("reachabilityGraph.place.width")){
			return Integer.parseInt(graphAttributes.get("reachabilityGraph.place.width"));
		} else {
			return 80; // default
		}
	}
	
	private void setPlaceWidth(int width){
		if(width > 0 && width < 1000){
			graphAttributes.put("reachabilityGraph.place.width", Integer.toString(width));	
		} else {
			graphAttributes.put("reachabilityGraph.place.width", "80");	// default
		}
	}
	
	private int getPlaceHeight(){
		if(graphAttributes.containsKey("reachabilityGraph.place.height")){
			return Integer.parseInt(graphAttributes.get("reachabilityGraph.place.height"));
		} else {
			return 20; // default
		}
	}
	
	private void setPlaceHeight(int height){
		if(height > 0 && height < 200){
			graphAttributes.put("reachabilityGraph.place.height", Integer.toString(height));	
		} else {
			graphAttributes.put("reachabilityGraph.place.height", "20"); // default
		}
	}
	
	private boolean getColored(){
		boolean isColored = true;
		
		if(graphAttributes.containsKey("reachabilityGraph.color")){
			if(graphAttributes.get("reachabilityGraph.color").equals("true")){
				isColored = true;
			} else if  (graphAttributes.get("reachabilityGraph.color").equals("false")){
				isColored = false;
			} else {
				isColored = true;
			}
		} else {
			return true;
		}
		return isColored;
	}
	
	private void setColor(boolean isColored){
		String enabledStr = "";
		
		if(isColored){
			enabledStr = "true";
		} else {
			enabledStr = "false";
		}
		
		if(graphAttributes.containsKey("reachabilityGraph.color")){
			graphAttributes.put("reachabilityGraph.color", enabledStr);
		} else {
			graphAttributes.put("reachabilityGraph.color", enabledStr);
		}
	}
	
	class CancelButtonListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			ReachabilitySettingsDialog.this.dispose();
		}
		
	}
	
	class SaveButtonListener implements ActionListener{

		ReachabilityGraphPanel rgp = null;
		
		public SaveButtonListener(ReachabilityGraphPanel rgp){
			this.rgp = rgp;
		}
		
		public void actionPerformed(ActionEvent e) {
			
			if(integerValueChecker(ReachabilitySettingsDialog.this.hierarchicSpaceHorizontalTf.getText())){
				setHierarchicSpacingHorizontal(Integer.parseInt(ReachabilitySettingsDialog.this.hierarchicSpaceHorizontalTf.getText()));	
			} else {
				// do default
			}
			
			if(integerValueChecker(ReachabilitySettingsDialog.this.hierarchicSpaceVerticalTf.getText())){
				setHierarchicSpacingVertical(Integer.parseInt(ReachabilitySettingsDialog.this.hierarchicSpaceVerticalTf.getText()));	
			} else {
				// do default
			}
			
			if(integerValueChecker(ReachabilitySettingsDialog.this.placeWidthTf.getText())){
				setPlaceWidth(Integer.parseInt(ReachabilitySettingsDialog.this.placeWidthTf.getText()));	
			} else {
				// do default
			}
			
			if(integerValueChecker(ReachabilitySettingsDialog.this.placeHeightTf.getText())){
				setPlaceHeight(Integer.parseInt(ReachabilitySettingsDialog.this.placeHeightTf.getText()));	
			} else {
				// do default
			}
			
			if(getParallelRoutingEnabled() != ReachabilitySettingsDialog.this.parallelRoutingCb.isSelected()){
				setParallelRoutingEnabled(ReachabilitySettingsDialog.this.parallelRoutingCb.isSelected());
				rgp.setParallelRouting(getParallelRoutingEnabled());
			}
			
			if(getColored() != ReachabilitySettingsDialog.this.colorGraphRb.isSelected()){
				setColor(ReachabilitySettingsDialog.this.colorGraphRb.isSelected());
				rgp.setGrayScale(!getColored());
			}
			rgp.layoutGraph(rgp.getSelectedType(), false);
			ReachabilitySettingsDialog.this.dispose();
		}

		private boolean integerValueChecker(String integer){
			try{
				Integer.parseInt(integer);
				return true;
			} catch (NumberFormatException nfe) {
				return false;
			}
		}
	}
}


