package org.woped.metrics.helpers;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTextField;

public class WatermarkedJTextField extends JTextField{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3808188968710167707L;
	private Font cFont;
	private String watermark;
	
	public void setText(String text){
		if(text == null || text.length() == 0){
			watermarkMode(true,true);
			return;
		}
		watermarkMode(false,false);
		super.setText(text);
	}
	
	public WatermarkedJTextField(String watermark){
		this.watermark = watermark;
		cFont = getFont();
		super.setText(watermark);
		
		final String water = watermark;
		
		watermarkMode(true, true);
		
		this.addKeyListener(new KeyAdapter(){

			public void keyPressed(KeyEvent arg0) {
				if(getText().equals(water))
					watermarkMode(false, true);
			}
			
		});
		
		this.addMouseListener(new MouseAdapter(){

			public void mouseReleased(MouseEvent arg0) {
				watermarkMode(false, getText().equals(water));
			}
			
		});
		
		this.addFocusListener(new FocusListener(){

			public void focusLost(FocusEvent arg0) {
				if(getText().length() == 0)
					watermarkMode(true, true);
			}

			public void focusGained(FocusEvent arg0) {
				if(getText().equals(water))
					watermarkMode(false, true);
			}
			
		});
	}
	
	public void setFont(Font f){
		cFont = f;
		super.setFont(f);
	}
	
	private void watermarkMode(boolean isWater, boolean overwrite){
		if(isWater){
			setForeground(Color.BLUE);
			super.setFont(new Font(cFont.getFontName(),Font.ITALIC,cFont.getSize()));
			super.setText(watermark);
			repaint();
		}
		else{
			if(overwrite) super.setText("");
			setForeground(Color.BLACK);
			super.setFont(cFont);
			repaint();
		}
	}
	
}
