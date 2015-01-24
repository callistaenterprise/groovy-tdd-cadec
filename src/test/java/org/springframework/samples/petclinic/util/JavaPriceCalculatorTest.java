package org.springframework.samples.petclinic.util;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;

public class JavaPriceCalculatorTest {

	PriceCalculator calculator = new PriceCalculator();
	DateTime birthDate, afterThreeYears, afterFourYears;
	Pet pet;
	List<Visit> visits;

	@Before
	public void fixture() {
		birthDate = new DateTime(2005, 3, 31, 0, 0);
		afterThreeYears = birthDate.plusYears(3);
		afterFourYears = birthDate.plusYears(4);
		pet = new Pet();
		pet.setBirthDate(birthDate);
		Visit visit1 = new Visit();
		visit1.setPet(pet);
		Visit visit2 = new Visit();
		visit2.setPet(pet);
		Visit visit3 = new Visit();
		visit3.setPet(pet);
		Visit visit4 = new Visit();
		visit4.setPet(pet);
		Visit visit5 = new Visit();
		visit5.setPet(pet);
		visits = new ArrayList<Visit>();
		visits.add(visit1);
		visits.add(visit2);
		visits.add(visit3);
		visits.add(visit4);
		visits.add(visit5);
		pet.setVisits(visits);
	}

	@Test
	public void testGetBasePriceForThreeYearOldPet() {
		assertEquals(new BigDecimal("400.00"), calculator.calculate(afterThreeYears, pet));
	}

	@Test
	public void testGetBasePriceForThreeYearOldPetSixthVisit() {
		Visit visit6 = new Visit();
		visit6.setPet(pet);
		visits.add(visit6);
		pet.setVisits(visits);
		assertEquals(new BigDecimal("320.00"), calculator.calculate(afterThreeYears, pet));
	}

	@Test
	public void testGetBasePriceForFourYearOldPet() {
		assertEquals(new BigDecimal("480.00"), calculator.calculate(afterFourYears, pet));
	}

	@Test
	public void testGetBasePriceForFourYearOldPetSixthVisit() {
		Visit visit6 = new Visit();
		visit6.setPet(pet);
		visits.add(visit6);
		pet.setVisits(visits);
		assertEquals(new BigDecimal("384.00"), calculator.calculate(afterFourYears, pet));
	}

}
