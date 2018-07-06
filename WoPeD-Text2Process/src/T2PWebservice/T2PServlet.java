package T2PWebservice;
import WorldModelToPetrinet.PetrinetGenerationException;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class T2PServlet extends HttpServlet {

    //Restricts the maximum of concurrently processed HTTP requests to avoid DOS
    public static final int MAX_CONCURRENT_HTTP_REQUESTS=6;
    private int serviceCounter = 0;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException,IOException{

        PrintWriter writer = response.getWriter();

        T2PController controller = null;
        if (numServices()<MAX_CONCURRENT_HTTP_REQUESTS){
            enteringServiceMethod();
            try {

                response.setContentType("text/html");
                response.setCharacterEncoding("UTF-8");
                String text=getBody(request);
                controller = new T2PController(text);
                String pnml= controller.generatePetrinetFromText();
                writer.append(pnml);
            } catch (InvalidInputException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writer.append(e.getMessage());
            }catch (PetrinetGenerationException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                writer.append(e.getMessage());
            }catch(IOException e){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writer.append(e.getMessage());
            }catch(Exception e){
               /* some unexspected Exception
                  The integrity of the initialized tools might not be given anymore
                  -> reset the Instances accordingly, next request will trigger a reload*/
                controller.resetNLPTools();
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