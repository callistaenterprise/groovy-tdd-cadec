package org.springframework.samples.petclinic.service

import org.joda.time.DateTime
import org.junit.Before
import org.junit.Test
import org.springframework.samples.petclinic.model.Owner
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.Visit

class ConfirmationServiceImplTest {

	private ConfirmationServiceImpl service = new ConfirmationServiceImpl()
	private Owner owner
	private Pet pet
	private Visit visit

	@Before
	void fixture() {
		def builder = new ObjectGraphBuilder()
		builder.classNameResolver = "org.springframework.samples.petclinic.model"
		visit = builder.visit(date: DateTime.now(),
		                      description: "visit description",
							  { pet(name: "a pet",
									{ petType(name: "type") },
									{ owner(firstName: "firstName", lastName: "lastName", address: "address",
										    city: "city", email: "name@gmail.com") } )
							  })
	}

	@Test
	void testConfirmationMessage() {
		def destination, message
		service.messageSender = {d, m -> destination = d; message = m; return true} as MessageSender
		
		service.sendConfirmationMessage(visit)

		assert destination == visit.pet.owner.email
		def visitMessage = new XmlSlurper().parseText(message)
		assert visitMessage.description == visit.description
		assert visitMessage.pet.name == visit.pet.name
		assert visitMessage.pet.owner == "firstName lastName"

	}
}
