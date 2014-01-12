package org.springframework.samples.petclinic.service

import org.gmock.GMockController
import org.joda.time.DateTime
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.Visit
import org.springframework.samples.petclinic.repository.VisitRepository
import org.springframework.samples.petclinic.util.PriceCalculator


class GroovyClinicServiceImplTest {

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
	public void stubInteractionsUsingExpandos() {
		def visitMock = new Expando()
		visitMock.save = {v -> assert v == visit}
		def confirmationMock = new Expando()
		confirmationMock.sendConfirmationMessage = {v -> assert v == visit}
		ClinicServiceImpl service = new ClinicServiceImpl(null, null, null, visitMock as VisitRepository, confirmationMock as ConfirmationService);
		service.saveVisit(visit);
	}

	@Test
	public void stubInteractionsUsingMaps() {
		def visitMock = [save:{v -> assert v == visit}] as VisitRepository
		def confirmationMock = [sendConfirmationMessage:{v -> assert v == visit}] as ConfirmationService
		ClinicServiceImpl service = new ClinicServiceImpl(null, null, null, visitMock, confirmationMock);
		service.saveVisit(visit);
	}

	@Test
	public void stubInteractionsUsingClosures() {
		def visitMock = {v -> assert v == visit} as VisitRepository
		def confirmationMock = {v -> assert v == visit} as ConfirmationService
		ClinicServiceImpl service = new ClinicServiceImpl(null, null, null, visitMock, confirmationMock);
		service.saveVisit(visit);
	}

	@Test
	public void stubInteractionsUsingGMock() {
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
	public void stubPriceCalculationUsingMockito() throws Exception {
		def visitMock = Mockito.mock(VisitRepository);
		def confirmationMock = Mockito.mock(ConfirmationService)
		PriceCalculator calculatorMock = Mockito.mock(PriceCalculator)
		Mockito.when(calculatorMock.calculate(Mockito.any(DateTime), Mockito.any(Pet))).thenReturn(100.0)
		ClinicServiceImpl service = new ClinicServiceImpl(null, null, null, visitMock, confirmationMock);
		service.calculator = calculatorMock
		service.saveVisit(visit);
		assert 100.0 == visit.getPrice()
	}


}
