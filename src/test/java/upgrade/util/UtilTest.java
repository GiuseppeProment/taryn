package upgrade.util;

import static org.junit.Assert.assertEquals;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Set;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import upgrade.domain.Day;

public class UtilTest {

	private static final UUID FIXED_UUID = UUID.fromString("b8f111f9-13e4-48c8-b9af-6cc0329a491b");
	private static final Clock FIXED_CLOCK = Clock.fixed( Instant.parse("1985-12-01T10:00:00.00Z") , ZoneId.of("Z"));

	@Before
	public void setUp() throws Exception {
		Util.setClock(null);
		Util.setFixedUUID(null);
	}
	
	@Test
	public void should_return_fixed_clock() {
		Util.setClock(FIXED_CLOCK );
		assertEquals(FIXED_CLOCK, Util.getClock());
	}

	@Test
	public void should_return_fixed_UUID() {
		Util.setFixedUUID(FIXED_UUID);
		assertEquals(FIXED_UUID, Util.getUUID());
	}
	
	@Test
	public void allDaysBetweenSmookeTest() {
		Set<Day> period = Util.allDaysBetween(LocalDate.parse("2019-01-01"), LocalDate.parse("2019-01-03") );
		assertEquals(3, period.size());
	}
}
