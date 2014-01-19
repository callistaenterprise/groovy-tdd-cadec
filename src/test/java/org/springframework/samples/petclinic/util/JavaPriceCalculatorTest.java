package org.springframework.samples.petclinic.util;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;

public class PriceCalculatorTest {

	private PriceCalculator uut;
	private DateTime birthDate;
	private Pet pet;
	private Visit visit1, visit2, visit3;

	@Before
	public void fixture() {
		uut = new PriceCalculator();
		birthDate = new DateTime(2005, 3, 31, 0, 0, 0, 0);
		pet = new Pet();
		pet.setBirthDate(birthDate);
		visit1 = new Visit();
		visit1.setPet(pet);
		visit1.setDate(birthDate.plusWeeks(5));
		visit2 = new Visit();
		visit2.setPet(pet);
		visit2.setDate(birthDate.plusWeeks(10));
		visit3 = new Visit();
		visit3.setPet(pet);
		visit3.setDate(birthDate.plusWeeks(15));
	}

	@Test
	public void testGetBasePriceForOneYearOldPet() {
		assertEquals("One year old", new BigDecimal("400.00"), uut.calculate(birthDate.plusYears(1), pet));
	}

	@Test
	public void testGetBasePriceForTwoYearOldPet() {
		assertEquals("Two years old", new BigDecimal("400.00"), uut.calculate(birthDate.plusYears(2), pet));
	}

	@Test
	public void testGetBasePriceForThreeYearOldPetSecondVisit() {
		pet.addVisit(visit1);
		pet.addVisit(visit2);
		assertEquals("Three years old", new BigDecimal("400.00"), uut.calculate(birthDate.plusYears(3), pet));
	}

	@Test
	public void testGetBasePriceForThreeYearOldPetThirdVisit() {
		pet.addVisit(visit1);
		pet.addVisit(visit2);
		pet.addVisit(visit3);
		assertEquals("Three years old", new BigDecimal("320.00"), uut.calculate(birthDate.plusYears(3), pet));
	}

	@Test
	public void testGetBasePriceForFourYearOldPet() {
		assertEquals("Four years old", new BigDecimal("480.00"), uut.calculate(birthDate.plusYears(4), pet));
	}

	@Test
	public void testGetBasePriceForFourYearOldPetThirdVisit() {
		pet.addVisit(visit1);
		pet.addVisit(visit2);
		pet.addVisit(visit3);
		assertEquals("Four years old", new BigDecimal("384.00"), uut.calculate(birthDate.plusYears(4), pet));
	}

	@Test
	public void testGetBasePriceForFiveYearOldPet() {
		assertEquals("Five years old", new BigDecimal("480.00"), uut.calculate(birthDate.plusYears(5), pet));
	}

}
