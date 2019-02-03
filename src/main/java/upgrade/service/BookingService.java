package upgrade.service;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upgrade.component.BookingRule;
import upgrade.domain.Booking;
import upgrade.domain.Day;
import upgrade.repository.BookingRepository;
import upgrade.repository.DayRepository;
import upgrade.util.Util;

/**
 * My responsibility is to coordinate business process, (e.g. a orchestration stuff).
 */
@Service
public class BookingService {

	@Autowired private BookingRepository bookRepo;
	@Autowired private DayRepository dayRepo;
	@Autowired private BookingRule rule;

	synchronized public Booking post(String email, LocalDate arrival, LocalDate departure) {
		rule.checkAll(arrival,departure);
		return bookRepo
				.saveAndFlush( new Booking(email, Util.allDaysBetween(arrival, departure)) );
	}

	public Set<Day> availability(LocalDate arrival, LocalDate departure) {
		Set<Day> unavailable = dayRepo.findAllBetween(arrival, departure);
		Set<Day> requested = Util.allDaysBetween(arrival, departure);
		requested.removeAll(unavailable);
		return requested;
	}

	public Set<Day> availability() {
		return availability(Util.now().plusDays(1), Util.now().plusDays(30));
	}

	public Booking put(String id, LocalDate arrival, LocalDate departure) {
		rule.checkAll(arrival,departure);
		Booking book = bookRepo.getOne(UUID.fromString(id));
		book.getDays().clear();
		book.getDays().addAll(Util.allDaysBetween(arrival, departure));
		return bookRepo.saveAndFlush(book);
	}

	public void delete(String id) {
		bookRepo.deleteById(UUID.fromString(id));
	}


}
