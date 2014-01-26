package org.springframework.samples.petclinic.service

import org.joda.time.DateTime
import org.slf4j.Logger
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.Visit
import org.springframework.samples.petclinic.repository.VisitRepository

import spock.lang.Specification


class ClinicServiceImplSpec extends Specification {

	def now = DateTime.now();
	def pet = new Pet(birthDate:now.minusYears(5));
	def visit = new Visit(date: now, pet: pet)

	def "save visit logs message on confirmation error"() {
		def visitStub = Mock(VisitRepository)
		def confirmationStub = Mock(ConfirmationService)
		def loggerMock = Mock(Logger)
		given:
			confirmationStub.sendConfirmationMessage(visit) >> { throw new RuntimeException("Oops") }
			ClinicServiceImpl service = new ClinicServiceImpl(null, null, null, visitStub, confirmationStub);
			ClinicServiceImpl.metaClass.setAttribute(service, "log", loggerMock)
		when:
			service.saveVisit(visit)
		then:
			1 * loggerMock.error("Failed to send confirmation message", _)
	}

}
