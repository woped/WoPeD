/*
 * 
 * Copyright (C) 2004-2009, see @author in JavaDoc for the author 
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *
 * For contact information please visit http://woped.dhbw-karlsruhe.de
 *
 */
package org.woped.editor.controller;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.Autoscroll;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.woped.core.config.DefaultStaticConfiguration;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.ResourceClassModel;
import org.woped.core.model.petrinet.ResourceModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.core.utilities.SwingUtils;
import org.woped.editor.Constants;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.gui.translations.Messages;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;



/**
 * @author Tobias Rehnig, Stephanie Obergfell, Jenny Braun, Linda Linsenbolz
 */

@SuppressWarnings("serial")
public class PetriNetResourceEditor extends JPanel 
{
	
	private JPanel				   borderPanel						= null;
	
	// Radio Buttons
	private JPanel				   RadioButtonPanel					= null;
	private ActionListener		   RadioButtonListener				= new RadioButtonListener();
	boolean showGraph 												= false;
	
	// Resource Graph Panel
	private JPanel				   ResourceGraph_Role_Panel			= null;
	private JPanel				   ResourceGraphGroupPanel		= null;
	private JPanel				   resourceExportButtonRole_Panel	= null;
	private JPanel				   resourceColorButtonRolePanel	= null;
	private JPanel				   resourceExportButtonGroupPanel= null;
	private JPanel				   resourceColorButtonGroupPanel	= null;
	private JButton				   resourceExportButtonRole		= null;
	private JButton				   resourceColorButtonRole		= null;
	private JButton				   resourceExportButtonGroup		= null;
	private JButton				   resourceColorButtonGroup		= null;
	private ActionListener		   Export							= new Export();
	private ActionListener		   Coloring							= new Coloring();
	
	private JScrollPane			   GroupPane						= null;
	private JScrollPane			   RolePane							= null;
	
	private com.mxgraph.view.mxGraph temp_Role_graph				= null;
	private com.mxgraph.view.mxGraph tempGroupGraph				= null;
	private mxGraphComponent 		temp_Role_graphComponent 		= null;
	private mxGraphComponent 		tempGroupGraphComponent 		= null;
	
	// Objects-Panel
	private JPanel 				   objectsPanel						= null;
	private JPanel                 objectsButtonPanel  				= null;
	private JScrollPane			   objectsScrollPane				= null;
	
	private JButton				   objectsNewButton					= null;
	private JButton				   objectsEditButton				= null;
	private JButton				   objectsDeleteButton				= null;
	private JButton			  	   objectsCollapseButton			= null;
	private JButton			  	   objectsExpandButton				= null;

	private JTree				   objectsTree						= null;
	private DefaultTreeModel 	   objectsTreeModel					= null;

	private DefaultMutableTreeNode objectsTopNode					= null;
	private DefaultMutableTreeNode objectsUnassignedNode			= null;
	private DefaultMutableTreeNode objectsAssignedNode				= null;

	private DefaultListModel       objectsAssignedListModel    	 	= new DefaultListModel();
	private DefaultListModel       objectsUnassignedListModel   	= new DefaultListModel();
	
	//Roles-Panel
	private JPanel                 RolesContentPanel           		= null;
	private JPanel                 RolesPanel            			= null;
	private JPanel                 superRolesPanel         			= null;
	private JPanel                 RolesButtonPanel     			= null;
	private JScrollPane            RolesScrollPane      			= null;
	private JButton				   RolesNewButton					= null;
	private JButton				   RolesEditButton					= null;
	private JButton				   RolesDeleteButton				= null;

	private JButton			  	   RolesCollapseButton				= null;
	private JButton			  	   RolesExpandButton  				= null;

	private JTree				   RolesTree						= null;
	private DefaultTreeModel 	   RolesTreeModel					= null;
	private DefaultMutableTreeNode RolesTopNode						= null;
	private DefaultListModel       RolesListModel               	= new DefaultListModel();

	//	Super-Roles Panel
	private JPanel                 superRolesButtonPanel			= null;
	private JScrollPane            superRolesScrollPane 			= null;
	private JButton				   superRolesNewButton				= null;
	private JButton				   superRolesEditButton				= null;
	private JButton				   superRolesDeleteButton			= null;

	private JButton			  	   superRolesCollapseButton			= null;
	private JButton			  	   superRolesExpandButton  			= null;

	private JTree				   superRolesTree					= null;
	private DefaultTreeModel 	   superRolesTreeModel				= null;
	private DefaultMutableTreeNode superRolesTopNode				= null;
	private DefaultListModel       superRolesListModel          	= new DefaultListModel();

	//	Groups-Panel
	private JPanel                 GroupsContentPanel   			= null;
	private JPanel                 GroupsPanel        				= null;
	private JPanel                 superGroupsPanel        			= null;
	private JPanel                 GroupsButtonPanel    			= null;
	private JScrollPane            GroupsScrollPane     			= null;
	private JButton				   GroupsNewButton					= null;
	private JButton				   GroupsEditButton					= null;
	private JButton				   GroupsDeleteButton				= null;

	private JButton			  	   GroupsCollapseButton				= null;
	private JButton			  	   GroupsExpandButton  				= null;

	private JTree				   GroupsTree						= null;
	private DefaultTreeModel 	   GroupsTreeModel					= null;
	private DefaultMutableTreeNode GroupsTopNode					= null;
	private DefaultListModel       GroupsListModel              	= new DefaultListModel();
	
	//	Super-Group Panel
	private JPanel                 superGroupsButtonPanel    		= null;
	private JScrollPane            superGroupsScrollPane			= null;
	private JButton				   superGroupsNewButton				= null;
	private JButton				   superGroupsEditButton			= null;
	private JButton				   superGroupsDeleteButton			= null;

	private JButton			  	   superGroupsCollapseButton		= null;
	private JButton			  	   superGroupsExpandButton  		= null;

	private JTree				   superGroupsTree					= null;
	private DefaultTreeModel 	   superGroupsTreeModel				= null;
	private DefaultMutableTreeNode superGroupsTopNode				= null;
	private DefaultListModel       superGroupsListModel             = new DefaultListModel();
	  


	// Graphic-Panel
	private JPanel					graphicPanel					= null;


	//  Dialog-Frame to create new Super-Resource
	private JFrame				   	dialogFrame						= null;
	private JButton 			  	dialogFrameConfirmButton		= null;
	private	JButton 			 	dialogFrameCancelButton			= null;
  	private JTextField			   	dialogFrameTextField			= null;	
  	private JList				   	selectedRolesList				= null;
  	private JList				   	selectedGroupsList				= null;
  	 
  	
	private ActionListener		   	createResource					= new createResource();
	private ActionListener		   	removeResource					= new removeResource();
	private ActionListener		   	editResource					= new editResource();

	private ActionListener  	   	createSuperResourceFrame		= new createSuperResourceFrame();
	private ActionListener  	   	editSuperResourceFrame			= new editSuperResourceFrame();
	private ActionListener  	   	createSuperResource				= new createSuperResourceListener();
	private ActionListener  	   	editSuperResource				= new editSuperResourceListener();
	private ActionListener  	   	removeSuperResource				= new removeSuperResource();

	private ActionListener		   	expandButtonListener			= new expandButtonListener();
	private ActionListener		   	collapseButtonListener			= new collapseButtonListener();
	private MyTreeExpansionListener treeListener					= new MyTreeExpansionListener();

	private TreeSelectionListener  	treeSelection					= new MyTreeSelectionListener();
	
	//ImageIcon used in the TreeCellRenderer-Classes  
	private ImageIcon 			   	resourceClass  					= Messages.getImageIcon("PetriNet.Resources.ResourceClass");
	private ImageIcon 			   	resourceSuperClass				= Messages.getImageIcon("PetriNet.Resources.ResourceSuperClass");		 				
	private ImageIcon 			   	object		 					= Messages.getImageIcon("PetriNet.Resources.Object");
	private ImageIcon 			   	unassignedIcon  				= Messages.getImageIcon("PetriNet.Resources.Unassigned");
    private	superTreeRenderer 		rendererResourceSuperClass 		= new superTreeRenderer();
    private	treeRenderer 			rendererResourceClass 			= new treeRenderer();
	
    private Font Nodes = DefaultStaticConfiguration.DEFAULT_HUGELABEL_ITALICFONT;
    
    // JgraphX Cell with Focus for Coloring
    private mxCell currentCell = null;
    private mxGraph currentGraph = null;
    
	
    private PetriNetModelProcessor petrinet;
    private EditorVC               editor;
    	
	 public EditorVC getEditor()
	    {
	        return editor;
	    }
	 public PetriNetModelProcessor getPetrinet()
	    {
	        return petrinet;
	    }
	 
	 public PetriNetResourceEditor (EditorVC editor)
	    {
	        this.editor = editor;
	        this.petrinet = (PetriNetModelProcessor) editor.getModelProcessor();
	        initialize();
	        reset();
	    }
	//! This rather important method will refresh the resource editor view
	//! if the resources have changed in the Petri-Net model
	//! It is called when the resource editor gains focus   
	 public void reset()
	 {
		 	refreshFromModel(); 
		 	refreshGUI();
	  }
	  

	    

	private void initialize(){
	        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	        //this.setSize(new Dimension(800, 600));
	        if(this.getLayout()==null){
	        	 this.setLayout(new BorderLayout());
	        }else{
	        	this.removeAll();
	        }
	        this.setLayout(new BorderLayout());
	        this.add(getBorderPanel(),BorderLayout.NORTH);

	        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
	        renderer.setLeafIcon(Messages.getImageIcon("PetriNet.Resources.Unassign"));
	        renderer.setOpenIcon(Messages.getImageIcon("PetriNet.Resources.Unassign"));

	        this.updateUI();
	   }


	   private JPanel getBorderPanel(){
		   
			   borderPanel = new JPanel(new GridBagLayout());
			   borderPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			   GridBagConstraints c = new GridBagConstraints();
				   
		       c.weightx = 4;
		       c.weighty = 0;
		       
			   if (showGraph == true) {
				   
				   refreshFromModel();
				   
				   c.anchor = GridBagConstraints.FIRST_LINE_START;
			       c.gridx = 0;
			       c.gridy = 0;
			       borderPanel.add(getRadioButtonPanel(),c);
			       
			       c.anchor = GridBagConstraints.NORTHWEST;
			       c.gridx = 0;
			       c.gridy = 1;
			       borderPanel.add(getResourceGraph_Role_Panel(),c);

			       c.anchor = GridBagConstraints.NORTHWEST;
			       c.gridx = 1;
			       c.gridy = 1;
			       borderPanel.add(getResourceGraphGroupPanel(),c);
			   }
			   else {
				   c.anchor = GridBagConstraints.FIRST_LINE_START;
				   c.gridx = 0;
				   c.gridy = 0;
				   borderPanel.add(getRadioButtonPanel(),c);
		        	
				   c.anchor = GridBagConstraints.WEST;
				   c.gridx = 0;
				   c.gridy = 1;
				   borderPanel.add(getObjectsContentPanel(),c);
		        	
				   c.anchor = GridBagConstraints.WEST;
				   c.gridx = 1;
				   c.gridy = 1;
				   borderPanel.add(getGraphicPanel(),c);
		        	
				   c.anchor = GridBagConstraints.NORTHWEST;
				   c.gridx = 2;
				   c.gridy = 1;
				   borderPanel.add(getRolesContentPanel(),c);
		        	
				   c.anchor = GridBagConstraints.NORTHWEST;
				   c.gridx = 3;
				   c.gridy = 1;
				   borderPanel.add(getGroupsContentPanel(),c);
			   }
			   
		   return borderPanel;
	   }
	   	   
   
//		*****************RadioButton_PANEL*************************************
//	 	*****************************************************************
	   private JPanel getRadioButtonPanel(){
		   
		   if(RadioButtonPanel == null){
			   RadioButtonPanel = new JPanel(new GridLayout(2,1));
			   RadioButtonPanel.setBorder(BorderFactory.createCompoundBorder(
					   BorderFactory.createTitledBorder(Messages.getString("PetriNet.Resources.View.RadioButtonPanel")),
	               		BorderFactory.createEmptyBorder()));
			   
			   SwingUtils.setFixedSize(RadioButtonPanel,160,60);
			   
			   JRadioButton ResourceEditor = new JRadioButton(Messages.getString("PetriNet.Resources.View.Editor"), true);
			   JRadioButton GraphView = new JRadioButton(Messages.getString("PetriNet.Resources.View.Graph"), false);
			   
			   ButtonGroup SwitchView = new ButtonGroup();
			   SwitchView.add(ResourceEditor);
			   SwitchView.add(GraphView);
			   RadioButtonPanel.add(ResourceEditor);
			   RadioButtonPanel.add(GraphView);
			   
			   ResourceEditor.addActionListener(RadioButtonListener);
		       GraphView.addActionListener(RadioButtonListener);
		   }
		   
		   return RadioButtonPanel;
	   }
	   
		  private class RadioButtonListener implements ActionListener{
				
			   public void actionPerformed(ActionEvent e) {	
				 JRadioButton trigger= (JRadioButton) e.getSource();
				 
				 if (trigger.getText()==Messages.getString("PetriNet.Resources.View.Editor")) {
					 showGraph = false;
				 }
				 if (trigger.getText()==Messages.getString("PetriNet.Resources.View.Graph")) {
					 showGraph = true;
				 }
				 borderPanel.removeAll();
				 borderPanel=null;
				 initialize();
			   }
		   }
	   
//		*****************Resource_Graph_Role_PANEL*************************************
//	 	*****************************************************************
	   private JPanel getResourceGraph_Role_Panel(){
			   
				ResourceGraph_Role_Panel = new JPanel(new GridBagLayout());
			    ResourceGraph_Role_Panel.setBorder(BorderFactory.createCompoundBorder(
               		BorderFactory.createTitledBorder(Messages.getString("PetriNet.Resources.RoleGraphTitle")),
               		BorderFactory.createEmptyBorder()));
			 
			    GridBagConstraints c = new GridBagConstraints();
			    c.anchor = GridBagConstraints.NORTHEAST; 
		        c.gridx = 0;
		        c.gridy = 0;  
		        ResourceGraph_Role_Panel.add(getresourceExportButtonRole_Panel(),c);
		        c.anchor = GridBagConstraints.NORTHWEST;
		        c.gridx = 1;
		        c.gridy = 0;
		        ResourceGraph_Role_Panel.add(getresourceColorButtonRolePanel(),c);
		        c.anchor = GridBagConstraints.NORTH;
		        c.gridx = 0;
		        c.gridy = 1;
		        c.gridwidth =2;
		        ResourceGraph_Role_Panel.add(getRole_Panel(),c);
		        return ResourceGraph_Role_Panel;
	   }
	   
	   private JScrollPane getRole_Panel(){
		   RolePane = new JScrollPane(getRolegraph());
		   RolePane.setBorder(BorderFactory.createEmptyBorder());

		   return RolePane;
	   }
	   
       public class selectListener implements mxIEventListener
       {
    	  	private mxGraph graph;
    	  	public selectListener(mxGraph graph) {
    	 		this.graph = graph;
    	 	}

    	  	public void invoke(Object sender, mxEventObject evt)
    	  		{	
	                mxCell selected = (mxCell) graph.getSelectionCell();
	                if (selected == null) {}
	                else {
	                    currentCell = selected;
	                    currentGraph = graph;
	                }
    	  	}
        };
	   
		   private mxGraphComponent getRolegraph(){
			   
			    LoggerManager.info(Constants.EDITOR_LOGGER, Messages.getString("PetriNet.Resources.DrawRoleGraph"));
			   	mxGraph graph;
			   	mxGraphComponent graphComponent;
			   	
		        graph = new mxGraph();
		        Object parentVertex = graph.getDefaultParent();
		        graph.getModel().beginUpdate();
	        	//Colors
	        	String[] colors = {"00DB26" ,
	        	                   "8DF200" , 
	        	                   "F2B705" , 
	        	                   "F29A2E" , 
	        	                   "F37542" , 
	        	                   "F25456" , 
	        	                   "A73ADB" , 
	        	                   "5549F2" , 
	        	                   "40ADDB" , 
	        	                   "04F9BE" , 
	        	                   "938466" , 
	        	                   "C6B38F" , 
	        	                   "F99249" , 
	        	                   "E26537" , 
	        	                   "FC553D" , 
	        	                   "E52C3B"};
	        	int alreadyPaintedFlag = 0;
	        	
		        try {
		        	LogicalModel lm = new LogicalModel(objectsAssignedListModel,objectsUnassignedListModel,RolesTreeModel,RolesTopNode,GroupsTreeModel,GroupsTopNode,superRolesTreeModel,superGroupsTreeModel,superRolesTopNode, superGroupsTopNode); 
				    LayoutAlgorithm layoutAlgorithm = new LayoutAlgorithm();
				    layoutAlgorithm.createLayout(lm);
				    Set<Layout> RoleLayoutSet = layoutAlgorithm.RoleLayoutSet;
		        	
				    //Assign color associations to the single Roles
				    String[] RoleColorAssociation = new String[lm.allRoles.size()];
				    int index = 0;
				    Iterator<?> colorIt = lm.allRoles.iterator();
				    while (colorIt.hasNext()) {
				    	Role currentRole = (Role) colorIt.next();
				    	RoleColorAssociation[index] = currentRole.name;
				    	index+=1;
				    }
				    
		        	Iterator<Layout> it = RoleLayoutSet.iterator();
		        	
		        	while (it.hasNext()) {
		        		Layout currentLayout = it.next();
		        		
		        		//Compound
		        		int dimensionX = 140;
			        	int dimensionY = 30;
			        	
			        	// Width== Number of Roles * dimensionX
			        	int compound_dimensionX = currentLayout.grid.length*dimensionX+20;
			        	int compoundDimensionY = 0;
			        	
			        	// maxRow= Longest Colum(with maximum of Rows)
                        int maxRow = 0;   
                        
                        // for each Role-Column -> do search the longest column, and remeber the Y
                        for (int i=0;i<currentLayout.grid.length;i++) {
                            if (currentLayout.grid[i] != null) {
                                for (int y=currentLayout.grid[i].length-1;y>=0;y--) {
                                    if (currentLayout.grid[i][y] != null) {
                                        if (y>maxRow) {
                                            maxRow = y;
                                        }
                                        break;
                                    }
                                }
                            }
                        }

                        // Search for the deepest Place
			        	Integer maxDepth[] = new Integer[currentLayout.grid.length];
                        Integer firstKill[] = new Integer[currentLayout.grid.length]; 
			        	Iterator<RoleLocator> deepBlue = currentLayout.Roles.iterator();
			        	
			        	while (deepBlue.hasNext()) {
			        		RoleLocator currentRole = deepBlue.next();
			        		
			        		if(maxDepth[currentRole.column]==null){
			        			maxDepth[currentRole.column]=1;
			        		}else {
			        			maxDepth[currentRole.column]++;
			        		}
			        	}
			        	
			        	int deepestSubset = 0;
			        	for(int i=0;i<maxDepth.length;i++){
			        		
			        		if(maxDepth[i]>deepestSubset){
			        			deepestSubset=maxDepth[i];
			        		}
			        	}

			        	if(deepestSubset==1){
			        		
			        		// so finally -> compound_dim_Y = LongestColumn * dimensionY 
	                        compoundDimensionY = (maxRow+1)*dimensionY+60;
				        	
	                        // now draw compound                                                       x  y                 x-length              y-length
				        	graph.insertVertex(parentVertex, null, currentLayout.name, 5, 5+alreadyPaintedFlag, compound_dimensionX + 40, compoundDimensionY, "rounded=1;strokeColor=black;fillColor=#CCCCCC;verticalAlign=top;fontStyle=1;fontColor=black" );
				        	
				        	// So now we build an Iterator to travel trough all Roles of the compound Role 
				        	//Roles
				        	Iterator<RoleLocator>  subIt = currentLayout.Roles.iterator();
				        	while (subIt.hasNext()) {
				        		RoleLocator currentRole = subIt.next();
				        		
				        		//Find the related color
				        		int colorRelation = 0;
				        		for (int i=0; i<RoleColorAssociation.length;i++) {
				        			if (RoleColorAssociation[i] == currentRole.name) {
				        				colorRelation = i;
				        				if (colorRelation>=colors.length) {
				        					colorRelation = colorRelation - colors.length;
				        				}
				        				break;
				        			}
				        		}
				        		graph.insertVertex(parentVertex, null, currentRole.name, 15+dimensionX*currentRole.column, 20+dimensionY*currentRole.ystart+alreadyPaintedFlag, dimensionX, dimensionY*(currentRole.yend-currentRole.ystart+1)+30, "rounded=1;strokeColor=black;fillColor="+colors[colorRelation]+";verticalAlign=top;fontStyle=1;fontColor=black"); //kursiv ;fontStyle=1;fontColor=black
				        	}
				        	
				        	//Components
				        	String containsString = new String();
				        	
				        	// for each column
				        	for (int x1=0; x1<currentLayout.grid.length;x1++) {
				        		// for each row/ Component
		        				for (int y1=0;y1<currentLayout.grid[x1].length;y1++) {
		        					int xMultiplicator = 0;
		        					// is the object availible ?
		    	        			if (currentLayout.grid[x1][y1] != null) {
				        				if(containsString.contains(currentLayout.grid[x1][y1].name)){}
				        					//do nothing -> cause it is already included
				        				else if(!containsString.contains(currentLayout.grid[x1][y1].name)){
				        					// Add current user
				        					containsString = containsString +" "+ currentLayout.grid[x1][y1].name;
				        					//for each column
				        					for(int x2 = x1 + 1; x2 < currentLayout.grid.length; x2++){
				        						// for each row
					        					for(int y2 = 0; y2 < currentLayout.grid[x2].length; y2++){
					        						// is the object availible ?
					        						if(currentLayout.grid[x2][y2] != null){
						        						if(currentLayout.grid[x1][y1].name == currentLayout.grid[x2][y2].name){
						        							if((x2 - x1) > xMultiplicator)
						        								// if the multiplicator has to be augmented by more than one column, it will not be done, cause in this case there ist a gap
						        								if((x2-x1)==(xMultiplicator+1)){
						        									xMultiplicator = x2 - x1;
						        								}else {
						        									containsString = containsString.replaceFirst(currentLayout.grid[x1][y1].name,"");
						        									xMultiplicator = 0;
						        								}
						        						}
					        						}
						        				}	
					        				}
				        					if(xMultiplicator==0){
				        						graph.insertVertex(parentVertex, null, currentLayout.grid[x1][y1].name, 20+x1*dimensionX, 45+y1*dimensionY+alreadyPaintedFlag, xMultiplicator * 5 + xMultiplicator * (dimensionX-10)  + (dimensionX-10), dimensionY-4, "rounded=1;strokeColor=black;fillColor=white;opacity=65;fontStyle=1;fontColor=black");
				        					}
				        					if(xMultiplicator>0){
				        						graph.insertVertex(parentVertex, null, currentLayout.grid[x1][y1].name, 20+x1*dimensionX, 45+y1*dimensionY+alreadyPaintedFlag, xMultiplicator * 5 + xMultiplicator * (dimensionX-10)  + (dimensionX-5), dimensionY-4, "rounded=1;strokeColor=black;fillColor=white;opacity=65;fontStyle=1;fontColor=black");
				        					}
				        				}
				        			}
		    	        		}
				        	}
			        	}else{
	                        compoundDimensionY = (maxRow+1)*dimensionY+70;
				        	graph.insertVertex(parentVertex, null, currentLayout.name, 5, 5+alreadyPaintedFlag, compound_dimensionX, compoundDimensionY, "rounded=1;strokeColor=black;fillColor=#CCCCCC;verticalAlign=top;fontStyle=1;fontColor=black" );
			        	
				        	//Roles
				        	Iterator<RoleLocator>  subIt = currentLayout.Roles.iterator();
				        	while (subIt.hasNext()) {
				        		RoleLocator currentRole = subIt.next();
				        	
				        		if(firstKill[currentRole.column]==null){ 
				        			firstKill[currentRole.column]=0;
				        		}else{
				        			firstKill[currentRole.column]=1;
				        		}
				        		
				        		//Find the related color
				        		int colorRelation = 0;
				        		for (int i=0; i<RoleColorAssociation.length;i++) {
				        			if (RoleColorAssociation[i] == currentRole.name) {
				        				colorRelation = i;
				        				if (colorRelation>=colors.length) {
				        					colorRelation = colorRelation - colors.length;
				        				}
				        				break;
				        			}
				        		}
				        		graph.insertVertex(parentVertex, null, currentRole.name, 15+dimensionX*currentRole.column, 20+(dimensionY*currentRole.ystart*2)+alreadyPaintedFlag+(firstKill[currentRole.column]*20), dimensionX, dimensionY*(currentRole.yend-currentRole.ystart+1)-(firstKill[currentRole.column]*dimensionY)+44, "rounded=1;strokeColor=black;fillColor="+colors[colorRelation]+";verticalAlign=top;fontStyle=1;fontColor=black");
				        	}
				        	
				        	//Components
				        	String containsString = new String();
				        	for (int x1=0; x1<currentLayout.grid.length;x1++) {
		        				for (int y1=0;y1<currentLayout.grid[x1].length;y1++) {
		        					int xMultiplicator = 0;
		    	        			if (currentLayout.grid[x1][y1] != null) {
				        				if(containsString.contains(currentLayout.grid[x1][y1].name)){}
				        				else if(!containsString.contains(currentLayout.grid[x1][y1].name)){
				        					containsString = containsString +" "+ currentLayout.grid[x1][y1].name;
				        					for(int x2 = x1 + 1; x2 < currentLayout.grid.length; x2++){
					        					for(int y2 = 0; y2 < currentLayout.grid[x2].length; y2++){
					        						if(currentLayout.grid[x2][y2] != null){
						        						if(currentLayout.grid[x1][y1].name == currentLayout.grid[x2][y2].name){
						        							if((x2 - x1) > xMultiplicator)
						        								if((x2-x1)==(xMultiplicator+1)){
						        									xMultiplicator = x2 - x1;
						        								}else {
						        									containsString = containsString.replaceFirst(currentLayout.grid[x1][y1].name,"");
						        									xMultiplicator = 0;
	
						        								}
						        						}
					        						}
						        				}	
					        				}
				        					//                                                                                           x                 y                                x-length                                                                y-length
				        					if(xMultiplicator==0){
				        						graph.insertVertex(parentVertex, null, currentLayout.grid[x1][y1].name, 20+x1*dimensionX, 55+y1*dimensionY+alreadyPaintedFlag, xMultiplicator * 5 + xMultiplicator * (dimensionX-10)  + (dimensionX-10), dimensionY-4, "rounded=1;strokeColor=black;fillColor=white;opacity=65;fontStyle=1;fontColor=black");
				        					}
				        					if(xMultiplicator>0){
				        						graph.insertVertex(parentVertex, null, currentLayout.grid[x1][y1].name, 20+x1*dimensionX, 55+y1*dimensionY+alreadyPaintedFlag, xMultiplicator * 5 + xMultiplicator * (dimensionX-10)  + (dimensionX-5), dimensionY-4, "rounded=1;strokeColor=black;fillColor=white;opacity=65;fontStyle=1;fontColor=black");
				        					}
				        				}
				        			}
		    	        		}
				        	}
			        	}
			        	alreadyPaintedFlag = alreadyPaintedFlag + compoundDimensionY + 10;
			        }
		        } finally {
		        	graph.getModel().endUpdate();
		        }
		        
		        if(alreadyPaintedFlag==0){
		        	graph.insertVertex(parentVertex, null,Messages.getString("PetriNet.Resources.NoRolesDefined"),10,10,150,30, "rounded=1;opacity=0;fontStyle=1;fontColor=black");	        	
		        }
		        
		        // Create Listener, first parameter in constructor is the observed graph
		        mxIEventListener mySelectListener = new selectListener(graph);
		        // add Listener as CHANGE-Listener
		        graph.getSelectionModel().addListener(mxEvent.CHANGE, mySelectListener);
		        graphComponent = new mxGraphComponent(graph);

		        temp_Role_graph = graph;
		        temp_Role_graphComponent= graphComponent;

		        return graphComponent;
		   }
	   
	   
//		*****************ResourceGraphGroupsPanel*************************************
//	 	*****************************************************************
	   private JPanel getResourceGraphGroupPanel(){
	   	   ResourceGraphGroupPanel = new JPanel(new GridBagLayout());
		   ResourceGraphGroupPanel.setBorder(BorderFactory.createCompoundBorder(
				   BorderFactory.createTitledBorder(Messages.getString("PetriNet.Resources.GroupGraphTitle")),
                   BorderFactory.createEmptyBorder()));
		 
		   GridBagConstraints c = new GridBagConstraints();
		   c.anchor = GridBagConstraints.NORTHEAST; 
	       c.gridx = 0;
	       c.gridy = 0;     
	       ResourceGraphGroupPanel.add(getresourceExportButtonGroupPanel(),c);
	       c.anchor = GridBagConstraints.NORTHWEST;
	       c.gridx = 1;
	       c.gridy = 0;
	       ResourceGraphGroupPanel.add(getresourceColorButtonGroupPanel(),c);
	       c.anchor = GridBagConstraints.NORTH;
	       c.gridx = 0;
	       c.gridy = 1;
	       c.gridwidth =2;
	       ResourceGraphGroupPanel.add(getGroup_Panel(),c);
	       
		   return ResourceGraphGroupPanel;
	   }
	   	   
