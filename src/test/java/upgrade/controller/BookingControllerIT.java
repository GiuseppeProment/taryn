package upgrade.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import upgrade.domain.Booking;
import upgrade.repository.BookingRepository;
import upgrade.util.Util;


/**
 * @author giuseppe 
 * Spring tests Looks more like lightly modified integration tests... then this is a Integration Test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerIT {

	@Autowired 	private MockMvc mvc;
	@Autowired 	private BookingRepository bookRepo;
	@Autowired private ObjectMapper mapper;

	@Test
	public void shouldGetAvailabilitySpecific() throws Exception {
		//@formatter:off
		mvc.perform(
				get("/availability/2019-01-01/2019-01-03"))
		.andDo(print())
		.andExpect( status().isOk() )
		.andExpect( jsonPath("$.length()",equalTo(3)))
		.andExpect( jsonPath("$[0].day",equalTo("2019-01-01")))
		.andExpect( jsonPath("$[1].day",equalTo("2019-01-02")))
		.andExpect( jsonPath("$[2].day",equalTo("2019-01-03")));
		//@formatter:on
	}
	
	@Test
	public void shouldGetAvailabilityDefault() throws Exception {
		//@formatter:off
		mvc.perform(
				get("/availability"))
		.andDo(print())
		.andExpect( status().isOk() )
		.andExpect( jsonPath("$.length()",equalTo(30)))
		.andExpect( jsonPath("$[0].day",equalTo("2019-01-02")))
		.andExpect( jsonPath("$[29].day",equalTo("2019-01-31")));
		//@formatter:on
	}

	@Test
	public void shouldAvailabilityBeUpdateAfterBook() throws Exception {
		//@formatter:off
		mvc.perform(
				post("/book/human@earth/2019-01-02/2019-01-03") )
		.andExpect( status().isCreated());
		mvc.perform(
				get("/availability/2019-01-01/2019-01-04"))
		.andDo(print())
		.andExpect( status().isOk() )
		.andExpect( jsonPath("$.length()",equalTo(2)))
		.andExpect( jsonPath("$[0].day",equalTo("2019-01-01")))
		.andExpect( jsonPath("$[1].day",equalTo("2019-01-04")));
		//@formatter:on
	}
	
	@Test
	public void shouldBookSucessfully() throws Exception {
		//@formatter:off
		mvc.perform(
				post("/book/human@earth/2019-01-02/2019-01-03"))
		.andDo(print())
		.andExpect( status().isCreated() )
		.andExpect( jsonPath("$.id").exists());
		//@formatter:on
	}

	@Test
	public void shouldNotBookOverlapping() throws Exception {
		//@formatter:off
		mvc.perform(
				post("/book/human@earth/2019-01-02/2019-01-03"));
		mvc.perform(
				post("/book/anotherHuman@earth/2019-01-03/2019-01-04"))
		.andDo(print())
		.andExpect( status().isBadRequest())
		.andExpect( jsonPath("$.message",equalTo("the períod you are asking for is not available anymore, please choose another one.")));
		//@formatter:on
	}
	
	@Test
	public void shouldNotBookBeyondThreeDays() throws Exception {
		//@formatter:off
		mvc.perform(
				post("/book/anHuman@earth/2019-01-02/2019-01-05"))
		.andDo(print())
		.andExpect( status().isBadRequest() )
		.andExpect( jsonPath("$.message",equalTo("the maximum períod accepted is tree days.")));
		//@formatter:on
	}
	
	@Test
	public void shouldBookMinimumOneDayAheadArrival() throws Exception {
		//@formatter:off
		mvc.perform(
				post("/book/anHuman@earth/2019-01-01/2019-01-02"))
		.andDo(print())
		.andExpect( status().isBadRequest() )
		.andExpect( jsonPath("$.message",equalTo("the minimum days ahead of arrival for reservation is one.")));
		//@formatter:on
	}
	
	@Test
	public void shouldBookMaxThirtyDaysInAdvance() throws Exception {
		//@formatter:off
		mvc.perform(
				post("/book/anHuman@earth/2019-02-01/2019-02-02"))
		.andDo(print())
		.andExpect( status().isBadRequest() )
		.andExpect( jsonPath("$.message",equalTo("the maximum days for reservation in advance is 30.")));
		//@formatter:on
	}
	
	@Test
	public void shouldModifyBook() throws Exception {
		//@formatter:off
		String content = mvc.perform(
				post("/book/anHuman@earth/2019-01-02/2019-01-03"))
				.andExpect( status().isCreated() )
				.andReturn()
				.getResponse()
				.getContentAsString();
		Booking book = mapper.readValue(content, Booking.class);
		
		mvc.perform(
				put( String.format("/book/%s/2019-01-04/2019-01-05",book.getId()) ))
		.andExpect( status().isOk() );
		mvc.perform(
				get("/availability/2019-01-01/2019-01-05"))
		.andDo(print())
		.andExpect( status().isOk() )
		.andExpect( jsonPath("$.length()",equalTo(3)))
		.andExpect( jsonPath("$[0].day",equalTo("2019-01-01")))
		.andExpect( jsonPath("$[1].day",equalTo("2019-01-02")))
		.andExpect( jsonPath("$[2].day",equalTo("2019-01-03")));
		//@formatter:on
	}
	
	@Test
	public void shouldCancelBook() throws Exception {
		//@formatter:off
		String content = mvc.perform(
				post("/book/anHuman@earth/2019-01-02/2019-01-03"))
				.andExpect( status().isCreated() )
				.andReturn()
				.getResponse()
				.getContentAsString();
		Booking book = mapper.readValue(content, Booking.class);
		mvc.perform(
				delete( String.format("/book/%s",book.getId()) ))
		.andExpect( status().isAccepted() );
		mvc.perform(
				get("/availability/2019-01-01/2019-01-03"))
		.andDo(print())
		.andExpect( status().isOk() )
		.andExpect( jsonPath("$.length()",equalTo(3)))
		.andExpect( jsonPath("$[0].day",equalTo("2019-01-01")))
		.andExpect( jsonPath("$[1].day",equalTo("2019-01-02")))
		.andExpect( jsonPath("$[2].day",equalTo("2019-01-03")));
		//@formatter:on
	}

	@Before
	public void setUp() {
		bookRepo.deleteAll();
		Util.setClock( Clock.fixed( Instant.parse(TODAY) , ZoneId.of("Z")));
	}
	private static final String TODAY = "2019-01-01T10:00:00.00Z";

}
