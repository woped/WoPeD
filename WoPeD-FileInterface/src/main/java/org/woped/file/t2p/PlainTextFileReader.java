package org.woped.file.t2p;

import java.awt.*;
import java.io.*;
import java.util.Optional;
import java.util.Scanner;
import javax.swing.*;
import org.apache.poi.OldFileFormatException;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.HWPFOldDocument;
import org.apache.poi.hwpf.extractor.Word6Extractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.utilities.FileFilterImpl;
import org.woped.core.utilities.Platform;

public class PlainTextFileReader implements FileReader {
  private JFileChooser chooser = new JFileChooser();
  private StringBuilder sb;

  /**
   * @latestEditors <a href="mailto:bielefeld.moritz@student.dhbw-karlsruhe.de">Moritz
   *     Bielefeld</a>, <a href="mailto:geist.semjon@student.dhbw-karlsruhe.de">Semjon Geist</a>, <a
   *     href="mailto:kanzler.benjamin@student.dhbw-karlsruhe.de">Benjamin Kanzler</a>
   * @return
   */
  @Override
  public String read() {
    sb = new StringBuilder();
    int res = 0;
    File file;
    String lastdir = "";
    String abspath;

    if (ConfigurationManager.getConfiguration().isHomedirSet()) {
      lastdir = ConfigurationManager.getConfiguration().getHomedir();
    }

    if (ConfigurationManager.getConfiguration().isCurrentWorkingdirSet()) {
      lastdir = ConfigurationManager.getConfiguration().getCurrentWorkingdir();
    }

    if (!Platform.isMac()) {

      // Configuring the possible file extensions for the upload
      chooser.setCurrentDirectory(new File(lastdir));
      chooser.addChoosableFileFilter(
          new FileFilterImpl(FileFilterImpl.SAMPLEFilter, "ASCII text", "txt"));
      chooser.addChoosableFileFilter(
          new FileFilterImpl(FileFilterImpl.SAMPLEFilter, "Word", "doc"));
      chooser.addChoosableFileFilter(
          new FileFilterImpl(FileFilterImpl.SAMPLEFilter, "Word (2007 - 365)", "docx"));

      // Open the prepared file input dialog
      res = chooser.showOpenDialog(null);

      if (res == JFileChooser.APPROVE_OPTION) {
        file = chooser.getSelectedFile();
        abspath = file.getAbsolutePath();

        String fileType = getExtensionByStringHandling(file).get();

        switch (fileType) {
          case "docx":
          case "doc":
            sb = readTextFromWordDocumentX(file, sb);
            break;
          case "txt":
            sb = readTxtFile(file, sb);
            break;
          default:
            // JFrame errorDialog: file extension not known
            break;
        }
      } else {
        return null;
      }
    } else {
      // Mac part, let's do the same with the awt.FileDialog
      JFrame frame = null;
      String fn;
      FileDialog fileDialog;

      fileDialog = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
      fileDialog.setDirectory(lastdir);

      // Set fileFilter to txt files here
      fileDialog.setFilenameFilter(
          new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
              return name.endsWith(".txt");
            }
          });

      fileDialog.setVisible(true);

      fn = fileDialog.getFile();
      if (fn != null) {
        abspath = fileDialog.getDirectory() + fn;
      } else {
        return null;
      }

      file = new File(abspath);

