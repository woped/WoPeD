package org.woped.file.WoPeDToYAWL;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.*;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.file.Constants;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

public class YAWLExport {

    private Document yawlDocument = null;
    private Document resourceDocument = null;

    private EditorVC editor = null;
    // Layout-Element for the whole specification
    private Element specificationLayout = null;
    // Layout-Element for the current net, needed for multiple subnets
    private Element netLayout = null;

    /**
     * Creates a new instance of an exporter.
     */
    public YAWLExport() {

    }

    /**
     * Saves the petrinet to *.yawl File and its resources to *.ybkp.
     *
     * @param editor
     * @param fileName
     */
    public void saveToFile(EditorVC editor, String fileName) {
        LoggerManager.debug(Constants.FILE_LOGGER, "##### START YAWL EXPORT #####");
        long start = System.currentTimeMillis();
        this.editor = editor;

        createYAWLDocument(fileName);

        if (YAWLExportUtils.hasResources(editor)) {
            createResourceDocument(fileName);
        }
        LoggerManager.debug(Constants.FILE_LOGGER, "Filename: " + fileName);
        LoggerManager.debug(Constants.FILE_LOGGER, "##### END YAWL EXPORT ##### (" + (System.currentTimeMillis() - start) + " ms)");
    }

    /**
     * Creates a new Document-instance.
     *
     * @return Document
     */
    private Document createNewDocument() {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return documentBuilder.newDocument();
    }

    /**
     * Creates the *.yawl File with all its elements.
     *
     * @param fileName
     */
    private void createYAWLDocument(String fileName) {
        yawlDocument = createNewDocument();
        // Build File Structure
        Element specificationSet = initSpecificationSet();
        Element metaData = initMetaData();
        Element specification = initSpecification();
        Element documentation = initDocumentation();
        Element xsSchema = initXsSchema();
        Element layout = initLayout();

        specification.appendChild(documentation);
        specification.appendChild(metaData);
        specification.appendChild(xsSchema);

        // Create the decompositions and processControlElements for the YAWL-net
        initProcessControlElements(specification);

        specificationSet.appendChild(specification);
        specificationSet.appendChild(layout);

        yawlDocument.appendChild(specificationSet);

        // transform file to .yawl
        transformFile(yawlDocument, fileName);
    }

    /**
     * Creates the *.ybkp file with all its resources.
     *
     * @param fileName
     */
    private void createResourceDocument(String fileName) {
        resourceDocument = createNewDocument();
        fileName = fileName.replaceAll(".yawl", "Resources.ybkp");
        // Build File Structure
        Element orgdata = resourceDocument.createElement(YAWLConstants.ORGDATA);
        Element participants = initParticipants();
        Element roles = initRoles();
        Element groups = initOrgGroups();

        orgdata.appendChild(participants);
        orgdata.appendChild(roles);
        orgdata.appendChild(groups);

        resourceDocument.appendChild(orgdata);

        // transform file to .ybkp
        transformFile(resourceDocument, fileName);
    }

    /**
     * Creates and returns the participants-elements.
     *
     * @return Element the <participants>-element *.ybkp file
     */
    private Element initParticipants() {
        Element participants = resourceDocument.createElement(YAWLConstants.PARTICIPANTS);

        Vector<ResourceModel> resources = editor.getModelProcessor().getResources();

        for (ResourceModel resource : resources) {
            Element participant = resourceDocument.createElement(YAWLConstants.PARTICIPANT);
            participant.setAttribute(YAWLConstants.ID, resource.getName());

            // userID is a necessary field, otherwise only one object can be uploaded to the yawl resource engine
            Element userID = resourceDocument.createElement(YAWLConstants.USERID);
            Element password = resourceDocument.createElement(YAWLConstants.PASSWORD);
            Element firstName = resourceDocument.createElement(YAWLConstants.FIRSTNAME);
            // In WoPeD lastname is not required
            Element lastName = resourceDocument.createElement(YAWLConstants.LASTNAME);
            Element description = resourceDocument.createElement(YAWLConstants.DESCRIPTION);
            Element notes = resourceDocument.createElement(YAWLConstants.NOTES);
            Element isAdministrator = resourceDocument.createElement(YAWLConstants.ISADMINISTRATOR);
            Element roles = resourceDocument.createElement(YAWLConstants.ROLES);
            Element positions = resourceDocument.createElement(YAWLConstants.POSITIONS);
            Element capabilities = resourceDocument.createElement(YAWLConstants.CAPABILITIES);
            Element privileges = resourceDocument.createElement(YAWLConstants.PRIVILEGES);

            // Check for '*'-delimiter in the name to set firstName and lastName
            String resourceName = resource.getName();
            String firstNameString = resourceName;
            String lastNameString = "";
            if(resource.getName().contains("*")) {
                firstNameString = resourceName.split("\\*")[0];
                lastNameString = resourceName.split("\\*")[1];
            }
            userID.appendChild(resourceDocument.createTextNode(firstNameString.toLowerCase()));
            // Allocate a default password to each user
            password.appendChild(resourceDocument.createTextNode(YAWLExportUtils.generateDefaultPassword()));

            firstName.appendChild(resourceDocument.createTextNode(firstNameString));
            lastName.appendChild(resourceDocument.createTextNode(lastNameString));

            // Add roles to the participant
            Vector<String> resourceRoles = YAWLExportUtils.getRolesForResource(editor, resource);

            for (String resourceRole : resourceRoles) {
                Element role = resourceDocument.createElement(YAWLConstants.ROLE);

                role.appendChild(resourceDocument.createTextNode(resourceRole));

                roles.appendChild(role);
            }

            participant.appendChild(userID);
            participant.appendChild(password);
            participant.appendChild(firstName);
            participant.appendChild(lastName);
            participant.appendChild(description);
            participant.appendChild(notes);
            participant.appendChild(isAdministrator);
            participant.appendChild(roles);
            participant.appendChild(positions);
            participant.appendChild(capabilities);
            participant.appendChild(privileges);

            participants.appendChild(participant);
        }
        return participants;
    }

