package org.springframework.samples.petclinic.service

import org.joda.time.DateTime
import org.slf4j.Logger
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.Visit
import org.springframework.samples.petclinic.repository.VisitRepository

import spock.lang.Specification


class ClinicServiceImplSpec extends Specification {

	Visit visit;
	Pet pet;
	DateTime now;
	
	def setup() {
		visit = new Visit();
		pet = new Pet();
		now = DateTime.now();
		pet.setBirthDate(now.minusYears(5));
		visit.setPet(pet);
		visit.setDate(DateTime.now());
	}

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
