/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.woped.file.yawl;

import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.woped.file.yawl.model.DecompositionType;
import org.woped.file.yawl.model.ExternalConditionFactsType;
import org.woped.file.yawl.model.ExternalNetElementFactsType;
import org.woped.file.yawl.model.ExternalTaskFactsType;
import org.woped.file.yawl.model.FlowsIntoType;
import org.woped.file.yawl.model.LayoutContainerFactsType;
import org.woped.file.yawl.model.LayoutDecoratorFactsType;
import org.woped.file.yawl.model.LayoutFactsType.Specification;
import org.woped.file.yawl.model.LayoutFlowFactsType;
import org.woped.file.yawl.model.LayoutFrameType;
import org.woped.file.yawl.model.LayoutLabelFactsType;
import org.woped.file.yawl.model.LayoutNetFactsType;
import org.woped.file.yawl.model.LayoutRectangleType;
import org.woped.file.yawl.model.LayoutVertexFactsType;
import org.woped.file.yawl.model.NetFactsType;
import org.woped.file.yawl.model.OutputConditionFactsType;
import org.woped.file.yawl.model.SpecificationSetFactsType;
import org.woped.file.yawl.model.YAWLSpecificationFactsType;
import org.woped.file.yawl.wfnet.Place;
import org.woped.file.yawl.wfnet.Rectangle;
import org.woped.file.yawl.wfnet.Transition;
import org.woped.file.yawl.wfnet.Transition.JoinSplitType;
import org.woped.file.yawl.wfnet.WfNet;
import org.woped.file.yawl.wfnet.WfNetNode;

/**
 *
 * @author Chris
 */
public class YawlImport {

    private WfNet wfNet = new WfNet();
    private ArrayList<YawlArc> yawlArcs = new ArrayList<YawlArc>();

    public WfNet importYawlXml(File file) throws JAXBException {

        // Use JAXB to import the YAWL XML file
        JAXBContext yawlJc = JAXBContext.newInstance("org.woped.file.yawlinterface.yawlmodel");
        Unmarshaller unmarshaller = yawlJc.createUnmarshaller();
        JAXBElement<?> jaxbElement = (JAXBElement<?>) unmarshaller.unmarshal(file);

        SpecificationSetFactsType yawlRootElement = (SpecificationSetFactsType) jaxbElement.getValue();

        // Imports conditions (=places) and tasks (=transitions) from
        // YAWL XML, create intermediary objects of type
        // Place and Transitions and store them in the HashMaps
        // this.places and this.transitions
        importYawlConditionsAndTasks(yawlRootElement);

        // Import the layout data (coordinates, sizes of the graphical
        // shapes from YAWL XML
        importYawlLayoutData(yawlRootElement);


        // Connect the nodes of the workflow net using the contents of the
        // yawlArcs list generated in importYawlConditionsAndTasks()
        connectNodes();

        return this.wfNet;
    }

