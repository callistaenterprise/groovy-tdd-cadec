package org.springframework.samples.petclinic.service;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.samples.petclinic.util.ReflectionUtils;

public class JavaClinicServiceImplTest {

	Owner owner;
	Pet pet;
	Visit visit;
	DateTime now;
	
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
	public void testSaveVisitSendsConfirmation() {
		VisitRepository visitStub = mock(VisitRepository.class);
		ConfirmationService confirmationMock = mock(ConfirmationService.class);
		ClinicServiceImpl service = new ClinicServiceImpl();
		service.setVisitRepository(visitStub);
		service.setConfirmationService(confirmationMock);
		service.saveVisit(visit);
		verify(confirmationMock).sendConfirmationMessage(visit);
	}

	@Test(expected=DataAccessException.class)
	public void testSaveVisitThrowsDataAccessException() {
		VisitRepository visitStub = mock(VisitRepository.class);
		doThrow(new DataIntegrityViolationException("Oops")).when(visitStub).save(visit);
		ClinicServiceImpl service = new ClinicServiceImpl();
		service.setVisitRepository(visitStub);
		service.saveVisit(visit);
	}

	@Test
	public void testSaveVisitLogsConfirmationError() throws Exception {
		VisitRepository visitStub = mock(VisitRepository.class);
		ConfirmationService confirmationStub = mock(ConfirmationService.class);
		Throwable cause = new RuntimeException("Oops");
		doThrow(cause).when(confirmationStub).sendConfirmationMessage(visit);
		Logger loggerMock = mock(Logger.class);
		ClinicServiceImpl service = new ClinicServiceImpl();
		service.setVisitRepository(visitStub);
		service.setConfirmationService(confirmationStub);
		ReflectionUtils.setStaticAttribute(ClinicServiceImpl.class, "log", loggerMock);
		service.saveVisit(visit);
		verify(loggerMock).error(anyString(), any(Throwable.class));
	}

}
