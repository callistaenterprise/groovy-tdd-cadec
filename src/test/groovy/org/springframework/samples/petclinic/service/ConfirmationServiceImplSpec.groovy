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
		// TODO: create a mock MessageSender and inject into service
		def message
		when:
		// TODO: trigger the service to send a confirmation message
		true
		then:
		1 * service.messageSender.sendMessage(_,_) >> {d, m ->
			// TODO: register an expectation on the mock messageSender to
			// - verify the destination
			// - retrieve the xml string, and store it to the message variable
			//   as a navigationable object using XmlSlurper
		}
		// verify the content of the message, e.g. description, pet's name and owner
	}
}
