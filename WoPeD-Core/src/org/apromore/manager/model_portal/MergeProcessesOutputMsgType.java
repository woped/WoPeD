
package org.apromore.manager.model_portal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MergeProcessesOutputMsgType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MergeProcessesOutputMsgType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Result" type="{http://www.apromore.org/manager/model_portal}ResultType"/>
 *         &lt;element name="ProcessSummary" type="{http://www.apromore.org/manager/model_portal}ProcessSummaryType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MergeProcessesOutputMsgType", propOrder = {
    "result",
    "processSummary"
})
public class MergeProcessesOutputMsgType {

    @XmlElement(name = "Result", required = true)
    protected ResultType result;
    @XmlElement(name = "ProcessSummary")
    protected ProcessSummaryType processSummary;

    /**
     * Gets the value of the result property.
     * 
     * @return
     *     possible object is
     *     {@link ResultType }
     *     
     */
    public ResultType getResult() {
        return result;
    }

    /**
     * Sets the value of the result property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultType }
     *     
     */
    public void setResult(ResultType value) {
        this.result = value;
    }

    /**
     * Gets the value of the processSummary property.
     * 
     * @return
     *     possible object is
     *     {@link ProcessSummaryType }
     *     
     */
    public ProcessSummaryType getProcessSummary() {
        return processSummary;
    }

    /**
     * Sets the value of the processSummary property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcessSummaryType }
     *     
     */
    public void setProcessSummary(ProcessSummaryType value) {
        this.processSummary = value;
    }

}