	   private JScrollPane getGroup_Panel(){
		   GroupPane = new JScrollPane(getGroupgraph());
		   GroupPane.setBorder(BorderFactory.createEmptyBorder());

		   return GroupPane;
	   }
	   
	   private mxGraphComponent getGroupgraph(){
			   	LoggerManager.info(Constants.EDITOR_LOGGER, Messages.getString("PetriNet.Resources.DrawGroupGraph"));
			   	mxGraph graph;
			   	mxGraphComponent graphComponent;
			   	graph = new mxGraph();
		        Object parentVertex = graph.getDefaultParent();
		        graph.getModel().beginUpdate();
		        
		        //Colors
	        	String[] colors = {"00DB26" ,
	        	                   "8DF200" , 
	        	                   "F2B705" , 
	        	                   "F29A2E" , 
	        	                   "F37542" , 
	        	                   "F25456" , 
	        	                   "A73ADB" , 
	        	                   "5549F2" , 
	        	                   "40ADDB" , 
	        	                   "04F9BE" , 
	        	                   "938466" , 
	        	                   "C6B38F" , 
	        	                   "F99249" , 
	        	                   "E26537" , 
	        	                   "FC553D" , 
	        	                   "E52C3B"};
		        int alreadyPaintedFlag = 0;
		        try {
		        	LogicalModel lm = new LogicalModel(objectsAssignedListModel,objectsUnassignedListModel,RolesTreeModel,RolesTopNode,GroupsTreeModel,GroupsTopNode,superRolesTreeModel,superGroupsTreeModel,superRolesTopNode, superGroupsTopNode); 
				    LayoutAlgorithm layoutAlgorithm = new LayoutAlgorithm();

				    layoutAlgorithm.createLayout(lm);
				    Set<Layout> GroupLayoutSet = layoutAlgorithm.GroupLayoutSet;
		        	
				    //Assign color associations to the single Roles
				    String[] RoleColorAssociation = new String[lm.allRoles.size()];
				    int index = 0;
				    Iterator<?> colorIt = lm.allGroups.iterator();
				    try {
				    while (colorIt.hasNext()) {
				    	Group currentGroup = (Group) colorIt.next();
				    	RoleColorAssociation[index] = currentGroup.name;
				    	index+=1;
				    }
				    }
				    catch (Exception e) {}
				    
		        	Iterator<Layout> it = GroupLayoutSet.iterator();
		        	
		        	while (it.hasNext()) {
		        		Layout currentLayout = it.next();
		        		
		        		//Compound
		        		int dimensionX = 140;
			        	int dimensionY = 30;
			        	int compound_dimensionX = currentLayout.grid.length * dimensionX + 20;
			        	int compoundDimensionY = 0;
			        	int maxRow = 0;   
		               
	                   for (int i=0;i<currentLayout.grid.length;i++) {
	                       if (currentLayout.grid[i] != null) {
	                           for (int y=currentLayout.grid[i].length-1; y>=0; y--) {
	                               if (currentLayout.grid[i][y] != null) {
	                                   if (y>maxRow) {
	                                       maxRow = y;
	                                   }
	                                   break;
	                               }
	                           }
	                       }
	                    }
                        

			        	// Search for the deepest Place
			        	
	                   Integer maxDepth[] = new Integer[currentLayout.grid.length];
	                   Integer firstKill[] = new Integer[currentLayout.grid.length]; 
			           Iterator<GroupLocator> deepBlue = currentLayout.Groups.iterator();
			        	
			        	while (deepBlue.hasNext()) {
			        		GroupLocator currentGroup = deepBlue.next();
			        		
			        		if(maxDepth[currentGroup.column]==null){
			        			maxDepth[currentGroup.column]=1;
			        		}else {
			        			maxDepth[currentGroup.column]++;
			        		}
			        	}
			        	
			        	int deepestSubset = 0;
			        	for(int i=0;i<maxDepth.length;i++){
			        		if(maxDepth[i]>deepestSubset){
			        			deepestSubset=maxDepth[i];
			        		}
			        	}
			        	
			        	if(deepestSubset==1){
			        		//dimensionY = 30;
			        		compoundDimensionY = (maxRow+1) * dimensionY + 60;
				        	graph.insertVertex(parentVertex, null, currentLayout.name, 5, 5+alreadyPaintedFlag, compound_dimensionX, compoundDimensionY, "rounded=1;strokeColor=black;fillColor=#CCCCCC;verticalAlign=top;fontStyle=1;fontColor=black" );

				        	// So now we build an Iterator to travel trough all Roles of the compound Role 
				        	//Roles
				        	Iterator<GroupLocator>  subIt = currentLayout.Groups.iterator();
				        	while (subIt.hasNext()) {
				        		GroupLocator currentGroup = subIt.next();
				        		
				        		//Find the related color
				        		int colorRelation = 0;
				        		for (int i=0; i<RoleColorAssociation.length;i++) {
				        			if (RoleColorAssociation[i] == currentGroup.name) {
				        				colorRelation = i;
				        				if (colorRelation>=colors.length) {
				        					colorRelation = colorRelation - colors.length;
				        				}
				        				break;
				        			}
				        		}
				        		graph.insertVertex(parentVertex, null, currentGroup.name, 15+dimensionX*currentGroup.column, 20+dimensionY*currentGroup.ystart+alreadyPaintedFlag, dimensionX, dimensionY*(currentGroup.yend-currentGroup.ystart+1)+30, "rounded=1;strokeColor=black;fillColor="+colors[colorRelation]+";verticalAlign=top;fontStyle=1;fontColor=black");
				        	}
				        	
				        	//Components
				        	String containsString = new String();
				        	
				        	for (int x1=0; x1<currentLayout.grid.length;x1++) {
		        				for (int y1=0;y1<currentLayout.grid[x1].length;y1++) {
		        					int xMultiplicator = 0;
		    	        			if (currentLayout.grid[x1][y1] != null) {
				        				if(containsString.contains(currentLayout.grid[x1][y1].name)){
				        				} else if(!containsString.contains(currentLayout.grid[x1][y1].name)){
				        					containsString = containsString +" "+ currentLayout.grid[x1][y1].name;
				        					for(int x2 = x1 + 1; x2 < currentLayout.grid.length; x2++){
					        					for(int y2 = 0; y2 < currentLayout.grid[x2].length; y2++){
					        						if(currentLayout.grid[x2][y2] != null){
						        						if(currentLayout.grid[x1][y1].name == currentLayout.grid[x2][y2].name){
						        							if((x2 - x1) > xMultiplicator)
						        								if((x2-x1)==(xMultiplicator+1)){
						        									xMultiplicator = x2 - x1;
						        								}else {
						        									containsString = containsString.replaceFirst(currentLayout.grid[x1][y1].name,"");
						        									xMultiplicator = 0;
						        								}
						        						}
					        						}
						        				}	
					        				}
				        					if(xMultiplicator==0){
				        						graph.insertVertex(parentVertex, null, currentLayout.grid[x1][y1].name, 20+x1*dimensionX, 45+y1*dimensionY+alreadyPaintedFlag, xMultiplicator * 5 + xMultiplicator * (dimensionX-10)  + (dimensionX-10), dimensionY-4, "rounded=1;strokeColor=black;fillColor=white;opacity=65;fontStyle=1;fontColor=black");
				        					}
				        					if(xMultiplicator>0){
				        						graph.insertVertex(parentVertex, null, currentLayout.grid[x1][y1].name, 20+x1*dimensionX, 45+y1*dimensionY+alreadyPaintedFlag, xMultiplicator * 5 + xMultiplicator * (dimensionX-10)  + (dimensionX-5), dimensionY-4, "rounded=1;strokeColor=black;fillColor=white;opacity=65;fontStyle=1;fontColor=black");
				        					}
				        				}
				        			}
		    	        		}
				        	}
			        	}else{
	                       compoundDimensionY = (maxRow+1)*dimensionY+70;
				        	graph.insertVertex(parentVertex, null, currentLayout.name, 5, 5+alreadyPaintedFlag, compound_dimensionX, compoundDimensionY, "rounded=1;strokeColor=black;fillColor=#CCCCCC;verticalAlign=top;fontStyle=1;fontColor=black" );
			        	
				        	//Roles
				        	Iterator<GroupLocator>  subIt = currentLayout.Groups.iterator();
				        	while (subIt.hasNext()) {
				        		GroupLocator currentGroup = subIt.next();
				        	
				        		if(firstKill[currentGroup.column]==null){ 
				        			firstKill[currentGroup.column]=0;
				        		}else{
				        			firstKill[currentGroup.column]=1;
				        		}
				        		
				        		//Find the related color
				        		int colorRelation = 0;
				        		for (int i=0; i<RoleColorAssociation.length;i++) {
				        			if (RoleColorAssociation[i] == currentGroup.name) {
				        				colorRelation = i;
				        				if (colorRelation>=colors.length) {
				        					colorRelation = colorRelation - colors.length;
				        				}
				        				break;
				        			}
				        		}
				        		graph.insertVertex(parentVertex, null, currentGroup.name, 15+dimensionX*currentGroup.column, 20+(dimensionY*currentGroup.ystart*2)+alreadyPaintedFlag+(firstKill[currentGroup.column]*20), dimensionX, dimensionY*(currentGroup.yend-currentGroup.ystart+1)-(firstKill[currentGroup.column]*dimensionY)+44, "rounded=1;strokeColor=black;fillColor="+colors[colorRelation]+";verticalAlign=top;fontStyle=1;fontColor=black");
				        	}
				        	
				        	//Components
				        	String containsString = new String();
				        	for (int x1=0; x1<currentLayout.grid.length;x1++) {
		        				for (int y1=0;y1<currentLayout.grid[x1].length;y1++) {
		        					int xMultiplicator = 0;
		    	        			if (currentLayout.grid[x1][y1] != null) {
				        				if(containsString.contains(currentLayout.grid[x1][y1].name)){
				        				} else if(!containsString.contains(currentLayout.grid[x1][y1].name)){
				        					containsString = containsString +" "+ currentLayout.grid[x1][y1].name;
				        					for(int x2 = x1 + 1; x2 < currentLayout.grid.length; x2++){
					        					for(int y2 = 0; y2 < currentLayout.grid[x2].length; y2++){
					        						if(currentLayout.grid[x2][y2] != null){
						        						if(currentLayout.grid[x1][y1].name == currentLayout.grid[x2][y2].name){
						        							if((x2 - x1) > xMultiplicator)
						        								if((x2-x1)==(xMultiplicator+1)){
						        									xMultiplicator = x2 - x1;
						        								}else {
						        									containsString = containsString.replaceFirst(currentLayout.grid[x1][y1].name,"");
						        									xMultiplicator = 0;
						        								}
						        						}
					        						}
						        				}	
					        				}
				        					if(xMultiplicator==0){
				        						graph.insertVertex(parentVertex, null, currentLayout.grid[x1][y1].name, 20+x1*dimensionX, 55+y1*dimensionY+alreadyPaintedFlag, xMultiplicator * 5 + xMultiplicator * (dimensionX-10)  + (dimensionX-10), dimensionY-4, "rounded=1;strokeColor=black;fillColor=white;opacity=65;fontStyle=1;fontColor=black");
				        					}
				        					if(xMultiplicator>0){
				        						graph.insertVertex(parentVertex, null, currentLayout.grid[x1][y1].name, 20+x1*dimensionX, 55+y1*dimensionY+alreadyPaintedFlag, xMultiplicator * 5 + xMultiplicator * (dimensionX-10)  + (dimensionX-5), dimensionY-4, "rounded=1;strokeColor=black;fillColor=white;opacity=65;fontStyle=1;fontColor=black");
				        					}
				        				}
				        			}
		    	        		}
				        	}
			        	}
			        	alreadyPaintedFlag = alreadyPaintedFlag + compoundDimensionY + 10;
			        }
		        } finally {
		            graph.getModel().endUpdate();
		        }
		        
		        if(alreadyPaintedFlag==0){
		        	graph.insertVertex(parentVertex, null,Messages.getString("PetriNet.Resources.NoGroupsDefined"),10,10,150,30, "rounded=1;opacity=0;fontStyle=1;fontColor=black");
		        }
		        
		        // Create Listener, first parameter in constructor is the observed graph
		        mxIEventListener mySelectListener = new selectListener(graph);
		        // add Listener as CHANGE-Listener
		        graph.getSelectionModel().addListener(mxEvent.CHANGE, mySelectListener);
		        graphComponent = new mxGraphComponent(graph);
	
		        tempGroupGraph = graph;
		        tempGroupGraphComponent= graphComponent;

		        return graphComponent;
		   }
	        
//****************************resourceExportButtonRole_PANEL********************************  
	   private JPanel getresourceExportButtonRole_Panel(){
		   if (resourceExportButtonRole_Panel == null){
			   resourceExportButtonRole_Panel = new JPanel();
			   resourceExportButtonRole_Panel.add(getresourceExportButtonRole());
		   }
		   return resourceExportButtonRole_Panel;
	   }
	   
	   private JButton getresourceExportButtonRole(){
	        if (resourceExportButtonRole == null){
	        	resourceExportButtonRole = new JButton();
	        	resourceExportButtonRole.setIcon(Messages.getImageIcon("ToolBar.Save"));
	        	resourceExportButtonRole.setToolTipText(Messages.getString("PetriNet.Resources.RoleExportButton"));
	        	resourceExportButtonRole.addActionListener(Export);
	        }
	        return resourceExportButtonRole;
	    }
	   
   //****************************resourceColorButtonRolePanel********************************  
	   private JPanel getresourceColorButtonRolePanel(){
		   if (resourceColorButtonRolePanel == null){
			   resourceColorButtonRolePanel = new JPanel();
			   resourceColorButtonRolePanel.add(getresourceColorButtonRole());
		   }
		   return resourceColorButtonRolePanel;
	   }
	   
	   private JButton getresourceColorButtonRole(){
	        if (resourceColorButtonRole == null){
	        	resourceColorButtonRole = new JButton();
	        	resourceColorButtonRole.setIcon(Messages.getImageIcon("Configuration.ColorLayout"));
	        	resourceColorButtonRole.setToolTipText(Messages.getString("PetriNet.Resources.RoleColoringButton"));
	        	resourceColorButtonRole.addActionListener(Coloring);
	        }
	        return resourceColorButtonRole;
	    }   
	   
	   
//****************************resourceExportButtonGroupPanel********************************  
	   private JPanel getresourceExportButtonGroupPanel(){
		   if (resourceExportButtonGroupPanel == null){
			   resourceExportButtonGroupPanel = new JPanel();
			   resourceExportButtonGroupPanel.add(getresourceExportButtonGroup());
		   }
		   return resourceExportButtonGroupPanel;
	   }
	   
	   private JButton getresourceExportButtonGroup(){
	        if (resourceExportButtonGroup == null){
	        	resourceExportButtonGroup = new JButton();
	        	resourceExportButtonGroup.setIcon(Messages.getImageIcon("ToolBar.Save"));
	        	resourceExportButtonGroup.setToolTipText(Messages.getString("PetriNet.Resources.GroupExportButton"));
	        	resourceExportButtonGroup.addActionListener(Export);
	        }
	        return resourceExportButtonGroup;
	    }
	   
 //****************************resourceColorButtonGroupPanel********************************  
	   private JPanel getresourceColorButtonGroupPanel(){
		   if (resourceColorButtonGroupPanel == null){
			   resourceColorButtonGroupPanel = new JPanel();
			   resourceColorButtonGroupPanel.add(getresourceColorButtonGroup());
		   }
		   return resourceColorButtonGroupPanel;
	   }
	   
	   private JButton getresourceColorButtonGroup(){
	        if (resourceColorButtonGroup == null){
	        	resourceColorButtonGroup = new JButton();
	        	resourceColorButtonGroup.setIcon(Messages.getImageIcon("Configuration.ColorLayout"));
	        	resourceColorButtonGroup.setToolTipText(Messages.getString("PetriNet.Resources.GroupColoringButton"));
	        	resourceColorButtonGroup.addActionListener(Coloring);
	        }
	        return resourceColorButtonGroup;
	   }   
	   
 		// ActionListener used to Export Graph       
	   private class Export implements ActionListener{

		   		public void actionPerformed(ActionEvent e) {
				   try{
					   class ExtensionFilter extends FileFilter {
						    private String extensions[];
						    private String description;

						    public ExtensionFilter(String description, String extension) {
						      this(description, new String[] { extension });
						    }
						    public ExtensionFilter(String description, String extensions[]) {
						        this.description = description;
						        this.extensions = (String[]) extensions.clone();
						    }
						    public boolean accept(File file) {
						      if (file.isDirectory()) {
						        return true;
						      }
						      int count = extensions.length;
						      String path = file.getAbsolutePath();
						      for (int i = 0; i < count; i++) {
						        String ext = extensions[i];
						        if (path.endsWith(ext)
						            && (path.charAt(path.length() - ext.length()) == '.')) {
						          return true;
						        }
						      }
						      return false;
						    }

						    public String getDescription() {
						      return (description == null ? extensions[0] : description);
						    }
						  }
					   if(e.getSource()== resourceExportButtonRole ){
						   JFileChooser fc = new JFileChooser();
							fc.setDialogType(JFileChooser.SAVE_DIALOG);
							
							 FileFilter type1 = new ExtensionFilter("JPG", ".jpg");
							 FileFilter type2 = new ExtensionFilter("PNG", ".png");
						    fc.addChoosableFileFilter(type1);
						    fc.addChoosableFileFilter(type2);
						    fc.setFileFilter(type2);
						    int state = fc.showSaveDialog(null);
						    if (state == JFileChooser.APPROVE_OPTION) {
								
								if(fc.getFileFilter()==type1){
									String path = fc.getSelectedFile().getPath();
									Color exportImageBackgroundColor = Color.white;
							        BufferedImage image = mxCellRenderer.createBufferedImage(temp_Role_graph, null, 1, exportImageBackgroundColor,temp_Role_graphComponent.isAntiAlias(), null,temp_Role_graphComponent.getCanvas());
							        try {
							        ImageIO.write(image, "jpg", new File(path+".jpg"));
							        LoggerManager.info(Constants.EDITOR_LOGGER, Messages.getString("PetriNet.Resources.ExportGraphToFile")+path+".jpg");
							} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							}
								}else{
									String path = fc.getSelectedFile().getPath();

									   Color exportImageBackgroundColor = Color.white;
								        BufferedImage image = mxCellRenderer.createBufferedImage(temp_Role_graph, null, 1, exportImageBackgroundColor,temp_Role_graphComponent.isAntiAlias(), null,temp_Role_graphComponent.getCanvas());
								        try {
								        ImageIO.write(image, "png", new File(path+".png"));
								        LoggerManager.info(Constants.EDITOR_LOGGER, Messages.getString("PetriNet.Resources.ExportGraphToFile")+path+".png");
								     } catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								}	
								}
						}
				 	}
				
