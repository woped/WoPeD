package org.woped.qualanalysis.soundness.datamodel;

import java.io.Serializable;
import java.util.Comparator;

import org.woped.core.model.petrinet.OperatorTransitionModel;

/**
 * Sort Algorithm for Places in a TreeMap
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public class PlaceSort implements Comparator<String>, Serializable {

    /** default serial version id. */
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(String arg0, String arg1) {
        boolean arg0CenterPlace = false;
        boolean arg1CenterPlace = false;
        int arg0sub = 0;
        int arg1sub = 0;
        String centerPlaceCutOff = OperatorTransitionModel.OPERATOR_SEPERATOR_PLACE;

        // cut off center place at the beginning for places in XOR-Transitions
        if (arg0.startsWith(centerPlaceCutOff)) {
            arg0 = arg0.substring(centerPlaceCutOff.length());
            arg0CenterPlace = true;
        }
        if (arg1.startsWith(centerPlaceCutOff)) {
            arg1 = arg1.substring(centerPlaceCutOff.length());
            arg1CenterPlace = true;
        }
        // cut off sub_ for every subprocess
        while (arg0.indexOf("_") != -1) {
            arg0 = arg0.substring(arg0.indexOf("_") + 1);
            arg0sub++;
        }
        while (arg1.indexOf("_") != -1) {
            arg1 = arg1.substring(arg1.indexOf("_") + 1);
            arg1sub++;
        }

        arg0 = arg0.substring(1);
        arg1 = arg1.substring(1);
        Integer arg0Int = Integer.parseInt(arg0);
        Integer arg1Int = Integer.parseInt(arg1);

        // return values if one place is a subnet place and the other one not.
        // no subnet places are greater than subnet places
        if (arg0sub > arg1sub) {
            return 1;
        }
        if (arg0sub < arg1sub) {
            return -1;
        }
        // return values if one place is a center place for an XORSplitJoin
        // no center places are greater than center places
        if (arg0CenterPlace && !arg1CenterPlace) {
            return 1;
        }
        if (!arg0CenterPlace && arg1CenterPlace) {
            return -1;
        }
        return arg0Int.compareTo(arg1Int);
    }

}
