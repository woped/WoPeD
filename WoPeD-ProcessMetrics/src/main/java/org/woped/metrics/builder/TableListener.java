package org.woped.metrics.builder;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TableListener implements MouseListener{

	public void lowerClicked(int row){
		if(MetricsBuilderPanel.jTableValues.getValueAt(row, 1).equals("-infinity"))
			MetricsBuilderPanel.jTableValues.setValueAt("", row, 1);
	}
	public void upperClicked(int row){
		if(MetricsBuilderPanel.jTableValues.getValueAt(row, 2).equals("infinity"))
			MetricsBuilderPanel.jTableValues.setValueAt("", row, 2);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		
		 Point click = new Point(e.getX(), e.getY());
	     int row = MetricsBuilderPanel.jTableValues.rowAtPoint(click);	     
	     int column = MetricsBuilderPanel.jTableValues.columnAtPoint(click);	
	     fillVoids();
	     if(column == 1)
	    	 lowerClicked(row);
	     else if(column == 2)
	    	 upperClicked(row);
	}
	
	private void fillVoids(){
		for(int i=0;i<MetricsBuilderPanel.jTableValues.getRowCount();i++){
			if(MetricsBuilderPanel.jTableValues.getValueAt(i, 1).equals(""))
				MetricsBuilderPanel.jTableValues.setValueAt("-infinity",i,1);
			if(MetricsBuilderPanel.jTableValues.getValueAt(i, 2).equals(""))
				MetricsBuilderPanel.jTableValues.setValueAt("infinity",i,2);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

}