						   if(e.getSource()== resourceExportButtonGroup ){
							   JFileChooser fc = new JFileChooser();
								fc.setDialogType(JFileChooser.SAVE_DIALOG);
								
								 FileFilter type1 = new ExtensionFilter("JPG", ".jpg");
								 FileFilter type2 = new ExtensionFilter("PNG", ".png");
							    fc.addChoosableFileFilter(type1);
							    fc.addChoosableFileFilter(type2);
							    fc.setFileFilter(type2);
							    int state = fc.showSaveDialog(null);
							    if (state == JFileChooser.APPROVE_OPTION) {
									
									if(fc.getFileFilter()==type1){
										String path = fc.getSelectedFile().getPath();
										Color exportImageBackgroundColor = Color.white;
								        BufferedImage image = mxCellRenderer.createBufferedImage(tempGroupGraph, null, 1, exportImageBackgroundColor,tempGroupGraphComponent.isAntiAlias(), null,tempGroupGraphComponent.getCanvas());
								        try {
								        ImageIO.write(image, "jpg", new File(path+".jpg"));
								        LoggerManager.info(Constants.EDITOR_LOGGER, Messages.getString("PetriNet.Resources.ExportGraphToFile")+path+".jpg");
								     } catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
									}
									}else{
										String path = fc.getSelectedFile().getPath();

										   Color exportImageBackgroundColor = Color.white;
									        BufferedImage image = mxCellRenderer.createBufferedImage(tempGroupGraph, null, 1, exportImageBackgroundColor,tempGroupGraphComponent.isAntiAlias(), null,tempGroupGraphComponent.getCanvas());
									        try {
										        ImageIO.write(image, "png", new File(path+".png"));
										        LoggerManager.info(Constants.EDITOR_LOGGER, Messages.getString("PetriNet.Resources.ExportGraphToFile")+path+".png");
									        } catch (IOException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
										   }	
									}
							}
					 	}
					   
					}
				   catch(Exception ex){
				}
			   }
	   }

	   
// ActionListener used to Coloring Graph       
	   private class Coloring implements ActionListener{

			   public void actionPerformed(ActionEvent e) {
				   try{
					   	Color initialColor = Color.blue;		
				   		Color newColor;
				   		
				   		JFrame frame = new JFrame();

				   		// Show the dialog; this method does not return until the dialog is closed
				   		newColor = JColorChooser.showDialog(frame, "Color chooser", initialColor);
				   		 
				   		currentCell.setStyle("rounded=1;strokeColor=black;verticalAlign=top;fillColor="+PetriNetResourceEditor.returnHexColor(newColor));
				   		currentGraph.refresh();
				   		
				   		if(e.getSource()== resourceColorButtonGroup ){

						   //JOptionPane.showMessageDialog(null, "Computer sagt NEIN", "Fehlermeldung", JOptionPane.ERROR_MESSAGE);
						   
					   	}
					   	if(e.getSource()== resourceColorButtonRole ){
							   
							 //JOptionPane.showMessageDialog(null, "Computer sagt NEIN", "Fehlermeldung", JOptionPane.ERROR_MESSAGE);
							 //mxGraph Objekt, jede Zelle im Graph pruefbar ob aktiv mit while schleife im mouselistener
					   	}
				   }
				   catch(Exception ex){
					   
				   }
			   }  
	   }
	   
	   public static String returnHexColor(Color newColor){
		   
	   		String myRed="";
	   		String myGreen="";
	   		String myBlue="";

	   		myRed	=  Integer.toHexString(newColor.getRed());
	   		if(myRed.length()==1) {myRed = "0"+myRed;};
	   		myGreen = Integer.toHexString(newColor.getGreen());
			if(myGreen.length()==1) {myGreen = "0"+myGreen;};
	   		myBlue  = Integer.toHexString(newColor.getBlue());
			if(myBlue.length()==1) {myBlue = "0"+myBlue;};
			
			return myRed+myGreen+myBlue;
	   }
	   
//		*****************OBJECT_PANEL*************************************
//	 	*****************************************************************
	   private JPanel getObjectsContentPanel(){
		   if (objectsPanel == null){
			   objectsPanel = new JPanel(new GridBagLayout());
			   objectsPanel.setBorder(BorderFactory
	                    .createCompoundBorder(BorderFactory.createTitledBorder(
	                    		Messages.getString("PetriNet.Resources.Resource")), 
	                    		BorderFactory.createEmptyBorder()));


	            SwingUtils.setFixedSize(objectsPanel,300,580);

	            GridBagConstraints c = new GridBagConstraints();
	            c.weightx = 1;
		        c.weighty = 1;
		        c.anchor = GridBagConstraints.NORTH;

		        c.gridx = 0;
		        c.gridy = 0;
		        objectsPanel.add(getObjectsButtonPanel(),c);
	            
		        c.fill = GridBagConstraints.VERTICAL;
		        c.gridx = 0;
		        c.gridy = 1;
		        objectsPanel.add(getObjectsScrollPane(),c);
		   }
		   return objectsPanel;
	   }
	   
//****************************OBJECTS_BUTTON_PANEL********************************   
	   private JPanel getObjectsButtonPanel(){
		   if (objectsButtonPanel == null){
			   objectsButtonPanel = new JPanel(new GridLayout());
			   SwingUtils.setFixedSize(objectsButtonPanel,300,23);
			   objectsButtonPanel.add(getObjectsNewButton());
			   objectsButtonPanel.add(getObjectsEditButton());
			   objectsButtonPanel.add(getObjectsDeleteButton());
			   objectsButtonPanel.add(getObjectsExpandButton());
			   objectsButtonPanel.add(getObjectsCollapseButton());
		   }
		   return objectsButtonPanel;
	   }
	   
	   
	   private JButton getObjectsNewButton(){
	        if (objectsNewButton == null){
	        	objectsNewButton = new JButton();
	        	objectsNewButton.setIcon(Messages.getImageIcon("PetriNet.Resources.New"));
	        	objectsNewButton.setToolTipText(Messages.getString("PetriNet.Resources.New.Title"));
	        	objectsNewButton.addActionListener(createResource);
	        }

	        return objectsNewButton;
	    }
	   
	   private JButton getObjectsEditButton(){
	        if (objectsEditButton == null){
	        	objectsEditButton = new JButton();
	        	objectsEditButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Edit"));
	        	objectsEditButton.setToolTipText(Messages.getString("Button.Edit.Title"));
	        	objectsEditButton.setEnabled(false);
	        	objectsEditButton.addActionListener(editResource);
	        }

	        return objectsEditButton;
	    }
	   
	   private JButton getObjectsDeleteButton(){
	        if (objectsDeleteButton == null){
	        	objectsDeleteButton = new JButton();
	        	objectsDeleteButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Delete"));
	        	objectsDeleteButton.setToolTipText(Messages.getString("PetriNet.Resources.Delete.Title"));
	        	objectsDeleteButton.addActionListener(removeResource);
	        	objectsDeleteButton.setEnabled(false);
	        	
	        }

	        return objectsDeleteButton;
	    }
	   
	   private JButton getObjectsExpandButton(){
	        if (objectsExpandButton == null){
	        	objectsExpandButton = new JButton();
	        	objectsExpandButton.setEnabled(false);
	        	objectsExpandButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Expand"));
	        	objectsExpandButton.setToolTipText(Messages.getString("PetriNet.Resources.Expand.Title"));
	        	objectsExpandButton.addActionListener(expandButtonListener);

	        }

	        return objectsExpandButton;
	    }
	   
	   private JButton getObjectsCollapseButton(){
	        if (objectsCollapseButton == null){
	        	objectsCollapseButton = new JButton();
	        	objectsCollapseButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Collapse"));
	        	objectsCollapseButton.setToolTipText(Messages.getString("PetriNet.Resources.Collapse.Title"));
	        	objectsCollapseButton.addActionListener(collapseButtonListener);
	        	objectsCollapseButton.setEnabled(false);
	        }

	        return objectsCollapseButton;
	   }
	   
//	   *********************OBJECTS_CONTENT_PANEL**********************************

	   private JScrollPane getObjectsScrollPane(){
		   if (objectsScrollPane == null){
			   objectsScrollPane = new JScrollPane(getObjectsTree());
			   objectsScrollPane.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.GRAY));


			   SwingUtils.setFixedSize(objectsScrollPane, 300,530);

		   }
		   return objectsScrollPane;
	   }
	   
	   
	  private DragTree getObjectsTree(){
		   if (objectsTree == null){
			    objectsTopNode = new DefaultMutableTreeNode("Objects");
			    objectsTreeModel = new DefaultTreeModel(objectsTopNode);		    
			    objectsUnassignedNode = new DefaultMutableTreeNode(
			    		Messages.getString("PetriNet.Resources.Resource.Unassigned"));
			    objectsTopNode.add(objectsUnassignedNode);
			    objectsAssignedNode = new DefaultMutableTreeNode(
			    		Messages.getString("PetriNet.Resources.Resource.Assigned"));
			    objectsTopNode.add(objectsAssignedNode);			
			    objectsTree = new DragTree(objectsTopNode,objectsAssignedNode);			    
			    objectsTree.setRootVisible(false);
			    objectsTree.setRowHeight(20);
			    objectsTree.setEditable(false);
			    objectsTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			    objectsTree.addTreeSelectionListener(new MyTreeSelectionListener());
			    objectsTree.addTreeExpansionListener(treeListener);
			    objectsTree.setShowsRootHandles(true);
			    objectsTree.setFont(Nodes);	
			    objectsTree.setShowsRootHandles(true);
			    objectsTree.setCellRenderer(rendererResourceClass);

			    
		   }return (DragTree)objectsTree;
	   }
	  
	
//*******************************GRAPHIC_PANEL*******************************
//***********************************************************************	 
	  private JPanel getGraphicPanel(){
		  if (graphicPanel == null){
			  graphicPanel = new JPanel(new GridBagLayout());



			  SwingUtils.setFixedSize(graphicPanel,60,200);

	            
	            JLabel graphic1 = new JLabel(), graphic2 = new JLabel();
	            
	            graphic1.setIcon(Messages.getImageIcon("PetriNet.Resources.Mouse"));
	            graphic1.setToolTipText("Drag&Drop");
	            graphic2.setIcon(Messages.getImageIcon("PetriNet.Resources.Drag"));
	            graphic2.setToolTipText("Drag&Drop");
	           	            
	            GridBagConstraints c = new GridBagConstraints();
	            c.weightx = 2;
		        c.weighty = 2;
		        c.anchor = GridBagConstraints.NORTHEAST;
		        c.gridx = 0;
		        c.gridy = 0;
		        graphicPanel.add(graphic1, c);
		        c.anchor = GridBagConstraints.NORTHWEST;
		        c.gridx = 1;
		        c.gridy = 0;
		        graphicPanel.add(graphic2, c);
		  }
		  
		  return graphicPanel;
	  }
	  

	  
	// *******************************ROLES_PANEL********************************************  
	// ***********************************************************
	   private JPanel getRolesContentPanel(){
		   if (RolesContentPanel  == null){
			   RolesContentPanel  = new JPanel(new GridBagLayout());

	            SwingUtils.setFixedSize(RolesContentPanel , 300,580);
	          
	            GridBagConstraints c = new GridBagConstraints();
	            
		        c.weightx = 1;
		        c.weighty = 1;
		        c.anchor = GridBagConstraints.NORTH;
	           
	            c.gridx = 0;
		        c.gridy = 0;
		        RolesContentPanel.add(getRolesPanel(),c);
                     
		        c.fill = GridBagConstraints.VERTICAL;
	            c.gridx = 0;
	            c.gridy = 1;
	            RolesContentPanel.add(getSuperRolesPanel(),c);
		   }
		   return RolesContentPanel ;
	   }

//****************************ROLES_PANEL********************************
	   private JPanel getRolesPanel(){
		   if (RolesPanel  == null){
			   RolesPanel  = new JPanel(new GridBagLayout());
	            RolesPanel .setBorder(BorderFactory
	                    .createCompoundBorder(BorderFactory.createTitledBorder(
	                    		Messages.getString("PetriNet.Resources.Roles")), 
	                    		BorderFactory.createEmptyBorder()));
	            SwingUtils.setFixedSize(RolesPanel , 300,395);
	            
	            GridBagConstraints c = new GridBagConstraints();
	            
		        c.weightx = 1;
		        c.weighty = 1;
		        c.anchor = GridBagConstraints.NORTH;
		        c.gridx = 0;
		        c.gridy = 0;
		        RolesPanel .add(getRolesButtonPanel(),c);
		        
		        c.fill = GridBagConstraints.VERTICAL;
	            c.gridx = 0;
	            c.gridy = 1;
	            RolesPanel .add(getRolesScrollPane(),c);
		   }
		   return RolesPanel;
	   }

	   

//****************************ROLES_BUTTON_PANEL*******************************	   
	   private JPanel getRolesButtonPanel(){
		   if (RolesButtonPanel == null){
			   RolesButtonPanel = new JPanel(new GridLayout());
			   SwingUtils.setFixedSize(RolesButtonPanel, 300,23);
			   RolesButtonPanel.add(getRolesNewButton());
			   RolesButtonPanel.add(getRolesEditButton());
			   RolesButtonPanel.add(getRolesDeleteButton());
			   RolesButtonPanel.add(getRolesExpandButton());
			   RolesButtonPanel.add(getRolesCollapseButton());
		   }
		   return RolesButtonPanel;
	   }
	   
	   
	   private JButton getRolesNewButton(){
	        if (RolesNewButton == null){
	        	RolesNewButton = new JButton();
	        	RolesNewButton.setIcon(Messages.getImageIcon("PetriNet.Resources.New"));
	        	RolesNewButton.setToolTipText(Messages.getString("PetriNet.Resources.New.Title"));
	            RolesNewButton.addActionListener(createResource);
	        }

	        return RolesNewButton;
	    }
	   
	   private JButton getRolesEditButton(){


	        if (RolesEditButton == null){
	        	RolesEditButton = new JButton();
	        	RolesEditButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Edit"));
	        	RolesEditButton.setToolTipText(Messages.getString("Button.Edit.Title"));
	        	RolesEditButton.setEnabled(false);
	        	RolesEditButton.addActionListener(editResource);

	        }

	        return RolesEditButton;
	    }
	   
	   private JButton getRolesDeleteButton(){
	        if (RolesDeleteButton == null){
	        	RolesDeleteButton = new JButton();
	        	RolesDeleteButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Delete"));
	        	RolesDeleteButton.setToolTipText(Messages.getString("PetriNet.Resources.Delete.Title"));
	        	RolesDeleteButton.addActionListener(removeResource);
	        	RolesDeleteButton.setEnabled(false);
	        }

	        return RolesDeleteButton;
	    }
	   
	   private JButton getRolesExpandButton(){
	        if (RolesExpandButton == null){
	        	RolesExpandButton = new JButton();
	        	RolesExpandButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Expand"));
	        	RolesExpandButton.setToolTipText(Messages.getString("PetriNet.Resources.Expand.Title"));
	        	RolesExpandButton.setEnabled(false);
	        	RolesExpandButton.addActionListener(expandButtonListener);
	        }

	        return RolesExpandButton;
	    }
	   
	   private JButton getRolesCollapseButton(){
	        if (RolesCollapseButton == null){
	        	RolesCollapseButton = new JButton();
	        	RolesCollapseButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Collapse"));
	        	RolesCollapseButton.setToolTipText(Messages.getString("PetriNet.Resources.Collapse.Title"));
	        	RolesCollapseButton.addActionListener(collapseButtonListener);
	        	RolesCollapseButton.setEnabled(false);
	        }

	        return RolesCollapseButton;
	    }
	   
//	   *********************ROLES_CONTENT_PANEL**************************
	
	   private JScrollPane getRolesScrollPane(){
		   if (RolesScrollPane == null){
			   RolesScrollPane = new JScrollPane(getRolesTree());
			   RolesScrollPane.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.GRAY));


			   SwingUtils.setFixedSize(RolesScrollPane, 300,345);



		   }
		   return RolesScrollPane;
	   }
	  	   
	   private DropTree getRolesTree(){
		   if (RolesTree == null){
			    RolesTopNode = new DefaultMutableTreeNode("Roles");
			    RolesTreeModel = new DefaultTreeModel(RolesTopNode);
			    RolesTree = new DropTree(RolesTreeModel,getPetrinet());
			    RolesTree.setRowHeight(20);
			    RolesTree.setEditable(false);
			    RolesTree.setRootVisible(false);
			    RolesTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			    RolesTree.setShowsRootHandles(true);
			    RolesTree.setFont(Nodes);  
			    RolesTree.setFont(Nodes);
			    RolesTree.addTreeSelectionListener(treeSelection);
			    RolesTree.addTreeExpansionListener(treeListener);
			    RolesTree.setCellRenderer(rendererResourceClass);

		   }return (DropTree)RolesTree;
	   }
	   

	 //*********************SUPER_ROLES_PANEL*************************************************************
	   private JPanel getSuperRolesPanel(){
		   if (superRolesPanel == null){
			   superRolesPanel = new JPanel(new GridBagLayout());
			   superRolesPanel.setBorder(BorderFactory
	                    .createCompoundBorder(BorderFactory.createTitledBorder(
	                    		Messages.getString("PetriNet.Resources.SuperRoles")), 
	                    		BorderFactory.createEmptyBorder()));
	            SwingUtils.setFixedSize(superRolesPanel, 300,180);
	          
	            GridBagConstraints c = new GridBagConstraints();
	            
		        c.weightx = 1;
		        c.weighty = 1;
		        c.anchor = GridBagConstraints.NORTH;
		        c.gridx = 0;
		        c.gridy = 0;
		        superRolesPanel.add(getSuperRolesButtonPanel(),c);
		        
		        c.fill = GridBagConstraints.VERTICAL;
	            c.gridx = 0;
	            c.gridy = 1;
	            superRolesPanel.add(getSuperRolesScrollPane(),c);
		   }
		   return superRolesPanel;
	   }  
		  

//			************************SUPER_ROLES_BUTTON_PANEL***************
		  private JPanel getSuperRolesButtonPanel(){
			   if (superRolesButtonPanel == null){
				   superRolesButtonPanel = new JPanel(new GridLayout());
				   SwingUtils.setFixedSize(superRolesButtonPanel, 300,23);
				   superRolesButtonPanel.add(getSuperRolesNewButton());
				   superRolesButtonPanel.add(getSuperRolesEditButton());
				   superRolesButtonPanel.add(getSuperRolesDeleteButton());
				   superRolesButtonPanel.add(getSuperRolesExpandButton());
				   superRolesButtonPanel.add(getSuperRolesCollapseButton());
			   }
			   return superRolesButtonPanel;
		   }
		   
		   
		   private JButton getSuperRolesNewButton(){
		        if (superRolesNewButton == null){
		        	superRolesNewButton = new JButton();
		        	superRolesNewButton.setIcon(Messages.getImageIcon("PetriNet.Resources.New"));
		        	superRolesNewButton.setToolTipText(Messages.getString("PetriNet.Resources.New.Title"));
		            superRolesNewButton.addActionListener(createSuperResourceFrame);
		        }

		        return superRolesNewButton;
		    }
		   
		   private JButton getSuperRolesEditButton(){
		        if (superRolesEditButton == null){
		        	superRolesEditButton = new JButton();
		        	superRolesEditButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Edit"));
		        	superRolesEditButton.setToolTipText(Messages.getString("Button.Edit.Title"));
		        	superRolesEditButton.setEnabled(false);
		        	superRolesEditButton.addActionListener(editSuperResourceFrame);
		        }

		        return superRolesEditButton;
		    }
		   
		   private JButton getSuperRolesDeleteButton(){
		        if (superRolesDeleteButton == null){
		        	superRolesDeleteButton = new JButton();
		        	superRolesDeleteButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Delete"));
		        	superRolesDeleteButton.setToolTipText(Messages.getString("PetriNet.Resources.Delete.Title"));
		        	superRolesDeleteButton.addActionListener(removeSuperResource);
		        	superRolesDeleteButton.setEnabled(false);
		        }

		        return superRolesDeleteButton;
		    }


		   
		   private JButton getSuperRolesExpandButton(){
		        if (superRolesExpandButton == null){
		        	superRolesExpandButton = new JButton();
		        	superRolesExpandButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Expand"));
		        	superRolesExpandButton.setToolTipText(Messages.getString("PetriNet.Resources.Expand.Title"));
		        	superRolesExpandButton.setEnabled(false);
		        	superRolesExpandButton.addActionListener(expandButtonListener);
		        }

		        return superRolesExpandButton;
		    }
		   
		   private JButton getSuperRolesCollapseButton(){
		        if (superRolesCollapseButton == null){
		        	superRolesCollapseButton = new JButton();
		        	superRolesCollapseButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Collapse"));
		        	superRolesCollapseButton.setToolTipText(Messages.getString("PetriNet.Resources.Collapse.Title"));
		        	superRolesCollapseButton.addActionListener(collapseButtonListener);
		        	superRolesCollapseButton.setEnabled(false);
		        }

		        return superRolesCollapseButton;
		    }



		  
//        ***************************SUPER_ROLES_CONTENT_PANEL********************

	          private JScrollPane getSuperRolesScrollPane(){
	              if (superRolesScrollPane == null){
	                   superRolesScrollPane = new JScrollPane(getSuperRolesTree());
	                   superRolesScrollPane.setBorder(BorderFactory.createEtchedBorder(new Color (141, 182, 205), new Color (132, 112, 255)));
	                   superRolesScrollPane.setBackground(new Color (2, 12, 241));
	                   SwingUtils.setFixedSize(superRolesScrollPane, 300,135);

	               }
	               return superRolesScrollPane;
	           }

	          private JTree getSuperRolesTree(){
	               if (superRolesTree == null){
	                    superRolesTopNode = new DefaultMutableTreeNode("Super-Roles");
	                    superRolesTreeModel = new DefaultTreeModel(superRolesTopNode);
	                    superRolesTree = new JTree(superRolesTreeModel);
	                    superRolesTree.setRowHeight(20);
	                    superRolesTree.setEditable(false);
	                    superRolesTree.setRootVisible(false);
	                    superRolesTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	                    superRolesTree.setShowsRootHandles(true);
	                    superRolesTree.setFont(Nodes);
	                    superRolesTree.addTreeExpansionListener(treeListener);
	                    superRolesTree.addTreeSelectionListener(treeSelection);
	                    superRolesTree.setCellRenderer(rendererResourceSuperClass);
	               }return (JTree)superRolesTree;
	           }
	   
