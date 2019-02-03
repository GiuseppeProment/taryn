package upgrade.component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

import upgrade.exception.MaximumBookingDays;
import upgrade.exception.MaximumThirtyDaysInAdvance;
import upgrade.exception.MinimumOneDayAheadArrival;
import upgrade.util.Util;

@Component
public class BookingRule {
	public void checkAll(LocalDate arrival, LocalDate departure) {
		checkMinimumDaysBeforeArrival(arrival);
		checkMaximumDaysInAdvance(arrival, departure);
		checkMaximumDays(arrival, departure);
	}

	void checkMinimumDaysBeforeArrival(LocalDate arrival) {
		if ( arrival.equals( Util.now() ) ) {
			throw new MinimumOneDayAheadArrival(); 
		}
	}
	
	void checkMaximumDaysInAdvance(LocalDate arrival, LocalDate departure) {
		if ( departure.isAfter( Util.now().plusDays(30)) )  {
			throw new MaximumThirtyDaysInAdvance(); 
		}
	}

	void checkMaximumDays(LocalDate arrival, LocalDate departure) {
		if ( ChronoUnit.DAYS.between(arrival, departure) >= 3 ) {
			throw new MaximumBookingDays(); 
		};
		
	}

}