    /**
     * Creates and returns the roles-elements.
     *
     * @return Element the <roles>-element *.ybkp file
     */
    private Element initRoles() {
        Element roles = resourceDocument.createElement(YAWLConstants.ROLES);

        Vector<ResourceClassModel> resources = editor.getModelProcessor().getRoles();

        for (ResourceClassModel resource : resources) {
            Element role = resourceDocument.createElement(YAWLConstants.ROLE);
            role.setAttribute(YAWLConstants.ID, resource.getName());

            Element name = resourceDocument.createElement(YAWLConstants.NAME);
            Element description = resourceDocument.createElement(YAWLConstants.DESCRIPTION);
            Element notes = resourceDocument.createElement(YAWLConstants.NOTES);

            name.appendChild(resourceDocument.createTextNode(resource.getName()));

            role.appendChild(name);
            role.appendChild(description);
            role.appendChild(notes);

            roles.appendChild(role);
        }
        return roles;
    }

    /**
     * Creates and returns the orggroups-elements.
     *
     * @return Element the <orggroups>-element *.ybkp file
     */
    private Element initOrgGroups() {
        Element groups = resourceDocument.createElement(YAWLConstants.ORGGROUPS);

        // Check if exportGroups is selected in settings
        if (ConfigurationManager.getConfiguration().isYAWLExportGroups()) {
            Vector<ResourceClassModel> resources = editor.getModelProcessor().getOrganizationUnits();

            for (ResourceClassModel resource : resources) {
                Element group = resourceDocument.createElement(YAWLConstants.ORGGROUP);
                group.setAttribute(YAWLConstants.ID, resource.getName());

                Element groupName = resourceDocument.createElement(YAWLConstants.GROUPNAME);
                Element groupType = resourceDocument.createElement(YAWLConstants.GROUPTYPE);
                Element description = resourceDocument.createElement(YAWLConstants.DESCRIPTION);
                Element notes = resourceDocument.createElement(YAWLConstants.NOTES);

                groupName.appendChild(resourceDocument.createTextNode(resource.getName()));
                groupType.appendChild(resourceDocument.createTextNode("GROUP"));

                group.appendChild(groupName);
                group.appendChild(groupType);
                group.appendChild(description);
                group.appendChild(notes);

                groups.appendChild(group);
            }
        }
        return groups;
    }

    /**
     * Creates and returns the layout-element.
     *
     * @return Element the <layout>-element of *.yawl file
     */
    private Element initLayout() {
        Element layout = yawlDocument.createElement(YAWLConstants.LAYOUT);

        Element locale = yawlDocument.createElement(YAWLConstants.LOCALE);
        locale.setAttribute(YAWLConstants.LANGUAGE, "de");
        locale.setAttribute(YAWLConstants.COUNTRY, "DE");

        Element specification = yawlDocument.createElement(YAWLConstants.SPECIFICATION);
        specification.setAttribute(YAWLConstants.ID, YAWLExportUtils.getNetName(editor));
        specification.setAttribute(YAWLConstants.DEFAULTBGCOLOR, "-526351");

        Element size = yawlDocument.createElement(YAWLConstants.SIZE);
        size.setAttribute(YAWLConstants.W, "58");
        size.setAttribute(YAWLConstants.H, "28");

        Element net = createNet(YAWLExportUtils.getNetName(editor));

        specification.appendChild(size);
        specification.appendChild(net);

        layout.appendChild(locale);
        layout.appendChild(specification);

        this.specificationLayout = specification;
        this.netLayout = net;

        return layout;
    }

