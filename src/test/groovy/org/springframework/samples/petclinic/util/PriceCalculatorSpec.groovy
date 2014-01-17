package org.springframework.samples.petclinic.util

import org.joda.time.DateTime
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.Visit

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class PriceCalculatorSpec extends Specification {

	@Shared def calculator = new PriceCalculator()
	@Shared def birthDate = new DateTime(2005, 3, 31, 0, 0, 0, 0)
	def pet = new Pet(birthDate: birthDate)
	
	def setup() {}

	@Unroll
	def "price for pet with age #age is #price"() {
		given:
		visits.times {
			pet.addVisit(new Visit(pet:pet, date: birthDate.plusWeeks(5 * it)))
		}

		expect:
		calculator.calculate(birthDate.plusYears(age), pet) == price
		
		where:
		age | visits | price
		1   | 1      | 400.00
		2   | 1      | 400.00
		3   | 2      | 400.00
		3   | 3      | 320.00
		4   | 2      | 480.00
		4   | 3      | 384.00
		5   | 2      | 480.00
	}
}
