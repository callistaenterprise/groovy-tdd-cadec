package org.springframework.samples.petclinic.service;

import java.io.IOException;

import org.hamcrest.MatcherAssert;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.util.XMLMatcher;
import org.springframework.test.util.ReflectionTestUtils;
import org.xml.sax.SAXException;


public class ConfirmationServiceImplTest {

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
		pet.addVisit(visit);
		service = new ConfirmationServiceImpl();
		message =
				"<visit><date>"+now+"</date><description>visit description</description>" +
				"<pet type=\"type\"><owner>firstName lastName</owner><name>a pet</name></pet></visit>";
	}

	@Test
	public void testSerialization() throws SAXException, IOException {
		MatcherAssert.assertThat(service.serialize(visit), XMLMatcher.isSimilarTo(message));
	}

	@Test
	public void testConfirmationMessage() {
		messageSender = Mockito.mock(MessageSender.class);
		Mockito.when(messageSender.sendMessage(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(true);
		ReflectionTestUtils.setField(service, "messageSender", messageSender);
		service.sendConfirmationMessage(visit);
		Mockito.verify(messageSender).sendMessage(Matchers.eq("name@gmail.com"), Matchers.argThat(XMLMatcher.isSimilarTo(message)));
	}
}
