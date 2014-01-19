package org.springframework.samples.petclinic.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.util.XMLMatcher;
import org.springframework.test.util.ReflectionTestUtils;
import org.xml.sax.SAXException;


public class JavaConfirmationServiceImplTest {

	private ConfirmationServiceImpl service;
	private Owner owner;
	private Pet pet;
	private PetType petType;
	private Visit visit;
	private DateTime now;
	private String message;
	private MessageSender messageSender;

	@Before
	public void fixture() {
		owner = new Owner();
		owner.setFirstName("firstName");
		owner.setLastName("lastName");
		owner.setAddress("address");
		owner.setCity("city");
		owner.setEmail("name@gmail.com");
		pet = new Pet();
		pet.setName("a pet");
		petType = new PetType();
		petType.setName("type");
		pet.setType(petType);
		owner.addPet(pet);
		visit = new Visit();
		now = DateTime.now();
		visit.setDate(now);
		visit.setDescription("visit description");
		visit.setPet(pet);
		List<Visit> visits = new ArrayList<Visit>();
		visits.add(visit);
		pet.setVisits(visits);
		service = new ConfirmationServiceImpl();
	}

	@Test
	public void testConfirmationMessage() {
		message =
				"<visit><date>"+now+"</date><description>visit description</description>" +
				"<pet petType=\"type\"><owner>firstName lastName</owner><name>a pet</name></pet></visit>";
		messageSender = mock(MessageSender.class);
		when(messageSender.sendMessage(any(String.class), any(String.class))).thenReturn(true);
		ReflectionTestUtils.setField(service, "messageSender", messageSender);
		service.sendConfirmationMessage(visit);
		verify(messageSender).sendMessage(eq(owner.getEmail()), argThat(XMLMatcher.isSimilarTo(message)));
	}
}
