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
		def confirmationStub // TODO: Create a stub that throws a RuntimeException when sendConfirmationMessage() is called

		def logMessage
		def loggerMock // TODO: Create a mock Logger that records an error() invocation as logMessage

				// TODO: Create a mock 
		ClinicServiceImpl service = new ClinicServiceImpl(visitRepository: visitStub, confirmationService: confirmationStub)

		// TODO: Inject logger into service

		service.saveVisit(visit)

		// TODO: Verify that an expected message has been logged
	}

}