      sb = readTxtFile(file, sb);
    }

    // Set the new working dir to the current files location
    ConfigurationManager.getConfiguration()
        .setCurrentWorkingdir(abspath.substring(0, abspath.lastIndexOf(File.separator)));

    // return the result of the StringBuilding
    return sb.toString();
  }

  /**
   * This method reads the plain text information of a txt-file.
   *
   * @latestEditors <a href="mailto:bielefeld.moritz@student.dhbw-karlsruhe.de">Moritz
   *     Bielefeld</a>, <a href="mailto:geist.semjon@student.dhbw-karlsruhe.de">Semjon Geist</a>, <a
   *     href="mailto:kanzler.benjamin@student.dhbw-karlsruhe.de">Benjamin Kanzler</a>
   * @author <a href="mailto:bielefeld.moritz@student.dhbw-karlsruhe.de">Moritz Bielefeld</a>, <a
   *     href="mailto:geist.semjon@student.dhbw-karlsruhe.de">Semjon Geist</a>, <a
   *     href="mailto:kanzler.benjamin@student.dhbw-karlsruhe.de">Benjamin Kanzler</a>
   * @param file
   * @return
   */
  public StringBuilder readTxtFile(File file, StringBuilder sb) {
    // Reading the information from the choosen file
    try {
      Scanner input = new Scanner(file);
      while (input.hasNext()) {
        sb.append(input.nextLine());
        sb.append('\n');
      }
      input.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    }
    return sb;
  }

  /**
   * @latestEditors <a href="mailto:bielefeld.moritz@student.dhbw-karlsruhe.de">Moritz
   *     Bielefeld</a>, <a href="mailto:geist.semjon@student.dhbw-karlsruhe.de">Semjon Geist</a>, <a
   *     href="mailto:kanzler.benjamin@student.dhbw-karlsruhe.de">Benjamin Kanzler</a>
   * @author <a href="mailto:bielefeld.moritz@student.dhbw-karlsruhe.de">Moritz Bielefeld</a>, <a
   *     href="mailto:geist.semjon@student.dhbw-karlsruhe.de">Semjon Geist</a>, <a
   *     href="mailto:kanzler.benjamin@student.dhbw-karlsruhe.de">Benjamin Kanzler</a>
   * @param file
   * @return
   */
  private Optional<String> getExtensionByStringHandling(File file) {
    return Optional.ofNullable(file.getAbsolutePath())
        .filter(f -> f.contains("."))
        .map(f -> f.substring(file.getAbsolutePath().lastIndexOf(".") + 1));
  }

  /**
   * Method to read text from doc and docx files.
   *
   * <p>Known bug: DOCX files incompatible with current xmlbeans API: java.lang.NoSuchMethodError:
   * 'org.apache.xmlbeans.XmlOptions org.apache.xmlbeans.XmlOptions.setEntityExpansionLimit(int)'
   *
   * @latestEditors <a href="mailto:bielefeld.moritz@student.dhbw-karlsruhe.de">Moritz
   *     Bielefeld</a>, <a href="mailto:geist.semjon@student.dhbw-karlsruhe.de">Semjon Geist</a>, <a
   *     href="mailto:kanzler.benjamin@student.dhbw-karlsruhe.de">Benjamin Kanzler</a>
   * @author <a href="mailto:bielefeld.moritz@student.dhbw-karlsruhe.de">Moritz Bielefeld</a>, <a
   *     href="mailto:geist.semjon@student.dhbw-karlsruhe.de">Semjon Geist</a>, <a
   *     href="mailto:kanzler.benjamin@student.dhbw-karlsruhe.de">Benjamin Kanzler</a>
   * @param file
   * @return
   */
  public StringBuilder readTextFromWordDocumentX(File file, StringBuilder sb) {
    try {
      FileInputStream fis = new FileInputStream(file.getAbsoluteFile());
      XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
      XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
      sb.append(extractor.getText());
    } catch (OLE2NotOfficeXmlFileException e) {
      readTextFromWordDocument(file, sb);
    } catch (InvalidFormatException e) {
      e.printStackTrace();
      return null;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
    return sb;
  }

  private StringBuilder readTextFromWordDocument(File file, StringBuilder sb) {
    try {
      FileInputStream fis = new FileInputStream(file.getAbsolutePath());
      HWPFDocument doc = new HWPFDocument(fis);
      WordExtractor extractor = new WordExtractor(doc);
      sb.append(extractor.getText());
    } catch (OldFileFormatException e) {
      readTextFromWordDocumentOLD(file, sb);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
    return sb;
  }

  private StringBuilder readTextFromWordDocumentOLD(File file, StringBuilder sb) {
    try {
      FileInputStream fis = new FileInputStream(file.getAbsolutePath());
      POIFSFileSystem fs = new POIFSFileSystem(fis);
      HWPFOldDocument doc = new HWPFOldDocument(fs);
      Word6Extractor extractor = new Word6Extractor(doc);
      sb.append(extractor.getText());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
    return sb;
  }
}
