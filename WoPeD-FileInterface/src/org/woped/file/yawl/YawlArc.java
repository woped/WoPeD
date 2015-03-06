/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.woped.file.yawl;

/**
 *
 * @author Chris
 */
public class YawlArc {
    
    private String flowFrom = "";

    private String flowTo = "";
    
    /**
     * Get the value of flowFrom
     *
     * @return the value of flowFrom
     */
    public String getFlowFrom() {
        return flowFrom;
    }

    /**
     * Set the value of flowFrom
     *
     * @param flowFrom new value of flowFrom
     */
    public void setFlowFrom(String flowFrom) {
        this.flowFrom = flowFrom;
    }

    /**
     * Get the value of flowTo
     *
     * @return the value of flowTo
     */
    public String getFlowTo() {
        return flowTo;
    }

    /**
     * Set the value of flowTo
     *
     * @param flowTo new value of flowTo
     */
    public void setFlowTo(String flowTo) {
        this.flowTo = flowTo;
    }

    public YawlArc() {
    }

    public YawlArc(String from, String to) {
        this.flowFrom = from;
        this.flowTo = to;
    }
}
