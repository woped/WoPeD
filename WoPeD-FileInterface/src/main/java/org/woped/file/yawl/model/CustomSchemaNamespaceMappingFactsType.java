//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference
// Implementation, v2.2.4
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2013.08.18 at 08:57:29 AM CEST
//

package org.woped.file.yawl.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Java class for CustomSchemaNamespaceMappingFactsType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="CustomSchemaNamespaceMappingFactsType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.yawlfoundation.org/yawlschema}CustomSchemaNamespaceMappingType">
 *       &lt;sequence>
 *         &lt;element name="expandsTo" type="{http://www.yawlfoundation.org/yawlschema}URIType"/>
 *         &lt;element name="definedAt" type="{http://www.yawlfoundation.org/yawlschema}URIType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "CustomSchemaNamespaceMappingFactsType",
    propOrder = {"expandsTo", "definedAt"})
public class CustomSchemaNamespaceMappingFactsType extends CustomSchemaNamespaceMappingType {

  @XmlElement(required = true)
  protected String expandsTo;

  @XmlElement(required = true)
  protected String definedAt;

  /**
   * Gets the value of the expandsTo property.
   *
   * @return possible object is {@link String }
   */
  public String getExpandsTo() {
    return expandsTo;
  }

  /**
   * Sets the value of the expandsTo property.
   *
   * @param value allowed object is {@link String }
   */
  public void setExpandsTo(String value) {
    this.expandsTo = value;
  }

  /**
   * Gets the value of the definedAt property.
   *
   * @return possible object is {@link String }
   */
  public String getDefinedAt() {
    return definedAt;
  }

  /**
   * Sets the value of the definedAt property.
   *
   * @param value allowed object is {@link String }
   */
  public void setDefinedAt(String value) {
    this.definedAt = value;
  }
}
