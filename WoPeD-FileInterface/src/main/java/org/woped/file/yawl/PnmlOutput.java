/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.woped.file.yawl;

import org.woped.file.yawl.wfnet.Place;
import org.woped.file.yawl.wfnet.Transition;
import org.woped.file.yawl.wfnet.WfNet;
import org.woped.file.yawl.wfnet.WfNetNode;

/**
 *
 * @author Chris
 */
public class PnmlOutput {

    private WfNet wfNet;

    public PnmlOutput(WfNet wfNet) {
        this.wfNet = wfNet;
    }

    public String getPnmlOutput() {
        // The root /pnml element
        XmlElement rootElement = new XmlElement("pnml");

        // The /pnml/condition element
        XmlElement netElement = rootElement.addAndGetChild("net");
        netElement.addAttribute(new XmlElement.XmlAttribute("type",
                "http://www.informatik.hu-berlin.de/top/pntd/ptNetb"));
        netElement.addAttribute(new XmlElement.XmlAttribute("id", "noID"));

        // Export places
        for (WfNetNode node : this.wfNet.getNodes()) {

            if (!(node instanceof Place)) {
                continue;
            }
            //Place place = (Place) node;

            // <place id="...">...</place>
            XmlElement xml_place = netElement.addAndGetChild("place");

            setIdAndNameAndLayoutData(xml_place, node);
            
            // some places which are part of the PNML representation of
            // "complex" transitions (such as XOR-Join-Splits) have
            // a "toolspecific" section with an operator code
            if (!node.getWopedOperatorId().equals("")) {
                XmlElement ts = xml_place.addAndGetChild("toolspecific");
                ts.addAttribute("tool", "WoPeD");
                ts.addAttribute("version", "1.0");
           
                ts.addAndGetChild("operator", false)
                        .addAttribute("id", node.getWopedOperatorId())
                        .addAttribute("type", node.getWopedOperatorCode());


            }


        }

        // Transitions
        for (WfNetNode node : this.wfNet.getNodes()) {

            if (!(node instanceof Transition)) {
                continue;
            }
            Transition transition = (Transition) node;


            XmlElement xml_trans = netElement.addAndGetChild("transition");

            setIdAndNameAndLayoutData(xml_trans, node);

            XmlElement ts = xml_trans.addAndGetChild("toolspecific");
            ts.addAttribute("tool", "WoPeD");
            ts.addAttribute("version", "1.0");

            if (transition.hasJoinOrSplit()) {
                ts.addAndGetChild("operator", false)
                        .addAttribute("id", transition.getWopedOperatorId())
                        .addAttribute("type", transition.getWopedOperatorCode());


            }

            ts.addAndGetChild("time").setContents("0");
            ts.addAndGetChild("timeUnit").setContents("1");
            ts.addAndGetChild("orientation").setContents(
                    transition.getWopedOrientation());

        }

        // generate arc xml
        for (WfNetNode node : this.wfNet.getNodes()) {

            for (WfNetNode output_node : node.getOutputNodes()) {
                //Arc arc = new Arc();
                XmlElement xml_arc = netElement.addAndGetChild("arc");
                xml_arc.addAttribute("id", wfNet.getNewArcId());
                
                String source_id = node.getId();
                String target_id = output_node.getId();
                
                //String source_id = (node instanceof Transition) ?
                //        node.getCoreId() : node.getId();
                //String target_id = (output_node instanceof Transition) ?
                //        output_node.getCoreId() : output_node.getId();
                        
                xml_arc.addAttribute("source", source_id);
                xml_arc.addAttribute("target", target_id);

                xml_arc.addAndGetChild("inscription")
                        .addAndGetChild("text")
                        .setContents("1");

                xml_arc.addChild("graphics", false); // <graphics />

                XmlElement ts = xml_arc.addAndGetChild("toolspecific");
                ts.addAttribute("tool", "WoPeD");
                ts.addAttribute("version", "1.0");

                ts.addAndGetChild("probability").setContents("1.0");
                ts.addAndGetChild("displayProbabilityOn").setContents("false");
                ts.addAndGetChild("displayProbabilityPosition", false).
                        addAttribute("x", "500.0").
                        addAttribute("y", "0.0");

            }
        }

        // other xml elements
        XmlElement ts = netElement.addAndGetChild("toolspecific");
        ts.addAttribute("tool", "WoPeD");
        ts.addAttribute("version", "1.0");

        XmlElement bounds = ts.addAndGetChild("bounds");
        bounds.addAndGetChild("position", false)
                .addAttribute("x", "2").addAttribute("y", "2");
        bounds.addAndGetChild("dimension", false)
                .addAttribute("x", "1024").addAttribute("y", "768");

        ts.addAndGetChild("scale").setContents("100");
        ts.addAndGetChild("treeWidthRight").setContents("800");
        ts.addAndGetChild("overviewPanelVisible").setContents("true");
        ts.addAndGetChild("treeHeightOverview").setContents("100");
        ts.addAndGetChild("treePanelVisible").setContents("true");
        ts.addAndGetChild("verticalLayout").setContents("false");
        ts.addAndGetChild("resources", false);
        ts.addAndGetChild("simulations", false);
        ts.addAndGetChild("partnerLinks", false);
        ts.addAndGetChild("variables", false);


        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + rootElement.toString();
    }

    
    /*private String getWoPedOperatorCode(Transition t) {

        Transition.JoinSplitType join = t.getJoinType();
        Transition.JoinSplitType split = t.getSplitType();

        if (join == Transition.JoinSplitType.None && split == Transition.JoinSplitType.And) {
            return "101";
        } else if (join == Transition.JoinSplitType.None && split == Transition.JoinSplitType.Xor) {
            return "104";
        } else if (join == Transition.JoinSplitType.And && split == Transition.JoinSplitType.None) {
            return "102";
        } else if (join == Transition.JoinSplitType.And && split == Transition.JoinSplitType.And) {
            return "107";
        }  else if (join == Transition.JoinSplitType.And && split == Transition.JoinSplitType.Xor) {
            return "108";
        }  else if (join == Transition.JoinSplitType.Xor && split == Transition.JoinSplitType.None) {
            return "105";
        }  else if (join == Transition.JoinSplitType.Xor && split == Transition.JoinSplitType.And) {
            return "109";
        }  else if (join == Transition.JoinSplitType.Xor && split == Transition.JoinSplitType.Xor) {
            return "106";
        }

        return "0";
    }*/

    private void setIdAndNameAndLayoutData(XmlElement xml_node, WfNetNode node) {
        
        xml_node.addAttribute("id", node.getId());

        // Name/label of the node
        XmlElement nameElem = xml_node.addAndGetChild("name"); // <name>

        nameElem.addAndGetChild("text").setContents(node.getName()); // <name><text>
        nameElem.addAndGetChild("graphics") // <name><graphics>
                .addAndGetChild("offset", false) // <name><graphics><offset>
                .addAttribute("x", node.getLabelPosition().x) // <name><graphics><offset x="...">
                .addAttribute("y", node.getLabelPosition().y); // <name><graphics><offset y="...">

        XmlElement graphics = xml_node.addAndGetChild("graphics");

        graphics.addAndGetChild("position", false)
                .addAttribute("x", node.getShapePosition().x)
                .addAttribute("y", node.getShapePosition().y);
        graphics.addAndGetChild("dimension", false)
                .addAttribute("x", node.getShapePosition().width)
                .addAttribute("y", node.getShapePosition().height);
    }
    
    
}