    private void importYawlLayoutData(SpecificationSetFactsType yawlRootElement) {

        NumberFormat numberFormat;

        // retrieve language and country settings from YAWL XML and create
        // a NumberFormat object for parsing numeric values (such as coordinates etc.)
        String country = yawlRootElement.getLayout().getLocale().getCountry();
        String language = yawlRootElement.getLayout().getLocale().getLanguage();
        numberFormat = NumberFormat.getInstance(new Locale(language, country));

        List<Specification> specs = yawlRootElement.getLayout().getSpecification();
        for (Specification spec : specs) {
            for (LayoutNetFactsType layout : spec.getNet()) {

                //String id = layout.getId();
                for (JAXBElement<?> boundsOrFrameOrViewport : layout.getBoundsOrFrameOrViewport()) {

                    if (boundsOrFrameOrViewport.getValue() instanceof LayoutFrameType) {

                        boundsOrFrameOrViewport.getValue();


                    } else if (boundsOrFrameOrViewport.getValue() instanceof LayoutFlowFactsType) {

                        boundsOrFrameOrViewport.getValue();

                    } else if (boundsOrFrameOrViewport.getValue() instanceof LayoutVertexFactsType) {

                        // Layout data for input- and output-conditions

                        LayoutVertexFactsType lvft = (LayoutVertexFactsType) boundsOrFrameOrViewport.getValue();

                        String yawlId = lvft.getId();

                        // Find the place or transition whose layout is being defined
                        //Node node = findNode(yawlId);
                        WfNetNode node = this.wfNet.findNodeExt(yawlId);
                        if (node == null) {
                            continue; // no matching place or transition: skip this object
                        }

                        // import position/size of shape
                        importYawlVertexFactsType(numberFormat, node, lvft);

                        // YAWL input- and output-conditions don't have labels and
                        // therefore no layout data for them. Set the label coordinates
                        // directly below the shape
                        node.setLabelPosition(node.getShapePosition().clone());
                        node.getLabelPosition().offsetBy(0,
                                node.getShapePosition().height + 5);


                    } else if (boundsOrFrameOrViewport.getValue() instanceof LayoutContainerFactsType) {

                        // layout data for conditions and tasks
                        LayoutContainerFactsType lcft = (LayoutContainerFactsType) boundsOrFrameOrViewport.getValue();

                        String yawlId = lcft.getId();

                        // Find the place or transition whose layout is being defined
                        WfNetNode node = this.wfNet.findNodeExt(yawlId);
                        if (node == null) {
                            continue; // no matching place or transition: skip this object
                        }

                        for (Object vld : lcft.getVertexOrLabelOrDecorator()) {

                            if (vld instanceof LayoutVertexFactsType) {
                                // this object contains the coordinates and
                                // bounds of the
                                // 2D-shape representing the place or transition
                                LayoutVertexFactsType v = (LayoutVertexFactsType) vld;

                                importYawlVertexFactsType(numberFormat, node, v);

                            } else if (vld instanceof LayoutLabelFactsType) {

                                // import label position/dimension
                                LayoutLabelFactsType lbl = (LayoutLabelFactsType) vld;
                                for (JAXBElement<?> sbb : lbl.getAttributes().getAutosizeOrBackgroundColorOrBendable()) {

                                    if (sbb.getValue() instanceof LayoutRectangleType) {
                                        LayoutRectangleType rectLabel = (LayoutRectangleType) sbb.getValue();
                                        importYawlLabelLayout(node, rectLabel, numberFormat);
                                    }


                                }
                            }
                            if (vld instanceof LayoutDecoratorFactsType
                                    && node instanceof Transition) {
                                // contains information about the (decorators for the)
                                // join- and split types of the transitions
                                LayoutDecoratorFactsType dec = (LayoutDecoratorFactsType) vld;
                                Transition trans = (Transition) node;

                                String dectype = dec.getType();

                                setJoinSplitType(trans, dectype);
                            }
                        }

                    }
                }
            }
        }
    }

    /**
     * Set join- and split-types using the
     * YAWL-XML decorator type
     * @param dectype
     */
    public void setJoinSplitType(Transition t, String dectype) {

        if (dectype.equals("AND_split")) {
            //t.setJoinType(JoinSplitType.None);
            t.setSplitType(JoinSplitType.And);
        } else if (dectype.equals("AND_join")) {
            t.setJoinType(JoinSplitType.And);
            //t.setSplitType(JoinSplitType.None);
        } else if (dectype.equals("XOR_split")) {
            //t.setJoinType(JoinSplitType.None);
            t.setSplitType(JoinSplitType.Xor);
        } else if (dectype.equals("XOR_join")) {
            t.setJoinType(JoinSplitType.Xor);
            //t.setSplitType(JoinSplitType.None);
        } else if (dectype.equals("OR_split")) {
            t.setSplitType(JoinSplitType.Or);
            //t.setSplitType(JoinSplitType.None);
        } else if (dectype.equals("OR_join")) {
            t.setJoinType(JoinSplitType.Or);
            //t.setSplitType(JoinSplitType.None);
        }

    }

    public void importYawlLabelLayout(WfNetNode node, LayoutRectangleType rect,
            NumberFormat numberFormat) {

        try {
            int x = numberFormat.parse(rect.getX()).intValue();
            int y = numberFormat.parse(rect.getY()).intValue();

            // Read width and height of the place or transition and
            // set minimum values
            int width = numberFormat.parse(rect.getW()).intValue();
            width = (width <= 0) ? 25 : width;
            int height = numberFormat.parse(rect.getH()).intValue();
            height = (height <= 0) ? 25 : height;

            node.setLabelPosition(new Rectangle(x, y, width, height));
        } catch (ParseException e) {
        }

    }

