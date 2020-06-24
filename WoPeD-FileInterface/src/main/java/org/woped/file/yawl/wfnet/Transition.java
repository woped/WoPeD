/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.woped.file.yawl.wfnet;

/**
 *
 * @author Chris
 */
public class Transition extends WfNetNode {

    private JoinSplitType joinType = JoinSplitType.None;
    private JoinSplitType splitType = JoinSplitType.None;
    
    private String wopedOrientation = "";

    
    
    /**
     * Constructor with package-level access
     * @param id 
     */
    Transition(String id, WfNet parent) {
        super(id, parent);
    }
    
    public JoinSplitType getJoinType() {
        return joinType;
    }

    public void setJoinType(JoinSplitType joinSplitType) {
        this.joinType = joinSplitType;
    }

    public JoinSplitType getSplitType() {
        return splitType;
    }

    public void setSplitType(JoinSplitType splitType) {
        this.splitType = splitType;
    }
    
    public boolean hasJoinOrSplit() {
        return (this.joinType != JoinSplitType.None) ||
                (this.splitType != JoinSplitType.None);
    }
    
    public enum JoinSplitType {
        None,
        And,
        Or,
        Xor
    }
    
    public void copyAttributesTo(Transition node,
            boolean copy_arcs) {
        super.copyAttributesTo(node, copy_arcs);
        
        node.joinType = this.joinType;
        node.splitType = this.splitType;
    }
    
    
    public static String getWoPedOperatorCode(Transition.JoinSplitType join,
            Transition.JoinSplitType split) {

        if (join == Transition.JoinSplitType.None && split == Transition.JoinSplitType.And) {
            return "101";
        } else if (join == Transition.JoinSplitType.None && split == Transition.JoinSplitType.Xor) {
            return "104";
        } else if (join == Transition.JoinSplitType.And && split == Transition.JoinSplitType.None) {
            return "102";
        } else if (join == Transition.JoinSplitType.And && split == Transition.JoinSplitType.And) {
            return "107";
        }  else if (join == Transition.JoinSplitType.And && split == Transition.JoinSplitType.Xor) {
            return "108";
        }  else if (join == Transition.JoinSplitType.Xor && split == Transition.JoinSplitType.None) {
            return "105";
        }  else if (join == Transition.JoinSplitType.Xor && split == Transition.JoinSplitType.And) {
            return "109";
        }  else if (join == Transition.JoinSplitType.Xor && split == Transition.JoinSplitType.Xor) {
            return "106";
        }

        return "0";
    }
    
    public String getWopedOrientation() {
        return wopedOrientation;
    }

    public void setWopedOrientation(String wopedOrientation) {
        this.wopedOrientation = wopedOrientation;
    }
}
