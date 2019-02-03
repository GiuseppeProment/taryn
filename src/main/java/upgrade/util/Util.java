package upgrade.util;

import java.time.Clock;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import upgrade.domain.Day;

public class Util {
	private static Clock clock;
	private static UUID fixedUUID;
	public static void setClock(Clock clock) {
		Util.clock = clock;
	}
	public static Clock getClock() {
		if (Objects.isNull(clock)) {
			return Clock.systemDefaultZone();
		}
		return clock;
	}
	public static UUID getUUID() {
		if (Objects.isNull(fixedUUID)) {
			return UUID.randomUUID();
		}
		return fixedUUID;
	}
	public static void setFixedUUID(UUID fixedUUID) {
		Util.fixedUUID = fixedUUID;
	}
	public static Set<Day> allDaysBetween(LocalDate arrival, LocalDate departure) {
		Set<Day> period = new LinkedHashSet<>();
		while( ! arrival.isAfter(departure) ) {
			period.add(new Day(arrival));
			arrival = arrival.plusDays(1);
		};
		return period;
	}
	public static LocalDate now() {
		return LocalDate.now(Util.getClock());
	}
}
