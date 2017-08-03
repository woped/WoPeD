/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package tools;

import java.util.LinkedList;
import java.util.List;

import nodes.ProcessNode;
import nodes.ProcessObject;

/**
 *
 * This class capsulates restrictions for the ReferencePropertyEditor
 *
 */
public class ReferenceChooserRestriction {

    private List<String> stereotypeRestrictions = new LinkedList<String>();
    private List<Class> nodeTypeRestrictions = new LinkedList<Class>();
    private boolean returnNameOnly = false;
    private boolean multipleSelectionAllowed = false;

    /**
     * @param stereotypeRestrictions
     * @param nodeTypeRestrictions
     * @param returnName Defines whether the name of the selected element or the id should be returned.
     */
    public ReferenceChooserRestriction(List<String> stereotypeRestrictions, List<Class> nodeTypeRestrictions) {
        if (stereotypeRestrictions != null) {
            this.stereotypeRestrictions.addAll(stereotypeRestrictions);
        }
        if (nodeTypeRestrictions != null) {
            this.nodeTypeRestrictions.addAll(nodeTypeRestrictions);
        }        
    }

    public boolean isMultipleSelectionAllowed() {
        return multipleSelectionAllowed;
    }

    public void setMultipleSelectionAllowed(boolean multipleSelectionAllowed) {
        this.multipleSelectionAllowed = multipleSelectionAllowed;
    }

    public boolean isReturnNameOnly() {
        return returnNameOnly;
    }

    public void setReturnNameOnly(boolean returnNameOnly) {
        this.returnNameOnly = returnNameOnly;
    }

    /**
     * Returns true if the node is restricted.
     * @param object
     * @return
     */
    public boolean isRestricted(ProcessObject object) {
        // Check if list of restrictions is empty, in this case restrict to all
        if (nodeTypeRestrictions.size() == 0 && stereotypeRestrictions.size() == 0) {
            return true;
        }
        // Check types
        for (Class c : nodeTypeRestrictions) {
            if (c.isAssignableFrom(object.getClass())) {
                return true;
            }
        }
        // Check stereotypes
        if (object instanceof ProcessNode) {
            for (String s : stereotypeRestrictions) {
                if (((ProcessNode)object).getStereotype().equalsIgnoreCase(s)) {
                    return true;
                }
            }
        }
        // Not contained in list of restrictions
        return false;
    }

    public List<Class> getNodeTypeRestrictions() {
        return nodeTypeRestrictions;
    }

    public List<String> getStereotypeRestrictions() {
        return stereotypeRestrictions;
    }
}
