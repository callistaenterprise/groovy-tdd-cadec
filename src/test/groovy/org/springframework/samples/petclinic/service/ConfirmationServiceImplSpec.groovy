package org.springframework.samples.petclinic.service

import org.joda.time.DateTime
import org.springframework.samples.petclinic.model.Owner
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.Visit

import spock.lang.Shared
import spock.lang.Specification

class ConfirmationServiceImplSpec extends Specification {

	@Shared ConfirmationServiceImpl service = new ConfirmationServiceImpl()

	@Shared def builder = new ObjectGraphBuilder()
	def setupSpec() {
		builder.classNameResolver = "org.springframework.samples.petclinic.model"
	}

	Visit visit =
	builder.visit(date: DateTime.now(),
		          description: "visit description",
				  { pet(name: "a pet",
						{ petType(name: "type") },
						{ owner(firstName: "firstName", lastName: "lastName", address: "address",
							    city: "city", email: "name@gmail.com") } )
				  })
	
	
	void "a confirmation message is sent to the correct destination, with an xml representation of the visit"() {
		given:
		def message
		// TODO: create a mock MessageSender and assign it to service.messageSender
		when:
		service.sendConfirmationMessage(visit)
		then:
		// TODO:
		// Verify that sendMessage() is called once with correct parameters.
		// The first parameter destination should be the pet's owners email, eg. visit.pet.owner.email
		// The second parameter should be a string containing an xml message. Parse this string using
		// XmlSlurper, and store it as message
		assert message.description == visit.description
	}
}
