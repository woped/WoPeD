package org.woped.layout.test;

import javax.swing.*;
import org.jgraph.*;

public class Example {
	public static void main(String[] args) {
	     JGraph graph = new JGraph();
	     JFrame frame = new JFrame();
	     frame.getContentPane().add(new JScrollPane(graph));
	     frame.pack();
	     frame.setVisible(true);
	   }
}