    /**
     * import a YAWL &lt;vertex>...&lt;/vertex> element (position and
     * dimensions) and set values to the Node object
     *
     * @param numberFormat number format used to parse the values
     * @param node
     * @param v contains the values of the YAWL vertex XML element
     */
    private void importYawlVertexFactsType(NumberFormat numberFormat,
            WfNetNode node, LayoutVertexFactsType v) {

        // import a YAWL <vertex>...</vertex> element (position and
        // dimensions) and set values to the Node object 

        for (JAXBElement<?> sizeOrBackgroundOrBendable : v.getAttributes().getAutosizeOrBackgroundColorOrBendable()) {
            if (sizeOrBackgroundOrBendable.getValue() instanceof LayoutRectangleType) {
                LayoutRectangleType rect = (LayoutRectangleType) sizeOrBackgroundOrBendable.getValue();

                try {
                    int x = numberFormat.parse(rect.getX()).intValue();
                    int y = numberFormat.parse(rect.getY()).intValue();

                    // Read width and height of the place or transition and
                    // set minimum values
                    int width = numberFormat.parse(rect.getW()).intValue();
                    width = (width <= 0) ? 25 : width;
                    int height = numberFormat.parse(rect.getH()).intValue();
                    height = (height <= 0) ? 25 : height;

                    node.setShapePosition(new Rectangle(x, y, width, height));

                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
    }

    private void importYawlConditionsAndTasks(
            SpecificationSetFactsType yawlRootElement) {
        // Drill down through the hierarchy of YAWL-XML elements
        List<YAWLSpecificationFactsType> specs = yawlRootElement.getSpecification();
        for (YAWLSpecificationFactsType spec : specs) {

            List<DecompositionType> decomposition = spec.getDecomposition();
            for (DecompositionType decompositionType : decomposition) {

                if (!(decompositionType instanceof NetFactsType)) {
                    continue;
                }

                NetFactsType nft = (NetFactsType) decompositionType;

                // The ProcessControlElements XML-Element finally
                // contains the list of conditions (=places)
                // and tasks (=transitions)
                NetFactsType.ProcessControlElements pce = nft.getProcessControlElements();

                // for the input and output conditions (=places) ...
                ExternalConditionFactsType inputCondition = pce.getInputCondition();

                // ... create new places in the intermediate data structure
                createPlaceFromYawlCondition(inputCondition);

                OutputConditionFactsType outputCondition = pce.getOutputCondition();
                createPlaceFromYawlOutputCondition(outputCondition);

                // import the rest of the conditions and tasks
                List<ExternalNetElementFactsType> taskOrCondition = pce.getTaskOrCondition();
                for (ExternalNetElementFactsType ne : taskOrCondition) {
                    if (ne instanceof ExternalConditionFactsType) {
                        ExternalConditionFactsType cond = (ExternalConditionFactsType) ne;

                        // Create a new place an add it to this.wfNet
                        createPlaceFromYawlCondition(cond);

                    } else if (ne instanceof ExternalTaskFactsType) {
                        ExternalTaskFactsType task = (ExternalTaskFactsType) ne;

                        // Create a new transition an add it to this.wfNet
                        createTransitionFromYawlTask(task);

                    }
                }
            }



        }
    }

    private Place createPlaceFromYawlOutputCondition(
            OutputConditionFactsType cond) {
        Place p = this.wfNet.createAndAddPlaceExt(cond.getId());

        String s = cond.getName();
        if (s == null || s.equals("")) {
            s = cond.getId();
        }

        p.setName(s);

        return p;
    }

    public Place createPlaceFromYawlCondition(ExternalConditionFactsType cond) {
        // Create a new Place, set its values and add it to 
        // our intermediary data structure
        String yawlId = cond.getId();
        Place p = this.wfNet.createAndAddPlaceExt(yawlId);

        String s = cond.getName();
        if (s == null || s.equals("")) {
            s = cond.getId();
        }
        p.setName(s);

        for (FlowsIntoType fit : cond.getFlowsInto()) {

            this.yawlArcs.add(new YawlArc(yawlId,
                    fit.getNextElementRef().getId()));
        }

        return p;
    }

    public Transition createTransitionFromYawlTask(ExternalTaskFactsType task) {
        // Create a new Transition, set its values and add it to
        // our intermediary data structure
        String yawlId = task.getId();
        Transition t = this.wfNet.createAndAddTransitionExt(yawlId);

        String s = task.getName();
        if (s == null || s.equals("")) {
            s = task.getId();
        }
        t.setName(s);

        for (FlowsIntoType fit : task.getFlowsInto()) {

            this.yawlArcs.add(new YawlArc(yawlId,
                    fit.getNextElementRef().getId()));
        }

        // Note: the standard codes for a "simple" task with
        // a single input place and a single output place are
        // an XOR join and an AND split. In cases where the task has,
        //  e.g., a visible AND split there is a definition for an 
        // additional decorator object in the layout section of
        // the XML

        return t;
    }

    private void connectNodes() {
        for (YawlArc arc : yawlArcs) {
            String fromId = arc.getFlowFrom();
            String toId = arc.getFlowTo();

            WfNetNode nodeFrom = this.wfNet.findNodeExt(fromId);
            WfNetNode nodeTo = this.wfNet.findNodeExt(toId);

            if (nodeFrom == null || nodeTo == null) {
                Logger.getLogger("YawlImport").log(Level.WARNING, "Node specified by YawlArc not found in WfNet");
                continue;
            }

            nodeFrom.addOutputNode(nodeTo);

        }
    }
}