    /**
     * Creates and returns the specificationset-element.
     *
     * @return Element the <specificationset>-element of *.yawl file
     */
    private Element initSpecificationSet() {
        Element specificationSet = yawlDocument.createElement(YAWLConstants.SPECIFICATIONSET);
        specificationSet.setAttribute("xmlns", "http://www.yawlfoundation.org/yawlschema");
        specificationSet.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        specificationSet.setAttribute("version", "4.0");
        specificationSet.setAttribute("xsi:schemaLocation", "http://www.yawlfoundation.org/yawlschema http://www.yawlfoundation.org/yawlschema/YAWL_Schema4.0.xsd");

        return specificationSet;
    }

    /**
     * Creates and returns the specification-element.
     *
     * @return Element the <specification>-element of *.yawl file
     */
    private Element initSpecification() {
        Element specification = yawlDocument.createElement(YAWLConstants.SPECIFICATION);

        specification.setAttribute("uri", YAWLExportUtils.getNetName(editor));

        return specification;
    }

    /**
     * Creates and returns the documentation-element.
     *
     * @return Element the <documentation>-element of *.yawl file
     */
    private Element initDocumentation() {
        Element documentation = yawlDocument.createElement(YAWLConstants.DOCUMENTATION);
        documentation.appendChild(yawlDocument.createTextNode("No description provided"));

        return documentation;
    }

    /**
     * Creates and returns the metadata-element with its elements.
     *
     * @return Element the <metadata>-element of *.yawl file
     */
    private Element initMetaData() {
        Element metaData = yawlDocument.createElement(YAWLConstants.METADATA);

        Element creator = yawlDocument.createElement(YAWLConstants.CREATOR);
        Element description = yawlDocument.createElement(YAWLConstants.DESCRIPTION);
        Element coverage = yawlDocument.createElement(YAWLConstants.COVERAGE);
        Element version = yawlDocument.createElement(YAWLConstants.VERSION);
        Element persistent = yawlDocument.createElement(YAWLConstants.PERSISTENT);
        Element identifier = yawlDocument.createElement(YAWLConstants.IDENTIFIER);

        creator.appendChild(yawlDocument.createTextNode("Creator"));
        description.appendChild(yawlDocument.createTextNode("No description provided"));
        coverage.appendChild(yawlDocument.createTextNode("4.3.1.772"));
        version.appendChild(yawlDocument.createTextNode("0.1"));
        persistent.appendChild(yawlDocument.createTextNode("false"));
        identifier.appendChild(yawlDocument.createTextNode("UID_1"));

        metaData.appendChild(creator);
        metaData.appendChild(description);
        metaData.appendChild(coverage);
        metaData.appendChild(version);
        metaData.appendChild(persistent);
        metaData.appendChild(identifier);

        return metaData;
    }

    /**
     * Creates and returns the xs:schema-element.
     *
     * @return Element the <xs:schema>-element of *.yawl file
     */
    private Element initXsSchema() {
        Element xsSchema = yawlDocument.createElement("xs:schema");
        xsSchema.setAttribute("xmlns:xs", "http://www.w3.org/2001/XMLSchema");

        return xsSchema;
    }

    /**
     * Creates and returns the decomposition-element.
     *
     * @return Element the <decomposition>-element of *.yawl file
     */
    private Element initDecomposition() {
        Element decomposition = yawlDocument.createElement(YAWLConstants.DECOMPOSITION);
        decomposition.setAttribute(YAWLConstants.ID, YAWLExportUtils.getNetName(editor));
        decomposition.setAttribute("isRootNet", "true");
        decomposition.setAttribute("xsi:type", "NetFactsType");

        return decomposition;
    }

    /**
     * Creates processControlElements-element of *.yawl file. This is the
     *
     * @param specification
     */
    private void initProcessControlElements(Element specification) {
        // Get the Net and its elements
        PetriNetModelProcessor petriNetModelProcessor = editor.getModelProcessor();
        ModelElementContainer modelElementContainer = petriNetModelProcessor.getElementContainer();

        initProcessControlElements(modelElementContainer, specification, initDecomposition(), netLayout);
    }

