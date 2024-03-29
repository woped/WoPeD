//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference
// Implementation, v2.2.4
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2013.08.18 at 08:57:29 AM CEST
//

package org.woped.file.yawl.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * Java class for TimerIntervalType.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <p>
 *
 * <pre>
 * &lt;simpleType name="TimerIntervalType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="YEAR"/>
 *     &lt;enumeration value="MONTH"/>
 *     &lt;enumeration value="WEEK"/>
 *     &lt;enumeration value="DAY"/>
 *     &lt;enumeration value="HOUR"/>
 *     &lt;enumeration value="MIN"/>
 *     &lt;enumeration value="SEC"/>
 *     &lt;enumeration value="MSEC"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "TimerIntervalType")
@XmlEnum
public enum TimerIntervalType {
  YEAR,
  MONTH,
  WEEK,
  DAY,
  HOUR,
  MIN,
  SEC,
  MSEC;

  public String value() {
    return name();
  }

  public static TimerIntervalType fromValue(String v) {
    return valueOf(v);
  }
}
