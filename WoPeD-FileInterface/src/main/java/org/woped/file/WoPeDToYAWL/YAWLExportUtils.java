package org.woped.file.WoPeDToYAWL;

import org.woped.core.Constants;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.model.ArcModel;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.ResourceClassModel;
import org.woped.core.model.petrinet.ResourceModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.controller.vc.EditorVC;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Vector;

public class YAWLExportUtils {

    /**
     * Returns the net name of the given editor.
     *
     * @param editor
     * @return String
     */
    public static String getNetName(EditorVC editor) {
        return editor.getName().substring(0, editor.getName().indexOf("."));
    }

    /**
     * Returns a normalized id for the id-attribute of a node. Replaces whitespaces with underscores.
     *
     * @param id
     * @return String
     */
    public static String getNormalizedString(String id) {
        return id.replaceAll(" ", "_");
    }

    /**
     * Returns true if the modelElementContainer has resources attached.
     * Returns false if the modelElementContainer has no resources attached.
     *
     * @param editor
     * @return boolean
     */
    public static boolean hasResources(EditorVC editor) {
        return !editor.getModelProcessor().getResources().isEmpty() || !editor.getModelProcessor().getRoles().isEmpty() || !editor.getModelProcessor().getOrganizationUnits().isEmpty();
    }

    /**
     * Returns the roles that a resource is assigned to.
     *
     * @param editor
     * @param resource
     * @return Vector<String>
     */
    public static Vector<String> getRolesForResource(EditorVC editor, ResourceModel resource) {
        Vector<String> resourceRoles = editor.getModelProcessor().getResourceClassesResourceIsAssignedTo(resource.getName());
        // Remove groups from the assignments
        Vector<ResourceClassModel> groups = editor.getModelProcessor().getOrganizationUnits();
        Vector<String> groupsString = new Vector<>();
        for (ResourceClassModel group : groups) {
            groupsString.add(group.getName());
        }
        resourceRoles.removeAll(groupsString);

        return resourceRoles;
    }

    /**
     * Returns a default password using Base64 as encoding.
     *
     * @return String
     */
    public static synchronized String generateDefaultPassword() {
        String encodedString = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(YAWLConstants.DEFAULTPASSWORD.getBytes("UTF-8"));
            byte raw[] = md.digest();
            encodedString = Base64.getEncoder().encodeToString(raw);

        } catch (Exception e) {
            LoggerManager.error(Constants.CORE_LOGGER, "Default Password could not be generated!");
        }
        return encodedString;
    }

    /**
     * Returns whether the AbstractPetriNetElementModel is a transition or not.
     *
     * @param element
     * @return boolean
     */
    public static boolean isTransition(AbstractPetriNetElementModel element) {
        int type = element.getType();
        if(type == AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE || type == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE || type == AbstractPetriNetElementModel.SUBP_TYPE) {
            return true;
        }
        return false;
    }

    /**
     * Returns whether the AbstractPetriNetElementModel is a implicit XOR-Split or not.
     *
     * @param element
     * @return boolean
     */
    public static boolean isImplicitXORSplit(AbstractPetriNetElementModel element) {
        if (isTransition(element)) {
            Collection<AbstractPetriNetElementModel> nextElements = getImmediateNextElements(element);
            // interpret XOR-Split
            if (nextElements.size() == 1) {
                // Check if there is one place that enables a XOR-Split
                for (AbstractPetriNetElementModel nextElement : nextElements) {
                    if (nextElement.getType() == AbstractPetriNetElementModel.PLACE_TYPE && getImmediateNextElements(nextElement).size() > 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns whether the AbstractPetriNetElementModel is a implicit XOR-Join or not.
     *
     * @param element
     * @return boolean
     */
    public static boolean isImplicitXORJoin(AbstractPetriNetElementModel element) {
        if (isTransition(element)) {
            Collection<AbstractPetriNetElementModel> previousElements = getPreviousElements(element);
            // interpret XOR-Join
            if (previousElements.size() == 1) {
                // Check if there is one place that enables a XOR-Join
                for (AbstractPetriNetElementModel previousElement : previousElements) {
                    if (previousElement.getType() == AbstractPetriNetElementModel.PLACE_TYPE && getPreviousElements(previousElement).size() > 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns whether the AbstractPetriNetElementModel is a implicit AND-Split or not.
     *
     * @param element
     * @return boolean
     */
    public static boolean isImplicitANDSplit(AbstractPetriNetElementModel element) {
        if (isTransition(element)) {
            Collection<AbstractPetriNetElementModel> nextElements = getImmediateNextElements(element);

            if(nextElements.size() > 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns whether the AbstractPetriNetElementModel is a implicit AND-Join or not.
     *
     * @param element
     * @return boolean
     */
    public static boolean isImplicitANDJoin(AbstractPetriNetElementModel element) {
        if (isTransition(element)) {
            Collection<AbstractPetriNetElementModel> previousElements = getPreviousElements(element);

            if (previousElements.size() > 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the immediate next Elements for a given Net element disregarding the configuration of the export for explicit conditions.
     *
     * @param element
     * @return Collection<AbstractPetriNetElementModel> with the nextElements
     */
    private static Collection<AbstractPetriNetElementModel> getImmediateNextElements(AbstractPetriNetElementModel element) {
        ModelElementContainer modelElementContainer = element.getRootOwningContainer();
        Collection<AbstractPetriNetElementModel> nextElements = new ArrayList<>();

        for (ArcModel arc : modelElementContainer.getArcMap().values()) {
            if (arc.getSourceId() == element.getId()) {
                nextElements.add(modelElementContainer.getElementById(arc.getTargetId()));
            }
        }

        return nextElements;
    }

    /**
     * Returns the next elements of an element. Next elements regarding the configuration of the export for explicit conditions.
     *
     * @param element
     * @return Collection<AbstractPetriNetElementModel>
     */
    public static Collection<AbstractPetriNetElementModel> getNextElements(AbstractPetriNetElementModel element) {
        if (ConfigurationManager.getConfiguration().isYAWLExportExplicitPlaces()) {
            return getImmediateNextElements(element);
        } else {
            Collection<AbstractPetriNetElementModel> result = new ArrayList<>();

            Collection<AbstractPetriNetElementModel> nextElements = getImmediateNextElements(element);

            for (AbstractPetriNetElementModel nextElement : nextElements) {
                // If next element is a place and not the sink, then retrieve the places' next elements
                if (nextElement.getType() == AbstractPetriNetElementModel.PLACE_TYPE && !nextElement.isSink()) {
                    Collection<AbstractPetriNetElementModel> nextElementsAfterPlace = getImmediateNextElements(nextElement);
                    for (AbstractPetriNetElementModel nextElementAfterPlace : nextElementsAfterPlace) {
                        result.add(nextElementAfterPlace);
                    }
                } else {
                    result.add(nextElement);
                }
            }
            return result;
        }
    }

    /**
     * Returns the previous Elements for a given Net element.
     *
     * @param element
     * @return Collection<AbstractPetriNetElementModel>
     */
    public static Collection<AbstractPetriNetElementModel> getPreviousElements(AbstractPetriNetElementModel element) {
        ModelElementContainer modelElementContainer = element.getRootOwningContainer();
        Collection<AbstractPetriNetElementModel> previousElements = new ArrayList<>();

        for (ArcModel arc : modelElementContainer.getArcMap().values()) {
            if (arc.getTargetId().equals(element.getId())) {
                previousElements.add(modelElementContainer.getElementById(arc.getSourceId()));
            }
        }
        return previousElements;
    }
}