    /**
     * Creates the processControlElements-element of *.yawl file.
     *
     * @param elementContainer the net containing all net-elements
     * @param specification the specification to which the decomposition needs to be added
     * @param decomposition the decomposition to which the processControlElements need to be added
     * @param netLayout the layout to which the net-elements need to be added
     */
    private void initProcessControlElements(ModelElementContainer elementContainer, Element specification, Element decomposition, Element netLayout) {
        Element processControlElements = yawlDocument.createElement(YAWLConstants.PROCESSCONTROLELEMENTS);

        List<AbstractPetriNetElementModel> netElements = elementContainer.getRootElements();

        // Iterate over all net elements
        for (AbstractPetriNetElementModel element : netElements) {
            Element node = null;

            // Check if element is place and edit node
            if (element.getType() == AbstractPetriNetElementModel.PLACE_TYPE) {
                node = processPlaces(element, netLayout);
            }
            // Check if element is transition and edit node
            if (element.getType() == AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE || element.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE) {
                node = processTransitions(element, netLayout);
            }
            // Check if element is subprocess and edit node
            if (element.getType() == AbstractPetriNetElementModel.SUBP_TYPE) {
                LoggerManager.debug(Constants.FILE_LOGGER, " ... Subprocess detected: (ID:" + element.getId() + ")");

                node = processTransitions(element, netLayout);

                Element decomposesTo = yawlDocument.createElement(YAWLConstants.DECOMPOSESTO);
                decomposesTo.setAttribute(YAWLConstants.ID, YAWLExportUtils.getNormalizedString(element.getNameValue()));
                node.appendChild(decomposesTo);

                // Retrieve the subprocess container
                ModelElementContainer subprocess = ((SubProcessModel) element).getSimpleTransContainer();

                Element subDecomposition = yawlDocument.createElement(YAWLConstants.DECOMPOSITION);
                subDecomposition.setAttribute(YAWLConstants.ID, YAWLExportUtils.getNormalizedString(element.getNameValue()));
                subDecomposition.setAttribute("xsi:type", "NetFactsType");

                // Add Layout for subnet elements
                Element subnetLayout = createNet(YAWLExportUtils.getNormalizedString(element.getNameValue()));

                initProcessControlElements(subprocess, specification, subDecomposition, subnetLayout);
            }
            // Add flowsInto-node for all elements except the sink
            if (!element.isSink() && node != null) {
                Collection<AbstractPetriNetElementModel> nextElements = YAWLExportUtils.getNextElements(element);

                // Counter for XOR-Split predicate ordering
                int counterXOR = -1;
                // Iterate over every next element
                for (AbstractPetriNetElementModel nextElement : nextElements) {
                    Element flowsInto = yawlDocument.createElement(YAWLConstants.FLOWSINTO);

                    Element nextElementRef = yawlDocument.createElement(YAWLConstants.NEXTELEMENTREF);
                    String nextElementRefName = nextElement.getNameValue();

                    nextElementRef.setAttribute(YAWLConstants.ID, YAWLExportUtils.getNormalizedString(nextElementRefName));

                    flowsInto.appendChild(nextElementRef);

                    // Get operator of element
                    int operatorType = 0;
                    if (element.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE) {
                        operatorType = ((OperatorTransitionModel) element).getOperatorType();
                    }
                    // Add Default-flow and predicates for XOR-Splits
                    // Check if element is an explicit or implicit XOR-Split
                    if (operatorType == OperatorTransitionModel.XOR_SPLIT_TYPE ||
                            operatorType == OperatorTransitionModel.XORJOIN_XORSPLIT_TYPE ||
                            operatorType == OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE ||
                            (YAWLExportUtils.isImplicitXORSplit(element) && !ConfigurationManager.getConfiguration().isYAWLExportExplicitPlaces())) {
                        if (counterXOR == -1) {
                            Element isDefaultFlow = yawlDocument.createElement(YAWLConstants.ISDEFAULTFLOW);

                            flowsInto.appendChild(isDefaultFlow);
                        } else {
                            Element predicate = yawlDocument.createElement(YAWLConstants.PREDICATE);
                            predicate.setAttribute(YAWLConstants.ORDERING, "" + counterXOR);
                            predicate.appendChild(yawlDocument.createTextNode("true()"));

                            flowsInto.appendChild(predicate);
                        }
                        ++counterXOR;
                    }
                    // Insert the flows into-element after the node's name-element
                    node.insertBefore(flowsInto, node.getFirstChild().getNextSibling());

                    // Add Layout for arc
                    Element flow = createFlow(element, nextElement);
                    netLayout.appendChild(flow);

                    LoggerManager.debug(Constants.FILE_LOGGER, "   ... Arc (" + YAWLExportUtils.getNormalizedString(element.getNameValue()) + " -> " + YAWLExportUtils.getNormalizedString(nextElement.getNameValue()) + ") set");
                }
            }
            if (node != null) {
                // Insert the inputCondition at the beginning of processControlElements-element to be valid against yawl schema
                if(element.isRoot()) {
                    processControlElements.insertBefore(node, processControlElements.getFirstChild());
                }
                else {
                    processControlElements.appendChild(node);
                }
            }
        }
        // Move the outputCondition at the end of processControlElements-element to be valid against yawl schema
        NodeList nodeList = processControlElements.getElementsByTagName(YAWLConstants.OUTPUTCONDITION);
        if(nodeList.getLength() > 0) {
            Node sink = nodeList.item(0);
            processControlElements.removeChild(nodeList.item(0));
            processControlElements.appendChild(sink);
        }

        // Add the process control elements to the net/decomposition
        decomposition.appendChild(processControlElements);
        // Add the net/decomposition to the specification
        specification.appendChild(decomposition);
        // Add the net to the layout node
        specificationLayout.appendChild(netLayout);
    }