//	   **************************GROUPS_PANEL****************************
//	   *****************************************************************
	   private JPanel getGroupsContentPanel(){
		   if (GroupsContentPanel == null){
			   GroupsContentPanel = new JPanel(new GridBagLayout());
	            SwingUtils.setFixedSize(GroupsContentPanel, 300,580);
	            GridBagConstraints c = new GridBagConstraints();
	            
		        c.weightx = 1;
		        c.weighty = 1;
		        c.anchor = GridBagConstraints.NORTH;

				c.gridx = 0;
		        c.gridy = 0;
		        GroupsContentPanel.add(getGroupsPanel(),c);
                     
		        c.fill = GridBagConstraints.VERTICAL;
	            c.gridx = 0;
	            c.gridy = 1;
	            GroupsContentPanel.add(getSuperGroupsPanel(),c);
		   }
		   return GroupsContentPanel;
	   }
	   
 //****************************GROUPS_PANEL and  with Border ********************************
	   private JPanel getGroupsPanel(){
		   if (GroupsPanel == null){
			   GroupsPanel = new JPanel(new GridBagLayout());
			   GroupsPanel.setBorder(BorderFactory
	                    .createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("PetriNet.Resources.Groups")), BorderFactory.createEmptyBorder()));
	            SwingUtils.setFixedSize(GroupsPanel , 300,395);
	          
	            GridBagConstraints c = new GridBagConstraints();
	            
		        c.weightx = 1;
		        c.weighty = 1;
		        c.anchor = GridBagConstraints.NORTH;
		        c.gridx = 0;
		        c.gridy = 0;
		        GroupsPanel.add(getGroupsButtonPanel(),c);
		        
		        c.fill = GridBagConstraints.VERTICAL;
	            c.gridx = 0;
	            c.gridy = 1;
	            GroupsPanel.add(getGroupsScrollPane(),c);
		   }
		   return GroupsPanel;
	   }

	   
//	   *********************GROUPS_CONTENT_PANEL**************************

	   private JScrollPane getGroupsScrollPane(){
		   if (GroupsScrollPane == null){
			   GroupsScrollPane = new JScrollPane(getGroupsTree());
			   GroupsScrollPane.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.GRAY));
			   SwingUtils.setFixedSize(GroupsScrollPane, 300,345);
			   

		   }
		   return GroupsScrollPane;
	   }
	   private DropTree getGroupsTree(){
		   if (GroupsTree == null){
			    GroupsTopNode = new DefaultMutableTreeNode("Groups");
			    GroupsTreeModel = new DefaultTreeModel(GroupsTopNode);
			    GroupsTree = new DropTree(GroupsTreeModel,getPetrinet());
			    GroupsTree.setRowHeight(20);
			    GroupsTree.setRootVisible(false);
			    GroupsTree.setEditable(false);
			    GroupsTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			    GroupsTree.setShowsRootHandles(true);
			    GroupsTree.setFont(Nodes);	
			    GroupsTree.addTreeExpansionListener(treeListener);
			    GroupsTree.addTreeSelectionListener(treeSelection);
		        GroupsTree.setCellRenderer(rendererResourceClass);
		        GroupsTree.updateUI();

		   }return (DropTree)GroupsTree;
	   }
	   

	   
	 //****************************GROUPS_BUTTON_PANEL********************************	   
	   private JPanel getGroupsButtonPanel(){
		   if (GroupsButtonPanel == null){
			   GroupsButtonPanel = new JPanel(new GridLayout());
			   SwingUtils.setFixedSize(GroupsButtonPanel, 300,23);
			   GroupsButtonPanel.add(getGroupsNewButton());
			   GroupsButtonPanel.add(getGroupsEditButton());
			   GroupsButtonPanel.add(getGroupsDeleteButton());
			   GroupsButtonPanel.add(getGroupsExpandButton());
			   GroupsButtonPanel.add(getGroupsCollapseButton());
		   }
		   return GroupsButtonPanel;
	   }
	   
	   
	   private JButton getGroupsNewButton(){
	        if (GroupsNewButton == null){
	        	GroupsNewButton = new JButton();
	        	GroupsNewButton.setIcon(Messages.getImageIcon("PetriNet.Resources.New"));
	        	GroupsNewButton.setToolTipText(Messages.getString("PetriNet.Resources.New.Title"));
	        	GroupsNewButton.addActionListener(createResource);
	        }

	        return GroupsNewButton;
	    }
	   
	   private JButton getGroupsEditButton(){
	        if (GroupsEditButton == null){
	        	GroupsEditButton = new JButton();
	        	GroupsEditButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Edit"));
	        	GroupsEditButton.setToolTipText(Messages.getString("Button.Edit.Title"));
	        	GroupsEditButton.setEnabled(false);
	        	GroupsEditButton.addActionListener(editResource);
	        }

	        return GroupsEditButton;
	    }
	   
	   private JButton getGroupsDeleteButton(){
	        if (GroupsDeleteButton == null){
	        	GroupsDeleteButton = new JButton();
	        	GroupsDeleteButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Delete"));
	        	GroupsDeleteButton.setToolTipText(Messages.getString("PetriNet.Resources.Delete.Title"));
	        	GroupsDeleteButton.addActionListener(removeResource);
	        	GroupsDeleteButton.setEnabled(false);
	        }

	        return GroupsDeleteButton;
	    }
	   


	   private JButton getGroupsExpandButton(){
	        if (GroupsExpandButton == null){
	        	GroupsExpandButton = new JButton();
	        	GroupsExpandButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Expand"));
	        	GroupsExpandButton.setToolTipText(Messages.getString("PetriNet.Resources.Expand.Title"));
	        	GroupsExpandButton.setEnabled(false);
	        	GroupsExpandButton.addActionListener(expandButtonListener);

	        }

	        return GroupsExpandButton;
	    }

	   
	   private JButton getGroupsCollapseButton(){
	        if (GroupsCollapseButton == null){
	        	GroupsCollapseButton = new JButton();
	        	GroupsCollapseButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Collapse"));
	        	GroupsCollapseButton.setToolTipText(Messages.getString("PetriNet.Resources.Collapse.Title"));
	        	GroupsCollapseButton.addActionListener(collapseButtonListener);
	        	GroupsCollapseButton.setEnabled(false);

	        }

	        return GroupsCollapseButton;
	    }

	 //**************************SUPER_GROUPS_PANEL**********************************************
	 //******************************************************************************************
	 	   private JPanel getSuperGroupsPanel(){
	 		   if (superGroupsPanel == null){
	 			   superGroupsPanel = new JPanel(new GridBagLayout());
	 			   superGroupsPanel.setBorder(BorderFactory
	 	                    .createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("PetriNet.Resources.SuperGroups")), BorderFactory.createEmptyBorder()));
	 	            SwingUtils.setFixedSize(superGroupsPanel, 300,180);
	 	          
	 	            GridBagConstraints c = new GridBagConstraints();
	 	            
	 		        c.weightx = 1;
	 		        c.weighty = 1;
	 		        c.anchor = GridBagConstraints.NORTH;
	 		        c.gridx = 0;
	 		        c.gridy = 0;
	 		        superGroupsPanel.add(getSuperGroupsButtonPanel(),c);
	 		        
	 		        c.fill = GridBagConstraints.VERTICAL;
	 	            c.gridx = 0;
	 	            c.gridy = 1;
	 	            superGroupsPanel.add(getSuperGroupsScrollPane(),c);
	 		   }
	 		   return superGroupsPanel;
	 	   }
	 	   
//      **********************SUPER_GROUPS_BUTTON_PANEL*****************************

		  private JPanel getSuperGroupsButtonPanel(){
			   if (superGroupsButtonPanel == null){
				   superGroupsButtonPanel = new JPanel(new GridLayout());
				   SwingUtils.setFixedSize(superGroupsButtonPanel, 300,23);
				   superGroupsButtonPanel.add(getSuperGroupsNewButton());
				   superGroupsButtonPanel.add(getSuperGroupsEditButton());
				   superGroupsButtonPanel.add(getSuperGroupsDeleteButton());
				   superGroupsButtonPanel.add(getSuperGroupsExpandButton());
				   superGroupsButtonPanel.add(getSuperGroupsCollapseButton());
			   }
			   return superGroupsButtonPanel;
		   }
		   
		   
		   private JButton getSuperGroupsNewButton(){
		        if (superGroupsNewButton == null){
		        	superGroupsNewButton = new JButton();
		        	superGroupsNewButton.setIcon(Messages.getImageIcon("PetriNet.Resources.New"));
		        	superGroupsNewButton.setToolTipText(Messages.getString("PetriNet.Resources.New.Title"));
		            superGroupsNewButton.addActionListener(createSuperResourceFrame);
		        }

		        return superGroupsNewButton;
		    }
		   
		   private JButton getSuperGroupsEditButton(){
		        if (superGroupsEditButton == null){
		        	superGroupsEditButton = new JButton();
		        	superGroupsEditButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Edit"));
		        	superGroupsEditButton.setToolTipText(Messages.getString("Button.Edit.Title"));
		        	superGroupsEditButton.setEnabled(false);
		        	superGroupsEditButton.addActionListener(editSuperResourceFrame);
		        }

		        return superGroupsEditButton;
		    }
		   
		   private JButton getSuperGroupsDeleteButton(){
		        if (superGroupsDeleteButton == null){
		        	superGroupsDeleteButton = new JButton();
		        	superGroupsDeleteButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Delete"));
		        	superGroupsDeleteButton.setToolTipText(Messages.getString("PetriNet.Resources.Delete.Title"));
		        	superGroupsDeleteButton.addActionListener(removeSuperResource);


		        	superGroupsDeleteButton.setEnabled(false);
		        }

		        return superGroupsDeleteButton;
		    }
		   
		   private JButton getSuperGroupsExpandButton(){
		        if (superGroupsExpandButton == null){
		        	superGroupsExpandButton = new JButton();
		        	superGroupsExpandButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Expand"));
		        	superGroupsExpandButton.setToolTipText(Messages.getString("PetriNet.Resources.Expand.Title"));
		        	superGroupsExpandButton.setEnabled(false);
		        	superGroupsExpandButton.addActionListener(expandButtonListener);
		        }

		        return superGroupsExpandButton;
		    }
		   
		   private JButton getSuperGroupsCollapseButton(){
		        if (superGroupsCollapseButton == null){
		        	superGroupsCollapseButton = new JButton();
		        	superGroupsCollapseButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Collapse"));
		        	superGroupsCollapseButton.setToolTipText(Messages.getString("PetriNet.Resources.Collapse.Title"));
		        	superGroupsCollapseButton.addActionListener(collapseButtonListener);
		        	superGroupsCollapseButton.setEnabled(false);
		        }

		        return superGroupsCollapseButton;
		    }

		   
