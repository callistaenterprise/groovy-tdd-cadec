package org.springframework.samples.petclinic.util

import org.joda.time.DateTime
import org.junit.Before
import org.junit.Test
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.Visit

class PriceCalculatorTest {

	def calculator = new PriceCalculator()
	def birthDate
	def pet
	def visits

	@Before
	void fixture() {
		birthDate = new DateTime(2005, 3, 31, 0, 0, 0, 0)
		pet = new Pet(birthDate: birthDate)
		visits = []
		10.times {
			visits << new Visit(pet:pet, date: birthDate.plusWeeks(it * 5))
		}
		pet.visits = visits[0..1]
	}

	@Test
	void testGetBasePriceForThreeYearOldPet() {
		assert calculator.calculate(birthDate.plusYears(3), pet) == 400.00
	}

	@Test
	void testGetBasePriceForThreeYearOldPetThirdVisit() {
		pet.visits = visits[0..2]
		assert calculator.calculate(birthDate.plusYears(3), pet) == 320.00
	}

	@Test
	void testGetBasePriceForFourYearOldPet() {
		assert calculator.calculate(birthDate.plusYears(4), pet) == 480.00
	}
	
	@Test
	void testGetBasePriceForFourYearOldPetThirdVisit() {
		pet.visits = visits[0..2]
		assert calculator.calculate(birthDate.plusYears(4), pet) == 384.00
	}

	@Test
	void testGetBasePriceForFourYearOldPet10Visit() {
		pet.visits = visits
		assert calculator.calculate(birthDate.plusYears(4), pet) == 384.00
	}

}
