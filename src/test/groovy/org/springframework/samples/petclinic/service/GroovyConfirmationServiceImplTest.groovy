package org.springframework.samples.petclinic.service

import org.joda.time.DateTime
import org.junit.Before
import org.junit.Test
import org.springframework.samples.petclinic.model.Owner
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.Visit

class GroovyConfirmationServiceImplTest {

	private ConfirmationServiceImpl service
	private Owner owner
	private Pet pet
	private Visit visit

	@Before
	void fixture() {
		def builder = new ObjectGraphBuilder()
		builder.classNameResolver = "org.springframework.samples.petclinic.model"
		owner = builder.owner(firstName: "firstName", lastName: "lastName", address: "address", city: "city", email: "name@gmail.com")
		pet = builder.pet(name: "a pet", owner: owner) { petType(name: "type") }
		visit = builder.visit( date: DateTime.now(), description: "visit description", pet: pet)
		service = new ConfirmationServiceImpl()
	}

	@Test
	void testSerialization() {
		verifyMessage(service.serialize(visit))
	}

	@Test
	void testConfirmationMessage() {
		def destination, message
		service.messageSender = {d, m -> destination = d; message = m; true} as MessageSender
		service.sendConfirmationMessage(visit)
		assert destination == "name@gmail.com"
		verifyMessage(message)
	}
	
	def verifyMessage(def message) {
		def visit = new XmlSlurper().parseText(message)
		assert visit.description == "visit description"
		assert visit.pet.name == "a pet"
		assert visit.pet.owner == "firstName lastName"

	}
}
