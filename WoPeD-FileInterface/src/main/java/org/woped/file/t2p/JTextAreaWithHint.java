package org.woped.file.t2p;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JTextArea;

public class JTextAreaWithHint extends JTextArea {
	private LineBreakMeasurer lineMeasurer;
	private int pStart;
	private int pEnd;
	private AttributedString aString;
	private String hintString;
	
	private ArrayList<HintCache> caches = new ArrayList<>();
	private Dimension sizeCache;
	
	private int repaintCount = 0;
	private boolean wasDrawingHint = false;
	
	private boolean wasCursorPrevVisible = false;
	
	public JTextAreaWithHint() {
		super();
		
		addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				repaint();
			}
			@Override
			public void focusGained(FocusEvent e) {
				repaint();
			}
		});
	}

	public void changeHintText(String text) {
		Hashtable<TextAttribute, Object> map = new Hashtable<>();
		map.put(TextAttribute.FONT, getFont());
		map.put(TextAttribute.SIZE, (float)getFont().getSize());
		hintString = text;
		aString = new AttributedString(text, map);
		sizeCache = getSize();
		caches.clear();
	}
	
	@Override
	public void paint(Graphics g) {
		boolean isTextEmpty = getText().isEmpty();
		boolean sizeChanged = !getSize().equals(sizeCache);
		sizeCache = getSize();
		
		if (isTextEmpty && (sizeChanged || caches.isEmpty())) {
			if (aString == null) return;
			// reducing repaint draw calls
			if (wasDrawingHint == false) {
				repaintCount = 0;
				wasDrawingHint = true;
			}
			caches.clear();
			clearCanvas(g);
			paintCursor(g);
			
			g.setColor(Color.gray);
			Graphics2D g2d = applyAntialias((Graphics2D)g);
		    
		    if (lineMeasurer == null) {
		        AttributedCharacterIterator paragraph = aString.getIterator();
		        pStart = paragraph.getBeginIndex();
		        pEnd = paragraph.getEndIndex();
		        FontRenderContext frc = g2d.getFontRenderContext();
		        lineMeasurer = new LineBreakMeasurer(paragraph, frc);
		    }
		    
		    float breakWidth = (float) (getSize().width - 2 * getMargin().right);
		    float drawY = (float) getMargin().top;
		    
		    lineMeasurer.setPosition(pStart);
		    
		    while (lineMeasurer.getPosition() < pEnd) {
		    	int next = lineMeasurer.nextOffset(breakWidth);
		    	int limit = next;
		    	
		    	if (limit <= hintString.length()) {
		    	  for (int i = lineMeasurer.getPosition(); i < next; ++i) {
		    	    char c = hintString.charAt(i);
		    	    if (c == '\n') {
		    	      limit = i + 1;
		    	      break;
		    	    }
		    	  }
		    	}
		    	
		    	TextLayout layout = lineMeasurer.nextLayout(breakWidth, limit, false);
		        
		        float drawX = layout.isLeftToRight() ? (float) getMargin().left : breakWidth - layout.getAdvance();
		        drawY += layout.getAscent();
		        
		        layout.draw(g2d, drawX, drawY);
		        caches.add(new HintCache(layout, drawX, drawY));
		        drawY += layout.getDescent() + layout.getLeading();
		    }
		} else if (isTextEmpty) {
			if (aString == null) return;
			// reducing repaint draw calls
			if (wasDrawingHint == false) {
				repaintCount = 0;
				wasDrawingHint = true;
			}
			clearCanvas(g);
			paintCursor(g);
			
			g.setColor(Color.gray);
			Graphics2D g2d = applyAntialias((Graphics2D)g);
			
			for (HintCache c : caches) {
				c.layout.draw(g2d, c.x, c.y);
			}
			
		} else if (!isTextEmpty) {
			// reducing repaint draw calls 
			if (wasDrawingHint == true) {
				repaintCount = 0;
				wasDrawingHint = false;
			}
			clearCanvas(g);
			super.paint(g);
		}
	}
		
	private void clearCanvas(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		if (repaintCount < 4) {
			repaintCount++;
			repaint();
		}
	}
	
	private void paintCursor(Graphics g) {
		boolean isOdd = (System.currentTimeMillis() % 2000L) > 1000;
		if (isOdd) {
			if (hasFocus()) {
				g.setColor(Color.black);
				g.drawRect(getMargin().left - 1, getMargin().top, 0, getFont().getSize() + 2);
			}
			if (wasCursorPrevVisible) {
				wasCursorPrevVisible = false;
				repaint();
			}
		} else {
			if (!wasCursorPrevVisible) {
				wasCursorPrevVisible = true;
				repaint();
			}
		}
	}
	
	private Graphics2D applyAntialias(Graphics2D g) {
	    RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
	    g.setRenderingHints(rh);
	    return g;
	}
}

class HintCache {
	public TextLayout layout;
	public float x;
	public float y;
	
	public HintCache(TextLayout layout, float x, float y) {
		this.layout = layout;
		this.x = x;
		this.y = y;
	}
}


