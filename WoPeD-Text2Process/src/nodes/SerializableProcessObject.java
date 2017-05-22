package nodes;

import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import processing.ProcessUtils;


public abstract class SerializableProcessObject implements Cloneable {

     /** Holds the properties of the ProcessObject */
    private HashMap<String, String> properties = new HashMap<String, String>();

    public void setProperty( String key, String value ) {
        if ( value == null )
            value = "";
        this.properties.put( key, value );
    }

    public void removeProperty(String key) {
        properties.remove(key);
    }

    /**
     * Returns a property.
     * @param key
     * @return
     */
    public String getProperty(String key) {
        return properties.get(key);
    }

    /**
     * Returns all property keys.
     * @return
     */
    public Set<String> getPropertyKeys() {
        return properties.keySet();
    }

    /**
     *
     * @param xmlDoc
     * @return
     */
    public Element getSerialization(Document xmlDoc) {
        Element tagNode = xmlDoc.createElement(getXmlTag());
        ProcessUtils.writeProperties(xmlDoc, tagNode, properties);
        return tagNode;
    }

    @Override
	public Object clone() {
        try {
            SerializableProcessObject copy = (SerializableProcessObject) super.clone();
            copy.properties = (HashMap<String, String>) this.properties.clone();
            return copy;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(SerializableProcessObject.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    

    protected abstract String getXmlTag();



}
