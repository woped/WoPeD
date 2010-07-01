package org.woped.qualanalysis.structure;

import java.io.Serializable;
import java.util.Comparator;

import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.petrinet.PlaceModel;

/**
 * compares to abstract element models. Transitions are always before places.
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public class TPHandleComparer implements Comparator<AbstractElementModel>, Serializable {

    /** default serial version id. */
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(AbstractElementModel o1, AbstractElementModel o2) {

        if (o1.getClass() == o2.getClass()) {
            if (o1.getNameValue() != null) {
                return o1.getNameValue().compareTo(o2.getNameValue());
            }
            return o1.getId().compareTo(o2.getId());
        }

        if (o1.getClass() == PlaceModel.class) {
            return 1;
        }

        return -1;
    }
}
