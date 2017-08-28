package org.woped.file.apromore.tree;

import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import org.woped.file.apromore.AbstractApromoreFrame;
import org.woped.gui.translations.Messages;

public class ApromoreFolderTree extends JTree {

	private JTree tree;
	public final static String TOP_NODE_NAME = Messages.getString("Apromore.UI.Node.Root.Title");
	private DefaultMutableTreeNode root = new DefaultMutableTreeNode(
			TOP_NODE_NAME);

	public ApromoreFolderTree(final AbstractApromoreFrame parent) {

		tree = new JTree(root);

		tree.getSelectionModel().addTreeSelectionListener(
			new TreeSelectionListener() {

				@Override
				public void valueChanged(TreeSelectionEvent e) {

					String uncuttedPath = e.getPath().toString();
					uncuttedPath = uncuttedPath.replace("[", "");
					uncuttedPath = uncuttedPath.replace("]", "");
					String[] path = uncuttedPath.split(", ");

					String[] singleFolder = new String[1];
					singleFolder[0] = path[path.length - 1];

					parent.setListFilter(singleFolder);

				}
			}
		);
	}

	public JTree getTree() {
		return tree;
	}

	public void setSubFolders1(ArrayList<String> subFolders) {

		final DefaultTreeModel model = (DefaultTreeModel) tree.getModel();

		DefaultMutableTreeNode root1 = (DefaultMutableTreeNode) model.getRoot();

		for (String str : subFolders) {
			root1.add(new DefaultMutableTreeNode(str));
		}
		tree = new JTree(root1);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				model.reload();
			}
		});
	}

	public void setSubFolders(ArrayList<String> subFolders) {

		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

		if (root.getChildCount() > 0) {
			while (root.getChildCount() > 0) {
				model.removeNodeFromParent((MutableTreeNode) model.getChild(
						root, 0));
			}
		}
		for (String str : subFolders) {
			model.insertNodeInto(new DefaultMutableTreeNode(str), root,
					root.getChildCount());
		}
		model.reload();
	}
}