//     ***************************SUPER_GROUPS_CONTENT_PANEL************************

	          private JScrollPane getSuperGroupsScrollPane(){
	              if (superGroupsScrollPane == null){
	                   superGroupsScrollPane = new JScrollPane(getSuperGroupsTree());
	                   superGroupsScrollPane.setBorder(BorderFactory.createEtchedBorder(new Color (141, 182, 205), new Color (132, 112, 255)));
	                   superGroupsScrollPane.setBackground(new Color (2, 12, 241));
	                   SwingUtils.setFixedSize(superGroupsScrollPane, 300,135);

	               }
	               return superGroupsScrollPane;
	           }
	          private JTree getSuperGroupsTree(){
	               if (superGroupsTree == null){
	                    superGroupsTopNode = new DefaultMutableTreeNode("Super-Groups");
	                    superGroupsTreeModel = new DefaultTreeModel(superGroupsTopNode);
	                    superGroupsTree = new JTree(superGroupsTreeModel);
	                    superGroupsTree.setRowHeight(20);
	                    superGroupsTree.setEditable(false);
	                    superGroupsTree.setRootVisible(false);
	                    superGroupsTree.getSelectionModel().setSelectionMode(
	                    		TreeSelectionModel.SINGLE_TREE_SELECTION);
	                    superGroupsTree.setShowsRootHandles(true);
	                    superGroupsTree.setFont(Nodes);
	                    superGroupsTree.addTreeExpansionListener(treeListener);
	                    superGroupsTree.addTreeSelectionListener(treeSelection);
	                    superGroupsTree.setCellRenderer(rendererResourceSuperClass);
	                 
	               }return (JTree)superGroupsTree;
	           }

    // ActionListener used to create Roles, Groups and objects       
	   private class createResource implements ActionListener{

			   public void actionPerformed(ActionEvent e) {
				   try{
					  
					  
					   	   // create a new Role
						   if(e.getSource()== RolesNewButton ){
							   String newResourceName = JOptionPane.showInputDialog(RolesContentPanel, 
									   (Object)Messages.getString("PetriNet.Resources.ResourceName"),
									   Messages.getString("PetriNet.Resources.CreateRole"),JOptionPane.QUESTION_MESSAGE);
							   if (checkClassSyntax(newResourceName )){
								   ResourceClassModel newRole = new ResourceClassModel(newResourceName, ResourceClassModel.TYPE_ROLE);
								   getPetrinet().addRole(newRole);
								   RolesListModel.addElement(newRole);
							    	refreshRolesFromModel();
							    	refreshGroupsFromModel();
							    	refreshObjectsFromModel();
							    	refreshGUI();
								   getEditor().setSaved(false);
							   }
						   }
						   // create a new Group
						   if(e.getSource()== GroupsNewButton ){
							   String newResourceName = JOptionPane.showInputDialog(RolesContentPanel, 
									   (Object)Messages.getString("PetriNet.Resources.ResourceName"),
									   Messages.getString("PetriNet.Resources.CreateGroup"),JOptionPane.QUESTION_MESSAGE);
							   if (checkClassSyntax(newResourceName )){
								   ResourceClassModel newGroup = new ResourceClassModel(newResourceName, 
										   ResourceClassModel.TYPE_ORGUNIT);
								   getPetrinet().addOrgUnit(newGroup);
								   GroupsListModel.addElement(newGroup);
							    	refreshRolesFromModel();
							    	refreshGroupsFromModel();
							    	refreshObjectsFromModel();
							    	refreshGUI();
								   getEditor().setSaved(false);
							   }
						   }
						   // create a new object
						   if(e.getSource()== objectsNewButton ){
							   String newResourceName = JOptionPane.showInputDialog(RolesContentPanel,
									   (Object)Messages.getString("PetriNet.Resources.ResourceName"),
									   Messages.getString("PetriNet.Resources.CreateObject"),JOptionPane.QUESTION_MESSAGE);
							   if (checkClassSyntax(newResourceName )){
								   ResourceModel newObject = new ResourceModel(newResourceName);
								   	getPetrinet().addResource(newObject);
								   	objectsUnassignedListModel.addElement(newObject);
							    	refreshRolesFromModel();
							    	refreshGroupsFromModel();
							    	refreshObjectsFromModel();
							    	refreshGUI();
								   	getEditor().setSaved(false);}
						   }
					   
					   
				   }catch (Exception ex){}
			   }  
		   }
	          
	   // ActionListener to remove a Role, Group or object AND to unassign an objects from a Role or Group
	   private class removeResource implements ActionListener{
	        		
	   		public void actionPerformed(ActionEvent e) {
	   			 
	   			   try{
	   				   //Remove an Object
	   				   if(e.getSource()== objectsDeleteButton){
	   					   if(!objectsTree.getLastSelectedPathComponent().equals(objectsUnassignedNode)&&
	   							   !objectsTree.getLastSelectedPathComponent().equals(objectsTopNode)&&
	   							   !objectsTree.getLastSelectedPathComponent().equals(objectsAssignedNode)){
	   						   MutableTreeNode nodeToDelete = (MutableTreeNode) objectsTree.getLastSelectedPathComponent();
	   						  // remove an object that isn't assigned 
	   						   if(nodeToDelete.getParent().equals(objectsUnassignedNode)){
	   							   objectsTreeModel.removeNodeFromParent(nodeToDelete);  
	   							   objectsUnassignedListModel.removeElement(nodeToDelete.toString());
	   							   int j = getPetrinet().containsResource(nodeToDelete.toString());
	   							   objectsUnassignedListModel.removeElement(j);
	   				               getPetrinet().getResources().remove(j);
	   							 	objectsEditButton.setEnabled(false);
	   							 	objectsDeleteButton.setEnabled(false);
							    	refreshRolesFromModel();
							    	refreshGroupsFromModel();
							    	refreshObjectsFromModel();
							    	refreshGUI();
	   							 	getEditor().setSaved(false);
	   							  
	   						   }
	   						   else{
	   							   //remove an object that is assigned
	   							   for (int i=0;i < RolesTopNode.getChildCount();i++){
	   								  
	   								   RolesTreeNode parent = (RolesTreeNode) RolesTopNode.getChildAt(i);
	   								   for (int j=0;j<parent.getChildCount();j++){
	   									   MutableTreeNode  child = (MutableTreeNode) parent.getChildAt(j);
	   									   if(child.toString().equals(nodeToDelete.toString())){
	   										   child.removeFromParent();
	   										   RolesTree.updateUI();		
	   									   }
	   								   }
	   							   }
	   							   for (int i=0;i < GroupsTopNode.getChildCount();i++){
	   									  
	   								   GroupsTreeNode parent = (GroupsTreeNode) GroupsTopNode.getChildAt(i);
	   								   for (int j=0;j<parent.getChildCount();j++){
	   									   MutableTreeNode  child = (MutableTreeNode) parent.getChildAt(j);
	   									   if(child.toString().equals(nodeToDelete.toString())){
	   										   child.removeFromParent();
	   										   GroupsTree.updateUI();
	   			
	   									   }
	   								   }
	   							   } 
	   							   objectsTreeModel.removeNodeFromParent(nodeToDelete);
	   							   objectsUnassignedListModel.removeElement(nodeToDelete.toString());
	   							   int j = getPetrinet().containsResource(nodeToDelete.toString());
	   							   objectsUnassignedListModel.removeElement(j);
	   				               getPetrinet().getResources().remove(j);
							    	refreshRolesFromModel();
							    	refreshGroupsFromModel();
							    	refreshObjectsFromModel();
							    	refreshGUI();
	   				               objectsEditButton.setEnabled(false);
	   				               objectsDeleteButton.setEnabled(false);
	   				               getEditor().setSaved(false);
	   						   }
	   					if(objectsAssignedNode.getChildCount()==0&&objectsUnassignedNode.getChildCount()==0){
	   							objectsDeleteButton.setEnabled(false);
	   							objectsEditButton.setEnabled(false); 
	   						   }
	   						   objectsTree.updateUI();
	   					   }
	   				   	}
	   			   }
	   			   catch (NullPointerException npe){
	   				   npe.printStackTrace();
	   			   }
//	   			   delete a Role or unassign an object from a Role
	   			   if(e.getSource()== RolesDeleteButton){
	   				  try{
	   					  String Role2remove = RolesTree.getLastSelectedPathComponent().toString();
	   					  if(!RoleIsUsed(Role2remove)){
	   						if(RolesDeleteButton.getToolTipText().equals(Messages.getString("PetriNet.Resources.Delete.Title"))){
	   						// Delete a Role
	   							RolesTreeNode nodeToDelete = (RolesTreeNode) RolesTree.getLastSelectedPathComponent();
	   							int j = getPetrinet().containsRole(Role2remove);	
	   							// if the Role is used in an compound Role show error message
	   							if(getPetrinet().getRoles().get(j).getSuperModels()!= null){
	   								  JOptionPane.showMessageDialog(RolesContentPanel, 
	   										  Messages.getString("ResourceEditor.Error.UsedResourceInSuperRole.Text"), 
	   										  Messages.getString("ResourceEditor.Error.UsedResourceClass.Title"),
	   					                        JOptionPane.ERROR_MESSAGE);
	   							}else{
	   								// delete a Role no objects are assigned to
	   								if(nodeToDelete.getChildCount()==0){
	   					                getPetrinet().getRoles().remove(j);
								    	refreshRolesFromModel();
								    	refreshGroupsFromModel();
								    	refreshObjectsFromModel();
								    	refreshGUI();
	   					                RolesDeleteButton.setEnabled(false);
	   								  	RolesEditButton.setEnabled(false);
	   					                getEditor().setSaved(false);
	   								}else{
	   									// delete a Role with objects assigned 
	   									getPetrinet().getRoles().remove(j);	
	   									for(int i=0;i<nodeToDelete.getChildCount();i++){
	   										String object2unassign = nodeToDelete.getChildAt(i).toString();	
	   										getPetrinet().removeResourceMapping(nodeToDelete.toString(), object2unassign);
	   									
	   									} 
								    	refreshRolesFromModel();
								    	refreshGroupsFromModel();
								    	refreshObjectsFromModel();
								    	refreshGUI();
	   					                RolesDeleteButton.setEnabled(false);
	   								  	RolesEditButton.setEnabled(false);
	   									getEditor().setSaved(false);
	   							  	
	   								}
	   							}
	   						}
	   						
	   						// Unassign an object from a Role
	   						else{
	   							String object2unassign = RolesTree.getLastSelectedPathComponent().toString();
	   							
	   							DefaultMutableTreeNode child =  (DefaultMutableTreeNode) RolesTree.getLastSelectedPathComponent();
	   							RolesTreeNode parent = (RolesTreeNode) child.getParent();
	   							
	   							int path = RolesTopNode.getIndex(parent);

	   							Vector<?> assignedClasses = getPetrinet().getResourceClassesResourceIsAssignedTo(object2unassign.toString());
	   							Object ass;
	   							for (Iterator<?> iter = assignedClasses.iterator(); iter.hasNext();){
	   			    				ass = iter.next();
	   			    				if(ass.toString().equals(parent.toString())){
	   			    					 getPetrinet().removeResourceMapping(parent.toString(), object2unassign);
	   			    				}
	   			    				for(int i=0;i<superRolesTopNode.getChildCount();i++){
	   			    					DefaultMutableTreeNode superRole=(DefaultMutableTreeNode) superRolesTopNode.getChildAt(i);
	   			    					for(int j=0;j<superRole.getChildCount();j++){
	   			    						String Role = superRole.getChildAt(j).toString();
	   			    						if(Role.equals(parent.toString())){
	   			    							getPetrinet().removeResourceMapping(superRole.toString(), object2unassign);
	   			    						}
	   			    					}
	   			    				}
	   							}	
						    	refreshRolesFromModel();
						    	refreshGroupsFromModel();
						    	refreshObjectsFromModel();
						    	refreshGUI();
	   						  	
	   						  	RolesDeleteButton.setEnabled(false);
	   						  	
	   						  	
	   						  	RolesTree.expandRow(path);
	   						}
	   						  
	   						  if(RolesTopNode.getChildCount()==0){
	   							RolesDeleteButton.setEnabled(false);
	   							RolesEditButton.setEnabled(false);
	   						  }			
	   						getEditor().setSaved(false);
	   					  }
	   			       else{
	   			         JOptionPane.showMessageDialog(RolesContentPanel, 
	   			        		 Messages.getString("ResourceEditor.Error.UsedResourceClass.Text"), 
	   			        		 Messages.getString("ResourceEditor.Error.UsedResourceClass.Title"),
	   			                        JOptionPane.ERROR_MESSAGE);
	   			       }
	   				  }
	   				  catch(Exception exc){
	   					  exc.printStackTrace();
	   				  }
	   			   }
	   			   // delete a Group or unassign an object from a Group
	   			   if(e.getSource()== GroupsDeleteButton){
	   				  try{
	   					  String Group2remove = GroupsTree.getLastSelectedPathComponent().toString();
	   					  if(!GroupIsUsed(Group2remove)){




	   						  if(GroupsDeleteButton.getToolTipText().equalsIgnoreCase(
	   								  Messages.getString("PetriNet.Resources.Delete.Title"))){

	   				  //Delete a Group
	   							  GroupsTreeNode nodeToDelete = (GroupsTreeNode) GroupsTree.getLastSelectedPathComponent();
	   							  int j = getPetrinet().containsOrgunit(Group2remove);
	   							// if the Group is used in an compound Group show error message
	   								if(getPetrinet().getOrganizationUnits().get(j).getSuperModels()!=null){
	   									  JOptionPane.showMessageDialog(RolesContentPanel,
	   											  Messages.getString("ResourceEditor.Error.UsedResourceInSuperGroup.Text"),
	   											  Messages.getString("ResourceEditor.Error.UsedResourceClass.Title"),
	   						                        JOptionPane.ERROR_MESSAGE);
	   								}else{
	   									// delete a Group no objects are assigned to
	   									if(nodeToDelete.getChildCount()==0){
	   						                getPetrinet().getOrganizationUnits().remove(j);
	   								    	refreshRolesFromModel();
	   								    	refreshGroupsFromModel();
	   								    	refreshObjectsFromModel();
	   								    	refreshGUI();
	   							        	GroupsDeleteButton.setEnabled(false);
	   							        	GroupsEditButton.setEnabled(false);
	   										
	   										getEditor().setSaved(false);
	   									}else{
	   										// delete a Group with objects assigned
	   								  		getPetrinet().getOrganizationUnits().remove(j);	
	   								  		for(int i=0;i<nodeToDelete.getChildCount();i++){
	   											String object2unassign = nodeToDelete.getChildAt(i).toString();	
	   											getPetrinet().removeResourceMapping(nodeToDelete.toString(), object2unassign);
	   								  		}
	   								    	refreshRolesFromModel();
	   								    	refreshGroupsFromModel();
	   								    	refreshObjectsFromModel();
	   								    	refreshGUI();
	   							        	GroupsDeleteButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Delete"));
	   							        	GroupsDeleteButton.setToolTipText(Messages.getString("PetriNet.Resources.Delete.Title"));
	   							        	GroupsDeleteButton.setEnabled(false);
	   							        	GroupsEditButton.setEnabled(false);
	   								  		
	   								  		getEditor().setSaved(false);
	   									}
	   								}
	   						}	  
	   					  // Unassign an object from a Group
	   					  else{
	   							String object2unassign = GroupsTree.getLastSelectedPathComponent().toString();
	   							
	   							DefaultMutableTreeNode child = (DefaultMutableTreeNode) GroupsTree.getLastSelectedPathComponent();
	   							GroupsTreeNode parent = (GroupsTreeNode) child.getParent();
	   							
	   							int path = GroupsTopNode.getIndex(parent);
	   							
	   							Vector<?> assignedClasses = getPetrinet().getResourceClassesResourceIsAssignedTo(object2unassign.toString());
	   							Object ass;
	   							for (Iterator<?> iter = assignedClasses.iterator(); iter.hasNext();){
	   			    				ass = iter.next();
	   			    				if(ass.toString().equals(parent.toString())){
	   			    					 getPetrinet().removeResourceMapping(parent.toString(), object2unassign);
	   			    				}
	   			    				for(int i=0;i<superGroupsTopNode.getChildCount();i++){
	   			    					DefaultMutableTreeNode superGroup =(DefaultMutableTreeNode) superGroupsTopNode.getChildAt(i);
	   			    					for(int j=0;j<superGroup.getChildCount();j++){
	   			    						String Group = superGroup.getChildAt(j).toString();
	   			    						if(Group.equals(parent.toString())){
	   			    							getPetrinet().removeResourceMapping(superGroup.toString(), object2unassign);
	   			    						}
	   			    					}
	   			    				}
	   						  		
	   							}
						    	refreshGroupsFromModel();
						    	refreshRolesFromModel();
						    	refreshObjectsFromModel();
						    	refreshGUI();
	   			      
	   							GroupsTree.expandRow(path);
	   							GroupsDeleteButton.setEnabled(false);
	   					  	}
	   						  
	   						  if (GroupsTopNode.getChildCount()==0){
	   							GroupsDeleteButton.setEnabled(false);
	   							GroupsEditButton.setEnabled(false);
	   						  }
	   ;
	   					  }
	   					  else{
	   						  JOptionPane.showMessageDialog(RolesContentPanel, 
	   								  Messages.getString("ResourceEditor.Error.UsedResourceClass.Text"), 
	   								  Messages.getString("ResourceEditor.Error.UsedResourceClass.Title"),
	   			                        JOptionPane.ERROR_MESSAGE);
	   					  }
	   					  getEditor().setSaved(false);
	   				  	}catch(NullPointerException npe){
	   					  npe.printStackTrace();
	   				  }
	   				  
	   			   }
	   		   }
	   	   }
	   	   
	   // Edit a Role Group or object	   
	   private class editResource implements ActionListener{
	   			
	   		   public void actionPerformed(ActionEvent e) {
	   			try{  
	   				//edit a Role
	   			   if(e.getSource()== RolesEditButton){
	   				   Object message = (Object) RolesTree.getLastSelectedPathComponent().toString();
	   				   String oldName = (String) message;
	   				   JOptionPane nameDialog = new JOptionPane(); 
	   				   String newName = (String) JOptionPane.showInputDialog(nameDialog,
	   						   Messages.getString("PetriNet.Resources.ResourceName"),
	   						   Messages.getString("PetriNet.Resources.EditRole"),
	                              JOptionPane.QUESTION_MESSAGE,null,null,message);
	   				   if (checkClassSyntax(newName )){
	   					   int j = getPetrinet().containsRole(oldName);
	   					   ResourceClassModel RoleModel = (ResourceClassModel) getPetrinet().getRoles().get(j);
	   	                   RoleModel.setName(newName);
	   	                   RolesListModel.set( j, RoleModel);
	   	                   updateRolesInPetrinet(oldName, newName);
					    	refreshRolesFromModel();
					    	refreshGroupsFromModel();
					    	refreshObjectsFromModel();
					    	refreshGUI();
	   	        
	   	                   getEditor().setSaved(false);
	   				   }
	   			   }
	   			   //edit a Group
	   			   if(e.getSource()== GroupsEditButton){
	   				   Object message = (Object) GroupsTree.getLastSelectedPathComponent().toString();
	   				   String oldName = (String) message;
	   				   JOptionPane nameDialog = new JOptionPane(); 
	   				   String newName = (String) JOptionPane.showInputDialog(nameDialog,
	   						   Messages.getString("PetriNet.Resources.ResourceName"),
	   						   Messages.getString("PetriNet.Resources.EditGroup"),
	                              JOptionPane.QUESTION_MESSAGE,null,null,message);
	   				   if (checkClassSyntax(newName )){
	   					   int j = getPetrinet().containsOrgunit(oldName);
	   					   ResourceClassModel GroupModel = (ResourceClassModel) getPetrinet().getOrganizationUnits().get(j);
	   	                   GroupModel.setName(newName);
	   	                   GroupsListModel.set( j, GroupModel);
	   	                   updateGroupsInPetrinet(oldName, newName);
	   	                 
					    	refreshRolesFromModel();
					    	refreshGroupsFromModel();
					    	refreshObjectsFromModel();
					    	refreshGUI();
	   	                
	   	                   getEditor().setSaved(false);
	   				   }
	   			   }
	   			   //edit an object
	   			   if(e.getSource()==objectsEditButton){
	   				   Object message = (Object) objectsTree.getLastSelectedPathComponent().toString();
	   				   JOptionPane nameDialog = new JOptionPane(); 
	   				   String newName = (String) JOptionPane.showInputDialog(nameDialog,
	   						   Messages.getString("PetriNet.Resources.ResourceName"),
	   						   Messages.getString("PetriNet.Resources.EditObject"),
	                              JOptionPane.QUESTION_MESSAGE,null,null,message);
	   				 if (checkClassSyntax(newName )){
	   					 	getPetrinet().replaceResourceMapping(message.toString(), newName);
	   		                ResourceModel resourceModel = (ResourceModel) getPetrinet().getResources().get(getPetrinet().containsResource(message.toString()));
	   		                resourceModel.setName(newName);
	   		         
					    	refreshRolesFromModel();
					    	refreshGroupsFromModel();
					    	refreshObjectsFromModel();
					    	refreshGUI();
	   				 }
	   			   }
	   			   getEditor().setSaved(false);
	   			}catch(Exception exc){
	   				exc.printStackTrace();
	   			}
	   		   }
	   		}
	          
	   // ActionListener that creates a dialog frame to create a compound Role or Group       
	   private class createSuperResourceFrame implements ActionListener {
			   	
	   		 public void actionPerformed(ActionEvent e) {
				   try{
					   dialogFrame = new JFrame();
					  // set frame title according to context
					   if(e.getSource()==superRolesNewButton){
						   dialogFrame.setTitle(Messages.getString("PetriNet.Resources.Resource.CreateSuperRole"));
					   }
					  if(e.getSource()==superGroupsNewButton){
						   dialogFrame.setTitle(Messages.getString("PetriNet.Resources.Resource.CreateSuperGroup")); 
					   }

					   dialogFrame.setSize(500, 400);
					   dialogFrame.setLocationRelativeTo(RolesContentPanel);
					   dialogFrame.setVisible(true);
					   dialogFrame.setResizable(false);

					   dialogFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		   		
					   java.awt.Container c =  dialogFrame.getContentPane();
					   JPanel contentPane = new JPanel(new BorderLayout());
					   contentPane.setBorder(BorderFactory.createEmptyBorder() );
					   contentPane.setBackground(Color.WHITE);
					   SwingUtils.setFixedSize(contentPane,500,400);
					   c.add(contentPane);
					   
					   JPanel namePanel = new JPanel(new GridBagLayout());
					   SwingUtils.setFixedSize(namePanel,500,50);
					   namePanel.setBorder(BorderFactory.createEtchedBorder(Color.WHITE, Color.GRAY));
					  
					   		GridBagConstraints a = new GridBagConstraints();
					   		a.weightx = 2;
					   		a.weighty = 1;
					   		JLabel nameLabel = new JLabel(Messages.getString("PetriNet.Resources.ResourceName")+":   ");
					   		dialogFrameTextField = new JTextField();
					   		SwingUtils.setFixedSize(dialogFrameTextField,260,25);
					
					   		a.gridx = 0;
					   		a.gridy = 0;
					   		a.anchor = GridBagConstraints.EAST;
					   		namePanel.add(nameLabel,a);
					 
					   		a.gridx = 1;					   
					   		a.gridy = 0;
					   		a.anchor = GridBagConstraints.WEST;
					   		namePanel.add(dialogFrameTextField,a);
					
					   		contentPane.add(namePanel,BorderLayout.NORTH);

					   JPanel defineResourcePanel = new JPanel(new GridBagLayout());
					   SwingUtils.setFixedSize(defineResourcePanel,500,300);
					   defineResourcePanel.setBorder(BorderFactory.createEtchedBorder(Color.WHITE, Color.GRAY));

					   		GridBagConstraints b = new GridBagConstraints();
					   		b.weightx = 1;
					   		b.weighty = 2;
					  
					   		JLabel addRolesLabel = new JLabel(("                                ")+
					   				(Messages.getString("PetriNet.Resources.Resource.AddtoSuperResource"))+(":"));
					   		b.gridx = 0;
					   		b.gridy = 1;
					   		b.anchor = GridBagConstraints.WEST;
					   		defineResourcePanel.add(addRolesLabel,b);
					   
					  
					   		JPanel selectPanel = new JPanel (new GridLayout());
					   		SwingUtils.setFixedSize(selectPanel,300,240);

					   		
					   		b.gridx = 0;
					   		b.gridy = 2;
					   		b.anchor = GridBagConstraints.CENTER;
					   		defineResourcePanel.add(selectPanel,b);
					   		// set selection list according to context
					   		if(e.getSource()==superRolesNewButton){
					   			if(selectedRolesList==null){
					   				selectedRolesList = new JList(RolesListModel);
					   				}   			
					   				selectedRolesList.setSelectionModel(new ToggleSelectionModel());
							   		JScrollPane selectScrollPane = new JScrollPane(selectedRolesList);
							   		selectScrollPane.setBackground(Color.WHITE);					   		
							   		SwingUtils.setFixedSize(selectScrollPane,300,260);
							   		selectPanel.add(selectScrollPane);

					   		}
					   		if(e.getSource()==superGroupsNewButton){
					   			selectedGroupsList = new JList (GroupsListModel);
					   			selectedGroupsList.setSelectionModel(new ToggleSelectionModel());
						   		JScrollPane selectScrollPane = new JScrollPane(selectedGroupsList);
						   		selectScrollPane.setBackground(Color.WHITE);					   		
						   		SwingUtils.setFixedSize(selectScrollPane,300,260);
						   		selectPanel.add(selectScrollPane);
					   			
					   		}

					   	contentPane.add(defineResourcePanel,BorderLayout.CENTER);
					 
					   
					   	JPanel buttonPanel = new JPanel();
					   	SwingUtils.setFixedSize(buttonPanel,500,50);
					   	buttonPanel.setBorder(BorderFactory.createEtchedBorder(Color.WHITE, Color.GRAY));
					   	
					   	dialogFrameConfirmButton = new JButton(Messages.getString("PetriNet.Resources.Ok.Title"));
					   	dialogFrameConfirmButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Ok"));
					   	dialogFrameConfirmButton.addActionListener(createSuperResource);
						SwingUtils.setFixedSize(dialogFrameConfirmButton,90,23);
					   	buttonPanel.add(dialogFrameConfirmButton);
					   	
					   	dialogFrameCancelButton = new JButton(Messages.getString
					   			("QuanlAna.ReachabilityGraph.Settings.Button.Cancel"));
					   	SwingUtils.setFixedSize(dialogFrameCancelButton,90,23);
					   	dialogFrameCancelButton.addActionListener(createSuperResource);
					   	buttonPanel.add(dialogFrameCancelButton);
					   	contentPane.add(buttonPanel,BorderLayout.SOUTH);
					   
					   dialogFrame.setContentPane(c);
					} catch (Exception exc) {
						exc.printStackTrace();
					}	   		
	   		 	}
		   
	   	   }
	   	   
	   // ActionListener registered to the OK and CANCEL button to check correctness of input
	   private class createSuperResourceListener implements ActionListener{
		@SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent e){
			   // CANCEL button pressed
			   if(e.getSource()==dialogFrameCancelButton){
					dialogFrame.dispose();
				}else{
					// create a new compound Role
					if(dialogFrame.getTitle().equals(Messages.getString("PetriNet.Resources.Resource.CreateSuperRole"))){
						Object [] selectedRoles = selectedRolesList.getSelectedValues();
						boolean checkedAndOk = true;
						// compound Role must at least contain 2 Roles
						if(selectedRoles.length<2){
							JOptionPane.showMessageDialog(dialogFrame , Messages.getString
								("ResourceEditor.Error.NoRolesChoosen.Text"), 
								Messages.getString("ResourceEditor.Error.NoRolesChoosen.Title"),
		                        JOptionPane.ERROR_MESSAGE);	
								checkedAndOk=false;
						}
						else if(dialogFrameTextField.getText().equals("")){
				            JOptionPane.showMessageDialog(dialogFrame, 
				            		Messages.getString("ResourceEditor.Error.EmptyResourceClass.Text"), 
				            		Messages.getString("ResourceEditor.Error.EmptyResourceClass.Title"),
				                    JOptionPane.ERROR_MESSAGE);	
				           
				            		checkedAndOk=false;
				        }
						// resource name already exists 
				        if(selectedRoles.length>=2&&!dialogFrameTextField.getText().equals("")){
				        	for (Iterator<?> iter = getPetrinet().getOrganizationUnits().iterator(); iter.hasNext();){
				        		if (iter.next().toString().equals(dialogFrameTextField.getText())){
				    	            JOptionPane.showMessageDialog(dialogFrame, 
				    	            		Messages.getString("ResourceEditor.Error.DuplicateResourceClass.Text"), 
				    	            		Messages.getString("ResourceEditor.Error.DuplicateResourceClass.Title"),
				    	                    JOptionPane.ERROR_MESSAGE);
				    	           
				    	            		checkedAndOk=false;
				        		} 
				               
				        	}
				        }
				        if(selectedRoles.length>=2&&!dialogFrameTextField.getText().equals("")){
				        	for (Iterator<?> iter = getPetrinet().getRoles().iterator();iter.hasNext();){
				        		if (iter.next().toString().equals(dialogFrameTextField.getText())){ 
					            JOptionPane.showMessageDialog(dialogFrame, 
					            		Messages.getString("ResourceEditor.Error.DuplicateResourceClass.Text"), 
					            		Messages.getString("ResourceEditor.Error.DuplicateResourceClass.Title"),
					                    JOptionPane.ERROR_MESSAGE);
					           
					            		checkedAndOk=false;
				        		}
				        	}
						}
						if(checkedAndOk){
							createSuperResource();
						}
					}
					// create a new compound Group
					if(dialogFrame.getTitle().equals(Messages.getString("PetriNet.Resources.Resource.CreateSuperGroup"))){
						Object [] selectedGroups = selectedGroupsList.getSelectedValues();
						boolean checkedAndOk = true;
						if(selectedGroups.length<2){
							JOptionPane.showMessageDialog(dialogFrame , Messages.getString
								("ResourceEditor.Error.NoGroupsChoosen.Text"), 
								Messages.getString("ResourceEditor.Error.NoGroupsChoosen.Title"),
		                        JOptionPane.ERROR_MESSAGE);	
								checkedAndOk=false;
						}
						else if(dialogFrameTextField.getText().equals("")){
				            JOptionPane.showMessageDialog(dialogFrame, 
				            		Messages.getString("ResourceEditor.Error.EmptyResourceClass.Text"), 
				            		Messages.getString("ResourceEditor.Error.EmptyResourceClass.Title"),
				                    JOptionPane.ERROR_MESSAGE);	
				           
				            		checkedAndOk=false;
				        }
				        if(selectedGroups.length>=2&&!dialogFrameTextField.getText().equals("")){
				        	for (Iterator<?> iter = getPetrinet().getOrganizationUnits().iterator(); iter.hasNext();){
				        		if (iter.next().toString().equals(dialogFrameTextField.getText())){
				    	            JOptionPane.showMessageDialog(dialogFrame, 
				    	            		Messages.getString("ResourceEditor.Error.DuplicateResourceClass.Text"), 
				    	            		Messages.getString("ResourceEditor.Error.DuplicateResourceClass.Title"),
				    	                    JOptionPane.ERROR_MESSAGE);
				    	           
				    	            		checkedAndOk=false;
				        		} 
				               
				        	}
				        }
				        if(selectedGroups.length>=2&&!dialogFrameTextField.getText().equals("")){
				        	for (Iterator<?> iter = getPetrinet().getRoles().iterator();iter.hasNext();){
				        		if (iter.next().toString().equals(dialogFrameTextField.getText())){ 
					            JOptionPane.showMessageDialog(dialogFrame, 
					            		Messages.getString("ResourceEditor.Error.DuplicateResourceClass.Text"), 
					            		Messages.getString("ResourceEditor.Error.DuplicateResourceClass.Title"),
					                    JOptionPane.ERROR_MESSAGE);
					           
					            		checkedAndOk=false;
				        		}
				        	}
						}
						if(checkedAndOk){
							createSuperResource();
						}
					}
					
				}
		   }
	       
	   }   

	   // method to create a compound resource
	   @SuppressWarnings("deprecation")
	private void createSuperResource() {
			// create a new compound Role	
		   if(selectedGroupsList==null&&selectedRolesList!=null){				
				try{
					Object [] selectedRoles = selectedRolesList.getSelectedValues();

							String superRole = dialogFrameTextField.getText();
							ResourceClassModel newSuperRole = new ResourceClassModel(superRole, ResourceClassModel.TYPE_ROLE);
							getPetrinet().addRole(newSuperRole);
							RolesListModel.addElement(newSuperRole);
							SuperRolesTreeNode superRoleNode = new SuperRolesTreeNode(superRole);
							RolesTreeModel.insertNodeInto(superRoleNode, superRolesTopNode,  superRolesTopNode.getChildCount());
						
							for(int i=0;i<selectedRoles.length;i++){
								String RoleName = selectedRoles[i].toString();
								int j = getPetrinet().containsRole(RoleName);
								ResourceClassModel currentRole = getPetrinet().getRoles().get(j);
								currentRole.addSuperModel(newSuperRole);

								// Assign the objects of the selected Roles to the compound Role
								DefaultMutableTreeNode child = new DefaultMutableTreeNode(RoleName);
								superRolesTreeModel.insertNodeInto(child, superRoleNode, superRoleNode.getChildCount());
								ArrayList <String> objects = getObjectsAssignedToResource(currentRole, ResourceClassModel.TYPE_ROLE);
								for(int a = 0; a< objects.size();a++ ){
									String currentObject = objects.get(a);
									petrinet.addResourceMapping(newSuperRole.toString(), currentObject);
								}

						}		
					    	refreshRolesFromModel();
					    	refreshGroupsFromModel();
					    	refreshObjectsFromModel();
					    	refreshGUI();
					selectedRolesList =null;
					selectedGroupsList =null;
					getEditor().setSaved(false);
					dialogFrame.dispose();
				}catch(Exception exc){
					exc.printStackTrace();
				}
				}
		   // create a new compound Group
			if(selectedRolesList==null&&selectedGroupsList!=null){
					Object [] selectedGroups = selectedGroupsList.getSelectedValues();
							String superGroup = dialogFrameTextField.getText();
							ResourceClassModel newSuperGroup = new ResourceClassModel(superGroup, ResourceClassModel.TYPE_ORGUNIT);
							getPetrinet().addOrgUnit(newSuperGroup);
							GroupsListModel.addElement(newSuperGroup);
							SuperGroupsTreeNode superGroupNode = new SuperGroupsTreeNode(superGroup);
							GroupsTreeModel.insertNodeInto(superGroupNode, superGroupsTopNode,  superGroupsTopNode.getChildCount());
						
							for(int i=0;i<selectedGroups.length;i++){
								String GroupName = selectedGroups[i].toString();
								int j = getPetrinet().containsOrgunit(GroupName);
								ResourceClassModel currentGroup = getPetrinet().getOrganizationUnits().get(j);
								currentGroup.addSuperModel(newSuperGroup);
								// Assign the objects of the selected Groups to the compound Group
								DefaultMutableTreeNode child = new DefaultMutableTreeNode(GroupName);
								superRolesTreeModel.insertNodeInto(child, superGroupNode, superGroupNode.getChildCount());
								ArrayList <String> objects = getObjectsAssignedToResource(currentGroup, ResourceClassModel.TYPE_ORGUNIT);
								for(int a = 0; a< objects.size();a++ ){
									String currentObject = objects.get(a);
									petrinet.addResourceMapping(newSuperGroup.toString(), currentObject);
								}
					
							}
					    	refreshRolesFromModel();
					    	refreshGroupsFromModel();
					    	refreshObjectsFromModel();
					    	refreshGUI();
							selectedRolesList =null;
							selectedGroupsList =null;
							getEditor().setSaved(false);
							dialogFrame.dispose();
			
			}
		   
		}

	   // ActionListener that creates the edit frame for compound resources
   	   private class editSuperResourceFrame implements ActionListener {
		   	
	   		 public void actionPerformed(ActionEvent e) {
				   try{
					   dialogFrame = new JFrame();
					  // set title according to context
					   if(e.getSource()== superRolesEditButton){
						   dialogFrame.setTitle(Messages.getString
								   ("PetriNet.Resources.Resource.EditSuperRole"));
					  }
					  if(e.getSource()== superGroupsEditButton){
						   dialogFrame.setTitle(Messages.getString
								   ("PetriNet.Resources.Resource.EditSuperGroup"));
					  }
					   dialogFrame.setSize(500, 400);
					   dialogFrame.setLocationRelativeTo(RolesContentPanel);
					   dialogFrame.setVisible(true);
					   dialogFrame.setResizable(false);

					   dialogFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		   		
					   java.awt.Container c =  dialogFrame.getContentPane();
					   JPanel contentPane = new JPanel(new BorderLayout());
					   contentPane.setBorder(BorderFactory.createEmptyBorder() );
					   contentPane.setBackground(Color.WHITE);
					   SwingUtils.setFixedSize(contentPane,500,400);
					   c.add(contentPane);
					   
					   JPanel namePanel = new JPanel(new GridBagLayout());
					   SwingUtils.setFixedSize(namePanel,500,50);
					   namePanel.setBorder(BorderFactory.createEtchedBorder(Color.WHITE, Color.GRAY));
					  
					   		GridBagConstraints a = new GridBagConstraints();
					   		a.weightx = 2;
					   		a.weighty = 1;
					   		JLabel nameLabel = new JLabel(Messages.getString
					   				("PetriNet.Resources.ResourceName")+":   ");
					   		dialogFrameTextField = new JTextField();
					   		SwingUtils.setFixedSize(dialogFrameTextField,260,25);
					
					   		a.gridx = 0;
					   		a.gridy = 0;
					   		a.anchor = GridBagConstraints.EAST;
					   		namePanel.add(nameLabel,a);
					 
					   		a.gridx = 1;					   
					   		a.gridy = 0;
					   		a.anchor = GridBagConstraints.WEST;
					   		namePanel.add(dialogFrameTextField,a);
					
					   		contentPane.add(namePanel,BorderLayout.NORTH);

					   JPanel defineResourcePanel = new JPanel(new GridBagLayout());
					   SwingUtils.setFixedSize(defineResourcePanel,500,300);
					   defineResourcePanel.setBorder(BorderFactory.createEtchedBorder(Color.WHITE, Color.GRAY));

					   		GridBagConstraints b = new GridBagConstraints();
					   		b.weightx = 1;
					   		b.weighty = 2;
					  
					   		JLabel addRolesLabel = new JLabel(("                                ")+
					   				(Messages.getString("PetriNet.Resources.Resource.AddtoSuperResource"))+(":"));
					   		b.gridx = 0;
					   		b.gridy = 1;
					   		b.anchor = GridBagConstraints.WEST;
					   		defineResourcePanel.add(addRolesLabel,b);
					   
					  
					   		JPanel selectPanel = new JPanel (new GridLayout());
					   		SwingUtils.setFixedSize(selectPanel,300,240);
					   		
					   		b.gridx = 0;
					   		b.gridy = 2;
					   		b.anchor = GridBagConstraints.CENTER;
					   		defineResourcePanel.add(selectPanel,b);
					   		if(e.getSource()==superRolesEditButton){
					   			selectedRolesList = new JList(RolesListModel);
					   			selectedRolesList.setSelectionModel(new ToggleSelectionModel());
					   			
					   			SuperRolesTreeNode superRole = (SuperRolesTreeNode) superRolesTree.getLastSelectedPathComponent();
					   			dialogFrameTextField.setText(superRole.toString());
					   			for (int i =0;i<superRole.getChildCount();i++){
					   				String currentRoleInTree = superRole.getChildAt(i).toString();
					   				for(int j=0;j < RolesListModel.getSize();j++){
					   					String currentRoleInList = RolesListModel.get(j).toString();
					   					if(currentRoleInTree.equals(currentRoleInList)){
					   						selectedRolesList.setSelectedIndex(j);	
					   					}
					   				}
					   			}
						   		JScrollPane selectScrollPane = new JScrollPane(selectedRolesList);
						   		selectScrollPane.setBackground(Color.WHITE);
						   		
						   		SwingUtils.setFixedSize(selectScrollPane,300,260);
						   		selectPanel.add(selectScrollPane);
					   		}
					   		
					   		if(e.getSource()==superGroupsEditButton){
					   			selectedGroupsList = new JList (GroupsListModel);
					   			
					   			selectedGroupsList.setSelectionModel(new ToggleSelectionModel());
					   			SuperGroupsTreeNode superGroup = (SuperGroupsTreeNode) superGroupsTree.getLastSelectedPathComponent();
					   			dialogFrameTextField.setText(superGroup.toString());
					   			for (int i =0;i<superGroup.getChildCount();i++){
					   				String currentGroupInTree = superGroup.getChildAt(i).toString();
					   				for(int j=0;j < GroupsListModel.getSize();j++){
					   					String currentGroupInList = GroupsListModel.get(j).toString();
					   					if(currentGroupInTree.equals(currentGroupInList)){
					   						selectedGroupsList.setSelectedIndex(j);	
					   					}
					   				}
					   			}
						   		JScrollPane selectScrollPane = new JScrollPane(selectedGroupsList);
						   		selectScrollPane.setBackground(Color.WHITE);
						   		
						   		SwingUtils.setFixedSize(selectScrollPane,300,260);
						   		selectPanel.add(selectScrollPane);
					   		}
					   
					   	contentPane.add(defineResourcePanel,BorderLayout.CENTER);
					 
					   
					   	JPanel buttonPanel = new JPanel();
					   	SwingUtils.setFixedSize(buttonPanel,500,50);
					   	buttonPanel.setBorder(BorderFactory.createEtchedBorder(Color.WHITE, Color.GRAY));
					   	
					   	dialogFrameConfirmButton = new JButton(Messages.getString("PetriNet.Resources.Ok.Title"));
					   	dialogFrameConfirmButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Ok"));
					   	dialogFrameConfirmButton.addActionListener(editSuperResource);
						SwingUtils.setFixedSize(dialogFrameConfirmButton,90,23);
					   	buttonPanel.add(dialogFrameConfirmButton);
					   	
					   	dialogFrameCancelButton = new JButton(Messages.getString
					   			("QuanlAna.ReachabilityGraph.Settings.Button.Cancel"));
					   	SwingUtils.setFixedSize(dialogFrameCancelButton,90,23);
					   	dialogFrameCancelButton.addActionListener(editSuperResource);
					   	buttonPanel.add(dialogFrameCancelButton);
					   	contentPane.add(buttonPanel,BorderLayout.SOUTH);
					   
					   dialogFrame.setContentPane(c);
					} catch (Exception exc) {
						exc.printStackTrace();
					}	   		
	   		 }
		   
	   	   }

	   // ActionListener to check correctness of user input   
	   private class editSuperResourceListener implements ActionListener{
		@SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent e){
			   if(e.getSource()==dialogFrameCancelButton){
					dialogFrame.dispose();
				}else{
					if(dialogFrame.getTitle().equals(Messages.getString("PetriNet.Resources.Resource.EditSuperRole"))){
						Object [] selectedRoles = selectedRolesList.getSelectedValues();
						boolean checkedAndOk = true;
						if(selectedRoles.length<2){
							JOptionPane.showMessageDialog(dialogFrame , Messages.getString
								("ResourceEditor.Error.NoRolesChoosen.Text"), 
								Messages.getString("ResourceEditor.Error.NoRolesChoosen.Title"),
		                        JOptionPane.ERROR_MESSAGE);	
								checkedAndOk=false;
						}
						else if(dialogFrameTextField.getText().equals("")){
				            JOptionPane.showMessageDialog(dialogFrame, 
				            		Messages.getString("ResourceEditor.Error.EmptyResourceClass.Text"), 
				            		Messages.getString("ResourceEditor.Error.EmptyResourceClass.Title"),
				                    JOptionPane.ERROR_MESSAGE);	
				           
				            		checkedAndOk=false;
				        }
				        if(selectedRoles.length>=2
				        		&&!dialogFrameTextField.getText().equalsIgnoreCase(superRolesTree.getLastSelectedPathComponent().toString())){
				        	for (Iterator<?> iter = getPetrinet().getOrganizationUnits().iterator(); iter.hasNext();){
				        		if (iter.next().toString().equals(dialogFrameTextField.getText())){
				    	            JOptionPane.showMessageDialog(dialogFrame, 
				    	            		Messages.getString("ResourceEditor.Error.DuplicateResourceClass.Text"), 
				    	            		Messages.getString("ResourceEditor.Error.DuplicateResourceClass.Title"),
				    	                    JOptionPane.ERROR_MESSAGE);
				    	           
				    	            		checkedAndOk=false;
				        		} 
				               
				        	}
				        }
				        if(selectedRoles.length>=2
				        		&&!dialogFrameTextField.getText().equalsIgnoreCase(superRolesTree.getLastSelectedPathComponent().toString())){
				        	for (Iterator<?> iter = getPetrinet().getRoles().iterator();iter.hasNext();){
				        		if (iter.next().toString().equals(dialogFrameTextField.getText())){ 
					            JOptionPane.showMessageDialog(dialogFrame, 
					            		Messages.getString("ResourceEditor.Error.DuplicateResourceClass.Text"), 
					            		Messages.getString("ResourceEditor.Error.DuplicateResourceClass.Title"),
					                    JOptionPane.ERROR_MESSAGE);
					           
					            		checkedAndOk=false;
				        		}
				        	}
						}
						if(checkedAndOk){
							editSuperResource();
						}
					}
					
					
					if(dialogFrame.getTitle().equals(Messages.getString("PetriNet.Resources.Resource.EditSuperGroup"))){
						Object [] selectedGroups = selectedGroupsList.getSelectedValues();
						boolean checkedAndOk = true;
						if(selectedGroups.length<2){
							JOptionPane.showMessageDialog(dialogFrame , Messages.getString
								("ResourceEditor.Error.NoGroupsChoosen.Text"), 
								Messages.getString("ResourceEditor.Error.NoGroupsChoosen.Title"),
		                        JOptionPane.ERROR_MESSAGE);	
								checkedAndOk=false;
						}
						else if(dialogFrameTextField.getText().equals("")){
				            JOptionPane.showMessageDialog(dialogFrame, 
				            		Messages.getString("ResourceEditor.Error.EmptyResourceClass.Text"), 
				            		Messages.getString("ResourceEditor.Error.EmptyResourceClass.Title"),
				                    JOptionPane.ERROR_MESSAGE);	
				           
				            		checkedAndOk=false;
				        }
				        if(selectedGroups.length>=2
				        		&&!dialogFrameTextField.getText().equalsIgnoreCase(superGroupsTree.getLastSelectedPathComponent().toString())){
				        	for (Iterator<?> iter = getPetrinet().getOrganizationUnits().iterator(); iter.hasNext();){
				        		if (iter.next().toString().equals(dialogFrameTextField.getText())){
				    	            JOptionPane.showMessageDialog(dialogFrame, 
				    	            		Messages.getString("ResourceEditor.Error.DuplicateResourceClass.Text"), 
				    	            		Messages.getString("ResourceEditor.Error.DuplicateResourceClass.Title"),
				    	                    JOptionPane.ERROR_MESSAGE);
				    	           
				    	            		checkedAndOk=false;
				        		} 
				               
				        	}
				        }
				        if(selectedGroups.length>=2
				        		&&!dialogFrameTextField.getText().equalsIgnoreCase(superGroupsTree.getLastSelectedPathComponent().toString())){
				        	for (Iterator<?> iter = getPetrinet().getRoles().iterator();iter.hasNext();){
				        		if (iter.next().toString().equals(dialogFrameTextField.getText())){ 
					            JOptionPane.showMessageDialog(dialogFrame, 
					            		Messages.getString("ResourceEditor.Error.DuplicateResourceClass.Text"), 
					            		Messages.getString("ResourceEditor.Error.DuplicateResourceClass.Title"),
					                    JOptionPane.ERROR_MESSAGE);
					           
					            		checkedAndOk=false;
				        		}
				        	}
						}
						if(checkedAndOk){
							editSuperResource();
						}
					}
					
				}
		   }
	       
	   }      

	   // Method to edit a compound resource
	   @SuppressWarnings("deprecation")
	private void editSuperResource (){
		   		// edit compound Role
		   		if(selectedGroupsList==null){				
					Object [] selectedRoles = selectedRolesList.getSelectedValues();
								String superRoleNewName = dialogFrameTextField.getText();
								ResourceClassModel newSuperRole = new ResourceClassModel(superRoleNewName, ResourceClassModel.TYPE_ROLE);
								String superRoleOldName = superRolesTree.getLastSelectedPathComponent().toString();
								SuperRolesTreeNode superRole = (SuperRolesTreeNode) superRolesTree.getLastSelectedPathComponent();
								 for(int i=0;i<superRole.getChildCount();i++){
									 String Role = superRole.getChildAt(i).toString();
									 int j = getPetrinet().containsRole(Role);
									 ResourceClassModel RoleModel = (ResourceClassModel) getPetrinet().getRoles().get(j);
									 ResourceClassModel superRoleModel= new ResourceClassModel(superRoleOldName,
											 ResourceClassModel.TYPE_ROLE);
									 RoleModel.removeSuperModel(superRoleModel);
								  }
								
								int j = getPetrinet().containsRole(superRoleOldName);
								ResourceClassModel RoleModel = (ResourceClassModel) getPetrinet().getRoles().get(j);
				                RoleModel.setName(superRoleNewName);
				                
				                updateRolesInPetrinet(superRoleOldName, superRoleNewName);
				                
				                int path = superRolesTopNode.getIndex(superRole);
							
								for(int i=0;i<selectedRoles.length;i++){
									String RoleName = selectedRoles[i].toString();
									int a = getPetrinet().containsRole(RoleName);
									ResourceClassModel currentRole = getPetrinet().getRoles().get(a);
									currentRole.addSuperModel(newSuperRole);
									ArrayList <String> objects = getObjectsAssignedToResource(currentRole, ResourceClassModel.TYPE_ROLE);
									for(int b = 0; b< objects.size();b++ ){
										String currentObject = objects.get(b);
									
										petrinet.addResourceMapping(newSuperRole.toString(), currentObject);
									}
								}
						    	refreshRolesFromModel();
						    	refreshGroupsFromModel();
						    	refreshObjectsFromModel();
						    	refreshGUI();
								superRolesTree.expandRow(path);
								getEditor().setSaved(false);
							
								superRolesEditButton.setEnabled(false);
								superRolesDeleteButton.setEnabled(false);		
					}
		   		
		   			// edit compound Group
					if(selectedRolesList==null){
						Object [] selectedGroups = selectedGroupsList.getSelectedValues();
								String superGroupNewName = dialogFrameTextField.getText();
								ResourceClassModel newSuperGroup = new ResourceClassModel(superGroupNewName, 
										ResourceClassModel.TYPE_ORGUNIT);
								String superGroupOldName = superGroupsTree.getLastSelectedPathComponent().toString();
								SuperGroupsTreeNode superGroup = (SuperGroupsTreeNode) superGroupsTree.getLastSelectedPathComponent();
								 for(int i=0;i<superGroup.getChildCount();i++){
									 String Group = superGroup.getChildAt(i).toString();
									 int j = getPetrinet().containsOrgunit(Group);
									 ResourceClassModel GroupModel = (ResourceClassModel) getPetrinet().getOrganizationUnits().get(j);
									 ResourceClassModel superGroupModel= new ResourceClassModel(superGroupOldName,
											 ResourceClassModel.TYPE_ORGUNIT);
									 GroupModel.removeSuperModel(superGroupModel);
								  }
								
								int j = getPetrinet().containsOrgunit(superGroupOldName);
								ResourceClassModel GroupModel = (ResourceClassModel) getPetrinet().getOrganizationUnits().get(j);
				                GroupModel.setName(superGroupNewName);
				                
				                updateRolesInPetrinet(superGroupOldName, superGroupNewName);	
							
				                int path = superGroupsTopNode.getIndex(superGroup);
				                
				                for(int i=0;i<selectedGroups.length;i++){
									String GroupName = selectedGroups[i].toString();
									int a = getPetrinet().containsOrgunit(GroupName);
									ResourceClassModel currentGroup = getPetrinet().getOrganizationUnits().get(a);
									currentGroup.addSuperModel(newSuperGroup);
									ArrayList <String> objects = getObjectsAssignedToResource(currentGroup, 
											ResourceClassModel.TYPE_ORGUNIT);
									for(int b = 0; b< objects.size();b++ ){
										String currentObject = objects.get(b);
										
										petrinet.addResourceMapping(newSuperGroup.toString(), currentObject);
									}
								}	
						    	refreshRolesFromModel();
						    	refreshGroupsFromModel();
						    	refreshObjectsFromModel();
						    	refreshGUI();
								superGroupsTree.expandRow(path);
								getEditor().setSaved(false);
								
								superGroupsEditButton.setEnabled(false);
								superGroupsDeleteButton.setEnabled(false);
							}												
				dialogFrame.dispose();
				selectedGroupsList = null;
				selectedRolesList = null;		   
			}

	   // ActionListener to delete compound resources
	   private class removeSuperResource implements ActionListener{

			public void actionPerformed(ActionEvent e) {
				// delete compound Role
				if(e.getSource()==superRolesDeleteButton){
					try{
						  String superRole2remove = (superRolesTree.getLastSelectedPathComponent().toString());
						  if(!RoleIsUsed(superRole2remove)){
							  SuperRolesTreeNode superRole= (SuperRolesTreeNode) superRolesTree.getLastSelectedPathComponent();
							  for(int i=0;i<superRole.getChildCount();i++){
								 String Role = superRole.getChildAt(i).toString();
								 int j = getPetrinet().containsRole(Role);
								 ResourceClassModel RoleModel = (ResourceClassModel) getPetrinet().getRoles().get(j);
								 ResourceClassModel superRoleModel= 
									 new ResourceClassModel(superRole2remove,ResourceClassModel.TYPE_ROLE);
								 RoleModel.removeSuperModel(superRoleModel);
								 
							  }
						  
						  	 int a = getPetrinet().containsRole(superRole2remove);
							 getPetrinet().getRoles().remove(a);
							 getPetrinet().getResourceMapping().remove(superRole2remove);
							 
						    	refreshRolesFromModel();
						    	refreshGroupsFromModel();
						    	refreshObjectsFromModel();
						    	refreshGUI();
							 
							 getEditor().setSaved(false);
				
							 superRolesDeleteButton.setEnabled(false);
							 superRolesEditButton.setEnabled(false);
						  }else{
						         JOptionPane.showMessageDialog(RolesContentPanel, 
						        		 Messages.getString("ResourceEditor.Error.UsedResourceClass.Text"), 
						        		 Messages.getString("ResourceEditor.Error.UsedResourceClass.Title"),
						                        JOptionPane.ERROR_MESSAGE);
						       }
					}catch (Exception exc){
						exc.printStackTrace();
					}
				}
				// delete compound Group
				if(e.getSource()==superGroupsDeleteButton){
					try{
						  String superGroup2remove = (superGroupsTree.getLastSelectedPathComponent().toString());
						  if(!GroupIsUsed(superGroup2remove)){
							  SuperGroupsTreeNode superGroup= (SuperGroupsTreeNode) superGroupsTree.getLastSelectedPathComponent();
							  for(int i=0;i<superGroup.getChildCount();i++){
								 String Group = superGroup.getChildAt(i).toString();
								 int j = getPetrinet().containsOrgunit(Group);
								 ResourceClassModel GroupModel = (ResourceClassModel) getPetrinet().getOrganizationUnits().get(j);
								 ResourceClassModel superGroupModel= new ResourceClassModel(superGroup2remove,ResourceClassModel.TYPE_ORGUNIT);
								 GroupModel.removeSuperModel(superGroupModel);
							  }
						  
						  	 int a = getPetrinet().containsOrgunit(superGroup2remove);
							 getPetrinet().getOrganizationUnits().remove(a);
							 getPetrinet().getResourceMapping().remove(superGroup2remove);
						    	refreshRolesFromModel();
						    	refreshGroupsFromModel();
						    	refreshObjectsFromModel();
						    	refreshGUI();
							 
							 getEditor().setSaved(false);
							 
							 superGroupsDeleteButton.setEnabled(false);
							 superGroupsEditButton.setEnabled(false);
					  }else{
					         JOptionPane.showMessageDialog(RolesContentPanel, 
					        		 Messages.getString("ResourceEditor.Error.UsedResourceClass.Text"), 
					        		 Messages.getString("ResourceEditor.Error.UsedResourceClass.Title"),
					                        JOptionPane.ERROR_MESSAGE);
					       }
					}catch (Exception exc){
						exc.printStackTrace();
					}
				}
			}
		}

	   // Allows toggle-selection for JListModel
       class ToggleSelectionModel extends DefaultListSelectionModel
       {
          
           boolean gestureStarted = false;
          
           public void setSelectionInterval(int index0, int index1) {
               if(getSelectionMode()==SINGLE_SELECTION)
               {
                   super.setSelectionInterval(index0, index1);
               } else
               {
                   if (isSelectedIndex(index0) && !gestureStarted) {
                       super.removeSelectionInterval(index0, index1);
                   }
                   else {
                       super.addSelectionInterval(index0, index1);
                   }
                   gestureStarted = true;
               }
           }

           public void setValueIsAdjusting(boolean isAdjusting) {
               if (isAdjusting == false) {
                   gestureStarted = false;
               }
           }
       }
       
       		   

