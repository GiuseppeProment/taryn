package upgrade.controller;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import upgrade.domain.Booking;
import upgrade.domain.Day;
import upgrade.service.BookingService;

/**
 * My responsibility is take care of rest reception calling the service.
 * @author giuseppe
 */
@RestController
public class BookingController {
    
	@Autowired
	private BookingService service;
	
	@PostMapping("/book/{email}/{arrival}/{departure}")
    @ResponseStatus(HttpStatus.CREATED)
    public Booking post(  
    		@PathVariable String email, 
    		@PathVariable LocalDate arrival,
    		@PathVariable LocalDate departure) {
        return service.post(email,arrival,departure);
    }
    
	@PutMapping("/book/{id}/{arrival}/{departure}")
    @ResponseStatus(HttpStatus.OK)
    public Booking put(  
    		@PathVariable String id, 
    		@PathVariable LocalDate arrival,
    		@PathVariable LocalDate departure) {
        return service.put(id,arrival,departure);
    }
    
	@DeleteMapping("/book/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void delete(  
    		@PathVariable String id) {
        service.delete(id);
    }
    
    @GetMapping("/availability/{arrival}/{departure}")
    @ResponseStatus(HttpStatus.OK)
    public Set<Day> availability( 
    		@PathVariable LocalDate arrival,
    		@PathVariable LocalDate departure) {
        return service.availability(arrival,departure);
    }

    @GetMapping("/availability")
    @ResponseStatus(HttpStatus.OK)
    public Set<Day> availabilityDefault() {
        return service.availability();
    }

    @RequestMapping(value="/",produces=MediaType.TEXT_HTML_VALUE)
    public String hello() {
    	return "<!DOCTYPE html>"
    			+ "<html><body>"
    			+ "<b>Available urls</b>"
    			+ "<p>/cadastro (post with json body)</p>"
    			+ "<p>/login/email/password</p>"
    			+ "<p>/perfil/userId</p>"
    			+ "</body></html>";		
    }
}

