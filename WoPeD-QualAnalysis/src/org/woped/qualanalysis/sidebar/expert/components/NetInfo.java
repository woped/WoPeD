package org.woped.qualanalysis.sidebar.expert.components;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings({"serial"})
public class NetInfo extends DefaultMutableTreeNode {

	public NetInfo(String myNetInfo) {
		super(myNetInfo);
	}

	// ! This method must be implemented
	// ! by all objects derived from NetInfo
	// ! It is called when the selection changes
	// ! to select the corresponding items in the
	// ! graph view
	// ! The returned objects are the graph model elements
	// ! to be selected
	public Object[] getReferencedElements() {
		return new Object[0];
	}

	// ! Information state, sorted by severity
	public static final int InfoStateInfo = 0x00;
	public static final int InfoStateOK = 0x01;
	public static final int InfoStateERROR = 0x02;

	// ! Return the information state for this object
	// ! @return returns the information type for this object
	public int getInfoState() {

		// Presume that we should define information only
		int result = InfoStateInfo;
		// Iterate through all children
		// and determine our own state through the state of our children

		for (Enumeration<?> e = children(); e.hasMoreElements();) {
			Object current = e.nextElement();
			NetInfo myInfo = null;

			try {
				myInfo = (NetInfo) current;
			} catch (Exception exception) {

			}

			if (myInfo != null) {
				int childState = myInfo.getInfoState();

				// If child's state is more severe than ours
				// we adopt the state of our child
				if (result < childState) {
					result = childState;
				}
			}
		}
		return result;
	}
}
