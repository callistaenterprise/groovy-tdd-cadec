package org.springframework.samples.petclinic.service

import org.joda.time.DateTime
import org.junit.Before
import org.junit.Test
import org.slf4j.Logger
import org.springframework.dao.DataAccessException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.Visit
import org.springframework.samples.petclinic.repository.VisitRepository


class ClinicServiceImplTest {

	final shouldFail = new GroovyTestCase().&shouldFail

	Visit visit
	Pet pet
	DateTime now
	
	@Before
	public void fixture() {
		now = DateTime.now()
		pet = new Pet(birthDate: now.minusYears(5))
		visit = new Visit(pet: pet, date: now)
	}

	@Test
	public void testSaveVisitSendsConfirmation() {
		def visitStub = {} as VisitRepository
		def confirmedVisit
		def confirmationMock = {v -> confirmedVisit = v} as ConfirmationService
		ClinicServiceImpl service = new ClinicServiceImpl(visitRepository: visitStub,
														  confirmationService: confirmationMock)

		service.saveVisit(visit)

		assert confirmedVisit == visit
	}

	@Test
	public void testSaveVisitThrowsDataAccessException() {
		def visitStub = {throw new DataIntegrityViolationException("Oops")} as VisitRepository
		ClinicServiceImpl service = new ClinicServiceImpl(visitRepository: visitStub)
		shouldFail(DataAccessException) { service.saveVisit(visit) }
	}

	@Test
	public void testSaveVisitLogsConfirmationError() throws Exception {
		def visitStub = {} as VisitRepository
		def confirmationStub = {throw new RuntimeException("Oops")} as ConfirmationService

		def logMessage
		def loggerMock = [error: {message -> logMessage = message}] as Logger

		ClinicServiceImpl service = new ClinicServiceImpl(visitRepository: visitStub, confirmationService: confirmationStub)

		ClinicServiceImpl.metaClass.setAttribute(service, "log", loggerMock)

		service.saveVisit(visit)

		assert logMessage.contains("Oops")
	}

}
