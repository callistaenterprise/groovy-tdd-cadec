package org.springframework.samples.petclinic.util

import org.joda.time.DateTime
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.Visit

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class PriceCalculatorSpec extends Specification {

	@Shared def calculator = new PriceCalculator()
	@Shared def birthDate = new DateTime(2005, 3, 31, 0, 0)
	def pet = new Pet(birthDate: birthDate)
	def visits = []
	
	@Unroll
	def "price for pet with age #age and #noOfVisits visits is #price"() {
		given:
		noOfVisits.times {
			visits << new Visit(pet)
		}
		pet.visits = visits

		expect:
		calculator.calculate(birthDate.plusYears(age), pet) == price  
		
		where:
		age | noOfVisits | price
		3   | 5          | 400.00
		3   | 6          | 320.00
		3   | 10         | 320.00
		4   | 5          | 480.00
		4   | 6          | 384.00
		10  | 5          | 480.00
		10  | 6          | 384.00
	}
}
