package textGenerator;

import java.io.File;

public class TestTextGenerator {

	public static void main(String[] args) throws Exception {
		//String input = "<?xml version='1.0' encoding='UTF-8'?><pnml><net type='http://www.informatik.hu-berlin.de/top/pntd/ptNetb' id='noID'><place id='p1'><name><text>p1</text><graphics><offset x='60' y='170'/></graphics></name><graphics><position x='60' y='130'/><dimension x='40' y='40'/></graphics></place><place id='p2'><name><text>p2</text><graphics><offset x='230' y='320'/></graphics></name><graphics><position x='230' y='280'/><dimension x='40' y='40'/></graphics></place><transition id='t1'><name><text>t1</text><graphics><offset x='250' y='160'/></graphics></name><graphics><position x='250' y='120'/><dimension x='40' y='40'/>      </graphics>      <toolspecific tool='WoPeD' version='1.0'>        <time>0</time>        <timeUnit>1</timeUnit>        <orientation>1</orientation>      </toolspecific>    </transition>   <arc id='a1' source='p1' target='t1'>      <inscription>        <text>1</text>      </inscription>      <graphics/>      <toolspecific tool='WoPeD' version='1.0'>       <probability>1.0</probability>        <displayProbabilityOn>false</displayProbabilityOn>        <displayProbabilityPosition x='500.0' y='0.0'/>      </toolspecific>    </arc>    <arc id='a2' source='t1' target='p2'>      <inscription>        <text>1</text>      </inscription>      <graphics/>      <toolspecific tool='WoPeD' version='1.0'>        <probability>1.0</probability>        <displayProbabilityOn>false</displayProbabilityOn>        <displayProbabilityPosition x='500.0' y='0.0'/>      </toolspecific>    </arc>    <toolspecific tool='WoPeD' version='1.0'>      <bounds>        <position x='2' y='25'/><dimension x='779' y='511'/></bounds><scale>100</scale><treeWidthRight>562</treeWidthRight><overviewPanelVisible>true</overviewPanelVisible><treeHeightOverview>100</treeHeightOverview><treePanelVisible>true</treePanelVisible><verticalLayout>false</verticalLayout><resources/><simulations/><partnerLinks/><variables/></toolspecific></net></pnml>";
		String input = "test.pnml";
		//File tempFile = File.createTempFile("TempXmlDat", ".pnml");
		//System.out.println(tempFile.getAbsolutePath());
		//tempFile.delete();
		TextGenerator tg = new TextGenerator();
		System.out.println(tg.toText("/tmp/test.pnml"));
	}
}
