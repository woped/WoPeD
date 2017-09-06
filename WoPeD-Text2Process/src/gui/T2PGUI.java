package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import processing.WordNetWrapper;

import javax.swing.JRadioButton;

public class T2PGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField processDescription;
	private JButton btnTransform;
	private static Initiator init;
	private static boolean bpmn=false;
	private JRadioButton rdbtnBpmn;
	private JRadioButton rdbtnEpk;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		if (args.length<1){
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						T2PGUI frame = new T2PGUI();
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} else {
			// CLI
			if (args.length != 3){
				
				// test for help command
				if (args.length == 1 && args[0].equalsIgnoreCase("help")){
					// help command
					System.out.println("input_type=file|text input=text|file.txt output=file.(epml|bpmn)");
					return;
				} else {
					System.out.println("There are some parameters missing!");
					return;
				}
				
			}
			
			// parameters
			String input_type = null;
			String input = null;
			String output = null;
			File outputFile = null;
			
			// parse parameters
			for (String arg : args) {
				String[] paramArray = arg.split("=");
				if (paramArray.length != 2){
					System.out.println("parameter is broken!");
					return;
				}
				if (paramArray[0].equalsIgnoreCase("input_type")){
					if (input_type != null) {
						System.out.println("parameter is defined multiple times!");
						return;
					} else {
						input_type = paramArray[1];
					}
				}
				if (paramArray[0].equalsIgnoreCase("input")){
					if (input != null) {
						System.out.println("parameter is defined multiple times!");
						return;
					} else {
						if (paramArray[1].startsWith("\"") && paramArray[1].endsWith("\"")){
							input = paramArray[1].substring(1, paramArray[1].length()-1);
						} else {
							input = paramArray[1];
						}
						System.out.println("input is: " + input);
					}
				}
				if (paramArray[0].equalsIgnoreCase("output")){
					if (output != null) {
						System.out.println("parameter is defined multiple times!");
						return;
					} else {
						output = paramArray[1];
						outputFile = new File(output);
						if (output.substring(output.lastIndexOf(".")).equalsIgnoreCase(".epml")){
							bpmn = false;
						} else if (output.substring(output.lastIndexOf(".")).equalsIgnoreCase(".bpmn")){
							bpmn = true;
						} else {
							System.out.println("unknown output data type!");
							return;
						}
					}
				}
			}
			
			// DEBUG info
//			System.out.println("DEBUG start");
//			System.out.println("input_type: " + input_type);
//			System.out.println("input: " + input);
//			System.out.println("output: " + output);
//			System.out.println("bpmn: " + bpmn);
//			System.out.println("DEBUG end");
			
			
			init = new Initiator();
			
			if (input_type.equalsIgnoreCase("file")){
				try {
					File inputFile = new File(input);
					init.convert(inputFile, bpmn, outputFile);
				} catch (IOException e) {
					System.out.println("Error reading input file!");
					return;
//					e.printStackTrace();
				}
			} else if (input_type.equalsIgnoreCase("text")){
					init.convert(input, bpmn, outputFile);
			} else {
				System.out.println("unknown input_type!");
				return;
			}
			
			
		}
	}
	
//	public static void convertFromConsole(File file, boolean bpmn){
//		init = new Initiator();
//		init.convert(file, bpmn);
//	}

	/**
	 * Create the frame.
	 */
	public T2PGUI() {
		init = new Initiator();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Text2Model");
		setBounds(100, 100, 720, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		processDescription = new JTextField();
		contentPane.add(processDescription);
		processDescription.setColumns(10);
		
		btnTransform = new JButton("Transform");
		btnTransform.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
//				System.out.println(processDescription.getText());
					init.convert(processDescription.getText(), bpmn, null);
			}
		});
		contentPane.add(btnTransform);
		
		rdbtnBpmn = new JRadioButton("BPMN");
		contentPane.add(rdbtnBpmn);
		
		rdbtnEpk = new JRadioButton("EPK");
		contentPane.add(rdbtnEpk);
		
		ButtonGroup group = new ButtonGroup();
	    group.add(rdbtnBpmn);
	    group.add(rdbtnEpk);
	    
	    rdbtnBpmn.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent ae) {
				bpmn=true;
		}
	    });
	    
	    rdbtnEpk.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent ae) {
				bpmn=false;
		}
	    });
	}

}
