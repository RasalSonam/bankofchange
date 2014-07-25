package com.ideas.bankofchange.machine;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class ChangeMachineTest {

    @Test
    public void shouldCreateAnInstanceOfChangeMachine(){
        ChangeMachine changeMachine = new ChangeMachine();

        assertNotNull(changeMachine);
    }

    @Test
    public void shouldAllowToAddNotesToMachineForVariousDenominations() throws Exception {
        ChangeMachine changeMachine = new ChangeMachine();

        changeMachine.addDenomination(100, 10);
        changeMachine.addDenomination(50, 20);

        Assert.assertEquals(10, changeMachine.getDenominationsFor(100));
        Assert.assertEquals(20, changeMachine.getDenominationsFor(50));
    }

    @Test
    public void shouldUpdateNoteCountToMachineWhenExistingDenominationIsAdded() throws Exception {
        ChangeMachine changeMachine = new ChangeMachine();

        changeMachine.addDenomination(100, 5);
        changeMachine.addDenomination(100, 5);

        Assert.assertEquals(10, changeMachine.getDenominationsFor(100));
    }

    @Test
    public void shouldReturnZeroAsNoteCountWhenTheNoteDoesNotExistInChangeMachine(){
        ChangeMachine changeMachine = new ChangeMachine();

        Assert.assertEquals(0, changeMachine.getDenominationsFor(100));
    }

    @Test
    public void shouldReturnChangeForTheInputNoteWhenGetMeChangeIsCalled() throws Exception {
        ChangeMachine changeMachine = new ChangeMachine();

        changeMachine.addDenomination(100, 5);
        changeMachine.addDenomination(50, 5);
        changeMachine.addDenomination(20, 5);
        changeMachine.addDenomination(10, 5);

        Map<Integer, Integer> changeAmount = changeMachine.getMeChangeFor(100);
        Assert.assertEquals(2, changeAmount.get(50).intValue());

        changeAmount = changeMachine.getMeChangeFor(500);
        Assert.assertEquals(5, changeAmount.get(100).intValue());

        changeAmount = changeMachine.getMeChangeFor(200);
        Assert.assertEquals(3, changeAmount.get(50).intValue());
        Assert.assertEquals(2, changeAmount.get(20).intValue());
        Assert.assertEquals(1, changeAmount.get(10).intValue());

        changeAmount = changeMachine.getMeChangeFor(200);
        Assert.assertEquals(1, changeAmount.get(200).intValue());
    }

    @Test
    public void shouldReturnOutOfStockNotesWhenGetOutOfStockMethodCalled() throws Exception {
        ChangeMachine changeMachine = new ChangeMachine();

        changeMachine.addDenomination(100, 5);
        changeMachine.addDenomination(50, 5);
        changeMachine.addDenomination(20, 5);
        changeMachine.addDenomination(10, 5);

        Map<Integer, Integer> changeAmount = changeMachine.getMeChangeFor(500);
        Assert.assertEquals(5, changeAmount.get(100).intValue());
        Assert.assertEquals("[100]", changeMachine.getOutOfStockNotes());

    }

    @Test
    public void shouldNotifyBankerThatTheNoteAmountHasCrossed1000RupeesLimit(){

        Exception ex = null;
        ChangeMachine changeMachine = new ChangeMachine();

        try {
            changeMachine.addDenomination(100, 11);
        } catch (Exception e) {
            ex=e;
            e.printStackTrace();
        }

        Assert.assertEquals("Maximum possible denomination value is 1000 Rs.",ex.getMessage().trim());

    }

    @Test
    public void shouldNotifyUserAskingForChangeThatNoChangeAvailableWhenNoDenominationsAreAddedByBanker() throws Exception {
        Exception ex = null;
        ChangeMachine changeMachine = new ChangeMachine();

        try {
            changeMachine.getMeChangeFor(100);
        } catch (Exception e) {
            ex=e;
            e.printStackTrace();
        }

        Assert.assertEquals("no denominations",ex.getMessage().trim());
    }

    @Test
    public void shouldNotifyUserAskingForChangeThatNoChangeAvailableWhenInputNoteAmountIsLessThanMinimumDenominations() throws Exception {
        Exception ex = null;
        ChangeMachine changeMachine = new ChangeMachine();

        changeMachine.addDenomination(100, 5);
        changeMachine.addDenomination(50, 5);
        changeMachine.addDenomination(20, 5);
        changeMachine.addDenomination(10, 5);

        try {
            changeMachine.getMeChangeFor(5);
        } catch (Exception e) {
            ex=e;
            e.printStackTrace();
        }

        Assert.assertEquals("we don't have denomination of lower value",ex.getMessage().trim());
    }
}
