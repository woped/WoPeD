package org.woped.file;

public class YAWLImport {

  public static String getTestPnml() {

    String s = "";

    s += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    s += "<!--PLEASE DO NOT EDIT THIS FILE\n";
    s += "Created with Workflow PetriNet Designer Version 1.0 (woped.org)-->\n";
    s += "<pnml>\n";
    s += "  <net type=\"http://www.informatik.hu-berlin.de/top/pntd/ptNetb\" id=\"noID\">\n";
    s += "    <place id=\"p2\">\n";
    s += "      <name>\n";
    s += "        <text>Ende</text>\n";
    s += "        <graphics>\n";
    s += "          <offset x=\"400\" y=\"230\"/>\n";
    s += "        </graphics>\n";
    s += "      </name>\n";
    s += "      <graphics>\n";
    s += "        <position x=\"400\" y=\"190\"/>\n";
    s += "        <dimension x=\"40\" y=\"40\"/>\n";
    s += "      </graphics>\n";
    s += "    </place>\n";
    s += "    <place id=\"p1\">\n";
    s += "      <name>\n";
    s += "        <text>Anfang</text>\n";
    s += "        <graphics>\n";
    s += "          <offset x=\"90\" y=\"230\"/>\n";
    s += "        </graphics>\n";
    s += "      </name>\n";
    s += "      <graphics>\n";
    s += "        <position x=\"90\" y=\"180\"/>\n";
    s += "        <dimension x=\"40\" y=\"40\"/>\n";
    s += "      </graphics>\n";
    s += "    </place>\n";
    s += "    <transition id=\"t1\">\n";
    s += "      <name>\n";
    s += "        <text>Trans01</text>\n";
    s += "        <graphics>\n";
    s += "          <offset x=\"240\" y=\"230\"/>\n";
    s += "        </graphics>\n";
    s += "      </name>\n";
    s += "      <graphics>\n";
    s += "        <position x=\"240\" y=\"190\"/>\n";
    s += "        <dimension x=\"40\" y=\"40\"/>\n";
    s += "      </graphics>\n";
    s += "      <toolspecific tool=\"WoPeD\" version=\"1.0\">\n";
    s += "        <time>0</time>\n";
    s += "        <timeUnit>1</timeUnit>\n";
    s += "        <orientation>1</orientation>\n";
    s += "      </toolspecific>\n";
    s += "    </transition>\n";
    s += "    <arc id=\"a1\" source=\"p1\" target=\"t1\">\n";
    s += "      <inscription>\n";
    s += "        <text>1</text>\n";
    s += "      </inscription>\n";
    s += "      <graphics/>\n";
    s += "      <toolspecific tool=\"WoPeD\" version=\"1.0\">\n";
    s += "        <probability>1.0</probability>\n";
    s += "        <displayProbabilityOn>false</displayProbabilityOn>\n";
    s += "        <displayProbabilityPosition x=\"500.0\" y=\"0.0\"/>\n";
    s += "      </toolspecific>\n";
    s += "    </arc>\n";
    s += "    <arc id=\"a2\" source=\"t1\" target=\"p2\">\n";
    s += "      <inscription>\n";
    s += "        <text>1</text>\n";
    s += "      </inscription>\n";
    s += "      <graphics/>\n";
    s += "      <toolspecific tool=\"WoPeD\" version=\"1.0\">\n";
    s += "        <probability>1.0</probability>\n";
    s += "        <displayProbabilityOn>false</displayProbabilityOn>\n";
    s += "        <displayProbabilityPosition x=\"500.0\" y=\"0.0\"/>\n";
    s += "      </toolspecific>\n";
    s += "    </arc>\n";
    s += "    <toolspecific tool=\"WoPeD\" version=\"1.0\">\n";
    s += "      <bounds>\n";
    s += "        <position x=\"2\" y=\"27\"/>\n";
    s += "        <dimension x=\"874\" y=\"576\"/>\n";
    s += "      </bounds>\n";
    s += "      <scale>100</scale>\n";
    s += "      <treeWidthRight>636</treeWidthRight>\n";
    s += "      <overviewPanelVisible>true</overviewPanelVisible>\n";
    s += "      <treeHeightOverview>100</treeHeightOverview>\n";
    s += "      <treePanelVisible>true</treePanelVisible>\n";
    s += "      <verticalLayout>false</verticalLayout>\n";
    s += "      <resources/>\n";
    s += "      <simulations/>\n";
    s += "      <partnerLinks/>\n";
    s += "      <variables/>\n";
    s += "    </toolspecific>\n";
    s += "  </net>\n";
    s += "</pnml>\n";

    return s;
  }
}
