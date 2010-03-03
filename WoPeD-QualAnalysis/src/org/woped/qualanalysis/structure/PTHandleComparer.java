package org.woped.qualanalysis.structure;

import java.util.Comparator;

import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.petrinet.PlaceModel;

/**
 * compares to abstract element models. Places are always before transitions.
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public class PTHandleComparer implements Comparator<AbstractElementModel> {

    @Override
    public int compare(AbstractElementModel o1, AbstractElementModel o2) {

        if (o1.getClass() == o2.getClass()) {
            if (o1.getNameValue() != null) {
                return o1.getNameValue().compareTo(o2.getNameValue());
            }
            return o1.getId().compareTo(o2.getId());
        }

        if (o1.getClass() == PlaceModel.class) {
            return -1;
        }

        return 1;
    }

}
