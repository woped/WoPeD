package org.woped.file.t2p;


import com.aspose.slides.Presentation;
import com.aspose.slides.SaveFormat;
import com.aspose.words.Document;
import com.aspose.words.PdfCompliance;
import com.aspose.words.PdfSaveOptions;
import org.apache.poi.OldFileFormatException;
import org.apache.poi.hwpf.extractor.Word6Extractor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.hwpf.HWPFOldDocument;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.apache.tika.parser.pdf.PDFParserConfig;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.utilities.FileFilterImpl;
import org.woped.core.utilities.Platform;

import java.io.IOException;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import javax.swing.*;

import org.apache.commons.io.FilenameUtils;


public class PlainTextFileReader implements FileReader {
    private JFileChooser chooser = new JFileChooser();
    private StringBuilder sb;

    /**
     * @return
     * @latestEditors <a href="mailto:bielefeld.moritz@student.dhbw-karlsruhe.de">Moritz Bielefeld</a>, <a href="mailto:geist.semjon@student.dhbw-karlsruhe.de">Semjon Geist</a>, <a href="mailto:kanzler.benjamin@student.dhbw-karlsruhe.de">Benjamin Kanzler</a>
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
            chooser.addChoosableFileFilter(new FileFilterImpl(FileFilterImpl.SAMPLEFilter, "ASCII text", "txt"));
            chooser.addChoosableFileFilter(new FileFilterImpl(FileFilterImpl.SAMPLEFilter, "Word", "doc"));
            chooser.addChoosableFileFilter(new FileFilterImpl(FileFilterImpl.SAMPLEFilter, "Word (2007 - 365)", "docx"));
            chooser.addChoosableFileFilter(new FileFilterImpl(FileFilterImpl.SAMPLEFilter, "PDF", "pdf"));
            chooser.addChoosableFileFilter(new FileFilterImpl(FileFilterImpl.SAMPLEFilter, "RTF", "rtf"));
            chooser.addChoosableFileFilter(new FileFilterImpl(FileFilterImpl.SAMPLEFilter, "PowerPoint", "ppt"));
            chooser.addChoosableFileFilter(new FileFilterImpl(FileFilterImpl.SAMPLEFilter, "PowerPoint (2007 - 365)", "pptx"));
            // Open the prepared file input dialog
            res = chooser.showOpenDialog(null);

            if (res == JFileChooser.APPROVE_OPTION) {
                file = chooser.getSelectedFile();
                abspath = file.getAbsolutePath();
                File tempFile;
                String fileType = getExtensionByStringHandling(file).get();

                if ("pptx".equals(fileType)) { //converts pptx doc to pdf and reads it
                    String fileNameWithoutExtension = FilenameUtils.removeExtension(file.getName());
                    String newFileName = fileNameWithoutExtension + ".pdf";
                    Presentation pres = new Presentation(abspath);
                    pres.save(newFileName, SaveFormat.Pdf);
                    tempFile = new File(newFileName);
                    sb = readTextFromFile(tempFile, sb);
                    tempFile.delete();
                } else if ("docx".equals(fileType)) { //converts docx to pdf and reads it
                    try {
                        Document doc = new Document(abspath);
                        String fileNameWithoutExtension = FilenameUtils.removeExtension(file.getName());
                        String newFileName = fileNameWithoutExtension + ".pdf";
                        doc.save(newFileName);
                        tempFile = new File(newFileName);
                        sb = readTextFromFile(tempFile, sb);
                        tempFile.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                    sb = readTextFromFile(file, sb);
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

            // Set fileFilter to files here --> all files you need in return statement
            fileDialog.setFilenameFilter(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".txt") || name.endsWith(".pdf") || name.endsWith(".doc") || name.endsWith(".ppt") || name.endsWith(".rtf");
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

            sb = readTextFromFile(file, sb);

        }

        // Set the new working dir to the current files location
        ConfigurationManager.getConfiguration().setCurrentWorkingdir(abspath.substring(0, abspath.lastIndexOf(File.separator)));

        // return the result of the StringBuilding
        return sb.toString();
    }

    /**
     * This method reads the plain text information of a txt-file.
     *
     * @param file
     * @return
     * @latestEditors <a href="mailto:bielefeld.moritz@student.dhbw-karlsruhe.de">Moritz Bielefeld</a>, <a href="mailto:geist.semjon@student.dhbw-karlsruhe.de">Semjon Geist</a>, <a href="mailto:kanzler.benjamin@student.dhbw-karlsruhe.de">Benjamin Kanzler</a>
     * @author <a href="mailto:bielefeld.moritz@student.dhbw-karlsruhe.de">Moritz Bielefeld</a>, <a href="mailto:geist.semjon@student.dhbw-karlsruhe.de">Semjon Geist</a>, <a href="mailto:kanzler.benjamin@student.dhbw-karlsruhe.de">Benjamin Kanzler</a>
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
     * @param file
     * @return
     * @latestEditors <a href="mailto:bielefeld.moritz@student.dhbw-karlsruhe.de">Moritz Bielefeld</a>, <a href="mailto:geist.semjon@student.dhbw-karlsruhe.de">Semjon Geist</a>, <a href="mailto:kanzler.benjamin@student.dhbw-karlsruhe.de">Benjamin Kanzler</a>
     * @author <a href="mailto:bielefeld.moritz@student.dhbw-karlsruhe.de">Moritz Bielefeld</a>, <a href="mailto:geist.semjon@student.dhbw-karlsruhe.de">Semjon Geist</a>, <a href="mailto:kanzler.benjamin@student.dhbw-karlsruhe.de">Benjamin Kanzler</a>
     */
    private Optional<String> getExtensionByStringHandling(File file) {
        return Optional.ofNullable(file.getAbsolutePath())
                .filter(f -> f.contains("."))
                .map(f -> f.substring(file.getAbsolutePath().lastIndexOf(".") + 1));
    }

