package org.woped.core.model.petrinet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.woped.core.model.CreationMap;

import static org.junit.Assert.assertEquals;

public class PlaceModelTest {

    private PlaceModel cut;

    @Before
    public void setUp() throws Exception {
        cut = new PlaceModel(CreationMap.createMap());
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void consumeVirtualTokens_enoghtVirtualTokens_decreasesVirtualTokensByAmount() throws Exception {

        int initialTokenCount = 4;
        int tokensToRemove = 2;

        cut.setVirtualTokens(initialTokenCount);
        int expected = initialTokenCount - tokensToRemove;

        cut.removeVirtualTokens(tokensToRemove);
        int actual = cut.getVirtualTokenCount();

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeVirtualTokens_amountNegative_throwsException() throws Exception {
        int tokensToRemove = -1;
        cut.removeVirtualTokens(tokensToRemove);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeVirtualTokens_notEnoughTokens_throwsException() throws Exception {

        int initialTokens = 2;
        int tokensToRemove = 3;

        cut.setVirtualTokens(initialTokens);
        cut.removeVirtualTokens(tokensToRemove);
    }

    @Test
    public void addVirtualTokens_positiveAmount_increasesVirtualTokensByAmount() throws Exception {

        int initialTokens = 1;
        int tokensToAdd = 3;

        cut.setVirtualTokens(initialTokens);
        int expected = initialTokens + tokensToAdd;

        cut.addVirtualTokens(tokensToAdd);
        int actual = cut.getVirtualTokenCount();

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addVirtualTokens_negativeAmount_throwsException() throws Exception {

        int tokensToAdd = -2;
        cut.addVirtualTokens(tokensToAdd);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addVirtualTokens_amountOverflowsRange_throwsException() throws Exception {
        int initialTokens = Integer.MAX_VALUE;
        int tokensToAdd = 1;

        cut.setVirtualTokens(initialTokens);
        cut.addVirtualTokens(tokensToAdd);
    }
}