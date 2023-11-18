package org.woped.file.t2p;

import static org.junit.Assert.*;

import java.io.File;
import org.junit.Ignore;
import org.junit.Test;

public class PlainTextFileReaderTest {

  public void read(String filetype) {
    PlainTextFileReader ptfr = new PlainTextFileReader();
    StringBuilder sb;
    sb = new StringBuilder();
    File file = new File(getClass().getResource("/org/woped/file/t2p/test." + filetype).getFile());
    switch (filetype) {
      case "doc":
      case "docx":
        sb = ptfr.readTextFromWordDocumentX(file, sb);
        break;
      case "txt":
        sb = ptfr.readTxtFile(file, sb);
        break;
      default:
        break;
    }
    assertTrue(sb.toString().contains("A manager is managing a project"));
  }

  @Test
  public void readTxt() {
    read("txt");
  }

  @Test
  public void readDoc() {
    read("doc");
  }

  @Test
  @Ignore
  /**
   * Ignored because incompatible with xmlbeans API. Can be re-enabled when fixed
   * java.lang.NoSuchMethodError: 'org.apache.xmlbeans.XmlOptions
   * org.apache.xmlbeans.XmlOptions.setEntityExpansionLimit(int)'
   */
  public void readDocx() {
    read("docx");
  }
}
