package org.springframework.samples.petclinic.util

import static org.junit.Assert.assertEquals

import org.joda.time.DateTime
import org.junit.Before
import org.junit.Test
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.Visit

class PriceCalculatorTest {

	PriceCalculator calculator = new PriceCalculator()
	DateTime birthDate
	Pet pet
	List<Visit> visits

	@Before
	void fixture() {
		birthDate = new DateTime(2005, 3, 31, 0, 0, 0, 0)
		pet = new Pet(birthDate: birthDate)
		visits = []
		visits << new Visit(pet:pet, date: birthDate.plusWeeks(5))
		visits << new Visit(pet:pet, date: birthDate.plusWeeks(10))
		pet.visits = visits
	}

	@Test
	void testGetBasePriceForThreeYearOldPet() {
		assertEquals(400.00, calculator.calculate(birthDate.plusYears(3), pet))
	}

	@Test
	void testGetBasePriceForThreeYearOldPetThirdVisit() {
		visits << new Visit(pet:pet, date: birthDate.plusWeeks(15))
		pet.visits = visits
		assertEquals(320.00, calculator.calculate(birthDate.plusYears(3), pet))
	}

	@Test
	void testGetBasePriceForFourYearOldPet() {
		assertEquals(480.00, calculator.calculate(birthDate.plusYears(4), pet))
	}
	
	@Test
	void testGetBasePriceForFourYearOldPetThirdVisit() {
		visits << new Visit(pet:pet, date: birthDate.plusWeeks(15))
		pet.visits = visits
		assertEquals(384.00, calculator.calculate(birthDate.plusYears(4), pet))
	}

}
