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
	def visits = []
	
	@Unroll
	def "price for pet with age #age and #noOfVisits visits is #price"() {
		given:
		// TODO: create visits for pet
		pet.visits = visits

		// expect:
		// TODO: write and expection 
		
		// where:
		// TODO: provide test data in table format: age, noOfVisits and price
	}
}
