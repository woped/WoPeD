package edu.stanford.nlp.parser.lexparser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import edu.stanford.nlp.trees.Tree;

public class EvalbFormatWriter {

  private static PrintWriter goldWriter;
  private static PrintWriter testWriter;

  public static void initEVALBfiles(TreebankLangParserParams tlpParams) {
    try {
      goldWriter = tlpParams.pw(new FileOutputStream("parses.gld"));
      testWriter = tlpParams.pw(new FileOutputStream("parses.tst"));
    } catch (IOException e) {
      System.exit(0);
    }
  }

  public static void closeEVALBfiles() {
    goldWriter.close();
    testWriter.close();
  }

  public static void writeEVALBline(Tree gold, Tree test) {
    goldWriter.println((gold == null) ? "(())" : gold.toString());
    testWriter.println((test == null) ? "(())" : test.toString());
    System.err.println("Wrote EVALB lines.");
  }
}
