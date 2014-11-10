package test.com.ideas;

import java.util.TreeMap;
import main.com.ideas.ChangeDispenser;
import org.junit.Assert;
import org.junit.Test;

public class ChangeDispenserTest {
	ChangeDispenser changeMachine = new ChangeDispenser();
	private TreeMap<Integer, Integer> denominations = new TreeMap<Integer, Integer>();
	
	private void getMoneyFromBanker(){
		denominations.put(100, 1);
		denominations.put(50, 2);
		denominations.put(10, 3);
		denominations.put(5, 5);
	}
	
	private void setUpMachineForTransactions() {
		getMoneyFromBanker();
		changeMachine.feedMachine(denominations);
	}
	
	@Test
	public void feedMachineWithValidDenominations() {
		getMoneyFromBanker();
		Assert.assertTrue(changeMachine.feedMachine(denominations));
	}

	@Test
	public void machineDoesNotAcceptInvalidDenominations() {
		denominations.put(25, 10);
		Assert.assertFalse(changeMachine.feedMachine(denominations));
	}
	
	@Test
	@SuppressWarnings("serial")
	public void machineUpdatesCountOfNotesForDenominationsThatAreAlreadyExisting() {
		setUpMachineForTransactions();
		TreeMap<Integer, Integer> additionalDenominations = new TreeMap<Integer, Integer>(){{put(50, 10);}};
		Assert.assertTrue(changeMachine.feedMachine(additionalDenominations) && changeMachine.getCountOfNotesForDenomination(50) == 12);
	}
	
	@Test
	public void emptyMachineDoesNotDispenseAnyChange() {
		Assert.assertTrue(changeMachine.getChange(100).get(100) == 1);
	}
	
	@Test
	public void lowestDenominationEnteredDoesNotDispenseAnyChange() {
		setUpMachineForTransactions();
		Assert.assertTrue(changeMachine.getChange(1).get(1) == 1);
	}

	@Test
	public void invalidDenominationDoesNotDispenseAnyChange() {
		setUpMachineForTransactions();
		Assert.assertTrue(changeMachine.getChange(1500).get(1500) == 1);
	}
	
	@Test
	public void getNotificationWhenNotesGoOutOfStock() {
		denominations.put(20, 0);
		setUpMachineForTransactions();
		Assert.assertTrue(changeMachine.inputDenominationOOS());
	}

	@Test
	public void denominationForWhichChangeCannotBeDispensedIsReturned() {
		setUpMachineForTransactions();
		Assert.assertTrue(changeMachine.getChange(1000).get(1000) == 1);
	}
	
	@Test
	public void getExactChangeForValidDenomination() {
		setUpMachineForTransactions();
		TreeMap<Integer, Integer> resultingChange;
		resultingChange = changeMachine.getChange(100);
		Assert.assertTrue(resultingChange.size() == 1 && resultingChange.get(50) == 2);
		resultingChange = changeMachine.getChange(50);
		Assert.assertTrue(resultingChange.size() == 2 && resultingChange.get(10) == 3 && resultingChange.get(5) == 4);
	}
	
	@Test
	public void dispenserUpdatesCountOfNotesAfterTransactionCompletes() {
		setUpMachineForTransactions();
		Assert.assertEquals(3, changeMachine.getCountOfNotesForDenomination(10));
		changeMachine.getChange(20);
		Assert.assertEquals(1, changeMachine.getCountOfNotesForDenomination(10));
	}
}
