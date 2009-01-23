/*
 * 
 * Copyright (C) 2004-2005, see @author in JavaDoc for the author 
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
 * For contact information please visit http://woped.ba-karlsruhe.de
 *
 */
package org.woped.editor.controller;


import java.awt.*;
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
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
//import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.AbstractPetriNetModelElement;
import org.woped.core.model.petrinet.ResourceClassModel;
import org.woped.core.model.petrinet.ResourceModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.utilities.SwingUtils;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.translations.Messages;


//import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

//import com.sun.xml.internal.ws.api.server.Container;

/**
 * @author Tobias, Stephanie, Jenny, Linda
 */

@SuppressWarnings("serial")
public class PetriNetResourceEditorNew extends JPanel //implements ListSelectionListener
{

	// Objects-Panel

	private JPanel 				   resourceObjectsPanel			= null;
	private JPanel                 resourceObjectsButtonPanel   = null;
	private JScrollPane			   resourceObjectsScrollPane	= null;
	private JButton				   resourceObjectsNewButton		= null;
	private JButton				   resourceObjectsEditButton	= null;
	private JButton				   resourceObjectsDeleteButton	= null;
	private JTree				   resourceObjectsTree			= null;
	private DefaultTreeModel 	   resourceObjectsTreeModel		= null;
	private DefaultMutableTreeNode objectsTopNode				= null;
	private DefaultMutableTreeNode objectsUnassignedNode		= null;
	private DefaultMutableTreeNode objectsAssignedNode			= null;

	//	private DefaultListModel	   objectsListModel				= new DefaultListModel();
	private DefaultListModel       objectsAssignedListModel     = new DefaultListModel();
	private DefaultListModel       objectsUnassignedListModel   = new DefaultListModel();

	
	//Roles-Panel
	private JPanel                 resourceRolesPanel           = null;
	
	private JPanel                 resourceRolesButtonPanel     = null;
	private JScrollPane            resourceRolesScrollPane      = null;
	private JButton				   resourceRolesNewButton		= null;
	private JButton				   resourceRolesEditButton		= null;
	private JButton				   resourceRolesDeleteButton	= null;
	private JTree				   resourceRolesTree			= null;
	private DefaultTreeModel 	   resourceRolesTreeModel		= null;
	private DefaultMutableTreeNode rolesTopNode					= null;
	private DefaultListModel       rolesListModel               = new DefaultListModel();
	
	private JPanel                 resourceSuperRolesButtonPanel= null;
	private JScrollPane            resourceSuperRolesScrollPane = null;
	private JButton				   resourceSuperRolesNewButton	= null;
	private JButton				   resourceSuperRolesEditButton	= null;
	private JButton				   resourceSuperRolesDeleteButton	= null;
	private JTree				   resourceSuperRolesTree		= null;
	private DefaultTreeModel 	   resourceSuperRolesTreeModel	= null;
	private DefaultMutableTreeNode superRolesTopNode			= null;
	private DefaultListModel       superRolesListModel          = new DefaultListModel();
	
//	private JList                  rolesList                    = null;
        

	//	Groups-Panel
	private JPanel                 resourceGroupsPanel          = null;
	private JPanel                 resourceGroupsButtonPanel    = null;
	private JScrollPane            resourceGroupsScrollPane     = null;
	private JButton				   resourceGroupsNewButton		= null;
	private JButton				   resourceGroupsEditButton		= null;
	private JButton				   resourceGroupsDeleteButton	= null;
	private JTree				   resourceGroupsTree			= null;
	private DefaultTreeModel 	   resourceGroupsTreeModel		= null;
	private DefaultMutableTreeNode groupsTopNode				= null;
	private DefaultListModel       groupsListModel              = new DefaultListModel();
	
	private JPanel                 resourceSuperGroupsButtonPanel    = null;
	private JScrollPane            resourceSuperGroupsScrollPane= null;
	private JButton				   resourceSuperGroupsNewButton	= null;
	private JButton				   resourceSuperGroupsEditButton= null;
	private JButton				   resourceSuperGroupsDeleteButton	= null;
	private JTree				   resourceSuperGroupsTree			= null;
	private DefaultTreeModel 	   resourceSuperGroupsTreeModel		= null;
	private DefaultMutableTreeNode superGroupsTopNode				= null;
	private DefaultListModel       superGroupsListModel             = new DefaultListModel();
	
//    private JList                  groupList                   = null;
  
	private String				   newResourceName				= null;

	// Graphic-Panel
	private JPanel					graphicPanel				= null;


//  Dialog-Frame
	private JFrame				   dialogFrame							= null;
	private JButton 			   dialogFrameConfirmButton				= null;
	private	JButton 			   dialogFrameCancelButton				= null;
  	private JTextField			   dialogFrameTextField					= null;	
  	private JList				   selectedRolesList					= null;
  	private JList				   selectedGroupsList					= null;
  	
  	
    private PetriNetModelProcessor petrinet;
    private EditorVC               editor;
    
	private ActionListener		   createResource				= new createResource();
	private ActionListener		   removeResource				= new removeResource();
	private ActionListener		   editResource					= new editResource();
	private ActionListener  	   createSuperResourceFrame		= new createSuperResourceFrame();
	private ActionListener  	   createSuperResource			= new createSuperResource();
	private TreeSelectionListener  treeSelection				= new MyTreeSelectionListener();
	private ImageIcon 			   resourceClass  				= Messages.getImageIcon("PetriNet.Resources.ResourceClass");
	private ImageIcon 			   resourceSuperClass			= Messages.getImageIcon("PetriNet.Resources.ResourceSuperClass");
	private ImageIcon 			   object		 				= Messages.getImageIcon("PetriNet.Resources.Object");
	private ImageIcon 			   unassignedIcon  				= Messages.getImageIcon("PetriNet.Resources.Unassigned");
	
	private static final int ITALIC = 0;
	private Font Nodes = new Font("Nodes",ITALIC,14);

    	
	 public EditorVC getEditor()
	    {
	        return editor;
	    }
	 public PetriNetModelProcessor getPetrinet()
	    {
	        return petrinet;
	    }
	 public PetriNetResourceEditorNew (EditorVC editor)
	    {
	        this.editor = editor;
	        this.petrinet = (PetriNetModelProcessor) editor.getModelProcessor();
	        initialize();
	        reset();
	    }
	    public void reset()
	    {
	        refreshFromModel(); 
	    }
	 
