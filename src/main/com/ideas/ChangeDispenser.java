package main.com.ideas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class ChangeDispenser {
	private TreeMap<Integer, Integer> availableDenominations;
	private final ArrayList<Integer> VALID_DENOMINATIONS = new ArrayList<Integer> (Arrays.asList(1, 5, 10, 20, 50, 100, 500, 1000));
	
	public ChangeDispenser() {
		availableDenominations = new TreeMap<Integer, Integer>();
	}

	public boolean feedMachine(TreeMap<Integer, Integer> denominationsFromBanker) {
		for(Map.Entry<Integer, Integer> entry : denominationsFromBanker.entrySet()) {
			if(!checkForValidDenomination(entry.getKey())) {
				availableDenominations.clear();
				return false;
			}
			int countOfNotes = entry.getValue();
			if(availableDenominations.containsKey(entry.getKey()))
				countOfNotes += availableDenominations.get(entry.getKey());
			availableDenominations.put(entry.getKey(), countOfNotes);
		}
		return true;
	}
	
	private boolean checkForValidDenomination(int denomination) {
		return VALID_DENOMINATIONS.contains(denomination);
	}
	
	public TreeMap<Integer, Integer> getChange(int userInputNote) {
		TreeMap<Integer, Integer> resultingChange = new TreeMap<Integer, Integer>();
		if(isMachineEmpty() || isDenominationInvalid(userInputNote))
			return returnUserNote(userInputNote);
		int currentAmountToDispense = userInputNote;
		Integer nextAvailableDenomination = currentAmountToDispense;
		while(currentAmountToDispense > 0) {
			nextAvailableDenomination = availableDenominations.lowerKey(nextAvailableDenomination);
			if(nextAvailableDenomination == null)
				return returnUserNote(userInputNote);
			int availableNotes = getAvailableNotesForDenomination(currentAmountToDispense, nextAvailableDenomination);
			currentAmountToDispense -= (nextAvailableDenomination * availableNotes);
			resultingChange.put(nextAvailableDenomination, availableNotes);
			updateCountOfNotesInDispenser(nextAvailableDenomination, availableNotes);
		}
		return resultingChange;
	}

	private int getAvailableNotesForDenomination(int currentAmountToDispense, Integer nextAvailableDenomination) {
		int leastNoOfNotesNeeded = currentAmountToDispense / nextAvailableDenomination;
		int availableNotes = Math.min(leastNoOfNotesNeeded, availableDenominations.get(nextAvailableDenomination));
		return availableNotes;
	}

	@SuppressWarnings("serial")
	private TreeMap<Integer, Integer> returnUserNote(final int userInputNote) {
		TreeMap<Integer, Integer> result = new TreeMap<Integer, Integer>(){
			{
				put(userInputNote, 1);
			}
		};
		return result;
	}

	private boolean isMachineEmpty() {
		return availableDenominations.size() == 0;
	}
	
	private boolean isDenominationInvalid(int denomination) {
		if(!VALID_DENOMINATIONS.contains(denomination) || denomination == this.availableDenominations.firstKey())
			return true;
		return false;
	}

	private void updateCountOfNotesInDispenser(int denomination, int noOfNotesUsed) {
		int currentlyAvailableNotes = availableDenominations.get(denomination);
		availableDenominations.put(denomination, currentlyAvailableNotes - noOfNotesUsed);
	}
	
	public int getCountOfNotesForDenomination(int denomination) {
		return availableDenominations.get(denomination);
	}

	public boolean inputDenominationOOS() {
		boolean OOSFlag = false;
		for(Map.Entry<Integer, Integer> currentCountOfNotes : availableDenominations.entrySet()) {
			if(currentCountOfNotes.getValue() == 0) {
				OOSFlag = true;
				break;
			}
		}
		return OOSFlag;
	}
}
