package org.springframework.samples.petclinic.util

import org.joda.time.DateTime
import org.springframework.samples.petclinic.model.Pet

import spock.lang.Specification
import spock.lang.Unroll

class PriceCalculatorSpec extends Specification {

	def calculator = new PriceCalculator()
	def birthDate = new DateTime(2005, 3, 31, 0, 0, 0, 0)
	def pet = new Pet(birthDate: birthDate)

	@Unroll
	def "price for pet with age #age is #price"() {
		expect:
		calculator.calculate(birthDate.plusYears(age), pet) == price
		
		where:
		age | price
		1   | 400.00
		2   | 400.00
		3   | 400.00
		4   | 480.00
		5   | 480.00
	}
}