	  //++++++ObjectClassRenderer++++++++
//	    class MyRenderer extends DefaultTreeCellRenderer{
//	    	public Component getTreeCellRendererComponent(JTree tree,Object value,boolean sel, boolean expanded,boolean leaf,int row, boolean hasFocus)
//	    	{
//	    		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
//	    		DefaultMutableTreeNode currentTreeNode = (DefaultMutableTreeNode) value;
//	    		if (currentTreeNode.isLeaf()&&currentTreeNode.getParent()==objectsTopNode) {
//	    			
//	    		} 
//	    		if (currentTreeNode.isLeaf()&&currentTreeNode.getParent()!=objectsTopNode) {
//	    			setLeafIcon(object);
//	    		}
//	    		//else{ 
////	    			if(currentTreeNode.isLeaf()){
////	    			setLeafIcon(object);
////	    			}
////	    		}
////	    		setClosedIcon(resourceSuperClass);
////			    setOpenIcon(resourceSuperClass);
//	    		return this;
//	    	}
//	    }
//	    MyRenderer objectRenderer = new MyRenderer();
//	    
	    //++++++ResourceClassRenderer++++++++++
	    class MyRenderer1 extends DefaultTreeCellRenderer{
	    	public Component getTreeCellRendererComponent(JTree tree,Object value,boolean sel, boolean expanded,boolean leaf,int row, boolean hasFocus)
	    	{
	    		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
	    		DefaultMutableTreeNode currentTreeNode = (DefaultMutableTreeNode) value;
	    		if	(currentTreeNode==objectsUnassignedNode){
	    			setIcon(unassignedIcon);
	    		}
	    		if (currentTreeNode.isLeaf()&&(currentTreeNode==objectsAssignedNode||currentTreeNode.getParent()==rolesTopNode||currentTreeNode.getParent()==groupsTopNode)) {
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
	    MyRenderer1 rendererResourceClass = new MyRenderer1();
	    
	    //++++++ResourceSuperClassRenderer++++++++
	    class MyRenderer2 extends DefaultTreeCellRenderer{
	    	public Component getTreeCellRendererComponent(JTree tree,Object value,boolean sel, boolean expanded,boolean leaf,int row, boolean hasFocus)
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
	    
	    MyRenderer2 rendererResourceSuperClass = new MyRenderer2();
	    //++++++
	    
	   private void initialize(){
	        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	        this.setSize(new Dimension(1200, 800));
	        this.setLayout(new GridBagLayout());
	        GridBagConstraints c = new GridBagConstraints();

	        c.weightx = 4;
	        c.weighty = 0;
	        

	        c.anchor = GridBagConstraints.WEST;
	        c.gridx = 0;
	        c.gridy = 0;
	        this.add(getResourceObjectsPanel(),c);
	        
	        c.anchor = GridBagConstraints.WEST;
	        c.gridx = 1;
	        c.gridy = 0;
	        this.add(getGraphicPanel(),c);

	        c.anchor = GridBagConstraints.EAST;
	        c.gridx = 2;
	        c.gridy = 0;
	        this.add(getResourceRolesPanel(),c);
	       
	        c.anchor = GridBagConstraints.WEST;
	     	c.gridx = 3;
	        c.gridy = 0;
	        this.add(getResourceGroupsPanel(),c);

	   }


	   
//		*****************OBJECT_PANEL*************************************
//	 	*****************************************************************
	   private JPanel getResourceObjectsPanel(){
		   if (resourceObjectsPanel == null){
			   resourceObjectsPanel = new JPanel(new GridBagLayout());
			   resourceObjectsPanel.setBorder(BorderFactory
	                    .createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("PetriNet.Resources.Resource")), BorderFactory.createEmptyBorder()));
	            SwingUtils.setFixedSize(resourceObjectsPanel,350,580);
	            GridBagConstraints c = new GridBagConstraints();
	            c.weightx = 1;
		        c.weighty = 1;
		        c.anchor = GridBagConstraints.NORTH;

		        c.gridx = 0;
		        c.gridy = 0;
		        resourceObjectsPanel.add(getResourceObjectsButtonPanel(),c);
	            
		        c.fill = GridBagConstraints.VERTICAL;
		        c.gridx = 0;
		        c.gridy = 1;

		        resourceObjectsPanel.add(getResourceObjectsScrollPane(),c);
		   }
		   return resourceObjectsPanel;
	   }
	   
//****************************OBJECTS_BUTTON_PANEL********************************   
	   private JPanel getResourceObjectsButtonPanel(){
		   if (resourceObjectsButtonPanel == null){
			   resourceObjectsButtonPanel = new JPanel(new GridLayout());
			   SwingUtils.setFixedSize(resourceObjectsButtonPanel, 330,23);
			   resourceObjectsButtonPanel.add(getResourceObjectsNewButton());
			   resourceObjectsButtonPanel.add(getResourceObjectsEditButton());
			   resourceObjectsButtonPanel.add(getResourceObjectsDeleteButton());
		   }
		   return resourceObjectsButtonPanel;
	   }
	   
	   
	   private JButton getResourceObjectsNewButton(){
	        if (resourceObjectsNewButton == null){
	        	resourceObjectsNewButton = new JButton();
	        	resourceObjectsNewButton.setIcon(Messages.getImageIcon("PetriNet.Resources.New"));
	        	resourceObjectsNewButton.setText(Messages.getString("PetriNet.Resources.New.Title"));
	        	resourceObjectsNewButton.addActionListener(createResource);
	        }

	        return resourceObjectsNewButton;
	    }
	   
	   private JButton getResourceObjectsEditButton(){
	        if (resourceObjectsEditButton == null){
	        	resourceObjectsEditButton = new JButton();
	        	resourceObjectsEditButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Edit"));
	        	resourceObjectsEditButton.setText(Messages.getString("Button.Edit.Title"));
	        	resourceObjectsEditButton.setEnabled(true);
	        	resourceObjectsEditButton.addActionListener(editResource);
	        }

	        return resourceObjectsEditButton;
	    }
	   
	   private JButton getResourceObjectsDeleteButton(){
	        if (resourceObjectsDeleteButton == null){
	        	resourceObjectsDeleteButton = new JButton();
	        	resourceObjectsDeleteButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Delete"));
	        	resourceObjectsDeleteButton.setText(Messages.getString("PetriNet.Resources.Delete.Title"));
	        	resourceObjectsDeleteButton.addActionListener(removeResource);
	        	resourceObjectsDeleteButton.setEnabled(true);
	        	
	        }

	        return resourceObjectsDeleteButton;
	    }
//	   *********************OBJECTS_CONTENT_PANEL**********************************

	   private JScrollPane getResourceObjectsScrollPane(){
		   if (resourceObjectsScrollPane == null){
			   resourceObjectsScrollPane = new JScrollPane(getResourceObjectsTree());
			   resourceObjectsScrollPane.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.GRAY));
			   SwingUtils.setFixedSize(resourceObjectsScrollPane, 330,515);  
		   }
		   return resourceObjectsScrollPane;
	   }
	   
	   
	  private DragTree getResourceObjectsTree(){
		   if (resourceObjectsTree == null){
			    objectsTopNode = new DefaultMutableTreeNode("Objects");
			    resourceObjectsTreeModel = new DefaultTreeModel(objectsTopNode);
			    
			    objectsUnassignedNode = new DefaultMutableTreeNode(Messages.getString("PetriNet.Resources.Resource.Unassigned"));
			    objectsTopNode.add(objectsUnassignedNode);
			    objectsAssignedNode = new DefaultMutableTreeNode(Messages.getString("PetriNet.Resources.Resource.Assigned"));
			    objectsTopNode.add(objectsAssignedNode);
			
			    resourceObjectsTree = new DragTree(objectsTopNode,objectsAssignedNode);
			    
			    resourceObjectsTree.setRootVisible(false);
			    resourceObjectsTree.setRowHeight(20);
			    resourceObjectsTree.setEditable(true);
//			    resourceObjectsTree.addTreeExpansionListener(new MyTreeExpansionListener());
			    resourceObjectsTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			    resourceObjectsTree.addTreeSelectionListener(new MyTreeSelectionListener());
			    resourceObjectsTree.setShowsRootHandles(true);
			    resourceObjectsTree.setFont(Nodes);			  
			    //resourceObjectsTree.setDragEnabled(true);
			    //resourceObjectsTree.setDropTarget(new DropTarget(resourceRolesTree,MyDropTargetListener));
			    resourceObjectsTree.setShowsRootHandles(true);
			    resourceObjectsTree.setCellRenderer(rendererResourceClass);

			    
		   }return (DragTree)resourceObjectsTree;
	   }
	  
	   
	  /*
	   * 
	   /*
	  //***GESTURE LISTENER
	  DragGestureListener MyDragGestureListener =
		    new DragGestureListener() {
		     public void dragGestureRecognized(
		       DragGestureEvent e) {
		       // Der Text des Label soll
		       // JVM-intern
		       // übertragen werden
		       StringSelection selection = 
		         new StringSelection(
		          resourceObjectsTree.getName()); 
		       e.startDrag(getCursor(), selection, MyDragSourceListener);
		     } 
		  };
		  DragSource dragSource = new DragSource();
		  DragGestureRecognizer dgr = 
		    dragSource.createDefaultDragGestureRecognizer(
		    resourceObjectsTree, 
		    DnDConstants.ACTION_COPY, 
		    dragGestureListener);

	  // ENDE Listener
		  //DRAG-Source-Listener
		 
		  DragSourceListener MyDragSourceListener = new DragSourceListener (){
			  dragExit();
		  }
		  
		 // ENDE DRAG SOURCE-Listener
	   */
	
	  //*******************************GRAPHIC_PANEL*******************************
	  //***********************************************************************	 
	  private JPanel getGraphicPanel(){
		  if (graphicPanel == null){
			  graphicPanel = new JPanel(new GridBagLayout());
//			  graphicPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),BorderFactory.createEmptyBorder()));
			  SwingUtils.setFixedSize(graphicPanel,100,200);
	            
	            JLabel graphic1 = new JLabel(), graphic2 = new JLabel(), graphic3 = new JLabel();
	            
	            graphic1.setIcon(Messages.getImageIcon("PetriNet.Resources.Mouse"));
	            graphic1.setToolTipText("Drag&Drop");
	            graphic2.setIcon(Messages.getImageIcon("PetriNet.Resources.Drag"));
	            graphic2.setToolTipText("Drag&Drop");
//	            graphic3.setText("Drag and Drop"); 
	           	            
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
	   private JPanel getResourceRolesPanel(){
		   if (resourceRolesPanel == null){
			   resourceRolesPanel = new JPanel(new GridBagLayout());
	            resourceRolesPanel.setBorder(BorderFactory
	                    .createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("PetriNet.Resources.Roles")), BorderFactory.createEmptyBorder()));
	            SwingUtils.setFixedSize(resourceRolesPanel, 350,580);
	          
	            GridBagConstraints c = new GridBagConstraints();
	            
		        c.weightx = 1;
		        c.weighty = 1;
		        c.anchor = GridBagConstraints.NORTH;
		        c.gridx = 0;
		        c.gridy = 0;
		        resourceRolesPanel.add(getResourceRolesButtonPanel(),c);
                     
		        c.fill = GridBagConstraints.SOUTH;
	            c.gridx = 0;
	            c.gridy = 1;
	            resourceRolesPanel.add(getResourceRolesScrollPane(),c);	
	            
	            c.fill = GridBagConstraints.NORTH;
	            c.gridx = 0;
	            c.gridy = 2;
	            resourceRolesPanel.add(getResourceSuperRolesButtonPanel(),c);
	            
	            c.fill = GridBagConstraints.VERTICAL;
	            c.gridx = 0;
	            c.gridy = 3;
	            resourceRolesPanel.add(getresourceSuperRolesScrollPane(),c);
	           
	            
		   }
		   return resourceRolesPanel;
	   }

	   

//****************************ROLES_BUTTON_PANEL*******************************	   
	   private JPanel getResourceRolesButtonPanel(){
		   if (resourceRolesButtonPanel == null){
			   resourceRolesButtonPanel = new JPanel(new GridLayout());
			   SwingUtils.setFixedSize(resourceRolesButtonPanel, 330,23);
			   resourceRolesButtonPanel.add(getResourceRolesNewButton());
			   resourceRolesButtonPanel.add(getResourceRolesEditButton());
			   resourceRolesButtonPanel.add(getResourceRolesDeleteButton());
		   }
		   return resourceRolesButtonPanel;
	   }
	   
	   
	   private JButton getResourceRolesNewButton(){
	        if (resourceRolesNewButton == null){
	        	resourceRolesNewButton = new JButton();
	        	resourceRolesNewButton.setIcon(Messages.getImageIcon("PetriNet.Resources.New"));
	        	resourceRolesNewButton.setText(Messages.getString("PetriNet.Resources.New.Title"));
	            resourceRolesNewButton.addActionListener(createResource);
	        }

	        return resourceRolesNewButton;
	    }
	   
	   private JButton getResourceRolesEditButton(){
	        if (resourceRolesEditButton == null){
	        	resourceRolesEditButton = new JButton();
	        	resourceRolesEditButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Edit"));
	        	resourceRolesEditButton.setText(Messages.getString("Button.Edit.Title"));
	        	resourceRolesEditButton.setEnabled(false);
	        	resourceRolesEditButton.addActionListener(editResource);
	        }

	        return resourceRolesEditButton;
	    }
	   
	   private JButton getResourceRolesDeleteButton(){
	        if (resourceRolesDeleteButton == null){
	        	resourceRolesDeleteButton = new JButton();
	        	resourceRolesDeleteButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Delete"));
	        	resourceRolesDeleteButton.setText(Messages.getString("PetriNet.Resources.Delete.Title"));
	        	resourceRolesDeleteButton.addActionListener(removeResource);
	        	resourceRolesDeleteButton.setEnabled(false);
	        }

	        return resourceRolesDeleteButton;
	    }
//	   *********************ROLES_CONTENT_PANEL**************************
	
