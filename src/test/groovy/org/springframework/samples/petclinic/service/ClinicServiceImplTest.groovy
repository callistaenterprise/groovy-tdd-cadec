package org.springframework.samples.petclinic.service

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.SQLException;

import org.gmock.GMockController
import org.joda.time.DateTime
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.Visit
import org.springframework.samples.petclinic.repository.VisitRepository
import org.springframework.samples.petclinic.util.PriceCalculator
import org.springframework.samples.petclinic.util.ReflectionUtils;


class GroovyClinicServiceImplTest {

	final shouldFail = new GroovyTestCase().&shouldFail

	Visit visit;
	Pet pet;
	DateTime now;
	GMockController gMock = new GMockController();
	
	@Before
	public void fixture() {
		visit = new Visit();
		pet = new Pet();
		now = DateTime.now();
		pet.setBirthDate(now.minusYears(5));
		visit.setPet(pet);
		visit.setDate(DateTime.now());
	}

	@Test
	public void testSaveVisitSendsConfirmationUsingExpandos() {
		def visitStub = new Expando()
		visitStub.save = {}
		def confirmationMock = new Expando()
		confirmationMock.sendConfirmationMessage = {v -> assert v == visit}
		ClinicServiceImpl service = new ClinicServiceImpl(null, null, null, visitStub as VisitRepository, confirmationMock as ConfirmationService);
		service.saveVisit(visit);
	}

	@Test
	public void testSaveVisitSendsConfirmationUsingMaps() {
		def visitStub = [save:{}] as VisitRepository
		def confirmationMock = [sendConfirmationMessage:{v -> assert v == visit}] as ConfirmationService
		ClinicServiceImpl service = new ClinicServiceImpl(null, null, null, visitStub, confirmationMock);
		service.saveVisit(visit);
	}

	@Test
	public void testSaveVisitSendsConfirmationUsingClosures() {
		def visitStub = {} as VisitRepository
		def confirmationMock = {v -> assert v == visit} as ConfirmationService
		ClinicServiceImpl service = new ClinicServiceImpl(null, null, null, visitStub, confirmationMock);
		service.saveVisit(visit);
	}

	@Test
	public void testSaveVisitSendsConfirmationUsingGMock() {
		VisitRepository visitMock = gMock.mock(VisitRepository)
		visitMock.save(visit)
		ConfirmationService confirmationMock = gMock.mock(ConfirmationService)
		confirmationMock.sendConfirmationMessage(visit)
		gMock.play {
			ClinicServiceImpl service = new ClinicServiceImpl(null, null, null, visitMock, confirmationMock);
			service.saveVisit(visit);
		}
	}
	
	@Test
	public void testSaveVisitThrowsDataAccessException() {
		def visitStub = {throw new  UncategorizedSQLException("", "", new SQLException("Oops"))} as VisitRepository
		ClinicServiceImpl service = new ClinicServiceImpl(null, null, null, visitStub, null)
		shouldFail(DataAccessException) { service.saveVisit(visit) }
	}

	@Test
	public void testSaveVisitLogsConfirmationError() throws Exception {
		def visitStub = {} as VisitRepository
		def confirmationStub = {throw new RuntimeException("Oops")} as ConfirmationService
		def logMessage
		Logger loggerMock = [error:{message, cause -> logMessage = message}] as Logger
		ClinicServiceImpl service = new ClinicServiceImpl(null, null, null, visitStub, confirmationStub);
		ClinicServiceImpl.metaClass.setAttribute(service, "log", loggerMock)
		service.saveVisit(visit);
		assert logMessage == "Failed to send confirmation message"
	}
	
	@Test
	public void stubPriceCalculationUsingMockito() throws Exception {
		def visitStub = Mockito.mock(VisitRepository)
		def confirmationStub = Mockito.mock(ConfirmationService)
		PriceCalculator calculatorMock = Mockito.mock(PriceCalculator)
		Mockito.when(calculatorMock.calculate(Mockito.any(DateTime), Mockito.any(Pet))).thenReturn(100.0)
		ClinicServiceImpl service = new ClinicServiceImpl(null, null, null, visitStub, confirmationStub)
		service.calculator = calculatorMock
		service.saveVisit(visit)
		assert 100.0 == visit.price
	}


}
