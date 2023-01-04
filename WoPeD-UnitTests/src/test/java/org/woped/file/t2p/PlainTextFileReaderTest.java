package org.woped.file.t2p;

import static org.junit.Assert.*;

import java.io.File;
import javax.swing.*;
import org.junit.Test;

public class PlainTextFileReaderTest {

  @Test
  public void read() {
    PlainTextFileReader ptfr = new PlainTextFileReader();
    StringBuilder sb;
    JFileChooser chooser = new JFileChooser();
    String usrPath = System.getProperty("user.dir");
    String[] filetype = {"txt", "doc", "docx"}; //

    for (String k : filetype) {
      sb = new StringBuilder();
      File file = new File(usrPath + "/tests/org/woped/file/t2p/test." + k);
      switch (k) {
        case "doc":
        case "docx":
          sb = ptfr.readTextFromWordDocumentX(file, sb);
          break;
        case "txt":
          sb = ptfr.readTxtFile(file, sb);
          break;
        default:
          // JFrame errorDialog: file extension not known
          break;
      }
      System.out.println(sb + k);
      assertTrue(sb.toString().contains("A manager is managing a project"));
    }
  }
}