//*****************************TREE-NODE**********************
	   //****************************************************
	   class ObjectsTreeNode extends DefaultMutableTreeNode {
		   	public ObjectsTreeNode(){super();}
			public ObjectsTreeNode(String name){super(name);}
	   }
	   
	   class RolesTreeNode extends DefaultMutableTreeNode {
		   public RolesTreeNode(){
			   super();
			   }
		   public RolesTreeNode(String name){super(name);}
	   }
	   class SuperRolesTreeNode extends DefaultMutableTreeNode {
		   public SuperRolesTreeNode(){super();}
		   public SuperRolesTreeNode(String name){super(name);}
	   }
	   
	   class GroupsTreeNode extends DefaultMutableTreeNode {
		   public GroupsTreeNode(){super();}
		   public GroupsTreeNode(String name){super(name);}
	   }
	   class SuperGroupsTreeNode extends DefaultMutableTreeNode {
		   public SuperGroupsTreeNode(){super();}
		   public SuperGroupsTreeNode(String name){super(name);}
	   }
	   	   

	   public void addGroupsNode(DefaultTreeModel model, DefaultMutableTreeNode parent, String name) {
		    GroupsTreeNode childNode = new GroupsTreeNode(name);
		    model.insertNodeInto(childNode, parent, parent.getChildCount());
	   }
	   public void addRolesNode(DefaultTreeModel model, DefaultMutableTreeNode parent, String name) {
		    RolesTreeNode childNode = new RolesTreeNode(name);
		    model.insertNodeInto(childNode, parent, parent.getChildCount());
	   }
	   public void addObjectsNode(DefaultTreeModel model, DefaultMutableTreeNode parent, String name) {
		    ObjectsTreeNode childNode = new ObjectsTreeNode(name);
		    model.insertNodeInto(childNode, parent, parent.getChildCount());
		    objectsDeleteButton.setEnabled(true);
		    objectsEditButton.setEnabled(true);
	   }
	   
	   
	   // TreeSelectionListener that controls the condition of the related buttons
	   class MyTreeSelectionListener implements TreeSelectionListener{
		   public void valueChanged(TreeSelectionEvent e) {	
			   try{
			   if (e.getSource()==RolesTree){
				   DefaultMutableTreeNode node = ( DefaultMutableTreeNode)RolesTree.getLastSelectedPathComponent();
				   if(RolesTree.isSelectionEmpty()){
						  RolesDeleteButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Delete"));
						  RolesDeleteButton.setToolTipText(Messages.getString("PetriNet.Resources.Delete.Title"));
						  RolesEditButton.setEnabled(false);
						  RolesDeleteButton.setEnabled(false);
				   }
				   else if(node.isLeaf()&& !node.getParent().toString().equals(RolesTopNode.toString())){
					   RolesDeleteButton.setToolTipText(Messages.getString("PetriNet.Resources.Unassign.Title")); 
					   RolesDeleteButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Unassign"));
					   RolesEditButton.setEnabled(false);
					   RolesDeleteButton.setEnabled(true);
					   
				   }else{
					  RolesDeleteButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Delete"));
					  RolesDeleteButton.setToolTipText(Messages.getString("PetriNet.Resources.Delete.Title"));
					  RolesEditButton.setEnabled(true);
					  RolesDeleteButton.setEnabled(true);
				   }
			   }
			   if (e.getSource()==GroupsTree){
				   MutableTreeNode node = ( MutableTreeNode)GroupsTree.getLastSelectedPathComponent();
				   if(GroupsTree.isSelectionEmpty()){
			        	GroupsDeleteButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Delete"));
			        	GroupsDeleteButton.setToolTipText(Messages.getString("PetriNet.Resources.Delete.Title"));
			        	GroupsDeleteButton.setEnabled(false);
			        	GroupsEditButton.setEnabled(false);
				   }

				   else if(node.isLeaf()&& !node.getParent().toString().equals(GroupsTopNode.toString())){
					   GroupsDeleteButton.setToolTipText(Messages.getString("PetriNet.Resources.Unassign.Title")); 
					   GroupsDeleteButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Unassign"));
					   GroupsEditButton.setEnabled(false);
					   GroupsDeleteButton.setEnabled(true);
				   }else{
			        	GroupsDeleteButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Delete"));
			        	GroupsDeleteButton.setToolTipText(Messages.getString("PetriNet.Resources.Delete.Title"));
			        	GroupsDeleteButton.setEnabled(true);
			        	GroupsEditButton.setEnabled(true);
				   }
				 } 
			   if(e.getSource()==superRolesTree){
				   MutableTreeNode node = ( MutableTreeNode)superRolesTree.getLastSelectedPathComponent();
				  if(superRolesTree.isSelectionEmpty()){
					   superRolesDeleteButton.setEnabled(false);
					   superRolesEditButton.setEnabled(false);  
				  }				   
				  else if(node.getParent().toString().equals(superRolesTopNode.toString())){
					   superRolesDeleteButton.setEnabled(true);
					   superRolesEditButton.setEnabled(true);
				  	}else{
					   superRolesDeleteButton.setEnabled(false);
					   superRolesEditButton.setEnabled(false);
				   }
			   }
			   if(e.getSource()==superGroupsTree){
				   MutableTreeNode node = ( MutableTreeNode)superGroupsTree.getLastSelectedPathComponent();
					if(superGroupsTree.isSelectionEmpty()){
						   superGroupsDeleteButton.setEnabled(false);
						   superGroupsEditButton.setEnabled(false);
					}				   
					else if(node.getParent().toString().equals(superGroupsTopNode.toString())){
					   superGroupsDeleteButton.setEnabled(true);
					   superGroupsEditButton.setEnabled(true);
					}else{
					   superGroupsDeleteButton.setEnabled(false);
					   superGroupsEditButton.setEnabled(false);
				   }
			   }
			  
			   if (e.getSource()== objectsTree){
					 MutableTreeNode node = ( MutableTreeNode)objectsTree.getLastSelectedPathComponent();
					if(objectsTree.isSelectionEmpty()){
					 	objectsEditButton.setEnabled(false);
					 	objectsDeleteButton.setEnabled(false);
					}
					 
					else if(node.isLeaf()&& !node.getParent().toString().equals(objectsTopNode.toString())){
						   objectsEditButton.setEnabled(true);
						   objectsDeleteButton.setEnabled(true);

					 }else{
						 	objectsEditButton.setEnabled(false);
						 	objectsDeleteButton.setEnabled(false);
					 }
			   }
		   }catch (Exception exc){
			   exc.printStackTrace();
		   }
		}		   
	   }
	   // TreeExpansionListener to control the condition of the expand and collapse buttons
	   private class MyTreeExpansionListener implements TreeExpansionListener{

		public void treeCollapsed(TreeExpansionEvent e) {
			if(e.getSource()==RolesTree){
				if(RolesTree.getRowCount()==RolesTopNode.getChildCount()){
					RolesCollapseButton.setEnabled(false);				
				}
				if(RolesTopNode.getChildCount()>1){
					RolesExpandButton.setEnabled(true);
				}
			}
			if(e.getSource()==superRolesTree){
				if(superRolesTree.getRowCount()==superRolesTopNode.getChildCount()){
					superRolesCollapseButton.setEnabled(false);
				}
				superRolesExpandButton.setEnabled(true);
			}
			if(e.getSource()==GroupsTree){
				if(GroupsTree.getRowCount()==GroupsTopNode.getChildCount()){
					GroupsCollapseButton.setEnabled(false);
				}
				GroupsExpandButton.setEnabled(true);
			}
			if(e.getSource()==superGroupsTree){
				if(superGroupsTree.getRowCount()==superGroupsTopNode.getChildCount()){
					superGroupsCollapseButton.setEnabled(false);
				}
				superGroupsExpandButton.setEnabled(true);
			}
			if(e.getSource()==objectsTree){
				if(objectsTree.getRowCount()==objectsTopNode.getChildCount()){
					objectsCollapseButton.setEnabled(false);
				}
				objectsExpandButton.setEnabled(true);
			}
			
		}

		public void treeExpanded(TreeExpansionEvent e) {
			if(e.getSource()==RolesTree){
				if(RolesTree.getRowCount()>RolesTopNode.getChildCount()){
					RolesCollapseButton.setEnabled(true);		
				}
				int ComponentCount = RolesTopNode.getChildCount();
				for(int i = 0; i<RolesTopNode.getChildCount();i++){
					ComponentCount+= RolesTopNode.getChildAt(i).getChildCount();
				}
				if(RolesTree.getRowCount()==ComponentCount)
					RolesExpandButton.setEnabled(false);
			}
			if(e.getSource()==superRolesTree){
				if(superRolesTree.getRowCount()>superRolesTopNode.getChildCount()){
					superRolesCollapseButton.setEnabled(true);
				}
				int ComponentCount = superRolesTopNode.getChildCount();
				for(int i = 0; i<superRolesTopNode.getChildCount();i++){
					ComponentCount+= superRolesTopNode.getChildAt(i).getChildCount();
				}
				if(superRolesTree.getRowCount()==ComponentCount)
					superRolesExpandButton.setEnabled(false);
			}
			
			if(e.getSource()==GroupsTree){
				if(GroupsTree.getRowCount()>GroupsTopNode.getChildCount()){
					GroupsCollapseButton.setEnabled(true);
				}
				int ComponentCount = GroupsTopNode.getChildCount();
				for(int i = 0; i<GroupsTopNode.getChildCount();i++){
					ComponentCount+= GroupsTopNode.getChildAt(i).getChildCount();
				}
				if(GroupsTree.getRowCount()==ComponentCount)
					GroupsExpandButton.setEnabled(false);
			}
			
			if(e.getSource()==superGroupsTree){
				if(superGroupsTree.getRowCount()>superGroupsTopNode.getChildCount()){
					superGroupsCollapseButton.setEnabled(true);
				}
				int ComponentCount = superGroupsTopNode.getChildCount();
				for(int i = 0; i<superGroupsTopNode.getChildCount();i++){
					ComponentCount+= superGroupsTopNode.getChildAt(i).getChildCount();
				}
				if(superGroupsTree.getRowCount()==ComponentCount)
					superGroupsExpandButton.setEnabled(false);
			}
			if(e.getSource()==objectsTree){
				if(objectsTree.getRowCount()>objectsTopNode.getChildCount()){
					objectsCollapseButton.setEnabled(true);
				}
				int ComponentCount = objectsTopNode.getChildCount();
				for(int i = 0; i<objectsTopNode.getChildCount();i++){
					ComponentCount+= objectsTopNode.getChildAt(i).getChildCount();
				}
				if(objectsTree.getRowCount()==ComponentCount)
					objectsExpandButton.setEnabled(false);
			}
			
		}
		   
	   }
   
	   
	   //********************Expand_Button_Action_Listener **********************************
	   private class expandButtonListener implements ActionListener{
			
		   public void actionPerformed(ActionEvent e) {
			   
			   if(e.getSource()== objectsExpandButton){
				   expandAll(objectsTree);
			   }
			   if(e.getSource()== RolesExpandButton){
				   expandAll(RolesTree);
			   }
			   if(e.getSource()== superRolesExpandButton){
				   expandAll(superRolesTree);
			   }
			   if(e.getSource()== GroupsExpandButton){
				   expandAll(GroupsTree);
			   }
			   if(e.getSource()== superGroupsExpandButton){
				   expandAll(superGroupsTree);
			   }
			 
			   
			   
		   }
	   }
	   
	   
	   //***************************EXPAND -ALL****************************************
	   //expand all nodes of a tree
	   public void expandAll(JTree tree) {
		    int row = 0;
		    while (row < tree.getRowCount()) {
		      tree.expandRow(row);
		      row++;
		      }
		    }
	   //********************Collapse_Button_Action_Listener **********************************
	   private class collapseButtonListener implements ActionListener{
			
		   public void actionPerformed(ActionEvent e) {
			   
			   if(e.getSource()== objectsCollapseButton){
				   collapseAll(objectsTree);
				   objectsCollapseButton.setEnabled(false);
			   }
			   if(e.getSource()== RolesCollapseButton){
				   collapseAll(RolesTree);
				   RolesCollapseButton.setEnabled(false);
			   }
			   if(e.getSource()== superRolesCollapseButton){
				   collapseAll(superRolesTree);
				   superRolesCollapseButton.setEnabled(false);
			   }
			   if(e.getSource()== GroupsCollapseButton){
				   collapseAll(GroupsTree);
				   GroupsCollapseButton.setEnabled(false);
			   }
			   if(e.getSource()== superGroupsCollapseButton){
				   collapseAll(superGroupsTree);
				   superGroupsCollapseButton.setEnabled(false);
			   }
			   
		   }
	   }

	   //****************************Collapse -All**************************************
	   //collapse all nodes of a tree
	   public void collapseAll(JTree tree) {
		    int row = tree.getRowCount() - 1;
		    while (row >= 0) {
		      tree.collapseRow(row);
		      row--;
		      }
		    }

	   
	   


	// Method that returns a list of objects that are assigned to a resource
		private ArrayList<String> getObjectsAssignedToResource(ResourceClassModel resource, int type){
	    	ArrayList<String> objects = new ArrayList<String>();
	    	if(type == ResourceClassModel.TYPE_ROLE||type==ResourceClassModel.TYPE_ORGUNIT){
	    	
	    		for(int i=0;i<objectsAssignedListModel.size();i++){
	    			String currentObject = objectsAssignedListModel.get(i).toString();
	    			
	    			Vector<?> assignedClasses = getPetrinet().getResourceClassesResourceIsAssignedTo(currentObject);
					Object ass;
	    			for (Iterator<?> iter = assignedClasses.iterator(); iter.hasNext();){
	    				ass = iter.next();
	    				if(ass.toString().equals(resource.toString())){
	    					objects.add(currentObject);
	    				}
				  		
					}
	    		}
	    		return objects;	
	    	}

	    	return objects;
	    }
	    
	    //Check if input String str is correct	   
		private boolean checkClassSyntax(String str){
	        boolean nameExists = false;
	    
	        if (str.equals("")){
	            JOptionPane.showMessageDialog(RolesContentPanel, 
	            		Messages.getString("ResourceEditor.Error.EmptyResourceClass.Text"), 
	            		Messages.getString("ResourceEditor.Error.EmptyResourceClass.Title"),
	                    JOptionPane.ERROR_MESSAGE);
	            return false;
	        }

	        for (Iterator<?> iter = getPetrinet().getOrganizationUnits().iterator(); !nameExists & iter.hasNext();){
	            if (iter.next().toString().equals(str)) 
	                nameExists = true;
	        }
	        for (Iterator<?> iter = getPetrinet().getRoles().iterator(); !nameExists & iter.hasNext();){
	            if (iter.next().toString().equals(str)) 
	                nameExists = true;
	        }

	        if (nameExists){
	            JOptionPane.showMessageDialog(RolesContentPanel, 
	            		Messages.getString("ResourceEditor.Error.DuplicateResourceClass.Text"), 
	            		Messages.getString("ResourceEditor.Error.DuplicateResourceClass.Title"),
	                    JOptionPane.ERROR_MESSAGE);
	            return false;
	        } 	       

	        return true;
	    }
	    
	    // Check if Group is used by any transition
		private boolean GroupIsUsed(String GroupName)
	    {
	        boolean isUsed = false;
	               
	        HashMap<String, AbstractPetriNetElementModel> alltrans = new HashMap<String, AbstractPetriNetElementModel>();
	        alltrans.putAll(getPetrinet().getElementContainer().getElementsByType(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE));
	        alltrans.putAll(getPetrinet().getElementContainer().getElementsByType(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE));

	        for (Iterator<AbstractPetriNetElementModel> transIter = alltrans.values().iterator(); transIter.hasNext() & !isUsed;)
	        {
	            TransitionModel transition = (TransitionModel)(transIter.next());
	            if (transition.getToolSpecific() != null &&
	                    transition.getToolSpecific().getTransResource() != null &&
	                    transition.getToolSpecific().getTransResource().getTransOrgUnitName() != null &&
	                    transition.getToolSpecific().getTransResource().getTransOrgUnitName().equals(GroupName))
	            {
	                isUsed = true;
	            }
	         }
	        
	        return isUsed;
	    }

	    // Check if Role is used by any transition
		private boolean RoleIsUsed(String RoleName)
	    {
	        boolean isUsed = false;
	        HashMap<String, AbstractPetriNetElementModel> alltrans = new HashMap<String, AbstractPetriNetElementModel>();
	        alltrans.putAll(getPetrinet().getElementContainer().getElementsByType(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE));
	        alltrans.putAll(getPetrinet().getElementContainer().getElementsByType(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE));
	        
	        for (Iterator<AbstractPetriNetElementModel> transIter = alltrans.values().iterator(); transIter.hasNext() & !isUsed;)
	        {
	            TransitionModel transition = (TransitionModel)(transIter.next());
	            if (transition.getToolSpecific() != null &&
	                transition.getToolSpecific().getTransResource() != null &&
	                transition.getToolSpecific().getTransResource().getTransRoleName() != null &&
	                transition.getToolSpecific().getTransResource().getTransRoleName().equals(RoleName))
	            {
	                isUsed = true;
	            }
	         }
	        
	        return isUsed;
	    }

	    // Update a changed Role in petrinet
		private void updateRolesInPetrinet(String oldName, String newName)
	    {
	        HashMap<String, AbstractPetriNetElementModel> alltrans = new HashMap<String, AbstractPetriNetElementModel>();
	        alltrans.putAll(getPetrinet().getElementContainer().getElementsByType(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE));
	        alltrans.putAll(getPetrinet().getElementContainer().getElementsByType(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE));

	        for (Iterator<AbstractPetriNetElementModel> transIter = alltrans.values().iterator(); transIter.hasNext();)
	        {
	            TransitionModel transition = (TransitionModel)(transIter.next());
	            if (transition.getToolSpecific() != null &&
	                    transition.getToolSpecific().getTransResource() != null &&
	                    transition.getToolSpecific().getTransResource().getTransRoleName() != null &&
	                    transition.getToolSpecific().getTransResource().getTransRoleName().equals(oldName))
	            {
	                transition.getToolSpecific().getTransResource().setTransRoleName(newName);
	            }
	        }    
	        
	        getPetrinet().replaceResourceClassMapping(oldName, newName);
	    }

	    // Update a changed Group in petrinet	    
		private void updateGroupsInPetrinet(String oldName, String newName)
	    {
	        HashMap<String, AbstractPetriNetElementModel> alltrans = new HashMap<String, AbstractPetriNetElementModel>();
	        alltrans.putAll(getPetrinet().getElementContainer().getElementsByType(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE));
	        alltrans.putAll(getPetrinet().getElementContainer().getElementsByType(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE));

	        for (Iterator<AbstractPetriNetElementModel> transIter = alltrans.values().iterator(); transIter.hasNext();)
	        {
	            TransitionModel transition = (TransitionModel)(transIter.next());
	            if (transition.getToolSpecific() != null &&
	                    transition.getToolSpecific().getTransResource() != null &&
	                    transition.getToolSpecific().getTransResource().getTransOrgUnitName() != null &&
	                    transition.getToolSpecific().getTransResource().getTransOrgUnitName().equals(oldName))
	            {
	                transition.getToolSpecific().getTransResource().setTransOrgUnitName(newName);
	            }
	        }   
	        
	        getPetrinet().replaceResourceClassMapping(oldName, newName);
	    }
	    

	    // Refresh all used ListModels from the petrinet model  
	    private void refreshFromModel()
	    {
	    	refreshRolesFromModel();
	    	refreshGroupsFromModel();
	    	refreshObjectsFromModel();
	    }

	    // Refresh GUI after changes 	    
	    private void refreshGUI() {
			   	objectsEditButton.setEnabled(false);
			   	objectsDeleteButton.setEnabled(false);
			   	objectsCollapseButton.setEnabled(false);
				RolesEditButton.setEnabled(false);
				RolesDeleteButton.setEnabled(false);
				RolesCollapseButton.setEnabled(false);
				GroupsEditButton.setEnabled(false);
				GroupsDeleteButton.setEnabled(false);
				GroupsCollapseButton.setEnabled(false);
				superRolesEditButton.setEnabled(false);
				superRolesDeleteButton.setEnabled(false);
				superRolesCollapseButton.setEnabled(false);
				superGroupsEditButton.setEnabled(false);
				superGroupsDeleteButton.setEnabled(false);
				superGroupsCollapseButton.setEnabled(false);
				
				if(RolesTopNode.getChildCount()>0){
					RolesExpandButton.setEnabled(true);
				}else
					RolesExpandButton.setEnabled(false);
				if(GroupsTopNode.getChildCount()>0){
					GroupsExpandButton.setEnabled(true);
				}else
					GroupsExpandButton.setEnabled(false);
				if(superRolesListModel.size()>0){
					superRolesExpandButton.setEnabled(true);
				}else
					superRolesExpandButton.setEnabled(false);
				if(superGroupsListModel.size()>0){
					superGroupsExpandButton.setEnabled(true);
				}else
					superGroupsExpandButton.setEnabled(false);
				if(objectsAssignedNode.getChildCount()>0||objectsUnassignedNode.getChildCount()>0){
					objectsExpandButton.setEnabled(true);
				}else
					objectsExpandButton.setEnabled(false);
			}
	    
	    //Refresh Roles and compound Roles from petrinet model	    
	    private void refreshRolesFromModel(){ 
	    	try{
	    		RolesListModel.removeAllElements();	 
	    		superRolesListModel.removeAllElements();
	    			for (int i = 0; i < getPetrinet().getRoles().size(); i++){
	    				RolesListModel.addElement((ResourceClassModel)getPetrinet().getRoles().get(i));	
	    			}
	    			for (int i = 0; i < getPetrinet().getRoles().size(); i++){
		    			if(getPetrinet().getRoles().get(i).getSuperModels()!=null){
		    				for(Iterator<ResourceClassModel> j = getPetrinet().getRoles().get(i).getSuperModels();j.hasNext();){
		    					ResourceClassModel superRole = j.next();
		    					if(!superRolesListModel.contains(superRole.toString())){
		    						superRolesListModel.addElement(superRole.toString());	
		    						for(int k=0;k<RolesListModel.size();k++){
		    							String Role = RolesListModel.get(k).toString();
		    							if(Role.equalsIgnoreCase(superRole.toString())){
		    								RolesListModel.remove(k);
		    							}
		    						}
		    					}
		    				}
		    			}
		    		}
//		    		refreshRolesTreeFromListModel();
//		    		refreshSuperRolesTreeFromListModel();
	    
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
    		refreshRolesTreeFromListModel();
    		refreshSuperRolesTreeFromListModel();
	    }
	    
	    //Refresh Groups and compound Groups from petrinet model		    
	    private void refreshGroupsFromModel(){ 
	    	try{
	    		GroupsListModel.removeAllElements();
	    		superGroupsListModel.removeAllElements();
	    		for (int i = 0; i < getPetrinet().getOrganizationUnits().size(); i++){
	    				GroupsListModel.addElement((ResourceClassModel)getPetrinet().getOrganizationUnits().get(i));	
	    		}
	    		for (int i = 0; i < getPetrinet().getOrganizationUnits().size(); i++){
	    			if(getPetrinet().getOrganizationUnits().get(i).getSuperModels()!=null){
	    				for(Iterator<ResourceClassModel> j = getPetrinet().getOrganizationUnits().get(i).getSuperModels();j.hasNext();){
	    					ResourceClassModel superGroup = j.next();
	    					if(!superGroupsListModel.contains(superGroup.toString())){
	    						superGroupsListModel.addElement(superGroup.toString());	
	    						for(int k=0;k<GroupsListModel.size();k++){
	    							String Group = GroupsListModel.get(k).toString();
	    							if(Group.equalsIgnoreCase(superGroup.toString())){
	    								GroupsListModel.remove(k);
	    							}
	    						}
	    					}
	    				}
	    			}
	    		}	    		
//	    		refreshGroupsTreeFromListModel(); 
//	    		refreshSuperGroupsTreeFromListModel();
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	    	refreshGroupsTreeFromListModel(); 
    		refreshSuperGroupsTreeFromListModel();
	    }

	    //Refresh objects from petrinet model		    
		private void refreshObjectsFromModel(){
	    	try{
	    		objectsAssignedListModel.removeAllElements();
	    		objectsUnassignedListModel.removeAllElements(); 
	    		for (int i = 0; i < getPetrinet().getResources().size(); i++){
	    			objectsUnassignedListModel.addElement((ResourceModel) getPetrinet().getResources().get(i));
	    	    }
	    		int a = objectsUnassignedListModel.getSize()-1;
	    		for (int i = 0; i< (a+1) ;i++){
	    			ResourceModel currentObject = (ResourceModel) objectsUnassignedListModel.getElementAt(a-i);
	    			Vector<?> assignedClasses = getPetrinet().getResourceClassesResourceIsAssignedTo(currentObject.toString());
	    			if (!assignedClasses.isEmpty() ){
	    				objectsUnassignedListModel.removeElement(currentObject);
	    				objectsAssignedListModel.addElement(currentObject);
	    				
	    				
	    			}

	    				
	    			Object ass;
	    			for (Iterator<?> iter = assignedClasses.iterator(); iter.hasNext();){
	    				ass = iter.next();
	    				String currentResource = ass.toString();
	    				for(int j =0; j <  RolesTreeModel.getChildCount(RolesTopNode);j++){
	    					String currentRole = RolesTreeModel.getChild(RolesTopNode, j).toString();
	    					if(currentResource.equals(currentRole)){
	    						RolesTreeNode currentNode = (RolesTreeNode) RolesTreeModel.getChild(RolesTopNode, j);
	    						RolesTreeModel.insertNodeInto(new ObjectsTreeNode(currentObject.toString()), currentNode, currentNode.getChildCount());
	    					}
	    				}
	    				for(int j =0; j <  GroupsTreeModel.getChildCount(GroupsTopNode);j++){
	    					String currentGroup = GroupsTreeModel.getChild(GroupsTopNode, j).toString();
	    					if(currentResource.equals(currentGroup)){
	    						GroupsTreeNode currentNode = (GroupsTreeNode) GroupsTreeModel.getChild(GroupsTopNode, j);
	    						DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) GroupsTree.getCellRenderer();
	    						renderer.setLeafIcon(Messages.getImageIcon("PetriNet.Resources.Delete"));
	    						GroupsTreeModel.insertNodeInto(new ObjectsTreeNode(currentObject.toString()), currentNode, currentNode.getChildCount());
	    					
	    					}
	    				}
	                 
	              }
	    		}

	    		
	    	}catch (Exception e){
	    		e.printStackTrace();
	    	}
	    	refreshObjectsTreeFromListModel();
	    }
	    
	    //Refresh the display of Roles in the related jtree		    
	    private void refreshRolesTreeFromListModel(){
	    	removeAllNodesFromTreeModel(RolesTreeModel,RolesTopNode);
	    	for (int i = 0; i < RolesListModel.getSize();i++){
    			RolesTreeModel.insertNodeInto(new RolesTreeNode(RolesListModel.get(i).toString()), RolesTopNode, i);
    		
    		}
	
	    	RolesTree.expandPath(new TreePath(RolesTreeModel.getRoot()));
    		RolesTree.updateUI();
	    }

	    //Refresh the display of compound Roles in the related jtree	
	    private void refreshSuperRolesTreeFromListModel(){
	    	try {
	    	removeAllNodesFromTreeModel(superRolesTreeModel,superRolesTopNode);
	    	}
	    	catch (Exception e) {}
	    	for (int i = 0; i < superRolesListModel.getSize();i++){
	    		String superRole = superRolesListModel.get(i).toString();
	    		SuperRolesTreeNode parent = new SuperRolesTreeNode(superRole);
    			superRolesTreeModel.insertNodeInto(parent, superRolesTopNode, i);
    			  			
    			for (int j = 0; j < getPetrinet().getRoles().size(); j++){
	    			if(getPetrinet().getRoles().get(j).getSuperModels()!=null){
	    				for(Iterator<ResourceClassModel> k = getPetrinet().getRoles().get(j).getSuperModels();k.hasNext();){
	    					ResourceClassModel currentSuperRole = k.next();
	    					if(currentSuperRole.toString().equalsIgnoreCase(superRole)){
	    						superRolesTreeModel.insertNodeInto(new SuperRolesTreeNode(getPetrinet().getRoles().get(j).toString()), parent, parent.getChildCount());				
	    					}
	    				}
	    			}
	    		}
    		}
	    	
	    	superRolesTree.expandPath(new TreePath(superRolesTreeModel.getRoot()));	    	
    		superRolesTree.updateUI();
	    }

	    //Refresh the display of compound Groups in the related jtree	
	    private void refreshSuperGroupsTreeFromListModel(){
	    	removeAllNodesFromTreeModel(superGroupsTreeModel,superGroupsTopNode);
	    	for (int i = 0; i < superGroupsListModel.getSize();i++){
	    		String superGroup = superGroupsListModel.get(i).toString();
	    		SuperGroupsTreeNode parent = new SuperGroupsTreeNode(superGroup);
    			superGroupsTreeModel.insertNodeInto(parent, superGroupsTopNode, i);
    			  			
    			for (int j = 0; j < getPetrinet().getOrganizationUnits().size(); j++){
	    			if(getPetrinet().getOrganizationUnits().get(j).getSuperModels()!=null){
	    				for(Iterator<ResourceClassModel> k = getPetrinet().getOrganizationUnits().get(j).getSuperModels();k.hasNext();){
	    					ResourceClassModel currentSuperGroup = k.next();
	    					if(currentSuperGroup.toString().equalsIgnoreCase(superGroup )){
	    						superGroupsTreeModel.insertNodeInto(new SuperGroupsTreeNode(getPetrinet().getOrganizationUnits().get(j).toString()), parent, parent.getChildCount());				
	    					}
	    				}
	    			}
	    		}
    		}
	    
	    	superGroupsTree.expandPath(new TreePath(superGroupsTreeModel.getRoot()));	    	
    		superGroupsTree.updateUI();
	    }

	    //Refresh the display of Groups in the related jtree	
	    private void refreshGroupsTreeFromListModel(){
	    	removeAllNodesFromTreeModel(GroupsTreeModel,GroupsTopNode);
	    	for (int i = 0; i < GroupsListModel.getSize();i++){
    			GroupsTreeModel.insertNodeInto(new GroupsTreeNode(GroupsListModel.get(i).toString()), GroupsTopNode, i);
    			

    		}
	    	
	    	GroupsTree.expandPath(new TreePath(GroupsTreeModel.getRoot()));	    	
    		GroupsTree.updateUI();
	    }
	    	    
	    //Refresh the display of objects in the related jtree	
	    private void refreshObjectsTreeFromListModel(){
	    	removeAllNodesFromTreeModel(objectsTreeModel,objectsAssignedNode);
	    	removeAllNodesFromTreeModel(objectsTreeModel,objectsUnassignedNode);
	    	for (int i = 0; i < objectsAssignedListModel.getSize();i++){
    			objectsTreeModel.insertNodeInto(new ObjectsTreeNode(objectsAssignedListModel.get(i).toString()), objectsAssignedNode, i);
    		}
	    	for (int i = 0; i < objectsUnassignedListModel.getSize();i++){
    			objectsTreeModel.insertNodeInto(new ObjectsTreeNode(objectsUnassignedListModel.get(i).toString()), objectsUnassignedNode, i);
    		}	    	
	    	objectsTree.expandPath(new TreePath(objectsTreeModel.getRoot()));	    	
    		objectsTree.updateUI();
	    }

	    //Method to remove all nodes from a passed treeModel
	    private void removeAllNodesFromTreeModel(DefaultTreeModel model,MutableTreeNode node){
	    	while(model.getChildCount(node)!=0)
	    		{
	    		try {
	    			MutableTreeNode node2remove = (MutableTreeNode) model.getChild(node, 0);
		    		model.removeNodeFromParent(node2remove);
	    		} catch (Exception e) {}
	    		}
	    }

	    // Renderer to control the used Icons in the objects-, Roles- and Groups tree
	    //++++++ResourceClassRenderer++++++++++
	    class treeRenderer extends DefaultTreeCellRenderer{
	    	public  java.awt.Component getTreeCellRendererComponent(JTree tree,Object value,boolean sel, boolean expanded,boolean leaf,int row, boolean hasFocus)
	    	{
	    		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
	    		DefaultMutableTreeNode currentTreeNode = (DefaultMutableTreeNode) value;

//	    		if (currentTreeNode.isLeaf()&&currentTreeNode.getParent()==GroupsTopNode) {
//	    			setLeafIcon(resourceClass);
//	    		} else 
//	    			if(currentTreeNode.isLeaf()&&currentTreeNode.getParent()!=GroupsTopNode){
//	    				setLeafIcon(object);
//	    			}	

	    		if	(currentTreeNode==objectsUnassignedNode){
	    			setIcon(unassignedIcon);
	    		}
	    		if (currentTreeNode.isLeaf()&&(currentTreeNode==objectsAssignedNode||currentTreeNode.getParent()==RolesTopNode||currentTreeNode.getParent()==GroupsTopNode)) {
	    			setIcon(resourceClass);
	    		} else {
	    			if(currentTreeNode.isLeaf()){
	    				setLeafIcon(object);
	    			}
	    		}

	    		setClosedIcon(resourceClass);
			    setOpenIcon(resourceClass);			    
			    
	    		return this;
	    	}
	    }

	    // Renderer to control the used Icons in compound Roles and compound Groups tree	    
	    //++++++ResourceSuperClassRenderer++++++++
	    class superTreeRenderer extends DefaultTreeCellRenderer{
	    	public java.awt.Component getTreeCellRendererComponent(JTree tree,Object value,boolean sel, boolean expanded,boolean leaf,int row, boolean hasFocus)
	    	{
	    		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
	    		DefaultMutableTreeNode currentTreeNode = (DefaultMutableTreeNode) value;
	    		if (currentTreeNode.isLeaf()&&(currentTreeNode.getParent()==superGroupsTopNode||currentTreeNode.getParent()==superRolesTopNode)) {
	    			setIcon(resourceSuperClass);
	    		} else{ 
	    			if(currentTreeNode.isLeaf()){
	    				setIcon(resourceClass);
	    			}
	    		}
	    		setClosedIcon(resourceSuperClass);
			    setOpenIcon(resourceSuperClass);
	    		return this;
	    	}
	    }
	    

	    //++++++
	 // Drop-Target-Listener:
		   DropTargetListener MyDropTargetListener =
			   new DropTargetListener() {


			    public void dragEnter(DropTargetDragEvent e) {}
			    	
			    public void dragExit(DropTargetEvent e) {}

			    public void dragOver(DropTargetDragEvent e) {}

			    public void drop(DropTargetDropEvent e) {
			      try {
			        Transferable tr = e.getTransferable();
			        DataFlavor[] flavors = tr.getTransferDataFlavors();
			        for (int i = 0; i < flavors.length; i++)
			         if (flavors[i].isFlavorJavaFileListType()) {
			          e.acceptDrop(e.getDropAction());		          
			          e.dropComplete(true);
			          
			          return;
			         }
			        		
			      } catch (Throwable t) { t.printStackTrace(); }

			      e.rejectDrop();
			    }
			    
			    public void dropActionChanged(DropTargetDragEvent e) {}
			  };
}
	
