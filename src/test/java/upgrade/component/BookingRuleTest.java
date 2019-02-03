package upgrade.component;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import org.junit.Before;
import org.junit.Test;

import upgrade.exception.MaximumBookingDays;
import upgrade.exception.MaximumThirtyDaysInAdvance;
import upgrade.exception.MinimumOneDayAheadArrival;
import upgrade.util.Util;

public class BookingRuleTest {
	
	private BookingRule rule;

	@Test
	public void shouldBookMiniumOneDayAheadArrival() {
		rule.checkMinimumDaysBeforeArrival(LocalDate.parse("2019-01-02"));
	}
	
	@Test(expected=MinimumOneDayAheadArrival.class)
	public void shouldNotBookOnArrivalDate() {
		rule.checkMinimumDaysBeforeArrival(LocalDate.parse("2019-01-01"));
	}
	
	@Test
	public void shouldBookMaximumThirtyDaysInAdvance() {
		rule.checkMaximumDaysInAdvance(LocalDate.parse("2019-01-28"), LocalDate.parse("2019-01-30"));
	}
	
	@Test(expected=MaximumThirtyDaysInAdvance.class)
	public void shouldNotBookMoreThanThirtyDaysInAdvanceArrivalOut() {
		rule.checkMaximumDaysInAdvance(LocalDate.parse("2019-01-30"), LocalDate.parse("2019-02-01"));
	}

	@Test(expected=MaximumBookingDays.class)
	public void shouldNotBookBeyondTreeDays() {
		rule.checkMaximumDays(LocalDate.parse("2019-01-02"), LocalDate.parse("2019-01-05"));
	}
	
	@Before
	public void setUp() {
		rule = new BookingRule();
		Util.setClock( Clock.fixed( Instant.parse(TODAY) , ZoneId.of("Z")));
	}
	private static final String TODAY = "2019-01-01T10:00:00.00Z";
}
