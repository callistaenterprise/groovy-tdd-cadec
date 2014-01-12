package org.springframework.samples.petclinic.util;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.samples.petclinic.model.Pet;

public class PriceCalculatorTest {

	private PriceCalculator uut;
	private DateTime birthDate;
	private Pet pet;

	@Before
	public void fixture() {
		uut = new PriceCalculator();
		birthDate = new DateTime(2005, 3, 31, 0, 0, 0, 0);
		pet = new Pet();
		pet.setBirthDate(birthDate);
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
	public void testGetBasePriceForThreeYearOldPet() {
		assertEquals("Three years old", new BigDecimal("400.00"), uut.calculate(birthDate.plusYears(3), pet));
	}

	@Test
	public void testGetBasePriceForFourYearOldPet() {
		assertEquals("Four years old", new BigDecimal("480.00"), uut.calculate(birthDate.plusYears(4), pet));
	}

	@Test
	public void testGetBasePriceForFiveYearOldPet() {
		assertEquals("Five years old", new BigDecimal("480.00"), uut.calculate(birthDate.plusYears(5), pet));
	}

}