	   private JScrollPane getResourceRolesScrollPane(){
		   if (resourceRolesScrollPane == null){
			   resourceRolesScrollPane = new JScrollPane(getResourceRolesTree());
			   resourceRolesScrollPane.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.GRAY));
			   SwingUtils.setFixedSize(resourceRolesScrollPane, 330,345);


		   }
		   return resourceRolesScrollPane;
	   }
	  	   
	   private DropTree getResourceRolesTree(){
		   if (resourceRolesTree == null){
			    rolesTopNode = new DefaultMutableTreeNode("Roles");
			    resourceRolesTree = new DropTree(rolesTopNode,getPetrinet());
			    resourceRolesTreeModel = new DefaultTreeModel(rolesTopNode);
			    resourceRolesTree.setRowHeight(20);
			    resourceRolesTree.setEditable(false);
			    resourceRolesTree.setRootVisible(false);
			    resourceRolesTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			    resourceRolesTree.setShowsRootHandles(true);
			    resourceRolesTree.setFont(Nodes);
			    //resourceRolesTree.setToolTipText("Objekt dieser Rolle zuordnen");
			    //resourceRolesTree.setDragEnabled(false);
			    //resourceRolesTree.setDropTarget(new DropTarget(resourceRolesTree, MyDropTargetListener));    
			    resourceRolesTree.setFont(Nodes);
			    resourceRolesTree.addTreeSelectionListener(treeSelection);
			    resourceRolesTree.setCellRenderer(rendererResourceClass);
			  
			   
			    
		   }return (DropTree)resourceRolesTree;
	   }
	   
// Drop-Target-Listener:
	   DropTargetListener MyDropTargetListener =
		   new DropTargetListener() {

		    // Die Maus betritt die Komponente mit
		    // einem Objekt
		    public void dragEnter(DropTargetDragEvent e) {
		    	System.out.println("Test");
		    }
		    	
		    // Die Komponente wird verlassen 
		    public void dragExit(DropTargetEvent e) {
		    	System.out.println("test2");
		    }

		    // Die Maus bewegt sich über die Komponente
		    public void dragOver(DropTargetDragEvent e) {}

		    public void drop(DropTargetDropEvent e) {
		      try {
		        Transferable tr = e.getTransferable();
		        DataFlavor[] flavors = tr.getTransferDataFlavors();
		        for (int i = 0; i < flavors.length; i++)
		         if (flavors[i].isFlavorJavaFileListType()) {
		          // Zunächst annehmen
		          e.acceptDrop(e.getDropAction());
//		          List files = (List) tr.getTransferData(flavors[i]);
		          
		          e.dropComplete(true);
		          
		          return;
		         }
		        		
		      } catch (Throwable t) { t.printStackTrace(); }
		      // Ein Problem ist aufgetreten
		      e.rejectDrop();
		    }
		    
//		    public void drop(DropTargetDropEvent e){
//		    	try{
//		    		if (resourceRolesTree.getLastSelectedPathComponent().equals(rolesTopNode)||resourceGroupsTree.getLastSelectedPathComponent().equals(groupsTopNode)){
//		    			e.acceptDrop(e.getDropAction());
//		    		}
//		    		else { 
//		    			e.rejectDrop();
//		    			System.out.println("Object cannot be dropped here");
//		    		}
//		    	} catch(Throwable t){
//		    		t.printStackTrace();
//		    		e.rejectDrop();
//		    	}
//		    }
		    
		    
		     
		    // Jemand hat die Art des Drops (Move, Copy, Link)
		    // geändert
		    public void dropActionChanged(
		           DropTargetDragEvent e) {}
		  };
		  
  	
		  	
		 // DropTarget dropTarget = new DropTarget(
		 //   label, dropTargetListener);


	   //ENDE Listener
		  

//			************************SUPER_ROLES_BUTTON_PANEL***************
		  private JPanel getResourceSuperRolesButtonPanel(){
			   if (resourceSuperRolesButtonPanel == null){
				   resourceSuperRolesButtonPanel = new JPanel(new GridLayout());
				   SwingUtils.setFixedSize(resourceSuperRolesButtonPanel, 330,23);
				   resourceSuperRolesButtonPanel.add(getResourceSuperRolesNewButton());
				   resourceSuperRolesButtonPanel.add(getResourceSuperRolesEditButton());
				   resourceSuperRolesButtonPanel.add(getResourceSuperRolesDeleteButton());
			   }
			   return resourceSuperRolesButtonPanel;
		   }
		   
		   
		   private JButton getResourceSuperRolesNewButton(){
		        if (resourceSuperRolesNewButton == null){
		        	resourceSuperRolesNewButton = new JButton();
		        	resourceSuperRolesNewButton.setIcon(Messages.getImageIcon("PetriNet.Resources.New"));
		        	resourceSuperRolesNewButton.setText(Messages.getString("PetriNet.Resources.New.Title"));
		            resourceSuperRolesNewButton.addActionListener(createSuperResourceFrame);
		        }

		        return resourceSuperRolesNewButton;
		    }
		   
		   private JButton getResourceSuperRolesEditButton(){
		        if (resourceSuperRolesEditButton == null){
		        	resourceSuperRolesEditButton = new JButton();
		        	resourceSuperRolesEditButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Edit"));
		        	resourceSuperRolesEditButton.setText(Messages.getString("Button.Edit.Title"));
		        	resourceSuperRolesEditButton.setEnabled(false);
		        	resourceSuperRolesEditButton.addActionListener(editResource);
		        }

		        return resourceSuperRolesEditButton;
		    }
		   
		   private JButton getResourceSuperRolesDeleteButton(){
		        if (resourceSuperRolesDeleteButton == null){
		        	resourceSuperRolesDeleteButton = new JButton();
		        	resourceSuperRolesDeleteButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Delete"));
		        	resourceSuperRolesDeleteButton.setText(Messages.getString("PetriNet.Resources.Delete.Title"));
		        	resourceSuperRolesDeleteButton.addActionListener(removeResource);
		        	resourceSuperRolesDeleteButton.setEnabled(false);
		        }

		        return resourceSuperRolesDeleteButton;
		    }
		  
//        ***************************SUPER_ROLES_CONTENT_PANEL********************

	          private JScrollPane getresourceSuperRolesScrollPane(){
	              if (resourceSuperRolesScrollPane == null){
	                   resourceSuperRolesScrollPane = new JScrollPane(getResourceSuperRolesTree());
	                   resourceSuperRolesScrollPane.setBorder(BorderFactory.createEtchedBorder(new Color (141, 182, 205), new Color (132, 112, 255)));
	                   resourceSuperRolesScrollPane.setBackground(new Color (2, 12, 241));
	                   SwingUtils.setFixedSize(resourceSuperRolesScrollPane, 330,135);

	               }
	               return resourceSuperRolesScrollPane;
	           }

	          private JTree getResourceSuperRolesTree(){
	               if (resourceSuperRolesTree == null){
	                    superRolesTopNode = new DefaultMutableTreeNode("Super-Roles");
	                    resourceSuperRolesTree = new JTree(superRolesTopNode);
	                    resourceSuperRolesTreeModel = new DefaultTreeModel(superRolesTopNode);
	                    resourceSuperRolesTree.setRowHeight(20);
	                    resourceSuperRolesTree.setEditable(false);
	                    resourceSuperRolesTree.setRootVisible(false);
	                    resourceSuperRolesTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	                    resourceSuperRolesTree.setShowsRootHandles(true);
	                    resourceSuperRolesTree.setFont(Nodes);
	                    //resourceRolesTree.setToolTipText("Objekt dieser Rolle zuordnen");
	                    //resourceRolesTree.setDragEnabled(false);
	                    //resourceRolesTree.setDropTarget(new DropTarget(resourceRolesTree, MyDropTargetListener));    
	                    //resourceRolesTree2.setFont(Nodes);
	                    resourceSuperRolesTree.addTreeSelectionListener(treeSelection);
	                    resourceSuperRolesTree.setCellRenderer(rendererResourceSuperClass);
	               }return (JTree)resourceSuperRolesTree;
	           }
	   
//	   **************************GROUPS_PANEL****************************
//	   *****************************************************************
	   private JPanel getResourceGroupsPanel(){
		   if (resourceGroupsPanel == null){
			   resourceGroupsPanel = new JPanel(new GridBagLayout());
	            resourceGroupsPanel.setBorder(BorderFactory
	                    .createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("PetriNet.Resources.Groups")), BorderFactory.createEmptyBorder()));
	            SwingUtils.setFixedSize(resourceGroupsPanel, 350,580);
	            GridBagConstraints c = new GridBagConstraints();
	            
		        c.weightx = 1;
		        c.weighty = 1;
		        c.anchor = GridBagConstraints.NORTH;

		        c.gridx = 0;
		        c.gridy = 0;
	            resourceGroupsPanel.add(getResourceGroupsButtonPanel(),c);
	            
		        c.fill = GridBagConstraints.SOUTH;
		        c.gridx = 0;
		        c.gridy = 1;

		        resourceGroupsPanel.add(getResourceGroupsScrollPane(),c);
		        
		        c.fill = GridBagConstraints.NORTH;
	            c.gridx = 0;
	            c.gridy = 2;
	            resourceGroupsPanel.add(getResourceSuperGroupsButtonPanel(),c);
	            
	            c.fill = GridBagConstraints.VERTICAL;
	            c.gridx = 0;
	            c.gridy = 3;
	            resourceGroupsPanel.add(getresourceSuperGroupsScrollPane(),c);
		   }
		   return resourceGroupsPanel;
	   }
	   

	   
