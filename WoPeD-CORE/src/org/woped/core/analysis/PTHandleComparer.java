package org.woped.core.analysis;

import java.util.Comparator;

import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.petrinet.PlaceModel;

public class PTHandleComparer implements Comparator<AbstractElementModel> {

    @Override
    public int compare(AbstractElementModel o1, AbstractElementModel o2) {

        if (o1.getClass() == o2.getClass()) {
            return o1.getId().compareTo(o2.getId());
        }

        if (o1.getClass() == PlaceModel.class) {
            return -1;
        }

        return 1;
    }

}
