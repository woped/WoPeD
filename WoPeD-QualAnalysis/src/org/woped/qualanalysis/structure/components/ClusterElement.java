package org.woped.qualanalysis.structure.components;

import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.CombiOperatorTransitionModel;

//! Special cluster element serving as a token for distinguishing between
// ! ordinary net nodes and combi-operators that are used either
// ! as a source or as a target in a given cluster
public class ClusterElement {
    // ! Construct a cluster element object
    // ! @param element Specifies the element referenced
    // ! @param isSource Specifies whether the element was detected as a source
    // ! ElementType will be set based on this
    public ClusterElement(AbstractPetriNetElementModel element, boolean isSource) {
        this.m_element = element;
        if (element instanceof CombiOperatorTransitionModel) {
            this.m_elementType = isSource ? 1 : 2;
        } else {
            this.m_elementType = 0;
        }
    }

    // ! Override equals operator to test for shallow-equality rather than
    // ! for same object
    // ! @param o Object this object should be compared to
    // ! @return true if objects are shallow-equal, false otherwise
    @Override
    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }

        if (this.getClass() == o.getClass()) {
            ClusterElement other = (ClusterElement) o;
            return (m_element == other.m_element) && (m_elementType == other.m_elementType);
        }
        return false;
    }

    // ! Override hash code method to meet the requirement of producing the
    // ! same hash code for shallow-equal objects
    // ! @return integer hash code built from the hash code of the child element
    // ! and the element type integer
    @Override
    public int hashCode() {
        return m_element.hashCode() | m_elementType;
    }

    // ! Store a reference to an abstract element model
    public AbstractPetriNetElementModel m_element;
    // ! Store the type of cluster element:
    // ! 0: Standard Operator or node
    // ! 1: Combi-Operator Source
    // ! 2: Combi-Operator Target
    public int m_elementType = 0;
}
