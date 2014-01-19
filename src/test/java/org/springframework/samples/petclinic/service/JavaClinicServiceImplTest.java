package org.springframework.samples.petclinic.service;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.samples.petclinic.util.PriceCalculator;
import org.springframework.samples.petclinic.util.ReflectionUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ClinicServiceImpl.class)
public class ClinicServiceImplTest {

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
		VisitRepository visitStub = Mockito.mock(VisitRepository.class);
		ConfirmationService confirmationMock = Mockito.mock(ConfirmationService.class);
		ClinicServiceImpl service = new ClinicServiceImpl(null, null, null, visitStub, confirmationMock);
		service.saveVisit(visit);
		Mockito.verify(confirmationMock).sendConfirmationMessage(visit);
	}

	@Test(expected=DataAccessException.class)
	public void testSaveVisitThrowsDataAccessException() {
		VisitRepository visitMock = Mockito.mock(VisitRepository.class);
		Mockito.doThrow(new UncategorizedSQLException("", "", new SQLException("Oops"))).when(visitMock).save(visit);
		ClinicServiceImpl service = new ClinicServiceImpl(null, null, null, visitMock, null);
		service.saveVisit(visit);
	}

	@Test
	public void testSaveVisitLogsConfirmationError() throws Exception {
		VisitRepository visitStub = Mockito.mock(VisitRepository.class);
		ConfirmationService confirmationStub = Mockito.mock(ConfirmationService.class);
		Throwable cause = new RuntimeException("Oops");
		Mockito.doThrow(cause).when(confirmationStub).sendConfirmationMessage(visit);
		Logger loggerMock = Mockito.mock(Logger.class);
		ClinicServiceImpl service = new ClinicServiceImpl(null, null, null, visitStub, confirmationStub);
		ReflectionUtils.setStaticAttribute(ClinicServiceImpl.class, "log", loggerMock);
		service.saveVisit(visit);
		Mockito.verify(loggerMock).error(Mockito.anyString(), Mockito.any(Throwable.class));
	}
	
	@Test
	public void testPriceCalculation() throws Exception {
		VisitRepository visitStub = Mockito.mock(VisitRepository.class);
		ConfirmationService confirmationStub = Mockito.mock(ConfirmationService.class);
		PriceCalculator calculatorMock = Mockito.mock(PriceCalculator.class);
		Mockito.when(calculatorMock.calculate(now, pet)).thenReturn(new BigDecimal(100.0));
		PowerMockito.whenNew(PriceCalculator.class).withNoArguments().thenReturn(calculatorMock);
		ClinicServiceImpl service = new ClinicServiceImpl(null, null, null, visitStub, confirmationStub);
		service.saveVisit(visit);
		Assert.assertEquals(new BigDecimal(100.0), visit.getPrice());
	}
}
