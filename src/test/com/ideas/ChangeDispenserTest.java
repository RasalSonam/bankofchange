package test.com.ideas;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TreeMap;
import main.com.ideas.ChangeDispenser;
import org.junit.Assert;
import org.junit.Test;

public class ChangeDispenserTest {
	ChangeDispenser changeDispenser = new ChangeDispenser();
	private TreeMap<Integer, Integer> denominations = new TreeMap<Integer, Integer>();
	
	private void getMoneyFromBanker(){
		denominations.put(100, 1);
		denominations.put(50, 2);
		denominations.put(10, 3);
		denominations.put(5, 5);
	}
	
	private void setUpMachineForTransactions() {
		getMoneyFromBanker();
		changeDispenser.feedMachine(denominations);
	}
	
	private Date getDummyWeekendDate() {
		Date weekendDate = null;
		try {
			weekendDate = new SimpleDateFormat("MM/dd/yyyy").parse("11/22/2014");
		} catch (ParseException e) {}
		return weekendDate;
	}

	@Test
	public void feedMachineWithValidDenominations() {
		getMoneyFromBanker();
		Assert.assertTrue(changeDispenser.feedMachine(denominations));
	}

	@Test
	public void machineDoesNotAcceptInvalidDenominations() {
		denominations.put(25, 10);
		Assert.assertFalse(changeDispenser.feedMachine(denominations));
	}
	
	@Test
	@SuppressWarnings("serial")
	public void machineUpdatesCountOfNotesForDenominationsThatAreAlreadyExisting() {
		setUpMachineForTransactions();
		TreeMap<Integer, Integer> additionalDenominations = new TreeMap<Integer, Integer>(){{put(50, 10);}};
		Assert.assertTrue(changeDispenser.feedMachine(additionalDenominations) && changeDispenser.getCountOfNotesForDenomination(50) == 12);
	}
	
	@Test
	public void emptyMachineDoesNotDispenseAnyChange() {
		Assert.assertTrue(changeDispenser.getChange(100, getDummyWeekendDate()).get(100) == 1);
	}
	
	@Test
	public void lowestDenominationEnteredDoesNotDispenseAnyChange() {
		setUpMachineForTransactions();
		Assert.assertTrue(changeDispenser.getChange(1, getDummyWeekendDate()).get(1) == 1);
	}

	@Test
	public void invalidDenominationDoesNotDispenseAnyChange() {
		setUpMachineForTransactions();
		Assert.assertTrue(changeDispenser.getChange(1500, getDummyWeekendDate()).get(1500) == 1);
	}
	
	@Test
	public void getNotificationWhenNotesGoOutOfStock() {
		denominations.put(20, 0);
		setUpMachineForTransactions();
		Assert.assertTrue(changeDispenser.inputDenominationOOS());
	}

	@Test
	public void denominationForWhichChangeCannotBeDispensedIsReturned() {
		setUpMachineForTransactions();
		Assert.assertTrue(changeDispenser.getChange(1000, getDummyWeekendDate()).get(1000) == 1);
	}
	
	@Test
	public void getExactChangeForValidDenomination() {
		setUpMachineForTransactions();
		TreeMap<Integer, Integer> resultingChange;
		resultingChange = changeDispenser.getChange(100, getDummyWeekendDate());
		Assert.assertTrue(resultingChange.size() == 1 && resultingChange.get(50) == 2);
		resultingChange = changeDispenser.getChange(50, getDummyWeekendDate());
		Assert.assertTrue(resultingChange.size() == 2 && resultingChange.get(10) == 3 && resultingChange.get(5) == 4);
	}
	
	@Test
	public void dispenserUpdatesCountOfNotesAfterTransactionCompletes() {
		setUpMachineForTransactions();
		Assert.assertEquals(3, changeDispenser.getCountOfNotesForDenomination(10));
		changeDispenser.getChange(20, getDummyWeekendDate());
		Assert.assertEquals(1, changeDispenser.getCountOfNotesForDenomination(10));
	}
	
	@Test
	public void bankerCanChangeServiceCharge() {
		changeDispenser.updateServiceCharge(3);
		Assert.assertEquals(3, changeDispenser.getServiceCharge());
	}
	
	@Test
	public void bankerCanUpdateDOWPattern() {
		Assert.assertEquals(new ArrayList<Integer>(Arrays.asList(1, 3, 5)), changeDispenser.updateDOWPattern(1, 3, 5));
	}
	
	@Test
	public void changeRequestOnWeekendIsNotCharged() {
		setUpMachineForTransactions();
		TreeMap<Integer, Integer> resultingChange;
		resultingChange = changeDispenser.getChange(100, getDummyWeekendDate());
		Assert.assertTrue(resultingChange.size() == 1 && resultingChange.get(50) == 2);
	}
}