    /**
     * Sets the place node for the given net element and adds its layout to the layout-node.
     *
     * @param element, netLayout
     * @return Element
     */
    private Element processPlaces(AbstractPetriNetElementModel element, Element netLayout) {
        Element node = null;

        // Add root-place
        if (element.isRoot()) {
            node = yawlDocument.createElement(YAWLConstants.INPUTCONDITION);
        }
        // Add sink-place
        if (element.isSink()) {
            node = yawlDocument.createElement(YAWLConstants.OUTPUTCONDITION);
        }
        // Add condition if explicit places is chosen in config
        if (!element.isRoot() && !element.isSink() && ConfigurationManager.getConfiguration().isYAWLExportExplicitPlaces()) {
            node = yawlDocument.createElement(YAWLConstants.CONDITION);
        }

        // Check if node is not null, node can be null if exportExplicitPlaces is false in config
        if (node != null) {
            Element name = yawlDocument.createElement(YAWLConstants.NAME);
            name.appendChild(yawlDocument.createTextNode(YAWLExportUtils.getNormalizedString(element.getNameValue())));

            node.appendChild(name);
            node.setAttribute(YAWLConstants.ID, YAWLExportUtils.getNormalizedString(element.getNameValue()));

            // Add layout for element
            Element containerLayout = null;
            Element vertexLayout = createVertex(element);
            vertexLayout.setAttribute(YAWLConstants.ID, YAWLExportUtils.getNormalizedString(element.getNameValue()));

            // Add layout for label of element
            containerLayout = yawlDocument.createElement(YAWLConstants.CONTAINER);
            containerLayout.setAttribute(YAWLConstants.ID, YAWLExportUtils.getNormalizedString(element.getNameValue()));

            Element labelLayout = createLabel(element);

            containerLayout.appendChild(vertexLayout);
            containerLayout.appendChild(labelLayout);

            if (containerLayout != null) {
                netLayout.appendChild(containerLayout);
            } else {
                netLayout.appendChild(vertexLayout);
            }
            LoggerManager.debug(Constants.FILE_LOGGER, "   ... Condition " + "(ID:" + node.getAttribute(YAWLConstants.ID) + ") set");
        }
        return node;
    }

