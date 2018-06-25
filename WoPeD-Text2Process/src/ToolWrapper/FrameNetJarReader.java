package ToolWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class FrameNetJarReader {
    public void getFrame() throws IOException {

       //Tryout for Reading Compressed Framenet Files
        Reader fileReader = null;
        InputStream is = this.getClass().getResourceAsStream("/fndata-1.5/schema/frame.xsd");
        if (null != is) {
            fileReader = new InputStreamReader(is);
            char [] x= new char [500];
            int s = fileReader.read(x);
            System.out.println(new String(x));
        }
    }
}