//	   *********************GROUPS_CONTENT_PANEL**************************

	   private JScrollPane getResourceGroupsScrollPane(){
		   if (resourceGroupsScrollPane == null){
			   resourceGroupsScrollPane = new JScrollPane(getResourceGroupsTree());
			   resourceGroupsScrollPane.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.GRAY));
			   SwingUtils.setFixedSize(resourceGroupsScrollPane, 330,345);
			   

		   }
		   return resourceGroupsScrollPane;
	   }
	   private DropTree getResourceGroupsTree(){
		   if (resourceGroupsTree == null){
			    groupsTopNode = new DefaultMutableTreeNode("Groups");
			    resourceGroupsTree = new DropTree(groupsTopNode,getPetrinet());
			    resourceGroupsTreeModel = new DefaultTreeModel(groupsTopNode);
			    resourceGroupsTree.setRowHeight(20);
			    resourceGroupsTree.setRootVisible(false);
			    resourceGroupsTree.setEditable(false);
//			    resourceGroupsTree.addTreeExpansionListener(new MyTreeExpansionListener());
			    resourceGroupsTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			    resourceGroupsTree.setShowsRootHandles(true);
			    resourceGroupsTree.setFont(Nodes);	
			    resourceGroupsTree.addTreeSelectionListener(treeSelection);
	
			    
//			    DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
//			    renderer.setClosedIcon(Messages.getImageIcon("PetriNet.Resources.Edit"));
//			    renderer.setOpenIcon(Messages.getImageIcon("PetriNet.Resources.Edit"));
		        resourceGroupsTree.setCellRenderer(rendererResourceClass);
		        //resourceGroupsTree.setCellRenderer(renderer);
		        resourceGroupsTree.updateUI();

		   }return (DropTree)resourceGroupsTree;
	   }
	   

	   
	 //****************************GROUPS_BUTTON_PANEL********************************	   
	   private JPanel getResourceGroupsButtonPanel(){
		   if (resourceGroupsButtonPanel == null){
			   resourceGroupsButtonPanel = new JPanel(new GridLayout());
			   SwingUtils.setFixedSize(resourceGroupsButtonPanel, 330,23);
			   resourceGroupsButtonPanel.add(getResourceGroupsNewButton());
			   resourceGroupsButtonPanel.add(getResourceGroupsEditButton());
			   resourceGroupsButtonPanel.add(getResourceGroupsDeleteButton());
		   }
		   return resourceGroupsButtonPanel;
	   }
	   
	   
	   private JButton getResourceGroupsNewButton(){
	        if (resourceGroupsNewButton == null){
	        	resourceGroupsNewButton = new JButton();
	        	resourceGroupsNewButton.setIcon(Messages.getImageIcon("PetriNet.Resources.New"));
	        	resourceGroupsNewButton.setText(Messages.getString("PetriNet.Resources.New.Title"));
	        	resourceGroupsNewButton.addActionListener(createResource);
	        }

	        return resourceGroupsNewButton;
	    }
	   
	   private JButton getResourceGroupsEditButton(){
	        if (resourceGroupsEditButton == null){
	        	resourceGroupsEditButton = new JButton();
	        	resourceGroupsEditButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Edit"));
	        	resourceGroupsEditButton.setText(Messages.getString("Button.Edit.Title"));
	        	resourceGroupsEditButton.setEnabled(false);
	        	resourceGroupsEditButton.addActionListener(editResource);
	        }

	        return resourceGroupsEditButton;
	    }
	   
	   private JButton getResourceGroupsDeleteButton(){
	        if (resourceGroupsDeleteButton == null){
	        	resourceGroupsDeleteButton = new JButton();
	        	resourceGroupsDeleteButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Delete"));
	        	resourceGroupsDeleteButton.setText(Messages.getString("PetriNet.Resources.Delete.Title"));
	        	resourceGroupsDeleteButton.addActionListener(removeResource);
	        	resourceGroupsDeleteButton.setEnabled(false);
	        }

	        return resourceGroupsDeleteButton;
	    }
	   
	   
//      **********************SUPER_GROUPS_BUTTON_PANEL*****************************

		  private JPanel getResourceSuperGroupsButtonPanel(){
			   if (resourceSuperGroupsButtonPanel == null){
				   resourceSuperGroupsButtonPanel = new JPanel(new GridLayout());
				   SwingUtils.setFixedSize(resourceSuperGroupsButtonPanel, 330,23);
				   resourceSuperGroupsButtonPanel.add(getResourceSuperGroupsNewButton());
				   resourceSuperGroupsButtonPanel.add(getResourceSuperGroupsEditButton());
				   resourceSuperGroupsButtonPanel.add(getResourceSuperGroupsDeleteButton());
			   }
			   return resourceSuperGroupsButtonPanel;
		   }
		   
		   
		   private JButton getResourceSuperGroupsNewButton(){
		        if (resourceSuperGroupsNewButton == null){
		        	resourceSuperGroupsNewButton = new JButton();
		        	resourceSuperGroupsNewButton.setIcon(Messages.getImageIcon("PetriNet.Resources.New"));
		        	resourceSuperGroupsNewButton.setText(Messages.getString("PetriNet.Resources.New.Title"));
		            resourceSuperGroupsNewButton.addActionListener(createSuperResourceFrame);
		        }

		        return resourceSuperGroupsNewButton;
		    }
		   
		   private JButton getResourceSuperGroupsEditButton(){
		        if (resourceSuperGroupsEditButton == null){
		        	resourceSuperGroupsEditButton = new JButton();
		        	resourceSuperGroupsEditButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Edit"));
		        	resourceSuperGroupsEditButton.setText(Messages.getString("Button.Edit.Title"));
		        	resourceSuperGroupsEditButton.setEnabled(false);
		        	resourceSuperGroupsEditButton.addActionListener(editResource);
		        }

		        return resourceSuperGroupsEditButton;
		    }
		   
		   private JButton getResourceSuperGroupsDeleteButton(){
		        if (resourceSuperGroupsDeleteButton == null){
		        	resourceSuperGroupsDeleteButton = new JButton();
		        	resourceSuperGroupsDeleteButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Delete"));
		        	resourceSuperGroupsDeleteButton.setText(Messages.getString("PetriNet.Resources.Delete.Title"));
		        	resourceSuperGroupsDeleteButton.addActionListener(removeResource);
		        	resourceSuperGroupsDeleteButton.setEnabled(false);
		        }

		        return resourceSuperGroupsDeleteButton;
		    }

		   
//     ***************************SUPER_GROUPS_CONTENT_PANEL************************

	          private JScrollPane getresourceSuperGroupsScrollPane(){
	              if (resourceSuperGroupsScrollPane == null){
	                   resourceSuperGroupsScrollPane = new JScrollPane(getResourceSuperGroupsTree());
	                   resourceSuperGroupsScrollPane.setBorder(BorderFactory.createEtchedBorder(new Color (141, 182, 205), new Color (132, 112, 255)));
	                   resourceSuperGroupsScrollPane.setBackground(new Color (2, 12, 241));
	                   SwingUtils.setFixedSize(resourceSuperGroupsScrollPane, 330,135);

	               }
	               return resourceSuperGroupsScrollPane;
	           }
	          private JTree getResourceSuperGroupsTree(){
	               if (resourceSuperGroupsTree == null){
	                    superGroupsTopNode = new DefaultMutableTreeNode("Super-Groups");
	                    resourceSuperGroupsTree = new JTree(superGroupsTopNode);
	                    resourceSuperGroupsTreeModel = new DefaultTreeModel(superGroupsTopNode);
	                    resourceSuperGroupsTree.setRowHeight(20);
	                    resourceSuperGroupsTree.setEditable(false);
	                    resourceSuperGroupsTree.setRootVisible(false);
	                    resourceSuperGroupsTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	                    resourceSuperGroupsTree.setShowsRootHandles(true);
	                    resourceSuperGroupsTree.setFont(Nodes);
	                    //resourceGroupsTree.setToolTipText("Objekt dieser Rolle zuordnen");
	                    //resourceGroupsTree.setDragEnabled(false);
	                    //resourceGroupsTree.setDropTarget(new DropTarget(resourceGroupsTree, MyDropTargetListener));    
	                    //resourceGroupsTree2.setFont(Nodes);
	                    resourceSuperGroupsTree.addTreeSelectionListener(treeSelection);
	                    resourceSuperGroupsTree.setCellRenderer(rendererResourceSuperClass);
	                 
	               }return (JTree)resourceSuperGroupsTree;
	           }
	           

	   
	          
	          
	   	   private class createSuperResourceFrame implements ActionListener {
			   	
	   		 public void actionPerformed(ActionEvent e) {
				   try{
					   dialogFrame = new JFrame();
					   dialogFrame.setTitle("Oberrolle anlegen");
					   dialogFrame.setSize(500, 400);
					   dialogFrame.setLocationRelativeTo(resourceRolesPanel);
					   dialogFrame.setVisible(true);
					   dialogFrame.setResizable(false);
					   dialogFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		   		
					   java.awt.Container c = dialogFrame.getContentPane();
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
					   		SwingUtils.setFixedSize(dialogFrameTextField,300,25);
					
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
					   		b.weightx = 2;
					   		b.weighty = 1;
					  
					   		JLabel addRolesLabel = new JLabel(/*Messages.getString("PetriNet.Resources.Resource.AddResourceClass")*/"Test"+":    ");
					   		b.gridx = 0;
					   		b.gridy = 0;
					   		b.anchor = GridBagConstraints.NORTHEAST;
					   		defineResourcePanel.add(addRolesLabel,b);
					   
					  
					   		JPanel selectPanel = new JPanel (new GridLayout());
					   		SwingUtils.setFixedSize(selectPanel,300,240);
					   		JScrollPane selectScrollPane = new JScrollPane();
					   		selectScrollPane.setBackground(Color.WHITE);
					   		
					   		SwingUtils.setFixedSize(selectScrollPane,300,260);
//					   		selectPanel.add(selectScrollPane);
					   		
					   		b.gridx = 1;
					   		b.gridy = 0;
					   		b.anchor = GridBagConstraints.NORTHWEST;
					   		defineResourcePanel.add(selectPanel,b);
					   		if(e.getSource()==resourceSuperRolesNewButton){
					   			selectedRolesList = new JList(rolesListModel);
					   			selectedRolesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
					   			selectPanel.add(selectedRolesList);
					   		}else{
					   			selectedGroupsList = new JList (groupsListModel);
					   			selectedGroupsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
					   			selectPanel.add(selectedGroupsList);
					   		}
//					   		for (int i = 0;i < rolesListModel.getSize();i++){
//					   			String role = rolesListModel.get(i).toString();
//					   			
//					   			selectScrollPane.add(new JCheckBox(role));
//					   		}
					   
					   	contentPane.add(defineResourcePanel,BorderLayout.CENTER);
					 
					   
					   	JPanel buttonPanel = new JPanel();
					   	SwingUtils.setFixedSize(buttonPanel,500,50);
					   	buttonPanel.setBorder(BorderFactory.createEtchedBorder(Color.WHITE, Color.GRAY));
					   	
					   	dialogFrameConfirmButton = new JButton(Messages.getString("PetriNet.Resources.Ok.Title"));
					   	dialogFrameConfirmButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Ok.Icon"));
					   	dialogFrameConfirmButton.addActionListener(createSuperResource);
						SwingUtils.setFixedSize(dialogFrameConfirmButton,80,23);
					   	buttonPanel.add(dialogFrameConfirmButton);
					   	
					   	dialogFrameCancelButton = new JButton(Messages.getString("QuanlAna.ReachabilityGraph.Settings.Button.Cancel"));
					   	SwingUtils.setFixedSize(dialogFrameCancelButton,80,23);
					   	dialogFrameCancelButton.addActionListener(createSuperResource);
					   	buttonPanel.add(dialogFrameCancelButton);
					   	contentPane.add(buttonPanel,BorderLayout.SOUTH);
					   
					   dialogFrame.setContentPane(c);
					} catch (Exception exc) {
						exc.printStackTrace();
					}	   		
	   		 }
		   
	   	   }
	   private class createSuperResource implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==dialogFrameCancelButton){
				dialogFrame.dispose();
			}else{
				if(selectedGroupsList==null){
				Object [] selectedRoles = selectedRolesList.getSelectedValues();
					if(checkClassSyntax(dialogFrameTextField.getText())){
						SuperRolesTreeNode superRole = new SuperRolesTreeNode(dialogFrameTextField.getText());
						resourceRolesTreeModel.insertNodeInto(superRole,superRolesTopNode,  superRolesTopNode.getChildCount());
						for(int i=0;i<selectedRoles.length;i++){
							String name = selectedRoles[i].toString();
							DefaultMutableTreeNode child = new DefaultMutableTreeNode(name);
							resourceSuperRolesTreeModel.insertNodeInto(child, superRole, superRole.getChildCount());
						}
						resourceSuperRolesTree.updateUI();
					}
				}
				if(selectedRolesList==null){
					Object [] selectedGroups = selectedGroupsList.getSelectedValues();
						if(checkClassSyntax(dialogFrameTextField.getText())){
							SuperGroupsTreeNode superGroup = new SuperGroupsTreeNode(dialogFrameTextField.getText());
							resourceGroupsTreeModel.insertNodeInto(superGroup,superGroupsTopNode,  superGroupsTopNode.getChildCount());
							for(int i=0;i<selectedGroups.length;i++){
								String name = selectedGroups[i].toString();
								DefaultMutableTreeNode child = new DefaultMutableTreeNode(name);
								resourceSuperGroupsTreeModel.insertNodeInto(child, superGroup, superGroup.getChildCount());
							}
							resourceSuperGroupsTree.updateUI();
							
							
						}
					}
			}	
			selectedGroupsList = null;
			selectedRolesList = null;
			dialogFrame.dispose();
		}
		   
	   }

		   