    /**
     * Sets the transition node for the given net element and adds its layout to the layout-node.
     *
     * @param element, netLayout
     * @return Element
     */
    private Element processTransitions(AbstractPetriNetElementModel element, Element netLayout) {
        Element node = yawlDocument.createElement(YAWLConstants.TASK);

        node.setAttribute(YAWLConstants.ID, YAWLExportUtils.getNormalizedString(element.getNameValue()));

        Element name = yawlDocument.createElement(YAWLConstants.NAME);
        Element join = yawlDocument.createElement(YAWLConstants.JOIN);
        Element split = yawlDocument.createElement(YAWLConstants.SPLIT);
        Element resourcing = yawlDocument.createElement(YAWLConstants.RESOURCING);

        Element offer = yawlDocument.createElement(YAWLConstants.OFFER);
        Element allocate = yawlDocument.createElement(YAWLConstants.ALLOCATE);
        Element start = yawlDocument.createElement(YAWLConstants.START);

        // join code="xor" and split code="and" seems to be the 'code' for atomic task without join-type and split-type
        String joinString = YAWLConstants.XOR;
        String splitString = YAWLConstants.AND;
        // hasJoinDecorator and hasSplitDecorator used for layout, identifying if an element needs a decorator
        boolean hasJoinDecorator = false;
        boolean hasSplitDecorator = false;

        // Check for explicit join- or split operator
        if (element.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE) {
            OperatorTransitionModel operator = (OperatorTransitionModel) element;
            switch (operator.getOperatorType()) {
                // AND-Split
                case 101:
                    splitString = YAWLConstants.AND;
                    hasSplitDecorator = true;
                    break;
                // AND-Join
                case 102:
                    joinString = YAWLConstants.AND;
                    hasJoinDecorator = true;
                    break;
                // XOR-Split
                case 104:
                    splitString = YAWLConstants.XOR;
                    hasSplitDecorator = true;
                    break;
                // XOR-Join
                case 105:
                    joinString = YAWLConstants.XOR;
                    hasJoinDecorator = true;
                    break;
                // XOR-Join-Split
                case 106:
                    joinString = splitString = YAWLConstants.XOR;
                    hasJoinDecorator = true;
                    hasSplitDecorator = true;
                    break;
                // AND-Join-Split
                case 107:
                    joinString = splitString = YAWLConstants.AND;
                    hasJoinDecorator = true;
                    hasSplitDecorator = true;
                    break;
                // AND-Join-XOR-Split
                case 108:
                    joinString = YAWLConstants.AND;
                    splitString = YAWLConstants.XOR;
                    hasJoinDecorator = true;
                    hasSplitDecorator = true;
                    break;
                // XOR-Join-AND-Split
                case 109:
                    joinString = YAWLConstants.XOR;
                    splitString = YAWLConstants.AND;
                    hasJoinDecorator = true;
                    hasSplitDecorator = true;
                    break;
                default:
                    break;
            }
        }
        // Check for implicit Joins and Splits if there is not already an explicit operator
        if (!hasSplitDecorator && YAWLExportUtils.isImplicitXORSplit(element) && !ConfigurationManager.getConfiguration().isYAWLExportExplicitPlaces()) {
            splitString = YAWLConstants.XOR;
            hasSplitDecorator = true;
        }
        if (!hasJoinDecorator && YAWLExportUtils.isImplicitXORJoin(element) && !ConfigurationManager.getConfiguration().isYAWLExportExplicitPlaces()) {
            joinString = YAWLConstants.XOR;
            hasJoinDecorator = true;
        }
        if (!hasSplitDecorator && YAWLExportUtils.isImplicitANDSplit(element)) {
            splitString = YAWLConstants.AND;
            hasSplitDecorator = true;
        }
        if (!hasJoinDecorator && YAWLExportUtils.isImplicitANDJoin(element)) {
            joinString = YAWLConstants.AND;
            hasJoinDecorator = true;
        }

        join.setAttribute(YAWLConstants.CODE, joinString);
        split.setAttribute(YAWLConstants.CODE, splitString);

        // Check for resource trigger
        if (YAWLExportUtils.isTransition(element)) {
            TransitionModel transitionModel = (TransitionModel) element;
            TransitionResourceModel resourceModel = transitionModel.getToolSpecific().getTransResource();
            TriggerModel triggerModel = ((TransitionModel) element).getToolSpecific().getTrigger();

            if (triggerModel != null && resourceModel != null && triggerModel.getTriggertype() == TriggerModel.TRIGGER_RESOURCE) {
                offer.setAttribute(YAWLConstants.INITIATOR, "system");

                Element distributionSet = yawlDocument.createElement(YAWLConstants.DISTRIBUTIONSET);
                Element initialSet = yawlDocument.createElement(YAWLConstants.INITIALSET);

                // Add the role to the task
                String roleString = resourceModel.getTransRoleName();
                Element role = yawlDocument.createElement(YAWLConstants.ROLE);
                role.appendChild(yawlDocument.createTextNode(roleString));

                allocate.setAttribute(YAWLConstants.INITIATOR, "user");
                start.setAttribute(YAWLConstants.INITIATOR, "user");

                initialSet.appendChild(role);
                distributionSet.appendChild(initialSet);
                offer.appendChild(distributionSet);
            }
            else {
                offer.setAttribute(YAWLConstants.INITIATOR, "user");
                allocate.setAttribute(YAWLConstants.INITIATOR, "user");
                start.setAttribute(YAWLConstants.INITIATOR, "user");
            }
        }
        resourcing.appendChild(offer);
        resourcing.appendChild(allocate);
        resourcing.appendChild(start);

        name.appendChild(yawlDocument.createTextNode(element.getNameValue()));

        node.appendChild(name);
        node.appendChild(join);
        node.appendChild(split);
        node.appendChild(resourcing);

        // Add Layout for the task
        Element containerLayout = yawlDocument.createElement(YAWLConstants.CONTAINER);
        Element vertexLayout = createVertex(element);
        Element labelLayout = createLabel(element);

        containerLayout.setAttribute(YAWLConstants.ID, "" + YAWLExportUtils.getNormalizedString(element.getNameValue()));

        // Add the Join-Decorator for the task
        if (hasJoinDecorator) {
            Element decorator = yawlDocument.createElement(YAWLConstants.DECORATOR);
            Element position = yawlDocument.createElement(YAWLConstants.POSITION);
            Element attributes = yawlDocument.createElement(YAWLConstants.ATTRIBUTES);
            Element bounds = yawlDocument.createElement(YAWLConstants.BOUNDS);

            decorator.setAttribute(YAWLConstants.TYPE, joinString.toUpperCase() + "_join");
            position.appendChild(yawlDocument.createTextNode("12"));
            bounds.setAttribute(YAWLConstants.X, "" + (element.getX() + YAWLConstants.XOFFSET_JOIN_DECORATOR));
            bounds.setAttribute(YAWLConstants.Y, "" + element.getY());
            bounds.setAttribute(YAWLConstants.W, "11");
            bounds.setAttribute(YAWLConstants.H, "32");

            attributes.appendChild(bounds);
            decorator.appendChild(position);
            decorator.appendChild(attributes);

            containerLayout.appendChild(decorator);
        }
        // Add the Split-Decorator for the task
        if (hasSplitDecorator) {
            Element decorator = yawlDocument.createElement(YAWLConstants.DECORATOR);
            Element position = yawlDocument.createElement(YAWLConstants.POSITION);
            Element attributes = yawlDocument.createElement(YAWLConstants.ATTRIBUTES);
            Element bounds = yawlDocument.createElement(YAWLConstants.BOUNDS);

            decorator.setAttribute(YAWLConstants.TYPE, splitString.toUpperCase() + "_split");
            position.appendChild(yawlDocument.createTextNode("13"));
            bounds.setAttribute(YAWLConstants.X, "" + (element.getX() + YAWLConstants.XOFFSET_SPLIT_DECORATOR));
            bounds.setAttribute(YAWLConstants.Y, "" + element.getY());
            bounds.setAttribute(YAWLConstants.W, "11");
            bounds.setAttribute(YAWLConstants.H, "32");

            attributes.appendChild(bounds);
            decorator.appendChild(position);
            decorator.appendChild(attributes);

            containerLayout.appendChild(decorator);
        }
        containerLayout.appendChild(vertexLayout);
        containerLayout.appendChild(labelLayout);

        netLayout.appendChild(containerLayout);

        LoggerManager.debug(Constants.FILE_LOGGER, "   ... Task " + "(ID:" + node.getAttribute(YAWLConstants.ID) + ") set");

        return node;
    }

