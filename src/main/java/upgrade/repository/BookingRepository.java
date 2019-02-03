package upgrade.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import upgrade.domain.Booking;
	
public interface BookingRepository extends JpaRepository<Booking, UUID> {}
