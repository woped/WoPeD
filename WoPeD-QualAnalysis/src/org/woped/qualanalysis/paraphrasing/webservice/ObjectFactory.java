
package org.woped.qualanalysis.paraphrasing.webservice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the processtotext.ws package.
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

    private final static QName _GenerateTextFromProcessSpecification_QNAME = new QName("http://ws.processtotext/", "generateTextFromProcessSpecification");
    private final static QName _GenerateTextFromProcessSpecificationResponse_QNAME = new QName("http://ws.processtotext/", "generateTextFromProcessSpecificationResponse");
    private final static QName _Exception_QNAME = new QName("http://ws.processtotext/", "Exception");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: processtotext.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GenerateTextFromProcessSpecification }
     * 
     */
    public GenerateTextFromProcessSpecification createGenerateTextFromProcessSpecification() {
        return new GenerateTextFromProcessSpecification();
    }

    /**
     * Create an instance of {@link GenerateTextFromProcessSpecificationResponse }
     * 
     */
    public GenerateTextFromProcessSpecificationResponse createGenerateTextFromProcessSpecificationResponse() {
        return new GenerateTextFromProcessSpecificationResponse();
    }

    /**
     * Create an instance of {@link Exception }
     * 
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerateTextFromProcessSpecification }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.processtotext/", name = "generateTextFromProcessSpecification")
    public JAXBElement<GenerateTextFromProcessSpecification> createGenerateTextFromProcessSpecification(GenerateTextFromProcessSpecification value) {
        return new JAXBElement<GenerateTextFromProcessSpecification>(_GenerateTextFromProcessSpecification_QNAME, GenerateTextFromProcessSpecification.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerateTextFromProcessSpecificationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.processtotext/", name = "generateTextFromProcessSpecificationResponse")
    public JAXBElement<GenerateTextFromProcessSpecificationResponse> createGenerateTextFromProcessSpecificationResponse(GenerateTextFromProcessSpecificationResponse value) {
        return new JAXBElement<GenerateTextFromProcessSpecificationResponse>(_GenerateTextFromProcessSpecificationResponse_QNAME, GenerateTextFromProcessSpecificationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.processtotext/", name = "Exception")
    public JAXBElement<Exception> createException(Exception value) {
        return new JAXBElement<Exception>(_Exception_QNAME, Exception.class, null, value);
    }

}
