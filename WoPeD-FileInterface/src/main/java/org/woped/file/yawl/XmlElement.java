/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.woped.file.yawl;

import java.util.ArrayList;

/**
 *
 * @author Chris
 */
public class XmlElement {
    private String name;
    private String contents;
    /// <summary>
    /// If true, this XML-Element has the form <name></name>,
    /// if false it has the form <name />
    /// </summary>
    private boolean hasClosingTag;
    private ArrayList<XmlAttribute> attributes = new ArrayList<XmlAttribute>();
    private ArrayList<XmlElement> childElements = new ArrayList<XmlElement>();

    public XmlElement(String name) {
        this(name, true);
    }

    public XmlElement(String name, String contents) {
        this(name, true);
        this.contents = contents;
    }
    
    public XmlElement(String name, boolean hasClosingTag) {
        this.name = name;
        this.hasClosingTag = hasClosingTag;

    }
    
    

    public void addAttribute(XmlAttribute attr) {
        this.attributes.add(attr);
    }

    public void addChildElement(XmlElement element) {
        this.childElements.add(element);
    }
    
    public XmlElement addChild(String name, boolean hasClosingTag)
    {
        addChildElement(new XmlElement(name, hasClosingTag));
        return this;
    }
    
    public XmlElement addChild(String name)
    {
        addChild(name, true);
        return this;
    }
    
    public XmlElement addAndGetChild(String name, boolean hasClosingTag)
    {
        XmlElement child = new XmlElement(name, hasClosingTag);
        this.addChildElement(child);
        return child;
    }
    
    public XmlElement addAndGetChild(String name)
    {
        return addAndGetChild(name, true);
    }
    
    public XmlElement addAttribute(String name, String value)
    {
        this.addAttribute(new XmlAttribute(name, value));
        return this;
    }
    
    public XmlElement addAttribute(String name, int value)
    {
        this.addAttribute(new XmlAttribute(name, 
                Integer.toString(value)));
        return this;
    }
    

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("\n<").append(name);
        for (int iAttr = 0; iAttr < attributes.size(); iAttr++) {
            XmlAttribute attr = attributes.get(iAttr);
            ret.append(" ").append(attr.name).append("=\"")
                    .append(attr.value).append("\"");
        }

        // If this is an element without closing tag, close the
        // first tag an return
        if (!this.hasClosingTag) {
            ret.append(" />");
            return ret.toString();
        }

        ret.append(">");
        
        if (this.contents != null) {
            ret.append(this.contents);
        }
        

        for (int iChild = 0; iChild < childElements.size(); iChild++) {
            XmlElement element = childElements.get(iChild);
            ret.append(element.toString());
        }

        ret.append("</").append(name).append(">");

        return ret.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContents() {
        return contents;
    }

    public XmlElement setContents(String contents) {
        this.contents = contents;
        return this;
    }

    static public class XmlAttribute {

        public String name;
        public String value;

        public XmlAttribute(String _name, String _value) {
            this.name = _name;
            this.value = _value;
        }
    }
}
