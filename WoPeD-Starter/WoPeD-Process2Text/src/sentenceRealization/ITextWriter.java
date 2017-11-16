package sentenceRealization;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.print.PrintTranscoder;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;



public class ITextWriter {
	
	private static String FILE = "GeneratedText.pdf";
	
	private static Font hlFont = new Font(Font.FontFamily.HELVETICA, 16,Font.BOLD);
	private static Font redFont = new Font(Font.FontFamily.HELVETICA, 10,Font.NORMAL, BaseColor.RED);
	private static Font stdFont = new Font(Font.FontFamily.HELVETICA, 10,Font.NORMAL);
	private static Font boldFont = new Font(Font.FontFamily.HELVETICA, 10,Font.BOLD);
	
	private Document document = null;
	private PdfWriter writer;

	public ITextWriter() {
		document = new Document();
		createDocument();
	}
	
	private void createDocument() {
		try {
			writer = PdfWriter.getInstance(document, new FileOutputStream(FILE));
			document.open();
			addMetaData(document);
			addTitlePage(document);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void closeDocument() {
		document.close();
	}
	
	public void newPage() {
		document.newPage();
	}
	
	public void putFigure(String file) throws MalformedURLException, IOException, DocumentException {
		
		String imgFile = file.substring(0, file.length()-4)+ "svg";
		
		String uri = new File(imgFile).toURI().toString();
        float width = 500;
        float height = 500;
      PdfContentByte cb = writer.getDirectContent();
      PdfTemplate template = cb.createTemplate(width, height);
      java.awt.Graphics2D g2d = template.createGraphics(width,height);
      PrintTranscoder prm = new PrintTranscoder();
      TranscoderInput ti = new TranscoderInput(uri);
      prm.transcode(ti, null);
      PageFormat pg = new PageFormat();
      Paper pp = new Paper();
      pp.setSize(width, height);
      pp.setImageableArea(0, 0, width, height);
      pg.setPaper(pp);
      prm.print(g2d, pg, 0);
      g2d.dispose();
      Image image = Image.getInstance(template);
      image.setRotationDegrees(90f);
		
//		Image image = Image.getInstance(imgFile);
        document.add(image);
	}
	
	public void writeText(String text, String title) throws DocumentException {
		
		addModelTitle(title);
		String[] splitText = text.split("\n");
		for (String s: splitText) {
			
			String clean = s.replaceAll("\t", "");
			int indent = (s.length() - clean.length()) * 20;
			
			Paragraph paragraph = null;
			
			if (clean.startsWith("-")) {
				clean = clean.substring(2);
				List list = new List(false, false, 10);
				list.add(new ListItem(clean,stdFont));
				paragraph = new Paragraph();
				paragraph.setFont(stdFont);
				paragraph.add(list);
			} else {
				paragraph = new Paragraph(clean, stdFont);
			}
			paragraph.setIndentationLeft(indent);
			document.add(paragraph);
		}
		Paragraph line = new Paragraph();
		addEmptyLine(line, 1);
		document.add(line);
	}
	
	
	// Add metadata to the PDF which can be viewed in your Adobe Reader under File -> Properties
	private void addMetaData(Document document) {
		document.addTitle("Model to Text Translation");
		document.addSubject("Document created using iText");
		document.addAuthor("Henrik Leopold");
		document.addCreator("Henrik Leopold");
	}

	// Add general information about document
	private void addTitlePage(Document document) throws DocumentException {
		
		Paragraph preface = new Paragraph();
		preface.add(new Paragraph("Model to Text Translation", hlFont));
		preface.add(new Paragraph("Generated on: " + new Date(), boldFont));
		addEmptyLine(preface, 1);

		document.add(preface);
	}
	
	private void addModelTitle(String title) throws DocumentException {
		Paragraph paragraph = new Paragraph(title, boldFont);
		document.add(paragraph);
	}

	private static void addContent(Document document) throws DocumentException {

	
	}

	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

}