@SuppressWarnings("serial")
class DragTree extends JTree {


	  private DefaultMutableTreeNode m_parent;

	  public DragTree(DefaultMutableTreeNode rootNode, DefaultMutableTreeNode parent) {
		  super( rootNode );
		  
		  m_parent = parent;
	    DragSource dragSource = DragSource.getDefaultDragSource();
	    dragSource
	        .createDefaultDragGestureRecognizer(this,
	            DnDConstants.ACTION_COPY_OR_MOVE,
	            new TreeDragGestureListener( this.getModel(),m_parent ) );
	  }

	  private static class TreeDragGestureListener implements DragGestureListener {
		  private TreeModel m_tree_model;
		  private DefaultMutableTreeNode m_parent;
		  
		  public TreeDragGestureListener( TreeModel tree_model, DefaultMutableTreeNode parent){
			  m_tree_model = tree_model;
			  m_parent = parent;
		  }
		  		  
	    public void dragGestureRecognized(DragGestureEvent dragGestureEvent) {
	      // Can only drag leafs
	      JTree tree = (JTree) dragGestureEvent.getComponent();
	      TreePath path = tree.getSelectionPath();
	      if (path == null) {
	        // Nothing selected, nothing to drag
	     
	        tree.getToolkit().beep();
	      } else {
	        DefaultMutableTreeNode selection = (DefaultMutableTreeNode) path
	            .getLastPathComponent();
	        if (selection.isLeaf() && (selection.getLevel() >= 2)) {
	          TransferableTreeNode node = new TransferableTreeNode(
	              selection);
	          dragGestureEvent.startDrag(DragSource.DefaultCopyDrop,
	              node, new MyDragSourceListener( selection,(DefaultTreeModel) m_tree_model, m_parent ));
	        } else {
	        
	          tree.getToolkit().beep();
	        }
	      }
	    }
	  }

