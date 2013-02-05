
package org.apromore.manager.model_portal;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for WriteAnnotationInputMsgType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="WriteAnnotationInputMsgType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Native" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *       &lt;/sequence>
 *       &lt;attribute name="EditSessionCode" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="AnnotationName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="IsNew" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="ProcessId" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="Version" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="NativeType" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WriteAnnotationInputMsgType", propOrder = {
    "_native"
})
public class WriteAnnotationInputMsgType {

    @XmlElement(name = "Native", required = true)
    @XmlMimeType("application/octet-stream")
    protected DataHandler _native;
    @XmlAttribute(name = "EditSessionCode")
    protected Integer editSessionCode;
    @XmlAttribute(name = "AnnotationName")
    protected String annotationName;
    @XmlAttribute(name = "IsNew")
    protected Boolean isNew;
    @XmlAttribute(name = "ProcessId")
    protected Integer processId;
    @XmlAttribute(name = "Version")
    protected String version;
    @XmlAttribute(name = "NativeType")
    protected String nativeType;

    /**
     * Gets the value of the native property.
     * 
     * @return
     *     possible object is
     *     {@link DataHandler }
     *     
     */
    public DataHandler getNative() {
        return _native;
    }

    /**
     * Sets the value of the native property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataHandler }
     *     
     */
    public void setNative(DataHandler value) {
        this._native = value;
    }

    /**
     * Gets the value of the editSessionCode property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getEditSessionCode() {
        return editSessionCode;
    }

    /**
     * Sets the value of the editSessionCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setEditSessionCode(Integer value) {
        this.editSessionCode = value;
    }

    /**
     * Gets the value of the annotationName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnnotationName() {
        return annotationName;
    }

    /**
     * Sets the value of the annotationName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnnotationName(String value) {
        this.annotationName = value;
    }

    /**
     * Gets the value of the isNew property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsNew() {
        return isNew;
    }

    /**
     * Sets the value of the isNew property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsNew(Boolean value) {
        this.isNew = value;
    }

    /**
     * Gets the value of the processId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getProcessId() {
        return processId;
    }

    /**
     * Sets the value of the processId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setProcessId(Integer value) {
        this.processId = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the nativeType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNativeType() {
        return nativeType;
    }

    /**
     * Sets the value of the nativeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNativeType(String value) {
        this.nativeType = value;
    }

}
