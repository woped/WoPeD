
package org.woped.qualanalysis.paraphrasing.webservice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.woped.qualanalysis.paraphrasing.webservice package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ToText_QNAME = new QName("http://server/", "toText");
    private final static QName _ToTextResponse_QNAME = new QName("http://server/", "toTextResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.woped.qualanalysis.paraphrasing.webservice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ToTextResponse }
     * 
     */
    public ToTextResponse createToTextResponse() {
        return new ToTextResponse();
    }

    /**
     * Create an instance of {@link ToText }
     * 
     */
    public ToText createToText() {
        return new ToText();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ToText }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server/", name = "toText")
    public JAXBElement<ToText> createToText(ToText value) {
        return new JAXBElement<ToText>(_ToText_QNAME, ToText.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ToTextResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server/", name = "toTextResponse")
    public JAXBElement<ToTextResponse> createToTextResponse(ToTextResponse value) {
        return new JAXBElement<ToTextResponse>(_ToTextResponse_QNAME, ToTextResponse.class, null, value);
    }

}
