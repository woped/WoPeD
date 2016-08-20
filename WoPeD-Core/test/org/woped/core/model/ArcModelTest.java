package org.woped.core.model;

import javafx.scene.shape.Arc;
import org.junit.Test;

import static org.junit.Assert.*;


public class ArcModelTest {
    @Test
    public void getInscriptionValue_newInstance_returnsOne() throws Exception {

        ArcModel cut = new ArcModel();

        int expected = 1;
        int actual = cut.getInscriptionValue();

        assertEquals(expected, actual);
    }

    @Test
    public void getInscriptionValue_changedValue_returnsThatValue() throws Exception {

        ArcModel cut = new ArcModel();

        int expected = 2;
        cut.setInscriptionValue(String.valueOf(expected));
        int actual = cut.getInscriptionValue();

        assertEquals(expected, actual);
    }


}