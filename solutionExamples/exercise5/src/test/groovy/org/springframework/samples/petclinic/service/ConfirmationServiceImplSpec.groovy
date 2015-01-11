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
		service.messageSender = Mock(MessageSender)
		def message
		when:
		service.sendConfirmationMessage(visit)
		then:
		1 * service.messageSender.sendMessage(
			visit.pet.owner.email,
			{m -> message = new XmlSlurper().parseText(m)}
		)
		message.description == visit.description
		message.pet.name == visit.pet.name
		message.pet.owner == "firstName lastName"
	}
}
