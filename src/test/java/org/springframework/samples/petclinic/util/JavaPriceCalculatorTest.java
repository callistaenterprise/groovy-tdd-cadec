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
	DateTime birthDate;
	Pet pet;
	List<Visit> visits;

	@Before
	public void fixture() {
		birthDate = new DateTime(2005, 3, 31, 0, 0, 0, 0);
		pet = new Pet();
		pet.setBirthDate(birthDate);
		Visit visit1 = new Visit();
		visit1.setPet(pet);
		visit1.setDate(birthDate.plusWeeks(5));
		Visit visit2 = new Visit();
		visit2.setPet(pet);
		visit2.setDate(birthDate.plusWeeks(10));
		visits = new ArrayList<Visit>();
		visits.add(visit1);
		visits.add(visit2);
		pet.setVisits(visits);
	}

	@Test
	public void testGetBasePriceForThreeYearOldPet() {
		assertEquals("Three years old", new BigDecimal("400.00"), calculator.calculate(birthDate.plusYears(3), pet));
	}

	@Test
	public void testGetBasePriceForThreeYearOldPetThirdVisit() {
		Visit visit3 = new Visit();
		visit3.setPet(pet);
		visit3.setDate(birthDate.plusWeeks(15));
		visits.add(visit3);
		pet.setVisits(visits);
		assertEquals("Three years old", new BigDecimal("320.00"), calculator.calculate(birthDate.plusYears(3), pet));
	}

	@Test
	public void testGetBasePriceForFourYearOldPet() {
		assertEquals("Four years old", new BigDecimal("480.00"), calculator.calculate(birthDate.plusYears(4), pet));
	}

	@Test
	public void testGetBasePriceForFourYearOldPetThirdVisit() {
		Visit visit3 = new Visit();
		visit3.setPet(pet);
		visit3.setDate(birthDate.plusWeeks(15));
		visits.add(visit3);
		pet.setVisits(visits);
		assertEquals("Four years old", new BigDecimal("384.00"), calculator.calculate(birthDate.plusYears(4), pet));
	}

}