//*****************************TREE-NODE**********************
	   //****************************************************
	   class ObjectsTreeNode extends DefaultMutableTreeNode {
		   	public ObjectsTreeNode(){super();}
			public ObjectsTreeNode(String name){super(name);}
	   }
	   
	   class RolesTreeNode extends DefaultMutableTreeNode {
		   public RolesTreeNode(){super();}
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
	   
	   
	   
	   private class createResource implements ActionListener{

		   public void actionPerformed(ActionEvent e) {
			   try{
				   newResourceName = getResourceName ();
				   if (checkClassSyntax(newResourceName )){

					   if(e.getSource()== resourceRolesNewButton ){
//						   addRolesNode(resourceRolesTreeModel,rolesTopNode,newResourceName);
//						   resourceRolesTree.updateUI();
						   ResourceClassModel newRole = new ResourceClassModel(newResourceName, ResourceClassModel.TYPE_ROLE);
		                   getPetrinet().addRole(newRole);
		                   rolesListModel.addElement(newRole);
		                   refreshRolesTreeFromListModel();
		                   refreshGroupsTreeFromListModel();
		                   refreshObjectsFromModel();
		                   getEditor().setSaved(false);
					   }
					   if(e.getSource()== resourceGroupsNewButton ){
						   ResourceClassModel newGroup = new ResourceClassModel(newResourceName, ResourceClassModel.TYPE_ORGUNIT);
		                   getPetrinet().addOrgUnit(newGroup);
		                   groupsListModel.addElement(newGroup);
		                   refreshRolesTreeFromListModel();
		                   refreshGroupsTreeFromListModel();
		                   refreshObjectsFromModel();
		                   getEditor().setSaved(false);
//						   addGroupsNode(resourceGroupsTreeModel,groupsTopNode,newResourceName);
//						 resourceGroupsTree.updateUI();   
					   }
					   if(e.getSource()== resourceObjectsNewButton ){
//						 addObjectsNode(resourceObjectsTreeModel,objectsUnassignedNode,newResourceName);
//						 resourceObjectsTree.updateUI(); 
						 ResourceModel newObject = new ResourceModel(newResourceName);
		                 getPetrinet().addResource(newObject);
		                 objectsUnassignedListModel.addElement(newObject);
		                 refreshObjectsTreeFromListModel();
		                 getEditor().setSaved(false);
					   }
				   }
				   
			   }catch (Exception ex){}
		   }  
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
		    resourceObjectsDeleteButton.setEnabled(true);
		    resourceObjectsEditButton.setEnabled(true);
	   }
	   
	   // ******************** REMOVE-RESSOURCE
	   
	   private class removeResource implements ActionListener{
	
		   public void actionPerformed(ActionEvent e) {
			 
			   try{
				   if(e.getSource()== resourceObjectsDeleteButton){
					   if(!resourceObjectsTree.getLastSelectedPathComponent().equals(objectsUnassignedNode)&&!resourceObjectsTree.getLastSelectedPathComponent().equals(objectsTopNode)&&!resourceObjectsTree.getLastSelectedPathComponent().equals(objectsAssignedNode)){
						   MutableTreeNode nodeToDelete = (MutableTreeNode) resourceObjectsTree.getLastSelectedPathComponent();
						  
						   if(nodeToDelete.getParent().equals(objectsUnassignedNode)){
							   resourceObjectsTreeModel.removeNodeFromParent(nodeToDelete);  
							   objectsUnassignedListModel.removeElement(nodeToDelete.toString());
							   int j = getPetrinet().containsResource(nodeToDelete.toString());
							   objectsUnassignedListModel.removeElement(j);
				               getPetrinet().getResources().remove(j);
				               getEditor().setSaved(false);
							  
						   }
						   else{
							
							   for (int i=0;i < rolesTopNode.getChildCount();i++){
								  
								   RolesTreeNode parent = (RolesTreeNode) rolesTopNode.getChildAt(i);
								   for (int j=0;j<parent.getChildCount();j++){
									   MutableTreeNode  child = (MutableTreeNode) parent.getChildAt(j);
									   if(child.toString().equals(nodeToDelete.toString())){
										   child.removeFromParent();
										   resourceRolesTree.updateUI();		
									   }
								   }
							   }
							   for (int i=0;i < groupsTopNode.getChildCount();i++){
									  
								   GroupsTreeNode parent = (GroupsTreeNode) groupsTopNode.getChildAt(i);
								   for (int j=0;j<parent.getChildCount();j++){
									   MutableTreeNode  child = (MutableTreeNode) parent.getChildAt(j);
									   if(child.toString().equals(nodeToDelete.toString())){
										   child.removeFromParent();
										   resourceGroupsTree.updateUI();
			
									   }
								   }
							   }  
							   resourceObjectsTreeModel.removeNodeFromParent(nodeToDelete);
							   objectsUnassignedListModel.removeElement(nodeToDelete.toString());
							   int j = getPetrinet().containsResource(nodeToDelete.toString());
							   objectsUnassignedListModel.removeElement(j);
				               getPetrinet().getResources().remove(j);
				               getEditor().setSaved(false);
						   }
					if(objectsAssignedNode.getChildCount()==0&&objectsUnassignedNode.getChildCount()==0){
							resourceObjectsDeleteButton.setEnabled(false);
							resourceObjectsEditButton.setEnabled(false); 
						   }
						   resourceObjectsTree.updateUI();
					   }
				   	}
			   }
			   catch (NullPointerException npe){
				   npe.printStackTrace();
			   }

			   if(e.getSource()== resourceRolesDeleteButton){
				  try{
					  String role2remove = resourceRolesTree.getLastSelectedPathComponent().toString();
					  if(!roleIsUsed(role2remove)){
						
						if(resourceRolesDeleteButton.getText().equals(Messages.getString("PetriNet.Resources.Delete.Title"))){
						// Delete a role
							RolesTreeNode nodeToDelete = (RolesTreeNode) resourceRolesTree.getLastSelectedPathComponent();
								if(nodeToDelete.getChildCount()==0){
									int j = getPetrinet().containsRole(role2remove);
									rolesListModel.remove(j);
					                getPetrinet().getRoles().remove(j);
					                refreshRolesTreeFromListModel();
					                refreshGroupsTreeFromListModel();
					                refreshObjectsFromModel();
					                getEditor().setSaved(false);
								}else{
//									for(int i=0; i<nodeToDelete.getChildCount();i++){
//							  			MutableTreeNode childOfNodeToDelete = (MutableTreeNode) nodeToDelete.getChildAt(i);
//							  			if(!treeContainsNode((MutableTreeNode)groupsTopNode,childOfNodeToDelete)){
//											for (int j = 0; j < objectsAssignedNode.getChildCount(); j++) {
//												ObjectsTreeNode nodeToUnassign = (ObjectsTreeNode) objectsAssignedNode.getChildAt(j);
//												String object2unassign = nodeToUnassign.toString();
//												if(childOfNodeToDelete.toString().equals(nodeToUnassign.toString())){
//													objectsAssignedListModel.removeElement(object2unassign);
//													objectsUnassignedListModel.addElement(object2unassign);
////													resourceObjectsTreeModel.removeNodeFromParent(nodeToUnassign);
////													resourceObjectsTreeModel.insertNodeInto(nodeToUnassign, objectsUnassignedNode, objectsUnassignedNode.getChildCount());		  			
//												}
//											}
//							  			}
//									}
								
//							  	resourceRolesTreeModel.removeNodeFromParent(nodeToDelete);
								int j = getPetrinet().containsRole(role2remove);
								rolesListModel.remove(j);
				                getPetrinet().getRoles().remove(j);
								
				                refreshRolesTreeFromListModel(); 
				                refreshGroupsTreeFromListModel();
							  	refreshObjectsTreeFromListModel();
							  	refreshObjectsFromModel();
							  	getEditor().setSaved(false);
							   
								}
//							  	resourceRolesTree.updateUI();
//								resourceObjectsTree.updateUI();
						}
						
						// Unassign an object
						else{
							String object2unassign = resourceRolesTree.getLastSelectedPathComponent().toString();
							DefaultMutableTreeNode child =  (DefaultMutableTreeNode) resourceRolesTree.getLastSelectedPathComponent();
							RolesTreeNode parent = (RolesTreeNode) child.getParent();
							Vector assignedClasses = getPetrinet().getResourceClassesResourceIsAssignedTo(object2unassign.toString());
							Object ass;
							for (Iterator iter = assignedClasses.iterator(); iter.hasNext();){
			    				ass = iter.next();
			    				if(ass.toString().equals(parent.toString())){
			    					 getPetrinet().removeResourceMapping(parent.toString(), object2unassign);
			    				}
							}
								
							
/*							resourceRolesTreeModel.removeNodeFromParent(nodeToDelete);
							if(!treeContainsNode((MutableTreeNode)groupsTopNode,nodeToDelete)){
								for (int i = 0; i < objectsAssignedNode.getChildCount(); i++) {
									ObjectsTreeNode nodeToUnassign = (ObjectsTreeNode) objectsAssignedNode.getChildAt(i);
									if(nodeToDelete.toString().equals(nodeToUnassign.toString())){
										resourceObjectsTreeModel.removeNodeFromParent(nodeToUnassign);

										resourceObjectsTreeModel.insertNodeInto(nodeToUnassign, objectsUnassignedNode, objectsUnassignedNode.getChildCount());
									
										resourceObjectsTree.updateUI();
										resourceRolesDeleteButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Delete"));
										resourceRolesDeleteButton.setText(Messages.getString("PetriNet.Resources.Delete.Title"));
									}
								}
							}*/
						}
						  
						  if(rolesTopNode.getChildCount()==0){
							resourceRolesDeleteButton.setEnabled(false);
							resourceRolesEditButton.setEnabled(false);
						  }
			                refreshRolesTreeFromListModel(); 
			                refreshGroupsTreeFromListModel();
						  	refreshObjectsTreeFromListModel();
						  	refreshObjectsFromModel();
						  	getEditor().setSaved(false);
					  }
			       else{
			         JOptionPane.showMessageDialog(resourceRolesPanel , Messages.getString("ResourceEditor.Error.UsedResourceClass.Text"), Messages.getString("ResourceEditor.Error.UsedResourceClass.Title"),
			                        JOptionPane.ERROR_MESSAGE);
			       }
				  }
				  catch(Exception exc){
					  exc.printStackTrace();
				  }
			   }
			   
			   if(e.getSource()== resourceGroupsDeleteButton){
				  try{
					  String group2remove = resourceGroupsTree.getLastSelectedPathComponent().toString();
					  if(!groupIsUsed(group2remove)){
						  if(resourceGroupsDeleteButton.getText().equalsIgnoreCase(Messages.getString("PetriNet.Resources.Delete.Title"))){
							  //Delete a group
							  GroupsTreeNode nodeToDelete = (GroupsTreeNode) resourceGroupsTree.getLastSelectedPathComponent();
							  	if(nodeToDelete.getChildCount()==0){
//						  		resourceGroupsTreeModel.removeNodeFromParent(nodeToDelete);
							  		int j = getPetrinet().containsOrgunit(group2remove);
									groupsListModel.remove(j);
					                getPetrinet().getOrganizationUnits().remove(j);
									refreshGroupsTreeFromListModel();
									refreshRolesTreeFromListModel();
									refreshObjectsFromModel();
									 getEditor().setSaved(false);
							  	}else{
						  		//*******************
	/*						  		for(int i=0; i<nodeToDelete.getChildCount();i++){
							  			MutableTreeNode childOfNodeToDelete = (MutableTreeNode) nodeToDelete.getChildAt(i);
							  			if(!treeContainsNode((MutableTreeNode)rolesTopNode,childOfNodeToDelete)){
							  				for (int j = 0; j < objectsAssignedNode.getChildCount(); j++) {
							  					ObjectsTreeNode nodeToUnassign = (ObjectsTreeNode) objectsAssignedNode.getChildAt(j);
							  					String object2unassign = nodeToUnassign.toString();
							  					if(childOfNodeToDelete.toString().equals(nodeToUnassign.toString())){
							  						objectsAssignedListModel.removeElement(object2unassign);
													objectsUnassignedListModel.addElement(object2unassign);
											}
										}
						  			}
	
								}*/
//						  		resourceGroupsTreeModel.removeNodeFromParent(nodeToDelete);
//						  		resourceGroupsTree.updateUI();
//						  		resourceObjectsTree.updateUI();
							  	int j = getPetrinet().containsOrgunit(group2remove);
							  	System.out.println("Entferne: "+group2remove);
								groupsListModel.remove(j);
					            getPetrinet().getOrganizationUnits().remove(j);	
					            
						  		refreshGroupsTreeFromListModel();
								refreshRolesTreeFromListModel();
								refreshObjectsTreeFromListModel();
								refreshObjectsFromModel();
								getEditor().setSaved(false);
						  	}
						  }
					  
					  
					  // Unassign an object
					  else{
							String object2unassign = resourceGroupsTree.getLastSelectedPathComponent().toString();
							ObjectsTreeNode child = (ObjectsTreeNode) resourceGroupsTree.getLastSelectedPathComponent();
							GroupsTreeNode parent = (GroupsTreeNode) child.getParent();
							Vector assignedClasses = getPetrinet().getResourceClassesResourceIsAssignedTo(object2unassign.toString());
							Object ass;
							for (Iterator iter = assignedClasses.iterator(); iter.hasNext();){
			    				ass = iter.next();
			    				if(ass.toString().equals(parent.toString())){
			    					 getPetrinet().removeResourceMapping(parent.toString(), object2unassign);
			    				}
						  		
							}
						  
						  
						  
						  
						  
						  
	/*					  MutableTreeNode  nodeToDelete = (MutableTreeNode) resourceGroupsTree.getLastSelectedPathComponent();
						  resourceGroupsTreeModel.removeNodeFromParent(nodeToDelete);
							if(!treeContainsNode((MutableTreeNode)rolesTopNode,nodeToDelete)){
									for (int i = 0; i < objectsAssignedNode.getChildCount(); i++) {
										ObjectsTreeNode nodeToUnassign = (ObjectsTreeNode) objectsAssignedNode.getChildAt(i);
										if(nodeToDelete.toString().equals(nodeToUnassign.toString())){
											resourceObjectsTreeModel.removeNodeFromParent(nodeToUnassign);
											resourceObjectsTreeModel.insertNodeInto(nodeToUnassign, objectsUnassignedNode, objectsUnassignedNode.getChildCount());
											resourceObjectsTree.updateUI();
											resourceGroupsDeleteButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Delete"));
											resourceGroupsDeleteButton.setText(Messages.getString("PetriNet.Resources.Delete.Title"));
							
										}
									}
							}*/
					  }
						  
				  	  
				  	  
				  	  
						  if (groupsTopNode.getChildCount()==0){
							resourceGroupsDeleteButton.setEnabled(false);
							resourceGroupsEditButton.setEnabled(false);
						  }
						  refreshGroupsTreeFromListModel();
						  refreshRolesTreeFromListModel();
						  refreshObjectsTreeFromListModel();
						  refreshObjectsFromModel();
//				  	  	resourceGroupsTree.updateUI();
					  }
					  else{
						  JOptionPane.showMessageDialog(resourceRolesPanel , Messages.getString("ResourceEditor.Error.UsedResourceClass.Text"), Messages.getString("ResourceEditor.Error.UsedResourceClass.Title"),
			                        JOptionPane.ERROR_MESSAGE);
					  }
					  getEditor().setSaved(false);
				  	}catch(NullPointerException npe){
					  npe.printStackTrace();
				  }
				  
			   }
		   }
	   }
	   

	   
	   private class editResource implements ActionListener{
			
		   public void actionPerformed(ActionEvent e) {
			   
			   if(e.getSource()== resourceRolesEditButton){
				   Object message = (Object) resourceRolesTree.getLastSelectedPathComponent().toString();
				   String oldName = (String) message;
				   JOptionPane nameDialog = new JOptionPane(); 
				   String newName = JOptionPane.showInputDialog(nameDialog, Messages.getString("PetriNet.Resources.ResourceName"), message);
				   if (checkClassSyntax(newName )){
					   int j = getPetrinet().containsRole(oldName);
					   ResourceClassModel roleModel = (ResourceClassModel) getPetrinet().getRoles().get(j);
	                   roleModel.setName(newName);
	                   rolesListModel.set( j, roleModel);
	                   updateRolesInPetrinet(oldName, newName);
//					   RolesTreeNode toBeChanged = (RolesTreeNode) resourceRolesTree.getLastSelectedPathComponent();					  
//					   toBeChanged.setUserObject((Object)newName);
//					   resourceRolesTree.updateUI();
	                   refreshRolesTreeFromListModel();
	                   refreshObjectsFromModel();
	                   getEditor().setSaved(false);
				   }
			   }
			   
			   if(e.getSource()== resourceGroupsEditButton){
				   Object message = (Object) resourceGroupsTree.getLastSelectedPathComponent().toString();
				   String oldName = (String) message;
				   JOptionPane nameDialog = new JOptionPane(); 
				   String newName = JOptionPane.showInputDialog(nameDialog, Messages.getString("PetriNet.Resources.ResourceName"), message);
				   if (checkClassSyntax(newName )){
//					  GroupsTreeNode toBeChanged = (GroupsTreeNode) resourceGroupsTree.getLastSelectedPathComponent();
//					  toBeChanged.setUserObject((Object)name);
//					  resourceGroupsTree.updateUI();
					   int j = getPetrinet().containsOrgunit(oldName);
					   ResourceClassModel groupModel = (ResourceClassModel) getPetrinet().getOrganizationUnits().get(j);
	                   groupModel.setName(newName);
	                   groupsListModel.set( j, groupModel);
	                   updateGroupsInPetrinet(oldName, newName);
	                   refreshGroupsTreeFromListModel();
	                   refreshObjectsFromModel();
	                   getEditor().setSaved(false);
				   }
			   }
			   if(e.getSource()==resourceObjectsEditButton){
				   Object message = (Object) resourceObjectsTree.getLastSelectedPathComponent().toString();
				   JOptionPane nameDialog = new JOptionPane(); 
				   String name = JOptionPane.showInputDialog(nameDialog,Messages.getString("PetriNet.Resources.ResourceName"), message);
//				   ObjectsTreeNode toBeChanged = (ObjectsTreeNode) resourceObjectsTree.getLastSelectedPathComponent();
				 if (checkClassSyntax(name )){
					 	getPetrinet().replaceResourceMapping(message.toString(), name);
		                ResourceModel resourceModel = (ResourceModel) getPetrinet().getResources().get(getPetrinet().containsResource(message.toString()));
		                resourceModel.setName(name);
		                refreshGroupsTreeFromListModel();
		                refreshRolesTreeFromListModel();
		                refreshObjectsTreeFromListModel();
		                refreshObjectsFromModel();
/*					 if(toBeChanged.getParent().toString().equals((objectsUnassignedNode).toString())){
					   toBeChanged.setUserObject((Object)name);
					   resourceObjectsTree.updateUI();
				   }else{
					   for (int i=0;i < rolesTopNode.getChildCount();i++){
							  
						   RolesTreeNode parent = (RolesTreeNode) rolesTopNode.getChildAt(i);
						   for (int j=0;j<parent.getChildCount();j++){
							   MutableTreeNode  child = (MutableTreeNode) parent.getChildAt(j);
							   if(child.toString().equals(toBeChanged.toString())){
								   child.setUserObject((Object)name);
								   toBeChanged.setUserObject((Object)name);
								   resourceRolesTree.updateUI();	
								   resourceObjectsTree.updateUI();
							   }
						   }
					   }
					   for (int i=0;i < groupsTopNode.getChildCount();i++){  
						   GroupsTreeNode parent = (GroupsTreeNode) groupsTopNode.getChildAt(i);
						   for (int j=0;j<parent.getChildCount();j++){
							   MutableTreeNode  child = (MutableTreeNode) parent.getChildAt(j);
							   if(child.toString().equals(toBeChanged.toString())){
								   child.setUserObject((Object)name);
								   toBeChanged.setUserObject((Object)name);
								   resourceGroupsTree.updateUI();
								   resourceObjectsTree.updateUI();

							   }
						   }
					   }  
				   }
	*/
				 }
			   }
			   getEditor().setSaved(false);
		   }
		}

	   private boolean treeContainsNode(MutableTreeNode root, MutableTreeNode node){
		   boolean contains= false;
		   for (int i= 0; i < root.getChildCount(); i++){
			  MutableTreeNode parent = (MutableTreeNode) root.getChildAt(i);
			  for(int j = 0;j<parent.getChildCount();j++){
				  MutableTreeNode child = (MutableTreeNode) parent.getChildAt(j);
				  if(node.toString().equalsIgnoreCase(child.toString()))
					  contains = true;
				  break;
			  }
		   }return contains;
	   }
	   
	   
	   private  static String getResourceName (){
		   JOptionPane nameDialog = new JOptionPane ();
		   String name = JOptionPane.showInputDialog(nameDialog, Messages.getString("PetriNet.Resources.ResourceName"));
		   return name;
	   }
	   
/*	   class MyTreeModelListener implements TreeModelListener {
		    public void treeNodesChanged(TreeModelEvent e) {
		    }
		    public void treeNodesInserted(TreeModelEvent e) {
		    }
		    public void treeNodesRemoved(TreeModelEvent e) {
		    }		    
		    public void treeStructureChanged(TreeModelEvent e) {
		    }
		}
	   
	   class MyTreeExpansionListener implements TreeExpansionListener {	     
	        public void treeExpanded(TreeExpansionEvent e) {
	        }	       
	        public void treeCollapsed(TreeExpansionEvent e) {
	        }
	    }
*/	   
	   class MyTreeSelectionListener implements TreeSelectionListener{
		   public void valueChanged(TreeSelectionEvent e) {	

			   if (e.getSource()==resourceRolesTree){
				   DefaultMutableTreeNode node = ( DefaultMutableTreeNode)resourceRolesTree.getLastSelectedPathComponent();
				  
				   if(node.isLeaf()&& !node.getParent().equals(rolesTopNode)){
					   resourceRolesDeleteButton.setText(Messages.getString("PetriNet.Resources.Unassign.Title")); 
					   resourceRolesDeleteButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Unassign"));
					   resourceRolesEditButton.setEnabled(false);
					   resourceRolesDeleteButton.setEnabled(true);
					   
				   }else{
					  resourceRolesDeleteButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Delete"));
					  resourceRolesDeleteButton.setText(Messages.getString("PetriNet.Resources.Delete.Title"));
					  resourceRolesEditButton.setEnabled(true);
					  resourceRolesDeleteButton.setEnabled(true);
				   }
			   }
			   if (e.getSource()==resourceGroupsTree){
				   MutableTreeNode node = ( MutableTreeNode)resourceGroupsTree.getLastSelectedPathComponent();
				   if(node.isLeaf()&& !node.getParent().equals((MutableTreeNode)groupsTopNode)){
					   resourceGroupsDeleteButton.setText(Messages.getString("PetriNet.Resources.Unassign.Title")); 
					   resourceGroupsDeleteButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Unassign"));
					   resourceGroupsEditButton.setEnabled(false);
					   resourceGroupsDeleteButton.setEnabled(true);
				   }else{
			        	resourceGroupsDeleteButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Delete"));
			        	resourceGroupsDeleteButton.setText(Messages.getString("PetriNet.Resources.Delete.Title"));
			        	resourceGroupsDeleteButton.setEnabled(true);
			        	resourceGroupsEditButton.setEnabled(true);
				   }
				 } 
			  
/*			   if (e.getSource()== resourceObjectsTree){
					 MutableTreeNode node = ( MutableTreeNode)resourceObjectsTree.getLastSelectedPathComponent();
					 if(node.isLeaf()&& !node.getParent().equals((MutableTreeNode)objectsTopNode)){
						   resourceObjectsEditButton.setEnabled(true);
						   resourceObjectsDeleteButton.setEnabled(true);
					 }else{
						 	resourceObjectsEditButton.setEnabled(false);
						 	resourceObjectsDeleteButton.setEnabled(false);
					 }
			   }*/
		   }		   
	   }
	   // *********************** CLASS-SYNTAX*****************************************
	    private boolean checkClassSyntax(String str){
	        boolean nameExists = false;
	      
	        if (str.equals("")){
	            JOptionPane.showMessageDialog(this, Messages.getString("ResourceEditor.Error.EmptyResourceClass.Text"), Messages.getString("ResourceEditor.Error.EmptyResourceClass.Title"),
	                    JOptionPane.ERROR_MESSAGE);
	            return false;
	        }

	        for (Iterator iter = getPetrinet().getOrganizationUnits().iterator(); !nameExists & iter.hasNext();){
	            if (iter.next().toString().equals(str)) 
	                nameExists = true;
	        }
	        for (Iterator iter = getPetrinet().getRoles().iterator(); !nameExists & iter.hasNext();){
	            if (iter.next().toString().equals(str)) 
	                nameExists = true;
	        }

	        if (nameExists){
	            JOptionPane.showMessageDialog(this, Messages.getString("ResourceEditor.Error.DuplicateResourceClass.Text"), Messages.getString("ResourceEditor.Error.DuplicateResourceClass.Title"),
	                    JOptionPane.ERROR_MESSAGE);
	            return false;
	       

	        } return true;
	    }
	    
	    // Check if group is used by any transition
	    private boolean groupIsUsed(String groupName)
	    {
	        boolean isUsed = false;
	               
	        HashMap<String, AbstractElementModel> alltrans = new HashMap<String, AbstractElementModel>();
	        alltrans.putAll(getPetrinet().getElementContainer().getElementsByType(AbstractPetriNetModelElement.TRANS_SIMPLE_TYPE));
	        alltrans.putAll(getPetrinet().getElementContainer().getElementsByType(AbstractPetriNetModelElement.TRANS_OPERATOR_TYPE));

	        for (Iterator transIter = alltrans.values().iterator(); transIter.hasNext() & !isUsed;)
	        {
	            TransitionModel transition = (TransitionModel)(transIter.next());
	            if (transition.getToolSpecific() != null &&
	                    transition.getToolSpecific().getTransResource() != null &&
	                    transition.getToolSpecific().getTransResource().getTransOrgUnitName() != null &&
	                    transition.getToolSpecific().getTransResource().getTransOrgUnitName().equals(groupName))
	            {
	                isUsed = true;
	            }
	         }
	        
	        return isUsed;
	    }

	    // Check if role is used by any transition
	    private boolean roleIsUsed(String roleName)
	    {
	        boolean isUsed = false;
	        HashMap<String, AbstractElementModel> alltrans = new HashMap<String, AbstractElementModel>();
	        alltrans.putAll(getPetrinet().getElementContainer().getElementsByType(AbstractPetriNetModelElement.TRANS_SIMPLE_TYPE));
	        alltrans.putAll(getPetrinet().getElementContainer().getElementsByType(AbstractPetriNetModelElement.TRANS_OPERATOR_TYPE));
	        
	        for (Iterator transIter = alltrans.values().iterator(); transIter.hasNext() & !isUsed;)
	        {
	            TransitionModel transition = (TransitionModel)(transIter.next());
	            if (transition.getToolSpecific() != null &&
	                transition.getToolSpecific().getTransResource() != null &&
	                transition.getToolSpecific().getTransResource().getTransRoleName() != null &&
	                transition.getToolSpecific().getTransResource().getTransRoleName().equals(roleName))
	            {
	                isUsed = true;
	            }
	         }
	        
	        return isUsed;
	    }
	    
	    private void updateRolesInPetrinet(String oldName, String newName)
	    {
	        HashMap<String, AbstractElementModel> alltrans = new HashMap<String, AbstractElementModel>();
	        alltrans.putAll(getPetrinet().getElementContainer().getElementsByType(AbstractPetriNetModelElement.TRANS_SIMPLE_TYPE));
	        alltrans.putAll(getPetrinet().getElementContainer().getElementsByType(AbstractPetriNetModelElement.TRANS_OPERATOR_TYPE));

	        for (Iterator transIter = alltrans.values().iterator(); transIter.hasNext();)
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
	    
	    private void updateGroupsInPetrinet(String oldName, String newName)
	    {
	        HashMap<String, AbstractElementModel> alltrans = new HashMap<String, AbstractElementModel>();
	        alltrans.putAll(getPetrinet().getElementContainer().getElementsByType(AbstractPetriNetModelElement.TRANS_SIMPLE_TYPE));
	        alltrans.putAll(getPetrinet().getElementContainer().getElementsByType(AbstractPetriNetModelElement.TRANS_OPERATOR_TYPE));

	        for (Iterator transIter = alltrans.values().iterator(); transIter.hasNext();)
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
	    
//! This rather important method will refresh the resource editor view
//! if the resources have changed in the Petri-Net model
//! It is called when the resource editor gains focus    
	    private void refreshFromModel()
	    {
	    	refreshRolesFromModel();
	    	refreshGroupsFromModel();
	    	refreshObjectsFromModel();
	    }
	    
	    
	    private void refreshRolesFromModel(){ 
	    	try{
	    		rolesListModel.removeAllElements();
	    		
	    		for (int i = 0; i < getPetrinet().getRoles().size(); i++){
	    			rolesListModel.addElement((ResourceClassModel)getPetrinet().getRoles().get(i));			    	   
	    		} 
	    		refreshRolesTreeFromListModel();
	    
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	    }
	    
	    private void refreshGroupsFromModel(){ 
	    	try{
	    		groupsListModel.removeAllElements();
	    		
	    		for (int i = 0; i < getPetrinet().getOrganizationUnits().size(); i++){
	    			groupsListModel.addElement((ResourceClassModel)getPetrinet().getOrganizationUnits().get(i));			    	   
	    		} 
	    		refreshGroupsTreeFromListModel();
	    
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	    }
	    
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
	    			Vector assignedClasses = getPetrinet().getResourceClassesResourceIsAssignedTo(currentObject.toString());
	    			if (!assignedClasses.isEmpty() ){
	    				objectsUnassignedListModel.removeElement(currentObject);
	    				objectsAssignedListModel.addElement(currentObject);
	    				
	    			}

	    				
	    			Object ass;
	    			for (Iterator iter = assignedClasses.iterator(); iter.hasNext();){
	    				ass = iter.next();
	    				String currentResource = ass.toString();
	    				for(int j =0; j <  resourceRolesTreeModel.getChildCount(rolesTopNode);j++){
	    					String currentRole = resourceRolesTreeModel.getChild(rolesTopNode, j).toString();
	    					if(currentResource.equals(currentRole)){
	    						RolesTreeNode currentNode = (RolesTreeNode) resourceRolesTreeModel.getChild(rolesTopNode, j);
	    						resourceRolesTreeModel.insertNodeInto(new ObjectsTreeNode(currentObject.toString()), currentNode, currentNode.getChildCount());
	    					}
	    				}
	    				for(int j =0; j <  resourceGroupsTreeModel.getChildCount(groupsTopNode);j++){
	    					String currentGroup = resourceGroupsTreeModel.getChild(groupsTopNode, j).toString();
	    					if(currentResource.equals(currentGroup)){
	    						GroupsTreeNode currentNode = (GroupsTreeNode) resourceGroupsTreeModel.getChild(groupsTopNode, j);
	    						resourceGroupsTreeModel.insertNodeInto(new ObjectsTreeNode(currentObject.toString()), currentNode, currentNode.getChildCount());
	    					}
	    				}
	                 
	              }
	    		}

	    		refreshObjectsTreeFromListModel();
	    	}catch (Exception e){
	    		e.printStackTrace();
	    	}
	    }
	    
	    
	    private void refreshRolesTreeFromListModel(){
	    	removeAllNodesFromTreeModel(resourceRolesTreeModel,rolesTopNode);
	    	for (int i = 0; i < rolesListModel.getSize();i++){
    			resourceRolesTreeModel.insertNodeInto(new RolesTreeNode(rolesListModel.get(i).toString()), rolesTopNode, i);
    		}
    		resourceRolesTree.updateUI();
	    }
	    
	    private void refreshGroupsTreeFromListModel(){
	    	removeAllNodesFromTreeModel(resourceGroupsTreeModel,groupsTopNode);
	    	for (int i = 0; i < groupsListModel.getSize();i++){
    			resourceGroupsTreeModel.insertNodeInto(new GroupsTreeNode(groupsListModel.get(i).toString()), groupsTopNode, i);
    		}
    		resourceGroupsTree.updateUI();
	    }
	    
	    

	    private void refreshObjectsTreeFromListModel(){
	    	removeAllNodesFromTreeModel(resourceObjectsTreeModel,objectsAssignedNode);
	    	removeAllNodesFromTreeModel(resourceObjectsTreeModel,objectsUnassignedNode);
	    	for (int i = 0; i < objectsAssignedListModel.getSize();i++){
    			resourceObjectsTreeModel.insertNodeInto(new ObjectsTreeNode(objectsAssignedListModel.get(i).toString()), objectsAssignedNode, i);
    		}
	    	for (int i = 0; i < objectsUnassignedListModel.getSize();i++){
    			resourceObjectsTreeModel.insertNodeInto(new ObjectsTreeNode(objectsUnassignedListModel.get(i).toString()), objectsUnassignedNode, i);
    		}
    		resourceObjectsTree.updateUI();
	    }
	    
	    private void removeAllNodesFromTreeModel(DefaultTreeModel model,MutableTreeNode node){
	    	while(model.getChildCount(node)!=0)
	    		{
	    			MutableTreeNode node2remove = (MutableTreeNode) model.getChild(node, 0);
		    		model.removeNodeFromParent(node2remove);
	    		}
	    }
	    

}
	






//Hier JTree mit drag and drop
	

class DragTree extends JTree {

	  private Insets insets;
	  private DefaultMutableTreeNode m_parent;
	  
	  private int top = 0, bottom = 0, topRow = 0, bottomRow = 0;

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
	        System.out.println("Nothing selected - beep");
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
	          System.out.println("Not a leaf - beep");
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
	       	        	
	          System.out.println("MOVE: remove node");
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

	class TransferableTreeNode extends DefaultMutableTreeNode implements
	    Transferable {
	  final static int TREE = 0;

	  //final static int STRING = 1;

	  //final static int PLAIN_TEXT = 1;

	  final public static DataFlavor DEFAULT_MUTABLE_TREENODE_FLAVOR = new DataFlavor(
	      DefaultMutableTreeNode.class, "Default Mutable Tree Node");

	  static DataFlavor flavors = DEFAULT_MUTABLE_TREENODE_FLAVOR ;
	   //  DataFlavor.stringFlavor, DataFlavor.plainTextFlavor ;

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
	
	class DropTree extends JTree implements Autoscroll {

		  private Insets insets;
		  PetriNetModelProcessor petrinet;

		  private int top = 0, bottom = 0, topRow = 0, bottomRow = 0;

		  public DropTree(DefaultMutableTreeNode rootNode,PetriNetModelProcessor petrinet ) {
			  super( rootNode );
			  this.petrinet=petrinet;
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

		      if ((node != null) && (node instanceof TreeNode))//&& (!((TreeNode) node).isLeaf())) 
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
		            System.err.println("Rejected");
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
		        System.out.println("Can't drop on a leaf");
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
			   
		    private void addElement(TreePath path, Object element) {
		      DefaultMutableTreeNode parent = (DefaultMutableTreeNode) path
		          .getLastPathComponent();
		      DefaultMutableTreeNode node = new DefaultMutableTreeNode(element);
		      System.out.println("Added: " + node + " to " + parent);
		      DefaultTreeModel model = (DefaultTreeModel) (DropTree.this
		          .getModel());
		      System.out.println(parentContainsNode(parent,node));
		      int d =  parent.getLevel();
		      System.out.println(d);
		      if(!(d > 1))
		      		if (!parentContainsNode(parent,node)){
		    	
		      			petrinet.addResourceMapping(parent.toString(), node.toString());		
		      			model.insertNodeInto(node, parent, parent.getChildCount());
		      		}else{
		      			JOptionPane.showMessageDialog(null , Messages.getString("ResourceEditor.Error.AlreadyAssigned.Text"), Messages.getString("ResourceEditor.Error.AlreadyAssigned.Title"),
	                        JOptionPane.ERROR_MESSAGE);
		      		}
		      else{
		    	  JOptionPane.showMessageDialog(null , Messages.getString("ResourceEditor.Error.AssignError.Text"), Messages.getString("ResourceEditor.Error.AssignError.Title"),
	                        JOptionPane.ERROR_MESSAGE);
		      }
		    }
		  }
	  
}



	   
	   
