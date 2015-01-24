package org.springframework.samples.petclinic.util

import java.util.List;

import org.joda.time.DateTime
import org.junit.Before
import org.junit.Test
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.Visit
import org.springframework.samples.petclinic.util.PriceCalculator

class PriceCalculatorTest {

	def calculator = new PriceCalculator()
	def birthDate, afterThreeYears, afterFourYears
	def visits
	def pet

	@Before
	void fixture() {
		birthDate = new DateTime(2005, 3, 31, 0, 0)
		afterThreeYears = birthDate.plusYears(3)
		afterFourYears = birthDate.plusYears(4)
		pet = new Pet(birthDate: birthDate)
		visits = []
		5.times {
			visits << new Visit(pet: pet)
		}
		pet.visits = visits
	}

	@Test
	void testGetBasePriceForThreeYearOldPet() {
		assert calculator.calculate(afterThreeYears, pet) == 400.00
	}

	@Test
	void testGetBasePriceForThreeYearOldPetSixthVisit() {
		visits << new Visit(pet: pet)
		pet.visits = visits
		assert calculator.calculate(afterThreeYears, pet) == 320.00
	}

	@Test
	void testGetBasePriceForFourYearOldPet() {
		assert calculator.calculate(afterFourYears, pet) == 480.00
	}
	
	@Test
	void testGetBasePriceForFourYearOldPetSixthVisit() {
		visits << new Visit(pet: pet)
		pet.visits = visits
		assert calculator.calculate(afterFourYears, pet) == 384.00
	}

}