    /**
     * Creates a vertex-Element for the layout-part of an element.
     * @param element
     * @return Element
     */
    private Element createVertex(AbstractPetriNetElementModel element) {
        Element vertex = yawlDocument.createElement(YAWLConstants.VERTEX);

        Element attributes = yawlDocument.createElement(YAWLConstants.ATTRIBUTES);

        Element bounds = yawlDocument.createElement(YAWLConstants.BOUNDS);
        bounds.setAttribute(YAWLConstants.X, "" + element.getX());
        bounds.setAttribute(YAWLConstants.Y, "" + element.getY());
        bounds.setAttribute(YAWLConstants.W, "32");
        bounds.setAttribute(YAWLConstants.H, "32");

        attributes.appendChild(bounds);

        vertex.appendChild(attributes);

        return vertex;
    }

    /**
     * Creates a label-Element for the layout-part of an element.
     * @param element
     * @return Element
     */
    private Element createLabel(AbstractPetriNetElementModel element) {
        Element label = yawlDocument.createElement(YAWLConstants.LABEL);
        Element attributes = yawlDocument.createElement(YAWLConstants.ATTRIBUTES);
        Element bounds = yawlDocument.createElement(YAWLConstants.BOUNDS);

        bounds.setAttribute(YAWLConstants.X, "" + (element.getX() + YAWLConstants.XOFFSET_LABEL));
        bounds.setAttribute(YAWLConstants.Y, "" + (element.getY() + YAWLConstants.YOFFSET_LABEL));
        //bounds.setAttribute(YAWLConstants.X, "" + (element.getNameModel().getX() + YAWLConstants.XOFFSET_LABEL));
        //bounds.setAttribute(YAWLConstants.Y, "" + (element.getNameModel().getY()));
        bounds.setAttribute(YAWLConstants.W, "96");
        bounds.setAttribute(YAWLConstants.H, "20");

        attributes.appendChild(bounds);
        label.appendChild(attributes);

        return label;
    }

