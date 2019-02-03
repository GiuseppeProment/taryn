package upgrade.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Booking {

	public Booking() {
		super();
	}

	public Booking(String email, Set<Day> period) {
		this.email = email;
		this.days = period;
	}

	@Id
	@GeneratedValue
	private UUID id;

	@Column
	private String name;

	@Column
	private String email;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
	private Set<Day> days = new HashSet<>();
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Day> getDays() {
		return days;
	}

	public void setDays(Set<Day> days) {
		this.days = days;
	}

}
