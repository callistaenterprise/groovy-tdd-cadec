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
		// TODO: set up visits, based on test data
		pet.visits = visits

		expect:
		// TODO: provide expectation based on test data
		
		where:
		age | noOfVisits | price
		// TODO: provide test data
	}
}