	  private static class MyDragSourceListener implements DragSourceListener {
		  
		private DefaultMutableTreeNode m_node;
		private DefaultTreeModel m_model;
		private DefaultMutableTreeNode m_parent;
		
		public  MyDragSourceListener (DefaultMutableTreeNode node, DefaultTreeModel model, DefaultMutableTreeNode parent){
			  m_node = node;
			  m_model = model;
			  m_parent = parent;
		  }
		  
	    public void dragDropEnd(DragSourceDropEvent dragSourceDropEvent) {
	      if (dragSourceDropEvent.getDropSuccess()) {
	        int dropAction = dragSourceDropEvent.getDropAction();
	        if (dropAction == DnDConstants.ACTION_MOVE) {
	        	
	        	m_model.removeNodeFromParent( m_node );
	        	
	        	
	        	m_model.insertNodeInto(m_node, m_parent, m_parent.getChildCount());
	       	        	
	        
	        }
	      }
	    }

	    public void dragEnter(DragSourceDragEvent dragSourceDragEvent) {
	      DragSourceContext context = dragSourceDragEvent
	          .getDragSourceContext();
	      int dropAction = dragSourceDragEvent.getDropAction();
	      if ((dropAction & DnDConstants.ACTION_COPY) != 0) {
	        context.setCursor(DragSource.DefaultCopyDrop);
	      } else if ((dropAction & DnDConstants.ACTION_MOVE) != 0) {
	        context.setCursor(DragSource.DefaultMoveDrop);
	      } else {
	        context.setCursor(DragSource.DefaultCopyNoDrop);
	      }
	    }

	    public void dragExit(DragSourceEvent dragSourceEvent) {
	    }

	    public void dragOver(DragSourceDragEvent dragSourceDragEvent) {
	    }

	    public void dropActionChanged(DragSourceDragEvent dragSourceDragEvent) {
	    }
	  }
	}

	@SuppressWarnings("serial")
	class TransferableTreeNode extends DefaultMutableTreeNode implements
	    Transferable {
	  final static int TREE = 0;


	  final public static DataFlavor DEFAULT_MUTABLE_TREENODE_FLAVOR = new DataFlavor(
	      DefaultMutableTreeNode.class, "Default Mutable Tree Node");

	  static DataFlavor flavors = DEFAULT_MUTABLE_TREENODE_FLAVOR ;


	  private DefaultMutableTreeNode data;

	  public TransferableTreeNode(DefaultMutableTreeNode data) {
	    this.data = data;
	  }

	  public DataFlavor isDataFlavorSupported() {
	    return flavors;
	  }

	  public Object getTransferData(DataFlavor flavor)
	      throws UnsupportedFlavorException, IOException {
	    Object returnObject;
	    if (flavor.equals(flavors)) {
	      Object userObject = data.getUserObject();
	      if (userObject == null) {
	        returnObject = data;
	      } else {
	        returnObject = userObject;
	      }
	    } 
	    else {
	      throw new UnsupportedFlavorException(flavor);
	    }
	    return returnObject;
	  }



	  public boolean isDataFlavorSupported(DataFlavor flavor) {
	    boolean returnValue = false;
	      if (flavor.equals(flavors)){
	        returnValue = true;
	      }
	    
	    return returnValue;
	  }


	public DataFlavor[] getTransferDataFlavors() {
		
		return null;
	}


	}
	
	@SuppressWarnings("serial")
	class DropTree extends JTree implements Autoscroll {

		  private Insets insets;
		  PetriNetModelProcessor petrinet;

		  private int top = 0, bottom = 0, topRow = 0, bottomRow = 0;

		  public DropTree(TreeModel treeModel,PetriNetModelProcessor petrinet ) {
			  super( treeModel );
			  this.petrinet=petrinet;
		      @SuppressWarnings("unused")
			DropTarget dropTarget = new DropTarget(this,
		         new TreeDropTargetListener());
		  }



		  public Insets getAutoscrollInsets() {
		    return insets;
		  }

		  public void autoscroll(Point p) {
		    // Only support up/down scrolling
		    top = Math.abs(getLocation().y) + 10;
		    bottom = top + getParent().getHeight() - 20;
		    int next;
		    if (p.y < top) {
		      next = topRow--;
		      bottomRow++;
		      scrollRowToVisible(next);
		    } else if (p.y > bottom) {
		      next = bottomRow++;
		      topRow--;
		      scrollRowToVisible(next);
		    }
		  }


		  private class TreeDropTargetListener implements DropTargetListener {

		    public void dragEnter(DropTargetDragEvent dropTargetDragEvent) {
		      // Setup positioning info for auto-scrolling
		      top = Math.abs(getLocation().y);
		      bottom = top + getParent().getHeight();
		      topRow = getClosestRowForLocation(0, top);
		      bottomRow = getClosestRowForLocation(0, bottom);
		      insets = new Insets(top + 10, 0, bottom - 10, getWidth());
		    }

		    public void dragExit(DropTargetEvent dropTargetEvent) {
		    }

		    public void dragOver(DropTargetDragEvent dropTargetDragEvent) {
		    }

		    public void dropActionChanged(DropTargetDragEvent dropTargetDragEvent) {
		    }

		    public synchronized void drop(DropTargetDropEvent dropTargetDropEvent) {
		      // Only support dropping over nodes that aren't leafs

		      Point location = dropTargetDropEvent.getLocation();
		      TreePath path = getPathForLocation(location.x, location.y);
		      Object node = path.getLastPathComponent();

		      if ((node != null) && (node instanceof TreeNode))
		    		  {
		        try {
		          Transferable tr = dropTargetDropEvent.getTransferable();
		          if (tr
		              .isDataFlavorSupported(TransferableTreeNode.DEFAULT_MUTABLE_TREENODE_FLAVOR)) {
		            dropTargetDropEvent
		                .acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
		            Object userObject = tr
		                .getTransferData(TransferableTreeNode.DEFAULT_MUTABLE_TREENODE_FLAVOR);
		            addElement(path, userObject);
		            dropTargetDropEvent.dropComplete(true);
		          } 
		          else {
		           
		            dropTargetDropEvent.rejectDrop();
		          }
		        } catch (IOException io) {
		          io.printStackTrace();
		          dropTargetDropEvent.rejectDrop();
		        } catch (UnsupportedFlavorException ufe) {
		          ufe.printStackTrace();
		          dropTargetDropEvent.rejectDrop();
		        }
		      } else {
		       
		        dropTargetDropEvent.rejectDrop();
		      }
		    }
		    
			   private boolean parentContainsNode(MutableTreeNode parent, MutableTreeNode node){
				   boolean contains= false;
					  for(int j = 0;j<parent.getChildCount();j++){
						  MutableTreeNode child = (MutableTreeNode) parent.getChildAt(j);
						  if(node.toString().equalsIgnoreCase(child.toString())){
							  contains = true;
						  break;}
					  }
				   return contains;
			   }
			   
		  public void collapseAll(JTree tree) {
			 int row = tree.getRowCount() - 1;
			 while (row >= 0) {
				  tree.collapseRow(row);
				  row--;
			 }
		  }
		    private void addElement(TreePath path, Object element) {
		      DefaultMutableTreeNode parent = (DefaultMutableTreeNode) path
		          .getLastPathComponent();
		      DefaultMutableTreeNode node = new DefaultMutableTreeNode(element);
		  
		      DefaultTreeModel model = (DefaultTreeModel) (DropTree.this
		          .getModel());
		      
		      int d =  parent.getLevel();
		     
		      if(!(d > 1))
		      		if (!parentContainsNode(parent,node)){
		    	
		      			petrinet.addResourceMapping(parent.toString(), node.toString());		
		      			model.insertNodeInto(node, parent, parent.getChildCount());
		      			Vector<ResourceClassModel> Groups = petrinet.getOrganizationUnits();
		      			Vector<ResourceClassModel> Roles = petrinet.getRoles();
		      			
		      			ResourceClassModel Group;
		      			for (Iterator <ResourceClassModel>iter = Groups.iterator(); iter.hasNext();){
		      				Group = iter.next();
		      				if(Group.getSuperModels()!= null && Group.toString().equals(parent.toString())){		      					
		      					for(Iterator<ResourceClassModel>superGroups = Group.getSuperModels();superGroups.hasNext();){
		      						String currentSuperGroup= superGroups.next().toString();		      				
		      						petrinet.addResourceMapping(currentSuperGroup.toString(), node.toString());		      					
		      					}
		      				}
		      			}
		      			ResourceClassModel Role;
		      			for (Iterator <ResourceClassModel>iter = Roles.iterator(); iter.hasNext();){
		      				Role = iter.next();
		      				if(Role.getSuperModels()!= null && Role.toString().equals(parent.toString())){		      					
		      					for(Iterator<ResourceClassModel>superRoles = Role.getSuperModels();superRoles.hasNext();){
		      						String currentSuperRole= superRoles.next().toString();		      				
		      						petrinet.addResourceMapping(currentSuperRole.toString(), node.toString());		      					
		      					}
		      				}
		      			}
		      			
		      			
		      			DefaultMutableTreeNode superParent = (DefaultMutableTreeNode) parent.getParent();
		      			
		      			int pathToExpand = superParent.getIndex(parent);
		      			collapseAll(DropTree.this);
		      			DropTree.this.expandRow(pathToExpand);
		      		}
		      		else{
		      			JOptionPane.showMessageDialog(null , Messages.getString("ResourceEditor.Error.AlreadyAssigned.Text"), Messages.getString("ResourceEditor.Error.AlreadyAssigned.Title"),
	                        JOptionPane.ERROR_MESSAGE);
		      		}
		      else{
		    	 DefaultMutableTreeNode  superParent = (DefaultMutableTreeNode) parent.getParent();
		      		if (!parentContainsNode(superParent,node)){
				    	
		      			petrinet.addResourceMapping(superParent.toString(), node.toString());		
		      			model.insertNodeInto(node, superParent, superParent.getChildCount());
		      			
		      			Vector<ResourceClassModel> Groups = petrinet.getOrganizationUnits();
		      			Vector<ResourceClassModel> Roles = petrinet.getRoles();
		      			
		      			ResourceClassModel Group;
		      			for (Iterator <ResourceClassModel>iter = Groups.iterator(); iter.hasNext();){
		      				Group = iter.next();
		      				if(Group.getSuperModels()!= null && Group.toString().equals(superParent.toString())){		      					
		      					for(Iterator<ResourceClassModel>superGroups = Group.getSuperModels();superGroups.hasNext();){
		      						String currentSuperGroup= superGroups.next().toString();		      				
		      						petrinet.addResourceMapping(currentSuperGroup.toString(), node.toString());		      					
		      					}
		      				}
		      			}
		      			ResourceClassModel Role;
		      			for (Iterator <ResourceClassModel>iter = Roles.iterator(); iter.hasNext();){
		      				Role = iter.next();
		      				if(Role.getSuperModels()!= null && Role.toString().equals(superParent.toString())){		      					
		      					for(Iterator<ResourceClassModel>superRoles = Role.getSuperModels();superRoles.hasNext();){
		      						String currentSuperRole= superRoles.next().toString();		      				
		      						petrinet.addResourceMapping(currentSuperRole.toString(), node.toString());		      					
		      					}
		      				}
		      			}
		      		}
		      		else{
		      			JOptionPane.showMessageDialog(null , Messages.getString("ResourceEditor.Error.AlreadyAssigned.Text"), Messages.getString("ResourceEditor.Error.AlreadyAssigned.Title"),
	                        JOptionPane.ERROR_MESSAGE);
		      		}
		      }
		    }
		  }  
}



	   
	   
