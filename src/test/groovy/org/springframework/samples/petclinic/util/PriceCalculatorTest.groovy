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

	@Before
	void fixture() {
		birthDate = new DateTime(2005, 3, 31, 0, 0)
		afterThreeYears = birthDate.plusYears(3)
		afterFourYears = birthDate.plusYears(4)
		// TODO: Set up required test data: a pet and a list of visits 
	}

	@Test
	void testGetBasePriceForThreeYearOldPet() {
		// TODO
	}

	@Test
	void testGetBasePriceForThreeYearOldPetSixthVisit() {
		// TODO
	}

	@Test
	void testGetBasePriceForFourYearOldPet() {
		// TODO
	}
	
	@Test
	void testGetBasePriceForFourYearOldPetSixthVisit() {
		// TODO
	}

}
