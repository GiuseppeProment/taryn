package upgrade.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;

import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;
import com.anarsoft.vmlens.concurrent.junit.ThreadCount;

import upgrade.util.Util;

@RunWith(ConcurrentTestRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BookingConcurrencyIT {

	@Autowired	 private MockMvc mvc;
	
	private List<Integer> statuses = new ArrayList<>(); 

	@Test
	@ThreadCount(5)
	public void doRaceContition() throws Exception {
		statuses.add( 	
				mvc.perform(post("/book/human@earth/2019-01-02/2019-01-03"))
					.andReturn().getResponse().getStatus() );
	}

	@After
	public void shouldHaveOneCreatedAndOthersBadRequest() {
		assertEquals(1, statuses.stream().filter(x -> x.equals(HttpStatus.CREATED.value())).count());
		assertEquals(4, statuses.stream().filter(x -> x.equals(HttpStatus.BAD_REQUEST.value())).count());
	}
	
	@Before
	public void setUp() {
		Util.setClock(Clock.fixed(Instant.parse(TODAY), ZoneId.of("Z")));
	}
	private static final String TODAY = "2019-01-01T10:00:00.00Z";
	@ClassRule 	public static final SpringClassRule springClassRule = new SpringClassRule();
	@Rule 	public final SpringMethodRule springMethodRule = new SpringMethodRule();
	
}