    private Element createNet(String netName) {
        Element net = yawlDocument.createElement(YAWLConstants.NET);
        net.setAttribute(YAWLConstants.ID, netName);
        net.setAttribute(YAWLConstants.BGCOLOR, "-526351");

        Element bounds = yawlDocument.createElement(YAWLConstants.BOUNDS);
        bounds.setAttribute(YAWLConstants.X, "0");
        bounds.setAttribute(YAWLConstants.Y, "0");
        bounds.setAttribute(YAWLConstants.W, "1684");
        bounds.setAttribute(YAWLConstants.H, "651");

        Element frame = yawlDocument.createElement(YAWLConstants.FRAME);
        frame.setAttribute(YAWLConstants.X, "0");
        frame.setAttribute(YAWLConstants.Y, "0");
        frame.setAttribute(YAWLConstants.W, "1687");
        frame.setAttribute(YAWLConstants.H, "654");

        Element viewport = yawlDocument.createElement(YAWLConstants.VIEWPORT);
        viewport.setAttribute(YAWLConstants.X, "0");
        viewport.setAttribute(YAWLConstants.Y, "0");
        viewport.setAttribute(YAWLConstants.W, "1687");
        viewport.setAttribute(YAWLConstants.H, "654");

        net.appendChild(bounds);
        net.appendChild(frame);
        net.appendChild(viewport);

        return net;
    }

    /**
     * Returns the flow-Element that represents the layout of an arc in a YAWL-net.
     *
     * @param source
     * @param target
     * @return Element
     */
    private Element createFlow(AbstractPetriNetElementModel source, AbstractPetriNetElementModel target) {
        Element flow = yawlDocument.createElement(YAWLConstants.FLOW);
        flow.setAttribute(YAWLConstants.SOURCE, YAWLExportUtils.getNormalizedString(source.getNameValue()));
        flow.setAttribute(YAWLConstants.TARGET, YAWLExportUtils.getNormalizedString(target.getNameValue()));

        Element ports = yawlDocument.createElement(YAWLConstants.PORTS);

        int xDiff = target.getX() - source.getX();
        int yDiff = target.getY() - source.getY();

        // Target is to the right
        if (xDiff > 0) {
            ports.setAttribute(YAWLConstants.IN, "13");
            ports.setAttribute(YAWLConstants.OUT, "12");
        }
        // Target is to the left
        if (xDiff < 0) {
            ports.setAttribute(YAWLConstants.IN, "12");
            ports.setAttribute(YAWLConstants.OUT, "13");
        }
        // Target is below
        if (yDiff > 0 && xDiff == 0) {
            ports.setAttribute(YAWLConstants.IN, "12");
            ports.setAttribute(YAWLConstants.OUT, "12");
        }
        // Target is above
        if (yDiff < 0 && xDiff == 0) {
            ports.setAttribute(YAWLConstants.IN, "13");
            ports.setAttribute(YAWLConstants.OUT, "13");
        }
        // Check if source or target are operators, in which case the 'in' and 'out' ports have to be changed
        if (source.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE || YAWLExportUtils.isImplicitANDSplit(source) || YAWLExportUtils.isImplicitXORSplit(source)) {
            ports.setAttribute(YAWLConstants.IN, "13");
        }
        if (target.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE || YAWLExportUtils.isImplicitANDJoin(target) || YAWLExportUtils.isImplicitXORJoin(target)) {
            ports.setAttribute(YAWLConstants.OUT, "12");
        }

        Element attributes = yawlDocument.createElement(YAWLConstants.ATTRIBUTES);
        Element lineStyle = yawlDocument.createElement(YAWLConstants.LINESTYLE);
        lineStyle.appendChild(yawlDocument.createTextNode("11"));

        attributes.appendChild(lineStyle);

        flow.appendChild(ports);
        flow.appendChild(attributes);

        return flow;
    }

    /**
     * Transforms the XML to the YAWL-file.
     *
     * @param document
     * @param fileName
     */
    private void transformFile(Document document, String fileName) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new FileOutputStream(fileName));

            transformer.transform(domSource, streamResult);
        } catch (Exception e) {
            LoggerManager.warn(Constants.FILE_LOGGER, "Could not transform YAWL-file: " + e.getMessage());
        }
    }
}