    /**
     * Method to read text from doc and docx files
     *
     * @param file
     * @return
     * @latestEditors <a href="mailto:bielefeld.moritz@student.dhbw-karlsruhe.de">Moritz Bielefeld</a>, <a href="mailto:geist.semjon@student.dhbw-karlsruhe.de">Semjon Geist</a>, <a href="mailto:kanzler.benjamin@student.dhbw-karlsruhe.de">Benjamin Kanzler</a>
     * @author <a href="mailto:bielefeld.moritz@student.dhbw-karlsruhe.de">Moritz Bielefeld</a>, <a href="mailto:geist.semjon@student.dhbw-karlsruhe.de">Semjon Geist</a>, <a href="mailto:kanzler.benjamin@student.dhbw-karlsruhe.de">Benjamin Kanzler</a>
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

    private StringBuilder readTextFromPDF(File file, StringBuilder sb) {
        try {
            String content = new Tika().parseToString(file);
            sb.append(content);

        } catch (IOException | TikaException e) {
            e.printStackTrace();
            return null;
        }
        return sb;
    }

    private StringBuilder readTextFromFile(File file, StringBuilder sb) {

        try {
            InputStream fileStream = new FileInputStream(file);
            AutoDetectParser parser = new AutoDetectParser();
            Metadata metadata = new Metadata();
            BodyContentHandler handler = new BodyContentHandler(Integer.MAX_VALUE);

            TesseractOCRConfig config = new TesseractOCRConfig();
            PDFParserConfig pdfConfig = new PDFParserConfig();
            pdfConfig.setExtractInlineImages(true);

            ParseContext parseContext = new ParseContext();
            parseContext.set(TesseractOCRConfig.class, config);
            parseContext.set(PDFParserConfig.class, pdfConfig);
            parseContext.set(Parser.class, parser);

            try {
                parser.parse(fileStream, handler, metadata, parseContext);
                String text = handler.toString();
                sb.append(text);
                if (text.trim().isEmpty()) {
                    System.out.println("The file could not be read");
                } else {
                    System.out.println("The file could be read");
                }
                return sb;
            } catch (IOException | SAXException | TikaException e) {
                System.out.println("The file could not be read");
            } finally {
                try {
                    fileStream.close();
                } catch (IOException e) {
                    throw new Exception(e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }

}

