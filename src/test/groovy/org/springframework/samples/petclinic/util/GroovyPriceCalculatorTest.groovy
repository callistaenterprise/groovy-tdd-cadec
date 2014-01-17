package org.springframework.samples.petclinic.util

import static org.junit.Assert.assertEquals

import org.joda.time.DateTime
import org.junit.Before
import org.junit.Test
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.Visit

class GroovyPriceCalculatorTest {

	private PriceCalculator calculator= new PriceCalculator()
	private DateTime birthDate
	private Pet pet
	def visits = []

	@Before
	public void fixture() {
		birthDate = new DateTime(2005, 3, 31, 0, 0, 0, 0)
		pet = new Pet(birthDate: birthDate)
		3.times {
			visits << new Visit(pet:pet, date: birthDate.plusWeeks(5 * it))
		}
	}

	@Test
	public void testGetBasePriceForOneYearOldPet() {
		assert 400.00 == calculator.calculate(birthDate.plusYears(1), pet)
	}

	@Test
	public void testGetBasePriceForTwoYearOldPet() {
		assert 400.00 == calculator.calculate(birthDate.plusYears(2), pet)
	}

	@Test
	public void testGetBasePriceForThreeYearOldPetSecondVisit() {
		pet.visits = visits[0..1]
		assert 400.00 == calculator.calculate(birthDate.plusYears(2), pet)
	}

	@Test
	public void testGetBasePriceForThreeYearOldPetThirdVisit() {
		pet.visits = visits[0..2]
		assert 320.00 == calculator.calculate(birthDate.plusYears(2), pet)
	}

		@Test
	public void testGetBasePriceForThreeYearOldPet() {
		assert 400.00 == calculator.calculate(birthDate.plusYears(3), pet)
	}

	@Test
	public void testGetBasePriceForFourYearOldPet() {
		assert 480.00 == calculator.calculate(birthDate.plusYears(4), pet)
	}
	
	@Test
	public void testGetBasePriceForFourYearOldPetThirdVisit() {
		pet.visits = visits[0..2]
		assert 384.00 == calculator.calculate(birthDate.plusYears(4), pet)
	}

	@Test
	public void testGetBasePriceForFiveYearOldPet() {
		assert 480.00 == calculator.calculate(birthDate.plusYears(5), pet)
	}

}
