package processtotext.ws;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.servlet.ServletContext;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import gui.Initiator;
import textGenerator.TextGenerator;
import worldModel.WorldModel;

@WebService
public class ProcessToTextWebService {
	
	
	private Initiator init = new Initiator();

	@Resource
	private WebServiceContext context;

	@WebMethod(operationName = "generateTextFromProcessSpecification")
	public String generateTextFromProcessSpecification(
			@WebParam(name = "processSpecification") String processSpecification)
			throws Exception {
		File tempFile = File.createTempFile("TempXmlDat", ".pnml");
		PrintWriter writer = new PrintWriter(tempFile, "UTF-8");
		writer.println(processSpecification);
		writer.close();
		ServletContext servletContext = (ServletContext) context.getMessageContext().get(MessageContext.SERVLET_CONTEXT);
		String contextPath = servletContext.getRealPath("/");
		TextGenerator textGenerator = new TextGenerator(contextPath);
		String result = textGenerator.toText(tempFile.getAbsolutePath());
		tempFile.delete();
		return result;
	}
	
	


	@WebMethod(operationName = "generateProcessToTextSpecification")
	public InputStream generateProcessFromTextSpecification(
			@WebParam(name = "textSpecification") String processSpecification)
			throws Exception {
		
		
		WorldModel world = init.convert(processSpecification);

		InterpetWorldModel interpreter = new InterpetWorldModel();

		PNMLGenerator generator = new PNMLGenerator();
		generator.init();
		generator.createDummyPlace();
		generator.setTransition(interpreter.getTextTrans(world.getActions()));
		generator.setArc();

	
		
		
	
		return generator.after();
	}
	
}
