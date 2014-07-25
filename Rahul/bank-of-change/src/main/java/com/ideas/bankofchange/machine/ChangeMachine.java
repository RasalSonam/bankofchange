package com.ideas.bankofchange.machine;

import java.util.*;

public class ChangeMachine {
    private Map<Integer, Integer> mapOfNoteCount = new HashMap<Integer, Integer>();
    private Set<Integer> notesOutOfStock = new TreeSet<Integer>();

    public void addDenomination(int note, int count) throws Exception {
        addDenominationToMap(mapOfNoteCount, note, count);
    }

    private void addDenominationToMap(Map<Integer, Integer> noteCount, int note, int count) throws Exception {
        if(noteCount.containsKey(note)){
            int calculatedNoteCount = noteCount.get(note) + count;
            checkIfMaxLimitReached(note, calculatedNoteCount);
            noteCount.put(note, calculatedNoteCount);
        }else{
            checkIfMaxLimitReached(note, count);
            noteCount.put(note, count);
        }
    }

    private void checkIfMaxLimitReached(int note, int calculatedNoteCount) throws Exception {
        if(calculatedNoteCount * note > 1000){
            ThrowExceptionWith("Maximum possible denomination value is 1000 Rs.");
        }
    }

    public int getDenominationsFor(int note) {
        return mapOfNoteCount.containsKey(note) ? mapOfNoteCount.get(note) : 0;
    }

    private void deductNote(int note, int count){
        if(mapOfNoteCount.containsKey(note)) {
            int calculatedCount = mapOfNoteCount.get(note) - count;
            if(calculatedCount > 0){
                mapOfNoteCount.put(note, calculatedCount);
            }else{
                mapOfNoteCount.remove(note);
                notesOutOfStock.add(note);
            }
        }
    }

    public Map<Integer, Integer> getMeChangeFor(int inputAmount) throws Exception {
        validateIfThereAreAnyDenominationsEntered();
        Map<Integer, Integer> changeAmount = new HashMap<Integer, Integer>();
        List<Integer> availableNotes = getOrderedListOFAvailableNotes();
        validateForLeastDinomination(inputAmount, availableNotes);
        Integer sum = 0;
        for (Integer note : availableNotes){
            if(note == inputAmount){
                continue;
            }

            int noteCount = getDenominationsFor(note);
            for (int i = 1; i <= noteCount; i++){
                if(sum + note <= inputAmount) {
                    sum += note;
                    deductNote(note, 1);
                    addDenominationToMap(changeAmount, note, 1);
                }
            }

        }

        if(sum!=inputAmount){
            changeAmount = new HashMap<Integer, Integer>();
            changeAmount.put(inputAmount, 1);
            return changeAmount;
        }
        return changeAmount;
    }

    private void validateIfThereAreAnyDenominationsEntered() throws Exception {
        if(mapOfNoteCount.isEmpty()) {
            ThrowExceptionWith("no denominations");
        }
    }

    private void validateForLeastDinomination(int inputAmount, List<Integer> availableNotes) throws Exception {
        if(availableNotes.get((availableNotes.size()-1)) > inputAmount) {
            ThrowExceptionWith("we don't have denomination of lower value");
        }
    }

    private void ThrowExceptionWith(String message) throws Exception {
        throw new Exception(message);
    }

    private List<Integer> getOrderedListOFAvailableNotes() {
        List<Integer> availableNotes = new ArrayList<Integer>();
        availableNotes.addAll(mapOfNoteCount.keySet());
        orderTheListInDescending(availableNotes);
        return availableNotes;
    }

    private void orderTheListInDescending(List<Integer> availableNotes) {
        Collections.sort(availableNotes);
        Collections.reverse(availableNotes);
    }

    public String getOutOfStockNotes() {
        return notesOutOfStock.toString();
    }
}
