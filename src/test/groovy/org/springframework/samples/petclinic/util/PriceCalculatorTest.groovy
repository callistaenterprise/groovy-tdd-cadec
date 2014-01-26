package org.springframework.samples.petclinic.util

import org.joda.time.DateTime
import org.junit.Before
import org.junit.Test
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.Visit

class PriceCalculatorTest {

	PriceCalculator calculator = new PriceCalculator()
	DateTime birthDate

	@Before
	void fixture() {
		birthDate = new DateTime(2005, 3, 31, 0, 0)
	}

	@Test
	void testGetBasePriceForThreeYearOldPet() {
	}

	@Test
	void testGetBasePriceForThreeYearOldPetThirdVisit() {
	}

	@Test
	void testGetBasePriceForFourYearOldPet() {
	}
	
	@Test
	void testGetBasePriceForFourYearOldPetThirdVisit() {
	}

}
