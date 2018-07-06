//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package processtotext.ws;

import java.io.File;
import java.io.PrintWriter;
import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.servlet.ServletContext;
import javax.xml.ws.WebServiceContext;
import textGenerator.TextGenerator;

@WebService
public class ProcessToTextWebService {
    @Resource
    private WebServiceContext context;

    public ProcessToTextWebService() {
    }

    @WebMethod(
            operationName = "generateTextFromProcessSpecification"
    )
    public String generateTextFromProcessSpecification(@WebParam(name = "processSpecification") String processSpecification) throws Exception {
        File tempFile = File.createTempFile("TempXmlDat", ".pnml");
        PrintWriter writer = new PrintWriter(tempFile, "UTF-8");
        writer.println(processSpecification);
        writer.close();
        ServletContext servletContext = (ServletContext)this.context.getMessageContext().get("javax.xml.ws.servlet.context");
        String contextPath = servletContext.getRealPath("/");
        TextGenerator textGenerator = new TextGenerator(contextPath);
        String result = textGenerator.toText(tempFile.getAbsolutePath());
        tempFile.delete();
        return result;
    }
}
