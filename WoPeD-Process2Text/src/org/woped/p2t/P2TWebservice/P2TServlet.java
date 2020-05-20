package org.woped.p2t.P2TWebservice;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

// WebServlet value = URL to call the Webservice from
@WebServlet(value = {"/p2t"})
public class P2TServlet extends HttpServlet {

    // P2T Servlet is the main servlet class which is being uploaded to wildfly
    // with the automatically created .war file. This class calls the P2T Controller
    // class to translate a petri net to text.
    // The value for the URL can be changed with the WebServlet value above


    //Restricts the maximum of concurrently processed HTTP requests to avoid DOS
    public static final int MAX_CONCURRENT_HTTP_REQUESTS = 6;
    private int serviceCounter = 0;

    // Call P2TController's generateText Method
    public String createText (String text) {
        P2TController control = new P2TController();
        String contextPath = getServletContext().getRealPath("/WEB-INF/classes");
        return control.generateText(contextPath, text);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException{
        // doPost is automatically called by the main program when calling the Webservice
        // If you want to change something about web service, you probably gonna do it in this method

        PrintWriter writer = response.getWriter();

        P2TController controller = null;
        if (numServices()<MAX_CONCURRENT_HTTP_REQUESTS){
            enteringServiceMethod();
            try {

                response.setContentType("text/html");
                response.setCharacterEncoding("UTF-8");
                // get request, create natural language and put it into the HTML Body
                String text= getBody(request);
                writer.append(createText(text));
            } catch(IOException e){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writer.append(e.getMessage());
            } catch(Exception e){
               /* some unexspected Exception
                  The integrity of the initialized tools might not be given anymore
                  -> reset the Instances accordingly, next request will trigger a reload*/
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            finally
            {
                leavingServiceMethod();
            }
        }else{
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
    }

    //simple Semaphor implementation for http requests
    protected synchronized void enteringServiceMethod() {
        serviceCounter++;
    }
    protected synchronized void leavingServiceMethod() {
        serviceCounter--;
    }
    protected synchronized int numServices() {
        return serviceCounter;
    }

    //extract the post argument from request
    public static String getBody(HttpServletRequest request) throws IOException {

        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }
}