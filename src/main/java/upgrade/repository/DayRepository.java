package upgrade.repository;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import upgrade.domain.Day;

public interface DayRepository extends JpaRepository<Day, LocalDate> {
	@Query("select d from Day d where d.day between :arrival and :departure order by d.day desc")
	Set<Day> findAllBetween(@Param("arrival") LocalDate arrival, @Param("departure") LocalDate departure);
}
